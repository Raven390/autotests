package tests.vindexBackofficeUiTests;

import businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObject;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.db.clickhouse.mtAccount.MtAccountObject;
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
import static businessObjects.db.clickhouse.mtAccount.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static businessObjects.db.clickhouse.mtMt4TradesCoerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoerced;
import static businessObjects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.DateTimeFormat.*;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.CleanTableHelper.cleanCrmUserTableByClient;
import static helpers.database.CleanTableHelper.cleanMt4CoercedTableByUcid;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.*;

public class TradingSummaryVolumeTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    DecimalFormat formatter = new DecimalFormat("#,###");
    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    private static final CrmTbAccountObject account = generateCrmTbAccountDataForUi(client);
    private static final MtAccountObject mtAccount = generateMtAccountByCrmTbAccount(account);
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
    private static final MtMt4TradesCoercedObject trade13 = generateMt4TradesCoerced(client);
    private static final MtMt4TradesCoercedObject trade14 = generateMt4TradesCoerced(client);
    private static final String MONTH_DAY_LABEL_PATTERN = "^(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec) \\d{2}$";
    private static final String WEEK_LABEL_PATTERN = "^(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec) \\d{2} - (Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec) \\d{2}$";
    private static final String MONTH_YEAR_LABEL_PATTERN = "^(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec) \\d{4}$";
    private static final String YEAR_LABEL_PATTERN = "^\\d{4}$";

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        trade14.notionalValueUsd = 3670.415;
        trade14.closeTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 38, 0, 0, 0);
        trade13.notionalValueUsd = 50_000d;
        trade13.closeTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 38, 0, 0, 0);
        trade12.notionalValueUsd = 3670.415;
        trade12.closeTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 254, 0, 0);
        trade11.notionalValueUsd = 70_000d;
        trade11.closeTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 254, 0, 0);
        trade10.notionalValueUsd = 3670.415;
        trade10.closeTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 62, 0, 0);
        trade9.notionalValueUsd = 30_000d;
        trade9.closeTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 62, 0, 0);
        trade8.notionalValueUsd = 2854.345;
        trade8.closeTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 8, 0, 0);
        trade7.notionalValueUsd = 884.243;
        trade7.closeTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 7, 0, 0);
        trade6.notionalValueUsd = 4224.867;
        trade6.closeTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 6, 0, 0);
        trade5.notionalValueUsd = 908.795;
        trade5.closeTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 5, 0, 0);
        trade4.notionalValueUsd = 1338.32;
        trade4.closeTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 4, 0, 0);
        trade3.notionalValueUsd = 6673.66;
        trade3.closeTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 3, 0, 0);
        trade2.notionalValueUsd = 10_212.975;
        trade2.closeTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 1, 0, 0);
        trade1.notionalValueUsd = 3670.415;
        trade1.closeTime = getCurrentTimestampDbFormat();
        crmTbUser.registrationDate = getCurrentTimestampMinusOffsetFormatted(DATE, 0, 0, 8, 0, 0);
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, account);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, mtAccount);
        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade1, trade2, trade3, trade4, trade5, trade6, trade7, trade8));
        RuleAlert alert = generateRuleAlertByUcid(crmTbUser.ucid);
        kafka.produceMessage(alert.alertId, objectMapper.writeValueAsString(alert), KAFKA_TOPIC_ALERTS);
    }

    @Order(1)
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("909")
    @DisplayName("Verify Volume chart in Trading - Summary by days")
    public void verifyTradingSummaryVolume1Test() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        tradingPage.openTradingTab();
        tradingPage.openSummaryTab();
        assertThat("Verify Volume chart title", tradingPage.getVolumeChartTitle(), is("VolumeUSD"));
        assertThat("Verify Volume Y axis label", tradingPage.getVolumeYAxisLabel(), is("15k"));
        String maxVolumeDate = transformDate(trade2.closeTime, DATE_AND_TIME, MONTH_TEXT_AND_DAY);
        String maxVolume = formatter.format(trade2.notionalValueUsd);
        String totalVolume = formatter.format(Stream.of(trade1, trade2, trade3, trade4, trade5, trade6, trade7, trade8).mapToDouble(t -> t.notionalValueUsd).sum());
        assertThat("Verify Volume max value", tradingPage.getVolumeMaxValue(), is(maxVolume));
        assertThat("Verify Volume max label", tradingPage.getVolumeMaxLabel(), is("Max"));
        assertThat("Verify Volume max date", tradingPage.getVolumeMaxDate(), is(maxVolumeDate));
        assertThat("Verify Volume total value", tradingPage.getVolumeTotalValue(), is(totalVolume));
        assertThat("Verify Volume total label", tradingPage.getVolumeTotalLabel(), is("Total"));
        assertThat("Verify Volume max volume graph dot value", tradingPage.getVolumeMaxGraphDot(), is(maxVolume));
        assertThat("Verify Volume x axis labels match expected pattern", tradingPage.getVolumeXAxisLabels(), everyItem(matchesPattern(MONTH_DAY_LABEL_PATTERN)));
    }

    @Order(2)
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1056")
    @DisplayName("Verify Volume chart in Trading - Summary by weeks")
    public void verifyTradingSummaryVolume2Test() throws Exception {
        cleanCrmUserTableByClient(crmTbUser.ucid);
        crmTbUser.registrationDate = getCurrentTimestampMinusOffsetFormatted(DATE, 0, 0, 62, 0, 0);
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade9, trade10));
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        tradingPage.openTradingTab();
        tradingPage.openSummaryTab();
        assertThat("Verify Volume chart title", tradingPage.getVolumeChartTitle(), is("VolumeUSD"));
        assertThat("Verify Volume Y axis label", tradingPage.getVolumeYAxisLabel(), is("35k"));
        String maxVolumeDate = transformDate(trade9.closeTime, DATE_AND_TIME, MONTH_TEXT_AND_YEAR);
        String maxVolume = formatter.format(Stream.of(trade9, trade10).mapToDouble(t -> t.notionalValueUsd).sum());
        String totalVolume = formatter.format(Stream.of(trade1, trade2, trade3, trade4, trade5, trade6, trade7, trade8, trade9, trade10).mapToDouble(t -> t.notionalValueUsd).sum());
        assertThat("Verify Volume max value", tradingPage.getVolumeMaxValue(), is(maxVolume));
        assertThat("Verify Volume max label", tradingPage.getVolumeMaxLabel(), is("Max"));
        assertThat("Verify Volume max date", tradingPage.getVolumeMaxDate(), matchesPattern(WEEK_LABEL_PATTERN));
        assertThat("Verify Volume total value", tradingPage.getVolumeTotalValue(), is(totalVolume));
        assertThat("Verify Volume total label", tradingPage.getVolumeTotalLabel(), is("Total"));
        assertThat("Verify Volume max volume graph dot value", tradingPage.getVolumeMaxGraphDot(), is(maxVolume));
        assertThat("Verify Volume x axis labels match expected pattern", tradingPage.getVolumeXAxisLabels(), everyItem(matchesPattern(MONTH_DAY_LABEL_PATTERN)));
    }

    @Order(3)
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("910")
    @DisplayName("Verify Volume chart in Trading - Summary by months")
    public void verifyTradingSummaryVolume3Test() throws Exception {
        cleanCrmUserTableByClient(crmTbUser.ucid);
        crmTbUser.registrationDate = getCurrentTimestampMinusOffsetFormatted(DATE, 0, 0, 254, 0, 0);
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade11, trade12));
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        tradingPage.openTradingTab();
        tradingPage.openSummaryTab();
        assertThat("Verify Volume chart title", tradingPage.getVolumeChartTitle(), is("VolumeUSD"));
        assertThat("Verify Volume Y axis label", tradingPage.getVolumeYAxisLabel(), is("75k"));
        String maxVolumeDate = transformDate(trade11.closeTime, DATE_AND_TIME, MONTH_TEXT_AND_YEAR);
        String maxVolume = formatter.format(Stream.of(trade11, trade12).mapToDouble(t -> t.notionalValueUsd).sum());
        String totalVolume = formatter.format(Stream.of(trade1, trade2, trade3, trade4, trade5, trade6, trade7, trade8, trade9, trade10, trade11, trade12).mapToDouble(t -> t.notionalValueUsd).sum());
        assertThat("Verify Volume max value", tradingPage.getVolumeMaxValue(), is(maxVolume));
        assertThat("Verify Volume max label", tradingPage.getVolumeMaxLabel(), is("Max"));
        assertThat("Verify Volume max date", tradingPage.getVolumeMaxDate(), is(maxVolumeDate));
        assertThat("Verify Volume total value", tradingPage.getVolumeTotalValue(), is(totalVolume));
        assertThat("Verify Volume total label", tradingPage.getVolumeTotalLabel(), is("Total"));
        assertThat("Verify Volume max volume graph dot value", tradingPage.getVolumeMaxGraphDot(), is(maxVolume));
        assertThat("Verify Volume x axis labels match expected pattern", tradingPage.getVolumeXAxisLabels(), everyItem(matchesPattern(MONTH_YEAR_LABEL_PATTERN)));
    }

    @Order(4)
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("911")
    @DisplayName("Verify Volume chart in Trading - Summary by years")
    public void verifyTradingSummaryVolume4Test() throws Exception {
        cleanCrmUserTableByClient(crmTbUser.ucid);
        crmTbUser.registrationDate = getCurrentTimestampMinusOffsetFormatted(DATE, 0, 38, 0, 0, 0);
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade13, trade14));
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        tradingPage.openTradingTab();
        tradingPage.openSummaryTab();
        assertThat("Verify Volume chart title", tradingPage.getVolumeChartTitle(), is("VolumeUSD"));
        assertThat("Verify Volume Y axis label", tradingPage.getVolumeYAxisLabel(), is("150k"));
        String maxVolumeDate = transformDate(trade11.closeTime, DATE_AND_TIME, YEAR);
        String maxVolume = formatter.format(Stream.of(trade9, trade10, trade11, trade12).mapToDouble(t -> t.notionalValueUsd).sum());
        String totalVolume = formatter.format(Stream.of(trade1, trade2, trade3, trade4, trade5, trade6, trade7, trade8, trade9, trade10, trade11, trade12, trade13, trade14).mapToDouble(t -> t.notionalValueUsd).sum());
        assertThat("Verify Volume max value", tradingPage.getVolumeMaxValue(), is(maxVolume));
        assertThat("Verify Volume max label", tradingPage.getVolumeMaxLabel(), is("Max"));
        assertThat("Verify Volume max date", tradingPage.getVolumeMaxDate(), is(maxVolumeDate));
        assertThat("Verify Volume total value", tradingPage.getVolumeTotalValue(), is(totalVolume));
        assertThat("Verify Volume total label", tradingPage.getVolumeTotalLabel(), is("Total"));
        assertThat("Verify Volume max volume graph dot value", tradingPage.getVolumeMaxGraphDot(), is(maxVolume));
        assertThat("Verify Volume x axis labels match expected pattern", tradingPage.getVolumeXAxisLabels(), everyItem(matchesPattern(YEAR_LABEL_PATTERN)));
    }

    @AfterAll
    public static void teardown() throws Exception {
        cleanCrmUserTableByClient(crmTbUser.ucid);
        cleanMt4CoercedTableByUcid(client.getUcid());
        closeAlert(crmTbUser.ucid);
    }
}
