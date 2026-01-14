package tests.vindex_backoffice_ui_tests.abuseRegistry.fraudsters;

import static business_objects.db.abuse_registry_db.AbuserDeductionFactory.generateAbuserDeductionByAccount;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_account_for_mt.crm_tb_account.CrmTbAccountForMtObjectFactory.generateAccountForMtByAccount;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoercedAccountProfitComment;
import static business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObjectFactory.generateMtMt5PositionsObject;
import static helpers.api.AbuseRegistryHelper.addFraudForClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.Currency.USD;
import static helpers.data.enums.FraudType.*;
import static helpers.data.enums.FraudTypeStatus.CONFIRMED;
import static helpers.data.enums.deduction.DeductionStatusApproval.*;
import static helpers.data.enums.deduction.DeductionStatusDeduction.NO_DEDUCTION;
import static helpers.data.enums.deduction.DeductionStatusDeduction.TO_BE_DEDUCTED;
import static helpers.data.enums.deduction.DeductionStatusEmail.NOT_SENT;
import static helpers.data.enums.deduction.DeductionStatusEmail.SENT;
import static helpers.data.enums.deduction.DeductionStatusOpenPositions.*;
import static helpers.database.ArHelper.deleteUserFromAbuseRegistry;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static utils.Constants.*;
import static utils.Utils.getCurrentTimestampSeconds;
import static utils.Utils.getRandomIntPositive;

import business_objects.db.abuse_registry_db.AbuserDeduction;
import business_objects.db.abuse_registry_db.AbuserHistory;
import business_objects.db.abuse_registry_db.PendingProcessing;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObject;
import business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObject;
import helpers.data.ClientHelper;
import helpers.data.enums.FraudType;
import helpers.data.enums.deduction.DeductionType;
import helpers.database.DbName;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
@Tag(ABUSE_REGISTRY)
@Feature("BMS-2427 Batch fraud type adding by server-account pair from Fraudsters tab")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MassUploadWithDeductionAccServerNameTest extends TestBaseWeb {

    private static final ClientHelper client4 = getRandomVantageClientAllFields();
    private static final ClientHelper client3 = getRandomVantageClientAllFields();
    private static final ClientHelper client2 = getRandomVantageClientAllFields();
    private static final ClientHelper client1 = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client1);
    private static final CrmTbAccountObject account1 = generateCrmTbAccountDataForUi(client1);
    private static final CrmTbAccountObject account2 = generateCrmTbAccountDataForUi(client2);
    private static final CrmTbAccountObject account3 = generateCrmTbAccountDataForUi(client2);
    private static final CrmTbAccountObject account4 = generateCrmTbAccountDataForUi(client3);
    private static final CrmTbAccountObject account5 = generateCrmTbAccountDataForUi(client4);
    private static MtAccountObject mtAccount1;
    private static MtMt4TradesCoercedObject trade1;
    private static MtMt4TradesCoercedObject tradeWithdrawal;
    private static AbuserDeduction activeDeduction;
    private static AbuserDeduction activeDeductionPartial;
    private static AbuserDeduction activeDeductionNo;
    private static AbuserDeduction completedDeduction;
    private static List<AbuserHistory> abuserHistory = List.of();

    @BeforeAll
    static void setup() throws Exception {
        account1.currency = USD.getCode();
        account2.account = getRandomIntPositive();
        account3.account = getRandomIntPositive();
        account4.currency = USD.getCode();
        account4.accountStatus = "Inactive";
        mtAccount1 = generateMtAccountByCrmTbAccount(account1);
        var mtAccount2 = generateMtAccountByCrmTbAccount(account2);
        var mtAccount3 = generateMtAccountByCrmTbAccount(account3);
        var mtAccount4 = generateMtAccountByCrmTbAccount(account4);
        var mtAccount5 = generateMtAccountByCrmTbAccount(account5);

        String comment = "comment";
        trade1 = generateMt4TradesCoercedAccountProfitComment(account1, 500.12 + 10_000d, comment);
        tradeWithdrawal = generateMt4TradesCoercedAccountProfitComment(account1, -10_000d, "withdraw");

        MtMt4TradesCoercedObject trade2 =
                generateMt4TradesCoercedAccountProfitComment(account2, 500.12 + 10_000d, comment);
        var tradeWithdrawal2 = generateMt4TradesCoercedAccountProfitComment(account2, -10_000d, "withdraw");
        var trade3 = generateMt4TradesCoercedAccountProfitComment(account3, 1800.45, comment);
        var trade4 = generateMt4TradesCoercedAccountProfitComment(account4, 1800.45, comment);
        var trade5 = generateMt4TradesCoercedAccountProfitComment(account5, 1800.45, comment);
        CrmTbUserObject crmClient2 = generateUserByClient(client2);
        CrmTbUserObject crmClient3 = generateUserByClient(client3);
        CrmTbUserObject crmClient4 = generateUserByClient(client4);
        insertObjectsToDb(CRM_USER_TABLE_NAME, List.of(crmTbUser, crmClient2, crmClient3, crmClient4));
        insertObjectsToDb(CRM_TB_ACCOUNT_TABLE_NAME, List.of(account1, account2, account3, account4, account5));
        insertObjectsToDb(
                CRM_TB_ACCOUNT_FOR_MT_TABLE_NAME,
                List.of(
                        generateAccountForMtByAccount(account1),
                        generateAccountForMtByAccount(account2),
                        generateAccountForMtByAccount(account3),
                        generateAccountForMtByAccount(account4),
                        generateAccountForMtByAccount(account5)));
        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccount1, mtAccount2, mtAccount3, mtAccount4, mtAccount5));
        insertObjectsToDb(
                MT4_TRADES_COERCED_TABLE_NAME,
                List.of(trade1, tradeWithdrawal, trade2, trade3, tradeWithdrawal2, trade4, trade5));
        MtMt5PositionsObject position1 = generateMtMt5PositionsObject(client1);
        MtMt5PositionsObject position2 = generateMtMt5PositionsObject(client2);
        MtMt5PositionsObject position3 = generateMtMt5PositionsObject(client3);
        insertObjectsToDb(MT5_POSITIONS_TABLE_NAME, List.of(position1, position2, position3));
        executeQueryToDb(
                DbName.CLICKHOUSE,
                String.format(
                        "UPDATE %s SET is_deleted = 1 WHERE account = %s",
                        MT5_POSITIONS_TABLE_NAME, mtAccount2.account));
        executeQueryToDb(
                DbName.CLICKHOUSE,
                String.format(
                        "UPDATE %s SET is_deleted = 1 WHERE account = %s",
                        MT5_POSITIONS_TABLE_NAME, mtAccount3.account));
        executeQueryToDb(
                DbName.CLICKHOUSE,
                String.format(
                        "UPDATE %s SET is_deleted = 1 WHERE account = %s",
                        MT5_POSITIONS_TABLE_NAME, mtAccount4.account));

        addFraudForClient(client4, MARKET_MANIPULATION, null, CONFIRMED, List.of("EURUSD", "GBPUSD"));
        addFraudForClient(client4, LOOPHOLE_ABUSE, null, CONFIRMED, List.of("EURUSD", "GBPUSD"));
        addFraudForClient(client4, NBP_ABUSE, null, CONFIRMED, List.of("EURUSD", "GBPUSD"));
        addFraudForClient(client4, BONUS_ABUSE, null, CONFIRMED, List.of("EURUSD", "GBPUSD"));

        abuserHistory = getObjectsFromDB(
                        DbName.POSTGRES,
                        AR_ABUSER_HISTORY_TABLE_NAME,
                        String.format("ucid = '%s' and source = 'FRAUD_TYPE_STATUS'", client4.getUcid()),
                        AbuserHistory.class)
                .stream()
                .sorted(Comparator.comparingLong(AbuserHistory::getId))
                .toList();
        activeDeduction = generateAbuserDeductionByAccount(
                account5, abuserHistory.getFirst().getId());
        activeDeduction.setStatusDeduction(TO_BE_DEDUCTED.getDisplayName());
        activeDeduction.setStatusApproval(APPROVED.getDisplayName());
        insertObjectToDb(DbName.POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, activeDeduction);

        activeDeductionPartial =
                generateAbuserDeductionByAccount(account5, abuserHistory.get(1).getId());
        activeDeductionPartial.setStatusDeduction(TO_BE_DEDUCTED.getDisplayName());
        activeDeductionPartial.setStatusApproval(APPROVED.getDisplayName());
        insertObjectToDb(DbName.POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, activeDeductionPartial);

        activeDeductionNo =
                generateAbuserDeductionByAccount(account5, abuserHistory.get(2).getId());
        activeDeductionNo.setStatusDeduction(TO_BE_DEDUCTED.getDisplayName());
        activeDeductionNo.setStatusApproval(APPROVED.getDisplayName());
        insertObjectToDb(DbName.POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, activeDeductionNo);

        completedDeduction =
                generateAbuserDeductionByAccount(account5, abuserHistory.get(3).getId());
        completedDeduction.setStatusDeduction(REJECTED.getDisplayName());
        completedDeduction.setStatusApproval(APPROVED.getDisplayName());
        insertObjectToDb(DbName.POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, completedDeduction);
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteUserFromAbuseRegistry(client1.getUcid());
        deleteUserFromAbuseRegistry(client2.getUcid());
        deleteUserFromAbuseRegistry(client3.getUcid());
        deleteUserFromAbuseRegistry(client4.getUcid());
        deleteObjectFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", client1.getUcid()));
        deleteObjectFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", client2.getUcid()));
        deleteObjectFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", client3.getUcid()));
        deleteObjectFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", client4.getUcid()));
        deleteObjectFromDb(MT4_TRADES_COERCED_TABLE_NAME, String.format("ucid = '%s'", client1.getUcid()));
        deleteObjectFromDb(MT4_TRADES_COERCED_TABLE_NAME, String.format("ucid = '%s'", client2.getUcid()));
        deleteObjectFromDb(MT4_TRADES_COERCED_TABLE_NAME, String.format("ucid = '%s'", client3.getUcid()));
        deleteObjectFromDb(MT4_TRADES_COERCED_TABLE_NAME, String.format("ucid = '%s'", client4.getUcid()));
        deleteObjectFromDb(MT5_POSITIONS_TABLE_NAME, String.format("ucid = '%s'", client1.getUcid()));
        deleteObjectFromDb(MT5_POSITIONS_TABLE_NAME, String.format("ucid = '%s'", client2.getUcid()));
        deleteObjectFromDb(MT5_POSITIONS_TABLE_NAME, String.format("ucid = '%s'", client3.getUcid()));
    }

    @Test
    @AllureId("1671")
    @DisplayName("Batch upload by serverName and acc PARTIAL_DEDUCTION test")
    void accServerNamePartialTest() throws Exception {

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        fraudstersPage.navigateAbuseRegistryFraudsters();
        fraudstersPage.openUploadDrawer();
        fraudstersPage.typeServerNameAcc(account1.serverName, account1.account.toString());
        fraudstersPage.clickAddFraudButton();
        FraudType fraudTypeOld = FraudType.PRICING_ERROR;
        fraudstersPage.addSelectedFraudAdd(fraudTypeOld.getName(), "Confirmed");
        String commentary = "test" + getCurrentTimestampSeconds();
        fraudstersPage.fillCommentary(commentary);
        fraudstersPage.clickApplyUpload();
        fraudstersPage.verifySuccessMessageUpload();
        fraudstersPage.verifyWarningMessageUpload(1);

        page.waitForTimeout(1000);

        List<PendingProcessing> pendingProcessing = getObjectsFromDB(
                DbName.POSTGRES,
                AR_PENDING_PROCESSING_TABLE_NAME,
                String.format("ucid in ('%s')", client1.getUcid()),
                PendingProcessing.class);
        assertThat(pendingProcessing.size(), is(1));
        PendingProcessing pending = pendingProcessing.getFirst();
        assertThat(pending.getUcid(), is(client1.getUcid()));
        assertThat(pending.getFraudTypeCode(), is(fraudTypeOld.getCode()));
        assertThat(pendingProcessing.getFirst().getFraudSubtypeCode(), nullValue());
    }

    @Test
    @AllureId("1672")
    @DisplayName("Batch upload by serverName and acc with extra deduction test")
    void moreThan1accAndFullDeductionTest() throws Exception {

        deleteUserFromAbuseRegistry(client1.getUcid());
        deleteUserFromAbuseRegistry(client2.getUcid());

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        fraudstersPage.navigateAbuseRegistryFraudsters();
        fraudstersPage.openUploadDrawer();
        fraudstersPage.typeServerNameAcc(
                account1.serverName, account1.account.toString(), account2.serverName, account2.account.toString());
        fraudstersPage.clickAddFraudButton();
        FraudType fraudType = FraudType.LOOPHOLE_ABUSE;
        // Partial Deduction
        fraudstersPage.addSelectedFraudAdd(fraudType.getName(), "Confirmed");
        String commentary = "test" + getCurrentTimestampSeconds();
        fraudstersPage.fillCommentary(commentary);
        fraudstersPage.clickApplyUpload();
        fraudstersPage.verifySuccessMessageUpload(3);
        page.waitForTimeout(1000);

        List<PendingProcessing> pendingProcessing = getObjectsFromDB(
                DbName.POSTGRES,
                AR_PENDING_PROCESSING_TABLE_NAME,
                String.format("ucid in ('%s')", client1.getUcid()),
                PendingProcessing.class);
        assertThat(pendingProcessing.size(), is(0));

        List<AbuserHistory> abuserHistory = getObjectsFromDB(
                DbName.POSTGRES,
                AR_ABUSER_HISTORY_TABLE_NAME,
                String.format("ucid = '%s'", client2.getUcid()),
                AbuserHistory.class);

        AbuserDeduction expected = new AbuserDeduction(
                client2.getUcid(),
                abuserHistory.getLast().getId(),
                account2.account.toString(),
                account2.serverIdSt,
                account2.serverName,
                "USD",
                "Vantage",
                NOT_HOLDING.getDisplayName(),
                NOT_SENT.getDisplayName(),
                TO_BE_DEDUCTED.getDisplayName(),
                AWAITING_APPROVAL.getDisplayName(),
                commentary,
                10_500.12,
                10_500.12,
                500.12,
                500.12,
                null,
                null,
                null,
                null,
                "backoffice-test backoffice-test",
                "Vindex BO",
                "ILLEGAL_PROFIT",
                null,
                null,
                500.12,
                500.12,
                client2.getUserId().toString(),
                500.12,
                500.12,
                false,
                DeductionType.FULL_DEDUCTION.getDisplayName());
        AbuserDeduction expected2 = new AbuserDeduction(
                client2.getUcid(),
                abuserHistory.getLast().getId(),
                account3.account.toString(),
                account3.serverIdSt,
                account3.serverName,
                "USD",
                "Vantage",
                NOT_HOLDING.getDisplayName(),
                NOT_SENT.getDisplayName(),
                TO_BE_DEDUCTED.getDisplayName(),
                AWAITING_APPROVAL.getDisplayName(),
                commentary,
                0.0,
                0.0,
                1800.45,
                1800.45,
                null,
                null,
                null,
                null,
                "backoffice-test backoffice-test",
                "Vindex BO",
                "NO_ILLEGAL_PROFIT",
                null,
                null,
                1800.45,
                1800.45,
                client2.getUserId().toString(),
                null,
                null,
                false,
                DeductionType.FULL_DEDUCTION.getDisplayName());

        List<AbuserDeduction> deductionList = getObjectsFromDB(
                DbName.POSTGRES,
                AR_ABUSER_DEDUCTION_TABLE_NAME,
                String.format("ucid = '%s'", client2.getUcid()),
                AbuserDeduction.class);
        assertThat(deductionList.size(), is(2));
        System.out.println(account3.account);
        AbuserDeduction actualDeduction = deductionList.stream()
                .filter(abuserDeduction -> abuserDeduction.getAccount().equals(account2.account.toString()))
                .findFirst()
                .get();
        AbuserDeduction actualDeduction2 = deductionList.stream()
                .filter(abuserDeduction -> abuserDeduction.getAccount().equals(account3.account.toString()))
                .findFirst()
                .get();
        assertThat(actualDeduction, is(expected));
        assertThat(actualDeduction2, is(expected2));
    }

    @Test
    @AllureId("1797")
    @DisplayName("Batch upload by serverName and acc INACTIVE ACCOUNT test")
    void accServerNameInactiveTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        fraudstersPage.navigateAbuseRegistryFraudsters();
        fraudstersPage.openUploadDrawer();
        fraudstersPage.typeServerNameAcc(account4.serverName, account4.account.toString());
        fraudstersPage.clickAddFraudButton();
        FraudType fraudTypeOld = FraudType.LOOPHOLE_ABUSE;
        fraudstersPage.addSelectedFraudAdd(fraudTypeOld.getName(), "Confirmed");
        fraudstersPage.clickVindexFraudSource();
        String commentary = "test" + getCurrentTimestampSeconds();
        fraudstersPage.fillCommentary(commentary);
        fraudstersPage.clickApplyUpload();
        fraudstersPage.verifySuccessMessageUpload();

        page.waitForTimeout(2000);

        var deductions = getObjectsFromDB(
                DbName.POSTGRES,
                AR_ABUSER_DEDUCTION_TABLE_NAME,
                String.format("ucid = '%s'", client3.getUcid()),
                AbuserDeduction.class);

        assertThat(deductions.size(), is(1));
        var deduction = deductions.getFirst();

        assertThat(deduction.getUcid(), is(client3.getUcid()));
        assertThat(deduction.getAccount(), is(String.valueOf(account4.account)));
        assertThat(deduction.getServerId(), is(account4.serverIdSt));
        assertThat(deduction.getDeductionType(), is(NO_DEDUCTION.toString()));
        assertThat(deduction.getStatusEmail(), is(SENT.toString()));
        assertThat(deduction.getStatusDeduction(), is(NO_DEDUCTION.toString()));
        assertThat(deduction.getStatusApproval(), is(NOT_REQUIRED.toString()));
    }

    @Test
    @AllureId("2041")
    @DisplayName("Batch upload by serverName and acc, don't create deduction for acc with active deduction test")
    void accServerNameHasActiveDeductionTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        fraudstersPage.navigateAbuseRegistryFraudsters();
        fraudstersPage.openUploadDrawer();
        fraudstersPage.typeServerNameAcc(account5.serverName, account5.account.toString());
        fraudstersPage.clickAddFraudButton();
        fraudstersPage.addSelectedFraud(MARKET_MANIPULATION, CONFIRMED);
        fraudstersPage.clickVindexFraudSource();
        String commentary = "test" + getCurrentTimestampSeconds();
        fraudstersPage.fillCommentary(commentary);
        fraudstersPage.clickApplyUpload();
        fraudstersPage.verifySuccessMessageUpload();

        page.waitForTimeout(2000);

        var deductions = getObjectsFromDB(
                DbName.POSTGRES,
                AR_ABUSER_DEDUCTION_TABLE_NAME,
                String.format(
                        "abuser_history_id not in (%s) and ucid = '%s'",
                        abuserHistory.stream().map(x -> x.getId().toString()).collect(Collectors.joining(", ")),
                        client4.getUcid()),
                AbuserDeduction.class);

        assertThat(deductions.size(), is(0));

        List<PendingProcessing> pendingProcessing = getObjectsFromDB(
                DbName.POSTGRES,
                AR_PENDING_PROCESSING_TABLE_NAME,
                String.format("ucid in ('%s')", client4.getUcid()),
                PendingProcessing.class);
        assertThat(pendingProcessing.size(), is(0));
    }

    @Test
    @AllureId("2046")
    @DisplayName(
            "Batch upload by serverName and acc, don't create deduction for acc with active deduction PARTIAL_DEDUCTION test")
    void accServerNameHasActiveDeductionPartialTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        fraudstersPage.navigateAbuseRegistryFraudsters();
        fraudstersPage.openUploadDrawer();
        fraudstersPage.typeServerNameAcc(account5.serverName, account5.account.toString());
        fraudstersPage.clickAddFraudButton();
        fraudstersPage.addSelectedFraud(LOOPHOLE_ABUSE, CONFIRMED);
        fraudstersPage.clickVindexFraudSource();
        String commentary = "test" + getCurrentTimestampSeconds();
        fraudstersPage.fillCommentary(commentary);
        fraudstersPage.clickApplyUpload();
        fraudstersPage.verifySuccessMessageUpload();

        page.waitForTimeout(2000);

        var deductions = getObjectsFromDB(
                DbName.POSTGRES,
                AR_ABUSER_DEDUCTION_TABLE_NAME,
                String.format(
                        "abuser_history_id not in (%s) and ucid = '%s'",
                        abuserHistory.stream().map(x -> x.getId().toString()).collect(Collectors.joining(", ")),
                        client4.getUcid()),
                AbuserDeduction.class);

        assertThat(deductions.size(), is(0));

        List<PendingProcessing> pendingProcessing = getObjectsFromDB(
                DbName.POSTGRES,
                AR_PENDING_PROCESSING_TABLE_NAME,
                String.format("ucid in ('%s')", client4.getUcid()),
                PendingProcessing.class);
        assertThat(pendingProcessing.size(), is(0));
    }

    @Test
    @AllureId("2048")
    @DisplayName(
            "Batch upload by serverName and acc, don't create deduction for acc with active deduction NO_DEDUCTION test")
    void accServerNameHasActiveDeductionNoDeductionTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        fraudstersPage.navigateAbuseRegistryFraudsters();
        fraudstersPage.openUploadDrawer();
        fraudstersPage.typeServerNameAcc(account5.serverName, account5.account.toString());
        fraudstersPage.clickAddFraudButton();
        fraudstersPage.addSelectedFraud(NBP_ABUSE, CONFIRMED);
        fraudstersPage.clickVindexFraudSource();
        String commentary = "test" + getCurrentTimestampSeconds();
        fraudstersPage.fillCommentary(commentary);
        fraudstersPage.clickApplyUpload();
        fraudstersPage.verifySuccessMessageUpload();

        page.waitForTimeout(2000);

        var deductions = getObjectsFromDB(
                DbName.POSTGRES,
                AR_ABUSER_DEDUCTION_TABLE_NAME,
                String.format(
                        "abuser_history_id not in (%s) and ucid = '%s'",
                        abuserHistory.stream().map(x -> x.getId().toString()).collect(Collectors.joining(", ")),
                        client4.getUcid()),
                AbuserDeduction.class);

        assertThat(deductions.size(), is(0));

        List<PendingProcessing> pendingProcessing = getObjectsFromDB(
                DbName.POSTGRES,
                AR_PENDING_PROCESSING_TABLE_NAME,
                String.format("ucid in ('%s')", client4.getUcid()),
                PendingProcessing.class);
        assertThat(pendingProcessing.size(), is(0));
    }

    @Test
    @AllureId("2049")
    @DisplayName("Batch upload by serverName and acc, create deduction for acc with completed deduction test")
    void accServerNameHasCompletedDeductionTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        fraudstersPage.navigateAbuseRegistryFraudsters();
        fraudstersPage.openUploadDrawer();
        fraudstersPage.typeServerNameAcc(account5.serverName, account5.account.toString());
        fraudstersPage.clickAddFraudButton();
        fraudstersPage.addSelectedFraud(BONUS_ABUSE, CONFIRMED);
        fraudstersPage.clickVindexFraudSource();
        String commentary = "test" + getCurrentTimestampSeconds();
        fraudstersPage.fillCommentary(commentary);
        fraudstersPage.clickApplyUpload();
        fraudstersPage.verifySuccessMessageUpload();

        page.waitForTimeout(2000);

        var deductions = getObjectsFromDB(
                DbName.POSTGRES,
                AR_ABUSER_DEDUCTION_TABLE_NAME,
                String.format(
                        "abuser_history_id not in (%s) and ucid = '%s'",
                        abuserHistory.stream().map(x -> x.getId().toString()).collect(Collectors.joining(", ")),
                        client4.getUcid()),
                AbuserDeduction.class);

        assertThat(deductions.size(), is(1));

        List<PendingProcessing> pendingProcessing = getObjectsFromDB(
                DbName.POSTGRES,
                AR_PENDING_PROCESSING_TABLE_NAME,
                String.format("ucid in ('%s')", client4.getUcid()),
                PendingProcessing.class);
        assertThat(pendingProcessing.size(), is(0));
    }
}
