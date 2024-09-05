package uiTests.example;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pageObjects.examplePages.SearchPage;
import pageObjects.examplePages.WikiPage;
import uiTests.TestBaseE2E;

public class TestExample extends TestBaseE2E {

    @Disabled("write roundabout around captcha is too costly")
    @Test
    void shouldSearchWiki() {
        SearchPage searchPage = new SearchPage(page);
        searchPage.navigate();
        searchPage.search("wikipedia");
        searchPage.clickFirst();
        WikiPage wikiPage = new WikiPage(page);
        wikiPage.checkLogo();
    }

    @Test
    @DisplayName("gyouza1")
    void navToGyouza1() {
        WikiPage wikiPage = new WikiPage(page);
        wikiPage.navigateGyoza();
    }

    @Test
    @DisplayName("gyouza2")
    void navToGyouza2() {
        WikiPage wikiPage = new WikiPage(page);
        wikiPage.navigateGyoza();
    }

    @Test
    @DisplayName("gyouza3")
    void navToGyouza3() {
        WikiPage wikiPage = new WikiPage(page);
        wikiPage.navigateGyoza();
    }
}
