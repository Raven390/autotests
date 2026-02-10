package tests.vindex_backoffice_ui_tests.investigationTool;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoerced;
import static business_objects.kafka.alerts.RuleAlertFactory.*;
import static helpers.api.AbuseRegistryHelper.*;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.FraudTypeStatus.*;
import static helpers.data.enums.Restriction.*;
import static helpers.database.ArHelper.deleteUserFromAbuseRegistry;
import static helpers.database.ArHelper.waitForClientToChangeStatus;
import static helpers.database.BoHelper.*;
import static helpers.database.CleanTableHelper.*;
import static helpers.database.DbHelper.*;
import static helpers.database.DbName.POSTGRES;
import static helpers.database.PaymentGateHelper.generateTradingWithdrawalPaymentGateData;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.Constants.*;
import static utils.Utils.insertCrmAccountsToDb;

import business_objects.db.abuse_registry_db.Abuser;
import business_objects.db.abuse_registry_db.AbuserFraudType;
import business_objects.db.abuse_registry_db.AbuserHistory;
import business_objects.db.backoffice_db.Investigation;
import business_objects.db.backoffice_db.alert.Alert;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObject;
import business_objects.db.mitigation_service_db.ClientGeneralRestriction;
import business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObject;
import business_objects.kafka.alerts.PaymentAlertMessageV2;
import business_objects.kafka.alerts.RuleAlert;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.data.PaymentGateData;
import helpers.data.enums.*;
import helpers.database.DbName;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import java.util.List;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
public class ResolveTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmUser = generateUserByClient(client);
    private static final CrmTbAccountObject crmAccount = generateCrmTbAccountDataForUi(client);
    private static final MtAccountObject mtAccount = generateMtAccountByCrmTbAccount(crmAccount);
    private static final RuleAlert alert = generateRuleAlertByUcid(client);
    private static PaymentGateData pgsData;
    private static PaymentAlertMessageV2 withdrawalAlert;
    private static MtMt4TradesCoercedObject trade1 = generateMt4TradesCoerced(client);
    private static MtMt4TradesCoercedObject trade2 = generateMt4TradesCoerced(client);
    private static final String RESOLVE_COMMENT = "Autotest resolve comment";
    private static final String ALERT_WHERE = "client_ucid = '%s'";
    private static final String UCID_WHERE = "ucid = '%s'";
    private static final String UCID_AND_FRAUD_WHERE = "ucid = '%s' and fraud_type_code = '%s'";

    @BeforeAll
    static void setup() throws JsonProcessingException {
        objectMapper.findAndRegisterModules();
        insertObjectToDb(CRM_USER_TABLE_NAME, crmUser);
        insertCrmAccountsToDb(crmAccount);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, mtAccount);
        trade1 = generateMt4TradesCoerced(client);
        trade2 = generateMt4TradesCoerced(client);
        trade1.setSymbol("USDEUR");
        trade2.setSymbol("JPYCZK");
        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade1, trade2));
        pgsData = generateTradingWithdrawalPaymentGateData(client);
        insertObjectToDb(POSTGRES, PAYMENT_EVENT_TABLE_NAME, pgsData.getPaymentEvent());
        insertObjectToDb(POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, pgsData.getPaymentDetails());
        insertObjectToDb(POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, pgsData.getPaymentDecisions());
        withdrawalAlert = generateTradingAlertWithDecision(
                client.getUcid(),
                "withdrawal",
                pgsData.getPaymentEvent().getPaymentId().toString());
    }

    @AfterAll
    static void teardown() throws Exception {
        cleanCrmUserTableByClient(client.getUcid());
        deleteObjectFromDb(MT4_TRADES_COERCED_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteUserBO(client.getUcid());
        cleanUserAudit(client.getUcid());
        deleteUserFromAbuseRegistry(client.getUcid());
        cleanUserRestrictionGeneral(client.getUcid());
    }

    @BeforeEach
    void setupEach() throws Exception {
        deleteUserBO(client.getUcid());
        cleanUserAudit(client.getUcid());
        deleteUserFromAbuseRegistry(client.getUcid());
        cleanUserRestrictionGeneral(client.getUcid());
    }

    @Test
    @AllureId("432")
    @DisplayName("Verify list of fraud types, subtypes, symbols, restrictions")
    void resolveTest1() throws Exception {
        kafka.produceMessages(alert.alertId, KAFKA_TOPIC_ALERTS, objectMapper.writeValueAsString(alert));
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsTradingOpsUser();
        investigationPage.navigateToClient(client.getUcid());
        resolvePage.openResolveSuspicious();
        assertThat(
                "Check fraud types",
                resolvePage.getFraudTypesList(),
                containsInAnyOrder(FraudType.getTradingFraudTypeNamesList().toArray()));
        assertThat(
                "Check hedging subtypes",
                resolvePage.getFraudSubtypesList(FraudType.HEDGING),
                containsInAnyOrder(FraudSubtype.INTERNAL.getName(), FraudSubtype.EXTERNAL.getName()));
        assertThat(
                "Check news trader subtypes",
                resolvePage.getFraudSubtypesList(FraudType.NEWS_TRADER),
                containsInAnyOrder(
                        FraudSubtype.BEFORE_NEWS.getName(),
                        FraudSubtype.BEFORE_NEWS_DEDUCTION.getName(),
                        FraudSubtype.AFTER_NEWS.getName()));
        assertThat(
                "Check swap arbitrage subtypes",
                resolvePage.getFraudSubtypesList(FraudType.SWAP_ARBITRAGE),
                containsInAnyOrder(FraudSubtype.SINGLE_ACCOUNT.getName(), FraudSubtype.HEDGING_STRATEGY.getName()));
        resolvePage.clickFraudListButton();
        resolvePage.addFraud(FraudType.BONUS_ABUSE);
        assertThat(
                "Check symbols list",
                resolvePage.getSymbolsList(),
                containsInAnyOrder(trade1.getSymbol(), trade2.getSymbol()));
        assertThat(
                "Check restrictions list",
                resolvePage.getRestrictionsList(),
                containsInAnyOrder(getVisibleRestrictionsList().toArray()));
    }

    @Test
    @AllureId("1323")
    @Feature("BMS-1139 Predefined restrictions")
    @DisplayName("Predefined restriction for the fraud type appears on the resolve screen")
    void resolveTest2() throws Exception {
        kafka.produceMessages(alert.alertId, KAFKA_TOPIC_ALERTS, objectMapper.writeValueAsString(alert));
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsTradingOpsUser();
        investigationPage.navigateToClient(client.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();

        // Check for confirmed frauds

        resolvePage.addFraud(FraudType.BONUS_ABUSE);
        assertThat(
                resolvePage.getSelectedRestrictionsList(),
                containsInAnyOrder(
                        ACCOUNT_CREATION.getName(),
                        DEPOSITS.getName(),
                        CREDIT_AND_BONUS.getName(),
                        INTERNAL_TRANSFER.getName(),
                        WITHDRAWALS.getName(),
                        CLOSE_ONLY_MODE.getName()));
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();

        resolvePage.addFraud(FraudType.CHARGEBACK);
        assertThat(
                resolvePage.getSelectedRestrictionsList(),
                containsInAnyOrder(
                        ACCOUNT_CREATION.getName(),
                        DEPOSITS.getName(),
                        CREDIT_AND_BONUS.getName(),
                        INTERNAL_TRANSFER.getName(),
                        CLOSE_ONLY_MODE.getName()));
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();

        resolvePage.addFraud(FraudType.CPA_ABUSE);
        assertThat(resolvePage.getSelectedRestrictionsList(), empty());
        resolvePage.resetFraudsChanges();

        resolvePage.addFraud(FraudType.GAP_TRADING);
        assertThat(
                resolvePage.getSelectedRestrictionsList(),
                containsInAnyOrder(INTERNAL_TRANSFER.getName(), WITHDRAWALS.getName()));
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();

        resolvePage.addFraud(FraudType.HEDGING, CONFIRMED, FraudSubtype.EXTERNAL);
        assertThat(
                resolvePage.getSelectedRestrictionsList(),
                containsInAnyOrder(
                        ACCOUNT_CREATION.getName(),
                        DEPOSITS.getName(),
                        CREDIT_AND_BONUS.getName(),
                        INTERNAL_TRANSFER.getName(),
                        WITHDRAWALS.getName(),
                        CLOSE_ONLY_MODE.getName()));
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();

        resolvePage.addFraud(FraudType.LATENCY_ARBITRAGE);
        assertThat(
                resolvePage.getSelectedRestrictionsList(),
                containsInAnyOrder(
                        ACCOUNT_CREATION.getName(),
                        DEPOSITS.getName(),
                        CREDIT_AND_BONUS.getName(),
                        INTERNAL_TRANSFER.getName(),
                        WITHDRAWALS.getName(),
                        CLOSE_ONLY_MODE.getName()));
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();

        resolvePage.addFraud(FraudType.LOOPHOLE_ABUSE);
        assertThat(
                resolvePage.getSelectedRestrictionsList(),
                containsInAnyOrder(
                        ACCOUNT_CREATION.getName(),
                        DEPOSITS.getName(),
                        CREDIT_AND_BONUS.getName(),
                        INTERNAL_TRANSFER.getName(),
                        WITHDRAWALS.getName(),
                        CLOSE_ONLY_MODE.getName()));
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();

        resolvePage.addFraud(FraudType.LOSS_VOUCHER_ABUSE);
        assertThat(
                resolvePage.getSelectedRestrictionsList(),
                containsInAnyOrder(
                        ACCOUNT_CREATION.getName(),
                        DEPOSITS.getName(),
                        CREDIT_AND_BONUS.getName(),
                        INTERNAL_TRANSFER.getName(),
                        WITHDRAWALS.getName(),
                        CLOSE_ONLY_MODE.getName()));
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();

        resolvePage.addFraud(FraudType.MARKET_MANIPULATION);
        assertThat(
                resolvePage.getSelectedRestrictionsList(),
                containsInAnyOrder(
                        ACCOUNT_CREATION.getName(),
                        DEPOSITS.getName(),
                        CREDIT_AND_BONUS.getName(),
                        INTERNAL_TRANSFER.getName(),
                        WITHDRAWALS.getName(),
                        CLOSE_ONLY_MODE.getName()));
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();

        resolvePage.addFraud(FraudType.NBP_ABUSE);
        assertThat(resolvePage.getSelectedRestrictionsList(), containsInAnyOrder(CREDIT_AND_BONUS.getName()));
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();

        resolvePage.addFraud(FraudType.NEWS_TRADER, CONFIRMED, FraudSubtype.BEFORE_NEWS_DEDUCTION);
        assertThat(
                resolvePage.getSelectedRestrictionsList(),
                containsInAnyOrder(
                        ACCOUNT_CREATION.getName(),
                        DEPOSITS.getName(),
                        CREDIT_AND_BONUS.getName(),
                        INTERNAL_TRANSFER.getName(),
                        WITHDRAWALS.getName(),
                        CLOSE_ONLY_MODE.getName()));
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();

        resolvePage.addFraud(FraudType.NEWS_TRADER, CONFIRMED, FraudSubtype.BEFORE_NEWS);
        assertThat(
                resolvePage.getSelectedRestrictionsList(),
                containsInAnyOrder(ACCOUNT_CREATION.getName(), CREDIT_AND_BONUS.getName()));
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();

        resolvePage.addFraud(FraudType.PRICING_ERROR);
        assertThat(
                resolvePage.getSelectedRestrictionsList(),
                containsInAnyOrder(
                        ACCOUNT_CREATION.getName(),
                        DEPOSITS.getName(),
                        CREDIT_AND_BONUS.getName(),
                        INTERNAL_TRANSFER.getName(),
                        WITHDRAWALS.getName(),
                        CLOSE_ONLY_MODE.getName()));
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();

        resolvePage.addFraud(FraudType.REBATE_CHURNING);
        assertThat(
                resolvePage.getSelectedRestrictionsList(),
                containsInAnyOrder(
                        ACCOUNT_CREATION.getName(),
                        DEPOSITS.getName(),
                        CREDIT_AND_BONUS.getName(),
                        INTERNAL_TRANSFER.getName(),
                        WITHDRAWALS.getName(),
                        CLOSE_ONLY_MODE.getName()));
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();

        resolvePage.addFraud(FraudType.SLIPPAGE_FREE_ABUSE);
        assertThat(resolvePage.getSelectedRestrictionsList(), empty());
        resolvePage.resetFraudsChanges();

        resolvePage.addFraud(FraudType.SWAP_ARBITRAGE, CONFIRMED, FraudSubtype.SINGLE_ACCOUNT);
        assertThat(
                resolvePage.getSelectedRestrictionsList(),
                containsInAnyOrder(ACCOUNT_CREATION.getName(), MANUAL_WITHDRAWAL_REVIEW.getName()));
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();

        resolvePage.addFraud(FraudType.TLS_ABUSE);
        assertThat(
                resolvePage.getSelectedRestrictionsList(),
                containsInAnyOrder(
                        ACCOUNT_CREATION.getName(),
                        DEPOSITS.getName(),
                        CREDIT_AND_BONUS.getName(),
                        INTERNAL_TRANSFER.getName(),
                        WITHDRAWALS.getName(),
                        CLOSE_ONLY_MODE.getName()));
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();

        // Check for potential frauds

        resolvePage.addFraud(FraudType.BONUS_ABUSE, POTENTIAL);
        assertThat(resolvePage.getSelectedRestrictionsList(), contains(MANUAL_WITHDRAWAL_REVIEW.getName()));
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();

        resolvePage.addFraud(FraudType.CHARGEBACK, POTENTIAL);
        assertThat(resolvePage.getSelectedRestrictionsList(), empty());
        resolvePage.resetFraudsChanges();

        resolvePage.addFraud(FraudType.CPA_ABUSE, POTENTIAL);
        assertThat(resolvePage.getSelectedRestrictionsList(), contains(MANUAL_WITHDRAWAL_REVIEW.getName()));
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();

        resolvePage.addFraud(FraudType.GAP_TRADING, POTENTIAL);
        assertThat(resolvePage.getSelectedRestrictionsList(), contains(MANUAL_WITHDRAWAL_REVIEW.getName()));
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();

        resolvePage.addFraud(FraudType.HEDGING, POTENTIAL);
        assertThat(resolvePage.getSelectedRestrictionsList(), contains(MANUAL_WITHDRAWAL_REVIEW.getName()));
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();

        resolvePage.addFraud(FraudType.LATENCY_ARBITRAGE, POTENTIAL);
        assertThat(resolvePage.getSelectedRestrictionsList(), contains(MANUAL_WITHDRAWAL_REVIEW.getName()));
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();

        resolvePage.addFraud(FraudType.LOOPHOLE_ABUSE, POTENTIAL);
        assertThat(resolvePage.getSelectedRestrictionsList(), contains(MANUAL_WITHDRAWAL_REVIEW.getName()));
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();

        resolvePage.addFraud(FraudType.LOSS_VOUCHER_ABUSE, POTENTIAL);
        assertThat(resolvePage.getSelectedRestrictionsList(), contains(MANUAL_WITHDRAWAL_REVIEW.getName()));
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();

        resolvePage.addFraud(FraudType.MARKET_MANIPULATION, POTENTIAL);
        assertThat(resolvePage.getSelectedRestrictionsList(), contains(MANUAL_WITHDRAWAL_REVIEW.getName()));
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();

        resolvePage.addFraud(FraudType.NBP_ABUSE, POTENTIAL);
        assertThat(resolvePage.getSelectedRestrictionsList(), contains(MANUAL_WITHDRAWAL_REVIEW.getName()));
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();

        resolvePage.addFraud(FraudType.NEWS_TRADER, POTENTIAL);
        assertThat(resolvePage.getSelectedRestrictionsList(), contains(MANUAL_WITHDRAWAL_REVIEW.getName()));
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();

        resolvePage.addFraud(FraudType.PRICING_ERROR, POTENTIAL);
        assertThat(resolvePage.getSelectedRestrictionsList(), contains(MANUAL_WITHDRAWAL_REVIEW.getName()));
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();

        resolvePage.addFraud(FraudType.REBATE_CHURNING, POTENTIAL);
        assertThat(resolvePage.getSelectedRestrictionsList(), contains(MANUAL_WITHDRAWAL_REVIEW.getName()));
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();

        resolvePage.addFraud(FraudType.SLIPPAGE_FREE_ABUSE, POTENTIAL);
        assertThat(resolvePage.getSelectedRestrictionsList(), contains(MANUAL_WITHDRAWAL_REVIEW.getName()));
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();

        resolvePage.addFraud(FraudType.SWAP_ARBITRAGE, POTENTIAL);
        assertThat(resolvePage.getSelectedRestrictionsList(), contains(MANUAL_WITHDRAWAL_REVIEW.getName()));
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();

        resolvePage.addFraud(FraudType.TLS_ABUSE, POTENTIAL);
        assertThat(resolvePage.getSelectedRestrictionsList(), contains(MANUAL_WITHDRAWAL_REVIEW.getName()));
        resolvePage.resetRestrictionChanges();
        resolvePage.resetFraudsChanges();
    }

    @Test
    @AllureId("286")
    @DisplayName("Resolve with alert_resolution CONFIRMED")
    void resolveTest3() throws Exception {
        kafka.produceMessages(alert.alertId, KAFKA_TOPIC_ALERTS, objectMapper.writeValueAsString(alert));
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsTradingOpsUser();
        investigationPage.navigateToClient(client.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.addFraud(FraudType.HEDGING, FraudSubtype.EXTERNAL);
        resolvePage.selectAllAccountsAsIllegalProfit();
        resolvePage.fillCommentAndApply(RESOLVE_COMMENT);
        waitForAlertsToClose(client.getUcid());
        Alert dbAlert = getObjectsFromDB(
                        DbName.POSTGRES, BO_ALERT_TABLE_NAME, String.format(ALERT_WHERE, client.getUcid()), Alert.class)
                .getFirst();
        assertThat(
                "Verify alert_resolution is CONFIRMED",
                dbAlert.getAlertResolution(),
                is(AlertResolution.CONFIRMED.getDisplayName()));
    }

    @Test
    @AllureId("226")
    @DisplayName("Resolve with alert_resolution FALSE_POSITIVE")
    void resolveTest4() throws Exception {
        kafka.produceMessages(alert.alertId, KAFKA_TOPIC_ALERTS, objectMapper.writeValueAsString(alert));
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsTradingOpsUser();
        investigationPage.navigateToClient(client.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.fillCommentAndApply(RESOLVE_COMMENT);
        waitForAlertsToClose(client.getUcid());
        Alert dbAlert = getObjectsFromDB(
                        DbName.POSTGRES, BO_ALERT_TABLE_NAME, String.format(ALERT_WHERE, client.getUcid()), Alert.class)
                .getFirst();
        assertThat(
                "Verify alert_resolution is FALSE_POSITIVE",
                dbAlert.getAlertResolution(),
                is(AlertResolution.FALSE_POSITIVE.getDisplayName()));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("294")
    @DisplayName("Resolve with alert_resolution FRAUD_TYPE_MISMATCH")
    void resolveTest5() throws Exception {
        kafka.produceMessages(alert.alertId, KAFKA_TOPIC_ALERTS, objectMapper.writeValueAsString(alert));
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsTradingOpsUser();
        investigationPage.navigateToClient(client.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.addFraud(FraudType.BONUS_ABUSE);
        resolvePage.selectAllAccountsAsIllegalProfit();
        resolvePage.fillCommentAndApply(RESOLVE_COMMENT);
        waitForAlertsToClose(client.getUcid());
        Alert dbAlert = getObjectsFromDB(
                        DbName.POSTGRES, BO_ALERT_TABLE_NAME, String.format(ALERT_WHERE, client.getUcid()), Alert.class)
                .getFirst();
        assertThat(
                "Verify alert_resolution is FRAUD_TYPE_MISMATCH",
                dbAlert.getAlertResolution(),
                is(AlertResolution.FRAUD_TYPE_MISMATCH.getDisplayName()));
    }

    @Test
    @AllureId("295")
    @DisplayName("Resolution works when confirmed fraud is applied to client with no frauds")
    void resolveTest6() throws Exception {
        kafka.produceMessages(alert.alertId, KAFKA_TOPIC_ALERTS, objectMapper.writeValueAsString(alert));
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsTradingOpsUser();
        investigationPage.navigateToClient(client.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.addFraud(FraudType.HEDGING, FraudSubtype.EXTERNAL);
        resolvePage.selectAllAccountsAsIllegalProfit();
        resolvePage.fillCommentAndApply(RESOLVE_COMMENT);
        waitForClientToChangeStatus(client.getUcid(), CONFIRMED);
        Abuser abuser = getObjectsFromDB(
                        DbName.POSTGRES,
                        AR_ABUSER_TABLE_NAME,
                        String.format(UCID_WHERE, client.getUcid()),
                        Abuser.class)
                .getFirst();
        assertThat("Verify client changed status", abuser.getStatus(), is(CONFIRMED.getStatus()));
        AbuserFraudType fraud = getObjectsFromDB(
                        DbName.POSTGRES,
                        AR_ABUSER_FRAUD_TYPE_TABLE_NAME,
                        String.format(UCID_AND_FRAUD_WHERE, client.getUcid(), FraudType.HEDGING.getCode()),
                        AbuserFraudType.class)
                .getFirst();
        assertThat("Verify client has confirmed fraud", fraud.getStatus(), is(CONFIRMED.getStatus()));
    }

    @Test
    @AllureId("300")
    @DisplayName("Resolution works when potential fraud is applied to client with no frauds")
    void resolveTest7() throws Exception {
        kafka.produceMessages(alert.alertId, KAFKA_TOPIC_ALERTS, objectMapper.writeValueAsString(alert));
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsTradingOpsUser();
        investigationPage.navigateToClient(client.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.addFraud(FraudType.HEDGING, POTENTIAL);
        resolvePage.fillCommentAndApply(RESOLVE_COMMENT);
        waitForClientToChangeStatus(client.getUcid(), POTENTIAL);
        Abuser abuser = getObjectsFromDB(
                        DbName.POSTGRES,
                        AR_ABUSER_TABLE_NAME,
                        String.format(UCID_WHERE, client.getUcid()),
                        Abuser.class)
                .getFirst();
        assertThat("Verify client changed status", abuser.getStatus(), is(POTENTIAL.getStatus()));
        AbuserFraudType fraud = getObjectsFromDB(
                        DbName.POSTGRES,
                        AR_ABUSER_FRAUD_TYPE_TABLE_NAME,
                        String.format(UCID_AND_FRAUD_WHERE, client.getUcid(), FraudType.HEDGING.getCode()),
                        AbuserFraudType.class)
                .getFirst();
        assertThat("Verify client has potential fraud", fraud.getStatus(), is(POTENTIAL.getStatus()));
    }

    @Test
    @AllureId("301")
    @DisplayName("Resolution works when potential fraud is applied to client with cleaned frauds")
    void resolveTest8() throws Exception {
        addFraudForClient(client, FraudType.CPA_ABUSE, CONFIRMED, List.of());
        waitForClientToChangeStatus(client.getUcid(), CONFIRMED);
        addFraudForClient(client, FraudType.CPA_ABUSE, CLEANED, List.of());
        waitForClientToChangeStatus(client.getUcid(), CLEANED);
        kafka.produceMessages(alert.alertId, KAFKA_TOPIC_ALERTS, objectMapper.writeValueAsString(alert));
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsTradingOpsUser();
        investigationPage.navigateToClient(client.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.addFraud(FraudType.HEDGING, POTENTIAL);
        resolvePage.fillCommentAndApply(RESOLVE_COMMENT);
        waitForClientToChangeStatus(client.getUcid(), POTENTIAL);
        Abuser abuser = getObjectsFromDB(
                        DbName.POSTGRES,
                        AR_ABUSER_TABLE_NAME,
                        String.format(UCID_WHERE, client.getUcid()),
                        Abuser.class)
                .getFirst();
        assertThat("Verify client changed status", abuser.getStatus(), is(POTENTIAL.getStatus()));
        AbuserFraudType fraud = getObjectsFromDB(
                        DbName.POSTGRES,
                        AR_ABUSER_FRAUD_TYPE_TABLE_NAME,
                        String.format(UCID_AND_FRAUD_WHERE, client.getUcid(), FraudType.HEDGING.getCode()),
                        AbuserFraudType.class)
                .getFirst();
        assertThat("Verify client has potential fraud", fraud.getStatus(), is(POTENTIAL.getStatus()));
    }

    @Test
    @AllureId("304")
    @DisplayName("Resolution works when confirmed fraud is applied to client with confirmed frauds")
    void resolveTest9() throws Exception {
        addFraudForClient(client, FraudType.CPA_ABUSE, CONFIRMED, List.of());
        kafka.produceMessages(alert.alertId, KAFKA_TOPIC_ALERTS, objectMapper.writeValueAsString(alert));
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsTradingOpsUser();
        investigationPage.navigateToClient(client.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.addFraud(FraudType.HEDGING, FraudSubtype.EXTERNAL);
        resolvePage.selectAllAccountsAsIllegalProfit();
        resolvePage.fillCommentAndApply(RESOLVE_COMMENT);
        waitForClientToChangeStatus(client.getUcid(), CONFIRMED);
        Abuser abuser = getObjectsFromDB(
                        DbName.POSTGRES,
                        AR_ABUSER_TABLE_NAME,
                        String.format(UCID_WHERE, client.getUcid()),
                        Abuser.class)
                .getFirst();
        assertThat("Verify client remained confirmed", abuser.getStatus(), is(CONFIRMED.getStatus()));
        Thread.sleep(2000);
        AbuserFraudType fraud = getObjectsFromDB(
                        DbName.POSTGRES,
                        AR_ABUSER_FRAUD_TYPE_TABLE_NAME,
                        String.format(UCID_AND_FRAUD_WHERE, client.getUcid(), FraudType.HEDGING.getCode()),
                        AbuserFraudType.class)
                .getFirst();
        assertThat("Verify client has confirmed fraud", fraud.getStatus(), is(CONFIRMED.getStatus()));
    }

    @Test
    @AllureId("305")
    @DisplayName("Resolution works when confirmed fraud is applied to client with potential frauds")
    void resolveTest10() throws Exception {
        addFraudForClient(client, FraudType.CPA_ABUSE, POTENTIAL, List.of());
        kafka.produceMessages(alert.alertId, KAFKA_TOPIC_ALERTS, objectMapper.writeValueAsString(alert));
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsTradingOpsUser();
        investigationPage.navigateToClient(client.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.addFraud(FraudType.HEDGING, FraudSubtype.EXTERNAL);
        resolvePage.selectAllAccountsAsIllegalProfit();
        resolvePage.fillCommentAndApply(RESOLVE_COMMENT);
        waitForClientToChangeStatus(client.getUcid(), CONFIRMED);
        Abuser abuser = getObjectsFromDB(
                        DbName.POSTGRES,
                        AR_ABUSER_TABLE_NAME,
                        String.format(UCID_WHERE, client.getUcid()),
                        Abuser.class)
                .getFirst();
        assertThat("Verify client changed status", abuser.getStatus(), is(CONFIRMED.getStatus()));
        AbuserFraudType fraud = getObjectsFromDB(
                        DbName.POSTGRES,
                        AR_ABUSER_FRAUD_TYPE_TABLE_NAME,
                        String.format(UCID_AND_FRAUD_WHERE, client.getUcid(), FraudType.HEDGING.getCode()),
                        AbuserFraudType.class)
                .getFirst();
        assertThat("Verify client has confirmed fraud", fraud.getStatus(), is(CONFIRMED.getStatus()));
    }

    @Test
    @AllureId("298")
    @DisplayName("Resolution works when confirmed fraud is applied to client with potential status")
    void resolveTest11() throws Exception {
        setClientStatus(client, POTENTIAL);
        kafka.produceMessages(alert.alertId, KAFKA_TOPIC_ALERTS, objectMapper.writeValueAsString(alert));
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsTradingOpsUser();
        investigationPage.navigateToClient(client.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.addFraud(FraudType.HEDGING, FraudSubtype.EXTERNAL);
        resolvePage.selectAllAccountsAsIllegalProfit();
        resolvePage.fillCommentAndApply(RESOLVE_COMMENT);
        waitForClientToChangeStatus(client.getUcid(), CONFIRMED);
        Abuser abuser = getObjectsFromDB(
                        DbName.POSTGRES,
                        AR_ABUSER_TABLE_NAME,
                        String.format(UCID_WHERE, client.getUcid()),
                        Abuser.class)
                .getFirst();
        assertThat("Verify client changed status", abuser.getStatus(), is(CONFIRMED.getStatus()));
        AbuserFraudType fraud = getObjectsFromDB(
                        DbName.POSTGRES,
                        AR_ABUSER_FRAUD_TYPE_TABLE_NAME,
                        String.format(UCID_AND_FRAUD_WHERE, client.getUcid(), FraudType.HEDGING.getCode()),
                        AbuserFraudType.class)
                .getFirst();
        assertThat("Verify client has confirmed fraud", fraud.getStatus(), is(CONFIRMED.getStatus()));
    }

    @Test
    @AllureId("303")
    @DisplayName("Resolution works with withdrawal approval")
    void resolveTest12() throws Exception {
        kafka.produceMessages(
                withdrawalAlert.getId().toString(),
                KAFKA_TOPIC_ALERTS,
                objectMapper.writeValueAsString(withdrawalAlert));
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsTradingOpsUser();
        investigationPage.navigateToClient(client.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveWithdrawalsAllApprove(RESOLVE_COMMENT);
        List<PaymentDecisionsObject> paymentDecisionsList = getObjectsFromDB(
                POSTGRES,
                PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE,
                String.format("payment_id = '%s'", pgsData.getPaymentDecisions().getPaymentId()),
                PaymentDecisionsObject.class);
        assertThat(
                "Verify approval is present in PGS",
                paymentDecisionsList.getFirst().getDecisionCode(),
                is(1));
    }

    @Test
    @AllureId("1710")
    @DisplayName("restriction and investigation and fraud type have linked though investigation Id")
    void resolveTestInvestigationId() throws Exception {
        kafka.produceMessages(alert.alertId, KAFKA_TOPIC_ALERTS, objectMapper.writeValueAsString(alert));
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsTradingOpsUser();
        investigationPage.navigateToClient(client.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.addFraud(FraudType.HEDGING, POTENTIAL);
        resolvePage.clickCleanRestrictionList();
        resolvePage.addRestriction(ACCOUNT_CREATION.getName());
        resolvePage.fillCommentAndApply(RESOLVE_COMMENT);
        waitForClientToChangeStatus(client.getUcid(), POTENTIAL);
        Investigation investigation = getObjectsFromDB(
                        DbName.POSTGRES,
                        BO_INVESTIGATION_TABLE_NAME,
                        String.format(ALERT_WHERE, client.getUcid()),
                        Investigation.class)
                .getFirst();
        AbuserHistory history = getObjectsFromDB(
                        DbName.POSTGRES,
                        AR_ABUSER_HISTORY_TABLE_NAME,
                        String.format(UCID_WHERE, client.getUcid()),
                        AbuserHistory.class)
                .getFirst();
        ClientGeneralRestriction restriction = getObjectsFromDB(
                        DbName.POSTGRES,
                        MITIGATION_CLIENT_GENERAL_RESTRICTION,
                        String.format(UCID_WHERE, client.getUcid()),
                        ClientGeneralRestriction.class)
                .getFirst();
        assertEquals(investigation.getId().toString(), restriction.getCorrelationId());
        assertEquals(investigation.getId().toString(), restriction.getCorrelationId());
        assertEquals("INVESTIGATION", history.getCorrelationType());
        assertEquals("INVESTIGATION", restriction.getCorrelationType());
    }
}
