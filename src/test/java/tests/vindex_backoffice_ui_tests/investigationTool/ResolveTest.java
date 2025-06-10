package tests.vindex_backoffice_ui_tests.investigationTool;


import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.kafka.alerts.RuleAlert;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.data.enums.Brand;
import helpers.data.enums.FraudType;
import helpers.data.enums.Regulator;
import helpers.data.enums.Restriction;
import helpers.database.DbName;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import okhttp3.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import page_objects.backoffice_pages.investigationTool.RestrictionPage;
import tests.TestBaseWeb;

import java.util.List;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateStaticCrmTbAccountActive;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static business_objects.kafka.alerts.RuleAlertFactory.generateWithdrawalNotificationAlert;
import static helpers.data.enums.Restriction.*;
import static helpers.database.CleanTableHelper.*;
import static helpers.database.BoHelper.*;
import static helpers.database.DbHelper.*;
import static helpers.kafka.alerts.CreateSimpleAlert.createSimpleAlert;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static utils.Constants.*;

public class ResolveTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    static ClientHelper withdrawalClient = new ClientHelper(141_402, "063cde3b-ea9d-48b5-8e2c-99f3d5f67999", Brand.VANTAGE, Regulator.VFSC2, 14_140_102, 42);
    static ClientHelper resolveClient = new ClientHelper(161_601, "063cde3b-ea9d-48b5-8e2c-99f3d5f67999", Brand.VANTAGE, Regulator.VFSC2, 161_601_001, 42);
    private static final RuleAlert alert1 = generateWithdrawalNotificationAlert(withdrawalClient);
    private static final RuleAlert alert2 = generateWithdrawalNotificationAlert(withdrawalClient);
    private static final RuleAlert alert3 = generateWithdrawalNotificationAlert(withdrawalClient);

    @BeforeAll
    public static void setup() throws Exception {
        CrmTbUserObject withdrawalClientDB = generateStaticUserByClient(withdrawalClient);
        CrmTbUserObject resolveClientDB = generateStaticUserByClient(resolveClient);
        alert1.rule.attributes.withdrawalId = "14140201";
        alert1.rule.attributes.amount = "1";
        alert1.rule.attributes.currency = "USD";
        alert1.rule.attributes.paymentType = "first withdrawal";
        alert1.rule.attributes.paymentChannel = "first withdrawal";
        alert1.rule.attributes.createTime = "2024-10-13T09:03:00+03:00";
        alert2.rule.attributes.withdrawalId = "14140202";
        alert2.rule.attributes.amount = "1";
        alert2.rule.attributes.currency = "USD";
        alert2.rule.attributes.paymentType = "second withdrawal";
        alert2.rule.attributes.paymentChannel = "second withdrawal";
        alert2.rule.attributes.createTime = "2024-10-13T09:03:00+03:00";
        alert3.rule.attributes.withdrawalId = "14140203";
        alert3.rule.attributes.amount = "1";
        alert3.rule.attributes.currency = "USD";
        alert2.rule.attributes.paymentType = "second withdrawal";
        alert3.rule.attributes.paymentChannel = "third withdrawal";
        alert3.rule.attributes.createTime = "2024-10-13T09:03:00+03:00";

        insertObjectsToDb(CRM_USER_TABLE_NAME, List.of(withdrawalClientDB, resolveClientDB));

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
        deleteEntryFromDb(DbName.BO, BO_WD_REQUEST_TABLE_NAME, String.format("ucid = '%s'", withdrawalClient.getUcid()));
        RestrictionPage.cleanUserRestriction(withdrawalClient.getUcid());
        Response response = enableCRMEmulator();
        assertNotNull(response);
        RestrictionPage.setRestrictionAPIGeneral(withdrawalClient.getUcid(), MANUAL_WITHDRAWAL_REVIEW.getCode());
        kafka.produceMessages(alert1.alertId, KAFKA_TOPIC_ALERTS, objectMapper.writeValueAsString(alert1), objectMapper.writeValueAsString(alert2), objectMapper.writeValueAsString(alert3));
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(withdrawalClient.getUcid());
        resolvePage.openResolveSuspicious();
        resolvePage.resolveWithdrawalsAllApprove();
        String details = "Transaction ID 14140201; 1.00 USD 2024-10-13 09:03 first withdrawal; Approve";
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
        deleteEntryFromDb(DbName.BO, BO_WD_REQUEST_TABLE_NAME, String.format("ucid = '%s'", withdrawalClient.getUcid()));
        RestrictionPage.cleanUserRestriction(withdrawalClient.getUcid());
        Response response = enableCRMEmulator();
        assertNotNull(response);
        RestrictionPage.setRestrictionAPIGeneral(withdrawalClient.getUcid(), MANUAL_WITHDRAWAL_REVIEW.getCode());

        kafka.produceMessages(alert1.alertId, KAFKA_TOPIC_ALERTS, objectMapper.writeValueAsString(alert1), objectMapper.writeValueAsString(alert2), objectMapper.writeValueAsString(alert3));
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(withdrawalClient.getUcid());
        resolvePage.openResolveSuspicious();
        resolvePage.resolveWithdrawalsAllReject();
        String details = "Transaction ID 14140202; 1.00 USD 2024-10-13 09:03 second withdrawal; Refuse";
        restrictionPage.checkRestrictionCancellationAuditBO(withdrawalClient.getUcid(), "WD_REQUEST_DECISION", details);
        restrictionPage.checkKafkaRequestWithdrawal("14140202", "Refuse");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("432")
    @DisplayName("resolve client with withdrawal transactions approve one")
    public void resolveWithWithdrawalsApproveOneTest() throws Exception {
        //first run
        cleanUserAudit(withdrawalClient.getUcid());
        deleteEntryFromDb(DbName.BO, BO_WD_REQUEST_TABLE_NAME, String.format("ucid = '%s'", withdrawalClient.getUcid()));
        RestrictionPage.cleanUserRestriction(withdrawalClient.getUcid());
        Response response = enableCRMEmulator();
        assertNotNull(response);
        RestrictionPage.setRestrictionAPIGeneral(withdrawalClient.getUcid(), MANUAL_WITHDRAWAL_REVIEW.getCode());
        kafka.produceMessages(alert1.alertId, KAFKA_TOPIC_ALERTS, objectMapper.writeValueAsString(alert1), objectMapper.writeValueAsString(alert2), objectMapper.writeValueAsString(alert3));
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(withdrawalClient.getUcid());
        resolvePage.openResolveSuspicious();
        resolvePage.resolveWithdrawalsApproveOneByType("first withdrawal");
        String details1 = "Transaction ID 14140201; 1.00 USD 2024-10-13 09:03 first withdrawal; Approve";
        restrictionPage.checkRestrictionCancellationAuditBO(withdrawalClient.getUcid(), "WD_REQUEST_DECISION", details1);
        restrictionPage.checkKafkaRequestWithdrawal("14140201", "Approve");
        //second run
        cleanUserAudit(withdrawalClient.getUcid());
        deleteEntryFromDb(DbName.BO, BO_WD_REQUEST_TABLE_NAME, String.format("ucid = '%s'", withdrawalClient.getUcid()));
        RestrictionPage.cleanUserRestriction(withdrawalClient.getUcid());
        RestrictionPage.setRestrictionAPIGeneral(withdrawalClient.getUcid(), MANUAL_WITHDRAWAL_REVIEW.getCode());
        kafka.produceMessages(alert1.alertId, KAFKA_TOPIC_ALERTS, objectMapper.writeValueAsString(alert1), objectMapper.writeValueAsString(alert2), objectMapper.writeValueAsString(alert3));
        investigationPage.navigateToClient(withdrawalClient.getUcid());
        resolvePage.openResolveSuspicious();
        resolvePage.resolveWithdrawalsApproveOneByType("first withdrawal");
        String details2 = "Transaction ID 14140202; 1.00 USD 2024-10-13 09:03 second withdrawal; Refuse";
        restrictionPage.checkRestrictionCancellationAuditBO(withdrawalClient.getUcid(), "WD_REQUEST_DECISION", details2);
        restrictionPage.checkKafkaRequestWithdrawal("14140202", "Refuse");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("286")
    @DisplayName("BO user can assign suspicious client with the active alert to himself to perform investigation from the alert list")
    public void assignAlertListTest() throws Exception {
        deleteUserBO(resolveClient.getUcid());
        cleanUserAudit(resolveClient.getUcid());
        createSimpleAlert(resolveClient.getUcid(), FraudType.CPA_ABUSE.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToMain();
        investigationPage.filterUnassigned();
        investigationPage.investigateUserAlertList(resolveClient.getUserId());
        investigationPage.checkInvestigationAssigmentAudit(resolveClient.getUcid());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("226")
    @DisplayName("BO user can assign suspicious client with the active alert to himself to perform investigation from the client card")
    public void assignClientCardTest() throws Exception {
        deleteUserBO(resolveClient.getUcid());
        cleanUserAudit(resolveClient.getUcid());
        createSimpleAlert(resolveClient.getUcid(), FraudType.CPA_ABUSE.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        investigationPage.checkInvestigationAssigmentAudit(resolveClient.getUcid());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("301")
    @DisplayName("BO user can resolve client in with fraud type BONUS_ABUSE")
    public void resolveClientBonusAbuseTest() throws Exception {
        deleteUserBO(resolveClient.getUcid());
        FraudType fraud = FraudType.BONUS_ABUSE;
        cleanUserAudit(resolveClient.getUcid());
        createSimpleAlert(resolveClient.getUcid(), fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud.getDisplayName());
        checkUserFraudBo(resolveClient.getUcid(), fraud.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("300")
    @DisplayName("BO user can resolve client in with fraud type CPA_ABUSE")
    public void resolveClientCpaTest() throws Exception {
        deleteUserBO(resolveClient.getUcid());
        FraudType fraud = FraudType.CPA_ABUSE;
        cleanUserAudit(resolveClient.getUcid());
        createSimpleAlert(resolveClient.getUcid(), fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud.getDisplayName());
        checkUserFraudBo(resolveClient.getUcid(), fraud.getFraudTypeId());

    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("298")
    @DisplayName("BO user can resolve client in with fraud type GAP_TRADING")
    public void resolveClientGapTradingTest() throws Exception {
        deleteUserBO(resolveClient.getUcid());
        FraudType fraud = FraudType.GAP_TRADING;
        cleanUserAudit(resolveClient.getUcid());
        createSimpleAlert(resolveClient.getUcid(), fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud.getDisplayName());
        checkUserFraudBo(resolveClient.getUcid(), fraud.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("294")
    @DisplayName("BO user can resolve client in with fraud type HEDGING")
    public void resolveClientHedgingTest() throws Exception {
        deleteUserBO(resolveClient.getUcid());
        FraudType fraud = FraudType.HEDGING;
        cleanUserAudit(resolveClient.getUcid());
        createSimpleAlert(resolveClient.getUcid(), fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud.getDisplayName());
        checkUserFraudBo(resolveClient.getUcid(), fraud.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("295")
    @DisplayName("BO user can resolve client in with fraud type LATENCY_ARBITRAGE")
    public void resolveClientGapLatencyArbitrageTest() throws Exception {
        deleteUserBO(resolveClient.getUcid());
        FraudType fraud = FraudType.LATENCY_ARBITRAGE;
        cleanUserAudit(resolveClient.getUcid());
        createSimpleAlert(resolveClient.getUcid(), fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud.getDisplayName());
        checkUserFraudBo(resolveClient.getUcid(), fraud.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("304")
    @DisplayName("BO user can resolve client in with fraud type LOSS_VOUCHER_ABUSE")
    public void resolveClientGapLossVoucherAbuseTest() throws Exception {
        deleteUserBO(resolveClient.getUcid());
        FraudType fraud = FraudType.LOSS_VOUCHER_ABUSE;
        cleanUserAudit(resolveClient.getUcid());
        createSimpleAlert(resolveClient.getUcid(), fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud.getDisplayName());
        checkUserFraudBo(resolveClient.getUcid(), fraud.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("296")
    @DisplayName("BO user can resolve client in with fraud type MARKET_MANIPULATION")
    public void resolveClientGapMarketManipulationTest() throws Exception {
        deleteUserBO(resolveClient.getUcid());
        FraudType fraud = FraudType.MARKET_MANIPULATION;
        cleanUserAudit(resolveClient.getUcid());
        createSimpleAlert(resolveClient.getUcid(), fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud.getDisplayName());
        checkUserFraudBo(resolveClient.getUcid(), fraud.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("305")
    @DisplayName("BO user can resolve client in with fraud type NBP_ABUSE")
    public void resolveClientGapNBPAbuseTest() throws Exception {
        deleteUserBO(resolveClient.getUcid());
        FraudType fraud = FraudType.NBP_ABUSE;
        cleanUserAudit(resolveClient.getUcid());
        createSimpleAlert(resolveClient.getUcid(), fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud.getDisplayName());
        checkUserFraudBo(resolveClient.getUcid(), fraud.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("307")
    @DisplayName("BO user can resolve client in with fraud type POTENTIAL_ABUSE")
    public void resolveClientGapPotentialAbuseTest() throws Exception {
        deleteUserBO(resolveClient.getUcid());
        FraudType fraud = FraudType.POTENTIAL_ABUSE;
        cleanUserAudit(resolveClient.getUcid());
        createSimpleAlert(resolveClient.getUcid(), fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud.getDisplayName());
        checkUserFraudBo(resolveClient.getUcid(), fraud.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("297")
    @DisplayName("BO user can resolve client in with fraud type PRICING_ERRORS")
    public void resolveClientGapPricingErrorsTest() throws Exception {
        deleteUserBO(resolveClient.getUcid());
        FraudType fraud = FraudType.PRICING_ERROR;
        cleanUserAudit(resolveClient.getUcid());
        createSimpleAlert(resolveClient.getUcid(), fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud.getDisplayName());
        checkUserFraudBo(resolveClient.getUcid(), fraud.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("302")
    @DisplayName("BO user can resolve client in with fraud type RAF_ABUSE")
    public void resolveClientGapRAFAbuseTest() throws Exception {
        deleteUserBO(resolveClient.getUcid());
        FraudType fraud = FraudType.RAF_ABUSE;
        cleanUserAudit(resolveClient.getUcid());
        createSimpleAlert(resolveClient.getUcid(), fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud.getDisplayName());
        checkUserFraudBo(resolveClient.getUcid(), fraud.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("303")
    @DisplayName("BO user can resolve client in with fraud type REBATE_CHURNING")
    public void resolveClientGapRebateChurningTest() throws Exception {
        deleteUserBO(resolveClient.getUcid());
        FraudType fraud = FraudType.REBATE_CHURNING;
        cleanUserAudit(resolveClient.getUcid());
        createSimpleAlert(resolveClient.getUcid(), fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud.getDisplayName());
        checkUserFraudBo(resolveClient.getUcid(), fraud.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("299")
    @DisplayName("BO user can resolve client in with fraud type SWAP_ARBITRAGE")
    public void resolveClientSwapArbitrageTest() throws Exception {
        deleteUserBO(resolveClient.getUcid());
        FraudType fraud = FraudType.SWAP_ARBITRAGE;
        cleanUserAudit(resolveClient.getUcid());
        createSimpleAlert(resolveClient.getUcid(), fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud.getDisplayName());
        checkUserFraudBo(resolveClient.getUcid(), fraud.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("306")
    @DisplayName("BO user can resolve client in with fraud type TLS_ABUSE")
    public void resolveClientTLSAbuseTest() throws Exception {
        deleteUserBO(resolveClient.getUcid());
        FraudType fraud = FraudType.TLS_ABUSE;
        cleanUserAudit(resolveClient.getUcid());
        createSimpleAlert(resolveClient.getUcid(), fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud.getDisplayName());
        checkUserFraudBo(resolveClient.getUcid(), fraud.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("762")
    @DisplayName("BO user can resolve client in with fraud type LOOPHOLE_ABUSE")
    public void resolveClientLoopholeAbuseTest() throws Exception {
        deleteUserBO(resolveClient.getUcid());
        FraudType fraud = FraudType.LOOPHOLE_ABUSE;
        cleanUserAudit(resolveClient.getUcid());
        createSimpleAlert(resolveClient.getUcid(), fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud.getDisplayName());
        checkUserFraudBo(resolveClient.getUcid(), fraud.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("763")
    @DisplayName("BO user can resolve client in with fraud type HFT_ABUSE")
    public void resolveClientHfyAbuseTest() throws Exception {
        deleteUserBO(resolveClient.getUcid());
        FraudType fraud = FraudType.HFT_ABUSE;
        cleanUserAudit(resolveClient.getUcid());
        createSimpleAlert(resolveClient.getUcid(), fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud.getDisplayName());
        checkUserFraudBo(resolveClient.getUcid(), fraud.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("764")
    @DisplayName("BO user can resolve client in with fraud type NEWS_TRADER")
    public void resolveClientNewsTraderAbuseTest() throws Exception {
        deleteUserBO(resolveClient.getUcid());
        FraudType fraud = FraudType.NEWS_TRADER;
        cleanUserAudit(resolveClient.getUcid());
        createSimpleAlert(resolveClient.getUcid(), fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud.getDisplayName());
        checkUserFraudBo(resolveClient.getUcid(), fraud.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("765")
    @DisplayName("BO user can resolve client in with fraud type ANOMALOUS_PROFIT")
    public void resolveClientAnomalousProfitAbuseTest() throws Exception {
        deleteUserBO(resolveClient.getUcid());
        FraudType fraud = FraudType.ANOMALOUS_PROFIT;
        cleanUserAudit(resolveClient.getUcid());
        createSimpleAlert(resolveClient.getUcid(), fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud.getDisplayName());
        checkUserFraudBo(resolveClient.getUcid(), fraud.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("322")
    @DisplayName("BO user can resolve client in with multiple fraud types")
    public void resolveClientMultipleAbuseTest() throws Exception {
        FraudType fraud1 = FraudType.HEDGING;
        FraudType fraud2 = FraudType.TLS_ABUSE;
        deleteUserBO(resolveClient.getUcid());
        cleanUserAudit(resolveClient.getUcid());
        createSimpleAlert(resolveClient.getUcid(), fraud1.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddMultipleFraud("test" + timestamp, fraud1.getDisplayName(), fraud2.getDisplayName());
        checkUserFraudBo(resolveClient.getUcid(), fraud1.getFraudTypeId());
        checkUserFraudBo(resolveClient.getUcid(), fraud2.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("325")
    @DisplayName("BO user can resolve client without any applied fraud")
    public void resolveClientNoFraudTest() throws Exception {
        deleteUserBO(resolveClient.getUcid());
        cleanUserAudit(resolveClient.getUcid());
        createSimpleAlert(resolveClient.getUcid(), FraudType.TLS_ABUSE.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveNoFrauds("test" + timestamp);
        checkUserNoFraudBo(resolveClient.getUcid());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("291")
    @DisplayName("BO user can't type more than a 250 symbols into resolve commentary section")
    public void cantTypeMoreThan250CommentTest() throws Exception {
        deleteUserBO(resolveClient.getUcid());
        cleanUserAudit(resolveClient.getUcid());
        createSimpleAlert(resolveClient.getUcid(), FraudType.TLS_ABUSE.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
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
        deleteUserBO(resolveClient.getUcid());
        cleanUserAudit(resolveClient.getUcid());
        createSimpleAlert(resolveClient.getUcid(), FraudType.TLS_ABUSE.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveNoFrauds("test" + timestamp);
        checkUserAlertConfirmation(resolveClient.getUcid(), "FALSE_POSITIVE");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("289")
    @DisplayName("on resolve violated rule can be tagged as true positive")
    public void resolveTruePositiveTest() throws Exception {
        deleteUserBO(resolveClient.getUcid());
        cleanUserAudit(resolveClient.getUcid());
        createSimpleAlert(resolveClient.getUcid(), FraudType.TLS_ABUSE.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, FraudType.TLS_ABUSE.getDisplayName());
        checkUserAlertConfirmation(resolveClient.getUcid(), "CONFIRMED");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("250")
    @DisplayName("Resolve have actual list of violations")
    public void resolveViolationsListTest() throws Exception {
        deleteUserBO(resolveClient.getUcid());
        cleanUserAudit(resolveClient.getUcid());
        createSimpleAlert(resolveClient.getUcid(), FraudType.TLS_ABUSE.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
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
        Restriction restriction = ACCOUNT_CREATION;
        //run 1
        RestrictionPage.cleanUserRestriction(resolveClient.getUcid());
        deleteUserBO(resolveClient.getUcid());
        cleanUserAudit(resolveClient.getUcid());
        Response response = enableCRMEmulator();
        assertNotNull(response);
        RestrictionPage.setRestrictionAPIGeneral(resolveClient.getUcid(), restriction.getCode());
        createSimpleAlert(resolveClient.getUcid(), FraudType.TLS_ABUSE.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.checkRestrictionIsDisplayed(restriction.getName());
        //run 2
        Restriction restriction1 = Restriction.LOGIN_CRM;
        RestrictionPage.cleanUserRestriction(resolveClient.getUcid());
        deleteUserBO(resolveClient.getUcid());
        cleanUserAudit(resolveClient.getUcid());
        RestrictionPage.setRestrictionAPIGeneral(resolveClient.getUcid(), restriction1.getCode());
        createSimpleAlert(resolveClient.getUcid(), FraudType.TLS_ABUSE.getKey());
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.checkRestrictionIsDisplayed(restriction1.getName());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("744")
    @DisplayName("Resolve Flow. User can comment suspicious client outside of resolve screen")
    public void commentOutsideResolveTest() throws Exception {
        String comment = "test" + timestamp;
        deleteUserBO(resolveClient.getUcid());
        cleanUserAudit(resolveClient.getUcid());
        createSimpleAlert(resolveClient.getUcid(), FraudType.CPA_ABUSE.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        investigationPage.openCommentForm();
        investigationPage.fillCommentForm(comment);
        investigationPage.submitCommentForm();

    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1085")
    @Feature("BMS-903 Manage restrictions & fraud types from resolve")
    @DisplayName("Alert in 'false positive' state when fraud not set and no frauds in history")
    public void resolveNoFraudAssignedNoFraudInHistoryTest() throws Exception {
        deleteUserBO(resolveClient.getUcid());
        FraudType fraud = FraudType.ANOMALOUS_PROFIT;
        cleanUserAudit(resolveClient.getUcid());
        createSimpleAlert(resolveClient.getUcid(), fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveNoFrauds("test" + timestamp);
        checkUserAlertConfirmation(resolveClient.getUcid(), "FALSE_POSITIVE");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1087")
    @Feature("BMS-903 Manage restrictions & fraud types from resolve")
    @DisplayName("Alert in 'confirmed' state when fraud in alert match fraud in history")
    public void resolveNoFraudAssignedMatchFraudInHistoryTest() throws Exception {
        deleteUserBO(resolveClient.getUcid());
        createUserFraudsBo(resolveClient.getUcid(), FraudType.ANOMALOUS_PROFIT.getFraudTypeId());
        FraudType fraud = FraudType.ANOMALOUS_PROFIT;
        cleanUserAudit(resolveClient.getUcid());
        createSimpleAlert(resolveClient.getUcid(), fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveNoActionFrauds("test" + timestamp);
        checkUserAlertConfirmation(resolveClient.getUcid(), "CONFIRMED");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1086")
    @Feature("BMS-903 Manage restrictions & fraud types from resolve")
    @DisplayName("Alert in 'Fraud Type Mismatch' state when fraud in alert not match fraud in history")
    public void resolveNoFraudAssignedMismatchFraudInHistoryTest() throws Exception {
        deleteUserBO(resolveClient.getUcid());
        cleanUserAR(resolveClient.getUcid());
        createUserFraudsBo(resolveClient.getUcid(), FraudType.HEDGING.getFraudTypeId());
        FraudType fraud = FraudType.ANOMALOUS_PROFIT;
        cleanUserAudit(resolveClient.getUcid());
        createSimpleAlert(resolveClient.getUcid(), fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveNoActionFrauds("test" + timestamp);
        checkUserAlertConfirmation(resolveClient.getUcid(), "FRAUD_TYPE_MISMATCH");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1088")
    @Feature("BMS-903 Manage restrictions & fraud types from resolve")
    @DisplayName("Alert in 'Fraud Type Mismatch' state when fraud in alert not match fraud in resolve")
    public void resolveMismatchFraudInResolveTest() throws Exception {
        deleteUserBO(resolveClient.getUcid());
        FraudType fraud = FraudType.ANOMALOUS_PROFIT;
        cleanUserAudit(resolveClient.getUcid());
        createSimpleAlert(resolveClient.getUcid(), fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, FraudType.TLS_ABUSE.getDisplayName());
        checkUserAlertConfirmation(resolveClient.getUcid(), "FRAUD_TYPE_MISMATCH");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1089")
    @Feature("BMS-903 Manage restrictions & fraud types from resolve")
    @DisplayName("Alert in 'False Positive' state when fraud in alert not match fraud in history")
    public void resolveNoFraudAssignedDeleteFraudInHistoryTest() throws Exception {
        deleteUserBO(resolveClient.getUcid());
        createUserFraudsBo(resolveClient.getUcid(), FraudType.ANOMALOUS_PROFIT.getFraudTypeId());
        FraudType fraud = FraudType.ANOMALOUS_PROFIT;
        cleanUserAudit(resolveClient.getUcid());
        createSimpleAlert(resolveClient.getUcid(), fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveClearFrauds("test" + timestamp);
        checkUserAlertConfirmation(resolveClient.getUcid(), "FALSE_POSITIVE");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1090")
    @Feature("BMS-903 Manage restrictions & fraud types from resolve")
    @DisplayName("Fraud types changes can be reset on resolve screen")
    public void resolveAddedFraudsCanBeResetTest() throws Exception {
        deleteUserBO(resolveClient.getUcid());
        createUserFraudsBo(resolveClient.getUcid(), FraudType.ANOMALOUS_PROFIT.getFraudTypeId());
        FraudType fraud = FraudType.HEDGING;
        cleanUserAudit(resolveClient.getUcid());
        createSimpleAlert(resolveClient.getUcid(), fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.addFraud(FraudType.HEDGING.getDisplayName());
        resolvePage.checkFraudDisplayed(FraudType.ANOMALOUS_PROFIT.getDisplayName(), FraudType.HEDGING.getDisplayName());
        resolvePage.resetFraudsChanges();
        resolvePage.checkFraudDisplayed(FraudType.ANOMALOUS_PROFIT.getDisplayName());
        resolvePage.checkFraudNotDisplayed(FraudType.HEDGING.getDisplayName());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1091")
    @Feature("BMS-903 Manage restrictions & fraud types from resolve")
    @DisplayName("Restrictions can be added on resolve screen")
    public void restrictionCanBeAdded() throws Exception {
        deleteUserBO(resolveClient.getUcid());
        cleanUserRestrictionGeneral(resolveClient.getUcid());
        createUserFraudsBo(resolveClient.getUcid(), FraudType.ANOMALOUS_PROFIT.getFraudTypeId());
        FraudType fraud = FraudType.HEDGING;
        cleanUserAudit(resolveClient.getUcid());
        createSimpleAlert(resolveClient.getUcid(), fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.addRestriction(Restriction.DEPOSITS.getName());
        resolvePage.resolveFillCommentary("test" + timestamp);
        resolvePage.resolveInvestigation();
        RestrictionPage.checkUserHaveRestrictionGeneral(resolveClient.getUcid(), Restriction.DEPOSITS.getId(), "APPLIED");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1091")
    @Feature("BMS-903 Manage restrictions & fraud types from resolve")
    @DisplayName("Restrictions can be added and then removed on resolve screen")
    public void restrictionCanBeAddedAndRemoved() throws Exception {
        deleteUserBO(resolveClient.getUcid());
        cleanUserRestrictionGeneral(resolveClient.getUcid());
        createUserFraudsBo(resolveClient.getUcid(), FraudType.ANOMALOUS_PROFIT.getFraudTypeId());
        FraudType fraud = FraudType.HEDGING;
        cleanUserAudit(resolveClient.getUcid());
        createSimpleAlert(resolveClient.getUcid(), fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.addRestriction(Restriction.DEPOSITS.getName());
        resolvePage.checkRestrictionDisplayed(Restriction.DEPOSITS.getName());
        resolvePage.clickDeleteRestrictionButtonByName(Restriction.DEPOSITS.getName());
        resolvePage.checkRestrictionNotDisplayed(Restriction.DEPOSITS.getName());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1091")
    @Feature("BMS-903 Manage restrictions & fraud types from resolve")
    @DisplayName("Restrictions can be added and then removed on resolve screen")
    public void previousSetRestrictionCanBeRemoved() throws Exception {
        deleteUserBO(resolveClient.getUcid());
        cleanUserRestrictionGeneral(resolveClient.getUcid());
        createUserFraudsBo(resolveClient.getUcid(), FraudType.ANOMALOUS_PROFIT.getFraudTypeId());
        FraudType fraud = FraudType.HEDGING;
        cleanUserAudit(resolveClient.getUcid());
        enableCRMEmulator();
        createSimpleAlert(resolveClient.getUcid(), fraud.getKey());
        RestrictionPage.setRestrictionAPIGeneral(resolveClient.getUcid(), Restriction.DEPOSITS.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.checkRestrictionDisplayed(Restriction.DEPOSITS.getName());
        resolvePage.clickDeleteRestrictionButtonByName(Restriction.DEPOSITS.getName());
        resolvePage.confirmRestrictionDeletion("No");
        resolvePage.checkRestrictionDisplayed(Restriction.DEPOSITS.getName());
        resolvePage.clickDeleteRestrictionButtonByName(Restriction.DEPOSITS.getName());
        resolvePage.confirmRestrictionDeletion("Yes");
        resolvePage.checkRestrictionNotDisplayed(Restriction.DEPOSITS.getName());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1323")
    @Feature("BMS-1139 Predefined restrictions")
    @DisplayName("Predefined restriction for the fraud type appears on the resolve screen")
    public void predefinedRestrictionAppearsOnResolveScreen() throws Exception {
        deleteUserBO(resolveClient.getUcid());
        cleanUserRestrictionGeneral(resolveClient.getUcid());
        deleteUserAR(resolveClient.getUcid());
        FraudType fraud = FraudType.HEDGING;
        cleanUserAudit(resolveClient.getUcid());
        createSimpleAlert(resolveClient.getUcid(), fraud.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.addFraud(FraudType.HEDGING.getDisplayName());
        resolvePage.checkRestrictionDisplayed(ACCOUNT_CREATION.getName(), DEPOSITS.getName(), CREDIT_AND_BONUS.getName(), INTERNAL_TRANSFER.getName(), WITHDRAWALS.getName());
        resolvePage.checkRestrictionNotDisplayed(MANUAL_WITHDRAWAL_REVIEW.getName());
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();
        resolvePage.addFraud("Swap Arbitrage");
        resolvePage.checkRestrictionDisplayed(ACCOUNT_CREATION.getName(), MANUAL_WITHDRAWAL_REVIEW.getName());
        resolvePage.checkRestrictionNotDisplayed(DEPOSITS.getName(), CREDIT_AND_BONUS.getName(), INTERNAL_TRANSFER.getName(), WITHDRAWALS.getName());
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();
        resolvePage.addFraud("Market Manipulation");
        resolvePage.checkRestrictionDisplayed(ACCOUNT_CREATION.getName(), DEPOSITS.getName(), CREDIT_AND_BONUS.getName(), INTERNAL_TRANSFER.getName(), WITHDRAWALS.getName());
        resolvePage.checkRestrictionNotDisplayed(MANUAL_WITHDRAWAL_REVIEW.getName());
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();
        resolvePage.addFraud(FraudType.LATENCY_ARBITRAGE.getDisplayName());
        resolvePage.checkRestrictionDisplayed(ACCOUNT_CREATION.getName(), DEPOSITS.getName(), CREDIT_AND_BONUS.getName(), INTERNAL_TRANSFER.getName(), WITHDRAWALS.getName());
        resolvePage.checkRestrictionNotDisplayed(MANUAL_WITHDRAWAL_REVIEW.getName());
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();
        resolvePage.addFraud(FraudType.CPA_ABUSE.getDisplayName());
        resolvePage.checkRestrictionDisplayed(ACCOUNT_CREATION.getName(), DEPOSITS.getName(), CREDIT_AND_BONUS.getName());
        resolvePage.checkRestrictionNotDisplayed(MANUAL_WITHDRAWAL_REVIEW.getName(), INTERNAL_TRANSFER.getName(), WITHDRAWALS.getName());
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();
        resolvePage.addFraud(FraudType.NBP_ABUSE.getDisplayName());
        resolvePage.checkRestrictionDisplayed(CREDIT_AND_BONUS.getName(), MANUAL_WITHDRAWAL_REVIEW.getName());
        resolvePage.checkRestrictionNotDisplayed(ACCOUNT_CREATION.getName(), DEPOSITS.getName(), INTERNAL_TRANSFER.getName(), WITHDRAWALS.getName());
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();
        resolvePage.addFraud(FraudType.LOSS_VOUCHER_ABUSE.getDisplayName());
        resolvePage.checkRestrictionDisplayed(ACCOUNT_CREATION.getName(), DEPOSITS.getName(), CREDIT_AND_BONUS.getName(), INTERNAL_TRANSFER.getName(), WITHDRAWALS.getName());
        resolvePage.checkRestrictionNotDisplayed(MANUAL_WITHDRAWAL_REVIEW.getName());
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();
        resolvePage.addFraud(FraudType.GAP_TRADING.getDisplayName());
        resolvePage.checkRestrictionDisplayed(ACCOUNT_CREATION.getName(), DEPOSITS.getName(), CREDIT_AND_BONUS.getName(), INTERNAL_TRANSFER.getName());
        resolvePage.checkRestrictionNotDisplayed(MANUAL_WITHDRAWAL_REVIEW.getName(), WITHDRAWALS.getName());
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();
        resolvePage.addFraud(FraudType.REBATE_CHURNING.getDisplayName());
        resolvePage.checkRestrictionDisplayed(ACCOUNT_CREATION.getName(), DEPOSITS.getName(), CREDIT_AND_BONUS.getName(), INTERNAL_TRANSFER.getName(), WITHDRAWALS.getName());
        resolvePage.checkRestrictionNotDisplayed(MANUAL_WITHDRAWAL_REVIEW.getName());
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();
        resolvePage.addFraud("Pricing Errors");
        resolvePage.checkRestrictionDisplayed(ACCOUNT_CREATION.getName(), DEPOSITS.getName(), CREDIT_AND_BONUS.getName(), INTERNAL_TRANSFER.getName(), WITHDRAWALS.getName());
        resolvePage.checkRestrictionNotDisplayed(MANUAL_WITHDRAWAL_REVIEW.getName());
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();
        resolvePage.addFraud(FraudType.TLS_ABUSE.getDisplayName());
        resolvePage.checkRestrictionDisplayed(ACCOUNT_CREATION.getName(), DEPOSITS.getName(), CREDIT_AND_BONUS.getName(), INTERNAL_TRANSFER.getName(), WITHDRAWALS.getName());
        resolvePage.checkRestrictionNotDisplayed(MANUAL_WITHDRAWAL_REVIEW.getName());
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();
        resolvePage.addFraud(FraudType.BONUS_ABUSE.getDisplayName());
        resolvePage.checkRestrictionDisplayed(ACCOUNT_CREATION.getName(), DEPOSITS.getName(), CREDIT_AND_BONUS.getName(), INTERNAL_TRANSFER.getName(), WITHDRAWALS.getName());
        resolvePage.checkRestrictionNotDisplayed(MANUAL_WITHDRAWAL_REVIEW.getName());
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();
        resolvePage.addFraud(FraudType.LOOPHOLE_ABUSE.getDisplayName());
        resolvePage.checkRestrictionDisplayed(ACCOUNT_CREATION.getName(), DEPOSITS.getName(), CREDIT_AND_BONUS.getName(), INTERNAL_TRANSFER.getName(), WITHDRAWALS.getName());
        resolvePage.checkRestrictionNotDisplayed(MANUAL_WITHDRAWAL_REVIEW.getName());
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();
        resolvePage.addFraud(FraudType.NEWS_TRADER.getDisplayName());
        resolvePage.checkRestrictionDisplayed(CREDIT_AND_BONUS.getName(), MANUAL_WITHDRAWAL_REVIEW.getName());
        resolvePage.checkRestrictionNotDisplayed(ACCOUNT_CREATION.getName(), DEPOSITS.getName(), INTERNAL_TRANSFER.getName(), WITHDRAWALS.getName());
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();
        resolvePage.addFraud(FraudType.MONEY_LAUNDRY_RECORD.getDisplayName());
        resolvePage.checkRestrictionDisplayed(ACCOUNT_CREATION.getName(), DEPOSITS.getName(), WITHDRAWALS.getName());
        resolvePage.checkRestrictionNotDisplayed(INTERNAL_TRANSFER.getName(), CREDIT_AND_BONUS.getName(), MANUAL_WITHDRAWAL_REVIEW.getName());
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();
        resolvePage.addFraud(FraudType.CHARGEBACK.getDisplayName());
        resolvePage.checkRestrictionNotDisplayed(ACCOUNT_CREATION.getName(), DEPOSITS.getName(), CREDIT_AND_BONUS.getName(), INTERNAL_TRANSFER.getName(), WITHDRAWALS.getName(), MANUAL_WITHDRAWAL_REVIEW.getName());
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();
    }

}
