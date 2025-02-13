package tests.vindexBackofficeUiTests;

import businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObject;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.db.clickhouse.mtAccount.mtAccountObject;
import businessObjects.db.clickhouse.mtMt4TradesCoerced.MtMt4TradesCoercedObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import helpers.data.ClientHelper;
import helpers.data.enums.Brand;
import helpers.data.enums.DateTimeFormat;
import helpers.data.enums.Regulator;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseWeb;

import java.sql.SQLException;
import java.util.List;

import static businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObjectFactory.generateAdditionalStaticCrmTbAccountActive;
import static businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObjectFactory.generateStaticCrmTbAccountActive;
import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateStaticUserByClient;
import static businessObjects.db.clickhouse.mtAccount.mtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static businessObjects.db.clickhouse.mtMt4TradesCoerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoercedRandomized;
import static helpers.data.enums.DateTimeFormat.DATE_AND_TIME;
import static helpers.database.DbHelper.insertObjectToDb;
import static helpers.database.DbHelper.insertObjectsToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;
import static utils.Utils.*;
import static utils.Utils.getCurrentDate;

public class TradingSummaryVolumeActivityTest extends TestBaseWeb {

    private static ClientHelper client = new ClientHelper(202_007, "e5880ca5-8578-4a1e-969d-7a64716ca41f", Brand.INFINOX, Regulator.FCA, 202_007_001, 202_007_002, 42);
    private static CrmTbUserObject crmTbUser = generateStaticUserByClient(client);
    private static CrmTbAccountObject account1 = generateStaticCrmTbAccountActive(client);
    private static CrmTbAccountObject account2 = generateAdditionalStaticCrmTbAccountActive(client);
    private static mtAccountObject mtAccount1 = generateMtAccountByCrmTbAccount(account1);
    private static mtAccountObject mtAccount2 = generateMtAccountByCrmTbAccount(account2);

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        crmTbUser.firstName = "Volumen";
        crmTbUser.lastName = "Active";
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertObjectsToDb(CRM_ACCOUNT_TABLE_NAME, List.of(account1, account2));
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
        operationsPage.shiftRightTimelineThumbToPreLastTimelineSection();
        operationsPage.checkLastTimelineSectionInactive();
        operationsPage.shiftLeftTimelineThumbToTimelineSectionIndex(2);
        operationsPage.checkTimelineSectionInactive(0);
    }

    @Test
    @AllureId("987")
    @Feature("BMS-55 Trading summary - Volume + activity + filters")
    @DisplayName("Trading/Summary. User can manipulate timeline by click")
    public void manipulateTimelineByClickTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        operationsPage.clickOnPreLastTimelineSection();
        operationsPage.checkLastTimelineSectionInactive();
        operationsPage.clickOnTimelineSectionByIndex(1);
        operationsPage.checkTimelineSectionInactive(0);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("988")
    @Feature("BMS-55 Trading summary - Volume + activity + filters")
    @DisplayName("Trading/Summary. When user filters 1-7 days one division on timeline is 1 day with date under each section")
    public void filterLegend1And7DaysTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        Allure.step("filter one day");
        operationsPage.selectDatesInCalendar(getCurrentDate(), getCurrentDate());
        operationsPage.checkTimelineSectionVisibleByDate(getCurrentDateMonthDay());
        page.reload();
        Allure.step("filter seven days");
        operationsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(6));
        operationsPage.checkTimelineSectionVisibleByDate(getCurrentDateMonthDay());
        operationsPage.checkTimelineSectionVisibleByDate(getPreviousDayMonthDayByIntDay(6));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("989")
    @Feature("BMS-55 Trading summary - Volume + activity + filters")
    @DisplayName("Trading/Summary. When user filters 8-30 days one division on timeline is 1 day with date for every two days")
    public void filterLegend8And31DaysTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        Allure.step("filter 8 day");
        operationsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(7));
        operationsPage.checkTimelineSectionVisibleByDate(getPreviousDayMonthDayByIntDay(7));
        operationsPage.checkTimelineSectionVisibleByDate(getPreviousDayMonthDayByIntDay(1));
        page.reload();
        Allure.step("filter 30 days");
        operationsPage.selectDatesInCalendar(getPreviousDayByIntDaysYearMonthDay(29), getCurrentDate());
        operationsPage.checkTimelineSectionVisibleByDate(getPreviousDayMonthDayByIntDay(1));
        operationsPage.checkTimelineSectionVisibleByDate(getPreviousDayMonthDayByIntDay(29));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("990")
    @Feature("BMS-55 Trading summary - Volume + activity + filters")
    @DisplayName("Trading/Summary. When user filters 31-98 days one division on timeline is 1 week with legend for every section")
    public void filterLegend31And98DaysTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        Allure.step("filter 31 day");
        operationsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(30));
        operationsPage.checkTimelineSectionVisibleByDate(getPreviousDayMonthDayByIntDay(30));
        operationsPage.checkTimelineSectionVisibleByDate(getPreviousDayMonthDayByIntDay(2));
        page.reload();
        Allure.step("filter 98 days");
        operationsPage.selectDatesInCalendar(getPreviousDayByIntDaysYearMonthDay(97), getCurrentDate());
        operationsPage.checkTimelineSectionVisibleByDate(getPreviousDayMonthDayByIntDay(6));
        operationsPage.checkTimelineSectionVisibleByDate(getPreviousDayMonthDayByIntDay(97));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("991")
    @Feature("BMS-55 Trading summary - Volume + activity + filters")
    @DisplayName("Trading/Summary. When user filters 99 days - 3 years one division on timeline is month with legend for every two months")
    public void filterLegend98DaysAnd3YearTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        Allure.step("filter 99 days");
        operationsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(98));
        operationsPage.checkTimelineSectionVisibleByDate(getPreviousDateMonthYearIntMonth(1));
        operationsPage.checkTimelineSectionVisibleByDate(getPreviousDateMonthYearIntMonth(3));
        page.reload();
        Allure.step("filter 3 years");
        operationsPage.selectDatesInCalendar(getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, 3, 0, -2, 0, 0), getCurrentDate());
        operationsPage.checkTimelineSectionVisibleByDate(getCurrentDateMonthYear());
        operationsPage.checkTimelineSectionVisibleByDate(getPreviousDateMonthYearIntYears(2));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("992")
    @Feature("BMS-55 Trading summary - Volume + activity + filters")
    @DisplayName("Trading/Summary. When user filters 3+ years division on timeline is 1 year with legend for every year")
    public void filterLegend3YearsTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        Allure.step("filter 3 years");
        operationsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDateByIntYearMonthDay(3));
        operationsPage.checkTimelineSectionVisibleByDate(getPreviousYearByInt(3));
        operationsPage.checkTimelineSectionVisibleByDate(getCurrentYear());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("993")
    @Feature("BMS-55 Trading summary - Volume + activity + filters")
    @DisplayName("Trading/Summary. When user uses timeline , when user filters 6 days must have 1 inactive day on the right.")
    public void timelineInactiveDaysFilter6DaysTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        Allure.step("filter 6 days");
        operationsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(5));
        operationsPage.checkTimelineSectionInactive(6);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("994")
    @Feature("BMS-55 Trading summary - Volume + activity + filters")
    @DisplayName("Trading/Summary. When user uses timeline , when user filters 5 days must have 1 inactive day on both sides")
    public void timelineInactiveDaysFilter5DaysTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        Allure.step("filter 5 days");
        operationsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(4));
        operationsPage.checkTimelineSectionInactive(6);
        operationsPage.checkTimelineSectionInactive(0);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("995")
    @Feature("BMS-55 Trading summary - Volume + activity + filters")
    @DisplayName("Trading/Summary. When user uses timeline , when user filters 4 days must have 1 inactive day on the left and 2 on the right.")
    public void timelineInactiveDaysFilter4DaysTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        Allure.step("filter 4 days");
        operationsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(3));
        operationsPage.checkTimelineSectionInactive(6);
        operationsPage.checkTimelineSectionInactive(5);
        operationsPage.checkTimelineSectionInactive(0);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("996")
    @Feature("BMS-55 Trading summary - Volume + activity + filters")
    @DisplayName("Trading/Summary. When user uses timeline , when user filters three days must have 2 inactive days on both sides")
    public void timelineInactiveDaysFilter3DaysTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        Allure.step("filter 3 days");
        operationsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(2));
        operationsPage.checkTimelineSectionInactive(6);
        operationsPage.checkTimelineSectionInactive(5);
        operationsPage.checkTimelineSectionInactive(0);
        operationsPage.checkTimelineSectionInactive(1);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("997")
    @Feature("BMS-55 Trading summary - Volume + activity + filters")
    @DisplayName("Trading/Summary. When user uses timeline , when user filters two days must have 2 inactive days on the left and 3 on the right")
    public void timelineInactiveDaysFilter2DaysTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        Allure.step("filter 2 days");
        operationsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(1));
        operationsPage.checkTimelineSectionInactive(6);
        operationsPage.checkTimelineSectionInactive(5);
        operationsPage.checkTimelineSectionInactive(4);
        operationsPage.checkTimelineSectionInactive(0);
        operationsPage.checkTimelineSectionInactive(1);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("998")
    @Feature("BMS-55 Trading summary - Volume + activity + filters")
    @DisplayName("Trading/Summary. When user uses timeline , when user filters one day must have 3 inactive days on both sides")
    public void timelineInactiveDaysFilter1DayTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        Allure.step("filter 1 day");
        operationsPage.selectDatesInCalendar(getCurrentDate(), getCurrentDate());
        operationsPage.checkTimelineSectionInactive(6);
        operationsPage.checkTimelineSectionInactive(5);
        operationsPage.checkTimelineSectionInactive(4);
        operationsPage.checkTimelineSectionInactive(0);
        operationsPage.checkTimelineSectionInactive(1);
        operationsPage.checkTimelineSectionInactive(2);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("999")
    @Feature("BMS-55 Trading summary - Volume + activity + filters")
    @DisplayName("Trading/Summary. When user uses timeline , user can switch timeline between display activity or volume")
    public void timelineSwitchVolumeActivity() throws SQLException, InterruptedException {

        tradingPage.deleteClientDeals(client.getUcid());

        Allure.step("create a three small deals for one day");

        MtMt4TradesCoercedObject trade0 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade1 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade2 = generateMt4TradesCoercedRandomized(client);
        trade0.notionalValueUsd = 10.0;
        trade1.notionalValueUsd = 10.0;
        trade2.notionalValueUsd = 10.0;


        trade0.openTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 1, 0, 0, 1);
        trade0.closeTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 1, 0, 0, 0);
        trade1.openTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 1, 0, 0, 1);
        trade1.closeTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 1, 0, 0, 0);
        trade2.openTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 1, 0, 0, 1);
        trade2.closeTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 1, 0, 0, 0);

        Allure.step("create a one big deals for another day notionalValueUsd of which will be 3 times bigger than sum of first three");

        MtMt4TradesCoercedObject trade3 = generateMt4TradesCoercedRandomized(client);
        trade3.notionalValueUsd = 90.0;
        trade3.openTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 0, 1);
        trade3.closeTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 0, 0);

        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade0, trade1, trade2, trade3));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        Allure.step("filter 2 days");
        operationsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(1));
        operationsPage.clickVolumeButtonTimeline();
        operationsPage.checkStileValueOfTimelineBar(2, "height: 33.3333%;");
        operationsPage.checkStileValueOfTimelineBar(3, "height: 100%;");
        operationsPage.clickActivityButtonTimeline();
        operationsPage.checkStileValueOfTimelineBar(2, "height: 100%;");
        operationsPage.checkStileValueOfTimelineBar(3, "height: 33.3333%;");
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
        trade1.openTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 1, -1, 0, 0, 0, 1);
        trade1.closeTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 1, -1, 0, 0, 0, 0);
        trade2.openTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 1, 0, 0, 0, 0, 0);
        trade2.closeTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 1, 0, 0, 0, 0, 1);

        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade0, trade1, trade2));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        Allure.step("filter test date");
        operationsPage.selectDateFilter("Last 1 year");
        Allure.step("check that only data for the test date is displayed");
        assertThat("Verify deals", tradingPage.getPerformanceOverviewDealsBySymbol("EURUSD"), equalTo("2"));
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
        trade1.openTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 29, 0, 0, 1);
        trade1.closeTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 29, 0, 0, 0);
        trade2.openTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 30, 0, 0, 0);
        trade2.closeTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 30, 0, 0, 1);

        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade0, trade1, trade2));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        Allure.step("filter test date");
        operationsPage.selectDateFilter("Last 30 days");
        Allure.step("check that only data for the test date is displayed");
        assertThat("Verify deals", tradingPage.getPerformanceOverviewDealsBySymbol("EURUSD"), equalTo("2"));
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
        trade1.openTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 5, 0, 0, 0, 1);
        trade1.closeTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 5, 0, 0, 0, 0);
        trade2.openTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 6, 0, 0, 0, 0);
        trade2.closeTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 6, 0, 0, 0, 1);

        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade0, trade1, trade2));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        Allure.step("filter test date");
        operationsPage.selectDateFilter("Last 6 months");
        Allure.step("check that only data for the test date is displayed");
        assertThat("Verify deals", tradingPage.getPerformanceOverviewDealsBySymbol("EURUSD"), equalTo("2"));
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
        trade1.openTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 6, 0, 0, 1);
        trade1.closeTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 6, 0, 0, 0);
        trade2.openTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 7, 0, 0, 0);
        trade2.closeTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 7, 0, 0, 1);

        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade0, trade1, trade2));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        Allure.step("filter test date");
        operationsPage.selectDateFilter("Last 7 days");
        Allure.step("check that only data for the test date is displayed");
        assertThat("Verify deals", tradingPage.getPerformanceOverviewDealsBySymbol("EURUSD"), equalTo("2"));
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
        trade1.openTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 89, 0, 0, 1);
        trade1.closeTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 89, 0, 0, 0);
        trade2.openTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 90, 0, 0, 0);
        trade2.closeTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 90, 0, 0, 1);

        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade0, trade1, trade2));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        Allure.step("filter test date");
        operationsPage.selectDateFilter("Last 7 days");
        Allure.step("check that only data for the test date is displayed");
        assertThat("Verify deals", tradingPage.getPerformanceOverviewDealsBySymbol("EURUSD"), equalTo("2"));
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
        trade1.openTime = "2024-02-13 14:09:35";
        trade1.closeTime = "2024-02-13 14:09:36";
        trade2.openTime = "2024-02-13 15:09:33";
        trade2.closeTime = "2024-02-13 15:09:34";

        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade0, trade1, trade2));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        Allure.step("filter test date");
        operationsPage.selectDatesInCalendar("2024-12-11", "2024-12-11");
        Allure.step("check that only data for the test date is displayed");
        assertThat("Verify deals", tradingPage.getPerformanceOverviewDealsBySymbol("EURUSD"), equalTo("2"));
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

        trade2.account = ((long) account2.account);

        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade0, trade1, trade2));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        Allure.step("filter test account");
        Allure.step("check that only data for the selected account is displayed");
        operationsPage.clickOnAccountSelectionWindow();
        operationsPage.selectTradingAccount(account1.account);
        assertThat("Verify deals", tradingPage.getPerformanceOverviewDealsBySymbol("EURUSD"), equalTo("2"));
        operationsPage.clearSelectedTradingAccount();
        operationsPage.selectTradingAccount(account2.account);
        assertThat("Verify deals", tradingPage.getPerformanceOverviewDealsBySymbol("EURUSD"), equalTo("1"));
    }
}
