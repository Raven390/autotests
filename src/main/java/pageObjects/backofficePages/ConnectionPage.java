package pageObjects.backofficePages;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.microsoft.playwright.options.ElementState;
import helpers.data.ClientHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.hamcrest.MatcherAssert;
import utils.Utils;

import java.util.*;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static utils.ConfigFactory.BASE_URL_E2E;
import static utils.Utils.*;

public class ConnectionPage extends AbstractPage {

    private final Locator loaderAnimation;
    private final Locator connectionTab;
    private final Locator tableViewButton;
    private final Locator graphViewButton;
    private final Locator connectionTable;
    private final Locator connectionGraph;
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
    private final Locator graphLinkHeader;
    private final Locator levelCell;
    private final Locator clientCell;
    private final Locator connectionCell;
    private final Locator attributeCell;
    private final Locator behaviorCell;
    private final Locator pnlCell;
    private final Locator operationCell;
    private final Locator ibCell;
    private final Locator registeredCell;
    private final Locator lastLoginCell;
    private final Locator graphLinkCell;
    private final Locator connectionCardSwitch;
    private final Locator connectionCard;
    private final Locator connectionCardLink;
    private final Locator unmaskConnectionCardDataButton;
    private final Locator connectionTableAttribute;
    private final Locator unmaskConnectionTableDataButton;
    private final Locator unmaskAttributeCardDataButton;
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
    private final Locator graphNodesHiddenTitles;
    private final Locator applyFiltersButton;
    private final Locator connectionTableRow;
    private final Locator connectionTableUserIds;
    private final Locator appliedFilters;
    private final Locator filtersCounter;
    private final Locator appliedFiltersHidden;
    private final Locator connectionTableBehaviorValue;
    private final Locator connectionTableScoreValue;
    private final Locator zoomInButton;
    private final Locator zoomValue;

    private final String CONNECTION_TABLE_BUTTON_SELECTOR = "input[value='TABLE']";
    private final String CONNECTION_TABLE_SELECTOR = ".v-connection-search-table";
    private final String CONNECTION_GRAPH_SELECTOR = ".v-connection-search-graph";
    private final String LEVEL_CELL_SELECTOR = "td.v-connection-search-table-view__column_type_level";
    private final String CONNECTION_CELL_SELECTOR = "td.v-connection-search-table-view__column_type_connection";
    private final String GRAPH_LINK_CELL_SELECTOR = "td.v-connection-search-table-view__column_type_graph-link";
    private final String CONNECTION_NODE_SELECTOR = ".v-graph-node";
    private final String CONNECTION_CARD_SWITCH_ON_SELECTOR = "//div[@class='v-connection-search-graph__right-side-controls']/button[contains(@class, 'g-button_selected')]";
    private final String CONNECTION_CARD_SWITCH_SELECTOR = "//div[@class='v-connection-search-graph__right-side-controls']/button";
    private final String CONNECTION_CARD_SELECTOR = "div.v-graph-node-details";
    private final String CONNECTION_CARD_LINK_SELECTOR = "//div[contains(@class, 'v-graph-node-details')]/div[@class='v-graph-node-details-header']//a/button";
    private final String CONNECTION_CARD_UNMASK_GENERAL_SELECTOR = "//*[contains(text(), 'General info')]/ancestor::div[@class='v-graph-node-details__content']//button[1]";
    private final String CONNECTION_CARD_HEADER_ID_SELECTOR = "//div[@class='v-graph-node-details-header']//div[contains(@class, 'g-color-text_color_secondary')]";
    private final String CONNECTION_CARD_HEADER_NAME_SELECTOR = "//div[@class='v-graph-node-details-header']//div[contains(@class, 'v-graph-node-details-header__client-name')]";
    private final String CONNECTION_CARD_HEADER_LEVEL_SELECTOR = "//div[@class='v-graph-node-details-header']//div[@class='v-graph-node-details-header__attributes']/div[1]//span";
    private final String CONNECTION_CARD_HEADER_POINTS_SELECTOR = "//div[@class='v-graph-node-details-header']//div[@class='v-graph-node-details-header__attributes']/div[2]//span";
    private final String CONNECTION_TABLE_ROW_BY_CLIENT_ID_PATTERN = "//div[text()='%s']/ancestor::tr";
    private final String MASKED_TEXT_LOCATOR = "//div[contains(text(), '***')]";
    private static final String FILTER_CONTAINER_BY_TITLE_PATTERN = "//div[@class='v-text-with-icon__text' and text()='%s']/ancestor::div[@class='v-filter-container']";
    private static final String FILTER_CONTAINER = "//div[@class='v-drawer-content-wrapper__content']";
    private static final String PRESET_BY_LABEL_AND_VALUE_PATTERN = FILTER_CONTAINER_BY_TITLE_PATTERN + "/descendant::span[text()='%s']";
    private static final String CHECKBOX_BY_LABEL_AND_VALUE_PATTERN = FILTER_CONTAINER_BY_TITLE_PATTERN + "/descendant::div[text()='%s']/ancestor::div[@class='v-checkbox-list__item']/descendant::input[@type='checkbox']";
    private static final String RESET_BUTTON_BY_LABEL_PATTERN = FILTER_CONTAINER_BY_TITLE_PATTERN + "/descendant::span[text()='Reset']";
    private static final String CHECKBOX_BY_LABEL_PATTERN = FILTER_CONTAINER_BY_TITLE_PATTERN + "/descendant::input[@type='checkbox']";
    private static final String BUTTON_BY_LABEL_PATTERN = FILTER_CONTAINER_BY_TITLE_PATTERN + "/descendant::button";
    private static final String ATTRIBUTE_FILTER_NAME_PATTERN = "//div[@class='v-drop-down-menu__menu']/descendant::div[text()='%s']";
    private static final String ATTRIBUTE_FILTER_VALUE_PATTERN = "//div[contains(@data-dd-value,'%s')]/descendant::div[text()='%s']";
    private static final String GRAPH_NODES_GROUP = "//div[@class='v-connection-search-graph-view__group']";
    private static final String CONNECTION_TABLE_ROW = "//tbody/tr";
    private static final String ZOOM_CONTROLS = "//div[@class='v-graph-scale-controls__zoom-controls']";

    public ConnectionPage(Page page) {
        super(page);
        this.loaderAnimation = page.locator(".v-loader");
        this.connectionTab = page.locator("[role=\"tab\"][title=\"Connections\"]");
        this.tableViewButton = page.locator(CONNECTION_TABLE_BUTTON_SELECTOR);
        this.graphViewButton = page.locator("input[value='GRAPH']");
        this.connectionTable = page.locator(CONNECTION_TABLE_SELECTOR);
        this.connectionGraph = page.locator(CONNECTION_GRAPH_SELECTOR);
        this.levelHeader = page.locator("th.v-connection-search-table-view__column_type_level");
        this.clientHeader = page.locator("th.v-connection-search-table-view__column_type_client");
        this.connectionHeader = page.locator("th.v-connection-search-table-view__column_type_connection");
        this.attributeHeader = page.locator("th.v-connection-search-table-view__column_type_attribute");
        this.behaviorHeader = page.locator("th.v-connection-search-table-view__column_type_behavior");
        this.pnlHeader = page.locator("th.v-connection-search-table-view__column_type_pnl");
        this.operationHeader = page.locator("th.v-connection-search-table-view__column_type_operations");
        this.ibHeader = page.locator("th.v-connection-search-table-view__column_type_ib");
        this.registeredHeader = page.locator("th.v-connection-search-table-view__column_type_registered");
        this.lastLoginHeader = page.locator("th.v-connection-search-table-view__column_type_last-login");
        this.graphLinkHeader = page.locator("th.v-connection-search-table-view__column_type_graph-link");
        this.levelCell = page.locator(LEVEL_CELL_SELECTOR);
        this.clientCell = page.locator("td.v-connection-search-table-view__column_type_client");
        this.connectionCell = page.locator(CONNECTION_CELL_SELECTOR);
        this.attributeCell = page.locator("td.v-connection-search-table-view__column_type_attribute");
        this.behaviorCell = page.locator("td.v-connection-search-table-view__column_type_behavior");
        this.pnlCell = page.locator("td.v-connection-search-table-view__column_type_pnl");
        this.operationCell = page.locator("td.v-connection-search-table-view__column_type_operations");
        this.ibCell = page.locator("td.v-connection-search-table-view__column_type_ib");
        this.registeredCell = page.locator("td.v-connection-search-table-view__column_type_registered");
        this.lastLoginCell = page.locator("td.v-connection-search-table-view__column_type_last-login");
        this.graphLinkCell = page.locator(GRAPH_LINK_CELL_SELECTOR);
        this.connectionCardSwitch = page.locator(CONNECTION_CARD_SWITCH_SELECTOR);
        this.connectionCard = page.locator(CONNECTION_CARD_SELECTOR);
        this.connectionCardLink = page.locator(CONNECTION_CARD_LINK_SELECTOR);
        this.unmaskConnectionCardDataButton = page.locator("//div[@class='v-graph-node-details-header__buttons']/button");
        this.connectionTableAttribute = page.locator("//div[@class='v-connection-search-table-view__attributes-list']/div");
        this.unmaskConnectionTableDataButton = page.locator("//div[@class='v-connection-search-table-view__attribute-column-name']/button");
        this.unmaskAttributeCardDataButton = page.locator("//div[@class = 'v-graph-attribute-details']/div/div/button[1]");
        this.filterButton = page.locator("//div[@class='v-connection-search-filter-button__filters']/button");
        this.filterOptionButton = page.locator("//span[@class='g-button__text']");
        this.filterCheckboxListOption = page.locator("//span[@class='g-control-label__text']/div");
        this.filterSliderRange = page.locator("//div[contains(@class,'g-text_ws_nowrap')]");
        this.filterSlider = page.locator("//div[@class='g-slider']");
        this.attributeFilterDropdown = page.locator(String.format(FILTER_CONTAINER_BY_TITLE_PATTERN, "Attribute")).locator("//input");
        this.dropdownMenuItems = page.locator("//div[@class='v-drop-down-menu__menu']/div/div/div[@class='v-drop-down-menu-item-base__text']");
        this.activeRestrictionsSwitch = page.locator(String.format(FILTER_CONTAINER_BY_TITLE_PATTERN, "Active restrictions")).locator("//input[@role='switch']/..");
        this.pnlFilterFromInput = page.locator(String.format(FILTER_CONTAINER_BY_TITLE_PATTERN, "PNL")).locator("//input").first();
        this.pnlFilterToInput = page.locator(String.format(FILTER_CONTAINER_BY_TITLE_PATTERN, "PNL")).locator("//input").last();
        this.lastLoginDatePicker = page.locator(String.format(FILTER_CONTAINER_BY_TITLE_PATTERN, "Last login")).locator("//input");
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
        this.graphNodesUnhidden = page.locator(String.format("%s%s", GRAPH_NODES_GROUP, "/descendant::div[@class='v-graph-node']"));
        this.graphNodesHiddenTitles = page.locator(String.format("%s%s", GRAPH_NODES_GROUP, "/descendant::div[@class='v-graph-hidden-node']/div[contains(@class,'g-text')]"));
        this.applyFiltersButton = page.locator("//div[@data-qa='drawer_body']/div/button");
        this.connectionTableRow = page.locator(CONNECTION_TABLE_ROW);
        this.connectionTableUserIds = page.locator(String.format("%s%s", CONNECTION_TABLE_ROW, "/descendant::div[@class='v-connection-search-table-view__client']/descendant::div[contains(@class,'g-color-text_color_secondary')]"));
        this.appliedFilters = page.locator("//div[@class='v-collapsible-horizontal-list__item']/descendant::div[@class='g-label__content']");
        this.appliedFiltersHidden = page.locator("//div[contains(@class,'v-collapsible-horizontal-list__item_hidden')]/descendant::div[@class='g-label__content']");
        this.filtersCounter = page.locator("//div[@class='v-connection-search-filter-button__filters']/div");
        this.connectionTableBehaviorValue = page.locator("div.v-connection-search-table-view__behavior");
        this.connectionTableScoreValue = page.locator("//td[contains(@class,'v-connection-search-table-view__column_type_connection')]/descendant::div[contains(@class,'g-color-text_color_secondary')]");
        this.zoomInButton = page.locator(String.format("%s/button", ZOOM_CONTROLS)).first();
        this.zoomValue = page.locator(String.format("%s/div/descendant::span", ZOOM_CONTROLS));
    }

    String mappedResponce = "{\n" + "    \"connections\": [\n" + "        {\n" + "            \"clientIdFrom\": \"infinox-424201\",\n" + "            \"clientIdTo\": \"infinox-424202\",\n" + "            \"connectionScore\": 12,\n" + "            \"connectionDetail\": [\n" + "                {\n" + "                    \"connectionAttributeName\": \"payout\",\n" + "                    \"connectionAttributeValue\": \"42424242424242\"\n" + "                }\n" + "            ],\n" + "            \"connectionType\": \"sameIdentity\",\n" + "            \"connectionDepth\": 1,\n" + "            \"abuseType\": null\n" + "        },\n" + "        {\n" + "            \"clientIdFrom\": \"infinox-424201\",\n" + "            \"clientIdTo\": \"infinox-424203\",\n" + "            \"connectionScore\": 12,\n" + "            \"connectionDetail\": [\n" + "                {\n" + "                    \"connectionAttributeName\": \"email\",\n" + "                    \"connectionAttributeValue\": \"4242424@2424242\"\n" + "                }\n" + "            ],\n" + "            \"connectionType\": \"sameIdentity\",\n" + "            \"connectionDepth\": 1,\n" + "            \"abuseType\": null\n" + "        },\n" + "        {\n" + "            \"clientIdFrom\": \"infinox-424201\",\n" + "            \"clientIdTo\": \"infinox-424204\",\n" + "            \"connectionScore\": 50,\n" + "            \"connectionDetail\": [\n" + "                {\n" + "                    \"connectionAttributeName\": \"payout\",\n" + "                    \"connectionAttributeValue\": \"42424242424242\"\n" + "                }\n" + "            ],\n" + "            \"connectionType\": \"sameIdentity\",\n" + "            \"connectionDepth\": 1,\n" + "            \"abuseType\": null\n" + "        }\n" + "    ],\n" + "    \"clients\": {\n" + "        \"infinox-424204\": {\n" + "            \"clientName\": \"Connect Fourthman\",\n" + "            \"status\": \"NORMAL\",\n" + "            \"fraudTypes\": null\n" + "        },\n" + "        \"infinox-424202\": {\n" + "            \"clientName\": \"Connect Secondman\",\n" + "            \"status\": \"NORMAL\",\n" + "            \"fraudTypes\": null\n" + "        },\n" + "        \"infinox-424203\": {\n" + "            \"clientName\": \"Connect Thrirdman\",\n" + "            \"status\": \"FRAUDSTER\",\n" + "            \"fraudTypes\": [\n" + "                {\n" + "                    \"key\": \"GAP_TRADING\",\n" + "                    \"value\": \"Gap trading\"\n" + "                },\n" + "                {\n" + "                    \"key\": \"LATENCY_ARBITRAGE\",\n" + "                    \"value\": \"Latency arbitrage\"\n" + "                }\n" + "            ]\n" + "        },\n" + "        \"infinox-424201\": {\n" + "            \"clientName\": \"Connect Firstman\",\n" + "            \"status\": \"SUSPICIOUS\",\n" + "            \"fraudTypes\": null\n" + "        }\n" + "    }\n" + "}";

    @Step("Open users restriction tab")
    public void navigateConnectionTab(String ucid) {
        Allure.step("Open users restriction tab");
        page.navigate(BASE_URL_E2E + "investigation/" + ucid);
        waitForPageToLoad();
        connectionTab.click();
    }

    @Step("Click connections tab")
    public void clickConnectionTabButton() {
        Allure.step("Click connections tab");
        connectionTab.click();
        waitForPageToLoad();
    }

    @Step("Go to main page")
    public void navigateMain() {
        Allure.step("Go to main page");
        page.navigate(BASE_URL_E2E);

    }

    public void checkLineStyle(String ucid1, String ucid2, String attributeName, double connectionScore) {
        Allure.step("check width of connection between two users");
        waitForPageToLoad();
        int width = connectionWidth(connectionScore);
        waitForPageToLoad();
        String locator = (String.format("[data-qa=\"%s:%s:%s\"][style=\"stroke-width: %s;\"]", ucid1, ucid2, attributeName, width));
        System.out.println("searched element is " + locator);
        assertTrue(page.locator(locator).isVisible());

    }

    public void checkLineStyle(String ucid1, String ucid2, double connectionScore) {
        Allure.step("check width of connection between two users");
        waitForPageToLoad();
        int width = connectionWidth(connectionScore);
        waitForPageToLoad();
        String locator = "//*[contains(@data-qa, '" + ucid1 + ":" + ucid2 + "') and contains(@style, 'stroke-width: " + width + "')] [1]";
        System.out.println("searched element is " + locator);
        page.waitForSelector(locator);
        assertTrue(page.locator(locator).isVisible());

    }

    @Step("Check text in the client in node")
    public void checkClientNodeText(String ucid1, String text) {
        waitForPageToLoad();
        page.waitForSelector(String.format(".v-graph-node[data-qa=\"%s\"]", ucid1));
        assertTrue(page.locator(String.format(".v-graph-node[data-qa=\"%s\"]", ucid1)).getByText(text).isVisible());

    }

    @Step("Check status of the client in node")
    public void checkClientStatus(String ucid, String status) {
        waitForPageToLoad();
        page.waitForSelector("//div[@data-qa='" + ucid + "']//div[text()='" + status + "']");
        assertTrue(page.locator("//div[@data-qa='" + ucid + "']//div[text()='" + status + "']").isVisible());

    }

    @Step("Check name of the client in node")
    public void checkClientName(String ucid, String name) {
        waitForPageToLoad();
        page.waitForSelector("//div[@data-qa='" + ucid + "']//div[text()='" + name + "']");
        assertTrue(page.locator("//div[@data-qa='" + ucid + "']//div[text()='" + name + "']").isVisible());

    }

    @Step("Open the MOCKED Connection search page")
    public void navigateMock() {
        page.route("**/api/alerts", route -> {
            String alert = "{\n" + "        \"id\": 1518,\n" + "        \"uuid\": \"c6b6af2e-43a2-425d-bf87-ee2b6141e267\",\n" + "        \"date\": \"2024-09-12T07:57:46.713048Z\",\n" + "        \"amount\": {\n" + "            \"value\": -235331367481903743,\n" + "            \"currency\": \"Monica\"\n" + "        },\n" + "        \"rule\": [\n" + "            \"ProctorMan\",\n" + "            \"Marquez\",\n" + "            \"Ramirez\",\n" + "            \"Simpson\",\n" + "            \"McFadden\",\n" + "            \"Farley\"\n" + "        ],\n" + "        \"client\": {\n" + "            \"id\": null,\n" + "            \"regulator\": null,\n" + "            \"brand\": null\n" + "        },\n" + "        \"status\": \"NEW\",\n" + "        \"tag\": []\n" + "    }";
            APIResponse response = route.fetch();
            String body = response.text();
            Map<String, String> headers = response.headers();
            route.fulfill(new Route.FulfillOptions().setResponse(response).setBody(alert).setHeaders(headers));
        });
        page.navigate(BASE_URL_E2E);
        waitForPageToLoad();
        page.evaluate("document.querySelector('.v-alert-list__cell_date .g-text_variant_body-1').innerText = 'YESTERDAY'");
    }

    @Step("Open connection table")
    public void openConnectionTable() {
        Allure.step("Open connection table");
        page.waitForSelector(CONNECTION_TABLE_BUTTON_SELECTOR);
        tableViewButton.click();
        page.waitForSelector(CONNECTION_TABLE_SELECTOR);
    }

    public void connectionTableIsRendered() {
        Allure.step("Check that connection table is have all headers");
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
        assertEquals("OPERATIONS", operationsText);
        String ibText = ibHeader.textContent();
        assertEquals("IB/CPA", ibText);
        String registeredText = registeredHeader.textContent();
        assertEquals("REGISTERED", registeredText);
        String lastLoginText = lastLoginHeader.textContent();
        assertEquals("LAST LOGIN", lastLoginText);
        assertTrue(graphLinkHeader.isVisible());
    }

    @Step("Verify sorting in connection table is by Behavior - Level - Connection Score")
    public void verifyTableSorting() {
        String fraudster = "fraudster";
        String suspicious = "suspicious";
        String normal = "normal";
        String behavior = "behavior";
        String level = "level";
        String score = "score";
        List<Map<String, String>> table = new ArrayList<>();
        Locator tableRows = page.locator(CONNECTION_TABLE_ROW);
        // Read all table rows and put values in a list
        for (int i = 0; i < tableRows.count(); i++) {
            Map<String, String> row = new HashMap<>();
            String rowLevel = levelCell.nth(i).textContent();
            String rowScore = connectionTableScoreValue.nth(i).textContent();
            String rowBehavior;
            if (connectionTableBehaviorValue.nth(i).getAttribute("class").contains("v-connection-search-table-view__behavior_fraudster")) {
                rowBehavior = fraudster;
            } else
                if (connectionTableBehaviorValue.nth(i).getAttribute("class").contains("v-connection-search-table-view__behavior_suspicious")) {
                    rowBehavior = suspicious;
                } else {
                    rowBehavior = normal;
                }
            row.put(behavior, rowBehavior);
            row.put(level, rowLevel);
            row.put(score, rowScore);
            table.add(row);
        }
        // Verify sorting in previously created list
        List<String> behaviorOrder = Arrays.asList(fraudster, suspicious, normal);
        for (int i = 0; i < table.size() - 1; i++) {
            Map<String, String> current = table.get(i);
            Map<String, String> next = table.get(i + 1);
            // Compare behaviors
            String behavior1 = current.get(behavior);
            String behavior2 = next.get(behavior);
            int behaviorComparison = behaviorOrder.indexOf(behavior1) - behaviorOrder.indexOf(behavior2);
            if (behaviorComparison > 0) {
                MatcherAssert.assertThat("The sorting by Behavior is incorrect", false, equalTo(true));
            } else if (behaviorComparison == 0) {
                // Behaviors are the same, compare levels
                int level1 = Integer.parseInt(current.get(level));
                int level2 = Integer.parseInt(next.get(level));
                if (level1 > level2) {
                    MatcherAssert.assertThat("The sorting by Level is incorrect", false, equalTo(true));
                } else if (level1 == level2) {
                    // Levels are the same, compare scores
                    double score1 = Double.parseDouble(current.get(score));
                    double score2 = Double.parseDouble(next.get(score));
                    if (score1 < score2) {
                        MatcherAssert.assertThat("The sorting by Score is incorrect", false, equalTo(true));
                    }
                }
            }
        }
    }

    public void openConnectionGraph() {
        Allure.step("Open connection graph");
        page.waitForSelector(CONNECTION_TABLE_BUTTON_SELECTOR);
        graphViewButton.click();
        page.waitForSelector(CONNECTION_GRAPH_SELECTOR);
    }

    public void openConnectionCard(String clientUcid) {

        Allure.step("Open connection card");
        page.waitForSelector(CONNECTION_GRAPH_SELECTOR);
        page.waitForSelector(CONNECTION_CARD_SWITCH_SELECTOR);
        if (!(page.locator(CONNECTION_CARD_SWITCH_ON_SELECTOR).isVisible())) {
            connectionCardSwitch.click();
            page.waitForSelector(CONNECTION_CARD_SWITCH_ON_SELECTOR);
        }
        page.waitForSelector(CONNECTION_NODE_SELECTOR + "[data-qa='" + clientUcid + "']");
        page.locator(CONNECTION_NODE_SELECTOR + "[data-qa='" + clientUcid + "']").locator("//div").first().click();
        page.waitForSelector(CONNECTION_CARD_SELECTOR);
    }

    public void clickConnectionLinkCc() {
        Allure.step("Test link to client from the connection card");
        page.waitForSelector(CONNECTION_CARD_SELECTOR);
        page.waitForSelector(CONNECTION_CARD_LINK_SELECTOR);
        page.waitForTimeout(500);
        connectionCardLink.click();
    }

    public void linkToCard(String clientId) {
        Allure.step("Go to the cliens page from connection table link button");
        page.waitForSelector(CONNECTION_TABLE_BUTTON_SELECTOR);
        page.waitForSelector("//div[contains(text(), " + clientId + ")]/preceding-sibling::div/a");
        page.locator("//div[contains(text(), " + clientId + ")]/preceding-sibling::div/a").click();
    }

    public void checkSelection(String userId, String userUcid) {
        Allure.step("Check highlight of user is saved (green highlight)");
        page.waitForSelector(CONNECTION_TABLE_BUTTON_SELECTOR);
        page.locator("tr").getByText(userId).click();
        page.waitForSelector("tr.v-connection-search-table-view__row_selected");
        openConnectionGraph();
        page.waitForSelector(CONNECTION_GRAPH_SELECTOR);
        page.waitForSelector(".v-graph-node_isSelected[data-qa='" + userUcid + "']");
    }

    public void checkSelectionTransitByLinkButton(String userId, String userUcid) {
        Allure.step("Check selection (green highlight)");
        page.waitForSelector(CONNECTION_TABLE_BUTTON_SELECTOR);
        page.locator("//div[contains(text(), '" + userId + "')]").hover();
        page.locator("//div[contains(text(), '" + userId + "')]/ancestor::tr//button").click();
        page.waitForSelector(CONNECTION_GRAPH_SELECTOR);
        page.waitForSelector(".v-graph-node_isSelected[data-qa='" + userUcid + "']");
    }

    public void ccCheckDirectConnectionRows(String clientToName, String rowTitle, String expectedVale) {
        Allure.step("Check direct connection values, connect to user in field " + rowTitle);
        String locator = ("//div[text() = 'Direct connections']/../../..//div[text() = '" + clientToName + "']/../..//span[text()='" + rowTitle + "']/ancestor::tr/td//*[text()='" + expectedVale + "']");
        System.out.println("searched element is " + locator);
        page.waitForSelector(locator);
        String actualValue = page.locator(locator).textContent();
        assertEquals(expectedVale, actualValue);
    }

    public void ccCheckGeneralInfoRows(String rowTitle, String expectedVale) {
        Allure.step("Check general data values, field " + rowTitle);
        page.waitForSelector("//*[contains(text(), 'General info')]/ancestor::div[@class='v-graph-node-details__content']//td//span[text()='" + rowTitle + "']/ancestor::tr/td//*[text()='" + expectedVale + "']");
        String actualValue = page.locator("//*[contains(text(), 'General info')]/ancestor::div[@class='v-graph-node-details__content']//td//span[text()='" + rowTitle + "']/ancestor::tr/td//*[text()='" + expectedVale + "']").textContent();
        assertEquals(expectedVale, actualValue);
    }

    public void ccCheckSummaryRows(String rowTitle, String expectedVale) {
        Allure.step("Check Summary data values, field " + rowTitle);
        page.waitForSelector("//*[contains(text(), 'Summary')]/ancestor::div[@class='v-graph-node-details__content']//td//span[text()='" + rowTitle + "']/ancestor::tr/td//*[text()='" + expectedVale + "']");
        String actualValue = page.locator("//*[contains(text(), 'Summary')]/ancestor::div[@class='v-graph-node-details__content']//td//span[text()='" + rowTitle + "']/ancestor::tr/td//*[text()='" + expectedVale + "']").textContent();
        assertEquals(expectedVale, actualValue);
    }

    public void ccCheckHeaderClientName(String clientName) {
        Allure.step("Check clients name in header");
        page.waitForSelector(CONNECTION_CARD_HEADER_NAME_SELECTOR);
        String actualValue = page.locator(CONNECTION_CARD_HEADER_NAME_SELECTOR).textContent();
        System.out.println("the current value for name is " + actualValue);
        assertEquals(clientName, actualValue);
    }

    public void ccCheckHeaderClientId(String clientId) {
        Allure.step("Check clients ID in header");
        page.waitForSelector(CONNECTION_CARD_HEADER_ID_SELECTOR);
        String actualValue = page.locator(CONNECTION_CARD_HEADER_ID_SELECTOR).textContent();
        System.out.println("the current value for ID is " + actualValue);
        assertEquals(clientId, actualValue);
    }

    public void ccCheckHeaderConnectionLevel(String expectedLevel) {
        Allure.step("Check connection level in header");
        page.waitForSelector(CONNECTION_CARD_HEADER_LEVEL_SELECTOR);
        String actualValue = page.locator(CONNECTION_CARD_HEADER_LEVEL_SELECTOR).textContent();
        System.out.println("the current value for level is " + actualValue);
        assertTrue(actualValue.contains(expectedLevel));
    }

    public void ccCheckHeaderConnectionPoints(String expectedPoints) {
        Allure.step("Check connection points in header");
        page.waitForSelector(CONNECTION_CARD_HEADER_POINTS_SELECTOR);
        String actualValue = page.locator(CONNECTION_CARD_HEADER_POINTS_SELECTOR).textContent();
        System.out.println("the current value for points is " + actualValue);
        assertTrue(actualValue.contains(expectedPoints));
    }

    public static int connectionWidth(double score) {
        if (score <= 16.6) return 1;
        if (score <= 33.2) return 2;
        if (score <= 49.8) return 3;
        if (score <= 66.4) return 4;
        if (score <= 83) return 5;
        return 6;
    }

    @Step("Click unmask button in card view")
    public void clickUnmaskConnectionCardDataButton() {
        unmaskConnectionCardDataButton.click();
        page.waitForCondition(() -> {
            String attributeValue = (String) unmaskConnectionCardDataButton.evaluate("el => el.getAttribute('disabled')");
            return attributeValue == null; // Check if the attribute is no longer present
        });
    }

    @Step("Get attributes from connection table by client")
    public List<String> getConnectionTableAttributesList(ClientHelper client) {
        List<String> connectionTableAttributesList = new ArrayList<>();
        Locator attributes = page.locator(String.format(CONNECTION_TABLE_ROW_BY_CLIENT_ID_PATTERN, client.getUserId())).locator(connectionTableAttribute);
        for (int i = 0; i < attributes.count(); i++) {
            connectionTableAttributesList.add(attributes.nth(i).textContent());
        }
        return connectionTableAttributesList;
    }

    @Step("Click unmask button in table view")
    public void clickUnmaskConnectionTableDataButton() {
        unmaskConnectionTableDataButton.click();
    }

    public void ccClickAttributeCardButton(String uuid, String attribute) {
        Allure.step("Click on attribute button " + attribute);
        String locator = "//div[@data-qa = '" + uuid + "']//div[contains(text(), '" + attribute + "')]";
        System.out.println("searched element is " + locator);
        page.locator(locator).click();
        page.waitForSelector("//div[@class = 'v-graph-attribute-details']//div[contains(text(), '" + attribute + "')]");
    }

    public void ccClickAttributeChevron(String uuid) {
        Allure.step("Click on attribute chevron on connection node");
        String locator = "//div[@data-qa = '" + uuid + "']//button";
        System.out.println("searched element is " + locator);
        page.locator(locator).click();
    }

    public void checkAttributeCardTitle(String expectedTitle) {
        Allure.step("Check attribute title on the header of attribute card");
        String locator = "//div[@class = 'v-graph-attribute-details']/div/div/div[contains(text(), '" + expectedTitle + "') and contains (@class, v-graph-attribute-details__header)]";
        System.out.println("searched element is " + locator);
        page.waitForSelector(locator);
    }

    public void checkAttributeCardSourceName(String expectedName) {
        Allure.step("Check client name on the header of attribute card");
        String locator = "//div[@class = 'v-graph-attribute-details']/div/div[contains(text(), '" + expectedName + "') and contains (@class, v-graph-attribute-details__header)]";
        System.out.println("searched element is " + locator);
        page.waitForSelector(locator);
    }

    public void checkAttributeCardConnectedName(String attributeName, String expectedName) {
        Allure.step("Check connected user name on the section of attribute card by attribute value");
        String locator = "//div[contains(text(), '" + attributeName + "')]/../../../..//div[contains(text(), '" + expectedName + "')]";
        System.out.println("searched element is " + locator);
        page.waitForSelector(locator);
    }

    public void checkAttributeCardFieldsValues(String attributeName, String attributeField, String expectedValue) {
        Allure.step("Check parameters and values of connection inside card, parameter " + attributeName);
        String locator = "//div[text() = '" + attributeName + "']/../../..//td//span[text()= '" + attributeField + "']/../../..//*[text()= '" + expectedValue + "']";
        System.out.println("searched element is " + locator);
        page.waitForSelector(locator);
        String actualValue = page.locator(locator).textContent();
        assertEquals(expectedValue, actualValue);
    }

    public void checkThatMaskedTextIsNotVisible() {
        Allure.step("Check that masked text is not presented ");
        page.waitForTimeout(300);
        assertFalse(page.locator(MASKED_TEXT_LOCATOR).isVisible());
    }

    public void checkThatMaskedTextIsVisible() {
        Allure.step("Check that masked text is presented on page");
        page.waitForSelector(MASKED_TEXT_LOCATOR).waitForElementState(ElementState.VISIBLE);
    }

    public void toggleAttributeCardMask() {
        Allure.step("Click mask button on attribute card");
        unmaskAttributeCardDataButton.click();
    }

    @Step("Click filter button")
    public void clickFilterButton() {
        filterButton.click();
    }

    @Step("Wait until filter is visible")
    public void waitForFilterToLoad() {
        page.waitForSelector(FILTER_CONTAINER, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
    }

    @Step("Get list of options for Level filter")
    public List<String> getLevelFilterOptions() {
        waitForFilterToLoad();
        List<String> optionsList = new ArrayList<>();
        Locator options = page.locator(String.format(FILTER_CONTAINER_BY_TITLE_PATTERN, "Level")).locator(filterOptionButton);
        for (int i = 0; i < options.count(); i++) {
            optionsList.add(options.nth(i).textContent());
        }
        return optionsList;
    }

    @Step("Get list of options for Connection type filter")
    public List<String> getConnectionTypeFilterOptions() {
        waitForFilterToLoad();
        List<String> optionsList = new ArrayList<>();
        Locator options = page.locator(String.format(FILTER_CONTAINER_BY_TITLE_PATTERN, "Connection type")).locator(filterCheckboxListOption);
        for (int i = 0; i < options.count(); i++) {
            optionsList.add(options.nth(i).textContent());
        }
        return optionsList;
    }

    @Step("Get current range for Score to initial filter")
    public String getScoreToInitialFilterCurrentRange() {
        waitForFilterToLoad();
        return page.locator(String.format(FILTER_CONTAINER_BY_TITLE_PATTERN, "Score to initial")).locator(filterSliderRange).textContent();
    }

    @Step("Is Score to initial filter slider visible")
    public Boolean isScoreToInitialFilterSliderVisible() {
        waitForFilterToLoad();
        return page.locator(String.format(FILTER_CONTAINER_BY_TITLE_PATTERN, "Score to initial")).locator(filterSliderRange).isVisible();
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
        Locator options = page.locator(String.format(FILTER_CONTAINER_BY_TITLE_PATTERN, "Behavior")).locator(filterCheckboxListOption);
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
        page.locator(String.format(PRESET_BY_LABEL_AND_VALUE_PATTERN, lastLogin, "Today")).click();
        assertThat(lastLoginDatePicker).hasValue(Utils.getCurrentDate());
        page.locator(String.format(PRESET_BY_LABEL_AND_VALUE_PATTERN, lastLogin, "Yesterday")).click();
        assertThat(lastLoginDatePicker).hasValue(Utils.getYesterdayDate());
        page.locator(String.format(PRESET_BY_LABEL_AND_VALUE_PATTERN, lastLogin, "Last 7 days")).click();
        assertThat(lastLoginDatePicker).hasValue(String.format("%s to %s", Utils.getPreviousWeekDate(), Utils.getCurrentDate()));
        page.locator(String.format(PRESET_BY_LABEL_AND_VALUE_PATTERN, lastLogin, "Last 14 days")).click();
        assertThat(lastLoginDatePicker).hasValue(String.format("%s to %s", getPrevious14DaysDate(), Utils.getCurrentDate()));
        page.locator(String.format(PRESET_BY_LABEL_AND_VALUE_PATTERN, lastLogin, "Last 30 days")).click();
        assertThat(lastLoginDatePicker).hasValue(String.format("%s to %s", getPrevious30DaysDate(), Utils.getCurrentDate()));
        page.locator(String.format(PRESET_BY_LABEL_AND_VALUE_PATTERN, lastLogin, "Last 90 days")).click();
        assertThat(lastLoginDatePicker).hasValue(String.format("%s to %s", getPrevious90DaysDateUtc(), Utils.getCurrentDate()));
    }

    @Step("Select Level filter option")
    public void selectLevelFilterOption(String option) {
        waitForFilterToLoad();
        page.locator(String.format(PRESET_BY_LABEL_AND_VALUE_PATTERN, "Level", option)).click();
    }

    @Step("Select Connection type filter option")
    public void selectConnectionTypeFilterOption(String option) {
        waitForFilterToLoad();
        page.locator(String.format(CHECKBOX_BY_LABEL_AND_VALUE_PATTERN, "Connection type", option)).click();
    }

    @Step("Select Behavior filter option")
    public void selectBehaviorFilterOption(String option) {
        waitForFilterToLoad();
        page.locator(String.format(CHECKBOX_BY_LABEL_AND_VALUE_PATTERN, "Behavior", option)).click();
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
            MatcherAssert.assertThat("Assert that each level button is not selected", button.getAttribute("class"), not(containsString("action")));
        }
    }

    public void verifyNoConnectionTypeIsSelected() {
        waitForFilterToLoad();
        for (int i = 0; i < connectionTypeFilterButtons.count(); i++) {
            Locator checkbox = connectionTypeFilterButtons.nth(i);
            MatcherAssert.assertThat("Assert that each Connection type checkbox is not selected", checkbox.isChecked(), not(equalTo(true)));
        }
    }

    public void verifyNoBehaviorIsSelected() {
        waitForFilterToLoad();
        for (int i = 0; i < behaviorFilterButtons.count(); i++) {
            Locator checkbox = behaviorFilterButtons.nth(i);
            MatcherAssert.assertThat("Assert that each Behavior checkbox is not selected", checkbox.isChecked(), not(equalTo(true)));
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
        page.locator(String.format(ATTRIBUTE_FILTER_NAME_PATTERN, attribute)).first().hover();
        page.locator(String.format(ATTRIBUTE_FILTER_VALUE_PATTERN, attribute, value)).click();
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

    @Step("Get all unhidden nodes ucids")
    public List<String> getAllUnhiddenNodesUcids() {
        List<String> ucidsList = new ArrayList<>();
        for (int i = 0; i < graphNodesUnhidden.count(); i++) {
            Locator node = graphNodesUnhidden.nth(i);
            ucidsList.add(node.getAttribute("data-qa"));
        }
        return ucidsList;
    }

    @Step("Get all hidden nodes text")
    public List<String> getAllHiddenNodesText() {
        List<String> textList = new ArrayList<>();
        for (int i = 0; i < graphNodesHiddenTitles.count(); i++) {
            Locator title = graphNodesHiddenTitles.nth(i);
            textList.add(title.textContent());
        }
        return textList;
    }

    @Step("Click apply filters button")
    public void clickApplyFiltersButton() {
        applyFiltersButton.click();
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

    @Step("Get list of applied hidden filters")
    public List<String> getAppliedFiltersHiddenList() {
        List<String> filtersList = new ArrayList<>();
        for (int i = 0; i < appliedFiltersHidden.count(); i++) {
            Locator filter = appliedFiltersHidden.nth(i);
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
}
