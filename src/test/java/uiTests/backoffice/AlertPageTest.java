package uiTests.backoffice;

import static utils.Constants.*;

import io.qameta.allure.AllureId;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pageObjects.backofficePages.AlertPage;
import pageObjects.backofficePages.KeycloackPage;
import pageObjects.backofficePages.ProfilePage;
import uiTests.TestBaseWeb;

public class AlertPageTest extends TestBaseWeb {

    @Disabled
    @Test
    @Owner(OWNER_DMITRI_KALACHEV)
    @Tag(TEAM_BACKOFFICE)
    @Tag(STATUS_AUTOMATED)
    @Tag(LAYER_WEB)
    @AllureId("")
    @DisplayName("positive login test")
    void PositiveloginUITest() {
        AlertPage alertPage = new AlertPage(page);
        alertPage.navigate();
        KeycloackPage keycloackPage = new KeycloackPage(page);
        keycloackPage.loginWEB("valid user name", "valid user password");
        alertPage.isLoggedIn(); // check some simple and bulletproof marker of logging into the system
    }

    @Disabled
    @Test
    @Owner(OWNER_DMITRI_KALACHEV)
    @Tag(TEAM_BACKOFFICE)
    @Tag(STATUS_AUTOMATED)
    @Tag(LAYER_WEB)
    @AllureId("")
    @DisplayName("negative login test")
    void NegativeloginUITest() {
        AlertPage alertPage = new AlertPage(page);
        KeycloackPage keycloackPage = new KeycloackPage(page);
        alertPage.navigate();
        keycloackPage.loginWEB(
                "WrongUserNameString", "userPassString"); // call the method for log in thought UI login form
        alertPage.isNotLoggedIn(); // check some simple and bulletproof marker of logging error
        keycloackPage.errorMessageIsShown();
    }

    @Disabled
    @Test
    @Owner(OWNER_DMITRI_KALACHEV)
    @Tag(TEAM_BACKOFFICE)
    @Tag(STATUS_AUTOMATED)
    @Tag(LAYER_WEB)
    @AllureId("")
    @DisplayName("alert page rendered basic elements")
    void alertPageRendersTest() {
        AlertPage alertPage = new AlertPage(page);
        KeycloackPage keycloackPage = new KeycloackPage(page);
        alertPage.navigate();
        keycloackPage.loginWEB("valid user name", "userPassString"); // call the method for log in thought UI login form
        alertPage.isAlertPageBasicElementsVisible();
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
        AlertPage alertPage = new AlertPage(page);
        KeycloackPage keycloackPage = new KeycloackPage(page);
        ProfilePage profilePage = new ProfilePage(page);
        alertPage.navigate();
        keycloackPage.loginWEB("valid user name", "userPassString"); // call the method for log in thought UI login form
        alertPage.isProfileButtonVisible();
        alertPage.clickProfileButton();
        profilePage.isOnProfilePage();
    }
}
