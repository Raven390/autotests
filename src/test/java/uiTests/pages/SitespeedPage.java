package uiTests.pages;

import com.microsoft.playwright.Locator;

public class SitespeedPage extends BasePage {

    public String
            startPageButtonLocator = "[role=\"navigation\"] li a[href=\"/\"]",
            documentationButtonLocator = "[role=\"navigation\"] li a[href=\"/documentation/\"]",
            startPageButtonText = "Start",
            documentationButtonText = "Documentation";

    public SitespeedPage openMainPage() {
        getPage().navigate("https://www.sitespeed.io/");
        return this;
    }

    public SitespeedPage checkNavBarButton(String locator, String text) {
        getPage().locator(locator)
                .filter(new Locator.FilterOptions()
                        .setHasText(text))
                .isVisible();
        getPage().locator(locator)
                .click();
        return this;
    }
}



