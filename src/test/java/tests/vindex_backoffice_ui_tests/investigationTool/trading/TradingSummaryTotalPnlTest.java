package tests.vindex_backoffice_ui_tests.investigationTool.trading;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.sql.SQLException;
import java.util.List;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoerced;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.DateTimeFormat.*;
import static helpers.database.CleanTableHelper.cleanCrmUserTableByClient;
import static helpers.database.CleanTableHelper.cleanMt4CoercedTableByUcid;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TradingSummaryTotalPnlTest extends TestBaseWeb {

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
    private static final String MONTH_DAY_LABEL_PATTERN = "^(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec) \\d{2}$";
    private static final String MONTH_YEAR_LABEL_PATTERN = "^(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec) \\d{4}$";

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        trade8.profitUsd = 2854.345;
        trade8.storageUsd = 0d;
        trade8.commissionUsd = 0d;
        trade8.closeTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 8, 0, 0);
        trade7.profitUsd = -884.243;
        trade7.storageUsd = 0d;
        trade7.commissionUsd = 0d;
        trade7.closeTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 7, 0, 0);
        trade6.profitUsd = -4224.867;
        trade6.storageUsd = 0d;
        trade6.commissionUsd = 0d;
        trade6.closeTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 6, 0, 0);
        trade5.profitUsd = 908.795;
        trade5.storageUsd = 0d;
        trade5.commissionUsd = 0d;
        trade5.closeTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 5, 0, 0);
        trade4.profitUsd = 1338.32;
        trade4.storageUsd = 0d;
        trade4.commissionUsd = 0d;
        trade4.closeTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 4, 0, 0);
        trade3.profitUsd = 6673.66;
        trade3.storageUsd = 0d;
        trade3.commissionUsd = 0d;
        trade3.closeTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 3, 0, 0);
        trade2.profitUsd = -10_212.975;
        trade2.storageUsd = 0d;
        trade2.commissionUsd = 0d;
        trade2.closeTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 1, 0, 0);
        trade1.profitUsd = 3670.415;
        trade1.storageUsd = 0d;
        trade1.commissionUsd = 0d;
        trade1.closeTime = getCurrentTimestampDbFormat();
        crmTbUser.registrationDate = getCurrentTimestampMinusOffsetFormatted(DATE, 0, 0, 8, 0, 0);
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertObjectToDb(CRM_TB_ACCOUNT_TABLE_NAME, account);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, mtAccount);
        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade1, trade2, trade3, trade4, trade5, trade6, trade7, trade8));
    }

    @Order(1)
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("868")
    @DisplayName("Verify Total PNL chart in Trading - Summary")
    public void verifyTradingSummaryTotalPnlTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        tradingPage.openTradingTab();
        tradingPage.openSummaryTab();
        assertThat("Verify Total PNL chart title", tradingPage.getTotalPnlChartTitle(), is("Realized PNLUSD"));
        assertThat("Verify Total PNL Y axis label", tradingPage.getTotalPnlYAxisLabel(), is("9K"));
        String maxProfitDate = transformDate(trade3.closeTime, DATE_AND_TIME, DAY_SHORT_MONTH_YEAR);
        String maxLossDate = transformDate(trade2.closeTime, DATE_AND_TIME, DAY_SHORT_MONTH_YEAR);
        String maxProfit = "6,666";
        String maxLoss = "-3,547";
        assertThat("Verify Total PNL max profit value", tradingPage.getTotalPnlMaxProfitValue(), is(maxProfit));
        assertThat("Verify Total PNL max profit label", tradingPage.getTotalPnlMaxProfitLabel(), is("Max profitable"));
        assertThat("Verify Total PNL max profit date", tradingPage.getTotalPnlMaxProfitDate(), is(maxProfitDate));
        assertThat("Verify Total PNL max loss value", tradingPage.getTotalPnlMaxLossValue(), is(maxLoss));
        assertThat("Verify Total PNL max loss label", tradingPage.getTotalPnlMaxLossLabel(), is("Max losing"));
        assertThat("Verify Total PNL max loss date", tradingPage.getTotalPnlMaxLossDate(), is(maxLossDate));
        assertThat("Verify Total PNL max profit graph dot value", tradingPage.getTotalPnlMaxProfitGraphDot(), is(maxProfit));
        assertThat("Verify Total PNL max loss graph dot value", tradingPage.getTotalPnlMaxLossGraphDot(), is(maxLoss));
        assertThat("Verify Total PNL x axis labels match expected pattern", tradingPage.getTotalPnlXAxisLabels(), everyItem(matchesPattern(MONTH_DAY_LABEL_PATTERN)));
    }

    @Order(2)
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("907")
    @DisplayName("Verify Total PNL chart in Trading - Summary by months")
    public void verifyTradingSummaryTotalPnl1Test() throws Exception {
        cleanCrmUserTableByClient(crmTbUser.ucid);
        crmTbUser.registrationDate = getCurrentTimestampMinusOffsetFormatted(DATE, 0, 0, 61, 0, 0);
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        tradingPage.openTradingTab();
        tradingPage.openSummaryTab();
        assertThat("Verify Total PNL chart title", tradingPage.getTotalPnlChartTitle(), equalTo("Realized PNLUSD"));
        assertThat("Verify Total PNL Y axis label", tradingPage.getTotalPnlYAxisLabel(), equalTo("9K"));
        String maxProfitDate = transformDate(trade3.closeTime, DATE_AND_TIME, DAY_SHORT_MONTH_YEAR);
        String maxLossDate = transformDate(trade2.closeTime, DATE_AND_TIME, DAY_SHORT_MONTH_YEAR);
        String maxProfit = "6,666";
        String maxLoss = "-3,547";
        assertThat("Verify Total PNL max profit value", tradingPage.getTotalPnlMaxProfitValue(), is(maxProfit));
        assertThat("Verify Total PNL max profit label", tradingPage.getTotalPnlMaxProfitLabel(), is("Max profitable"));
        assertThat("Verify Total PNL max profit date", tradingPage.getTotalPnlMaxProfitDate(), is(maxProfitDate));
        assertThat("Verify Total PNL max loss value", tradingPage.getTotalPnlMaxLossValue(), is(maxLoss));
        assertThat("Verify Total PNL max loss label", tradingPage.getTotalPnlMaxLossLabel(), is("Max losing"));
        assertThat("Verify Total PNL max loss date", tradingPage.getTotalPnlMaxLossDate(), is(maxLossDate));
        assertThat("Verify Total PNL max profit graph dot value", tradingPage.getTotalPnlMaxProfitGraphDot(), is(maxProfit));
        assertThat("Verify Total PNL max loss graph dot value", tradingPage.getTotalPnlMaxLossGraphDot(), is(maxLoss));
        assertThat("Verify Total PNL x axis labels match expected pattern", tradingPage.getTotalPnlXAxisLabels(), everyItem(matchesPattern(MONTH_YEAR_LABEL_PATTERN)));
    }

    @Order(3)
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("908")
    @DisplayName("Verify Total PNL chart in Trading - Summary by years")
    public void verifyTradingSummaryTotalPnl2Test() throws Exception {
        cleanCrmUserTableByClient(crmTbUser.ucid);
        crmTbUser.registrationDate = getCurrentTimestampMinusOffsetFormatted(DATE, 0, 37, 0, 0, 0);
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        tradingPage.openTradingTab();
        tradingPage.openSummaryTab();
        assertThat("Verify Total PNL chart title", tradingPage.getTotalPnlChartTitle(), equalTo("Realized PNLUSD"));
        assertThat("Verify Total PNL Y axis label", tradingPage.getTotalPnlYAxisLabel(), equalTo("9K"));
        String maxProfitDate = transformDate(trade3.closeTime, DATE_AND_TIME, DAY_SHORT_MONTH_YEAR);
        String maxLossDate = transformDate(trade2.closeTime, DATE_AND_TIME, DAY_SHORT_MONTH_YEAR);
        String maxProfit = "6,666";
        String maxLoss = "-3,547";
        assertThat("Verify Total PNL max profit value", tradingPage.getTotalPnlMaxProfitValue(), is(maxProfit));
        assertThat("Verify Total PNL max profit label", tradingPage.getTotalPnlMaxProfitLabel(), is("Max profitable"));
        assertThat("Verify Total PNL max profit date", tradingPage.getTotalPnlMaxProfitDate(), is(maxProfitDate));
        assertThat("Verify Total PNL max loss value", tradingPage.getTotalPnlMaxLossValue(), is(maxLoss));
        assertThat("Verify Total PNL max loss label", tradingPage.getTotalPnlMaxLossLabel(), is("Max losing"));
        assertThat("Verify Total PNL max loss date", tradingPage.getTotalPnlMaxLossDate(), is(maxLossDate));
        assertThat("Verify Total PNL max profit graph dot value", tradingPage.getTotalPnlMaxProfitGraphDot(), is(maxProfit));
        assertThat("Verify Total PNL max loss graph dot value", tradingPage.getTotalPnlMaxLossGraphDot(), is(maxLoss));
        assertThat("Verify Total PNL x axis labels match expected pattern", tradingPage.getTotalPnlXAxisLabels(), everyItem(matchesPattern(MONTH_YEAR_LABEL_PATTERN)));
    }

    @AfterAll
    public static void teardown() throws Exception {
        cleanCrmUserTableByClient(crmTbUser.ucid);
        cleanMt4CoercedTableByUcid(client.getUcid());
    }
}
