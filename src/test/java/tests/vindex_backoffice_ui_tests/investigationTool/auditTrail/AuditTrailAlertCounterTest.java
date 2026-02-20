package tests.vindex_backoffice_ui_tests.investigationTool.auditTrail;

import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.database.BoHelper.*;
import static helpers.database.CleanTableHelper.cleanUserAudit;
import static helpers.database.DbHelper.deleteObjectFromDb;
import static helpers.database.DbHelper.insertObjectToDb;
import static helpers.kafka.alerts.CreateSimpleAlert.sendSimpleAlert;
import static helpers.kafka.alerts.CreateSimpleAlert.sendSimplePaymentAlert;
import static utils.Constants.*;

import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import helpers.data.ClientHelper;
import helpers.database.ArHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

@Tag(TAG_MANUAL)
@Disabled
@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
@Feature("BMS-2413 Display counter for active alert on Audit Trail tab")
class AuditTrailAlertCounterTest extends TestBaseWeb {
    static ClientHelper client = getRandomVantageClient();
    private static CrmTbUserObject crmTbUser = generateStaticUserByClient(client);

    @BeforeAll
    static void setup() {
        crmTbUser.firstName = "Alerting";
        crmTbUser.lastName = "Testman";
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
    }

    @AfterEach
    void teardown() {
        closeAlert(crmTbUser.ucid);
        deleteUserBO(client.getUcid());
    }

    @AfterAll
    static void clean() throws Exception {
        deleteObjectFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", crmTbUser.ucid));
        closeAlert(crmTbUser.ucid);
        deleteUserBO(client.getUcid());
        cleanUserAudit(client.getUcid());
        ArHelper.deleteUserFromAbuseRegistry(client.getUcid());
    }

    @Test
    @AllureId("1874")
    @DisplayName(
            "BO user with Payment Team role can see counter with the active payment alert and not for trading alerts")
    void alertCounterPaymentTest() throws Exception {
        sendSimplePaymentAlert(client.getUcid());
        sendSimplePaymentAlert(client.getUcid());
        sendSimpleAlert(client.getUcid(), "MARKET_MANIPULATION");
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsPaymentTeamUser();
        auditTrailPage.navigate(client.getUcid());
        auditTrailPage.checkAuditTrailAlertCounter(2);
        sendSimplePaymentAlert(client.getUcid());
        auditTrailPage.checkAuditTrailAlertCounter(3);
    }

    @Test
    @AllureId("1890")
    @DisplayName("BO user with OPS24 role can see counter with the active payment alert and not for trading alerts")
    void alertCounterTradingTest() throws Exception {
        sendSimplePaymentAlert(client.getUcid());
        sendSimplePaymentAlert(client.getUcid());
        sendSimpleAlert(client.getUcid(), "MARKET_MANIPULATION");
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsOps24User();
        auditTrailPage.navigate(client.getUcid());
        investigationPage.selectInvestigationTypeSwitchIsHidden();
        auditTrailPage.checkAuditTrailAlertCounter(1);
        sendSimpleAlert(client.getUcid(), "MARKET_MANIPULATION");
        auditTrailPage.checkAuditTrailAlertCounter(2);
    }

    @Test
    @AllureId("1891")
    @DisplayName(
            "BO user with General Role role can see counter with the active payment alert for selected type of alerts")
    void alertCounterGeneralRoleTest() throws Exception {
        sendSimplePaymentAlert(client.getUcid());
        sendSimplePaymentAlert(client.getUcid());
        sendSimpleAlert(client.getUcid(), "MARKET_MANIPULATION");
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        auditTrailPage.navigate(client.getUcid());
        investigationPage.clickSelectInvestigationType("Payments");
        auditTrailPage.checkAuditTrailAlertCounter(2);
        sendSimplePaymentAlert(client.getUcid());
        auditTrailPage.checkAuditTrailAlertCounter(3);
        investigationPage.clickSelectInvestigationType("Trading");
        auditTrailPage.checkAuditTrailAlertCounter(1);
        sendSimpleAlert(client.getUcid(), "MARKET_MANIPULATION");
        auditTrailPage.checkAuditTrailAlertCounter(2);
        investigationPage.clickSelectInvestigationType("Payments");
        auditTrailPage.checkAuditTrailAlertCounter(3);
    }
}
