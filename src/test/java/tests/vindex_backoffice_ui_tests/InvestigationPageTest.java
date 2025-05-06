package tests.vindex_backoffice_ui_tests;

import static utils.Constants.*;

import io.qameta.allure.AllureId;
import io.qameta.allure.Muted;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseWeb;

public class InvestigationPageTest extends TestBaseWeb {

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
        investigationPage.navigateToClient("infinox-424242");
        investigationPage.mockCommentError("infinox-424242");
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
        investigationPage.navigateToClient("infinox-424242");
        investigationPage.openCommentForm();
        String message = "comment test " + timestamp;
        investigationPage.fillCommentForm(message);
        investigationPage.submitCommentForm();
        auditTrailPage.openAuditTrailTab();
        auditTrailPage.findRecord(message);
    }


}
