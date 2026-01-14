package tests.vindex_backoffice_ui_tests.investigationTool;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObjectFactory.generateRiskPaymentDecisionObject;
import static business_objects.db.payment_gate.payment_details.PaymentDetailsObjectFactory.generatePaymentDetailsObject;
import static business_objects.db.payment_gate.payment_events.PaymentEventsObjectFactory.generatePaymentEventsObject;
import static business_objects.kafka.alerts.RuleAlertFactory.generatePgsWithdrawalNotificationAlert;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.CleanTableHelper.cleanPaymentGateData;
import static helpers.database.DbHelper.*;
import static helpers.database.DbHelper.insertObjectsToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.*;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObject;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import business_objects.kafka.alerts.RuleAlert;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.database.DbName;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
public class TradingWithdrawalsTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    private static final CrmTbAccountObject account = generateCrmTbAccountDataForUi(client);
    private static final MtAccountObject mtAccount = generateMtAccountByCrmTbAccount(account);
    private static final List<String> withdrawalData1 = new ArrayList<>();
    private static PaymentEventsObject paymentEventsObject1 = setupDataPGS();
    private static final RuleAlert withdrawalAlert1 =
            generatePgsWithdrawalNotificationAlert(client, paymentEventsObject1.getPaymentId());
    private static final DecimalFormat formatter = new DecimalFormat("#,###.##");

    @BeforeAll
    static void setup() throws IOException {
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertCrmAccountsToDb(account);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, mtAccount);

        // data from pgs event payload
        withdrawalData1.add("2025-05-20");
        withdrawalData1.add("17:30:00");
        withdrawalData1.add("Credit card");
        withdrawalData1.add("CREDIT_CARD");
        withdrawalData1.add("1,500 USD");
        withdrawalData1.add("1,500 USD");
        withdrawalData1.add("3031915");
        withdrawalData1.add("MT5");
        withdrawalData1.add("Big_Amount");
        withdrawalData1.add("Risk Audit");

        List<RuleAlert> withdrawalAlertList = List.of(withdrawalAlert1);
        for (RuleAlert withdrawalAlert : withdrawalAlertList) {
            kafka.produceMessage(
                    withdrawalAlert.alertId, objectMapper.writeValueAsString(withdrawalAlert), KAFKA_TOPIC_ALERTS);
        }
        formatter.setMinimumFractionDigits(0);
        formatter.setMaximumFractionDigits(2);
        formatter.setRoundingMode(RoundingMode.HALF_DOWN);
    }

    static PaymentEventsObject setupDataPGS() {
        paymentEventsObject1 = generatePaymentEventsObject(client);
        var paymentDetailsObject1 = generatePaymentDetailsObject(paymentEventsObject1, client);
        paymentDetailsObject1.setPayload(
                "{\"id\": \"123e4567-e89b-12d3-a456-426614174000\", \"ip\": \"121.233.122.82\", \"card\": {\"card3ds\": 0, \"expYear\": \"2029\", \"expMonth\": \"4\", \"fullName\": \"sheryar shah\", \"lastFour\": \"1225\", \"binNumber\": \"654321\"}, \"cost\": 0.56, \"type\": \"withdrawal\", \"brand\": \"vantage\", \"status\": \"Success\", \"clientId\": 112341, \"platform\": \"WEB\", \"statusId\": 1, \"checkName\": \"WR_Blacklist\", \"eventDate\": \"2025-05-20T14:30:00Z\", \"regulator\": \"CIMA\", \"statusKYC\": \"Confirmed\", \"mt4Account\": 3031915, \"accountType\": \"MT5\", \"withdrawalId\": 2373634, \"schemaVersion\": \"1.0\", \"merchantOrderId\": \"VTSG1115142220250202132259\", \"paymentTypeCode\": 2, \"paymentTypeName\": \"Credit card\", \"withdrawalAmount\": 1500.00, \"paymentMethodCode\": \"CREDIT_CARD\", \"paymentChannelCode\": 1, \"paymentChannelName\": \"Credit card\", \"withdrawalCurrency\": \"USD\", \"withdrawalAmountUSD\": 1500.00, \"withdrawalApplicationTime\": \"2025-07-15 07:38:05\"}");
        var paymentDecisionsObject1 = generateRiskPaymentDecisionObject(paymentEventsObject1);

        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, List.of(paymentEventsObject1));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, List.of(paymentDetailsObject1));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, List.of(paymentDecisionsObject1));

        return paymentEventsObject1;
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteObjectFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteObjectFromDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, String.format("ucid = '%s'", client.getUcid()));
        closeAlert(crmTbUser.ucid);
        cleanPaymentGateData(
                client.getUcid(),
                client.getUserId(),
                paymentEventsObject1.getPaymentId().toString());
    }

    @Test
    @Order(1)
    @AllureId("1687")
    @DisplayName("Trading Withdrawals. Payments tab. Verify withdrawals count")
    public void checkWdListOnPaymentsPage() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        paymentsPage.navigatePaymentsTab(client.getUcid());
        paymentsPage.clickPaymentsTabButton();
        paymentsPage.clickWithdrawalsTabButton();
        assertThat(
                "Verify table headers",
                paymentsPage.getTableHeaders(),
                containsInAnyOrder("DATE", "TYPE", "AMOUNT", "ACCOUNT", "CHECK", "STATUS"));
        assertThat("Verify rows count", paymentsPage.getRowsCount(), is(1));
        assertThat(
                "Verify data in table",
                paymentsPage.getAllRowsData(),
                hasItems(withdrawalData1.toArray(String[]::new)));
    }

    @Test
    @Order(2)
    @AllureId("1688")
    @DisplayName("Trading Withdrawals. Resolve tab. Verify withdrawals count")
    public void checkWdListOnResolvePage() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        var wdList = resolvePage.getWithdrawalList();
        assertThat(wdList.size(), is(1));
        var wdContent = wdList.getFirst();
        assertThat(wdContent.contains("1,500") && wdContent.contains("CREDIT_CARD"), is(true));
    }

    @Test
    @Order(3)
    @AllureId("1688")
    @DisplayName("Trading Withdrawals. Resolve tab. Resolve client test")
    public void resolveWdRequests() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        resolvePage.openResolveSuspicious();
        resolvePage.resolveWithdrawalsAllApprove("COMMENT");

        // PGS decision check in db
        List<PaymentDecisionsObject> decisions = getObjectsFromDB(
                DbName.POSTGRES,
                PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE,
                String.format(
                        "payment_id = '%s'", paymentEventsObject1.getPaymentId().toString()),
                PaymentDecisionsObject.class);
        assertThat("Verify that decision is saved in db", decisions.size(), is(1));
        PaymentDecisionsObject actualDecision = decisions.getFirst();
        assertThat("Verify that decision is approved", actualDecision.getDecisionCode(), is(1));
        assertThat("Verify that actor is Vindex BO", actualDecision.getActor(), is("Vindex BO"));
    }
}
