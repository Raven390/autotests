package tests.vindex_backoffice_ui_tests.investigationTool;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.kafka.alerts.RuleAlertFactory.generateTradingAlertWithDecision;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.DateTimeFormat.*;
import static helpers.database.BoHelper.deleteUserBO;
import static helpers.database.DbHelper.*;
import static helpers.database.DbName.POSTGRES;
import static helpers.database.PaymentGateHelper.generateTradingWithdrawalPaymentGateData;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.*;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObject;
import business_objects.kafka.alerts.PaymentAlertMessageV2;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.data.PaymentGateData;
import helpers.database.DbName;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TradingWithdrawalsTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    private static final CrmTbAccountObject account = generateCrmTbAccountDataForUi(client);
    private static final MtAccountObject mtAccount = generateMtAccountByCrmTbAccount(account);
    private static final List<String> withdrawalData1 = new ArrayList<>();
    private static final List<String> withdrawalData2 = new ArrayList<>();
    private static final List<String> withdrawalData3 = new ArrayList<>();
    private static PaymentAlertMessageV2 withdrawalAlert1;
    private static PaymentAlertMessageV2 withdrawalAlert2;
    private static PaymentAlertMessageV2 withdrawalAlert3;
    private static final DecimalFormat formatter = new DecimalFormat("#,###.##");
    private static final String PAYMENT_TYPE = "Bank Transfers";
    private static final String PAYMENT_CHANNEL = "Brazil-CPS";
    private static final String PAYMENT_METHOD = "Brazil Bank Transfer";
    private static final String TRIGGER = "withdrawal";
    private static final Double AMOUNT3 = 60_783.76;
    private static final Double AMOUNT3_USD = 80_000d;
    private static final String CURRENCY_USD = "USD";
    private static PaymentGateData pgsData1;
    private static PaymentGateData pgsData2;
    private static PaymentGateData pgsData3;

    @BeforeAll
    static void setup() throws IOException {
        objectMapper.findAndRegisterModules();
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertCrmAccountsToDb(account);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, mtAccount);

        String riskAuditStatus = "Risk audit";
        Double amount1 = 123.45;
        Double amount1Usd = 111.67;
        String currency1 = "EUR";
        String checkName1 = "Big_Amount";
        String dateDbFormat1 = getCurrentTimestampDbFormat();
        String eventDate1 = dateDbFormat1.replace(" ", "T") + "+02:00";
        pgsData1 = generateTradingWithdrawalPaymentGateData(
                client,
                amount1,
                amount1Usd,
                currency1,
                checkName1,
                eventDate1,
                PAYMENT_CHANNEL,
                PAYMENT_METHOD,
                account.account,
                account.platform);
        withdrawalData1.add(transformDate(dateDbFormat1, DATE_AND_TIME, DATE));
        withdrawalData1.add(transformDate(dateDbFormat1, DATE_AND_TIME, TIME));
        withdrawalData1.add(PAYMENT_TYPE);
        withdrawalData1.add(PAYMENT_METHOD);
        withdrawalData1.add(String.format("%s %s", formatter.format(amount1Usd), CURRENCY_USD));
        withdrawalData1.add(String.format("%s %s", formatter.format(amount1), currency1));
        withdrawalData1.add(account.account.toString());
        withdrawalData1.add(account.platform);
        withdrawalData1.add(checkName1);
        withdrawalData1.add(riskAuditStatus);

        Double amount2 = 1001.13;
        Double amount2Usd = 1001.13;
        String currency2 = "USD";
        String checkName2 = "WR_Blacklist";
        String dateDbFormat2 = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 1, 0, 0);
        String eventDate2 = dateDbFormat2.replace(" ", "T") + "+02:00";
        pgsData2 = generateTradingWithdrawalPaymentGateData(
                client,
                amount2,
                amount2Usd,
                currency2,
                checkName2,
                eventDate2,
                PAYMENT_CHANNEL,
                PAYMENT_METHOD,
                account.account,
                account.platform);
        withdrawalData2.add(transformDate(dateDbFormat2, DATE_AND_TIME, DATE));
        withdrawalData2.add(transformDate(dateDbFormat2, DATE_AND_TIME, TIME));
        withdrawalData2.add(PAYMENT_TYPE);
        withdrawalData2.add(PAYMENT_METHOD);
        withdrawalData2.add(String.format("%s %s", formatter.format(amount2Usd), CURRENCY_USD));
        withdrawalData2.add(String.format("%s %s", formatter.format(amount2), currency2));
        withdrawalData2.add(account.account.toString());
        withdrawalData2.add(account.platform);
        withdrawalData2.add(checkName2);
        withdrawalData2.add(riskAuditStatus);

        String currency3 = "GBP";
        String checkName3 = "High_Risk";
        String dateDbFormat3 = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 1, 0, 0, 0);
        String eventDate3 = dateDbFormat3.replace(" ", "T") + "+02:00";
        pgsData3 = generateTradingWithdrawalPaymentGateData(
                client,
                AMOUNT3,
                AMOUNT3_USD,
                currency3,
                checkName3,
                eventDate3,
                PAYMENT_CHANNEL,
                PAYMENT_METHOD,
                account.account,
                account.platform);
        withdrawalData3.add(transformDate(dateDbFormat3, DATE_AND_TIME, DATE));
        withdrawalData3.add(transformDate(dateDbFormat3, DATE_AND_TIME, TIME));
        withdrawalData3.add(PAYMENT_TYPE);
        withdrawalData3.add(PAYMENT_METHOD);
        withdrawalData3.add(String.format("%s %s", formatter.format(AMOUNT3_USD), CURRENCY_USD));
        withdrawalData3.add(String.format("%s %s", formatter.format(AMOUNT3), currency3));
        withdrawalData3.add(account.account.toString());
        withdrawalData3.add(account.platform);
        withdrawalData3.add(checkName3);
        withdrawalData3.add(riskAuditStatus);

        insertObjectsToDb(
                POSTGRES,
                PAYMENT_EVENT_TABLE_NAME,
                List.of(pgsData1.getPaymentEvent(), pgsData2.getPaymentEvent(), pgsData3.getPaymentEvent()));
        insertObjectsToDb(
                POSTGRES,
                PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE,
                List.of(pgsData1.getPaymentDetails(), pgsData2.getPaymentDetails(), pgsData3.getPaymentDetails()));
        insertObjectsToDb(
                POSTGRES,
                PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE,
                List.of(
                        pgsData1.getPaymentDecisions(),
                        pgsData2.getPaymentDecisions(),
                        pgsData3.getPaymentDecisions()));

        withdrawalAlert1 = generateTradingAlertWithDecision(
                client.getUcid(),
                TRIGGER,
                pgsData1.getPaymentEvent().getPaymentId().toString());
        withdrawalAlert2 = generateTradingAlertWithDecision(
                client.getUcid(),
                TRIGGER,
                pgsData2.getPaymentEvent().getPaymentId().toString());
        withdrawalAlert3 = generateTradingAlertWithDecision(
                client.getUcid(),
                TRIGGER,
                pgsData3.getPaymentEvent().getPaymentId().toString());
        List<PaymentAlertMessageV2> withdrawalAlertList = List.of(withdrawalAlert1, withdrawalAlert2, withdrawalAlert3);
        for (PaymentAlertMessageV2 withdrawalAlert : withdrawalAlertList) {
            kafka.produceMessage(
                    withdrawalAlert.getId().toString(),
                    objectMapper.writeValueAsString(withdrawalAlert),
                    KAFKA_TOPIC_ALERTS);
        }
        formatter.setMinimumFractionDigits(0);
        formatter.setMaximumFractionDigits(2);
        formatter.setRoundingMode(RoundingMode.HALF_DOWN);
    }

    @AfterAll
    static void teardown() {
        deleteObjectFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteObjectFromDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, String.format("ucid = '%s'", client.getUcid()));
        deleteUserBO(crmTbUser.ucid);
    }

    @Test
    @Order(1)
    @AllureId("1114")
    @DisplayName("Payments Withdrawals. Verify withdrawals count")
    void paymentsWithdrawalsTest1() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.waitForPageToLoad();
        paymentsPage.clickPaymentsTabButton();
        paymentsPage.clickWithdrawalsTabButton();
        assertThat(
                "Verify amount of withdrawals",
                paymentsPage.getWithdrawalsTabButtonText(),
                is("Withdrawal requests 3"));
    }

    @Test
    @Order(2)
    @AllureId("1115")
    @DisplayName("Payments Withdrawals. Verify filter options")
    void paymentsWithdrawalsTest2() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.waitForPageToLoad();
        paymentsPage.clickPaymentsTabButton();
        paymentsPage.clickWithdrawalsTabButton();
        assertThat(
                "Verify create time filter options",
                paymentsPage.getCreateTimeFilterOptions(),
                containsInAnyOrder(
                        "Last 8 hours",
                        "Last 24 hours",
                        "Last 7 days",
                        "Last 30 days",
                        "Last 60 days",
                        "Custom dates"));
        assertThat(
                "Verify amount filter options",
                paymentsPage.getAmountFilterOptions(),
                containsInAnyOrder(
                        "0-1,000", "1,000-5,000", "5,000-10,000", "10,000-50,000", ">50,000", "Custom amount"));
    }

    @Test
    @Order(3)
    @AllureId("1116")
    @DisplayName("Payments Withdrawals. Verify table data")
    void paymentsWithdrawalsTest3() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.waitForPageToLoad();
        paymentsPage.clickPaymentsTabButton();
        paymentsPage.clickWithdrawalsTabButton();
        assertThat(
                "Verify table headers",
                paymentsPage.getTableHeaders(),
                containsInAnyOrder("DATE", "TYPE", "AMOUNT", "ACCOUNT", "CHECK", "STATUS"));
        assertThat("Verify rows count", paymentsPage.getRowsCount(), is(3));
        assertThat(
                "Verify data in table",
                paymentsPage.getAllRowsData(),
                hasItems(Stream.of(withdrawalData1, withdrawalData2, withdrawalData3)
                        .flatMap(List::stream)
                        .toArray(String[]::new)));
    }

    @Test
    @Order(4)
    @AllureId("1117")
    @DisplayName("Payments Withdrawals. Verify sorting")
    void paymentsWithdrawalsTest4() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.waitForPageToLoad();
        paymentsPage.clickPaymentsTabButton();
        paymentsPage.clickWithdrawalsTabButton();
        assertThat(
                "Verify sort by amount tooltip",
                paymentsPage.getAmountColumnTooltip(),
                is("Sort by amount:Descending"));
        paymentsPage.clickAmountColumn();
        assertThat("Verify sort by amount tooltip", paymentsPage.getAmountColumnTooltip(), is("Sorted:Descending"));
        paymentsPage.clickAmountColumn();
        assertThat("Verify sort by amount tooltip", paymentsPage.getAmountColumnTooltip(), is("Sorted:Ascending"));
        assertThat(
                "Verify sort by date tooltip",
                paymentsPage.getDateColumnTooltip(),
                is("Sort by request date:Newest → Oldest"));
        paymentsPage.clickDateColumn();
        assertThat("Verify sort by date tooltip", paymentsPage.getDateColumnTooltip(), is("Sorted:Newest → Oldest"));
        paymentsPage.clickDateColumn();
        assertThat("Verify sort by date tooltip", paymentsPage.getDateColumnTooltip(), is("Sorted:Oldest → Newest"));
    }

    @Test
    @Order(5)
    @AllureId("1118")
    @DisplayName("Payments Withdrawals. Verify filtration by date")
    void paymentsWithdrawalsTest5() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.waitForPageToLoad();
        paymentsPage.clickPaymentsTabButton();
        paymentsPage.clickWithdrawalsTabButton();
        paymentsPage.selectCreateTimeFilterOption("Last 7 days");
        assertThat("Verify rows count", paymentsPage.getRowsCount(), is(2));
        assertThat(
                "Verify data in table",
                paymentsPage.getAllRowsData(),
                hasItems(Stream.of(withdrawalData1, withdrawalData2)
                        .flatMap(List::stream)
                        .toArray(String[]::new)));
    }

    @Test
    @Order(6)
    @AllureId("1120")
    @DisplayName("Payments Withdrawals. Verify filtration by amount")
    void paymentsWithdrawalsTest7() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.waitForPageToLoad();
        paymentsPage.clickPaymentsTabButton();
        paymentsPage.clickWithdrawalsTabButton();
        paymentsPage.selectAmountFilterOption(">50,000");
        assertThat("Verify rows count", paymentsPage.getRowsCount(), is(1));
        assertThat(
                "Verify data in table",
                paymentsPage.getAllRowsData(),
                hasItems(withdrawalData3.toArray(String[]::new)));
    }

    @Test
    @Order(7)
    @AllureId("1121")
    @DisplayName("Payments Withdrawals. Process withdrawals")
    void paymentsWithdrawalsTest8() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.waitForPageToLoad();
        paymentsPage.clickPaymentsTabButton();
        paymentsPage.clickWithdrawalsTabButton();
        paymentsPage.clickWithdrawalByPaymentId(
                pgsData1.getPaymentEvent().getPaymentId().toString());
        paymentsPage.clickWithdrawalByPaymentId(
                pgsData2.getPaymentEvent().getPaymentId().toString());
        assertThat("Verify submit panel counter", paymentsPage.getSubmitPanelCounterText(), is("2 selected"));
        String comment = "Autotest process withdrawals comment";
        paymentsPage.fillSubmitPanelInput(comment);
        paymentsPage.clickRejectAllButton();
        List<PaymentDecisionsObject> decisions = getObjectsFromDB(
                DbName.POSTGRES,
                PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE,
                String.format(
                        "payment_id in ('%s', '%s')",
                        pgsData1.getPaymentEvent().getPaymentId().toString(),
                        pgsData2.getPaymentEvent().getPaymentId().toString()),
                PaymentDecisionsObject.class);
        assertThat(
                "Verify that decision is present in PGS",
                decisions.stream().map(PaymentDecisionsObject::getDecisionCode).toList(),
                everyItem(is(2)));
    }

    @Test
    @Order(8)
    @AllureId("1688")
    @DisplayName("Trading Withdrawals. Resolve tab. Verify withdrawals count")
    void checkWdListOnResolvePage() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        var wdList = resolvePage.getWithdrawalList();
        assertThat(wdList.size(), is(1));
        var wdContent = wdList.getFirst();
        assertThat(wdContent, containsString(formatter.format(AMOUNT3)));
        assertThat(wdContent, containsString(PAYMENT_METHOD));
    }

    @Test
    @Order(9)
    @AllureId("1688")
    @DisplayName("Trading Withdrawals. Resolve tab. Resolve client test")
    void resolveWdRequests() throws Exception {
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
                        "payment_id = '%s'",
                        pgsData3.getPaymentEvent().getPaymentId().toString()),
                PaymentDecisionsObject.class);
        assertThat("Verify that decision is saved in db", decisions.size(), is(1));
        PaymentDecisionsObject actualDecision = decisions.getFirst();
        assertThat("Verify that decision is approved", actualDecision.getDecisionCode(), is(1));
        assertThat("Verify that actor is Vindex BO", actualDecision.getActor(), is("Vindex BO"));
    }
}
