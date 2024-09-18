package tests.uiTests.backoffice;

import static utils.Constants.*;

import io.qameta.allure.AllureId;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseWeb;

public class AlertPageVisualTest extends TestBaseWeb {

    @Test
    @Owner(OWNER_DMITRI_KALACHEV)
    @Tag(TEAM_BACKOFFICE)
    @Tag(STATUS_AUTOMATED)
    @Tag(LAYER_WEB)
    @AllureId("")
    @DisplayName("Compare Alert Page with baseline Dark Mode, Unfolded Sidebar")
    void compareAScreenshotAPDMUS() {
        alertPage.navigate();
        keycloackPage.loginWEB("DEV", "123");
        alertPage.isLoggedIn();
        alertPage.navigateMock();
        alertPage.unfoldSidebar();
        alertPage.turnDarkMode();
        alertPage.compareAlertPageWithBaseline(page, "AP_DM_US.png");
    }

    @Test
    @Owner(OWNER_DMITRI_KALACHEV)
    @Tag(TEAM_BACKOFFICE)
    @Tag(STATUS_AUTOMATED)
    @Tag(LAYER_WEB)
    @AllureId("")
    @DisplayName("Compare Alert Page with baseline Light Mode, Folded Sidebar")
    void compareAScreenshotAPLMFS() {
        alertPage.navigate();
        keycloackPage.loginWEB("DEV", "123");
        alertPage.isLoggedIn();
        alertPage.navigateMock();
        alertPage.foldSidebar();
        alertPage.turnLightMode();
        alertPage.compareAlertPageWithBaseline(page, "AP_DM_FS.png");
    }
}
