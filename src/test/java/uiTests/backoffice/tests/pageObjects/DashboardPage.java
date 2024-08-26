package uiTests.backoffice.tests.pageObjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

import static utils.ConfigFactory.BASE_URL_E2E;

public class DashboardPage {
    private final Page page;
    private final Locator pageLogo;

    public DashboardPage(Page page) {
        this.page = page;
        this.pageLogo = page.locator("body .logo");
    }

    @Step("Open the BackOffice dashboard page")
    public void navigate() {
        page.navigate(BASE_URL_E2E + "/dashboard");
        pageLogo.isVisible();
    }
}
