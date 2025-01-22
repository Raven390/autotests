package pageObjects.backofficePages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Allure;

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
    private static final String SORTABLE_CELL_LOCATOR = "//*[contains(@class, 'v-sortable-cell')]";

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
        this.filterOs = page.locator("//*[contains(@class, 'v-activity-tab-filters__filter-section')]//*[text()='Any OS']");
        this.filterRisk = page.locator("//*[contains(@class, 'v-activity-tab-filters__filter-section')]//*[text()='Any risk rating']");
        this.filterDate = page.locator("//*[contains(@class, 'v-activity-tab-filters__filter-section')]//*[text()='Lifetime']");
        this.filterOption = page.locator("//*[@data-qa='select-list']//*[@role='option']//span");
        this.filterClearButton = page.locator("//button[@data-qa='select-clear']");
        this.sortByScoreButton = page.locator(TABLE_HEADERS_LOCATOR + COLUMN_SCORE_LOCATOR + SORTABLE_CELL_LOCATOR);
        this.sortByDateButton = page.locator(TABLE_HEADERS_LOCATOR + COLUMN_DATE_LOCATOR + SORTABLE_CELL_LOCATOR);
        this.tooltip = page.locator("[role='tooltip']");
    }

    public void navigate(String ucid) {
        Allure.step("Navigate to users activity tab");
        page.navigate(BASE_URL_E2E + "investigation/" + ucid + "/activity");
        waitForPageToLoad();
        waitForPageToLoad();
    }

    public void openTab(String ucid) {
        Allure.step("open users activity tab by click tab");
        page.navigate(BASE_URL_E2E + "investigation/" + ucid + "/");
        waitForPageToLoad();
        activityTab.click();
        waitForPageToLoad();
    }

    public void checkDateColumnValue(String expectedDate, String expectedTime) {
        Allure.step("check values in the dates column");
        String actualDate = columnDateDataPrimary.textContent();
        assertEquals(expectedDate, actualDate);
        String actualTime = columnDateDataSecodary.textContent();
        assertEquals(expectedTime, actualTime);
    }

    public void checkDateColumnValue(String dateTime) {
        Allure.step("check values in the dates column");
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
        Allure.step("check that expected values in the dates column are presented");
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
        Allure.step("check values in the dates column not include unwanted values");
        String[] dates = unexpectedDateTime.split(" ");
        String unexpectedDate = dates[0];
        assertFalse(columnDateDataPrimary.getByText(unexpectedDate).isVisible());
    }

    public void checkEventColumnValue(String expectedValue) {
        Allure.step("check values in the event column");
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
        Allure.step("check values in the agent column");
        if ("agent_mobile".equals(expectedValue)) {
            expectedValue = " Mobile app";
        }
        if ("browser_mobile".equals(expectedValue)) {
            expectedValue = " Mobile browser";
        }
        if ("browser_computer".equals(expectedValue)) {
            expectedValue = " Web browser";
        }
        String actualValue = columnAgentData.textContent();
        assertEquals(expectedValue, actualValue);
    }

    public void checkOsColumnValue(String expectedValue) {
        Allure.step("check values in the OS column");
        String actualValue = columnOsData.textContent();
        assertEquals(expectedValue, actualValue);
    }

    public void checkOsColumnValue(String expectedValue, int index) {
        Allure.step("check values in the OS column");
        String actualValue = columnOsData.nth(index).textContent();
        assertEquals(expectedValue, actualValue);
    }

    public void checkRiskColumnValue(String expectedValue) {
        Allure.step("check values in the risk column");
        expectedValue = expectedValue.substring(0, 1).toUpperCase() + expectedValue.substring(1);
        String actualValue = columnRiskData.textContent();
        assertEquals(expectedValue, actualValue);
    }

    public void checkRiskColumnValue(String expectedValue, int index) {
        Allure.step("check values in the risk column");
        expectedValue = expectedValue.substring(0, 1).toUpperCase() + expectedValue.substring(1);
        String actualValue = columnRiskData.nth(index).textContent();
        assertEquals(expectedValue, actualValue);
    }

    public void checkRiskColumnColourDanger() {
        Allure.step("check that colour of the text in the risk column is danger");
        columnRiskDataDanger.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertFalse(columnRiskDataPositive.isVisible());
    }

    public void checkRiskColumnColourNeutral() {
        Allure.step("check that colour of the text in the risk column is neutral");
        columnRiskDataNeutral.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertFalse(columnRiskDataDanger.isVisible());
    }

    public void checkRiskColumnColourPositive() {
        Allure.step("check that colour of the text in the risk column is positive");
        columnRiskDataPositive.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertFalse(columnRiskDataDanger.isVisible());
    }

    public void checkScoreColumnValue(int expectedValue) {
        Allure.step("check values in the score column");
        int actualValue = Integer.parseInt(columnScoreData.textContent());
        assertEquals(expectedValue, actualValue);
    }

    public void checkScoreColumnIsAsc() {
        waitForPageToLoad();
        assertTrue(columnScoreData.count() > 1);
        int firstScore = Integer.parseInt(columnScoreData.first().textContent());
        int lastScore = Integer.parseInt(columnScoreData.last().textContent());
        assertTrue(firstScore < lastScore);
    }

    public void checkScoreColumnIsDesc() {
        waitForPageToLoad();
        assertTrue(columnScoreData.count() > 1);
        int firstScore = Integer.parseInt(columnScoreData.first().textContent());
        int lastScore = Integer.parseInt(columnScoreData.last().textContent());
        System.out.println("firstScore: " + firstScore + " and lastScore" + lastScore);
        assertTrue(firstScore > lastScore);
    }

    public void checkScoreColumnColourDanger() {
        Allure.step("check colour of the text in the risk column");
        columnScoreDataDanger.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

    }

    public void checkScoreColumnColourNeutral() {
        Allure.step("check that colour of the text in the risk column is neutral");
        columnScoreDataNeutral.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertFalse(columnRiskDataDanger.isVisible());
    }

    public void checkScoreColumnColourPositive() {
        Allure.step("check that colour of the text in the risk column is positive");
        columnScoreDataPositive.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertFalse(columnRiskDataDanger.isVisible());
    }

    public void checkPoliciesColumnValue(String expectedValue) {
        Allure.step("check values in the policies column");
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
        Allure.step("check presented filter options");
        filterOption.getByText(expectedOption).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public void clickFilterOption(String expectedOption) {
        Allure.step("check presented filter options " + expectedOption);
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
        Allure.step("filter record by score asc");
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
        Allure.step("filter record by score desc");
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
        Allure.step("filter record by score desc");
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
        Allure.step("filter record by score desc");
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

}

