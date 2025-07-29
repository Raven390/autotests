package tests.vindex_backoffice_ui_tests.investigationTool.trading;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import helpers.data.ClientHelper;
import helpers.data.enums.Brand;
import helpers.data.enums.Regulator;
import helpers.data.enums.Symbol;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.TestBaseWeb;

import java.sql.SQLException;
import java.util.List;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateStaticCrmTbAccountActive;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoercedRandomized;
import static helpers.database.DbHelper.insertObjectToDb;
import static helpers.database.DbHelper.insertObjectsToDb;
import static utils.Constants.*;

public class TradingSummarySymbolsTradedTest extends TestBaseWeb {

    private static ClientHelper client = new ClientHelper(202_005, "e5880ca5-8578-4a1e-969d-7a64716ca41f", Brand.INFINOX, Regulator.FCA, 202_005_001, 42);
    private static CrmTbUserObject crmTbUser = generateStaticUserByClient(client);
    private static CrmTbAccountObject account1 = generateStaticCrmTbAccountActive(client);
    private static MtAccountObject mtAccount = generateMtAccountByCrmTbAccount(account1);

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        crmTbUser.firstName = "Tradeus";
        crmTbUser.lastName = "Symboll";
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertObjectToDb(CRM_TB_ACCOUNT_TABLE_NAME, account1);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, mtAccount);
    }

    @BeforeEach
    public void cleanup() throws SQLException, InterruptedException {
        tradingPage.deleteClientDeals(client.getUcid());
    }

    @Test
    @AllureId("950")
    @Feature("BMS-724 Symbols traded")
    @DisplayName("Test that Symbol Traded show empty state when there is no trades to display")
    public void SymbolsTradedEmptyStateTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        tradingPage.symbolTradedEmptyState();
    }

    @Test
    @AllureId("965")
    @Feature("BMS-724 Symbols traded")
    @DisplayName("Test that Symbol Traded show one segment 'other' when all symbols volume are lesser than total volume")
    public void SymbolsTradedOnlyOtherTest() throws ReflectiveOperationException, SQLException {
        MtMt4TradesCoercedObject trade1 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade2 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade3 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade4 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade5 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade6 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade7 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade8 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade9 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade10 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade11 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade12 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade13 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade14 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade15 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade16 = generateMt4TradesCoercedRandomized(client);

        trade1.notionalValueUsd = 1.0;
        trade2.notionalValueUsd = 1.0;
        trade3.notionalValueUsd = 1.0;
        trade4.notionalValueUsd = 1.0;
        trade5.notionalValueUsd = 1.0;
        trade6.notionalValueUsd = 1.0;
        trade7.notionalValueUsd = 1.0;
        trade8.notionalValueUsd = 1.0;
        trade9.notionalValueUsd = 1.0;
        trade10.notionalValueUsd = 1.0;
        trade11.notionalValueUsd = 1.0;
        trade12.notionalValueUsd = 1.0;
        trade13.notionalValueUsd = 1.0;
        trade14.notionalValueUsd = 1.0;
        trade15.notionalValueUsd = 1.0;
        trade16.notionalValueUsd = 1.0;

        trade1.symbol = "USDA";
        trade2.symbol = "USDB";
        trade3.symbol = "USDC";
        trade4.symbol = "USDD";
        trade5.symbol = "USDE";
        trade6.symbol = "USDF";
        trade7.symbol = "USDG";
        trade8.symbol = "USDH";
        trade9.symbol = "USDK";
        trade10.symbol = "USDL";
        trade11.symbol = "USDM";
        trade12.symbol = "USDN";
        trade13.symbol = "USDO";
        trade14.symbol = "USDP";
        trade15.symbol = "USDQ";
        trade16.symbol = "USDR";

        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade1, trade2, trade3, trade4, trade5, trade6, trade7, trade8, trade9, trade10, trade11, trade12, trade13, trade14, trade15, trade16));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        tradingPage.enableViewAmount();
        tradingPage.countSymbolTradedBar(1);
        int expectedAmount = tradingPage.calculateNotionValueUsdByDealInt(trade1, trade2, trade3, trade4, trade5, trade6, trade7, trade8, trade9, trade10, trade11, trade12, trade13, trade14, trade15, trade16);
        tradingPage.hoverOverSymbolTradedBar(0);
        tradingPage.checkSymbolTradedOtherTooltipHeaderValue(16, expectedAmount);
        tradingPage.checkSymbolTradedOtherTooltipLinesCount(12);
        int expectedAmountOther = tradingPage.calculateNotionValueUsdByDealInt(trade11, trade12, trade13, trade14, trade15, trade16);
        tradingPage.checkSymbolTradedOtherTooltipFooter(6, expectedAmountOther);
    }

    @Test
    @AllureId("966")
    @Feature("BMS-724 Symbols traded")
    @DisplayName("Test that Symbol Traded header shows correct info")
    public void SymbolsTradedHeaderTest() throws ReflectiveOperationException, SQLException {
        MtMt4TradesCoercedObject trade1 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade2 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade3 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade4 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade5 = generateMt4TradesCoercedRandomized(client);

        trade1.notionalValueUsd = 50_000.1;
        trade1.volumeLots = 10.0;
        trade2.notionalValueUsd = 50_000.16;
        trade2.volumeLots = 1.0;
        trade3.notionalValueUsd = 50_000.14;
        trade3.volumeLots = 11.14;
        trade4.notionalValueUsd = 25_000.5;
        trade4.volumeLots = 25_000.56;
        trade5.notionalValueUsd = 25_000.2;
        trade5.volumeLots = 29_000.2;

        trade1.symbol = "FIRST";
        trade2.symbol = "FIRST";
        trade3.symbol = "THRIRD";
        trade4.symbol = "FOURTH";
        trade5.symbol = "FIFTH";

        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade1, trade2, trade3, trade4, trade5));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        tradingPage.enableViewAmount();
        tradingPage.countSymbolTradedBar(4);
        tradingPage.checkSymbolTradedHeaderMostTraded(trade1.symbol);
        tradingPage.checkSymbolTradedHeader(2, trade3.symbol);
        tradingPage.checkSymbolTradedHeader(3, trade4.symbol);
        int expectedAmount = tradingPage.calculateNotionValueUsdByDealInt(trade1, trade2);
        tradingPage.checkSymbolTradedGraphDescription(expectedAmount, trade1.symbol);

        Allure.step("repeat for volume in lots");
        tradingPage.enableViewLots();
        tradingPage.countSymbolTradedBar(3);
        tradingPage.checkSymbolTradedHeaderMostTraded(trade5.symbol);
        tradingPage.checkSymbolTradedHeader(2, trade4.symbol);
        tradingPage.checkSymbolTradedHeader(3, trade3.symbol);
    }

    @Test
    @AllureId("967")
    @Feature("BMS-724 Symbols traded")
    @DisplayName("Test that Symbol Traded tooltip have correct info from DB")
    public void SymbolsTooltipTest() throws ReflectiveOperationException, SQLException, InterruptedException {
        MtMt4TradesCoercedObject trade1 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade2 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade3 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade4 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade5 = generateMt4TradesCoercedRandomized(client);

        trade1.notionalValueUsd = 50_000.1;
        trade1.volumeLots = 253_200.2;
        trade2.notionalValueUsd = 50_000.16;
        trade2.volumeLots = 110.16;
        trade3.notionalValueUsd = 50_000.14;
        trade3.volumeLots = 5032.14;
        trade4.notionalValueUsd = 25_000.5;
        trade4.volumeLots = 21_000.5;
        trade5.notionalValueUsd = 25_000.2;
        trade5.volumeLots = 253_200.2;

        Symbol simbol1 = Symbol.getRandomSymbol();
        Symbol simbol2 = Symbol.getNextRandomSymbol(simbol1);
        Symbol simbol3 = Symbol.getNextRandomSymbol(simbol1, simbol2);
        Symbol simbol4 = Symbol.getNextRandomSymbol(simbol1, simbol2, simbol3);

        trade1.symbol = simbol1.getSymbolCode();
        trade2.symbol = simbol1.getSymbolCode();
        trade3.symbol = simbol2.getSymbolCode();
        trade4.symbol = simbol3.getSymbolCode();
        trade5.symbol = simbol4.getSymbolCode();

        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade1, trade2, trade3, trade4, trade5));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        tradingPage.enableViewAmount();
        tradingPage.hoverOverSymbolTradedBar(0);
        int expectedAmount = tradingPage.calculateNotionValueUsdByDealInt(trade1, trade2);
        tradingPage.checkSymbolTradedTooltipValue(trade1.symbol, expectedAmount);
        tradingPage.hoverOverSymbolTradedBar(1);
        tradingPage.checkSymbolTradedTooltipValue(trade3.symbol, trade3.notionalValueUsd);

        Allure.step("repeat the same for lots view");
        tradingPage.enableViewLots();
        tradingPage.hoverOverSymbolTradedBar(0);
        int expectedVolume = tradingPage.calculateLotsByDealInt(trade1, trade2);
        tradingPage.checkSymbolTradedTooltipValueLots(trade1.symbol, expectedVolume);
        tradingPage.hoverOverSymbolTradedBar(1);
        tradingPage.checkSymbolTradedTooltipValueLots(trade5.symbol, trade5.volumeLots);

        Allure.step("now try to repeat with two trades with one symbol and random values");
        tradingPage.deleteClientDeals(client.getUcid());
        MtMt4TradesCoercedObject trade21 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade22 = generateMt4TradesCoercedRandomized(client);

        trade21.symbol = simbol1.getSymbolCode();
        trade22.symbol = simbol1.getSymbolCode();

        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade21, trade22));

        tradingPage.navigate(client.getUcid());
        tradingPage.enableViewAmount();
        tradingPage.hoverOverSymbolTradedBar(0);
        int expectedAmount2 = tradingPage.calculateNotionValueUsdByDealInt(trade21, trade22);
        tradingPage.checkSymbolTradedTooltipValue(trade21.symbol, expectedAmount2);

        tradingPage.enableViewLots();
        tradingPage.hoverOverSymbolTradedBar(0);
        int expectedVolume2 = tradingPage.calculateLotsByDealInt(trade21, trade22);
        tradingPage.checkSymbolTradedTooltipValueLots(trade21.symbol, expectedVolume2);
    }

}
