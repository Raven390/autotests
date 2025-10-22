package tests.vindex_backoffice_ui_tests.investigationTool;

import business_objects.db.backoffice_db.Investigation;
import business_objects.db.backoffice_db.alert.Alert;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import helpers.data.ClientHelper;
import helpers.database.ArHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.sql.SQLException;

import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static business_objects.ui.user.UserFactory.autotestUserOne;
import static business_objects.ui.user.UserFactory.autotestUserPT;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.data.enums.AlertType.PAYMENT;
import static helpers.data.enums.AlertType.TRADING;
import static helpers.data.enums.InvestigationStatus.ACTIVE;
import static helpers.data.enums.InvestigationStatus.NEW;
import static helpers.data.enums.deduction.AlertStatus.OPEN;
import static helpers.database.BoHelper.*;
import static helpers.database.CleanTableHelper.cleanUserAudit;
import static helpers.database.DbHelper.*;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.kafka.alerts.CreateSimpleAlert.sendSimpleAlert;
import static helpers.kafka.alerts.CreateSimpleAlert.sendSimplePaymentAlert;
import static org.junit.jupiter.api.Assertions.*;
import static utils.Constants.*;


@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
@Feature("BMS-1980 Investigation tool visibility + assignment payment alerts")
class PaymentTeamRolePermissionsResolveTest extends TestBaseWeb {
    static ClientHelper client = getRandomVantageClient();
    private static CrmTbUserObject crmTbUser = generateStaticUserByClient(client);


    @BeforeAll
    static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        crmTbUser.firstName = "Payteram";
        crmTbUser.lastName = "Testman";
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
    }

    @AfterEach
    void teardown() throws Exception {
        closeAlert(crmTbUser.ucid);
        deleteUserBO(client.getUcid());
    }

    @AfterAll
    static void clean() throws Exception {
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", crmTbUser.ucid));
        closeAlert(crmTbUser.ucid);
        deleteUserBO(client.getUcid());
        cleanUserAudit(client.getUcid());
        ArHelper.deleteUserFromAbuseRegistry(client.getUcid());
    }

    @Test
    @AllureId("1565")
    @DisplayName("BO user with Payment Team role can assign suspicious client with the active alert to himself to perform investigation from the alert list")
    void assignAlertListTest() throws Exception {
        sendSimplePaymentAlert(client.getUcid());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsPaymentTeamUser();
        investigationPage.navigateInvestigationTool();
        investigationPage.filterUnassigned();
        investigationPage.investigateUserAlertList(client.getUserId());
        Investigation investigation = getClientsInvestigationsDb(client.getUcid(), PAYMENT).getFirst();
        Allure.step("check that investigation is assigned to current user");
        assertEquals(autotestUserPT().getId(), investigation.getAssignedUserId());
        assertEquals(ACTIVE.getDisplayName(), investigation.getStatus());
        Alert alert = getClientsAlertsDb(client.getUcid(), PAYMENT).getFirst();
        Allure.step("check that alert is assigned to current user");
        assertEquals(investigation.getId().toString(), alert.getInvestigationId());
        assertEquals(OPEN.getDisplayName(), alert.getStatus());
    }

    @Test
    @AllureId("1566")
    @DisplayName("BO user with Payment Team role can assign suspicious client with the active alert to himself to perform investigation from the client card")
    void assignClientCardTest() throws Exception {
        sendSimplePaymentAlert(client.getUcid());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsPaymentTeamUser();
        investigationPage.navigateToClient(client.getUcid());
        investigationPage.investigateClientCard();
        Investigation investigation = getClientsInvestigationsDb(client.getUcid(), PAYMENT).getFirst();
        Allure.step("check that investigation is assigned to current user");
        assertEquals(autotestUserPT().getId(), investigation.getAssignedUserId());
        assertEquals(ACTIVE.getDisplayName(), investigation.getStatus());
        Alert alert = getClientsAlertsDb(client.getUcid(), PAYMENT).getFirst();
        Allure.step("check that alert is assigned to current user");
        assertEquals(investigation.getId().toString(), alert.getInvestigationId());
        assertEquals(OPEN.getDisplayName(), alert.getStatus());
    }

    @Test
    @AllureId("1572")
    @DisplayName("BO user with Payment Team role can assign suspicious client with the active payment alert to himself to perform investigation from the client card without affecting trading alert")
    void assignClientCardBothTypesAlertsTest() throws Exception {
        sendSimplePaymentAlert(client.getUcid());
        sendSimpleAlert(client.getUcid(), "MARKET_MANIPULATION");
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsPaymentTeamUser();
        investigationPage.navigateToClient(client.getUcid());
        investigationPage.investigateClientCard();
        Investigation investigation = getClientsInvestigationsDb(client.getUcid(), PAYMENT).getFirst();
        Allure.step("check that payment investigation is assigned to current user");
        assertEquals(autotestUserPT().getId(), investigation.getAssignedUserId());
        assertEquals(ACTIVE.getDisplayName(), investigation.getStatus());
        Alert alert = getClientsAlertsDb(client.getUcid(), PAYMENT).getFirst();
        Allure.step("check that payment alert is assigned to current user");
        assertEquals(investigation.getId().toString(), alert.getInvestigationId());
        assertEquals(OPEN.getDisplayName(), alert.getStatus());
        Investigation investigation2 = getClientsInvestigationsDb(client.getUcid(), TRADING).getFirst();
        Allure.step("check that trading investigation is not assigned to current user");
        assertNull(investigation2.getAssignedUserId());
        assertEquals(NEW.getDisplayName(), investigation2.getStatus());
        Alert alert2 = getClientsAlertsDb(client.getUcid(), TRADING).getFirst();
        Allure.step("check that trading alert is not assigned to current user");
        assertEquals(investigation2.getId().toString(), alert2.getInvestigationId());
        assertEquals(OPEN.getDisplayName(), alert2.getStatus());
    }

    @Test
    @AllureId("1574")
    @DisplayName("BO user with general role can assign suspicious client with the active payment alert to himself to perform investigation from the alert list and not affecting active trading alert")
    void assignClientCardBothTypesAlertsGeneralRolePaymentTest() throws Exception {
        sendSimplePaymentAlert(client.getUcid());
        sendSimpleAlert(client.getUcid(), "MARKET_MANIPULATION");
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateInvestigationTool();
        investigationPage.clickSelectInvestigationType("Payments");
        investigationPage.filterUnassigned();
        investigationPage.investigateUserAlertList(client.getUserId());
        Investigation investigation = getClientsInvestigationsDb(client.getUcid(), PAYMENT).getFirst();
        Allure.step("check that payment investigation is assigned to current user");
        assertEquals(autotestUserOne().getId(), investigation.getAssignedUserId());
        assertEquals(ACTIVE.getDisplayName(), investigation.getStatus());
        Alert alert = getClientsAlertsDb(client.getUcid(), PAYMENT).getFirst();
        Allure.step("check that payment alert is assigned to current user");
        assertEquals(investigation.getId().toString(), alert.getInvestigationId());
        assertEquals(OPEN.getDisplayName(), alert.getStatus());
        Investigation investigation2 = getClientsInvestigationsDb(client.getUcid(), TRADING).getFirst();
        Allure.step("check that trading investigation is not assigned to current user");
        assertNull(investigation2.getAssignedUserId());
        assertEquals(NEW.getDisplayName(), investigation2.getStatus());
        Alert alert2 = getClientsAlertsDb(client.getUcid(), TRADING).getFirst();
        Allure.step("check that trading alert is not assigned to current user");
        assertEquals(investigation2.getId().toString(), alert2.getInvestigationId());
        assertEquals(OPEN.getDisplayName(), alert2.getStatus());
    }

    @Test
    @AllureId("1573")
    @DisplayName("BO user with general role can assign suspicious client with the active trading alert to himself to perform investigation from the alert list and not affecting active payment alert")
    void assignClientCardBothTypesAlertsGeneralRoleTradingTest() throws Exception {
        sendSimplePaymentAlert(client.getUcid());
        sendSimpleAlert(client.getUcid(), "MARKET_MANIPULATION");
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateInvestigationTool();
        investigationPage.clickSelectInvestigationType("Trading");
        investigationPage.filterUnassigned();
        investigationPage.investigateUserAlertList(client.getUserId());
        Investigation investigation = getClientsInvestigationsDb(client.getUcid(), TRADING).getFirst();
        Allure.step("check that payment investigation is assigned to current user");
        assertEquals(autotestUserOne().getId(), investigation.getAssignedUserId());
        assertEquals(ACTIVE.getDisplayName(), investigation.getStatus());
        Alert alert = getClientsAlertsDb(client.getUcid(), TRADING).getFirst();
        Allure.step("check that payment alert is assigned to current user");
        assertEquals(investigation.getId().toString(), alert.getInvestigationId());
        assertEquals(OPEN.getDisplayName(), alert.getStatus());
        Investigation investigation2 = getClientsInvestigationsDb(client.getUcid(), PAYMENT).getFirst();
        Allure.step("check that trading investigation is not assigned to current user");
        assertNull(investigation2.getAssignedUserId());
        assertEquals(NEW.getDisplayName(), investigation2.getStatus());
        Alert alert2 = getClientsAlertsDb(client.getUcid(), PAYMENT).getFirst();
        Allure.step("check that trading alert is not assigned to current user");
        assertEquals(investigation2.getId().toString(), alert2.getInvestigationId());
        assertEquals(OPEN.getDisplayName(), alert2.getStatus());
    }

    @Test
    @AllureId("1691")
    @DisplayName("BO user with Payment Team role dont have investigation type selector")
    void dontHaveAlertTypeFilterTest() throws Exception {
        sendSimplePaymentAlert(client.getUcid());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsPaymentTeamUser();
        investigationPage.navigateInvestigationTool();
        investigationPage.investigationTypeSwitchIsNotPresented();
    }

    @Test
    @AllureId("1692")
    @DisplayName("BO user with Payment Team role can comment client")
    void canCommentClientTest() throws Exception {
        sendSimplePaymentAlert(client.getUcid());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsPaymentTeamUser();
        investigationPage.navigateInvestigationTool();
        investigationPage.navigateToClient(client.getUcid());
        investigationPage.openCommentForm();
        String message = "comment test " + timestamp;
        investigationPage.fillCommentForm(message);
        investigationPage.submitCommentForm();
        auditTrailPage.openAuditTrailTab();
        auditTrailPage.findRecord(message);
    }

    @Test
    @AllureId("1695")
    @DisplayName("BO user with Payment Team role can comment client")
    void cantDoBulkActionsArTest() throws Exception {
        sendSimplePaymentAlert(client.getUcid());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsPaymentTeamUser();
        fraudstersPage.navigateAbuseRegistryFraudsters();
        fraudstersPage.openRemoveDrawerButtonIsHidden();
        fraudstersPage.openRemoveDrawerButtonIsDisabled();
    }

    @Test
    @AllureId("1694")
    @DisplayName("BO user with Payment Team role can open alert history page")
    void canOpenAlertHistoryTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsPaymentTeamUser();
        alertHistoryPage.navigateAlertHistory();
        alertHistoryPage.waitForPageToLoad();
    }

    @Test
    @AllureId("")
    @DisplayName("BO user with Payment Team role cant view deduction page open Ву")
    void cantOpenDeductionPageTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsPaymentTeamUser();
        deductionPage.navigateDeduction();
        Allure.step("check that deduction page is not opened");
        assertTrue(page.url().contains("fraudsters"));
    }

    @Test
    @AllureId("1696")
    @DisplayName("BO user with Payment Team role can view search page")
    void canOpenSearchPageTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsPaymentTeamUser();
        searchPage.navigateToSearchPage();
        Allure.step("check that search page is opened");
        assertTrue(page.url().contains("clients-search"));
    }

    @Test
    @AllureId("1699")
    @DisplayName("BO user with Payment Team role can't view duty team portal")
    void cantOpenDutyTeamPortalTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsPaymentTeamUser();
        dutyTeamPage.navigateToDutyTeamPage();
        Allure.step("check that duty portal page is not opened");
        assertFalse(page.url().contains("duty-team-portal"));
    }

}
