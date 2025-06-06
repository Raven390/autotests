package page_objects.backoffice_pages.investigationTool;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import helpers.data.enums.Country;
import io.qameta.allure.Allure;
import page_objects.backoffice_pages.AbstractPage;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static utils.ConfigFactory.BASE_URL_E2E;

public class ActivityTab extends AbstractPage {

    private final Locator columnDateHeader;
    private final Locator columnDateData;
    private final Locator columnEventHeader;
    private final Locator columnEventData;
    private final Locator columnAgentHeader;
    private final Locator columnAgentData;
    private final Locator columnOsHeader;
    private final Locator columnOsData;
    private final Locator columnRiskHeader;
    private final Locator columnRiskData;
    private final Locator columnScoreData;
    private final Locator columnScoreHeader;
    private final Locator columnPoliciesHeader;
    private final Locator columnPoliciesData;
    private final Locator activityTab;
    private final Locator columnDateDataPrimary;
    private final Locator columnDateDataSecodary;
    private final Locator columnRiskDataDanger;
    private final Locator columnRiskDataNeutral;
    private final Locator columnRiskDataPositive;
    private final Locator columnScoreDataDanger;
    private final Locator columnScoreDataNeutral;
    private final Locator columnScoreDataPositive;
    private final Locator filterOs;
    private final Locator filterRisk;
    private final Locator filterDate;
    private final Locator filterOption;
    private final Locator filterClearButton;
    private final Locator sortByScoreButton;
    private final Locator sortByDateButton;
    private final Locator tooltip;
    private final Locator lineDivider;
    private final Locator dataRow;
    private final Locator negativeScoreLine;
    private final Locator positiveScoreLine;
    private final Locator scoreLine;
    private final Locator scoreHeader;
    private final Locator scoreMeterTitle;
    private final Locator scoreMeterLable;
    private final Locator scoreMeterSuccessLable;
    private final Locator scoreMeterDangerLable;
    private final Locator applyedPoliciesHeader;
    private final Locator applyedPoliciesPolicyName;
    private final Locator applyedPoliciesPolicyScore;
    private final Locator tmxReasonHeader;
    private final Locator tmxReasonLabel;
    private final Locator ipScoreLevel;
    private final Locator ipScoreText;
    private final Locator emailAdvice;
    private final Locator emailScore;
    private final Locator digitalIdentytyTitle;
    private final Locator digitalIdentytyScore;
    private final Locator emailageTab;
    private final Locator deviceTab;
    private final Locator ipAddressTab;


    private static final String COLUMN_DATE_LOCATOR = "//*[contains(@class, 'v-activity-tab-table__column_type_date')]";
    private static final String COLUMN_EVENT_LOCATOR = "//*[contains(@class, 'v-activity-tab-table__column_type_event')]";
    private static final String COLUMN_AGENT_LOCATOR = "//*[contains(@class, 'v-activity-tab-table__column_type_agent')]";
    private static final String COLUMN_OS_LOCATOR = "//*[contains(@class, 'v-activity-tab-table__column_type_os')]";
    private static final String COLUMN_RISK_LOCATOR = "//*[contains(@class, 'v-activity-tab-table__column_type_risk')]";
    private static final String COLUMN_SCORE_LOCATOR = "//*[contains(@class, 'v-activity-tab-table__column_type_score')]";
    private static final String COLUMN_POLICIES_LOCATOR = "//*[contains(@class, 'v-activity-tab-table__column_type_policies')]";
    private static final String TABLE_HEADERS_LOCATOR = "//thead";
    private static final String TABLE_BODY_LOCATOR = "//tbody";
    private static final String PRIMARY_TEXT_LOCATOR = "//*[contains(@class, 'g-color-text_color_primary')]";
    private static final String SECONDARY_TEXT_LOCATOR = "//*[contains(@class, 'g-color-text_color_secondary')]";
    private static final String DANGER_TEXT_LOCATOR = "//*[contains(@class, 'g-color-text_color_danger')]";
    private static final String POSITIVE_TEXT_LOCATOR = "//*[contains(@class, 'g-color-text_color_positive')]";
    private static final String SORTABLE_CELL_LOCATOR = "//*[contains(@class, 'v-activity-tab-table__sortable-cell')]";
    private static final String SCORE_METER_SECTION_LOCATOR = "//*[@class = 'v-activity-tab-risk-score-meter']";
    private static final String APPLIED_POLICIES_SECTION_LOCATOR = "//*[@class = 'v-applied-policies-labels']";
    private static final String TMX_REASON_SECTION_LOCATOR = "//*[@class = 'v-tmx-reasons-labels']";
    private static final String SCORE_SECTION_LOCATOR = "//*[@class = 'v-summary-score-bars__scores']";
    private static final String SUBHEADER_3_LOCATOR = "//*[contains(@class, 'g-text_variant_subheader-3')]";
    private static final String HEADER_2_LOCATOR = "//*[contains(@class, 'g-text_variant_header-2')]";
    private static final String BODY_SHORT_LOCATOR = "//*[contains(@class, 'g-text_variant_body-short')]";
    private static final String LABEL_LOCATOR = "//*[contains(@class, 'g-label')]";
    private static final String LABEL_CONTENT_LOCATOR = "//*[@class = 'g-label__content']";
    private static final String SUCCESS_LABEL_LOCATOR = "//*[contains(@class, 'g-label_theme_success')]";
    private static final String DANGER_LABEL_LOCATOR = "//*[contains(@class, 'g-label_theme_danger')]";
    private static final String SCORE_BAR_SUCCESS_LOCATOR = "//*[contains(@class, 'v-score-bar__score-bar_color_success')]";
    private static final String SCORE_BAR_WARNING_LOCATOR = "//*[contains(@class, 'v-score-bar__score-bar_color_warning')]";
    private static final String SCORE_BAR_DANGER_LOCATOR = "//*[contains(@class, 'v-score-bar__score-bar_color_danger')]";
    private static final String IP_SCORE_TEXTS_LOCATOR = "//*[@class = 'v-summary-score-bars__scores']//*[text()='IP address']/preceding-sibling::div";
    private static final String EMAIL_SCORE_TEXTS_LOCATOR = "//*[@class = 'v-summary-score-bars__scores']//*[text()='email']/preceding-sibling::div";
    private static final String DIGITAL_IDENTITY_SCORE_TEXTS_LOCATOR = "//*[@class = 'v-summary-score-bars__scores']//*[text()='digital identity']/preceding-sibling::div";
    private static final String IP_SCORE_BAR_LOCATOR = "//*[@class = 'v-summary-score-bars__scores']//*[text()='IP address']/../..//*[@class = 'v-score-bar__score-line']";
    private static final String EMAIL_SCORE_BAR_LOCATOR = "//*[@class = 'v-summary-score-bars__scores']//*[text()='email']/../..//*[@class = 'v-score-bar__score-line']";
    private static final String DIGITAL_IDENTITY_SCORE_BAR_LOCATOR = "//*[@class = 'v-summary-score-bars__scores']//*[text()='digital identity']/../..//*[@class = 'v-score-bar__score-line']";

    DecimalFormat df = new DecimalFormat("#.");


    public ActivityTab(Page page) {
        super(page);
        this.columnDateHeader = page.locator(TABLE_HEADERS_LOCATOR + COLUMN_DATE_LOCATOR);
        this.columnDateData = page.locator(TABLE_BODY_LOCATOR + COLUMN_DATE_LOCATOR);
        this.columnDateDataPrimary = page.locator(TABLE_BODY_LOCATOR + COLUMN_DATE_LOCATOR + PRIMARY_TEXT_LOCATOR);
        this.columnDateDataSecodary = page.locator(TABLE_BODY_LOCATOR + COLUMN_DATE_LOCATOR + SECONDARY_TEXT_LOCATOR);
        this.columnEventHeader = page.locator(TABLE_HEADERS_LOCATOR + COLUMN_EVENT_LOCATOR);
        this.columnEventData = page.locator(TABLE_BODY_LOCATOR + COLUMN_EVENT_LOCATOR);
        this.columnAgentHeader = page.locator(TABLE_HEADERS_LOCATOR + COLUMN_AGENT_LOCATOR);
        this.columnAgentData = page.locator(TABLE_BODY_LOCATOR + COLUMN_AGENT_LOCATOR);
        this.columnOsHeader = page.locator(TABLE_HEADERS_LOCATOR + COLUMN_OS_LOCATOR);
        this.columnOsData = page.locator(TABLE_BODY_LOCATOR + COLUMN_OS_LOCATOR);
        this.columnRiskHeader = page.locator(TABLE_HEADERS_LOCATOR + COLUMN_RISK_LOCATOR);
        this.columnRiskData = page.locator(TABLE_BODY_LOCATOR + COLUMN_RISK_LOCATOR);
        this.columnRiskDataDanger = page.locator(TABLE_BODY_LOCATOR + COLUMN_RISK_LOCATOR + DANGER_TEXT_LOCATOR);
        this.columnRiskDataNeutral = page.locator(TABLE_BODY_LOCATOR + COLUMN_RISK_LOCATOR + PRIMARY_TEXT_LOCATOR);
        this.columnRiskDataPositive = page.locator(TABLE_BODY_LOCATOR + COLUMN_RISK_LOCATOR + POSITIVE_TEXT_LOCATOR);
        this.columnScoreHeader = page.locator(TABLE_HEADERS_LOCATOR + COLUMN_SCORE_LOCATOR);
        this.columnScoreData = page.locator(TABLE_BODY_LOCATOR + COLUMN_SCORE_LOCATOR);
        this.columnScoreDataDanger = page.locator(TABLE_BODY_LOCATOR + COLUMN_SCORE_LOCATOR + DANGER_TEXT_LOCATOR);
        this.columnScoreDataNeutral = page.locator(TABLE_BODY_LOCATOR + COLUMN_SCORE_LOCATOR + PRIMARY_TEXT_LOCATOR);
        this.columnScoreDataPositive = page.locator(TABLE_BODY_LOCATOR + COLUMN_SCORE_LOCATOR + POSITIVE_TEXT_LOCATOR);
        this.columnPoliciesHeader = page.locator(TABLE_HEADERS_LOCATOR + COLUMN_POLICIES_LOCATOR);
        this.columnPoliciesData = page.locator(TABLE_BODY_LOCATOR + COLUMN_POLICIES_LOCATOR);
        this.activityTab = page.locator("[role=\"tab\"][title=\"Activity\"]");
        this.emailageTab = page.locator("[role=\"tab\"][title=\"Emailage\"]");
        this.deviceTab = page.locator("[role=\"tab\"][title=\"Device\"]");
        this.ipAddressTab = page.locator("[role=\"tab\"][title=\"IP address\"]");
        this.filterOs = page.locator("//*[contains(@class, 'v-activity-tab-filters__filter-section')]//*[text()='Any OS']");
        this.filterRisk = page.locator("//*[contains(@class, 'v-activity-tab-filters__filter-section')]//*[text()='Any risk rating']");
        this.filterDate = page.locator("//*[contains(@class, 'v-activity-tab-filters__filter-section')]//*[text()='Lifetime']");
        this.filterOption = page.locator("//*[@data-qa='select-list']//*[@role='option']//span");
        this.filterClearButton = page.locator("//button[@data-qa='select-clear']");
        this.sortByScoreButton = page.locator(TABLE_HEADERS_LOCATOR + COLUMN_SCORE_LOCATOR + SORTABLE_CELL_LOCATOR);
        this.sortByDateButton = page.locator(TABLE_HEADERS_LOCATOR + COLUMN_DATE_LOCATOR + SORTABLE_CELL_LOCATOR);
        this.tooltip = page.locator(".v-pointer-tooltip__tooltip");
        this.lineDivider = page.locator("//*[contains(@class, 'v-score-line__divider')]");
        this.dataRow = page.locator("tbody tr.g-table__row");
        this.negativeScoreLine = page.locator(".v-score-line__line_negative");
        this.scoreLine = page.locator(".v-score-line__line");
        this.positiveScoreLine = page.locator(".v-score-line__line_positive");
        this.scoreHeader = page.locator(SCORE_METER_SECTION_LOCATOR + HEADER_2_LOCATOR);
        this.scoreMeterTitle = page.locator(SCORE_METER_SECTION_LOCATOR + SUBHEADER_3_LOCATOR);
        this.scoreMeterLable = page.locator(SCORE_METER_SECTION_LOCATOR + LABEL_LOCATOR);
        this.scoreMeterSuccessLable = page.locator(SCORE_METER_SECTION_LOCATOR + SUCCESS_LABEL_LOCATOR);
        this.scoreMeterDangerLable = page.locator(SCORE_METER_SECTION_LOCATOR + DANGER_LABEL_LOCATOR);
        this.applyedPoliciesHeader = page.locator(APPLIED_POLICIES_SECTION_LOCATOR + SUBHEADER_3_LOCATOR);
        this.applyedPoliciesPolicyName = page.locator(APPLIED_POLICIES_SECTION_LOCATOR + "//span[not(contains(@class, 'v-applied-policies-labels__score'))]");
        this.applyedPoliciesPolicyScore = page.locator(APPLIED_POLICIES_SECTION_LOCATOR + "//span[contains(@class, 'v-applied-policies-labels__score')]");
        this.tmxReasonHeader = page.locator(TMX_REASON_SECTION_LOCATOR + SUBHEADER_3_LOCATOR);
        this.tmxReasonLabel = page.locator(TMX_REASON_SECTION_LOCATOR + LABEL_CONTENT_LOCATOR);
        this.ipScoreLevel = page.locator(IP_SCORE_TEXTS_LOCATOR + HEADER_2_LOCATOR);
        this.ipScoreText = page.locator(IP_SCORE_TEXTS_LOCATOR + BODY_SHORT_LOCATOR);
        this.emailAdvice = page.locator(EMAIL_SCORE_TEXTS_LOCATOR + BODY_SHORT_LOCATOR);
        this.emailScore = page.locator(EMAIL_SCORE_TEXTS_LOCATOR + HEADER_2_LOCATOR);
        this.digitalIdentytyTitle = page.locator(DIGITAL_IDENTITY_SCORE_TEXTS_LOCATOR + BODY_SHORT_LOCATOR);
        this.digitalIdentytyScore = page.locator(DIGITAL_IDENTITY_SCORE_TEXTS_LOCATOR + HEADER_2_LOCATOR);
    }

    public void navigate(String ucid) {
        Allure.step("Navigate to users activity tab");
        page.navigate(BASE_URL_E2E + "investigation/" + ucid + "/sessions");
        waitForPageToLoad();
        waitForPageToLoad();
    }

    public void openTab(String ucid) {
        Allure.step("Open users activity tab by click tab");
        page.navigate(BASE_URL_E2E + "investigation/" + ucid + "/");
        waitForPageToLoad();
        activityTab.click();
        waitForPageToLoad();
    }

    public void openTabEmailage() {
        Allure.step("Open users Emailage tab by click tab");
        emailageTab.click();
        waitForPageToLoad();
    }

    public void openTabDevice() {
        Allure.step("Open users Emailage tab by click tab");
        deviceTab.click();
        waitForPageToLoad();
    }

    public void openTabIpAdress() {
        Allure.step("Open users Emailage tab by click tab");
        ipAddressTab.click();
        waitForPageToLoad();
    }

    public void checkDateColumnValue(String expectedDate, String expectedTime) {
        Allure.step("Check values in the dates column");
        String actualDate = columnDateDataPrimary.textContent();
        assertEquals(expectedDate, actualDate);
        String actualTime = columnDateDataSecodary.textContent();
        assertEquals(expectedTime, actualTime);
    }

    public void checkDateColumnValue(String dateTime) {
        Allure.step("Check values in the dates column");
        String[] dates = dateTime.split(" ");
        String expectedDate = dates[0];
        String expectedTime = dates[1].substring(0, 5);
        System.out.println("Expected date: " + expectedDate);
        System.out.println("Expected time: " + expectedTime);
        String actualDate = columnDateDataPrimary.textContent();
        assertEquals(expectedDate, actualDate);
        String actualTime = columnDateDataSecodary.textContent();
        assertEquals(expectedTime, actualTime);
    }

    public void checkDateColumnValue(String dateTime, int index) {
        Allure.step("Check that expected values in the dates column are presented");
        String[] dates = dateTime.split(" ");
        String expectedDate = dates[0];
        System.out.println("Expected date: " + expectedDate);
        String expectedTime = dates[1].substring(0, 5);
        System.out.println("Expected time: " + expectedTime);
        String actualDate = columnDateDataPrimary.nth(index).textContent();
        assertEquals(expectedDate, actualDate);
        String actualTime = columnDateDataSecodary.nth(index).textContent();
        assertEquals(expectedTime, actualTime);
    }

    public void checkDateColumnValueNotPresented(String unexpectedDateTime) {
        Allure.step("Check values in the dates column not include unwanted values");
        String[] dates = unexpectedDateTime.split(" ");
        String unexpectedDate = dates[0];
        assertFalse(columnDateDataPrimary.getByText(unexpectedDate).isVisible());
    }

    public void checkEventColumnValue(String expectedValue) {
        Allure.step("Check values in the event column");
        if ("account_creation".equals(expectedValue)) {
            expectedValue = "Registration";
        }
        if ("login".equals(expectedValue)) {
            expectedValue = "Login";
        }
        String actualValue = columnEventData.textContent();
        assertEquals(expectedValue, actualValue);
    }

    public void checkAgentColumnValue(String expectedValue) {
        Allure.step("Check values in the agent column");
        String expectedText = switch (expectedValue) {
            case "agent_mobile", "Mobile app" -> " Mobile app";
            case "browser_mobile", "Mobile browser" -> " Mobile browser";
            case "browser_computer", "Web browser" -> " Web browser";
            default -> " Other";
        };

        String actualValue = columnAgentData.textContent();
        assertEquals(expectedText, actualValue);
    }

    public void checkAgentColumnValueDeviceSubTab(String expectedValue) {
        Allure.step("Check values in the agent column");
        String expectedText = switch (expectedValue) {
            case "agent_mobile", "Mobile app" -> "Mobile app";
            case "browser_mobile", "Mobile browser" -> "Mobile browser";
            case "browser_computer", "Web browser" -> "Web browser";
            default -> "Other";
        };

        String locator = "//div[contains(@class, 'v-attribute-table-section__title') and (text() = 'Device')]/..//div[contains(@class, 'g-color-text_color_secondary') and (text() = 'agent')]/../following-sibling::div";
        page.waitForSelector(locator);

        String actualValue = page.locator(locator).textContent();
        assertEquals(expectedText, actualValue);
    }

    public void checkOsColumnValue(String expectedValue) {
        Allure.step("Check values in the OS column");
        String actualValue = columnOsData.textContent();
        assertEquals(expectedValue, actualValue);
    }

    public void checkOsColumnValue(String expectedValue, int index) {
        Allure.step("Check values in the OS column");
        String actualValue = columnOsData.nth(index).textContent();
        assertEquals(expectedValue, actualValue);
    }

    public void checkRiskColumnValue(String expectedValue) {
        Allure.step("Check values in the risk column");
        expectedValue = expectedValue.substring(0, 1).toUpperCase() + expectedValue.substring(1);
        String actualValue = columnRiskData.textContent();
        assertEquals(expectedValue, actualValue);
    }

    public void checkRiskColumnValue(String expectedValue, int index) {
        Allure.step("Check values in the risk column");
        expectedValue = expectedValue.substring(0, 1).toUpperCase() + expectedValue.substring(1);
        String actualValue = columnRiskData.nth(index).textContent();
        assertEquals(expectedValue, actualValue);
    }

    public void checkRiskColumnColourDanger() {
        Allure.step("Check that colour of the text in the risk column is danger");
        columnRiskDataDanger.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertFalse(columnRiskDataPositive.isVisible());
    }

    public void checkRiskColumnColourNeutral() {
        Allure.step("Check that colour of the text in the risk column is neutral");
        columnRiskDataNeutral.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertFalse(columnRiskDataDanger.isVisible());
    }

    public void checkRiskColumnColourPositive() {
        Allure.step("Check that colour of the text in the risk column is positive");
        columnRiskDataPositive.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertFalse(columnRiskDataDanger.isVisible());
    }

    public void checkScoreColumnValue(int expectedValue) {
        Allure.step("Check values in the score column");
        int actualValue = Integer.parseInt(columnScoreData.textContent());
        assertEquals(expectedValue, actualValue);
    }

    public void checkScoreColumnIsAsc() {
        Allure.step("Check that record is sorted by score in ASC order");
        waitForPageToLoad();
        assertTrue(columnScoreData.count() > 1);
        int firstScore = Integer.parseInt(columnScoreData.first().textContent());
        int lastScore = Integer.parseInt(columnScoreData.last().textContent());
        assertTrue(firstScore < lastScore);
    }

    public void checkScoreColumnIsDesc() {
        Allure.step("Check that record is sorted by score in DESC order");
        waitForPageToLoad();
        assertTrue(columnScoreData.count() > 1);
        int firstScore = Integer.parseInt(columnScoreData.first().textContent());
        int lastScore = Integer.parseInt(columnScoreData.last().textContent());
        System.out.println("firstScore: " + firstScore + " and lastScore" + lastScore);
        assertTrue(firstScore > lastScore);
    }

    public void checkScoreColumnColourDanger() {
        Allure.step("Check colour of the text in the risk column");
        columnScoreDataDanger.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

    }

    public void checkScoreColumnColourNeutral() {
        Allure.step("Check that colour of the text in the risk column is neutral");
        columnScoreDataNeutral.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertFalse(columnRiskDataDanger.isVisible());
    }

    public void checkScoreColumnColourPositive() {
        Allure.step("Check that colour of the text in the risk column is positive");
        columnScoreDataPositive.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertFalse(columnRiskDataDanger.isVisible());
    }

    public void checkPoliciesColumnValue(String expectedValue) {
        Allure.step("Check values in the policies column");
        String actualValue = columnPoliciesData.textContent();
        assertEquals(expectedValue, actualValue);
    }

    public void clickOsFilter() {
        Allure.step("click on the os filter");
        filterOs.click();
    }

    public void clickRiskFilter() {
        Allure.step("click on the risk filter");
        filterRisk.click();
    }

    public void clickDateFilter() {
        Allure.step("click on the date filter");
        filterDate.click();
    }

    public void checkThatOptionPresented(String expectedOption) {
        Allure.step("Check presented filter options");
        filterOption.getByText(expectedOption).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public void clickFilterOption(String expectedOption) {
        Allure.step("Check presented filter options " + expectedOption);
        waitForPageToLoad();
        filterOption.getByText(expectedOption).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        waitForPageToLoad();
        filterOption.getByText(expectedOption).click();
        waitForPageToLoad();
    }

    public void setCustomDates(String dateFrom, String dateTo) {
        selectDateRangeInElement(filterOption.getByText("Custom dates"), dateFrom, dateTo);
    }

    public void clearFilterButton() {
        Allure.step("click clear filter button");
        filterClearButton.click();
    }

    public void setSortByScoreAsc() {
        Allure.step("Filter record by score asc");
        sortByScoreButton.first().click();
        sortByScoreButton.first().hover();
        tooltip.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        if (tooltip.getByText("Highest negative first").isVisible()) {
            sortByScoreButton.first().click();
        } else {
            page.waitForTimeout(1);
        }
    }

    public void setSortByScoreDesc() {
        Allure.step("Sort record by score desc");
        sortByScoreButton.first().click();
        sortByScoreButton.first().hover();
        tooltip.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        if (tooltip.getByText("Highest positive first").isVisible()) {
            sortByScoreButton.first().click();
        } else {
            page.waitForTimeout(1);
        }
    }

    public void setSortByDateAsc() {
        Allure.step("Sort record by score desc");
        sortByDateButton.first().click();
        sortByDateButton.first().hover();
        tooltip.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        if (tooltip.getByText("Newest events first").isVisible()) {
            sortByDateButton.first().click();
        } else {
            page.waitForTimeout(1);
        }
    }

    public void setSortByDateDesc() {
        Allure.step("Sort record by score desc");
        sortByDateButton.first().click();
        sortByDateButton.first().hover();
        tooltip.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        if (tooltip.getByText("Oldest events first").isVisible()) {
            sortByDateButton.first().click();
        } else {
            page.waitForTimeout(1);
        }
    }

    public void checkDateColumnIsAsc() {
        Allure.step("Sort record by date ASC");
        waitForPageToLoad();
        assertTrue(columnDateData.count() > 1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String firstDate = columnDateDataPrimary.first().textContent() + " " + columnDateDataSecodary.first().textContent();
        String lastDate = columnDateDataPrimary.last().textContent() + " " + columnDateDataSecodary.last().textContent();
        LocalDateTime firstDateTime = LocalDateTime.parse(firstDate, formatter);
        LocalDateTime lastDateTime = LocalDateTime.parse(lastDate, formatter);
        System.out.println("firstDate: " + firstDate + " and lastDate" + lastDate);
        assertTrue(firstDateTime.isAfter(lastDateTime));
    }

    public void checkDateColumnIsDesc() {
        Allure.step("Sort record by date DESC");
        waitForPageToLoad();
        assertTrue(columnDateData.count() > 1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String firstDate = columnDateDataPrimary.first().textContent() + " " + columnDateDataSecodary.first().textContent();
        String lastDate = columnDateDataPrimary.last().textContent() + " " + columnDateDataSecodary.last().textContent();
        LocalDateTime firstDateTime = LocalDateTime.parse(firstDate, formatter);
        LocalDateTime lastDateTime = LocalDateTime.parse(lastDate, formatter);
        System.out.println("firstDate: " + firstDate + " and lastDate" + lastDate);
        assertTrue(firstDateTime.isBefore(lastDateTime));
    }

    public void clickOnDataRow() {
        Allure.step("click on data row");
        waitForPageToLoad();
        dataRow.click();
    }

    public void clickOnDataRow(int index) {
        Allure.step("click on data row");
        waitForPageToLoad();
        dataRow.nth(index).click();
    }

    public void checkScoreHeaderValueScoreLine(int expectedValue) {
        Allure.step("Check values in the score header of the score line of summary sub-tab");
        assertEquals("Risk score", scoreMeterTitle.textContent());
        int actualValue = Integer.parseInt(scoreHeader.textContent());
        assertEquals(expectedValue, actualValue);
    }

    public void positiveScoreMeterDisplayed() {
        Allure.step("Check that line in score meter is positive");
        assertTrue(positiveScoreLine.isVisible());
        assertFalse(negativeScoreLine.isVisible());
    }

    public void setSuccessLabelDisplayed() {
        Allure.step("Check if risk label have positive style");
        assertTrue(scoreMeterSuccessLable.isVisible());
        assertFalse(scoreMeterDangerLable.isVisible());
    }

    public void checkNeutralLabelDisplayed() {
        Allure.step("Check if risk label have positive style");
        assertTrue(scoreMeterLable.last().isVisible());
        assertFalse(scoreMeterSuccessLable.isVisible());
        assertFalse(scoreMeterDangerLable.isVisible());
    }

    public void checkDangerLabelDisplayed() {
        Allure.step("Check if risk label have positive style");
        assertFalse(scoreMeterSuccessLable.isVisible());
        assertTrue(scoreMeterDangerLable.isVisible());
    }

    public void checkTextInLabel(String expectedText) {
        Allure.step("Check test in label in score-meter section");
        String expectedValue = expectedText.substring(0, 1).toUpperCase() + expectedText.substring(1);
        assertEquals(expectedValue, scoreMeterLable.last().textContent());
    }

    public void checkPositionOfTheLineDivider(String positionVal) {
        Allure.step("Check position of line divider in score-meter section");
        String attr = lineDivider.getAttribute("style");
        System.out.println(attr);
        System.out.println("left: calc(" + positionVal + "%");
        assertTrue(attr.contains("left: calc(" + positionVal + "%"));
    }

    public void checkTitleOfPoliciesSection(String expectedTitle) {
        Allure.step("Check correct title for the applied policies section");
        applyedPoliciesHeader.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertEquals(expectedTitle, applyedPoliciesHeader.textContent());
    }

    public void checkRuleName(String expectedTitle) {
        Allure.step("Check name of applied policy rule");
        applyedPoliciesPolicyName.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertEquals(expectedTitle, applyedPoliciesPolicyName.textContent());
    }

    public void checkRuleName(String expectedTitle, int index) {
        Allure.step("Check name of applied policy rule");
        applyedPoliciesPolicyName.nth(index).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertEquals(expectedTitle, applyedPoliciesPolicyName.nth(index).textContent());
    }

    public void checkRuleScore(String expectedScore) {
        Allure.step("Check Score of applied policy rule");
        applyedPoliciesPolicyScore.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertEquals(expectedScore, applyedPoliciesPolicyScore.textContent());
    }

    public void checkRuleScore(String expectedScore, int index) {
        Allure.step("Check Score of applied policy rule");
        applyedPoliciesPolicyScore.nth(index).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertEquals(expectedScore, applyedPoliciesPolicyScore.nth(index).textContent());
    }

    public void checkRuleScore(String expectedScore, String policyName) {
        Allure.step("Check Score of applied policy rule " + policyName);
        String locator = (APPLIED_POLICIES_SECTION_LOCATOR + "//span[not(contains(@class, 'v-applied-policies-labels__score')) and (text()='" + policyName + "')]/following-sibling::span");
        assertEquals(expectedScore, page.locator(locator).textContent());
    }

    public void checkTitleOfTmxReasonSection(String expectedTitle) {
        Allure.step("Check correct title for the TMX reason section");
        tmxReasonHeader.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertEquals(expectedTitle, tmxReasonHeader.textContent());
    }

    public void checkThatTmxReasonCodeIsDisplayed(String expectedTitle) {
        Allure.step("Check that label with expected TMX reason code is displayed");
        String[] titles = expectedTitle.split(", ");
        for (String i : titles) {
            String cleaned = i.replaceAll("[^a-zA-Z_0-9]", "");
            tmxReasonLabel.getByText(cleaned).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        }
    }

    public void checkIpScoreRiskLevelValue(String expectedValue) {
        Allure.step("Check text risk level value in ip subsection of Scores section");
        String expectedText = switch (expectedValue) {
            case "Review" -> "review";
            case "Very low" -> "very low risk";
            case "Low" -> "low risk";
            case "Moderate" -> "moderate risk";
            case "High" -> "high risk";
            case "Very high" -> "very high risk";
            case "Null" -> "risk unknown";
            case null -> "risk unknown";
            default -> " unhandled value";
        };
        assertEquals(expectedText, ipScoreText.textContent());
    }

    public void checkIpScoreRiskLevelNumber(String expectedValue) {
        Allure.step("Check numeric level value in ip subsection of Scores section");
        String expectedText = switch (expectedValue) {
            case "Review" -> "1";
            case "Very low" -> "2";
            case "Low" -> "3";
            case "Moderate" -> "4";
            case "High" -> "5";
            case "Very high" -> "6";
            case "Null" -> "0";
            case null -> "0";
            default -> " unhandled value";
        };
        String actualValue = ipScoreLevel.textContent();
        assertEquals(expectedText, actualValue);
    }

    public void checkIpScoreRiskBarStyle(String expectedValue) {
        Allure.step("Check line styles in ip subsection of Scores section");
        switch (expectedValue) {
            case "Review":
                page.waitForSelector(IP_SCORE_BAR_LOCATOR + SCORE_BAR_SUCCESS_LOCATOR);
                assertEquals(page.locator(IP_SCORE_BAR_LOCATOR + SCORE_BAR_SUCCESS_LOCATOR).getAttribute("style"), "width: 16.6667%;");
                break;
            case "Very low":
                page.waitForSelector(IP_SCORE_BAR_LOCATOR + SCORE_BAR_SUCCESS_LOCATOR);
                assertEquals(page.locator(IP_SCORE_BAR_LOCATOR + SCORE_BAR_SUCCESS_LOCATOR).getAttribute("style"), "width: 33.3333%;");
                break;
            case "Low":
                page.waitForSelector(IP_SCORE_BAR_LOCATOR + SCORE_BAR_SUCCESS_LOCATOR);
                assertEquals(page.locator(IP_SCORE_BAR_LOCATOR + SCORE_BAR_SUCCESS_LOCATOR).getAttribute("style"), "width: 50%;");
                break;
            case "Moderate":
                page.waitForSelector(IP_SCORE_BAR_LOCATOR + SCORE_BAR_WARNING_LOCATOR);
                assertEquals(page.locator(IP_SCORE_BAR_LOCATOR + SCORE_BAR_WARNING_LOCATOR).getAttribute("style"), "width: 66.6667%;");
                break;
            case "High":
                page.waitForSelector(IP_SCORE_BAR_LOCATOR + SCORE_BAR_DANGER_LOCATOR);
                assertEquals(page.locator(IP_SCORE_BAR_LOCATOR + SCORE_BAR_DANGER_LOCATOR).getAttribute("style"), "width: 83.3333%;");
                break;
            case "Very high":
                page.waitForSelector(IP_SCORE_BAR_LOCATOR + SCORE_BAR_DANGER_LOCATOR);
                assertEquals(page.locator(IP_SCORE_BAR_LOCATOR + SCORE_BAR_DANGER_LOCATOR).getAttribute("style"), "width: 100%;");
                break;
            case "Null":
                page.waitForSelector(IP_SCORE_BAR_LOCATOR + SCORE_BAR_SUCCESS_LOCATOR);
                assertEquals(page.locator(IP_SCORE_BAR_LOCATOR + SCORE_BAR_DANGER_LOCATOR).getAttribute("style"), "width: 16.6667%;");
                break;
            case null:
                assertEquals(page.locator(IP_SCORE_BAR_LOCATOR + SCORE_BAR_SUCCESS_LOCATOR).getAttribute("style"), "width: 0%;");
                break;
            default:
                System.out.println("unhandled value");
                assertTrue(false);
        }
    }

    public void checkEmailScoreAdvice(String expectedValue) {
        Allure.step("Check advice text in email section");
        assertEquals(expectedValue, emailAdvice.textContent());
    }

    public void checkEmailScoreValue(int expectedValue) {
        Allure.step("Check score number in email section");
        assertEquals(expectedValue, Integer.parseInt(emailScore.textContent()));
    }

    public void checkEmailScoreRiskBarStyle(Integer expectedValue) {
        Allure.step("Check score bar in email sub-section");
        if (expectedValue == null) {
            expectedValue = 0;
        }

        if ((0 < expectedValue) && (expectedValue < 301)) {
            String expectedWidth = String.valueOf(((expectedValue * 100) / 999));
            System.out.println("expected width is " + expectedWidth);
            String widthStyle = page.locator(EMAIL_SCORE_BAR_LOCATOR + SCORE_BAR_SUCCESS_LOCATOR).getAttribute("style");
            System.out.println("width style is " + widthStyle);
            assertTrue(widthStyle.contains(expectedWidth), "expectedValue is " + expectedValue + "expectedWidth is " + expectedWidth);
        } else if (0 == expectedValue) {
            String expectedWidth = String.valueOf(((expectedValue * 100) / 999));
            System.out.println("expected width is " + expectedWidth);
            String widthStyle = page.locator(EMAIL_SCORE_BAR_LOCATOR + SCORE_BAR_SUCCESS_LOCATOR).getAttribute("style");
            System.out.println("width style is " + widthStyle);
            assertTrue(widthStyle.contains(expectedWidth), "expectedValue is " + expectedValue + "expectedWidth is " + expectedWidth);
        } else if ((300 < expectedValue) && (expectedValue < 601)) {
            page.waitForSelector(EMAIL_SCORE_BAR_LOCATOR + SCORE_BAR_WARNING_LOCATOR);
            String expectedWidth = String.valueOf(((expectedValue * 100) / 999));
            System.out.println("expected width is " + expectedWidth);
            String widthStyle = page.locator(EMAIL_SCORE_BAR_LOCATOR + SCORE_BAR_WARNING_LOCATOR).getAttribute("style");
            System.out.println("width style is " + widthStyle);
            assertTrue(widthStyle.contains(expectedWidth), "expectedValue is " + expectedValue + "expectedWidth is " + expectedWidth);
        } else if ((600 < expectedValue) && (expectedValue < 1000)) {
            page.waitForSelector(EMAIL_SCORE_BAR_LOCATOR + SCORE_BAR_DANGER_LOCATOR);
            String expectedWidth = String.valueOf(((expectedValue * 100) / 999));
            System.out.println("expected width is " + expectedWidth);
            String widthStyle = page.locator(EMAIL_SCORE_BAR_LOCATOR + SCORE_BAR_DANGER_LOCATOR).getAttribute("style");
            System.out.println("width style is " + widthStyle);
            assertTrue(widthStyle.contains(expectedWidth), "expectedValue is " + expectedValue + "expectedWidth is " + expectedWidth);
        } else {
            System.out.println("UNHANDLED VALUE");
            assertTrue(false);
        }
    }

    public void checkDigitalIdentityScoreTitle(String expectedValue) {
        Allure.step("Check advice text in email section");
        if ((expectedValue.isEmpty()) || ("0".equals(expectedValue))) {
            expectedValue = "confidence unknown";
        }
        assertEquals(expectedValue, digitalIdentytyTitle.textContent());
    }

    public void checkDigitalIdentityScoreNumber(Integer expectedValue) {
        Allure.step("Check advice text in email section");
        String expectedText;
        if (expectedValue == null) {
            expectedText = "0";
        } else if (expectedValue == 0) {
            expectedText = "0";
        } else {
            expectedText = String.valueOf(expectedValue) + "%";
        }
        assertEquals(expectedText, digitalIdentytyScore.textContent());
    }

    public void checkDigitalIdentityScoreRiskBarStyle(Integer expectedValue) {
        Allure.step("Check score bar in email sub-section");
        if (expectedValue == null) {
            expectedValue = 0;
        }
        if ((expectedValue < 101) && (expectedValue > 59)) {
            page.waitForSelector(DIGITAL_IDENTITY_SCORE_BAR_LOCATOR + SCORE_BAR_SUCCESS_LOCATOR);
            String expectedWidth = String.valueOf(((expectedValue * 100) / 99));
            System.out.println("expected width is " + expectedWidth);
            String widthStyle = page.locator(DIGITAL_IDENTITY_SCORE_BAR_LOCATOR + SCORE_BAR_SUCCESS_LOCATOR).getAttribute("style");
            System.out.println("width style is " + widthStyle);
            assertTrue(widthStyle.contains(expectedWidth), "expectedValue is " + expectedValue + "expectedWidth is " + expectedWidth);
        } else if ((expectedValue < 60) && (expectedValue > 39)) {
            page.waitForSelector(DIGITAL_IDENTITY_SCORE_BAR_LOCATOR + SCORE_BAR_WARNING_LOCATOR);
            String expectedWidth = String.valueOf(((expectedValue * 100) / 99));
            System.out.println("expected width is " + expectedWidth);
            String widthStyle = page.locator(DIGITAL_IDENTITY_SCORE_BAR_LOCATOR + SCORE_BAR_WARNING_LOCATOR).getAttribute("style");
            System.out.println("width style is " + widthStyle);
            assertTrue(widthStyle.contains(expectedWidth), "expectedValue is " + expectedValue + "expectedWidth is " + expectedWidth);
        } else if ((expectedValue < 40) && (expectedValue > 0)) {
            page.waitForSelector(DIGITAL_IDENTITY_SCORE_BAR_LOCATOR + SCORE_BAR_DANGER_LOCATOR);
            String expectedWidth = String.valueOf(((expectedValue * 100) / 99));
            System.out.println("expected width is " + expectedWidth);
            String widthStyle = page.locator(DIGITAL_IDENTITY_SCORE_BAR_LOCATOR + SCORE_BAR_DANGER_LOCATOR).getAttribute("style");
            System.out.println("width style is " + widthStyle);
            assertTrue(widthStyle.contains(expectedWidth), "expectedValue is " + expectedValue + "expectedWidth is " + expectedWidth);
        } else if (expectedValue == 0) {
            String expectedWidth = String.valueOf(((expectedValue * 100) / 99));
            System.out.println("expected width is " + expectedWidth);
            String widthStyle = page.locator(DIGITAL_IDENTITY_SCORE_BAR_LOCATOR + SCORE_BAR_DANGER_LOCATOR).getAttribute("style");
            System.out.println("width style is " + widthStyle);
            assertTrue(widthStyle.contains(expectedWidth), "expectedValue is " + expectedValue + "expectedWidth is " + expectedWidth);
        } else {
            System.out.println("UNHANDLED VALUE");
            assertTrue(false, "UNHANDLED VALUE");
        }
    }

    public void checkValueOfSubTableRowNAmeConfidence(String sectionTitle, String rowTitle, int expectedValue) {
        Allure.step("Check value of row " + rowTitle);
        String locator = "//div[contains(@class, 'v-attribute-table-section__title') and (text() = '" + sectionTitle + "')]/..//div[contains(@class, 'g-color-text_color_secondary') and (text() = '" + rowTitle + "')]/../following-sibling::div";
        page.waitForSelector(locator);
        assertEquals(String.valueOf(expectedValue) + "% confidence", page.locator(locator).textContent());
    }

    public void checkValueOfSubTableRowPhoneOwner(String expectedValue) {
        Allure.step("Check value of row owner");
        String locator = "//div[contains(@class, 'v-attribute-table-section__title') and (text() = 'Phone')]/..//div[contains(@class, 'g-color-text_color_secondary') and (text() = 'owner')]/../following-sibling::div";
        page.waitForSelector(locator);
        page.waitForSelector(locator);
        String owner = page.locator(locator).textContent();
        String result = switch (expectedValue) {
            case "U" -> "Unknown";
            case "N" -> "No Match";
            case "Y" -> "Full Match";
            case "P" -> "Partial Match";
            default -> "unhandled value";
        };
        assertEquals(result, owner);
    }

    public void checkValueOfSubTableRowDomainCountry(String expectedValue) {
        String locator = "//div[contains(@class, 'v-attribute-table-section__title') and (text() = 'Domain')]/..//div[contains(@class, 'g-color-text_color_secondary') and (text() = 'country')]/../following-sibling::div";
        page.waitForSelector(locator);
        String actualValue = page.locator(locator).textContent();
        String result = switch (expectedValue) {
            case "US" -> "United States";
            case "JP" -> "Japan";
            case "CY" -> "Cyprus";
            case "PH" -> "Philippines";
            default -> "unhandled value";
        };
        assertEquals(result, actualValue);
    }

    public void checkValueOfSubTableRowDomainCountryByCode(String expectedValue) {
        String locator = "//div[contains(@class, 'v-attribute-table-section__title') and (text() = 'Domain')]/..//div[contains(@class, 'g-color-text_color_secondary') and (text() = 'country')]/../following-sibling::div";
        page.waitForSelector(locator);
        String result = Country.getCountryNameByCode(expectedValue);
        String actualValue = page.locator(locator).textContent();
        assertEquals(result, actualValue);
    }

    public void checkValueOfSubTableRow(String sectionTitle, String rowTitle, int expectedValue) {
        String locator = "//div[contains(@class, 'v-attribute-table-section__title') and (text() = '" + sectionTitle + "')]/..//div[contains(@class, 'g-color-text_color_secondary') and (text() = '" + rowTitle + "')]/../following-sibling::div";
        page.waitForSelector(locator);
        assertEquals(String.valueOf(expectedValue), page.locator(locator).textContent());
    }

    public void checkValueOfSubTableRow(String sectionTitle, String rowTitle, double expectedValue) {
        String locator = "//div[contains(@class, 'v-attribute-table-section__title') and (text() = '" + sectionTitle + "')]/..//div[contains(@class, 'g-color-text_color_secondary') and (text() = '" + rowTitle + "')]/../following-sibling::div";
        page.waitForSelector(locator);
        assertEquals(String.valueOf(expectedValue), page.locator(locator).textContent());
    }

    public void checkValueOfSubTableRow(String sectionTitle, String rowTitle, String expectedValue) {
        String locator = "//div[contains(@class, 'v-attribute-table-section__title') and (text() = '" + sectionTitle + "')]/..//div[contains(@class, 'g-color-text_color_secondary') and (text() = '" + rowTitle + "')]/../following-sibling::div";
        page.waitForSelector(locator);
        assertEquals(String.valueOf(expectedValue), page.locator(locator).textContent());
    }

    public void checkValueOfBrowserLanguageRow(String expectedValue) {
        String locator = "//div[contains(@class, 'v-attribute-table-section__title') and (text() = 'Browser')]/..//div[contains(@class, 'g-color-text_color_secondary') and (text() = 'languages')]/../../following-sibling::div";
        page.waitForSelector(locator);
        assertEquals(String.valueOf(expectedValue), page.locator(locator).textContent());
    }

    public void checkSubTableRowNotPresented(String sectionTitle, String rowTitle) {
        String locator = "//div[contains(@class, 'v-attribute-table-section__title') and (text() = '" + sectionTitle + "')]/..//div[contains(@class, 'g-color-text_color_secondary') and (text() = '" + rowTitle + "')]/../following-sibling::div";
        assertFalse(page.locator(locator).isVisible());
    }

    public void checkIpSubTableRow(String rowTitle, String trueIpExpectedVal, String inputIpExpectedVal) {
        String locator1 = "//div[(@class='v-drawer-tab-ip-address__row')]/div[(text() = '" + rowTitle + "')]/following-sibling::div[1]";
        String locator2 = "//div[(@class='v-drawer-tab-ip-address__row')]/div[(text() = '" + rowTitle + "')]/following-sibling::div[2]";
        System.out.println("locator1: " + locator1);
        System.out.println("locator2: " + locator2);
        page.waitForSelector(locator1);
        page.waitForSelector(locator2);
        assertEquals(trueIpExpectedVal, page.locator(locator1).textContent(), "test true IP value");
        assertEquals(inputIpExpectedVal, page.locator(locator2).textContent(), "test input IP value");
    }


}

