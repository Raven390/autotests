package tests.vindex_backoffice_ui_tests.investigationTool;

import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.database.DbHelper.*;
import static helpers.kafka.alerts.CreateSimpleAlert.sendSimpleAlert;
import static helpers.kafka.alerts.CreateSimpleAlert.sendSimplePaymentAlert;
import static org.junit.jupiter.api.Assertions.*;
import static utils.Constants.*;

import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import helpers.data.ClientHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import java.sql.SQLException;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
@Feature("BMS-1872 [Q3] Role based model")
class ViewerRolePermissionsTest extends TestBaseWeb {
    static ClientHelper client = getRandomVantageClient();
    private static CrmTbUserObject crmTbUser = generateStaticUserByClient(client);

    @BeforeAll
    static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        crmTbUser.firstName = "Payteram";
        crmTbUser.lastName = "Testman";
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
    }

    @Test
    @AllureId("1724")
    @DisplayName(
            "BO user with viewer role can't assign suspicious client with the active payment alert to himself to perform investigation")
    void assignClientCardBothTypesAlertsViewerRolePaymentTest() throws Exception {
        sendSimplePaymentAlert(client.getUcid());
        sendSimpleAlert(client.getUcid(), "MARKET_MANIPULATION");
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsViewerUser();
        investigationPage.navigateInvestigationTool();
        investigationPage.clickSelectInvestigationType("Payments");
        investigationPage.filterUnassigned();
        investigationPage.investigateUserAlertListDisabled(client.getUserId());
    }

    @Test
    @AllureId("1723")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("cant assign a client with payment alert to the current user ")
    void assignClientPaymentTest() throws Exception {
        sendSimplePaymentAlert(client.getUcid());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsViewerUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        investigationPage.clickSelectInvestigationType("Trading");
        alertsPage.waitForPageToLoad();
        investigationPage.investigateClientCardDisabled();
    }

    @Test
    @AllureId("1722")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("cant assign a client with trading alert to the current user ")
    void assignClientTradingTest() {
        sendSimpleAlert(client.getUcid(), "MARKET_MANIPULATION");
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsViewerUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        investigationPage.clickSelectInvestigationType("Trading");
        alertsPage.waitForPageToLoad();
        investigationPage.investigateClientCardDisabled();
    }

    @Test
    @AllureId("1721")
    @DisplayName(
            "BO user with viewer role can assign suspicious client with the active trading alert to himself to perform investigation ")
    void assignClientCardBothTypesAlertsViewerRoleTradingTest() throws Exception {
        sendSimplePaymentAlert(client.getUcid());
        sendSimpleAlert(client.getUcid(), "MARKET_MANIPULATION");
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsViewerUser();
        investigationPage.navigateInvestigationTool();
        investigationPage.clickSelectInvestigationType("Trading");
        investigationPage.filterUnassigned();
        investigationPage.investigateUserAlertListDisabled(client.getUserId());
    }

    @Test
    @AllureId("1720")
    @DisplayName("BO user with viewer role have investigation type selector")
    void haveAlertTypeFilterTest() throws Exception {
        sendSimplePaymentAlert(client.getUcid());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsViewerUser();
        investigationPage.navigateInvestigationTool();
        investigationPage.investigationTypeSwitchIsPresented();
    }

    @Test
    @AllureId("1719")
    @DisplayName("BO user with viewer role can comment client")
    void canCommentClientTest() throws Exception {
        sendSimplePaymentAlert(client.getUcid());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsViewerUser();
        investigationPage.navigateInvestigationTool();
        investigationPage.navigateToClient(client.getUcid());
        investigationPage.isCommentButtonDisabled();
    }

    @Test
    @AllureId("1718")
    @DisplayName("BO user with viewer role can comment client")
    void cantDoBulkActionsArTest() throws Exception {
        sendSimplePaymentAlert(client.getUcid());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsViewerUser();
        fraudstersPage.navigateAbuseRegistryFraudsters();
        fraudstersPage.openRemoveDrawerButtonIsHidden();
        fraudstersPage.openRemoveDrawerButtonIsDisabled();
    }

    @Test
    @AllureId("1717")
    @DisplayName("BO user with viewer role can open alert history page")
    void canOpenAlertHistoryTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsViewerUser();
        alertHistoryPage.navigateAlertHistory();
        alertHistoryPage.waitForPageToLoad();
    }

    @Test
    @AllureId("1716")
    @DisplayName("BO user with viewer role can view deduction page")
    void cantOpenDeductionPageTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsViewerUser();
        deductionPage.navigateDeduction();
        Allure.step("check that deduction page is opened");
        assertTrue(page.url().contains("deduction"));
    }

    @Test
    @AllureId("1715")
    @DisplayName("BO user with viewer role can view search page")
    void canOpenSearchPageTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsViewerUser();
        searchPage.navigateToSearchPage();
        Allure.step("check that search page is opened");
        assertTrue(page.url().contains("clients-search"));
    }

    @Test
    @AllureId("1714")
    @DisplayName("BO user with viewer role can't view duty team portal")
    void cantOpenDutyTeamPortalTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsViewerUser();
        dutyTeamPage.navigateToDutyTeamPage();
        Allure.step("check that duty portal page is not opened");
        assertFalse(page.url().contains("duty-team-portal"));
    }

    @Test
    @AllureId("1713")
    @DisplayName("BO user with viewer role dont have QC funtions on alerts")
    void notHaveQcTest() throws Exception {
        sendSimpleAlert(client.getUcid(), "MARKET_MANIPULATION");
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsViewerUser();
        alertHistoryPage.navigateAlertHistory();
        alertHistoryPage.notHaveQc();
    }
}
