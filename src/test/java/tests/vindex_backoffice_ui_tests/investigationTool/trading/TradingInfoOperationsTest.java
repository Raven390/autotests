package tests.vindex_backoffice_ui_tests.investigationTool.trading;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObject;
import business_objects.kafka.alerts.RuleAlert;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.sql.SQLException;
import java.text.DecimalFormat;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateAdditionalCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoerced;
import static business_objects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.DateTimeFormat.DATE_AND_TIME;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;
import static utils.Utils.getCurrentTimestampMinusOffsetFormatted;
import static utils.Utils.insertCrmAccountsToDb;

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
        trade1.serverId = account1.serverIdSt.longValue();
        trade1.ticketType = "Sell";
        trade1.reasonName = "API";
        insertObjectToDb(MT4_TRADES_COERCED_TABLE_NAME, trade1);
        trade2 = generateMt4TradesCoerced(client);
        trade2.account = client.getTradingAccount2().longValue();
        trade2.serverId = account2.serverIdSt.longValue();
        trade2.platform = "MT5";
        trade2.openTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 1, 0, 0);
        insertObjectToDb(MT4_TRADES_COERCED_TABLE_NAME, trade2);
        RuleAlert alert = generateRuleAlertByUcid(client.getUcid());
        alert.rule.attributes.ticketId = trade1.ticket.toString();
        new KafkaHelper().produceMessage(alert.alertId, new ObjectMapper().writeValueAsString(alert), KAFKA_TOPIC_ALERTS);
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
        assertThat("Assert value in account column for the 1st operation is as expected", tradingPage.getOperationAccountByIndex(0), equalTo(String.format("%s%s", trade2.account, trade2.platform)));
        assertThat("Assert value in type column for the 1st operation is as expected", tradingPage.getOperationTypeByIndex(0), equalTo(String.format("%s%s", trade2.symbol, trade2.ticketType)));
        assertThat("Assert that the deal is without alert (no lightning icon)", tradingPage.isOperationWithAlert(0), equalTo(false));
        assertThat("Assert value in volume column for the 1st operation is as expected", tradingPage.getOperationVolumeByIndex(0), equalTo(String.format("%s lots%s USD", trade2.volumeLots, trade2.notionalValueUsd)));
        assertThat("Assert value in profit column for the 1st operation is as expected", tradingPage.getOperationProfitByIndex(0), equalTo(String.format("%s USD", trade2.profitUsd.toString())));
        DecimalFormat formatter = new DecimalFormat("#,##0.00");
        assertThat("Assert value in open column for the 1st operation is as expected", tradingPage.getOperationOpenByIndex(0), equalTo(String.format("%s%s", trade2.openTime, formatter.format(trade2.openPrice))));
        assertThat("Assert value in close column for the 1st operation is as expected", tradingPage.getOperationCloseByIndex(0), equalTo(String.format("%s%s", trade2.closeTime, formatter.format(trade2.closePrice))));
        assertThat("Assert value in tp/sl column for the 1st operation is as expected", tradingPage.getOperationTpSlByIndex(0), equalTo(String.format("TP %sSL %s", trade2.takeProfit, trade2.stopLoss)));
        assertThat("Assert value in swap column for the 1st operation is as expected", tradingPage.getOperationSwapByIndex(0), equalTo(String.format("%s USD", trade2.storageUsd.toString())));
        assertThat("Assert value in sr column for the 1st operation is as expected", tradingPage.getOperationSrByIndex(0), equalTo(String.format("%s USD", trade2.spreadRevenueUsd.toString())));
        assertThat("Assert value in commission column for the 1st operation is as expected", tradingPage.getOperationCommissionByIndex(0), equalTo(String.format("%s USD", trade2.commissionUsd.toString())));
        assertThat("Assert value in method column for the 1st operation is as expected", tradingPage.getOperationMethodByIndex(0), equalTo(trade2.reasonName));
        assertThat("Assert value in comment column for the 1st operation is as expected", tradingPage.getOperationCommentByIndex(0), equalTo(trade2.comment));
        // Verify 2nd row data
        assertThat("Assert value in account column for the 2nd operation is as expected", tradingPage.getOperationAccountByIndex(1), equalTo(String.format("%s%s", trade1.account, trade1.platform)));
        assertThat("Assert value in type column for the 2nd operation is as expected", tradingPage.getOperationTypeByIndex(1), equalTo(String.format("%s%s", trade1.symbol, trade1.ticketType)));
        assertThat("Assert that the deal is with alert (lightning icon)", tradingPage.isOperationWithAlert(0), equalTo(false));
        assertThat("Assert value in volume column for the 2nd operation is as expected", tradingPage.getOperationVolumeByIndex(1), equalTo(String.format("%s lots%s USD", trade1.volumeLots, trade1.notionalValueUsd)));
        assertThat("Assert value in profit column for the 2nd operation is as expected", tradingPage.getOperationProfitByIndex(1), equalTo(String.format("%s USD", trade1.profitUsd.toString())));
        assertThat("Assert value in open column for the 2nd operation is as expected", tradingPage.getOperationOpenByIndex(1), equalTo(String.format("%s%s", trade1.openTime, formatter.format(trade1.openPrice))));
        assertThat("Assert value in close column for the 2nd operation is as expected", tradingPage.getOperationCloseByIndex(1), equalTo(String.format("%s%s", trade1.closeTime, formatter.format(trade1.closePrice))));
        assertThat("Assert value in tp/sl column for the 2nd operation is as expected", tradingPage.getOperationTpSlByIndex(1), equalTo(String.format("TP %sSL %s", trade1.takeProfit, trade1.stopLoss)));
        assertThat("Assert value in swap column for the 2nd operation is as expected", tradingPage.getOperationSwapByIndex(1), equalTo(String.format("%s USD", trade1.storageUsd.toString())));
        assertThat("Assert value in sr column for the 2nd operation is as expected", tradingPage.getOperationSrByIndex(1), equalTo(String.format("%s USD", trade1.spreadRevenueUsd.toString())));
        assertThat("Assert value in commission column for the 2nd operation is as expected", tradingPage.getOperationCommissionByIndex(1), equalTo(String.format("%s USD", trade1.commissionUsd.toString())));
        assertThat("Assert value in method column for the 2nd operation is as expected", tradingPage.getOperationMethodByIndex(1), equalTo(trade1.reasonName));
        assertThat("Assert value in comment column for the 2nd operation is as expected", tradingPage.getOperationCommentByIndex(1), equalTo(trade1.comment));
    }

    @AfterAll
    public static void teardown() throws SQLException {
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", crmTbUser.ucid));
        deleteEntryFromDb(MT4_TRADES_COERCED_TABLE_NAME, String.format("account = %s OR account = %s", account1.account, account2.account));
        closeAlert(crmTbUser.ucid);
    }
}
