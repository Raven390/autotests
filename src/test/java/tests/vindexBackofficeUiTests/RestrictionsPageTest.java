package tests.vindexBackofficeUiTests;

import io.qameta.allure.AllureId;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.io.IOException;

import static businessObjects.api.mitigationService.MitigationServiceRequest.enableCRMEmulator;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static utils.Constants.*;

public class RestrictionsPageTest extends TestBaseWeb {

    @BeforeAll
    public static void CRMEmulation() throws IOException {
        Response response = enableCRMEmulator();
        assertNotNull(response);

    }

    @BeforeEach
    public void before() throws Exception {
        restrictionPage.cleanUserRestriction("infinox-141401");
        restrictionPage.cleanUserAudit("infinox-141401");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("327")
    @DisplayName("restriction tab set account restriction UI")
    void setAccountRestrictionUITest() throws Exception {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        restrictionPage.navigate("infinox-141401");
        restrictionPage.clickAccountSwitch();
        restrictionPage.fillApplyReason("test reason");
        restrictionPage.checkThatAccountIsChecked();
        restrictionPage.checkKafkaRequestApplyUCID("141401");
        restrictionPage.checkRestrictionApplymentAudit("infinox-141401", "Open new account");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("328")
    @DisplayName("restriction tab remove account restriction UI")
    void cancelAccountRestrictionUITest() throws Exception {
        restrictionPage.setRestrictionAPIGeneral("infinox-141401", "01");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        restrictionPage.navigate("infinox-141401");
        restrictionPage.checkThatAccountIsChecked();
        restrictionPage.clickCheckedAccount();
        restrictionPage.fillCancelReason("test reason");
        restrictionPage.checkKafkaRequestApplyUCID("141401");
        restrictionPage.checkRestrictionCancellationAudit("infinox-141401", "Open new account");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("329")
    @DisplayName("restriction tab set transfer restriction UI")
    void setTransferRestrictionUITest() throws Exception {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        restrictionPage.navigate("infinox-141401");
        restrictionPage.clickTransferSwitch();
        restrictionPage.fillApplyReason("test reason");
        restrictionPage.checkThatTransferIsChecked();
        restrictionPage.checkKafkaRequestApplyUCID("141401");
        restrictionPage.checkRestrictionApplymentAudit("infinox-141401", "Internal transfer");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("330")
    @DisplayName("restriction tab remove Transfer Restriction UI")
    void cancelTransferRestrictionUITest() throws Exception {
        restrictionPage.setRestrictionAPIGeneral("infinox-141401", "02");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        restrictionPage.navigate("infinox-141401");
        restrictionPage.checkThatTransferIsChecked();
        restrictionPage.clickCheckedTransfer();
        restrictionPage.fillCancelReason("test reason");
        restrictionPage.checkRestrictionCancellationAudit("infinox-141401", "Internal transfer");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("331")
    @DisplayName("restriction tab set Deposits restriction UI")
    void setDepositsRestrictionUITest() throws Exception {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        restrictionPage.navigate("infinox-141401");
        restrictionPage.clickDepositsSwitch();
        restrictionPage.fillApplyReason("test reason");
        restrictionPage.checkThatDepositsIsChecked();
        restrictionPage.checkKafkaRequestApplyUCID("141401");
        restrictionPage.checkRestrictionApplymentAudit("infinox-141401", "Deposits");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("332")
    @DisplayName("restriction tab remove Deposits restriction UI")
    void cancelDepositsRestrictionUITest() throws Exception {
        restrictionPage.setRestrictionAPIGeneral("infinox-141401", "03");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        restrictionPage.navigate("infinox-141401");
        restrictionPage.checkThatDepositsIsChecked();
        restrictionPage.clickCheckedDeposits();
        restrictionPage.fillCancelReason("test reason");
        restrictionPage.checkRestrictionCancellationAudit("infinox-141401", "Deposits");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("333")
    @DisplayName("restriction tab set Withdrawals restriction UI")
    void setWithdrawalsRestrictionUITest() throws Exception {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        restrictionPage.navigate("infinox-141401");
        restrictionPage.clickWithdrawalsSwitch();
        restrictionPage.fillApplyReason("test reason");
        restrictionPage.checkThatWithdrawalsIsChecked();
        restrictionPage.checkKafkaRequestApplyUCID("141401");
        restrictionPage.checkRestrictionApplymentAudit("infinox-141401", "Withdrawals");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("334")
    @DisplayName("restriction tab remove Withdrawals restriction UI")
    void cancelWithdrawalsRestrictionUITest() throws Exception {
        restrictionPage.setRestrictionAPIGeneral("infinox-141401", "04");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        restrictionPage.navigate("infinox-141401");
        restrictionPage.checkThatWithdrawalsIsChecked();
        restrictionPage.clickCheckedWithdrawals();
        restrictionPage.fillCancelReason("test reason");
        restrictionPage.checkRestrictionCancellationAudit("infinox-141401", "Withdrawals");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("335")
    @DisplayName("restriction tab set Login CRM restriction UI")
    void setLoginCRMRestrictionUITest() throws Exception {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        restrictionPage.navigate("infinox-141401");
        restrictionPage.clickLoginSwitch();
        restrictionPage.fillApplyReason("test reason");
        restrictionPage.checkThatLoginIsChecked();
        restrictionPage.checkKafkaRequestApplyUCID("141401");
        restrictionPage.checkRestrictionApplymentAudit("infinox-141401", "Login CRM");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("336")
    @DisplayName("restriction tab remove Login CRM restriction UI")
    void cancelLoginCRMRestrictionUITest() throws Exception {
        restrictionPage.setRestrictionAPIGeneral("infinox-141401", "05");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        restrictionPage.navigate("infinox-141401");
        restrictionPage.checkThatLoginIsChecked();
        restrictionPage.clickCheckedLogin();
        restrictionPage.fillCancelReason("test reason");
        restrictionPage.checkRestrictionCancellationAudit("infinox-141401", "Login CRM");
    }


    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("337")
    @DisplayName("restriction tab set Manual Withdrawal Review restriction UI")
    void setManualWithdrawalRestrictionUITest() throws Exception {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        restrictionPage.navigate("infinox-141401");
        restrictionPage.clickManualWithdrawalSwitch();
        restrictionPage.fillApplyReason("test reason");
        restrictionPage.checkThatManualWithdrawalIsChecked();
        restrictionPage.checkKafkaRequestApplyUCID("141401");
        restrictionPage.checkRestrictionApplymentAudit("infinox-141401", "Manual Withdrawal Review");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("338")
    @DisplayName("restriction tab remove Manual Withdrawal Review restriction UI client without transactions")
    void cancelManualWithdrawalRestrictionUITest() throws Exception {
        restrictionPage.setRestrictionAPIGeneral("infinox-141401", "12");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        restrictionPage.navigate("infinox-141401");
        restrictionPage.checkThatManualWithdrawalIsChecked();
        restrictionPage.clickCheckedManual();
        restrictionPage.fillCancelReason("test reason");
        restrictionPage.checkRestrictionCancellationAudit("infinox-141401", "Manual Withdrawal Review");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("361")
    @DisplayName("restriction tab remove Manual Withdrawal Review restriction UI client with transactions all green")
    void cancelManualWithdrawalRestrictionUITestWithTransactionsGreen() throws Exception {
        restrictionPage.cleanUserAudit("infinox-141402");
        restrictionPage.setRestrictionAPIGeneral("infinox-141402", "12");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        restrictionPage.navigate("infinox-141402");
        restrictionPage.checkThatManualWithdrawalIsChecked();
        restrictionPage.clickCheckedManual();
        restrictionPage.fillCancelReasonManualWithdrawalAllGreen("test reason");
        String details = "Transaction ID 141405;71.00 USDT 2024-11-13 10:11 bank trasfer; Accept";
        restrictionPage.checkRestrictionCancellationAudit("infinox-141402", "WD_REQUEST_DECISION", details);
        restrictionPage.checkKafkaRequestWithdrawal("141401", "5");
        restrictionPage.checkKafkaRequestApplyUCID("141402");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("365")
    @DisplayName("restriction tab remove Manual Withdrawal Review restriction UI client with transactions all refuse")
    void cancelManualWithdrawalRestrictionUITestWithTransactionsRefuse() throws Exception {
        restrictionPage.cleanUserAudit("infinox-141402");
        restrictionPage.setRestrictionAPIGeneral("infinox-141402", "12");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        restrictionPage.navigate("infinox-141402");
        restrictionPage.checkThatManualWithdrawalIsChecked();
        restrictionPage.clickCheckedManual();
        restrictionPage.fillCancelReasonManualWithdrawalAllrefuse("test reason");
        String details = "Transaction ID 141403; 71.00 USDT 2024-11-13 10:11 bank trasfer; Refuse";
        restrictionPage.checkRestrictionCancellationAudit("infinox-141402", "WD_REQUEST_DECISION", details);
        restrictionPage.checkKafkaRequestWithdrawal("141401", "4");
        restrictionPage.checkKafkaRequestApplyUCID("141402");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("366")
    @DisplayName("restriction tab remove Manual Withdrawal Review restriction UI client with transactions  approve one")
    void cancelManualWithdrawalRestrictionUITestWithTransactionsApproveOne() throws Exception {
        //login
        restrictionPage.cleanUserAudit("infinox-141402");
        restrictionPage.setRestrictionAPIGeneral("infinox-141402", "12");
        investigationPage.navigate();
        //first run
        keycloackPage.loginWeb("dev", "123");
        restrictionPage.navigate("infinox-141402");
        restrictionPage.checkThatManualWithdrawalIsChecked();
        restrictionPage.clickCheckedManual();
        restrictionPage.fillCancelReasonManualWithdrawalApproveOne("test reason");
        String details1 = "Transaction ID 141403; 71.00 USDT 2024-11-13 10:11 bank trasfer; Refuse";
        restrictionPage.checkRestrictionCancellationAudit("infinox-141402", "WD_REQUEST_DECISION", details1);
        restrictionPage.checkKafkaRequestWithdrawal("141401", "5");
        restrictionPage.checkKafkaRequestApplyUCID("141402");
        //second run
        restrictionPage.cleanUserAudit("infinox-141402");
        restrictionPage.setRestrictionAPIGeneral("infinox-141402", "12");
        restrictionPage.navigate("infinox-141402");
        restrictionPage.checkThatManualWithdrawalIsChecked();
        restrictionPage.clickCheckedManual();
        restrictionPage.fillCancelReasonManualWithdrawalApproveOne("test reason");
        String details2 = "Transaction ID 141401; 5.00 USD 2024-11-13 10:11 bank card; Accept";
        restrictionPage.checkRestrictionCancellationAudit("infinox-141402", "WD_REQUEST_DECISION", details2);
        restrictionPage.checkKafkaRequestWithdrawal("141402", "4");
        restrictionPage.checkKafkaRequestApplyUCID("141402");
    }


    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("339")
    @DisplayName("restriction tab set Close only mode Review restriction UI")
    void setCloseOnlyModeRestrictionUITest() throws Exception {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        restrictionPage.navigate("infinox-141401");
        restrictionPage.clickCloseOnlyModeSwitch();
        restrictionPage.fillApplyReasonTradingAllAccs("test reason");
        restrictionPage.checkThatCloseOnlyIsChecked();
//        restrictionPage.checkKafkaRequestApplyTradeUCID("141401");
        restrictionPage.checkRestrictionApplymentAudit("infinox-141401", "Close only mode; account: 14140101");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("340")
    @DisplayName("restriction tab remove Close only mode restriction UI")
    void cancelCloseOnlyModeRestrictionUITest() throws Exception {
        restrictionPage.setRestrictionAPITrade("infinox-141401", 14_140_101, 3, "06");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        restrictionPage.navigate("infinox-141401");
        restrictionPage.checkThatCloseOnlyIsChecked();
        restrictionPage.clickCheckedCloseOnly();
        restrictionPage.fillCancelReasonTrade("test reason");
        restrictionPage.checkRestrictionCancellationAudit("infinox-141401", "Close only mode; account: 14140101");
    }


    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("341")
    @DisplayName("restriction tab set Off quotes Review restriction UI")
    void setOffQuotesRestrictionUITest() throws Exception {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        restrictionPage.navigate("infinox-141401");
        restrictionPage.clickOffQuotesModeSwitch();
        restrictionPage.fillApplyReasonTradingAllAccs("test reason");
        restrictionPage.checkThatAOffQuotesIsChecked();
//        restrictionPage.checkKafkaRequestApplyTradeUCID("141401");
        restrictionPage.checkRestrictionApplymentAudit("infinox-141401", "Off quotes; account: 14140101");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("342")
    @DisplayName("restriction tab remove Off quotes restriction UI")
    void cancelOffQuotesRestrictionUITest() throws Exception {
        restrictionPage.setRestrictionAPITrade("infinox-141401", 14_140_101, 3, "08");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        restrictionPage.navigate("infinox-141401");
        restrictionPage.checkThatAOffQuotesIsChecked();
        restrictionPage.clickCheckedOffQuotes();
        restrictionPage.fillCancelReasonTrade("test reason");
        restrictionPage.checkRestrictionCancellationAudit("infinox-141401", "Off quotes; account: 14140101");
    }


    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("343")
    @DisplayName("restriction tab set AB book Review restriction UI")
    void setAbBookRestrictionUITest() throws Exception {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        restrictionPage.navigate("infinox-141401");
        restrictionPage.clickAbBookSwitch();
        restrictionPage.fillApplyReasonTradingAllAccs("test reason");
        restrictionPage.checkThatAbBookIsChecked();
//        restrictionPage.checkKafkaRequestApplyUCID("141401");
        restrictionPage.checkRestrictionApplymentAudit("infinox-141401", "B-Book -> A-Book; account: 14140101");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("344")
    @DisplayName("restriction tab remove AB book restriction UI")
    void cancelAbBookRestrictionUITest() throws Exception {
        restrictionPage.setRestrictionAPITrade("infinox-141401", 14_140_101, 3, "09");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        restrictionPage.navigate("infinox-141401");
        restrictionPage.checkThatAbBookIsChecked();
        restrictionPage.clickCheckedAbBook();
        restrictionPage.fillCancelReasonTrade("test reason");
        restrictionPage.checkRestrictionCancellationAudit("infinox-141401", "B-Book -> A-Book; account: 14140101");
    }
}
