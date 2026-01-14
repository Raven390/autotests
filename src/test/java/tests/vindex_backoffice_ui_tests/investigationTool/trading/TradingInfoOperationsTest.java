package tests.vindex_backoffice_ui_tests.investigationTool.trading;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateAdditionalCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoerced;
import static business_objects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.DateTimeFormat.DATE_AND_TIME;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.DbHelper.deleteObjectFromDb;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;
import static utils.Utils.getCurrentTimestampMinusOffsetFormatted;
import static utils.Utils.insertCrmAccountsToDb;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObject;
import business_objects.kafka.alerts.RuleAlert;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import java.sql.SQLException;
import java.text.DecimalFormat;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

public class TradingInfoOperationsTest extends TestBaseWeb {

    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    private static CrmTbAccountObject account1;
    private static CrmTbAccountObject account2;
    private static MtMt4TradesCoercedObject trade1;
    private static MtMt4TradesCoercedObject trade2;

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        account1 = generateCrmTbAccountDataForUi(client);
        insertCrmAccountsToDb(account1);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, generateMtAccountByCrmTbAccount(account1));
        account2 = generateAdditionalCrmTbAccountDataForUi(client);
        account2.platform = "MT5";
        insertCrmAccountsToDb(account2);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, generateMtAccountByCrmTbAccount(account2));
        trade1 = generateMt4TradesCoerced(client);
        trade1.setServerId(account1.serverIdSt.longValue());
        trade1.setTicketType("Sell");
        trade1.setReasonName("API");
        insertObjectToDb(MT4_TRADES_COERCED_TABLE_NAME, trade1);
        trade2 = generateMt4TradesCoerced(client);
        trade2.setAccount(client.getTradingAccount2().longValue());
        trade2.setServerId(account2.serverIdSt.longValue());
        trade2.setPlatform("MT5");
        trade2.setOpenTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 1, 0, 0));
        insertObjectToDb(MT4_TRADES_COERCED_TABLE_NAME, trade2);
        RuleAlert alert = generateRuleAlertByUcid(client.getUcid());
        alert.rule.attributes.ticketId = trade1.getTicket().toString();
        new KafkaHelper()
                .produceMessage(alert.alertId, new ObjectMapper().writeValueAsString(alert), KAFKA_TOPIC_ALERTS);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("563")
    @DisplayName("Verify all data is present in trading info - operations tab")
    public void verifyTradingInfoDealsTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        tradingPage.openTradingTab();
        tradingPage.openOperationsTab();
        // Verify table headers
        tradingPage.verifyHeaders();
        // Verify 1st row data
        assertThat(
                "Assert value in account column for the 1st operation is as expected",
                tradingPage.getOperationAccountByIndex(0),
                equalTo(String.format("%s%s", trade2.getAccount(), trade2.getPlatform())));
        assertThat(
                "Assert value in type column for the 1st operation is as expected",
                tradingPage.getOperationTypeByIndex(0),
                equalTo(String.format("%s%s", trade2.getSymbol(), trade2.getTicketType())));
        assertThat(
                "Assert that the deal is without alert (no lightning icon)",
                tradingPage.isOperationWithAlert(0),
                equalTo(false));
        assertThat(
                "Assert value in volume column for the 1st operation is as expected",
                tradingPage.getOperationVolumeByIndex(0),
                equalTo(String.format("%s lots%s USD", trade2.getVolumeLots(), trade2.getNotionalValueUsd())));
        assertThat(
                "Assert value in profit column for the 1st operation is as expected",
                tradingPage.getOperationProfitByIndex(0),
                equalTo(String.format("%s USD", trade2.getProfitUsd().toString())));
        DecimalFormat formatter = new DecimalFormat("#,##0.00");
        assertThat(
                "Assert value in open column for the 1st operation is as expected",
                tradingPage.getOperationOpenByIndex(0),
                equalTo(String.format("%s%s", trade2.getOpenTime(), formatter.format(trade2.getOpenPrice()))));
        assertThat(
                "Assert value in close column for the 1st operation is as expected",
                tradingPage.getOperationCloseByIndex(0),
                equalTo(String.format("%s%s", trade2.getCloseTime(), formatter.format(trade2.getClosePrice()))));
        assertThat(
                "Assert value in tp/sl column for the 1st operation is as expected",
                tradingPage.getOperationTpSlByIndex(0),
                equalTo(String.format("TP %sSL %s", trade2.getTakeProfit(), trade2.getStopLoss())));
        assertThat(
                "Assert value in swap column for the 1st operation is as expected",
                tradingPage.getOperationSwapByIndex(0),
                equalTo(String.format("%s USD", trade2.getStorageUsd().toString())));
        assertThat(
                "Assert value in sr column for the 1st operation is as expected",
                tradingPage.getOperationSrByIndex(0),
                equalTo(String.format("%s USD", trade2.getSpreadRevenueUsd().toString())));
        assertThat(
                "Assert value in commission column for the 1st operation is as expected",
                tradingPage.getOperationCommissionByIndex(0),
                equalTo(String.format("%s USD", trade2.getCommissionUsd().toString())));
        assertThat(
                "Assert value in method column for the 1st operation is as expected",
                tradingPage.getOperationMethodByIndex(0),
                equalTo(trade2.getReasonName()));
        assertThat(
                "Assert value in comment column for the 1st operation is as expected",
                tradingPage.getOperationCommentByIndex(0),
                equalTo(trade2.getComment()));
        // Verify 2nd row data
        assertThat(
                "Assert value in account column for the 2nd operation is as expected",
                tradingPage.getOperationAccountByIndex(1),
                equalTo(String.format("%s%s", trade1.getAccount(), trade1.getPlatform())));
        assertThat(
                "Assert value in type column for the 2nd operation is as expected",
                tradingPage.getOperationTypeByIndex(1),
                equalTo(String.format("%s%s", trade1.getSymbol(), trade1.getTicketType())));
        assertThat(
                "Assert that the deal is with alert (lightning icon)",
                tradingPage.isOperationWithAlert(0),
                equalTo(false));
        assertThat(
                "Assert value in volume column for the 2nd operation is as expected",
                tradingPage.getOperationVolumeByIndex(1),
                equalTo(String.format("%s lots%s USD", trade1.getVolumeLots(), trade1.getNotionalValueUsd())));
        assertThat(
                "Assert value in profit column for the 2nd operation is as expected",
                tradingPage.getOperationProfitByIndex(1),
                equalTo(String.format("%s USD", trade1.getProfitUsd().toString())));
        assertThat(
                "Assert value in open column for the 2nd operation is as expected",
                tradingPage.getOperationOpenByIndex(1),
                equalTo(String.format("%s%s", trade1.getOpenTime(), formatter.format(trade1.getOpenPrice()))));
        assertThat(
                "Assert value in close column for the 2nd operation is as expected",
                tradingPage.getOperationCloseByIndex(1),
                equalTo(String.format("%s%s", trade1.getCloseTime(), formatter.format(trade1.getClosePrice()))));
        assertThat(
                "Assert value in tp/sl column for the 2nd operation is as expected",
                tradingPage.getOperationTpSlByIndex(1),
                equalTo(String.format("TP %sSL %s", trade1.getTakeProfit(), trade1.getStopLoss())));
        assertThat(
                "Assert value in swap column for the 2nd operation is as expected",
                tradingPage.getOperationSwapByIndex(1),
                equalTo(String.format("%s USD", trade1.getStorageUsd().toString())));
        assertThat(
                "Assert value in sr column for the 2nd operation is as expected",
                tradingPage.getOperationSrByIndex(1),
                equalTo(String.format("%s USD", trade1.getSpreadRevenueUsd().toString())));
        assertThat(
                "Assert value in commission column for the 2nd operation is as expected",
                tradingPage.getOperationCommissionByIndex(1),
                equalTo(String.format("%s USD", trade1.getCommissionUsd().toString())));
        assertThat(
                "Assert value in method column for the 2nd operation is as expected",
                tradingPage.getOperationMethodByIndex(1),
                equalTo(trade1.getReasonName()));
        assertThat(
                "Assert value in comment column for the 2nd operation is as expected",
                tradingPage.getOperationCommentByIndex(1),
                equalTo(trade1.getComment()));
    }

    @AfterAll
    public static void teardown() throws SQLException {
        deleteObjectFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", crmTbUser.ucid));
        deleteObjectFromDb(
                MT4_TRADES_COERCED_TABLE_NAME,
                String.format("account = %s OR account = %s", account1.account, account2.account));
        closeAlert(crmTbUser.ucid);
    }
}
