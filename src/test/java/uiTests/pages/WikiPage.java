package uiTests.pages;

import io.qameta.allure.Step;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class WikiPage extends BasePage {

    private String
            startPageButtonLocator = ".mw-logo",
            documentationButtonLocator = "[role=\"navigation\"] li a[href=\"/documentation/\"]",
            startPageButtonText = "Start",
            documentationButtonText = "Documentation";

    @Step("check that start button is visible")
    public void startButtonShouldBeVisible() {
        assertTrue(locator(startPageButtonLocator).isVisible());
    }

    @Step("open main wikipedia page")
    public WikiPage openMainPage() {
        getPage().navigate("/wiki/Playwright");
        return this;
    }
}