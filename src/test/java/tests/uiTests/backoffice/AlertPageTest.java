package tests.uiTests.backoffice;

import static utils.Constants.*;

import io.qameta.allure.AllureId;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseWeb;

public class AlertPageTest extends TestBaseWeb {

    @Test
    @Owner(OWNER_DMITRI_KALACHEV)
    @Tag(TEAM_BACKOFFICE)
    @Tag(STATUS_AUTOMATED)
    @Tag(LAYER_WEB)
    @AllureId("")
    @DisplayName("positive login test")
    void PositiveloginUITest() {
        alertPage.navigate();
        keycloackPage.loginWEB("DEV", "123");
        alertPage.isLoggedIn(); // check some simple and bulletproof marker of logging into the system
    }

    @Test
    @Owner(OWNER_DMITRI_KALACHEV)
    @Tag(TEAM_BACKOFFICE)
    @Tag(STATUS_AUTOMATED)
    @Tag(LAYER_WEB)
    @AllureId("")
    @DisplayName("negative login test")
    void NegativeLoginUITest() {
        alertPage.navigate();
        keycloackPage.loginWEB(
                "WrongUserNameString", "userPassString"); // call the method for log in thought UI login form
        alertPage.isNotLoggedIn(); // check some simple and bulletproof marker of logging error
        keycloackPage.errorMessageIsShown();
    }

    @Test
    @Owner(OWNER_DMITRI_KALACHEV)
    @Tag(TEAM_BACKOFFICE)
    @Tag(STATUS_AUTOMATED)
    @Tag(LAYER_WEB)
    @AllureId("")
    @DisplayName("alert page rendered basic elements")
    void alertPageRendersTest() {
        alertPage.navigate();
        keycloackPage.loginWEB("DEV", "123"); // call the method for log in thought UI login form
        alertPage.isAlertPageBasicElementsVisible();
    }

    @Test
    @Owner(OWNER_DMITRI_KALACHEV)
    @Tag(TEAM_BACKOFFICE)
    @Tag(STATUS_AUTOMATED)
    @Tag(LAYER_WEB)
    @AllureId("")
    @DisplayName("test that side menu folds")
    void sideMenuFoldsTest() throws InterruptedException {
        alertPage.navigate();
        keycloackPage.loginWEB("DEV", "123"); // call the method for log in thought UI login form
        alertPage.sideMenuFoldButtonTest();
    }

    @Disabled
    @Test
    @Owner(OWNER_DMITRI_KALACHEV)
    @Tag(TEAM_BACKOFFICE)
    @Tag(STATUS_AUTOMATED)
    @Tag(LAYER_WEB)
    @AllureId("")
    @DisplayName("test that side menu folds")
    void sidebarRenderTest() {
        alertPage.navigate();
        keycloackPage.loginWEB("DEV", "123"); // call the method for log in thought UI login form
        alertPage.foldSidebar();
        // TODO test folded sidebar elements
        alertPage.unfoldSidebar();
        // TODO test unfolded sidebar elements
    }

    @Test
    @Owner(OWNER_DMITRI_KALACHEV)
    @Tag(TEAM_BACKOFFICE)
    @Tag(STATUS_AUTOMATED)
    @Tag(LAYER_WEB)
    @AllureId("")
    @DisplayName("test that color scheme is changing")
    void colorThemeSwitchTest() {
        alertPage.navigate();
        keycloackPage.loginWEB("DEV", "123"); // call the method for log in thought UI login form
        alertPage.colorThemeSwitch();
    }

    @Disabled
    @Test
    @Owner(OWNER_DMITRI_KALACHEV)
    @Tag(TEAM_BACKOFFICE)
    @Tag(STATUS_AUTOMATED)
    @Tag(LAYER_WEB)
    @AllureId("")
    @DisplayName("user can go to profile page from alert page")
    void alertPageOpenProfilePageTest() {
        alertPage.navigate();
        keycloackPage.loginWEB("valid user name", "userPassString"); // call the method for log in thought UI login form
        alertPage.isProfileButtonVisible();
        alertPage.clickProfileButton();
        profilePage.isOnProfilePage();
    }
}
