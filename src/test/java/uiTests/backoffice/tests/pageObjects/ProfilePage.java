package uiTests.backoffice.tests.pageObjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

import static utils.ConfigFactory.BASEURLE2E;

public class ProfilePage {
    private final Page page;
    private final Locator logoutButton;
    private final Locator button1;
    private final Locator somethingToIDProfilePage;

    public ProfilePage(Page page) {
        this.page = page;
        this.button1 = page.locator(".buttono");
        this.logoutButton = page.locator(".logOut");
        this.somethingToIDProfilePage = page.locator(".somethingToIDProfilePage");
    }

    @Step("Open the BackOffice user profile page")
    public void navigate() {
        page.navigate(BASEURLE2E + "/account");
    }

    @Step("check that user on profile page")
    public void isOnProfilePage() {
        somethingToIDProfilePage.isVisible();
    }

    @Step("Click the log out button")
    public void clickLogoutButton() {
        logoutButton.click();
    }
}
