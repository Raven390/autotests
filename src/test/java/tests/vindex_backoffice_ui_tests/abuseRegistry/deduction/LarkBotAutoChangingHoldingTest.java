package tests.vindex_backoffice_ui_tests.abuseRegistry.deduction;

import business_objects.api.lark.TenantAccessToken.TenantAccessTokenResponse;
import business_objects.api.lark.chatHistory.ByBitRestrictionBotMessage;
import business_objects.api.lark.chatHistory.ChatHistoryResponse;
import business_objects.db.abuse_registry_db.AbuserDeduction;
import business_objects.db.abuse_registry_db.AbuserHistory;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObject;
import business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObjectFactory;
import business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObject;
import business_objects.kafka.deductions.AccountDeductionRequestResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.database.ArHelper;
import helpers.database.DbName;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static business_objects.api.lark.LarkRequest.getMessagesChatLast10Minutes;
import static business_objects.api.lark.LarkRequest.getTenantToken;
import static business_objects.db.abuse_registry_db.AbuserDeductionFactory.generateAbuserDeductionByAccount;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObjectFactory.generateMtMt5PositionsObject;
import static business_objects.ui.user.UserFactory.autotestUserOne;
import static helpers.api.AbuseRegistryHelper.addFraudForClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
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
import static helpers.database.ArHelper.deleteUserFromAbuseRegistry;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.Constants.*;
import static utils.Utils.insertCrmAccountsToDb;


@Feature("BMS-2169 Notifications on auto-deductions after holding to Lark")
class LarkBotAutoChangingHoldingTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    private static final CrmTbAccountObject account = generateCrmTbAccountDataForUi(client);
    private static final ClientHelper client2 = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser2 = generateUserByClient(client2);
    private static final CrmTbAccountObject account2 = generateCrmTbAccountDataForUi(client2);
    private static AbuserDeduction holdingDeduction1;
    private static AbuserDeduction holdingDeduction2;
    private static List<AbuserHistory> abuserHistory;
    private static List<AbuserHistory> abuserHistory2;
    private static MtMt4TradesCoercedObject coercedObject;
    private static MtMt4TradesCoercedObject coercedObject2;

    @BeforeAll
    static void setup() throws Exception {
        account.currency = USD.getCode();
        MtAccountObject mtAccount = generateMtAccountByCrmTbAccount(account);
        coercedObject = MtMt4TradesCoercedObjectFactory.generateMt4TradesCoercedBalance(client, 500.12, "Initial balance");
        insertObjectToDb(MT4_TRADES_COERCED_TABLE_NAME, coercedObject);
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertCrmAccountsToDb(account);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, mtAccount);
        MtMt5PositionsObject position1 = generateMtMt5PositionsObject(client);
        insertObjectToDb(MT5_POSITIONS_TABLE_NAME, position1);
        Thread.sleep(2000);
        addFraudForClient(client, HEDGING, INTERNAL, CONFIRMED, List.of("EURUSD", "GBPUSD"));
        abuserHistory = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_HISTORY_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()), AbuserHistory.class);
        holdingDeduction1 = generateAbuserDeductionByAccount(account, abuserHistory.getLast().getId());
        holdingDeduction1.setStatusOpenPositions(HOLDING.getDisplayName());
        holdingDeduction1.setStatusEmail(SENT.getDisplayName());
        holdingDeduction1.setStatusDeduction(TO_BE_DEDUCTED.getDisplayName());
        holdingDeduction1.setStatusApproval(APPROVED.getDisplayName());
        holdingDeduction1.setIllegalProfit(coercedObject.getProfit());
        holdingDeduction1.setIllegalProfitUsd(coercedObject.getProfitUsd());
        holdingDeduction1.setSuggestedDeduction(0d);
        holdingDeduction1.setSuggestedDeductionUsd(0d);
        holdingDeduction1.setActualDeduction(null);
        holdingDeduction1.setActualDeductionUsd(null);
        holdingDeduction1.setBalanceAtResolution(coercedObject.getProfit());
        holdingDeduction1.setBalanceAtResolutionUsd(coercedObject.getProfitUsd());
        holdingDeduction1.setDeductionType("FULL_DEDUCTION");
        insertObjectToDb(DbName.POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, holdingDeduction1);

        account.currency = USD.getCode();
        MtAccountObject mtAccount2 = generateMtAccountByCrmTbAccount(account2);
        coercedObject2 = MtMt4TradesCoercedObjectFactory.generateMt4TradesCoercedBalance(client2, 500.12, "Initial balance");
        insertObjectToDb(MT4_TRADES_COERCED_TABLE_NAME, coercedObject2);
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser2);
        insertCrmAccountsToDb(account2);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, mtAccount2);
        MtMt5PositionsObject position2 = generateMtMt5PositionsObject(client2);
        insertObjectToDb(MT5_POSITIONS_TABLE_NAME, position2);
        Thread.sleep(2000);
        addFraudForClient(client2, HEDGING, INTERNAL, CONFIRMED, List.of("EURUSD", "GBPUSD"));
        abuserHistory2 = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_HISTORY_TABLE_NAME, String.format("ucid = '%s'", client2.getUcid()), AbuserHistory.class);
        holdingDeduction2 = generateAbuserDeductionByAccount(account2, abuserHistory2.getLast().getId());
        holdingDeduction2.setStatusOpenPositions(HOLDING.getDisplayName());
        holdingDeduction2.setStatusEmail(SENT.getDisplayName());
        holdingDeduction2.setStatusDeduction(TO_BE_DEDUCTED.getDisplayName());
        holdingDeduction2.setStatusApproval(APPROVED.getDisplayName());
        holdingDeduction2.setIllegalProfit(coercedObject2.getProfit());
        holdingDeduction2.setIllegalProfitUsd(coercedObject2.getProfitUsd());
        holdingDeduction2.setSuggestedDeduction(0d);
        holdingDeduction2.setSuggestedDeductionUsd(0d);
        holdingDeduction2.setActualDeduction(null);
        holdingDeduction2.setActualDeductionUsd(null);
        holdingDeduction2.setBalanceAtResolution(coercedObject2.getProfit());
        holdingDeduction2.setBalanceAtResolutionUsd(coercedObject2.getProfitUsd());
        holdingDeduction2.setDeductionType("FULL_DEDUCTION");
        insertObjectToDb(DbName.POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, holdingDeduction2);
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteUserFromAbuseRegistry(client.getUcid());
        deleteEntryFromDb(MT5_POSITIONS_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteEntryFromDb(MT4_TRADES_COERCED_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteUserFromAbuseRegistry(client2.getUcid());
        deleteEntryFromDb(MT5_POSITIONS_TABLE_NAME, String.format("ucid = '%s'", client2.getUcid()));
        deleteEntryFromDb(MT4_TRADES_COERCED_TABLE_NAME, String.format("ucid = '%s'", client2.getUcid()));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @Tag(ABUSE_REGISTRY)
    @AllureId("1441")
    @DisplayName("Holding scheduler test send_to_lark=true and message send to lark on AccountDeductionRequestResponse")
    void holdingSchedulerSendToLarkTest() throws Exception {
        executeQueryToDb(DbName.CLICKHOUSE, String.format("UPDATE %s SET is_deleted = 1 WHERE account = %s", MT5_POSITIONS_TABLE_NAME, account.account));
        // Verify the deduction after positions are closed
        List<AbuserDeduction> deductionList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            deductionList = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, String.format("account = '%s'", account.account), AbuserDeduction.class);
            if (!Objects.equals(deductionList.getFirst().getStatusOpenPositions(), WAS_HOLDING.getDisplayName())) {
                Thread.sleep(2000);
                if (i == 9) {
                    assertThat("The holding scheduler did not recalculate deductions in 15 sec", deductionList.getFirst().getStatusOpenPositions(), is(WAS_HOLDING.getDisplayName()));
                }
            } else {
                break;
            }
        }
        AbuserDeduction wasHoldingDeduction = deductionList.getFirst();
        AbuserDeduction expectedDeduction = new AbuserDeduction(client.getUcid(), holdingDeduction1.getAbuserHistoryId(), account.account.toString(), account.serverIdSt, account.serverName, account.currency, client.getBrand(), WAS_HOLDING.getDisplayName(), SENT.getDisplayName(), TO_BE_DEDUCTED.getDisplayName(), APPROVED.getDisplayName(), holdingDeduction1.getComment(), coercedObject.getProfit(), null, coercedObject.getProfit(), null, null, null, null, null, String.format("%s %s", autotestUserOne().getFirstName(), autotestUserOne().getLastName()), VINDEX_BO_SYSTEM, ILLEGAL_PROFIT.getDisplayName(), null, holdingDeduction1.getCommentDeduction(), coercedObject.getProfit(), null, client.getUserId().toString(), coercedObject.getProfit(), null, true, FULL_DEDUCTION.getDisplayName());
// Check important fields individually
        assertThat("Verify account", wasHoldingDeduction.getAccount(), is(expectedDeduction.getAccount()));
        assertThat("Verify ucid", wasHoldingDeduction.getUcid(), is(expectedDeduction.getUcid()));
        assertThat("Verify statusOpenPositions", wasHoldingDeduction.getStatusOpenPositions(), is(WAS_HOLDING.getDisplayName()));
        assertThat("Verify statusDeduction", wasHoldingDeduction.getStatusDeduction(), anyOf(
                is(TO_BE_DEDUCTED.getDisplayName()), is(PROCESSING.getDisplayName()), is(FAILED.getDisplayName())
        ));
        assertThat("Verify statusApproval", wasHoldingDeduction.getStatusApproval(), is(APPROVED.getDisplayName()));
        assertThat("Verify illegalProfit", wasHoldingDeduction.getIllegalProfit(), is(coercedObject.getProfit()));
        assertThat("Verify balanceAtResolution", wasHoldingDeduction.getBalanceAtResolution(), is(coercedObject.getProfit()));
        // Verify the deduction after deduction is picked up for sending to kafka
        for (int i = 0; i < 7; i++) {
            deductionList = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, String.format("account = '%s'", coercedObject.getAccount()), AbuserDeduction.class);
            if (!Objects.equals(deductionList.getFirst().getStatusDeduction(), FAILED.getDisplayName())) {
                Thread.sleep(2000);
                if (i == 6) {
                    assertThat("The processing scheduler did not recalculate deductions in 10 sec", deductionList.getFirst().getStatusOpenPositions(), is(WAS_HOLDING.getDisplayName()));
                }
            } else {
                break;
            }
        }
//delete kafka response
        String kafkaRequestId = ArHelper.deleteDeductionsKafkaResponse(client.getUcid()).getFirst();

        executeQueryToDb(DbName.POSTGRES, String.format("UPDATE %s SET status_deduction = '%s' WHERE account = '%s'", AR_ABUSER_DEDUCTION_TABLE_NAME, PROCESSING.getDisplayName(), account.account));


        // Produce AccountDeductionRequestResponse for holdingDeduction2

        AccountDeductionRequestResponse response = new AccountDeductionRequestResponse(
                Instant.now().toString(), kafkaRequestId, "Success", ""
        );
        kafka.produceMessage(null, objectMapper.writeValueAsString(response), KAFKA_TOPIC_ACCOUNT_DEDUCTION_REQUEST_RESPONSE);
        AbuserDeduction finalizedDeduction = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, String.format("account = '%s'", account.account), AbuserDeduction.class).getFirst();
        //check that message is delivered

        Response tenant = getTenantToken("cli_a829a3882cb8902f", "x3Tu9aG8DBY8XOQXc0WZneu8lQdauXR2");
        String token = objectMapper.readValue(tenant.body().string(), TenantAccessTokenResponse.class).getTenantAccessToken();
        Response messageHistory = getMessagesChatLast10Minutes(token, "oc_be10822c2c8879ccb8ed8e6b40f0326d");
        ChatHistoryResponse responseLark = objectMapper.readValue(messageHistory.body().string(), ChatHistoryResponse.class);
        List<ChatHistoryResponse.LarkApiDataItem> items = responseLark.getData().getItems();
        List<ChatHistoryResponse.LarkApiDataItem> itemsFiltered = items.stream().filter(i -> i.getBody().getContent().contains(client.getUserId().toString())).toList();
        String clearedContent = itemsFiltered.getFirst().getBody().getContent().toString().replace("\\n", "").replace("\\", "");
        ByBitRestrictionBotMessage message = objectMapper.readValue(clearedContent, ByBitRestrictionBotMessage.class);
        Allure.step("check that message contains accountId");
        assertEquals(": " + client.getTradingAccount(), message.getElements().getFirst().get(1).getText());
        Allure.step("check that message contains Server name");
        assertEquals(": " + account.serverName, message.getElements().getFirst().get(3).getText());
        Allure.step("check that message contains Client id");
        assertEquals(": " + client.getUserId(), message.getElements().getFirst().get(5).getText());
        Allure.step("check that message contains Client id");
        assertEquals(": " + client.getBrand(), message.getElements().getFirst().get(7).getText());
        Allure.step("check that message contains Client id");
        assertEquals(": " + finalizedDeduction.getActualDeduction() + " " + account.currency, message.getElements().getFirst().get(9).getText());

    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @Tag(ABUSE_REGISTRY)
    @AllureId("1442")
    @DisplayName("Holding scheduler test send_to_lark=false")
    void holdingSchedulerNoSendToLarkTest() throws Exception {
        executeQueryToDb(DbName.CLICKHOUSE, String.format("UPDATE %s SET profit=5000 WHERE account = %s", MT4_TRADES_COERCED_TABLE_NAME, account2.account));
        coercedObject2.setProfit(5000.0);
        executeQueryToDb(DbName.CLICKHOUSE, String.format("UPDATE %s SET is_deleted = 1 WHERE account = %s", MT5_POSITIONS_TABLE_NAME, account2.account));
        // Verify the deduction after positions are closed
        List<AbuserDeduction> deductionList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            deductionList = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, String.format("account = '%s'", account2.account), AbuserDeduction.class);
            if (!Objects.equals(deductionList.getFirst().getStatusOpenPositions(), WAS_HOLDING.getDisplayName())) {
                Thread.sleep(2000);
                if (i == 9) {
                    assertThat("The holding scheduler did not recalculate deductions in 15 sec", deductionList.getFirst().getStatusOpenPositions(), is(WAS_HOLDING.getDisplayName()));
                }
            } else {
                break;
            }
        }
        AbuserDeduction wasHoldingDeduction = deductionList.getFirst();
        AbuserDeduction expectedDeduction = new AbuserDeduction(client2.getUcid(), holdingDeduction2.getAbuserHistoryId(), account2.account.toString(), account2.serverIdSt, account2.serverName, account2.currency, client2.getBrand(), WAS_HOLDING.getDisplayName(), NOT_SENT.getDisplayName(), TO_BE_DEDUCTED.getDisplayName(), AWAITING_APPROVAL.getDisplayName(), holdingDeduction2.getComment(), coercedObject2.getProfit(), null, coercedObject2.getProfit(), null, null, null, null, null, String.format("%s %s", autotestUserOne().getFirstName(), autotestUserOne().getLastName()), VINDEX_BO_SYSTEM, ILLEGAL_PROFIT.getDisplayName(), null, holdingDeduction2.getCommentDeduction(), coercedObject2.getProfit(), null, client2.getUserId().toString(), 500.12, null, false, FULL_DEDUCTION.getDisplayName());

        assertThat("Verify deduction in abuser_deduction table is as expected", wasHoldingDeduction, is(expectedDeduction));

    }
}
