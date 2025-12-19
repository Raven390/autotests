package page_objects.backoffice_pages.investigationTool;

import business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObject;
import business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedObject;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Route;
import com.microsoft.playwright.options.ElementState;
import com.microsoft.playwright.options.WaitForSelectorState;
import helpers.data.ClientHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.hamcrest.MatcherAssert;
import page_objects.backoffice_pages.AbstractPage;
import utils.Utils;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.*;

import static business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoercedRandomized;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.insertObjectsToDb;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static utils.ConfigFactory.BASE_URL_E2E;
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
    private final Locator typeValue;
    private final Locator profitColumnCell;
    private final Locator volumeLotsValue;
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
    private final Locator volumeAmountFromInput;
    private final Locator volumeAmountToInput;
    private final Locator volumeUsdValue;
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
    private final Locator filterPopupElement;
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
    private final Locator totalPnlMaxProfitDate;
    private final Locator totalPnlMaxLossDate;
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
    private final Locator volumeMaxDate;
    private final Locator volumeTotalLabel;
    private final Locator volumeMaxGraphDot;
    private final Locator pnlByDurationGraphSection;
    private final Locator pnlByDurationTooltip;
    private final Locator totalPnlXAxisLabels;
    private final Locator volumeXAxisLabels;
    private final Locator holdingTimeTooltip;
    private final Locator toxicityAndProfitChartTitle;
    private final Locator toxicityAndProfitYAxisLabel;
    private final Locator toxicityAndProfitMaxToxicityValue;
    private final Locator toxicityAndProfitMaxProfitValue;
    private final Locator toxicityAndProfitMaxToxicityLabel;
    private final Locator toxicityAndProfitMaxProfitLabel;
    private final Locator toxicityAndProfitMaxToxicityGraphDot;
    private final Locator toxicityAndProfitXAxisLabels;
    private final Locator toxicityAndProfitTooltipIcon;
    private final Locator tooltip;
    private final Locator absoluteToxicityWidget;
    private final Locator absoluteToxicityWidgetValue;
    private final Locator absoluteToxicityWidgetInfo;
    private final Locator absoluteToxicityWidgetTitle;
    private final Locator ibRebatesWidget;
    private final Locator ibRebatesWidgetValue;
    private final Locator ibRebatesWidgetInfo;
    private final Locator ibRebatesWidgetTitle;
    private final Locator ibRebatesWidgetText;
    private final Locator operationsTableTooltip;
    private final Locator highlightedRow;
    private final Locator notHighlightedRow;
    private final Locator enabledHftButton;
    private final Locator disabledHftButton;
    private final Locator typeColumnCell;
    private final Locator volumeColumnCell;
    private final Locator lotsAmountSwitch;
    private final Locator volumeLotFromInput;
    private final Locator volumeLotToInput;
    private final Locator errorMessage;
    private final Locator illegalProfitButton;
    private final Locator checkboxIllegalProfit;
    private final Locator illegalProfitAmountLoaded;
    private final Locator saveIllegalProfitButton;
    private final Locator toastMessage;
    private final Locator selectedTradesCounter;
    private final Locator selectedIllegalProfitAmout;
    private final Locator selectedIllegalProfitAccountCount;
    private final Locator illegalProfitSelectAllCheckBox;

    private static final String ACCOUNT_CARD_VALUE_BY_TITLE_PATTERN = "//div[contains(@class,'v-trading-tab-accounts-card__column-title') and text()='%s']/following-sibling::div";
    private static final String POPUP_ELEMENT_XPATH = "//div[contains(@class,'g-popup__content')]";
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
    private static final String CHART_CONTAINER_PATTERN = "//div[text()='%s']/following-sibling::span[text()='%s']/ancestor::div[contains(@class,'v-trading-summary__chart') and not(contains(@class,'v-trading-summary__charts'))]";
    private static final String TOTAL_PNL_CHART_CONTAINER = String.format(CHART_CONTAINER_PATTERN, "Realized PNL", "USD");
    private static final String TOTAL_PNL_CHART_FEATURES = String.format("%s/descendant::div[@class='v-chart-wrapper__feature']", TOTAL_PNL_CHART_CONTAINER);
    private static final String TOTAL_PNL_CHART = String.format("%s/descendant::div[@class='v-chart-wrapper__content']", TOTAL_PNL_CHART_CONTAINER);
    private static final String TOTAL_PNL_X_AXIS_LABEL_BY_TEXT_PATTERN = TOTAL_PNL_CHART + "/descendant::div[@class='v-line-chart__ticks-container']/descendant::div[contains(@class,'g-text') and text()='%s']";
    private static final String PERFORMANCE_OVERVIEW_CHART_CONTAINER = String.format(WIDGET_CONTAINER_PATTERN, "Performance overview");
    private static final String CHART_TITLE = "//div[@class='v-chart-wrapper__title']";
    private static final String PERFORMANCE_OVERVIEW_ROW_BY_SYMBOL_PATTERN = PERFORMANCE_OVERVIEW_CHART_CONTAINER + "/descendant::td[text()='%s']/parent::tr";
    private static final String PERFORMANCE_OVERVIEW_TICKETS_BY_SYMBOL_PATTERN = PERFORMANCE_OVERVIEW_ROW_BY_SYMBOL_PATTERN + "/td[contains(@class,'v-trading-summary-performance__column_type_tickets')]";
    private static final String PERFORMANCE_OVERVIEW_WINRATE_BY_SYMBOL_PATTERN = PERFORMANCE_OVERVIEW_ROW_BY_SYMBOL_PATTERN + "/td[contains(@class,'v-trading-summary-performance__column_type_winrate')]";
    private static final String PERFORMANCE_OVERVIEW_HFT_BY_SYMBOL_PATTERN = PERFORMANCE_OVERVIEW_ROW_BY_SYMBOL_PATTERN + "/td[contains(@class,'v-trading-summary-performance__column_type_os')]";
    private static final String PERFORMANCE_OVERVIEW_PNL_BY_SYMBOL_PATTERN = PERFORMANCE_OVERVIEW_ROW_BY_SYMBOL_PATTERN + "/td[contains(@class,'v-trading-summary-performance__column_type_risk')]";
    private static final String WIDGET_BY_TITLE_PATTERN = "//div[contains(@class,'v-number-widget__title') and text()='%s']/..";
    private static final String VOLUME_CHART_CONTAINER = String.format(CHART_CONTAINER_PATTERN, "Volume", "USD");
    private static final String VOLUME_CHART_FEATURES = String.format("%s/descendant::div[@class='v-chart-wrapper__feature']", VOLUME_CHART_CONTAINER);
    private static final String VOLUME_CHART = String.format("%s/descendant::div[@class='v-trading-summary-volume__chart-container']", VOLUME_CHART_CONTAINER);
    private static final String PNL_BY_DURATION = "//div[text()='Realized PNL']/following-sibling::span[text()='by trade duration, USD']/ancestor::div[@class='v-trading-summary__chart']";
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
    private static final String PNL_SYMBOL_SECTION = "//span[text() = 'per symbol, USD']/ancestor::div[@class='v-trading-summary__chart']";
    private static final String SYMBOL_TRADED_SECTION = "//div[text() = 'Symbol traded']/ancestor::div[@class='v-trading-summary__chart']";
    private static final String PNL_SYMBOL_BAR_DESCRIPTION = "//div[(@class='v-pnl-symbol-bar__bar-description') and not (contains(@class,'v-pnl-symbol-bar__bar-description_right'))]";
    private static final String PNL_SYMBOL_BAR_DESCRIPTION_RIGHT = "//div[contains(@class,'v-pnl-symbol-bar__bar-description_right')]";
    private static final String PNL_SYMBOL_BAR_EMPTY = "//div[@class='v-pnl-symbol-bar__no-data']";
    private static final String SYMBOL_TRADED_EMPTY = "//div[@class='v-symbol-traded-bar__no-data']";
    private static final String PNL_SYMBOL_BAR = "//div[@class='v-pnl-symbol-bar__bar']";
    private static final String DANGER_HEAVY_TEXT = "//*[contains(@class,'g-color-text_color_danger-heavy')]";
    private static final String PRIMARY_TEXT = "*[contains(@class,'g-color-text_color_primary')]";
    private static final String SECONDARY_TEXT = "//*[contains(@class,'g-color-text_color_secondary')]";
    private static final String SUBHEADER_2_TEXT = "//*[contains(@class,'g-text_variant_subheader-2')]";
    private static final String PNL_SYMBOL_BAR_NEGATIVE = "//*[contains(@class,'v-pnl-symbol-bar__bar_negative')]";
    private static final String PNL_SYMBOL_BAR_POSITIVE = "//div[(@class='v-pnl-symbol-bar__bar') or (contains(@class,'v-pnl-symbol-bar__bar') and contains(@class,'v-pnl-symbol-bar__bar_begin'))]";
    private static final String PNL_SYMBOL_BAR_POSITIVE_BEGIN = "//div[contains(@class,'v-pnl-symbol-bar__bar_begin')]";
    private static final String SYMBOL_TRADED_BAR = "//div[@class='v-symbol-traded-bar__bar' or @class='v-symbol-traded-bar__bar v-symbol-traded-bar__bar_isFirst' ]";
    private static final String VOLUME_TRADED_BAR_ANNOTATION = "//div[@class='v-trading-summary-volume__chart-container']/div[contains(@class, 'g-text_variant_body-short')]";
    private static final String SYMBOL_TRADED_BAR_FIRST = "//div[contains(@class,'v-symbol-traded-bar__bar_isFirst')]";
    private static final String PNL_SYMBOL_TOOLTIP_LINE = "//div[(@class='v-pnl-symbol-tooltip__symbol')]";
    private static final String SYMBOL_TRADED_TOOLTIP = "//div[(@class='v-symbol-traded-tooltip')]";
    private static final String VOLUME_TOOLTIP = "//div[(@class='v-trading-summary-volume__tooltip')]";
    private static final String SYMBOL_TRADED_TOOLTIP_LINE = "//div[(@class='v-symbol-traded-tooltip__symbol')]";
    private static final String SYMBOL_TRADED_TOOLTIP_FOOTER_TITLE = "//div[(@class='v-symbol-traded-tooltip__other-title')]";
    private static final String SYMBOL_TRADED_BAR_DESCRIPTION = "//div[(@class='v-symbol-traded-bar__bar-description')]";
    private static final String TRADING_CHART_FEATURE = "//div[@class='v-chart-wrapper__feature']";
    private static final String TRADING_CHART_FEATURE_VALUE = "//div[contains(@class,'g-text_variant_header-1')]";
    private static final String HOLDING_TIME_SECTION = "//*[text()='Holding time']/ancestor::div[@class='v-trading-summary__chart']";
    private static final String HOLDING_TIME_BAR_ANNOTATION = "//div[@class='v-trading-summary-holding-time__ticks-container']/div/div";
    private static final String HOLDING_TIME_TOOLTIP = "//div[@class='v-trading-summary-holding-time__tooltip']";
    private static final String ERROR_CONTAINER = "//div[@class='v-error-view__container']";
    private static final String RETRY_BUTTON = "//button/span[text()='Retry']";
    private static final String TOXICITY_AND_PROFIT_CHART_CONTAINER = String.format(CHART_CONTAINER_PATTERN, "Toxicity and profit", "USD");
    private static final String TOXICITY_AND_PROFIT_CHART_FEATURES = String.format("%s/descendant::div[@class='v-chart-wrapper__feature']", TOXICITY_AND_PROFIT_CHART_CONTAINER);
    private static final String TOXICITY_AND_PROFIT_CHART = String.format("%s/descendant::div[@class='v-chart-wrapper__content']", TOXICITY_AND_PROFIT_CHART_CONTAINER);
    private static final String WIDGET_TITLE = "//div[contains(@class,'v-number-widget__title')]";
    private static final String ACCOUNT_CARD = "//div[@class='v-trading-tab-accounts-card']";
    private static final String ACCOUNT_CARD_IB_ACCOUNT = "//div[@class='v-ib-accounts__ib-accounts']";
    private static final String IB_ACCOUNT_ROW_CELL = "//td[contains(@class ,'v-trading-tab-accounts-table__column_type_ib')]";
    private static final String IB_ACCOUNT_REBATES_ROW_CELL = "//td[contains(@class ,'v-trading-tab-accounts-table__column_type_rebates')]";
    private static final String ACCOUNT_ROW_CELL = "//td[contains(@class ,'v-trading-tab-accounts-table__column')]";
    private static final String ACCOUNT_ROW = "//tr[@class = 'g-table__row g-table__row_vertical-align_top']";
    private static final String TABLE_HEADER = "*[contains(@class,'header-cell')";
    private static final String LOTS_AMOUNT_SWITCH = "//span[text()='Volume in USD']//preceding-sibling::span/input";
    private static final String OPERATIONS_ROW_BY_TICKET_PATTERN = "//div[@data-qa='trading_deals__table__rows__%s']";
    private final Locator scrollOperationsListDownButton;
    private final Locator scrollOperationsListUpButton;


    public TradingPage(Page page) {
        super(page);
        this.tradingTab = page.locator("[role=\"tab\"][title=\"Trading\"]");
        this.accountsTab = page.locator(".g-radio-button__option-control[value=\"Accounts\"]");
        this.operationsTab = page.locator(".g-radio-button__option-control[value=\"Deals\"]");
        this.accountsTabContent = page.locator(".v-trading-tab-accounts");
        this.dealsTabContent = page.locator(".v-trading-tab-deals");
        this.accountColumnHeader = page.locator("//" + TABLE_HEADER + " and (text()='ACCOUNT')]");
        this.typeColumnHeader = page.locator("//" + TABLE_HEADER + " and (text()='TYPE')]");
        this.volumeColumnHeader = page.locator("//" + TABLE_HEADER + " and (text()='VOLUME')]");
        this.profitColumnHeader = page.locator("//" + TABLE_HEADER + " and (text()='PROFIT')]");
        this.openColumnHeader = page.locator("//" + TABLE_HEADER + " and (text()='OPEN')]");
        this.closeColumnHeader = page.locator("//" + TABLE_HEADER + " and (text()='CLOSE')]");
        this.tpslColumnHeader = page.locator("//" + TABLE_HEADER + " and (text()='TP/SL')]");
        this.swapColumnHeader = page.locator("//" + TABLE_HEADER + " and (text()='SWAP')]");
        this.srColumnHeader = page.locator("//" + TABLE_HEADER + " and (text()='SR')]");
        this.commissionColumnHeader = page.locator("//" + TABLE_HEADER + " and (text()='COMMISSION')]");
        this.methodColumnHeader = page.locator("//" + TABLE_HEADER + " and (text()='METHOD')]");
        this.commentColumnHeader = page.locator("//" + TABLE_HEADER + " and (text()='COMMENT')]");
        this.accountColumnCell = page.locator("//*[@class='v-body-cell'][contains(@data-qa, '__account')]");
        this.typeValue = page.locator("//*[@class='v-body-cell'][contains(@data-qa, '__type')]//*[contains(@class,'v-trading-tab-deals__deal-type')]");
        this.typeColumnCell = page.locator("//*[@class='v-body-cell'][contains(@data-qa, '__type')]");
        this.profitColumnCell = page.locator("//*[@class='v-body-cell'][contains(@data-qa, '__profit')]");
        this.volumeColumnCell = page.locator("//*[@class='v-body-cell'][contains(@data-qa, '__volume')]");
        this.volumeLotsValue = page.locator(String.format("//*[@class='v-body-cell'][contains(@data-qa, '__volume')]//%s", PRIMARY_TEXT));
        this.volumeUsdValue = page.locator(String.format("//*[@class='v-body-cell'][contains(@data-qa, '__volume')]%s", SECONDARY_TEXT));
        this.openColumnCellDate = page.locator(String.format("//*[@class='v-body-cell'][contains(@data-qa, '__open')]//%s", PRIMARY_TEXT));
        this.closeColumnCellDate = page.locator(String.format("//*[@class='v-body-cell'][contains(@data-qa, '__close')]//%s", PRIMARY_TEXT));
        this.openColumnCell = page.locator("//*[@class='v-body-cell'][contains(@data-qa, '__open')]");
        this.closeColumnCell = page.locator("//*[@class='v-body-cell'][5]");
        this.tpslColumnCell = page.locator("//*[@class='v-body-cell'][contains(@data-qa, '__tp/sl')]");
        this.swapColumnCell = page.locator("//*[@class='v-body-cell'][contains(@data-qa, '__swap')]");
        this.srColumnCell = page.locator("//*[@class='v-body-cell'][contains(@data-qa, '__sr')]");
        this.commissionColumnCell = page.locator("//*[@class='v-body-cell'][contains(@data-qa, '__commission')]");
        this.methodColumnCell = page.locator("//*[@class='v-body-cell'][contains(@data-qa, '__method')]");
        this.commentColumnCell = page.locator("//*[@class='v-body-cell'][contains(@data-qa, '__comment')]");
        this.filterButton = page.locator("//button//*[text()=' Filter']");
        this.filterMenu = page.locator("[data-qa=\"drawer_body\"] .v-trading-tab-deals-filter__content");
        this.checkboxItem = page.locator(".v-trading-tab-deals-filter__filter-container  .g-checkbox");
        this.typeShowMoreButton = page.locator(".v-trading-tab-deals-filter__filter-container button").getByText("Show more");
        this.applyFiltersButton = page.locator("button").getByText("Apply");
        this.filterContainer = page.locator("v-trading-tab-deals-filter__filter-container");
        this.volumeAmountFromInput = page.locator("//div[text()=\"Volume in USD\"]/ancestor::div[contains(@class,'v-numeric-range-input')]/descendant::span[text()=\"From\"]/ancestor::span/input");
        this.volumeAmountToInput = page.locator("//div[text()=\"Volume in USD\"]/ancestor::div[contains(@class,'v-numeric-range-input')]/descendant::span[text()=\"To\"]/ancestor::span/input");
        this.volumeLotFromInput = page.locator("//div[text()=\"Volume in lots\"]/ancestor::div[contains(@class,'v-numeric-range-input')]/descendant::span[text()=\"From\"]/ancestor::span/input");
        this.volumeLotToInput = page.locator("//div[text()=\"Volume in lots\"]/ancestor::div[contains(@class,'v-numeric-range-input')]/descendant::span[text()=\"To\"]/ancestor::span/input");
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
        this.filterPopupElement = page.locator("//div[contains(@class,'g-popup__content')]");
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
        this.openDatePicker = page.locator(String.format(DATE_PICKER_BY_LABEL_PATTERN, "Open date"));
        this.closeDatePicker = page.locator(String.format(DATE_PICKER_BY_LABEL_PATTERN, "Close date"));
        this.typeCheckboxes = page.locator(String.format(CHECKBOXES_BY_LABEL_PATTERN, "Type"));
        this.accountsCheckboxes = page.locator(String.format(CHECKBOXES_BY_LABEL_PATTERN, "Accounts"));
        this.symbolCheckboxes = page.locator(String.format(CHECKBOXES_BY_LABEL_PATTERN, "Symbol"));
        this.methodCheckboxes = page.locator(String.format(CHECKBOXES_BY_LABEL_PATTERN, "Method"));
        this.resetTypeButton = page.locator(String.format(RESET_BUTTON_BY_LABEL_PATTERN, "Type"));
        this.resetAccountsButton = page.locator(String.format(RESET_BUTTON_BY_LABEL_PATTERN, "Account"));
        this.resetSymbolButton = page.locator(String.format(RESET_BUTTON_BY_LABEL_PATTERN, "Symbol"));
        this.resetMethodButton = page.locator(String.format(RESET_BUTTON_BY_LABEL_PATTERN, "Method"));
        this.resetOpenDateButton = page.locator(String.format(RESET_BUTTON_BY_LABEL_PATTERN, "Open date"));
        this.resetCloseDateButton = page.locator(String.format(RESET_BUTTON_BY_LABEL_PATTERN, "Close date"));
        this.resetDurationButton = page.locator(String.format(RESET_BUTTON_BY_LABEL_PATTERN, "Duration"));
        this.resetProfitButton = page.locator(String.format(RESET_BUTTON_BY_LABEL_PATTERN, "Profit"));
        this.resetVolumeButton = page.locator(String.format(RESET_BUTTON_BY_LABEL_PATTERN, "Volume in USD"));
        this.resetAllButton = page.locator("//span[text()='Reset all']/parent::button");
        this.openDateTooltip = page.locator(String.format(TOOLTIP_BY_LABEL_PATTERN, "Open date"));
        this.profitTooltip = page.locator(String.format(TOOLTIP_BY_LABEL_PATTERN, "Profit"));
        this.typeCheckboxLabels = page.locator(String.format(CHECKBOX_LABEL_BY_TITLE_PATTERN, "Type"));
        this.methodCheckboxLabels = page.locator(String.format(CHECKBOX_LABEL_BY_TITLE_PATTERN, "Method"));
        this.summaryTab = page.locator(".g-radio-button__option-control[value=\"Summary\"]");
        this.totalPnlMaxProfitValue = page.locator(String.format("%s/descendant::div[contains(@class,'g-color-text_color_brand')]", TOTAL_PNL_CHART_FEATURES)).last();
        this.totalPnlMaxLossValue = page.locator(String.format("%s/descendant::div[contains(@class,'g-color-text_color_danger-heavy')]", TOTAL_PNL_CHART_FEATURES)).last();
        this.totalPnlMaxProfitLabel = page.locator(String.format("%s/descendant::div[contains(@class,'g-color-text_color_brand')]", TOTAL_PNL_CHART_FEATURES)).first();
        this.totalPnlMaxLossLabel = page.locator(String.format("%s/descendant::div[contains(@class,'g-color-text_color_danger-heavy')]", TOTAL_PNL_CHART_FEATURES)).first();
        this.totalPnlMaxProfitDate = page.locator(String.format("%s/descendant::div[contains(@class,'g-color-text_color_secondary v-chart-wrapper__feature-description')]", TOTAL_PNL_CHART_FEATURES)).first();
        this.totalPnlMaxLossDate = page.locator(String.format("%s/descendant::div[contains(@class,'g-color-text_color_secondary v-chart-wrapper__feature-description')]", TOTAL_PNL_CHART_FEATURES)).last();
        this.totalPnlMaxProfitGraphDot = page.locator(String.format("%s/descendant::div[@style='color: rgb(77, 215, 175);']", TOTAL_PNL_CHART));
        this.totalPnlMaxLossGraphDot = page.locator(String.format("%s/descendant::div[@style='color: rgb(249, 116, 144);']", TOTAL_PNL_CHART));
        this.totalPnlYAxisLabel = page.locator(String.format("%s/descendant::div[@class='v-line-chart__padded-value']/div", TOTAL_PNL_CHART_CONTAINER));
        this.totalPnlTooltip = page.locator(".v-trading-summary-total-pnl__tooltip");
        this.totalPnlChartTitle = page.locator(TOTAL_PNL_CHART_CONTAINER).locator(CHART_TITLE);
        this.totalPnlXAxisLabels = page.locator(String.format("%s/descendant::div[@class='v-line-chart__ticks-container']/descendant::div[contains(@class,'g-text')]", TOTAL_PNL_CHART));
        this.performanceOverviewTableTitle = page.locator(PERFORMANCE_OVERVIEW_CHART_CONTAINER).locator(CHART_TITLE);
        this.performanceOverviewTableHeaders = page.locator(PERFORMANCE_OVERVIEW_CHART_CONTAINER).locator("//th");
        this.performanceOverviewSymbols = page.locator(PERFORMANCE_OVERVIEW_CHART_CONTAINER).locator("//td[contains(@class,'v-trading-summary-performance__column_type_date')]");
        this.winrateWidget = page.locator(String.format(WIDGET_BY_TITLE_PATTERN, "Win rate"));
        this.winrateWidgetValue = winrateWidget.locator(".v-number-widget__value");
        this.winrateWidgetInfo = winrateWidget.locator(".v-number-widget__info");
        this.volumeChartTitle = page.locator(VOLUME_CHART_CONTAINER).locator(CHART_TITLE);
        this.volumeYAxisLabel = page.locator(String.format("%s/descendant::div[@class='v-trading-summary-volume__padded-value']/div", VOLUME_CHART_CONTAINER));
        this.volumeMaxValue = page.locator(String.format("%s/descendant::div[contains(@class,'g-color-text_color_brand')]", VOLUME_CHART_FEATURES)).last();
        this.volumeTotalValue = page.locator(String.format("%s/descendant::div[contains(@class,'g-color-text_color_primary')]", VOLUME_CHART_FEATURES)).last();
        this.volumeMaxLabel = page.locator(String.format("%s/descendant::div[contains(@class,'g-color-text_color_brand')]", VOLUME_CHART_FEATURES)).first();
        this.volumeMaxDate = page.locator(String.format("%s/descendant::div[contains(@class,'g-color-text_color_secondary')]", VOLUME_CHART_FEATURES)).first();
        this.volumeTotalLabel = page.locator(String.format("%s/descendant::div[contains(@class,'g-color-text_color_primary')]", VOLUME_CHART_FEATURES)).first();
        this.volumeMaxGraphDot = page.locator(String.format("%s/descendant::div[contains(@class,'g-color-text_color_brand')]", VOLUME_CHART));
        this.pnlByDurationGraphSection = page.locator(PNL_BY_DURATION);
        this.pnlByDurationTooltip = page.locator(PNL_BY_DURATION_TOOLTIP);
        this.volumeXAxisLabels = page.locator("//div[@class='v-trading-summary-volume__ticks-container']/descendant::div[contains(@class,'g-text')]");
        this.holdingTimeTooltip = page.locator(HOLDING_TIME_TOOLTIP);
        this.toxicityAndProfitChartTitle = page.locator(TOXICITY_AND_PROFIT_CHART_CONTAINER).locator(CHART_TITLE);
        this.toxicityAndProfitYAxisLabel = page.locator(String.format("%s/descendant::div[@class='v-line-chart__padded-value']/div", TOXICITY_AND_PROFIT_CHART_CONTAINER));
        this.toxicityAndProfitMaxToxicityValue = page.locator(String.format("%s/descendant::div[contains(@class,'g-color-text_color_warning')]", TOXICITY_AND_PROFIT_CHART_FEATURES)).last();
        this.toxicityAndProfitMaxProfitValue = page.locator(String.format("%s/descendant::div[contains(@class,'g-color-text_color_utility')]", TOXICITY_AND_PROFIT_CHART_FEATURES)).last();
        this.toxicityAndProfitMaxToxicityLabel = page.locator(String.format("%s/descendant::div[contains(@class,'g-color-text_color_warning')]", TOXICITY_AND_PROFIT_CHART_FEATURES)).first();
        this.toxicityAndProfitMaxProfitLabel = page.locator(String.format("%s/descendant::div[contains(@class,'g-color-text_color_utility')]", TOXICITY_AND_PROFIT_CHART_FEATURES)).first();
        this.toxicityAndProfitMaxToxicityGraphDot = page.locator(String.format("%s/descendant::div[@class='v-peak-point__point-label']", TOXICITY_AND_PROFIT_CHART));
        this.toxicityAndProfitXAxisLabels = page.locator(String.format("%s/descendant::div[@class='v-line-chart__ticks-container']/descendant::div[contains(@class,'g-text')]", TOXICITY_AND_PROFIT_CHART));
        this.toxicityAndProfitTooltipIcon = page.locator(String.format("%s/descendant::div[@class='v-chart-wrapper__info-hint']", TOXICITY_AND_PROFIT_CHART_CONTAINER));
        this.tooltip = page.locator("//div[contains(@class,'g-tooltip__content')]");
        this.absoluteToxicityWidget = page.locator(String.format(WIDGET_BY_TITLE_PATTERN, "Absolute toxicity"));
        this.absoluteToxicityWidgetTitle = absoluteToxicityWidget.locator(WIDGET_TITLE);
        this.absoluteToxicityWidgetValue = absoluteToxicityWidget.locator(".v-number-widget__value");
        this.absoluteToxicityWidgetInfo = absoluteToxicityWidget.locator(".v-number-widget__info");
        this.ibRebatesWidget = page.locator(String.format(WIDGET_BY_TITLE_PATTERN, "IB rebates"));
        this.ibRebatesWidgetTitle = ibRebatesWidget.locator(WIDGET_TITLE);
        this.ibRebatesWidgetValue = ibRebatesWidget.locator(".v-number-widget__value");
        this.ibRebatesWidgetInfo = ibRebatesWidget.locator(".v-number-widget__info");
        this.ibRebatesWidgetText = ibRebatesWidget.locator(".v-number-widget__empty");
        this.operationsTableTooltip = page.locator("//div[@class='v-tooltip-content']");
        this.enabledHftButton = page.locator("//*[text()=' HFT']/ancestor::button[contains(@class, 'g-button_view_toned-action')]");
        this.disabledHftButton = page.locator("//*[text()=' HFT']/ancestor::button[not (contains(@class, 'g-button_view_toned-action'))]");
        this.highlightedRow = page.locator("//*[@class='v-virtualized-table__body-container']//*[contains(@class, 'v-body-row_highlighted')]");
        this.notHighlightedRow = page.locator("//*[@class='v-virtualized-table__body-container']//*[contains(@class, 'v-body-row') and not (contains(@class, 'v-body-row_highlighted'))]");
        this.lotsAmountSwitch = page.locator(LOTS_AMOUNT_SWITCH);
        this.errorMessage = page.locator("//*[@data-qa='trading_deals__table']//div[@class='v-error-view__error-text']");
        this.illegalProfitButton = page.locator("//button[@data-qa='trading_deals__controls__illegal_profit_button']");
        this.checkboxIllegalProfit = page.locator("//input[@type='checkbox']");
        this.illegalProfitAmountLoaded = page.locator("//div[@class='v-trading-tab-deals-multiselect-panel__illegal-profit']");
        this.saveIllegalProfitButton = page.locator("//button[@data-qa='trading_deals__multiselect_panel__save_illegal_profit']");
        this.toastMessage = page.locator("//div[contains(@class,'g-toast__container')]");
        this.selectedTradesCounter = page.locator("//div[@data-qa='trading_deals__multiselect_panel__counter']");
        this.scrollOperationsListUpButton = page.locator("//*[@class='v-trading-tab-deals-controls__button-group']/button[1]");
        this.scrollOperationsListDownButton = page.locator("//*[@class='v-trading-tab-deals-controls__button-group']/button[2]");
        this.illegalProfitSelectAllCheckBox = page.locator("//div[@data-qa='trading_deals__table__header__checkbox']");
        this.selectedIllegalProfitAmout = page.locator("//span[@data-qa='trading_deals__multiselect_panel__illegal_profit_amount']");
        this.selectedIllegalProfitAccountCount = page.locator("//span[@data-qa='trading_deals__multiselect_panel__account_count_string']");
    }

    public void navigate(String ucid) {
        Allure.step("Navigate to trading tab");
        page.navigate(String.format("%sinvestigation/%s/%s", BASE_URL_E2E, ucid, "trading"));
        page.waitForTimeout(200);
    }

    public void navigateSummary(String ucid) {
        Allure.step("Navigate to trading/summary tab");
        page.navigate(String.format("%sinvestigation/%s/%s", BASE_URL_E2E, ucid, "trading/summary"));
        page.waitForTimeout(200);
    }

    @Step("Navigate to users restriction tab/operations")
    public void navigateOperations(String ucid) {
        Allure.step("Navigate to users trading tab/operations");
        navigate(ucid);
        operationsTab.click();
        super.waitForPageToLoad();
    }

    @Step("Open users trading tab")
    public void openTradingTab() {
        tradingTab.click();
        waitForPageToLoadTrading();
    }

    public void clickScrollOperationsListUpButton() {
        Allure.step("Click scroll operations list up button");
        scrollOperationsListUpButton.click();
        waitForPageToLoad();
    }

    public void clickScrollOperationsListDownButton() {
        Allure.step("Click scroll operations list down button");
        scrollOperationsListDownButton.click();
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
        page.waitForTimeout(500);
    }


    @Step("Check if the trading/operations tab renders all basic elements")
    public void operationsRendersTest() {
        Allure.step("Check if the trading/operations tab renders all basic elements");
        operationsTab.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        accountColumnHeader.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        accountColumnCell.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        typeColumnHeader.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        typeValue.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        volumeColumnHeader.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        volumeColumnHeader.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        profitColumnHeader.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        profitColumnCell.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        openColumnHeader.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        openColumnCell.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        closeColumnHeader.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        closeColumnCell.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        tpslColumnHeader.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        tpslColumnCell.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        tpslColumnCell.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        swapColumnHeader.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        swapColumnCell.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        //deprecated column srColumnHeader.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        //deprecated column srColumnCell.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        commissionColumnHeader.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        commissionColumnCell.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        methodColumnHeader.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        methodColumnCell.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        commentColumnHeader.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        commissionColumnCell.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        filterButton.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        accountColumnHeader.getByText("ACCOUNT").waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        typeColumnHeader.getByText("TYPE").waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        volumeColumnHeader.getByText("VOLUME").waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        profitColumnHeader.getByText("PROFIT").waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        openColumnHeader.getByText("OPEN").waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        closeColumnHeader.getByText("CLOSE").waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        tpslColumnHeader.getByText("TP/SL").waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        swapColumnHeader.getByText("SWAP").waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        //deprecated column srColumnHeader.getByText("SR").waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        commissionColumnHeader.getByText("COMMISSION").waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        methodColumnHeader.getByText("METHOD").waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        commentColumnHeader.getByText("COMMENT").waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    @Step("Verify that all expected headers are present")
    public void verifyHeaders() {
        assertThat(operationsTab).isVisible();
        assertThat(accountColumnHeader).isVisible();
        assertThat(typeColumnHeader).isVisible();
        assertThat(volumeColumnHeader).isVisible();
        assertThat(openColumnHeader).isVisible();
        assertThat(closeColumnHeader).isVisible();
        assertThat(tpslColumnHeader).isVisible();
        assertThat(profitColumnHeader).isVisible();
        assertThat(swapColumnHeader).isVisible();
        assertThat(srColumnHeader).isVisible();
        assertThat(commissionColumnHeader).isVisible();
        assertThat(methodColumnHeader).isVisible();
        assertThat(commentColumnHeader).isVisible();
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
        assertEquals(methodName, methodColumnCell.first().textContent());
        assertEquals(methodName, methodColumnCell.last().textContent());
    }


    @Step("Check text content of first and last method cells on page")
    public void checkTypeCellsContent(String typeName) {
        Allure.step("Check text content of first and last type cells on page " + typeName);
        assertTrue(typeValue.first().textContent().matches("(.)*" + typeName + "*"));
        assertTrue(typeValue.last().textContent().matches("(.)*" + typeName + "*"));
        assertEquals(typeName, typeValue.first().textContent());
        assertEquals(typeName, typeValue.last().textContent());
    }

    @Step("Click apply button")
    public void clickApplyButton() {
        Allure.step("Click apply button");
        applyFiltersButton.click();
        super.waitForPageToLoad();
    }

    @Step("Fill volume values")
    public void fillVolumeAmountValues(String from, String to) {
        Allure.step("Fill volume amount values");
        volumeAmountFromInput.fill(from);
        volumeAmountToInput.fill(to);
    }

    @Step("Fill volume values")
    public void fillVolumeLotValues(String from, String to) {
        Allure.step("Fill volume amount values");
        volumeLotFromInput.fill(from);
        volumeLotToInput.fill(to);
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

    @Step("Check text content of first and last Volume in USD cells on page is in interval")
    public void checkVolumeCellsContentUSD(double from, double to) {
        Allure.step("Check text content of first and last Volume cells on page is in interval");
        String firstCell = volumeUsdValue.first().textContent().replace(" USD", "");
        String lastCell = volumeUsdValue.last().textContent().replace(" USD", "");
        writeLog(Double.parseDouble(firstCell));
        writeLog(Double.parseDouble(lastCell));
        assertTrue(from <= Double.parseDouble(firstCell) && Double.parseDouble(firstCell) <= to);
        assertTrue(from <= Double.parseDouble(lastCell) && Double.parseDouble(lastCell) <= to);
    }

    @Step("Check text content of first and last Volume in Lots cells on page is in interval")
    public void checkVolumeCellsContentLots(Double from, Double to) {
        Allure.step("Check text content of first and last Volume cells on page is in interval");
        String firstCell = volumeLotsValue.first().textContent().replace(" lots", "");
        String lastCell = volumeLotsValue.last().textContent().replace(" lots", "");
        writeLog(Double.parseDouble(firstCell));
        writeLog(Double.parseDouble(lastCell));
        assertTrue(from <= Double.parseDouble(firstCell) && Double.parseDouble(firstCell) <= to);
        assertTrue(from <= Double.parseDouble(lastCell) && Double.parseDouble(lastCell) <= to);
    }

    @Step("Check text content of first and last profit cells on page is in interval")
    public void checkProfitCellsContent(int from, int to) {
        Allure.step("Check text content of first and last profit cells on page is in interval");
        String firstCell = (profitColumnCell.first().textContent()).replace(" USD", "");
        String lastCell = profitColumnCell.last().textContent().replace(" USD", "");
        writeLog(Double.parseDouble(firstCell));
        writeLog(Double.parseDouble(lastCell));

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
        String firstCell = profitColumnCell.first().textContent().replace(" USD", "");

        writeLog(Double.parseDouble(firstCell));

        double profit1 = Double.parseDouble(firstCell);

        assertEquals(expected, profit1);
    }

    @Step("Check text content of first and last Dates in cells on page is in interval")
    public void checkDatesMinutes(int from, int to) {
        Allure.step("Check text content of first and last Dates in cells on page is in interval");
        String openDate = openColumnCellDate.first().textContent();
        String closeDate = closeColumnCellDate.first().textContent();
        writeLog(openDate);
        writeLog(closeDate);
        long difference = Utils.getDifferenceTimeMinutes(openDate, closeDate);
        assertTrue(from <= difference && difference <= to);
    }

    @Step("Check that operation with provided date is displayed")
    public void checkDealPresentedByDate(String date) {
        Allure.step("Check that operation with provided date is displayed");
        Locator target = openColumnCellDate.getByText(date);
        target.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    @Step("Check that operation with provided date is not displayed")
    public void checkDealHiddenByDate(String date) {
        Allure.step("Check that operation with provided date is not displayed");
        Locator target = openColumnCellDate.getByText(date);
        target.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
    }

    @Step("Wait for page to load")
    public void waitForPageToLoadTrading() {
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
    public String getAccountTradingPnl(int accountId) {
        return accountCard.nth(getAccountIndex(accountId)).locator(String.format(ACCOUNT_CARD_VALUE_BY_TITLE_PATTERN, "Trading PNL")).textContent();
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
        assertThat(popupElement.last()).containsText("Login");
    }

    @Step("Verify account balance popup in card view is as expected")
    public void verifyBalancePopup(int accountId) {
        accountCard.nth(getAccountIndex(accountId)).locator(balanceElement).locator("//span[contains(@class,'g-text_variant_header-2')]").hover();
        assertThat(popupElement.last()).containsText("Balance");
    }

    @Step("Verify account balance usd popup in card view is as expected")
    public void verifyBalanceUsdPopup(int accountId) {
        accountCard.nth(getAccountIndex(accountId)).locator(balanceElement).locator("//span[contains(@class,'g-text_variant_subheader-2')]").hover();
        assertThat(popupElement.last()).containsText("Balance in USD");
    }

    @Step("Verify account status popup in card view is as expected")
    public void verifyStatusPopup() {
        accountCard.first().locator(statusElement).hover();
        assertThat(popupElement.last()).containsText("Status");
    }

    @Step("Verify account platform popup in card view is as expected")
    public void verifyPlatformPopup() {
        accountCard.first().locator(platformElement).hover();
        assertThat(popupElement.last()).containsText("Platform");
    }

    @Step("Verify account type popup in card view is as expected")
    public void verifyAccountTypePopup() {
        accountCard.first().locator(accountTypeElement).hover();
        assertThat(popupElement.last()).containsText("Account type");
    }

    @Step("Verify account created time popup in card view is as expected")
    public void verifyCreatedTimePopup() {
        accountCard.first().locator(createdTimeElement).hover();
        assertThat(popupElement.last()).containsText("Created time");
    }

    @Step("Verify account updated time popup in card view is as expected")
    public void verifyUpdatedTimePopup() {
        accountCard.first().locator(updatedTimeElement).hover();
        assertThat(popupElement.last()).containsText("Updated time");
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

    @Step("Is operation with alert by index")
    public boolean isOperationWithAlert(int index) {
        return typeColumnCell.nth(index).locator("//*[local-name()='svg']").isVisible();
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
        openColumnHeader.click();
    }

    @Step("Change sorting by profit")
    public void sortByProfit() {
        profitColumnHeader.click();
    }

    @Step("Change sorting by close")
    public void sortByClose() {
        closeColumnHeader.click();
    }

    @Step("Get text of popup when hovering over sorting by open element")
    public String getSortByOpenPopupText() {
        openColumnHeader.hover();
        return operationsTableTooltip.textContent();
    }

    @Step("Get text of popup when hovering over sorting by profit element")
    public String getSortByProfitPopupText() {
        profitColumnHeader.hover();
        return operationsTableTooltip.textContent();
    }

    @Step("Get text of popup when hovering over sorting by close element")
    public String getSortByClosePopupText() {
        closeColumnHeader.hover();
        return operationsTableTooltip.textContent();
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
        String volume = "Volume in USD";
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
        assertThat(volumeAmountFromInput).hasValue("0 USD");
        assertThat(volumeAmountToInput).hasValue("50 USD");
        page.locator(String.format(PRESET_BY_LABEL_AND_VALUE_PATTERN, volume, "50-200")).click();
        assertThat(volumeAmountFromInput).hasValue("50 USD");
        assertThat(volumeAmountToInput).hasValue("200 USD");
        page.locator(String.format(PRESET_BY_LABEL_AND_VALUE_PATTERN, volume, "200-500")).click();
        assertThat(volumeAmountFromInput).hasValue("200 USD");
        assertThat(volumeAmountToInput).hasValue("500 USD");
        page.locator(String.format(PRESET_BY_LABEL_AND_VALUE_PATTERN, volume, ">500")).click();
        assertThat(volumeAmountFromInput).hasValue("500 USD");
        assertThat(volumeAmountToInput).hasValue("");
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
        assertThat(volumeAmountFromInput).hasValue("");
        assertThat(volumeAmountToInput).hasValue("");
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
        assertThat(filterPopupElement).containsText("Open date for trading operations/ Date for payments operations");
    }

    @Step("Verify profit tooltip is as expected")
    public void verifyProfitTooltip() {
        profitTooltip.hover();
        assertThat(filterPopupElement).containsText("Profit date for trading operations/ Amount for payments operations");
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

    @Step("Get Total PNL max profit date")
    public String getTotalPnlMaxProfitDate() {
        return totalPnlMaxProfitDate.textContent();
    }

    @Step("Get Total PNL max loss date")
    public String getTotalPnlMaxLossDate() {
        return totalPnlMaxLossDate.textContent();
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

    @Step("Get Volume max date")
    public String getVolumeMaxDate() {
        return volumeMaxDate.textContent();
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

    @Step("Get Performance overview tickets by symbol {symbol}")
    public String getPerformanceOverviewTicketsBySymbol(String symbol) {
        return page.locator(String.format(PERFORMANCE_OVERVIEW_TICKETS_BY_SYMBOL_PATTERN, symbol)).textContent();
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

    public void deleteClientDeals(String ucid) throws SQLException, InterruptedException {
        deleteEntryFromDb(MT4_TRADES_COERCED_TABLE_NAME, "ucid ='" + ucid + "'");
        Thread.sleep(1000);
    }

    public void openPnlDurationTooltip(String annotationText) {
        Allure.step("Hover mouse over graph section to open tooltip");
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
        assertThat(pnlByDurationTooltip).isVisible();
    }

    public void checkTextPnlDurationTooltipAmount(String sumAmout) {
        Allure.step("Check that PNL by duration tooltip shows right amount");
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
        Allure.step("Check duration percentage in the tooltip");
        assertThat(pnlByDurationTooltip).isVisible();
        String locator = (PNL_BY_DURATION_TOOLTIP + "//*[contains(text(),'" + percentage + "')]");
        assertThat(page.locator(locator)).hasText(percentage + "% of all tickets");
    }

    public void checkTextPnlDurationTooltipPercentage(double innerText) {
        checkTextPnlDurationTooltipPercentage(String.valueOf((int) innerText));
    }

    public void checkTextPnlDurationTooltipPercentage(int innerText) {
        checkTextPnlDurationTooltipPercentage(String.valueOf(innerText));
    }

    public void checkMaxProfitableValue(int expectedValue) {
        Allure.step("Check value in max profitable");
        Locator element = page.locator(PNL_DURATION_GRAPH + GREEN_TEXT);
        NumberFormat formatter = NumberFormat.getInstance(Locale.US);
        assertEquals(formatter.format(expectedValue), element.textContent());
    }

    public void checkMaxProfitableValue(double expectedValue) {
        checkMaxProfitableValue((int) Math.round(expectedValue));
    }

    public void checkMaxLossValue(int expectedValue) {
        Allure.step("Check value in max loss");
        Locator element = page.locator(PNL_DURATION_GRAPH + RED_TEXT);
        NumberFormat formatter = NumberFormat.getInstance(Locale.US);
        assertEquals(formatter.format(expectedValue), element.textContent());
    }

    public void checkMaxLossValue(double expectedValue) {
        checkMaxLossValue((int) Math.round(expectedValue));
    }

    public void checkTopProfitCategory(String expectedValue) {
        Allure.step("Check category in top profit header");
        assertEquals(expectedValue, page.locator(PNL_BY_DURATION + "//div[text() = 'Max profitable']/following-sibling::div").nth(0).textContent());
    }

    public void checkTopLossCategory(String expectedValue) {
        Allure.step("Check category in top loss header");
        assertEquals(expectedValue, page.locator(PNL_BY_DURATION + "//div[text() = 'Max losing']/following-sibling::div").nth(0).textContent());
    }

    public double calculatePnlByDeal(MtMt4TradesCoercedObject deal) {
        return deal.getProfitUsd() + deal.getCommissionUsd() + deal.getStorageUsd();
    }

    public double calculatePnlByDeal(Mt5DealsCoercedObject deal) {
        return deal.getProfitUsd() + deal.getCommissionUsd() + deal.getStorageUsd();
    }

    public int calculatePnlByDealInt(MtMt4TradesCoercedObject trade1, MtMt4TradesCoercedObject trade2) {
        return (int) (Math.round(calculatePnlByDeal(trade1) + calculatePnlByDeal(trade2)));
    }

    public int calculatePnlByDealInt(MtMt4TradesCoercedObject... trades) {
        double result = 0;
        for (MtMt4TradesCoercedObject i : trades) {
            result += (calculatePnlByDeal(i));
        }
        return (int) (Math.round(result));
    }

    public int calculatePnlByDealInt(MtMt4TradesCoercedObject deal) {
        return ((int) Math.round(deal.getProfitUsd() + deal.getCommissionUsd() + deal.getStorageUsd()));
    }

    public void checkBothPnlBySymbolBarsEmpty() {
        Allure.step("Check bars section is empty");
        page.waitForSelector(PNL_SYMBOL_BAR_EMPTY).isVisible();
        assertEquals(2, page.locator(PNL_SYMBOL_BAR_EMPTY).count());
    }

    public void checkProfitPnlBySymbolBarsEmpty() {
        Allure.step("Check profits side of bars section is empty");
        page.waitForSelector(PNL_SYMBOL_BAR_EMPTY).isVisible();
        page.waitForSelector(PNL_SYMBOL_BAR_NEGATIVE).isVisible();
        assertEquals(1, page.locator(PNL_SYMBOL_BAR_EMPTY).count());
    }

    public void checkLossesPnlBySymbolBarsEmpty() {
        Allure.step("Check loses side of bars section is empty");
        page.waitForSelector(PNL_SYMBOL_BAR_EMPTY).isVisible();
        page.waitForSelector(PNL_SYMBOL_BAR_POSITIVE).isVisible();
        assertEquals(1, page.locator(PNL_SYMBOL_BAR_EMPTY).count());
    }

    public void checkPnlBySymbolBarDescriptionProfits(String expectedText) {
        Allure.step("Check description near profits bar");
        page.waitForSelector(PNL_SYMBOL_BAR_DESCRIPTION_RIGHT).isVisible();
        assertEquals(expectedText, page.locator(PNL_SYMBOL_BAR_DESCRIPTION_RIGHT).textContent());
    }

    public void checkPnlBySymbolBarDescriptionProfits(String amount, String symbol) {
        Allure.step("Check description near profits bar");
        page.waitForSelector(PNL_SYMBOL_BAR_DESCRIPTION).isVisible();
        assertEquals(amount, page.locator(PNL_SYMBOL_BAR_DESCRIPTION + GREEN_TEXT).textContent());
        assertEquals(symbol, page.locator(PNL_SYMBOL_BAR_DESCRIPTION + SECONDARY_TEXT).nth(1).textContent());
    }

    public void checkPnlBySymbolBarDescriptionLoses(String expectedText) {
        Allure.step("Check description near loses bar");
        page.waitForSelector(PNL_SYMBOL_BAR_DESCRIPTION).isVisible();
        assertEquals(expectedText, page.locator(PNL_SYMBOL_BAR_DESCRIPTION).nth(0).textContent());
    }

    public void checkPnlBySymbolBarDescriptionLoses(String amount, String symbol) {
        Allure.step("Check description near loses bar");
        page.waitForSelector(PNL_SYMBOL_BAR_DESCRIPTION).isVisible();
        assertEquals(amount, page.locator(PNL_SYMBOL_BAR_DESCRIPTION + DANGER_HEAVY_TEXT).textContent());
        assertEquals(symbol, page.locator(PNL_SYMBOL_BAR_DESCRIPTION + SECONDARY_TEXT).nth(0).textContent());
    }

    public void checkPnlBySymbolBarPositiveCount(int expectedCount) {
        Allure.step("count displayed positive bars in PNL per Symbol graph");
        page.waitForSelector(PNL_SYMBOL_SECTION).waitForElementState(ElementState.VISIBLE);
        super.waitForPageToLoad();
        assertEquals(expectedCount, page.locator(PNL_SYMBOL_BAR_POSITIVE).count());
    }

    public void checkPnlBySymbolBarNegativeCount(int expectedCount) {
        Allure.step("count displayed negative bars in PNL per Symbol graph");
        page.waitForSelector(PNL_SYMBOL_SECTION).waitForElementState(ElementState.VISIBLE);
        super.waitForPageToLoad();
        assertEquals(expectedCount, page.locator(PNL_SYMBOL_BAR_NEGATIVE).count());
    }

    public void hoverOverRightPositiveBarPnlSymbol() {
        Allure.step("hover cursor over the rightest bar in PNL per Symbol graph");
        page.locator(PNL_SYMBOL_BAR).last().hover();
        page.waitForSelector(PNL_SYMBOL_TOOLTIP_LINE).waitForElementState(ElementState.VISIBLE);
    }

    public void checkPnlBySymbolTooltipValue(int numberOfLine, String expectedSymbol, String expectedAmount) {
        Allure.step("Check the symbol and PNL amount in the tooltip");
        page.waitForSelector(PNL_SYMBOL_TOOLTIP_LINE).waitForElementState(ElementState.VISIBLE);
        String actualSymbol = page.locator(PNL_SYMBOL_TOOLTIP_LINE + "[" + (numberOfLine + 1) + "]" + "//" + PRIMARY_TEXT).nth(0).textContent();
        String actualAmount = page.locator(PNL_SYMBOL_TOOLTIP_LINE + "[" + (numberOfLine + 1) + "]" + "//" + PRIMARY_TEXT).nth(1).textContent();
        assertEquals(expectedSymbol, actualSymbol);
        assertEquals(expectedAmount + " USD", actualAmount);
    }

    public void checkPnlBySymbolTooltipValue(String expectedSymbol, String expectedAmount) {
        checkPnlBySymbolTooltipValue(0, expectedSymbol, expectedAmount);
    }

    public void checkPnlBySymbolTooltipValue(String expectedSymbol, int expectedAmount) {
        checkPnlBySymbolTooltipValue(expectedSymbol, (decimalFormat.format(expectedAmount)));
    }

    public void hoverOverOtherPositiveBarPnlSymbol() {
        Allure.step("hover cursor over the 'Other' bar in PNL per Symbol graph");
        page.locator(PNL_SYMBOL_BAR_POSITIVE_BEGIN).hover();
        page.waitForSelector(PNL_SYMBOL_TOOLTIP_LINE).waitForElementState(ElementState.VISIBLE);
    }

    public void checkPnlBySymbolOtherTooltipHeaderValue(String expectedSymbols, String expectedAmount) {
        Allure.step("Check the symbol and PNL amount in the tooltip");
        page.waitForSelector(PNL_SYMBOL_TOOLTIP_LINE).waitForElementState(ElementState.VISIBLE);
        String actualSymbol = page.locator(PNL_SYMBOL_TOOLTIP_LINE + SUBHEADER_2_TEXT).nth(0).textContent();
        String actualAmount = page.locator(PNL_SYMBOL_TOOLTIP_LINE + SUBHEADER_2_TEXT).nth(1).textContent();
        assertEquals(expectedSymbols + " symbols", actualSymbol);
        assertEquals(expectedAmount + " USD", actualAmount);
    }

    public void checkPnlBySymbolOtherTooltipLinesCount(int expectedCount) {
        Allure.step("Check the count inside the tooltip including header and footer");
        page.waitForSelector(PNL_SYMBOL_TOOLTIP_LINE).waitForElementState(ElementState.VISIBLE);
        assertEquals(expectedCount, page.locator(PNL_SYMBOL_TOOLTIP_LINE).count());
    }

    public void checkPnlBySymbolOtherTooltipHeaderValue(int expectedSymbols, int expectedAmount) {
        checkPnlBySymbolOtherTooltipHeaderValue(String.valueOf(expectedSymbols), String.valueOf(expectedAmount));
    }

    public void symbolTradedEmptyState() {
        Allure.step("Check that Symbol Traded empty state is shown");
        page.waitForSelector(SYMBOL_TRADED_SECTION).waitForElementState(ElementState.VISIBLE);
        page.waitForSelector(SYMBOL_TRADED_EMPTY).waitForElementState(ElementState.VISIBLE);
    }

    public void hoverOverSymbolTradedBar(int index) {
        Allure.step("Hover over Symbol traded graph bar");
        page.waitForTimeout(300);
        page.waitForSelector(SYMBOL_TRADED_BAR).waitForElementState(ElementState.VISIBLE);
        page.locator(SYMBOL_TRADED_BAR).nth(index).hover();
        page.waitForTimeout(300);
        page.locator(SYMBOL_TRADED_BAR).nth(index).hover();
        page.waitForSelector(SYMBOL_TRADED_TOOLTIP).waitForElementState(ElementState.VISIBLE);
    }

    public void hoverOverVolumeTradedBar(int index) {
        Allure.step("Hover over Volume traded graph bar");
        page.waitForSelector(VOLUME_TRADED_BAR_ANNOTATION).waitForElementState(ElementState.VISIBLE);

        Locator target = page.locator(VOLUME_TRADED_BAR_ANNOTATION).nth(index);
        target.hover();
        page.waitForTimeout(300);
        target.hover();
        int i = 1;
        while ((!(page.locator(VOLUME_TOOLTIP).isVisible())) && (i < 1000)) {
            page.waitForTimeout(10);
            page.mouse().move(target.boundingBox().x, target.boundingBox().y + (i));
            page.waitForTimeout(10);
            i += 10;
        }
        page.waitForTimeout(100);
        page.waitForSelector(VOLUME_TOOLTIP).waitForElementState(ElementState.VISIBLE);
    }

    public void countSymbolTradedBar(int expectedCount) {
        Allure.step("Count Symbol traded graph bar");
        page.waitForSelector(SYMBOL_TRADED_BAR).waitForElementState(ElementState.VISIBLE);
        assertEquals(expectedCount, page.locator(SYMBOL_TRADED_BAR).count());
    }

    public void checkSymbolTradedOtherTooltipHeaderValue(String expectedSymbols, String expectedAmount) {
        Allure.step("Check the symbol and amount in the tooltip");
        page.waitForSelector(SYMBOL_TRADED_TOOLTIP_LINE).waitForElementState(ElementState.VISIBLE);
        String actualSymbol = page.locator(SYMBOL_TRADED_TOOLTIP_LINE + SUBHEADER_2_TEXT).nth(0).textContent();
        String actualAmount = page.locator(SYMBOL_TRADED_TOOLTIP_LINE + SUBHEADER_2_TEXT).nth(1).textContent();
        assertEquals(expectedSymbols + " symbols", actualSymbol);
        assertEquals(expectedAmount + " USD", actualAmount);
    }

    public void checkSymbolTradedOtherTooltipHeaderValue(String expectedSymbols, int expectedAmount) {
        checkSymbolTradedOtherTooltipHeaderValue(expectedSymbols, String.valueOf(expectedAmount));
    }

    public void checkSymbolTradedOtherTooltipHeaderValue(int expectedSymbols, int expectedAmount) {
        checkSymbolTradedOtherTooltipHeaderValue(String.valueOf(expectedSymbols), String.valueOf(expectedAmount));
    }

    public void checkSymbolTradedOtherTooltipHeaderValue(int expectedSymbols, Double expectedAmount) {
        checkSymbolTradedOtherTooltipHeaderValue(expectedSymbols, (int) Math.round(expectedAmount));
    }

    public int calculateNotionValueUsdByDealInt(MtMt4TradesCoercedObject... trades) {
        double result = 0;
        for (MtMt4TradesCoercedObject i : trades) {
            result += i.getNotionalValueUsd();
        }
        return (int) (Math.round(result));
    }

    public int calculateLotsByDealInt(MtMt4TradesCoercedObject... trades) {
        double result = 0;
        for (MtMt4TradesCoercedObject i : trades) {
            result += i.getVolumeLots();
        }
        return (int) (Math.round(result));
    }

    public double calculateNotionValueUsdByDealDouble(MtMt4TradesCoercedObject... trades) {
        double result = 0;
        for (MtMt4TradesCoercedObject i : trades) {
            result += i.getNotionalValueUsd();
        }
        return result;
    }

    public void checkSymbolTradedOtherTooltipLinesCount(int expectedCount) {
        Allure.step("Check the count inside the tooltip including header and footer");
        page.waitForSelector(SYMBOL_TRADED_TOOLTIP_LINE).waitForElementState(ElementState.VISIBLE);
        assertEquals(expectedCount, page.locator(SYMBOL_TRADED_TOOLTIP_LINE).count());
    }

    public void checkSymbolTradedOtherTooltipFooter(int expectedCount, int expectedAmount) {
        Allure.step("Check the count inside the tooltip including header and footer");
        page.waitForSelector(SYMBOL_TRADED_TOOLTIP_FOOTER_TITLE).waitForElementState(ElementState.VISIBLE);
        assertEquals("Others", page.locator(SYMBOL_TRADED_TOOLTIP_FOOTER_TITLE + "//" + PRIMARY_TEXT).textContent());
        assertEquals(decimalFormat.format(expectedCount), page.locator(SYMBOL_TRADED_TOOLTIP_FOOTER_TITLE + SECONDARY_TEXT).textContent());
        assertEquals(decimalFormat.format(expectedAmount) + " USD", page.locator(SYMBOL_TRADED_TOOLTIP_FOOTER_TITLE + "/following-sibling::div").textContent());
    }

    public void checkSymbolTradedHeaderMostTraded(String expectedSymbol) {
        Allure.step("Check the most traded symbol info above the graph");
        page.waitForSelector(SYMBOL_TRADED_SECTION + TRADING_CHART_FEATURE).waitForElementState(ElementState.VISIBLE);
        assertEquals(expectedSymbol, page.locator(SYMBOL_TRADED_SECTION + TRADING_CHART_FEATURE + TRADING_CHART_FEATURE_VALUE).nth(0).textContent());
        assertEquals(expectedSymbol, page.locator(SYMBOL_TRADED_SECTION + SYMBOL_TRADED_BAR_DESCRIPTION + SECONDARY_TEXT).nth(0).textContent());
        assertEquals("Most tradeable", page.locator(SYMBOL_TRADED_SECTION + TRADING_CHART_FEATURE + "//" + VARIANT_CAPTION_2_SELECTOR).nth(0).textContent());
    }

    public void checkSymbolTradedHeader(int position, String expectedSymbol) {
        Allure.step("Check the most traded symbol info above the graph");
        page.waitForSelector(SYMBOL_TRADED_SECTION + TRADING_CHART_FEATURE).waitForElementState(ElementState.VISIBLE);
        if (position == 2) {
            assertEquals(expectedSymbol, page.locator(SYMBOL_TRADED_SECTION + TRADING_CHART_FEATURE + TRADING_CHART_FEATURE_VALUE).nth(1).textContent());
            writeLog(SYMBOL_TRADED_SECTION + TRADING_CHART_FEATURE + "//" + VARIANT_CAPTION_2_SELECTOR);
            assertEquals("2nd", page.locator(SYMBOL_TRADED_SECTION + TRADING_CHART_FEATURE + "//" + VARIANT_CAPTION_2_SELECTOR).nth(2).textContent());
        } else if (position == 3) {
            assertEquals(expectedSymbol, page.locator(SYMBOL_TRADED_SECTION + TRADING_CHART_FEATURE + TRADING_CHART_FEATURE_VALUE).nth(2).textContent());
            assertEquals("3rd", page.locator(SYMBOL_TRADED_SECTION + TRADING_CHART_FEATURE + "//" + VARIANT_CAPTION_2_SELECTOR).nth(4).textContent());
        } else {
            writeLog("unexpected position " + position);
            assertTrue(false);
        }
    }

    public void checkSymbolTradedGraphDescription(int expectedAmountInt, String expectedSymbol) {
        Allure.step("Check the most traded symbol info above the bar in graph");
        String expectedAmount = decimalFormat.format(expectedAmountInt);
        assertEquals(expectedAmount, page.locator(SYMBOL_TRADED_BAR_DESCRIPTION + GREEN_TEXT).textContent());
        assertEquals(expectedSymbol, page.locator(SYMBOL_TRADED_BAR_DESCRIPTION + SECONDARY_TEXT).textContent());
    }


    public void checkSymbolTradedTooltipValue(int numberOfLine, String expectedSymbol, String expectedAmount) {
        Allure.step("Check the symbol and PNL amount in the tooltip");
        page.waitForSelector(SYMBOL_TRADED_TOOLTIP_LINE).waitForElementState(ElementState.VISIBLE);
        String actualSymbol = page.locator(SYMBOL_TRADED_TOOLTIP_LINE + "[" + (numberOfLine + 1) + "]" + "//" + PRIMARY_TEXT).nth(0).textContent();
        String actualAmount = page.locator(SYMBOL_TRADED_TOOLTIP_LINE + "[" + (numberOfLine + 1) + "]" + "//" + PRIMARY_TEXT).nth(1).textContent();
        assertEquals(expectedSymbol, actualSymbol);
        assertEquals(expectedAmount + " USD", actualAmount);
    }


    public void checkSymbolTradedTooltipValueLots(int numberOfLine, String expectedSymbol, String expectedLots) {
        Allure.step("Check the symbol and PNL amount in the tooltip");
        page.waitForSelector(SYMBOL_TRADED_TOOLTIP_LINE).waitForElementState(ElementState.VISIBLE);
        String actualSymbol = page.locator(SYMBOL_TRADED_TOOLTIP_LINE + "[" + (numberOfLine + 1) + "]" + "//" + PRIMARY_TEXT).nth(0).textContent();
        String actualAmount = page.locator(SYMBOL_TRADED_TOOLTIP_LINE + "[" + (numberOfLine + 1) + "]" + "//" + PRIMARY_TEXT).nth(1).textContent();
        assertEquals(expectedSymbol, actualSymbol);
        assertEquals(expectedLots + " lots", actualAmount);
    }

    public void checkSymbolTradedTooltipValue(String expectedSymbol, String expectedAmount) {
        checkSymbolTradedTooltipValue(0, expectedSymbol, expectedAmount);
    }

    public void checkSymbolTradedTooltipValue(String expectedSymbol, int expectedAmount) {
        checkSymbolTradedTooltipValue(expectedSymbol, (decimalFormat.format(expectedAmount)));
    }

    public void checkSymbolTradedTooltipValue(String expectedSymbol, double expectedAmount) {
        checkSymbolTradedTooltipValue(expectedSymbol, (decimalFormat.format((int) Math.round(expectedAmount))));
    }

    public void checkSymbolTradedTooltipValueLots(String expectedSymbol, String expectedLots) {
        checkSymbolTradedTooltipValueLots(0, expectedSymbol, expectedLots);
    }

    public void checkSymbolTradedTooltipValueLots(String expectedSymbol, int expectedLots) {
        checkSymbolTradedTooltipValueLots(expectedSymbol, (decimalFormat.format(expectedLots)));
    }

    public void checkSymbolTradedTooltipValueLots(String expectedSymbol, double expectedLots) {
        checkSymbolTradedTooltipValueLots(expectedSymbol, (decimalFormat.format((int) Math.round(expectedLots))));
    }

    public void openHoldingTimeTooltip(String annotationText) {
        Allure.step("Hover mouse over graph section to open tooltip");
        page.waitForTimeout(1000);
        String locator = HOLDING_TIME_SECTION + HOLDING_TIME_BAR_ANNOTATION + "[text()='" + annotationText + "']";
        page.hover(locator, new Page.HoverOptions().setForce(true));
        Locator target = page.locator(locator);
        int i = 1;
        while ((!(holdingTimeTooltip.isVisible())) && (i < 100)) {
            page.waitForTimeout(10);
            page.mouse().move(target.boundingBox().x, target.boundingBox().y - (i));
            page.waitForTimeout(10);
            i++;
        }
        page.waitForTimeout(100);
        assertThat(holdingTimeTooltip).isVisible();
    }

    public void checkHoldingTimeTooltip(int numberOfDeals, int percentageOfDeals) {
        Allure.step("Check that tooltip show data from DB");
        assertThat(holdingTimeTooltip).isVisible();
        if (numberOfDeals == 1) {
            assertEquals(String.valueOf(numberOfDeals) + " deal", page.locator(HOLDING_TIME_TOOLTIP + "//" + PRIMARY_TEXT + "[1]").textContent());
        } else {
            assertEquals(String.valueOf(numberOfDeals) + " deals", page.locator(HOLDING_TIME_TOOLTIP + "//" + PRIMARY_TEXT + "[1]").textContent());
        }
        assertEquals(String.valueOf(percentageOfDeals) + "% of all deals", page.locator(HOLDING_TIME_TOOLTIP + "//" + PRIMARY_TEXT + "[2]").textContent());
    }

    public void checkHoldingTimeEmpty() {
        Allure.step("Check that Holding Time annotations show empty state");
        assertEquals("-", page.locator(HOLDING_TIME_SECTION + TRADING_CHART_FEATURE + "[1]" + GREEN_TEXT).textContent());
        assertEquals("Most often", page.locator(HOLDING_TIME_SECTION + TRADING_CHART_FEATURE + "[1]" + SECONDARY_TEXT).textContent());

        assertEquals("-", page.locator(HOLDING_TIME_SECTION + TRADING_CHART_FEATURE + "[2]" + "//" + PRIMARY_TEXT).textContent());
        assertEquals("Of all deals", page.locator(HOLDING_TIME_SECTION + TRADING_CHART_FEATURE + "[2]" + SECONDARY_TEXT).textContent());
    }

    public void checkHoldingTimeHeader(String expectedInterval, String percentageOfDeals) {
        Allure.step("Check that Holding Time annotations show data from DB");
        assertEquals(expectedInterval, page.locator(HOLDING_TIME_SECTION + TRADING_CHART_FEATURE + "[1]" + GREEN_TEXT).textContent());
        assertEquals("Most often", page.locator(HOLDING_TIME_SECTION + TRADING_CHART_FEATURE + "[1]" + SECONDARY_TEXT).textContent());

        assertEquals(percentageOfDeals + "%", page.locator(HOLDING_TIME_SECTION + TRADING_CHART_FEATURE + "[2]" + "//" + PRIMARY_TEXT).textContent());
        assertEquals("Of all deals", page.locator(HOLDING_TIME_SECTION + TRADING_CHART_FEATURE + "[2]" + SECONDARY_TEXT).textContent());
    }

    public void checkHoldingTimeHeader(String expectedInterval, int percentageOfDeals) {
        checkHoldingTimeHeader(expectedInterval, String.valueOf(percentageOfDeals));
    }

    public void mockDurationError(String ucid) {
        Allure.step("mock duration return error");
        page.route("**/api/clients/" + ucid + "/trading/summaryDuration", route -> {
            APIResponse response = route.fetch();
            Map<String, String> headers = response.headers();
            route.fulfill(new Route.FulfillOptions().setResponse(response).setBody("500").setHeaders(headers).setStatus(500));
        });
    }

    public void mockDurationError() {
        Allure.step("mock duration return error");
        page.route("**/api/clients/**/trading/summaryDuration", route -> {
            APIResponse response = route.fetch();
            Map<String, String> headers = response.headers();
            route.fulfill(new Route.FulfillOptions().setResponse(response).setBody("500").setHeaders(headers).setStatus(500));
        });
    }

    public void checkHoldingTimeErrorState() {
        Allure.step("Check that Holding Time error state is shown");
        checkHoldingTimeEmpty();
        super.waitForPageToLoad();
        String errorLocator = HOLDING_TIME_SECTION + ERROR_CONTAINER;
        page.waitForSelector(errorLocator).waitForElementState(ElementState.VISIBLE);
        assertEquals("An error occurred. Please try visualizing the data again.Retry", page.locator(errorLocator).textContent());
        page.waitForSelector(errorLocator + RETRY_BUTTON).waitForElementState(ElementState.VISIBLE);

    }

    @Step("Get Toxicity & Profit chart title")
    public String getToxicityAndProfitChartTitle() {
        toxicityAndProfitChartTitle.hover();
        return toxicityAndProfitChartTitle.textContent();
    }

    @Step("Get Toxicity & Profit Y axis label")
    public String getToxicityAndProfitYAxisLabel() {
        return toxicityAndProfitYAxisLabel.textContent();
    }

    @Step("Get Toxicity & Profit max toxicity value")
    public String getToxicityAndProfitMaxToxicityValue() {
        return toxicityAndProfitMaxToxicityValue.textContent();
    }

    @Step("Get Toxicity & Profit max toxicity label")
    public String getToxicityAndProfitMaxToxicityLabel() {
        return toxicityAndProfitMaxToxicityLabel.textContent();
    }

    @Step("Get Toxicity & Profit max profit value")
    public String getToxicityAndProfitMaxProfitValue() {
        return toxicityAndProfitMaxProfitValue.textContent();
    }

    @Step("Get Toxicity & Profit max profit label")
    public String getToxicityAndProfitMaxProfitLabel() {
        return toxicityAndProfitMaxProfitLabel.textContent();
    }

    @Step("Get Toxicity & Profit max toxicity graph dot label")
    public String getToxicityAndProfitMaxToxicityGraphDot() {
        return toxicityAndProfitMaxToxicityGraphDot.textContent();
    }

    @Step("Get Toxicity & Profit chart x axis labels")
    public List<String> getToxicityAndProfitXAxisLabels() {
        List<String> xAxisLabels = new ArrayList<>();
        for (int i = 0; i < toxicityAndProfitXAxisLabels.count(); i++) {
            Locator label = toxicityAndProfitXAxisLabels.nth(i);
            xAxisLabels.add(label.textContent());
        }
        return xAxisLabels;
    }

    @Step("Get Toxicity & Profit tooltip text")
    public String getToxicityAndProfitTooltip() {
        toxicityAndProfitTooltipIcon.hover();
        return tooltip.textContent();
    }

    @Step("Get Absolute toxicity widget title")
    public String getAbsoluteToxicityWidgetTitle() {
        return absoluteToxicityWidgetTitle.textContent();
    }

    @Step("Get Absolute toxicity widget value")
    public String getAbsoluteToxicityWidgetValue() {
        return absoluteToxicityWidgetValue.textContent();
    }

    @Step("Get Absolute toxicity widget info")
    public String getAbsoluteToxicityWidgetInfo() {
        return absoluteToxicityWidgetInfo.textContent();
    }

    public void checkIbAccountValueCard(int account, int expectedValue) {
        Allure.step("Check that Ib account value is shown and match expected");
        String locator = "//*[text()='" + account + "']//ancestor::div" + ACCOUNT_CARD + ACCOUNT_CARD_IB_ACCOUNT;
        page.waitForSelector(locator).waitForElementState(ElementState.VISIBLE);
        assertEquals(String.valueOf(expectedValue), page.locator(locator).textContent());
    }

    public void checkIbAccountValueCard(int account, int expectedValue, int number) {
        Allure.step("Check that Ib account value is shown and match expected");
        String locator = "//*[text()='" + account + "']//ancestor::div" + ACCOUNT_CARD + ACCOUNT_CARD_IB_ACCOUNT + SECONDARY_TEXT + "[" + number + "]";
        page.waitForSelector(locator).waitForElementState(ElementState.VISIBLE);
        String result = page.locator(locator).textContent().replace(",", "").trim();
        assertEquals(String.valueOf(expectedValue), result);
    }

    public void checkIbAccountValueCardMultiple(int account, int expectedValue) {
        Allure.step("Check that Ib account value is shown and match expected");
        String locator = "//*[text()='" + account + "']//ancestor::div" + ACCOUNT_CARD + ACCOUNT_CARD_IB_ACCOUNT;
        page.waitForSelector(locator).waitForElementState(ElementState.VISIBLE);
        assertTrue(page.locator(locator).textContent().contains(String.valueOf(expectedValue)));
    }

    public void checkIbAccountValueTable(int account, int expectedValue) {
        Allure.step("Check that Ib account value is shown and match expected in table view");
        String locator = ACCOUNT_ROW_CELL + "//*[text()='" + account + "']//ancestor::tr" + IB_ACCOUNT_ROW_CELL;
        page.waitForSelector(locator).waitForElementState(ElementState.VISIBLE);
        assertEquals(String.valueOf(expectedValue), page.locator(locator).textContent());
    }

    public void checkIbAccountValueTable(int account, int expectedValue, int number) {
        Allure.step("Check that Ib account value is shown and match expected in table view");
        String locator = ACCOUNT_ROW_CELL + "//*[text()='" + account + "']//ancestor::tr" + IB_ACCOUNT_ROW_CELL + "//" + PRIMARY_TEXT + "[" + number + "]";
        page.waitForSelector(locator).waitForElementState(ElementState.VISIBLE);
        assertEquals(String.valueOf(expectedValue), page.locator(locator).textContent());
    }

    public void checkIbRebatesValueTable(int account, double expectedValue) {
        Allure.step("Check that Ib account value is shown and match expected in table view");
        String locator = ACCOUNT_ROW_CELL + "//*[text()='" + account + "']//ancestor::tr" + IB_ACCOUNT_REBATES_ROW_CELL;
        page.waitForSelector(locator).waitForElementState(ElementState.VISIBLE);
        assertEquals(String.valueOf(decimalFormat.format(expectedValue)) + " USD", page.locator(locator).textContent());
    }

    public void checkIbRebatesValueTable(int account, double expectedValue, int number) {
        Allure.step("Check that Ib account value is shown and match expected in table view");
        String locator = ACCOUNT_ROW_CELL + "//*[text()='" + account + "']//ancestor::tr" + IB_ACCOUNT_REBATES_ROW_CELL + "//" + PRIMARY_TEXT + "[" + number + "]";
        page.waitForSelector(locator).waitForElementState(ElementState.VISIBLE);
        assertEquals(String.valueOf(decimalFormat.format(expectedValue)) + " USD", page.locator(locator).textContent());
    }

    public void checkIbRebatesValueCard(int account, double expectedValue) {
        Allure.step("Check that Ib rebate value is shown and match expected");
        String locator = "//*[text()='" + account + "']//ancestor::div" + ACCOUNT_CARD + "//div[text()='IB rebates']/following-sibling::div";
        page.waitForSelector(locator).waitForElementState(ElementState.VISIBLE);
        assertEquals(String.valueOf(decimalFormat.format(expectedValue)) + " USD", page.locator(locator).textContent());
    }

    public void checkIbRebatesValueCard(int account, double expectedValue, int number) {
        Allure.step("Check that Ib rebate value is shown and match expected");
        String locator = "//*[text()='" + account + "']//ancestor::div" + ACCOUNT_CARD + "//div[text()='IB rebates']/following-sibling::div[contains(@class, 'g-text')][" + number + "]";
        page.waitForSelector(locator).waitForElementState(ElementState.VISIBLE);
        assertEquals(String.valueOf(decimalFormat.format(expectedValue)) + " USD", page.locator(locator).textContent());
    }

    @Step("Get IB rebates widget title")
    public String getIbRebatesWidgetTitle() {
        return ibRebatesWidgetTitle.textContent();
    }

    @Step("Get IB rebates widget text")
    public String getIbRebatesWidgetText() {
        return ibRebatesWidgetText.textContent();
    }

    @Step("Get IB rebates widget value")
    public String getIbRebatesWidgetValue() {
        return ibRebatesWidgetValue.textContent();
    }

    @Step("Get IB rebates widget info")
    public String getIbRebatesWidgetInfo() {
        return ibRebatesWidgetInfo.textContent();
    }


    public void generateDifferentTicketTypes(ClientHelper client) {
        Allure.step("Prepare client test data");
        MtMt4TradesCoercedObject trade1 = generateMt4TradesCoercedRandomized(client);
        trade1.setTicketType("Sell Limit");
        MtMt4TradesCoercedObject trade2 = generateMt4TradesCoercedRandomized(client);
        trade2.setTicketType("Buy");
        MtMt4TradesCoercedObject trade3 = generateMt4TradesCoercedRandomized(client);
        trade3.setTicketType("Balance");
        MtMt4TradesCoercedObject trade4 = generateMt4TradesCoercedRandomized(client);
        trade4.setTicketType("Sell");
        MtMt4TradesCoercedObject trade5 = generateMt4TradesCoercedRandomized(client);
        trade5.setTicketType("Sell Stop");
        MtMt4TradesCoercedObject trade6 = generateMt4TradesCoercedRandomized(client);
        trade6.setTicketType("Buy Limit");
        MtMt4TradesCoercedObject trade7 = generateMt4TradesCoercedRandomized(client);
        trade7.setTicketType("Credit");
        MtMt4TradesCoercedObject trade8 = generateMt4TradesCoercedRandomized(client);
        trade8.setTicketType("Buy Stop");
        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade1, trade2, trade3, trade4, trade5, trade6, trade7, trade8));
        page.waitForTimeout(1000);
    }


    public void generateDifferentReason(ClientHelper client) {
        Allure.step("Prepare client test data");
        MtMt4TradesCoercedObject trade1 = generateMt4TradesCoercedRandomized(client);
        trade1.setReasonName("Expert");
        MtMt4TradesCoercedObject trade2 = generateMt4TradesCoercedRandomized(client);
        trade2.setReasonName("Client");
        MtMt4TradesCoercedObject trade3 = generateMt4TradesCoercedRandomized(client);
        trade3.setReasonName("Dealer");
        MtMt4TradesCoercedObject trade4 = generateMt4TradesCoercedRandomized(client);
        trade4.setReasonName("API");
        MtMt4TradesCoercedObject trade5 = generateMt4TradesCoercedRandomized(client);
        trade5.setReasonName("Mobile");
        MtMt4TradesCoercedObject trade6 = generateMt4TradesCoercedRandomized(client);
        trade6.setReasonName("Web");
        MtMt4TradesCoercedObject trade7 = generateMt4TradesCoercedRandomized(client);
        trade7.setReasonName("Signal");
        MtMt4TradesCoercedObject trade8 = generateMt4TradesCoercedRandomized(client);
        trade8.setReasonName("Stop Loss");
        MtMt4TradesCoercedObject trade9 = generateMt4TradesCoercedRandomized(client);
        trade9.setReasonName("Take Profit");
        MtMt4TradesCoercedObject trade10 = generateMt4TradesCoercedRandomized(client);
        trade10.setReasonName("Stop-Out");
        MtMt4TradesCoercedObject trade11 = generateMt4TradesCoercedRandomized(client);
        trade11.setReasonName("External client");
        MtMt4TradesCoercedObject trade12 = generateMt4TradesCoercedRandomized(client);
        trade12.setReasonName("Symbol split");
        MtMt4TradesCoercedObject trade13 = generateMt4TradesCoercedRandomized(client);
        trade13.setReasonName("Gateway");
        MtMt4TradesCoercedObject trade14 = generateMt4TradesCoercedRandomized(client);
        trade14.setReasonName("Migration");
        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade1, trade2, trade3, trade4, trade5, trade6, trade7, trade8, trade9, trade10, trade11, trade12, trade13, trade14));
        page.waitForTimeout(1000);
    }

    public static String translateActionMT5(Integer action) {
        return switch (action) {
            case 0 -> "Buy";
            case 1 -> "Sell";
            case 2 -> "Balance";
            case 3 -> "Credit";
            case 4 -> "Charge";
            case 5 -> "Correction";
            case 6 -> "Bonus";
            case 7 -> "Commission";
            case 8 -> "Commission Daily";
            case 9 -> "Commission Monthly";
            case 10 -> "Agent Daily";
            case 11 -> "Agent Monthly";
            case 12 -> "Interestrate";
            case 13 -> "Buy Cancelled";
            case 14 -> "Sell Cancelled";
            case 15 -> "Dividend";
            case 16 -> "Dividend Franked";
            case 17 -> "Tax";
            case 18 -> "Agent";
            case 19 -> "SO Compensation";

            default -> "Error. Case integer is unknown";
        };
    }

    public static String translateActionMT4(Integer cmd) {
        return switch (cmd) {
            case 0 -> "Buy";
            case 1 -> "Sell";
            case 6 -> "Balance";
            case 7 -> "Credit";

            default -> "Error. Case integer is unknown";
        };
    }

    public static String translateReasonMT5(Integer action) {
        return switch (action) {
            case 0 -> "Client";
            case 1 -> "Expert";
            case 2 -> "Dealer";
            case 3 -> "Sl";
            case 4 -> "Tp";
            case 5 -> "So";
            case 6 -> "Rollover";
            case 7 -> "External Client";
            case 8 -> "VMargin";
            case 9 -> "Getaway";
            case 10 -> "Signal";
            case 11 -> "Settlement";
            case 12 -> "Transfer";
            case 13 -> "Sync";
            case 14 -> "External Service";
            case 15 -> "Migration";
            case 16 -> "Mobile";
            case 17 -> "Web";
            case 18 -> "Split";

            default -> "Error. Reason integer is unknown";
        };
    }

    public static String translateReasonMT4(Integer cmd) {
        return switch (cmd) {
            case 0 -> "Client";
            case 1 -> "Expert";
            case 2 -> "Dealer";
            case 3 -> "Signal";
            case 4 -> "Gateway";
            case 5 -> "Mobile";
            case 6 -> "Web";
            case 7 -> "API";

            default -> "Error. Case integer is unknown";
        };
    }

    public void enabledHftButton() {
        Allure.step("enable HFT highlight by clicking HFT button");
        enabledHftButton.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
        disabledHftButton.click();
        enabledHftButton.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public void disableHftButton() {
        Allure.step("disable HFT highlight by clicking HFT button");
        disabledHftButton.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
        enabledHftButton.click();
        disabledHftButton.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public void checkCountHighlightedRows(Integer expectedRowCount) {
        super.waitForPageToLoad();
        assertEquals(expectedRowCount, highlightedRow.count());
    }

    public void checkCountNotHighlightedRows(Integer expectedRowCount) {
        super.waitForPageToLoad();
        assertEquals(expectedRowCount, notHighlightedRow.count());
    }

    public void enableViewAmount() {
        super.waitForPageToLoad();
        if ("false".equals(lotsAmountSwitch.getAttribute("aria-checked"))) {
//            lotsAmountSwitch.click();
            page.locator(LOTS_AMOUNT_SWITCH + "/following-sibling::span[@class='g-switch__slider']").click();
            assertEquals("true", lotsAmountSwitch.getAttribute("aria-checked"));
            page.waitForTimeout(300);
        }
    }

    public void enableViewLots() {
        super.waitForPageToLoad();
        if ("true".equals(lotsAmountSwitch.getAttribute("aria-checked"))) {
//            lotsAmountSwitch.click();
            page.locator(LOTS_AMOUNT_SWITCH + "/following-sibling::span[@class='g-switch__slider']").click();
            assertEquals("false", lotsAmountSwitch.getAttribute("aria-checked"));
            page.waitForTimeout(300);
        }
    }

    public void errorMessageIsNotVisible() {
        super.waitForPageToLoad();
        page.waitForTimeout(500);
        assertFalse(errorMessage.isVisible());
    }

    public void isTradingTabVisible() {
        Allure.step("check is trading tab visible");
        tradingTab.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public void isIBRebatesHidden() {
        Allure.step("check is ib Rebates vidget hidden");
        waitForPageToLoadTrading();
        ibRebatesWidget.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
    }

    @Step("Click Illegal profit button")
    public void clickIllegalProfitButton() {
        illegalProfitButton.click();
    }

    @Step("Select trade for illegal profit by ticket")
    public void selectIllegalTradeByTicket(Long ticket) {
        page.locator(String.format(OPERATIONS_ROW_BY_TICKET_PATTERN, ticket)).locator(checkboxIllegalProfit).click();
    }

    @Step("Get illegal profit amount")
    public String getIllegalProfitAmount() {
        return illegalProfitAmountLoaded.locator("//span").first().textContent();
    }

    @Step("Get illegal profit accounts quantity")
    public String getIllegalProfitAccountsQuantity() {
        return illegalProfitAmountLoaded.locator("//span").last().textContent();
    }

    @Step("Get selected illegal trades counter")
    public String getSelectedIllegalTradesCounter() {
        return selectedTradesCounter.textContent();
    }

    @Step("Select trade for illegal profit by ticket")
    public void clickSaveAsIllegalProfit() {
        saveIllegalProfitButton.click();
        MatcherAssert.assertThat("Verify success popup", getToastMessageText(), is("Illegal profit savedSuggested deduction will be calculated automatically after fraud confirmation"));
    }

    @Step("Get toast message text")
    public String getToastMessageText() {
        return toastMessage.textContent();
    }

    public void isIllegalProfitButtonHidden() {
        Allure.step("check is illegal profit button hidden");
        super.waitForPageToLoad();
        illegalProfitButton.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
        assertFalse(illegalProfitButton.isVisible());
    }

    public void isIllegalProfitButtonVisible() {
        Allure.step("check is illegal profit button hidden");
        super.waitForPageToLoad();
        illegalProfitButton.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertTrue(illegalProfitButton.isVisible());
    }

    @Step("Click select all trades as illegal button")
    public void clickSelectAllTradesAsIllegal() {
        illegalProfitSelectAllCheckBox.click();
    }

    @Step("Get selected illegal profit amount")
    public String getSelectedIllegalProfitAmount() {
        return selectedIllegalProfitAmout.textContent();
    }

    @Step("Get selected illegal profit accounts quantity")
    public String getSelectedIllegalProfitAccountsQuantity() {
        return selectedIllegalProfitAccountCount.textContent();
    }
}

