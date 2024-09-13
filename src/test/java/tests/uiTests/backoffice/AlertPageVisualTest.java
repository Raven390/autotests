package tests.uiTests.backoffice;

import static utils.Constants.*;

import io.qameta.allure.AllureId;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pageObjects.backofficePages.AlertPage;
import pageObjects.backofficePages.KeycloackPage;
import tests.TestBaseWeb;

public class AlertPageVisualTest extends TestBaseWeb {

    @Test
    @Owner(OWNER_DMITRI_KALACHEV)
    @Tag(TEAM_BACKOFFICE)
    @Tag(STATUS_AUTOMATED)
    @Tag(LAYER_WEB)
    @AllureId("")
    @DisplayName("compare Alert Page with etalon Dark Mode, Unfolded Sidebar")
    void compareAScreenshotAPDMUS() throws InterruptedException {
        AlertPage alertPage = new AlertPage(page);
        alertPage.navigate();
        KeycloackPage keycloackPage = new KeycloackPage(page);
        keycloackPage.loginWEB("DEV", "123");
        alertPage.isLoggedIn();
        alertPage.navigateMock();
        alertPage.foldSidebar();
        alertPage.turnDarkMode();
        comparePageScreenshot("visual-comparsion/baseline-screenshots/AP_DM_US.png");
    }

    @Test
    @Owner(OWNER_DMITRI_KALACHEV)
    @Tag(TEAM_BACKOFFICE)
    @Tag(STATUS_AUTOMATED)
    @Tag(LAYER_WEB)
    @AllureId("")
    @DisplayName("compare Alert Page with etalon Light Mode, Folded Sidebar")
    void compareAScreenshotAPLMFS() throws InterruptedException {
        AlertPage alertPage = new AlertPage(page);
        alertPage.navigate();
        KeycloackPage keycloackPage = new KeycloackPage(page);
        keycloackPage.loginWEB("DEV", "123");
        alertPage.isLoggedIn();
        alertPage.navigateMock();
        alertPage.foldSidebar();
        alertPage.turnLightMode();
    }
}
