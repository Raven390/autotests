package tests.vindexBackofficeUiTests;

import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.db.clickhouse.lnSessionParsed.LnSessionParsedObject;
import helpers.data.ClientHelper;
import helpers.data.enums.Brand;
import helpers.data.enums.Regulator;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseWeb;

import java.sql.SQLException;
import java.util.ArrayList;

import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateStaticUserByClient;
import static businessObjects.db.clickhouse.lnSessionParsed.LnSessionParsedObjectFactory.generateLexisNexisDataByClient;
import static helpers.data.enums.DateTimeFormat.DATE;
import static helpers.data.enums.DateTimeFormat.DATE_AND_TIME;
import static helpers.database.DbHelper.*;
import static utils.Constants.*;
import static utils.Constants.LAYER_WEB;
import static utils.Utils.*;

public class ActivityTabTest extends TestBaseWeb {

    public void deleteLexis(ClientHelper client) throws SQLException {
        deleteEntryFromDb(LEXIS_NEXIS_TABLE_NAME, "user_id=" + client.getUserId() + " AND brand='" + client.getBrand() + "'");
    }

    static ClientHelper activityClient = new ClientHelper(181_801, Brand.INFINOX, Regulator.FCA);

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException {
        CrmTbUserObject clientDb = generateStaticUserByClient(activityClient);
        clientDb.firstName = "Activity";
        insertObjectToDb(CRM_USER_TABLE_NAME, clientDb);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Activity tab record in list shows data from DB")
    @AllureId("768")
    public void activityTabShowBasicInfoFromDBTest() throws ReflectiveOperationException, SQLException {
        deleteLexis(activityClient);
        Allure.step("prepare all clients test data into DB with negative risk data");
        LnSessionParsedObject lexis = generateLexisNexisDataByClient(activityClient);
        lexis.eventDatetime = "2024-12-24 21:03:56";
        lexis.eventType = "account_creation";
        lexis.conditionAttrib_5 = "agent_mobile";
        lexis.os = "android";
        lexis.riskRating = "high";
        lexis.policyScore = -50;
        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, lexis);

        activityTab.navigateToMain();
        keycloackPage.loginAsDevUser();
        activityTab.navigate(activityClient.getUcid());
        String[] dates = lexis.eventDatetime.split(" ");
        activityTab.checkDateColumnValue(dates[0], dates[1].substring(0, 5));
        activityTab.checkEventColumnValue(lexis.eventType);
        activityTab.checkAgentColumnValue(lexis.conditionAttrib_5);
        activityTab.checkOsColumnValue(lexis.os);
        activityTab.checkRiskColumnValue(lexis.riskRating);
        activityTab.checkRiskColumnColourDanger();
        activityTab.checkScoreColumnValue(lexis.policyScore);
        activityTab.checkScoreColumnColourDanger();
        activityTab.checkPoliciesColumnValue("testRule-10");

    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Activity tab filter by OS")
    @AllureId("791")
    public void filterAgentTest() throws SQLException, ReflectiveOperationException {
        deleteLexis(activityClient);

        Allure.step("Activity tab filter by OS");
        LnSessionParsedObject androidLexis = generateLexisNexisDataByClient(activityClient);
        androidLexis.eventDatetime = "2024-12-24 21:03:56";
        androidLexis.eventType = "account_creation";
        androidLexis.conditionAttrib_5 = "agent_mobile";
        androidLexis.os = "android";
        androidLexis.riskRating = "high";
        androidLexis.policyScore = -50;

        LnSessionParsedObject windowsLexis = generateLexisNexisDataByClient(activityClient);
        windowsLexis.eventDatetime = "2024-12-24 21:03:56";
        windowsLexis.eventType = "account_creation";
        windowsLexis.conditionAttrib_5 = "agent_mobile";
        windowsLexis.os = "windows";
        windowsLexis.riskRating = "high";
        windowsLexis.policyScore = -50;

        LnSessionParsedObject testLexis = generateLexisNexisDataByClient(activityClient);
        testLexis.eventDatetime = "2024-12-24 21:03:56";
        testLexis.eventType = "account_creation";
        testLexis.conditionAttrib_5 = "agent_mobile";
        testLexis.os = "testOS";
        testLexis.riskRating = "high";
        testLexis.policyScore = -50;

        ArrayList<LnSessionParsedObject> lexis = new ArrayList<>();
        lexis.add(testLexis);
        lexis.add(windowsLexis);
        lexis.add(androidLexis);
        insertObjectsToDb(LEXIS_NEXIS_TABLE_NAME, lexis);

        activityTab.navigateToMain();
        keycloackPage.loginAsDevUser();
        activityTab.navigate(activityClient.getUcid());
        activityTab.clickOsFilter();
        activityTab.checkThatOptionPresented(androidLexis.os);
        activityTab.checkThatOptionPresented(windowsLexis.os);
        activityTab.checkThatOptionPresented(testLexis.os);
        activityTab.clickFilterOption(testLexis.os);
        activityTab.checkOsColumnValue(testLexis.os, 0);
        activityTab.clearFilterButton();
        activityTab.clickFilterOption(androidLexis.os);
        activityTab.checkOsColumnValue(androidLexis.os, 0);
        activityTab.clearFilterButton();
        activityTab.clickFilterOption(windowsLexis.os);
        activityTab.checkOsColumnValue(windowsLexis.os, 0);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Activity tab filter by risk")
    @AllureId("790")
    public void filterRiskTest() throws SQLException, ReflectiveOperationException {
        deleteLexis(activityClient);

        Allure.step("Prepare data for DB with different risk ");
        LnSessionParsedObject highLexis = generateLexisNexisDataByClient(activityClient);
        highLexis.eventDatetime = "2024-12-24 21:03:56";
        highLexis.eventType = "account_creation";
        highLexis.conditionAttrib_5 = "agent_mobile";
        highLexis.os = "android";
        highLexis.riskRating = "high";
        highLexis.policyScore = -50;

        LnSessionParsedObject MediumLexis = generateLexisNexisDataByClient(activityClient);
        MediumLexis.eventDatetime = "2024-12-24 21:03:56";
        MediumLexis.eventType = "account_creation";
        MediumLexis.conditionAttrib_5 = "agent_mobile";
        MediumLexis.os = "windows";
        MediumLexis.riskRating = "Medium";
        MediumLexis.policyScore = -50;

        LnSessionParsedObject lowLexis = generateLexisNexisDataByClient(activityClient);
        lowLexis.eventDatetime = "2024-12-24 21:03:56";
        lowLexis.eventType = "account_creation";
        lowLexis.conditionAttrib_5 = "agent_mobile";
        lowLexis.os = "testOS";
        lowLexis.riskRating = "Low";
        lowLexis.policyScore = -50;

        LnSessionParsedObject neutralLexis = generateLexisNexisDataByClient(activityClient);
        neutralLexis.eventDatetime = "2024-12-24 21:03:56";
        neutralLexis.eventType = "account_creation";
        neutralLexis.conditionAttrib_5 = "agent_mobile";
        neutralLexis.os = "testOS";
        neutralLexis.riskRating = "Neutral";
        neutralLexis.policyScore = -50;

        LnSessionParsedObject trusredlLexis = generateLexisNexisDataByClient(activityClient);
        trusredlLexis.eventDatetime = "2024-12-24 21:03:56";
        trusredlLexis.eventType = "account_creation";
        trusredlLexis.conditionAttrib_5 = "agent_mobile";
        trusredlLexis.os = "testOS";
        trusredlLexis.riskRating = "Trusred";
        trusredlLexis.policyScore = -50;

        ArrayList<LnSessionParsedObject> lexis = new ArrayList<>();
        lexis.add(lowLexis);
        lexis.add(MediumLexis);
        lexis.add(highLexis);
        lexis.add(neutralLexis);
        lexis.add(trusredlLexis);
        insertObjectsToDb(LEXIS_NEXIS_TABLE_NAME, lexis);

        activityTab.navigateToMain();
        keycloackPage.loginAsDevUser();
        activityTab.navigate(activityClient.getUcid());
        activityTab.clickRiskFilter();
        activityTab.checkThatOptionPresented(highLexis.riskRating);
        activityTab.checkThatOptionPresented(lowLexis.riskRating);
        activityTab.checkThatOptionPresented(MediumLexis.riskRating);
        activityTab.checkThatOptionPresented(neutralLexis.riskRating);
        activityTab.checkThatOptionPresented(trusredlLexis.riskRating);
        activityTab.clickFilterOption(highLexis.riskRating);
        activityTab.checkRiskColumnValue(highLexis.riskRating, 0);
        activityTab.clearFilterButton();
        activityTab.clickFilterOption(lowLexis.riskRating);
        activityTab.checkRiskColumnValue(lowLexis.riskRating, 0);
        activityTab.clearFilterButton();
        activityTab.clickFilterOption(MediumLexis.riskRating);
        activityTab.checkRiskColumnValue(MediumLexis.riskRating, 0);
        activityTab.clearFilterButton();
        activityTab.clickFilterOption(neutralLexis.riskRating);
        activityTab.checkRiskColumnValue(neutralLexis.riskRating, 0);
        activityTab.clearFilterButton();
        activityTab.clickFilterOption(trusredlLexis.riskRating);
        activityTab.checkRiskColumnValue(trusredlLexis.riskRating, 0);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Activity tab filter by date - Today")
    @AllureId("792")
    public void filterDateTodayTest() throws SQLException, ReflectiveOperationException {
        deleteLexis(activityClient);

        Allure.step("Prepare data for DB with dates inside and outside filtered diapason ");
        LnSessionParsedObject todayLexis = generateLexisNexisDataByClient(activityClient);
        todayLexis.eventDatetime = getCurrentTimestampDbFormat();
        todayLexis.eventType = "account_creation";
        todayLexis.conditionAttrib_5 = "agent_mobile";
        todayLexis.os = "android";
        todayLexis.riskRating = "high";
        todayLexis.policyScore = -50;

        LnSessionParsedObject yesterdayLexis = generateLexisNexisDataByClient(activityClient);
        yesterdayLexis.eventDatetime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 1, 0, 0);
        yesterdayLexis.eventType = "account_creation";
        yesterdayLexis.conditionAttrib_5 = "agent_mobile";
        yesterdayLexis.os = "windows";
        yesterdayLexis.riskRating = "Medium";
        yesterdayLexis.policyScore = -50;

        ArrayList<LnSessionParsedObject> lexis = new ArrayList<>();
        lexis.add(todayLexis);
        lexis.add(yesterdayLexis);
        insertObjectsToDb(LEXIS_NEXIS_TABLE_NAME, lexis);

        activityTab.navigateToMain();
        keycloackPage.loginAsDevUser();
        activityTab.navigate(activityClient.getUcid());
        activityTab.clickDateFilter();
        activityTab.clickFilterOption("Today");
        activityTab.checkDateColumnValue(todayLexis.eventDatetime);
        activityTab.checkDateColumnValueNotPresented(yesterdayLexis.eventDatetime);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Activity tab filter by date - Last 7 Days")
    @AllureId("819")
    public void filterDateLast7daysTest() throws SQLException, ReflectiveOperationException {
        deleteLexis(activityClient);

        Allure.step("Prepare data for DB with dates inside and outside filtered diapason: one today, one today - 6 days, and one today - 7 days ");
        LnSessionParsedObject todayLexis = generateLexisNexisDataByClient(activityClient);
        todayLexis.eventDatetime = getCurrentTimestampDbFormat();
        todayLexis.eventType = "account_creation";
        todayLexis.conditionAttrib_5 = "mobile_browser";
        todayLexis.os = "android";
        todayLexis.riskRating = "high";
        todayLexis.policyScore = -50;

        LnSessionParsedObject borderlineLexis = generateLexisNexisDataByClient(activityClient);
        borderlineLexis.eventDatetime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 6, 0, 0);
        borderlineLexis.eventType = "account_creation";
        borderlineLexis.conditionAttrib_5 = "web_browser";
        borderlineLexis.os = "MACOSS";
        borderlineLexis.riskRating = "Medium";
        borderlineLexis.policyScore = -50;

        LnSessionParsedObject outsideLexis = generateLexisNexisDataByClient(activityClient);
        outsideLexis.eventDatetime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 7, 0, 0);
        outsideLexis.eventType = "account_creation";
        outsideLexis.conditionAttrib_5 = "agent_mobile";
        outsideLexis.os = "Linux";
        outsideLexis.riskRating = "Medium";
        outsideLexis.policyScore = -50;

        ArrayList<LnSessionParsedObject> lexis = new ArrayList<>();
        lexis.add(todayLexis);
        lexis.add(borderlineLexis);
        lexis.add(outsideLexis);

        insertObjectsToDb(LEXIS_NEXIS_TABLE_NAME, lexis);

        activityTab.navigateToMain();
        keycloackPage.loginAsDevUser();
        activityTab.navigate(activityClient.getUcid());
        activityTab.clickDateFilter();
        activityTab.clickFilterOption("Last 7 days");
        activityTab.checkDateColumnValue(todayLexis.eventDatetime, 1);
        activityTab.checkDateColumnValue(borderlineLexis.eventDatetime, 0);
        activityTab.checkDateColumnValueNotPresented(outsideLexis.eventDatetime);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Activity tab filter by date - Last 30 Days")
    @AllureId("820")
    public void filterDateLast30daysTest() throws SQLException, ReflectiveOperationException {
        deleteLexis(activityClient);

        Allure.step("Prepare data for DB with dates inside and outside filtered diapason: one today, one today - 29 days, and one today - 30 days ");
        LnSessionParsedObject todayLexis = generateLexisNexisDataByClient(activityClient);
        todayLexis.eventDatetime = getCurrentTimestampDbFormat();
        todayLexis.eventType = "account_creation";
        todayLexis.conditionAttrib_5 = "mobile_browser";
        todayLexis.os = "android";
        todayLexis.riskRating = "high";
        todayLexis.policyScore = -50;

        LnSessionParsedObject borderlineLexis = generateLexisNexisDataByClient(activityClient);
        borderlineLexis.eventDatetime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 29, 0, 0);
        borderlineLexis.eventType = "account_creation";
        borderlineLexis.conditionAttrib_5 = "web_browser";
        borderlineLexis.os = "MACOSS";
        borderlineLexis.riskRating = "Medium";
        borderlineLexis.policyScore = -50;

        LnSessionParsedObject outsideLexis = generateLexisNexisDataByClient(activityClient);
        outsideLexis.eventDatetime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 30, 0, 0);
        outsideLexis.eventType = "account_creation";
        outsideLexis.conditionAttrib_5 = "agent_mobile";
        outsideLexis.os = "Linux";
        outsideLexis.riskRating = "Medium";
        outsideLexis.policyScore = -50;

        ArrayList<LnSessionParsedObject> lexis = new ArrayList<>();
        lexis.add(todayLexis);
        lexis.add(borderlineLexis);
        lexis.add(outsideLexis);

        insertObjectsToDb(LEXIS_NEXIS_TABLE_NAME, lexis);

        activityTab.navigateToMain();
        keycloackPage.loginAsDevUser();
        activityTab.navigate(activityClient.getUcid());
        activityTab.clickDateFilter();
        activityTab.clickFilterOption("Last 30 days");
        activityTab.checkDateColumnValue(todayLexis.eventDatetime, 1);
        activityTab.checkDateColumnValue(borderlineLexis.eventDatetime, 0);
        activityTab.checkDateColumnValueNotPresented(outsideLexis.eventDatetime);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Activity tab filter by date - Last 90 Days")
    @AllureId("821")
    public void filterDateLast90daysTest() throws SQLException, ReflectiveOperationException {
        deleteLexis(activityClient);

        Allure.step("Prepare data for DB with dates inside and outside filtered diapason: one today, one today - 90 days, and one today - 89 days ");
        LnSessionParsedObject todayLexis = generateLexisNexisDataByClient(activityClient);
        todayLexis.eventDatetime = getCurrentTimestampDbFormat();
        todayLexis.eventType = "account_creation";
        todayLexis.conditionAttrib_5 = "mobile_browser";
        todayLexis.os = "android";
        todayLexis.riskRating = "high";
        todayLexis.policyScore = -50;

        LnSessionParsedObject borderlineLexis = generateLexisNexisDataByClient(activityClient);
        borderlineLexis.eventDatetime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 89, 0, 0);
        borderlineLexis.eventType = "account_creation";
        borderlineLexis.conditionAttrib_5 = "web_browser";
        borderlineLexis.os = "MACOSS";
        borderlineLexis.riskRating = "Medium";
        borderlineLexis.policyScore = -50;

        LnSessionParsedObject outsideLexis = generateLexisNexisDataByClient(activityClient);
        outsideLexis.eventDatetime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 90, 0, 0);
        outsideLexis.eventType = "account_creation";
        outsideLexis.conditionAttrib_5 = "agent_mobile";
        outsideLexis.os = "Linux";
        outsideLexis.riskRating = "Medium";
        outsideLexis.policyScore = -50;

        ArrayList<LnSessionParsedObject> lexis = new ArrayList<>();
        lexis.add(todayLexis);
        lexis.add(borderlineLexis);
        lexis.add(outsideLexis);

        insertObjectsToDb(LEXIS_NEXIS_TABLE_NAME, lexis);

        activityTab.navigateToMain();
        keycloackPage.loginAsDevUser();
        activityTab.navigate(activityClient.getUcid());
        activityTab.clickDateFilter();
        activityTab.clickFilterOption("Last 90 days");
        activityTab.checkDateColumnValue(todayLexis.eventDatetime, 1);
        activityTab.checkDateColumnValue(borderlineLexis.eventDatetime, 0);
        activityTab.checkDateColumnValueNotPresented(outsideLexis.eventDatetime);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Activity tab filter by date - Custom dates")
    @AllureId("822")
    public void filterDateCustomTest() throws SQLException, ReflectiveOperationException {
        deleteLexis(activityClient);

        Allure.step("Prepare data for DB with dates inside and outside filtered diapason.");
        LnSessionParsedObject todayLexis = generateLexisNexisDataByClient(activityClient);
        todayLexis.eventDatetime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 5, 0, 0);
        todayLexis.eventType = "account_creation";
        todayLexis.conditionAttrib_5 = "mobile_browser";
        todayLexis.os = "android";
        todayLexis.riskRating = "high";
        todayLexis.policyScore = -50;

        LnSessionParsedObject borderlineLexis = generateLexisNexisDataByClient(activityClient);
        borderlineLexis.eventDatetime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 98, 0, 0);
        borderlineLexis.eventType = "account_creation";
        borderlineLexis.conditionAttrib_5 = "web_browser";
        borderlineLexis.os = "MACOSS";
        borderlineLexis.riskRating = "Medium";
        borderlineLexis.policyScore = -50;

        LnSessionParsedObject outsideLexis = generateLexisNexisDataByClient(activityClient);
        outsideLexis.eventDatetime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 99, 0, 0);
        outsideLexis.eventType = "account_creation";
        outsideLexis.conditionAttrib_5 = "agent_mobile";
        outsideLexis.os = "Linux";
        outsideLexis.riskRating = "Medium";
        outsideLexis.policyScore = -50;

        ArrayList<LnSessionParsedObject> lexis = new ArrayList<>();
        lexis.add(todayLexis);
        lexis.add(borderlineLexis);
        lexis.add(outsideLexis);

        insertObjectsToDb(LEXIS_NEXIS_TABLE_NAME, lexis);

        activityTab.navigateToMain();
        keycloackPage.loginAsDevUser();
        activityTab.navigate(activityClient.getUcid());
        activityTab.clickDateFilter();
        activityTab.setCustomDates(getCurrentTimestampMinusOffsetFormatted(DATE, 0, 0, 5, 0, 0), getCurrentTimestampMinusOffsetFormatted(DATE, 0, 0, 98, 0, 0));
        activityTab.checkDateColumnValue(todayLexis.eventDatetime, 1);
        activityTab.checkDateColumnValue(borderlineLexis.eventDatetime, 0);
        activityTab.checkDateColumnValueNotPresented(outsideLexis.eventDatetime);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Activity tab sort by score")
    @AllureId("794")
    public void sortingScoreTest() throws SQLException, ReflectiveOperationException {
        deleteLexis(activityClient);

        Allure.step("Prepare data for DB with dates inside and outside filtered diapason.");
        LnSessionParsedObject firstLexis = generateLexisNexisDataByClient(activityClient);
        firstLexis.eventDatetime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 5, 0, 0);
        firstLexis.eventType = "account_creation";
        firstLexis.conditionAttrib_5 = "mobile_browser";
        firstLexis.os = "android";
        firstLexis.riskRating = "high";
        firstLexis.policyScore = 0;

        LnSessionParsedObject secondLexis = generateLexisNexisDataByClient(activityClient);
        secondLexis.eventDatetime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 98, 0, 0);
        secondLexis.eventType = "account_creation";
        secondLexis.conditionAttrib_5 = "web_browser";
        secondLexis.os = "MACOSS";
        secondLexis.riskRating = "Medium";
        secondLexis.policyScore = 5;

        LnSessionParsedObject thirdLexis = generateLexisNexisDataByClient(activityClient);
        thirdLexis.eventDatetime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 99, 0, 0);
        thirdLexis.eventType = "account_creation";
        thirdLexis.conditionAttrib_5 = "agent_mobile";
        thirdLexis.os = "Linux";
        thirdLexis.riskRating = "Medium";
        thirdLexis.policyScore = -50;

        LnSessionParsedObject fourthLexis = generateLexisNexisDataByClient(activityClient);
        fourthLexis.eventDatetime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 99, 0, 0);
        fourthLexis.eventType = "account_creation";
        fourthLexis.conditionAttrib_5 = "agent_mobile";
        fourthLexis.os = "Linux";
        fourthLexis.riskRating = "Medium";
        fourthLexis.policyScore = 50;

        ArrayList<LnSessionParsedObject> lexis = new ArrayList<>();
        lexis.add(firstLexis);
        lexis.add(secondLexis);
        lexis.add(thirdLexis);
        lexis.add(fourthLexis);

        insertObjectsToDb(LEXIS_NEXIS_TABLE_NAME, lexis);

        activityTab.navigateToMain();
        keycloackPage.loginAsDevUser();
        activityTab.navigate(activityClient.getUcid());
        activityTab.setSortByScoreDesc();
        activityTab.checkScoreColumnIsDesc();
        activityTab.setSortByScoreAsc();
        activityTab.checkScoreColumnIsAsc();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Activity tab sort by date")
    @AllureId("793")
    public void sortingDateTest() throws SQLException, ReflectiveOperationException {
        deleteLexis(activityClient);

        Allure.step("Prepare data for DB with dates inside and outside filtered diapason.");
        LnSessionParsedObject firstLexis = generateLexisNexisDataByClient(activityClient);
        firstLexis.eventDatetime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 5, 0, 0);
        firstLexis.eventType = "account_creation";
        firstLexis.conditionAttrib_5 = "mobile_browser";
        firstLexis.os = "android";
        firstLexis.riskRating = "high";
        firstLexis.policyScore = 0;

        LnSessionParsedObject secondLexis = generateLexisNexisDataByClient(activityClient);
        secondLexis.eventDatetime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 98, 0, 0);
        secondLexis.eventType = "account_creation";
        secondLexis.conditionAttrib_5 = "web_browser";
        secondLexis.os = "MACOSS";
        secondLexis.riskRating = "Medium";
        secondLexis.policyScore = 5;

        LnSessionParsedObject thirdLexis = generateLexisNexisDataByClient(activityClient);
        thirdLexis.eventDatetime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 99, 0, 0);
        thirdLexis.eventType = "account_creation";
        thirdLexis.conditionAttrib_5 = "agent_mobile";
        thirdLexis.os = "Linux";
        thirdLexis.riskRating = "Medium";
        thirdLexis.policyScore = -50;

        LnSessionParsedObject fourthLexis = generateLexisNexisDataByClient(activityClient);
        fourthLexis.eventDatetime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 99, 0, 0);
        fourthLexis.eventType = "account_creation";
        fourthLexis.conditionAttrib_5 = "agent_mobile";
        fourthLexis.os = "Linux";
        fourthLexis.riskRating = "Medium";
        fourthLexis.policyScore = 50;

        ArrayList<LnSessionParsedObject> lexis = new ArrayList<>();
        lexis.add(firstLexis);
        lexis.add(secondLexis);
        lexis.add(thirdLexis);
        lexis.add(fourthLexis);

        insertObjectsToDb(LEXIS_NEXIS_TABLE_NAME, lexis);

        activityTab.navigateToMain();
        keycloackPage.loginAsDevUser();
        activityTab.navigate(activityClient.getUcid());
        activityTab.setSortByDateDesc();
        activityTab.checkDateColumnIsDesc();
        activityTab.setSortByDateAsc();
        activityTab.checkDateColumnIsAsc();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Activity tab - Summary, risk score section test")
    @AllureId("834")
    public void summaryRiskScoreShowDataFromDB() throws SQLException, ReflectiveOperationException {

        deleteLexis(activityClient);

        LnSessionParsedObject firstLexis = generateLexisNexisDataByClient(activityClient);
        firstLexis.eventDatetime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 5, 0, 0);
        firstLexis.eventType = "account_creation";
        firstLexis.conditionAttrib_5 = "mobile_app";
        firstLexis.os = "android";
        firstLexis.riskRating = "high";
        firstLexis.policyScore = 99;
        firstLexis.emailageEmailriskscoreEascore = 11;
        firstLexis.emailageEmailriskscoreEariskbandid = 22;
        firstLexis.emailageEmailriskscoreEaadvice = "test advice";

        Allure.step("Prepare data for DB with riskRating = " + firstLexis.riskRating + ", and policyScore = " + firstLexis.policyScore);


        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, firstLexis);

        activityTab.navigateToMain();
        keycloackPage.loginAsDevUser();
        activityTab.navigate(activityClient.getUcid());
        activityTab.clickOnDataRow();
        activityTab.checkScoreHeaderValueScoreLine(firstLexis.policyScore);
        activityTab.positiveScoreMeterDisplayed();
        activityTab.positiveScoreMeterDisplayed();
        activityTab.setSuccessLabelDisplayed();
        activityTab.checkTextInLabel(firstLexis.riskRating);
        activityTab.checkPositionOfTheLineDivider("99.5");

        deleteLexis(activityClient);

        firstLexis.riskRating = "low";
        firstLexis.policyScore = 50;

        Allure.step("Prepare data for DB with riskRating = " + firstLexis.riskRating + ", and policyScore = " + firstLexis.policyScore);

        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, firstLexis);

        page.reload();
        activityTab.clickOnDataRow();
        activityTab.checkScoreHeaderValueScoreLine(firstLexis.policyScore);
        activityTab.positiveScoreMeterDisplayed();
        activityTab.positiveScoreMeterDisplayed();
        activityTab.setSuccessLabelDisplayed();
        activityTab.checkTextInLabel(firstLexis.riskRating);
        activityTab.checkPositionOfTheLineDivider("75");

        deleteLexis(activityClient);

        firstLexis.riskRating = "medium";
        firstLexis.policyScore = 0;

        Allure.step("Prepare data for DB with riskRating = " + firstLexis.riskRating + ", and policyScore = " + firstLexis.policyScore);

        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, firstLexis);

        page.reload();
        activityTab.clickOnDataRow();
        activityTab.checkScoreHeaderValueScoreLine(firstLexis.policyScore);
        activityTab.checkNeutralLabelDisplayed();
        activityTab.checkTextInLabel(firstLexis.riskRating);
        activityTab.checkPositionOfTheLineDivider("50");


        deleteLexis(activityClient);

        firstLexis.riskRating = "trusted";
        firstLexis.policyScore = -50;

        Allure.step("Prepare data for DB with riskRating = " + firstLexis.riskRating + ", and policyScore = " + firstLexis.policyScore);

        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, firstLexis);

        page.reload();
        activityTab.clickOnDataRow();
        activityTab.checkScoreHeaderValueScoreLine(firstLexis.policyScore);
        activityTab.checkDangerLabelDisplayed();
        activityTab.checkTextInLabel(firstLexis.riskRating);
        activityTab.checkPositionOfTheLineDivider("25");

        deleteLexis(activityClient);

        firstLexis.riskRating = "low";
        firstLexis.policyScore = -99;

        Allure.step("Prepare data for DB with riskRating = " + firstLexis.riskRating + ", and policyScore = " + firstLexis.policyScore);

        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, firstLexis);

        page.reload();
        activityTab.clickOnDataRow();
        activityTab.checkScoreHeaderValueScoreLine(firstLexis.policyScore);
        activityTab.checkDangerLabelDisplayed();
        activityTab.checkTextInLabel(firstLexis.riskRating);
        activityTab.checkPositionOfTheLineDivider("0.5");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Activity tab - Summary, applied policies section test")
    @AllureId("836")
    public void summaryAppliedPoliciesShowDataFromDB() throws SQLException, ReflectiveOperationException {

        deleteLexis(activityClient);

        LnSessionParsedObject firstLexis = generateLexisNexisDataByClient(activityClient);
        firstLexis.eventDatetime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 5, 0, 0);
        firstLexis.eventType = "account_creation";
        firstLexis.conditionAttrib_5 = "mobile_app";
        firstLexis.os = "android";
        firstLexis.riskRating = "high";
        firstLexis.policyScore = 99;
        firstLexis.emailageEmailriskscoreEascore = 11;
        firstLexis.emailageEmailriskscoreEariskbandid = 22;
        firstLexis.emailageEmailriskscoreEaadvice = "test advice";

        Allure.step("Prepare data for DB with riskRating = " + firstLexis.riskRating + ", and policyScore = " + firstLexis.policyScore);


        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, firstLexis);

        activityTab.navigateToMain();
        keycloackPage.loginAsDevUser();
        activityTab.navigate(activityClient.getUcid());
        activityTab.clickOnDataRow();
        activityTab.checkTitleOfPoliciesSection("Applied policies");
        activityTab.checkRuleName("testRule");
        activityTab.checkRuleScore("-10", "testRule");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Activity tab - Summary, TMX reason section test")
    @AllureId("842")
    public void summaryTmxShowDataFromDB() throws SQLException, ReflectiveOperationException {

        deleteLexis(activityClient);

        LnSessionParsedObject firstLexis = generateLexisNexisDataByClient(activityClient);
        firstLexis.eventDatetime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 5, 0, 0);
        firstLexis.eventType = "account_creation";
        firstLexis.conditionAttrib_5 = "mobile_app";
        firstLexis.os = "android";
        firstLexis.riskRating = "high";
        firstLexis.policyScore = 99;
        firstLexis.emailageEmailriskscoreEascore = 11;
        firstLexis.emailageEmailriskscoreEariskbandid = 22;

        Allure.step("Prepare data for DB with the tmxSummaryReasonCode equals " + firstLexis.tmxSummaryReasonCode);


        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, firstLexis);

        activityTab.navigateToMain();
        keycloackPage.loginAsDevUser();
        activityTab.navigate(activityClient.getUcid());
        activityTab.clickOnDataRow();
        activityTab.checkTitleOfTmxReasonSection("TMX reason codes");
        activityTab.checkThatTmxReasonCodeIsDisplayed(firstLexis.tmxSummaryReasonCode);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Activity tab - Summary, score section test")
    @AllureId("843")
    public void summaryScoresDataFromDB() throws SQLException, ReflectiveOperationException {

        deleteLexis(activityClient);

        LnSessionParsedObject firstLexis = generateLexisNexisDataByClient(activityClient);
        firstLexis.eventDatetime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 5, 0, 0);
        firstLexis.eventType = "account_creation";
        firstLexis.conditionAttrib_5 = "mobile_app";
        firstLexis.os = "android";
        firstLexis.riskRating = "high";
        firstLexis.policyScore = 99;

        firstLexis.emailageEmailriskscoreEascore = 1;
        firstLexis.emailageEmailriskscoreEaadvice = "some advice" + getCurrentTimestamp();

        firstLexis.emailageEmailriskscoreIpRisklevel = "Review";

        firstLexis.emailageEmailriskscoreOveralldigitalidentityscore = 100;
        firstLexis.emailageEmailriskscoreDisdescription = "some description" + getCurrentTimestamp();

        Allure.step("Prepare data for DB with the first set of data");


        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, firstLexis);

        activityTab.navigateToMain();
        keycloackPage.loginAsDevUser();
        activityTab.navigate(activityClient.getUcid());
        activityTab.clickOnDataRow();
        activityTab.checkIpScoreRiskLevelValue(firstLexis.emailageEmailriskscoreIpRisklevel);
        activityTab.checkIpScoreRiskLevelNumber(firstLexis.emailageEmailriskscoreIpRisklevel);
        activityTab.checkIpScoreRiskBarStyle(firstLexis.emailageEmailriskscoreIpRisklevel);
        activityTab.checkEmailScoreAdvice(firstLexis.emailageEmailriskscoreEaadvice);
        activityTab.checkEmailScoreValue(firstLexis.emailageEmailriskscoreEascore);
        activityTab.checkEmailScoreRiskBarStyle(firstLexis.emailageEmailriskscoreEascore);
        activityTab.checkDigitalIdentityScoreNumber(firstLexis.emailageEmailriskscoreOveralldigitalidentityscore);
        activityTab.checkDigitalIdentityScoreTitle(firstLexis.emailageEmailriskscoreDisdescription);
        activityTab.checkDigitalIdentityScoreRiskBarStyle(firstLexis.emailageEmailriskscoreOveralldigitalidentityscore);

        deleteLexis(activityClient);
        //email
        firstLexis.emailageEmailriskscoreEascore = 300;
        firstLexis.emailageEmailriskscoreEaadvice = "some advice" + getCurrentTimestamp();
        //ip
        firstLexis.emailageEmailriskscoreIpRisklevel = "Very low";
        //digital identity
        firstLexis.emailageEmailriskscoreOveralldigitalidentityscore = 80;
        firstLexis.emailageEmailriskscoreDisdescription = "some description" + getCurrentTimestamp();

        Allure.step("Prepare data for DB with the new set of data");
        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, firstLexis);
        activityTab.navigateToMain();
        keycloackPage.loginAsDevUser();
        activityTab.navigate(activityClient.getUcid());
        activityTab.clickOnDataRow();
        activityTab.checkIpScoreRiskLevelValue(firstLexis.emailageEmailriskscoreIpRisklevel);
        activityTab.checkIpScoreRiskLevelNumber(firstLexis.emailageEmailriskscoreIpRisklevel);
        activityTab.checkIpScoreRiskBarStyle(firstLexis.emailageEmailriskscoreIpRisklevel);
        activityTab.checkEmailScoreAdvice(firstLexis.emailageEmailriskscoreEaadvice);
        activityTab.checkEmailScoreValue(firstLexis.emailageEmailriskscoreEascore);
        activityTab.checkEmailScoreRiskBarStyle(firstLexis.emailageEmailriskscoreEascore);
        activityTab.checkDigitalIdentityScoreNumber(firstLexis.emailageEmailriskscoreOveralldigitalidentityscore);
        activityTab.checkDigitalIdentityScoreTitle(firstLexis.emailageEmailriskscoreDisdescription);
        activityTab.checkDigitalIdentityScoreRiskBarStyle(firstLexis.emailageEmailriskscoreOveralldigitalidentityscore);

        deleteLexis(activityClient);
        //email
        firstLexis.emailageEmailriskscoreEascore = 301;
        firstLexis.emailageEmailriskscoreEaadvice = "some advice" + getCurrentTimestamp();
        //ip
        firstLexis.emailageEmailriskscoreIpRisklevel = "Low";
        //digital identity
        firstLexis.emailageEmailriskscoreOveralldigitalidentityscore = 79;
        firstLexis.emailageEmailriskscoreDisdescription = "some description" + getCurrentTimestamp();

        Allure.step("Prepare data for DB with the new set of data");
        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, firstLexis);
        activityTab.navigateToMain();
        keycloackPage.loginAsDevUser();
        activityTab.navigate(activityClient.getUcid());
        activityTab.clickOnDataRow();
        activityTab.checkIpScoreRiskLevelValue(firstLexis.emailageEmailriskscoreIpRisklevel);
        activityTab.checkIpScoreRiskLevelNumber(firstLexis.emailageEmailriskscoreIpRisklevel);
        activityTab.checkIpScoreRiskBarStyle(firstLexis.emailageEmailriskscoreIpRisklevel);
        activityTab.checkEmailScoreAdvice(firstLexis.emailageEmailriskscoreEaadvice);
        activityTab.checkEmailScoreValue(firstLexis.emailageEmailriskscoreEascore);
        activityTab.checkEmailScoreRiskBarStyle(firstLexis.emailageEmailriskscoreEascore);
        activityTab.checkDigitalIdentityScoreNumber(firstLexis.emailageEmailriskscoreOveralldigitalidentityscore);
        activityTab.checkDigitalIdentityScoreTitle(firstLexis.emailageEmailriskscoreDisdescription);
        activityTab.checkDigitalIdentityScoreRiskBarStyle(firstLexis.emailageEmailriskscoreOveralldigitalidentityscore);

        deleteLexis(activityClient);
        //email
        firstLexis.emailageEmailriskscoreEascore = 600;
        //ip
        firstLexis.emailageEmailriskscoreIpRisklevel = "Moderate";
        //digital identity
        firstLexis.emailageEmailriskscoreOveralldigitalidentityscore = 60;

        Allure.step("Prepare data for DB with the new set of data");
        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, firstLexis);
        activityTab.navigateToMain();
        keycloackPage.loginAsDevUser();
        activityTab.navigate(activityClient.getUcid());
        activityTab.clickOnDataRow();
        activityTab.checkIpScoreRiskLevelValue(firstLexis.emailageEmailriskscoreIpRisklevel);
        activityTab.checkIpScoreRiskLevelNumber(firstLexis.emailageEmailriskscoreIpRisklevel);
        activityTab.checkIpScoreRiskBarStyle(firstLexis.emailageEmailriskscoreIpRisklevel);
        activityTab.checkEmailScoreAdvice(firstLexis.emailageEmailriskscoreEaadvice);
        activityTab.checkEmailScoreValue(firstLexis.emailageEmailriskscoreEascore);
        activityTab.checkEmailScoreRiskBarStyle(firstLexis.emailageEmailriskscoreEascore);
        activityTab.checkDigitalIdentityScoreNumber(firstLexis.emailageEmailriskscoreOveralldigitalidentityscore);
        activityTab.checkDigitalIdentityScoreTitle(firstLexis.emailageEmailriskscoreDisdescription);
        activityTab.checkDigitalIdentityScoreRiskBarStyle(firstLexis.emailageEmailriskscoreOveralldigitalidentityscore);

        deleteLexis(activityClient);
        //email
        firstLexis.emailageEmailriskscoreEascore = 601;
        //ip
        firstLexis.emailageEmailriskscoreIpRisklevel = "High";
        //digital identity
        firstLexis.emailageEmailriskscoreOveralldigitalidentityscore = 59;

        Allure.step("Prepare data for DB with the new set of data");
        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, firstLexis);
        activityTab.navigateToMain();
        keycloackPage.loginAsDevUser();
        activityTab.navigate(activityClient.getUcid());
        activityTab.clickOnDataRow();
        activityTab.checkIpScoreRiskLevelValue(firstLexis.emailageEmailriskscoreIpRisklevel);
        activityTab.checkIpScoreRiskLevelNumber(firstLexis.emailageEmailriskscoreIpRisklevel);
        activityTab.checkIpScoreRiskBarStyle(firstLexis.emailageEmailriskscoreIpRisklevel);
        activityTab.checkEmailScoreAdvice(firstLexis.emailageEmailriskscoreEaadvice);
        activityTab.checkEmailScoreValue(firstLexis.emailageEmailriskscoreEascore);
        activityTab.checkEmailScoreRiskBarStyle(firstLexis.emailageEmailriskscoreEascore);
        activityTab.checkDigitalIdentityScoreNumber(firstLexis.emailageEmailriskscoreOveralldigitalidentityscore);
        activityTab.checkDigitalIdentityScoreTitle(firstLexis.emailageEmailriskscoreDisdescription);
        activityTab.checkDigitalIdentityScoreRiskBarStyle(firstLexis.emailageEmailriskscoreOveralldigitalidentityscore);


    }

}
