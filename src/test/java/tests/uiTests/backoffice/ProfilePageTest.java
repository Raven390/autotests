package tests.uiTests.backoffice;

import static utils.Constants.*;
import static utils.Constants.LAYER_WEB;

import io.qameta.allure.AllureId;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pageObjects.backofficePages.KeycloackPage;
import pageObjects.backofficePages.ProfilePage;
import tests.TestBaseWeb;

public class ProfilePageTest extends TestBaseWeb {

    @Disabled
    @Test
    @Owner(OWNER_DMITRI_KALACHEV)
    @Tag(TEAM_BACKOFFICE)
    @Tag(STATUS_AUTOMATED)
    @Tag(LAYER_WEB)
    @AllureId("")
    @DisplayName("user can navigate to the profile page")
    void navigateToProfilePage() {
        KeycloackPage keycloackPage = new KeycloackPage(page);
        ProfilePage profilePage = new ProfilePage(page);
        profilePage.navigate();
        keycloackPage.loginWEB("userName", "userPass");
        profilePage.isOnProfilePage();
    }

    @Disabled
    @Test
    @Owner(OWNER_DMITRI_KALACHEV)
    @Tag(TEAM_BACKOFFICE)
    @Tag(STATUS_AUTOMATED)
    @Tag(LAYER_WEB)
    @AllureId("")
    @DisplayName("user can log out from the profile page")
    void LogOutFromProfilePage() {
        KeycloackPage keycloackPage = new KeycloackPage(page);
        ProfilePage profilePage = new ProfilePage(page);
        profilePage.navigate();
        keycloackPage.loginWEB("userName", "userPass");
        profilePage.clickLogoutButton();
        keycloackPage.isLoggedOut();
    }
}
