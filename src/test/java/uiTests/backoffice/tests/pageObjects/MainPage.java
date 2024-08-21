package uiTests.backoffice.tests.pageObjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import utils.ConfigFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.ConfigFactory.BASEURLE2E;

public class MainPage {
    private final Page page;
    private final Locator table;
    private final Locator loginField;
    private final Locator passwordField;
    private final Locator loginConfirmButton;
    private final Locator mainPageLogo;

    public MainPage(Page page) {
        this.page = page;
        this.table = page.locator(".table");
        this.loginField = page.locator("input .login");
        this.passwordField = page.locator("input .password");
        this.loginConfirmButton = page.locator("button .submit");
        this.mainPageLogo = page.locator("body .logo");
    }

    public void navigate() {
        page.navigate(BASEURLE2E);
    }

    public void loginWEB(String userName, String userPass) {
        loginField.fill(userName);
        passwordField.fill(userPass);
        loginConfirmButton.click();
    }

    public void isLoggedIn (){
        mainPageLogo.isVisible();
    }

    public void isNotLoggedIn (){
        assertEquals(mainPageLogo.count(), 0);
    }

}