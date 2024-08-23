package uiTests.backoffice.tests.pageObjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.ConfigFactory.BASEURLE2E;

public class MainPage {
    private final Page page;
    private final Locator mainPageLogo;
    private final Locator alertList;
    private final Locator soundButton;
    private final Locator refreshButton;
    private final Locator profileButton;
    private final Locator settingButton;
    private final Locator supportButton;
    private final Locator BugReportButton;

    public MainPage(Page page) {
        this.page = page;
        this.mainPageLogo = page.locator("body .logo");
        this.alertList = page.locator(".alertList");
        this.soundButton = page.locator(".soundButton");
        this.refreshButton = page.locator(".refreshButton");
        this.profileButton = page.locator(".profileButton");
        this.settingButton = page.locator(".settingButton");
        this.supportButton = page.locator(".supportButton");
        this.BugReportButton = page.locator(".BugReportButton");
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

    @Step("Check is main page basic elements visible")
    public void isMainPageBasicElementsVisible() {
        alertList.isVisible();
        soundButton.isVisible();
        refreshButton.isVisible();
        profileButton.isVisible();
        settingButton.isVisible();
        supportButton.isVisible();
        BugReportButton.isVisible();
    }

    @Step("Click profile button")
    public void clickProfileButton() {
        profileButton.click();
    }
}