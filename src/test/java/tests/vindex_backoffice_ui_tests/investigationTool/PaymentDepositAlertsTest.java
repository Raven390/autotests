package tests.vindex_backoffice_ui_tests.investigationTool;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoercedAccountProfitComment;
import static business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObjectFactory.generateMtMt5PositionsObject;
import static business_objects.kafka.alerts.RuleAlertFactory.generatePaymentAlertByUcid;
import static business_objects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.Currency.USD;
import static helpers.data.enums.FraudType.MARKET_MANIPULATION;
import static helpers.data.enums.FraudTypeStatus.CONFIRMED;
import static helpers.database.ArHelper.deleteUserFromAbuseRegistry;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.BoHelper.getClientsInvestigationsDb;
import static helpers.database.CleanTableHelper.cleanPaymentGateData;
import static helpers.database.DbHelper.*;
import static helpers.database.DbHelper.deleteObjectFromDb;
import static helpers.database.DbName.POSTGRES;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static utils.Constants.*;
import static utils.Utils.getRandomIntPositive;

import business_objects.db.backoffice_db.Investigation;
import business_objects.db.backoffice_db.alert.Alert;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObject;
import business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObject;
import business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObject;
import business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObjectFactory;
import business_objects.db.payment_gate.payment_details.PaymentDetailsObject;
import business_objects.db.payment_gate.payment_details.PaymentDetailsObjectFactory;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import business_objects.db.payment_gate.payment_events.PaymentEventsObjectFactory;
import business_objects.kafka.alerts.PaymentAlertMessage;
import business_objects.kafka.alerts.RuleAlert;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.data.enums.AlertResolution;
import helpers.data.enums.AlertType;
import helpers.database.DbName;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

@Feature("BMS-2166 Process deposit alerts")
public class PaymentDepositAlertsTest extends TestBaseWeb {
    private static final double WD_USD = 20_000.12;
    private static final double WD_EUR = 14_654.76;
    private static final String PAYMENT_METHOD_CODE = "Brazil Bank Transfer";
    private static final String ALERT_WHERE = "client_ucid = '%s' and type='%s' ORDER BY happened_at DESC";

    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);

    private static MtAccountObject mtAccount1;
    private static MtAccountObject mtAccount2;
    private static MtMt4TradesCoercedObject trade1;
    private static MtMt4TradesCoercedObject trade2;
    private static PaymentEventsObject event = PaymentEventsObjectFactory.generatePaymentEventsObject(client);
    private static MtMt4TradesCoercedObject tradeWithdrawal;

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String buildPayload() throws Exception {
        Map<String, Object> payload = Map.ofEntries(
                Map.entry("id", "361564a9-2404-4dd6-aafb-31ce12af19e8"),
                Map.entry("iban", ""),
                Map.entry("type", "withdrawal"),
                Map.entry("brand", "vantage"),
                Map.entry("bankName", "testvv"),
                Map.entry("clientId", "99887766"),
                Map.entry("platform", "WEB"),
                Map.entry("checkName", "Little_Amount"),
                Map.entry("eventDate", "2025-09-18T06:15:50+03:00"),
                Map.entry("regulator", "VFSC2"),
                Map.entry("mt4Account", 1_398_842_009),
                Map.entry("accountType", "MT4"),
                Map.entry("withdrawalId", "41000779"),
                Map.entry("schemaVersion", "1.0"),
                Map.entry("merchantOrderId", "VU856068920250918061547"),
                Map.entry("paymentTypeName", "Bank Transfers"),
                Map.entry("withdrawalAmount", WD_EUR),
                Map.entry("paymentMethodCode", PAYMENT_METHOD_CODE),
                Map.entry("paymentChannelCode", "642"),
                Map.entry("paymentChannelName", "Brazil-CPS"),
                Map.entry("withdrawalCurrency", "EUR"),
                Map.entry("withdrawalAmountUSD", WD_USD),
                Map.entry("bankAccountHolderName", "testvv"),
                Map.entry("withdrawalApplicationTime", "2025-09-18T06:15:46.379Z"));

        return objectMapper.writeValueAsString(payload);
    }

    @BeforeAll
    static void setup() {
        objectMapper.findAndRegisterModules();
        CrmTbAccountObject account1 = generateCrmTbAccountDataForUi(client);
        account1.currency = USD.getIsoCode();
        CrmTbAccountObject account2 = generateCrmTbAccountDataForUi(client);
        account2.account = getRandomIntPositive();
        account2.currency = USD.getIsoCode();
        mtAccount1 = generateMtAccountByCrmTbAccount(account1);
        mtAccount2 = generateMtAccountByCrmTbAccount(account2);

        String comment = "comment";
        trade1 = generateMt4TradesCoercedAccountProfitComment(account1, 500.12 + 10_000d, comment);
        trade2 = generateMt4TradesCoercedAccountProfitComment(account2, 1000.23, comment);
        tradeWithdrawal = generateMt4TradesCoercedAccountProfitComment(account1, -10_000d, "withdraw");

        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertObjectsToDb(CRM_TB_ACCOUNT_TABLE_NAME, List.of(account1, account2));
        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccount1, mtAccount2));
        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade1, trade2, tradeWithdrawal));
        MtMt5PositionsObject position = generateMtMt5PositionsObject(client);
        position.setAccount(mtAccount2.account);
        position.setServerId(mtAccount2.sourceIdSt);
        insertObjectToDb(MT5_POSITIONS_TABLE_NAME, position);
    }

    @BeforeEach
    void sendAlert() throws Exception {
        PaymentAlertMessage alertPayment = generatePaymentAlertByUcid(crmTbUser.ucid);
        kafka.produceMessage(
                alertPayment.getId().toString(), objectMapper.writeValueAsString(alertPayment), KAFKA_TOPIC_ALERTS);
        RuleAlert alert = generateRuleAlertByUcid(client.getUcid());
        alert.rule.attributes.account = mtAccount1.account.toString();
        kafka.produceMessage(alert.alertId, objectMapper.writeValueAsString(alert), KAFKA_TOPIC_ALERTS);
        // pgs
        event.setPaymentId(UUID.fromString(alertPayment.getPaymentEventId()));
        insertObjectToDb(POSTGRES, PAYMENT_EVENT_TABLE_NAME, event);

        PaymentDetailsObject details = PaymentDetailsObjectFactory.generatePaymentDetailsObject(client);
        details.setType("withdrawal");
        String payload = buildPayload();
        details.setPayload(payload);
        details.setPaymentId(event.getPaymentId());
        insertObjectToDb(POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, details);

        PaymentDecisionsObject decision = PaymentDecisionsObjectFactory.generatePaymentDecisionObject(event);
        decision.setDecisionType("payment");
        decision.setDecisionCode(3);
        insertObjectToDb(POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, decision);
    }

    @AfterEach
    void teardownInPGS() throws Exception {
        cleanPaymentGateData(
                client.getUcid(), client.getUserId(), event.getPaymentId().toString());
        deleteUserFromAbuseRegistry(client.getUcid());
        closeAlert(crmTbUser.ucid);
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteObjectFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteObjectFromDb(MT4_TRADES_COERCED_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteObjectFromDb(MT5_POSITIONS_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteUserFromAbuseRegistry(client.getUcid());
        closeAlert(crmTbUser.ucid);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1774")
    @DisplayName("Process deposit alerts - no withdrawal tab displayed")
    void noWithdrawalTab_whenNoDecisionsPresent() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToMain();
        investigationPage.waitForPageToLoad();
        investigationPage.clickSelectPaymentInvestigationType();
        investigationPage.waitForPageToLoad();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openResolveSuspicious();
        boolean isWithdrawalTabDisplayed = resolvePage.isWithdrawalListVisible();
        assertThat("Verify that withdrawal list is not empty", isWithdrawalTabDisplayed, is(false));

        resolvePage.addFraud(MARKET_MANIPULATION, CONFIRMED);
        resolvePage.resolveNoActions("test comment");

        // check investigation in db
        List<Investigation> clientsInvestigationsDb = getClientsInvestigationsDb(client.getUcid(), AlertType.PAYMENT);
        assertThat("Verify that investigation is completed", clientsInvestigationsDb.size(), is(1));
        assertThat(
                "Verify that investigation is completed",
                clientsInvestigationsDb.getFirst().getStatus(),
                is("COMPLETED"));

        // check trading investigation is not closed
        List<Investigation> clientsInvestigationsTrading =
                getClientsInvestigationsDb(client.getUcid(), AlertType.TRADING);
        assertThat("Verify that trading investigation is completed", clientsInvestigationsTrading.size(), is(1));
        assertThat(
                "Verify that trading investigation is not completed",
                clientsInvestigationsTrading.getFirst().getStatus(),
                not("COMPLETED"));

        // PGS decision check in db
        List<PaymentDecisionsObject> decisions = getObjectsFromDB(
                DbName.POSTGRES,
                PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE,
                String.format("payment_id = '%s'", event.getPaymentId().toString()),
                PaymentDecisionsObject.class);
        assertThat("Verify that decision is saved in db", decisions.size(), is(1));
        PaymentDecisionsObject actualDecision = decisions.getFirst();
        assertThat("Verify that decision is \"not applicable\"", actualDecision.getDecisionCode(), is(3));

        // Alert resolution
        Alert dbAlert = getObjectsFromDB(
                        DbName.POSTGRES,
                        BO_ALERT_TABLE_NAME,
                        String.format(ALERT_WHERE, client.getUcid(), AlertType.PAYMENT),
                        Alert.class)
                .getFirst();
        assertThat(
                "Verify alert_resolution is CONFIRMED",
                dbAlert.getAlertResolution(),
                is(AlertResolution.CONFIRMED.getDisplayName()));
    }
}
