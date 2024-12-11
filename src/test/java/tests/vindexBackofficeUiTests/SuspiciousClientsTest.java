package tests.vindexBackofficeUiTests;

import io.qameta.allure.AllureId;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import static businessObjects.ui.user.UserFactory.coreUser;
import static utils.Constants.*;

public class SuspiciousClientsTest extends TestBaseWeb {

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("525")
    @DisplayName("Verify that all elements are present for all suspicious clients")
    public void verifyAllElementsArePresentForSuspiciousClientsTest() throws InterruptedException {
        investigationPage.navigate();
        keycloackPage.loginAsCoreUser();
        investigationPage.waitForPageToLoad();
        // 'My clients' tab
        investigationPage.verifyEachClientHasBrandImg();
        investigationPage.verifyEachClientHasCountryCode();
        investigationPage.verifyEachClientHasClientId();
        investigationPage.verifyEachClientHasInvestigationStatusInvestigating();
        investigationPage.verifyEachClientAssignedToUser(coreUser());
        investigationPage.verifyEachClientHasCardTimer();
        investigationPage.verifyEachClientHasAlertCount();
        investigationPage.verifyClientCardsCount();
        // 'Unassigned' tab
        investigationPage.filterUnassigned();
        investigationPage.waitForPageToLoad();
        investigationPage.verifyEachClientHasBrandImg();
        investigationPage.verifyEachClientHasCountryCode();
        investigationPage.verifyEachClientHasClientId();
        investigationPage.verifyEachClientHasAnyInvestigationStatus();
        investigationPage.verifyEachClientHasCardTimer();
        investigationPage.verifyEachClientHasAlertCount();
        investigationPage.verifyClientCardsCount();
        // 'All' tab
        investigationPage.filterAll();
        investigationPage.waitForPageToLoad();
        investigationPage.verifyEachClientHasBrandImg();
        investigationPage.verifyEachClientHasCountryCode();
        investigationPage.verifyEachClientHasClientId();
        investigationPage.verifyEachClientHasAnyInvestigationStatus();
        investigationPage.verifyEachClientHasCardTimer();
        investigationPage.verifyEachClientHasAlertCount();
        investigationPage.verifyClientCardsCount();
    }
}
