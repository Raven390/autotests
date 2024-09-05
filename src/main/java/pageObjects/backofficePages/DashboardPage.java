package pageObjects.backofficePages;

import static utils.ConfigFactory.BASE_URL_E2E;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class DashboardPage {
    private final Page page;
    private final Locator pageLogo;
    private final Locator breadcrumbs;

    public DashboardPage(Page page) {
        this.page = page;
        this.pageLogo = page.locator("body .logo");
        this.breadcrumbs = page.locator(".breadcrumps");
    }

    @Step("Open the BackOffice dashboard page")
    public void navigate() {
        page.navigate(BASE_URL_E2E + "/dashboard");
        pageLogo.isVisible();
    }
}
