package pageObjects.backofficePages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class KeycloackPage {
    private final Page page;
    private final Locator loginField;
    private final Locator passwordField;
    private final Locator loginConfirmButton;
    private final Locator errorMessage;
    private final Locator breadcrumbs;

    private final String errorText = " Invalid username or password.";

    public KeycloackPage(Page page) {
        this.page = page;
        this.loginField = page.locator("input[id=\"username\"]");
        this.passwordField = page.locator("input[id=\"password\"]");
        this.loginConfirmButton = page.locator("input[type=\"submit\"]");
        this.errorMessage = page.locator("[id=\"input-error\"]");
        this.breadcrumbs = page.locator(".breadcrumps");
    }

    @Step("Log In trough UI")
    public void loginWEB(String userName, String userPass) {
        page.url().contains("keycloak");
        loginField.fill(userName);
        passwordField.fill(userPass);
        loginConfirmButton.click();
    }

    @Step("Check that authorisation is failed")
    public void errorMessageIsShown() {
        errorMessage.isVisible();
        errorMessage.textContent().contains(" Invalid username or password.");
    }

    @Step("Check that user is logged out")
    public void isLoggedOut() {
        passwordField.isVisible();
        loginField.isVisible();
    }
}
