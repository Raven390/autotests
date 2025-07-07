package tests.vindex_backoffice_ui_tests.investigationTool.connectionSearch;

import business_objects.db.clickhouse.connection_table.ConnectionTableEntry;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalObject;
import business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObject;
import com.microsoft.playwright.Page;
import helpers.data.ClientHelper;
import helpers.data.enums.Brand;
import helpers.data.enums.FraudTypeOld;
import helpers.data.enums.Regulator;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Muted;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

import static business_objects.db.clickhouse.client_fraud_types.ClientFraudTypesFactory.createClientFraudsCh;
import static business_objects.db.clickhouse.client_fraud_types.ClientFraudTypesFactory.deleteUserFraudsCh;
import static business_objects.db.clickhouse.connection_table.ConnectionTableEntryFactory.getConnectionTableEntry;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateStaticCrmTbAccountActive;
import static business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositObjectFactory.generateDepositByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalObjectFactory.generateWithdrawalByClient;
import static business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoercedRandomized;
import static helpers.database.DbHelper.*;
import static helpers.database.DbHelper.insertObjectsToDb;
import static helpers.kafka.alerts.CreateSimpleAlert.createSimpleAlert;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.Constants.*;
import static utils.Utils.*;

@Tag(TAG_MANUAL)
@Disabled
@Muted
public class ConnectionSearchOldTest extends TestBaseWeb {


    DecimalFormat dfd = new DecimalFormat("#####,###.##");

    static ClientHelper client1 = new ClientHelper(42_424_201, "063cde3b-ea9d-48b5-8e2c-99f3d5f67999", Brand.VANTAGE, Regulator.VFSC2, 424_201_001, 42);
    static ClientHelper client2 = new ClientHelper(42_424_202, "063cde3b-ea9d-48b5-8e2c-99f3d5f67999", Brand.VANTAGE, Regulator.VFSC2, 424_202_001, 42);
    static ClientHelper client3 = new ClientHelper(42_424_203, "063cde3b-ea9d-48b5-8e2c-99f3d5f67999", Brand.VANTAGE, Regulator.VFSC2, 424_203_001, 42);
    static ClientHelper client4 = new ClientHelper(42_424_204, "063cde3b-ea9d-48b5-8e2c-99f3d5f67999", Brand.VANTAGE, Regulator.VFSC2, 424_204_001, 42);
    static ClientHelper client5 = new ClientHelper(42_424_205, "063cde3b-ea9d-48b5-8e2c-99f3d5f67999", Brand.VANTAGE, Regulator.VFSC2, 424_205_001, 42);
    static ClientHelper client6 = new ClientHelper(42_424_206, "063cde3b-ea9d-48b5-8e2c-99f3d5f67999", Brand.VANTAGE, Regulator.VFSC2, 424_206_001, 42);
    static ClientHelper client7 = new ClientHelper(42_424_207, "063cde3b-ea9d-48b5-8e2c-99f3d5f67999", Brand.VANTAGE, Regulator.VFSC2, 424_207_001, 42);
    static ClientHelper client8 = new ClientHelper(42_424_208, "063cde3b-ea9d-48b5-8e2c-99f3d5f67999", Brand.VANTAGE, Regulator.VFSC2, 424_208_001, 42);
    static ClientHelper client9 = new ClientHelper(42_424_209, "063cde3b-ea9d-48b5-8e2c-99f3d5f67999", Brand.VANTAGE, Regulator.VFSC2, 424_209_001, 42);
    static ClientHelper client10 = new ClientHelper(42_424_210, "063cde3b-ea9d-48b5-8e2c-99f3d5f67999", Brand.VANTAGE, Regulator.VFSC2, 424_210_001, 42);
    static ClientHelper client11 = new ClientHelper(42_424_211, "063cde3b-ea9d-48b5-8e2c-99f3d5f67999", Brand.VANTAGE, Regulator.VFSC2, 424_211_001, 42);
    static ClientHelper client12 = new ClientHelper(42_424_212, "063cde3b-ea9d-48b5-8e2c-99f3d5f67999", Brand.VANTAGE, Regulator.VFSC2, 424_212_001, 42);
    static ClientHelper client13 = new ClientHelper(42_424_213, "063cde3b-ea9d-48b5-8e2c-99f3d5f67999", Brand.VANTAGE, Regulator.VFSC2, 424_213_001, 42);
    static ClientHelper client14 = new ClientHelper(42_424_214, "063cde3b-ea9d-48b5-8e2c-99f3d5f67999", Brand.VANTAGE, Regulator.VFSC2, 424_214_001, 42);
    static ClientHelper client15 = new ClientHelper(42_424_215, "063cde3b-ea9d-48b5-8e2c-99f3d5f67999", Brand.VANTAGE, Regulator.VFSC2, 424_215_001, 42);

    @BeforeAll
    static void setup() throws Exception {
        CrmTbUserObject client1DB = generateStaticUserByClient(client1);
        client1DB.firstName = "Connect";
        client1DB.lastName = "Firstman";
        CrmTbUserObject client2DB = generateStaticUserByClient(client2);
        client2DB.firstName = "Connect";
        client2DB.lastName = "Secondman";
        CrmTbUserObject client3DB = generateStaticUserByClient(client3);
        client3DB.firstName = "Connect";
        client3DB.lastName = "Thrirdman";
        CrmTbUserObject client4DB = generateStaticUserByClient(client4);
        client4DB.firstName = "Connect";
        client4DB.lastName = "Fourthman";
        CrmTbUserObject client5DB = generateStaticUserByClient(client5);
        client5DB.firstName = "Connect";
        client5DB.lastName = "Fifthmsn";
        CrmTbUserObject client6DB = generateStaticUserByClient(client6);
        client6DB.firstName = "Connect";
        client6DB.lastName = "Sixthman";
        CrmTbUserObject client7DB = generateStaticUserByClient(client7);
        client7DB.firstName = "Connect";
        client7DB.lastName = "Seventhman";
        CrmTbUserObject client8DB = generateStaticUserByClient(client8);
        client8DB.firstName = "Connect";
        client8DB.lastName = "Eightman";
        CrmTbUserObject client9DB = generateStaticUserByClient(client9);
        client9DB.firstName = "Connect";
        client9DB.lastName = "Ninthman";
        CrmTbUserObject client10DB = generateStaticUserByClient(client10);
        client10DB.firstName = "Connect";
        client10DB.lastName = "Tenthman";
        CrmTbUserObject client11DB = generateStaticUserByClient(client11);
        client11DB.firstName = "Connect";
        client11DB.lastName = "Elewenthman";
        CrmTbUserObject client12DB = generateStaticUserByClient(client12);
        client12DB.firstName = "Connect";
        client12DB.lastName = "Twelwthman";
        CrmTbUserObject client13DB = generateStaticUserByClient(client13);
        client13DB.firstName = "Connect";
        client13DB.lastName = "Threerteenhman";
        CrmTbUserObject client14DB = generateStaticUserByClient(client14);
        client14DB.firstName = "Connect";
        client14DB.lastName = "Fourteenman";
        CrmTbUserObject client15DB = generateStaticUserByClient(client15);
        client15DB.firstName = "Connect";
        client15DB.lastName = "Fifteenhman";

        ConnectionTableEntry connection1 = getConnectionTableEntry(client1, client2, 0.01);
        ConnectionTableEntry connection2 = getConnectionTableEntry(client1, client3, 0.16);
        ConnectionTableEntry connection3 = getConnectionTableEntry(client1, client4, 0.17);
        ConnectionTableEntry connection4 = getConnectionTableEntry(client1, client5, 0.33);
        ConnectionTableEntry connection5 = getConnectionTableEntry(client1, client6, 0.34);
        ConnectionTableEntry connection6 = getConnectionTableEntry(client1, client7, 0.49);
        ConnectionTableEntry connection7 = getConnectionTableEntry(client1, client8, 0.50);
        ConnectionTableEntry connection8 = getConnectionTableEntry(client1, client9, 0.66);
        ConnectionTableEntry connection9 = getConnectionTableEntry(client1, client10, 0.67);
        ConnectionTableEntry connection10 = getConnectionTableEntry(client1, client11, 0.83);
        ConnectionTableEntry connection11 = getConnectionTableEntry(client1, client12, 0.84);
        ConnectionTableEntry connection12 = getConnectionTableEntry(client10, client13, 0.16);
        connection12.connectionInfo = "[{\"connectionAttributeName\": \"phone\", \"connectionAttributeValue\": \"cTsGbMYzhsD5SxSOhmgpmQ==\", \"sourceAttributeValue\": \"BjrbbdAHkwhBFLnPclfvbg==\", \"relationType\": \"similar\"}]";
        ConnectionTableEntry connection13 = getConnectionTableEntry(client13, client15, 0.16);

        String list = ucidListDbFormat(client1, client2, client3, client4, client5, client6, client7, client8, client9, client10, client11, client12, client13, client14, client15);
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, "user_from in (" + list + ")");

        insertObjectsToDb(CRM_USER_TABLE_NAME, List.of(client1DB, client2DB, client3DB, client4DB, client5DB, client6DB, client7DB, client8DB, client9DB, client10DB, client11DB, client12DB, client13DB, client14DB, client15DB));
        insertConnectionToDb(connection1, connection2, connection3, connection4, connection5, connection6, connection7, connection8, connection9, connection10, connection11, connection12, connection13);
    }

//    @AfterAll
//    public static void tearDown() throws Exception {
//    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("315")
    @DisplayName("Check line width")
    void csPageConnectionLinesStileTest() {
        connectionPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        connectionPage.navigateConnectionTab(client1.getUcid());
        connectionPage.checkLineStyle(client1.getUcid(), client2.getUcid(), 0.01);
        connectionPage.checkLineStyle(client1.getUcid(), client3.getUcid(), 0.16);
        connectionPage.checkLineStyle(client1.getUcid(), client4.getUcid(), 0.17);
        connectionPage.checkLineStyle(client1.getUcid(), client5.getUcid(), 0.33);
        connectionPage.checkLineStyle(client1.getUcid(), client6.getUcid(), 0.34);
        connectionPage.checkLineStyle(client1.getUcid(), client7.getUcid(), 0.49);
        connectionPage.checkLineStyle(client1.getUcid(), client8.getUcid(), 0.50);
        connectionPage.checkLineStyle(client1.getUcid(), client9.getUcid(), 0.66);
        connectionPage.checkLineStyle(client1.getUcid(), client10.getUcid(), 0.67);
        connectionPage.checkLineStyle(client1.getUcid(), client11.getUcid(), 0.83);
        connectionPage.checkLineStyle(client1.getUcid(), client12.getUcid(), 0.84);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("311")
    @DisplayName("Check that connection node have right client name")
    void csPageConnectionNodesHaveRightClientNamesTest() {
        connectionPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        connectionPage.navigateConnectionTab(client1.getUcid());
        connectionPage.checkClientNodeText(client1.getUcid(), "Connect Firstman");
        connectionPage.checkClientNodeText(client2.getUcid(), "Connect Secondman");
        connectionPage.checkClientNodeText(client3.getUcid(), "Connect Thrirdman");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("311")
    @DisplayName("Check that connection node have right client status")
    void csPageConnectionNodesHaveRightClientStatusTest() throws Exception {
        deleteUserFraudsCh(client1.getUcid());
        createSimpleAlert(client1.getUcid(), "HEDGING");
        deleteUserFraudsCh(client2.getUcid());
        deleteUserFraudsCh(client3.getUcid());
        createClientFraudsCh(client3.getUcid(), FraudTypeOld.GAP_TRADING.getKey());
        createClientFraudsCh(client3.getUcid(), FraudTypeOld.LATENCY_ARBITRAGE.getKey());
        connectionPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        connectionPage.navigateConnectionTab(client1.getUcid());
        connectionPage.checkClientStatus(client1.getUcid(), "Suspicious");
        connectionPage.checkClientStatus(client2.getUcid(), "Normal");
        connectionPage.checkClientStatus(client3.getUcid(), "Latency arbitrage, Gap trading");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("518")
    @DisplayName("Check that connection table opens")
    void csPageConnectionTableOpensTest() {
        connectionPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        connectionPage.navigateConnectionTab(client1.getUcid());
        connectionPage.openConnectionTable();
        connectionPage.connectionTableIsRendered();
    }

    @Disabled("need update")
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("526")
    @DisplayName("Check that sorting works")
    void csPageConnectionTableSortTest() {
        connectionPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        connectionPage.navigateConnectionTab(client1.getUcid());
        connectionPage.openConnectionTable();
        connectionPage.verifyTableSorting();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("520")
    @DisplayName("Connection Search connection search tab can switches between graph and table mode")
    void csPageConnectionTableSwitchesBackToGraphTest() {
        connectionPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        connectionPage.navigateConnectionTab(client1.getUcid());
        connectionPage.openConnectionTable();
        connectionPage.openConnectionGraph();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("521")
    @DisplayName("Test the same user save highlighted state between different modes of the connection search when you switches view mode")
    void csPageConnectionSelectionSwitchViewTest() {
        connectionPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        connectionPage.navigateConnectionTab(client1.getUcid());
        connectionPage.openConnectionTable();
        connectionPage.checkSelection(client2.getUserId(), client2.getUcid());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("527")
    @DisplayName("Test the same user save highlighted state between different modes of the connection search when you click link button")
    void csPageConnectionSelectionLinkButtonTest() {
        connectionPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        connectionPage.navigateConnectionTab(client1.getUcid());
        connectionPage.openConnectionTable();
        connectionPage.checkSelectionTransitByLinkButton(client2.getUserId(), client2.getUcid());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("523")
    @DisplayName("Connection Search. User can go to clients card from connection table")
    void csPageGoToClientCardTest() {
        connectionPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        connectionPage.navigateConnectionTab(client1.getUcid());
        connectionPage.openConnectionTable();
        Page newPage = context.waitForPage(() -> {
            connectionPage.linkToCard(String.valueOf(client2.getUserId()));
        });
        int count = 0;
        while (newPage.url() == null && count < 50) {
            newPage.waitForTimeout(500);
            count += 1;
        }
        System.out.println("page URL is: " + newPage.url().toString());
        assertTrue(newPage.url().contains(client2.getUcid()));
        newPage.close();

    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("556")
    @DisplayName("Connection search Connection Card user can open clients page from the connection card")
    void csPageGoToClientCardFromConnectionCardTest() {
        connectionPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        connectionPage.navigateConnectionTab(client1.getUcid());
        connectionPage.openConnectionCard(client2.getUcid());
        Page newPage = context.waitForPage(() -> {
            connectionPage.clickConnectionLinkCc();
        });
        int count = 0;
        while (newPage.url() == null && count < 50) {
            newPage.waitForTimeout(500);
            count += 1;
        }
        System.out.println("page URL is: " + newPage.url().toString());
        assertTrue(newPage.url().contains(client2.getUcid()));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("555")
    @DisplayName("Test connection card content Direct Connections  info")
    void csPageConnectionCardDirectConnectionContentTest() {
        connectionPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        connectionPage.navigateConnectionTab(client1.getUcid());
        connectionPage.openConnectionCard(client15.getUcid());
        connectionPage.checkDirectConnectionRows("Connect Threerteenhman", "Type", "Same Person");
        connectionPage.checkDirectConnectionRows("Connect Threerteenhman", "Score", "0.16");
        connectionPage.checkDirectConnectionRows("Connect Threerteenhman", "payoutId", "535456**** **0344");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("554")
    @DisplayName("Test connection card content General info")
    void csPageConnectionCardGeneralInfoContentTest() {
        connectionPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        connectionPage.navigateConnectionTab(client1.getUcid());
        connectionPage.openConnectionCard(client6.getUcid());
        connectionPage.checkGeneralInfoRows("Brand", client6.getBrand());
        connectionPage.checkGeneralInfoRows("Country", "Cyprus");
        connectionPage.checkGeneralInfoRows("Email", "t***4@example.com");
        connectionPage.checkGeneralInfoRows("CPA", "2");
        connectionPage.checkGeneralInfoRows("Registered", "2014-10-23");
        connectionPage.checkGeneralInfoRows("Last login", "-");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("547")
    @DisplayName("Test connection card content Summary")
    void csPageConnectionCardSummaryContentTest() {
        deleteEntryFromDb(CRM_WITHDRAWAL_TABLE_NAME, "ucid ='" + client13.getUcid() + "'");
        deleteEntryFromDb(CRM_DEPOSIT_TABLE_NAME, "ucid ='" + client13.getUcid() + "'");
        deleteEntryFromDb(MT4_TRADES_COERCED_TABLE_NAME, "ucid ='" + client13.getUcid() + "'");
        CrmTbAccountObject account = generateStaticCrmTbAccountActive(client13);
        CrmTbWithdrawalObject withdrawalObject = generateWithdrawalByClient(client13);
        CrmTbDepositObject depositObject = generateDepositByClient(client13);
        MtMt4TradesCoercedObject trade = generateMt4TradesCoercedRandomized(client13);
        insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, account);
        insertObjectToDb(CRM_WITHDRAWAL_TABLE_NAME, withdrawalObject);
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, depositObject);
        insertObjectToDb(MT4_TRADES_COERCED_TABLE_NAME, trade);
        connectionPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        connectionPage.navigateConnectionTab(client1.getUcid());
        connectionPage.openConnectionCard(client13.getUcid());
        connectionPage.checkSummaryRows("Trading", "1 closed deal");
        connectionPage.checkSummaryRows("Total PNL", dfd.format(trade.profitUsd + trade.commissionUsd + trade.storageUsd) + " USD");
        connectionPage.checkSummaryRows("Deposit", dfd.format(depositObject.amountUsd) + " USD");
        connectionPage.checkSummaryRows("Withdrawal", dfd.format(withdrawalObject.amountUsd - withdrawalObject.reversedAmountUsd) + " USD");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("557")
    @DisplayName("Test connection card content Header")
    void csPageConnectionCardHeaderContentTest() {
        connectionPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        connectionPage.navigateConnectionTab(client1.getUcid());
        connectionPage.openConnectionCard(client13.getUcid());
        connectionPage.ccCheckHeaderClientName("Connect Threerteenhman");
        connectionPage.ccCheckHeaderClientId(String.valueOf(client13.getUserId()));
        connectionPage.ccCheckHeaderConnectionLevel("2");
        connectionPage.ccCheckHeaderConnectionPoints("0.16");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("312")
    @DisplayName("Connection Search clients frauds must be taken from DB")
    void csGraphPageConnectionHaveFraudsFromDBTest() throws Exception {
        connectionPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        for (FraudTypeOld i : FraudTypeOld.values()) {
            deleteUserFraudsCh(client4.getUcid());
            createClientFraudsCh(client4.getUcid(), i.getKey());
            connectionPage.navigateConnectionTab(client1.getUcid());
            connectionPage.checkClientStatus(client4.getUcid(), i.getDisplayName());
        }
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("316")
    @DisplayName("Connection Search Name of clients must be taken from the DB")
    void csPageConnectionNodeHasNameFromDbTest() throws ReflectiveOperationException, SQLException {
        connectionPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        connectionPage.navigateConnectionTab(client1.getUcid());
        connectionPage.checkClientName(client4.getUcid(), "Connect Fourthman");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("724")
    @DisplayName("Connection Search Connection Graph user can see data from connection info on Attribute Card")
    void csAttributeCardHaveAttributeDataFromDbTest() {

        connectionPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        connectionPage.navigateConnectionTab(client1.getUcid());
        connectionPage.ccClickAttributeChevron(client13.getUcid());
        Allure.step("list of user connection attributes must unfolds");
        connectionPage.ccClickAttributeCardButton(client13.getUcid(), "phoneNumber");
        connectionPage.checkAttributeCardTitle("phoneNumber");
        connectionPage.checkAttributeCardSourceName("Connect Threerteenhman");
        connectionPage.checkAttributeCardConnectedName("phoneNumber", "Connect Tenthman");
        connectionPage.checkAttributeCardFieldsValues("phoneNumber", "Match", "similar");
        connectionPage.checkAttributeCardFieldsValues("phoneNumber", "Value", "B*********=");
        connectionPage.ccClickAttributeCardButton(client13.getUcid(), "payoutId");
        connectionPage.checkAttributeCardTitle("payoutId");
        connectionPage.checkAttributeCardSourceName("Connect Threerteenhman");
        connectionPage.checkAttributeCardConnectedName("payoutId", "Connect Fifteenhman");
        connectionPage.checkAttributeCardFieldsValues("payoutId", "Match", "exact");
        connectionPage.checkAttributeCardFieldsValues("payoutId", "Value", "535456**** **0344");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("725")
    @DisplayName("Connection Search Connection Graph user can unmask data on Attribute Card")
    void csAttributeCardCanBeUnmaskedTest() {
        connectionPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        connectionPage.navigateConnectionTab(client1.getUcid());
        connectionPage.ccClickAttributeChevron(client13.getUcid());
        Allure.step("list of user connection attributes must unfolds");
        connectionPage.ccClickAttributeCardButton(client13.getUcid(), "phoneNumber");
        connectionPage.checkAttributeCardTitle("phoneNumber");
        connectionPage.checkThatMaskedTextIsVisible();
        connectionPage.toggleAttributeCardMask();
        connectionPage.checkThatMaskedTextIsNotVisible();
        connectionPage.toggleAttributeCardMask();
        connectionPage.checkThatMaskedTextIsVisible();
    }
}
