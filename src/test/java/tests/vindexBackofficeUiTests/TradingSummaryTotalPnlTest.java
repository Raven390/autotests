package tests.vindexBackofficeUiTests;

import businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObject;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.db.clickhouse.tsBySymbolDaily.TsBySymbolDailyObject;
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
import java.util.List;

import static businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateUserByClient;
import static businessObjects.db.clickhouse.tsBySymbolDaily.TsBySymbolDailyFactory.generateTsBySymbolDailyByClient;
import static businessObjects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.DateTimeFormat.DATE;
import static helpers.data.enums.DateTimeFormat.MONTH_TEXT_AND_DAY;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.CleanTableHelper.cleanCrmUserTableByClient;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;
import static utils.Utils.getCurrentTimestampMinusOffsetFormatted;
import static utils.Utils.transformDate;

public class TradingSummaryTotalPnlTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    private static final CrmTbAccountObject account = generateCrmTbAccountDataForUi(client);
    private static final TsBySymbolDailyObject tsBySymbolDaily1 = generateTsBySymbolDailyByClient(client);
    private static final TsBySymbolDailyObject tsBySymbolDaily2 = generateTsBySymbolDailyByClient(client);
    private static final TsBySymbolDailyObject tsBySymbolDaily3 = generateTsBySymbolDailyByClient(client);
    private static final TsBySymbolDailyObject tsBySymbolDaily4 = generateTsBySymbolDailyByClient(client);
    private static final TsBySymbolDailyObject tsBySymbolDaily5 = generateTsBySymbolDailyByClient(client);
    private static final TsBySymbolDailyObject tsBySymbolDaily6 = generateTsBySymbolDailyByClient(client);
    private static final TsBySymbolDailyObject tsBySymbolDaily7 = generateTsBySymbolDailyByClient(client);
    private static final TsBySymbolDailyObject tsBySymbolDaily8 = generateTsBySymbolDailyByClient(client);


    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        tsBySymbolDaily1.date = getCurrentTimestampMinusOffsetFormatted(DATE, 0, 0, 1, 0, 0);
        tsBySymbolDaily2.date = getCurrentTimestampMinusOffsetFormatted(DATE, 0, 0, 2, 0, 0);
        tsBySymbolDaily2.totalPnl = -3546.965;
        tsBySymbolDaily3.date = getCurrentTimestampMinusOffsetFormatted(DATE, 0, 0, 4, 0, 0);
        tsBySymbolDaily3.totalPnl = 6666.01;
        tsBySymbolDaily4.date = getCurrentTimestampMinusOffsetFormatted(DATE, 0, 0, 5, 0, 0);
        tsBySymbolDaily4.totalPnl = -7.65;
        tsBySymbolDaily5.date = getCurrentTimestampMinusOffsetFormatted(DATE, 0, 0, 6, 0, 0);
        tsBySymbolDaily5.totalPnl = -1345.97;
        tsBySymbolDaily6.date = getCurrentTimestampMinusOffsetFormatted(DATE, 0, 0, 7, 0, 0);
        tsBySymbolDaily6.totalPnl = -2254.765;
        tsBySymbolDaily7.date = getCurrentTimestampMinusOffsetFormatted(DATE, 0, 0, 8, 0, 0);
        tsBySymbolDaily7.totalPnl = 1970.102;
        tsBySymbolDaily8.date = getCurrentTimestampMinusOffsetFormatted(DATE, 0, 0, 9, 0, 0);
        tsBySymbolDaily8.totalPnl = 2854.345;
        crmTbUser.registrationDate = tsBySymbolDaily8.date;
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, account);
        insertObjectsToDb(TS_BY_SYMBOL_DAILY_TABLE_NAME, List.of(tsBySymbolDaily1, tsBySymbolDaily2, tsBySymbolDaily3, tsBySymbolDaily4, tsBySymbolDaily5, tsBySymbolDaily6, tsBySymbolDaily7, tsBySymbolDaily8));
        RuleAlert alert = generateRuleAlertByUcid(crmTbUser.ucid);
        kafka.produceMessage(alert.alertId, objectMapper.writeValueAsString(alert), KAFKA_TOPIC_ALERTS);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("868")
    @DisplayName("Verify Total PNL chart in Trading - Summary")
    public void verifyTradingSummaryTotalPnlTest() {
        investigationPage.navigateToClient(crmTbUser.ucid);
        keycloackPage.loginAsCoreUser();
        alertsPage.waitForPageToLoad();
        tradingPage.openTradingTab();
        tradingPage.openSummaryTab();
        assertThat("Verify Total PNL chart title", tradingPage.getTotalPnlChartTitle(), equalTo("PNLtotal, USD"));
        assertThat("Verify Total PNL Y axis label", tradingPage.getTotalPnlYAxisLabel(), equalTo("7k"));
        DecimalFormat format = new DecimalFormat("#,###");
        String maxProfitDate = transformDate(tsBySymbolDaily3.date, DATE, MONTH_TEXT_AND_DAY);
        String maxLossDate = transformDate(tsBySymbolDaily2.date, DATE, MONTH_TEXT_AND_DAY);
        String maxProfit = format.format(Math.round(tsBySymbolDaily3.totalPnl));
        String maxLoss = format.format(Math.round(tsBySymbolDaily2.totalPnl));
        assertThat("Verify Total PNL max profit value", tradingPage.getTotalPnlMaxProfitValue(), equalTo(maxProfit));
        assertThat("Verify Total PNL max profit label", tradingPage.getTotalPnlMaxProfitLabel(), equalTo(String.format("Max profit – %s", maxProfitDate)));
        assertThat("Verify Total PNL max loss value", tradingPage.getTotalPnlMaxLossValue(), equalTo(maxLoss));
        assertThat("Verify Total PNL max loss label", tradingPage.getTotalPnlMaxLossLabel(), equalTo(String.format("Max loss – %s", maxLossDate)));
        assertThat("Verify Total PNL max profit graph dot value", tradingPage.getTotalPnlMaxProfitGraphDot(), equalTo(maxProfit));
        assertThat("Verify Total PNL max loss graph dot value", tradingPage.getTotalPnlMaxLossGraphDot(), equalTo(maxLoss));
    }

    @AfterAll
    public static void teardown() throws Exception {
        cleanCrmUserTableByClient(crmTbUser.ucid);
        deleteEntryFromDb(TS_BY_SYMBOL_DAILY_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        closeAlert(crmTbUser.ucid);
    }
}
