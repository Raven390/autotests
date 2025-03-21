package tests.vindex_backoffice_ui_tests;

import static utils.Constants.*;
import static utils.Constants.LAYER_WEB;

import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseWeb;

public class ProfilePageTest extends TestBaseWeb {

    @Disabled
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(TAG_AUTOMATED)
    @Tag(LAYER_WEB)
    @AllureId("")
    @DisplayName("user can navigate to the profile page")
    void navigateToProfilePage() {
        profilePage.navigate();
        keycloackPage.loginWeb("userName", "userPass");
        profilePage.isOnProfilePage();
    }

    @Disabled
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(TAG_AUTOMATED)
    @Tag(LAYER_WEB)
    @AllureId("")
    @DisplayName("user can log out from the profile page")
    void LogOutFromProfilePage() {
        profilePage.navigate();
        keycloackPage.loginWeb("userName", "userPass");
        profilePage.clickLogoutButton();
        keycloackPage.isLoggedOut();
    }
}
