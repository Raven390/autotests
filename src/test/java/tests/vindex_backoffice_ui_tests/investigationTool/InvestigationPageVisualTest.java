package tests.vindex_backoffice_ui_tests.investigationTool;

import static utils.Constants.*;

import io.qameta.allure.AllureId;
import io.qameta.allure.Muted;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseWeb;

public class InvestigationPageVisualTest extends TestBaseWeb {

    @Test
    @Disabled // need to update mocking
    @Muted
    @Tag(TAG_MANUAL)
    @Tag(TEAM_BACKOFFICE)
    @Tag(TAG_AUTOMATED)
    @Tag(LAYER_WEB)
    @AllureId("")
    @DisplayName("Compare Alert Page with baseline Dark Mode, Unfolded Sidebar")
    void compareAScreenshotAPDMUS() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginWeb("DEV", "123");
        investigationPage.isLoggedIn();
        investigationPage.navigateMock();
        investigationPage.unfoldSidebar();
        investigationPage.turnDarkMode();
        investigationPage.compareAlertPageWithBaseline(page, "AP_DM_US.png");
    }

    @Test
    @Disabled // need to update mocking
    @Muted
    @Tag(TAG_MANUAL)
    @Tag(TEAM_BACKOFFICE)
    @Tag(TAG_AUTOMATED)
    @Tag(LAYER_WEB)
    @AllureId("")
    @DisplayName("Compare Alert Page with baseline Light Mode, Folded Sidebar")
    void compareAScreenshotAPLMFS() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginWeb("DEV", "123");
        investigationPage.isLoggedIn();
        investigationPage.navigateMock();
        investigationPage.foldSidebar();
        investigationPage.turnLightMode();
        investigationPage.compareAlertPageWithBaseline(page, "AP_DM_FS.png");
    }
}
