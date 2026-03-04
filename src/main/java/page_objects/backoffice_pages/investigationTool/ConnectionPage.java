package page_objects.backoffice_pages.investigationTool;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static com.microsoft.playwright.options.WaitForSelectorState.HIDDEN;
import static com.microsoft.playwright.options.WaitForSelectorState.VISIBLE;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static utils.ConfigFactory.BASE_URL_E2E;
import static utils.Utils.*;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;
import helpers.data.ClientHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import java.util.*;
import org.hamcrest.MatcherAssert;
import page_objects.backoffice_pages.AbstractPage;
import utils.Utils;

public class ConnectionPage extends AbstractPage {

    private final Locator connectionTab;
    private final Locator tableViewButton;
    private final Locator graphViewButton;
    private final Locator levelHeader;
    private final Locator clientHeader;
    private final Locator connectionHeader;
    private final Locator attributeHeader;
    private final Locator behaviorHeader;
    private final Locator pnlHeader;
    private final Locator operationHeader;
    private final Locator ibHeader;
    private final Locator registeredHeader;
    private final Locator lastLoginHeader;
    private final Locator unmaskConnectionCardDataButton;
    private final Locator connectionTableAttribute;
    private final Locator unmaskConnectionTableDataButton;
    private final Locator filterButton;
    private final Locator filterOptionButton;
    private final Locator filterCheckboxListOption;
    private final Locator filterSliderRange;
    private final Locator filterSlider;
    private final Locator attributeFilterDropdown;
    private final Locator dropdownMenuItems;
    private final Locator activeRestrictionsSwitch;
    private final Locator pnlFilterFromInput;
    private final Locator pnlFilterToInput;
    private final Locator lastLoginDatePicker;
    private final Locator resetLevelButton;
    private final Locator resetConnectionTypeButton;
    private final Locator resetScoreToInitialButton;
    private final Locator resetAttributeButton;
    private final Locator resetBehaviorButton;
    private final Locator resetPnlButton;
    private final Locator resetLastLoginButton;
    private final Locator levelFilterButtons;
    private final Locator connectionTypeFilterButtons;
    private final Locator behaviorFilterButtons;
    private final Locator attributesFilterSelectedItems;
    private final Locator resetAllButton;
    private final Locator graphNodesUnhidden;
    private final Locator applyFiltersButton;
    private final Locator connectionTableRow;
    private final Locator connectionTableUserIds;
    private final Locator appliedFilters;
    private final Locator filtersCounter;
    private final Locator zoomInButton;
    private final Locator zoomValue;
    private final Locator tooltip;
    private final Locator connectionScoreFilterPresetsOptions;
    private final Locator multiselectButton;
    private final Locator multiselectSelectAllCheckbox;
    private final Locator multiselectCounter;
    private final Locator multiselectCommentButton;
    private final Locator multiselectCommentInput;
    private final Locator multiselectAddCommentButton;
    private final Locator graphNodeStatus;
    private final Locator graphNodeExpand;
    private final Locator graphNodeAttributes;
    private final Locator cardClientName;
    private final Locator cardClientId;
    private final Locator cardConnectionLevel;
    private final Locator cardConnectionScore;
    private final Locator cardOpenInNewTabButton;
    private final Locator valueIcon;
    private final Locator valueText;
    private final Locator cardBrandIcon;
    private final Locator cardBrandText;
    private final Locator cardCountryIcon;
    private final Locator cardCountryText;
    private final Locator cardEmail;
    private final Locator cardCpa;
    private final Locator cardIb;
    private final Locator cardRegistered;
    private final Locator cardLastLogin;
    private final Locator cardTrading;
    private final Locator cardTotalPnl;
    private final Locator cardDeposit;
    private final Locator cardWithdrawal;
    private final Locator cardFraud;
    private final Locator directConnectionsAmount;
    private final Locator zoomOptions;
    private final Locator cardAttributeName;
    private final Locator cardAttributeClient;
    private final Locator cardAttributeValue;
    private final Locator connectionTableRowData;
    private final Locator successToast;
    private final Locator fraudTypes;
    private final Locator addFraudRestrictionsButton;
    private final Locator addFraudRestrictionsDrawer;
    private final Locator fraudSelectButton;
    private final Locator addFraudRestrictionsComment;
    private final Locator addFraudRestrictionsSubmitButton;

    private final String CONNECTION_TABLE_BUTTON_SELECTOR = "input[value='TABLE']";
    private static final String CONNECTION_TABLE_SELECTOR = ".v-connection-search-table-mode-v2__view";
    private static final String CONNECTION_GRAPH_SELECTOR = ".v-graph-canvas-v2";
    private static final String CONNECTION_TABLE_ROW_BY_CLIENT_ID_PATTERN =
            "//span[text()='%s']/ancestor::div[contains(@class,'v-body-row')]";
    private static final String FILTER_CONTAINER_BY_TITLE_PATTERN =
            "//div[@class='v-text-with-icon__text' and text()='%s']/ancestor::div[@class='v-filter-container']";
    private static final String FILTER_CONTAINER = "//div[@class='v-drawer-content-wrapper__content']";
    private static final String PRESET_BY_LABEL_AND_VALUE_PATTERN =
            FILTER_CONTAINER_BY_TITLE_PATTERN + "/descendant::span[text()='%s']";
    private static final String CHECKBOX_BY_LABEL_AND_VALUE_PATTERN = FILTER_CONTAINER_BY_TITLE_PATTERN
            + "/descendant::div[text()='%s']/ancestor::div[@class='v-checkbox-list__item']/descendant::input[@type='checkbox']";
    private static final String RESET_BUTTON_BY_LABEL_PATTERN =
            FILTER_CONTAINER_BY_TITLE_PATTERN + "/descendant::span[text()='Reset']";
    private static final String CHECKBOX_BY_LABEL_PATTERN =
            FILTER_CONTAINER_BY_TITLE_PATTERN + "/descendant::input[@type='checkbox']";
    private static final String BUTTON_BY_LABEL_PATTERN = FILTER_CONTAINER_BY_TITLE_PATTERN + "/descendant::button";
    private static final String ATTRIBUTE_FILTER_NAME_PATTERN =
            "//div[@class='v-drop-down-menu__menu']/descendant::div[text()='%s']";
    private static final String ATTRIBUTE_FILTER_VALUE_PATTERN =
            "//div[contains(@data-dd-value,'%s')]/descendant::div[text()='%s']";
    private static final String CONNECTION_TABLE_ROW = "//div[contains(@class,'v-body-row')]";
    private static final String ZOOM_CONTROLS = "//div[@class='v-graph-scale-controls-v2__zoom-controls']";
    private static final String CONNECTION_TABLE_HEADER_BY_TEXT_PATTERN =
            "//div[contains(@class,'header-cell') and text()='%s']";
    private static final String CONNECTION_SCORE_FILTER_PRESETS =
            "//div[@class='v-connection-search-filter-score-presets-v2']";
    private static final String CONNECTION_SCORE_FILTER_PRESET_BY_TEXT_PATTERN =
            CONNECTION_SCORE_FILTER_PRESETS + "/descendant::span[@class='g-button__text' and text()='%s']/..";
    private static final String GRAPH_NODE_BY_ORDER =
            "//div[@class='graph-block-container undefined' and contains(@style,'--graph-block-order: %s;')]";
    private static final String GRAPH_NODE_SECTION_BY_NAME =
            "//div[text()='%s']/ancestor::div[@class='v-graph-node-details-section-v2']";
    private static final String GRAPH_NODE_GENERAL_INFO_SECTION =
            String.format(GRAPH_NODE_SECTION_BY_NAME, "General info");
    private static final String GRAPH_NODE_DIRECT_CONNECTIONS_SECTION =
            String.format(GRAPH_NODE_SECTION_BY_NAME, "Direct connections");
    private static final String GRAPH_NODE_VALUE_BY_TITLE =
            "/descendant::span[text()='%s']/ancestor::tr/descendant::div[@class='v-graph-node-details-attributes-table-v2__value']/descendant::*[contains(@class,'g-text')]";
    private static final String GRAPH_NODE_DIRECT_CONNECTION_BY_CLIENT_NAME =
            "/descendant::div[text()='%s']/ancestor::div[@class='v-graph-node-details-connection-data-v2']";
    private static final String GRAPH_NODE_GENERAL_INFO_VALUE_BY_TITLE =
            String.format("%s%s", GRAPH_NODE_GENERAL_INFO_SECTION, GRAPH_NODE_VALUE_BY_TITLE);
    private static final String GRAPH_NODE_DIRECT_CONNECTIONS_VALUE_BY_CLIENT_AND_TITLE = String.format(
            "%s%s%s",
            GRAPH_NODE_DIRECT_CONNECTIONS_SECTION,
            GRAPH_NODE_DIRECT_CONNECTION_BY_CLIENT_NAME,
            GRAPH_NODE_VALUE_BY_TITLE);
    private static final String GRAPH_NODE_TITLE_BY_UCID = "//div[@data-qa='connections__node__%s__title']";
    private static final String GRAPH_NODE_CONTAINER_BY_UCID =
            String.format("%s/ancestor::div[@class='graph-block-container undefined']", GRAPH_NODE_TITLE_BY_UCID);
    private static final String CARD_CLIENT_ATTRIBUTE_VALUE_BY_NAME =
            "//span[text()='%s']/ancestor::tr/descendant::div[@class='v-graph-node-details-attributes-table-v2__value']";
    private static final String CARD_BRAND_VALUE = String.format(CARD_CLIENT_ATTRIBUTE_VALUE_BY_NAME, "Brand");
    private static final String CARD_COUNTRY_VALUE = String.format(CARD_CLIENT_ATTRIBUTE_VALUE_BY_NAME, "Country");
    private static final String DIRECT_CONNECTION_ATTRIBUTE_BY_CLIENT_AND_ATTRIBUTE_NAME =
            "//div[contains(text(),'%s')]/ancestor::div[@class='v-graph-node-details-connection-data-v2']/descendant::span[text()='%s']/ancestor::tr/descendant::div[@class='v-graph-node-details-attributes-table-v2__value']";
    private static final String LINK_WITH_TEXT_PATTERN = "//a[text()='%s' and text()='%s']";
    private static final String FRAUD_SELECTION_OPTION_PATTERN =
            "//*[contains(@class,'v-drop-down-menu-2__content')]/div/div[text()='%s']";
    private static final String STATUS_SELECTION_OPTION_PATTERN =
            "//*[contains(@class, 'v-dropdown-select-item__sub-menu_isHovered')]//*[@data-qa='connections__report_fraud__fraud_type_selector__dropdown__item__submenu']//*[text()='%s']";
    private static final String FRAUD = "Fraud";
    private static final String CHECKBOX_BY_UCID_PATTERN =
            "//div[@data-qa='virtualized_table__rows__%s__checkbox']/descendant::input";

    public ConnectionPage(Page page) {
        super(page);
        this.connectionTab = page.locator("[role=\"tab\"][title=\"Connections\"]");
        this.tableViewButton = page.locator(CONNECTION_TABLE_BUTTON_SELECTOR);
        this.graphViewButton = page.locator("input[value='GRAPH']");
        this.levelHeader = page.locator(String.format(CONNECTION_TABLE_HEADER_BY_TEXT_PATTERN, "LVL"));
        this.clientHeader = page.locator(String.format(CONNECTION_TABLE_HEADER_BY_TEXT_PATTERN, "CLIENT"));
        this.connectionHeader = page.locator(String.format(CONNECTION_TABLE_HEADER_BY_TEXT_PATTERN, "CONNECTION"));
        this.attributeHeader = page.locator(String.format(CONNECTION_TABLE_HEADER_BY_TEXT_PATTERN, "ATTRIBUTE"));
        this.behaviorHeader = page.locator(String.format(CONNECTION_TABLE_HEADER_BY_TEXT_PATTERN, "BEHAVIOR"));
        this.pnlHeader = page.locator(String.format(CONNECTION_TABLE_HEADER_BY_TEXT_PATTERN, "TOTAL PNL"));
        this.operationHeader = page.locator(String.format(CONNECTION_TABLE_HEADER_BY_TEXT_PATTERN, "PAYMENTS"));
        this.ibHeader = page.locator(String.format(CONNECTION_TABLE_HEADER_BY_TEXT_PATTERN, "CPA/IB"));
        this.registeredHeader = page.locator(String.format(CONNECTION_TABLE_HEADER_BY_TEXT_PATTERN, "REGISTERED"));
        this.lastLoginHeader = page.locator(String.format(CONNECTION_TABLE_HEADER_BY_TEXT_PATTERN, "LAST LOGIN"));
        this.unmaskConnectionCardDataButton =
                page.locator("//div[@class='v-graph-node-details-header-v2__buttons']/button");
        this.connectionTableAttribute = page.locator("//div[@class='v-table-view-v2__attributes-list']/div");
        this.unmaskConnectionTableDataButton =
                page.locator("//div[contains(@class,'v-table-view-v2__custom-header-cell')]");
        this.filterButton = page.locator("//button[@data-qa='connections__filter_button__open']");
        this.filterOptionButton = page.locator("//span[@class='g-button__text']");
        this.filterCheckboxListOption = page.locator("//span[@class='g-control-label__text']/div");
        this.filterSliderRange = page.locator("//div[contains(@class,'g-text_ws_nowrap')]");
        this.filterSlider = page.locator("//div[@class='g-slider']");
        this.attributeFilterDropdown = page.locator(String.format(FILTER_CONTAINER_BY_TITLE_PATTERN, "Attribute"))
                .locator("//input");
        this.dropdownMenuItems = page.locator(
                "//div[@class='v-drop-down-menu__menu']/div/div/div[@class='v-drop-down-menu-item-base__text']");
        this.activeRestrictionsSwitch = page.locator(
                        String.format(FILTER_CONTAINER_BY_TITLE_PATTERN, "Active restrictions"))
                .locator("//input[@role='switch']/..");
        this.pnlFilterFromInput = page.locator(String.format(FILTER_CONTAINER_BY_TITLE_PATTERN, "PNL"))
                .locator("//input")
                .first();
        this.pnlFilterToInput = page.locator(String.format(FILTER_CONTAINER_BY_TITLE_PATTERN, "PNL"))
                .locator("//input")
                .last();
        this.lastLoginDatePicker = page.locator(String.format(FILTER_CONTAINER_BY_TITLE_PATTERN, "Last login"))
                .locator("//input");
        this.resetLevelButton = page.locator(String.format(RESET_BUTTON_BY_LABEL_PATTERN, "Level"));
        this.resetConnectionTypeButton = page.locator(String.format(RESET_BUTTON_BY_LABEL_PATTERN, "Connection type"));
        this.resetScoreToInitialButton = page.locator(String.format(RESET_BUTTON_BY_LABEL_PATTERN, "Score to initial"));
        this.resetAttributeButton = page.locator(String.format(RESET_BUTTON_BY_LABEL_PATTERN, "Attribute"));
        this.resetBehaviorButton = page.locator(String.format(RESET_BUTTON_BY_LABEL_PATTERN, "Behavior"));
        this.resetPnlButton = page.locator(String.format(RESET_BUTTON_BY_LABEL_PATTERN, "PNL"));
        this.resetLastLoginButton = page.locator(String.format(RESET_BUTTON_BY_LABEL_PATTERN, "Last login"));
        this.levelFilterButtons = page.locator(String.format(BUTTON_BY_LABEL_PATTERN, "Level"));
        this.connectionTypeFilterButtons = page.locator(String.format(CHECKBOX_BY_LABEL_PATTERN, "Connection type"));
        this.behaviorFilterButtons = page.locator(String.format(CHECKBOX_BY_LABEL_PATTERN, "Behavior"));
        this.attributesFilterSelectedItems = page.locator("//div[@class='v-drop-down-menu__checked-values']");
        this.resetAllButton = page.locator("//span[text()='Reset all']/parent::button");
        this.graphNodesUnhidden = page.locator("//div[contains(@class,'v-graph-node-v2__title-text')]");
        this.applyFiltersButton = page.locator("//button[@data-qa='connections__filter__apply']");
        this.connectionTableRow = page.locator(CONNECTION_TABLE_ROW);
        this.connectionTableUserIds = page.locator(
                String.format(
                        "%s%s",
                        CONNECTION_TABLE_ROW,
                        "/descendant::a[contains(@class,'client')]/descendant::span[contains(@class,'g-color-text_color_secondary')]"));
        this.appliedFilters = page.locator(
                "//div[@class='v-collapsible-horizontal-list__item']/descendant::div[@class='g-label__content']");
        this.filtersCounter = page.locator("//div[@class='v-connection-search-filter-button-v2__filters']/div");
        this.zoomInButton =
                page.locator(String.format("%s/button", ZOOM_CONTROLS)).first();
        this.zoomValue = page.locator(String.format("%s/div/descendant::span", ZOOM_CONTROLS));
        this.tooltip = page.locator("//div[@class='v-tooltip-content']");
        this.connectionScoreFilterPresetsOptions = page.locator(
                String.format("%s/descendant::span[@class='g-button__text']", CONNECTION_SCORE_FILTER_PRESETS));
        this.multiselectButton = page.locator("//span[contains(text(),'Multiselect')]/ancestor::button");
        this.multiselectSelectAllCheckbox =
                page.locator("//input[@type='checkbox']").first();
        this.multiselectCounter = page.locator("//div[@class='v-multiselect-panel__counter']");
        this.multiselectCommentButton =
                page.locator("//button[@data-qa='connections__multiselect_panel__toggle_comment']");
        this.multiselectCommentInput =
                page.locator("//div[@class='v-connection-search-multiselect-panel-v2__input']/descendant::input");
        this.multiselectAddCommentButton =
                page.locator("//div[@class='v-connection-search-multiselect-panel-v2__buttons']/button");
        this.graphNodeStatus =
                page.locator("//div[@class='v-graph-node-v2__status-line']/div[contains(@class,'g-text')]");
        this.graphNodeExpand = page.locator("//button[contains(@class,'v-graph-node-v2__expand-button')]");
        this.graphNodeAttributes = page.locator("//div[@class='v-graph-node-attributes-v2__attribute']");
        this.cardClientName = page.locator("//div[contains(@class,'v-graph-node-details-header-v2__client-name')]");
        this.cardClientId = page.locator(
                "//div[@class='v-graph-node-details-header-v2']/descendant::div[contains(@class,'g-color-text_color_secondary')]");
        this.cardConnectionLevel =
                page.locator("//div[@class='v-graph-node-details-header-v2__attributes']/descendant::span[1]");
        this.cardConnectionScore =
                page.locator("//div[@class='v-graph-node-details-header-v2__attributes']/descendant::span[2]");
        this.cardOpenInNewTabButton =
                page.locator("//div[@class='v-graph-node-details-header-v2__buttons']/descendant::a");
        this.valueIcon = page.locator("//*[name()='svg']");
        this.valueText = page.locator("//*[contains(@class,'g-text')]");
        this.cardBrandIcon = page.locator(CARD_BRAND_VALUE).locator(valueIcon);
        this.cardBrandText = page.locator(CARD_BRAND_VALUE).locator(valueText);
        this.cardCountryIcon = page.locator(CARD_COUNTRY_VALUE).locator(valueIcon);
        this.cardCountryText = page.locator(CARD_COUNTRY_VALUE).locator(valueText);
        this.cardEmail = page.locator(String.format(CARD_CLIENT_ATTRIBUTE_VALUE_BY_NAME, "Email"))
                .locator(valueText);
        this.cardCpa = page.locator(String.format(CARD_CLIENT_ATTRIBUTE_VALUE_BY_NAME, "CPA"))
                .locator(valueText);
        this.cardIb = page.locator(String.format(CARD_CLIENT_ATTRIBUTE_VALUE_BY_NAME, "IB"))
                .locator(valueText);
        this.cardRegistered = page.locator(String.format(CARD_CLIENT_ATTRIBUTE_VALUE_BY_NAME, "Registered"))
                .locator(valueText);
        this.cardLastLogin = page.locator(String.format(CARD_CLIENT_ATTRIBUTE_VALUE_BY_NAME, "Last login"))
                .locator(valueText);
        this.cardTrading = page.locator(String.format(CARD_CLIENT_ATTRIBUTE_VALUE_BY_NAME, "Trading"))
                .locator(valueText);
        this.cardTotalPnl = page.locator(String.format(CARD_CLIENT_ATTRIBUTE_VALUE_BY_NAME, "Total PNL"))
                .locator(valueText);
        this.cardDeposit = page.locator(String.format(CARD_CLIENT_ATTRIBUTE_VALUE_BY_NAME, "Deposit"))
                .locator(valueText);
        this.cardWithdrawal = page.locator(String.format(CARD_CLIENT_ATTRIBUTE_VALUE_BY_NAME, "Withdrawal"))
                .locator(valueText);
        this.cardFraud = page.locator(String.format(CARD_CLIENT_ATTRIBUTE_VALUE_BY_NAME, FRAUD))
                .first();
        this.directConnectionsAmount = page.locator("//div[text()='Direct connections']/../descendant::span");
        this.zoomOptions = page.locator("//span[@class='g-select-list__option-default-label']");
        this.cardAttributeName = page.locator("//div[@class='v-graph-attribute-details-v2__attribute-name']");
        this.cardAttributeClient = page.locator(
                "//div[@class='v-graph-attribute-details-v2__header']/div[contains(@class,'g-color-text_color_secondary')]");
        this.cardAttributeValue = page.locator("//div[@class='v-graph-node-details-section-v2__title']");
        this.connectionTableRowData =
                page.locator("//div[@class='v-body-cell']/descendant::*[contains(@class,'g-text')]");
        this.addFraudRestrictionsButton =
                page.locator("//button[@data-qa=\"connections__multiselect_panel__report_fraud\"]");
        this.addFraudRestrictionsDrawer =
                page.locator("//*[@data-qa='drawer_container']//*[text() = 'Apply fraud and restrictions']");
        this.addFraudRestrictionsComment =
                page.locator("//*[@data-qa=\"connections__report_fraud__comment_input\"]//textarea");
        this.addFraudRestrictionsSubmitButton = page.locator("//button[@data-qa=\"connections__report_fraud__apply\"]");
        this.fraudSelectButton =
                page.locator("[data-qa='connection_search_report_fraud_drawer__fraud_type_selector__anchor']");
        this.successToast = page.locator("//*[contains(@class, 'g-toast_theme_success')]");
        this.fraudTypes =
                page.locator("//div[@class='v-drop-down-menu-2__content']/descendant::div[contains(@class,'g-text')]");
    }

    @Step("Click connections tab")
    public void clickConnectionTabButton() {
        Allure.step("Click connections tab");
        connectionTab.click();
        waitForPageToLoad();
    }

    @Step("Open connection table")
    public void openConnectionTable() {
        Allure.step("Open connection table");
        page.waitForSelector(CONNECTION_TABLE_BUTTON_SELECTOR);
        tableViewButton.click();
        page.waitForSelector(CONNECTION_TABLE_SELECTOR);
        page.waitForTimeout(1000);
    }

    @Step("Verify connection table is rendered with all headers")
    public void verifyConnectionTableIsRendered() {
        page.waitForSelector(CONNECTION_TABLE_SELECTOR);
        String levelText = levelHeader.textContent();
        assertEquals("LVL", levelText);
        String clientText = clientHeader.textContent();
        assertEquals("CLIENT", clientText);
        String connectionText = connectionHeader.textContent();
        assertEquals("CONNECTION", connectionText);
        String attributeText = attributeHeader.textContent();
        assertEquals("ATTRIBUTE", attributeText);
        String behaviorText = behaviorHeader.textContent();
        assertEquals("BEHAVIOR", behaviorText);
        String pnlText = pnlHeader.textContent();
        assertEquals("TOTAL PNL", pnlText);
        String operationsText = operationHeader.textContent();
        assertEquals("PAYMENTS", operationsText);
        String ibText = ibHeader.textContent();
        assertEquals("CPA/IB", ibText);
        String registeredText = registeredHeader.textContent();
        assertEquals("REGISTERED", registeredText);
        String lastLoginText = lastLoginHeader.textContent();
        assertEquals("LAST LOGIN", lastLoginText);
    }

    public void openConnectionGraph() {
        Allure.step("Open connection graph");
        page.waitForSelector(CONNECTION_TABLE_BUTTON_SELECTOR);
        graphViewButton.click();
        page.waitForSelector(CONNECTION_GRAPH_SELECTOR);
    }

    public void checkDirectConnectionRows(String clientToName, String rowTitle, String expectedValue) {
        Allure.step(String.format("Check direct connection values, connect to user in field %s", rowTitle));
        assertThat(page.locator(
                        String.format(GRAPH_NODE_DIRECT_CONNECTIONS_VALUE_BY_CLIENT_AND_TITLE, clientToName, rowTitle)))
                .hasText(expectedValue);
    }

    public void checkGeneralInfoRows(String rowTitle, String expectedValue) {
        Allure.step(String.format("Check general data values, field %s", rowTitle));
        assertThat(page.locator(String.format(GRAPH_NODE_GENERAL_INFO_VALUE_BY_TITLE, rowTitle)))
                .hasText(expectedValue);
    }

    @Step("Click unmask button in card view")
    public void clickUnmaskConnectionCardDataButton() {
        unmaskConnectionCardDataButton.click();
        page.waitForCondition(() -> {
            String attributeValue =
                    (String) unmaskConnectionCardDataButton.evaluate("el => el.getAttribute('disabled')");
            return attributeValue == null; // Check if the attribute is no longer present
        });
    }

    @Step("Get attributes from connection table by client")
    public List<String> getConnectionTableAttributesList(ClientHelper client) {
        List<String> connectionTableAttributesList = new ArrayList<>();
        Locator attributes = page.locator(String.format(CONNECTION_TABLE_ROW_BY_CLIENT_ID_PATTERN, client.getUserId()))
                .locator(connectionTableAttribute);
        for (int i = 0; i < attributes.count(); i++) {
            connectionTableAttributesList.add(attributes.nth(i).textContent());
        }
        return connectionTableAttributesList;
    }

    @Step("Click unmask button in table view")
    public void clickUnmaskConnectionTableDataButton() {
        unmaskConnectionTableDataButton.click();
    }

    @Step("Click filter button")
    public void clickFilterButton() {
        filterButton.click();
        page.waitForTimeout(1000);
    }

    @Step("Wait until filter is visible")
    public void waitForFilterToLoad() {
        page.waitForSelector(
                FILTER_CONTAINER, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
    }

    @Step("Get list of options for Level filter")
    public List<String> getLevelFilterOptions() {
        waitForFilterToLoad();
        List<String> optionsList = new ArrayList<>();
        Locator options = page.locator(String.format(FILTER_CONTAINER_BY_TITLE_PATTERN, "Level"))
                .locator(filterOptionButton);
        for (int i = 0; i < options.count(); i++) {
            optionsList.add(options.nth(i).textContent());
        }
        return optionsList;
    }

    @Step("Get list of options for Connection type filter")
    public List<String> getConnectionTypeFilterOptions() {
        waitForFilterToLoad();
        List<String> optionsList = new ArrayList<>();
        Locator options = page.locator(String.format(FILTER_CONTAINER_BY_TITLE_PATTERN, "Connection type"))
                .locator(filterCheckboxListOption);
        for (int i = 0; i < options.count(); i++) {
            optionsList.add(options.nth(i).textContent());
        }
        return optionsList;
    }

    @Step("Get current range for Score to initial filter")
    public String getScoreToInitialFilterCurrentRange() {
        waitForFilterToLoad();
        return page.locator(String.format(FILTER_CONTAINER_BY_TITLE_PATTERN, "Score to initial"))
                .locator(filterSliderRange)
                .textContent();
    }

    @Step("Is Score to initial filter slider visible")
    public Boolean isScoreToInitialFilterSliderVisible() {
        waitForFilterToLoad();
        return page.locator(String.format(FILTER_CONTAINER_BY_TITLE_PATTERN, "Score to initial"))
                .locator(filterSliderRange)
                .isVisible();
    }

    @Step("Get Attribute filter dropdown placeholder")
    public String getAttributeFilterDropdownPlaceholder() {
        waitForFilterToLoad();
        return attributeFilterDropdown.getAttribute("placeholder");
    }

    @Step("Click Attribute filter dropdown")
    public void clickAttributeFilterDropdown() {
        waitForFilterToLoad();
        attributeFilterDropdown.click();
    }

    @Step("Get Attribute filter dropdown options")
    public List<String> getAttributeFilterDropdownOptions() {
        waitForFilterToLoad();
        List<String> optionsList = new ArrayList<>();
        for (int i = 0; i < dropdownMenuItems.count(); i++) {
            optionsList.add(dropdownMenuItems.nth(i).textContent());
        }
        return optionsList;
    }

    @Step("Get list of options for Behavior filter")
    public List<String> getBehaviorFilterOptions() {
        waitForFilterToLoad();
        List<String> optionsList = new ArrayList<>();
        Locator options = page.locator(String.format(FILTER_CONTAINER_BY_TITLE_PATTERN, "Behavior"))
                .locator(filterCheckboxListOption);
        for (int i = 0; i < options.count(); i++) {
            optionsList.add(options.nth(i).textContent());
        }
        return optionsList;
    }

    @Step("Is Active restrictions switch visible")
    public Boolean isActiveRestrictionsFilterSwitchVisible() {
        waitForFilterToLoad();
        return activeRestrictionsSwitch.isVisible();
    }

    @Step("Get PNL filter from placeholder")
    public String getPnlFilterFromPlaceholder() {
        waitForFilterToLoad();
        return pnlFilterFromInput.getAttribute("placeholder");
    }

    @Step("Get PNL filter to placeholder")
    public String getPnlFilterToPlaceholder() {
        waitForFilterToLoad();
        return pnlFilterToInput.getAttribute("placeholder");
    }

    @Step("Verify list of options for Last login filter")
    public void verifyPresetOptionsLastLoginFilter() {
        waitForFilterToLoad();
        String lastLogin = "Last login";
        page.locator(String.format(PRESET_BY_LABEL_AND_VALUE_PATTERN, lastLogin, "Today"))
                .click();
        assertThat(lastLoginDatePicker).hasValue(Utils.getCurrentDate());
        page.locator(String.format(PRESET_BY_LABEL_AND_VALUE_PATTERN, lastLogin, "Yesterday"))
                .click();
        assertThat(lastLoginDatePicker).hasValue(Utils.getYesterdayDate());
        page.locator(String.format(PRESET_BY_LABEL_AND_VALUE_PATTERN, lastLogin, "Last 7 days"))
                .click();
        assertThat(lastLoginDatePicker)
                .hasValue(String.format("%s to %s", Utils.getPreviousWeekDate(), Utils.getCurrentDate()));
        page.locator(String.format(PRESET_BY_LABEL_AND_VALUE_PATTERN, lastLogin, "Last 14 days"))
                .click();
        assertThat(lastLoginDatePicker)
                .hasValue(String.format("%s to %s", getPrevious14DaysDate(), Utils.getCurrentDate()));
        page.locator(String.format(PRESET_BY_LABEL_AND_VALUE_PATTERN, lastLogin, "Last 30 days"))
                .click();
        assertThat(lastLoginDatePicker)
                .hasValue(String.format("%s to %s", getPrevious30DaysDate(), Utils.getCurrentDate()));
        page.locator(String.format(PRESET_BY_LABEL_AND_VALUE_PATTERN, lastLogin, "Last 90 days"))
                .click();
        assertThat(lastLoginDatePicker)
                .hasValue(String.format("%s to %s", getPrevious90DaysDateUtc(), Utils.getCurrentDate()));
    }

    @Step("Select Level filter option")
    public void selectLevelFilterOption(String option) {
        waitForFilterToLoad();
        page.locator(String.format(PRESET_BY_LABEL_AND_VALUE_PATTERN, "Level", option))
                .click();
    }

    @Step("Select Connection type filter option")
    public void selectConnectionTypeFilterOption(String option) {
        waitForFilterToLoad();
        page.locator(String.format(CHECKBOX_BY_LABEL_AND_VALUE_PATTERN, "Connection type", option))
                .click();
    }

    @Step("Select Behavior filter option")
    public void selectBehaviorFilterOption(String option) {
        waitForFilterToLoad();
        page.locator(String.format(CHECKBOX_BY_LABEL_AND_VALUE_PATTERN, "Behavior", option))
                .click();
    }

    @Step("Click active restrictions switch")
    public void clickActiveRestrictionsSwitch() {
        waitForFilterToLoad();
        activeRestrictionsSwitch.click();
    }

    @Step("Fill PNL from input")
    public void fillPnlFromInput(String text) {
        waitForFilterToLoad();
        pnlFilterFromInput.fill(text);
    }

    @Step("Fill PNL to input")
    public void fillPnlToInput(String text) {
        waitForFilterToLoad();
        pnlFilterToInput.fill(text);
    }

    @Step("Select Last login from datepicker")
    public void selectLastLogin(String from, String to) {
        waitForFilterToLoad();
        selectDateRangeInElement(lastLoginDatePicker, from, to);
    }

    public void verifyNoLevelIsSelected() {
        waitForFilterToLoad();
        for (int i = 0; i < levelFilterButtons.count(); i++) {
            Locator button = levelFilterButtons.nth(i);
            MatcherAssert.assertThat(
                    "Assert that each level button is not selected",
                    button.getAttribute("class"),
                    not(containsString("action")));
        }
    }

    public void verifyNoConnectionTypeIsSelected() {
        waitForFilterToLoad();
        for (int i = 0; i < connectionTypeFilterButtons.count(); i++) {
            Locator checkbox = connectionTypeFilterButtons.nth(i);
            MatcherAssert.assertThat(
                    "Assert that each Connection type checkbox is not selected",
                    checkbox.isChecked(),
                    not(equalTo(true)));
        }
    }

    public void verifyNoBehaviorIsSelected() {
        waitForFilterToLoad();
        for (int i = 0; i < behaviorFilterButtons.count(); i++) {
            Locator checkbox = behaviorFilterButtons.nth(i);
            MatcherAssert.assertThat(
                    "Assert that each Behavior checkbox is not selected", checkbox.isChecked(), not(equalTo(true)));
        }
    }

    @Step("Select Score to initial range")
    public void selectScoreToInitialRange() {
        waitForFilterToLoad();
        filterSlider.locator("//div[@role='slider']").first().dragTo(filterSlider);
    }

    public void verifyNoScoreToInitialIsSelected(String expectedText) {
        waitForFilterToLoad();
        MatcherAssert.assertThat(getScoreToInitialFilterCurrentRange(), equalTo(expectedText));
    }

    @Step("Select Score to initial range")
    public void selectAttribute(String attribute, String value) {
        waitForFilterToLoad();
        clickAttributeFilterDropdown();
        page.locator(String.format(ATTRIBUTE_FILTER_NAME_PATTERN, attribute))
                .first()
                .hover();
        page.locator(String.format(ATTRIBUTE_FILTER_VALUE_PATTERN, attribute, value))
                .click();
    }

    public void verifyNoAttributesAreSelected() {
        waitForFilterToLoad();
        MatcherAssert.assertThat(attributesFilterSelectedItems.isVisible(), equalTo(false));
    }

    public void verifyNoActiveRestrictionsAreSelected() {
        waitForFilterToLoad();
        MatcherAssert.assertThat(activeRestrictionsSwitch.locator("//input").isChecked(), equalTo(false));
    }

    public void verifyNoPnlFilterValuesAreSelected() {
        waitForFilterToLoad();
        assertThat(pnlFilterFromInput).hasValue("");
        assertThat(pnlFilterToInput).hasValue("");
    }

    public void verifyNoLastLoginValueIsSelected() {
        waitForFilterToLoad();
        assertThat(lastLoginDatePicker).hasValue("");
    }

    @Step("Press reset button for all filters and verify that none are selected")
    public void resetAllFiltersAndVerify(String expectedDefaultRange) {
        resetAllButton.click();
        verifyNoLevelIsSelected();
        verifyNoConnectionTypeIsSelected();
        verifyNoScoreToInitialIsSelected(expectedDefaultRange);
        verifyNoAttributesAreSelected();
        verifyNoBehaviorIsSelected();
        verifyNoActiveRestrictionsAreSelected();
        verifyNoPnlFilterValuesAreSelected();
        verifyNoLastLoginValueIsSelected();
    }

    @Step("Press reset Level button and verify that none are selected")
    public void resetLevelAndVerify() {
        resetLevelButton.click();
        verifyNoLevelIsSelected();
    }

    @Step("Press reset Connection type button and verify that none are selected")
    public void resetConnectionTypeAndVerify() {
        resetConnectionTypeButton.click();
        verifyNoConnectionTypeIsSelected();
    }

    @Step("Press reset Score to initial button and verify that none are selected")
    public void resetScoreToInitialAndVerify(String expectedDefaultRange) {
        resetScoreToInitialButton.click();
        verifyNoScoreToInitialIsSelected(expectedDefaultRange);
        verifyNoAttributesAreSelected();
        verifyNoBehaviorIsSelected();
        verifyNoActiveRestrictionsAreSelected();
        verifyNoPnlFilterValuesAreSelected();
        verifyNoLastLoginValueIsSelected();
    }

    @Step("Press reset Attributes button and verify that none are selected")
    public void resetAttributesAndVerify() {
        resetAttributeButton.click();
        verifyNoAttributesAreSelected();
    }

    @Step("Press reset Behavior button and verify that none are selected")
    public void resetBehaviorAndVerify() {
        resetBehaviorButton.click();
        verifyNoBehaviorIsSelected();
    }

    @Step("Press reset PNL button and verify that none are selected")
    public void resetPnlAndVerify() {
        resetPnlButton.click();
        verifyNoPnlFilterValuesAreSelected();
    }

    @Step("Press reset Last login button and verify that none are selected")
    public void resetLastLoginAndVerify() {
        resetLastLoginButton.click();
        verifyNoLastLoginValueIsSelected();
    }

    @Step("Get all unhidden nodes names")
    public List<String> getAllUnhiddenNodesNames() {
        List<String> namesList = new ArrayList<>();
        for (int i = 0; i < graphNodesUnhidden.count(); i++) {
            Locator node = graphNodesUnhidden.nth(i);
            namesList.add(node.textContent().trim());
        }
        return namesList;
    }

    @Step("Click apply filters button")
    public void clickApplyFiltersButton() {
        applyFiltersButton.click();
        page.waitForTimeout(1000);
    }

    @Step("Get connection table row count")
    public int getConnectionTableRowCount() {
        return connectionTableRow.count();
    }

    @Step("Get connection table user ids list")
    public List<String> getConnectionTableUserIdsList() {
        List<String> idsList = new ArrayList<>();
        for (int i = 0; i < connectionTableUserIds.count(); i++) {
            Locator userId = connectionTableUserIds.nth(i);
            idsList.add(userId.textContent());
        }
        return idsList;
    }

    @Step("Get list of applied filters")
    public List<String> getAppliedFiltersList() {
        List<String> filtersList = new ArrayList<>();
        for (int i = 0; i < appliedFilters.count(); i++) {
            Locator filter = appliedFilters.nth(i);
            filtersList.add(filter.textContent());
        }
        return filtersList;
    }

    @Step("Get applied filters count")
    public String getAppliedFiltersCount() {
        return filtersCounter.textContent();
    }

    @Step("Click zoom in button")
    public void clickZoomInButton() {
        zoomInButton.click();
    }

    @Step("Get zoom value")
    public String getZoomValue() {
        return zoomValue.textContent();
    }

    @Step("Click zoom value")
    public void clickZoomValue() {
        zoomValue.click();
    }

    @Step("Get pnl sorting tooltip")
    public String getTotalPnlSortingTooltip() {
        pnlHeader.hover();
        return tooltip.textContent();
    }

    @Step("Get last login sorting tooltip")
    public String getLastLoginSortingTooltip() {
        lastLoginHeader.hover();
        return tooltip.textContent();
    }

    @Step("Click total pnl header")
    public void clickTotalPnlHeader() {
        pnlHeader.click();
    }

    @Step("Click last login header")
    public void clickLastLoginHeader() {
        lastLoginHeader.click();
    }

    @Step("Get connection score filter presets text")
    public List<String> getConnectionScoreFilterPresets() {
        List<String> textList = new ArrayList<>();
        for (int i = 0; i < connectionScoreFilterPresetsOptions.count(); i++) {
            Locator preset = connectionScoreFilterPresetsOptions.nth(i);
            textList.add(preset.textContent());
        }
        return textList;
    }

    @Step("Click connection score filter preset")
    public void clickConnectionScoreFilterPresetByText(String preset) {
        page.locator(String.format(CONNECTION_SCORE_FILTER_PRESET_BY_TEXT_PATTERN, preset))
                .click();
    }

    @Step("Click multiselect button")
    public void clickMultiselectButton() {
        multiselectButton.click();
    }

    @Step("Click multiselect select all checkbox")
    public void clickMultiselectSelectAllCheckbox() {
        multiselectSelectAllCheckbox.click();
    }

    @Step("Get multiselect counter text")
    public String getMultiselectCounterText() {
        return multiselectCounter.textContent();
    }

    @Step("Click multiselect comment button")
    public void clickMultiselectCommentButton() {
        multiselectCommentButton.click();
    }

    @Step("Fill multiselect comment")
    public void fillMultiselectComment(String text) {
        multiselectCommentInput.fill(text);
    }

    @Step("Click multiselect add comment button")
    public void clickMultiselectAddCommentButton() {
        multiselectAddCommentButton.click();
    }

    @Step("Click connection node by order")
    public void clickConnectionNodeByOrder(int order) {
        page.locator(String.format(GRAPH_NODE_BY_ORDER, order)).click();
    }

    @Step("Get status by node title")
    public String getStatusByNodeUcid(String ucid) {
        return page.locator(String.format(GRAPH_NODE_CONTAINER_BY_UCID, ucid))
                .locator(graphNodeStatus)
                .textContent();
    }

    @Step("Get order by node title")
    public String getOrderByNodeUcid(String ucid) {
        return page.locator(String.format(GRAPH_NODE_CONTAINER_BY_UCID, ucid))
                .evaluate("el => getComputedStyle(el).getPropertyValue('--graph-block-order')")
                .toString();
    }

    @Step("Click expand node by title")
    public void clickExpandNodeByUcid(String ucid) {
        page.locator(String.format(GRAPH_NODE_CONTAINER_BY_UCID, ucid))
                .locator(graphNodeExpand)
                .click();
    }

    @Step("Get node attributes by title")
    public List<String> getNodeAttributesByUcid(String ucid) {
        Locator attributes =
                page.locator(String.format(GRAPH_NODE_CONTAINER_BY_UCID, ucid)).locator(graphNodeAttributes);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < attributes.count(); i++) {
            list.add(attributes.nth(i).textContent());
        }
        return list;
    }

    @Step("Click node by title")
    public void clickNodeByUcid(String ucid) {
        page.locator(String.format(GRAPH_NODE_TITLE_BY_UCID, ucid)).click();
    }

    @Step("Get card client name")
    public String getCardClientName() {
        return cardClientName.textContent();
    }

    @Step("Get card client id")
    public String getCardClientId() {
        return cardClientId.textContent();
    }

    @Step("Get card connection level")
    public String getCardConnectionLevel() {
        return cardConnectionLevel.textContent();
    }

    @Step("Get card connection score")
    public String getCardConnectionScore() {
        return cardConnectionScore.textContent();
    }

    @Step("Is card show hidden button visible")
    public Boolean isCardShowHiddenButtonVisible() {
        return unmaskConnectionCardDataButton.isVisible();
    }

    @Step("Is card open in new tab button visible")
    public Boolean isCardOpenInNewTabButtonVisible() {
        return cardOpenInNewTabButton.isVisible();
    }

    @Step("Is card open in new tab button visible")
    public String getCardOpenInNewTabUrl() {
        return cardOpenInNewTabButton.getAttribute("href");
    }

    @Step("Is card Brand icon visible")
    public Boolean isCardBrandIconVisible() {
        return cardBrandIcon.isVisible();
    }

    @Step("Get card Brand")
    public String getCardBrand() {
        return cardBrandText.textContent();
    }

    @Step("Is card Country icon visible")
    public Boolean isCardCountryIconVisible() {
        return cardCountryIcon.isVisible();
    }

    @Step("Get card Country")
    public String getCardCountry() {
        return cardCountryText.textContent();
    }

    @Step("Get card Email")
    public String getCardEmail() {
        return cardEmail.textContent();
    }

    @Step("Get card CPA")
    public String getCardCpa() {
        return cardCpa.textContent();
    }

    @Step("Click card CPA")
    public void clickCardCpa() {
        cardCpa.click();
    }

    @Step("Get card IB")
    public String getCardIb() {
        return cardIb.textContent();
    }

    @Step("Click card IB")
    public void clickCardIb() {
        cardIb.click();
    }

    @Step("Get card Registered")
    public String getCardRegistered() {
        return cardRegistered.textContent();
    }

    @Step("Get card Last login")
    public String getCardLastLogin() {
        return cardLastLogin.textContent();
    }

    @Step("Get card Trading")
    public String getCardTrading() {
        return cardTrading.textContent();
    }

    @Step("Get card Total PNL")
    public String getCardTotalPnl() {
        return cardTotalPnl.textContent();
    }

    @Step("Get card Deposit")
    public String getCardDeposit() {
        return cardDeposit.textContent();
    }

    @Step("Get card Withdrawal")
    public String getCardWithdrawal() {
        return cardWithdrawal.textContent();
    }

    @Step("Get card Fraud")
    public String getCardFraud() {
        return cardFraud.textContent();
    }

    @Step("Get card direct connections amount")
    public String getDirectConnectionsAmount() {
        return directConnectionsAmount.textContent();
    }

    @Step("Get card direct connection Type")
    public String getDirectConnectionType(String clientName) {
        return page.locator(String.format(DIRECT_CONNECTION_ATTRIBUTE_BY_CLIENT_AND_ATTRIBUTE_NAME, clientName, "Type"))
                .textContent();
    }

    @Step("Get card direct connection Score")
    public String getDirectConnectionScore(String clientName) {
        return page.locator(
                        String.format(DIRECT_CONNECTION_ATTRIBUTE_BY_CLIENT_AND_ATTRIBUTE_NAME, clientName, "Score"))
                .textContent();
    }

    @Step("Get card direct connection payoutId")
    public String getDirectConnectionPayoutId(String clientName) {
        return page.locator(
                        String.format(DIRECT_CONNECTION_ATTRIBUTE_BY_CLIENT_AND_ATTRIBUTE_NAME, clientName, "payoutId"))
                .textContent();
    }

    @Step("Get card direct connection emailAddress")
    public String getDirectConnectionEmailAddress(String clientName) {
        return page.locator(String.format(
                        DIRECT_CONNECTION_ATTRIBUTE_BY_CLIENT_AND_ATTRIBUTE_NAME, clientName, "emailAddress"))
                .textContent();
    }

    @Step("Get card direct connection digital")
    public String getDirectConnectionDigital(String clientName) {
        return page.locator(
                        String.format(DIRECT_CONNECTION_ATTRIBUTE_BY_CLIENT_AND_ATTRIBUTE_NAME, clientName, "digital"))
                .textContent();
    }

    @Step("Get card direct connection session")
    public String getDirectConnectionSession(String clientName) {
        return page.locator(
                        String.format(DIRECT_CONNECTION_ATTRIBUTE_BY_CLIENT_AND_ATTRIBUTE_NAME, clientName, "session"))
                .textContent();
    }

    @Step("Get card direct connection phoneNumber")
    public String getDirectConnectionPhoneNumber(String clientName) {
        return page.locator(String.format(
                        DIRECT_CONNECTION_ATTRIBUTE_BY_CLIENT_AND_ATTRIBUTE_NAME, clientName, "phoneNumber"))
                .textContent();
    }

    @Step("Get card direct connection nameBirth")
    public String getDirectConnectionNameBirth(String clientName) {
        return page.locator(String.format(
                        DIRECT_CONNECTION_ATTRIBUTE_BY_CLIENT_AND_ATTRIBUTE_NAME, clientName, "nameBirth"))
                .textContent();
    }

    @Step("Get card direct connection documentNumber")
    public String getDirectConnectionDocumentNumber(String clientName) {
        return page.locator(String.format(
                        DIRECT_CONNECTION_ATTRIBUTE_BY_CLIENT_AND_ATTRIBUTE_NAME, clientName, "documentNumber"))
                .textContent();
    }

    @Step("Get card direct connection ipAddress")
    public String getDirectConnectionIpAddress(String clientName) {
        return page.locator(String.format(
                        DIRECT_CONNECTION_ATTRIBUTE_BY_CLIENT_AND_ATTRIBUTE_NAME, clientName, "ipAddress"))
                .textContent();
    }

    @Step("Get card direct connection device")
    public String getDirectConnectionDevice(String clientName) {
        return page.locator(
                        String.format(DIRECT_CONNECTION_ATTRIBUTE_BY_CLIENT_AND_ATTRIBUTE_NAME, clientName, "device"))
                .textContent();
    }

    @Step("Get card direct connection Fraud")
    public String getDirectConnectionFraud(String clientName) {
        return page.locator(String.format(DIRECT_CONNECTION_ATTRIBUTE_BY_CLIENT_AND_ATTRIBUTE_NAME, clientName, FRAUD))
                .textContent();
    }

    @Step("Get zoom preset options")
    public List<String> getZoomPresetOptions() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < zoomOptions.count(); i++) {
            list.add(zoomOptions.nth(i).textContent());
        }
        return list;
    }

    @Step("Click node attribute by name")
    public void clickNodeAttributeByName(String ucid, String attribute) {
        page.locator(String.format(GRAPH_NODE_CONTAINER_BY_UCID, ucid))
                .locator(graphNodeAttributes)
                .getByText(attribute)
                .click();
    }

    @Step("Get connection card attribute name")
    public String getConnectionCardAttributeName() {
        return cardAttributeName.textContent();
    }

    @Step("Get connection card attribute client")
    public String getConnectionCardAttributeClient() {
        return cardAttributeClient.textContent();
    }

    @Step("Get connection card attribute value")
    public String getConnectionCardAttributeValue() {
        return cardAttributeValue.textContent();
    }

    @Step("Get connection card Match")
    public String getConnectionMatch(String clientName) {
        return page.locator(
                        String.format(DIRECT_CONNECTION_ATTRIBUTE_BY_CLIENT_AND_ATTRIBUTE_NAME, clientName, "Match"))
                .textContent();
    }

    @Step("Get connection card Value")
    public String getConnectionValue(String clientName) {
        return page.locator(
                        String.format(DIRECT_CONNECTION_ATTRIBUTE_BY_CLIENT_AND_ATTRIBUTE_NAME, clientName, "Value"))
                .textContent();
    }

    @Step("Get connection card Fraud")
    public String getConnectionFraud(String clientName) {
        return page.locator(String.format(DIRECT_CONNECTION_ATTRIBUTE_BY_CLIENT_AND_ATTRIBUTE_NAME, clientName, FRAUD))
                .textContent();
    }

    @Step("Get connection table data by rows")
    public List<List<String>> getConnectionTableDataByRows() {
        List<List<String>> list = new ArrayList<>();
        for (int i = 0; i < connectionTableRow.count(); i++) {
            Locator row = connectionTableRow.nth(i);
            List<String> rowDataList = new ArrayList<>();
            for (int k = 0; k < row.locator(connectionTableRowData).count(); k++) {
                rowDataList.add(row.locator(connectionTableRowData).nth(k).textContent());
            }
            list.add(rowDataList);
        }
        return list;
    }

    @Step("Click CPA in connection table")
    public void clickTableCpa(String cpaId) {
        page.locator(String.format(LINK_WITH_TEXT_PATTERN, "CPA", cpaId)).click();
    }

    @Step("Click IB in connection table")
    public void clickTableIb(String ibId) {
        page.locator(String.format(LINK_WITH_TEXT_PATTERN, "IB", ibId)).click();
    }

    public void isConnectionsTabHidden() {
        Allure.step("check is connections tab hidden");
        waitForPageToLoad();
        connectionTab.waitFor(new Locator.WaitForOptions().setState(HIDDEN));
        assertFalse(connectionTab.isVisible());
    }

    public void isConnectionsTabVisible() {
        Allure.step("check is connections tab visible");
        waitForPageToLoad();
        connectionTab.waitFor(new Locator.WaitForOptions().setState(VISIBLE));
    }

    public void isMultiselectButtonHidden() {
        Allure.step("check is multiselect button hidden");
        waitForPageToLoad();
        multiselectButton.waitFor(new Locator.WaitForOptions().setState(HIDDEN));
        assertFalse(multiselectButton.isVisible());
    }

    public void isMultiselectButtonVisible() {
        Allure.step("check is multiselect button visible");
        waitForPageToLoad();
        multiselectButton.waitFor(new Locator.WaitForOptions().setState(VISIBLE));
        assertTrue(multiselectButton.isVisible());
    }

    public void navigate(String ucid) {
        Allure.step("Navigate to connections tab");
        page.navigate(String.format("%sinvestigation/%s/%s", BASE_URL_E2E, ucid, "connections"));
        waitForPageToLoad();
    }

    public void openFraudRestrictionsForm() {
        Allure.step("Open fom for managing frauds and restriction");
        addFraudRestrictionsButton.click();
        addFraudRestrictionsDrawer.waitFor(new Locator.WaitForOptions().setState(VISIBLE));
    }

    public void addFraud(String fraud, String status, String comment) {
        Allure.step("Add and submit fraud");
        fraudSelectButton.click();
        String element = String.format(FRAUD_SELECTION_OPTION_PATTERN, fraud);
        page.locator(element).hover();
        page.locator(element).hover();
        String subelement = "//*[contains(@class, 'v-sub-menu__content')]//div[text()='" + status + "']";
        page.locator(subelement).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        page.locator(subelement).click();
        addFraudRestrictionsComment.fill(comment);
        addFraudRestrictionsSubmitButton.click();
    }

    public void verifySuccessMessageUpload(int deductionsCount) {
        successToast.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        String s = successToast.textContent();
        MatcherAssert.assertThat(s, containsString("Request received"));
        MatcherAssert.assertThat(
                s, containsString(String.format("%d deductions were created automatically", deductionsCount)));
    }

    public void addFraud(String fraud) {
        addFraud(fraud, "Confirmed", "comment" + getCurrentTimestampSeconds());
    }

    @Step("Get list of visible fraud types in connection search bulk operations")
    public List<String> getFraudTypesList() {
        fraudSelectButton.click();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < fraudTypes.count(); i++) {
            list.add(fraudTypes.nth(i).textContent());
        }
        fraudSelectButton.click();
        return list;
    }

    @Step("Click multiselect checkbox by ucid")
    public void clickMultiselectCheckboxByUcid(String ucid) {
        page.locator(String.format(CHECKBOX_BY_UCID_PATTERN, ucid)).click();
    }
}
