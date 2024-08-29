package uiTests.backoffice.tests.pageObjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.ConfigFactory.BASE_URL_E2E;

public class AlertPage {
    private final Page page;
    private final Locator PageLogo;
    private final Locator alertList;
    private final Locator soundButton;
    private final Locator refreshButton;
    private final Locator profileButton;
    private final Locator settingButton;
    private final Locator supportButton;
    private final Locator bugReportButton;
    private final Locator breadcrumbs;

    public AlertPage(Page page) {
        this.page = page;
        this.PageLogo = page.locator("body .logo");
        this.alertList = page.locator(".alertList");
        this.soundButton = page.locator(".soundButton");
        this.refreshButton = page.locator(".refreshButton");
        this.profileButton = page.locator(".profileButton");
        this.settingButton = page.locator(".settingButton");
        this.supportButton = page.locator(".supportButton");
        this.bugReportButton = page.locator(".BugReportButton");
        this.breadcrumbs = page.locator(".breadcrumbs");
    }

    @Step("Open the BackOffice alert page")
    public void navigate() {
        page.navigate(BASE_URL_E2E);
    }

    @Step("Check that user is logged in")
    public void isLoggedIn() {
        PageLogo.isVisible();
    }

    @Step("Check that user is logged in")
    public void isNotLoggedIn() {
        assertEquals(PageLogo.count(), 0);
    }

    @Step("Check is  page basic elements visible")
    public void isAlertPageBasicElementsVisible() {
        alertList.isVisible();
        soundButton.isVisible();
        refreshButton.isVisible();
        profileButton.isVisible();
        settingButton.isVisible();
        supportButton.isVisible();
        bugReportButton.isVisible();
        breadcrumbs.isVisible();
    }

    @Step("Check is profile button on the  page bis visible")
    public void isProfileButtonVisible() {
        profileButton.isVisible();
    }

    @Step("Click profile button")
    public void clickProfileButton() {
        profileButton.click();
    }
}