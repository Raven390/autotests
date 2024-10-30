package tests.uiTests.backoffice;

import static utils.Constants.*;

import io.qameta.allure.AllureId;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseWeb;

public class InvestigationPageTest extends TestBaseWeb {

    @Test
    @Owner("DMITRI KALACHEV")
    @Tag(TEAM_BACKOFFICE)
    @Tag(STATUS_AUTOMATED)
    @Tag(LAYER_WEB)
    @AllureId("132")
    @DisplayName("positive login test")
    void PositiveloginUITest() {
        investigationPage.navigate();
        keycloackPage.loginWEB("DEV", "123");
        investigationPage.isLoggedIn(); // check some simple and bulletproof marker of logging into the system
    }

    @Test
    @Owner("DMITRI KALACHEV")
    @Tag(TEAM_BACKOFFICE)
    @Tag(STATUS_AUTOMATED)
    @Tag(LAYER_WEB)
    @AllureId("133")
    @DisplayName("negative login test")
    void NegativeLoginUITest() {
        investigationPage.navigate();
        keycloackPage.loginWEB("WrongUserNameString", "userPassString"); // call the method for log in thought UI login form
        investigationPage.isNotLoggedIn(); // check some simple and bulletproof marker of logging error
        keycloackPage.errorMessageIsShown();
    }

    @Test
    @Owner("DMITRI KALACHEV")
    @Tag(TEAM_BACKOFFICE)
    @Tag(STATUS_AUTOMATED)
    @Tag(LAYER_WEB)
    @AllureId("134")
    @DisplayName("alert page rendered basic elements")
    void alertPageRendersTest() {
        investigationPage.navigate();
        keycloackPage.loginWEB("DEV", "123"); // call the method for log in thought UI login form
        investigationPage.isAlertPageBasicElementsVisible();
    }

    @Test
    @Disabled("disabled in UI")
    @Owner("DMITRI KALACHEV")
    @Tag(TEAM_BACKOFFICE)
    @Tag(STATUS_AUTOMATED)
    @Tag(LAYER_WEB)
    @AllureId("135")
    @DisplayName("test that side menu folds")
    void sideMenuFoldsTest() throws InterruptedException {
        investigationPage.navigate();
        keycloackPage.loginWEB("DEV", "123"); // call the method for log in thought UI login form
        investigationPage.sideMenuFoldButtonTest();
    }

    @Disabled("not implemented")
    @Test
    @Owner("DMITRI KALACHEV")
    @Tag(TEAM_BACKOFFICE)
    @Tag(STATUS_AUTOMATED)
    @Tag(LAYER_WEB)
    @AllureId("136")
    @DisplayName("test that side menu renders")
    void sidebarRenderTest() {
        investigationPage.navigate();
        keycloackPage.loginWEB("DEV", "123"); // call the method for log in thought UI login form
        investigationPage.foldSidebar();
        investigationPage.unfoldSidebar();
    }

    @Test
    @Disabled("disabled in UI")
    @Owner("DMITRI KALACHEV")
    @Tag(TEAM_BACKOFFICE)
    @Tag(STATUS_AUTOMATED)
    @Tag(LAYER_WEB)
    @AllureId("137")
    @DisplayName("test that color scheme is changing")
    void colorThemeSwitchTest() {
        investigationPage.navigate();
        keycloackPage.loginWEB("DEV", "123"); // call the method for log in thought UI login form
        investigationPage.colorThemeSwitch();
    }


    @Test
    @Disabled("disabled in UI")
    @Owner("DMITRI KALACHEV")
    @Tag(TEAM_BACKOFFICE)
    @Tag(STATUS_AUTOMATED)
    @Tag(LAYER_WEB)
    @AllureId("138")
    @DisplayName("user can go to profile page from alert page")
    void alertPageOpenProfilePageTest() {
        investigationPage.navigate();
        keycloackPage.loginWEB("valid user name", "userPassString"); // call the method for log in thought UI login form
        investigationPage.isProfileButtonVisible();
        investigationPage.clickProfileButton();
        profilePage.isOnProfilePage();
    }

    @Test
    @Owner("DMITRI KALACHEV")
    @Tag(TEAM_BACKOFFICE)
    @Tag(STATUS_AUTOMATED)
    @Tag(LAYER_WEB)
    @AllureId("139")
    @DisplayName("test folding feature of suspicious client list section in investigation tool")
    void susClientFoldButtonTest() {
        investigationPage.navigate();
        keycloackPage.loginWEB("dev", "123"); // call the method for log in thought UI login form
        investigationPage.unfoldSusClientSectionIfFolded();
        investigationPage.foldSusClientFoldSection();
        investigationPage.unfoldSusClientFoldSection();
    }

    @Test
    @Owner("DMITRI KALACHEV")
    @Tag(TEAM_BACKOFFICE)
    @Tag(STATUS_AUTOMATED)
    @Tag(LAYER_WEB)
    @AllureId("140")
    @DisplayName("test quick filter")
    void quickFiltersTest() {
        investigationPage.navigate();
        keycloackPage.loginWEB("dev", "123"); // call the method for log in thought UI login form
        investigationPage.unfoldSusClientSectionIfFolded();
        investigationPage.filterAssignedMe();
        investigationPage.filterUnassigned();
        investigationPage.filterAll();

    }
}
