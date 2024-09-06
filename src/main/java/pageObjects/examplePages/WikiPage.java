package pageObjects.examplePages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class WikiPage {
    private final Page page;
    private final Locator wikiLogo;
    private final String GyozaURL = "https://en.wikipedia.org/wiki/Jiaozi";
    private final String cheeseURL = "https://en.wikipedia.org/wiki/Cheese";

    public WikiPage(Page page) {
        this.page = page;
        this.wikiLogo = page.locator(".central-textlogo__image.svg-Wikipedia_wordmark");
    }

    @Step("navigate to gyouza page")
    public void navigateGyoza() {
        page.navigate(GyozaURL);
    }

    @Step("navigate to cheese page")
    public void navigateCheesea() {
        page.navigate(cheeseURL);
    }

    public void checkLogo() {
        wikiLogo.isVisible();
    }
}
