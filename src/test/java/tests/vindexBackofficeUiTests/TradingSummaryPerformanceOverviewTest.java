package tests.vindexBackofficeUiTests;

import businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObject;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.db.clickhouse.mtMt4TradesCoerced.MtMt4TradesCoercedObject;
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
import java.util.stream.Stream;

import static businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateUserByClient;
import static businessObjects.db.clickhouse.mtMt4TradesCoerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoerced;
import static businessObjects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.DateTimeFormat.DATE;
import static helpers.data.enums.DateTimeFormat.DATE_AND_TIME;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.CleanTableHelper.cleanCrmUserTableByClient;
import static helpers.database.CleanTableHelper.cleanMt4CoercedTableByUcid;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;
import static utils.Utils.getCurrentTimestampMinusOffsetFormatted;
import static utils.Utils.transformDate;

public class TradingSummaryPerformanceOverviewTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    private static final CrmTbAccountObject account = generateCrmTbAccountDataForUi(client);
    private static final MtMt4TradesCoercedObject trade1 = generateMt4TradesCoerced(client);
    private static final MtMt4TradesCoercedObject trade2 = generateMt4TradesCoerced(client);
    private static final MtMt4TradesCoercedObject trade3 = generateMt4TradesCoerced(client);
    private static final MtMt4TradesCoercedObject trade4 = generateMt4TradesCoerced(client);
    private static final MtMt4TradesCoercedObject trade5 = generateMt4TradesCoerced(client);
    private static final MtMt4TradesCoercedObject trade6 = generateMt4TradesCoerced(client);
    private static final MtMt4TradesCoercedObject trade7 = generateMt4TradesCoerced(client);
    private static final MtMt4TradesCoercedObject trade8 = generateMt4TradesCoerced(client);
    private static final MtMt4TradesCoercedObject trade9 = generateMt4TradesCoerced(client);
    private static final MtMt4TradesCoercedObject trade10 = generateMt4TradesCoerced(client);
    private static final MtMt4TradesCoercedObject trade11 = generateMt4TradesCoerced(client);
    private static final MtMt4TradesCoercedObject trade12 = generateMt4TradesCoerced(client);

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        trade1.symbol = "USDEUR";
        trade1.profitUsd = 1056d;
        trade1.storageUsd = 256d;
        trade1.commissionUsd = 111.33d;
        trade2.symbol = "USDEUR";
        trade2.profitUsd = 0d;
        trade2.storageUsd = 0d;
        trade2.commissionUsd = 0d;
        trade3.symbol = "USDEUR";
        trade3.profitUsd = -11.45d;
        trade3.storageUsd = 0d;
        trade3.commissionUsd = -96.4d;
        trade3.openTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 5);
        trade4.symbol = "EURUSD";
        trade4.profitUsd = 0d;
        trade4.storageUsd = 0d;
        trade4.commissionUsd = 0d;
        trade5.symbol = "EURUSD";
        trade5.profitUsd = 0d;
        trade5.storageUsd = 0d;
        trade5.commissionUsd = 0d;
        trade6.symbol = "EURUSD";
        trade6.profitUsd = 0d;
        trade6.storageUsd = 0d;
        trade6.commissionUsd = 0d;
        trade7.symbol = "GBPJPY";
        trade7.profitUsd = 1056d;
        trade7.storageUsd = 256d;
        trade7.commissionUsd = 111.33d;
        trade7.openTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 5);
        trade8.symbol = "GBPJPY";
        trade8.profitUsd = 1056d;
        trade8.storageUsd = 256d;
        trade8.commissionUsd = 11.33d;
        trade8.openTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 5);
        trade9.symbol = "GBPJPY";
        trade9.profitUsd = 1056d;
        trade9.storageUsd = 256d;
        trade9.commissionUsd = 111.33d;
        trade9.openTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 5);
        trade10.symbol = "AEDCZK";
        trade10.profitUsd = -1056d;
        trade10.storageUsd = -256d;
        trade10.commissionUsd = -111.33d;
        trade11.symbol = "CZKAED";
        trade12.symbol = "HRKEUR";
        trade12.profitUsd = -11_056d;
        trade12.storageUsd = -85_256d;
        trade12.commissionUsd = -111.33d;
        crmTbUser.registrationDate = transformDate(trade9.openTimeUtc, DATE_AND_TIME, DATE);
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, account);
        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade1, trade2, trade3, trade4, trade5, trade6, trade7, trade8, trade9, trade10, trade11, trade12));
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
        assertThat("Verify symbols", tradingPage.getPerformanceOverviewSymbols(), contains("GBPJPY", "USDEUR", "CZKAED", "EURUSD", "AEDCZK"));
        // Deals
        assertThat("Verify deals", tradingPage.getPerformanceOverviewDealsBySymbol("EURUSD"), equalTo("3"));
        assertThat("Verify deals", tradingPage.getPerformanceOverviewDealsBySymbol("CZKAED"), equalTo("1"));
        assertThat("Verify deals", tradingPage.getPerformanceOverviewDealsBySymbol("AEDCZK"), equalTo("1"));
        assertThat("Verify deals", tradingPage.getPerformanceOverviewDealsBySymbol("USDEUR"), equalTo("3"));
        assertThat("Verify deals", tradingPage.getPerformanceOverviewDealsBySymbol("GBPJPY"), equalTo("3"));
        // Winrate
        assertThat("Verify winrate", tradingPage.getPerformanceOverviewWinrateBySymbol("EURUSD"), equalTo("0%"));
        assertThat("Verify winrate", tradingPage.getPerformanceOverviewWinrateBySymbol("CZKAED"), equalTo("100%"));
        assertThat("Verify winrate", tradingPage.getPerformanceOverviewWinrateBySymbol("AEDCZK"), equalTo("0%"));
        assertThat("Verify winrate", tradingPage.getPerformanceOverviewWinrateBySymbol("USDEUR"), equalTo("33.33%"));
        assertThat("Verify winrate", tradingPage.getPerformanceOverviewWinrateBySymbol("GBPJPY"), equalTo("100%"));
        // HFT
        assertThat("Verify HFT", tradingPage.getPerformanceOverviewHftBySymbol("EURUSD"), equalTo("0%"));
        assertThat("Verify HFT", tradingPage.getPerformanceOverviewHftBySymbol("CZKAED"), equalTo("0%"));
        assertThat("Verify HFT", tradingPage.getPerformanceOverviewHftBySymbol("AEDCZK"), equalTo("0%"));
        assertThat("Verify HFT", tradingPage.getPerformanceOverviewHftBySymbol("USDEUR"), equalTo("33.33%"));
        assertThat("Verify HFT", tradingPage.getPerformanceOverviewHftBySymbol("GBPJPY"), equalTo("100%"));
        // PNL
        assertThat("Verify PNL", tradingPage.getPerformanceOverviewPnlBySymbol("EURUSD"), equalTo(calculateProfit(trade4, trade5, trade6)));
        assertThat("Verify PNL", tradingPage.getPerformanceOverviewPnlBySymbol("CZKAED"), equalTo(calculateProfit(trade11)));
        assertThat("Verify PNL", tradingPage.getPerformanceOverviewPnlBySymbol("AEDCZK"), equalTo(calculateProfit(trade10)));
        assertThat("Verify PNL", tradingPage.getPerformanceOverviewPnlBySymbol("USDEUR"), equalTo(calculateProfit(trade1, trade2, trade3)));
        assertThat("Verify PNL", tradingPage.getPerformanceOverviewPnlBySymbol("GBPJPY"), equalTo(calculateProfit(trade7, trade8, trade9)));
    }

    private static String calculateProfit(MtMt4TradesCoercedObject... trades) {
        DecimalFormat formatter = new DecimalFormat("#,###.#");
        formatter.setMinimumFractionDigits(0);
        formatter.setMaximumFractionDigits(2);
        return formatter.format(Stream.of(trades).mapToDouble(t -> t.profitUsd + t.storageUsd + t.commissionUsd).sum());
    }

    @AfterAll
    public static void teardown() throws Exception {
        cleanCrmUserTableByClient(crmTbUser.ucid);
        cleanMt4CoercedTableByUcid(client.getUcid());
        closeAlert(crmTbUser.ucid);
    }
}
