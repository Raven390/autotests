package tests.vindex_backoffice_ui_tests.investigationTool;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
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
import static business_objects.db.clickhouse.mtAccount.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.kafka.alerts.RuleAlertFactory.generateWithdrawalNotificationAlert;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.DateTimeFormat.*;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PaymentsWithdrawalsTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    private static final CrmTbAccountObject account = generateCrmTbAccountDataForUi(client);
    private static final MtAccountObject mtAccount = generateMtAccountByCrmTbAccount(account);
    private static final List<String> withdrawalData1 = new ArrayList<>();
    private static final List<String> withdrawalData2 = new ArrayList<>();
    private static final List<String> withdrawalData3 = new ArrayList<>();
    private static final RuleAlert withdrawalAlert1 = generateWithdrawalNotificationAlert(client);
    private static final RuleAlert withdrawalAlert2 = generateWithdrawalNotificationAlert(client);
    private static final RuleAlert withdrawalAlert3 = generateWithdrawalNotificationAlert(client);
    private static final DecimalFormat formatter = new DecimalFormat("#,###.##");

    @BeforeAll
    static void setup() throws IOException {
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, account);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, mtAccount);
        withdrawalAlert1.rule.attributes.amount = "123.45";
        withdrawalAlert1.rule.attributes.currency = "EUR";
        withdrawalAlert1.rule.attributes.check = "Big_Amount";
        withdrawalAlert2.rule.attributes.amount = "1001.13";
        withdrawalAlert2.rule.attributes.currency = "USD";
        withdrawalAlert2.rule.attributes.createTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 1, 0, 0).replace(" ", "T") + "+03:00";
        withdrawalAlert2.rule.attributes.check = "WR_Blacklist";
        withdrawalAlert3.rule.attributes.amount = "60783.76";
        withdrawalAlert3.rule.attributes.currency = "GBP";
        withdrawalAlert3.rule.attributes.createTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 1, 0, 0, 0).replace(" ", "T") + "+03:00";
        withdrawalAlert3.rule.attributes.check = "High_Risk";
        withdrawalData1.add(transformDate(withdrawalAlert1.rule.attributes.createTime.replace("T", " ").replace("+03:00", ""), DATE_AND_TIME, DATE));
        withdrawalData1.add(transformDate(withdrawalAlert1.rule.attributes.createTime.replace("T", " ").replace("+03:00", ""), DATE_AND_TIME, TIME));
        withdrawalData1.add(withdrawalAlert1.rule.attributes.paymentChannel);
        withdrawalData1.add(withdrawalAlert1.rule.attributes.paymentType);
        withdrawalData1.add(String.format("%s %s", formatter.format(Double.valueOf(withdrawalAlert1.rule.attributes.amount)), withdrawalAlert1.rule.attributes.currency));
        withdrawalData1.add(withdrawalAlert1.rule.attributes.account);
        withdrawalData1.add(withdrawalAlert1.rule.attributes.platform);
        withdrawalData1.add(withdrawalAlert1.rule.attributes.check);
        withdrawalData1.add("Risk Audit");
        withdrawalData2.add(transformDate(withdrawalAlert2.rule.attributes.createTime.replace("T", " ").replace("+03:00", ""), DATE_AND_TIME, DATE));
        withdrawalData2.add(transformDate(withdrawalAlert2.rule.attributes.createTime.replace("T", " ").replace("+03:00", ""), DATE_AND_TIME, TIME));
        withdrawalData2.add(withdrawalAlert2.rule.attributes.paymentChannel);
        withdrawalData2.add(withdrawalAlert2.rule.attributes.paymentType);
        withdrawalData2.add(String.format("%s %s", formatter.format(Double.valueOf(withdrawalAlert2.rule.attributes.amount)), withdrawalAlert2.rule.attributes.currency));
        withdrawalData2.add(withdrawalAlert2.rule.attributes.account);
        withdrawalData2.add(withdrawalAlert2.rule.attributes.platform);
        withdrawalData2.add(withdrawalAlert2.rule.attributes.check);
        withdrawalData2.add("Risk Audit");
        withdrawalData3.add(transformDate(withdrawalAlert3.rule.attributes.createTime.replace("T", " ").replace("+03:00", ""), DATE_AND_TIME, DATE));
        withdrawalData3.add(transformDate(withdrawalAlert3.rule.attributes.createTime.replace("T", " ").replace("+03:00", ""), DATE_AND_TIME, TIME));
        withdrawalData3.add(withdrawalAlert3.rule.attributes.paymentChannel);
        withdrawalData3.add(withdrawalAlert3.rule.attributes.paymentType);
        withdrawalData3.add(String.format("%s %s", formatter.format(Double.valueOf(withdrawalAlert3.rule.attributes.amount)), withdrawalAlert3.rule.attributes.currency));
        withdrawalData3.add(withdrawalAlert3.rule.attributes.account);
        withdrawalData3.add(withdrawalAlert3.rule.attributes.platform);
        withdrawalData3.add(withdrawalAlert3.rule.attributes.check);
        withdrawalData3.add("Risk Audit");
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
        assertThat("Verify data in table", paymentsPage.getAllRowsData(), hasItems(Stream.of(withdrawalData1, withdrawalData2, withdrawalData3).flatMap(List::stream).toArray(String[]::new)));
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
        assertThat("Verify rows count", paymentsPage.getRowsCount(), is(2));
        assertThat("Verify data in table", paymentsPage.getAllRowsData(), hasItems(Stream.of(withdrawalData1, withdrawalData2).flatMap(List::stream).toArray(String[]::new)));
    }

    @Test
    @Order(6)
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
        assertThat("Verify rows count", paymentsPage.getRowsCount(), is(1));
        assertThat("Verify data in table", paymentsPage.getAllRowsData(), hasItems(withdrawalData3.toArray(String[]::new)));
    }

    @Test
    @Order(7)
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
        Map<String, List<String>> messagesMap = kafka.consumeMessages(KAFKA_TOPIC_WITHDRAWAL_APPROVALS, withdrawalAlert1.rule.attributes.withdrawalId, withdrawalAlert2.rule.attributes.withdrawalId, withdrawalAlert3.rule.attributes.withdrawalId);
        WithdrawalApprovals approval1 = objectMapper.readValue(messagesMap.get(withdrawalAlert1.rule.attributes.withdrawalId).getLast(), WithdrawalApprovals.class);
        WithdrawalApprovals approval2 = objectMapper.readValue(messagesMap.get(withdrawalAlert2.rule.attributes.withdrawalId).getLast(), WithdrawalApprovals.class);
        WithdrawalApprovals approval3 = objectMapper.readValue(messagesMap.get(withdrawalAlert3.rule.attributes.withdrawalId).getLast(), WithdrawalApprovals.class);
        for (WithdrawalApprovals approval : List.of(approval1, approval2, approval3)) {
            assertThat("Verify kafka withdrawal message id", approval.messageId, notNullValue());
            assertThat("Verify kafka withdrawal timestamp", approval.timestamp, notNullValue());
            assertThat("Verify kafka withdrawal brand", approval.brand, is(client.getBrand()));
            assertThat("Verify kafka withdrawal regulator", approval.regulator, is(client.getRegulator()));
            assertThat("Verify kafka withdrawal internal reason", approval.internalReason, is(""));
            assertThat("Verify kafka withdrawal status", approval.status, is("Refuse"));
            assertThat("Verify kafka withdrawal orderNumber", approval.orderNumber, is(withdrawalAlert1.rule.attributes.orderId));
            assertThat("Verify kafka withdrawal checkName", approval.checkName, notNullValue());
        }
        assertThat("Verify kafka withdrawal transfer id", approval1.transferId, is(Long.valueOf(withdrawalAlert1.rule.attributes.withdrawalId)));
        assertThat("Verify kafka withdrawal checkName", approval1.checkName, is(withdrawalAlert1.rule.attributes.check));
        assertThat("Verify kafka withdrawal transfer id", approval2.transferId, is(Long.valueOf(withdrawalAlert2.rule.attributes.withdrawalId)));
        assertThat("Verify kafka withdrawal checkName", approval2.checkName, is(withdrawalAlert2.rule.attributes.check));
        assertThat("Verify kafka withdrawal transfer id", approval3.transferId, is(Long.valueOf(withdrawalAlert3.rule.attributes.withdrawalId)));
        assertThat("Verify kafka withdrawal checkName", approval3.checkName, is(withdrawalAlert3.rule.attributes.check));
    }

    @AfterAll
    static void teardown() throws SQLException {
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteEntryFromDb(CRM_WITHDRAWAL_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        closeAlert(crmTbUser.ucid);
    }
}
