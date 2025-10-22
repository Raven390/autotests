package page_objects.backoffice_pages.alertHistory;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import page_objects.backoffice_pages.AbstractPage;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static utils.ConfigFactory.BASE_URL_E2E;


public class AlertHistoryPage extends AbstractPage {

    private final Locator alertHistoryButton;
    private final Locator alertHistoryTitle;
    private final Locator alertHistoryTableHeaders;
    private final Locator filterButton;
    private final Locator applyFilterButton;

    private static final String ALERT_HISTORY_TABLE = "//div[@class='v-table-body']";
    private static final String ALERT_HISTORY_TABLE_CELL_BY_INDEX = "//div[contains(@class,'v-body-row')]/div[%s]";
    private static final String FILTER_CONTAINER_BY_TITLE = "//div[@class='v-filter-title']/descendant::div[text()='%s']/ancestor::div[@class='v-alert-history-filters__filter-container']";
    private static final String BRAND_FILTER_CONTAINER = String.format(FILTER_CONTAINER_BY_TITLE, "Brand");
    private static final String RULES_FILTER_CONTAINER = String.format(FILTER_CONTAINER_BY_TITLE, "Rules");
    private static final String CREATION_DATE_FILTER_CONTAINER = String.format(FILTER_CONTAINER_BY_TITLE, "Creation date");
    private static final String RESOLUTION_DATE_FILTER_CONTAINER = String.format(FILTER_CONTAINER_BY_TITLE, "Resolution date");
    private static final String INVESTIGATOR_FILTER_CONTAINER = String.format(FILTER_CONTAINER_BY_TITLE, "Investigator");

    private static final String LOADER_ANIMATION = ".v-loader";
    private final Locator clickableRow;
    private final Locator drawer;

    public AlertHistoryPage(Page page) {
        super(page);
        this.alertHistoryButton = page.locator("//div[@class='v-sidebar__menu']/descendant::a[contains(@href,'alert-history')]");
        this.alertHistoryTitle = page.locator("//div[@class='v-alert-history-layout__header']/div").first();
        this.alertHistoryTableHeaders = page.locator("//div[contains(@class,'v-header-cell')]");
        this.filterButton = page.locator("//div[@class='v-alert-history-filter-button__filters']/button");
        this.applyFilterButton = page.locator("//span[text()='Apply']/..");
        this.clickableRow = page.locator(".v-body-row_clickable");
        this.drawer = page.locator("[data-qa=\"drawer_body\"]");
    }

    @Override
    @Step("Wait for page to load")
    public void waitForPageToLoad() {
        page.waitForSelector(ALERT_HISTORY_TABLE, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
    }

    @Step("Open alert history")
    public void openAlertHistory() {
        alertHistoryButton.click();
        waitForPageToLoad();
    }

    @Step("Get alert history page title")
    public String getTitle() {
        return alertHistoryTitle.textContent();
    }

    @Step("Get list of alert history table headers")
    public List<String> getTableHeaders() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < alertHistoryTableHeaders.count(); i++) {
            list.add(alertHistoryTableHeaders.nth(i).textContent());
        }
        return list;
    }

    @Step("Get list of alert history table headers")
    public void notHaveQc() {
        Allure.step("check if QC column is not present in alert history table");
        assertThat(getTableHeaders(), not(contains("QC", "REVIEWER", "QC NOTE")));
        clickableRow.nth(0).click();
        page.waitForTimeout(100);
        assertFalse(drawer.isVisible());

    }

    public void navigateAlertHistory() {
        Allure.step("navigate alert history page");
        page.navigate(BASE_URL_E2E + "alert-history");
        super.waitForPageToLoad();
    }

    private List<String> getCellValuesByColumnIndex(int columnIndex) {
        List<String> list = new ArrayList<>();
        Locator cells = page.locator(String.format(ALERT_HISTORY_TABLE_CELL_BY_INDEX, columnIndex));
        for (int i = 0; i < cells.count(); i++) {
            list.add(cells.nth(i).textContent());
        }
        return list;
    }

    @Step("Get list of client values in alert history table")
    public List<String> getClientValues() {
        return getCellValuesByColumnIndex(1);
    }

    @Step("Get list of alerted rule values in alert history table")
    public List<String> getAlertedRuleValues() {
        return getCellValuesByColumnIndex(2);
    }

    @Step("Get list of created values in alert history table")
    public List<String> getCreatedValues() {
        return getCellValuesByColumnIndex(3);
    }

    @Step("Get list of resolved values in alert history table")
    public List<String> getResolvedValues() {
        return getCellValuesByColumnIndex(4);
    }

    @Step("Get list of duration values in alert history table")
    public List<String> getDurationValues() {
        return getCellValuesByColumnIndex(5);
    }

    @Step("Get list of investigator values in alert history table")
    public List<String> getInvestigatorValues() {
        return getCellValuesByColumnIndex(6);
    }

    @Step("Click filter button")
    public void clickFilterButton() {
        filterButton.click();
    }

    @Step("Select brand in filter")
    public void selectBrandFilter(String text) {
        page.locator(String.format(BRAND_FILTER_CONTAINER + "/descendant::span[text()='%s']/..", text)).click();
    }

    @Step("Select rules in filter")
    public void selectRulesFilter(String text) {
        page.locator(RULES_FILTER_CONTAINER + "/descendant::span[text()='Show more']/..");
        page.locator(RULES_FILTER_CONTAINER + "/descendant::div[@class='v-checkbox-list__filter']/descendant::input").fill(text);
        page.locator(String.format(RULES_FILTER_CONTAINER + "/descendant::div[text()='%s']/ancestor::div[@class='v-checkbox-list__item']/descendant::input", text)).click();
    }

    @Step("Select creation date in filter")
    public void selectCreationDate(String from, String to) {
        selectDateRangeInElement(page.locator(CREATION_DATE_FILTER_CONTAINER + "/descendant::input"), from, to);
    }

    @Step("Select resolution date in filter")
    public void selectResolutionDate(String from, String to) {
        selectDateRangeInElement(page.locator(RESOLUTION_DATE_FILTER_CONTAINER + "/descendant::input"), from, to);
    }

    @Step("Select investigator in filter")
    public void selectInvestigatorFilter(String text) {
        page.locator(String.format(INVESTIGATOR_FILTER_CONTAINER + "/descendant::div[text()='%s']/ancestor::div[@class='v-checkbox-list__item']/descendant::input", text)).click();
    }

    @Step("Apply filter")
    public void applyFilter() {
        applyFilterButton.click();
        page.waitForSelector(LOADER_ANIMATION, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.DETACHED));
    }

    public List<String> getUcidBrandValues() {
        List<String> list = new ArrayList<>();
        Locator cells = page.locator(String.format(ALERT_HISTORY_TABLE_CELL_BY_INDEX, 1) + "/a");
        Pattern pattern = Pattern.compile(".*/([^/-]+)-\\d+");
        for (int i = 0; i < cells.count(); i++) {
            String href = cells.nth(i).getAttribute("href");
            if (href != null) {
                Matcher matcher = pattern.matcher(href);
                if (matcher.find()) {
                    list.add(matcher.group(1));
                }
            }
        }
        return list;
    }
}

