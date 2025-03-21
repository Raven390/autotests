package page_objects.backoffice_pages;

import static utils.ConfigFactory.BASE_URL_E2E;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class DashboardPage extends AbstractPage {
    private final Locator pageLogo;
    private final Locator breadcrumbs;

    public DashboardPage(Page page) {
        super(page);
        this.pageLogo = page.locator("body .logo");
        this.breadcrumbs = page.locator(".breadcrumps");
    }

    @Step("Open the BackOffice dashboard page")
    public void navigate() {
        page.navigate(BASE_URL_E2E + "/dashboard");
        pageLogo.isVisible();
    }
}
