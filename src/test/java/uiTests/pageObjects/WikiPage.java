package uiTests.pageObjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class WikiPage {
    private final Page page;
    private final Locator wikiLogo;
    private final String GyozaURL = "https://en.wikipedia.org/wiki/Jiaozi";

    public WikiPage(Page page) {
        this.page = page;
        this.wikiLogo = page.locator(".central-textlogo__image.svg-Wikipedia_wordmark");
    }

    public void navigateGyoza() {
        page.navigate(GyozaURL);
    }

    public void checkLogo() {
        wikiLogo.isVisible();
    }
}

