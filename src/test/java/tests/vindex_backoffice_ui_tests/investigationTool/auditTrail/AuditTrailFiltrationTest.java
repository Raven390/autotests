package tests.vindex_backoffice_ui_tests.investigationTool.auditTrail;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import business_objects.kafka.alerts.RuleAlert;
import business_objects.ui.audit_trail.AuditTrailItemV2;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.database.DbName;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObjectFactory.generateRiskPaymentDecisionObject;
import static business_objects.db.payment_gate.payment_details.PaymentDetailsObjectFactory.generatePaymentDetailsObject;
import static business_objects.db.payment_gate.payment_events.PaymentEventsObjectFactory.generatePaymentEventsObject;
import static business_objects.kafka.alerts.RuleAlertFactory.*;
import static helpers.api.RestrictionHelper.setRestrictionAPIGeneral;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.Restriction.LOGIN_CRM;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.CleanTableHelper.cleanPaymentGateData;
import static helpers.database.DbHelper.*;
import static helpers.database.DbHelper.insertObjectsToDb;
import static helpers.kafka.alerts.CreateSimpleAlert.sendSimplePaymentAlert;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.insertCrmAccountsToDb;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuditTrailFiltrationTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static CrmTbUserObject crmTbUser;
    private static CrmTbAccountObject account;
    private static PaymentEventsObject paymentEventsObject1;
    private static ClientHelper client = getRandomVantageClientAllFields();


    static PaymentEventsObject setupDataPGS() {
        paymentEventsObject1 = generatePaymentEventsObject(client);
        var paymentDetailsObject1 = generatePaymentDetailsObject(paymentEventsObject1, client);
        paymentDetailsObject1.setPayload("{\"id\": \"123e4567-e89b-12d3-a456-426614174000\", \"ip\": \"121.233.122.82\", \"card\": {\"card3ds\": 0, \"expYear\": \"2029\", \"expMonth\": \"4\", \"fullName\": \"sheryar shah\", \"lastFour\": \"1225\", \"binNumber\": \"654321\"}, \"cost\": 0.56, \"type\": \"withdrawal\", \"brand\": \"vantage\", \"status\": \"Success\", \"clientId\": 112341, \"platform\": \"WEB\", \"statusId\": 1, \"checkName\": \"WR_Blacklist\", \"eventDate\": \"2025-05-20T14:30:00Z\", \"regulator\": \"CIMA\", \"statusKYC\": \"Confirmed\", \"mt4Account\": 3031915, \"accountType\": \"MT5\", \"withdrawalId\": 2373634, \"schemaVersion\": \"1.0\", \"merchantOrderId\": \"VTSG1115142220250202132259\", \"paymentTypeCode\": 2, \"paymentTypeName\": \"Credit card\", \"withdrawalAmount\": 1500.00, \"paymentMethodCode\": \"CREDIT_CARD\", \"paymentChannelCode\": 1, \"paymentChannelName\": \"Credit card\", \"withdrawalCurrency\": \"USD\", \"withdrawalAmountUSD\": 1500.00, \"withdrawalApplicationTime\": \"2025-07-15 07:38:05\"}");
        var paymentDecisionsObject1 = generateRiskPaymentDecisionObject(paymentEventsObject1);

        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, List.of(paymentEventsObject1));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, List.of(paymentDetailsObject1));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, List.of(paymentDecisionsObject1));

        return paymentEventsObject1;
    }

    @BeforeAll
    static void setup() throws ReflectiveOperationException, SQLException, IOException {
        crmTbUser = generateUserByClient(client);
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        account = generateCrmTbAccountDataForUi(client);
        insertCrmAccountsToDb(account);
        paymentEventsObject1 = setupDataPGS();
        RuleAlert withdrawalAlert = generateWithdrawalNotificationAlertWithPaymentId(client, paymentEventsObject1.getPaymentId());
        kafka.produceMessage(withdrawalAlert.alertId, objectMapper.writeValueAsString(withdrawalAlert), KAFKA_TOPIC_ALERTS);
        setRestrictionAPIGeneral(crmTbUser.ucid, LOGIN_CRM.getCode());
    }

    @Test
    @Order(1)
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("618")
    @DisplayName("Audit trail. Setting up data before filtration test")
    void setupData() throws JsonProcessingException {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        investigationPage.openCommentForm();
        investigationPage.fillCommentForm("Test comment added action type");
        investigationPage.submitCommentForm();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        restrictionPage.openRestrictionsTab();
        restrictionPage.removeRestriction(LOGIN_CRM, "Test cancel restriction for audit trail");
        resolvePage.openResolveSuspicious();
        resolvePage.resolveWithdrawalsAllApprove("Test investigation completed action type");
        RuleAlert alert = generateRuleAlertByUcid(crmTbUser.ucid);
        kafka.produceMessage(alert.alertId, objectMapper.writeValueAsString(alert), KAFKA_TOPIC_ALERTS);
    }

    @Test
    @Order(2)
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("619")
    @DisplayName("Audit trail. Verify filtration by active alerts")
    void verifyAuditTrailFiltrationActiveAlertTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        auditTrailPage.openAuditTrailTab();
        auditTrailPage.clickAuditTrailActiveFilter();
        List<AuditTrailItemV2> auditTrailItems = auditTrailPage.getAuditTrailItemsV2();
        assertThat("Assert that there is one audit trail item", auditTrailItems.size(), is(1));
        assertThat("Assert audit trail item contains active alert", auditTrailItems.getFirst().getHeader(), containsString("Registration"));
    }

    @Test
    @Order(3)
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("620")
    @DisplayName("Audit trail. Verify filtration by Comment added")
    void verifyAuditTrailFiltrationCommentAddedTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        auditTrailPage.openAuditTrailTab();
        auditTrailPage.clickAuditTrailCommentsFilter();
        List<AuditTrailItemV2> auditTrailItems = auditTrailPage.getAuditTrailItemsV2();
        assertThat("Assert that there are 3 audit trail items", auditTrailItems.size(), is(3));
        String[] alertHeaders = {"Investigation completed", "Restriction management", "Comment added"};
        for (int i = 0; i < auditTrailItems.size(); i++) {
            AuditTrailItemV2 item = auditTrailItems.get(i);
            assertThat(String.format("Assert audit trail item header contains %s", alertHeaders[i]), item.getHeader(), containsString(alertHeaders[i]));
        }
    }

    @Test
    @Order(4)
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1754")
    @DisplayName("Audit trail. Verify filtration by Team")
    void verifyAuditTrailFiltrationTeamTest() throws JsonProcessingException {
        sendSimplePaymentAlert(client.getUcid());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        auditTrailPage.openAuditTrailTab();
        auditTrailPage.clickAuditTrailTeamFilter();
        auditTrailPage.selectAuditTrailTeamFilter("Payment");
        List<AuditTrailItemV2> auditTrailItems = auditTrailPage.getAuditTrailItemsV2();
        assertThat("Assert that there is 1 audit trail item", auditTrailItems.size(), is(1));
        assertThat("Assert audit trail item contains payment alert", auditTrailItems.getFirst().getHeader(), containsString("Payment"));
        auditTrailPage.clickAuditTrailTeamFilter();
        auditTrailPage.selectAuditTrailTeamFilter("Trading");
        auditTrailItems = auditTrailPage.getAuditTrailItemsV2();
        String[] alertHeaders = {"Registration", "Investigation completed", "Investigation started", "Withdrawal Review"};
        for (int i = 0; i < auditTrailItems.size(); i++) {
            AuditTrailItemV2 item = auditTrailItems.get(i);
            assertThat(String.format("Assert audit trail item header contains %s", alertHeaders[i]), item.getHeader(), containsString(alertHeaders[i]));
        }
        auditTrailPage.clickAuditTrailActiveFilter();
        auditTrailItems = auditTrailPage.getAuditTrailItemsV2();
        assertThat("Assert that there is 1 audit trail item", auditTrailItems.size(), is(1));
        assertThat("Assert audit trail item contains active alert", auditTrailItems.getFirst().getHeader(), containsString("Registration"));
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", crmTbUser.ucid));
        deleteEntryFromDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, String.format("ucid = '%s'", crmTbUser.ucid));
        closeAlert(crmTbUser.ucid);
        cleanPaymentGateData(client.getUcid(), client.getUserId(), paymentEventsObject1.getPaymentId().toString());
    }
}
