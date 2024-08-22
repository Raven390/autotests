package uiTests.backoffice.tests.pageObjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import utils.ConfigFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.ConfigFactory.BASEURLE2E;

public class KeycloackPage {
    private final Page page;
    private final Locator loginField;
    private final Locator passwordField;
    private final Locator loginConfirmButton;
    private final Locator errorScreen;

    public KeycloackPage(Page page) {
        this.page = page;
        this.loginField = page.locator("input .login");
        this.passwordField = page.locator("input .password");
        this.loginConfirmButton = page.locator("button .submit");
        this.errorScreen = page.locator(".error_message");
    }

    @Step("Log In trough UI")
    public void loginWEB(String userName, String userPass) {
        loginField.fill(userName);
        passwordField.fill(userPass);
        loginConfirmButton.click();
    }

    @Step("Check that authorisation is failed")
    public void errorMessageIsShown() {
        errorScreen.isVisible();
    }
}