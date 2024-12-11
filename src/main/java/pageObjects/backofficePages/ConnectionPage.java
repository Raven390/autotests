package pageObjects.backofficePages;

import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Route;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static utils.ConfigFactory.BASE_URL_E2E;

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

    private final String CONNECTION_TABLE_BUTTON_SELECTOR = "input[value='TABLE']";
    private final String CONNECTION_TABLE_SELECTOR = ".v-connection-search-table";
    private final String CONNECTION_GRAPH_SELECTOR = ".v-connection-search-graph";
    private final String LEVEL_CELL_SELECTOR = "td.v-connection-search-table-view__column_type_level";
    private final String CONNECTION_CELL_SELECTOR = "td.v-connection-search-table-view__column_type_connection";
    private final String GRAPH_LINK_CELL_SELECTOR = "td.v-connection-search-table-view__column_type_graph-link";


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
    }

    String mappedResponce = "{\n" + "    \"connections\": [\n" + "        {\n" + "            \"clientIdFrom\": \"infinox-424201\",\n" + "            \"clientIdTo\": \"infinox-424202\",\n" + "            \"connectionScore\": 12,\n" + "            \"connectionDetail\": [\n" + "                {\n" + "                    \"connectionAttributeName\": \"payout\",\n" + "                    \"connectionAttributeValue\": \"42424242424242\"\n" + "                }\n" + "            ],\n" + "            \"connectionType\": \"sameIdentity\",\n" + "            \"connectionDepth\": 1,\n" + "            \"abuseType\": null\n" + "        },\n" + "        {\n" + "            \"clientIdFrom\": \"infinox-424201\",\n" + "            \"clientIdTo\": \"infinox-424203\",\n" + "            \"connectionScore\": 12,\n" + "            \"connectionDetail\": [\n" + "                {\n" + "                    \"connectionAttributeName\": \"email\",\n" + "                    \"connectionAttributeValue\": \"4242424@2424242\"\n" + "                }\n" + "            ],\n" + "            \"connectionType\": \"sameIdentity\",\n" + "            \"connectionDepth\": 1,\n" + "            \"abuseType\": null\n" + "        },\n" + "        {\n" + "            \"clientIdFrom\": \"infinox-424201\",\n" + "            \"clientIdTo\": \"infinox-424204\",\n" + "            \"connectionScore\": 50,\n" + "            \"connectionDetail\": [\n" + "                {\n" + "                    \"connectionAttributeName\": \"payout\",\n" + "                    \"connectionAttributeValue\": \"42424242424242\"\n" + "                }\n" + "            ],\n" + "            \"connectionType\": \"sameIdentity\",\n" + "            \"connectionDepth\": 1,\n" + "            \"abuseType\": null\n" + "        }\n" + "    ],\n" + "    \"clients\": {\n" + "        \"infinox-424204\": {\n" + "            \"clientName\": \"Connect Fourthman\",\n" + "            \"status\": \"NORMAL\",\n" + "            \"fraudTypes\": null\n" + "        },\n" + "        \"infinox-424202\": {\n" + "            \"clientName\": \"Connect Secondman\",\n" + "            \"status\": \"NORMAL\",\n" + "            \"fraudTypes\": null\n" + "        },\n" + "        \"infinox-424203\": {\n" + "            \"clientName\": \"Connect Thrirdman\",\n" + "            \"status\": \"FRAUDSTER\",\n" + "            \"fraudTypes\": [\n" + "                {\n" + "                    \"key\": \"GAP_TRADING\",\n" + "                    \"value\": \"Gap trading\"\n" + "                },\n" + "                {\n" + "                    \"key\": \"LATENCY_ARBITRAGE\",\n" + "                    \"value\": \"Latency arbitrage\"\n" + "                }\n" + "            ]\n" + "        },\n" + "        \"infinox-424201\": {\n" + "            \"clientName\": \"Connect Firstman\",\n" + "            \"status\": \"SUSPICIOUS\",\n" + "            \"fraudTypes\": null\n" + "        }\n" + "    }\n" + "}";

    @Step("Open users restriction tab")
    public void navigateConnectionTab(String ucid) {
        page.navigate(BASE_URL_E2E + "investigation?client_ucid=" + ucid);
        waitForPageToLoad();
        connectionTab.click();
    }

    @Step("Go to main page")
    public void navigateMain() {
        page.navigate(BASE_URL_E2E);

    }

    public void checkLineStyle(String ucid1, String ucid2, String attributeName, double connectionScore) {
        Allure.step("check width of connection between " + ucid1 + " and " + ucid2);
        waitForPageToLoad();
        int width = connectionWidth(connectionScore);
        waitForPageToLoad();
        assertTrue(page.locator(String.format("[data-qa=\"%s:%s:%s\"][style=\"stroke-width: %s;\"]", ucid1, ucid2, attributeName, width)).isVisible());

    }

    @Step("Check text in the client in node")
    public void checkClientNodeText(String ucid1, String text) {
        waitForPageToLoad();
        page.waitForSelector(String.format(".v-graph-node[data-qa=\"%s\"]", ucid1));
        assertTrue(page.locator(String.format(".v-graph-node[data-qa=\"%s\"]", ucid1)).getByText(text).isVisible());

    }

    @Step("Check status of the client in node")
    public void checkClientStatus(String ucid1, String status) {
        waitForPageToLoad();
        assertTrue(page.locator(String.format(".v-graph-node[data-qa=\"%s\"]  .g-text_variant_caption-2", ucid1)).getByText(status).isVisible());

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
        assertEquals("LEVEL", levelText);
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

    public void testSortingLevel() {
        Allure.step("Test sorting by level. users must be sorted  in ascending order");
        page.waitForTimeout(2000);
        int level = 0;
        int numberOfLevelCells = levelCell.count();
        assertNotEquals(0, numberOfLevelCells);
        for (int i = 0; i < numberOfLevelCells - 1; i++) {
            String currentLevel = levelCell.nth(i).textContent();
            int currentLevelInt = Integer.parseInt(currentLevel);
            assertTrue(level <= currentLevelInt);
            level = currentLevelInt;
        }
    }

    public void testSortingConnection() {
        Allure.step("Test sorting by connectionScoreToInitial. users must be sorted by connectionScoreToInitial in descending order inside one level");
        page.waitForTimeout(2000);
        int level = 0;
        float connectionToInit = 1.0F;
        int numberOfLevelCells = levelCell.count();
        int numberOfConnectionCells = page.locator(CONNECTION_CELL_SELECTOR + " .g-color-text_color_secondary").count();
        System.out.println("Number of connection cells: " + numberOfConnectionCells);
        assertNotEquals(0, numberOfConnectionCells);
        for (int i = 0; i < numberOfConnectionCells - 1; i++) {
            String currentLevel = levelCell.nth(i).textContent();
            int currentLevelInt = Integer.parseInt(currentLevel);
            String currentconnectionToInit = page.locator(CONNECTION_CELL_SELECTOR + " .g-color-text_color_secondary").nth(i).textContent();
            Float currentconnectionToInitF = Float.parseFloat(currentconnectionToInit);
            if (currentLevelInt > level) {
                connectionToInit = 1;
                level = currentLevelInt;
            }

            System.out.println("connectionToInit: " + connectionToInit);
            System.out.println("currentconnectionToInitF: " + currentconnectionToInitF);
            assertTrue(connectionToInit <= currentconnectionToInitF);
            connectionToInit = currentconnectionToInitF;
        }
    }

    public void openConnectionGraph() {
        Allure.step("Open connection graph");
        page.waitForSelector(CONNECTION_TABLE_BUTTON_SELECTOR);
        graphViewButton.click();
        page.waitForSelector(CONNECTION_GRAPH_SELECTOR);
    }

    public void checkSelection(String userId, String userUcid) {
        Allure.step("check selection (green highlight)");
        page.waitForSelector(CONNECTION_TABLE_BUTTON_SELECTOR);
        page.locator("tr").getByText(userId).click();
        page.waitForSelector("tr.v-connection-search-table-view__row_selected");
        openConnectionGraph();
        page.waitForSelector(CONNECTION_GRAPH_SELECTOR);
        page.waitForSelector(".v-graph-node_isSelected[data-qa='" + userUcid + "']");
    }

    public void checkSelectionTransitByLinkButton(String userId, String userUcid) {
        Allure.step("check selection (green highlight)");
        page.waitForSelector(CONNECTION_TABLE_BUTTON_SELECTOR);
        page.locator("//div[contains(text(), " + userId + ")]/ancestor::tr//button").click();
        page.waitForSelector(CONNECTION_GRAPH_SELECTOR);
        page.waitForSelector(".v-graph-node_isSelected[data-qa='" + userUcid + "']");
    }

    public static int connectionWidth(double score) {
        if (score <= 16.6) return 1;
        if (score <= 33.2) return 2;
        if (score <= 49.8) return 3;
        if (score <= 66.4) return 4;
        if (score <= 83) return 5;
        return 6;
    }


}
