package tests.vindex_backoffice_ui_tests.investigationTool;


import business_objects.db.abuse_registry_db.AbuserFraudType;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.mitigation_service_db.ClientGeneralRestriction;
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
import org.junit.jupiter.api.*;
import page_objects.backoffice_pages.investigationTool.RestrictionPage;
import tests.TestBaseWeb;

import java.util.List;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateStaticCrmTbAccountActive;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static business_objects.kafka.alerts.RuleAlertFactory.generateWithdrawalNotificationAlert;
import static helpers.api.AbuseRegistryHelper.addFraudsForClient;
import static helpers.data.enums.FraudTypeStatus.CONFIRMED;
import static helpers.data.enums.Restriction.*;
import static helpers.database.CleanTableHelper.*;
import static helpers.database.BoHelper.*;
import static helpers.database.DbHelper.*;
import static helpers.kafka.alerts.CreateSimpleAlert.createSimpleAlert;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
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
    private static final String WHERE_STATEMENT = "ucid = '%s'";

    @BeforeAll
    static void setup() {
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

    @BeforeEach
    void cleanData() throws Exception {
        deleteUserBO(resolveClient.getUcid());
        cleanUserAudit(resolveClient.getUcid());
        deleteUserAR(resolveClient.getUcid());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("432")
    @DisplayName("Resolve client with withdrawal transactions approve all")
    public void resolveWithWithdrawalsApproveAllTest() throws Exception {
        cleanUserAudit(withdrawalClient.getUcid());
        deleteEntryFromDb(DbName.BO, BO_WD_REQUEST_TABLE_NAME, String.format("ucid = '%s'", withdrawalClient.getUcid()));
        cleanUserRestrictionGeneral(withdrawalClient.getUcid());
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
    @DisplayName("Resolve client with withdrawal transactions reject all")
    public void resolveWithWithdrawalsRejectAllTest() throws Exception {
        deleteEntryFromDb(DbName.BO, BO_WD_REQUEST_TABLE_NAME, String.format("ucid = '%s'", withdrawalClient.getUcid()));
        cleanUserRestrictionGeneral(withdrawalClient.getUcid());
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
    @DisplayName("Resolve client with withdrawal transactions approve one")
    public void resolveWithWithdrawalsApproveOneTest() throws Exception {
        //first run
        deleteEntryFromDb(DbName.BO, BO_WD_REQUEST_TABLE_NAME, String.format("ucid = '%s'", withdrawalClient.getUcid()));
        cleanUserRestrictionGeneral(withdrawalClient.getUcid());
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
        cleanUserRestrictionGeneral(withdrawalClient.getUcid());
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
        createSimpleAlert(resolveClient.getUcid(), FraudType.CPA_ABUSE.getCode());
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
        createSimpleAlert(resolveClient.getUcid(), FraudType.CPA_ABUSE.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        investigationPage.checkInvestigationAssigmentAudit(resolveClient.getUcid());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("294")
    @DisplayName("BO user can resolve client in with fraud type HEDGING")
    public void resolveClientHedgingTest() throws Exception {
        FraudType fraud = FraudType.HEDGING;
        createSimpleAlert(resolveClient.getUcid(), fraud.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud);
        List<AbuserFraudType> frauds = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, String.format(WHERE_STATEMENT, resolveClient.getUcid()), AbuserFraudType.class);
        assertThat("Verify there is only 1 fraud", frauds.size(), is(1));
        assertThat("Verify fraud type", frauds.getFirst().getFraudTypeCode(), is(fraud.getCode()));
        assertThat("Verify status", frauds.getFirst().getStatus(), is(CONFIRMED.getStatus()));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("295")
    @DisplayName("BO user can resolve client in with fraud type LATENCY_ARBITRAGE")
    public void resolveClientGapLatencyArbitrageTest() throws Exception {
        FraudType fraud = FraudType.LATENCY_ARBITRAGE;
        createSimpleAlert(resolveClient.getUcid(), fraud.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud);
        List<AbuserFraudType> frauds = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, String.format(WHERE_STATEMENT, resolveClient.getUcid()), AbuserFraudType.class);
        assertThat("Verify there is only 1 fraud", frauds.size(), is(1));
        assertThat("Verify fraud type", frauds.getFirst().getFraudTypeCode(), is(fraud.getCode()));
        assertThat("Verify status", frauds.getFirst().getStatus(), is(CONFIRMED.getStatus()));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("300")
    @DisplayName("BO user can resolve client in with fraud type CPA_ABUSE")
    public void resolveClientCpaTest() throws Exception {
        FraudType fraud = FraudType.CPA_ABUSE;
        createSimpleAlert(resolveClient.getUcid(), fraud.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud);
        List<AbuserFraudType> frauds = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, String.format(WHERE_STATEMENT, resolveClient.getUcid()), AbuserFraudType.class);
        assertThat("Verify there is only 1 fraud", frauds.size(), is(1));
        assertThat("Verify fraud type", frauds.getFirst().getFraudTypeCode(), is(fraud.getCode()));
        assertThat("Verify status", frauds.getFirst().getStatus(), is(CONFIRMED.getStatus()));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("301")
    @DisplayName("BO user can resolve client in with fraud type BONUS_ABUSE")
    public void resolveClientBonusAbuseTest() throws Exception {
        FraudType fraud = FraudType.BONUS_ABUSE;
        createSimpleAlert(resolveClient.getUcid(), fraud.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud);
        List<AbuserFraudType> frauds = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, String.format(WHERE_STATEMENT, resolveClient.getUcid()), AbuserFraudType.class);
        assertThat("Verify there is only 1 fraud", frauds.size(), is(1));
        assertThat("Verify fraud type", frauds.getFirst().getFraudTypeCode(), is(fraud.getCode()));
        assertThat("Verify status", frauds.getFirst().getStatus(), is(CONFIRMED.getStatus()));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("304")
    @DisplayName("BO user can resolve client in with fraud type LOSS_VOUCHER_ABUSE")
    public void resolveClientGapLossVoucherAbuseTest() throws Exception {
        FraudType fraud = FraudType.LOSS_VOUCHER_ABUSE;
        createSimpleAlert(resolveClient.getUcid(), fraud.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud);
        List<AbuserFraudType> frauds = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, String.format(WHERE_STATEMENT, resolveClient.getUcid()), AbuserFraudType.class);
        assertThat("Verify there is only 1 fraud", frauds.size(), is(1));
        assertThat("Verify fraud type", frauds.getFirst().getFraudTypeCode(), is(fraud.getCode()));
        assertThat("Verify status", frauds.getFirst().getStatus(), is(CONFIRMED.getStatus()));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("305")
    @DisplayName("BO user can resolve client in with fraud type NBP_ABUSE")
    public void resolveClientGapNBPAbuseTest() throws Exception {
        FraudType fraud = FraudType.NBP_ABUSE;
        createSimpleAlert(resolveClient.getUcid(), fraud.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud);
        List<AbuserFraudType> frauds = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, String.format(WHERE_STATEMENT, resolveClient.getUcid()), AbuserFraudType.class);
        assertThat("Verify there is only 1 fraud", frauds.size(), is(1));
        assertThat("Verify fraud type", frauds.getFirst().getFraudTypeCode(), is(fraud.getCode()));
        assertThat("Verify status", frauds.getFirst().getStatus(), is(CONFIRMED.getStatus()));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("298")
    @DisplayName("BO user can resolve client in with fraud type GAP_TRADING")
    public void resolveClientGapTradingTest() throws Exception {
        FraudType fraud = FraudType.GAP_TRADING;
        createSimpleAlert(resolveClient.getUcid(), fraud.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud);
        List<AbuserFraudType> frauds = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, String.format(WHERE_STATEMENT, resolveClient.getUcid()), AbuserFraudType.class);
        assertThat("Verify there is only 1 fraud", frauds.size(), is(1));
        assertThat("Verify fraud type", frauds.getFirst().getFraudTypeCode(), is(fraud.getCode()));
        assertThat("Verify status", frauds.getFirst().getStatus(), is(CONFIRMED.getStatus()));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("303")
    @DisplayName("BO user can resolve client in with fraud type REBATE_CHURNING")
    public void resolveClientGapRebateChurningTest() throws Exception {
        FraudType fraud = FraudType.REBATE_CHURNING;
        createSimpleAlert(resolveClient.getUcid(), fraud.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud);
        List<AbuserFraudType> frauds = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, String.format(WHERE_STATEMENT, resolveClient.getUcid()), AbuserFraudType.class);
        assertThat("Verify there is only 1 fraud", frauds.size(), is(1));
        assertThat("Verify fraud type", frauds.getFirst().getFraudTypeCode(), is(fraud.getCode()));
        assertThat("Verify status", frauds.getFirst().getStatus(), is(CONFIRMED.getStatus()));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("306")
    @DisplayName("BO user can resolve client in with fraud type TLS_ABUSE")
    public void resolveClientTLSAbuseTest() throws Exception {
        FraudType fraud = FraudType.TLS_ABUSE;
        createSimpleAlert(resolveClient.getUcid(), fraud.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud);
        List<AbuserFraudType> frauds = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, String.format(WHERE_STATEMENT, resolveClient.getUcid()), AbuserFraudType.class);
        assertThat("Verify there is only 1 fraud", frauds.size(), is(1));
        assertThat("Verify fraud type", frauds.getFirst().getFraudTypeCode(), is(fraud.getCode()));
        assertThat("Verify status", frauds.getFirst().getStatus(), is(CONFIRMED.getStatus()));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("762")
    @DisplayName("BO user can resolve client in with fraud type LOOPHOLE_ABUSE")
    public void resolveClientLoopholeAbuseTest() throws Exception {
        FraudType fraud = FraudType.LOOPHOLE_ABUSE;
        createSimpleAlert(resolveClient.getUcid(), fraud.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud);
        List<AbuserFraudType> frauds = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, String.format(WHERE_STATEMENT, resolveClient.getUcid()), AbuserFraudType.class);
        assertThat("Verify there is only 1 fraud", frauds.size(), is(1));
        assertThat("Verify fraud type", frauds.getFirst().getFraudTypeCode(), is(fraud.getCode()));
        assertThat("Verify status", frauds.getFirst().getStatus(), is(CONFIRMED.getStatus()));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("764")
    @DisplayName("BO user can resolve client in with fraud type NEWS_TRADER")
    public void resolveClientNewsTraderAbuseTest() throws Exception {
        FraudType fraud = FraudType.NEWS_TRADER;
        createSimpleAlert(resolveClient.getUcid(), fraud.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud);
        List<AbuserFraudType> frauds = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, String.format(WHERE_STATEMENT, resolveClient.getUcid()), AbuserFraudType.class);
        assertThat("Verify there is only 1 fraud", frauds.size(), is(1));
        assertThat("Verify fraud type", frauds.getFirst().getFraudTypeCode(), is(fraud.getCode()));
        assertThat("Verify status", frauds.getFirst().getStatus(), is(CONFIRMED.getStatus()));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("302")
    @DisplayName("BO user can resolve client in with fraud type CHARGEBACK")
    public void resolveClientChargebackAbuseTest() throws Exception {
        FraudType fraud = FraudType.CHARGEBACK;
        createSimpleAlert(resolveClient.getUcid(), fraud.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud);
        List<AbuserFraudType> frauds = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, String.format(WHERE_STATEMENT, resolveClient.getUcid()), AbuserFraudType.class);
        assertThat("Verify there is only 1 fraud", frauds.size(), is(1));
        assertThat("Verify fraud type", frauds.getFirst().getFraudTypeCode(), is(fraud.getCode()));
        assertThat("Verify status", frauds.getFirst().getStatus(), is(CONFIRMED.getStatus()));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("296")
    @DisplayName("BO user can resolve client in with fraud type MARKET_MANIPULATION")
    public void resolveClientGapMarketManipulationTest() throws Exception {
        FraudType fraud = FraudType.MARKET_MANIPULATION;
        createSimpleAlert(resolveClient.getUcid(), fraud.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud);
        List<AbuserFraudType> frauds = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, String.format(WHERE_STATEMENT, resolveClient.getUcid()), AbuserFraudType.class);
        assertThat("Verify there is only 1 fraud", frauds.size(), is(1));
        assertThat("Verify fraud type", frauds.getFirst().getFraudTypeCode(), is(fraud.getCode()));
        assertThat("Verify status", frauds.getFirst().getStatus(), is(CONFIRMED.getStatus()));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("299")
    @DisplayName("BO user can resolve client in with fraud type SWAP_ARBITRAGE")
    public void resolveClientSwapArbitrageTest() throws Exception {
        FraudType fraud = FraudType.SWAP_ARBITRAGE;
        createSimpleAlert(resolveClient.getUcid(), fraud.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud);
        List<AbuserFraudType> frauds = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, String.format(WHERE_STATEMENT, resolveClient.getUcid()), AbuserFraudType.class);
        assertThat("Verify there is only 1 fraud", frauds.size(), is(1));
        assertThat("Verify fraud type", frauds.getFirst().getFraudTypeCode(), is(fraud.getCode()));
        assertThat("Verify status", frauds.getFirst().getStatus(), is(CONFIRMED.getStatus()));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("297")
    @DisplayName("BO user can resolve client in with fraud type PRICING_ERRORS")
    public void resolveClientGapPricingErrorsTest() throws Exception {
        FraudType fraud = FraudType.PRICING_ERROR;
        createSimpleAlert(resolveClient.getUcid(), fraud.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud);
        List<AbuserFraudType> frauds = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, String.format(WHERE_STATEMENT, resolveClient.getUcid()), AbuserFraudType.class);
        assertThat("Verify there is only 1 fraud", frauds.size(), is(1));
        assertThat("Verify fraud type", frauds.getFirst().getFraudTypeCode(), is(fraud.getCode()));
        assertThat("Verify status", frauds.getFirst().getStatus(), is(CONFIRMED.getStatus()));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("765")
    @DisplayName("BO user can resolve client in with fraud type SLIPPAGE_FREE_ABUSE")
    public void resolveClientSlippageFreeAbuseTest() throws Exception {
        FraudType fraud = FraudType.SLIPPAGE_FREE_ABUSE;
        createSimpleAlert(resolveClient.getUcid(), fraud.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud);
        List<AbuserFraudType> frauds = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, String.format(WHERE_STATEMENT, resolveClient.getUcid()), AbuserFraudType.class);
        assertThat("Verify there is only 1 fraud", frauds.size(), is(1));
        assertThat("Verify fraud type", frauds.getFirst().getFraudTypeCode(), is(fraud.getCode()));
        assertThat("Verify status", frauds.getFirst().getStatus(), is(CONFIRMED.getStatus()));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("322")
    @DisplayName("BO user can resolve client in with multiple fraud types")
    public void resolveClientMultipleAbuseTest() throws Exception {
        FraudType fraud1 = FraudType.HEDGING;
        FraudType fraud2 = FraudType.TLS_ABUSE;
        createSimpleAlert(resolveClient.getUcid(), fraud1.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddMultipleFraud("test" + timestamp, fraud1, fraud2);
        List<AbuserFraudType> frauds = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, String.format(WHERE_STATEMENT, resolveClient.getUcid()), AbuserFraudType.class);
        assertThat("Verify there are 2 frauds", frauds.size(), is(2));
        assertThat("Verify first fraud type", frauds.getFirst().getFraudTypeCode(), anyOf(is(fraud1.getCode()), is(fraud2.getCode())));
        assertThat("Verify second fraud type", frauds.getLast().getFraudTypeCode(), anyOf(is(fraud1.getCode()), is(fraud2.getCode())));
        assertThat("Verify first fraud status", frauds.getFirst().getStatus(), is(CONFIRMED.getStatus()));
        assertThat("Verify first fraud status", frauds.getLast().getStatus(), is(CONFIRMED.getStatus()));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("325")
    @DisplayName("BO user can resolve client without any applied fraud")
    public void resolveClientNoFraudTest() throws Exception {
        createSimpleAlert(resolveClient.getUcid(), FraudType.TLS_ABUSE.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveNoFrauds("test" + timestamp);
        List<AbuserFraudType> frauds = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, String.format(WHERE_STATEMENT, resolveClient.getUcid()), AbuserFraudType.class);
        assertThat("Verify there are no frauds", frauds, is(empty()));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("291")
    @DisplayName("BO user can't type more than a 250 symbols into resolve commentary section")
    public void cantTypeMoreThan250CommentTest() throws Exception {
        createSimpleAlert(resolveClient.getUcid(), FraudType.TLS_ABUSE.getCode());
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
    @DisplayName("On resolve violated rule can be tagged as false positive")
    public void resolveFalsePositiveTest() throws Exception {
        createSimpleAlert(resolveClient.getUcid(), FraudType.TLS_ABUSE.getCode());
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
    @DisplayName("On resolve violated rule can be tagged as true positive")
    public void resolveTruePositiveTest() throws Exception {
        FraudType fraud = FraudType.TLS_ABUSE;
        createSimpleAlert(resolveClient.getUcid(), fraud.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, fraud);
        checkUserAlertConfirmation(resolveClient.getUcid(), "CONFIRMED");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("250")
    @DisplayName("Resolve have actual list of violations")
    public void resolveViolationsListTest() {
        createSimpleAlert(resolveClient.getUcid(), FraudType.TLS_ABUSE.getCode());
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
        Response response = enableCRMEmulator();
        assertNotNull(response);
        RestrictionPage.setRestrictionAPIGeneral(resolveClient.getUcid(), restriction.getCode());
        createSimpleAlert(resolveClient.getUcid(), FraudType.TLS_ABUSE.getCode());
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
        createSimpleAlert(resolveClient.getUcid(), FraudType.TLS_ABUSE.getCode());
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
    public void commentOutsideResolveTest() {
        String comment = "test" + timestamp;
        createSimpleAlert(resolveClient.getUcid(), FraudType.CPA_ABUSE.getCode());
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
        FraudType fraud = FraudType.CPA_ABUSE;
        createSimpleAlert(resolveClient.getUcid(), fraud.getCode());
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
        FraudType fraud = FraudType.HEDGING;
        addFraudsForClient(resolveClient, List.of(fraud), CONFIRMED);
        cleanUserAudit(resolveClient.getUcid());
        createSimpleAlert(resolveClient.getUcid(), fraud.getCode());
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
        addFraudsForClient(resolveClient, List.of(FraudType.HEDGING), CONFIRMED);
        createSimpleAlert(resolveClient.getUcid(), FraudType.CPA_ABUSE.getCode());
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
        createSimpleAlert(resolveClient.getUcid(), FraudType.CPA_ABUSE.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveAddFraud("test" + timestamp, FraudType.HEDGING);
        checkUserAlertConfirmation(resolveClient.getUcid(), "FRAUD_TYPE_MISMATCH");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1089")
    @Feature("BMS-903 Manage restrictions & fraud types from resolve")
    @DisplayName("Alert in 'False Positive' state when fraud in alert not match fraud in history")
    public void resolveNoFraudAssignedDeleteFraudInHistoryTest() throws Exception {
        FraudType fraud = FraudType.HEDGING;
        addFraudsForClient(resolveClient, List.of(fraud), CONFIRMED);
        createSimpleAlert(resolveClient.getUcid(), fraud.getCode());
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
        addFraudsForClient(resolveClient, List.of(FraudType.CPA_ABUSE), CONFIRMED);
        FraudType fraud = FraudType.HEDGING;
        cleanUserAudit(resolveClient.getUcid());
        createSimpleAlert(resolveClient.getUcid(), fraud.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.addFraud(FraudType.HEDGING);
        resolvePage.checkFraudDisplayed(FraudType.CPA_ABUSE.getName(), FraudType.HEDGING.getName());
        resolvePage.resetFraudsChanges();
        resolvePage.checkFraudDisplayed(FraudType.CPA_ABUSE.getName());
        resolvePage.checkFraudNotDisplayed(FraudType.HEDGING.getName());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1091")
    @Feature("BMS-903 Manage restrictions & fraud types from resolve")
    @DisplayName("Restrictions can be added on resolve screen")
    public void restrictionCanBeAdded() throws Exception {
        cleanUserRestrictionGeneral(resolveClient.getUcid());
        addFraudsForClient(resolveClient, List.of(FraudType.CPA_ABUSE), CONFIRMED);
        FraudType fraud = FraudType.HEDGING;
        createSimpleAlert(resolveClient.getUcid(), fraud.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.addRestriction(Restriction.DEPOSITS.getName());
        resolvePage.resolveFillCommentary("test" + timestamp);
        resolvePage.resolveInvestigation();
        List<ClientGeneralRestriction> restrictionList = getObjectsFromDB(DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION, String.format("ucid = '%s' and restriction_id = %s and status = '%s'", resolveClient.getUcid(), DEPOSITS.getId(), APPLIED_STATUS), ClientGeneralRestriction.class);
        assertThat("Verify restriction is present in DB", restrictionList.size(), is(1));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1091")
    @Feature("BMS-903 Manage restrictions & fraud types from resolve")
    @DisplayName("Restrictions can be added and then removed on resolve screen")
    public void restrictionCanBeAddedAndRemoved() throws Exception {
        cleanUserRestrictionGeneral(resolveClient.getUcid());
        addFraudsForClient(resolveClient, List.of(FraudType.CPA_ABUSE), CONFIRMED);
        FraudType fraud = FraudType.HEDGING;
        createSimpleAlert(resolveClient.getUcid(), fraud.getCode());
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
        cleanUserRestrictionGeneral(resolveClient.getUcid());
        addFraudsForClient(resolveClient, List.of(FraudType.CPA_ABUSE), CONFIRMED);
        FraudType fraud = FraudType.HEDGING;
        createSimpleAlert(resolveClient.getUcid(), fraud.getCode());
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
        cleanUserRestrictionGeneral(resolveClient.getUcid());
        FraudType fraud = FraudType.HEDGING;
        createSimpleAlert(resolveClient.getUcid(), fraud.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.addFraud(FraudType.HEDGING);
        resolvePage.checkRestrictionDisplayed(ACCOUNT_CREATION.getName(), DEPOSITS.getName(), CREDIT_AND_BONUS.getName(), INTERNAL_TRANSFER.getName(), WITHDRAWALS.getName());
        resolvePage.checkRestrictionNotDisplayed(MANUAL_WITHDRAWAL_REVIEW.getName());
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();
        resolvePage.addFraud(FraudType.SWAP_ARBITRAGE);
        resolvePage.checkRestrictionDisplayed(ACCOUNT_CREATION.getName(), MANUAL_WITHDRAWAL_REVIEW.getName());
        resolvePage.checkRestrictionNotDisplayed(DEPOSITS.getName(), CREDIT_AND_BONUS.getName(), INTERNAL_TRANSFER.getName(), WITHDRAWALS.getName());
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();
        resolvePage.addFraud(FraudType.MARKET_MANIPULATION);
        resolvePage.checkRestrictionDisplayed(ACCOUNT_CREATION.getName(), DEPOSITS.getName(), CREDIT_AND_BONUS.getName(), INTERNAL_TRANSFER.getName(), WITHDRAWALS.getName());
        resolvePage.checkRestrictionNotDisplayed(MANUAL_WITHDRAWAL_REVIEW.getName());
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();
        resolvePage.addFraud(FraudType.LATENCY_ARBITRAGE);
        resolvePage.checkRestrictionDisplayed(ACCOUNT_CREATION.getName(), DEPOSITS.getName(), CREDIT_AND_BONUS.getName(), INTERNAL_TRANSFER.getName(), WITHDRAWALS.getName());
        resolvePage.checkRestrictionNotDisplayed(MANUAL_WITHDRAWAL_REVIEW.getName());
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();
        resolvePage.addFraud(FraudType.CPA_ABUSE);
        resolvePage.checkRestrictionDisplayed(ACCOUNT_CREATION.getName(), DEPOSITS.getName(), CREDIT_AND_BONUS.getName());
        resolvePage.checkRestrictionNotDisplayed(MANUAL_WITHDRAWAL_REVIEW.getName(), INTERNAL_TRANSFER.getName(), WITHDRAWALS.getName());
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();
        resolvePage.addFraud(FraudType.NBP_ABUSE);
        resolvePage.checkRestrictionDisplayed(CREDIT_AND_BONUS.getName(), MANUAL_WITHDRAWAL_REVIEW.getName());
        resolvePage.checkRestrictionNotDisplayed(ACCOUNT_CREATION.getName(), DEPOSITS.getName(), INTERNAL_TRANSFER.getName(), WITHDRAWALS.getName());
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();
        resolvePage.addFraud(FraudType.LOSS_VOUCHER_ABUSE);
        resolvePage.checkRestrictionDisplayed(ACCOUNT_CREATION.getName(), DEPOSITS.getName(), CREDIT_AND_BONUS.getName(), INTERNAL_TRANSFER.getName(), WITHDRAWALS.getName());
        resolvePage.checkRestrictionNotDisplayed(MANUAL_WITHDRAWAL_REVIEW.getName());
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();
        resolvePage.addFraud(FraudType.GAP_TRADING);
        resolvePage.checkRestrictionDisplayed(ACCOUNT_CREATION.getName(), DEPOSITS.getName(), CREDIT_AND_BONUS.getName(), INTERNAL_TRANSFER.getName());
        resolvePage.checkRestrictionNotDisplayed(MANUAL_WITHDRAWAL_REVIEW.getName(), WITHDRAWALS.getName());
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();
        resolvePage.addFraud(FraudType.REBATE_CHURNING);
        resolvePage.checkRestrictionDisplayed(ACCOUNT_CREATION.getName(), DEPOSITS.getName(), CREDIT_AND_BONUS.getName(), INTERNAL_TRANSFER.getName(), WITHDRAWALS.getName());
        resolvePage.checkRestrictionNotDisplayed(MANUAL_WITHDRAWAL_REVIEW.getName());
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();
        resolvePage.addFraud(FraudType.PRICING_ERROR);
        resolvePage.checkRestrictionDisplayed(ACCOUNT_CREATION.getName(), DEPOSITS.getName(), CREDIT_AND_BONUS.getName(), INTERNAL_TRANSFER.getName(), WITHDRAWALS.getName());
        resolvePage.checkRestrictionNotDisplayed(MANUAL_WITHDRAWAL_REVIEW.getName());
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();
        resolvePage.addFraud(FraudType.TLS_ABUSE);
        resolvePage.checkRestrictionDisplayed(ACCOUNT_CREATION.getName(), DEPOSITS.getName(), CREDIT_AND_BONUS.getName(), INTERNAL_TRANSFER.getName(), WITHDRAWALS.getName());
        resolvePage.checkRestrictionNotDisplayed(MANUAL_WITHDRAWAL_REVIEW.getName());
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();
        resolvePage.addFraud(FraudType.BONUS_ABUSE);
        resolvePage.checkRestrictionDisplayed(ACCOUNT_CREATION.getName(), DEPOSITS.getName(), CREDIT_AND_BONUS.getName(), INTERNAL_TRANSFER.getName(), WITHDRAWALS.getName());
        resolvePage.checkRestrictionNotDisplayed(MANUAL_WITHDRAWAL_REVIEW.getName());
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();
        resolvePage.addFraud(FraudType.LOOPHOLE_ABUSE);
        resolvePage.checkRestrictionDisplayed(ACCOUNT_CREATION.getName(), DEPOSITS.getName(), CREDIT_AND_BONUS.getName(), INTERNAL_TRANSFER.getName(), WITHDRAWALS.getName());
        resolvePage.checkRestrictionNotDisplayed(MANUAL_WITHDRAWAL_REVIEW.getName());
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();
        resolvePage.addFraud(FraudType.NEWS_TRADER);
        resolvePage.checkRestrictionDisplayed(CREDIT_AND_BONUS.getName(), MANUAL_WITHDRAWAL_REVIEW.getName());
        resolvePage.checkRestrictionNotDisplayed(ACCOUNT_CREATION.getName(), DEPOSITS.getName(), INTERNAL_TRANSFER.getName(), WITHDRAWALS.getName());
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();
    }

}
