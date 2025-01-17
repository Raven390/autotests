package tests.vindexBackofficeUiTests;


import businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObject;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.db.clickhouse.crmTbWithdrawal.CrmTbWithdrawalObject;
import helpers.data.ClientHelper;
import helpers.data.enums.Brand;
import helpers.data.enums.Regulator;
import io.qameta.allure.AllureId;
import okhttp3.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseWeb;

import java.util.ArrayList;
import java.util.List;

import static businessObjects.api.mitigationService.MitigationServiceRequest.enableCRMEmulator;
import static businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObjectFactory.generateStaticCrmTbAccountActive;
import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateStaticUserByClient;
import static businessObjects.db.clickhouse.crmTbWithdrawal.CrmTbWithdrawalObjectFactory.generateStaticWithdrawalByClient;
import static helpers.database.AuditHelper.cleanUserAudit;
import static helpers.database.BoHelper.*;
import static helpers.database.DbHelper.insertObjectToDb;
import static helpers.database.DbHelper.insertObjectsToDb;
import static helpers.kafka.alerts.CreateSimpleAlert.createSimpleAlert;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static utils.Constants.*;

public class ResolveTest extends TestBaseWeb {


    static ClientHelper withdrawalClient = new ClientHelper(141_402, "063cde3b-ea9d-48b5-8e2c-99f3d5f67999", Brand.INFINOX, Regulator.VFSC2, 14_140_102, 42);

    @BeforeAll
    public static void setup() throws Exception {
        CrmTbUserObject withdrawalClientDB = generateStaticUserByClient(withdrawalClient);
        CrmTbWithdrawalObject withdrawal1 = generateStaticWithdrawalByClient(withdrawalClient, "first withdrawal", 1);
        CrmTbWithdrawalObject withdrawal2 = generateStaticWithdrawalByClient(withdrawalClient, "second withdrawal", 2);
        CrmTbWithdrawalObject withdrawal3 = generateStaticWithdrawalByClient(withdrawalClient, "third withdrawal", 3);

        insertObjectToDb(CRM_USER_TABLE_NAME, withdrawalClientDB);

        List<CrmTbWithdrawalObject> withdrawals = new ArrayList<>();
        withdrawals.add(withdrawal1);
        withdrawals.add(withdrawal2);
        withdrawals.add(withdrawal3);
        insertObjectsToDb(CRM_WITHDRAWAL_TABLE_NAME, withdrawals);


        CrmTbAccountObject account = generateStaticCrmTbAccountActive(withdrawalClient);
        insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, account);
    }

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
        createSimpleAlert("infinox-141402", "CPA");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        investigationPage.navigateToClient("infinox-141402");
        resolvePage.openResolveSuspicious();
        resolvePage.resolveWithdrawalsAllApprove();
        String details = "Transaction ID 14140201; 71.00 USDT 2024-11-13 10:11 first withdrawal; Accept";
        restrictionPage.checkRestrictionCancellationAudit(withdrawalClient.getUcid(), "WD_REQUEST_DECISION", details);
        restrictionPage.checkKafkaRequestWithdrawal("14140201", "5");

    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("432")
    @DisplayName("resolve client with withdrawal transactions reject all")
    public void resolveWithWithdrawalsRejectAllTest() throws Exception {
        cleanUserAudit("infinox-141402");
//        restrictionPage.cleanKafka("infinox-141402");
        restrictionPage.cleanUserRestriction("infinox-141402");
        Response response = enableCRMEmulator();
        assertNotNull(response);
        restrictionPage.setRestrictionAPIGeneral("infinox-141402", "13");
        createSimpleAlert("infinox-141402", "CPA");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        investigationPage.navigateToClient("infinox-141402");
        resolvePage.openResolveSuspicious();
        resolvePage.resolveWithdrawalsAllReject();
        String details = "Transaction ID 14140201; 71.00 USDT 2024-11-13 10:11 first withdrawal; Refuse";
        restrictionPage.checkRestrictionCancellationAudit(withdrawalClient.getUcid(), "WD_REQUEST_DECISION", details);
        restrictionPage.checkKafkaRequestWithdrawal("14140201", "4");
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
        createSimpleAlert("infinox-141402", "CPA");
        investigationPage.navigateToClient("infinox-141402");
        resolvePage.openResolveSuspicious();
        resolvePage.resolveWithdrawalsApproveOneByType("first withdrawal");
        String details1 = "Transaction ID 14140201; 71.00 USDT 2024-11-13 10:11 first withdrawal; Accept";
        restrictionPage.checkRestrictionCancellationAudit(withdrawalClient.getUcid(), "WD_REQUEST_DECISION", details1);
        restrictionPage.checkKafkaRequestWithdrawal("14140201", "5");
        //second run
        cleanUserAudit("infinox-141402");
        restrictionPage.cleanUserRestriction("infinox-141402");
        Response response1 = enableCRMEmulator();
        assertNotNull(response1);
        restrictionPage.setRestrictionAPIGeneral("infinox-141402", "13");
        createSimpleAlert("infinox-141402", "CPA");
        investigationPage.navigateToClient("infinox-141402");
        resolvePage.openResolveSuspicious();
        resolvePage.resolveWithdrawalsApproveOneByType("first withdrawal");
        String details2 = "Transaction ID 14140203; 71.00 USDT 2024-11-13 10:11 third withdrawal; Refuse";
        restrictionPage.checkRestrictionCancellationAudit(withdrawalClient.getUcid(), "WD_REQUEST_DECISION", details2);
        restrictionPage.checkKafkaRequestWithdrawal("14140203", "4");
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
        createSimpleAlert(clientUcid, "CPA");
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
        createSimpleAlert(clientUcid, "CPA");
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
        createSimpleAlert(clientUcid, "AFFILIATE_ABUSE");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveSimple("test" + timestamp);
        checkUserFraudDb(clientUcid, 8);

    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("300")
    @DisplayName("BO user can resolve client in with fraud type CPA")
    public void resolveClientCpaTest() throws Exception {
        String clientUcid = "infinox-161601";
        deleteUserBO(clientUcid);
        cleanUserAudit(clientUcid);
        createSimpleAlert(clientUcid, "CPA");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveSimple("test" + timestamp);
        checkUserFraudDb(clientUcid, 7);

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
        createSimpleAlert(clientUcid, "GAP_TRADING");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveSimple("test" + timestamp);
        checkUserFraudDb(clientUcid, 5);
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
        createSimpleAlert(clientUcid, "HEDGING");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveSimple("test" + timestamp);
        checkUserFraudDb(clientUcid, 1);
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
        createSimpleAlert(clientUcid, "LATENCY_ARBITRAGE");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveSimple("test" + timestamp);
        checkUserFraudDb(clientUcid, 2);
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
        createSimpleAlert(clientUcid, "LOSS_VOUCHER_ABUSE");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveSimple("test" + timestamp);
        checkUserFraudDb(clientUcid, 11);
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
        createSimpleAlert(clientUcid, "MARKET_MANIPULATION");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveSimple("test" + timestamp);
        checkUserFraudDb(clientUcid, 3);
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
        createSimpleAlert(clientUcid, "NBP_ABUSE");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveSimple("test" + timestamp);
        checkUserFraudDb(clientUcid, 12);
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
        createSimpleAlert(clientUcid, "POTENTIAL_ABUSE");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveSimple("test" + timestamp);
        checkUserFraudDb(clientUcid, 14);
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
        createSimpleAlert(clientUcid, "PRICING_ERRORS");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveSimple("test" + timestamp);
        checkUserFraudDb(clientUcid, 4);
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
        createSimpleAlert(clientUcid, "RAF_ABUSE");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveSimple("test" + timestamp);
        checkUserFraudDb(clientUcid, 9);
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
        createSimpleAlert(clientUcid, "REBATE_CHURNING");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveSimple("test" + timestamp);
        checkUserFraudDb(clientUcid, 10);
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
        createSimpleAlert(clientUcid, "SWAP_ARBITRAGE");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveSimple("test" + timestamp);
        checkUserFraudDb(clientUcid, 6);
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
        createSimpleAlert(clientUcid, "TLS_ABUSE");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveSimple("test" + timestamp);
        checkUserFraudDb(clientUcid, 13);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("322")
    @DisplayName("BO user can resolve client in with multiple fraud types")
    public void resolveClientMultipleAbuseTest() throws Exception {
        String clientUcid = "infinox-161601";
        deleteUserBO(clientUcid);
        cleanUserAudit(clientUcid);
        createSimpleAlert(clientUcid, "TLS_ABUSE");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, "Hedging");
        checkUserFraudDb(clientUcid, 13);
        checkUserFraudDb(clientUcid, 1);
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
        createSimpleAlert(clientUcid, "TLS_ABUSE");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveNoFrauds("test" + timestamp);
        checkUserNoFraudDb(clientUcid);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("291")
    @DisplayName("BO user can't type more than a 250 symbols into resolve commentary section")
    public void cantTypeMoreThan250CommentTest() throws Exception {
        String clientUcid = "infinox-161601";
        deleteUserBO(clientUcid);
        cleanUserAudit(clientUcid);
        createSimpleAlert(clientUcid, "TLS_ABUSE");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.test250Symbols();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("233")
    @DisplayName("on resolve violated rule can be tagged as false positive")
    public void resolveFalsePositiveTest() throws Exception {
        String clientUcid = "infinox-161601";
        deleteUserBO(clientUcid);
        cleanUserAudit(clientUcid);
        createSimpleAlert(clientUcid, "TLS_ABUSE");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveNoFrauds("test" + timestamp);
        checkUserAlertConfirmation(clientUcid, false);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("289")
    @DisplayName("on resolve violated rule can be tagged as true positive")
    public void resolveTruePositiveTest() throws Exception {
        String clientUcid = "infinox-161601";
        deleteUserBO(clientUcid);
        cleanUserAudit(clientUcid);
        createSimpleAlert(clientUcid, "TLS_ABUSE");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveSimple("test" + timestamp);
        checkUserAlertConfirmation(clientUcid, true);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("250")
    @DisplayName("Resolve have actual list of violations")
    public void resolveViolationsListTest() throws Exception {
        String clientUcid = "infinox-161601";
        deleteUserBO(clientUcid);
        cleanUserAudit(clientUcid);
        createSimpleAlert(clientUcid, "TLS_ABUSE");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.checkFraudsList();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("238")
    @DisplayName("Resolve tab have info about currently applied restrictions")
    public void resolveRestrictionsListTest() throws Exception {
        //run 1
        String clientUcid = "infinox-161601";
        restrictionPage.cleanUserRestriction(clientUcid);
        deleteUserBO(clientUcid);
        cleanUserAudit(clientUcid);
        Response response = enableCRMEmulator();
        assertNotNull(response);
        restrictionPage.setRestrictionAPIGeneral(clientUcid, "01");
        createSimpleAlert(clientUcid, "TLS_ABUSE");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.checkRestrictionIsDisplayed("Open new account");
        //run 2
        restrictionPage.cleanUserRestriction(clientUcid);
        deleteUserBO(clientUcid);
        cleanUserAudit(clientUcid);
        Response response2 = enableCRMEmulator();
        assertNotNull(response2);
        restrictionPage.setRestrictionAPIGeneral(clientUcid, "05");
        createSimpleAlert(clientUcid, "TLS_ABUSE");
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.checkRestrictionIsDisplayed("Login CRM");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("744")
    @DisplayName("Resolve Flow. User can comment suspicious client outside of resolve screen")
    public void commentOutsideResolve() throws Exception {
        String clientUcid = "infinox-161601";
        String comment = "test" + timestamp;
        deleteUserBO(clientUcid);
        cleanUserAudit(clientUcid);
        createSimpleAlert(clientUcid, "CPA");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        investigationPage.openCommentForm();
        investigationPage.fillCommentForm(comment);
        investigationPage.submitCommentForm();

    }

}
