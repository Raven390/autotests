package tests.vindex_backoffice_ui_tests;

import io.qameta.allure.AllureId;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.io.IOException;

import static business_objects.api.mitigation_service.MitigationServiceRequest.disableCRMEmulator;
import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static utils.ConfigFactory.CRM_INEGRATION_USER_UCID;
import static utils.Constants.LAYER_WEB;
import static utils.Constants.TEAM_BACKOFFICE;

public class CRMRestrictionsPageTest extends TestBaseWeb {

    @BeforeEach
    public void CRMEmulationDisable() throws IOException {
        Response response = disableCRMEmulator();
        assertNotNull(response);
    }

    @AfterAll
    public static void CRMEmulationEnable() throws IOException {
        Response response = enableCRMEmulator();
        assertNotNull(response);
    }

//    @Test
//    void ddd() throws Exception {
//        RestrictionPage.cleanUserRestriction("vantage-10103201");
//        restrictionPage.cleanUserAudit("vantage-10103201");
//    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("345")
    @DisplayName("CRM restriction tab  account restriction UI")
    void CRMAccountRestrictionUITest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(CRM_INEGRATION_USER_UCID);
        restrictionPage.clickAccountSwitch();
        restrictionPage.fillApplyReason("test reason");
        restrictionPage.checkThatAccountIsChecked();
        restrictionPage.navigate(CRM_INEGRATION_USER_UCID);
        restrictionPage.checkThatAccountIsChecked();
        restrictionPage.clickCheckedAccount();
        restrictionPage.fillCancelReason("test reason");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("346")
    @DisplayName("CRM restriction tab transfer restriction UI")
    void CRMTransferRestrictionUITest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(CRM_INEGRATION_USER_UCID);
        restrictionPage.clickTransferSwitch();
        restrictionPage.fillApplyReason("test reason");
        restrictionPage.checkThatTransferIsChecked();
        restrictionPage.navigate(CRM_INEGRATION_USER_UCID);
        restrictionPage.checkThatTransferIsChecked();
        restrictionPage.clickCheckedTransfer();
        restrictionPage.fillCancelReason("test reason");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("347")
    @DisplayName("CRM restriction tab  Deposits restriction UI")
    void CRMDepositsRestrictionUITest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(CRM_INEGRATION_USER_UCID);
        restrictionPage.clickDepositsSwitch();
        restrictionPage.fillApplyReason("test reason");
        restrictionPage.checkThatDepositsIsChecked();
        restrictionPage.navigate(CRM_INEGRATION_USER_UCID);
        restrictionPage.checkThatDepositsIsChecked();
        restrictionPage.clickCheckedDeposits();
        restrictionPage.fillCancelReason("test reason");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("348")
    @DisplayName("CRM restriction tab Withdrawals restriction UI")
    void CRMWithdrawalsRestrictionUITest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(CRM_INEGRATION_USER_UCID);
        restrictionPage.clickWithdrawalsSwitch();
        restrictionPage.fillApplyReason("test reason");
        restrictionPage.checkThatWithdrawalsIsChecked();
        restrictionPage.navigate(CRM_INEGRATION_USER_UCID);
        restrictionPage.checkThatWithdrawalsIsChecked();
        restrictionPage.clickCheckedWithdrawals();
        restrictionPage.fillCancelReason("test reason");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("349")
    @DisplayName("CRM restriction tab Login CRM restriction UI")
    void CRMLoginCRMRestrictionUITest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(CRM_INEGRATION_USER_UCID);
        restrictionPage.clickLoginSwitch();
        restrictionPage.fillApplyReason("test reason");
        restrictionPage.checkThatLoginIsChecked();
        restrictionPage.navigate(CRM_INEGRATION_USER_UCID);
        restrictionPage.checkThatLoginIsChecked();
        restrictionPage.clickCheckedLogin();
        restrictionPage.fillCancelReason("test reason");
    }


    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("350")
    @DisplayName("CRM restriction tab Manual Withdrawal Review restriction UI")
    void CRMManualWithdrawalRestrictionUITest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(CRM_INEGRATION_USER_UCID);
        restrictionPage.clickManualWithdrawalSwitch();
        restrictionPage.fillApplyReason("test reason");
        restrictionPage.checkThatManualWithdrawalIsChecked();
        restrictionPage.navigate(CRM_INEGRATION_USER_UCID);
        restrictionPage.checkThatManualWithdrawalIsChecked();
        restrictionPage.clickCheckedManual();
        restrictionPage.fillCancelReason("test reason");
    }
}
