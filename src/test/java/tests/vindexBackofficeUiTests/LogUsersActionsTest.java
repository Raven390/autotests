package tests.vindexBackofficeUiTests;

import businessObjects.api.mitigationService.PostRestrictionRequestBody;
import businessObjects.db.backofficeDb.userActionAudit.UserActionAudit;
import businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObject;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.db.clickhouse.crmTbWithdrawal.CrmTbWithdrawalObject;
import businessObjects.kafka.alerts.RuleAlert;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.database.DbName;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.sql.SQLException;
import java.util.List;

import static businessObjects.api.mitigationService.MitigationServiceRequest.postRestriction;
import static businessObjects.db.clickhouse.connectionTable.ConnectionTableEntryFactory.getConnectionTableEntryForUi;
import static businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static businessObjects.db.clickhouse.crmTbKycFiles.KycFilesTableEntryFactory.getKycFile;
import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateUserByClient;
import static businessObjects.db.clickhouse.crmTbWithdrawal.CrmTbWithdrawalObjectFactory.generateWithdrawalByClient;
import static businessObjects.db.clickhouse.ctmTbIdProof.IdProofTableEntryFactory.getIdProof;
import static businessObjects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static businessObjects.ui.user.UserFactory.coreUser;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.BoHelper.getUserIdByUser;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LogUsersActionsTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static CrmTbUserObject crmTbUser;
    private static CrmTbUserObject crmTbUserConnected;
    private static CrmTbAccountObject account;
    private static String userId;
    private static CrmTbWithdrawalObject withdrawal;

    @BeforeAll
    public static void setup() throws Exception {
        ClientHelper client = getRandomVantageClientAllFields();
        crmTbUser = generateUserByClient(client);
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        crmTbUserConnected = generateUserByClient(connectedClient);
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUserConnected);
        insertObjectToDb(CONNECTIONS_TABLE_NAME, getConnectionTableEntryForUi(client, connectedClient));
        insertObjectToDb(KYC_FILES_TABLE_NAME, getKycFile(client));
        insertObjectToDb(ID_PROOF_TABLE_NAME, getIdProof(client));
        account = generateCrmTbAccountDataForUi(client);
        insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, account);
        RuleAlert alert = generateRuleAlertByUcid(crmTbUser.ucid);
        kafka.produceMessage(alert.alertId, objectMapper.writeValueAsString(alert), KAFKA_TOPIC_ALERTS);
        Response response = postRestriction(new PostRestrictionRequestBody(
                crmTbUser.ucid, "13", "GENERAL", null, null, "Automation test", new PostRestrictionRequestBody.UpdatedBy("Auto", "Test")
        ));
        assertThat("Assert that restriction has been set successfully", response.code(), equalTo(200));
        withdrawal = generateWithdrawalByClient(client);
        insertObjectToDb(CRM_WITHDRAWAL_TABLE_NAME, withdrawal);
        userId = getUserIdByUser(coreUser());
    }

    @Test
    @Order(1)
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("646")
    @DisplayName("Log users actions. Verify login")
    public void verifyLogUsersActionsLoginTest() throws Exception {
        investigationPage.navigateToClient(crmTbUser.ucid);
        keycloackPage.loginAsCoreUser();
        alertsPage.waitForPageToLoad();
        List<UserActionAudit> userActionAudits = getObjectsFromDB(
                DbName.BO, BO_USER_ACTION_AUDIT_TABLE_NAME, String.format("user_id = '%s'", userId), UserActionAudit.class
        );
        UserActionAudit expectedUserActionAudit = new UserActionAudit(null, userId, null, "LOGIN", "AUTH", null);
        assertThat("Assert that user_action_audit table contains expected data", userActionAudits, hasItem(expectedUserActionAudit));
    }

    @Test
    @Order(2)
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("647")
    @DisplayName("Log users actions. Sensitive data")
    public void verifyLogUsersActionsSensitiveDataTest() throws Exception {
        investigationPage.navigateToClient(crmTbUser.ucid);
        keycloackPage.loginAsCoreUser();
        alertsPage.waitForPageToLoad();
        generalPage.clickGeneralTabButton();
        generalPage.clickShowHiddenDataButton();
        connectionPage.clickConnectionTabButton();
        connectionPage.openConnectionCard(crmTbUser.ucid);
        connectionPage.clickUnmaskConnectionCardDataButton();
        connectionPage.openConnectionTable();
        connectionPage.clickUnmaskConnectionTableDataButton();
        List<UserActionAudit> userActionAudits = getObjectsFromDB(
                DbName.BO, BO_USER_ACTION_AUDIT_TABLE_NAME, String.format("user_id = '%s' AND entity = 'SENSITIVE_DATA'", userId), UserActionAudit.class
        );
        UserActionAudit expectedUserActionAuditGeneral = new UserActionAudit(null, userId, null, "VIEW", "SENSITIVE_DATA", String.format("{\"%s\": \"%s\"}", "ucid", crmTbUser.ucid));
        UserActionAudit expectedUserActionAuditConnection = new UserActionAudit(null, userId, null, "VIEW", "SENSITIVE_DATA", String.format("{\"%s\": \"%s\"}", "ucid", crmTbUser.ucid));
        UserActionAudit expectedUserActionAuditConnectionTable = new UserActionAudit(null, userId, null, "VIEW", "SENSITIVE_DATA", String.format("{\"%s\": \"%s\"}", "ucid", crmTbUserConnected.ucid));
        assertThat("Assert that user_action_audit table contains expected data", userActionAudits, hasItems(expectedUserActionAuditGeneral, expectedUserActionAuditConnection, expectedUserActionAuditConnectionTable));
    }

    @Test
    @Order(3)
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("648")
    @DisplayName("Log users actions. KYC data")
    public void verifyLogUsersActionsKycDataTest() throws Exception {
        investigationPage.navigateToClient(crmTbUser.ucid);
        keycloackPage.loginAsCoreUser();
        alertsPage.waitForPageToLoad();
        generalPage.clickGeneralTabButton();
        generalPage.kycDetailsOpen("Proof of identity");
        List<UserActionAudit> userActionAudits = getObjectsFromDB(
                DbName.BO, BO_USER_ACTION_AUDIT_TABLE_NAME, String.format("user_id = '%s'", userId), UserActionAudit.class
        );
        UserActionAudit expectedUserActionAudit = new UserActionAudit(null, userId, null, "VIEW", "KYC_DATA", String.format("{\"%s\": \"%s\", \"%s\": \"%s\"}", "ucid", crmTbUser.ucid, "fileName", "POA.jpg"));
        assertThat("Assert that user_action_audit table contains expected data", userActionAudits, hasItem(expectedUserActionAudit));
    }

    @Test
    @Order(4)
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("649")
    @DisplayName("Log users actions. Routing")
    public void verifyLogUsersActionsRoutingTest() throws Exception {
        investigationPage.navigateToClient(crmTbUser.ucid);
        keycloackPage.loginAsCoreUser();
        alertsPage.waitForPageToLoad();
        alertsPage.openAlertsTab();
        generalPage.clickGeneralTabButton();
        operationsPage.clickOperationsTabButton();
        tradingPage.openTradingTab();
        tradingPage.openAccountsTab();
        tradingPage.clickTableViewButton();
        tradingPage.openOperationsTab();
        restrictionPage.openRestrictionsTab();
        auditTrailPage.openAuditTrailTab();
        connectionPage.clickConnectionTabButton();
        List<UserActionAudit> userActionAudits = getObjectsFromDB(
                DbName.BO, BO_USER_ACTION_AUDIT_TABLE_NAME, String.format("user_id = '%s'", userId), UserActionAudit.class
        );
        UserActionAudit expectedUserActionAuditInvestigation = new UserActionAudit(null, userId, null, "VIEW", "ROUTING", String.format("{\"%s\": \"%s\"}", "path", "/investigation"));
        UserActionAudit expectedUserActionAuditAlerts = new UserActionAudit(null, userId, null, "VIEW", "ROUTING", String.format("{\"%s\": \"%s%s%s\", \"%s\": \"%s\"}", "path", "/investigation/", crmTbUser.ucid, "/alerts", "ucid", crmTbUser.ucid));
        UserActionAudit expectedUserActionAuditGeneral = new UserActionAudit(null, userId, null, "VIEW", "ROUTING", String.format("{\"%s\": \"%s%s%s\", \"%s\": \"%s\"}", "path", "/investigation/", crmTbUser.ucid, "/general", "ucid", crmTbUser.ucid));
        UserActionAudit expectedUserActionAuditOperations = new UserActionAudit(null, userId, null, "VIEW", "ROUTING", String.format("{\"%s\": \"%s%s%s\", \"%s\": \"%s\"}", "path", "/investigation/", crmTbUser.ucid, "/operations/summary", "ucid", crmTbUser.ucid));
        UserActionAudit expectedUserActionAuditTradingAccountsCards = new UserActionAudit(null, userId, null, "VIEW", "ROUTING", String.format("{\"%s\": \"%s%s%s\", \"%s\": \"%s\"}", "path", "/investigation/", crmTbUser.ucid, "/trading/accounts/cards", "ucid", crmTbUser.ucid));
        UserActionAudit expectedUserActionAuditTradingSummary = new UserActionAudit(null, userId, null, "VIEW", "ROUTING", String.format("{\"%s\": \"%s%s%s\", \"%s\": \"%s\"}", "path", "/investigation/", crmTbUser.ucid, "/trading/summary", "ucid", crmTbUser.ucid));
        UserActionAudit expectedUserActionAuditTradingAccountsTable = new UserActionAudit(null, userId, null, "VIEW", "ROUTING", String.format("{\"%s\": \"%s%s%s\", \"%s\": \"%s\"}", "path", "/investigation/", crmTbUser.ucid, "/trading/accounts/table", "ucid", crmTbUser.ucid));
        UserActionAudit expectedUserActionAuditTradingOperations = new UserActionAudit(null, userId, null, "VIEW", "ROUTING", String.format("{\"%s\": \"%s%s%s\", \"%s\": \"%s\"}", "path", "/investigation/", crmTbUser.ucid, "/trading/operations", "ucid", crmTbUser.ucid));
        UserActionAudit expectedUserActionAuditRestrictions = new UserActionAudit(null, userId, null, "VIEW", "ROUTING", String.format("{\"%s\": \"%s%s%s\", \"%s\": \"%s\"}", "path", "/investigation/", crmTbUser.ucid, "/restrictions", "ucid", crmTbUser.ucid));
        UserActionAudit expectedUserActionAuditAuditTrail = new UserActionAudit(null, userId, null, "VIEW", "ROUTING", String.format("{\"%s\": \"%s%s%s\", \"%s\": \"%s\"}", "path", "/investigation/", crmTbUser.ucid, "/audit", "ucid", crmTbUser.ucid));
        UserActionAudit expectedUserActionAuditConnections = new UserActionAudit(null, userId, null, "VIEW", "ROUTING", String.format("{\"%s\": \"%s%s%s\", \"%s\": \"%s\"}", "path", "/investigation/", crmTbUser.ucid, "/connections", "ucid", crmTbUser.ucid));
        assertThat("Assert that user_action_audit table contains expected data", userActionAudits, hasItems(expectedUserActionAuditInvestigation, expectedUserActionAuditAlerts, expectedUserActionAuditGeneral, expectedUserActionAuditOperations, expectedUserActionAuditTradingAccountsCards, expectedUserActionAuditTradingAccountsTable, expectedUserActionAuditTradingOperations, expectedUserActionAuditRestrictions, expectedUserActionAuditAuditTrail, expectedUserActionAuditConnections));
    }

    @Test
    @Order(5)
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("650")
    @DisplayName("Log users actions. Assign client")
    public void verifyLogUsersActionsAssignClientTest() throws Exception {
        investigationPage.navigate();
        keycloackPage.loginAsCoreUser();
        investigationPage.waitForPageToLoad();
        investigationPage.clickSuspiciousClientsFiltration();
        investigationPage.selectBrandFilterByText(crmTbUser.brand);
        investigationPage.clickApplyFiltrationButton();
        investigationPage.filterUnassigned();
        investigationPage.waitForPageToLoad();
        investigationPage.clickClientCardByClientId(String.valueOf(crmTbUser.userId));
        investigationPage.assignClientByClientId(String.valueOf(crmTbUser.userId));
        List<UserActionAudit> userActionAudits = getObjectsFromDB(
                DbName.BO, BO_USER_ACTION_AUDIT_TABLE_NAME, String.format("user_id = '%s'", userId), UserActionAudit.class
        );
        UserActionAudit expectedUserActionAudit = new UserActionAudit(null, userId, null, "ASSIGN", "CLIENT", String.format("{\"%s\": \"%s\"}", "ucid", crmTbUser.ucid));
        assertThat("Assert that user_action_audit table contains expected data", userActionAudits, hasItem(expectedUserActionAudit));
    }

    @Test
    @Order(6)
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("651")
    @DisplayName("Log users actions. Comment")
    public void verifyLogUsersActionsCommentTest() throws Exception {
        investigationPage.navigateToClient(crmTbUser.ucid);
        keycloackPage.loginAsCoreUser();
        alertsPage.waitForPageToLoad();
        investigationPage.openCommentForm();
        investigationPage.fillCommentForm("Test log users actions comment");
        investigationPage.submitCommentForm();
        List<UserActionAudit> userActionAudits = getObjectsFromDB(
                DbName.BO, BO_USER_ACTION_AUDIT_TABLE_NAME, String.format("user_id = '%s'", userId), UserActionAudit.class
        );
        UserActionAudit expectedUserActionAudit = new UserActionAudit(null, userId, null, "COMMENT", "CLIENT", String.format("{\"%s\": \"%s\"}", "ucid", crmTbUser.ucid));
        assertThat("Assert that user_action_audit table contains expected data", userActionAudits, hasItem(expectedUserActionAudit));
    }

    @Test
    @Order(7)
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("652")
    @DisplayName("Log users actions. Restriction")
    public void verifyLogUsersActionsRestrictionTest() throws Exception {
        investigationPage.navigateToClient(crmTbUser.ucid);
        keycloackPage.loginAsCoreUser();
        alertsPage.waitForPageToLoad();
        restrictionPage.openRestrictionsTab();
        restrictionPage.clickLoginSwitch();
        restrictionPage.fillApplyReason("Test log users actions restriction apply");
        restrictionPage.clickCheckedLogin();
        restrictionPage.fillCancelReason("Test log users actions restriction cancel");
        List<UserActionAudit> userActionAudits = getObjectsFromDB(
                DbName.BO, BO_USER_ACTION_AUDIT_TABLE_NAME, String.format("user_id = '%s'", userId), UserActionAudit.class
        );
        UserActionAudit expectedUserActionAuditApply = new UserActionAudit(null, userId, null, "RESTRICTION", "CLIENT", String.format("{\"%s\": \"%s\", \"%s\": \"%s\"}", "ucid", crmTbUser.ucid, "action", "APPLY"));
        UserActionAudit expectedUserActionAuditCancel = new UserActionAudit(null, userId, null, "RESTRICTION", "CLIENT", String.format("{\"%s\": \"%s\", \"%s\": \"%s\"}", "ucid", crmTbUser.ucid, "action", "CANCEL"));
        assertThat("Assert that user_action_audit table contains expected data", userActionAudits, hasItems(expectedUserActionAuditApply, expectedUserActionAuditCancel));
    }

    @Test
    @Order(8)
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("653")
    @DisplayName("Log users actions. Withdrawal")
    public void verifyLogUsersActionsWithdrawalTest() throws Exception {
        investigationPage.navigateToClient(crmTbUser.ucid);
        keycloackPage.loginAsCoreUser();
        alertsPage.waitForPageToLoad();
        restrictionPage.openRestrictionsTab();
        restrictionPage.clickCheckedManual();
        restrictionPage.fillCancelReasonManualWithdrawalApproveOne("Test log users actions withdrawal");
        List<UserActionAudit> userActionAudits = getObjectsFromDB(
                DbName.BO, BO_USER_ACTION_AUDIT_TABLE_NAME, String.format("user_id = '%s'", userId), UserActionAudit.class
        );
        UserActionAudit expectedUserActionAudit = new UserActionAudit(null, userId, null, "ACCEPT", "WD_REQUEST", String.format("{\"%s\": \"%s\", \"%s\": \"%s\"}", "ucid", crmTbUser.ucid, "transferId", withdrawal.transferId));
        assertThat("Assert that user_action_audit table contains expected data", userActionAudits, hasItem(expectedUserActionAudit));
    }

    @Test
    @Order(9)
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("654")
    @DisplayName("Log users actions. Resolve")
    public void verifyLogUsersActionsResolveTest() throws Exception {
        investigationPage.navigateToClient(crmTbUser.ucid);
        keycloackPage.loginAsCoreUser();
        alertsPage.waitForPageToLoad();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveSimple("Test log users actions resolve");
        RuleAlert alert = generateRuleAlertByUcid(crmTbUser.ucid);
        kafka.produceMessage(alert.alertId, objectMapper.writeValueAsString(alert), KAFKA_TOPIC_ALERTS);
        List<UserActionAudit> userActionAudits = getObjectsFromDB(
                DbName.BO, BO_USER_ACTION_AUDIT_TABLE_NAME, String.format("user_id = '%s'", userId), UserActionAudit.class
        );
        UserActionAudit expectedUserActionAudit = new UserActionAudit(null, userId, null, "RESOLVE", "CLIENT", String.format("{\"%s\": \"%s\"}", "ucid", crmTbUser.ucid));
        assertThat("Assert that user_action_audit table contains expected data", userActionAudits, hasItem(expectedUserActionAudit));
    }

    @AfterAll
    public static void teardown() throws SQLException {
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", crmTbUser.ucid));
        deleteEntryFromDb(KYC_FILES_TABLE_NAME, String.format("ucid = '%s'", crmTbUser.ucid));
        deleteEntryFromDb(ID_PROOF_TABLE_NAME, String.format("ucid = '%s'", crmTbUser.ucid));
        deleteEntryFromDb(CRM_WITHDRAWAL_TABLE_NAME, String.format("ucid = '%s'", crmTbUser.ucid));
        closeAlert(crmTbUser.ucid);
    }
}
