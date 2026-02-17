package tests.vindex_backoffice_ui_tests.investigationTool;

import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static business_objects.ui.user.UserFactory.autotestUserOPS24;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.data.enums.AlertType.TRADING;
import static helpers.data.enums.InvestigationStatus.INVESTIGATING;
import static helpers.data.enums.deduction.AlertStatus.OPEN;
import static helpers.database.BoHelper.getClientsAlertsDb;
import static helpers.database.BoHelper.getClientsInvestigationsDb;
import static helpers.database.DbHelper.insertObjectToDb;
import static helpers.kafka.alerts.CreateSimpleAlert.sendSimpleAlert;
import static helpers.kafka.alerts.CreateSimpleAlert.sendSimplePaymentAlert;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.Constants.*;

import business_objects.db.backoffice_db.Investigation;
import business_objects.db.backoffice_db.alert.Alert;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import helpers.data.ClientHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseWeb;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
@Feature("BMS-1872 [Q3] Role based model")
class Ops24PermissionsTest extends TestBaseWeb {
    static ClientHelper client = getRandomVantageClient();
    private static CrmTbUserObject crmTbUser = generateStaticUserByClient(client);

    @BeforeAll
    static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        crmTbUser.firstName = "Payteram";
        crmTbUser.lastName = "Testman";
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
    }

    @Test
    @AllureId("1733")
    @DisplayName(
            "BO user with OPS24 role can assign suspicious client with the active trading alert to himself to perform investigation ")
    void assignClientCardBothTypesAlertsViewerRoleTradingTest() throws Exception {
        sendSimpleAlert(client.getUcid(), "MARKET_MANIPULATION");
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsOps24User();
        investigationPage.navigateToClient(client.getUcid());
        investigationPage.investigateClientCard();
        Investigation investigation =
                getClientsInvestigationsDb(client.getUcid(), TRADING).getFirst();
        Allure.step("check that investigation is assigned to current user");
        assertEquals(autotestUserOPS24().getId(), investigation.getAssignedUserId());
        assertEquals(INVESTIGATING.getDisplayName(), investigation.getStatus());
        Alert alert = getClientsAlertsDb(client.getUcid(), TRADING).getFirst();
        Allure.step("check that alert is assigned to current user");
        assertEquals(investigation.getId().toString(), alert.getInvestigationId());
        assertEquals(OPEN.getDisplayName(), alert.getStatus());
    }

    @Test
    @AllureId("1732")
    @DisplayName("BO user with OPS24 role not have investigation type selector")
    void haveAlertTypeFilterTest() throws Exception {
        sendSimplePaymentAlert(client.getUcid());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsOps24User();
        investigationPage.navigateInvestigationTool();
        investigationPage.selectInvestigationTypeSwitchIsHidden();
    }

    @Test
    @AllureId("1731")
    @DisplayName("BO user with OPS24 role can comment client")
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
    @AllureId("1730")
    @DisplayName("BO user with OPS24 role can comment client")
    void cantDoBulkActionsArTest() throws Exception {
        sendSimplePaymentAlert(client.getUcid());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsOps24User();
        fraudstersPage.navigateAbuseRegistryFraudsters();
        fraudstersPage.openUploadDrawer();
    }

    @Test
    @AllureId("1729")
    @DisplayName("BO user with OPS24 role can open alert history page")
    void canOpenAlertHistoryTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsOps24User();
        alertHistoryPage.navigateAlertHistory();
        alertHistoryPage.waitForPageToLoad();
    }

    @Test
    @AllureId("1728")
    @DisplayName("BO user with OPS24 role can view deduction page")
    void cantOpenDeductionPageTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsOps24User();
        deductionPage.navigateDeduction();
        Allure.step("check that deduction page is opened");
        assertTrue(page.url().contains("deduction"));
    }

    @Test
    @AllureId("1727")
    @DisplayName("BO user with OPS24 role can view search page")
    void canOpenSearchPageTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsOps24User();
        searchPage.navigateToSearchPage();
        Allure.step("check that search page is opened");
        assertTrue(page.url().contains("clients-search"));
    }

    @Test
    @AllureId("1726")
    @DisplayName("BO user with OPS24 role can't view duty team portal")
    void cantOpenDutyTeamPortalTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsOps24User();
        dutyTeamPage.navigateToDutyTeamPage();
        Allure.step("check that duty portal page is not opened");
        assertFalse(page.url().contains("duty-team-portal"));
    }

    @Test
    @AllureId("1725")
    @DisplayName("BO user with OPS24 role dont have QC functions on alerts")
    void notHaveQcTest() throws Exception {
        sendSimpleAlert(client.getUcid(), "MARKET_MANIPULATION");
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsOps24User();
        alertHistoryPage.navigateAlertHistory();
        alertHistoryPage.notHaveQc();
    }
}
