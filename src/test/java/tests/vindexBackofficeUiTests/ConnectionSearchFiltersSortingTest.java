package tests.vindexBackofficeUiTests;

import businessObjects.api.mitigationService.PostRestrictionRequestBody;
import businessObjects.db.backofficeDb.client.Client;
import businessObjects.db.clickhouse.clientFraudTypes.ClientFraudTypes;
import businessObjects.db.clickhouse.connectionTable.ConnectionTableEntry;
import businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObject;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.db.clickhouse.mtMt4TradesCoerced.MtMt4TradesCoercedObject;
import businessObjects.kafka.alerts.RuleAlert;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.data.enums.DateTimeFormat;
import helpers.database.DbName;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;


import java.util.List;

import static businessObjects.api.mitigationService.MitigationServiceRequest.postRestriction;
import static businessObjects.db.clickhouse.connectionTable.ConnectionTableEntryFactory.*;
import static businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateUserByClient;
import static businessObjects.db.clickhouse.mtMt4TradesCoerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoercedForConnectionSearch;
import static businessObjects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.BoHelper.*;
import static helpers.database.DbHelper.*;
import static helpers.database.MitigationHelper.cleanUserRestriction;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.*;

public class ConnectionSearchFiltersSortingTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final ClientHelper connectedClient1 = getRandomVantageClientAllFields();
    private static final ClientHelper connectedClient2 = getRandomVantageClientAllFields();
    private static final ClientHelper connectedClient3 = getRandomVantageClientAllFields();
    private static final ClientHelper connectedClient4 = getRandomVantageClientAllFields();
    private static final ClientHelper connectedClient5 = getRandomVantageClientAllFields();
    private static final ClientHelper connectedClient6 = getRandomVantageClientAllFields();

    @BeforeAll
    public static void setup() throws Exception {
        CrmTbUserObject crmTbUser = generateUserByClient(client);
        CrmTbUserObject connectedCrmTbUser1 = generateUserByClient(connectedClient1);
        CrmTbUserObject connectedCrmTbUser2 = generateUserByClient(connectedClient2);
        CrmTbUserObject connectedCrmTbUser3 = generateUserByClient(connectedClient3);
        CrmTbUserObject connectedCrmTbUser4 = generateUserByClient(connectedClient4);
        CrmTbUserObject connectedCrmTbUser5 = generateUserByClient(connectedClient5);
        CrmTbUserObject connectedCrmTbUser6 = generateUserByClient(connectedClient6);
        insertObjectsToDb(CRM_USER_TABLE_NAME, List.of(crmTbUser, connectedCrmTbUser1, connectedCrmTbUser2, connectedCrmTbUser3, connectedCrmTbUser4, connectedCrmTbUser5, connectedCrmTbUser6));
        ConnectionTableEntry connectionTableEntry1 = getConnectionTableEntryForUiFiltration1(client, connectedClient1);
        ConnectionTableEntry connectionTableEntry2 = getConnectionTableEntryForUiFiltration2(client, connectedClient2);
        ConnectionTableEntry connectionTableEntry3 = getConnectionTableEntryForUiFiltration3(connectedClient1, connectedClient3);
        ConnectionTableEntry connectionTableEntry4 = getConnectionTableEntryForUiFiltration4(connectedClient2, connectedClient4);
        ConnectionTableEntry connectionTableEntry5 = getConnectionTableEntryForUiFiltration5(connectedClient3, connectedClient5);
        ConnectionTableEntry connectionTableEntry6 = getConnectionTableEntryForUiFiltration6(connectedClient4, connectedClient6);
        insertObjectsToDb(CONNECTIONS_TABLE_NAME, List.of(connectionTableEntry1, connectionTableEntry2, connectionTableEntry3, connectionTableEntry4, connectionTableEntry5, connectionTableEntry6));
        CrmTbAccountObject account = generateCrmTbAccountDataForUi(client);
        CrmTbAccountObject account1 = generateCrmTbAccountDataForUi(connectedClient1);
        CrmTbAccountObject account2 = generateCrmTbAccountDataForUi(connectedClient2);
        CrmTbAccountObject account3 = generateCrmTbAccountDataForUi(connectedClient3);
        CrmTbAccountObject account4 = generateCrmTbAccountDataForUi(connectedClient4);
        CrmTbAccountObject account5 = generateCrmTbAccountDataForUi(connectedClient5);
        CrmTbAccountObject account6 = generateCrmTbAccountDataForUi(connectedClient6);
        insertObjectsToDb(CRM_ACCOUNT_TABLE_NAME, List.of(account, account1, account2, account3, account4, account5, account6));
        MtMt4TradesCoercedObject trade = generateMt4TradesCoercedForConnectionSearch(client, 123.45, getCurrentTimestampDbFormat());
        MtMt4TradesCoercedObject trade1 = generateMt4TradesCoercedForConnectionSearch(connectedClient1, 12.45, getCurrentTimestampDbFormat());
        MtMt4TradesCoercedObject trade2 = generateMt4TradesCoercedForConnectionSearch(connectedClient2, 25.46, getPreviousWeekTimestampDbFormat());
        MtMt4TradesCoercedObject trade3 = generateMt4TradesCoercedForConnectionSearch(connectedClient3, 568.95, getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 1, 0));
        MtMt4TradesCoercedObject trade4 = generateMt4TradesCoercedForConnectionSearch(connectedClient4, 78.42, getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 1, 0, 0, 0, 0));
        MtMt4TradesCoercedObject trade5 = generateMt4TradesCoercedForConnectionSearch(connectedClient5, 1111.24, getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 1, 0, 0, 1, 0));
        MtMt4TradesCoercedObject trade6 = generateMt4TradesCoercedForConnectionSearch(connectedClient6, 89.34, getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 6, 1, 0));
        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade, trade1, trade2, trade3, trade4, trade5, trade6));
        Response response1 = postRestriction(new PostRestrictionRequestBody(
                connectedClient2.getUcid(), "03", "GENERAL", null, null, "Automation test", new PostRestrictionRequestBody.UpdatedBy("Auto", "Test")
        ));
        assertThat("Assert that restriction has been set successfully", response1.code(), equalTo(200));
        Response response2 = postRestriction(new PostRestrictionRequestBody(
                connectedClient5.getUcid(), "05", "GENERAL", null, null, "Automation test", new PostRestrictionRequestBody.UpdatedBy("Auto", "Test")
        ));
        assertThat("Assert that restriction has been set successfully", response2.code(), equalTo(200));
        RuleAlert alert = generateRuleAlertByUcid(client.getUcid());
        kafka.produceMessage(alert.alertId, objectMapper.writeValueAsString(alert), KAFKA_TOPIC_ALERTS);
        RuleAlert connectionAlert1 = generateRuleAlertByUcid(connectedClient1.getUcid());
        kafka.produceMessage(connectionAlert1.alertId, objectMapper.writeValueAsString(connectionAlert1), KAFKA_TOPIC_ALERTS);
        RuleAlert connectionAlert2 = generateRuleAlertByUcid(connectedClient6.getUcid());
        kafka.produceMessage(connectionAlert2.alertId, objectMapper.writeValueAsString(connectionAlert2), KAFKA_TOPIC_ALERTS);
        Client connectedBoClient1 = getObjectsFromDB(DbName.BO, BO_CLIENT_TABLE_NAME, String.format("ucid = '%s'", connectedClient1.getUcid()), Client.class).getFirst();
        Client connectedBoClient6 = getObjectsFromDB(DbName.BO, BO_CLIENT_TABLE_NAME, String.format("ucid = '%s'", connectedClient6.getUcid()), Client.class).getFirst();
        ClientFraudTypes fraud1 = new ClientFraudTypes(connectedBoClient1.ucid, "REBATE_CHURNING", "VINDEX", 0, getCurrentTimestampDbFormat());
        ClientFraudTypes fraud2 = new ClientFraudTypes(connectedBoClient1.ucid, "LATENCY_ARBITRAGE", "VINDEX", 0, getCurrentTimestampDbFormat());
        ClientFraudTypes fraud3 = new ClientFraudTypes(connectedBoClient1.ucid, "MARKET_MANIPULATION", "VINDEX", 0, getCurrentTimestampDbFormat());
        ClientFraudTypes fraud4 = new ClientFraudTypes(connectedBoClient1.ucid, "PRICING_ERROR", "VINDEX", 0, getCurrentTimestampDbFormat());
        ClientFraudTypes fraud5 = new ClientFraudTypes(connectedBoClient6.ucid, "GAP_TRADING", "VINDEX", 0, getCurrentTimestampDbFormat());
        ClientFraudTypes fraud6 = new ClientFraudTypes(connectedBoClient6.ucid, "SWAP_ARBITRAGE", "VINDEX", 0, getCurrentTimestampDbFormat());
        ClientFraudTypes fraud7 = new ClientFraudTypes(connectedBoClient6.ucid, "RAF_ABUSE", "VINDEX", 0, getCurrentTimestampDbFormat());
        ClientFraudTypes fraud8 = new ClientFraudTypes(connectedBoClient6.ucid, "REBATE_CHURNING", "VINDEX", 0, getCurrentTimestampDbFormat());
        insertObjectsToDb(CLIENT_FRAUD_TYPES_TABLE_NAME, List.of(fraud1, fraud2, fraud3, fraud4, fraud5, fraud6, fraud7, fraud8));
        waitForConnectionSearchToUpdate(client);
    }

    @BeforeEach
    public void goToConnectionSearchPage() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.waitForPageToLoad();
        connectionPage.clickConnectionTabButton();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("726")
    @DisplayName("Verify connection search filtration. Filter options")
    public void verifyConnectionSearchFiltration1Test() {
        connectionPage.clickFilterButton();
        assertThat("Verify that all options are present in level filter", connectionPage.getLevelFilterOptions(), contains("Level 1", "Level 2", "Level 3"));
        assertThat("Verify that all options are present in connection type filter", connectionPage.getConnectionTypeFilterOptions(), containsInAnyOrder("Same Identity", "Same Network", "Same Person"));
        assertThat("Verify that score to initial range is correct", connectionPage.getScoreToInitialFilterCurrentRange(), equalTo("Current range: 0.2 - 1"));
        assertThat("Verify that score to initial slider is visible", connectionPage.isScoreToInitialFilterSliderVisible(), equalTo(true));
        assertThat("Verify placeholder of the attribute filter dropdown", connectionPage.getAttributeFilterDropdownPlaceholder(), equalTo("Attribute name or value"));
        connectionPage.clickAttributeFilterDropdown();
        assertThat("Verify options of the attribute filter dropdown", connectionPage.getAttributeFilterDropdownOptions(), contains("device2 values", "documentNumber1 value", "ipAddress2 values", "payoutId1 value"));
        List<String> behaviorFilterOptions = connectionPage.getBehaviorFilterOptions();
        assertThat("Verify that all options are present in behavior filter", behaviorFilterOptions, containsInAnyOrder("Normal", "Suspicious", "Gap trading", "Latency arbitrage", "Market manipulation", "Pricing error", "RAF abuse", "Rebate churning", "Swap arbitrage"));
        assertThat("Verify that active restrictions switch is visible", connectionPage.isActiveRestrictionsFilterSwitchVisible(), equalTo(true));
        assertThat("Verify placeholder of the PNL from filter", connectionPage.getPnlFilterFromPlaceholder(), equalTo("12.45 USD"));
        assertThat("Verify placeholder of the PNL to filter", connectionPage.getPnlFilterToPlaceholder(), equalTo("1,111.24 USD"));
        connectionPage.verifyPresetOptionsLastLoginFilter();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("727")
    @DisplayName("Verify connection search filtration. Reset all filters")
    public void verifyConnectionSearchFiltration2Test() {
        // Reset all
        connectionPage.clickFilterButton();
        connectionPage.selectLevelFilterOption("Level 1");
        connectionPage.selectConnectionTypeFilterOption("Same Identity");
        connectionPage.selectScoreToInitialRange();
        connectionPage.selectAttribute("device", "All values");
        connectionPage.selectBehaviorFilterOption("Normal");
        connectionPage.clickActiveRestrictionsSwitch();
        connectionPage.fillPnlFromInput("20");
        connectionPage.fillPnlToInput("100");
        connectionPage.selectLastLogin(getCurrentDate(), getCurrentDate());
        connectionPage.resetAllFiltersAndVerify("Current range: 0.2 - 1");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("728")
    @DisplayName("Verify connection search filtration. Reset individual filters")
    public void verifyConnectionSearchFiltration3Test() {
        connectionPage.clickFilterButton();
        // Level
        connectionPage.selectLevelFilterOption("Level 1");
        connectionPage.resetLevelAndVerify();
        // Connection type
        connectionPage.selectConnectionTypeFilterOption("Same Identity");
        connectionPage.resetConnectionTypeAndVerify();
        // Score to initial
        connectionPage.selectScoreToInitialRange();
        connectionPage.resetScoreToInitialAndVerify("Current range: 0.2 - 1");
        // Attribute
        connectionPage.selectAttribute("device", "All values");
        connectionPage.resetAttributesAndVerify();
        // Behavior
        connectionPage.selectBehaviorFilterOption("Normal");
        connectionPage.resetBehaviorAndVerify();
        // PNL
        connectionPage.fillPnlFromInput("20");
        connectionPage.fillPnlToInput("100");
        connectionPage.resetPnlAndVerify();
        // Last login
        connectionPage.selectLastLogin(getCurrentDate(), getCurrentDate());
        connectionPage.resetLastLoginAndVerify();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("729")
    @DisplayName("Verify connection search filtration. Level filter")
    public void verifyConnectionSearchFiltration4Test() {
        connectionPage.clickFilterButton();
        connectionPage.selectLevelFilterOption("Level 2");
        connectionPage.clickApplyFiltersButton();
        List<String> unhiddenNodesUcids = connectionPage.getAllUnhiddenNodesUcids();
        assertThat("Verify all expected unhidden nodes are present", unhiddenNodesUcids, containsInAnyOrder(client.getUcid(), connectedClient3.getUcid(), connectedClient4.getUcid()));
        List<String> hiddenNodesText = connectionPage.getAllHiddenNodesText();
        assertThat("Verify all expected hidden nodes are present", hiddenNodesText, containsInAnyOrder("2 hidden", "2 hidden"));
        connectionPage.openConnectionTable();
        assertThat("Verify amount of displayed rows", connectionPage.getConnectionTableRowCount(), equalTo(2));
        assertThat("Verify user ids displayed in the table", connectionPage.getConnectionTableUserIdsList(), containsInAnyOrder(connectedClient3.getUserId().toString(), connectedClient4.getUserId().toString()));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("730")
    @DisplayName("Verify connection search filtration. Connection type filter")
    public void verifyConnectionSearchFiltration5Test() {
        connectionPage.clickFilterButton();
        connectionPage.selectConnectionTypeFilterOption("Same Network");
        connectionPage.clickApplyFiltersButton();
        List<String> unhiddenNodesUcids = connectionPage.getAllUnhiddenNodesUcids();
        assertThat("Verify all expected unhidden nodes are present", unhiddenNodesUcids, containsInAnyOrder(client.getUcid(), connectedClient1.getUcid(), connectedClient3.getUcid(), connectedClient5.getUcid()));
        List<String> hiddenNodesText = connectionPage.getAllHiddenNodesText();
        assertThat("Verify all expected hidden nodes are present", hiddenNodesText, containsInAnyOrder("1 hidden", "1 hidden", "1 hidden"));
        connectionPage.openConnectionTable();
        assertThat("Verify amount of displayed rows", connectionPage.getConnectionTableRowCount(), equalTo(3));
        assertThat("Verify user ids displayed in the table", connectionPage.getConnectionTableUserIdsList(), containsInAnyOrder(connectedClient1.getUserId().toString(), connectedClient3.getUserId().toString(), connectedClient5.getUserId().toString()));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("731")
    @DisplayName("Verify connection search filtration. Connection score to initial filter")
    public void verifyConnectionSearchFiltration6Test() {
        connectionPage.clickFilterButton();
        connectionPage.selectScoreToInitialRange();
        connectionPage.clickApplyFiltersButton();
        List<String> unhiddenNodesUcids = connectionPage.getAllUnhiddenNodesUcids();
        assertThat("Verify all expected unhidden nodes are present", unhiddenNodesUcids, containsInAnyOrder(client.getUcid(), connectedClient1.getUcid(), connectedClient2.getUcid(), connectedClient4.getUcid()));
        List<String> hiddenNodesText = connectionPage.getAllHiddenNodesText();
        assertThat("Verify all expected hidden nodes are present", hiddenNodesText, containsInAnyOrder("1 hidden", "2 hidden"));
        connectionPage.openConnectionTable();
        assertThat("Verify amount of displayed rows", connectionPage.getConnectionTableRowCount(), equalTo(3));
        assertThat("Verify user ids displayed in the table", connectionPage.getConnectionTableUserIdsList(), containsInAnyOrder(connectedClient1.getUserId().toString(), connectedClient2.getUserId().toString(), connectedClient4.getUserId().toString()));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("732")
    @DisplayName("Verify connection search filtration. Attribute filter")
    public void verifyConnectionSearchFiltration7Test() {
        connectionPage.clickFilterButton();
        connectionPage.selectAttribute("ipAddress", "All values");
        connectionPage.clickApplyFiltersButton();
        List<String> unhiddenNodesUcids = connectionPage.getAllUnhiddenNodesUcids();
        assertThat("Verify all expected unhidden nodes are present", unhiddenNodesUcids, containsInAnyOrder(client.getUcid(), connectedClient1.getUcid(), connectedClient3.getUcid(), connectedClient5.getUcid()));
        List<String> hiddenNodesText = connectionPage.getAllHiddenNodesText();
        assertThat("Verify all expected hidden nodes are present", hiddenNodesText, containsInAnyOrder("1 hidden", "1 hidden", "1 hidden"));
        connectionPage.openConnectionTable();
        assertThat("Verify amount of displayed rows", connectionPage.getConnectionTableRowCount(), equalTo(3));
        assertThat("Verify user ids displayed in the table", connectionPage.getConnectionTableUserIdsList(), containsInAnyOrder(connectedClient1.getUserId().toString(), connectedClient3.getUserId().toString(), connectedClient5.getUserId().toString()));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("733")
    @DisplayName("Verify connection search filtration. Behavior filter")
    public void verifyConnectionSearchFiltration8Test() {
        connectionPage.clickFilterButton();
        connectionPage.selectBehaviorFilterOption("Normal");
        connectionPage.selectBehaviorFilterOption("Market manipulation");
        connectionPage.clickApplyFiltersButton();
        List<String> unhiddenNodesUcids = connectionPage.getAllUnhiddenNodesUcids();
        assertThat("Verify all expected unhidden nodes are present", unhiddenNodesUcids, containsInAnyOrder(client.getUcid(), connectedClient1.getUcid(), connectedClient2.getUcid(), connectedClient3.getUcid(), connectedClient4.getUcid(), connectedClient5.getUcid()));
        List<String> hiddenNodesText = connectionPage.getAllHiddenNodesText();
        assertThat("Verify all expected hidden nodes are present", hiddenNodesText, containsInAnyOrder("1 hidden"));
        connectionPage.openConnectionTable();
        assertThat("Verify amount of displayed rows", connectionPage.getConnectionTableRowCount(), equalTo(5));
        assertThat("Verify user ids displayed in the table", connectionPage.getConnectionTableUserIdsList(), containsInAnyOrder(connectedClient1.getUserId().toString(), connectedClient2.getUserId().toString(), connectedClient3.getUserId().toString(), connectedClient4.getUserId().toString(), connectedClient5.getUserId().toString()));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("734")
    @DisplayName("Verify connection search filtration. Active restrictions filter")
    public void verifyConnectionSearchFiltration9Test() {
        connectionPage.clickFilterButton();
        connectionPage.clickActiveRestrictionsSwitch();
        connectionPage.clickApplyFiltersButton();
        List<String> unhiddenNodesUcids = connectionPage.getAllUnhiddenNodesUcids();
        assertThat("Verify all expected unhidden nodes are present", unhiddenNodesUcids, containsInAnyOrder(client.getUcid(), connectedClient2.getUcid(), connectedClient5.getUcid()));
        List<String> hiddenNodesText = connectionPage.getAllHiddenNodesText();
        assertThat("Verify all expected hidden nodes are present", hiddenNodesText, containsInAnyOrder("1 hidden", "2 hidden", "1 hidden"));
        connectionPage.openConnectionTable();
        assertThat("Verify amount of displayed rows", connectionPage.getConnectionTableRowCount(), equalTo(2));
        assertThat("Verify user ids displayed in the table", connectionPage.getConnectionTableUserIdsList(), containsInAnyOrder(connectedClient2.getUserId().toString(), connectedClient5.getUserId().toString()));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("735")
    @DisplayName("Verify connection search filtration. PNL filter")
    public void verifyConnectionSearchFiltration10Test() {
        connectionPage.clickFilterButton();
        connectionPage.fillPnlFromInput("78.42");
        connectionPage.fillPnlToInput("568.95");
        connectionPage.clickApplyFiltersButton();
        List<String> unhiddenNodesUcids = connectionPage.getAllUnhiddenNodesUcids();
        assertThat("Verify all expected unhidden nodes are present", unhiddenNodesUcids, containsInAnyOrder(client.getUcid(), connectedClient3.getUcid(), connectedClient4.getUcid(), connectedClient6.getUcid()));
        List<String> hiddenNodesText = connectionPage.getAllHiddenNodesText();
        assertThat("Verify all expected hidden nodes are present", hiddenNodesText, containsInAnyOrder("2 hidden", "1 hidden"));
        connectionPage.openConnectionTable();
        assertThat("Verify amount of displayed rows", connectionPage.getConnectionTableRowCount(), equalTo(3));
        assertThat("Verify user ids displayed in the table", connectionPage.getConnectionTableUserIdsList(), containsInAnyOrder(connectedClient3.getUserId().toString(), connectedClient4.getUserId().toString(), connectedClient6.getUserId().toString()));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("736")
    @DisplayName("Verify connection search filtration. Last login filter")
    public void verifyConnectionSearchFiltration11Test() {
        connectionPage.clickFilterButton();
        connectionPage.selectLastLogin(getCurrentDate(), getPreviousWeekDate());
        connectionPage.clickApplyFiltersButton();
        List<String> unhiddenNodesUcids = connectionPage.getAllUnhiddenNodesUcids();
        assertThat("Verify all expected unhidden nodes are present", unhiddenNodesUcids, containsInAnyOrder(client.getUcid(), connectedClient1.getUcid(), connectedClient2.getUcid(), connectedClient3.getUcid(), connectedClient6.getUcid()));
        List<String> hiddenNodesText = connectionPage.getAllHiddenNodesText();
        assertThat("Verify all expected hidden nodes are present", hiddenNodesText, containsInAnyOrder("1 hidden", "1 hidden"));
        connectionPage.openConnectionTable();
        assertThat("Verify amount of displayed rows", connectionPage.getConnectionTableRowCount(), equalTo(4));
        assertThat("Verify user ids displayed in the table", connectionPage.getConnectionTableUserIdsList(), containsInAnyOrder(connectedClient1.getUserId().toString(), connectedClient2.getUserId().toString(), connectedClient3.getUserId().toString(), connectedClient6.getUserId().toString()));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("737")
    @DisplayName("Verify connection search filtration. Applied filters and counter")
    public void verifyConnectionSearchFiltration12Test() {
        connectionPage.clickFilterButton();
        connectionPage.selectConnectionTypeFilterOption("Same Person");
        connectionPage.selectAttribute("device", "All values");
        connectionPage.selectBehaviorFilterOption("Normal");
        connectionPage.fillPnlFromInput("12.45");
        connectionPage.clickApplyFiltersButton();
        assertThat("Verify applied filters list", connectionPage.getAppliedFiltersList(), containsInAnyOrder("Connection type", "Behavior", "PNL", "Attribute"));
        assertThat("Verify applied filters counter is correct", connectionPage.getAppliedFiltersCount(), equalTo("4"));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1071")
    @DisplayName("Verify connection table default sorting")
    public void verifyConnectionSearchTableDefaultSortingTest() {
        connectionPage.openConnectionTable();
        assertThat("Verify sorting", connectionPage.getConnectionTableUserIdsList(), contains(connectedClient1.getUserId().toString(), connectedClient6.getUserId().toString(), connectedClient2.getUserId().toString(), connectedClient4.getUserId().toString(), connectedClient3.getUserId().toString(), connectedClient5.getUserId().toString()));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1072")
    @DisplayName("Verify connection table total pnl sorting")
    public void verifyConnectionSearchTableTotalPnlSortingTest() {
        connectionPage.openConnectionTable();
        assertThat("Verify tooltip", connectionPage.getTotalPnlSortingTooltip(), is("Sort by PNL:Descending"));
        connectionPage.clickTotalPnlHeader();
        assertThat("Verify sorting", connectionPage.getConnectionTableUserIdsList(), contains(connectedClient5.getUserId().toString(), connectedClient3.getUserId().toString(), connectedClient6.getUserId().toString(), connectedClient4.getUserId().toString(), connectedClient2.getUserId().toString(), connectedClient1.getUserId().toString()));
        assertThat("Verify tooltip", connectionPage.getTotalPnlSortingTooltip(), is("Change sorting to:Ascending"));
        connectionPage.clickTotalPnlHeader();
        assertThat("Verify sorting", connectionPage.getConnectionTableUserIdsList(), contains(connectedClient1.getUserId().toString(), connectedClient2.getUserId().toString(), connectedClient4.getUserId().toString(), connectedClient6.getUserId().toString(), connectedClient3.getUserId().toString(), connectedClient5.getUserId().toString()));
        assertThat("Verify tooltip", connectionPage.getTotalPnlSortingTooltip(), is("Remove sorting"));
        connectionPage.clickTotalPnlHeader();
        assertThat("Verify sorting", connectionPage.getConnectionTableUserIdsList(), contains(connectedClient1.getUserId().toString(), connectedClient6.getUserId().toString(), connectedClient2.getUserId().toString(), connectedClient4.getUserId().toString(), connectedClient3.getUserId().toString(), connectedClient5.getUserId().toString()));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1073")
    @DisplayName("Verify connection table last login sorting")
    public void verifyConnectionSearchTableLastLoginSortingTest() {
        connectionPage.openConnectionTable();
        assertThat("Verify tooltip", connectionPage.getLastLoginSortingTooltip(), is("Sort by last login date:Newest → Oldest"));
        connectionPage.clickLastLoginHeader();
        assertThat("Verify sorting", connectionPage.getConnectionTableUserIdsList(), contains(connectedClient1.getUserId().toString(), connectedClient3.getUserId().toString(), connectedClient2.getUserId().toString(), connectedClient6.getUserId().toString(), connectedClient4.getUserId().toString(), connectedClient5.getUserId().toString()));
        assertThat("Verify tooltip", connectionPage.getLastLoginSortingTooltip(), is("Change sorting to:Oldest → Newest"));
        connectionPage.clickLastLoginHeader();
        assertThat("Verify sorting", connectionPage.getConnectionTableUserIdsList(), contains(connectedClient5.getUserId().toString(), connectedClient4.getUserId().toString(), connectedClient6.getUserId().toString(), connectedClient2.getUserId().toString(), connectedClient3.getUserId().toString(), connectedClient1.getUserId().toString()));
        assertThat("Verify tooltip", connectionPage.getLastLoginSortingTooltip(), is("Remove sorting"));
        connectionPage.clickLastLoginHeader();
        assertThat("Verify sorting", connectionPage.getConnectionTableUserIdsList(), contains(connectedClient1.getUserId().toString(), connectedClient6.getUserId().toString(), connectedClient2.getUserId().toString(), connectedClient4.getUserId().toString(), connectedClient3.getUserId().toString(), connectedClient5.getUserId().toString()));
    }

    @AfterAll
    public static void teardown() throws Exception {
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid IN ('%s', '%s', '%s', '%s', '%s', '%s', '%s')", client.getUcid(), connectedClient1.getUcid(), connectedClient2.getUcid(), connectedClient3.getUcid(), connectedClient4.getUcid(), connectedClient5.getUcid(), connectedClient6.getUcid()));
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from IN ('%s', '%s', '%s', '%s', '%s')", client.getUcid(), connectedClient1.getUcid(), connectedClient2.getUcid(), connectedClient3.getUcid(), connectedClient4.getUcid()));
        deleteEntryFromDb(MT4_TRADES_COERCED_TABLE_NAME, String.format("ucid IN ('%s', '%s', '%s', '%s', '%s', '%s', '%s')", client.getUcid(), connectedClient1.getUcid(), connectedClient2.getUcid(), connectedClient3.getUcid(), connectedClient4.getUcid(), connectedClient5.getUcid(), connectedClient6.getUcid()));
        closeAlert(client.getUcid());
        closeAlert(connectedClient1.getUcid());
        closeAlert(connectedClient6.getUcid());
        cleanUserRestriction(connectedClient2.getUcid());
        cleanUserRestriction(connectedClient5.getUcid());
        deleteEntryFromDb(CLIENT_FRAUD_TYPES_TABLE_NAME, String.format("ucid IN ('%s', '%s')", connectedClient1.getUcid(), connectedClient6.getUcid()));
    }
}
