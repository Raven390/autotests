package uiTests.backoffice.tests;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uiTests.backoffice.tests.pageObjects.MainPage;

public class BOTestDummies extends TestBaseE2E {

    @Test
    @DisplayName("positive login test")
    void PositiveloginUI() {
        MainPage mainPage = new MainPage(page);
        mainPage.loginWEB("valid user name", "valid user password");// call the method for log in thought UI login form and check redirect into system
//        mainPage.isLoggedIn check some simple and bulletproof marker of logging into the system
    }
    @Test
    @DisplayName("negative login test")
    void NegativeloginUI() {
        MainPage mainPage = new MainPage(page);
        mainPage.loginWEB("WrongUserNameString", "userPassString"); //call the method for log in thought UI login form
        mainPage.isNotLoggedIn(); //check some simple and bulletproof marker of logging error
    }

}


