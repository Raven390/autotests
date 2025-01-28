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
import java.util.Arrays;
import java.util.List;

import static businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateUserByClient;
import static businessObjects.db.clickhouse.tsBySymbolDaily.TsBySymbolDailyFactory.generateTsBySymbolDailyByClient;
import static businessObjects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.CleanTableHelper.cleanCrmUserTableByClient;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;
import static utils.Utils.calculatePercentageFromList;

public class TradingSummaryPerformanceOverviewTest extends TestBaseWeb {

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
    private static final TsBySymbolDailyObject tsBySymbolDaily9 = generateTsBySymbolDailyByClient(client);

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        tsBySymbolDaily1.countTotalDeals = 13L;
        tsBySymbolDaily1.countWinDeals = 9L;
        tsBySymbolDaily1.countDealsLess10m = 0L;
        tsBySymbolDaily2.countTotalDeals = 5L;
        tsBySymbolDaily2.countWinDeals = 1L;
        tsBySymbolDaily2.countDealsLess10m = 4L;
        tsBySymbolDaily2.totalPnl = -3546.965;
        tsBySymbolDaily3.symbol = "EURUSD";
        tsBySymbolDaily3.totalPnl = 6666.01;
        tsBySymbolDaily3.countTotalDeals = 22L;
        tsBySymbolDaily3.countWinDeals = 3L;
        tsBySymbolDaily3.countDealsLess10m = 22L;
        tsBySymbolDaily4.symbol = "EURUSD";
        tsBySymbolDaily4.totalPnl = -7.65;
        tsBySymbolDaily4.countTotalDeals = 2L;
        tsBySymbolDaily4.countWinDeals = 2L;
        tsBySymbolDaily4.countDealsLess10m = 0L;
        tsBySymbolDaily5.symbol = "GBPJPY";
        tsBySymbolDaily5.totalPnl = -1345.97;
        tsBySymbolDaily5.countTotalDeals = 1L;
        tsBySymbolDaily5.countWinDeals = 1L;
        tsBySymbolDaily5.countDealsLess10m = 0L;
        tsBySymbolDaily6.symbol = "GBPJPY";
        tsBySymbolDaily6.totalPnl = -2254.765;
        tsBySymbolDaily6.countTotalDeals = 3L;
        tsBySymbolDaily6.countWinDeals = 3L;
        tsBySymbolDaily6.countDealsLess10m = 0L;
        tsBySymbolDaily7.symbol = "AEDCZK";
        tsBySymbolDaily7.totalPnl = 1970.102;
        tsBySymbolDaily7.countTotalDeals = 13L;
        tsBySymbolDaily7.countWinDeals = 3L;
        tsBySymbolDaily7.countDealsLess10m = 7L;
        tsBySymbolDaily8.symbol = "CZKAED";
        tsBySymbolDaily8.totalPnl = 2854.345;
        tsBySymbolDaily8.countTotalDeals = 7L;
        tsBySymbolDaily8.countWinDeals = 2L;
        tsBySymbolDaily8.countDealsLess10m = 3L;
        tsBySymbolDaily9.symbol = "HRKEUR";
        tsBySymbolDaily9.totalPnl = -99_999.0;
        crmTbUser.registrationDate = tsBySymbolDaily8.date;
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, account);
        insertObjectsToDb(TS_BY_SYMBOL_DAILY_TABLE_NAME, List.of(tsBySymbolDaily1, tsBySymbolDaily2, tsBySymbolDaily3, tsBySymbolDaily4, tsBySymbolDaily5, tsBySymbolDaily6, tsBySymbolDaily7, tsBySymbolDaily8, tsBySymbolDaily9));
        RuleAlert alert = generateRuleAlertByUcid(crmTbUser.ucid);
        kafka.produceMessage(alert.alertId, objectMapper.writeValueAsString(alert), KAFKA_TOPIC_ALERTS);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("869")
    @DisplayName("Verify Performance overview table in Trading - Summary")
    public void verifyTradingSummaryPerformanceOverviewTest() {
        investigationPage.navigateToClient(crmTbUser.ucid);
        keycloackPage.loginAsCoreUser();
        alertsPage.waitForPageToLoad();
        tradingPage.openTradingTab();
        tradingPage.openSummaryTab();
        assertThat("Verify title", tradingPage.getPerformanceOverviewTableTitle(), equalTo("Performance overview"));
        assertThat("Verify headers", tradingPage.getPerformanceOverviewTableHeaders(), contains("SYMBOL", "DEALS", "WINRATE", "HFT", "PNL, USD"));
        assertThat("Verify symbols", tradingPage.getPerformanceOverviewSymbols(), contains("EURUSD", "CZKAED", "AEDCZK", "USDEUR", "GBPJPY"));
        // Deals
        assertThat("Verify deals", tradingPage.getPerformanceOverviewDealsBySymbol("EURUSD"), equalTo(String.valueOf(tsBySymbolDaily3.countTotalDeals + tsBySymbolDaily4.countTotalDeals)));
        assertThat("Verify deals", tradingPage.getPerformanceOverviewDealsBySymbol("CZKAED"), equalTo(String.valueOf(tsBySymbolDaily8.countTotalDeals)));
        assertThat("Verify deals", tradingPage.getPerformanceOverviewDealsBySymbol("AEDCZK"), equalTo(String.valueOf(tsBySymbolDaily7.countTotalDeals)));
        assertThat("Verify deals", tradingPage.getPerformanceOverviewDealsBySymbol("USDEUR"), equalTo(String.valueOf(tsBySymbolDaily1.countTotalDeals + tsBySymbolDaily2.countTotalDeals)));
        assertThat("Verify deals", tradingPage.getPerformanceOverviewDealsBySymbol("GBPJPY"), equalTo(String.valueOf(tsBySymbolDaily5.countTotalDeals + tsBySymbolDaily6.countTotalDeals)));
        // Winrate
        assertThat("Verify winrate", tradingPage.getPerformanceOverviewWinrateBySymbol("EURUSD"), equalTo(calculateWinrate(tsBySymbolDaily3, tsBySymbolDaily4)));
        assertThat("Verify winrate", tradingPage.getPerformanceOverviewWinrateBySymbol("CZKAED"), equalTo(calculateWinrate(tsBySymbolDaily8)));
        assertThat("Verify winrate", tradingPage.getPerformanceOverviewWinrateBySymbol("AEDCZK"), equalTo(calculateWinrate(tsBySymbolDaily7)));
        assertThat("Verify winrate", tradingPage.getPerformanceOverviewWinrateBySymbol("USDEUR"), equalTo(calculateWinrate(tsBySymbolDaily1, tsBySymbolDaily2)));
        assertThat("Verify winrate", tradingPage.getPerformanceOverviewWinrateBySymbol("GBPJPY"), equalTo(calculateWinrate(tsBySymbolDaily5, tsBySymbolDaily6)));
        // HFT
        assertThat("Verify HFT", tradingPage.getPerformanceOverviewHftBySymbol("EURUSD"), equalTo(calculateHft(tsBySymbolDaily3, tsBySymbolDaily4)));
        assertThat("Verify HFT", tradingPage.getPerformanceOverviewHftBySymbol("CZKAED"), equalTo(calculateHft(tsBySymbolDaily8)));
        assertThat("Verify HFT", tradingPage.getPerformanceOverviewHftBySymbol("AEDCZK"), equalTo(calculateHft(tsBySymbolDaily7)));
        assertThat("Verify HFT", tradingPage.getPerformanceOverviewHftBySymbol("USDEUR"), equalTo(calculateHft(tsBySymbolDaily1, tsBySymbolDaily2)));
        assertThat("Verify HFT", tradingPage.getPerformanceOverviewHftBySymbol("GBPJPY"), equalTo(calculateHft(tsBySymbolDaily5, tsBySymbolDaily6)));
        // PNL
        DecimalFormat formatter = new DecimalFormat("#,###.0");
        assertThat("Verify PNL", tradingPage.getPerformanceOverviewPnlBySymbol("EURUSD"), equalTo(formatter.format(tsBySymbolDaily3.totalPnl + tsBySymbolDaily4.totalPnl)));
        assertThat("Verify PNL", tradingPage.getPerformanceOverviewPnlBySymbol("CZKAED"), equalTo(formatter.format(tsBySymbolDaily8.totalPnl)));
        assertThat("Verify PNL", tradingPage.getPerformanceOverviewPnlBySymbol("AEDCZK"), equalTo(formatter.format(tsBySymbolDaily7.totalPnl)));
        assertThat("Verify PNL", tradingPage.getPerformanceOverviewPnlBySymbol("USDEUR"), equalTo(formatter.format(tsBySymbolDaily1.totalPnl + tsBySymbolDaily2.totalPnl)));
        assertThat("Verify PNL", tradingPage.getPerformanceOverviewPnlBySymbol("GBPJPY"), equalTo(formatter.format(tsBySymbolDaily5.totalPnl + tsBySymbolDaily6.totalPnl)));
    }

    private static String calculateWinrate(TsBySymbolDailyObject... objects) {
        List<Long> totalDealsList = Arrays.stream(objects).map(object -> object.countTotalDeals).toList();
        List<Long> winDealsList = Arrays.stream(objects).map(object -> object.countWinDeals).toList();
        return calculatePercentageFromList(winDealsList, totalDealsList);
    }

    private static String calculateHft(TsBySymbolDailyObject... objects) {
        List<Long> totalDealsList = Arrays.stream(objects).map(object -> object.countTotalDeals).toList();
        List<Long> winDealsList = Arrays.stream(objects).map(object -> object.countDealsLess10m).toList();
        return calculatePercentageFromList(winDealsList, totalDealsList);
    }

    @AfterAll
    public static void teardown() throws Exception {
        cleanCrmUserTableByClient(crmTbUser.ucid);
        deleteEntryFromDb(TS_BY_SYMBOL_DAILY_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        closeAlert(crmTbUser.ucid);
    }
}
