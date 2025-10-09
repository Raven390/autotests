package page_objects.backoffice_pages.search;


import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import page_objects.backoffice_pages.AbstractPage;

import java.util.ArrayList;
import java.util.List;

import static utils.ConfigFactory.BASE_URL_E2E;

public class SearchPage extends AbstractPage {

    private final Locator searchInput;
    private final Locator searchButton;
    private final Locator clearButton;
    private final Locator searchResultRows;
    private final Locator noResultsMessage;

    private static final String SEARCH_RESULT_ROW = "div.v-body-row[data-qa^='clients_search__table__rows__']";
    private static final String FIRST_RESULT_ROW = "div.v-body-row >> nth=0";
    private static final String SEARCH_PAGE_PATH = "clients-search";

    public SearchPage(Page page) {
        super(page);
        this.searchInput = page.locator("//*[@data-qa='clients_search__input']/descendant::input");
        this.searchButton = page.locator("button.g-button.g-button_view_action");
        this.clearButton = page.locator("button.g-clear-button");
        this.searchResultRows = page.locator(SEARCH_RESULT_ROW);
        this.noResultsMessage = page.locator("//span[@class='g-text g-text_variant_body-2']");
    }

    @Step("Navigate to Bulk Search page")
    public void navigateToBulkSearchPage() {
        page.navigate(BASE_URL_E2E + SEARCH_PAGE_PATH);
    }

    @Step("Search by bulk input: {searchQuery}")
    public void searchByBulk(String searchQuery) {
        searchInput.waitFor();
        searchInput.fill(searchQuery);
        searchButton.click();
    }

    @Step("Clear search input")
    public void clearSearch() {
        if (clearButton.isVisible()) {
            clearButton.click();
        } else {
            searchInput.fill("");
        }
    }

    @Step("Get all search results as text")
    public List<String> getSearchResults() {
        List<String> results = new ArrayList<>();
        List<Locator> rows = searchResultRows.all();
        int count = rows.size();

        if (count == 0 && noResultsMessage.isVisible()) {
            return results; // Return an empty list if no results
        }

        for (int i = 0; i < rows.size(); i++) {
            results = rows.stream().map(Locator::textContent).toList();
        }
        return results;
    }

    @Step("Get first search result")
    public String getFirstSearchResult() {
        Locator firstRow = page.locator(FIRST_RESULT_ROW);
        firstRow.waitFor();
        return firstRow.textContent();
    }

    @Step("Click on first search result and handle new tab")
    public Page clickFirstSearchResultAndSwitchTab() {
        List<Locator> rows = searchResultRows.all();
        var firstRow = rows.getFirst();
        Locator clientLink = firstRow.locator("a.v-client-cell-with-link__client");

        // Wait for new page to open
        Page newPage = page.context().waitForPage(clientLink::click);

        // Wait for the new page to load
        newPage.waitForLoadState();

        return newPage;
    }

    @Step("Get cell value from row by data-qa")
    public String getCellValue(String rowDataQa, String columnName) {
        String cellDataQa = String.format("%s__%s", rowDataQa, columnName);
        Locator cell = page.locator(String.format("div.v-body-cell[data-qa='%s']", cellDataQa));
        if (cell.count() > 0) {
            return cell.textContent().trim();
        }
        return "";
    }

    @Step("Get structured data for search result row")
    public SearchResultRow getSearchResultRow(String serverName, Integer account) {
        String rowDataQa = String.format("clients_search__table__rows__%s_%d", serverName, account);
        Locator row = page.locator(String.format("div.v-body-row[data-qa='%s']", rowDataQa));

        if (row.count() == 0) {
            return null;
        }

        SearchResultRow data = new SearchResultRow();
        data.rowDataQa = rowDataQa;
        data.serverName = serverName;
        data.account = account.toString();

        // Extract all column values
        data.client = getCellValue(rowDataQa, "client");
        data.accountInfo = getCellValue(rowDataQa, "account");
        data.behavior = getCellValue(rowDataQa, "behavior");
        data.country = getCellValue(rowDataQa, "country");
        data.salesGroup = getCellValue(rowDataQa, "sales_group");
        data.tradingPnl = getCellValue(rowDataQa, "trading_pnl");
        data.grossPnl = getCellValue(rowDataQa, "gross_pnl");
        data.lots = getCellValue(rowDataQa, "lots");
        data.balance = getCellValue(rowDataQa, "balance");
        data.equity = getCellValue(rowDataQa, "equity");
        data.credits = getCellValue(rowDataQa, "credits");
        data.deposits = getCellValue(rowDataQa, "deposits");
        data.withdrawals = getCellValue(rowDataQa, "withdrawals");
        data.netDeposits = getCellValue(rowDataQa, "net_deposits");
        data.ibCpa = getCellValue(rowDataQa, "ib_cpa");
        data.registered = getCellValue(rowDataQa, "registered");
        data.lastLogin = getCellValue(rowDataQa, "last_login");

        return data;
    }

    @Step("Get client ID from row")
    public String getClientIdFromRow(String rowDataQa) {
        Locator clientCell = page.locator(String.format("div.v-body-cell[data-qa='%s__client']", rowDataQa));
        Locator clientIdSpan = clientCell.locator("span.v-client-cell-with-link__crm-id");
        return clientIdSpan.textContent().trim();
    }

    @Step("Get client link href from row")
    public String getClientLinkHref(String rowDataQa) {
        Locator clientCell = page.locator(String.format("div.v-body-cell[data-qa='%s__client']", rowDataQa));
        Locator link = clientCell.locator("a.v-client-cell-with-link__client");
        return link.getAttribute("href");
    }

    @Step("Get restrictions list from behavior tooltip")
    public List<String> getRestrictionsFromBehaviorTooltip(String rowDataQa) {
        // Find the behavior cell
        Locator behaviorCell = page.locator(String.format("div.v-body-cell[data-qa='%s__behavior']", rowDataQa));

        // Find the lock icon (SVG) within the cell
        Locator lockIcon = behaviorCell.locator("div.v-clients-search-table__restricted-icon svg");

        // Hover over the lock icon to show tooltip
        lockIcon.hover();

        // Wait for tooltip to appear
        Locator tooltip = page.locator("div.g-tooltip__content, div.v-tooltip__content");
        tooltip.waitFor();


        // Get all restriction items from tooltip
        List<String> restrictions = new ArrayList<>();
        List<Locator> restrictionItems = tooltip.locator("span").all();

        for (Locator item : restrictionItems) {
            String text = item.textContent().trim();
            if (!text.isEmpty()) {
                restrictions.add(text);
            }
        }

        return restrictions;
    }

    // Inner class for structured search result row data
    public static class SearchResultRow {
        public String rowDataQa;
        public String serverName;
        public String account;
        public String client;
        public String accountInfo;
        public String behavior;
        public String country;
        public String salesGroup;
        public String tradingPnl;
        public String grossPnl;
        public String lots;
        public String balance;
        public String equity;
        public String credits;
        public String deposits;
        public String withdrawals;
        public String netDeposits;
        public String ibCpa;
        public String registered;
        public String lastLogin;

        @Override
        public String toString() {
            return "SearchResultRow{" + "rowDataQa='" + rowDataQa + '\'' + ", serverName='" + serverName + '\'' + ", account='" + account + '\'' + ", client='" + client + '\'' + ", accountInfo='" + accountInfo + '\'' + ", behavior='" + behavior + '\'' + ", country='" + country + '\'' + ", salesGroup='" + salesGroup + '\'' + ", balance='" + balance + '\'' + ", equity='" + equity + '\'' + '}';
        }
    }
}