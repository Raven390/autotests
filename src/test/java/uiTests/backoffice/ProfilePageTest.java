package uiTests.backoffice;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pageObjects.backofficePages.KeycloackPage;
import pageObjects.backofficePages.ProfilePage;
import uiTests.TestBaseE2E;

public class ProfilePageTest extends TestBaseE2E {

    @Disabled
    @DisplayName("user can navigate to the profile page")
    @Test
    void navigateToProfilePage() {
        KeycloackPage keycloackPage = new KeycloackPage(page);
        ProfilePage profilePage = new ProfilePage(page);
        profilePage.navigate();
        keycloackPage.loginWEB("userName", "userPass");
        profilePage.isOnProfilePage();
    }

    @Disabled
    @DisplayName("user can log out from the profile page")
    @Test
    void LogOutFromProfilePage() {
        KeycloackPage keycloackPage = new KeycloackPage(page);
        ProfilePage profilePage = new ProfilePage(page);
        profilePage.navigate();
        keycloackPage.loginWEB("userName", "userPass");
        profilePage.clickLogoutButton();
        keycloackPage.isLoggedOut();
    }
}
