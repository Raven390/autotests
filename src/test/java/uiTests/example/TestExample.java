package uiTests.example;

import static utils.Constants.*;

import io.qameta.allure.AllureId;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
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
    @Owner(OWNER_DMITRI_KALACHEV)
    @Tag(TEAM_BACKOFFICE)
    @Tag(STATUS_AUTOMATED)
    @Tag(FEATURE_EXAMPLE)
    @Tag(LAYER_WEB)
    @AllureId("")
    @DisplayName("gyouza1")
    void navToGyouza1() {
        WikiPage wikiPage = new WikiPage(page);
        wikiPage.navigateGyoza();
    }

    @Test
    @Owner(OWNER_DMITRI_KALACHEV)
    @Tag(STATUS_AUTOMATED)
    @Tag(FEATURE_EXAMPLE)
    @Tag(LAYER_WEB)
    @AllureId("")
    @DisplayName("cheese")
    void navToCheese() {
        WikiPage wikiPage = new WikiPage(page);
        wikiPage.navigateCheesea();
    }

    @Test
    @Owner(OWNER_DMITRI_KALACHEV)
    @Tag(TEAM_BACKOFFICE)
    @Tag(STATUS_AUTOMATED)
    @Tag(STATUS_AUTOMATED)
    @Tag(FEATURE_EXAMPLE)
    @Tag(LAYER_WEB)
    @AllureId("")
    @DisplayName("gyouza3")
    void navToGyouza3() {
        WikiPage wikiPage = new WikiPage(page);
        wikiPage.navigateGyoza();
    }
}
