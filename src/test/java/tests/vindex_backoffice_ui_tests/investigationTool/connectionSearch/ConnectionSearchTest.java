//package tests.vindex_backoffice_ui_tests.investigationTool.connectionSearch;
//
//import business_objects.db.clickhouse.connection_table.ConnectionTableEntry;
//import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
//import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositObject;
//import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
//import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalObject;
//import business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObject;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.microsoft.playwright.Page;
//import helpers.data.ClientHelper;
//import helpers.data.enums.Brand;
//import helpers.data.enums.FraudTypeOld;
//import helpers.data.enums.Regulator;
//import helpers.kafka.KafkaHelper;
//import io.qameta.allure.Allure;
//import io.qameta.allure.AllureId;
//import org.junit.jupiter.api.*;
//import tests.TestBaseWeb;
//
//import java.sql.SQLException;
//import java.text.DecimalFormat;
//import java.util.List;
//
//import static business_objects.db.clickhouse.client_fraud_types.ClientFraudTypesFactory.createClientFraudsCh;
//import static business_objects.db.clickhouse.client_fraud_types.ClientFraudTypesFactory.deleteUserFraudsCh;
//import static business_objects.db.clickhouse.connection_table.ConnectionTableEntryFactory.*;
//import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateStaticCrmTbAccountActive;
//import static business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositObjectFactory.generateDepositByClient;
//import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
//import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserWithUcidFirstName;
//import static business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalObjectFactory.generateWithdrawalByClient;
//import static business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoercedRandomized;
//import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
//import static helpers.database.DbHelper.*;
//import static helpers.kafka.alerts.CreateSimpleAlert.createSimpleAlert;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static utils.Constants.*;
//import static utils.Utils.insertConnectionToDb;
//import static utils.Utils.ucidListDbFormat;
//
//public class ConnectionSearchTest extends TestBaseWeb {
//
//
//    DecimalFormat dfd = new DecimalFormat("#####,###.##");
//
//    private static final KafkaHelper kafka = new KafkaHelper();
//    private static final ObjectMapper objectMapper = new ObjectMapper();
//    private static final ClientHelper client = getRandomVantageClientAllFields();
//    private static final ClientHelper connectedClient1 = getRandomVantageClientAllFields(); // lvl 1 normal
//    private static final ClientHelper connectedClient2 = getRandomVantageClientAllFields(); // lvl 1 suspicious
//    private static final ClientHelper connectedClient3 = getRandomVantageClientAllFields(); // lvl 2 potential status client
//    private static final ClientHelper connectedClient4 = getRandomVantageClientAllFields(); // lvl 2 potential with abuse
//    private static final ClientHelper connectedClient5 = getRandomVantageClientAllFields(); // lvl 3
//
//    @BeforeAll
//    static void setup() throws Exception {
//        // Users
//        CrmTbUserObject crmTbUser = generateUserWithUcidFirstName(client);
//        CrmTbUserObject connectedCrmTbUser1 = generateUserWithUcidFirstName(connectedClient1);
//        CrmTbUserObject connectedCrmTbUser2 = generateUserWithUcidFirstName(connectedClient2);
//        CrmTbUserObject connectedCrmTbUser3 = generateUserWithUcidFirstName(connectedClient3);
//        CrmTbUserObject connectedCrmTbUser4 = generateUserWithUcidFirstName(connectedClient4);
//        CrmTbUserObject connectedCrmTbUser5 = generateUserWithUcidFirstName(connectedClient5);
//        insertObjectsToDb(CRM_USER_TABLE_NAME, List.of(crmTbUser, connectedCrmTbUser1, connectedCrmTbUser2, connectedCrmTbUser3, connectedCrmTbUser4, connectedCrmTbUser5));
//        // Connections
//        ConnectionTableEntry connectionTableEntry1 = getConnectionTableEntry(client, connectedClient1);
//        ConnectionTableEntry connectionTableEntry2 = getConnectionTableEntry(client, connectedClient2);
//        ConnectionTableEntry connectionTableEntry3 = getConnectionTableEntry(connectedClient1, connectedClient3);
//        ConnectionTableEntry connectionTableEntry4 = getConnectionTableEntry(connectedClient2, connectedClient4);
//        ConnectionTableEntry connectionTableEntry5 = getConnectionTableEntryForUi(connectedClient3, connectedClient5);
//        insertObjectsToDb(CONNECTIONS_TABLE_NAME, List.of(connectionTableEntry1, connectionTableEntry2, connectionTableEntry3, connectionTableEntry4, connectionTableEntry5));
//        //
//        insertObjectsToDb(CRM_USER_TABLE_NAME, List.of(client1DB, client2DB, client3DB, client4DB, client5DB, client6DB, client7DB, client8DB, client9DB, client10DB, client11DB, client12DB, client13DB, client14DB, client15DB));
//        insertConnectionToDb(connection1, connection2, connection3, connection4, connection5, connection6, connection7, connection8, connection9, connection10, connection11, connection12, connection13);
//    }
//
////    @AfterAll
////    public static void tearDown() throws Exception {
////    }
//
//    @Test
//    @Tag(TEAM_BACKOFFICE)
//    @Tag(LAYER_WEB)
//    @AllureId("315")
//    @DisplayName("Check line width")
//    void csPageConnectionLinesStileTest() {
//        connectionPage.navigateEnterPage();
//        keycloackPage.loginAsAutotestUser();
//        connectionPage.navigateConnectionTab(client1.getUcid());
//        connectionPage.checkLineStyle(client1.getUcid(), client2.getUcid(), 0.01);
//        connectionPage.checkLineStyle(client1.getUcid(), client3.getUcid(), 0.16);
//        connectionPage.checkLineStyle(client1.getUcid(), client4.getUcid(), 0.17);
//        connectionPage.checkLineStyle(client1.getUcid(), client5.getUcid(), 0.33);
//        connectionPage.checkLineStyle(client1.getUcid(), client6.getUcid(), 0.34);
//        connectionPage.checkLineStyle(client1.getUcid(), client7.getUcid(), 0.49);
//        connectionPage.checkLineStyle(client1.getUcid(), client8.getUcid(), 0.50);
//        connectionPage.checkLineStyle(client1.getUcid(), client9.getUcid(), 0.66);
//        connectionPage.checkLineStyle(client1.getUcid(), client10.getUcid(), 0.67);
//        connectionPage.checkLineStyle(client1.getUcid(), client11.getUcid(), 0.83);
//        connectionPage.checkLineStyle(client1.getUcid(), client12.getUcid(), 0.84);
//    }
//
//    @Test
//    @Tag(TEAM_BACKOFFICE)
//    @Tag(LAYER_WEB)
//    @AllureId("311")
//    @DisplayName("Check that connection node have right client name")
//    void csPageConnectionNodesHaveRightClientNamesTest() {
//        connectionPage.navigateEnterPage();
//        keycloackPage.loginAsAutotestUser();
//        connectionPage.navigateConnectionTab(client1.getUcid());
//        connectionPage.checkClientNodeText(client1.getUcid(), "Connect Firstman");
//        connectionPage.checkClientNodeText(client2.getUcid(), "Connect Secondman");
//        connectionPage.checkClientNodeText(client3.getUcid(), "Connect Thrirdman");
//    }
//
//    @Test
//    @Tag(TEAM_BACKOFFICE)
//    @Tag(LAYER_WEB)
//    @AllureId("311")
//    @DisplayName("Check that connection node have right client status")
//    void csPageConnectionNodesHaveRightClientStatusTest() throws Exception {
//        deleteUserFraudsCh(client1.getUcid());
//        createSimpleAlert(client1.getUcid(), "HEDGING");
//        deleteUserFraudsCh(client2.getUcid());
//        deleteUserFraudsCh(client3.getUcid());
//        createClientFraudsCh(client3.getUcid(), FraudTypeOld.GAP_TRADING.getKey());
//        createClientFraudsCh(client3.getUcid(), FraudTypeOld.LATENCY_ARBITRAGE.getKey());
//        connectionPage.navigateEnterPage();
//        keycloackPage.loginAsAutotestUser();
//        connectionPage.navigateConnectionTab(client1.getUcid());
//        connectionPage.checkClientStatus(client1.getUcid(), "Suspicious");
//        connectionPage.checkClientStatus(client2.getUcid(), "Normal");
//        connectionPage.checkClientStatus(client3.getUcid(), "Latency arbitrage, Gap trading");
//    }
//
//    @Test
//    @Tag(TEAM_BACKOFFICE)
//    @Tag(LAYER_WEB)
//    @AllureId("518")
//    @DisplayName("Check that connection table opens")
//    void csPageConnectionTableOpensTest() {
//        connectionPage.navigateEnterPage();
//        keycloackPage.loginAsAutotestUser();
//        connectionPage.navigateConnectionTab(client1.getUcid());
//        connectionPage.openConnectionTable();
//        connectionPage.connectionTableIsRendered();
//    }
//
//    @Disabled("need update")
//    @Test
//    @Tag(TEAM_BACKOFFICE)
//    @Tag(LAYER_WEB)
//    @AllureId("526")
//    @DisplayName("Check that sorting works")
//    void csPageConnectionTableSortTest() {
//        connectionPage.navigateEnterPage();
//        keycloackPage.loginAsAutotestUser();
//        connectionPage.navigateConnectionTab(client1.getUcid());
//        connectionPage.openConnectionTable();
//        connectionPage.verifyTableSorting();
//    }
//
//    @Test
//    @Tag(TEAM_BACKOFFICE)
//    @Tag(LAYER_WEB)
//    @AllureId("520")
//    @DisplayName("Connection Search connection search tab can switches between graph and table mode")
//    void csPageConnectionTableSwitchesBackToGraphTest() {
//        connectionPage.navigateEnterPage();
//        keycloackPage.loginAsAutotestUser();
//        connectionPage.navigateConnectionTab(client1.getUcid());
//        connectionPage.openConnectionTable();
//        connectionPage.openConnectionGraph();
//    }
//
//    @Test
//    @Tag(TEAM_BACKOFFICE)
//    @Tag(LAYER_WEB)
//    @AllureId("521")
//    @DisplayName("Test the same user save highlighted state between different modes of the connection search when you switches view mode")
//    void csPageConnectionSelectionSwitchViewTest() {
//        connectionPage.navigateEnterPage();
//        keycloackPage.loginAsAutotestUser();
//        connectionPage.navigateConnectionTab(client1.getUcid());
//        connectionPage.openConnectionTable();
//        connectionPage.checkSelection(client2.getUserId(), client2.getUcid());
//    }
//
//    @Test
//    @Tag(TEAM_BACKOFFICE)
//    @Tag(LAYER_WEB)
//    @AllureId("527")
//    @DisplayName("Test the same user save highlighted state between different modes of the connection search when you click link button")
//    void csPageConnectionSelectionLinkButtonTest() {
//        connectionPage.navigateEnterPage();
//        keycloackPage.loginAsAutotestUser();
//        connectionPage.navigateConnectionTab(client1.getUcid());
//        connectionPage.openConnectionTable();
//        connectionPage.checkSelectionTransitByLinkButton(client2.getUserId(), client2.getUcid());
//    }
//
//    @Test
//    @Tag(TEAM_BACKOFFICE)
//    @Tag(LAYER_WEB)
//    @AllureId("523")
//    @DisplayName("Connection Search. User can go to clients card from connection table")
//    void csPageGoToClientCardTest() {
//        connectionPage.navigateEnterPage();
//        keycloackPage.loginAsAutotestUser();
//        connectionPage.navigateConnectionTab(client1.getUcid());
//        connectionPage.openConnectionTable();
//        Page newPage = context.waitForPage(() -> {
//            connectionPage.linkToCard(String.valueOf(client2.getUserId()));
//        });
//        int count = 0;
//        while (newPage.url() == null && count < 50) {
//            newPage.waitForTimeout(500);
//            count += 1;
//        }
//        System.out.println("page URL is: " + newPage.url().toString());
//        assertTrue(newPage.url().contains(client2.getUcid()));
//        newPage.close();
//
//    }
//
//    @Test
//    @Tag(TEAM_BACKOFFICE)
//    @Tag(LAYER_WEB)
//    @AllureId("556")
//    @DisplayName("Connection search Connection Card user can open clients page from the connection card")
//    void csPageGoToClientCardFromConnectionCardTest() {
//        connectionPage.navigateEnterPage();
//        keycloackPage.loginAsAutotestUser();
//        connectionPage.navigateConnectionTab(client1.getUcid());
//        connectionPage.openConnectionCard(client2.getUcid());
//        Page newPage = context.waitForPage(() -> {
//            connectionPage.clickConnectionLinkCc();
//        });
//        int count = 0;
//        while (newPage.url() == null && count < 50) {
//            newPage.waitForTimeout(500);
//            count += 1;
//        }
//        System.out.println("page URL is: " + newPage.url().toString());
//        assertTrue(newPage.url().contains(client2.getUcid()));
//    }
//
//    @Test
//    @Tag(TEAM_BACKOFFICE)
//    @Tag(LAYER_WEB)
//    @AllureId("555")
//    @DisplayName("Test connection card content Direct Connections  info")
//    void csPageConnectionCardDirectConnectionContentTest() {
//        connectionPage.navigateEnterPage();
//        keycloackPage.loginAsAutotestUser();
//        connectionPage.navigateConnectionTab(client1.getUcid());
//        connectionPage.openConnectionCard(client15.getUcid());
//        connectionPage.checkDirectConnectionRows("Connect Threerteenhman", "Type", "Same Person");
//        connectionPage.checkDirectConnectionRows("Connect Threerteenhman", "Score", "0.16");
//        connectionPage.checkDirectConnectionRows("Connect Threerteenhman", "payoutId", "535456**** **0344");
//    }
//
//    @Test
//    @Tag(TEAM_BACKOFFICE)
//    @Tag(LAYER_WEB)
//    @AllureId("554")
//    @DisplayName("Test connection card content General info")
//    void csPageConnectionCardGeneralInfoContentTest() {
//        connectionPage.navigateEnterPage();
//        keycloackPage.loginAsAutotestUser();
//        connectionPage.navigateConnectionTab(client1.getUcid());
//        connectionPage.openConnectionCard(client6.getUcid());
//        connectionPage.checkGeneralInfoRows("Brand", client6.getBrand());
//        connectionPage.checkGeneralInfoRows("Country", "Cyprus");
//        connectionPage.checkGeneralInfoRows("Email", "t***4@example.com");
//        connectionPage.checkGeneralInfoRows("CPA", "2");
//        connectionPage.checkGeneralInfoRows("Registered", "2014-10-23");
//        connectionPage.checkGeneralInfoRows("Last login", "-");
//    }
//
//    @Test
//    @Tag(TEAM_BACKOFFICE)
//    @Tag(LAYER_WEB)
//    @AllureId("547")
//    @DisplayName("Test connection card content Summary")
//    void csPageConnectionCardSummaryContentTest() {
//        deleteEntryFromDb(CRM_WITHDRAWAL_TABLE_NAME, "ucid ='" + client13.getUcid() + "'");
//        deleteEntryFromDb(CRM_DEPOSIT_TABLE_NAME, "ucid ='" + client13.getUcid() + "'");
//        deleteEntryFromDb(MT4_TRADES_COERCED_TABLE_NAME, "ucid ='" + client13.getUcid() + "'");
//        CrmTbAccountObject account = generateStaticCrmTbAccountActive(client13);
//        CrmTbWithdrawalObject withdrawalObject = generateWithdrawalByClient(client13);
//        CrmTbDepositObject depositObject = generateDepositByClient(client13);
//        MtMt4TradesCoercedObject trade = generateMt4TradesCoercedRandomized(client13);
//        insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, account);
//        insertObjectToDb(CRM_WITHDRAWAL_TABLE_NAME, withdrawalObject);
//        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, depositObject);
//        insertObjectToDb(MT4_TRADES_COERCED_TABLE_NAME, trade);
//        connectionPage.navigateEnterPage();
//        keycloackPage.loginAsAutotestUser();
//        connectionPage.navigateConnectionTab(client1.getUcid());
//        connectionPage.openConnectionCard(client13.getUcid());
//        connectionPage.checkSummaryRows("Trading", "1 closed deal");
//        connectionPage.checkSummaryRows("Total PNL", dfd.format(trade.profitUsd + trade.commissionUsd + trade.storageUsd) + " USD");
//        connectionPage.checkSummaryRows("Deposit", dfd.format(depositObject.amountUsd) + " USD");
//        connectionPage.checkSummaryRows("Withdrawal", dfd.format(withdrawalObject.amountUsd - withdrawalObject.reversedAmountUsd) + " USD");
//    }
//
//    @Test
//    @Tag(TEAM_BACKOFFICE)
//    @Tag(LAYER_WEB)
//    @AllureId("557")
//    @DisplayName("Test connection card content Header")
//    void csPageConnectionCardHeaderContentTest() {
//        connectionPage.navigateEnterPage();
//        keycloackPage.loginAsAutotestUser();
//        connectionPage.navigateConnectionTab(client1.getUcid());
//        connectionPage.openConnectionCard(client13.getUcid());
//        connectionPage.ccCheckHeaderClientName("Connect Threerteenhman");
//        connectionPage.ccCheckHeaderClientId(String.valueOf(client13.getUserId()));
//        connectionPage.ccCheckHeaderConnectionLevel("2");
//        connectionPage.ccCheckHeaderConnectionPoints("0.16");
//    }
//
//    @Test
//    @Tag(TEAM_BACKOFFICE)
//    @Tag(LAYER_WEB)
//    @AllureId("312")
//    @DisplayName("Connection Search clients frauds must be taken from DB")
//    void csGraphPageConnectionHaveFraudsFromDBTest() throws Exception {
//        connectionPage.navigateEnterPage();
//        keycloackPage.loginAsAutotestUser();
//        for (FraudTypeOld i : FraudTypeOld.values()) {
//            deleteUserFraudsCh(client4.getUcid());
//            createClientFraudsCh(client4.getUcid(), i.getKey());
//            connectionPage.navigateConnectionTab(client1.getUcid());
//            connectionPage.checkClientStatus(client4.getUcid(), i.getDisplayName());
//        }
//    }
//
//    @Test
//    @Tag(TEAM_BACKOFFICE)
//    @Tag(LAYER_WEB)
//    @AllureId("316")
//    @DisplayName("Connection Search Name of clients must be taken from the DB")
//    void csPageConnectionNodeHasNameFromDbTest() throws ReflectiveOperationException, SQLException {
//        connectionPage.navigateEnterPage();
//        keycloackPage.loginAsAutotestUser();
//        connectionPage.navigateConnectionTab(client1.getUcid());
//        connectionPage.checkClientName(client4.getUcid(), "Connect Fourthman");
//    }
//
//    @Test
//    @Tag(TEAM_BACKOFFICE)
//    @Tag(LAYER_WEB)
//    @AllureId("724")
//    @DisplayName("Connection Search Connection Graph user can see data from connection info on Attribute Card")
//    void csAttributeCardHaveAttributeDataFromDbTest() {
//
//        connectionPage.navigateEnterPage();
//        keycloackPage.loginAsAutotestUser();
//        connectionPage.navigateConnectionTab(client1.getUcid());
//        connectionPage.ccClickAttributeChevron(client13.getUcid());
//        Allure.step("list of user connection attributes must unfolds");
//        connectionPage.ccClickAttributeCardButton(client13.getUcid(), "phoneNumber");
//        connectionPage.checkAttributeCardTitle("phoneNumber");
//        connectionPage.checkAttributeCardSourceName("Connect Threerteenhman");
//        connectionPage.checkAttributeCardConnectedName("phoneNumber", "Connect Tenthman");
//        connectionPage.checkAttributeCardFieldsValues("phoneNumber", "Match", "similar");
//        connectionPage.checkAttributeCardFieldsValues("phoneNumber", "Value", "B*********=");
//        connectionPage.ccClickAttributeCardButton(client13.getUcid(), "payoutId");
//        connectionPage.checkAttributeCardTitle("payoutId");
//        connectionPage.checkAttributeCardSourceName("Connect Threerteenhman");
//        connectionPage.checkAttributeCardConnectedName("payoutId", "Connect Fifteenhman");
//        connectionPage.checkAttributeCardFieldsValues("payoutId", "Match", "exact");
//        connectionPage.checkAttributeCardFieldsValues("payoutId", "Value", "535456**** **0344");
//    }
//
//    @Test
//    @Tag(TEAM_BACKOFFICE)
//    @Tag(LAYER_WEB)
//    @AllureId("725")
//    @DisplayName("Connection Search Connection Graph user can unmask data on Attribute Card")
//    void csAttributeCardCanBeUnmaskedTest() {
//        connectionPage.navigateEnterPage();
//        keycloackPage.loginAsAutotestUser();
//        connectionPage.navigateConnectionTab(client1.getUcid());
//        connectionPage.ccClickAttributeChevron(client13.getUcid());
//        Allure.step("list of user connection attributes must unfolds");
//        connectionPage.ccClickAttributeCardButton(client13.getUcid(), "phoneNumber");
//        connectionPage.checkAttributeCardTitle("phoneNumber");
//        connectionPage.checkThatMaskedTextIsVisible();
//        connectionPage.toggleAttributeCardMask();
//        connectionPage.checkThatMaskedTextIsNotVisible();
//        connectionPage.toggleAttributeCardMask();
//        connectionPage.checkThatMaskedTextIsVisible();
//    }
//}
