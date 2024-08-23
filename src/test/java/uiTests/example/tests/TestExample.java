package uiTests.example.tests;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import uiTests.example.PageObjects.SearchPage;
import uiTests.example.PageObjects.WikiPage;

public class TestExample extends TestBaseE2E {

    @Disabled("write roundabout around capcha is too costly")
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
    void navToGyouza() {
        WikiPage wikiPage = new WikiPage(page);
        wikiPage.navigateGyoza();
    }
}
