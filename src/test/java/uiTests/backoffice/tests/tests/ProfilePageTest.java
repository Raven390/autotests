package uiTests.backoffice.tests.tests;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uiTests.backoffice.tests.pageObjects.KeycloackPage;
import uiTests.backoffice.tests.pageObjects.ProfilePage;

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
