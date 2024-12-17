package tests.vindexBackofficeUiTests;

import helpers.kafka.alerts.CreateSimpleAlert;
import io.qameta.allure.AllureId;
import okhttp3.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseWeb;

import static businessObjects.api.mitigationService.MitigationServiceRequest.enableCRMEmulator;
import static helpers.database.AuditHelper.cleanUserAudit;
import static helpers.database.BoHelper.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static utils.Constants.LAYER_WEB;
import static utils.Constants.TEAM_BACKOFFICE;

public class ResolveTest extends TestBaseWeb {

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("432")
    @DisplayName("resolve client with withdrawal transactions approve all")
    public void resolveWithWithdrawalsApproveAllTest() throws Exception {
        cleanUserAudit("infinox-141402");
        restrictionPage.cleanUserRestriction("infinox-141402");
        Response response = enableCRMEmulator();
        assertNotNull(response);
        restrictionPage.setRestrictionAPIGeneral("infinox-141402", "13");
        CreateSimpleAlert.createSimpleAlert("infinox-141402", "CPA");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        investigationPage.navigateToClient("infinox-141402");
        resolveScreen.openResolveSuspicious();
        resolveScreen.resolveWithdrawalsAllApprove();
        String details = "Transaction ID 141402; 5.00 USD 2024-11-13 10:11 crypto; Accept";
        restrictionPage.checkRestrictionCancellationAudit("infinox-141402", "WD_REQUEST_DECISION", details);
        restrictionPage.checkKafkaRequestWithdrawal("141402", "5");

    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("432")
    @DisplayName("resolve client with withdrawal transactions reject all")
    public void resolveWithWithdrawalsRejectAllTest() throws Exception {
        cleanUserAudit("infinox-141402");
        restrictionPage.cleanUserRestriction("infinox-141402");
        Response response = enableCRMEmulator();
        assertNotNull(response);
        restrictionPage.setRestrictionAPIGeneral("infinox-141402", "13");
        CreateSimpleAlert.createSimpleAlert("infinox-141402", "CPA");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        investigationPage.navigateToClient("infinox-141402");
        resolveScreen.openResolveSuspicious();
        resolveScreen.resolveWithdrawalsAllReject();
        String details = "Transaction ID 141404; 71.00 USDT 2024-11-13 10:11 bank trasfer; Refuse";
        restrictionPage.checkRestrictionCancellationAudit("infinox-141402", "WD_REQUEST_DECISION", details);
        restrictionPage.checkKafkaRequestWithdrawal("141401", "4");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("432")
    @DisplayName("resolve client with withdrawal transactions approve one")
    public void resolveWithWithdrawalsApproveOneTest() throws Exception {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        //first run
        cleanUserAudit("infinox-141402");
        restrictionPage.cleanUserRestriction("infinox-141402");
        Response response = enableCRMEmulator();
        assertNotNull(response);
        restrictionPage.setRestrictionAPIGeneral("infinox-141402", "13");
        CreateSimpleAlert.createSimpleAlert("infinox-141402", "CPA");
        investigationPage.navigateToClient("infinox-141402");
        resolveScreen.openResolveSuspicious();
        resolveScreen.resolveWithdrawalsApproveFirst();
        String details1 = "Transaction ID 141404; 71.00 USDT 2024-11-13 10:11 bank trasfer; Refuse";
        restrictionPage.checkRestrictionCancellationAudit("infinox-141402", "WD_REQUEST_DECISION", details1);
        restrictionPage.checkKafkaRequestWithdrawal("141402", "4");
        //second run
        cleanUserAudit("infinox-141402");
        restrictionPage.cleanUserRestriction("infinox-141402");
        Response response1 = enableCRMEmulator();
        assertNotNull(response1);
        restrictionPage.setRestrictionAPIGeneral("infinox-141402", "13");
        CreateSimpleAlert.createSimpleAlert("infinox-141402", "CPA");
        investigationPage.navigateToClient("infinox-141402");
        resolveScreen.openResolveSuspicious();
        resolveScreen.resolveWithdrawalsApproveFirst();
        String details2 = "Transaction ID 141401; 5.00 USD 2024-11-13 10:11 bank card; Accept";
        restrictionPage.checkRestrictionCancellationAudit("infinox-141402", "WD_REQUEST_DECISION", details2);
        restrictionPage.checkKafkaRequestWithdrawal("141401", "5");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("286")
    @DisplayName("BO user can assign suspicious client with the active alert to himself to perform investigation from the alert list")
    public void assignAlertListTest() throws Exception {
        String clientUcid = "infinox-161601";
        String clientId = "161601";
        deleteUserBO(clientUcid);
        cleanUserAudit(clientUcid);
        CreateSimpleAlert.createSimpleAlert(clientUcid, "CPA");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        investigationPage.navigate();
        investigationPage.filterUnassigned();
        investigationPage.investigateUserAlertList(clientId);
        investigationPage.checkInvestigationAssigmentAudit(clientUcid);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("226")
    @DisplayName("BO user can assign suspicious client with the active alert to himself to perform investigation from the client card")
    public void assignClientCardTest() throws Exception {
        String clientUcid = "infinox-161601";
        deleteUserBO(clientUcid);
        cleanUserAudit(clientUcid);
        CreateSimpleAlert.createSimpleAlert(clientUcid, "CPA");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        investigationPage.checkInvestigationAssigmentAudit(clientUcid);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("301")
    @DisplayName("BO user can resolve client in with fraud type Affiliate abuse")
    public void resolveClientAffiliateAbuseTest() throws Exception {
        String clientUcid = "infinox-161601";
        deleteUserBO(clientUcid);
        cleanUserAudit(clientUcid);
        CreateSimpleAlert.createSimpleAlert(clientUcid, "AFFILIATE_ABUSE");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolveScreen.openResolveSuspicious();
        resolveScreen.resolveSimple("test" + timestamp);
        checkUserFraudDB(clientUcid, 8);

    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("298")
    @DisplayName("BO user can resolve client in with fraud type Gap trading")
    public void resolveClientGapTradingTest() throws Exception {
        String clientUcid = "infinox-161601";
        deleteUserBO(clientUcid);
        cleanUserAudit(clientUcid);
        CreateSimpleAlert.createSimpleAlert(clientUcid, "GAP_TRADING");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolveScreen.openResolveSuspicious();
        resolveScreen.resolveSimple("test" + timestamp);
        checkUserFraudDB(clientUcid, 5);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("294")
    @DisplayName("BO user can resolve client in with fraud type HEDGING")
    public void resolveClientHedgingTest() throws Exception {
        String clientUcid = "infinox-161601";
        deleteUserBO(clientUcid);
        cleanUserAudit(clientUcid);
        CreateSimpleAlert.createSimpleAlert(clientUcid, "HEDGING");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolveScreen.openResolveSuspicious();
        resolveScreen.resolveSimple("test" + timestamp);
        checkUserFraudDB(clientUcid, 1);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("295")
    @DisplayName("BO user can resolve client in with fraud type LATENCY_ARBITRAGE")
    public void resolveClientGapLatencyArbitrageTest() throws Exception {
        String clientUcid = "infinox-161601";
        deleteUserBO(clientUcid);
        cleanUserAudit(clientUcid);
        CreateSimpleAlert.createSimpleAlert(clientUcid, "LATENCY_ARBITRAGE");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolveScreen.openResolveSuspicious();
        resolveScreen.resolveSimple("test" + timestamp);
        checkUserFraudDB(clientUcid, 2);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("304")
    @DisplayName("BO user can resolve client in with fraud type LOSS_VOUCHER_ABUSE")
    public void resolveClientGapLossVoucherAbuseTest() throws Exception {
        String clientUcid = "infinox-161601";
        deleteUserBO(clientUcid);
        cleanUserAudit(clientUcid);
        CreateSimpleAlert.createSimpleAlert(clientUcid, "LOSS_VOUCHER_ABUSE");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolveScreen.openResolveSuspicious();
        resolveScreen.resolveSimple("test" + timestamp);
        checkUserFraudDB(clientUcid, 11);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("296")
    @DisplayName("BO user can resolve client in with fraud type MARKET_MANIPULATION")
    public void resolveClientGapMarketManipulationTest() throws Exception {
        String clientUcid = "infinox-161601";
        deleteUserBO(clientUcid);
        cleanUserAudit(clientUcid);
        CreateSimpleAlert.createSimpleAlert(clientUcid, "MARKET_MANIPULATION");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolveScreen.openResolveSuspicious();
        resolveScreen.resolveSimple("test" + timestamp);
        checkUserFraudDB(clientUcid, 3);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("305")
    @DisplayName("BO user can resolve client in with fraud type NBP_ABUSE")
    public void resolveClientGapNBPAbuseTest() throws Exception {
        String clientUcid = "infinox-161601";
        deleteUserBO(clientUcid);
        cleanUserAudit(clientUcid);
        CreateSimpleAlert.createSimpleAlert(clientUcid, "NBP_ABUSE");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolveScreen.openResolveSuspicious();
        resolveScreen.resolveSimple("test" + timestamp);
        checkUserFraudDB(clientUcid, 12);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("307")
    @DisplayName("BO user can resolve client in with fraud type POTENTIAL_ABUSE")
    public void resolveClientGapPotentialAbuseTest() throws Exception {
        String clientUcid = "infinox-161601";
        deleteUserBO(clientUcid);
        cleanUserAudit(clientUcid);
        CreateSimpleAlert.createSimpleAlert(clientUcid, "POTENTIAL_ABUSE");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolveScreen.openResolveSuspicious();
        resolveScreen.resolveSimple("test" + timestamp);
        checkUserFraudDB(clientUcid, 14);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("297")
    @DisplayName("BO user can resolve client in with fraud type PRICING_ERRORS")
    public void resolveClientGapPricingErrorsTest() throws Exception {
        String clientUcid = "infinox-161601";
        deleteUserBO(clientUcid);
        cleanUserAudit(clientUcid);
        CreateSimpleAlert.createSimpleAlert(clientUcid, "PRICING_ERRORS");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolveScreen.openResolveSuspicious();
        resolveScreen.resolveSimple("test" + timestamp);
        checkUserFraudDB(clientUcid, 4);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("302")
    @DisplayName("BO user can resolve client in with fraud type RAF_ABUSE")
    public void resolveClientGapRAFAbuseTest() throws Exception {
        String clientUcid = "infinox-161601";
        deleteUserBO(clientUcid);
        cleanUserAudit(clientUcid);
        CreateSimpleAlert.createSimpleAlert(clientUcid, "RAF_ABUSE");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolveScreen.openResolveSuspicious();
        resolveScreen.resolveSimple("test" + timestamp);
        checkUserFraudDB(clientUcid, 9);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("303")
    @DisplayName("BO user can resolve client in with fraud type REBATE_CHURNING")
    public void resolveClientGapRebateChurningTest() throws Exception {
        String clientUcid = "infinox-161601";
        deleteUserBO(clientUcid);
        cleanUserAudit(clientUcid);
        CreateSimpleAlert.createSimpleAlert(clientUcid, "REBATE_CHURNING");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolveScreen.openResolveSuspicious();
        resolveScreen.resolveSimple("test" + timestamp);
        checkUserFraudDB(clientUcid, 10);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("299")
    @DisplayName("BO user can resolve client in with fraud type SWAP_ARBITRAGE")
    public void resolveClientSwapArbitrageTest() throws Exception {
        String clientUcid = "infinox-161601";
        deleteUserBO(clientUcid);
        cleanUserAudit(clientUcid);
        CreateSimpleAlert.createSimpleAlert(clientUcid, "SWAP_ARBITRAGE");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolveScreen.openResolveSuspicious();
        resolveScreen.resolveSimple("test" + timestamp);
        checkUserFraudDB(clientUcid, 6);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("306")
    @DisplayName("BO user can resolve client in with fraud type TLS_ABUSE")
    public void resolveClientTLSAbuseTest() throws Exception {
        String clientUcid = "infinox-161601";
        deleteUserBO(clientUcid);
        cleanUserAudit(clientUcid);
        CreateSimpleAlert.createSimpleAlert(clientUcid, "TLS_ABUSE");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolveScreen.openResolveSuspicious();
        resolveScreen.resolveSimple("test" + timestamp);
        checkUserFraudDB(clientUcid, 13);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("325")
    @DisplayName("BO user can resolve client without any applied fraud")
    public void resolveClientNoFraudTest() throws Exception {
        String clientUcid = "infinox-161601";
        deleteUserBO(clientUcid);
        cleanUserAudit(clientUcid);
        CreateSimpleAlert.createSimpleAlert(clientUcid, "TLS_ABUSE");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolveScreen.openResolveSuspicious();
        resolveScreen.resolveNoFrauds("test" + timestamp);
        checkUserNoFraudDb(clientUcid);
    }
}
