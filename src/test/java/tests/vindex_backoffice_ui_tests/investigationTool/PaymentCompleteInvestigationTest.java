package tests.vindex_backoffice_ui_tests.investigationTool;

import business_objects.db.abuse_registry_db.Abuser;
import business_objects.db.abuse_registry_db.AbuserFraudType;
import business_objects.db.abuse_registry_db.AbuserHistory;
import business_objects.db.audit_service_db.AuditEvent;
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
import business_objects.kafka.alerts.*;
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
import java.time.Instant;
import java.util.List;
import java.util.Map;
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
    private static final double WD_USD = 20_000.12;
    private static final double WD_EUR = 14_654.76;
    private static final String PAYMENT_METHOD_CODE = "Brazil Bank Transfer";
    private static final String ALERT_WHERE = "client_ucid = '%s' and type='%s' ORDER BY happened_at DESC";

    private static final java.text.DecimalFormat formatter = new java.text.DecimalFormat("#,##0.00", java.text.DecimalFormatSymbols.getInstance(java.util.Locale.US));

    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    public static final String REASON_NO_PARAMS = "Trading account inactive";
    public static final String REASON_WITH_PARAMS = "Use recommended method";

    private static MtAccountObject mtAccount1;
    private static MtAccountObject mtAccount2;
    private static MtMt4TradesCoercedObject trade1;
    private static MtMt4TradesCoercedObject trade2;
    private static PaymentEventsObject event = PaymentEventsObjectFactory.generatePaymentEventsObject(client);
    private static MtMt4TradesCoercedObject tradeWithdrawal;

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String buildPayload(double WD_EUR, String PAYMENT_METHOD_CODE, double WD_USD) throws Exception {
        Map<String, Object> payload = Map.ofEntries(
                Map.entry("id", "361564a9-2404-4dd6-aafb-31ce12af19e8"), Map.entry("iban", ""), Map.entry("type", "withdrawal"), Map.entry("brand", "vantage"), Map.entry("bankName", "testvv"), Map.entry("clientId", "99887766"), Map.entry("platform", "WEB"), Map.entry("checkName", "Little_Amount"), Map.entry("eventDate", "2025-09-18T06:15:50+03:00"), Map.entry("regulator", "VFSC2"), Map.entry("mt4Account", 1_398_842_009), Map.entry("accountType", "MT4"), Map.entry("withdrawalId", "41000779"), Map.entry("schemaVersion", "1.0"), Map.entry("merchantOrderId", "VU856068920250918061547"), Map.entry("paymentTypeName", "Bank Transfers"), Map.entry("withdrawalAmount", WD_EUR), Map.entry("paymentMethodCode", PAYMENT_METHOD_CODE), Map.entry("paymentChannelCode", "642"), Map.entry("paymentChannelName", "Brazil-CPS"), Map.entry("withdrawalCurrency", "EUR"), Map.entry("withdrawalAmountUSD", WD_USD), Map.entry("bankAccountHolderName", "testvv"), Map.entry("withdrawalApplicationTime", "2025-09-18T06:15:46.379Z")
        );

        return objectMapper.writeValueAsString(payload);
    }

    @BeforeAll
    static void setup() throws JsonProcessingException {
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
    void sendAlert() throws Exception {
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
        String payload = buildPayload(WD_EUR, PAYMENT_METHOD_CODE, WD_USD);
        details.setPayload(payload);
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
        String longResolveComment = "I will not waste chalk. I will not waste chalk. I will not waste chalk. I will not waste chalk. I will not waste chalk. I will not waste chalk. I will not waste chalk. I will not waste chalk. I will not waste chalk. I will not waste chalk. I will not waste chalk. I will not waste chalk. I will not waste chalk. I will not waste chalk. I will not waste chalk. I will not waste chalk. I will not waste chalk. I will not waste chalk. I will not waste chalk. I will not waste chalk. I will not waste chalk.";
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
        assertWithdrawalText(withdrawalList.getFirst());
        resolvePage.clickWithdrawalApprove();
        resolvePage.addFraud();
        resolvePage.resolveNoActionsPayment(longResolveComment);

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
        assertThat("Verify alert_resolution is FALSE_POSITIVE", dbAlert.getAlertResolution(), is(AlertResolution.FALSE_POSITIVE.getDisplayName()));
        AuditEvent audit = getObjectsFromDB(DbName.POSTGRES, AUDIT_EVENT_TABLE, String.format("ucid = '%s' AND type = '%s'", client.getUcid(), COMMENT_ADDED_TYPE), AuditEvent.class).getFirst();
        assertThat("Verify comment was saved until 500th character", audit.getComment(), is(longResolveComment.substring(0, 500)));
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
        assertWithdrawalText(withdrawalList.getFirst());
        resolvePage.clickWithdrawalReject(REASON_NO_PARAMS);
        resolvePage.addFraud(CHARGEBACK, CONFIRMED);
        resolvePage.resolveNoActionsPayment("test comment");

        //PGS decision check in db
        List<PaymentDecisionsObject> decisions = getObjectsFromDB(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, String.format("payment_id = '%s'", event.getPaymentId().toString()), PaymentDecisionsObject.class);
        assertThat("Verify that decision is saved in db", decisions.size(), is(1));
        PaymentDecisionsObject actualDecision = decisions.getFirst();
        assertThat("Verify that decision is rejected", actualDecision.getDecisionCode(), is(2));
        assertThat("Verify that rejectionCode is not default 0 (reason code should be provided)", actualDecision.getRejectionCode(), greaterThan(0));
        assertThat("Verify that actor is Vindex BO", actualDecision.getActor(), is("Vindex BO"));

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
        assertWithdrawalText(withdrawalList.getFirst());
        resolvePage.clickWithdrawalApprove();
        resolvePage.resolveNoActionsPayment("test comment");
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
        assertWithdrawalText(withdrawalList.getFirst());
        resolvePage.clickWithdrawalApprove();
        resolvePage.addFraud(CHARGEBACK, CONFIRMED);
        resolvePage.resolveNoActionsPayment("test comment");
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
        assertWithdrawalText(withdrawalList.getFirst());
        resolvePage.clickWithdrawalApprove();
        resolvePage.resolveNoActionsPayment("test comment");
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
        assertWithdrawalText(withdrawalList.getFirst());
        resolvePage.clickWithdrawalReject(REASON_NO_PARAMS);
        resolvePage.resolveNoActionsPayment("test comment");
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
        assertWithdrawalText(withdrawalList.getFirst());
        resolvePage.clickWithdrawalReject(REASON_NO_PARAMS);
        resolvePage.addFraud(CHARGEBACK, CONFIRMED);
        resolvePage.resolveNoActionsPayment("test comment");
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
        assertWithdrawalText(withdrawalList.getFirst());
        resolvePage.clickWithdrawalReject(REASON_NO_PARAMS);
        resolvePage.resolveNoActionsPayment("test comment");
        //Alert resolution
        Alert dbAlert = getObjectsFromDB(DbName.POSTGRES, BO_ALERT_TABLE_NAME, String.format(ALERT_WHERE, client.getUcid(), AlertType.PAYMENT), Alert.class).getFirst();
        assertThat("Verify alert_resolution is CONFIRMED", dbAlert.getAlertResolution(), is(AlertResolution.CONFIRMED.getDisplayName()));
    }

    @Order(9)
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1766")
    @DisplayName("Verify complete investigation with reject withdrawals for payment team with dynamic values in Rejection Reasons")
    void completeInvestigationRejectWithDynamicValueTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsPaymentTeamUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openResolveSuspicious();
        List<String> withdrawalList = resolvePage.getWithdrawalList();
        assertThat("Verify that withdrawal list is not empty", withdrawalList.size(), is(1));
        assertWithdrawalText(withdrawalList.getFirst());
        resolvePage.resolveWithdrawalsAllRejectPayment(REASON_WITH_PARAMS, "AQA");

        //PGS decision check in db
        List<PaymentDecisionsObject> decisions = getObjectsFromDB(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, String.format("payment_id = '%s'", event.getPaymentId().toString()), PaymentDecisionsObject.class);
        assertThat("Verify that decision is saved in db", decisions.size(), is(1));
        PaymentDecisionsObject actualDecision = decisions.getFirst();
        assertThat("Verify that decision is rejected", actualDecision.getDecisionCode(), is(2));
        assertThat("Verify that rejectionCode is not default 0 (reason code should be provided)", actualDecision.getRejectionCode(), greaterThan(0));
        assertThat("Verify that actor is Vindex BO", actualDecision.getActor(), is("Vindex BO"));
        assertThat("Verify that reason string contains AQA tag", actualDecision.getReasonString(), containsString("AQA"));

        //Alert resolution
        Alert dbAlert = getObjectsFromDB(DbName.POSTGRES, BO_ALERT_TABLE_NAME, String.format(ALERT_WHERE, client.getUcid(), AlertType.PAYMENT), Alert.class).getFirst();
        assertThat("Verify alert_resolution is CONFIRMED", dbAlert.getAlertResolution(), is(AlertResolution.CONFIRMED.getDisplayName()));
    }

    private static void assertWithdrawalText(String text) {
        assertThat("Verify withdrawal item shows USD amount", text, containsString(formatter.format(WD_USD)));
        assertThat("Verify withdrawal item shows EUR amount", text, containsString(formatter.format(WD_EUR)));
        assertThat("Verify withdrawal item shows payment method", text, containsString(PAYMENT_METHOD_CODE));
    }

    @Test
    @AllureId("1884")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Check that resolve screen not have symbol selector on fraud")
    void paymentResolveNotHaveSymbolSelector() {
        var nowTimestamp = Timestamp.from(Instant.now());
        insertObjectToDb(DbName.POSTGRES, AR_ABUSER_TABLE_NAME, new Abuser(client.getUcid(), CONFIRMED.getStatus(), "auto-test comment", "AUTOTEST USER", "Vindex BO", nowTimestamp, nowTimestamp, true));
        insertObjectToDb(DbName.POSTGRES, AR_ABUSER_HISTORY_TABLE_NAME, new AbuserHistory(null, client.getUcid(), "CLIENT_STATUS", CONFIRMED.getStatus(), "CLIENT_STATUS", "auto-test comment", "AUTOTEST USER", "Vindex BO", nowTimestamp, null, null));
        insertObjectToDb(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, new AbuserFraudType(client.getUcid(), HEDGING.getCode(), CONFIRMED.getStatus(), "auto-test comment", "AUTOTEST USER", "Vindex BO", nowTimestamp, nowTimestamp, "INTERNAL", null, "Vindex"));
        insertObjectToDb(DbName.POSTGRES, AR_ABUSER_HISTORY_TABLE_NAME, new AbuserHistory(null, client.getUcid(), HEDGING.getCode(), CONFIRMED.getStatus(), "FRAUD_TYPE_STATUS", "auto-test comment", "AUTOTEST USER", "Vindex BO", nowTimestamp, "INTERNAL", null));
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsPaymentTeamUser();
        investigationPage.waitForPageToLoad();
        investigationPage.navigateToClient(crmTbUser.ucid);
        resolvePage.openResolveSuspicious();
        resolvePage.addFraud(EXCHANGER);
        resolvePage.checkSymbolDropdownIsNotVisible();
    }
}
