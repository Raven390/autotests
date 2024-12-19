package pageObjects.backofficePages;

import static utils.ConfigFactory.BASE_URL_E2E;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class ProfilePage extends AbstractPage {
    private final Locator logoutButton;
    private final Locator button1;
    private final Locator somethingToIDProfilePage;
    private final Locator breadcrumbs;

    public ProfilePage(Page page) {
        super(page);
        this.button1 = page.locator(".buttono");
        this.logoutButton = page.locator(".logOut");
        this.somethingToIDProfilePage = page.locator(".somethingToIDProfilePage");
        this.breadcrumbs = page.locator(".breadcrumps");
    }

    @Step("Open the BackOffice user profile page")
    public void navigate() {
        page.navigate(BASE_URL_E2E + "account");
    }

    @Step("Check that user on profile page")
    public void isOnProfilePage() {
        somethingToIDProfilePage.isVisible();
    }

    @Step("Click the log out button")
    public void clickLogoutButton() {
        logoutButton.click();
    }
}
