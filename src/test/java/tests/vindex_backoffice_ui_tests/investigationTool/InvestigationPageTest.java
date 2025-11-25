package tests.vindex_backoffice_ui_tests.investigationTool;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static utils.Constants.*;

import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import helpers.data.ClientHelper;
import helpers.data.enums.Brand;
import helpers.data.enums.Regulator;
import io.qameta.allure.AllureId;
import io.qameta.allure.Muted;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.io.IOException;

public class InvestigationPageTest extends TestBaseWeb {

    static ClientHelper restrictionClient = new ClientHelper(424_343, "062cde3b-ea8d-48b5-8e2c-98f3d5f67999", Brand.VANTAGE, Regulator.VFSC2, 424_343_101, 42);

    @BeforeAll
    static void setup() throws IOException, InterruptedException {
        Response response = enableCRMEmulator();
        assertNotNull(response);
        CrmTbUserObject restrictionClientDB = generateStaticUserByClient(restrictionClient);
        insertObjectToDb(CRM_USER_TABLE_NAME, restrictionClientDB);
        Thread.sleep(5000);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(TAG_AUTOMATED)
    @Tag(LAYER_WEB)
    @AllureId("132")
    @DisplayName("positive login test")
    void PositiveloginUITest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.isLoggedIn(); // check some simple and bulletproof marker of logging into the system
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(TAG_AUTOMATED)
    @Tag(LAYER_WEB)
    @AllureId("133")
    @DisplayName("negative login test")
    void NegativeLoginUITest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginWeb("WrongUserNameString", "userPassString"); // call the method for log in thought UI login form
        investigationPage.isNotLoggedIn(); // check some simple and bulletproof marker of logging error
        keycloackPage.errorMessageIsShown();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(TAG_AUTOMATED)
    @Tag(LAYER_WEB)
    @AllureId("134")
    @DisplayName("alert page rendered basic elements")
    void alertPageRendersTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser(); // call the method for log in thought UI login form
        investigationPage.isAlertPageBasicElementsVisible();
    }

    @Test
    @Disabled("disabled in UI")
    @Muted
    @Tag(TAG_MANUAL)
    @Tag(TEAM_BACKOFFICE)
    @Tag(TAG_AUTOMATED)
    @Tag(LAYER_WEB)
    @AllureId("135")
    @DisplayName("test that side menu folds")
    void sideMenuFoldsTest() throws InterruptedException {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser(); // call the method for log in thought UI login form
        investigationPage.sideMenuFoldButtonTest();
    }

    @Disabled("not implemented")
    @Muted
    @Tag(TAG_MANUAL)
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(TAG_AUTOMATED)
    @Tag(LAYER_WEB)
    @AllureId("136")
    @DisplayName("test that side menu renders")
    void sidebarRenderTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser(); // call the method for log in thought UI login form
        investigationPage.foldSidebar();
        investigationPage.unfoldSidebar();
    }

    @Test
    @Disabled("disabled in UI")
    @Muted
    @Tag(TAG_MANUAL)
    @Tag(TEAM_BACKOFFICE)
    @Tag(TAG_AUTOMATED)
    @Tag(LAYER_WEB)
    @AllureId("137")
    @DisplayName("test that color scheme is changing")
    void colorThemeSwitchTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser(); // call the method for log in thought UI login form
        investigationPage.colorThemeSwitch();
    }


    @Test
    @Disabled("disabled in UI")
    @Muted
    @Tag(TAG_MANUAL)
    @Tag(TEAM_BACKOFFICE)
    @Tag(TAG_AUTOMATED)
    @Tag(LAYER_WEB)
    @AllureId("138")
    @DisplayName("user can go to profile page from alert page")
    void alertPageOpenProfilePageTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginWeb("valid user name", "userPassString"); // call the method for log in thought UI login form
        investigationPage.isProfileButtonVisible();
        investigationPage.clickProfileButton();
        profilePage.isOnProfilePage();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(TAG_AUTOMATED)
    @Tag(LAYER_WEB)
    @AllureId("139")
    @DisplayName("test folding feature of suspicious client list section in investigation tool")
    void susClientFoldButtonTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser(); // call the method for log in thought UI login form
        investigationPage.navigateToMain();
        investigationPage.unfoldSusClientSectionIfFolded();
        investigationPage.foldSusClientFoldSection();
        investigationPage.unfoldSusClientFoldSection();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(TAG_AUTOMATED)
    @Tag(LAYER_WEB)
    @AllureId("140")
    @DisplayName("test quick filter")
    void quickFiltersTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser(); // call the method for log in thought UI login form
        investigationPage.navigateToMain();
        investigationPage.unfoldSusClientSectionIfFolded();
        investigationPage.filterAssignedMe();
        investigationPage.filterUnassigned();
        investigationPage.filterAll();

    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(TAG_AUTOMATED)
    @Tag(LAYER_WEB)
    @AllureId("324")
    @DisplayName("B0 user must see error message if commenting suspicious client failed")
    void commentErrorScreenTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(restrictionClient.getUcid());
        investigationPage.mockCommentError(restrictionClient.getUcid());
        investigationPage.openCommentForm();
        investigationPage.fillCommentForm("error test");
        investigationPage.submitCommentFormError();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(TAG_AUTOMATED)
    @Tag(LAYER_WEB)
    @AllureId("227")
    @DisplayName("BO user can add commentary to the suspicious client's audit trail")
    void commentTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(restrictionClient.getUcid());
        investigationPage.openCommentForm();
        String message = "comment test " + timestamp;
        investigationPage.fillCommentForm(message);
        investigationPage.submitCommentForm();
        auditTrailPage.openAuditTrailTab();
        auditTrailPage.findRecord(message);
    }

    @Test
    @AllureId("1869")
    @Tag(TEAM_BACKOFFICE)
    @Tag(TAG_AUTOMATED)
    @Tag(LAYER_WEB)
    @DisplayName("Tab Order Test")
    void tabsOrderTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(restrictionClient.getUcid());
        investigationPage.checkTabOrder();
    }

    @Test
    @AllureId("1870")
    @Tag(TEAM_BACKOFFICE)
    @Tag(TAG_AUTOMATED)
    @Tag(LAYER_WEB)
    @DisplayName("Default tab Test")
    void defaultTabTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(restrictionClient.getUcid());
        investigationPage.checkOpenedTab("audit");
    }


}
