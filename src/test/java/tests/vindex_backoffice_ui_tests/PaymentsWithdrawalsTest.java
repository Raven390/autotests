package tests.vindex_backoffice_ui_tests;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalObject;
import business_objects.db.clickhouse.mtAccount.MtAccountObject;
import business_objects.kafka.alerts.RuleAlert;
import business_objects.kafka.restriction_events.WithdrawalApprovals;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.io.IOException;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalObjectFactory.generateWithdrawalByClient;
import static business_objects.db.clickhouse.mtAccount.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.kafka.alerts.RuleAlertFactory.generateWithdrawalNotificationAlert;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.DateTimeFormat.*;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.getCurrentTimestampMinusOffsetFormatted;
import static utils.Utils.transformDate;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PaymentsWithdrawalsTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    private static final CrmTbAccountObject account = generateCrmTbAccountDataForUi(client);
    private static final MtAccountObject mtAccount = generateMtAccountByCrmTbAccount(account);
    private static final CrmTbWithdrawalObject withdrawal1 = generateWithdrawalByClient(client);
    private static final CrmTbWithdrawalObject withdrawal2 = generateWithdrawalByClient(client);
    private static final CrmTbWithdrawalObject withdrawal3 = generateWithdrawalByClient(client);
    private static final List<String> withdrawalData1 = new ArrayList<>();
    private static final List<String> withdrawalData2 = new ArrayList<>();
    private static final List<String> withdrawalData3 = new ArrayList<>();
    private static final DecimalFormat formatter = new DecimalFormat("#,###.##");

    @BeforeAll
    static void setup() throws IOException {
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, account);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, mtAccount);
        withdrawal2.createTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 1, 0, 0);
        withdrawal3.createTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 1, 0, 0, 0);
        withdrawal1.paymentType = "Paypal";
        withdrawal2.paymentType = "Cryptocurrency-USDT";
        withdrawal3.paymentType = "otherPaymentType";
        withdrawal1.amount = 123.45;
        withdrawal1.currency = "EUR";
        withdrawal1.amountUsd = 130.12;
        withdrawal2.amount = 1001.13;
        withdrawal2.currency = "USD";
        withdrawal2.amountUsd = 1001.13;
        withdrawal3.amount = 60_783.76;
        withdrawal3.currency = "GBP";
        withdrawal3.amountUsd = 55_678.98;
        RuleAlert withdrawalAlert1 = generateWithdrawalNotificationAlert(withdrawal1);
        RuleAlert withdrawalAlert2 = generateWithdrawalNotificationAlert(withdrawal2);
        RuleAlert withdrawalAlert3 = generateWithdrawalNotificationAlert(withdrawal3);
        withdrawalAlert1.rule.attributes.check = "Big_Amount";
        withdrawalAlert2.rule.attributes.check = "WR_Blacklist";
        withdrawalAlert3.rule.attributes.check = "High_Risk";
        withdrawalData1.add(transformDate(withdrawal1.createTime, DATE_AND_TIME, DATE));
        withdrawalData1.add(transformDate(withdrawal1.createTime, DATE_AND_TIME, TIME));
        withdrawalData1.add(withdrawal1.paymentType);
        withdrawalData1.add("Payment Services");
        withdrawalData1.add(String.format("%s %s", formatter.format(withdrawal1.amount), withdrawal1.currency));
        withdrawalData1.add(String.format("%s %s", formatter.format(withdrawal1.amountUsd), "USD"));
        withdrawalData1.add(withdrawal1.account.toString());
        withdrawalData1.add(account.platform);
        withdrawalData1.add(withdrawalAlert1.rule.attributes.check);
        withdrawalData1.add(withdrawal1.status);
        withdrawalData2.add(transformDate(withdrawal2.createTime, DATE_AND_TIME, DATE));
        withdrawalData2.add(transformDate(withdrawal2.createTime, DATE_AND_TIME, TIME));
        withdrawalData2.add(withdrawal2.paymentType);
        withdrawalData2.add("Crypto");
        withdrawalData2.add(String.format("%s %s", formatter.format(withdrawal2.amount), withdrawal2.currency));
        withdrawalData2.add(String.format("%s %s", formatter.format(withdrawal2.amountUsd), "USD"));
        withdrawalData2.add(withdrawal2.account.toString());
        withdrawalData2.add(account.platform);
        withdrawalData2.add(withdrawalAlert2.rule.attributes.check);
        withdrawalData2.add(withdrawal2.status);
        withdrawalData3.add(transformDate(withdrawal3.createTime, DATE_AND_TIME, DATE));
        withdrawalData3.add(transformDate(withdrawal3.createTime, DATE_AND_TIME, TIME));
        withdrawalData3.add(withdrawal3.paymentType);
        withdrawalData3.add("Other");
        withdrawalData3.add(String.format("%s %s", formatter.format(withdrawal3.amount), withdrawal3.currency));
        withdrawalData3.add(String.format("%s %s", formatter.format(withdrawal3.amountUsd), "USD"));
        withdrawalData3.add(withdrawal3.account.toString());
        withdrawalData3.add(account.platform);
        withdrawalData3.add(withdrawalAlert3.rule.attributes.check);
        withdrawalData3.add(withdrawal3.status);
        List<RuleAlert> withdrawalAlertList = List.of(withdrawalAlert1, withdrawalAlert2, withdrawalAlert3);
        for (RuleAlert withdrawalAlert : withdrawalAlertList) {
            kafka.produceMessage(withdrawalAlert.alertId, objectMapper.writeValueAsString(withdrawalAlert), KAFKA_TOPIC_ALERTS);
        }
        formatter.setMinimumFractionDigits(0);
        formatter.setMaximumFractionDigits(2);
        formatter.setRoundingMode(RoundingMode.HALF_DOWN);
    }

    @Test
    @Order(1)
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1114")
    @DisplayName("Payments Withdrawals. Verify withdrawals count")
    public void paymentsWithdrawalsTest1() {
        insertObjectsToDb(CRM_WITHDRAWAL_TABLE_NAME, List.of(withdrawal1, withdrawal2, withdrawal3));
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.waitForPageToLoad();
        paymentsPage.clickPaymentsTabButton();
        paymentsPage.clickWithdrawalsTabButton();
        assertThat("Verify amount of withdrawals", paymentsPage.getWithdrawalsTabButtonText(), is("Withdrawal requests 3"));
    }

    @Test
    @Order(2)
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1115")
    @DisplayName("Payments Withdrawals. Verify filter options")
    public void paymentsWithdrawalsTest2() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.waitForPageToLoad();
        paymentsPage.clickPaymentsTabButton();
        paymentsPage.clickWithdrawalsTabButton();
        assertThat("Verify create time filter options", paymentsPage.getCreateTimeFilterOptions(), containsInAnyOrder("Last 8 hours", "Last 24 hours", "Last 7 days", "Last 30 days", "Last 60 days", "Custom dates"));
        assertThat("Verify type filter options", paymentsPage.getTypeFilterOptions(), containsInAnyOrder("P2P", "Payment Services", "Crypto", "Cards", "Bank Transfers", "Other"));
        assertThat("Verify amount filter options", paymentsPage.getAmountFilterOptions(), containsInAnyOrder("0-1,000", "1,000-5,000", "5,000-10,000", "10,000-50,000", ">50,000", "Custom amount"));
    }

    @Test
    @Order(3)
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1116")
    @DisplayName("Payments Withdrawals. Verify table data")
    public void paymentsWithdrawalsTest3() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.waitForPageToLoad();
        paymentsPage.clickPaymentsTabButton();
        paymentsPage.clickWithdrawalsTabButton();
        assertThat("Verify table headers", paymentsPage.getTableHeaders(), containsInAnyOrder("DATE", "TYPE", "AMOUNT", "ACCOUNT", "CHECK", "STATUS"));
        assertThat("Verify rows count", paymentsPage.getRowsCount(), is(3));
        assertThat("Verify data in table", paymentsPage.getAllRowsData(), containsInAnyOrder(Stream.of(withdrawalData1, withdrawalData2, withdrawalData3).flatMap(List::stream).toList().toArray()));
    }

    @Test
    @Order(4)
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1117")
    @DisplayName("Payments Withdrawals. Verify sorting")
    public void paymentsWithdrawalsTest4() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.waitForPageToLoad();
        paymentsPage.clickPaymentsTabButton();
        paymentsPage.clickWithdrawalsTabButton();
        assertThat("Verify sort by amount tooltip", paymentsPage.getAmountColumnTooltip(), is("Sort by amount:Descending"));
        paymentsPage.clickAmountColumn();
        assertThat("Verify sort by amount tooltip", paymentsPage.getAmountColumnTooltip(), is("Change sorting to:Ascending"));
        paymentsPage.clickAmountColumn();
        assertThat("Verify sort by amount tooltip", paymentsPage.getAmountColumnTooltip(), is("Remove sorting"));
        assertThat("Verify sort by date tooltip", paymentsPage.getDateColumnTooltip(), is("Sort by request date:Newest → Oldest"));
        paymentsPage.clickDateColumn();
        assertThat("Verify sort by date tooltip", paymentsPage.getDateColumnTooltip(), is("Change sorting to:Oldest → Newest"));
        paymentsPage.clickDateColumn();
        assertThat("Verify sort by date tooltip", paymentsPage.getDateColumnTooltip(), is("Change sorting to:Newest → Oldest"));
    }

    @Test
    @Order(5)
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1118")
    @DisplayName("Payments Withdrawals. Verify filtration by date")
    public void paymentsWithdrawalsTest5() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.waitForPageToLoad();
        paymentsPage.clickPaymentsTabButton();
        paymentsPage.clickWithdrawalsTabButton();
        paymentsPage.selectCreateTimeFilterOption("Last 7 days");
        assertThat("Verify data in table", paymentsPage.getAllRowsData(), containsInAnyOrder(Stream.of(withdrawalData1, withdrawalData2).flatMap(List::stream).toList().toArray()));
    }

    @Test
    @Order(6)
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1119")
    @DisplayName("Payments Withdrawals. Verify filtration by type")
    public void paymentsWithdrawalsTest6() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.waitForPageToLoad();
        paymentsPage.clickPaymentsTabButton();
        paymentsPage.clickWithdrawalsTabButton();
        paymentsPage.selectTypeFilterOption("Crypto");
        assertThat("Verify data in table", paymentsPage.getAllRowsData(), containsInAnyOrder(withdrawalData2.toArray()));
    }

    @Test
    @Order(7)
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1120")
    @DisplayName("Payments Withdrawals. Verify filtration by amount")
    public void paymentsWithdrawalsTest7() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.waitForPageToLoad();
        paymentsPage.clickPaymentsTabButton();
        paymentsPage.clickWithdrawalsTabButton();
        paymentsPage.selectAmountFilterOption(">50,000");
        assertThat("Verify data in table", paymentsPage.getAllRowsData(), containsInAnyOrder(withdrawalData3.toArray()));
    }

    @Test
    @Order(8)
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1121")
    @DisplayName("Payments Withdrawals. Process withdrawals")
    public void paymentsWithdrawalsTest8() throws InterruptedException, JsonProcessingException {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.waitForPageToLoad();
        paymentsPage.clickPaymentsTabButton();
        paymentsPage.clickWithdrawalsTabButton();
        paymentsPage.selectAllWithdrawals();
        assertThat("Verify submit panel counter", paymentsPage.getSubmitPanelCounterText(), is("3 selected"));
        String comment = "Autotest process withdrawals comment";
        paymentsPage.fillSubmitPanelInput(comment);
        paymentsPage.clickRejectAllButton();
        Map<String, List<String>> messagesMap = kafka.consumeMessages(KAFKA_TOPIC_WITHDRAWAL_APPROVALS, withdrawal1.transferId.toString(), withdrawal2.transferId.toString(), withdrawal3.transferId.toString());
        WithdrawalApprovals approval1 = objectMapper.readValue(messagesMap.get(withdrawal1.transferId.toString()).getLast(), WithdrawalApprovals.class);
        WithdrawalApprovals approval2 = objectMapper.readValue(messagesMap.get(withdrawal2.transferId.toString()).getLast(), WithdrawalApprovals.class);
        WithdrawalApprovals approval3 = objectMapper.readValue(messagesMap.get(withdrawal3.transferId.toString()).getLast(), WithdrawalApprovals.class);
        for (WithdrawalApprovals approval : List.of(approval1, approval2, approval3)) {
            assertThat("Verify kafka withdrawal message id", approval.messageId, notNullValue());
            assertThat("Verify kafka withdrawal timestamp", approval.timestamp, notNullValue());
            assertThat("Verify kafka withdrawal brand", approval.brand, is(client.getBrand()));
            assertThat("Verify kafka withdrawal regulator", approval.regulator, is(client.getRegulator()));
            assertThat("Verify kafka withdrawal internal reason", approval.internalReason, is(comment));
            assertThat("Verify kafka withdrawal status", approval.status, is("Refuse"));
        }
        assertThat("Verify kafka withdrawal transfer id", approval1.transferId, is(withdrawal1.transferId.longValue()));
        assertThat("Verify kafka withdrawal transfer id", approval2.transferId, is(withdrawal2.transferId.longValue()));
        assertThat("Verify kafka withdrawal transfer id", approval3.transferId, is(withdrawal3.transferId.longValue()));
    }

    @AfterAll
    static void teardown() throws SQLException {
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteEntryFromDb(CRM_WITHDRAWAL_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        closeAlert(crmTbUser.ucid);
    }
}
