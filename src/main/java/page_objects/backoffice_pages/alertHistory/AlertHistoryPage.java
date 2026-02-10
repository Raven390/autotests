package page_objects.backoffice_pages.alertHistory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static utils.ConfigFactory.BASE_URL_E2E;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import helpers.data.ClientHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import page_objects.backoffice_pages.AbstractPage;

public class AlertHistoryPage extends AbstractPage {

    private final Locator alertHistoryButton;
    private final Locator alertHistoryTitle;
    private final Locator alertHistoryTableHeaders;
    private final Locator filterButton;
    private final Locator applyFilterButton;
    private final Locator commentTextArea;
    private final Locator correctResolutionButton;
    private final Locator clickableRow;
    private final Locator drawer;

    private static final String ALERT_HISTORY_TABLE = "//div[@class='v-table-body']";
    private static final String ALERT_HISTORY_TABLE_CELL_BY_INDEX = "//div[contains(@class,'v-body-row')]/div[%s]";
    private static final String FILTER_CONTAINER_BY_TITLE =
            "//div[@class='v-filter-title']/descendant::div[text()='%s']/ancestor::div[@class='v-alert-history-filters__filter-container']";
    private static final String BRAND_FILTER_CONTAINER = String.format(FILTER_CONTAINER_BY_TITLE, "Brand");
    private static final String RULES_FILTER_CONTAINER = String.format(FILTER_CONTAINER_BY_TITLE, "Rules");
    private static final String CREATION_DATE_FILTER_CONTAINER =
            String.format(FILTER_CONTAINER_BY_TITLE, "Creation date");
    private static final String RESOLUTION_DATE_FILTER_CONTAINER =
            String.format(FILTER_CONTAINER_BY_TITLE, "Resolution date");
    private static final String INVESTIGATOR_FILTER_CONTAINER =
            String.format(FILTER_CONTAINER_BY_TITLE, "Investigator");
    private static final String LOADER_ANIMATION = ".v-loader";
    private static final String TABLE_ROW_BY_USER_ID =
            "//*[contains(@class,'v-client-cell-with-link__crm-id') and text()='%s']/ancestor::div[contains(@class,'v-body-row')]";
    private static final String QC_CHECK_BUTTON_PATTERN = "//button[@data-qa='buttons_list__item__%s']";
    private static final String REVIEWER_FILTER_PATTERN =
            "//*[text()='%s']/ancestor::label[contains(@data-qa,'alert_history_filters__reviewers__item')]/descendant::input";
    private final Locator filterByIdInput;

    public AlertHistoryPage(Page page) {
        super(page);
        this.alertHistoryButton =
                page.locator("//div[@class='v-sidebar__menu']/descendant::a[contains(@href,'alert-history')]");
        this.alertHistoryTitle = page.locator("//div[@class='v-alert-history-layout__header']/div")
                .first();
        this.alertHistoryTableHeaders = page.locator("//div[contains(@class,'v-header-cell')]");
        this.filterButton = page.locator("//div[@class='v-alert-history-filter-button__filters']/button");
        this.applyFilterButton = page.locator(
                "//button[contains(@class,'g-button_view_action') and contains(@class,'g-button_size_l')]");
        this.clickableRow = page.locator(".v-body-row_clickable");
        this.drawer = page.locator("[data-qa=\"drawer_body\"]");
        this.commentTextArea = page.locator("//textarea[@class='g-text-area__control']");
        this.correctResolutionButton = page.locator("//*[@data-qa='qc_decision_drawer__correct_resolution_button']");
        this.filterByIdInput = page.locator("//input[@placeholder='Enter client ID to filter table']");
    }

    @Override
    @Step("Wait for page to load")
    public void waitForPageToLoad() {
        page.waitForSelector(
                ALERT_HISTORY_TABLE, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
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
        page.locator(String.format(BRAND_FILTER_CONTAINER + "/descendant::span[text()='%s']/..", text))
                .click();
    }

    @Step("Select rules in filter")
    public void selectRulesFilter(String text) {
        page.locator(RULES_FILTER_CONTAINER + "/descendant::span[text()='Show more']/..");
        page.locator(RULES_FILTER_CONTAINER + "/descendant::div[@class='v-checkbox-list__filter']/descendant::input")
                .fill(text);
        page.locator(String.format(
                        RULES_FILTER_CONTAINER
                                + "/descendant::div[text()='%s']/ancestor::div[@class='v-checkbox-list__item']/descendant::input",
                        text))
                .click();
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
        page.locator(String.format(
                        INVESTIGATOR_FILTER_CONTAINER
                                + "/descendant::div[text()='%s']/ancestor::div[@class='v-checkbox-list__item']/descendant::input",
                        text))
                .click();
    }

    @Step("Apply filter")
    public void applyFilter() {
        applyFilterButton.click();
        page.waitForSelector(
                LOADER_ANIMATION, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.DETACHED));
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

    @Step("Open alert history drawer by user id")
    public void openAlertHistoryDrawerByClient(ClientHelper client) {
        page.locator(String.format(TABLE_ROW_BY_USER_ID, client.getUserId())).click();
    }

    @Step("Fill qa qc comment")
    public void fillQaQcComment(String text) {
        commentTextArea.fill(text);
    }

    @Step("Click correct resolution button")
    public void clickCorrectResolutionButton() {
        correctResolutionButton.click();
    }

    @Step("Select qc check result in filter")
    public void selectQcCheckResultFilter(String text) {
        page.locator(String.format(QC_CHECK_BUTTON_PATTERN, text)).click();
    }

    public List<String> getQcResultValues() {
        return page.locator(String.format(ALERT_HISTORY_TABLE_CELL_BY_INDEX, 8)).all().stream()
                .map(cell -> {
                    Locator danger = cell.locator("div.g-color-text_color_danger");
                    if (danger.count() > 0) return "Incorrect";

                    Locator positive = cell.locator("div.g-color-text_color_positive");
                    if (positive.count() > 0) return "Correct";

                    return null; // or "Unknown", up to you
                })
                .filter(Objects::nonNull)
                .toList();
    }

    @Step("Select reviewer in filter")
    public void selectReviewerFilter(String text) {
        page.locator(String.format(REVIEWER_FILTER_PATTERN, text)).click();
    }

    @Step("Get list of reviewer values in alert history table")
    public List<String> getReviewerValues() {
        return getCellValuesByColumnIndex(9);
    }

    public void checkCountOfAlertsInFilter(int expectedCount) {
        String count = applyFilterButton.textContent().split(" ")[1];
        assertEquals(expectedCount, Integer.valueOf(count));
    }

    public void fillFilterById(String id) {
        filterByIdInput.fill(id);
    }
}
