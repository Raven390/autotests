package uiTests;

import org.junit.jupiter.api.Test;
import uiTests.pages.SitespeedPage;
import uiTests.pages.WikiPage;

public class SiteSpeedSiteTest extends TestBaseE2E {
//public class tests.SiteSpeedSiteTest extends pages.SitespeedPage {

SitespeedPage ssp = new SitespeedPage();
WikiPage wikiPage = new WikiPage();

//    @Test
//    public void oneTest(){
//        ssp.openMainPage();
//        ssp.checkNavBarButton(ssp.documentationButtonLocator, ssp.documentationButtonText);
//        ssp.checkNavBarButton(ssp.startPageButtonLocator, ssp.startPageButtonText);
//        wikiPage.openMainPage();
//    }

    @Test
    public void wikiTest() {
        wikiPage.openMainPage();
        wikiPage.startButtonShouldBeVisible();
    }
}
