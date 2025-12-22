package tests.vindex_backoffice_ui_tests.investigationTool.auditTrail;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObjectFactory.generateRiskPaymentDecisionObject;
import static business_objects.db.payment_gate.payment_details.PaymentDetailsObjectFactory.generatePaymentDetailsObject;
import static business_objects.db.payment_gate.payment_events.PaymentEventsObjectFactory.generatePaymentEventsObject;
import static business_objects.kafka.alerts.RuleAlertFactory.*;
import static business_objects.ui.user.UserFactory.*;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.Restriction.*;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.CleanTableHelper.cleanPaymentGateData;
import static helpers.database.DbHelper.*;
import static helpers.database.DbHelper.insertObjectsToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.insertCrmAccountsToDb;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import business_objects.kafka.alerts.RuleAlert;
import business_objects.ui.audit_trail.AuditTrailItemV2;
import business_objects.ui.user.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.database.DbName;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

class AuditTrailTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static CrmTbUserObject crmTbUser;
    private static CrmTbAccountObject account;
    private static PaymentEventsObject paymentEventsObject1;
    private static RuleAlert alert;
    private static ClientHelper client;
    private static final String TIME_PATTERN = "^([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$";
    private static final User user = autotestUserOne();

    @BeforeEach
    public void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        client = getRandomVantageClientAllFields();
        crmTbUser = generateUserByClient(client);
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        account = generateCrmTbAccountDataForUi(client);
        insertCrmAccountsToDb(account);
        alert = generateRuleAlertByUcid(crmTbUser.ucid);
        kafka.produceMessage(alert.alertId, objectMapper.writeValueAsString(alert), KAFKA_TOPIC_ALERTS);
    }

    @AfterEach
    public void teardown() throws SQLException {
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", crmTbUser.ucid));
        closeAlert(crmTbUser.ucid);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("601")
    @DisplayName("Audit trail. Verify message for 'Alert received' action type")
    public void verifyAlertReceivedTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.waitForPageToLoad();
        auditTrailPage.openAuditTrailTab();
        List<AuditTrailItemV2> auditTrailItems = auditTrailPage.getAuditTrailItemsV2();
        assertThat("Assert that there is 1 audit trail item", auditTrailItems, hasSize(1));
        AuditTrailItemV2 item = auditTrailItems.getFirst();
        assertThat("Verify audit trail item header", item.getHeader(), equalTo("Registration"));
        assertThat(
                "Verify audit trail item details",
                item.getDetails(),
                equalTo("stepName:\nLinked market manipulator abuser"));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("602")
    @DisplayName("Audit trail. Verify message for 'Comment added' action type")
    public void verifyCommentAddedTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.waitForPageToLoad();
        investigationPage.openCommentForm();
        String comment = "Test comment added action type";
        investigationPage.fillCommentForm(comment);
        investigationPage.submitCommentForm();
        auditTrailPage.openAuditTrailTab();
        List<AuditTrailItemV2> auditTrailItems = auditTrailPage.getAuditTrailItemsV2();
        assertThat("Assert that there are 2 audit trail items", auditTrailItems, hasSize(2));
        AuditTrailItemV2 item = auditTrailItems.getFirst();
        assertThat("Verify audit trail item header", item.getHeader(), equalTo("Comment added"));
        assertThat("Verify audit trail item details", item.getDetails(), equalTo("Test comment added action type"));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("603")
    @DisplayName("Audit trail. Verify message for 'Client assigned' action type")
    public void verifyClientAssignedTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.waitForPageToLoad();
        investigationPage.investigateClientCard();
        auditTrailPage.openAuditTrailTab();
        List<AuditTrailItemV2> auditTrailItems = auditTrailPage.getAuditTrailItemsV2();
        assertThat("Assert that there are 2 audit trail items", auditTrailItems, hasSize(2));
        AuditTrailItemV2 item = auditTrailItems.getFirst();
        assertThat("Verify audit trail item header", item.getHeader(), equalTo("Investigation started"));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("604")
    @DisplayName("Audit trail. Verify message for 'Investigation completed' action type")
    public void verifyInvestigationCompletedTest() throws JsonProcessingException {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.waitForPageToLoad();
        resolvePage.openResolveSuspicious();
        String comment = "Test investigation completed action type";
        resolvePage.resolveNoActions(comment);
        alert = generateRuleAlertByUcid(crmTbUser.ucid);
        kafka.produceMessage(alert.alertId, objectMapper.writeValueAsString(alert), KAFKA_TOPIC_ALERTS);
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        auditTrailPage.openAuditTrailTab();
        List<AuditTrailItemV2> auditTrailItems = auditTrailPage.getAuditTrailItemsV2();
        assertThat("Assert that there are 4 audit trail items", auditTrailItems, hasSize(4));
        AuditTrailItemV2 item = auditTrailItems.get(1);
        assertThat("Verify audit trail item header", item.getHeader(), equalTo("Investigation completed"));
        assertThat(
                "Verify audit trail item details",
                item.getDetails(),
                equalTo("Test investigation completed action type"));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("605")
    @DisplayName("Audit trail. Verify message for restrictions action types")
    public void verifyRestrictionsTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.waitForPageToLoad();
        restrictionPage.openRestrictionsTab();
        String commentSet = "Test restriction requested action type";
        restrictionPage.addNewRestriction(LOGIN_CRM, commentSet);
        String commentCancel = "Test cancellation requested action type";
        restrictionPage.removeRestriction(LOGIN_CRM, commentCancel);
        auditTrailPage.openAuditTrailTab();
        List<AuditTrailItemV2> auditTrailItems = auditTrailPage.getAuditTrailItemsV2();
        assertThat("Assert that there are 3 audit trail items", auditTrailItems, hasSize(3));
        assertThat(
                "Verify audit trail items",
                auditTrailItems,
                hasItems(
                        new AuditTrailItemV2(
                                "Restriction management", "Test cancellation requested action type\nLogin CRM"),
                        new AuditTrailItemV2(
                                "Restriction management", "Test restriction requested action type\nLogin CRM")));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("606")
    @DisplayName("Audit trail. Verify message for withdrawal request decision action type")
    public void verifyWithdrawalRequestDecisionTest() throws Exception {
        paymentEventsObject1 = setupDataPGS();
        RuleAlert withdrawalAlert =
                generateWithdrawalNotificationAlertWithPaymentId(client, paymentEventsObject1.getPaymentId());
        kafka.produceMessage(
                withdrawalAlert.alertId, objectMapper.writeValueAsString(withdrawalAlert), KAFKA_TOPIC_ALERTS);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.waitForPageToLoad();
        paymentsPage.clickPaymentsTabButton();
        paymentsPage.clickWithdrawalsTabButton();
        paymentsPage.selectAllWithdrawals();
        String comment = "Test withdrawal request decision action type";
        paymentsPage.fillSubmitPanelInput(comment);
        paymentsPage.clickApproveButton();
        page.waitForTimeout(2000);
        auditTrailPage.openAuditTrailTab();
        List<AuditTrailItemV2> auditTrailItems = auditTrailPage.getAuditTrailItemsV2();
        assertThat("Assert that there are 3 audit trail items", auditTrailItems, hasSize(3));
        var firstEvent = auditTrailItems.get(0);
        var secondEvent = auditTrailItems.get(1);
        assertThat("Verify audit trail item header", firstEvent.getHeader(), equalTo("Withdrawal decision"));
        assertThat(
                "Verify audit trail item details",
                firstEvent.getDetails(),
                equalTo("Test withdrawal request decision action type\n123.45 EUR"));
        assertThat("Verify audit trail item header", secondEvent.getHeader(), equalTo("Withdrawal Review"));
        cleanPaymentGateData(
                client.getUcid(),
                client.getUserId(),
                paymentEventsObject1.getPaymentId().toString());
    }

    static PaymentEventsObject setupDataPGS() {
        paymentEventsObject1 = generatePaymentEventsObject(client);
        var paymentDetailsObject1 = generatePaymentDetailsObject(paymentEventsObject1, client);
        paymentDetailsObject1.setPayload(
                "{\"id\": \"123e4567-e89b-12d3-a456-426614174000\", \"ip\": \"121.233.122.82\", \"card\": {\"card3ds\": 0, \"expYear\": \"2029\", \"expMonth\": \"4\", \"fullName\": \"sheryar shah\", \"lastFour\": \"1225\", \"binNumber\": \"654321\"}, \"cost\": 0.56, \"type\": \"withdrawal\", \"brand\": \"vantage\", \"status\": \"Success\", \"clientId\": 112341, \"platform\": \"WEB\", \"statusId\": 1, \"checkName\": \"WR_Blacklist\", \"eventDate\": \"2025-05-20T14:30:00Z\", \"regulator\": \"CIMA\", \"statusKYC\": \"Confirmed\", \"mt4Account\": 3031915, \"accountType\": \"MT5\", \"withdrawalId\": 2373634, \"schemaVersion\": \"1.0\", \"merchantOrderId\": \"VTSG1115142220250202132259\", \"paymentTypeCode\": 2, \"paymentTypeName\": \"Credit card\", \"withdrawalAmount\": 1500.00, \"paymentMethodCode\": \"CREDIT_CARD\", \"paymentChannelCode\": 1, \"paymentChannelName\": \"Credit card\", \"withdrawalCurrency\": \"USD\", \"withdrawalAmountUSD\": 1500.00, \"withdrawalApplicationTime\": \"2025-07-15 07:38:05\"}");
        var paymentDecisionsObject1 = generateRiskPaymentDecisionObject(paymentEventsObject1);

        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, List.of(paymentEventsObject1));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, List.of(paymentDetailsObject1));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, List.of(paymentDecisionsObject1));

        return paymentEventsObject1;
    }
}
