package uiTests.backoffice.tests;


import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uiTests.backoffice.tests.apiHelpers.KeycloackAPI;
import uiTests.backoffice.tests.pageObjects.KeycloackPage;
import uiTests.backoffice.tests.pageObjects.MainPage;

public class MainPageTest extends TestBaseE2E {

    @Disabled
    @Test
    @DisplayName("positive login test")
    void PositiveloginUITest() {
        MainPage mainPage = new MainPage(page);
        mainPage.navigate();
        KeycloackPage keycloackPage = new KeycloackPage(page);
        keycloackPage.loginWEB("valid user name", "valid user password");// call the method for log in thought UI login form and check redirect into system
        mainPage.isLoggedIn(); //check some simple and bulletproof marker of logging into the system
    }

    @Disabled
    @Test
    @DisplayName("negative login test")
    void NegativeloginUITest() {
        MainPage mainPage = new MainPage(page);
        KeycloackPage keycloackPage = new KeycloackPage(page);
        mainPage.navigate();
        keycloackPage.loginWEB("WrongUserNameString", "userPassString"); //call the method for log in thought UI login form
        mainPage.isNotLoggedIn(); //check some simple and bulletproof marker of logging error
        keycloackPage.errorMessageIsShown();
    }

    @Disabled
    @Test
    @DisplayName("main page rendered basic elements")
    void MainPageRendersTest() {
        MainPage mainPage = new MainPage(page);
        KeycloackPage keycloackPage = new KeycloackPage(page);
        mainPage.navigate();
        keycloackPage.loginWEB("valid user name", "userPassString"); //call the method for log in thought UI login form
        mainPage.isMainPageBasicElementsVisible();
    }

    @Disabled
    @Test
    @DisplayName("user can go to profile page from main page")
    void MainPAgeRenderesTest() {
        MainPage mainPage = new MainPage(page);
        KeycloackPage keycloackPage = new KeycloackPage(page);
        mainPage.navigate();
        keycloackPage.loginWEB("valid user name", "userPassString"); //call the method for log in thought UI login form
        mainPage.isProfileButtonVisible();
        mainPage.clickProfileButton();
    }

}