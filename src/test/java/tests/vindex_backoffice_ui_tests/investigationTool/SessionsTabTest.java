package tests.vindex_backoffice_ui_tests.investigationTool;

import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static business_objects.db.clickhouse.ln_session_parsed.LnSessionParsedObjectFactory.generateLexisNexisDataByClient;
import static helpers.data.enums.DateTimeFormat.DATE;
import static helpers.data.enums.DateTimeFormat.DATE_AND_TIME;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;
import static utils.Constants.LAYER_WEB;
import static utils.Utils.*;

import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.ln_session_parsed.LnSessionParsedObject;
import helpers.data.ClientHelper;
import helpers.data.enums.Brand;
import helpers.data.enums.Country;
import helpers.data.enums.Regulator;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.random.RandomGenerator;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseWeb;

public class SessionsTabTest extends TestBaseWeb {

    public void deleteLexis(ClientHelper client) {
        deleteEntryFromDb(
                LEXIS_NEXIS_TABLE_NAME, "user_id=" + client.getUserId() + " AND brand='" + client.getBrand() + "'");
    }

    static ClientHelper activityClient;

    static {
        activityClient = ClientHelper.builder()
                .userId(181_801)
                .brand(Brand.INFINOX)
                .regulator(Regulator.FCA)
                .build();
    }

    @BeforeAll
    static void setup() throws ReflectiveOperationException, SQLException {
        CrmTbUserObject clientDb = generateStaticUserByClient(activityClient);
        clientDb.firstName = "Activity";
        insertObjectToDb(CRM_USER_TABLE_NAME, clientDb);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Activity tab record in list shows data from DB")
    @AllureId("768")
    public void activityTabShowBasicInfoFromDBTest() {
        deleteLexis(activityClient);
        Allure.step("prepare all clients test data into DB with negative risk data");
        LnSessionParsedObject lexis = generateLexisNexisDataByClient(activityClient);
        lexis.setEventDatetime("2024-12-24 21:03:56");
        lexis.setEventType("account_creation");
        lexis.setConditionAttrib5("agent_mobile");
        lexis.setOs("android");
        lexis.setRiskRating("high");
        lexis.setPolicyScore(-50);
        lexis.setSummaryReasonCodeOriginal(
                "[ \"Connection via Mobile Hotspot\", \"First Time Mobile Hotspot  Used\", \"Computer browser used\" ]");
        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, lexis);

        sessionsTab.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        sessionsTab.navigate(activityClient.getUcid());
        String[] dates = lexis.getEventDatetime().split(" ");
        sessionsTab.checkDateColumnValue(dates[0], dates[1].substring(0, 5));
        sessionsTab.checkEventColumnValue(lexis.getEventType());
        sessionsTab.checkAgentColumnValue(lexis.getConditionAttrib5());
        sessionsTab.checkOsColumnValue(lexis.getOs());
        sessionsTab.checkRiskColumnValue(lexis.getRiskRating());
        sessionsTab.checkRiskColumnColourDanger();
        sessionsTab.checkScoreColumnValue(lexis.getPolicyScore());
        sessionsTab.checkScoreColumnColourDanger();
        sessionsTab.checkSummaryColumnValue(
                "Connection via Mobile HotspotFirst Time Mobile Hotspot  UsedComputer browser used");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Activity tab filter by OS")
    @AllureId("791")
    public void filterAgentTest() {
        deleteLexis(activityClient);

        Allure.step("Activity tab filter by OS");
        LnSessionParsedObject androidLexis = generateLexisNexisDataByClient(activityClient);
        androidLexis.setEventDatetime("2024-12-24 21:03:56");
        androidLexis.setEventType("account_creation");
        androidLexis.setConditionAttrib5("agent_mobile");
        androidLexis.setOs("android");
        androidLexis.setRiskRating("high");
        androidLexis.setPolicyScore(-50);

        LnSessionParsedObject windowsLexis = generateLexisNexisDataByClient(activityClient);
        windowsLexis.setEventDatetime("2024-12-24 21:03:56");
        windowsLexis.setEventType("account_creation");
        windowsLexis.setConditionAttrib5("agent_mobile");
        windowsLexis.setOs("windows");
        windowsLexis.setRiskRating("high");
        windowsLexis.setPolicyScore(-50);

        LnSessionParsedObject testLexis = generateLexisNexisDataByClient(activityClient);
        testLexis.setEventDatetime("2024-12-24 21:03:56");
        testLexis.setEventType("account_creation");
        testLexis.setConditionAttrib5("agent_mobile");
        testLexis.setOs("testOS");
        testLexis.setRiskRating("high");
        testLexis.setPolicyScore(-50);

        ArrayList<LnSessionParsedObject> lexis = new ArrayList<>();
        lexis.add(testLexis);
        lexis.add(windowsLexis);
        lexis.add(androidLexis);
        insertObjectsToDb(LEXIS_NEXIS_TABLE_NAME, lexis);

        sessionsTab.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        sessionsTab.navigate(activityClient.getUcid());
        sessionsTab.clickOsFilter();
        sessionsTab.checkThatOptionPresented(androidLexis.getOs());
        sessionsTab.checkThatOptionPresented(windowsLexis.getOs());
        sessionsTab.checkThatOptionPresented(testLexis.getOs());
        sessionsTab.clickFilterOption(testLexis.getOs());
        sessionsTab.checkOsColumnValue(testLexis.getOs(), 0);
        sessionsTab.clearFilterButton();
        sessionsTab.clickFilterOption(androidLexis.getOs());
        sessionsTab.checkOsColumnValue(androidLexis.getOs(), 0);
        sessionsTab.clearFilterButton();
        sessionsTab.clickFilterOption(windowsLexis.getOs());
        sessionsTab.checkOsColumnValue(windowsLexis.getOs(), 0);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Activity tab filter by risk")
    @AllureId("790")
    public void filterRiskTest() {
        deleteLexis(activityClient);

        Allure.step("Prepare data for DB with different risk ");
        LnSessionParsedObject highLexis = generateLexisNexisDataByClient(activityClient);
        highLexis.setEventDatetime("2024-12-24 21:03:56");
        highLexis.setEventType("account_creation");
        highLexis.setConditionAttrib5("agent_mobile");
        highLexis.setOs("android");
        highLexis.setRiskRating("high");
        highLexis.setPolicyScore(-50);

        LnSessionParsedObject MediumLexis = generateLexisNexisDataByClient(activityClient);
        MediumLexis.setEventDatetime("2024-12-24 21:03:56");
        MediumLexis.setEventType("account_creation");
        MediumLexis.setConditionAttrib5("agent_mobile");
        MediumLexis.setOs("windows");
        MediumLexis.setRiskRating("Medium");
        MediumLexis.setPolicyScore(-50);

        LnSessionParsedObject lowLexis = generateLexisNexisDataByClient(activityClient);
        lowLexis.setEventDatetime("2024-12-24 21:03:56");
        lowLexis.setEventType("account_creation");
        lowLexis.setConditionAttrib5("agent_mobile");
        lowLexis.setOs("testOS");
        lowLexis.setRiskRating("Low");
        lowLexis.setPolicyScore(-50);

        LnSessionParsedObject neutralLexis = generateLexisNexisDataByClient(activityClient);
        neutralLexis.setEventDatetime("2024-12-24 21:03:56");
        neutralLexis.setEventType("account_creation");
        neutralLexis.setConditionAttrib5("agent_mobile");
        neutralLexis.setOs("testOS");
        neutralLexis.setRiskRating("Neutral");
        neutralLexis.setPolicyScore(-50);

        LnSessionParsedObject trusredlLexis = generateLexisNexisDataByClient(activityClient);
        trusredlLexis.setEventDatetime("2024-12-24 21:03:56");
        trusredlLexis.setEventType("account_creation");
        trusredlLexis.setConditionAttrib5("agent_mobile");
        trusredlLexis.setOs("testOS");
        trusredlLexis.setRiskRating("Trusred");
        trusredlLexis.setPolicyScore(-50);

        ArrayList<LnSessionParsedObject> lexis = new ArrayList<>();
        lexis.add(lowLexis);
        lexis.add(MediumLexis);
        lexis.add(highLexis);
        lexis.add(neutralLexis);
        lexis.add(trusredlLexis);
        insertObjectsToDb(LEXIS_NEXIS_TABLE_NAME, lexis);

        sessionsTab.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        sessionsTab.navigate(activityClient.getUcid());
        sessionsTab.clickRiskFilter();
        sessionsTab.checkThatOptionPresented(highLexis.getRiskRating());
        sessionsTab.checkThatOptionPresented(lowLexis.getRiskRating());
        sessionsTab.checkThatOptionPresented(MediumLexis.getRiskRating());
        sessionsTab.checkThatOptionPresented(neutralLexis.getRiskRating());
        sessionsTab.checkThatOptionPresented(trusredlLexis.getRiskRating());
        sessionsTab.clickFilterOption(highLexis.getRiskRating());
        sessionsTab.checkRiskColumnValue(highLexis.getRiskRating(), 0);
        sessionsTab.clearFilterButton();
        sessionsTab.clickFilterOption(lowLexis.getRiskRating());
        sessionsTab.checkRiskColumnValue(lowLexis.getRiskRating(), 0);
        sessionsTab.clearFilterButton();
        sessionsTab.clickFilterOption(MediumLexis.getRiskRating());
        sessionsTab.checkRiskColumnValue(MediumLexis.getRiskRating(), 0);
        sessionsTab.clearFilterButton();
        sessionsTab.clickFilterOption(neutralLexis.getRiskRating());
        sessionsTab.checkRiskColumnValue(neutralLexis.getRiskRating(), 0);
        sessionsTab.clearFilterButton();
        sessionsTab.clickFilterOption(trusredlLexis.getRiskRating());
        sessionsTab.checkRiskColumnValue(trusredlLexis.getRiskRating(), 0);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Activity tab filter by date - Today")
    @AllureId("792")
    public void filterDateTodayTest() {
        deleteLexis(activityClient);

        Allure.step("Prepare data for DB with dates inside and outside filtered diapason ");
        LnSessionParsedObject todayLexis = generateLexisNexisDataByClient(activityClient);
        todayLexis.setEventDatetime(getCurrentTimestampDbFormat());
        todayLexis.setEventType("account_creation");
        todayLexis.setConditionAttrib5("agent_mobile");
        todayLexis.setOs("android");
        todayLexis.setRiskRating("high");
        todayLexis.setPolicyScore(-50);

        LnSessionParsedObject yesterdayLexis = generateLexisNexisDataByClient(activityClient);
        yesterdayLexis.setEventDatetime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 1, 0, 0));
        yesterdayLexis.setEventType("account_creation");
        yesterdayLexis.setConditionAttrib5("agent_mobile");
        yesterdayLexis.setOs("windows");
        yesterdayLexis.setRiskRating("Medium");
        yesterdayLexis.setPolicyScore(-50);

        ArrayList<LnSessionParsedObject> lexis = new ArrayList<>();
        lexis.add(todayLexis);
        lexis.add(yesterdayLexis);
        insertObjectsToDb(LEXIS_NEXIS_TABLE_NAME, lexis);

        sessionsTab.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        sessionsTab.navigate(activityClient.getUcid());
        sessionsTab.clickDateFilter();
        sessionsTab.clickFilterOption("Today");
        sessionsTab.checkDateColumnValue(todayLexis.getEventDatetime());
        sessionsTab.checkDateColumnValueNotPresented(yesterdayLexis.getEventDatetime());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Activity tab filter by date - Last 7 Days")
    @AllureId("819")
    public void filterDateLast7daysTest() {
        deleteLexis(activityClient);

        Allure.step(
                "Prepare data for DB with dates inside and outside filtered diapason: one today, one today - 6 days, and one today - 7 days ");
        LnSessionParsedObject todayLexis = generateLexisNexisDataByClient(activityClient);
        todayLexis.setEventDatetime(getCurrentTimestampDbFormat());
        todayLexis.setEventType("account_creation");
        todayLexis.setConditionAttrib5("mobile_browser");
        todayLexis.setOs("android");
        todayLexis.setRiskRating("high");
        todayLexis.setPolicyScore(-50);

        LnSessionParsedObject borderlineLexis = generateLexisNexisDataByClient(activityClient);
        borderlineLexis.setEventDatetime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 6, 0, 0));
        borderlineLexis.setEventType("account_creation");
        borderlineLexis.setConditionAttrib5("web_browser");
        borderlineLexis.setOs("MACOSS");
        borderlineLexis.setRiskRating("Medium");
        borderlineLexis.setPolicyScore(-50);

        LnSessionParsedObject outsideLexis = generateLexisNexisDataByClient(activityClient);
        outsideLexis.setEventDatetime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 7, 0, 0));
        outsideLexis.setEventType("account_creation");
        outsideLexis.setConditionAttrib5("agent_mobile");
        outsideLexis.setOs("Linux");
        outsideLexis.setRiskRating("Medium");
        outsideLexis.setPolicyScore(-50);

        ArrayList<LnSessionParsedObject> lexis = new ArrayList<>();
        lexis.add(todayLexis);
        lexis.add(borderlineLexis);
        lexis.add(outsideLexis);

        insertObjectsToDb(LEXIS_NEXIS_TABLE_NAME, lexis);

        sessionsTab.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        sessionsTab.navigate(activityClient.getUcid());
        sessionsTab.clickDateFilter();
        sessionsTab.clickFilterOption("Last 7 days");
        sessionsTab.checkDateColumnValue(todayLexis.getEventDatetime(), 1);
        sessionsTab.checkDateColumnValue(borderlineLexis.getEventDatetime(), 0);
        sessionsTab.checkDateColumnValueNotPresented(outsideLexis.getEventDatetime());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Activity tab filter by date - Last 30 Days")
    @AllureId("820")
    public void filterDateLast30daysTest() {
        deleteLexis(activityClient);

        Allure.step(
                "Prepare data for DB with dates inside and outside filtered diapason: one today, one today - 29 days, and one today - 30 days ");
        LnSessionParsedObject todayLexis = generateLexisNexisDataByClient(activityClient);
        todayLexis.setEventDatetime(getCurrentTimestampDbFormat());
        todayLexis.setEventType("account_creation");
        todayLexis.setConditionAttrib5("mobile_browser");
        todayLexis.setOs("android");
        todayLexis.setRiskRating("high");
        todayLexis.setPolicyScore(-50);

        LnSessionParsedObject borderlineLexis = generateLexisNexisDataByClient(activityClient);
        borderlineLexis.setEventDatetime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 29, 0, 0));
        borderlineLexis.setEventType("account_creation");
        borderlineLexis.setConditionAttrib5("web_browser");
        borderlineLexis.setOs("MACOSS");
        borderlineLexis.setRiskRating("Medium");
        borderlineLexis.setPolicyScore(-50);

        LnSessionParsedObject outsideLexis = generateLexisNexisDataByClient(activityClient);
        outsideLexis.setEventDatetime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 30, 0, 0));
        outsideLexis.setEventType("account_creation");
        outsideLexis.setConditionAttrib5("agent_mobile");
        outsideLexis.setOs("Linux");
        outsideLexis.setRiskRating("Medium");
        outsideLexis.setPolicyScore(-50);

        ArrayList<LnSessionParsedObject> lexis = new ArrayList<>();
        lexis.add(todayLexis);
        lexis.add(borderlineLexis);
        lexis.add(outsideLexis);

        insertObjectsToDb(LEXIS_NEXIS_TABLE_NAME, lexis);

        sessionsTab.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        sessionsTab.navigate(activityClient.getUcid());
        sessionsTab.clickDateFilter();
        sessionsTab.clickFilterOption("Last 30 days");
        sessionsTab.checkDateColumnValue(todayLexis.getEventDatetime(), 1);
        sessionsTab.checkDateColumnValue(borderlineLexis.getEventDatetime(), 0);
        sessionsTab.checkDateColumnValueNotPresented(outsideLexis.getEventDatetime());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Activity tab filter by date - Last 90 Days")
    @AllureId("821")
    public void filterDateLast90daysTest() {
        deleteLexis(activityClient);

        Allure.step(
                "Prepare data for DB with dates inside and outside filtered diapason: one today, one today - 90 days, and one today - 89 days ");
        LnSessionParsedObject todayLexis = generateLexisNexisDataByClient(activityClient);
        todayLexis.setEventDatetime(getCurrentTimestampDbFormat());
        todayLexis.setEventType("account_creation");
        todayLexis.setConditionAttrib5("mobile_browser");
        todayLexis.setOs("android");
        todayLexis.setRiskRating("high");
        todayLexis.setPolicyScore(-50);

        LnSessionParsedObject borderlineLexis = generateLexisNexisDataByClient(activityClient);
        borderlineLexis.setEventDatetime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 89, 0, 0));
        borderlineLexis.setEventType("account_creation");
        borderlineLexis.setConditionAttrib5("web_browser");
        borderlineLexis.setOs("MACOSS");
        borderlineLexis.setRiskRating("Medium");
        borderlineLexis.setPolicyScore(-50);

        LnSessionParsedObject outsideLexis = generateLexisNexisDataByClient(activityClient);
        outsideLexis.setEventDatetime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 90, 0, 0));
        outsideLexis.setEventType("account_creation");
        outsideLexis.setConditionAttrib5("agent_mobile");
        outsideLexis.setOs("Linux");
        outsideLexis.setRiskRating("Medium");
        outsideLexis.setPolicyScore(-50);

        ArrayList<LnSessionParsedObject> lexis = new ArrayList<>();
        lexis.add(todayLexis);
        lexis.add(borderlineLexis);
        lexis.add(outsideLexis);

        insertObjectsToDb(LEXIS_NEXIS_TABLE_NAME, lexis);

        sessionsTab.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        sessionsTab.navigate(activityClient.getUcid());
        sessionsTab.clickDateFilter();
        sessionsTab.clickFilterOption("Last 90 days");
        sessionsTab.checkDateColumnValue(todayLexis.getEventDatetime(), 1);
        sessionsTab.checkDateColumnValue(borderlineLexis.getEventDatetime(), 0);
        sessionsTab.checkDateColumnValueNotPresented(outsideLexis.getEventDatetime());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Activity tab filter by date - Custom dates")
    @AllureId("822")
    public void filterDateCustomTest() {
        deleteLexis(activityClient);

        Allure.step("Prepare data for DB with dates inside and outside filtered diapason.");
        LnSessionParsedObject todayLexis = generateLexisNexisDataByClient(activityClient);
        todayLexis.setEventDatetime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 5, 0, 0));
        todayLexis.setEventType("account_creation");
        todayLexis.setConditionAttrib5("mobile_browser");
        todayLexis.setOs("android");
        todayLexis.setRiskRating("high");
        todayLexis.setPolicyScore(-50);

        LnSessionParsedObject borderlineLexis = generateLexisNexisDataByClient(activityClient);
        borderlineLexis.setEventDatetime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 98, 0, 0));
        borderlineLexis.setEventType("account_creation");
        borderlineLexis.setConditionAttrib5("web_browser");
        borderlineLexis.setOs("MACOSS");
        borderlineLexis.setRiskRating("Medium");
        borderlineLexis.setPolicyScore(-50);

        LnSessionParsedObject outsideLexis = generateLexisNexisDataByClient(activityClient);
        outsideLexis.setEventDatetime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 99, 0, 0));
        outsideLexis.setEventType("account_creation");
        outsideLexis.setConditionAttrib5("agent_mobile");
        outsideLexis.setOs("Linux");
        outsideLexis.setRiskRating("Medium");
        outsideLexis.setPolicyScore(-50);

        ArrayList<LnSessionParsedObject> lexis = new ArrayList<>();
        lexis.add(todayLexis);
        lexis.add(borderlineLexis);
        lexis.add(outsideLexis);

        insertObjectsToDb(LEXIS_NEXIS_TABLE_NAME, lexis);

        sessionsTab.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        sessionsTab.navigate(activityClient.getUcid());
        sessionsTab.clickDateFilter();
        sessionsTab.setCustomDates(
                getCurrentTimestampMinusOffsetFormatted(DATE, 0, 0, 5, 0, 0),
                getCurrentTimestampMinusOffsetFormatted(DATE, 0, 0, 98, 0, 0));
        sessionsTab.checkDateColumnValue(todayLexis.getEventDatetime(), 1);
        sessionsTab.checkDateColumnValue(borderlineLexis.getEventDatetime(), 0);
        sessionsTab.checkDateColumnValueNotPresented(outsideLexis.getEventDatetime());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Activity tab sort by score")
    @AllureId("794")
    public void sortingScoreTest() {
        deleteLexis(activityClient);

        Allure.step("Prepare data for DB with dates inside and outside filtered diapason.");
        LnSessionParsedObject firstLexis = generateLexisNexisDataByClient(activityClient);
        firstLexis.setEventDatetime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 5, 0, 0));
        firstLexis.setEventType("account_creation");
        firstLexis.setConditionAttrib5("mobile_browser");
        firstLexis.setOs("android");
        firstLexis.setRiskRating("high");
        firstLexis.setPolicyScore(0);

        LnSessionParsedObject secondLexis = generateLexisNexisDataByClient(activityClient);
        secondLexis.setEventDatetime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 98, 0, 0));
        secondLexis.setEventType("account_creation");
        secondLexis.setConditionAttrib5("web_browser");
        secondLexis.setOs("MACOSS");
        secondLexis.setRiskRating("Medium");
        secondLexis.setPolicyScore(5);

        LnSessionParsedObject thirdLexis = generateLexisNexisDataByClient(activityClient);
        thirdLexis.setEventDatetime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 99, 0, 0));
        thirdLexis.setEventType("account_creation");
        thirdLexis.setConditionAttrib5("agent_mobile");
        thirdLexis.setOs("Linux");
        thirdLexis.setRiskRating("Medium");
        thirdLexis.setPolicyScore(-50);

        LnSessionParsedObject fourthLexis = generateLexisNexisDataByClient(activityClient);
        fourthLexis.setEventDatetime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 99, 0, 0));
        fourthLexis.setEventType("account_creation");
        fourthLexis.setConditionAttrib5("agent_mobile");
        fourthLexis.setOs("Linux");
        fourthLexis.setRiskRating("Medium");
        fourthLexis.setPolicyScore(50);

        ArrayList<LnSessionParsedObject> lexis = new ArrayList<>();
        lexis.add(firstLexis);
        lexis.add(secondLexis);
        lexis.add(thirdLexis);
        lexis.add(fourthLexis);

        insertObjectsToDb(LEXIS_NEXIS_TABLE_NAME, lexis);

        sessionsTab.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        sessionsTab.navigate(activityClient.getUcid());
        sessionsTab.setSortByScoreDesc();
        sessionsTab.checkScoreColumnIsDesc();
        sessionsTab.setSortByScoreAsc();
        sessionsTab.checkScoreColumnIsAsc();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Activity tab sort by date")
    @AllureId("793")
    public void sortingDateTest() {
        deleteLexis(activityClient);

        Allure.step("Prepare data for DB with dates inside and outside filtered diapason.");
        LnSessionParsedObject firstLexis = generateLexisNexisDataByClient(activityClient);
        firstLexis.setEventDatetime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 5, 0, 0));
        firstLexis.setEventType("account_creation");
        firstLexis.setConditionAttrib5("mobile_browser");
        firstLexis.setOs("android");
        firstLexis.setRiskRating("high");
        firstLexis.setPolicyScore(0);

        LnSessionParsedObject secondLexis = generateLexisNexisDataByClient(activityClient);
        secondLexis.setEventDatetime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 98, 0, 0));
        secondLexis.setEventType("account_creation");
        secondLexis.setConditionAttrib5("web_browser");
        secondLexis.setOs("MACOSS");
        secondLexis.setRiskRating("Medium");
        secondLexis.setPolicyScore(5);

        LnSessionParsedObject thirdLexis = generateLexisNexisDataByClient(activityClient);
        thirdLexis.setEventDatetime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 99, 0, 0));
        thirdLexis.setEventType("account_creation");
        thirdLexis.setConditionAttrib5("agent_mobile");
        thirdLexis.setOs("Linux");
        thirdLexis.setRiskRating("Medium");
        thirdLexis.setPolicyScore(-50);

        LnSessionParsedObject fourthLexis = generateLexisNexisDataByClient(activityClient);
        fourthLexis.setEventDatetime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 99, 0, 0));
        fourthLexis.setEventType("account_creation");
        fourthLexis.setConditionAttrib5("agent_mobile");
        fourthLexis.setOs("Linux");
        fourthLexis.setRiskRating("Medium");
        fourthLexis.setPolicyScore(50);

        ArrayList<LnSessionParsedObject> lexis = new ArrayList<>();
        lexis.add(firstLexis);
        lexis.add(secondLexis);
        lexis.add(thirdLexis);
        lexis.add(fourthLexis);

        insertObjectsToDb(LEXIS_NEXIS_TABLE_NAME, lexis);

        sessionsTab.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        sessionsTab.navigate(activityClient.getUcid());
        sessionsTab.setSortByDateDesc();
        sessionsTab.checkDateColumnIsDesc();
        sessionsTab.setSortByDateAsc();
        sessionsTab.checkDateColumnIsAsc();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Activity tab - Summary, risk score section test")
    @AllureId("834")
    public void summaryRiskScoreShowDataFromDи() {

        deleteLexis(activityClient);

        LnSessionParsedObject firstLexis = generateLexisNexisDataByClient(activityClient);
        firstLexis.setEventDatetime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 5, 0, 0));
        firstLexis.setEventType("account_creation");
        firstLexis.setConditionAttrib5("mobile_app");
        firstLexis.setOs("android");
        firstLexis.setRiskRating("high");
        firstLexis.setPolicyScore(99);
        firstLexis.setEmailageEmailriskscoreEascore(11);
        firstLexis.setEmailageEmailriskscoreEariskbandid(22);
        firstLexis.setEmailageEmailriskscoreEaadvice("test advice");

        Allure.step("Prepare data for DB with riskRating = " + firstLexis.getRiskRating() + ", and policyScore = "
                + firstLexis.getPolicyScore());

        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, firstLexis);

        sessionsTab.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        sessionsTab.navigate(activityClient.getUcid());
        sessionsTab.clickOnDataRow();
        sessionsTab.checkScoreHeaderValueScoreLine(firstLexis.getPolicyScore());
        sessionsTab.positiveScoreMeterDisplayed();
        sessionsTab.positiveScoreMeterDisplayed();
        sessionsTab.setSuccessLabelDisplayed();
        sessionsTab.checkTextInLabel(firstLexis.getRiskRating());
        sessionsTab.checkPositionOfTheLineDivider("99.5");

        deleteLexis(activityClient);

        firstLexis.setRiskRating("Low");
        firstLexis.setPolicyScore(50);

        Allure.step("Prepare data for DB with riskRating = " + firstLexis.getRiskRating() + ", and policyScore = "
                + firstLexis.getPolicyScore());

        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, firstLexis);

        page.reload();
        sessionsTab.clickOnDataRow();
        sessionsTab.checkScoreHeaderValueScoreLine(firstLexis.getPolicyScore());
        sessionsTab.positiveScoreMeterDisplayed();
        sessionsTab.positiveScoreMeterDisplayed();
        sessionsTab.setSuccessLabelDisplayed();
        sessionsTab.checkTextInLabel(firstLexis.getRiskRating());
        sessionsTab.checkPositionOfTheLineDivider("75");

        deleteLexis(activityClient);

        firstLexis.setRiskRating("Medium");
        firstLexis.setPolicyScore(0);

        Allure.step("Prepare data for DB with riskRating = " + firstLexis.getRiskRating() + ", and policyScore = "
                + firstLexis.getPolicyScore());

        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, firstLexis);

        page.reload();
        sessionsTab.clickOnDataRow();
        sessionsTab.checkScoreHeaderValueScoreLine(firstLexis.getPolicyScore());
        sessionsTab.checkNeutralLabelDisplayed();
        sessionsTab.checkTextInLabel(firstLexis.getRiskRating());
        sessionsTab.checkPositionOfTheLineDivider("50");

        deleteLexis(activityClient);

        firstLexis.setRiskRating("trusted");
        firstLexis.setPolicyScore(-50);

        Allure.step("Prepare data for DB with riskRating = " + firstLexis.getRiskRating() + ", and policyScore = "
                + firstLexis.getPolicyScore());

        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, firstLexis);

        page.reload();
        sessionsTab.clickOnDataRow();
        sessionsTab.checkScoreHeaderValueScoreLine(firstLexis.getPolicyScore());
        sessionsTab.checkDangerLabelDisplayed();
        sessionsTab.checkTextInLabel(firstLexis.getRiskRating());
        sessionsTab.checkPositionOfTheLineDivider("25");

        deleteLexis(activityClient);

        firstLexis.setRiskRating("Low");
        firstLexis.setPolicyScore(-99);

        Allure.step("Prepare data for DB with riskRating = " + firstLexis.getRiskRating() + ", and policyScore = "
                + firstLexis.getPolicyScore());

        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, firstLexis);

        page.reload();
        sessionsTab.clickOnDataRow();
        sessionsTab.checkScoreHeaderValueScoreLine(firstLexis.getPolicyScore());
        sessionsTab.checkDangerLabelDisplayed();
        sessionsTab.checkTextInLabel(firstLexis.getRiskRating());
        sessionsTab.checkPositionOfTheLineDivider("0.5");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Activity tab - Summary, summary section test")
    @AllureId("1789")
    public void summarySummaryShowDataFromDb() {

        deleteLexis(activityClient);

        LnSessionParsedObject firstLexis = generateLexisNexisDataByClient(activityClient);
        firstLexis.setEventDatetime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 5, 0, 0));
        firstLexis.setEventType("account_creation");
        firstLexis.setConditionAttrib5("mobile_app");
        firstLexis.setOs("android");
        firstLexis.setRiskRating("high");
        firstLexis.setPolicyScore(99);
        firstLexis.setEmailageEmailriskscoreEascore(11);
        firstLexis.setEmailageEmailriskscoreEariskbandid(22);
        firstLexis.setEmailageEmailriskscoreEaadvice("test advice");
        firstLexis.setSummaryReasonCodeOriginal(
                "[ \"Connection via Mobile Hotspot\", \"First Time Mobile Hotspot  Used\", \"Computer browser used\" ]");

        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, firstLexis);

        sessionsTab.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        sessionsTab.navigate(activityClient.getUcid());
        sessionsTab.clickOnDataRow();
        assertThat(sessionsTab.getSummarySectionTitle(), is("Summary"));
        assertThat(
                sessionsTab.getSummarySectionText(),
                is("Connection via Mobile HotspotFirst Time Mobile Hotspot  UsedComputer browser used"));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Activity tab - Summary, applied policies section test")
    @AllureId("836")
    public void summaryAppliedPoliciesShowDataFromDb() {

        deleteLexis(activityClient);

        LnSessionParsedObject firstLexis = generateLexisNexisDataByClient(activityClient);
        firstLexis.setEventDatetime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 5, 0, 0));
        firstLexis.setEventType("account_creation");
        firstLexis.setConditionAttrib5("mobile_app");
        firstLexis.setOs("android");
        firstLexis.setRiskRating("high");
        firstLexis.setPolicyScore(99);
        firstLexis.setEmailageEmailriskscoreEascore(11);
        firstLexis.setEmailageEmailriskscoreEariskbandid(22);
        firstLexis.setEmailageEmailriskscoreEaadvice("test advice");

        Allure.step("Prepare data for DB with riskRating = " + firstLexis.getRiskRating() + ", and policyScore = "
                + firstLexis.getPolicyScore());

        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, firstLexis);

        sessionsTab.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        sessionsTab.navigate(activityClient.getUcid());
        sessionsTab.clickOnDataRow();
        sessionsTab.checkTitleOfPoliciesSection("Applied policies");
        sessionsTab.checkRuleName("testRule");
        sessionsTab.checkRuleScore("-10", "testRule");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Activity tab - Summary, TMX reason section test")
    @AllureId("842")
    public void summaryTmxShowDataFromDB() {

        deleteLexis(activityClient);

        LnSessionParsedObject firstLexis = generateLexisNexisDataByClient(activityClient);
        firstLexis.setEventDatetime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 5, 0, 0));
        firstLexis.setEventType("account_creation");
        firstLexis.setConditionAttrib5("mobile_app");
        firstLexis.setOs("android");
        firstLexis.setRiskRating("high");
        firstLexis.setPolicyScore(99);
        firstLexis.setEmailageEmailriskscoreEascore(11);
        firstLexis.setEmailageEmailriskscoreEariskbandid(22);

        Allure.step("Prepare data for DB with the tmxSummaryReasonCode equals " + firstLexis.getTmxSummaryReasonCode());

        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, firstLexis);

        sessionsTab.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        sessionsTab.navigate(activityClient.getUcid());
        sessionsTab.clickOnDataRow();
        sessionsTab.checkTitleOfTmxReasonSection("TMX reason codes");
        sessionsTab.checkThatTmxReasonCodeIsDisplayed(firstLexis.getTmxSummaryReasonCode());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Activity tab - Summary, score section test")
    @AllureId("843")
    public void summaryScoresDataFromDb() {

        deleteLexis(activityClient);

        LnSessionParsedObject firstLexis = generateLexisNexisDataByClient(activityClient);
        firstLexis.setEventDatetime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 5, 0, 0));
        firstLexis.setEventType("account_creation");
        firstLexis.setConditionAttrib5("mobile_app");
        firstLexis.setOs("android");
        firstLexis.setRiskRating("high");
        firstLexis.setPolicyScore(99);

        firstLexis.setEmailageEmailriskscoreEascore(1);
        firstLexis.setEmailageEmailriskscoreEaadvice("some advice" + getCurrentTimestampSeconds());

        firstLexis.setEmailageEmailriskscoreIpRisklevel("Review");

        firstLexis.setEmailageEmailriskscoreOveralldigitalidentityscore(100);
        firstLexis.setEmailageEmailriskscoreDisdescription("some description" + getCurrentTimestampSeconds());

        Allure.step("Prepare data for DB with the first set of data");
        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, firstLexis);

        sessionsTab.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        sessionsTab.navigate(activityClient.getUcid());
        sessionsTab.clickOnDataRow();
        sessionsTab.checkIpScoreRiskLevelValue(firstLexis.getEmailageEmailriskscoreIpRisklevel());
        sessionsTab.checkIpScoreRiskLevelNumber(firstLexis.getEmailageEmailriskscoreIpRisklevel());
        sessionsTab.checkIpScoreRiskBarStyle(firstLexis.getEmailageEmailriskscoreIpRisklevel());
        sessionsTab.checkEmailScoreAdvice(firstLexis.getEmailageEmailriskscoreEaadvice());
        sessionsTab.checkEmailScoreValue(firstLexis.getEmailageEmailriskscoreEascore());
        sessionsTab.checkEmailScoreRiskBarStyle(firstLexis.getEmailageEmailriskscoreEascore());
        sessionsTab.checkDigitalIdentityScoreNumber(firstLexis.getEmailageEmailriskscoreOveralldigitalidentityscore());
        sessionsTab.checkDigitalIdentityScoreTitle(firstLexis.getEmailageEmailriskscoreDisdescription());
        sessionsTab.checkDigitalIdentityScoreRiskBarStyle(
                firstLexis.getEmailageEmailriskscoreOveralldigitalidentityscore());

        deleteLexis(activityClient);
        // email
        firstLexis.setEmailageEmailriskscoreEascore(300);
        firstLexis.setEmailageEmailriskscoreEaadvice("some advice" + getCurrentTimestampSeconds());
        // ip
        firstLexis.setEmailageEmailriskscoreIpRisklevel("Very low");
        // digital identity
        firstLexis.setEmailageEmailriskscoreOveralldigitalidentityscore(80);
        firstLexis.setEmailageEmailriskscoreDisdescription("some description" + getCurrentTimestampSeconds());

        Allure.step("Prepare data for DB with the new set of data");
        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, firstLexis);
        sessionsTab.navigate(activityClient.getUcid());
        sessionsTab.clickOnDataRow();
        sessionsTab.checkIpScoreRiskLevelValue(firstLexis.getEmailageEmailriskscoreIpRisklevel());
        sessionsTab.checkIpScoreRiskLevelNumber(firstLexis.getEmailageEmailriskscoreIpRisklevel());
        sessionsTab.checkIpScoreRiskBarStyle(firstLexis.getEmailageEmailriskscoreIpRisklevel());
        sessionsTab.checkEmailScoreAdvice(firstLexis.getEmailageEmailriskscoreEaadvice());
        sessionsTab.checkEmailScoreValue(firstLexis.getEmailageEmailriskscoreEascore());
        sessionsTab.checkEmailScoreRiskBarStyle(firstLexis.getEmailageEmailriskscoreEascore());
        sessionsTab.checkDigitalIdentityScoreNumber(firstLexis.getEmailageEmailriskscoreOveralldigitalidentityscore());
        sessionsTab.checkDigitalIdentityScoreTitle(firstLexis.getEmailageEmailriskscoreDisdescription());
        sessionsTab.checkDigitalIdentityScoreRiskBarStyle(
                firstLexis.getEmailageEmailriskscoreOveralldigitalidentityscore());

        deleteLexis(activityClient);
        // email
        firstLexis.setEmailageEmailriskscoreEascore(301);
        firstLexis.setEmailageEmailriskscoreEaadvice("some advice" + getCurrentTimestampSeconds());
        // ip
        firstLexis.setEmailageEmailriskscoreIpRisklevel("Low");
        // digital identity
        firstLexis.setEmailageEmailriskscoreOveralldigitalidentityscore(79);
        firstLexis.setEmailageEmailriskscoreDisdescription("some description" + getCurrentTimestampSeconds());

        Allure.step("Prepare data for DB with the new set of data");
        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, firstLexis);
        sessionsTab.navigate(activityClient.getUcid());
        sessionsTab.clickOnDataRow();
        sessionsTab.checkIpScoreRiskLevelValue(firstLexis.getEmailageEmailriskscoreIpRisklevel());
        sessionsTab.checkIpScoreRiskLevelNumber(firstLexis.getEmailageEmailriskscoreIpRisklevel());
        sessionsTab.checkIpScoreRiskBarStyle(firstLexis.getEmailageEmailriskscoreIpRisklevel());
        sessionsTab.checkEmailScoreAdvice(firstLexis.getEmailageEmailriskscoreEaadvice());
        sessionsTab.checkEmailScoreValue(firstLexis.getEmailageEmailriskscoreEascore());
        sessionsTab.checkEmailScoreRiskBarStyle(firstLexis.getEmailageEmailriskscoreEascore());
        sessionsTab.checkDigitalIdentityScoreNumber(firstLexis.getEmailageEmailriskscoreOveralldigitalidentityscore());
        sessionsTab.checkDigitalIdentityScoreTitle(firstLexis.getEmailageEmailriskscoreDisdescription());
        sessionsTab.checkDigitalIdentityScoreRiskBarStyle(
                firstLexis.getEmailageEmailriskscoreOveralldigitalidentityscore());

        deleteLexis(activityClient);
        // email
        firstLexis.setEmailageEmailriskscoreEascore(600);
        // ip
        firstLexis.setEmailageEmailriskscoreIpRisklevel("Moderate");
        // digital identity
        firstLexis.setEmailageEmailriskscoreOveralldigitalidentityscore(60);

        Allure.step("Prepare data for DB with the new set of data");
        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, firstLexis);
        sessionsTab.navigate(activityClient.getUcid());
        sessionsTab.clickOnDataRow();
        sessionsTab.checkIpScoreRiskLevelValue(firstLexis.getEmailageEmailriskscoreIpRisklevel());
        sessionsTab.checkIpScoreRiskLevelNumber(firstLexis.getEmailageEmailriskscoreIpRisklevel());
        sessionsTab.checkIpScoreRiskBarStyle(firstLexis.getEmailageEmailriskscoreIpRisklevel());
        sessionsTab.checkEmailScoreAdvice(firstLexis.getEmailageEmailriskscoreEaadvice());
        sessionsTab.checkEmailScoreValue(firstLexis.getEmailageEmailriskscoreEascore());
        sessionsTab.checkEmailScoreRiskBarStyle(firstLexis.getEmailageEmailriskscoreEascore());
        sessionsTab.checkDigitalIdentityScoreNumber(firstLexis.getEmailageEmailriskscoreOveralldigitalidentityscore());
        sessionsTab.checkDigitalIdentityScoreTitle(firstLexis.getEmailageEmailriskscoreDisdescription());
        sessionsTab.checkDigitalIdentityScoreRiskBarStyle(
                firstLexis.getEmailageEmailriskscoreOveralldigitalidentityscore());

        deleteLexis(activityClient);
        // email
        firstLexis.setEmailageEmailriskscoreEascore(601);
        // ip
        firstLexis.setEmailageEmailriskscoreIpRisklevel("High");
        // digital identity
        firstLexis.setEmailageEmailriskscoreOveralldigitalidentityscore(59);

        Allure.step("Prepare data for DB with the new set of data");
        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, firstLexis);
        sessionsTab.navigate(activityClient.getUcid());
        sessionsTab.clickOnDataRow();
        sessionsTab.checkIpScoreRiskLevelValue(firstLexis.getEmailageEmailriskscoreIpRisklevel());
        sessionsTab.checkIpScoreRiskLevelNumber(firstLexis.getEmailageEmailriskscoreIpRisklevel());
        sessionsTab.checkIpScoreRiskBarStyle(firstLexis.getEmailageEmailriskscoreIpRisklevel());
        sessionsTab.checkEmailScoreAdvice(firstLexis.getEmailageEmailriskscoreEaadvice());
        sessionsTab.checkEmailScoreValue(firstLexis.getEmailageEmailriskscoreEascore());
        sessionsTab.checkEmailScoreRiskBarStyle(firstLexis.getEmailageEmailriskscoreEascore());
        sessionsTab.checkDigitalIdentityScoreNumber(firstLexis.getEmailageEmailriskscoreOveralldigitalidentityscore());
        sessionsTab.checkDigitalIdentityScoreTitle(firstLexis.getEmailageEmailriskscoreDisdescription());
        sessionsTab.checkDigitalIdentityScoreRiskBarStyle(
                firstLexis.getEmailageEmailriskscoreOveralldigitalidentityscore());

        deleteLexis(activityClient);
        // email
        firstLexis.setEmailageEmailriskscoreEascore(799);
        // ip
        firstLexis.setEmailageEmailriskscoreIpRisklevel("Very high");
        // digital identity
        firstLexis.setEmailageEmailriskscoreOveralldigitalidentityscore(40);

        Allure.step("Prepare data for DB with the new set of data");
        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, firstLexis);
        sessionsTab.navigate(activityClient.getUcid());
        sessionsTab.clickOnDataRow();
        sessionsTab.checkIpScoreRiskLevelValue(firstLexis.getEmailageEmailriskscoreIpRisklevel());
        sessionsTab.checkIpScoreRiskLevelNumber(firstLexis.getEmailageEmailriskscoreIpRisklevel());
        sessionsTab.checkIpScoreRiskBarStyle(firstLexis.getEmailageEmailriskscoreIpRisklevel());
        sessionsTab.checkEmailScoreAdvice(firstLexis.getEmailageEmailriskscoreEaadvice());
        sessionsTab.checkEmailScoreValue(firstLexis.getEmailageEmailriskscoreEascore());
        sessionsTab.checkEmailScoreRiskBarStyle(firstLexis.getEmailageEmailriskscoreEascore());
        sessionsTab.checkDigitalIdentityScoreNumber(firstLexis.getEmailageEmailriskscoreOveralldigitalidentityscore());
        sessionsTab.checkDigitalIdentityScoreTitle(firstLexis.getEmailageEmailriskscoreDisdescription());
        sessionsTab.checkDigitalIdentityScoreRiskBarStyle(
                firstLexis.getEmailageEmailriskscoreOveralldigitalidentityscore());

        deleteLexis(activityClient);
        // email
        firstLexis.setEmailageEmailriskscoreEascore(800);
        // ip
        firstLexis.setEmailageEmailriskscoreIpRisklevel(null);
        // digital identity
        firstLexis.setEmailageEmailriskscoreOveralldigitalidentityscore(39);

        Allure.step("Prepare data for DB with the new set of data");
        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, firstLexis);
        sessionsTab.navigate(activityClient.getUcid());
        sessionsTab.clickOnDataRow();
        sessionsTab.checkIpScoreRiskLevelValue(firstLexis.getEmailageEmailriskscoreIpRisklevel());
        sessionsTab.checkIpScoreRiskLevelNumber(firstLexis.getEmailageEmailriskscoreIpRisklevel());
        sessionsTab.checkIpScoreRiskBarStyle(firstLexis.getEmailageEmailriskscoreIpRisklevel());
        sessionsTab.checkEmailScoreAdvice(firstLexis.getEmailageEmailriskscoreEaadvice());
        sessionsTab.checkEmailScoreValue(firstLexis.getEmailageEmailriskscoreEascore());
        sessionsTab.checkEmailScoreRiskBarStyle(firstLexis.getEmailageEmailriskscoreEascore());
        sessionsTab.checkDigitalIdentityScoreNumber(firstLexis.getEmailageEmailriskscoreOveralldigitalidentityscore());
        sessionsTab.checkDigitalIdentityScoreTitle(firstLexis.getEmailageEmailriskscoreDisdescription());
        sessionsTab.checkDigitalIdentityScoreRiskBarStyle(
                firstLexis.getEmailageEmailriskscoreOveralldigitalidentityscore());

        deleteLexis(activityClient);
        // email
        firstLexis.setEmailageEmailriskscoreEascore(999);
        // ip
        firstLexis.setEmailageEmailriskscoreIpRisklevel(null);
        // digital identity
        firstLexis.setEmailageEmailriskscoreOveralldigitalidentityscore(1);

        Allure.step("Prepare data for DB with the new set of data");
        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, firstLexis);
        sessionsTab.navigate(activityClient.getUcid());
        sessionsTab.clickOnDataRow();
        sessionsTab.checkIpScoreRiskLevelValue(firstLexis.getEmailageEmailriskscoreIpRisklevel());
        sessionsTab.checkIpScoreRiskLevelNumber(firstLexis.getEmailageEmailriskscoreIpRisklevel());
        sessionsTab.checkIpScoreRiskBarStyle(firstLexis.getEmailageEmailriskscoreIpRisklevel());
        sessionsTab.checkEmailScoreAdvice(firstLexis.getEmailageEmailriskscoreEaadvice());
        sessionsTab.checkEmailScoreValue(firstLexis.getEmailageEmailriskscoreEascore());
        sessionsTab.checkEmailScoreRiskBarStyle(firstLexis.getEmailageEmailriskscoreEascore());
        sessionsTab.checkDigitalIdentityScoreNumber(firstLexis.getEmailageEmailriskscoreOveralldigitalidentityscore());
        sessionsTab.checkDigitalIdentityScoreTitle(firstLexis.getEmailageEmailriskscoreDisdescription());
        sessionsTab.checkDigitalIdentityScoreRiskBarStyle(
                firstLexis.getEmailageEmailriskscoreOveralldigitalidentityscore());

        deleteLexis(activityClient);
        // email
        firstLexis.setEmailageEmailriskscoreEascore(0);
        // ip
        firstLexis.setEmailageEmailriskscoreIpRisklevel(null);
        // digital identity
        firstLexis.setEmailageEmailriskscoreOveralldigitalidentityscore(null);

        Allure.step("Prepare data for DB with the new set of data");
        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, firstLexis);
        sessionsTab.navigate(activityClient.getUcid());
        sessionsTab.clickOnDataRow();
        sessionsTab.checkIpScoreRiskLevelValue(firstLexis.getEmailageEmailriskscoreIpRisklevel());
        sessionsTab.checkIpScoreRiskLevelNumber(firstLexis.getEmailageEmailriskscoreIpRisklevel());
        sessionsTab.checkIpScoreRiskBarStyle(firstLexis.getEmailageEmailriskscoreIpRisklevel());
        sessionsTab.checkEmailScoreAdvice(firstLexis.getEmailageEmailriskscoreEaadvice());
        sessionsTab.checkEmailScoreValue(firstLexis.getEmailageEmailriskscoreEascore());
        sessionsTab.checkEmailScoreRiskBarStyle(firstLexis.getEmailageEmailriskscoreEascore());
        sessionsTab.checkDigitalIdentityScoreNumber(firstLexis.getEmailageEmailriskscoreOveralldigitalidentityscore());
        sessionsTab.checkDigitalIdentityScoreTitle(firstLexis.getEmailageEmailriskscoreDisdescription());
        sessionsTab.checkDigitalIdentityScoreRiskBarStyle(
                firstLexis.getEmailageEmailriskscoreOveralldigitalidentityscore());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Activity tab - Emailage sub-tab test")
    @AllureId("863")
    public void emailageDataFromDb() {

        deleteLexis(activityClient);

        LnSessionParsedObject firstLexis = generateLexisNexisDataByClient(activityClient);
        firstLexis.setEventDatetime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 5, 0, 0));
        firstLexis.setEventType("account_creation");
        firstLexis.setConditionAttrib5("mobile_app");
        firstLexis.setOs("android");
        firstLexis.setRiskRating("high");
        firstLexis.setPolicyScore(99);

        firstLexis.setEmailageEmailriskscoreEascore(RandomGenerator.getDefault().nextInt(0, 101));
        firstLexis.setEmailageEmailriskscoreEaadvice("some advice" + getCurrentTimestampSeconds());
        firstLexis.setEmailageEmailriskscoreEareason("some email reason" + getCurrentTimestampSeconds());
        firstLexis.setEmailageEmailriskscoreEmailCreationDays(5);
        firstLexis.setEmailageEmailriskscoreEmailage("2007-11-19 06:58:43");

        firstLexis.setEmailageEmailriskscorePhonecarriertype("phoneCarrier" + getCurrentTimestampSeconds());
        firstLexis.setEmailageEmailriskscorePhoneownermatch("U");
        firstLexis.setEmailageEmailriskscorePhonetofullnameconfidence(
                RandomGenerator.getDefault().nextInt(1, 101));
        firstLexis.setEmailageEmailriskscorePhonetolastnameconfidence(
                RandomGenerator.getDefault().nextInt(1, 101));

        firstLexis.setEmailageEmailriskscoreIpRisklevel("Review");
        firstLexis.setEmailageEmailriskscoreIpRiskreason("some IP reason" + getCurrentTimestampSeconds());

        firstLexis.setEmailageEmailriskscoreDomainrisklevel("some domain risk level" + getCurrentTimestampSeconds());
        firstLexis.setEmailageEmailriskscoreDomainCreationDays(
                RandomGenerator.getDefault().nextInt(0, 101));
        firstLexis.setEmailageEmailriskscoreDomainage("2007-11-19 06:58:43");
        firstLexis.setEmailageEmailriskscoreDomainexists("Not Sure");
        firstLexis.setEmailageEmailriskscoreDomaincategory("some domain category" + getCurrentTimestampSeconds());
        firstLexis.setEmailageEmailriskscoreDomainname("some domain name" + getCurrentTimestampSeconds());
        firstLexis.setEmailageEmailriskscoreDomaincompany("some domain company" + getCurrentTimestampSeconds());
        firstLexis.setEmailageEmailriskscoreDomaincountry(
                Country.getRandomCountry().getCountryCode());

        Allure.step("Prepare data for DB with the first set of data");
        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, firstLexis);

        sessionsTab.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        sessionsTab.navigate(activityClient.getUcid());
        sessionsTab.clickOnDataRow();
        sessionsTab.openTabEmailage();
        sessionsTab.checkValueOfSubTableRow("Email", "fraud risk", firstLexis.getEmailageEmailriskscoreEaadvice());
        sessionsTab.checkValueOfSubTableRow("Email", "score", firstLexis.getEmailageEmailriskscoreEascore());
        sessionsTab.checkValueOfSubTableRow("Email", "reason", firstLexis.getEmailageEmailriskscoreEareason());
        //        activityTab.checkValueOfSubTableRow("Email", "created",firstLexis.emailageEmailriskscoreEareason);

        sessionsTab.checkValueOfSubTableRow("Phone", "type", firstLexis.getEmailageEmailriskscorePhonecarriertype());
        sessionsTab.checkValueOfSubTableRowPhoneOwner(firstLexis.getEmailageEmailriskscorePhoneownermatch());
        sessionsTab.checkValueOfSubTableRowNAmeConfidence(
                "Phone", "full name", firstLexis.getEmailageEmailriskscorePhonetofullnameconfidence());
        sessionsTab.checkValueOfSubTableRowNAmeConfidence(
                "Phone", "last name", firstLexis.getEmailageEmailriskscorePhonetolastnameconfidence());

        sessionsTab.checkValueOfSubTableRow(
                "IP address", "risk level", firstLexis.getEmailageEmailriskscoreIpRisklevel());
        sessionsTab.checkValueOfSubTableRow("IP address", "reason", firstLexis.getEmailageEmailriskscoreIpRiskreason());

        sessionsTab.checkValueOfSubTableRow(
                "Domain", "risk level", firstLexis.getEmailageEmailriskscoreDomainrisklevel());
        //        activityTab.checkValueOfSubTableRow("Domain",
        // "created",firstLexis.emailageEmailriskscoreDomainCreationDays);
        sessionsTab.checkValueOfSubTableRow("Domain", "exists", firstLexis.getEmailageEmailriskscoreDomainexists());
        sessionsTab.checkValueOfSubTableRow("Domain", "category", firstLexis.getEmailageEmailriskscoreDomaincategory());
        sessionsTab.checkValueOfSubTableRow("Domain", "name", firstLexis.getEmailageEmailriskscoreDomainname());
        sessionsTab.checkValueOfSubTableRow("Domain", "company", firstLexis.getEmailageEmailriskscoreDomaincompany());
        sessionsTab.checkValueOfSubTableRowDomainCountryByCode(firstLexis.getEmailageEmailriskscoreDomaincountry());

        deleteLexis(activityClient);

        firstLexis.setEmailageEmailriskscorePhoneownermatch("N");
        firstLexis.setEmailageEmailriskscoreDomaincountry(
                Country.getRandomCountry().getCountryCode());

        Allure.step("Prepare data for DB with the another set of data");

        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, firstLexis);

        sessionsTab.navigate(activityClient.getUcid());
        sessionsTab.clickOnDataRow();
        sessionsTab.openTabEmailage();
        sessionsTab.checkValueOfSubTableRowPhoneOwner(firstLexis.getEmailageEmailriskscorePhoneownermatch());
        sessionsTab.checkValueOfSubTableRowDomainCountryByCode(firstLexis.getEmailageEmailriskscoreDomaincountry());

        deleteLexis(activityClient);

        firstLexis.setEmailageEmailriskscorePhoneownermatch("Y");
        firstLexis.setEmailageEmailriskscoreDomaincountry(
                Country.getRandomCountry().getCountryCode());

        Allure.step("Prepare data for DB with the another set of data");

        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, firstLexis);

        sessionsTab.navigate(activityClient.getUcid());
        sessionsTab.clickOnDataRow();
        sessionsTab.openTabEmailage();
        sessionsTab.checkValueOfSubTableRowPhoneOwner(firstLexis.getEmailageEmailriskscorePhoneownermatch());
        sessionsTab.checkValueOfSubTableRowDomainCountryByCode(firstLexis.getEmailageEmailriskscoreDomaincountry());

        deleteLexis(activityClient);

        firstLexis.setEmailageEmailriskscorePhoneownermatch("P");
        firstLexis.setEmailageEmailriskscoreDomaincountry(
                Country.getRandomCountry().getCountryCode());

        Allure.step("Prepare data for DB with the another set of data");

        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, firstLexis);

        sessionsTab.navigate(activityClient.getUcid());
        sessionsTab.clickOnDataRow();
        sessionsTab.openTabEmailage();
        sessionsTab.checkValueOfSubTableRowPhoneOwner(firstLexis.getEmailageEmailriskscorePhoneownermatch());
        sessionsTab.checkValueOfSubTableRowDomainCountryByCode(firstLexis.getEmailageEmailriskscoreDomaincountry());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Activity tab - Device sub-tab test, Device")
    @AllureId("864")
    public void deviceDataFromDb() {

        deleteLexis(activityClient);

        LnSessionParsedObject firstLexis = generateLexisNexisDataByClient(activityClient);
        firstLexis.setEventDatetime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 5, 0, 0));
        firstLexis.setEventType("account_creation");
        firstLexis.setRiskRating("high");
        firstLexis.setPolicyScore(RandomGenerator.getDefault().nextInt(0, 101));

        firstLexis.setConditionAttrib5("agent_mobile");
        firstLexis.setOs("android");
        firstLexis.setOsVersion("android" + getCurrentTimestampSeconds());
        firstLexis.setAgentBrand("brand1, brand2 " + getCurrentTimestampSeconds());
        firstLexis.setAgentModel("agent model" + getCurrentTimestampSeconds());
        firstLexis.setDeviceName("human readable device name " + getCurrentTimestampSeconds());
        firstLexis.setAgentLanguage("en-US");
        firstLexis.setScreenRes("1515x" + getCurrentTimestampSeconds());
        firstLexis.setDeviceId(null);

        Allure.step(
                "Prepare data for DB with the first set of data. set conditionAttrib5 = \"agent_mobile\", so if condition_attrib_5 in (‘browser_computer’, 'other') then take the value from device_model else from agent_model");
        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, firstLexis);

        sessionsTab.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        sessionsTab.navigate(activityClient.getUcid());
        sessionsTab.clickOnDataRow();
        sessionsTab.openTabDevice();

        sessionsTab.checkAgentColumnValueDeviceSubTab(firstLexis.getConditionAttrib5());
        sessionsTab.checkValueOfSubTableRow("Device", "os", firstLexis.getOs());
        sessionsTab.checkValueOfSubTableRow("Device", "os version", firstLexis.getOsVersion());
        sessionsTab.checkValueOfSubTableRow("Device", "screen", firstLexis.getScreenRes());
        sessionsTab.checkValueOfSubTableRow("Device", "brand", startFromUpper(firstLexis.getAgentBrand()));
        sessionsTab.checkValueOfSubTableRow(
                "Device",
                "model",
                firstLexis
                        .getAgentModel()); // if condition_attrib_5 in (‘browser_computer’, 'other') then take the value
        // from device_model else from agent_model
        sessionsTab.checkValueOfSubTableRow("Device", "name", firstLexis.getDeviceName());
        sessionsTab.checkValueOfSubTableRow("Device", "language", "English (United States)");

        deleteLexis(activityClient);

        firstLexis.setConditionAttrib5("browser_mobile");
        firstLexis.setOs("android");
        firstLexis.setOsVersion("android" + getCurrentTimestampSeconds());
        firstLexis.setAgentBrand("brand1, brand2 " + getCurrentTimestampSeconds());
        firstLexis.setAgentModel("agent model" + getCurrentTimestampSeconds());
        firstLexis.setDeviceName("human readable device name " + getCurrentTimestampSeconds());
        firstLexis.setAgentLanguage("fr-FR");
        firstLexis.setScreenRes("1515x" + getCurrentTimestampSeconds());
        firstLexis.setDeviceId(null);

        Allure.step(
                "Prepare data for DB with the another set of data. set conditionAttrib5 = \"browser_mobile\", so if condition_attrib_5 in (‘browser_computer’, 'other') then take the value from device_model else from agent_model");
        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, firstLexis);

        sessionsTab.navigate(activityClient.getUcid());
        sessionsTab.clickOnDataRow();
        sessionsTab.openTabDevice();

        sessionsTab.checkAgentColumnValueDeviceSubTab(firstLexis.getConditionAttrib5());
        sessionsTab.checkValueOfSubTableRow("Device", "os", firstLexis.getOs());
        sessionsTab.checkValueOfSubTableRow("Device", "os version", firstLexis.getOsVersion());
        sessionsTab.checkValueOfSubTableRow("Device", "screen", firstLexis.getScreenRes());
        sessionsTab.checkValueOfSubTableRow("Device", "brand", startFromUpper(firstLexis.getAgentBrand()));
        sessionsTab.checkValueOfSubTableRow(
                "Device",
                "model",
                firstLexis
                        .getAgentModel()); // if condition_attrib_5 in (‘browser_computer’, 'other') then take the value
        // from device_model else from agent_model
        sessionsTab.checkValueOfSubTableRow("Device", "name", firstLexis.getDeviceName());
        sessionsTab.checkValueOfSubTableRow("Device", "language", "French (France)");
        deleteLexis(activityClient);

        firstLexis.setConditionAttrib5("browser_computer");
        firstLexis.setOs("win");
        firstLexis.setOsVersion("win" + getCurrentTimestampSeconds());
        firstLexis.setAgentBrand("brand1, brand2 " + getCurrentTimestampSeconds());
        firstLexis.setAgentModel("agent model" + getCurrentTimestampSeconds());
        firstLexis.setDeviceModel("device model" + getCurrentTimestampSeconds());
        firstLexis.setDeviceName("human readable device name " + getCurrentTimestampSeconds());
        firstLexis.setAgentLanguage("it-IT");
        firstLexis.setScreenRes("1515x" + getCurrentTimestampSeconds());
        firstLexis.setDeviceId(null);

        Allure.step(
                "Prepare data for DB with the another set of data. set conditionAttrib5 = \"browser_computer\", so if condition_attrib_5 in (‘browser_computer’, 'other') then take the value from device_model else from agent_model");
        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, firstLexis);

        sessionsTab.navigate(activityClient.getUcid());
        sessionsTab.clickOnDataRow();
        sessionsTab.openTabDevice();

        sessionsTab.checkAgentColumnValueDeviceSubTab(firstLexis.getConditionAttrib5());
        sessionsTab.checkValueOfSubTableRow("Device", "os", firstLexis.getOs());
        sessionsTab.checkValueOfSubTableRow("Device", "os version", firstLexis.getOsVersion());
        sessionsTab.checkSubTableRowNotPresented(
                "Device", "model"); // if condition_attrib_5 in (‘browser_computer’, 'other') then take the value from
        // device_model else from agent_model
        sessionsTab.checkSubTableRowNotPresented("Device", "name");
        sessionsTab.checkSubTableRowNotPresented("Device", "language");

        deleteLexis(activityClient);

        firstLexis.setConditionAttrib5("Something");
        firstLexis.setOs("win");
        firstLexis.setOsVersion("win" + getCurrentTimestampSeconds());
        firstLexis.setAgentBrand("brand1, brand2 " + getCurrentTimestampSeconds());
        firstLexis.setAgentModel("agent model" + getCurrentTimestampSeconds());
        firstLexis.setDeviceModel("device model" + getCurrentTimestampSeconds());
        firstLexis.setDeviceName("human readable device name " + getCurrentTimestampSeconds());
        firstLexis.setAgentLanguage("it-IT");
        firstLexis.setScreenRes("1515x" + getCurrentTimestampSeconds());
        firstLexis.setDeviceId(null);

        Allure.step(
                "Prepare data for DB with the another set of data. set conditionAttrib5 = \"Something\", so if condition_attrib_5 in (‘browser_computer’, 'other') then take the value from device_model else from agent_model");
        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, firstLexis);

        sessionsTab.navigate(activityClient.getUcid());
        sessionsTab.clickOnDataRow();
        sessionsTab.openTabDevice();

        sessionsTab.checkAgentColumnValueDeviceSubTab(firstLexis.getConditionAttrib5());
        sessionsTab.checkValueOfSubTableRow("Device", "os", firstLexis.getOs());
        sessionsTab.checkValueOfSubTableRow("Device", "os version", firstLexis.getOsVersion());
        sessionsTab.checkSubTableRowNotPresented(
                "Device", "model"); // if condition_attrib_5 in (‘browser_computer’, 'other') then take the value from
        // device_model else from agent_model
        sessionsTab.checkSubTableRowNotPresented("Device", "name");
        sessionsTab.checkSubTableRowNotPresented("Device", "language");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Activity tab - Device sub-tab test, Browser")
    @AllureId("865")
    public void browserDataFromDb() {

        deleteLexis(activityClient);

        LnSessionParsedObject firstLexis = generateLexisNexisDataByClient(activityClient);
        firstLexis.setEventDatetime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 5, 0, 0));
        firstLexis.setEventType("account_creation");
        firstLexis.setRiskRating("high");
        firstLexis.setPolicyScore(RandomGenerator.getDefault().nextInt(0, 101));

        firstLexis.setBrowser("browser name" + getCurrentTimestampSeconds());
        firstLexis.setBrowserVersion("browser version" + getCurrentTimestampSeconds());
        firstLexis.setScreenResZoom(RandomGenerator.getDefault().nextDouble(0, 101));
        firstLexis.setBrowserLanguage("en-US,en;q=0.9,zh-CN;q=0.8,zh-TW;q=0.7,zh;q=0.6");
        firstLexis.setProfiledUrl("https://" + getCurrentTimestampSeconds() + ".com/login");
        firstLexis.setBrowserString("some user agent" + getCurrentTimestampSeconds());
        firstLexis.setPluginNumber(RandomGenerator.getDefault().nextInt(0, 101));

        Allure.step("Prepare data for DB with the set of the test data.");
        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, firstLexis);

        String expectedLanguages =
                "100% English (United States)90% English80% Chinese (China)70% Chinese (Taiwan, Province of China)60% Chinese";

        sessionsTab.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        sessionsTab.navigate(activityClient.getUcid());
        sessionsTab.clickOnDataRow();
        sessionsTab.openTabDevice();

        sessionsTab.checkValueOfSubTableRow("Browser", "name", firstLexis.getBrowser());
        sessionsTab.checkValueOfSubTableRow("Browser", "version", firstLexis.getBrowserVersion());
        sessionsTab.checkValueOfSubTableRow("Browser", "zoom", ((int) (firstLexis.getScreenResZoom() * 100)) + "%");
        sessionsTab.checkValueOfBrowserLanguageRow(expectedLanguages);
        sessionsTab.checkValueOfSubTableRow("Browser", "profiled URL", firstLexis.getProfiledUrl());
        sessionsTab.checkValueOfSubTableRow("Browser", "string", firstLexis.getBrowserString());
        sessionsTab.checkValueOfSubTableRow("Browser", "plugin number", firstLexis.getPluginNumber());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Activity tab - Device sub-tab test, ThreatMetrix")
    @AllureId("866")
    public void threatMetrixDataFromDb() {

        deleteLexis(activityClient);

        LnSessionParsedObject firstLexis = generateLexisNexisDataByClient(activityClient);
        firstLexis.setEventDatetime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 5, 0, 0));
        firstLexis.setEventType("account_creation");
        firstLexis.setRiskRating("high");
        firstLexis.setPolicyScore(RandomGenerator.getDefault().nextInt(0, 101));

        firstLexis.setDeviceId("some smart id" + getCurrentTimestampSeconds());
        firstLexis.setFuzzyDeviceId("some exact id" + getCurrentTimestampSeconds());

        Allure.step("Prepare data for DB with the set of the test data.");
        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, firstLexis);

        sessionsTab.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        sessionsTab.navigate(activityClient.getUcid());
        sessionsTab.clickOnDataRow();
        sessionsTab.openTabDevice();
        sessionsTab.checkValueOfSubTableRow("ThreatMetrix", "smart id", firstLexis.getDeviceId());
        sessionsTab.checkValueOfSubTableRow("ThreatMetrix", "exact id", firstLexis.getFuzzyDeviceId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Activity tab - IP address sub-tab test")
    @AllureId("867")
    void ipAddressDataFromDb() {

        deleteLexis(activityClient);
        Faker faker = new Faker();
        Country testCountry = Country.getRandomCountry();
        Country testCountry2 = Country.getRandomCountry();

        LnSessionParsedObject firstLexis = generateLexisNexisDataByClient(activityClient);
        firstLexis.setEventDatetime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 5, 0, 0));
        firstLexis.setEventType("account_creation");
        firstLexis.setRiskRating("high");
        firstLexis.setPolicyScore(RandomGenerator.getDefault().nextInt(0, 101));

        firstLexis.setTrueIp(faker.internet().ipV4Address());
        firstLexis.setTrueIpIsp("some true IP ISP" + getCurrentTimestampSeconds());
        firstLexis.setTrueIpPostalCode(faker.address().zipCode());
        firstLexis.setTrueIpCity(faker.address().city());
        firstLexis.setTrueIpRegion(faker.address().state());
        firstLexis.setTrueIpGeo(testCountry.getCountryCode());
        firstLexis.setTrueIpConnectionType("some true IP connection type" + getCurrentTimestampSeconds());
        firstLexis.setTrueIpRoutingType("some true IP routing type" + getCurrentTimestampSeconds());
        firstLexis.setProxyType("some true IP proxy type" + getCurrentTimestampSeconds());

        firstLexis.setInputIpAddress(faker.internet().ipV4Address());
        firstLexis.setInputIpIsp("some input IP ISP" + getCurrentTimestampSeconds());
        firstLexis.setInputIpCity(faker.address().city());
        firstLexis.setInputIpRegion(faker.address().state());
        firstLexis.setInputIpGeo(testCountry2.getCountryCode());
        firstLexis.setInputIpRoutingType("some input IP routing type" + getCurrentTimestampSeconds());

        Allure.step("Prepare data for DB with the set of the test data.");
        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, firstLexis);

        sessionsTab.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        sessionsTab.navigate(activityClient.getUcid());
        sessionsTab.clickOnDataRow();
        sessionsTab.openTabIpAdress();

        sessionsTab.checkIpSubTableRow("ip", firstLexis.getTrueIp(), firstLexis.getInputIpAddress());
        sessionsTab.checkIpSubTableRow(
                "isp",
                startFromUpper(firstLexis.getTrueIpIsp().toLowerCase()),
                startFromUpper(firstLexis.getInputIpIsp().toLowerCase()));
        sessionsTab.checkIpSubTableRow("postcode", firstLexis.getTrueIpPostalCode(), "–");
        sessionsTab.checkIpSubTableRow("city", firstLexis.getTrueIpCity(), firstLexis.getInputIpCity());
        sessionsTab.checkIpSubTableRow("geo", testCountry.getCountryCode(), testCountry2.getCountryCode());
        sessionsTab.checkIpSubTableRow(
                "connection",
                startFromUpper(firstLexis.getTrueIpConnectionType().toLowerCase()),
                "–");
        sessionsTab.checkIpSubTableRow("region", firstLexis.getTrueIpRegion(), firstLexis.getInputIpRegion());
        sessionsTab.checkIpSubTableRow("country", testCountry.getCountryName(), testCountry2.getCountryName());
    }
}
