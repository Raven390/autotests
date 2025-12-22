package tests.vindex_backoffice_ui_tests.investigationTool.trading;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateAdditionalStaticCrmTbAccountActive;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateStaticCrmTbAccountActive;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoercedRandomized;
import static helpers.data.enums.DateTimeFormat.*;
import static helpers.database.DbHelper.insertObjectToDb;
import static helpers.database.DbHelper.insertObjectsToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;
import static utils.Utils.*;
import static utils.Utils.getCurrentDate;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import helpers.data.ClientHelper;
import helpers.data.enums.Brand;
import helpers.data.enums.DateTimeFormat;
import helpers.data.enums.Regulator;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseWeb;

public class TradingSummaryVolumeActivityTest extends TestBaseWeb {

    private static final ClientHelper client;

    static {
        client = ClientHelper.builder()
                .userId(202_007)
                .uid("e5880ca5-8578-4a1e-969d-7a64716ca41f")
                .brand(Brand.INFINOX)
                .regulator(Regulator.FCA)
                .tradingAccount(202_007_001)
                .tradingAccount2(202_007_002)
                .serverId(42)
                .build();
    }

    private static CrmTbUserObject crmTbUser = generateStaticUserByClient(client);
    private static CrmTbAccountObject account1 = generateStaticCrmTbAccountActive(client);
    private static CrmTbAccountObject account2 = generateAdditionalStaticCrmTbAccountActive(client);
    private static MtAccountObject mtAccount1 = generateMtAccountByCrmTbAccount(account1);
    private static MtAccountObject mtAccount2 = generateMtAccountByCrmTbAccount(account2);

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        crmTbUser.firstName = "Volumen";
        crmTbUser.lastName = "Active";
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertCrmAccountsToDb(account1, account2);
        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccount1, mtAccount2));
    }

    @Test
    @AllureId("986")
    @Feature("BMS-55 Trading summary - Volume + activity + filters")
    @DisplayName("Trading/Summary. User can manipulate timeline by drag")
    public void manipulateTimelineByDragTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        paymentsPage.selectDateFilter("Last 7 days");
        paymentsPage.shiftRightTimelineThumbToPreLastTimelineSection();
        paymentsPage.checkLastTimelineSectionInactive();
        paymentsPage.shiftLeftTimelineThumbToTimelineSectionIndex(2);
        paymentsPage.checkTimelineSectionInactive(0);
    }

    @Test
    @AllureId("987")
    @Feature("BMS-55 Trading summary - Volume + activity + filters")
    @DisplayName("Trading/Summary. User can manipulate timeline by click")
    public void manipulateTimelineByClickTest() throws InterruptedException {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        paymentsPage.selectDateFilter("Last 7 days");
        paymentsPage.clickOnPreLastTimelineSection();
        paymentsPage.checkLastTimelineSectionInactive();
        paymentsPage.clickOnTimelineSectionByIndex(1);
        paymentsPage.checkTimelineSectionInactive(0);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("988")
    @Feature("BMS-55 Trading summary - Volume + activity + filters")
    @DisplayName(
            "Trading/Summary. When user filters 1-7 days one division on timeline is 1 day with date under each section")
    public void filterLegend1And7DaysTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        Allure.step("filter one day");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getCurrentDate());
        paymentsPage.checkTimelineSectionVisibleByDate(getCurrentDateMonthDay());
        page.reload();
        Allure.step("filter seven days");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(6));
        paymentsPage.checkTimelineSectionVisibleByDate(getCurrentDateMonthDay());
        paymentsPage.checkTimelineSectionVisibleByDate(getPreviousDayMonthDayByIntDay(6));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("989")
    @Feature("BMS-55 Trading summary - Volume + activity + filters")
    @DisplayName(
            "Trading/Summary. When user filters 8-98 days one division on timeline is 1 Division = 1 day annotation = Days MON DD")
    public void filterLegend8And31DaysTest() throws ParseException {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        Allure.step("filter 8 day");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(7));
        paymentsPage.checkTimelineAnnotationInFormat(MONTH_TEXT_AND_DAY);
        page.reload();
        Allure.step("filter 98 days");
        paymentsPage.selectDatesInCalendar(getPreviousDayByIntDaysYearMonthDay(97), getCurrentDate());
        paymentsPage.checkTimelineAnnotationInFormat(DateTimeFormat.MONTH_TEXT_AND_DAY);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("990")
    @Feature("BMS-55 Trading summary - Volume + activity + filters")
    @DisplayName(
            "Trading/Summary. When user filters 99-365 days one division on timeline is 1 Division = 1 day annotation = Days MON DD")
    public void filterLegend31And98DaysTest() throws ParseException {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        Allure.step("filter 99 day");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(98));
        paymentsPage.checkTimelineAnnotationInFormat(MONTH_TEXT_AND_DAY);
        page.reload();
        Allure.step("filter 365 days");
        paymentsPage.selectDatesInCalendar(getPreviousDayByIntDaysYearMonthDay(363), getCurrentDate());
        paymentsPage.checkTimelineAnnotationInFormat(DAY_SHORT_MONTH_YEAR);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("991")
    @Feature("BMS-55 Trading summary - Volume + activity + filters")
    @DisplayName("Trading/Summary. When user filters 1-6 years one Division = 1 week annotation = DD MON YYYY")
    public void filterLegend98DaysAnd3YearTest() throws ParseException {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        Allure.step("filter 366 days");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(366));
        paymentsPage.checkTimelineAnnotationInFormat(MONTH_TEXT_AND_YEAR);
        page.reload();
        Allure.step("filter 6 years");
        paymentsPage.selectDatesInCalendar(
                getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, 0, 0, 364 * 6, 0, 0), getCurrentDate());
        paymentsPage.checkTimelineAnnotationInFormat(MONTH_TEXT_AND_YEAR);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("992")
    @Feature("BMS-55 Trading summary - Volume + activity + filters")
    @DisplayName(
            "Trading/Summary. When user filters 6+ years division on timeline is 1 year with eek annotation = YYYY")
    public void filterLegend3YearsTest() throws ParseException {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        Allure.step("filter 7 years");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getPreviousYearByIntYearMonthDay(7));
        paymentsPage.checkTimelineAnnotationInFormat(YEAR);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("993")
    @Feature("BMS-55 Trading summary - Volume + activity + filters")
    @DisplayName(
            "Trading/Summary. When user uses timeline , when user filters 6 days must have 1 inactive day on the right.")
    public void timelineInactiveDaysFilter6DaysTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        Allure.step("filter 60 days");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(5));
        paymentsPage.checkTimelineSectionInactive(6);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("994")
    @Feature("BMS-55 Trading summary - Volume + activity + filters")
    @DisplayName(
            "Trading/Summary. When user uses timeline , when user filters 5 days must have 1 inactive day on both sides")
    public void timelineInactiveDaysFilter5DaysTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        Allure.step("filter 5 days");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(4));
        paymentsPage.checkTimelineSectionInactive(6);
        paymentsPage.checkTimelineSectionInactive(0);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("995")
    @Feature("BMS-55 Trading summary - Volume + activity + filters")
    @DisplayName(
            "Trading/Summary. When user uses timeline , when user filters 4 days must have 1 inactive day on the left and 2 on the right.")
    public void timelineInactiveDaysFilter4DaysTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        Allure.step("filter 4 days");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(3));
        paymentsPage.checkTimelineSectionInactive(6);
        paymentsPage.checkTimelineSectionInactive(5);
        paymentsPage.checkTimelineSectionInactive(0);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("996")
    @Feature("BMS-55 Trading summary - Volume + activity + filters")
    @DisplayName(
            "Trading/Summary. When user uses timeline , when user filters three days must have 2 inactive days on both sides")
    public void timelineInactiveDaysFilter3DaysTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        Allure.step("filter 3 days");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(2));
        paymentsPage.checkTimelineSectionInactive(6);
        paymentsPage.checkTimelineSectionInactive(5);
        paymentsPage.checkTimelineSectionInactive(0);
        paymentsPage.checkTimelineSectionInactive(1);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("997")
    @Feature("BMS-55 Trading summary - Volume + activity + filters")
    @DisplayName(
            "Trading/Summary. When user uses timeline , when user filters two days must have 2 inactive days on the left and 3 on the right")
    public void timelineInactiveDaysFilter2DaysTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        Allure.step("filter 2 days");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(1));
        paymentsPage.checkTimelineSectionInactive(6);
        paymentsPage.checkTimelineSectionInactive(5);
        paymentsPage.checkTimelineSectionInactive(4);
        paymentsPage.checkTimelineSectionInactive(0);
        paymentsPage.checkTimelineSectionInactive(1);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("998")
    @Feature("BMS-55 Trading summary - Volume + activity + filters")
    @DisplayName(
            "Trading/Summary. When user uses timeline , when user filters one day must have 3 inactive days on both sides")
    public void timelineInactiveDaysFilter1DayTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        Allure.step("filter 1 day");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getCurrentDate());
        paymentsPage.checkTimelineSectionInactive(6);
        paymentsPage.checkTimelineSectionInactive(5);
        paymentsPage.checkTimelineSectionInactive(4);
        paymentsPage.checkTimelineSectionInactive(0);
        paymentsPage.checkTimelineSectionInactive(1);
        paymentsPage.checkTimelineSectionInactive(2);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("999")
    @Feature("BMS-55 Trading summary - Volume + activity + filters")
    @DisplayName(
            "Trading/Summary. When user uses timeline , user can switch timeline between display activity or volume")
    public void timelineSwitchVolumeActivity() throws SQLException, InterruptedException {

        tradingPage.deleteClientDeals(client.getUcid());

        Allure.step("create a three small deals for one day");

        MtMt4TradesCoercedObject trade0 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade1 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade2 = generateMt4TradesCoercedRandomized(client);
        trade0.setNotionalValueUsd(10.0);
        trade1.setNotionalValueUsd(10.0);
        trade2.setNotionalValueUsd(10.0);

        trade0.setOpenTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 1, 0, 0, 1));
        trade0.setCloseTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 1, 0, 0, 0));
        trade1.setOpenTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 1, 0, 0, 1));
        trade1.setCloseTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 1, 0, 0, 0));
        trade2.setOpenTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 1, 0, 0, 1));
        trade2.setCloseTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 1, 0, 0, 0));

        Allure.step(
                "create a one big deals for another day notionalValueUsd of which will be 3 times bigger than sum of first three");

        MtMt4TradesCoercedObject trade3 = generateMt4TradesCoercedRandomized(client);
        trade3.setNotionalValueUsd(90.0);
        trade3.setOpenTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 0, 1));
        trade3.setCloseTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 0, 0));

        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade0, trade1, trade2, trade3));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        tradingPage.enableViewAmount();
        Allure.step("filter 2 days");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(1));
        paymentsPage.clickVolumeButtonTimeline();
        paymentsPage.checkStileValueOfTimelineBar(2, "height: 33.3333%; min-height: 2px;");
        paymentsPage.checkStileValueOfTimelineBar(3, "height: 100%; min-height: 2px;");
        paymentsPage.clickActivityButtonTimeline();
        paymentsPage.checkStileValueOfTimelineBar(2, "height: 100%; min-height: 2px;");
        paymentsPage.checkStileValueOfTimelineBar(3, "height: 33.3333%; min-height: 2px;");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1000")
    @Feature("BMS-55 Trading summary - Volume + activity + filters")
    @DisplayName("Trading/Summary. User can filter operations by Dates Last 1 year")
    public void filterLastYearTest() throws Exception {
        tradingPage.deleteClientDeals(client.getUcid());
        Allure.step("add to DB two transaction inside the test period and one outside of the test period");

        MtMt4TradesCoercedObject trade0 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade1 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade2 = generateMt4TradesCoercedRandomized(client);
        trade1.setOpenTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 1, -1, 0, 0, 0, 1));
        trade1.setCloseTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 1, -1, 0, 0, 0, 0));
        trade2.setOpenTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 1, 0, 0, 0, 0, 0));
        trade2.setCloseTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 1, 0, 0, 0, 0, 1));
        trade1.setSymbol("EURUSD");
        trade2.setSymbol("EURUSD");
        trade0.setSymbol("EURUSD");

        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade0, trade1, trade2));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        Allure.step("filter test date");
        paymentsPage.selectDateFilter("Last 1 year");
        Allure.step("check that only data for the test date is displayed");
        assertThat("Verify deals", tradingPage.getPerformanceOverviewTicketsBySymbol("EURUSD"), equalTo("2"));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1001")
    @Feature("BMS-55 Trading summary - Volume + activity + filters")
    @DisplayName("Trading/Summary. User can filter operations by Dates Last 30 days")
    public void filterLast30DaysTest() throws Exception {
        tradingPage.deleteClientDeals(client.getUcid());
        Allure.step("add to DB two transaction inside the test period and one outside of the test period");

        MtMt4TradesCoercedObject trade0 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade1 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade2 = generateMt4TradesCoercedRandomized(client);
        trade1.setOpenTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 29, 0, 0, 1));
        trade1.setCloseTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 29, 0, 0, 0));
        trade2.setOpenTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 30, 0, 0, 0));
        trade2.setCloseTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 30, 0, 0, 1));
        trade1.setSymbol("EURUSD");
        trade2.setSymbol("EURUSD");
        trade0.setSymbol("EURUSD");

        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade0, trade1, trade2));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        Allure.step("filter test date");
        paymentsPage.selectDateFilter("Last 30 days");
        Allure.step("check that only data for the test date is displayed");
        assertThat("Verify deals", tradingPage.getPerformanceOverviewTicketsBySymbol("EURUSD"), equalTo("2"));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1002")
    @Feature("BMS-55 Trading summary - Volume + activity + filters")
    @DisplayName("Trading/Summary. User can filter operations by Dates Last 6 months")
    public void filterLast6MonthsTest() throws Exception {
        tradingPage.deleteClientDeals(client.getUcid());
        Allure.step("add to DB two transaction inside the test period and one outside of the test period");

        MtMt4TradesCoercedObject trade0 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade1 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade2 = generateMt4TradesCoercedRandomized(client);
        trade1.setOpenTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 5, 0, 0, 0, 1));
        trade1.setCloseTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 5, 0, 0, 0, 0));
        trade2.setOpenTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 6, 0, 0, 0, 0));
        trade2.setCloseTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 6, 0, 0, 0, 1));
        trade1.setSymbol("EURUSD");
        trade2.setSymbol("EURUSD");
        trade0.setSymbol("EURUSD");

        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade0, trade1, trade2));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        Allure.step("filter test date");
        paymentsPage.selectDateFilter("Last 6 months");
        Allure.step("check that only data for the test date is displayed");
        assertThat("Verify deals", tradingPage.getPerformanceOverviewTicketsBySymbol("EURUSD"), equalTo("2"));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1003")
    @Feature("BMS-55 Trading summary - Volume + activity + filters")
    @DisplayName("Trading/Summary. User can filter operations by Dates Last 7 days")
    public void filterLast7DaysTest() throws Exception {
        tradingPage.deleteClientDeals(client.getUcid());
        Allure.step("add to DB two transaction inside the test period and one outside of the test period");

        MtMt4TradesCoercedObject trade0 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade1 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade2 = generateMt4TradesCoercedRandomized(client);
        trade1.setOpenTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 6, 0, 0, 1));
        trade1.setCloseTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 6, 0, 0, 0));
        trade1.setSymbol("EURUSD");
        trade2.setSymbol("EURUSD");
        trade0.setSymbol("EURUSD");
        trade2.setOpenTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 7, 0, 0, 0));
        trade2.setCloseTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 7, 0, 0, 1));

        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade0, trade1, trade2));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        Allure.step("filter test date");
        paymentsPage.selectDateFilter("Last 7 days");
        Allure.step("check that only data for the test date is displayed");
        assertThat("Verify deals", tradingPage.getPerformanceOverviewTicketsBySymbol("EURUSD"), equalTo("2"));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1004")
    @Feature("BMS-55 Trading summary - Volume + activity + filters")
    @DisplayName("Trading/Summary. User can filter operations by Dates Last 90 days")
    public void filterLast90DaysTest() throws Exception {
        tradingPage.deleteClientDeals(client.getUcid());
        Allure.step("add to DB two transaction inside the test period and one outside of the test period");

        MtMt4TradesCoercedObject trade0 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade1 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade2 = generateMt4TradesCoercedRandomized(client);
        trade1.setOpenTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 89, 0, 0, 1));
        trade1.setCloseTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 89, 0, 0, 0));
        trade2.setOpenTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 90, 0, 0, 0));
        trade2.setCloseTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 90, 0, 0, 1));
        trade1.setSymbol("EURUSD");
        trade2.setSymbol("EURUSD");
        trade0.setSymbol("EURUSD");

        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade0, trade1, trade2));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        Allure.step("filter test date");
        paymentsPage.selectDateFilter("Last 90 days");
        Allure.step("check that only data for the test date is displayed");
        assertThat("Verify deals", tradingPage.getPerformanceOverviewTicketsBySymbol("EURUSD"), equalTo("2"));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1005")
    @Feature("BMS-55 Trading summary - Volume + activity + filters")
    @DisplayName("Trading/Summary. User can filter operations by Dates Custom - one day")
    public void filterCustomOneDayTest() throws Exception {
        tradingPage.deleteClientDeals(client.getUcid());
        Allure.step("add to DB two transaction inside the test period and one outside of the test period");

        MtMt4TradesCoercedObject trade0 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade1 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade2 = generateMt4TradesCoercedRandomized(client);
        trade1.setSymbol("EURUSD");
        trade2.setSymbol("EURUSD");
        trade0.setSymbol("EURUSD");
        trade1.setOpenTime("2024-02-13 14:09:35");
        trade1.setCloseTime("2024-02-13 14:09:36");
        trade2.setOpenTime("2024-02-13 15:09:33");
        trade2.setCloseTime("2024-02-13 15:09:34");

        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade0, trade1, trade2));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        Allure.step("filter test date");
        paymentsPage.selectDatesInCalendar("2024-02-13", "2024-02-13");
        Allure.step("check that only data for the test date is displayed");
        assertThat("Verify deals", tradingPage.getPerformanceOverviewTicketsBySymbol("EURUSD"), equalTo("2"));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1006")
    @Feature("BMS-55 Trading summary - Volume + activity + filters")
    @DisplayName("Trading/Summary. User can filter operations by account")
    public void filterAccountTest() throws Exception {
        tradingPage.deleteClientDeals(client.getUcid());
        Allure.step("add to DB two transaction inside the test period and one outside of the test period");

        MtMt4TradesCoercedObject trade0 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade1 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade2 = generateMt4TradesCoercedRandomized(client);
        trade1.setSymbol("EURUSD");
        trade2.setSymbol("EURUSD");
        trade0.setSymbol("EURUSD");

        trade2.setAccount(((long) account2.account));

        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade0, trade1, trade2));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        Allure.step("filter test account");
        Allure.step("check that only data for the selected account is displayed");
        paymentsPage.clickOnAccountSelection();
        paymentsPage.selectTradingAccount(account1.account);
        assertThat("Verify deals", tradingPage.getPerformanceOverviewTicketsBySymbol("EURUSD"), equalTo("2"));
        paymentsPage.clearSelectedTradingAccount();
        paymentsPage.selectTradingAccount(account2.account);
        assertThat("Verify deals", tradingPage.getPerformanceOverviewTicketsBySymbol("EURUSD"), equalTo("1"));
    }
}
