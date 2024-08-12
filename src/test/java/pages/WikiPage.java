package pages;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class WikiPage  extends BasePage {

    private String
            startPageButtonLocator = ".mw-logo",
            documentationButtonLocator = "[role=\"navigation\"] li a[href=\"/documentation/\"]",
            startPageButtonText = "Start",
            documentationButtonText ="Documentation" ;

    public void startButtonShouldBeVisible() {
        assertTrue(locator(startPageButtonLocator).isVisible());
    }

    public WikiPage openMainPage() {
        getPage().navigate("https://en.wikipedia.org/wiki/Playwright");
        return this;
    }
}
