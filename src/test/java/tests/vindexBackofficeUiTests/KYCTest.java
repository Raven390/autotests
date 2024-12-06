package tests.vindexBackofficeUiTests;

import io.qameta.allure.AllureId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseWeb;

import static utils.Constants.LAYER_WEB;
import static utils.Constants.TEAM_BACKOFFICE;

public class KYCTest extends TestBaseWeb {

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("351")
    @DisplayName("Check correct status display Submitted")
    public void checkCorrectStatusDisplaySubmitted() {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        generalPage.navigateGeneralTab("infinox-525206");
        generalPage.checkKycStatusGeneral("Proof of identity", "Submitted");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("354")
    @DisplayName("Check correct status display Rejected")
    public void checkCorrectStatusDisplayRejected() {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        generalPage.navigateGeneralTab("infinox-525208");
        generalPage.checkKycStatusGeneral("Proof of identity", "Rejected");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("352")
    @DisplayName("Check correct status display Pending")
    public void checkCorrectStatusDisplayPending() {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        generalPage.navigateGeneralTab("infinox-525207");
        generalPage.checkKycStatusGeneral("Proof of identity", "Pending");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("353")
    @DisplayName("Check correct status display Completed")
    public void checkCorrectStatusDisplayCompleted() {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        generalPage.navigateGeneralTab("infinox-525205");
        generalPage.checkKycStatusGeneral("Proof of identity", "Approved");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("357")
    @DisplayName("KYC File viewer BO user can zoom displayed file using buttons in UI")
    public void userCanZoom() {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        generalPage.navigateGeneralTab("infinox-525204");
        generalPage.kycDetailsOpen("Proof of identity");
        generalPage.FVZoomFunctions();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("358")
    @DisplayName("KYC File viewer BO user can rotate displayed file using buttons in UI")
    public void userCanRotate() {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        generalPage.navigateGeneralTab("infinox-525204");
        generalPage.kycDetailsOpen("Proof of identity");
        generalPage.FVRotateFunctions();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("359")
    @DisplayName("KYC File viewer BO user can mirror displayed file using buttons in UI")
    public void userCanMirror() {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        generalPage.navigateGeneralTab("infinox-525204");
        generalPage.kycDetailsOpen("Proof of identity");
        generalPage.FVMirrorFunctions();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("356")
    @DisplayName("KYC File viewer BO user can slide displayed file using buttons in UI")
    public void userCanSlide() {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        generalPage.navigateGeneralTab("infinox-525204");
        generalPage.kycDetailsOpen("Proof of identity");
        generalPage.FVSlideFunctions();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("326")
    @DisplayName("User has history drawer")
    public void userHasHistoryDrawer() {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        generalPage.navigateGeneralTab("infinox-525204");
        generalPage.kycDetailsOpen("Proof of identity");
        generalPage.kycHistoryDrawerDisplayed();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("318")
    @DisplayName("Client without KYC applyment must have placeholder")
    public void userHavePlaceholderNoKYC() {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        generalPage.navigateGeneralTab("infinox-525201");
        generalPage.noAppliedIsVisible();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("321")
    @DisplayName("Client without multiple KYC attempts must have displayed number of attempts")
    public void userHaveNumberOfAttempt() {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        generalPage.navigateGeneralTab("infinox-525204");
        generalPage.checkKycAttemptsGeneral("Proof of identity", "2");
        generalPage.checkKycAttemptsGeneral("Proof of address", "2");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("320")
    @DisplayName("Client applied ID must have address info on general tab")
    public void clientHaveAddressInfoGeneral() {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        generalPage.navigateGeneralTab("infinox-525204");
        generalPage.poaDetailsGeneral("USA, DC, Washington", "321 Main St, 654321");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("319")
    @DisplayName("Client applied only POI must have placeholder about POA")
    public void clientHavePlaceholderPOA() {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        generalPage.navigateGeneralTab("infinox-525202");
        generalPage.poaPlaceholderIsVisible();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("320")
    @DisplayName("Client have placeholder if not applied POI")
    public void clientHavePlaceholderPOI() {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        generalPage.navigateGeneralTab("infinox-525203");
        generalPage.poiPlaceholderIsVisible();
    }
}
