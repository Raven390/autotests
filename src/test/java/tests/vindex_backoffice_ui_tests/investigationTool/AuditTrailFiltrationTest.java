package tests.vindex_backoffice_ui_tests.investigationTool;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.kafka.alerts.RuleAlert;
import business_objects.ui.audit_trail.AuditTrailItem;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.*;
import page_objects.backoffice_pages.investigationTool.RestrictionPage;
import tests.TestBaseWeb;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static business_objects.kafka.alerts.RuleAlertFactory.generateWithdrawalNotificationAlert;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.Restriction.LOGIN_CRM;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuditTrailFiltrationTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static CrmTbUserObject crmTbUser;
    private static CrmTbAccountObject account;

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, IOException {
        ClientHelper client = getRandomVantageClientAllFields();
        crmTbUser = generateUserByClient(client);
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        account = generateCrmTbAccountDataForUi(client);
        insertObjectToDb(CRM_TB_ACCOUNT_TABLE_NAME, account);
        RuleAlert alert = generateWithdrawalNotificationAlert(client);
        kafka.produceMessage(alert.alertId, objectMapper.writeValueAsString(alert), KAFKA_TOPIC_ALERTS);
        RestrictionPage.setRestrictionAPIGeneral(crmTbUser.ucid, LOGIN_CRM.getCode());
    }

    @Test
    @Order(1)
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("618")
    @DisplayName("Audit trail. Setting up data before filtration test")
    public void setupData() throws JsonProcessingException {
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
    @DisplayName("Audit trail. Verify filtration by Alert received")
    public void verifyAuditTrailFiltrationAlertReceivedTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        auditTrailPage.openAuditTrailTab();
        auditTrailPage.clickAuditTrailFilter();
        String alertText = "Alert received";
        auditTrailPage.selectAuditTrailFilter(alertText);
        List<AuditTrailItem> auditTrailItems = auditTrailPage.getAuditTrailItems();
        assertThat("Assert that there are some audit trail items", auditTrailItems, not(empty()));
        for (AuditTrailItem item : auditTrailItems) {
            assertThat(String.format("Assert audit trail item header contains %s", alertText), item.getHeader(), containsString(alertText));
        }
    }

    @Test
    @Order(3)
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("620")
    @DisplayName("Audit trail. Verify filtration by Comment added")
    public void verifyAuditTrailFiltrationCommentAddedTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        auditTrailPage.openAuditTrailTab();
        auditTrailPage.clickAuditTrailFilter();
        String alertText = "Comment added";
        auditTrailPage.selectAuditTrailFilter(alertText);
        List<AuditTrailItem> auditTrailItems = auditTrailPage.getAuditTrailItems();
        assertThat("Assert that there are some audit trail items", auditTrailItems, not(empty()));
        for (AuditTrailItem item : auditTrailItems) {
            assertThat(String.format("Assert audit trail item header contains %s", alertText), item.getHeader(), containsString(alertText));
        }
    }

    @Test
    @Order(4)
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("621")
    @DisplayName("Audit trail. Verify filtration by Client assigned")
    public void verifyAuditTrailFiltrationClientAssignedTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        auditTrailPage.openAuditTrailTab();
        auditTrailPage.clickAuditTrailFilter();
        String alertText = "Client assigned";
        auditTrailPage.selectAuditTrailFilter(alertText);
        List<AuditTrailItem> auditTrailItems = auditTrailPage.getAuditTrailItems();
        assertThat("Assert that there are some audit trail items", auditTrailItems, not(empty()));
        for (AuditTrailItem item : auditTrailItems) {
            assertThat(String.format("Assert audit trail item header contains %s", alertText), item.getHeader(), containsString(alertText));
        }
    }

    @Test
    @Order(5)
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("622")
    @DisplayName("Audit trail. Verify filtration by Investigation completed")
    public void verifyAuditTrailFiltrationInvestigationCompletedTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        auditTrailPage.openAuditTrailTab();
        auditTrailPage.clickAuditTrailFilter();
        String alertText = "Investigation completed";
        auditTrailPage.selectAuditTrailFilter(alertText);
        List<AuditTrailItem> auditTrailItems = auditTrailPage.getAuditTrailItems();
        assertThat("Assert that there are some audit trail items", auditTrailItems, not(empty()));
        for (AuditTrailItem item : auditTrailItems) {
            assertThat(String.format("Assert audit trail item header contains %s", alertText), item.getHeader(), containsString(alertText));
        }
    }

    @Test
    @Order(6)
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("623")
    @DisplayName("Audit trail. Verify filtration by Restriction cancelled")
    public void verifyAuditTrailFiltrationRestrictionCancelledTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        auditTrailPage.openAuditTrailTab();
        auditTrailPage.clickAuditTrailFilter();
        String alertText = "Restriction cancelled";
        auditTrailPage.selectAuditTrailFilter(alertText);
        List<AuditTrailItem> auditTrailItems = auditTrailPage.getAuditTrailItems();
        assertThat("Assert that there are some audit trail items", auditTrailItems, not(empty()));
        for (AuditTrailItem item : auditTrailItems) {
            assertThat(String.format("Assert audit trail item header contains %s", alertText), item.getHeader(), containsString(alertText));
        }
    }

    @Test
    @Order(7)
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("624")
    @DisplayName("Audit trail. Verify filtration by Restriction applied")
    public void verifyAuditTrailFiltrationRestrictionAppliedTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        auditTrailPage.openAuditTrailTab();
        auditTrailPage.clickAuditTrailFilter();
        String alertText = "Restriction applied";
        auditTrailPage.selectAuditTrailFilter(alertText);
        List<AuditTrailItem> auditTrailItems = auditTrailPage.getAuditTrailItems();
        assertThat("Assert that there are some audit trail items", auditTrailItems, not(empty()));
        for (AuditTrailItem item : auditTrailItems) {
            assertThat(String.format("Assert audit trail item header contains %s", alertText), item.getHeader(), containsString(alertText));
        }
    }

    @Test
    @Order(8)
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("625")
    @DisplayName("Audit trail. Verify filtration by Restriction requested")
    public void verifyAuditTrailFiltrationRestrictionRequestedTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        auditTrailPage.openAuditTrailTab();
        auditTrailPage.clickAuditTrailFilter();
        String alertText = "Restriction requested";
        auditTrailPage.selectAuditTrailFilter(alertText);
        List<AuditTrailItem> auditTrailItems = auditTrailPage.getAuditTrailItems();
        assertThat("Assert that there are some audit trail items", auditTrailItems, not(empty()));
        for (AuditTrailItem item : auditTrailItems) {
            assertThat(String.format("Assert audit trail item header contains %s", alertText), item.getHeader(), containsString(alertText));
        }
    }

    @Test
    @Order(9)
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("626")
    @DisplayName("Audit trail. Verify filtration by Cancellation requested")
    public void verifyAuditTrailFiltrationCancellationRequestedTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        auditTrailPage.openAuditTrailTab();
        auditTrailPage.clickAuditTrailFilter();
        String alertText = "Cancellation requested";
        auditTrailPage.selectAuditTrailFilter(alertText);
        List<AuditTrailItem> auditTrailItems = auditTrailPage.getAuditTrailItems();
        assertThat("Assert that there are some audit trail items", auditTrailItems, not(empty()));
        for (AuditTrailItem item : auditTrailItems) {
            assertThat(String.format("Assert audit trail item header contains %s", alertText), item.getHeader(), containsString(alertText));
        }
    }

    @Test
    @Order(10)
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("627")
    @DisplayName("Audit trail. Verify filtration by Withdrawal request decision")
    public void verifyAuditTrailFiltrationWithdrawalRequestDecisionTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        auditTrailPage.openAuditTrailTab();
        auditTrailPage.clickAuditTrailFilter();
        String alertText = "Withdrawal request decision";
        auditTrailPage.selectAuditTrailFilter(alertText);
        List<AuditTrailItem> auditTrailItems = auditTrailPage.getAuditTrailItems();
        assertThat("Assert that there are some audit trail items", auditTrailItems, not(empty()));
        for (AuditTrailItem item : auditTrailItems) {
            assertThat(String.format("Assert audit trail item header contains %s", alertText), item.getHeader(), containsString(alertText));
        }
    }

    @Test
    @Order(11)
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("628")
    @DisplayName("Audit trail. Verify filtration by multiple types")
    public void verifyAuditTrailFiltrationMultipleTypesTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        auditTrailPage.openAuditTrailTab();
        auditTrailPage.clickAuditTrailFilter();
        String alertText1 = "Alert received";
        String alertText2 = "Comment added";
        auditTrailPage.selectAuditTrailFilter(alertText1);
        auditTrailPage.selectAuditTrailFilter(alertText2);
        List<AuditTrailItem> auditTrailItems = auditTrailPage.getAuditTrailItems();
        assertThat("Assert that there are 3 audit trail items", auditTrailItems, hasSize(3));
        for (AuditTrailItem item : auditTrailItems) {
            assertThat(String.format("Assert audit trail item header contains %s or %s", alertText1, alertText2), item.getHeader(), anyOf(containsString(alertText1), containsString(alertText2)));
        }
    }

    @Test
    @Order(12)
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("629")
    @DisplayName("Audit trail. Verify clear filtration")
    public void verifyAuditTrailFiltrationClearTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        auditTrailPage.openAuditTrailTab();
        int initialItemsCount = auditTrailPage.getAuditTrailItems().size();
        auditTrailPage.clickAuditTrailFilter();
        String alertText = "Alert received";
        auditTrailPage.selectAuditTrailFilter(alertText);
        assertThat("Assert that filtration is applied", auditTrailPage.getAuditTrailItems().size(), lessThan(initialItemsCount));
        auditTrailPage.clearAuditTrailFilter();
        assertThat("Assert that filtration is cleared", auditTrailPage.getAuditTrailItems().size(), equalTo(initialItemsCount));
    }

    @AfterAll
    public static void teardown() throws SQLException {
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", crmTbUser.ucid));
        deleteEntryFromDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, String.format("ucid = '%s'", crmTbUser.ucid));
        closeAlert(crmTbUser.ucid);
    }
}
