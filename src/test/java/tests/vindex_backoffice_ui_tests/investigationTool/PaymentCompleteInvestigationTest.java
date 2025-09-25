package tests.vindex_backoffice_ui_tests.investigationTool;

import business_objects.db.abuse_registry_db.Abuser;
import business_objects.db.abuse_registry_db.AbuserFraudType;
import business_objects.db.abuse_registry_db.AbuserHistory;
import business_objects.db.backoffice_db.Investigation;
import business_objects.db.backoffice_db.alert.Alert;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObject;
import business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObject;
import business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObject;
import business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObjectFactory;
import business_objects.db.payment_gate.payment_details.PaymentDetailsObject;
import business_objects.db.payment_gate.payment_details.PaymentDetailsObjectFactory;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import business_objects.db.payment_gate.payment_events.PaymentEventsObjectFactory;
import business_objects.kafka.alerts.PaymentAlertMessage;
import business_objects.kafka.alerts.RuleAlert;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.data.enums.AlertResolution;
import helpers.data.enums.AlertType;
import helpers.database.DbName;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoercedAccountProfitComment;
import static business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObjectFactory.generateMtMt5PositionsObject;
import static business_objects.kafka.alerts.RuleAlertFactory.generatePaymentAlertByUcid;
import static business_objects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.Currency.USD;
import static helpers.data.enums.FraudType.*;
import static helpers.data.enums.FraudTypeStatus.CONFIRMED;
import static helpers.database.ArHelper.deleteUserFromAbuseRegistry;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.BoHelper.getClientsInvestigationsDb;
import static helpers.database.CleanTableHelper.cleanPaymentGateData;
import static helpers.database.DbHelper.*;
import static helpers.database.DbName.POSTGRES;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.getRandomIntPositive;


@Feature("BMS-2051 Complete client investigation (Payment alerts)")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PaymentCompleteInvestigationTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    private static final DecimalFormat formatter = new DecimalFormat("#,##0.##");
    public static final String WD_PATTERN = "1 request for %s USD%s EUR%s%s USD";
    private static final String PAYMENT_METHOD_CODE = "Brazil Bank Transfer";
    public static final double WD_USD = 20_000.12;
    public static final double WD_EUR = 14_654.76;
    private static final String ALERT_WHERE = "client_ucid = '%s' and type='%s' ORDER BY happened_at DESC";
    public static final String PAYLOAD = "{\"id\": \"361564a9-2404-4dd6-aafb-31ce12af19e8\", \"iban\": \"\", \"type\": \"withdrawal\", \"brand\": \"vantage\", \"bankName\": \"testvv\", \"clientId\": \"99887766\", \"platform\": \"WEB\", \"checkName\": \"Little_Amount\", \"eventDate\": \"2025-09-18T06:15:50+03:00\", \"regulator\": \"VFSC2\", \"mt4Account\": 1398842009, \"accountType\": \"MT4\", \"withdrawalId\": \"41000779\", \"schemaVersion\": \"1.0\", \"merchantOrderId\": \"VU856068920250918061547\", \"paymentTypeName\": \"Bank Transfers\", \"withdrawalAmount\": %f, \"paymentMethodCode\": \"%s\", \"paymentChannelCode\": \"642\", \"paymentChannelName\": \"Brazil-CPS\", \"withdrawalCurrency\": \"EUR\", \"withdrawalAmountUSD\": %f, \"bankAccountHolderName\": \"testvv\", \"withdrawalApplicationTime\": \"2025-09-18T06:15:46.379Z\"}".formatted(WD_EUR, PAYMENT_METHOD_CODE, WD_USD);
    private static MtAccountObject mtAccount1;
    private static MtAccountObject mtAccount2;
    private static MtMt4TradesCoercedObject trade1;
    private static MtMt4TradesCoercedObject trade2;
    private static PaymentEventsObject event = PaymentEventsObjectFactory.generatePaymentEventsObject(client);
    private static MtMt4TradesCoercedObject tradeWithdrawal;

    @BeforeAll
    static void setup() {
        objectMapper.findAndRegisterModules();
        CrmTbAccountObject account1 = generateCrmTbAccountDataForUi(client);
        account1.currency = USD.getCode();
        CrmTbAccountObject account2 = generateCrmTbAccountDataForUi(client);
        account2.account = getRandomIntPositive();
        account2.currency = USD.getCode();
        mtAccount1 = generateMtAccountByCrmTbAccount(account1);
        mtAccount2 = generateMtAccountByCrmTbAccount(account2);


        String comment = "comment";
        trade1 = generateMt4TradesCoercedAccountProfitComment(account1, 500.12 + 10_000d, comment);
        trade2 = generateMt4TradesCoercedAccountProfitComment(account2, 1000.23, comment);
        tradeWithdrawal = generateMt4TradesCoercedAccountProfitComment(account1, -10_000d, "withdraw");

        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertObjectsToDb(CRM_TB_ACCOUNT_TABLE_NAME, List.of(account1, account2));
        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccount1, mtAccount2));
        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade1, trade2, tradeWithdrawal));
        MtMt5PositionsObject position = generateMtMt5PositionsObject(client);
        position.setAccount(mtAccount2.account);
        position.setServerId(mtAccount2.sourceIdSt);
        insertObjectToDb(MT5_POSITIONS_TABLE_NAME, position);
    }


    @BeforeEach
    void sendAlert() throws JsonProcessingException {
        PaymentAlertMessage alertPayment = generatePaymentAlertByUcid(crmTbUser.ucid);
        kafka.produceMessage(alertPayment.getId().toString(), objectMapper.writeValueAsString(alertPayment), KAFKA_TOPIC_ALERTS);
        RuleAlert alert = generateRuleAlertByUcid(client.getUcid());
        alert.rule.attributes.account = mtAccount1.account.toString();
        kafka.produceMessage(alert.alertId, objectMapper.writeValueAsString(alert), KAFKA_TOPIC_ALERTS);
        //pgs
        event.setPaymentId(UUID.fromString(alertPayment.getPaymentEventId()));
        insertObjectToDb(POSTGRES, PAYMENT_EVENT_TABLE_NAME, event);

        PaymentDetailsObject details = PaymentDetailsObjectFactory.generatePaymentDetailsObject(client);
        details.setType("withdrawal");
        details.setPayload(PAYLOAD);
        details.setPaymentId(event.getPaymentId());
        insertObjectToDb(POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, details);
        PaymentDecisionsObject decision = PaymentDecisionsObjectFactory.generatePaymentDecisionObject(event);
        decision.setDecisionType("payment");
        decision.setDecisionCode(0);
        insertObjectToDb(POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, decision);
    }

    @AfterEach
    void teardownInPGS() throws Exception {
        cleanPaymentGateData(client.getUcid(), client.getUserId(), event.getPaymentId().toString());
        deleteUserFromAbuseRegistry(client.getUcid());
        closeAlert(crmTbUser.ucid);
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteEntryFromDb(MT4_TRADES_COERCED_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteEntryFromDb(MT5_POSITIONS_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteUserFromAbuseRegistry(client.getUcid());
        closeAlert(crmTbUser.ucid);
    }

    @Order(1)
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1610")
    @DisplayName("Verify complete investigation with withdrawals for payment team and investigation for trading is not closed")
    void completeInvestigationFlowTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToMain();
        investigationPage.waitForPageToLoad();
        investigationPage.clickSelectPaymentInvestigationType();
        investigationPage.waitForPageToLoad();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openResolveSuspicious();
        List<String> withdrawalList = resolvePage.getWithdrawalList();
        assertThat("Verify that withdrawal list is not empty", withdrawalList.size(), is(1));
        assertThat("Verify that withdrawal list contains withdrawal", withdrawalList.getFirst(), containsString(String.format(WD_PATTERN, formatter.format(WD_USD), formatter.format(WD_EUR), PAYMENT_METHOD_CODE, formatter.format(WD_USD))));
        resolvePage.clickWithdrawalApprove();
        resolvePage.addFraud(MARKET_MANIPULATION, CONFIRMED);
        resolvePage.resolveNoActions("test comment");

        //check investigation in db
        List<Investigation> clientsInvestigationsDb = getClientsInvestigationsDb(client.getUcid(), AlertType.PAYMENT);
        assertThat("Verify that investigation is completed", clientsInvestigationsDb.size(), is(1));
        assertThat("Verify that investigation is completed", clientsInvestigationsDb.getFirst().getStatus(), is("COMPLETED"));

        //check trading investigation is not closed
        List<Investigation> clientsInvestigationsTrading = getClientsInvestigationsDb(client.getUcid(), AlertType.TRADING);
        assertThat("Verify that trading investigation is completed", clientsInvestigationsTrading.size(), is(1));
        assertThat("Verify that trading investigation is not completed", clientsInvestigationsTrading.getFirst().getStatus(), not("COMPLETED"));


        //PGS decision check in db
        List<PaymentDecisionsObject> decisions = getObjectsFromDB(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, String.format("payment_id = '%s'", event.getPaymentId().toString()), PaymentDecisionsObject.class);
        assertThat("Verify that decision is saved in db", decisions.size(), is(1));
        PaymentDecisionsObject actualDecision = decisions.getFirst();
        assertThat("Verify that decision is approved", actualDecision.getDecisionCode(), is(1));

        //Alert resolution
        Alert dbAlert = getObjectsFromDB(DbName.POSTGRES, BO_ALERT_TABLE_NAME, String.format(ALERT_WHERE, client.getUcid(), AlertType.PAYMENT), Alert.class).getFirst();
        assertThat("Verify alert_resolution is CONFIRMED", dbAlert.getAlertResolution(), is(AlertResolution.CONFIRMED.getDisplayName()));


    }

    @Order(2)
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1611")
    @DisplayName("Verify complete investigation with reject withdrawals for payment team")
    void completeInvestigationRejectTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsPaymentTeamUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openResolveSuspicious();
        List<String> withdrawalList = resolvePage.getWithdrawalList();
        assertThat("Verify that withdrawal list is not empty", withdrawalList.size(), is(1));
        assertThat("Verify that withdrawal list contains withdrawal", withdrawalList.getFirst(), containsString(String.format(WD_PATTERN, formatter.format(WD_USD), formatter.format(WD_EUR), PAYMENT_METHOD_CODE, formatter.format(WD_USD))));
        resolvePage.clickWithdrawalReject();
        resolvePage.addFraud(MARKET_MANIPULATION, CONFIRMED);
        resolvePage.resolveNoActions("test comment");

        //PGS decision check in db
        List<PaymentDecisionsObject> decisions = getObjectsFromDB(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, String.format("payment_id = '%s'", event.getPaymentId().toString()), PaymentDecisionsObject.class);
        assertThat("Verify that decision is saved in db", decisions.size(), is(1));
        PaymentDecisionsObject actualDecision = decisions.getFirst();
        assertThat("Verify that decision is rejected", actualDecision.getDecisionCode(), is(2));
        assertThat("Verify that rejectionCode is 0", actualDecision.getRejectionCode(), is(0));

        //Alert resolution
        Alert dbAlert = getObjectsFromDB(DbName.POSTGRES, BO_ALERT_TABLE_NAME, String.format(ALERT_WHERE, client.getUcid(), AlertType.PAYMENT), Alert.class).getFirst();
        assertThat("Verify alert_resolution is CONFIRMED", dbAlert.getAlertResolution(), is(AlertResolution.CONFIRMED.getDisplayName()));

    }


    @Order(3)
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1612")
    @DisplayName("Verify alert resolution FALSE_POSITIVE on approve and no fraud for payment team")
    void alertResolutionTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsPaymentTeamUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openResolveSuspicious();
        List<String> withdrawalList = resolvePage.getWithdrawalList();
        assertThat("Verify that withdrawal list is not empty", withdrawalList.size(), is(1));
        assertThat("Verify that withdrawal list contains withdrawal", withdrawalList.getFirst(), containsString(String.format(WD_PATTERN, formatter.format(WD_USD), formatter.format(WD_EUR), PAYMENT_METHOD_CODE, formatter.format(WD_USD))));
        resolvePage.clickWithdrawalApprove();
        resolvePage.resolveNoActions("test comment");
        //Alert resolution
        Alert dbAlert = getObjectsFromDB(DbName.POSTGRES, BO_ALERT_TABLE_NAME, String.format(ALERT_WHERE, client.getUcid(), AlertType.PAYMENT), Alert.class).getFirst();
        assertThat("Verify alert_resolution is FALSE_POSITIVE", dbAlert.getAlertResolution(), is(AlertResolution.FALSE_POSITIVE.getDisplayName()));

    }

    @Order(4)
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1613")
    @DisplayName("Verify alert resolution FALSE_POSITIVE withdrawals on approve and different fraud for payment team")
    void alertResolutionTest2() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsPaymentTeamUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openResolveSuspicious();
        List<String> withdrawalList = resolvePage.getWithdrawalList();
        assertThat("Verify that withdrawal list is not empty", withdrawalList.size(), is(1));
        assertThat("Verify that withdrawal list contains withdrawal", withdrawalList.getFirst(), containsString(String.format(WD_PATTERN, formatter.format(WD_USD), formatter.format(WD_EUR), PAYMENT_METHOD_CODE, formatter.format(WD_USD))));
        resolvePage.clickWithdrawalApprove();
        resolvePage.addFraud(CPA_ABUSE, CONFIRMED);
        resolvePage.resolveNoActions("test comment");
        //Alert resolution
        Alert dbAlert = getObjectsFromDB(DbName.POSTGRES, BO_ALERT_TABLE_NAME, String.format(ALERT_WHERE, client.getUcid(), AlertType.PAYMENT), Alert.class).getFirst();
        assertThat("Verify alert_resolution is FALSE_POSITIVE", dbAlert.getAlertResolution(), is(AlertResolution.FALSE_POSITIVE.getDisplayName()));

    }


    @Order(5)
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1614")
    @DisplayName("Verify alert resolution FALSE_POSITIVE withdrawals on approve and already has different fraud for payment team")
    void alertResolutionTest3() throws Exception {
        var nowTimestamp = Timestamp.from(Instant.now());
        insertObjectToDb(DbName.POSTGRES, AR_ABUSER_TABLE_NAME, new Abuser(client.getUcid(), CONFIRMED.getStatus(), "auto-test comment", "AUTOTEST USER", "Vindex BO", nowTimestamp, nowTimestamp, true));
        insertObjectToDb(DbName.POSTGRES, AR_ABUSER_HISTORY_TABLE_NAME, new AbuserHistory(null, client.getUcid(), "CLIENT_STATUS", CONFIRMED.getStatus(), "CLIENT_STATUS", "auto-test comment", "AUTOTEST USER", "Vindex BO", nowTimestamp, null, null));
        insertObjectToDb(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, new AbuserFraudType(client.getUcid(), HEDGING.getCode(), CONFIRMED.getStatus(), "auto-test comment", "AUTOTEST USER", "Vindex BO", nowTimestamp, nowTimestamp, "INTERNAL", null, "Vindex"));
        insertObjectToDb(DbName.POSTGRES, AR_ABUSER_HISTORY_TABLE_NAME, new AbuserHistory(null, client.getUcid(), HEDGING.getCode(), CONFIRMED.getStatus(), "FRAUD_TYPE_STATUS", "auto-test comment", "AUTOTEST USER", "Vindex BO", nowTimestamp, "INTERNAL", null));
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsPaymentTeamUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openResolveSuspicious();
        List<String> withdrawalList = resolvePage.getWithdrawalList();
        assertThat("Verify that withdrawal list is not empty", withdrawalList.size(), is(1));
        assertThat("Verify that withdrawal list contains withdrawal", withdrawalList.getFirst(), containsString(String.format(WD_PATTERN, formatter.format(WD_USD), formatter.format(WD_EUR), PAYMENT_METHOD_CODE, formatter.format(WD_USD))));
        resolvePage.clickWithdrawalApprove();
        resolvePage.resolveNoActions("test comment");
        //Alert resolution
        Alert dbAlert = getObjectsFromDB(DbName.POSTGRES, BO_ALERT_TABLE_NAME, String.format(ALERT_WHERE, client.getUcid(), AlertType.PAYMENT), Alert.class).getFirst();
        assertThat("Verify alert_resolution is FALSE_POSITIVE", dbAlert.getAlertResolution(), is(AlertResolution.FALSE_POSITIVE.getDisplayName()));

    }

    @Order(6)
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1615")
    @DisplayName("Verify alert resolution CONFIRMED withdrawals on reject and already has different fraud for payment team")
    void alertResolutionTest4() throws Exception {
        var nowTimestamp = Timestamp.from(Instant.now());
        insertObjectToDb(DbName.POSTGRES, AR_ABUSER_TABLE_NAME, new Abuser(client.getUcid(), CONFIRMED.getStatus(), "auto-test comment", "AUTOTEST USER", "Vindex BO", nowTimestamp, nowTimestamp, true));
        insertObjectToDb(DbName.POSTGRES, AR_ABUSER_HISTORY_TABLE_NAME, new AbuserHistory(null, client.getUcid(), "CLIENT_STATUS", CONFIRMED.getStatus(), "CLIENT_STATUS", "auto-test comment", "AUTOTEST USER", "Vindex BO", nowTimestamp, null, null));
        insertObjectToDb(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, new AbuserFraudType(client.getUcid(), HEDGING.getCode(), CONFIRMED.getStatus(), "auto-test comment", "AUTOTEST USER", "Vindex BO", nowTimestamp, nowTimestamp, "INTERNAL", null, "Vindex"));
        insertObjectToDb(DbName.POSTGRES, AR_ABUSER_HISTORY_TABLE_NAME, new AbuserHistory(null, client.getUcid(), HEDGING.getCode(), CONFIRMED.getStatus(), "FRAUD_TYPE_STATUS", "auto-test comment", "AUTOTEST USER", "Vindex BO", nowTimestamp, "INTERNAL", null));
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsPaymentTeamUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openResolveSuspicious();
        List<String> withdrawalList = resolvePage.getWithdrawalList();
        assertThat("Verify that withdrawal list is not empty", withdrawalList.size(), is(1));
        assertThat("Verify that withdrawal list contains withdrawal", withdrawalList.getFirst(), containsString(String.format(WD_PATTERN, formatter.format(WD_USD), formatter.format(WD_EUR), PAYMENT_METHOD_CODE, formatter.format(WD_USD))));
        resolvePage.clickWithdrawalReject();
        resolvePage.resolveNoActions("test comment");
        //Alert resolution
        Alert dbAlert = getObjectsFromDB(DbName.POSTGRES, BO_ALERT_TABLE_NAME, String.format(ALERT_WHERE, client.getUcid(), AlertType.PAYMENT), Alert.class).getFirst();
        assertThat("Verify alert_resolution is CONFIRMED", dbAlert.getAlertResolution(), is(AlertResolution.CONFIRMED.getDisplayName()));

    }

    @Order(7)
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1616")
    @DisplayName("Verify alert resolution CONFIRMED withdrawals on reject with different fraud for payment team")
    void alertResolutionTest5() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsPaymentTeamUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openResolveSuspicious();
        List<String> withdrawalList = resolvePage.getWithdrawalList();
        assertThat("Verify that withdrawal list is not empty", withdrawalList.size(), is(1));
        assertThat("Verify that withdrawal list contains withdrawal", withdrawalList.getFirst(), containsString(String.format(WD_PATTERN, formatter.format(WD_USD), formatter.format(WD_EUR), PAYMENT_METHOD_CODE, formatter.format(WD_USD))));
        resolvePage.clickWithdrawalReject();
        resolvePage.addFraud(CPA_ABUSE, CONFIRMED);
        resolvePage.resolveNoActions("test comment");
        //Alert resolution
        Alert dbAlert = getObjectsFromDB(DbName.POSTGRES, BO_ALERT_TABLE_NAME, String.format(ALERT_WHERE, client.getUcid(), AlertType.PAYMENT), Alert.class).getFirst();
        assertThat("Verify alert_resolution is CONFIRMED", dbAlert.getAlertResolution(), is(AlertResolution.CONFIRMED.getDisplayName()));

    }

    @Order(8)
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1617")
    @DisplayName("Verify alert resolution CONFIRMED withdrawals on reject with no fraud for payment team")
    void alertResolutionTest6() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsPaymentTeamUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openResolveSuspicious();
        List<String> withdrawalList = resolvePage.getWithdrawalList();
        assertThat("Verify that withdrawal list is not empty", withdrawalList.size(), is(1));
        assertThat("Verify that withdrawal list contains withdrawal", withdrawalList.getFirst(), containsString(String.format(WD_PATTERN, formatter.format(WD_USD), formatter.format(WD_EUR), PAYMENT_METHOD_CODE, formatter.format(WD_USD))));
        resolvePage.clickWithdrawalReject();
        resolvePage.resolveNoActions("test comment");
        //Alert resolution
        Alert dbAlert = getObjectsFromDB(DbName.POSTGRES, BO_ALERT_TABLE_NAME, String.format(ALERT_WHERE, client.getUcid(), AlertType.PAYMENT), Alert.class).getFirst();
        assertThat("Verify alert_resolution is CONFIRMED", dbAlert.getAlertResolution(), is(AlertResolution.CONFIRMED.getDisplayName()));

    }


}
