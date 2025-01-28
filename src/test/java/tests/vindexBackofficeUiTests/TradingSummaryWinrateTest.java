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
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.CleanTableHelper.cleanCrmUserTableByClient;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;

public class TradingSummaryWinrateTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    private static final CrmTbAccountObject account = generateCrmTbAccountDataForUi(client);
    private static final TsBySymbolDailyObject tsBySymbolDaily1 = generateTsBySymbolDailyByClient(client);
    private static final TsBySymbolDailyObject tsBySymbolDaily2 = generateTsBySymbolDailyByClient(client);

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, account);
        RuleAlert alert = generateRuleAlertByUcid(crmTbUser.ucid);
        kafka.produceMessage(alert.alertId, objectMapper.writeValueAsString(alert), KAFKA_TOPIC_ALERTS);
    }

    @AfterEach
    public void teardownEach() throws SQLException {
        deleteEntryFromDb(TS_BY_SYMBOL_DAILY_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("870")
    @DisplayName("Verify Winrate widget 100% in Trading - Summary")
    public void verifyTradingSummaryWinrate1Test() throws ReflectiveOperationException, SQLException {
        tsBySymbolDaily1.countTotalDeals = 1L;
        tsBySymbolDaily1.countWinDeals = 1L;
        insertObjectToDb(TS_BY_SYMBOL_DAILY_TABLE_NAME, tsBySymbolDaily1);
        investigationPage.navigateToClient(crmTbUser.ucid);
        keycloackPage.loginAsCoreUser();
        alertsPage.waitForPageToLoad();
        tradingPage.openTradingTab();
        tradingPage.openSummaryTab();
        assertThat("Verify value", tradingPage.getWinrateWidgetValue(), equalTo("100%"));
        DecimalFormat format = new DecimalFormat("#,###");
        assertThat("Verify info", tradingPage.getWinrateWidgetInfo(), equalTo(String.format("on %s operation", format.format(tsBySymbolDaily1.countTotalDeals))));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("871")
    @DisplayName("Verify Winrate widget 0% in Trading - Summary")
    public void verifyTradingSummaryWinrate2Test() throws ReflectiveOperationException, SQLException {
        tsBySymbolDaily1.countTotalDeals = 13213L;
        tsBySymbolDaily1.countWinDeals = 0L;
        tsBySymbolDaily2.symbol = "GBPJPY";
        tsBySymbolDaily2.countTotalDeals = 31321L;
        tsBySymbolDaily2.countWinDeals = 0L;
        insertObjectsToDb(TS_BY_SYMBOL_DAILY_TABLE_NAME, List.of(tsBySymbolDaily1, tsBySymbolDaily2));
        investigationPage.navigateToClient(crmTbUser.ucid);
        keycloackPage.loginAsCoreUser();
        alertsPage.waitForPageToLoad();
        tradingPage.openTradingTab();
        tradingPage.openSummaryTab();
        assertThat("Verify value", tradingPage.getWinrateWidgetValue(), equalTo("0%"));
        DecimalFormat format = new DecimalFormat("#,###");
        assertThat("Verify info", tradingPage.getWinrateWidgetInfo(), equalTo(String.format("on %s operations", format.format(tsBySymbolDaily1.countTotalDeals + tsBySymbolDaily2.countTotalDeals))));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("872")
    @DisplayName("Verify Winrate widget 66.67% in Trading - Summary")
    public void verifyTradingSummaryWinrate3Test() throws ReflectiveOperationException, SQLException {
        tsBySymbolDaily1.countTotalDeals = 9L;
        tsBySymbolDaily1.countWinDeals = 6L;
        tsBySymbolDaily2.symbol = "GBPJPY";
        tsBySymbolDaily2.countTotalDeals = 9L;
        tsBySymbolDaily2.countWinDeals = 6L;
        insertObjectsToDb(TS_BY_SYMBOL_DAILY_TABLE_NAME, List.of(tsBySymbolDaily1, tsBySymbolDaily2));
        investigationPage.navigateToClient(crmTbUser.ucid);
        keycloackPage.loginAsCoreUser();
        alertsPage.waitForPageToLoad();
        tradingPage.openTradingTab();
        tradingPage.openSummaryTab();
        assertThat("Verify value", tradingPage.getWinrateWidgetValue(), equalTo("66.67%"));
        DecimalFormat format = new DecimalFormat("#,###");
        assertThat("Verify info", tradingPage.getWinrateWidgetInfo(), equalTo(String.format("on %s operations", format.format(tsBySymbolDaily1.countTotalDeals + tsBySymbolDaily2.countTotalDeals))));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("873")
    @DisplayName("Verify Winrate widget no data in Trading - Summary")
    public void verifyTradingSummaryWinrate4Test() {
        investigationPage.navigateToClient(crmTbUser.ucid);
        keycloackPage.loginAsCoreUser();
        alertsPage.waitForPageToLoad();
        tradingPage.openTradingTab();
        tradingPage.openSummaryTab();
        assertThat("Verify value", tradingPage.getWinrateWidgetValue(), equalTo("No data"));
        assertThat("Verify info", tradingPage.getWinrateWidgetInfo(), equalTo("on 0 operations"));
    }

    @AfterAll
    public static void teardown() throws Exception {
        cleanCrmUserTableByClient(crmTbUser.ucid);
        deleteEntryFromDb(TS_BY_SYMBOL_DAILY_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        closeAlert(crmTbUser.ucid);
    }
}
