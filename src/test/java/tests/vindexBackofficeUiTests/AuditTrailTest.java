package tests.vindexBackofficeUiTests;

import businessObjects.api.mitigationService.PostRestrictionRequestBody;
import businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObject;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.db.clickhouse.crmTbWithdrawal.CrmTbWithdrawalObject;
import businessObjects.kafka.alerts.RuleAlert;
import businessObjects.ui.auditTrail.AuditTrailItem;
import businessObjects.ui.user.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

import static businessObjects.api.mitigationService.MitigationServiceRequest.postRestriction;
import static businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateUserByClient;
import static businessObjects.db.clickhouse.crmTbWithdrawal.CrmTbWithdrawalObjectFactory.generateWithdrawalByClient;
import static businessObjects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static businessObjects.ui.user.UserFactory.coreUser;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
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
        investigationPage.navigateToClient(crmTbUser.ucid);
        keycloackPage.loginAsCoreUser();
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
        investigationPage.navigateToClient(crmTbUser.ucid);
        keycloackPage.loginAsCoreUser();
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
        User user = coreUser();
        assertThat("Verify audit trail item header", item.getHeader(), equalTo(String.format("%s%s %s", "Comment added", user.getFirstName(), user.getLastName())));
        assertThat("Verify audit trail item comment", item.getComment(), equalTo(comment));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("603")
    @DisplayName("Audit trail. Verify message for 'Client assigned' action type")
    public void verifyClientAssignedTest() {
        investigationPage.navigateToClient(crmTbUser.ucid);
        keycloackPage.loginAsCoreUser();
        alertsPage.waitForPageToLoad();
        investigationPage.investigateClientCard();
        auditTrailPage.openAuditTrailTab();
        List<AuditTrailItem> auditTrailItems = auditTrailPage.getAuditTrailItems();
        assertThat("Assert that there are 2 audit trail items", auditTrailItems, hasSize(2));
        AuditTrailItem item = auditTrailItems.getFirst();
        assertThat("Verify audit trail item time", item.getTime(), matchesPattern(TIME_PATTERN));
        User user = coreUser();
        assertThat("Verify audit trail item header", item.getHeader(), equalTo(String.format("%s%s %s", "Client assigned", user.getFirstName(), user.getLastName())));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("604")
    @DisplayName("Audit trail. Verify message for 'Investigation completed' action type")
    public void verifyInvestigationCompletedTest() throws JsonProcessingException {
        investigationPage.navigateToClient(crmTbUser.ucid);
        keycloackPage.loginAsCoreUser();
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
        User user = coreUser();
        assertThat("Verify audit trail item header", item.getHeader(), equalTo(String.format("%s%s %s", "Investigation completed", user.getFirstName(), user.getLastName())));
        assertThat("Verify audit trail item comment", item.getComment(), equalTo(comment));
        assertThat("Verify audit trail item details", item.getDetails(), equalTo(String.format("Confirmed fraud type: %s", "Market manipulation")));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("605")
    @DisplayName("Audit trail. Verify message for restrictions action types")
    public void verifyRestrictionsTest() {
        investigationPage.navigateToClient(crmTbUser.ucid);
        keycloackPage.loginAsCoreUser();
        alertsPage.waitForPageToLoad();
        restrictionPage.openRestrictionsTab();
        restrictionPage.clickLoginSwitch();
        String commentSet = "Test restriction requested action type";
        restrictionPage.fillApplyReason(commentSet);
        restrictionPage.clickCheckedLogin();
        String commentCancel = "Test cancellation requested action type";
        restrictionPage.fillCancelReason(commentCancel);
        auditTrailPage.openAuditTrailTab();
        List<AuditTrailItem> auditTrailItems = auditTrailPage.getAuditTrailItems();
        assertThat("Assert that there are 5 audit trail items", auditTrailItems, hasSize(5));
        for (AuditTrailItem item : auditTrailItems) {
            assertThat("Verify audit trail item time", item.getTime(), matchesPattern(TIME_PATTERN));
        }
        User user = coreUser();
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
    public void verifyWithdrawalRequestDecisionTest() throws IOException, ReflectiveOperationException, SQLException {
        Response response = postRestriction(new PostRestrictionRequestBody(
                crmTbUser.ucid, "13", "GENERAL", null, null, "Automation test", new PostRestrictionRequestBody.UpdatedBy("Auto", "Test")
        ));
        assertThat("Assert that restriction has been set successfully", response.code(), equalTo(200));
        CrmTbWithdrawalObject withdrawal = generateWithdrawalByClient(client);
        insertObjectToDb(CRM_WITHDRAWAL_TABLE_NAME, withdrawal);
        investigationPage.navigateToClient(crmTbUser.ucid);
        keycloackPage.loginAsCoreUser();
        alertsPage.waitForPageToLoad();
        restrictionPage.openRestrictionsTab();
        restrictionPage.clickCheckedManual();
        String comment = "Test withdrawal request decision action type";
        restrictionPage.fillCancelReasonManualWithdrawalApproveOne(comment);
        auditTrailPage.openAuditTrailTab();
        List<AuditTrailItem> auditTrailItems = auditTrailPage.getAuditTrailItems();
        assertThat("Assert that there are 6 audit trail items", auditTrailItems, hasSize(6));
        for (AuditTrailItem item : auditTrailItems) {
            assertThat("Verify audit trail item time", item.getTime(), matchesPattern(TIME_PATTERN));
        }
        User user = coreUser();
        AuditTrailItem cancellationRequested = new AuditTrailItem(
                String.format("%s%s %s", "Withdrawal request decision", user.getFirstName(), user.getLastName()), comment, String.format("Transaction ID %s; %s %s %s %s; Approve", withdrawal.transferId, new DecimalFormat("#.00").format(withdrawal.amount), withdrawal.currency, withdrawal.createTime.substring(0, withdrawal.createTime.length() - 3), withdrawal.paymentType), null
        );
        assertThat("Verify audit trail items", auditTrailItems, hasItem(cancellationRequested));
    }
}
