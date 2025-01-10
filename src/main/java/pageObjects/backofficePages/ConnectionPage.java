package pageObjects.backofficePages;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.ElementState;
import helpers.data.ClientHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

import java.util.ArrayList;
import java.util.List;
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
    private final Locator connectionCardSwitch;
    private final Locator connectionCard;
    private final Locator connectionCardLink;
    private final Locator unmaskConnectionCardDataButton;
    private final Locator connectionTableAttribute;
    private final Locator unmaskConnectionTableDataButton;
    private final Locator unmaskAttributeCardDataButton;

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
        String locator = ("//div[text() = 'Direct connections']/../../..//div[text() = 'Connect Tenthman']/../..//span[text()='" + rowTitle + "']/ancestor::tr/td//*[text()='" + expectedVale + "']");
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
}
