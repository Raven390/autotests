package tests.vindex_backoffice_ui_tests;


import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalObject;
import helpers.data.ClientHelper;
import helpers.data.enums.Brand;
import helpers.data.enums.FraudType;
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

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateStaticCrmTbAccountActive;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalObjectFactory.generateStaticWithdrawalByClient;
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
        cleanUserAudit(withdrawalClient.getUcid());
        restrictionPage.cleanUserRestriction(withdrawalClient.getUcid());
        Response response = enableCRMEmulator();
        assertNotNull(response);
        restrictionPage.setRestrictionAPIGeneral(withdrawalClient.getUcid(), "13");
        createSimpleAlert(withdrawalClient.getUcid(), FraudType.HEDGING.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient("infinox-141402");
        resolvePage.openResolveSuspicious();
        resolvePage.resolveWithdrawalsAllApprove();
        String details = "Transaction ID 14140201; 1.00 USD 2024-10-13 12:03 first withdrawal; Approve";
        restrictionPage.checkRestrictionCancellationAuditBO(withdrawalClient.getUcid(), "WD_REQUEST_DECISION", details);
        restrictionPage.checkKafkaRequestWithdrawal("14140201", "Approve");

    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("432")
    @DisplayName("resolve client with withdrawal transactions reject all")
    public void resolveWithWithdrawalsRejectAllTest() throws Exception {
        cleanUserAudit(withdrawalClient.getUcid());
        restrictionPage.cleanUserRestriction(withdrawalClient.getUcid());
        Response response = enableCRMEmulator();
        assertNotNull(response);
        restrictionPage.setRestrictionAPIGeneral(withdrawalClient.getUcid(), "13");
        createSimpleAlert(withdrawalClient.getUcid(), FraudType.HEDGING.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient("infinox-141402");
        resolvePage.openResolveSuspicious();
        resolvePage.resolveWithdrawalsAllReject();
        String details = "Transaction ID 14140202; 1.00 USD 2024-10-13 12:03 second withdrawal; Refuse";
        restrictionPage.checkRestrictionCancellationAuditBO(withdrawalClient.getUcid(), "WD_REQUEST_DECISION", details);
        restrictionPage.checkKafkaRequestWithdrawal("14140201", "Refuse");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("432")
    @DisplayName("resolve client with withdrawal transactions approve one")
    public void resolveWithWithdrawalsApproveOneTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        //first run
        cleanUserAudit(withdrawalClient.getUcid());
        restrictionPage.cleanUserRestriction(withdrawalClient.getUcid());
        Response response = enableCRMEmulator();
        assertNotNull(response);
        restrictionPage.setRestrictionAPIGeneral(withdrawalClient.getUcid(), "13");
        createSimpleAlert(withdrawalClient.getUcid(), FraudType.HEDGING.getKey());
        investigationPage.navigateToClient(withdrawalClient.getUcid());
        resolvePage.openResolveSuspicious();
        resolvePage.resolveWithdrawalsApproveOneByType("first withdrawal");
        String details1 = "Transaction ID 14140201; 1.00 USD 2024-10-13 12:03 first withdrawal; Approve";
        restrictionPage.checkRestrictionCancellationAuditBO(withdrawalClient.getUcid(), "WD_REQUEST_DECISION", details1);
        restrictionPage.checkKafkaRequestWithdrawal("14140201", "Approve");
        //second run
        cleanUserAudit(withdrawalClient.getUcid());
        restrictionPage.cleanUserRestriction(withdrawalClient.getUcid());
        Response response1 = enableCRMEmulator();
        assertNotNull(response1);
        restrictionPage.setRestrictionAPIGeneral(withdrawalClient.getUcid(), "13");
        createSimpleAlert(withdrawalClient.getUcid(), FraudType.HEDGING.getKey());
        investigationPage.navigateToClient(withdrawalClient.getUcid());
        resolvePage.openResolveSuspicious();
        resolvePage.resolveWithdrawalsApproveOneByType("first withdrawal");
        String details2 = "Transaction ID 14140202; 1.00 USD 2024-10-13 12:03 second withdrawal; Refuse";
        restrictionPage.checkRestrictionCancellationAuditBO(withdrawalClient.getUcid(), "WD_REQUEST_DECISION", details2);
        restrictionPage.checkKafkaRequestWithdrawal("14140202", "Refuse");
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
        createSimpleAlert(clientUcid, FraudType.CPA_ABUSE.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateEnterPage();
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
        createSimpleAlert(clientUcid, FraudType.CPA_ABUSE.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        investigationPage.checkInvestigationAssigmentAudit(clientUcid);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("301")
    @DisplayName("BO user can resolve client in with fraud type BONUS_ABUSE")
    public void resolveClientBonusAbuseTest() throws Exception {
        String clientUcid = "infinox-161601";
        deleteUserBO(clientUcid);
        FraudType fraud = FraudType.BONUS_ABUSE;
        cleanUserAudit(clientUcid);
        createSimpleAlert(clientUcid, fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud.getDisplayName());
        checkUserFraudDb(clientUcid, fraud.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("300")
    @DisplayName("BO user can resolve client in with fraud type CPA_ABUSE")
    public void resolveClientCpaTest() throws Exception {
        String clientUcid = "infinox-161601";
        deleteUserBO(clientUcid);
        FraudType fraud = FraudType.CPA_ABUSE;
        cleanUserAudit(clientUcid);
        createSimpleAlert(clientUcid, fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud.getDisplayName());
        checkUserFraudDb(clientUcid, fraud.getFraudTypeId());

    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("298")
    @DisplayName("BO user can resolve client in with fraud type GAP_TRADING")
    public void resolveClientGapTradingTest() throws Exception {
        String clientUcid = "infinox-161601";
        deleteUserBO(clientUcid);
        FraudType fraud = FraudType.GAP_TRADING;
        cleanUserAudit(clientUcid);
        createSimpleAlert(clientUcid, fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud.getDisplayName());
        checkUserFraudDb(clientUcid, fraud.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("294")
    @DisplayName("BO user can resolve client in with fraud type HEDGING")
    public void resolveClientHedgingTest() throws Exception {
        String clientUcid = "infinox-161601";
        deleteUserBO(clientUcid);
        FraudType fraud = FraudType.HEDGING;
        cleanUserAudit(clientUcid);
        createSimpleAlert(clientUcid, fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud.getDisplayName());
        checkUserFraudDb(clientUcid, fraud.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("295")
    @DisplayName("BO user can resolve client in with fraud type LATENCY_ARBITRAGE")
    public void resolveClientGapLatencyArbitrageTest() throws Exception {
        String clientUcid = "infinox-161601";
        deleteUserBO(clientUcid);
        FraudType fraud = FraudType.LATENCY_ARBITRAGE;
        cleanUserAudit(clientUcid);
        createSimpleAlert(clientUcid, fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud.getDisplayName());
        checkUserFraudDb(clientUcid, fraud.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("304")
    @DisplayName("BO user can resolve client in with fraud type LOSS_VOUCHER_ABUSE")
    public void resolveClientGapLossVoucherAbuseTest() throws Exception {
        String clientUcid = "infinox-161601";
        deleteUserBO(clientUcid);
        FraudType fraud = FraudType.LOSS_VOUCHER_ABUSE;
        cleanUserAudit(clientUcid);
        createSimpleAlert(clientUcid, fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud.getDisplayName());
        checkUserFraudDb(clientUcid, fraud.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("296")
    @DisplayName("BO user can resolve client in with fraud type MARKET_MANIPULATION")
    public void resolveClientGapMarketManipulationTest() throws Exception {
        String clientUcid = "infinox-161601";
        deleteUserBO(clientUcid);
        FraudType fraud = FraudType.MARKET_MANIPULATION;
        cleanUserAudit(clientUcid);
        createSimpleAlert(clientUcid, fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud.getDisplayName());
        checkUserFraudDb(clientUcid, fraud.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("305")
    @DisplayName("BO user can resolve client in with fraud type NBP_ABUSE")
    public void resolveClientGapNBPAbuseTest() throws Exception {
        String clientUcid = "infinox-161601";
        deleteUserBO(clientUcid);
        FraudType fraud = FraudType.NBP_ABUSE;
        cleanUserAudit(clientUcid);
        createSimpleAlert(clientUcid, fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud.getDisplayName());
        checkUserFraudDb(clientUcid, fraud.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("307")
    @DisplayName("BO user can resolve client in with fraud type POTENTIAL_ABUSE")
    public void resolveClientGapPotentialAbuseTest() throws Exception {
        String clientUcid = "infinox-161601";
        deleteUserBO(clientUcid);
        FraudType fraud = FraudType.POTENTIAL_ABUSE;
        cleanUserAudit(clientUcid);
        createSimpleAlert(clientUcid, fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud.getDisplayName());
        checkUserFraudDb(clientUcid, fraud.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("297")
    @DisplayName("BO user can resolve client in with fraud type PRICING_ERRORS")
    public void resolveClientGapPricingErrorsTest() throws Exception {
        String clientUcid = "infinox-161601";
        deleteUserBO(clientUcid);
        FraudType fraud = FraudType.PRICING_ERROR;
        cleanUserAudit(clientUcid);
        createSimpleAlert(clientUcid, fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud.getDisplayName());
        checkUserFraudDb(clientUcid, fraud.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("302")
    @DisplayName("BO user can resolve client in with fraud type RAF_ABUSE")
    public void resolveClientGapRAFAbuseTest() throws Exception {
        String clientUcid = "infinox-161601";
        deleteUserBO(clientUcid);
        FraudType fraud = FraudType.RAF_ABUSE;
        cleanUserAudit(clientUcid);
        createSimpleAlert(clientUcid, fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud.getDisplayName());
        checkUserFraudDb(clientUcid, fraud.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("303")
    @DisplayName("BO user can resolve client in with fraud type REBATE_CHURNING")
    public void resolveClientGapRebateChurningTest() throws Exception {
        String clientUcid = "infinox-161601";
        deleteUserBO(clientUcid);
        FraudType fraud = FraudType.REBATE_CHURNING;
        cleanUserAudit(clientUcid);
        createSimpleAlert(clientUcid, fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud.getDisplayName());
        checkUserFraudDb(clientUcid, fraud.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("299")
    @DisplayName("BO user can resolve client in with fraud type SWAP_ARBITRAGE")
    public void resolveClientSwapArbitrageTest() throws Exception {
        String clientUcid = "infinox-161601";
        deleteUserBO(clientUcid);
        FraudType fraud = FraudType.SWAP_ARBITRAGE;
        cleanUserAudit(clientUcid);
        createSimpleAlert(clientUcid, fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud.getDisplayName());
        checkUserFraudDb(clientUcid, fraud.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("306")
    @DisplayName("BO user can resolve client in with fraud type TLS_ABUSE")
    public void resolveClientTLSAbuseTest() throws Exception {
        String clientUcid = "infinox-161601";
        deleteUserBO(clientUcid);
        FraudType fraud = FraudType.TLS_ABUSE;
        cleanUserAudit(clientUcid);
        createSimpleAlert(clientUcid, fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud.getDisplayName());
        checkUserFraudDb(clientUcid, fraud.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("762")
    @DisplayName("BO user can resolve client in with fraud type LOOPHOLE_ABUSE")
    public void resolveClientLoopholeAbuseTest() throws Exception {
        String clientUcid = "infinox-161601";
        deleteUserBO(clientUcid);
        FraudType fraud = FraudType.LOOPHOLE_ABUSE;
        cleanUserAudit(clientUcid);
        createSimpleAlert(clientUcid, fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud.getDisplayName());
        checkUserFraudDb(clientUcid, fraud.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("763")
    @DisplayName("BO user can resolve client in with fraud type HFT_ABUSE")
    public void resolveClientHfyAbuseTest() throws Exception {
        String clientUcid = "infinox-161601";
        deleteUserBO(clientUcid);
        FraudType fraud = FraudType.HFT_ABUSE;
        cleanUserAudit(clientUcid);
        createSimpleAlert(clientUcid, fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud.getDisplayName());
        checkUserFraudDb(clientUcid, fraud.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("764")
    @DisplayName("BO user can resolve client in with fraud type NEWS_TRADER")
    public void resolveClientNewsTraderAbuseTest() throws Exception {
        String clientUcid = "infinox-161601";
        deleteUserBO(clientUcid);
        FraudType fraud = FraudType.NEWS_TRADER;
        cleanUserAudit(clientUcid);
        createSimpleAlert(clientUcid, fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud.getDisplayName());
        checkUserFraudDb(clientUcid, fraud.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("765")
    @DisplayName("BO user can resolve client in with fraud type ANOMALOUS_PROFIT")
    public void resolveClientAnomalousProfitAbuseTest() throws Exception {
        String clientUcid = "infinox-161601";
        deleteUserBO(clientUcid);
        FraudType fraud = FraudType.ANOMALOUS_PROFIT;
        cleanUserAudit(clientUcid);
        createSimpleAlert(clientUcid, fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud.getDisplayName());
        checkUserFraudDb(clientUcid, fraud.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("322")
    @DisplayName("BO user can resolve client in with multiple fraud types")
    public void resolveClientMultipleAbuseTest() throws Exception {
        String clientUcid = "infinox-161601";
        FraudType fraud1 = FraudType.HEDGING;
        FraudType fraud2 = FraudType.TLS_ABUSE;
        deleteUserBO(clientUcid);
        cleanUserAudit(clientUcid);
        createSimpleAlert(clientUcid, fraud1.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddMultipleFraud("test" + timestamp, fraud1.getDisplayName(), fraud2.getDisplayName());
        checkUserFraudDb(clientUcid, fraud1.getFraudTypeId());
        checkUserFraudDb(clientUcid, fraud2.getFraudTypeId());
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
        createSimpleAlert(clientUcid, FraudType.TLS_ABUSE.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
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
        createSimpleAlert(clientUcid, FraudType.TLS_ABUSE.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
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
        createSimpleAlert(clientUcid, FraudType.TLS_ABUSE.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
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
        createSimpleAlert(clientUcid, FraudType.TLS_ABUSE.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, FraudType.TLS_ABUSE.getDisplayName());
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
        createSimpleAlert(clientUcid, FraudType.TLS_ABUSE.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
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
        createSimpleAlert(clientUcid, FraudType.TLS_ABUSE.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
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
        createSimpleAlert(clientUcid, FraudType.TLS_ABUSE.getKey());
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
        createSimpleAlert(clientUcid, FraudType.CPA_ABUSE.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(clientUcid);
        investigationPage.investigateClientCard();
        investigationPage.openCommentForm();
        investigationPage.fillCommentForm(comment);
        investigationPage.submitCommentForm();

    }

}
