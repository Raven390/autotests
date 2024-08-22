package uiTests.backoffice.tests.pageObjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
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

    @Step("Open the BackOffice main page")
    public void navigate() {
        page.navigate(BASEURLE2E);
    }

    @Step("Check that user is logged in")
    public void isLoggedIn() {
        mainPageLogo.isVisible();
    }

    @Step("Check that user is logged in")
    public void isNotLoggedIn() {
        assertEquals(mainPageLogo.count(), 0);
    }
}



}