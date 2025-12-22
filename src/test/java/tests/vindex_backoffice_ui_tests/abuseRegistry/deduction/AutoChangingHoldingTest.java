package tests.vindex_backoffice_ui_tests.abuseRegistry.deduction;

import static business_objects.db.abuse_registry_db.AbuserDeductionFactory.generateAbuserDeductionByAccount;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoercedAccountProfitComment;
import static business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObjectFactory.generateMtMt5PositionsObject;
import static business_objects.ui.user.UserFactory.autotestUserOne;
import static helpers.api.AbuseRegistryHelper.addFraudForClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.Currency.EUR;
import static helpers.data.enums.Currency.USD;
import static helpers.data.enums.FraudSubtype.INTERNAL;
import static helpers.data.enums.FraudType.HEDGING;
import static helpers.data.enums.FraudTypeStatus.CONFIRMED;
import static helpers.data.enums.deduction.DeductionStatusApproval.APPROVED;
import static helpers.data.enums.deduction.DeductionStatusApproval.AWAITING_APPROVAL;
import static helpers.data.enums.deduction.DeductionStatusDeduction.*;
import static helpers.data.enums.deduction.DeductionStatusEmail.NOT_SENT;
import static helpers.data.enums.deduction.DeductionStatusEmail.SENT;
import static helpers.data.enums.deduction.DeductionStatusOpenPositions.*;
import static helpers.data.enums.deduction.DeductionType.FULL_DEDUCTION;
import static helpers.data.enums.deduction.DeductionTypeAccount.ILLEGAL_PROFIT;
import static helpers.data.enums.deduction.DeductionTypeAccount.NO_ILLEGAL_PROFIT;
import static helpers.database.ArHelper.deleteDeductions;
import static helpers.database.ArHelper.deleteUserFromAbuseRegistry;
import static helpers.database.DbHelper.*;
import static helpers.database.DbHelper.insertObjectsToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.*;

import business_objects.db.abuse_registry_db.AbuserDeduction;
import business_objects.db.abuse_registry_db.AbuserHistory;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObject;
import business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObject;
import business_objects.kafka.deductions.AccountDeductionRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.database.DbName;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

@Feature("BMS-1549 Auto changing holding")
@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
@Tag(ABUSE_REGISTRY)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AutoChangingHoldingTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    private static final CrmTbAccountObject account1 = generateCrmTbAccountDataForUi(client);
    private static final CrmTbAccountObject account2 = generateCrmTbAccountDataForUi(client);
    private static AbuserDeduction holdingDeduction1;
    private static MtAccountObject mtAccount1;
    private static MtAccountObject mtAccount2;
    private static MtAccountObject mtAccount3;
    private static MtAccountObject mtAccount4;
    private static MtAccountObject mtAccount5;
    private static MtMt4TradesCoercedObject trade1;
    private static MtMt4TradesCoercedObject trade2;
    private static MtMt4TradesCoercedObject trade3;
    private static MtMt4TradesCoercedObject trade4;
    private static MtMt4TradesCoercedObject trade5;
    private static MtMt4TradesCoercedObject tradeWithdrawal;
    private static List<AbuserHistory> abuserHistory;

    @BeforeAll
    static void setup() throws Exception {
        account1.currency = USD.getCode();
        account2.account = getRandomIntPositive();
        account2.currency = USD.getCode();
        CrmTbAccountObject account3 = generateCrmTbAccountDataForUi(client);
        account3.account = getRandomIntPositive();
        account3.currency = EUR.getCode();
        CrmTbAccountObject account4 = generateCrmTbAccountDataForUi(client);
        account4.account = getRandomIntPositive();
        account4.currency = EUR.getCode();
        CrmTbAccountObject account5 = generateCrmTbAccountDataForUi(client);
        account5.account = getRandomIntPositive();
        account5.currency = USD.getCode();
        mtAccount1 = generateMtAccountByCrmTbAccount(account1);
        mtAccount2 = generateMtAccountByCrmTbAccount(account2);
        mtAccount3 = generateMtAccountByCrmTbAccount(account3);
        mtAccount4 = generateMtAccountByCrmTbAccount(account4);
        mtAccount5 = generateMtAccountByCrmTbAccount(account5);

        String comment = "comment";
        trade1 = generateMt4TradesCoercedAccountProfitComment(account1, 500.12 + 10_000d, comment);
        trade2 = generateMt4TradesCoercedAccountProfitComment(account2, 1000.23, comment);
        trade3 = generateMt4TradesCoercedAccountProfitComment(account3, 1800.45, comment);
        trade4 = generateMt4TradesCoercedAccountProfitComment(account4, 2800.67, comment);
        trade5 = generateMt4TradesCoercedAccountProfitComment(account5, 1500.89, comment);
        tradeWithdrawal = generateMt4TradesCoercedAccountProfitComment(account1, -10_000d, "withdraw");

        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertCrmAccountsToDb(account1, account2, account3, account4, account5);
        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccount1, mtAccount2, mtAccount3, mtAccount4, mtAccount5));
        insertObjectsToDb(
                MT4_TRADES_COERCED_TABLE_NAME, List.of(trade1, trade2, trade3, trade4, trade5, tradeWithdrawal));
        MtMt5PositionsObject position1 = generateMtMt5PositionsObject(client);
        MtMt5PositionsObject position2 = generateMtMt5PositionsObject(client);
        position2.setAccount(mtAccount3.account);
        position2.setServerId(mtAccount3.sourceIdSt);
        insertObjectsToDb(MT5_POSITIONS_TABLE_NAME, List.of(position1, position2));
        addFraudForClient(client, HEDGING, INTERNAL, CONFIRMED, List.of("EURUSD", "GBPUSD"));
        abuserHistory = getObjectsFromDB(
                DbName.POSTGRES,
                AR_ABUSER_HISTORY_TABLE_NAME,
                String.format("ucid = '%s'", client.getUcid()),
                AbuserHistory.class);
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteUserFromAbuseRegistry(client.getUcid());
        deleteEntryFromDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, String.format("ucid = '%s'", client.getUcid()));
        deleteEntryFromDb(MT5_POSITIONS_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
    }

    @Test
    @Order(1)
    @AllureId("1436")
    @DisplayName("Holding scheduler deduction with additional accounts test")
    void holdingSchedulerTest1() throws Exception {
        holdingDeduction1 = generateAbuserDeductionByAccount(
                account1, abuserHistory.getLast().getId());
        holdingDeduction1.setStatusOpenPositions(HOLDING.getDisplayName());
        holdingDeduction1.setStatusEmail(SENT.getDisplayName());
        holdingDeduction1.setStatusDeduction(TO_BE_DEDUCTED.getDisplayName());
        holdingDeduction1.setStatusApproval(APPROVED.getDisplayName());
        holdingDeduction1.setIllegalProfit(
                (trade1.getProfit() + tradeWithdrawal.getProfit()) - tradeWithdrawal.getProfit());
        holdingDeduction1.setSuggestedDeduction(0d);
        holdingDeduction1.setSuggestedDeductionUsd(0d);
        holdingDeduction1.setActualDeduction(null);
        holdingDeduction1.setBalanceAtResolution(trade1.getProfit() + tradeWithdrawal.getProfit());
        holdingDeduction1.setBalanceAtResolution(holdingDeduction1.getBalanceAtResolution());
        insertObjectToDb(DbName.POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, holdingDeduction1);
        executeQueryToDb(
                DbName.CLICKHOUSE,
                String.format(
                        "UPDATE %s SET is_deleted = 1 WHERE account = %s",
                        MT5_POSITIONS_TABLE_NAME, mtAccount1.account));
        List<AbuserDeduction> deductionList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            deductionList = getObjectsFromDB(
                    DbName.POSTGRES,
                    AR_ABUSER_DEDUCTION_TABLE_NAME,
                    String.format("ucid = '%s'", client.getUcid()),
                    AbuserDeduction.class);
            if (deductionList.size() <= 1) {
                Thread.sleep(2000);
                if (i == 9) {
                    assertThat(
                            "The holding scheduler did not recalculate deductions in 20 sec",
                            deductionList.size(),
                            greaterThan(1));
                }
            } else {
                break;
            }
        }
        for (AbuserDeduction deduction : deductionList) {
            assertThat(
                    "Verify created deduction has abuser_history_id not null",
                    deduction.getAbuserHistoryId(),
                    is(deduction.getAbuserHistoryId()));
            assertThat(
                    "Verify created deduction has illegal_profit_usd not null",
                    deduction.getIllegalProfitUsd(),
                    notNullValue());
            assertThat(
                    "Verify created deduction has suggested_deduction_usd not null",
                    deduction.getSuggestedDeductionUsd(),
                    notNullValue());
            assertThat("Verify created deduction has created_at not null", deduction.getCreatedAt(), notNullValue());
            assertThat("Verify created deduction has updated_at not null", deduction.getUpdatedAt(), notNullValue());
        }
        AbuserDeduction deduction1 = new AbuserDeduction(
                client.getUcid(),
                holdingDeduction1.getAbuserHistoryId(),
                mtAccount1.account.toString(),
                mtAccount1.sourceIdSt,
                mtAccount1.server,
                mtAccount1.currency,
                client.getBrand(),
                WAS_HOLDING.getDisplayName(),
                SENT.getDisplayName(),
                TO_BE_DEDUCTED.getDisplayName(),
                AWAITING_APPROVAL.getDisplayName(),
                holdingDeduction1.getComment(),
                (trade1.getProfit() + tradeWithdrawal.getProfit()) - tradeWithdrawal.getProfit(),
                null,
                trade1.getProfit() + tradeWithdrawal.getProfit(),
                null,
                null,
                null,
                null,
                null,
                String.format(
                        "%s %s",
                        autotestUserOne().getFirstName(), autotestUserOne().getLastName()),
                VINDEX_BO_SYSTEM,
                ILLEGAL_PROFIT.getDisplayName(),
                null,
                holdingDeduction1.getCommentDeduction(),
                trade1.getProfit() + tradeWithdrawal.getProfit(),
                null,
                client.getUserId().toString(),
                trade1.getProfit() + tradeWithdrawal.getProfit(),
                null,
                false,
                FULL_DEDUCTION.getDisplayName());
        AbuserDeduction deduction2 = new AbuserDeduction(
                client.getUcid(),
                holdingDeduction1.getAbuserHistoryId(),
                mtAccount5.account.toString(),
                mtAccount5.sourceIdSt,
                mtAccount5.server,
                mtAccount5.currency,
                client.getBrand(),
                NOT_HOLDING.getDisplayName(),
                SENT.getDisplayName(),
                TO_BE_DEDUCTED.getDisplayName(),
                AWAITING_APPROVAL.getDisplayName(),
                holdingDeduction1.getComment(),
                0d,
                null,
                trade5.getProfit(),
                null,
                null,
                null,
                null,
                null,
                String.format(
                        "%s %s",
                        autotestUserOne().getFirstName(), autotestUserOne().getLastName()),
                VINDEX_BO_SYSTEM,
                NO_ILLEGAL_PROFIT.getDisplayName(),
                null,
                holdingDeduction1.getCommentDeduction(),
                trade5.getProfit(),
                null,
                client.getUserId().toString(),
                null,
                null,
                false,
                FULL_DEDUCTION.getDisplayName());
        AbuserDeduction deduction3 = new AbuserDeduction(
                client.getUcid(),
                holdingDeduction1.getAbuserHistoryId(),
                mtAccount4.account.toString(),
                mtAccount4.sourceIdSt,
                mtAccount4.server,
                mtAccount4.currency,
                client.getBrand(),
                NOT_HOLDING.getDisplayName(),
                SENT.getDisplayName(),
                TO_BE_DEDUCTED.getDisplayName(),
                AWAITING_APPROVAL.getDisplayName(),
                holdingDeduction1.getComment(),
                0d,
                null,
                trade4.getProfit(),
                null,
                null,
                null,
                null,
                null,
                String.format(
                        "%s %s",
                        autotestUserOne().getFirstName(), autotestUserOne().getLastName()),
                VINDEX_BO_SYSTEM,
                NO_ILLEGAL_PROFIT.getDisplayName(),
                null,
                holdingDeduction1.getCommentDeduction(),
                trade4.getProfit(),
                null,
                client.getUserId().toString(),
                null,
                null,
                false,
                FULL_DEDUCTION.getDisplayName());
        AbuserDeduction deduction5 = new AbuserDeduction(
                client.getUcid(),
                holdingDeduction1.getAbuserHistoryId(),
                mtAccount2.account.toString(),
                mtAccount2.sourceIdSt,
                mtAccount2.server,
                mtAccount2.currency,
                client.getBrand(),
                NOT_HOLDING.getDisplayName(),
                SENT.getDisplayName(),
                TO_BE_DEDUCTED.getDisplayName(),
                AWAITING_APPROVAL.getDisplayName(),
                holdingDeduction1.getComment(),
                0d,
                null,
                trade2.getProfit(),
                null,
                null,
                null,
                null,
                null,
                String.format(
                        "%s %s",
                        autotestUserOne().getFirstName(), autotestUserOne().getLastName()),
                VINDEX_BO_SYSTEM,
                NO_ILLEGAL_PROFIT.getDisplayName(),
                null,
                holdingDeduction1.getCommentDeduction(),
                trade2.getProfit(),
                null,
                client.getUserId().toString(),
                null,
                null,
                false,
                FULL_DEDUCTION.getDisplayName());
        AbuserDeduction deduction4 = new AbuserDeduction(
                client.getUcid(),
                holdingDeduction1.getAbuserHistoryId(),
                mtAccount3.account.toString(),
                mtAccount3.sourceIdSt,
                mtAccount3.server,
                mtAccount3.currency,
                client.getBrand(),
                HOLDING.getDisplayName(),
                SENT.getDisplayName(),
                TO_BE_DEDUCTED.getDisplayName(),
                AWAITING_APPROVAL.getDisplayName(),
                holdingDeduction1.getComment(),
                0d,
                null,
                0d,
                null,
                null,
                null,
                null,
                null,
                String.format(
                        "%s %s",
                        autotestUserOne().getFirstName(), autotestUserOne().getLastName()),
                VINDEX_BO_SYSTEM,
                NO_ILLEGAL_PROFIT.getDisplayName(),
                null,
                holdingDeduction1.getCommentDeduction(),
                0d,
                null,
                client.getUserId().toString(),
                null,
                null,
                false,
                FULL_DEDUCTION.getDisplayName());
        assertThat(
                "Verify deductions in abuser_deduction table are as expected",
                deductionList,
                containsInAnyOrder(deduction1, deduction2, deduction3, deduction4, deduction5));
    }

    @Test
    @Order(2)
    @AllureId("1437")
    @DisplayName("Holding scheduler deduction for linked deduction test")
    void holdingSchedulerTest2() throws Exception {
        executeQueryToDb(
                DbName.CLICKHOUSE,
                String.format(
                        "UPDATE %s SET is_deleted = 1 WHERE account = %s",
                        MT5_POSITIONS_TABLE_NAME, mtAccount3.account));
        List<AbuserDeduction> deductionList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            deductionList = getObjectsFromDB(
                    DbName.POSTGRES,
                    AR_ABUSER_DEDUCTION_TABLE_NAME,
                    String.format("account = '%s'", mtAccount3.account),
                    AbuserDeduction.class);
            if (!Objects.equals(deductionList.getFirst().getStatusOpenPositions(), WAS_HOLDING.getDisplayName())) {
                Thread.sleep(2000);
                if (i == 9) {
                    assertThat(
                            "The holding scheduler did not recalculate deductions in 20 sec",
                            deductionList.getFirst().getStatusOpenPositions(),
                            is(WAS_HOLDING.getDisplayName()));
                }
            } else {
                deductionList = getObjectsFromDB(
                        DbName.POSTGRES,
                        AR_ABUSER_DEDUCTION_TABLE_NAME,
                        String.format("ucid = '%s'", client.getUcid()),
                        AbuserDeduction.class);
                break;
            }
        }
        for (AbuserDeduction deduction : deductionList) {
            assertThat(
                    "Verify created deduction has abuser_history_id not null",
                    deduction.getAbuserHistoryId(),
                    is(deduction.getAbuserHistoryId()));
            assertThat(
                    "Verify created deduction has illegal_profit_usd not null",
                    deduction.getIllegalProfitUsd(),
                    notNullValue());
            assertThat(
                    "Verify created deduction has suggested_deduction_usd not null",
                    deduction.getSuggestedDeductionUsd(),
                    notNullValue());
            assertThat("Verify created deduction has created_at not null", deduction.getCreatedAt(), notNullValue());
            assertThat("Verify created deduction has updated_at not null", deduction.getUpdatedAt(), notNullValue());
        }
        AbuserDeduction deduction1 = new AbuserDeduction(
                client.getUcid(),
                holdingDeduction1.getAbuserHistoryId(),
                mtAccount1.account.toString(),
                mtAccount1.sourceIdSt,
                mtAccount1.server,
                mtAccount1.currency,
                client.getBrand(),
                WAS_HOLDING.getDisplayName(),
                NOT_SENT.getDisplayName(),
                TO_BE_DEDUCTED.getDisplayName(),
                AWAITING_APPROVAL.getDisplayName(),
                holdingDeduction1.getComment(),
                (trade1.getProfit() + tradeWithdrawal.getProfit()) - tradeWithdrawal.getProfit(),
                null,
                trade1.getProfit() + tradeWithdrawal.getProfit(),
                null,
                null,
                null,
                null,
                null,
                String.format(
                        "%s %s",
                        autotestUserOne().getFirstName(), autotestUserOne().getLastName()),
                VINDEX_BO_SYSTEM,
                ILLEGAL_PROFIT.getDisplayName(),
                null,
                holdingDeduction1.getCommentDeduction(),
                trade1.getProfit() + tradeWithdrawal.getProfit(),
                null,
                client.getUserId().toString(),
                trade1.getProfit() + tradeWithdrawal.getProfit(),
                null,
                false,
                FULL_DEDUCTION.getDisplayName());
        AbuserDeduction deduction2 = new AbuserDeduction(
                client.getUcid(),
                holdingDeduction1.getAbuserHistoryId(),
                mtAccount5.account.toString(),
                mtAccount5.sourceIdSt,
                mtAccount5.server,
                mtAccount5.currency,
                client.getBrand(),
                NOT_HOLDING.getDisplayName(),
                NOT_SENT.getDisplayName(),
                TO_BE_DEDUCTED.getDisplayName(),
                AWAITING_APPROVAL.getDisplayName(),
                holdingDeduction1.getComment(),
                0d,
                null,
                trade5.getProfit(),
                null,
                null,
                null,
                null,
                null,
                String.format(
                        "%s %s",
                        autotestUserOne().getFirstName(), autotestUserOne().getLastName()),
                VINDEX_BO_SYSTEM,
                NO_ILLEGAL_PROFIT.getDisplayName(),
                null,
                holdingDeduction1.getCommentDeduction(),
                trade5.getProfit(),
                null,
                client.getUserId().toString(),
                null,
                null,
                false,
                FULL_DEDUCTION.getDisplayName());
        AbuserDeduction deduction3 = new AbuserDeduction(
                client.getUcid(),
                holdingDeduction1.getAbuserHistoryId(),
                mtAccount4.account.toString(),
                mtAccount4.sourceIdSt,
                mtAccount4.server,
                mtAccount4.currency,
                client.getBrand(),
                NOT_HOLDING.getDisplayName(),
                NOT_SENT.getDisplayName(),
                TO_BE_DEDUCTED.getDisplayName(),
                AWAITING_APPROVAL.getDisplayName(),
                holdingDeduction1.getComment(),
                0d,
                null,
                trade4.getProfit(),
                null,
                null,
                null,
                null,
                null,
                String.format(
                        "%s %s",
                        autotestUserOne().getFirstName(), autotestUserOne().getLastName()),
                VINDEX_BO_SYSTEM,
                NO_ILLEGAL_PROFIT.getDisplayName(),
                null,
                holdingDeduction1.getCommentDeduction(),
                trade4.getProfit(),
                null,
                client.getUserId().toString(),
                null,
                null,
                false,
                FULL_DEDUCTION.getDisplayName());
        AbuserDeduction deduction5 = new AbuserDeduction(
                client.getUcid(),
                holdingDeduction1.getAbuserHistoryId(),
                mtAccount2.account.toString(),
                mtAccount2.sourceIdSt,
                mtAccount2.server,
                mtAccount2.currency,
                client.getBrand(),
                NOT_HOLDING.getDisplayName(),
                NOT_SENT.getDisplayName(),
                TO_BE_DEDUCTED.getDisplayName(),
                AWAITING_APPROVAL.getDisplayName(),
                holdingDeduction1.getComment(),
                0d,
                null,
                trade2.getProfit(),
                null,
                null,
                null,
                null,
                null,
                String.format(
                        "%s %s",
                        autotestUserOne().getFirstName(), autotestUserOne().getLastName()),
                VINDEX_BO_SYSTEM,
                NO_ILLEGAL_PROFIT.getDisplayName(),
                null,
                holdingDeduction1.getCommentDeduction(),
                trade2.getProfit(),
                null,
                client.getUserId().toString(),
                null,
                null,
                false,
                FULL_DEDUCTION.getDisplayName());
        AbuserDeduction deduction4 = new AbuserDeduction(
                client.getUcid(),
                holdingDeduction1.getAbuserHistoryId(),
                mtAccount3.account.toString(),
                mtAccount3.sourceIdSt,
                mtAccount3.server,
                mtAccount3.currency,
                client.getBrand(),
                WAS_HOLDING.getDisplayName(),
                NOT_SENT.getDisplayName(),
                TO_BE_DEDUCTED.getDisplayName(),
                AWAITING_APPROVAL.getDisplayName(),
                holdingDeduction1.getComment(),
                0d,
                null,
                trade3.getProfit(),
                null,
                null,
                null,
                null,
                null,
                String.format(
                        "%s %s",
                        autotestUserOne().getFirstName(), autotestUserOne().getLastName()),
                VINDEX_BO_SYSTEM,
                NO_ILLEGAL_PROFIT.getDisplayName(),
                null,
                holdingDeduction1.getCommentDeduction(),
                trade3.getProfit(),
                null,
                client.getUserId().toString(),
                null,
                null,
                false,
                FULL_DEDUCTION.getDisplayName());
        assertThat(
                "Verify deductions in abuser_deduction table are as expected",
                deductionList,
                containsInAnyOrder(deduction1, deduction2, deduction3, deduction4, deduction5));
    }

    @Test
    @Order(3)
    @AllureId("1438")
    @DisplayName(
            "Holding scheduler deduction keeps approved status when no major changes in balance or illegal profit and rest of the flow")
    void holdingSchedulerTest3() throws Exception {
        deleteDeductions(client.getUcid());
        AbuserDeduction holdingDeduction2 = generateAbuserDeductionByAccount(
                account2, abuserHistory.getLast().getId());
        holdingDeduction2.setStatusOpenPositions(HOLDING.getDisplayName());
        holdingDeduction2.setStatusEmail(SENT.getDisplayName());
        holdingDeduction2.setStatusDeduction(TO_BE_DEDUCTED.getDisplayName());
        holdingDeduction2.setStatusApproval(APPROVED.getDisplayName());
        holdingDeduction2.setIllegalProfit(trade2.getProfit());
        holdingDeduction2.setSuggestedDeduction(0d);
        holdingDeduction2.setSuggestedDeductionUsd(0d);
        holdingDeduction2.setActualDeduction(null);
        holdingDeduction2.setActualDeductionUsd(null);
        holdingDeduction2.setBalanceAtResolution(trade2.getProfit());
        holdingDeduction2.setBalanceAtResolutionUsd(holdingDeduction2.getBalanceAtResolution());
        insertObjectToDb(DbName.POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, holdingDeduction2);

        // Verify the deduction after positions are closed
        List<AbuserDeduction> deductionList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            deductionList = getObjectsFromDB(
                    DbName.POSTGRES,
                    AR_ABUSER_DEDUCTION_TABLE_NAME,
                    String.format("account = '%s'", mtAccount2.account),
                    AbuserDeduction.class);
            if (!Objects.equals(deductionList.getFirst().getStatusOpenPositions(), WAS_HOLDING.getDisplayName())) {
                Thread.sleep(2000);
                if (i == 9) {
                    assertThat(
                            "The holding scheduler did not recalculate deductions in 15 sec",
                            deductionList.getFirst().getStatusOpenPositions(),
                            is(WAS_HOLDING.getDisplayName()));
                }
            } else {
                break;
            }
        }
        assertThat("Verify there were no additional deductions", deductionList.size(), is(1));
        AbuserDeduction wasHoldingDeduction = deductionList.getFirst();
        AbuserDeduction expectedDeduction1 = new AbuserDeduction(
                client.getUcid(),
                holdingDeduction2.getAbuserHistoryId(),
                mtAccount2.account.toString(),
                mtAccount2.sourceIdSt,
                mtAccount2.server,
                mtAccount2.currency,
                client.getBrand(),
                WAS_HOLDING.getDisplayName(),
                SENT.getDisplayName(),
                TO_BE_DEDUCTED.getDisplayName(),
                APPROVED.getDisplayName(),
                holdingDeduction2.getComment(),
                trade2.getProfit(),
                null,
                trade2.getProfit(),
                null,
                null,
                null,
                null,
                null,
                String.format(
                        "%s %s",
                        autotestUserOne().getFirstName(), autotestUserOne().getLastName()),
                VINDEX_BO_SYSTEM,
                ILLEGAL_PROFIT.getDisplayName(),
                null,
                holdingDeduction2.getCommentDeduction(),
                trade2.getProfit(),
                null,
                client.getUserId().toString(),
                trade2.getProfit(),
                null,
                true,
                FULL_DEDUCTION.getDisplayName());
        AbuserDeduction expectedDeduction2 = new AbuserDeduction(
                client.getUcid(),
                holdingDeduction2.getAbuserHistoryId(),
                mtAccount2.account.toString(),
                mtAccount2.sourceIdSt,
                mtAccount2.server,
                mtAccount2.currency,
                client.getBrand(),
                WAS_HOLDING.getDisplayName(),
                SENT.getDisplayName(),
                TO_BE_DEDUCTED.getDisplayName(),
                APPROVED.getDisplayName(),
                holdingDeduction2.getComment(),
                trade2.getProfit(),
                null,
                trade2.getProfit(),
                null,
                null,
                null,
                null,
                null,
                String.format(
                        "%s %s",
                        autotestUserOne().getFirstName(), autotestUserOne().getLastName()),
                VINDEX_BO_SYSTEM,
                ILLEGAL_PROFIT.getDisplayName(),
                null,
                holdingDeduction2.getCommentDeduction(),
                trade2.getProfit(),
                null,
                client.getUserId().toString(),
                trade2.getProfit(),
                null,
                true,
                FULL_DEDUCTION.getDisplayName());
        assertThat(
                "Verify created deduction has abuser_history_id not null",
                wasHoldingDeduction.getAbuserHistoryId(),
                is(expectedDeduction1.getAbuserHistoryId()));
        assertThat(
                "Verify created deduction has illegal_profit_usd not null",
                wasHoldingDeduction.getIllegalProfitUsd(),
                notNullValue());
        assertThat(
                "Verify created deduction has suggested_deduction_usd not null",
                wasHoldingDeduction.getSuggestedDeductionUsd(),
                notNullValue());
        assertThat(
                "Verify created deduction has created_at not null", wasHoldingDeduction.getCreatedAt(), notNullValue());
        assertThat(
                "Verify created deduction has updated_at not null", wasHoldingDeduction.getUpdatedAt(), notNullValue());
        assertThat(
                "Verify deduction in abuser_deduction table is as expected",
                wasHoldingDeduction,
                anyOf(is(expectedDeduction1), is(expectedDeduction2)));

        // Verify the deduction after deduction is picked up for sending to kafka
        for (int i = 0; i < 7; i++) {
            deductionList = getObjectsFromDB(
                    DbName.POSTGRES,
                    AR_ABUSER_DEDUCTION_TABLE_NAME,
                    String.format("account = '%s'", mtAccount2.account),
                    AbuserDeduction.class);
            if (!Objects.equals(deductionList.getFirst().getStatusDeduction(), PROCESSING.getDisplayName())) {
                Thread.sleep(2000);
                if (i == 6) {
                    assertThat(
                            "The processing scheduler did not recalculate deductions in 10 sec",
                            deductionList.getFirst().getStatusOpenPositions(),
                            is(WAS_HOLDING.getDisplayName()));
                }
            } else {
                break;
            }
        }
        expectedDeduction1.setStatusDeduction(PROCESSING.getDisplayName());
        assertThat(
                "Verify deduction in abuser_deduction table is as expected",
                deductionList.getFirst(),
                is(expectedDeduction1));
        AccountDeductionRequest kafkaDeductionRequest = objectMapper.readValue(
                kafka.consumeMessage(KAFKA_TOPIC_ACCOUNT_DEDUCTION_REQUEST, mtAccount2.account.toString()),
                AccountDeductionRequest.class);
        AccountDeductionRequest expectedKafkaDeductionRequest = new AccountDeductionRequest(
                null,
                null,
                mtAccount2.sourceIdSt,
                mtAccount2.account,
                "BALANCE",
                mtAccount2.currency,
                trade2.getProfit() * -1,
                "Cash Adjustment-PNL");
        assertThat("Verify kafka message is as expected", kafkaDeductionRequest, is(expectedKafkaDeductionRequest));
    }
}
