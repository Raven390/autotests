package pageObjects.backofficePages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.BoundingBox;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.hamcrest.MatcherAssert;
import utils.Utils;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.*;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.Constants.MT4_TRADES_COERCED_TABLE_NAME;
import static utils.Utils.*;

public class TradingPage extends AbstractPage {

    private final Locator tradingTab;
    private final Locator accountsTab;
    private final Locator operationsTab;
    private final Locator accountsTabContent;
    private final Locator dealsTabContent;
    private final Locator accountColumnHeader;
    private final Locator typeColumnHeader;
    private final Locator volumeColumnHeader;
    private final Locator profitColumnHeader;
    private final Locator openColumnHeader;
    private final Locator closeColumnHeader;
    private final Locator tpslColumnHeader;
    private final Locator swapColumnHeader;
    private final Locator srColumnHeader;
    private final Locator commissionColumnHeader;
    private final Locator methodColumnHeader;
    private final Locator commentColumnHeader;
    private final Locator accountColumnCell;
    private final Locator typeColumnCell;
    private final Locator profitColumnCell;
    private final Locator volumeColumnCell;
    private final Locator openColumnCell;
    private final Locator closeColumnCell;
    private final Locator tpslColumnCell;
    private final Locator swapColumnCell;
    private final Locator srColumnCell;
    private final Locator commissionColumnCell;
    private final Locator methodColumnCell;
    private final Locator commentColumnCell;
    private final Locator filterButton;
    private final Locator filterMenu;
    private final Locator checkboxItem;
    private final Locator typeShowMoreButton;
    private final Locator applyFiltersButton;
    private final Locator filterContainer;
    private final Locator volumeFromInput;
    private final Locator volumeToInput;
    private final Locator volumeColumnCellUSD;
    private final Locator openColumnCellDate;
    private final Locator closeColumnCellDate;
    private final Locator durationToInput;
    private final Locator durationFromInput;
    private final Locator profitToInput;
    private final Locator profitFromInput;
    private final Locator accountCard;
    private final Locator accountId;
    private final Locator balanceElement;
    private final Locator statusElement;
    private final Locator platformElement;
    private final Locator accountTypeElement;
    private final Locator createdTimeElement;
    private final Locator updatedTimeElement;
    private final Locator popupElement;
    private final Locator tableViewButton;
    private final Locator accountRow;
    private final Locator accountTableId;
    private final Locator accountTablePlatform;
    private final Locator accountTableType;
    private final Locator accountTableStatus;
    private final Locator accountTableCreated;
    private final Locator accountTableUpdated;
    private final Locator accountTableBalance;
    private final Locator accountTableTotalPnl;
    private final Locator accountTableEquity;
    private final Locator accountTableCredit;
    private final Locator accountTableLeverage;
    private final Locator accountTableMarginFree;
    private final Locator accountTableServer;
    private final Locator accountTableGroup;
    private final Locator accountTableHeaderAccount;
    private final Locator accountTableHeaderType;
    private final Locator accountTableHeaderStatus;
    private final Locator accountTableHeaderCreated;
    private final Locator accountTableHeaderUpdated;
    private final Locator accountTableHeaderBalance;
    private final Locator accountTableHeaderTotalPnl;
    private final Locator accountTableHeaderEquity;
    private final Locator accountTableHeaderCredit;
    private final Locator accountTableHeaderLeverage;
    private final Locator accountTableHeaderMarginFree;
    private final Locator accountTableHeaderServer;
    private final Locator accountTableHeaderGroup;
    private final Locator operationOpenSortElement;
    private final Locator openDatePicker;
    private final Locator closeDatePicker;
    private final Locator typeCheckboxes;
    private final Locator accountsCheckboxes;
    private final Locator symbolCheckboxes;
    private final Locator methodCheckboxes;
    private final Locator resetTypeButton;
    private final Locator resetAccountsButton;
    private final Locator resetSymbolButton;
    private final Locator resetMethodButton;
    private final Locator resetOpenDateButton;
    private final Locator resetCloseDateButton;
    private final Locator resetDurationButton;
    private final Locator resetProfitButton;
    private final Locator resetVolumeButton;
    private final Locator resetAllButton;
    private final Locator openDateTooltip;
    private final Locator profitTooltip;
    private final Locator typeCheckboxLabels;
    private final Locator methodCheckboxLabels;
    private final Locator summaryTab;
    private final Locator totalPnlMaxProfitValue;
    private final Locator totalPnlMaxProfitLabel;
    private final Locator totalPnlMaxLossValue;
    private final Locator totalPnlMaxLossLabel;
    private final Locator totalPnlMaxProfitGraphDot;
    private final Locator totalPnlMaxLossGraphDot;
    private final Locator totalPnlYAxisLabel;
    private final Locator totalPnlTooltip;
    private final Locator totalPnlChartTitle;
    private final Locator performanceOverviewTableTitle;
    private final Locator performanceOverviewTableHeaders;
    private final Locator performanceOverviewSymbols;
    private final Locator winrateWidget;
    private final Locator winrateWidgetValue;
    private final Locator winrateWidgetInfo;
    private final Locator volumeChartTitle;
    private final Locator volumeYAxisLabel;
    private final Locator volumeMaxValue;
    private final Locator volumeTotalValue;
    private final Locator volumeMaxLabel;
    private final Locator volumeTotalLabel;
    private final Locator volumeMaxGraphDot;
    private final Locator pnlByDurationGraphSection;
    private final Locator pnlByDurationTooltip;
    private final Locator totalPnlXAxisLabels;
    private final Locator volumeXAxisLabels;

    private static final String ACCOUNT_CARD_VALUE_BY_TITLE_PATTERN = "//div[contains(@class,'v-trading-tab-accounts-card__column-title') and text()='%s']/following-sibling::div";
    private static final String POPUP_ELEMENT_XPATH = "//div[contains(@class,'g-popup_open')]";
    private static final String ACCOUNT_TABLE_COLUMN = "//td[contains(@class,'v-trading-tab-accounts-table__column_type_account')]%s";
    private static final String ACCOUNT_DATES_ELEMENT = "//div[@class='v-trading-tab-accounts-card__dates']%s";
    private static final String ACCOUNT_TABLE_HEADER_PATTERN = "//th[contains(@class,'v-trading-tab-accounts-table__column_type_%s')]";
    private static final String ACCOUNT_TABLE_CELL_PATTERN = "//td[contains(@class,'v-trading-tab-accounts-table__column_type_%s')]/div";
    private static final String FILTER_CONTAINER = "//div[text()='%s']/ancestor::div[@class='v-trading-tab-deals-filter__filter-container']";
    private static final String DATE_PICKER_BY_LABEL_PATTERN = FILTER_CONTAINER + "/descendant::input";
    private static final String PRESET_BY_LABEL_AND_VALUE_PATTERN = FILTER_CONTAINER + "/descendant::span[text()='%s']";
    private static final String CHECKBOXES_BY_LABEL_PATTERN = FILTER_CONTAINER + "/descendant::input[@type='checkbox']";
    private static final String RESET_BUTTON_BY_LABEL_PATTERN = FILTER_CONTAINER + "/descendant::span[text()='Reset']";
    private static final String TOOLTIP_BY_LABEL_PATTERN = "//div[text()='%s']/following-sibling::div";
    private static final String ACCOUNT_CARD_XPATH = "//div[@class='v-trading-tab-accounts-card']";
    private static final String CHECKBOX_LABEL_BY_TITLE_PATTERN = "//div[text()='%s']/ancestor::div[@class='v-checkbox-list']/descendant::span[@class='g-control-label__text']";
    private static final String WIDGET_CONTAINER_PATTERN = "//div[text()='%s']/ancestor::div[@class='v-trading-summary__chart']";
    private static final String CHART_CONTAINER_PATTERN = "//div[text()='%s']/following-sibling::span[text()='%s']/ancestor::div[@class='v-trading-summary__chart']";
    private static final String TOTAL_PNL_CHART_CONTAINER = String.format(CHART_CONTAINER_PATTERN, "PNL", "total, USD");
    private static final String TOTAL_PNL_CHART_FEATURES = String.format("%s/descendant::div[@class='v-chart-wrapper__feature']", TOTAL_PNL_CHART_CONTAINER);
    private static final String TOTAL_PNL_CHART = String.format("%s/descendant::div[@class='v-trading-summary-total-pnl__chart-container']", TOTAL_PNL_CHART_CONTAINER);
    private static final String TOTAL_PNL_X_AXIS_LABEL_BY_TEXT_PATTERN = "//div[@class='v-trading-summary-total-pnl__ticks-container']/descendant::div[contains(@class,'g-text') and text()='%s']";
    private static final String PERFORMANCE_OVERVIEW_CHART_CONTAINER = String.format(WIDGET_CONTAINER_PATTERN, "Performance overview");
    private static final String CHART_TITLE = "//div[@class='v-chart-wrapper__title']";
    private static final String PERFORMANCE_OVERVIEW_ROW_BY_SYMBOL_PATTERN = PERFORMANCE_OVERVIEW_CHART_CONTAINER + "/descendant::td[text()='%s']/parent::tr";
    private static final String PERFORMANCE_OVERVIEW_DEALS_BY_SYMBOL_PATTERN = PERFORMANCE_OVERVIEW_ROW_BY_SYMBOL_PATTERN + "/td[contains(@class,'v-trading-summary-performance__column_type_deals')]";
    private static final String PERFORMANCE_OVERVIEW_WINRATE_BY_SYMBOL_PATTERN = PERFORMANCE_OVERVIEW_ROW_BY_SYMBOL_PATTERN + "/td[contains(@class,'v-trading-summary-performance__column_type_winrate')]";
    private static final String PERFORMANCE_OVERVIEW_HFT_BY_SYMBOL_PATTERN = PERFORMANCE_OVERVIEW_ROW_BY_SYMBOL_PATTERN + "/td[contains(@class,'v-trading-summary-performance__column_type_os')]";
    private static final String PERFORMANCE_OVERVIEW_PNL_BY_SYMBOL_PATTERN = PERFORMANCE_OVERVIEW_ROW_BY_SYMBOL_PATTERN + "/td[contains(@class,'v-trading-summary-performance__column_type_risk')]";
    private static final String WIDGET_BY_TITLE_PATTERN = "//div[contains(@class,'v-number-widget__title') and text()='%s']/..";
    private static final String VOLUME_CHART_CONTAINER = String.format(CHART_CONTAINER_PATTERN, "Volume", "USD");
    private static final String VOLUME_CHART_FEATURES = String.format("%s/descendant::div[@class='v-chart-wrapper__feature']", VOLUME_CHART_CONTAINER);
    private static final String VOLUME_CHART = String.format("%s/descendant::div[@class='v-trading-summary-volume__chart-container']", VOLUME_CHART_CONTAINER);
    private static final String PNL_BY_DURATION = "//div[text()='PNL']/following-sibling::span[text()='by trade duration, USD']/ancestor::div[@class='v-trading-summary__chart']";
    private static final String PNL_BY_DURATION_0_10_ANNOTATION = "//*[@style='position: absolute; transform: translate(calc(-50% + 94.9px), 196px);']";
    private static final String PNL_BY_DURATION_10_30_ANNOTATION = "//*[@style='position: absolute; transform: translate(calc(-50% + 284.7px), 196px);']";
    private static final String PNL_BY_DURATION_05_6_ANNOTATION = "//*[@style='position: absolute; transform: translate(calc(-50% + 474.5px), 196px);']";
    private static final String PNL_BY_DURATION_6_24_ANNOTATION = "//*[@style='position: absolute; transform: translate(calc(-50% + 664.3px), 196px);']";
    private static final String PNL_BY_DURATION_MORE24_ANNOTATION = "//*[@style='position: absolute; transform: translate(calc(-50% + 854.1px), 196px);']";
    private static final String PNL_BY_DURATION_ANNOTATION = "//div[@class='v-trading-summary-pnl-by-duration__ticks-container']/div/div";
    private static final String PNL_BY_DURATION_TOOLTIP = "//div[@class='v-trading-summary-pnl-by-duration__tooltip']";
    private static final String GREEN_TEXT = "//*[contains(@class, 'g-color-text_color_brand')]";
    private static final String RED_TEXT = "//*[contains(@class, 'g-color-text_color_danger')]";
    private static final String PNL_DURATION_GRAPH = "//*[contains(@class, 'v-trading-summary-pnl-by-duration')]";

    public TradingPage(Page page) {
        super(page);
        this.tradingTab = page.locator("[role=\"tab\"][title=\"Trading\"]");
        this.accountsTab = page.locator(".g-radio-button__option-control[value=\"Accounts\"]");
        this.operationsTab = page.locator(".g-radio-button__option-control[value=\"Deals\"]");
        this.accountsTabContent = page.locator(".v-trading-tab-accounts");
        this.dealsTabContent = page.locator(".v-trading-tab-deals");
        this.accountColumnHeader = page.locator(".g-table__head .v-trading-tab-deals__column_type_account");
        this.typeColumnHeader = page.locator(".g-table__head .v-trading-tab-deals__column_type_type");
        this.volumeColumnHeader = page.locator(".g-table__head .v-trading-tab-deals__column_type_volume");
        this.profitColumnHeader = page.locator(".g-table__head .v-trading-tab-deals__column_type_profit");
        this.openColumnHeader = page.locator(".g-table__head .v-trading-tab-deals__column_type_open");
        this.closeColumnHeader = page.locator(".g-table__head .v-trading-tab-deals__column_type_close");
        this.tpslColumnHeader = page.locator(".g-table__head .v-trading-tab-deals__column_type_tp");
        this.swapColumnHeader = page.locator(".g-table__head .v-trading-tab-deals__column_type_swap");
        this.srColumnHeader = page.locator(".g-table__head .v-trading-tab-deals__column_type_sr");
        this.commissionColumnHeader = page.locator(".g-table__head .v-trading-tab-deals__column_type_commission");
        this.methodColumnHeader = page.locator(".g-table__head .v-trading-tab-deals__column_type_method");
        this.commentColumnHeader = page.locator(".g-table__head .v-trading-tab-deals__column_type_comment");
        this.accountColumnCell = page.locator(".g-table__body .v-trading-tab-deals__column_type_account");
        this.typeColumnCell = page.locator(".g-table__body .v-trading-tab-deals__column_type_type");
        this.profitColumnCell = page.locator(".g-table__body .v-trading-tab-deals__column_type_profit");
        this.volumeColumnCell = page.locator(".g-table__body .v-trading-tab-deals__column_type_volume");
        this.volumeColumnCellUSD = page.locator(".g-table__body .v-trading-tab-deals__column_type_volume .g-color-text_color_secondary");
        this.openColumnCellDate = page.locator(".g-table__body .v-trading-tab-deals__column_type_open .g-color-text_color_secondary");
        this.closeColumnCellDate = page.locator(".g-table__body .v-trading-tab-deals__column_type_close .g-color-text_color_secondary");
        this.openColumnCell = page.locator(".g-table__body .v-trading-tab-deals__column_type_open");
        this.closeColumnCell = page.locator(".g-table__body .v-trading-tab-deals__column_type_close");
        this.tpslColumnCell = page.locator(".g-table__body .v-trading-tab-deals__column_type_tp");
        this.swapColumnCell = page.locator(".g-table__body .v-trading-tab-deals__column_type_swap");
        this.srColumnCell = page.locator(".g-table__body .v-trading-tab-deals__column_type_sr");
        this.commissionColumnCell = page.locator(".g-table__body .v-trading-tab-deals__column_type_commission");
        this.methodColumnCell = page.locator(".g-table__body .v-trading-tab-deals__column_type_method");
        this.commentColumnCell = page.locator(".g-table__body .v-trading-tab-deals__column_type_comment");
        this.filterButton = page.locator(".v-trading-tab-deals__filters button");
        this.filterMenu = page.locator("[data-qa=\"drawer_body\"] .v-trading-tab-deals-filter__content");
        this.checkboxItem = page.locator(".v-trading-tab-deals-filter__filter-container  .g-checkbox");
        this.typeShowMoreButton = page.locator(".v-trading-tab-deals-filter__filter-container button").getByText("Show more");
        this.applyFiltersButton = page.locator("button").getByText("Apply");
        this.filterContainer = page.locator("v-trading-tab-deals-filter__filter-container");
        this.volumeFromInput = page.locator("//div[text()=\"Volume\"]/ancestor::div[contains(@class,'v-numeric-range-input')]/descendant::span[text()=\"From\"]/ancestor::span/input");
        this.volumeToInput = page.locator("//div[text()=\"Volume\"]/ancestor::div[contains(@class,'v-numeric-range-input')]/descendant::span[text()=\"To\"]/ancestor::span/input");
        this.durationFromInput = page.locator("//div[text()=\"Duration\"]/ancestor::div[contains(@class,'v-numeric-range-input')]/descendant::span[text()=\"From\"]/ancestor::span/input");
        this.durationToInput = page.locator("//div[text()=\"Duration\"]/ancestor::div[contains(@class,'v-numeric-range-input')]/descendant::span[text()=\"To\"]/ancestor::span/input");
        this.profitFromInput = page.locator("//div[text()=\"Profit\"]/ancestor::div[contains(@class,'v-numeric-range-input')]/descendant::span[text()=\"From\"]/ancestor::span/input");
        this.profitToInput = page.locator("//div[text()=\"Profit\"]/ancestor::div[contains(@class,'v-numeric-range-input')]/descendant::span[text()=\"To\"]/ancestor::span/input");
        this.accountCard = page.locator(ACCOUNT_CARD_XPATH);
        this.accountId = page.locator("//span[contains(@class,'g-text_variant_subheader-2')]");
        this.balanceElement = page.locator("//div[@class='v-trading-tab-accounts-card__balance']");
        this.statusElement = page.locator("//div[contains(@class,'v-trading-account-status-label')]/div[@class='v-text-with-icon__text']");
        this.platformElement = page.locator("//div[@class='v-trading-tab-accounts-card__left-col-footer']/div[@class='v-trading-tab-accounts-card__tooltip-wrap'][2]/descendant::div[@class='v-text-with-icon__text']");
        this.accountTypeElement = page.locator("//div[contains(@class,'v-trading-tab-accounts-card__account-type')]/descendant::div[@class='v-text-with-icon__text']");
        this.createdTimeElement = page.locator(String.format(ACCOUNT_DATES_ELEMENT, "/div[1]/descendant::div[@class='v-text-with-icon__text']"));
        this.updatedTimeElement = page.locator(String.format(ACCOUNT_DATES_ELEMENT, "/div[2]/descendant::div[@class='v-text-with-icon__text']"));
        this.popupElement = page.locator(POPUP_ELEMENT_XPATH);
        this.tableViewButton = page.locator("//input[@value='TABLE']");
        this.accountRow = page.locator("//tr[contains(@class,'g-table__row_vertical-align_top')]");
        this.accountTableId = page.locator(String.format(ACCOUNT_TABLE_COLUMN, "/descendant::div[contains(@class,'g-color-text_color_primary')]"));
        this.accountTablePlatform = page.locator(String.format(ACCOUNT_TABLE_COLUMN, "/descendant::div[contains(@class,'g-color-text_color_secondary')]"));
        this.accountTableType = page.locator(String.format(ACCOUNT_TABLE_CELL_PATTERN, "type"));
        this.accountTableStatus = page.locator(String.format(ACCOUNT_TABLE_CELL_PATTERN, "status"));
        this.accountTableCreated = page.locator(String.format(ACCOUNT_TABLE_CELL_PATTERN, "created"));
        this.accountTableUpdated = page.locator(String.format(ACCOUNT_TABLE_CELL_PATTERN, "updated"));
        this.accountTableBalance = page.locator(String.format(ACCOUNT_TABLE_CELL_PATTERN, "balance"));
        this.accountTableTotalPnl = page.locator(String.format(ACCOUNT_TABLE_CELL_PATTERN, "pnl"));
        this.accountTableEquity = page.locator(String.format(ACCOUNT_TABLE_CELL_PATTERN, "equity"));
        this.accountTableCredit = page.locator(String.format(ACCOUNT_TABLE_CELL_PATTERN, "credit"));
        this.accountTableLeverage = page.locator(String.format(ACCOUNT_TABLE_CELL_PATTERN, "leverage"));
        this.accountTableMarginFree = page.locator(String.format(ACCOUNT_TABLE_CELL_PATTERN, "margin-free"));
        this.accountTableServer = page.locator(String.format(ACCOUNT_TABLE_CELL_PATTERN, "server"));
        this.accountTableGroup = page.locator(String.format(ACCOUNT_TABLE_CELL_PATTERN, "group"));
        this.accountTableHeaderAccount = page.locator(String.format(ACCOUNT_TABLE_HEADER_PATTERN, "account"));
        this.accountTableHeaderType = page.locator(String.format(ACCOUNT_TABLE_HEADER_PATTERN, "type"));
        this.accountTableHeaderStatus = page.locator(String.format(ACCOUNT_TABLE_HEADER_PATTERN, "status"));
        this.accountTableHeaderCreated = page.locator(String.format(ACCOUNT_TABLE_HEADER_PATTERN, "created"));
        this.accountTableHeaderUpdated = page.locator(String.format(ACCOUNT_TABLE_HEADER_PATTERN, "updated"));
        this.accountTableHeaderBalance = page.locator(String.format(ACCOUNT_TABLE_HEADER_PATTERN, "balance"));
        this.accountTableHeaderTotalPnl = page.locator(String.format(ACCOUNT_TABLE_HEADER_PATTERN, "pnl"));
        this.accountTableHeaderEquity = page.locator(String.format(ACCOUNT_TABLE_HEADER_PATTERN, "equity"));
        this.accountTableHeaderCredit = page.locator(String.format(ACCOUNT_TABLE_HEADER_PATTERN, "credit"));
        this.accountTableHeaderLeverage = page.locator(String.format(ACCOUNT_TABLE_HEADER_PATTERN, "leverage"));
        this.accountTableHeaderMarginFree = page.locator(String.format(ACCOUNT_TABLE_HEADER_PATTERN, "margin-free"));
        this.accountTableHeaderServer = page.locator(String.format(ACCOUNT_TABLE_HEADER_PATTERN, "server"));
        this.accountTableHeaderGroup = page.locator(String.format(ACCOUNT_TABLE_HEADER_PATTERN, "group"));
        this.operationOpenSortElement = page.locator("//*[name()='svg' and contains(@data-qa, 'trading_deals__arrow')]/..");
        this.openDatePicker = page.locator(String.format(DATE_PICKER_BY_LABEL_PATTERN, "Open date"));
        this.closeDatePicker = page.locator(String.format(DATE_PICKER_BY_LABEL_PATTERN, "Close date"));
        this.typeCheckboxes = page.locator(String.format(CHECKBOXES_BY_LABEL_PATTERN, "Type"));
        this.accountsCheckboxes = page.locator(String.format(CHECKBOXES_BY_LABEL_PATTERN, "Accounts"));
        this.symbolCheckboxes = page.locator(String.format(CHECKBOXES_BY_LABEL_PATTERN, "Symbol"));
        this.methodCheckboxes = page.locator(String.format(CHECKBOXES_BY_LABEL_PATTERN, "Method"));
        this.resetTypeButton = page.locator(String.format(RESET_BUTTON_BY_LABEL_PATTERN, "Type"));
        this.resetAccountsButton = page.locator(String.format(RESET_BUTTON_BY_LABEL_PATTERN, "Accounts"));
        this.resetSymbolButton = page.locator(String.format(RESET_BUTTON_BY_LABEL_PATTERN, "Symbol"));
        this.resetMethodButton = page.locator(String.format(RESET_BUTTON_BY_LABEL_PATTERN, "Method"));
        this.resetOpenDateButton = page.locator(String.format(RESET_BUTTON_BY_LABEL_PATTERN, "Open date"));
        this.resetCloseDateButton = page.locator(String.format(RESET_BUTTON_BY_LABEL_PATTERN, "Close date"));
        this.resetDurationButton = page.locator(String.format(RESET_BUTTON_BY_LABEL_PATTERN, "Duration"));
        this.resetProfitButton = page.locator(String.format(RESET_BUTTON_BY_LABEL_PATTERN, "Profit"));
        this.resetVolumeButton = page.locator(String.format(RESET_BUTTON_BY_LABEL_PATTERN, "Volume"));
        this.resetAllButton = page.locator("//span[text()='Reset all']/parent::button");
        this.openDateTooltip = page.locator(String.format(TOOLTIP_BY_LABEL_PATTERN, "Open date"));
        this.profitTooltip = page.locator(String.format(TOOLTIP_BY_LABEL_PATTERN, "Profit"));
        this.typeCheckboxLabels = page.locator(String.format(CHECKBOX_LABEL_BY_TITLE_PATTERN, "Type"));
        this.methodCheckboxLabels = page.locator(String.format(CHECKBOX_LABEL_BY_TITLE_PATTERN, "Method"));
        this.summaryTab = page.locator(".g-radio-button__option-control[value=\"Summary\"]");
        this.totalPnlMaxProfitValue = page.locator(String.format("%s/descendant::div[contains(@class,'g-color-text_color_brand')]", TOTAL_PNL_CHART_FEATURES));
        this.totalPnlMaxLossValue = page.locator(String.format("%s/descendant::div[contains(@class,'g-color-text_color_danger-heavy')]", TOTAL_PNL_CHART_FEATURES));
        this.totalPnlMaxProfitLabel = page.locator(String.format("(%s/descendant::div[contains(@class,'g-color-text g-color-text_color_secondary')])[1]", TOTAL_PNL_CHART_FEATURES));
        this.totalPnlMaxLossLabel = page.locator(String.format("(%s/descendant::div[contains(@class,'g-color-text g-color-text_color_secondary')])[2]", TOTAL_PNL_CHART_FEATURES));
        this.totalPnlMaxProfitGraphDot = page.locator(String.format("%s/descendant::div[contains(@class,'g-color-text_color_brand')]", TOTAL_PNL_CHART));
        this.totalPnlMaxLossGraphDot = page.locator(String.format("%s/descendant::div[contains(@class,'g-color-text_color_danger-heavy')]", TOTAL_PNL_CHART));
        this.totalPnlYAxisLabel = page.locator(String.format("%s/descendant::div[@class='v-trading-summary-total-pnl__padded-value']/div", TOTAL_PNL_CHART_CONTAINER));
        this.totalPnlTooltip = page.locator(".v-trading-summary-total-pnl__tooltip");
        this.totalPnlChartTitle = page.locator(TOTAL_PNL_CHART_CONTAINER).locator(CHART_TITLE);
        this.totalPnlXAxisLabels = page.locator("//div[@class='v-trading-summary-total-pnl__ticks-container']/descendant::div[contains(@class,'g-text')]");
        this.performanceOverviewTableTitle = page.locator(PERFORMANCE_OVERVIEW_CHART_CONTAINER).locator(CHART_TITLE);
        this.performanceOverviewTableHeaders = page.locator(PERFORMANCE_OVERVIEW_CHART_CONTAINER).locator("//th");
        this.performanceOverviewSymbols = page.locator(PERFORMANCE_OVERVIEW_CHART_CONTAINER).locator("//td[contains(@class,'v-trading-summary-performance__column_type_date')]");
        this.winrateWidget = page.locator(String.format(WIDGET_BY_TITLE_PATTERN, "Win rate"));
        this.winrateWidgetValue = winrateWidget.locator(".v-number-widget__value");
        this.winrateWidgetInfo = winrateWidget.locator(".v-number-widget__info");
        this.volumeChartTitle = page.locator(VOLUME_CHART_CONTAINER).locator(CHART_TITLE);
        this.volumeYAxisLabel = page.locator(String.format("%s/descendant::div[@class='v-trading-summary-volume__padded-value']/div", VOLUME_CHART_CONTAINER));
        this.volumeMaxValue = page.locator(String.format("%s/descendant::div[contains(@class,'g-color-text_color_brand')]", VOLUME_CHART_FEATURES));
        this.volumeTotalValue = page.locator(String.format("%s/descendant::div[contains(@class,'g-color-text_color_primary')]", VOLUME_CHART_FEATURES));
        this.volumeMaxLabel = page.locator(String.format("(%s/descendant::div[contains(@class,'g-color-text g-color-text_color_secondary')])[1]", VOLUME_CHART_FEATURES));
        this.volumeTotalLabel = page.locator(String.format("(%s/descendant::div[contains(@class,'g-color-text g-color-text_color_secondary')])[2]", VOLUME_CHART_FEATURES));
        this.volumeMaxGraphDot = page.locator(String.format("%s/descendant::div[contains(@class,'g-color-text_color_brand')]", VOLUME_CHART));
        this.pnlByDurationGraphSection = page.locator(PNL_BY_DURATION);
        this.pnlByDurationTooltip = page.locator(PNL_BY_DURATION_TOOLTIP);
        this.volumeXAxisLabels = page.locator("//div[@class='v-trading-summary-volume__ticks-container']/descendant::div[contains(@class,'g-text')]");
    }

    @Step("Navigate to users trading tab")
    public void navigate(String ucid) {
        Allure.step("Navigate to users trading tab");
        page.navigate("http://k8s-test-nginxrev-55e209d446-410128713.us-east-1.elb.amazonaws.com/investigation/" + ucid + "/trading");
        super.waitForPageToLoad();
    }

    @Step("Navigate to users restriction tab/operations")
    public void navigateOperations(String ucid) {
        Allure.step("Navigate to users trading tab/operations");
        navigate(ucid);
        operationsTab.click();
        waitForPageToLoad();
    }

    @Step("Open users trading tab")
    public void openTradingTab() {
        tradingTab.click();
        waitForPageToLoad();
    }

    @Step("Open users trading-operations tab")
    public void openOperationsTab() {
        operationsTab.click();
        super.waitForPageToLoad();
    }

    @Step("Open users trading-accounts tab")
    public void openAccountsTab() {
        Allure.step("Open users trading-accounts tab");
        accountsTab.click();
        super.waitForPageToLoad();
    }


    @Step("Check if the trading/operations tab renders all basic elements")
    public void operationsRendersTest() {
        Allure.step("Check if the trading/operations tab renders all basic elements");
        assertTrue(operationsTab.isVisible());
        assertTrue(accountColumnHeader.isVisible());
        assertTrue(accountColumnCell.first().isVisible());
        assertTrue(typeColumnHeader.isVisible());
        assertTrue(typeColumnCell.first().isVisible());
        assertTrue(volumeColumnHeader.isVisible());
        assertTrue(volumeColumnHeader.isVisible());
        assertTrue(profitColumnHeader.isVisible());
        assertTrue(profitColumnCell.first().isVisible());
        assertTrue(openColumnHeader.isVisible());
        assertTrue(openColumnCell.first().isVisible());
        assertTrue(closeColumnHeader.isVisible());
        assertTrue(closeColumnCell.first().isVisible());
        assertTrue(tpslColumnHeader.isVisible());
        assertTrue(tpslColumnCell.first().isVisible());
        assertTrue(tpslColumnCell.first().isVisible());
        assertTrue(swapColumnHeader.isVisible());
        assertTrue(swapColumnCell.first().isVisible());
        assertTrue(srColumnHeader.isVisible());
        assertTrue(srColumnCell.first().isVisible());
        assertTrue(commissionColumnHeader.isVisible());
        assertTrue(commissionColumnCell.first().isVisible());
        assertTrue(methodColumnHeader.isVisible());
        assertTrue(methodColumnCell.first().isVisible());
        assertTrue(commentColumnHeader.isVisible());
        assertTrue(commissionColumnCell.first().isVisible());
        assertTrue(filterButton.isVisible());
        assertTrue(accountColumnHeader.getByText("ACCOUNT").isVisible());
        assertTrue(typeColumnHeader.getByText("TYPE").isVisible());
        assertTrue(volumeColumnHeader.getByText("VOLUME").isVisible());
        assertTrue(profitColumnHeader.getByText("PROFIT").isVisible());
        assertTrue(openColumnHeader.getByText("OPEN").isVisible());
        assertTrue(closeColumnHeader.getByText("CLOSE").isVisible());
        assertTrue(tpslColumnHeader.getByText("TP/SL").isVisible());
        assertTrue(swapColumnHeader.getByText("SWAP").isVisible());
        assertTrue(srColumnHeader.getByText("SR").isVisible());
        assertTrue(commissionColumnHeader.getByText("COMMISSION").isVisible());
        assertTrue(methodColumnHeader.getByText("METHOD").isVisible());
        assertTrue(commentColumnHeader.getByText("COMMENT").isVisible());
    }

    @Step("Check list of Type filter options")
    public void checkTypeFilterList() {
        page.waitForTimeout(100);
        List<String> actualTypesList = new ArrayList<>();
        for (int i = 0; i < typeCheckboxLabels.count(); i++) {
            actualTypesList.add(typeCheckboxLabels.nth(i).textContent());
        }
        MatcherAssert.assertThat("Verify that Type filter contains all expected options", actualTypesList, contains("Buy", "Sell", "Balance", "Credit", "Buy Limit", "Sell Limit", "Buy Stop", "Sell Stop"));
    }

    @Step("Check list of Method filter options")
    public void checkMethodFilterList() {
        page.waitForTimeout(100);
        List<String> actualMethodsList = new ArrayList<>();
        for (int i = 0; i < methodCheckboxLabels.count(); i++) {
            actualMethodsList.add(methodCheckboxLabels.nth(i).textContent());
        }
        MatcherAssert.assertThat("Verify that Type filter contains all expected options", actualMethodsList, contains("API", "Client", "Dealer", "Expert", "Gateway", "Mobile", "Signal", "Web"));
    }

    @Step("Open filter")
    public void openFilter() {
        Allure.step("Open filter");
        filterButton.click();
    }

    @Step("Click filter")
    public void clickFilterCheckbox(String typeName) {
        Allure.step("Click filter type " + typeName);
        checkboxItem.getByText(typeName, new Locator.GetByTextOptions().setExact(true)).click();
    }

    @Step("Check text content of first and last method cells on page")
    public void checkMethodCellsContent(String methodName) {
        Allure.step("Check text content of first and last method cells on page " + methodName);
        assertTrue(methodColumnCell.first().textContent().matches("(.)*" + methodName + "*"));
        assertTrue(methodColumnCell.last().textContent().matches("(.)*" + methodName + "*"));
    }

    @Step("Check text content of first and last method cells on page")
    public void checkTypeCellsContent(String typeName) {
        Allure.step("Check text content of first and last type cells on page " + typeName);
        assertTrue(typeColumnCell.first().textContent().matches("(.)*" + typeName + "*"));
        assertTrue(typeColumnCell.last().textContent().matches("(.)*" + typeName + "*"));
    }

    @Step("Click apply button")
    public void clickApplyButton() {
        Allure.step("Click apply button");
        applyFiltersButton.click();
        super.waitForPageToLoad();
    }

    @Step("Fill volume values")
    public void fillVolumeValues(String from, String to) {
        Allure.step("Fill volume values");
        volumeFromInput.fill(from);
        volumeToInput.fill(to);
    }

    @Step("Fill profit values")
    public void fillProfitValues(String from, String to) {
        Allure.step("Fill profit values");
        profitFromInput.fill(from);
        profitToInput.fill(to);
    }

    @Step("Fill volume values")
    public void fillDurationValues(String from, String to) {
        Allure.step("Fill volume values");
        durationFromInput.fill(from);
        durationToInput.fill(to);
    }

    @Step("Check text content of first and last Volume cells on page is in interval")
    public void checkVolumeCellsContentUSD(int from, int to) {
        Allure.step("Check text content of first and last Volume cells on page is in interval");
        String firstCell = volumeColumnCellUSD.first().textContent();
        String lastCell = volumeColumnCellUSD.last().textContent();
        System.out.println(Integer.parseInt(firstCell));
        System.out.println(Integer.parseInt(lastCell));
        assertTrue(from <= Integer.parseInt(firstCell) && Integer.parseInt(firstCell) <= to);
        assertTrue(from <= Integer.parseInt(lastCell) && Integer.parseInt(lastCell) <= to);
    }

    @Step("Check text content of first and last profit cells on page is in interval")
    public void checkProfitCellsContent(int from, int to) {
        Allure.step("Check text content of first and last profit cells on page is in interval");
        String firstCell = profitColumnCell.first().textContent();
        String lastCell = profitColumnCell.last().textContent();
        System.out.println(Double.parseDouble(firstCell));
        System.out.println(Double.parseDouble(lastCell));

        double profit1 = Double.parseDouble(firstCell);
        double profit2 = Double.parseDouble(lastCell);

        if (profit1 < 0) {
            profit1 = profit1 * -1;
        }

        if (profit2 < 0) {
            profit2 = profit2 * -1;
        }

        assertTrue(from <= profit1 && profit1 <= to);
        assertTrue(from <= profit2 && profit2 <= to);
    }

    @Step("Check text content of first profit cell")
    public void checkProfitCellsContentFirst(double expected) {
        Allure.step("Check text content of first profit cell");
        String firstCell = profitColumnCell.first().textContent();

        System.out.println(Double.parseDouble(firstCell));

        double profit1 = Double.parseDouble(firstCell);

        assertEquals(expected, profit1);
    }

    @Step("Check text content of first and last Dates in cells on page is in interval")
    public void checkDatesMinutes(int from, int to) {
        Allure.step("Check text content of first and last Dates in cells on page is in interval");
        String openDate = openColumnCellDate.first().textContent();
        String closeDate = closeColumnCellDate.first().textContent();
        System.out.println(openDate);
        System.out.println(closeDate);
        long difference = Utils.getDifferenceTimeMinutes(openDate, closeDate);
        assertTrue(from <= difference && difference <= to);
    }

    @Step("Wait for page to load")
    public void waitForPageToLoad() {
        page.waitForSelector(TOTAL_PNL_CHART_CONTAINER, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
    }

    private int getAccountIndex(int accId) {
        for (int i = 0; i < accountCard.count(); i++) {
            Locator child = accountCard.nth(i).locator(accountId).last();
            if (Objects.equals(child.textContent(), String.valueOf(accId))) {
                return i;
            }
        }
        throw new NoSuchElementException(String.format("Account with accountId '%s' not found", accId));
    }

    @Step("Get account balance in card view")
    public String getAccountBalance(int accountId) {
        return accountCard.nth(getAccountIndex(accountId)).locator(balanceElement).locator("//span[contains(@class,'g-text_variant_header-2')]").textContent();
    }

    @Step("Get account balance usd in card view")
    public String getAccountBalanceUsd(int accountId) {
        return accountCard.nth(getAccountIndex(accountId)).locator(balanceElement).locator("//span[contains(@class,'g-text_variant_subheader-2')]").textContent();
    }

    @Step("Get account status in card view")
    public String getAccountStatus(int accountId) {
        return accountCard.nth(getAccountIndex(accountId)).locator(statusElement).textContent();
    }

    @Step("Get account platform in card view")
    public String getAccountPlatform(int accountId) {
        return accountCard.nth(getAccountIndex(accountId)).locator(platformElement).textContent();
    }

    @Step("Get account type in card view")
    public String getAccountType(int accountId) {
        return accountCard.nth(getAccountIndex(accountId)).locator(accountTypeElement).textContent();
    }

    @Step("Get account total pnl in card view")
    public String getAccountTotalPnl(int accountId) {
        return accountCard.nth(getAccountIndex(accountId)).locator(String.format(ACCOUNT_CARD_VALUE_BY_TITLE_PATTERN, "Total PNL")).textContent();
    }

    @Step("Get account equity in card view")
    public String getAccountEquity(int accountId) {
        return accountCard.nth(getAccountIndex(accountId)).locator(String.format(ACCOUNT_CARD_VALUE_BY_TITLE_PATTERN, "Equity")).textContent();
    }

    @Step("Get account credit in card view")
    public String getAccountCredit(int accountId) {
        return accountCard.nth(getAccountIndex(accountId)).locator(String.format(ACCOUNT_CARD_VALUE_BY_TITLE_PATTERN, "Credit")).textContent();
    }

    @Step("Get account leverage in card view")
    public String getAccountLeverage(int accountId) {
        return accountCard.nth(getAccountIndex(accountId)).locator(String.format(ACCOUNT_CARD_VALUE_BY_TITLE_PATTERN, "Leverage")).textContent();
    }

    @Step("Get account margin free in card view")
    public String getAccountMarginFree(int accountId) {
        return accountCard.nth(getAccountIndex(accountId)).locator(String.format(ACCOUNT_CARD_VALUE_BY_TITLE_PATTERN, "Margin free")).textContent();
    }

    @Step("Get account server in card view")
    public String getAccountServer(int accountId) {
        return accountCard.nth(getAccountIndex(accountId)).locator(String.format(ACCOUNT_CARD_VALUE_BY_TITLE_PATTERN, "Server")).textContent();
    }

    @Step("Get account group in card view")
    public String getAccountGroup(int accountId) {
        return accountCard.nth(getAccountIndex(accountId)).locator(String.format(ACCOUNT_CARD_VALUE_BY_TITLE_PATTERN, "Group")).textContent();
    }

    @Step("Get account created time in card view")
    public String getAccountCreatedTime(int accountId) {
        return accountCard.nth(getAccountIndex(accountId)).locator(createdTimeElement).textContent();
    }

    @Step("Get account updated time in card view")
    public String getAccountUpdatedTime(int accountId) {
        return accountCard.nth(getAccountIndex(accountId)).locator(updatedTimeElement).textContent();
    }

    @Step("Verify account id popup in card view is as expected")
    public void verifyAccountIdPopup() {
        accountCard.first().locator(accountId).last().hover();
        assertThat(popupElement).containsText("Login");
    }

    @Step("Verify account balance popup in card view is as expected")
    public void verifyBalancePopup(int accountId) {
        accountCard.nth(getAccountIndex(accountId)).locator(balanceElement).locator("//span[contains(@class,'g-text_variant_header-2')]").hover();
        assertThat(popupElement).containsText("Balance");
    }

    @Step("Verify account balance usd popup in card view is as expected")
    public void verifyBalanceUsdPopup(int accountId) {
        accountCard.nth(getAccountIndex(accountId)).locator(balanceElement).locator("//span[contains(@class,'g-text_variant_subheader-2')]").hover();
        assertThat(popupElement).containsText("Balance in USD");
    }

    @Step("Verify account status popup in card view is as expected")
    public void verifyStatusPopup() {
        accountCard.first().locator(statusElement).hover();
        assertThat(popupElement).containsText("Status");
    }

    @Step("Verify account platform popup in card view is as expected")
    public void verifyPlatformPopup() {
        accountCard.first().locator(platformElement).hover();
        assertThat(popupElement).containsText("Platform");
    }

    @Step("Verify account type popup in card view is as expected")
    public void verifyAccountTypePopup() {
        accountCard.first().locator(accountTypeElement).hover();
        assertThat(popupElement).containsText("Account type");
    }

    @Step("Verify account created time popup in card view is as expected")
    public void verifyCreatedTimePopup() {
        accountCard.first().locator(createdTimeElement).hover();
        assertThat(popupElement).containsText("Created time");
    }

    @Step("Verify account updated time popup in card view is as expected")
    public void verifyUpdatedTimePopup() {
        accountCard.first().locator(updatedTimeElement).hover();
        assertThat(popupElement).containsText("Updated time");
    }

    @Step("Click table view button")
    public void clickTableViewButton() {
        tableViewButton.click();
    }

    private int getAccountIndexTableView(int accId) {
        for (int i = 0; i < accountRow.count(); i++) {
            Locator child = accountRow.nth(i).locator(accountTableId);
            if (Objects.equals(child.textContent(), String.valueOf(accId))) {
                return i;
            }
        }
        throw new NoSuchElementException(String.format("Account with accountId '%s' not found", accId));
    }

    @Step("Get account platform in table view")
    public String getAccountTablePlatform(int accountId) {
        return accountRow.nth(getAccountIndexTableView(accountId)).locator(accountTablePlatform).textContent();
    }

    @Step("Get account type in table view")
    public String getAccountTableType(int accountId) {
        return accountRow.nth(getAccountIndexTableView(accountId)).locator(accountTableType).textContent();
    }

    @Step("Get account status in table view")
    public String getAccountTableStatus(int accountId) {
        return accountRow.nth(getAccountIndexTableView(accountId)).locator(accountTableStatus).textContent();
    }

    @Step("Get account created time in table view")
    public String getAccountTableCreated(int accountId) {
        return accountRow.nth(getAccountIndexTableView(accountId)).locator(accountTableCreated).textContent();
    }

    @Step("Get account updated time in table view")
    public String getAccountTableUpdated(int accountId) {
        return accountRow.nth(getAccountIndexTableView(accountId)).locator(accountTableUpdated).textContent();
    }

    @Step("Get account balance in table view")
    public String getAccountTableBalance(int accountId) {
        return accountRow.nth(getAccountIndexTableView(accountId)).locator(accountTableBalance).textContent();
    }

    @Step("Get account total pnl in table view")
    public String getAccountTableTotalPnl(int accountId) {
        return accountRow.nth(getAccountIndexTableView(accountId)).locator(accountTableTotalPnl).textContent();
    }

    @Step("Get account equity in table view")
    public String getAccountTableEquity(int accountId) {
        return accountRow.nth(getAccountIndexTableView(accountId)).locator(accountTableEquity).textContent();
    }

    @Step("Get account credit in table view")
    public String getAccountTableCredit(int accountId) {
        return accountRow.nth(getAccountIndexTableView(accountId)).locator(accountTableCredit).textContent();
    }

    @Step("Get account leverage in table view")
    public String getAccountTableLeverage(int accountId) {
        return accountRow.nth(getAccountIndexTableView(accountId)).locator(accountTableLeverage).textContent();
    }

    @Step("Get account margin free in table view")
    public String getAccountTableMarginFree(int accountId) {
        return accountRow.nth(getAccountIndexTableView(accountId)).locator(accountTableMarginFree).textContent();
    }

    @Step("Get account server in table view")
    public String getAccountTableServer(int accountId) {
        return accountRow.nth(getAccountIndexTableView(accountId)).locator(accountTableServer).textContent();
    }

    @Step("Get account group in table view")
    public String getAccountTableGroup(int accountId) {
        return accountRow.nth(getAccountIndexTableView(accountId)).locator(accountTableGroup).textContent();
    }

    @Step("Verify account table headers visibility and text")
    public void verifyAccountTableHeaders() {
        assertThat(accountTableHeaderAccount).isVisible();
        assertThat(accountTableHeaderAccount).containsText("ACCOUNT");
        assertThat(accountTableHeaderType).isVisible();
        assertThat(accountTableHeaderType).containsText("TYPE");
        assertThat(accountTableHeaderStatus).isVisible();
        assertThat(accountTableHeaderStatus).containsText("STATUS");
        assertThat(accountTableHeaderCreated).isVisible();
        assertThat(accountTableHeaderCreated).containsText("CREATED");
        assertThat(accountTableHeaderUpdated).isVisible();
        assertThat(accountTableHeaderUpdated).containsText("UPDATED");
        assertThat(accountTableHeaderBalance).isVisible();
        assertThat(accountTableHeaderBalance).containsText("BALANCE");
        assertThat(accountTableHeaderTotalPnl).isVisible();
        assertThat(accountTableHeaderTotalPnl).containsText("TOTAL PNL");
        assertThat(accountTableHeaderEquity).isVisible();
        assertThat(accountTableHeaderEquity).containsText("EQUITY");
        assertThat(accountTableHeaderCredit).isVisible();
        assertThat(accountTableHeaderCredit).containsText("CREDIT");
        assertThat(accountTableHeaderLeverage).isVisible();
        assertThat(accountTableHeaderLeverage).containsText("LEVERAGE");
        assertThat(accountTableHeaderMarginFree).isVisible();
        assertThat(accountTableHeaderMarginFree).containsText("MARGIN FREE");
        assertThat(accountTableHeaderServer).isVisible();
        assertThat(accountTableHeaderServer).containsText("SERVER");
        assertThat(accountTableHeaderGroup).isVisible();
        assertThat(accountTableHeaderGroup).containsText("GROUP");
    }

    @Step("Get account cell value for operation by index")
    public String getOperationAccountByIndex(int index) {
        return accountColumnCell.nth(index).textContent();
    }

    @Step("Get type cell value for operation by index")
    public String getOperationTypeByIndex(int index) {
        return typeColumnCell.nth(index).textContent();
    }

    @Step("Get volume cell value for operation by index")
    public String getOperationVolumeByIndex(int index) {
        return volumeColumnCell.nth(index).textContent();
    }

    @Step("Get profit cell value for operation by index")
    public String getOperationProfitByIndex(int index) {
        return profitColumnCell.nth(index).textContent();
    }

    @Step("Get open cell value for operation by index")
    public String getOperationOpenByIndex(int index) {
        return openColumnCell.nth(index).textContent();
    }

    @Step("Get close cell value for operation by index")
    public String getOperationCloseByIndex(int index) {
        return closeColumnCell.nth(index).textContent();
    }

    @Step("Get tp/sl cell value for operation by index")
    public String getOperationTpSlByIndex(int index) {
        return tpslColumnCell.nth(index).textContent();
    }

    @Step("Get swap cell value for operation by index")
    public String getOperationSwapByIndex(int index) {
        return swapColumnCell.nth(index).textContent();
    }

    @Step("Get sr cell value for operation by index")
    public String getOperationSrByIndex(int index) {
        return srColumnCell.nth(index).textContent();
    }

    @Step("Get commission cell value for operation by index")
    public String getOperationCommissionByIndex(int index) {
        return commissionColumnCell.nth(index).textContent();
    }

    @Step("Get method cell value for operation by index")
    public String getOperationMethodByIndex(int index) {
        return methodColumnCell.nth(index).textContent();
    }

    @Step("Get comment cell value for operation by index")
    public String getOperationCommentByIndex(int index) {
        return commentColumnCell.nth(index).textContent();
    }

    @Step("Get count of operations")
    public int getOperationsCount() {
        return accountColumnCell.count();
    }

    @Step("Change sorting by open")
    public void sortByOpen() {
        operationOpenSortElement.click();
    }

    @Step("Get text of popup when hovering over sorting by open element")
    public String getSortByOpenPopupText() {
        operationOpenSortElement.hover();
        return popupElement.textContent();
    }

    @Step("Select open date from date picker")
    public void selectOpenDate(String openDate) {
        selectDateInElement(openDatePicker, openDate);
    }

    @Step("Select close date from date picker")
    public void selectCloseDate(String closeDate) {
        selectDateInElement(closeDatePicker, closeDate);
    }

    @Step("Verify preset options for filters")
    public void verifyPresetOptionsForFilters() {
        String openDate = "Open date";
        String closeDate = "Close date";
        String profit = "Profit";
        String volume = "Volume";
        // Open date
        page.locator(String.format(PRESET_BY_LABEL_AND_VALUE_PATTERN, openDate, "Today")).click();
        assertThat(openDatePicker).hasValue(Utils.getCurrentDate());
        page.locator(String.format(PRESET_BY_LABEL_AND_VALUE_PATTERN, openDate, "Yesterday")).click();
        assertThat(openDatePicker).hasValue(Utils.getYesterdayDate());
        page.locator(String.format(PRESET_BY_LABEL_AND_VALUE_PATTERN, openDate, "Last 7 days")).click();
        assertThat(openDatePicker).hasValue(String.format("%s to %s", Utils.getPreviousWeekDate(), Utils.getCurrentDate()));
        page.locator(String.format(PRESET_BY_LABEL_AND_VALUE_PATTERN, openDate, "Last 30 days")).click();
        assertThat(openDatePicker).hasValue(String.format("%s to %s", getPrevious30DaysDate(), Utils.getCurrentDate()));
        // Close date
        page.locator(String.format(PRESET_BY_LABEL_AND_VALUE_PATTERN, closeDate, "Today")).click();
        assertThat(closeDatePicker).hasValue(Utils.getCurrentDate());
        page.locator(String.format(PRESET_BY_LABEL_AND_VALUE_PATTERN, closeDate, "Yesterday")).click();
        assertThat(closeDatePicker).hasValue(Utils.getYesterdayDate());
        page.locator(String.format(PRESET_BY_LABEL_AND_VALUE_PATTERN, closeDate, "Last 7 days")).click();
        assertThat(closeDatePicker).hasValue(String.format("%s to %s", Utils.getPreviousWeekDate(), Utils.getCurrentDate()));
        page.locator(String.format(PRESET_BY_LABEL_AND_VALUE_PATTERN, closeDate, "Last 30 days")).click();
        assertThat(closeDatePicker).hasValue(String.format("%s to %s", getPrevious30DaysDate(), Utils.getCurrentDate()));
        // Profit
        page.locator(String.format(PRESET_BY_LABEL_AND_VALUE_PATTERN, profit, "0-50")).click();
        assertThat(profitFromInput).hasValue("0 USD");
        assertThat(profitToInput).hasValue("50 USD");
        page.locator(String.format(PRESET_BY_LABEL_AND_VALUE_PATTERN, profit, "50-200")).click();
        assertThat(profitFromInput).hasValue("50 USD");
        assertThat(profitToInput).hasValue("200 USD");
        page.locator(String.format(PRESET_BY_LABEL_AND_VALUE_PATTERN, profit, "200-500")).click();
        assertThat(profitFromInput).hasValue("200 USD");
        assertThat(profitToInput).hasValue("500 USD");
        page.locator(String.format(PRESET_BY_LABEL_AND_VALUE_PATTERN, profit, ">500")).click();
        assertThat(profitFromInput).hasValue("500 USD");
        assertThat(profitToInput).hasValue("");
        // Volume
        page.locator(String.format(PRESET_BY_LABEL_AND_VALUE_PATTERN, volume, "0-50")).click();
        assertThat(volumeFromInput).hasValue("0 USD");
        assertThat(volumeToInput).hasValue("50 USD");
        page.locator(String.format(PRESET_BY_LABEL_AND_VALUE_PATTERN, volume, "50-200")).click();
        assertThat(volumeFromInput).hasValue("50 USD");
        assertThat(volumeToInput).hasValue("200 USD");
        page.locator(String.format(PRESET_BY_LABEL_AND_VALUE_PATTERN, volume, "200-500")).click();
        assertThat(volumeFromInput).hasValue("200 USD");
        assertThat(volumeToInput).hasValue("500 USD");
        page.locator(String.format(PRESET_BY_LABEL_AND_VALUE_PATTERN, volume, ">500")).click();
        assertThat(volumeFromInput).hasValue("500 USD");
        assertThat(volumeToInput).hasValue("");
    }

    public void verifyNoTypeIsSelected() {
        for (int i = 0; i < typeCheckboxes.count(); i++) {
            Locator checkbox = typeCheckboxes.nth(i);
            MatcherAssert.assertThat("Assert that each type checkbox is not selected", checkbox.isChecked(), not(equalTo(true)));
        }
    }

    @Step("Press reset button for type and verify that none are selected")
    public void resetTypeFilterAndVerify() {
        resetTypeButton.click();
        verifyNoTypeIsSelected();
    }

    public void verifyNoAccountIsSelected() {
        for (int i = 0; i < accountsCheckboxes.count(); i++) {
            Locator checkbox = accountsCheckboxes.nth(i);
            MatcherAssert.assertThat("Assert that each account checkbox is not selected", checkbox.isChecked(), not(equalTo(true)));
        }
    }

    @Step("Press reset button for accounts and verify that none are selected")
    public void resetAccountsFilterAndVerify() {
        resetAccountsButton.click();
        verifyNoAccountIsSelected();
    }

    public void verifyNoSymbolIsSelected() {
        for (int i = 0; i < symbolCheckboxes.count(); i++) {
            Locator checkbox = symbolCheckboxes.nth(i);
            MatcherAssert.assertThat("Assert that each symbol checkbox is not selected", checkbox.isChecked(), not(equalTo(true)));
        }
    }

    @Step("Press reset button for symbol and verify that none are selected")
    public void resetSymbolFilterAndVerify() {
        resetSymbolButton.click();
        verifyNoSymbolIsSelected();
    }

    public void verifyNoMethodIsSelected() {
        for (int i = 0; i < methodCheckboxes.count(); i++) {
            Locator checkbox = methodCheckboxes.nth(i);
            MatcherAssert.assertThat("Assert that each method checkbox is not selected", checkbox.isChecked(), not(equalTo(true)));
        }
    }

    @Step("Press reset button for method and verify that none are selected")
    public void resetMethodFilterAndVerify() {
        resetMethodButton.click();
        verifyNoMethodIsSelected();
    }

    public void verifyNoOpenDateIsFilled() {
        assertThat(openDatePicker).hasValue("");
    }

    @Step("Press reset button for open date and verify that none are selected")
    public void resetOpenDateFilterAndVerify() {
        resetOpenDateButton.click();
        verifyNoOpenDateIsFilled();
    }

    public void verifyNoCloseDateIsFilled() {
        assertThat(closeDatePicker).hasValue("");
    }

    @Step("Press reset button for close date and verify that none are selected")
    public void resetCloseDateFilterAndVerify() {
        resetCloseDateButton.click();
        verifyNoCloseDateIsFilled();
    }

    public void verifyNoDurationIsFilled() {
        assertThat(durationFromInput).hasValue("");
        assertThat(durationToInput).hasValue("");
    }

    @Step("Press reset button for duration and verify that none are selected")
    public void resetDurationFilterAndVerify() {
        resetDurationButton.click();
        verifyNoDurationIsFilled();
    }

    public void verifyNoProfitIsFilled() {
        assertThat(profitFromInput).hasValue("");
        assertThat(profitToInput).hasValue("");
    }

    @Step("Press reset button for profit and verify that none are selected")
    public void resetProfitFilterAndVerify() {
        resetProfitButton.click();
        verifyNoProfitIsFilled();
    }

    public void verifyNoVolumeIsFilled() {
        assertThat(volumeFromInput).hasValue("");
        assertThat(volumeToInput).hasValue("");
    }

    @Step("Press reset button for volume and verify that none are selected")
    public void resetVolumeFilterAndVerify() {
        resetVolumeButton.click();
        verifyNoVolumeIsFilled();
    }

    @Step("Press reset button for all filters and verify that none are selected")
    public void resetAllFiltersAndVerify() {
        resetAllButton.click();
        verifyNoTypeIsSelected();
        verifyNoAccountIsSelected();
        verifyNoOpenDateIsFilled();
        verifyNoCloseDateIsFilled();
        verifyNoDurationIsFilled();
        verifyNoSymbolIsSelected();
        verifyNoProfitIsFilled();
        verifyNoVolumeIsFilled();
        verifyNoMethodIsSelected();
    }

    @Step("Verify open date tooltip is as expected")
    public void verifyOpenDateTooltip() {
        openDateTooltip.hover();
        assertThat(popupElement).containsText("Open date for trading operations/ Date for payments operations");
    }

    @Step("Verify profit tooltip is as expected")
    public void verifyProfitTooltip() {
        profitTooltip.hover();
        assertThat(popupElement).containsText("Profit date for trading operations/ Amount for payments operations");
    }

    @Step("Open users trading-summary tab")
    public void openSummaryTab() {
        summaryTab.click();
        super.waitForPageToLoad();
    }

    @Step("Get Total PNL max profit value")
    public String getTotalPnlMaxProfitValue() {
        return totalPnlMaxProfitValue.textContent();
    }

    @Step("Get Total PNL max profit label")
    public String getTotalPnlMaxProfitLabel() {
        return totalPnlMaxProfitLabel.textContent();
    }

    @Step("Get Total PNL max loss value")
    public String getTotalPnlMaxLossValue() {
        return totalPnlMaxLossValue.textContent();
    }

    @Step("Get Total PNL max loss label")
    public String getTotalPnlMaxLossLabel() {
        return totalPnlMaxLossLabel.textContent();
    }

    @Step("Get Total PNL max profit graph dot label")
    public String getTotalPnlMaxProfitGraphDot() {
        return totalPnlMaxProfitGraphDot.textContent();
    }

    @Step("Get Total PNL max loss graph dot label")
    public String getTotalPnlMaxLossGraphDot() {
        return totalPnlMaxLossGraphDot.textContent();
    }

    @Step("Get Total PNL Y axis label")
    public String getTotalPnlYAxisLabel() {
        return totalPnlYAxisLabel.textContent();
    }

    @Step("Get Total PNL chart title")
    public String getTotalPnlChartTitle() {
        return totalPnlChartTitle.textContent();
    }

    @Step("Get Total PNL chart x axis labels")
    public List<String> getTotalPnlXAxisLabels() {
        List<String> xAxisLabels = new ArrayList<>();
        for (int i = 0; i < totalPnlXAxisLabels.count(); i++) {
            Locator label = totalPnlXAxisLabels.nth(i);
            xAxisLabels.add(label.textContent());
        }
        return xAxisLabels;
    }

    public void hoverOverTotalPnlXAxisLabelWithOffset(String labelText) {
        BoundingBox box = page.locator(String.format(TOTAL_PNL_X_AXIS_LABEL_BY_TEXT_PATTERN, labelText)).last().boundingBox();
        if (box != null) {
            double centerX = box.x + box.width / 2;
            double centerY = box.y + box.height / 2;

            // Hover over the center of the element
            page.mouse().move(centerX, centerY);

            // Move the mouse 50 pixels up (negative y direction)
            page.mouse().move(centerX, centerY - 50);
        }
    }

    @Step("Get Total PNL tooltip")
    public String getTotalPnlTooltip() {
        return totalPnlTooltip.textContent();
    }

    @Step("Get Performance overview title")
    public String getPerformanceOverviewTableTitle() {
        return performanceOverviewTableTitle.textContent();
    }

    @Step("Get Performance overview table headers")
    public List<String> getPerformanceOverviewTableHeaders() {
        List<String> headers = new ArrayList<>();
        for (int i = 0; i < performanceOverviewTableHeaders.count(); i++) {
            headers.add(performanceOverviewTableHeaders.nth(i).textContent());
        }
        return headers;
    }

    @Step("Get Volume chart title")
    public String getVolumeChartTitle() {
        return volumeChartTitle.textContent();
    }

    @Step("Get Volume Y axis label")
    public String getVolumeYAxisLabel() {
        return volumeYAxisLabel.textContent();
    }

    @Step("Get Volume max value")
    public String getVolumeMaxValue() {
        return volumeMaxValue.textContent();
    }

    @Step("Get Volume max label")
    public String getVolumeMaxLabel() {
        return volumeMaxLabel.textContent();
    }

    @Step("Get Volume total value")
    public String getVolumeTotalValue() {
        return volumeTotalValue.textContent();
    }

    @Step("Get Volume total label")
    public String getVolumeTotalLabel() {
        return volumeTotalLabel.textContent();
    }

    @Step("Get Volume max graph dot label")
    public String getVolumeMaxGraphDot() {
        return volumeMaxGraphDot.textContent();
    }

    @Step("Get Volume chart x axis labels")
    public List<String> getVolumeXAxisLabels() {
        List<String> xAxisLabels = new ArrayList<>();
        for (int i = 0; i < volumeXAxisLabels.count(); i++) {
            Locator label = volumeXAxisLabels.nth(i);
            xAxisLabels.add(label.textContent());
        }
        return xAxisLabels;
    }

    @Step("Get Performance overview symbols")
    public List<String> getPerformanceOverviewSymbols() {
        List<String> symbols = new ArrayList<>();
        for (int i = 0; i < performanceOverviewSymbols.count(); i++) {
            symbols.add(performanceOverviewSymbols.nth(i).textContent());
        }
        return symbols;
    }

    @Step("Get Performance overview deals by symbol {symbol}")
    public String getPerformanceOverviewDealsBySymbol(String symbol) {
        return page.locator(String.format(PERFORMANCE_OVERVIEW_DEALS_BY_SYMBOL_PATTERN, symbol)).textContent();
    }

    @Step("Get Performance overview winrate by symbol {symbol}")
    public String getPerformanceOverviewWinrateBySymbol(String symbol) {
        return page.locator(String.format(PERFORMANCE_OVERVIEW_WINRATE_BY_SYMBOL_PATTERN, symbol)).textContent();
    }

    @Step("Get Performance overview HFT by symbol {symbol}")
    public String getPerformanceOverviewHftBySymbol(String symbol) {
        return page.locator(String.format(PERFORMANCE_OVERVIEW_HFT_BY_SYMBOL_PATTERN, symbol)).textContent();
    }

    @Step("Get Performance overview PNL by symbol {symbol}")
    public String getPerformanceOverviewPnlBySymbol(String symbol) {
        return page.locator(String.format(PERFORMANCE_OVERVIEW_PNL_BY_SYMBOL_PATTERN, symbol)).textContent();
    }

    @Step("Get Winrate widget value")
    public String getWinrateWidgetValue() {
        return winrateWidgetValue.textContent();
    }

    @Step("Get Winrate widget info")
    public String getWinrateWidgetInfo() {
        return winrateWidgetInfo.textContent();
    }

    public void deleteAccountDeals(int accountNumber) throws SQLException {
        deleteEntryFromDb(MT4_TRADES_COERCED_TABLE_NAME, "account =" + accountNumber);
    }

    public void deleteClientDeals(String ucid) throws SQLException {
        deleteEntryFromDb(MT4_TRADES_COERCED_TABLE_NAME, "ucid ='" + ucid + "'");
    }

    public void openPnlDurationTooltip(String annotationText) {
        page.waitForTimeout(100);
        String locator = PNL_BY_DURATION + PNL_BY_DURATION_ANNOTATION + "[text()='" + annotationText + "']";
        page.hover(locator, new Page.HoverOptions().setForce(true));
        Locator target = page.locator(locator);
        int i = 1;
        while ((!(pnlByDurationTooltip.isVisible())) && (i < 100)) {
            page.waitForTimeout(10);
            page.mouse().move(target.boundingBox().x, target.boundingBox().y - (i));
            page.waitForTimeout(10);
            i++;
        }
        page.waitForTimeout(100);
        System.out.println(pnlByDurationTooltip.textContent());
        assertThat(pnlByDurationTooltip).isVisible();
    }

    public void checkTextPnlDurationTooltipAmount(String sumAmout) {
        assertThat(pnlByDurationTooltip).isVisible();
        String locator = (PNL_BY_DURATION_TOOLTIP + "//*[contains(text(),'" + sumAmout + "')]");
        assertThat(page.locator(locator)).hasText(sumAmout + " USD");
    }

    public void checkTextPnlDurationTooltipAmount(double innerText) {
        NumberFormat formatter = NumberFormat.getInstance(Locale.US);
        int roundedInnerText = (int) Math.round(innerText);
        checkTextPnlDurationTooltipAmount(formatter.format(roundedInnerText));
    }

    public void checkTextPnlDurationTooltipAmount(int innerText) {
        NumberFormat formatter = NumberFormat.getInstance(Locale.US);
        checkTextPnlDurationTooltipAmount(formatter.format(innerText));
    }

    public void checkTextPnlDurationTooltipPercentage(String percentage) {
        assertThat(pnlByDurationTooltip).isVisible();
        String locator = (PNL_BY_DURATION_TOOLTIP + "//*[contains(text(),'" + percentage + "')]");
        assertThat(page.locator(locator)).hasText(percentage + "% of all deals");
    }

    public void checkTextPnlDurationTooltipPercentage(double innerText) {
        checkTextPnlDurationTooltipPercentage(String.valueOf((int) innerText));
    }

    public void checkTextPnlDurationTooltipPercentage(int innerText) {
        checkTextPnlDurationTooltipPercentage(String.valueOf(innerText));
    }

    public void checkMaxProfitableValue(int expectedValue) {
        Locator element = page.locator(PNL_DURATION_GRAPH + GREEN_TEXT);
        NumberFormat formatter = NumberFormat.getInstance(Locale.US);
        assertEquals(formatter.format(expectedValue), element.textContent());
    }

    public void checkMaxProfitableValue(double expectedValue) {
        checkMaxProfitableValue((int) Math.round(expectedValue));
    }

    public void checkMaxLossValue(int expectedValue) {
        Locator element = page.locator(PNL_DURATION_GRAPH + RED_TEXT);
        NumberFormat formatter = NumberFormat.getInstance(Locale.US);
        assertEquals(formatter.format(expectedValue), element.textContent());
    }

    public void checkMaxLossValue(double expectedValue) {
        checkMaxLossValue((int) Math.round(expectedValue));
    }

    public void checkTopProfitCategory(String expectedValue) {
        assertEquals(expectedValue, page.locator(PNL_BY_DURATION + "//div[text() = 'Max profitable']/preceding-sibling::div").textContent());
    }

    public void checkTopLossCategory(String expectedValue) {
        assertEquals(expectedValue, page.locator(PNL_BY_DURATION + "//div[text() = 'Max loosing']/preceding-sibling::div").textContent());
    }
}

