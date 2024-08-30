package uiTests.backoffice.tests.tests;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uiTests.backoffice.tests.pageObjects.AlertPage;
import uiTests.backoffice.tests.pageObjects.KeycloackPage;
import uiTests.backoffice.tests.pageObjects.ProfilePage;

public class AlertPageTest extends TestBaseE2E {

    @Disabled
    @Test
    @DisplayName("positive login test")
    void PositiveloginUITest() {
        AlertPage alertPage = new AlertPage(page);
        alertPage.navigate();
        KeycloackPage keycloackPage = new KeycloackPage(page);
        keycloackPage.loginWEB(
                "valid user name",
                "valid user password"); // call the method for log in thought UI login form and check redirect into
        // system
        alertPage.isLoggedIn(); // check some simple and bulletproof marker of logging into the system
    }

    @Disabled
    @Test
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
