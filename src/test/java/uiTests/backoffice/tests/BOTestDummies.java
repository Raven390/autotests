package uiTests.backoffice.tests;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BOTestDummies extends TestBaseE2E {

    @Test
    @DisplayName("positive login test")
    void PositiveloginUI() {
//        loginWEB(userNameString, userPassString); call the method for log in thought UI login form and check redirect into sistem
//        mainPage.isLoggedIn check some simple and bulletproof marker of logging into the system
    }
    @Test
    @DisplayName("negative login test")
    void NegativeloginUI() {
//        loginWEB(WrongUserNameString, userPassString); call the method for log in thought UI login form
//        mainPage.isNotLoggedIn check some simple and bulletproof marker of logging error
    }


}


