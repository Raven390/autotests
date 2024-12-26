package tests.vindexBackofficeUiTests;

import businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObject;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.db.clickhouse.mtTbTradeTable.MtTbTradeObject;
import businessObjects.kafka.alerts.RuleAlert;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.sql.SQLException;
import java.text.DecimalFormat;

import static businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObjectFactory.generateAdditionalCrmTbAccountDataForUi;
import static businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateUserByClient;
import static businessObjects.db.clickhouse.mtTbTradeTable.MtTbTradeFactory.generateMtTbTrade;
import static businessObjects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;

public class TradingInfoDealsTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    private static CrmTbAccountObject account1;
    private static CrmTbAccountObject account2;
    private static MtTbTradeObject trade1;
    private static MtTbTradeObject trade2;

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        account1 = generateCrmTbAccountDataForUi(client);
        insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, account1);
        account2 = generateAdditionalCrmTbAccountDataForUi(client);
        account2.platform = "MT5";
        insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, account2);
        trade1 = generateMtTbTrade(account1.account, account1.serverIdSt);
        trade1.type = "Sell";
        trade1.reason = "API";
        insertObjectToDb(MT_TRADES_TABLE_NAME, trade1);
        trade2 = generateMtTbTrade(account2.account, account2.serverIdSt);
        trade2.platform = "MT5";
        insertObjectToDb(MT_TRADES_TABLE_NAME, trade2);
        RuleAlert alert = generateRuleAlertByUcid(crmTbUser.ucid);
        kafka.produceMessage(alert.alertId, objectMapper.writeValueAsString(alert), KAFKA_TOPIC_ALERTS);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("563")
    @DisplayName("Verify all data is present in trading info - operations tab")
    public void verifyTradingInfoDealsTest() {
        investigationPage.navigate();
        keycloackPage.loginAsCoreUser();
        investigationPage.waitForPageToLoad();
        investigationPage.filterUnassigned();
        investigationPage.waitForPageToLoad();
        investigationPage.clickClientCardByClientId(String.valueOf(crmTbUser.userId));
        alertsPage.waitForPageToLoad();
        tradingPage.openTradingTab();
        tradingPage.openOperationsTab();
        // Verify table headers
        tradingPage.operationsRendersTest();
        // Verify 1st row data
        assertThat("Assert value in account column for the 1st operation is as expected", tradingPage.getOperationAccountByIndex(0), equalTo(String.format("%s%s", trade1.account, trade1.platform)));
        assertThat("Assert value in type column for the 1st operation is as expected", tradingPage.getOperationTypeByIndex(0), equalTo(String.format("%s%s", trade1.symbol, trade1.type)));
        assertThat("Assert value in volume column for the 1st operation is as expected", tradingPage.getOperationVolumeByIndex(0), equalTo(String.format("%s lots%s", trade1.volumeLots, trade1.volumeUsd)));
        assertThat("Assert value in profit column for the 1st operation is as expected", tradingPage.getOperationProfitByIndex(0), equalTo(trade1.profitUsd.toString()));
        DecimalFormat formatter = new DecimalFormat("#,##0.00");
        assertThat("Assert value in open column for the 1st operation is as expected", tradingPage.getOperationOpenByIndex(0), equalTo(String.format("%s%s", formatter.format(trade1.openPrice), trade1.openTime)));
        assertThat("Assert value in close column for the 1st operation is as expected", tradingPage.getOperationCloseByIndex(0), equalTo(String.format("%s%s", formatter.format(trade1.closePrice), trade1.closeTime)));
        assertThat("Assert value in tp/sl column for the 1st operation is as expected", tradingPage.getOperationTpSlByIndex(0), equalTo(String.format("%s%s", trade1.takeProfit, trade1.stopLoss)));
        assertThat("Assert value in swap column for the 1st operation is as expected", tradingPage.getOperationSwapByIndex(0), equalTo(trade1.swapUsd.toString()));
        assertThat("Assert value in sr column for the 1st operation is as expected", tradingPage.getOperationSrByIndex(0), equalTo(trade1.spreadRevenueUsd.toString()));
        assertThat("Assert value in commission column for the 1st operation is as expected", tradingPage.getOperationCommissionByIndex(0), equalTo(trade1.commissionUsd.toString()));
        assertThat("Assert value in method column for the 1st operation is as expected", tradingPage.getOperationMethodByIndex(0), equalTo(trade1.reason));
        assertThat("Assert value in comment column for the 1st operation is as expected", tradingPage.getOperationCommentByIndex(0), equalTo(trade1.comment));
        // Verify 2nd row data
        assertThat("Assert value in account column for the 2nd operation is as expected", tradingPage.getOperationAccountByIndex(1), equalTo(String.format("%s%s", trade2.account, trade2.platform)));
        assertThat("Assert value in type column for the 2nd operation is as expected", tradingPage.getOperationTypeByIndex(1), equalTo(String.format("%s%s", trade2.symbol, trade2.type)));
        assertThat("Assert value in volume column for the 2nd operation is as expected", tradingPage.getOperationVolumeByIndex(1), equalTo(String.format("%s lots%s", trade2.volumeLots, trade2.volumeUsd)));
        assertThat("Assert value in profit column for the 2nd operation is as expected", tradingPage.getOperationProfitByIndex(1), equalTo(trade2.profitUsd.toString()));
        assertThat("Assert value in open column for the 2nd operation is as expected", tradingPage.getOperationOpenByIndex(1), equalTo(String.format("%s%s", formatter.format(trade2.openPrice), trade2.openTime)));
        assertThat("Assert value in close column for the 2nd operation is as expected", tradingPage.getOperationCloseByIndex(1), equalTo(String.format("%s%s", formatter.format(trade2.closePrice), trade2.closeTime)));
        assertThat("Assert value in tp/sl column for the 2nd operation is as expected", tradingPage.getOperationTpSlByIndex(1), equalTo(String.format("%s%s", trade2.takeProfit, trade2.stopLoss)));
        assertThat("Assert value in swap column for the 2nd operation is as expected", tradingPage.getOperationSwapByIndex(1), equalTo(trade2.swapUsd.toString()));
        assertThat("Assert value in sr column for the 2nd operation is as expected", tradingPage.getOperationSrByIndex(1), equalTo(trade2.spreadRevenueUsd.toString()));
        assertThat("Assert value in commission column for the 2nd operation is as expected", tradingPage.getOperationCommissionByIndex(1), equalTo(trade2.commissionUsd.toString()));
        assertThat("Assert value in method column for the 2nd operation is as expected", tradingPage.getOperationMethodByIndex(1), equalTo(trade2.reason));
        assertThat("Assert value in comment column for the 2nd operation is as expected", tradingPage.getOperationCommentByIndex(1), equalTo(trade2.comment));
    }

    @AfterAll
    public static void teardown() throws SQLException {
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", crmTbUser.ucid));
        deleteEntryFromDb(MT_TRADES_TABLE_NAME, String.format("account = %s OR account = %s", account1.account, account2.account));
        closeAlert(crmTbUser.ucid);
    }
}
