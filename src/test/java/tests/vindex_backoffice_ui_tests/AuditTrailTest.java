package tests.vindex_backoffice_ui_tests;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.kafka.alerts.RuleAlert;
import business_objects.ui.audit_trail.AuditTrailItem;
import business_objects.ui.user.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static business_objects.kafka.alerts.RuleAlertFactory.generateWithdrawalNotificationAlert;
import static business_objects.ui.user.UserFactory.*;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.Restriction.*;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

public class AuditTrailTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static CrmTbUserObject crmTbUser;
    private static CrmTbAccountObject account;
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
        insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, account);
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
        List<AuditTrailItem> auditTrailItems = auditTrailPage.getAuditTrailItems();
        assertThat("Assert that there is 1 audit trail item", auditTrailItems, hasSize(1));
        AuditTrailItem item = auditTrailItems.getFirst();
        assertThat("Verify audit trail item time", item.getTime(), matchesPattern(TIME_PATTERN));
        assertThat("Verify audit trail item header", item.getHeader(), equalTo(String.format("%s%s", "Alert received", "Vindex BO")));
        assertThat("Verify audit trail item details", item.getDetails(), equalTo(String.format("Alert: %s; rule: %s", alert.alertId, alert.rule.name)));
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
        List<AuditTrailItem> auditTrailItems = auditTrailPage.getAuditTrailItems();
        assertThat("Assert that there are 2 audit trail items", auditTrailItems, hasSize(2));
        AuditTrailItem item = auditTrailItems.getFirst();
        assertThat("Verify audit trail item time", item.getTime(), matchesPattern(TIME_PATTERN));
        assertThat("Verify audit trail item header", item.getHeader(), equalTo(String.format("%s%s %s", "Comment added", user.getFirstName(), user.getLastName())));
        assertThat("Verify audit trail item comment", item.getComment(), equalTo(comment));
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
        List<AuditTrailItem> auditTrailItems = auditTrailPage.getAuditTrailItems();
        assertThat("Assert that there are 2 audit trail items", auditTrailItems, hasSize(2));
        AuditTrailItem item = auditTrailItems.getFirst();
        assertThat("Verify audit trail item time", item.getTime(), matchesPattern(TIME_PATTERN));
        assertThat("Verify audit trail item header", item.getHeader(), equalTo(String.format("%s%s %s", "Client assigned", user.getFirstName(), user.getLastName())));
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
        resolvePage.resolveSimple(comment);
        alert = generateRuleAlertByUcid(crmTbUser.ucid);
        kafka.produceMessage(alert.alertId, objectMapper.writeValueAsString(alert), KAFKA_TOPIC_ALERTS);
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        auditTrailPage.openAuditTrailTab();
        List<AuditTrailItem> auditTrailItems = auditTrailPage.getAuditTrailItems();
        assertThat("Assert that there are 4 audit trail items", auditTrailItems, hasSize(4));
        AuditTrailItem item = auditTrailItems.get(1);
        assertThat("Verify audit trail item time", item.getTime(), matchesPattern(TIME_PATTERN));
        assertThat("Verify audit trail item header", item.getHeader(), equalTo(String.format("%s%s %s", "Investigation completed", user.getFirstName(), user.getLastName())));
        assertThat("Verify audit trail item comment", item.getComment(), equalTo(comment));
        assertThat("Verify audit trail item details", item.getDetails(), equalTo("Fraud type not detected."));
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
        List<AuditTrailItem> auditTrailItems = auditTrailPage.getAuditTrailItems();
        assertThat("Assert that there are 5 audit trail items", auditTrailItems, hasSize(5));
        for (AuditTrailItem item : auditTrailItems) {
            assertThat("Verify audit trail item time", item.getTime(), matchesPattern(TIME_PATTERN));
        }
        AuditTrailItem restrictionRequested = new AuditTrailItem(
                String.format("%s%s %s", "Restriction requested", user.getFirstName(), user.getLastName()), commentSet, "Login CRM", null
        );
        AuditTrailItem restrictionApplied = new AuditTrailItem(
                String.format("%s%s %s", "Restriction applied", user.getFirstName(), user.getLastName()), null, "Login CRM", null
        );
        AuditTrailItem cancellationRequested = new AuditTrailItem(
                String.format("%s%s %s", "Cancellation requested", user.getFirstName(), user.getLastName()), commentCancel, "Login CRM", null
        );
        AuditTrailItem restrictionCancelled = new AuditTrailItem(
                String.format("%s%s %s", "Restriction cancelled", user.getFirstName(), user.getLastName()), null, "Login CRM", null
        );
        assertThat("Verify audit trail items", auditTrailItems, hasItems(restrictionRequested, restrictionApplied, cancellationRequested, restrictionCancelled));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("606")
    @DisplayName("Audit trail. Verify message for withdrawal request decision action type")
    public void verifyWithdrawalRequestDecisionTest() throws IOException {
        RuleAlert withdrawalAlert = generateWithdrawalNotificationAlert(client);
        kafka.produceMessage(withdrawalAlert.alertId, objectMapper.writeValueAsString(withdrawalAlert), KAFKA_TOPIC_ALERTS);
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
        List<AuditTrailItem> auditTrailItems = auditTrailPage.getAuditTrailItems();
        assertThat("Assert that there are 3 audit trail items", auditTrailItems, hasSize(3));
        for (AuditTrailItem item : auditTrailItems) {
            assertThat("Verify audit trail item time", item.getTime(), matchesPattern(TIME_PATTERN));
        }
        AuditTrailItem withdrawalRequestDecision = new AuditTrailItem(
                String.format("%s%s %s", "Withdrawal request decision", user.getFirstName(), user.getLastName()), comment, String.format("Transaction ID %s; %s %s %s %s; Approve", withdrawalAlert.rule.attributes.withdrawalId, new DecimalFormat("#.00").format(Float.valueOf(withdrawalAlert.rule.attributes.amount)), withdrawalAlert.rule.attributes.currency, withdrawalAlert.rule.attributes.createTime.substring(0, withdrawalAlert.rule.attributes.createTime.length() - 9).replace("T", " "), withdrawalAlert.rule.attributes.paymentType), null
        );
        assertThat("Verify audit trail items", auditTrailItems, hasItem(withdrawalRequestDecision));
    }
}
