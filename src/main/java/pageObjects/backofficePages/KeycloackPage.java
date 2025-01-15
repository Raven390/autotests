package pageObjects.backofficePages;

import businessObjects.ui.user.User;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

import java.util.regex.Pattern;

import static businessObjects.ui.user.UserFactory.*;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class KeycloackPage extends AbstractPage {
    private final Locator loginField;
    private final Locator passwordField;
    private final Locator loginConfirmButton;
    private final Locator errorMessage;
    private final Locator breadcrumbs;

    private final String errorText = " Invalid username or password.";

    public KeycloackPage(Page page) {
        super(page);
        this.loginField = page.locator("input[id=\"username\"]");
        this.passwordField = page.locator("input[id=\"password\"]");
        this.loginConfirmButton = page.locator("input[type=\"submit\"]");
        this.errorMessage = page.locator("[id=\"input-error\"]");
        this.breadcrumbs = page.locator(".breadcrumps");
    }

    @Step("Log In trough UI")
    public void loginWeb(String userName, String userPass) {
        Allure.step("Login Web");
        page.url().contains("keycloak");
        loginField.fill(userName);
        passwordField.fill(userPass);
        loginConfirmButton.click();
    }

    public void loginWithUser(User user) {
        assertThat(page).hasURL(Pattern.compile(".*keycloak.*"));
        loginField.fill(user.getUsername());
        passwordField.fill(user.getPassword());
        loginConfirmButton.click();
    }

    @Step("Log in as first login user")
    public void loginAsFirstLoginUser() {
        loginWithUser(firstLoginUser());
    }

    @Step("Log in as core user")
    public void loginAsCoreUser() {
        loginWithUser(coreUser());
    }

    @Step("Log in as dev user")
    public void loginAsDevUser() {
        loginWithUser(devUser());
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
