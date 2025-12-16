package tests.vindex_backoffice_ui_tests.investigationTool.trading;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import helpers.data.ClientHelper;
import helpers.data.enums.Brand;
import helpers.data.enums.DateTimeFormat;
import helpers.data.enums.Regulator;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.sql.SQLException;
import java.util.List;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateAdditionalStaticCrmTbAccountActive;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateStaticCrmTbAccountActive;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoercedRandomized;
import static helpers.database.DbHelper.*;
import static utils.Constants.*;
import static utils.Utils.getCurrentTimestampMinusOffsetFormatted;
import static utils.Utils.insertCrmAccountsToDb;

public class TradingTest extends TestBaseWeb {

    private static final ClientHelper client;
    static {
        client = ClientHelper.builder().userId(151_501).uid("e5880ca5-8578-4a1e-969d-7a64716ca41f").brand(Brand.INFINOX).regulator(Regulator.FCA).tradingAccount(151_501_001).tradingAccount2(151_501_002).serverId(42).build();
    }
    private static CrmTbUserObject crmTbUser = generateStaticUserByClient(client);
    private static CrmTbAccountObject account1 = generateStaticCrmTbAccountActive(client);
    private static CrmTbAccountObject account2 = generateAdditionalStaticCrmTbAccountActive(client);
    private static MtAccountObject mtAccount1 = generateMtAccountByCrmTbAccount(account1);
    private static MtAccountObject mtAccount2 = generateMtAccountByCrmTbAccount(account2);

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        crmTbUser.firstName = "Trading";
        crmTbUser.lastName = "Trademan";
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertCrmAccountsToDb(account1, account2);
        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccount1, mtAccount2));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("241")
    @DisplayName("Test that operations subtab renders all basic elements")
    public void rendersAllBasicElementsTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
        tradingPage.operationsRendersTest();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("505")
    @DisplayName("Test that type filter list contains all necessary types")
    public void testTypeFilterList() {
        deleteObjectFromDb(MT4_TRADES_COERCED_TABLE_NAME, "ucid = '" + client.getUcid() + "'");
        tradingPage.generateDifferentTicketTypes(client);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
        tradingPage.openFilter();
        tradingPage.checkTypeFilterList();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("455")
    @DisplayName("Test that type filter works Sell")
    public void testTypeFilterSell() {
        deleteObjectFromDb(MT4_TRADES_COERCED_TABLE_NAME, "ucid = '" + client.getUcid() + "'");
        tradingPage.generateDifferentTicketTypes(client);

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
        tradingPage.openFilter();
        tradingPage.clickFilterCheckbox("Sell");
        tradingPage.clickApplyButton();
        tradingPage.checkTypeCellsContent("Sell");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("435")
    @DisplayName("Test that type filter works Balance")
    public void testTypeFilterBalance() {
        deleteObjectFromDb(MT4_TRADES_COERCED_TABLE_NAME, "ucid = '" + client.getUcid() + "'");
        tradingPage.generateDifferentTicketTypes(client);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
        tradingPage.openFilter();
        tradingPage.clickFilterCheckbox("Balance");
        tradingPage.clickApplyButton();
        tradingPage.checkTypeCellsContent("Balance");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("451")
    @DisplayName("Test that type filter works Buy")
    public void testTypeFilterBuy() {
        deleteObjectFromDb(MT4_TRADES_COERCED_TABLE_NAME, "ucid = '" + client.getUcid() + "'");
        tradingPage.generateDifferentTicketTypes(client);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
        tradingPage.openFilter();
        tradingPage.clickFilterCheckbox("Buy");
        tradingPage.clickApplyButton();
        tradingPage.checkTypeCellsContent("Buy");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("452")
    @DisplayName("Test that type filter works Buy Limit")
    public void testTypeFilterBuyLimit() {
        deleteObjectFromDb(MT4_TRADES_COERCED_TABLE_NAME, "ucid = '" + client.getUcid() + "'");
        tradingPage.generateDifferentTicketTypes(client);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
        tradingPage.openFilter();
        tradingPage.clickFilterCheckbox("Buy Limit");
        tradingPage.clickApplyButton();
        tradingPage.checkTypeCellsContent("Buy Limit");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("453")
    @DisplayName("Test that type filter works Buy Stop")
    public void testTypeFilterBuyStop() {
        deleteObjectFromDb(MT4_TRADES_COERCED_TABLE_NAME, "ucid = '" + client.getUcid() + "'");
        tradingPage.generateDifferentTicketTypes(client);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
        tradingPage.openFilter();
        tradingPage.clickFilterCheckbox("Buy Stop");
        tradingPage.clickApplyButton();
        tradingPage.checkTypeCellsContent("Buy Stop");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("454")
    @DisplayName("Test that type filter works Credit")
    public void testTypeFilterCredit() {
        deleteObjectFromDb(MT4_TRADES_COERCED_TABLE_NAME, "ucid = '" + client.getUcid() + "'");
        tradingPage.generateDifferentTicketTypes(client);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
        tradingPage.openFilter();
        tradingPage.clickFilterCheckbox("Credit");
        tradingPage.clickApplyButton();
        tradingPage.checkTypeCellsContent("Credit");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("456")
    @DisplayName("Test that type filter works Sell Limit")
    public void testTypeFilterSellLimit() {
        deleteObjectFromDb(MT4_TRADES_COERCED_TABLE_NAME, "ucid = '" + client.getUcid() + "'");
        tradingPage.generateDifferentTicketTypes(client);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
        tradingPage.openFilter();
        tradingPage.clickFilterCheckbox("Sell Limit");
        tradingPage.clickApplyButton();
        tradingPage.checkTypeCellsContent("Sell Limit");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("457")
    @DisplayName("Test that type filter works Sell Stop")
    public void testTypeFilterSellStop() {
        deleteObjectFromDb(MT4_TRADES_COERCED_TABLE_NAME, "ucid = '" + client.getUcid() + "'");
        tradingPage.generateDifferentTicketTypes(client);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
        tradingPage.openFilter();
        tradingPage.clickFilterCheckbox("Sell Stop");
        tradingPage.clickApplyButton();
        tradingPage.checkTypeCellsContent("Sell Stop");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("475")
    @DisplayName("Test that Volume amount filter ")
    public void testVolumeAmountFilter() {
        deleteObjectFromDb(MT4_TRADES_COERCED_TABLE_NAME, "ucid = '" + client.getUcid() + "'");
        MtMt4TradesCoercedObject trade0 = generateMt4TradesCoercedRandomized(client);
        trade0.ticketType = "Sell";
        trade0.notionalValueUsd = 3.9;
        MtMt4TradesCoercedObject trade1 = generateMt4TradesCoercedRandomized(client);
        trade1.ticketType = "Sell";
        trade1.notionalValueUsd = 4.0;
        MtMt4TradesCoercedObject trade2 = generateMt4TradesCoercedRandomized(client);
        trade2.ticketType = "Sell";
        trade2.notionalValueUsd = 4.1;
        MtMt4TradesCoercedObject trade3 = generateMt4TradesCoercedRandomized(client);
        trade3.ticketType = "Sell";
        trade3.notionalValueUsd = 8.0;
        MtMt4TradesCoercedObject trade4 = generateMt4TradesCoercedRandomized(client);
        trade4.ticketType = "Sell";
        trade4.notionalValueUsd = 8.1;
        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade0, trade1, trade2, trade3, trade4));

        int from = 4;
        int to = 8;
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
        tradingPage.openFilter();
        tradingPage.fillVolumeAmountValues(String.valueOf(from), String.valueOf(to));
        tradingPage.clickApplyButton();
        tradingPage.checkVolumeCellsContentUSD(from, to);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1175")
    @Feature("BMS-1364 Trading volume in lots")
    @DisplayName("Test that Volume Lot filter ")
    public void testVolumeLotFilter() {
        deleteObjectFromDb(MT4_TRADES_COERCED_TABLE_NAME, "ucid = '" + client.getUcid() + "'");
        MtMt4TradesCoercedObject trade0 = generateMt4TradesCoercedRandomized(client);
        trade0.ticketType = "Sell";
        trade0.volumeLots = 3.9;
        MtMt4TradesCoercedObject trade1 = generateMt4TradesCoercedRandomized(client);
        trade1.ticketType = "Sell";
        trade1.volumeLots = 4.0;
        MtMt4TradesCoercedObject trade2 = generateMt4TradesCoercedRandomized(client);
        trade2.ticketType = "Sell";
        trade2.volumeLots = 4.1;
        MtMt4TradesCoercedObject trade3 = generateMt4TradesCoercedRandomized(client);
        trade3.ticketType = "Sell";
        trade3.volumeLots = 8.0;
        MtMt4TradesCoercedObject trade4 = generateMt4TradesCoercedRandomized(client);
        trade4.ticketType = "Sell";
        trade4.volumeLots = 8.1;
        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade0, trade1, trade2, trade3, trade4));

        double from = 4.0;
        double to = 8.0;
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
        tradingPage.openFilter();
        tradingPage.fillVolumeLotValues(String.valueOf(from), String.valueOf(to));
        tradingPage.clickApplyButton();
        tradingPage.checkVolumeCellsContentLots(from, to);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("474")
    @DisplayName("Test that Duration filter works")
    public void testDurationFilter() {
        deleteObjectFromDb(MT4_TRADES_COERCED_TABLE_NAME, "ucid = '" + client.getUcid() + "'");
        MtMt4TradesCoercedObject trade0 = generateMt4TradesCoercedRandomized(client);
        trade0.ticketType = "Sell";
        trade0.openTime = "2025-03-17 11:00:00";
        trade0.closeTime = "2025-03-17 11:03:59";
        MtMt4TradesCoercedObject trade1 = generateMt4TradesCoercedRandomized(client);
        trade1.ticketType = "Sell";
        trade1.openTime = "2025-03-17 11:00:00";
        trade1.closeTime = "2025-03-17 11:04:00";
        MtMt4TradesCoercedObject trade2 = generateMt4TradesCoercedRandomized(client);
        trade2.ticketType = "Sell";
        trade2.openTime = "2025-03-17 11:00:00";
        trade2.closeTime = "2025-03-17 11:07:59";
        MtMt4TradesCoercedObject trade3 = generateMt4TradesCoercedRandomized(client);
        trade3.ticketType = "Sell";
        trade3.openTime = "2025-03-17 11:00:00";
        trade3.closeTime = "2025-03-17 11:08:00";
        MtMt4TradesCoercedObject trade4 = generateMt4TradesCoercedRandomized(client);
        trade4.ticketType = "Sell";
        trade4.openTime = "2025-03-17 11:00:00";
        trade4.closeTime = "2025-03-17 11:08:01";
        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade0, trade1, trade2, trade3, trade4));
        int from = 4;
        int to = 8;
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
        tradingPage.openFilter();
        tradingPage.fillDurationValues(String.valueOf(from), String.valueOf(to));
        tradingPage.clickApplyButton();
        tradingPage.checkDatesMinutes(from, to);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("476")
    @DisplayName("Test that method filter works API")
    public void testMethodFilterAPI() {
        deleteObjectFromDb(MT4_TRADES_COERCED_TABLE_NAME, "ucid = '" + client.getUcid() + "'");
        tradingPage.generateDifferentReason(client);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
        tradingPage.openFilter();
        tradingPage.clickFilterCheckbox("API");
        tradingPage.clickApplyButton();
        tradingPage.checkMethodCellsContent("API");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("477")
    @DisplayName("Test that method filter works Client")
    public void testMethodFilterClient() {
        deleteObjectFromDb(MT4_TRADES_COERCED_TABLE_NAME, "ucid = '" + client.getUcid() + "'");
        tradingPage.generateDifferentReason(client);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
        tradingPage.openFilter();
        tradingPage.clickFilterCheckbox("Client");
        tradingPage.clickApplyButton();
        tradingPage.checkMethodCellsContent("Client");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("478")
    @DisplayName("Test that method filter works Dealer")
    public void testMethodFilterDealer() {
        deleteObjectFromDb(MT4_TRADES_COERCED_TABLE_NAME, "ucid = '" + client.getUcid() + "'");
        tradingPage.generateDifferentReason(client);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
        tradingPage.openFilter();
        tradingPage.clickFilterCheckbox("Dealer");
        tradingPage.clickApplyButton();
        tradingPage.checkMethodCellsContent("Dealer");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("479")
    @DisplayName("Test that method filter works Expert")
    public void testMethodFilterExpert() {
        deleteObjectFromDb(MT4_TRADES_COERCED_TABLE_NAME, "ucid = '" + client.getUcid() + "'");
        tradingPage.generateDifferentReason(client);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
        tradingPage.openFilter();
        tradingPage.clickFilterCheckbox("Expert");
        tradingPage.clickApplyButton();
        tradingPage.checkMethodCellsContent("Expert");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("480")
    @DisplayName("Test that method filter works Gateway")
    public void testMethodFilterGateway() {
        deleteObjectFromDb(MT4_TRADES_COERCED_TABLE_NAME, "ucid = '" + client.getUcid() + "'");
        tradingPage.generateDifferentReason(client);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
        tradingPage.openFilter();
        tradingPage.clickFilterCheckbox("Gateway");
        tradingPage.clickApplyButton();
        tradingPage.checkMethodCellsContent("Gateway");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("481")
    @DisplayName("Test that method filter works Mobile")
    public void testMethodFilterMobile() {
        deleteObjectFromDb(MT4_TRADES_COERCED_TABLE_NAME, "ucid = '" + client.getUcid() + "'");
        tradingPage.generateDifferentReason(client);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
        tradingPage.openFilter();
        tradingPage.clickFilterCheckbox("Mobile");
        tradingPage.clickApplyButton();
        tradingPage.checkMethodCellsContent("Mobile");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("482")
    @DisplayName("Test that method filter works Signal")
    public void testMethodFilterSignal() {
        deleteObjectFromDb(MT4_TRADES_COERCED_TABLE_NAME, "ucid = '" + client.getUcid() + "'");
        tradingPage.generateDifferentReason(client);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
        tradingPage.openFilter();
        tradingPage.clickFilterCheckbox("Signal");
        tradingPage.clickApplyButton();
        tradingPage.checkMethodCellsContent("Signal");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("483")
    @DisplayName("Test that method filter works Web")
    public void testMethodFilterWeb() {
        deleteObjectFromDb(MT4_TRADES_COERCED_TABLE_NAME, "ucid = '" + client.getUcid() + "'");
        tradingPage.generateDifferentReason(client);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
        tradingPage.openFilter();
        tradingPage.clickFilterCheckbox("Web");
        tradingPage.clickApplyButton();
        tradingPage.checkMethodCellsContent("Web");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("472")
    @DisplayName("Test that Profit filter works")
    public void testProfitFilter() {

        deleteObjectFromDb(MT4_TRADES_COERCED_TABLE_NAME, "ucid = '" + client.getUcid() + "'");
        MtMt4TradesCoercedObject trade0 = generateMt4TradesCoercedRandomized(client);
        trade0.ticketType = "Sell";
        trade0.profitUsd = 3.99;
        MtMt4TradesCoercedObject trade1 = generateMt4TradesCoercedRandomized(client);
        trade1.ticketType = "Sell";
        trade1.profitUsd = 4.00;
        MtMt4TradesCoercedObject trade2 = generateMt4TradesCoercedRandomized(client);
        trade2.ticketType = "Sell";
        trade2.profitUsd = 79.99;
        MtMt4TradesCoercedObject trade3 = generateMt4TradesCoercedRandomized(client);
        trade3.ticketType = "Sell";
        trade3.profitUsd = 80.00;
        MtMt4TradesCoercedObject trade4 = generateMt4TradesCoercedRandomized(client);
        trade4.ticketType = "Sell";
        trade4.profitUsd = 80.01;
        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade0, trade1, trade2, trade3, trade4));

        int from = 4;
        int to = 80;
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
        tradingPage.openFilter();
        tradingPage.fillProfitValues(String.valueOf(from), String.valueOf(to));
        tradingPage.clickApplyButton();
        tradingPage.checkProfitCellsContent(from, to);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("472")
    @DisplayName("Test that profit filter works with negative")

    public void testProfitFilterNegativeValuesTest() {
        deleteObjectFromDb(MT4_TRADES_COERCED_TABLE_NAME, "ucid = '" + client.getUcid() + "'");
        MtMt4TradesCoercedObject trade0 = generateMt4TradesCoercedRandomized(client);
        trade0.ticketType = "Sell";
        trade0.profitUsd = -37.99;
        insertObjectToDb(MT4_TRADES_COERCED_TABLE_NAME, trade0);
        int from = -40;
        int to = -35;
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
        tradingPage.openFilter();
        tradingPage.fillProfitValues(String.valueOf(from), String.valueOf(to));
        tradingPage.clickApplyButton();
        tradingPage.checkProfitCellsContentFirst(-37.99);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("474")
    @Feature("BMS-1080 Highlight HFT deals")
    @DisplayName("Test that Highlight HFT works")
    public void testHighlightHftDeals() {
        //case1 trade duration 0
        deleteObjectFromDb(MT4_TRADES_COERCED_TABLE_NAME, "ucid = '" + client.getUcid() + "'");
        MtMt4TradesCoercedObject trade0 = generateMt4TradesCoercedRandomized(client);
        trade0.ticketType = "Buy";
        trade0.closeTime = getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 0, 0, 0);
        trade0.openTime = trade0.closeTime;
        Allure.step("prepare trade with duration 0");
        insertObjectToDb(MT4_TRADES_COERCED_TABLE_NAME, trade0);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
        tradingPage.checkCountNotHighlightedRows(1);
        tradingPage.checkCountHighlightedRows(0);
        tradingPage.enabledHftButton();
        tradingPage.checkCountNotHighlightedRows(0);
        tradingPage.checkCountHighlightedRows(1);
        tradingPage.disableHftButton();
        tradingPage.checkCountNotHighlightedRows(1);
        tradingPage.checkCountHighlightedRows(0);
        //case2 trade duration 10 minutes
        deleteObjectFromDb(MT4_TRADES_COERCED_TABLE_NAME, "ucid = '" + client.getUcid() + "'");
        MtMt4TradesCoercedObject trade1 = generateMt4TradesCoercedRandomized(client);
        trade1.ticketType = "Buy";
        trade1.closeTime = getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 0, 0, 0);
        trade1.openTime = getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 0, 10, 0);
        Allure.step("prepare trade with duration 10 minutes");
        insertObjectToDb(MT4_TRADES_COERCED_TABLE_NAME, trade1);
        Allure.step("reload Operations page");
        tradingPage.navigateOperations(client.getUcid());
        tradingPage.checkCountNotHighlightedRows(1);
        tradingPage.checkCountHighlightedRows(0);
        tradingPage.enabledHftButton();
        tradingPage.checkCountNotHighlightedRows(0);
        tradingPage.checkCountHighlightedRows(1);
        tradingPage.disableHftButton();
        tradingPage.checkCountNotHighlightedRows(1);
        tradingPage.checkCountHighlightedRows(0);
        //case3 trade duration 10 minutes 1 second
        deleteObjectFromDb(MT4_TRADES_COERCED_TABLE_NAME, "ucid = '" + client.getUcid() + "'");
        MtMt4TradesCoercedObject trade2 = generateMt4TradesCoercedRandomized(client);
        trade2.ticketType = "Buy";
        trade2.closeTime = getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 0, 0, 0);
        trade2.openTime = getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 0, 10, 1);
        Allure.step("prepare trade with duration 10 minutes 1 second");
        insertObjectToDb(MT4_TRADES_COERCED_TABLE_NAME, trade2);
        Allure.step("reload Operations page");
        tradingPage.navigateOperations(client.getUcid());
        tradingPage.checkCountNotHighlightedRows(1);
        tradingPage.checkCountHighlightedRows(0);
        tradingPage.enabledHftButton();
        tradingPage.checkCountNotHighlightedRows(1);
        tradingPage.checkCountHighlightedRows(0);
        tradingPage.disableHftButton();
        tradingPage.checkCountNotHighlightedRows(1);
        tradingPage.checkCountHighlightedRows(0);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1176")
    @DisplayName("Test that multiple filters not return empty state")
    public void testMultipleFiltersShowResult() {

        deleteObjectFromDb(MT4_TRADES_COERCED_TABLE_NAME, "ucid = '" + client.getUcid() + "'");
        MtMt4TradesCoercedObject trade0 = generateMt4TradesCoercedRandomized(client);
        trade0.ticketType = "Sell";
        trade0.profitUsd = 4.99;
        trade0.openTime = "2025-03-17 11:00:00";
        trade0.closeTime = "2025-03-17 11:03:59";
        trade0.notionalValueUsd = 390.0;
        trade0.volumeLots = 3.9;
        MtMt4TradesCoercedObject trade1 = generateMt4TradesCoercedRandomized(client);
        trade1.ticketType = "Sell";
        trade1.profitUsd = 5.00;
        trade1.openTime = "2025-03-17 11:00:00";
        trade1.closeTime = "2025-03-17 11:04:00";
        trade1.notionalValueUsd = 400.0;
        trade1.volumeLots = 4.0;
        MtMt4TradesCoercedObject trade2 = generateMt4TradesCoercedRandomized(client);
        trade2.ticketType = "Sell";
        trade2.profitUsd = 79.99;
        trade2.openTime = "2025-03-17 11:00:00";
        trade2.closeTime = "2025-03-17 11:07:59";
        trade2.notionalValueUsd = 410.0;
        trade2.volumeLots = 4.1;
        MtMt4TradesCoercedObject trade3 = generateMt4TradesCoercedRandomized(client);
        trade3.ticketType = "Sell";
        trade3.profitUsd = 80.00;
        trade3.openTime = "2025-03-17 11:00:00";
        trade3.closeTime = "2025-03-17 11:08:00";
        trade3.notionalValueUsd = 800.0;
        MtMt4TradesCoercedObject trade4 = generateMt4TradesCoercedRandomized(client);
        trade4.ticketType = "Sell";
        trade4.profitUsd = 800.1;
        trade4.openTime = "2025-03-17 11:00:00";
        trade4.closeTime = "2025-03-17 11:08:01";
        trade4.notionalValueUsd = 810.0;
        trade4.volumeLots = 8.1;
        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade0, trade1, trade2, trade3, trade4));

        int profitUSDFrom = 5;
        int profitUSDTo = 80;
        int durationFrom = 4;
        int durationTo = 8;
        int volumeUSDFrom = 400;
        int volumeUSDTo = 800;
        int lotsFrom = 4;
        int lotsTo = 8;

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
        tradingPage.openFilter();
        tradingPage.fillVolumeLotValues(String.valueOf(lotsFrom), String.valueOf(lotsTo));
        tradingPage.fillProfitValues(String.valueOf(profitUSDFrom), String.valueOf(profitUSDTo));
        tradingPage.fillDurationValues(String.valueOf(durationFrom), String.valueOf(durationTo));
        tradingPage.fillVolumeAmountValues(String.valueOf(volumeUSDFrom), String.valueOf(volumeUSDTo));
        tradingPage.clickApplyButton();
        tradingPage.errorMessageIsNotVisible();

    }

}
