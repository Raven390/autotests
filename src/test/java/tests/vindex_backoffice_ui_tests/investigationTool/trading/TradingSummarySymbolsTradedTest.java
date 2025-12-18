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
import static utils.Utils.insertCrmAccountsToDb;

public class TradingSummarySymbolsTradedTest extends TestBaseWeb {

    private static final ClientHelper client;
    static {
        client = ClientHelper.builder().userId(202_005).uid("e5880ca5-8578-4a1e-969d-7a64716ca41f").brand(Brand.INFINOX).regulator(Regulator.FCA).tradingAccount(202_005_001).serverId(42).build();
    }
    private static CrmTbUserObject crmTbUser = generateStaticUserByClient(client);
    private static CrmTbAccountObject account1 = generateStaticCrmTbAccountActive(client);
    private static MtAccountObject mtAccount = generateMtAccountByCrmTbAccount(account1);

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        crmTbUser.firstName = "Tradeus";
        crmTbUser.lastName = "Symboll";
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertCrmAccountsToDb(account1);
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

        trade1.setNotionalValueUsd(1.0);
        trade2.setNotionalValueUsd(1.0);
        trade3.setNotionalValueUsd(1.0);
        trade4.setNotionalValueUsd(1.0);
        trade5.setNotionalValueUsd(1.0);
        trade6.setNotionalValueUsd(1.0);
        trade7.setNotionalValueUsd(1.0);
        trade8.setNotionalValueUsd(1.0);
        trade9.setNotionalValueUsd(1.0);
        trade10.setNotionalValueUsd(1.0);
        trade11.setNotionalValueUsd(1.0);
        trade12.setNotionalValueUsd(1.0);
        trade13.setNotionalValueUsd(1.0);
        trade14.setNotionalValueUsd(1.0);
        trade15.setNotionalValueUsd(1.0);
        trade16.setNotionalValueUsd(1.0);

        trade1.setSymbol("USDA");
        trade2.setSymbol("USDB");
        trade3.setSymbol("USDC");
        trade4.setSymbol("USDD");
        trade5.setSymbol("USDE");
        trade6.setSymbol("USDF");
        trade7.setSymbol("USDG");
        trade8.setSymbol("USDH");
        trade9.setSymbol("USDK");
        trade10.setSymbol("USDL");
        trade11.setSymbol("USDM");
        trade12.setSymbol("USDN");
        trade13.setSymbol("USDO");
        trade14.setSymbol("USDP");
        trade15.setSymbol("USDQ");
        trade16.setSymbol("USDR");

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

        trade1.setNotionalValueUsd(50_000.1);
        trade1.setVolumeLots(10.0);
        trade2.setNotionalValueUsd(50_000.16);
        trade2.setVolumeLots(1.0);
        trade3.setNotionalValueUsd(50_000.14);
        trade3.setVolumeLots(11.14);
        trade4.setNotionalValueUsd(25_000.5);
        trade4.setVolumeLots(25_000.56);
        trade5.setNotionalValueUsd(25_000.2);
        trade5.setVolumeLots(29_000.2);

        trade1.setSymbol("FIRST");
        trade2.setSymbol("FIRST");
        trade3.setSymbol("THRIRD");
        trade4.setSymbol("FOURTH");
        trade5.setSymbol("FIFTH");

        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade1, trade2, trade3, trade4, trade5));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        tradingPage.enableViewAmount();
        tradingPage.countSymbolTradedBar(4);
        tradingPage.checkSymbolTradedHeaderMostTraded(trade1.getSymbol());
        tradingPage.checkSymbolTradedHeader(2, trade3.getSymbol());
        tradingPage.checkSymbolTradedHeader(3, trade4.getSymbol());
        int expectedAmount = tradingPage.calculateNotionValueUsdByDealInt(trade1, trade2);
        tradingPage.checkSymbolTradedGraphDescription(expectedAmount, trade1.getSymbol());

        Allure.step("repeat for volume in lots");
        tradingPage.enableViewLots();
        tradingPage.countSymbolTradedBar(3);
        tradingPage.checkSymbolTradedHeaderMostTraded(trade5.getSymbol());
        tradingPage.checkSymbolTradedHeader(2, trade4.getSymbol());
        tradingPage.checkSymbolTradedHeader(3, trade3.getSymbol());
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

        trade1.setNotionalValueUsd(50_000.1);
        trade1.setVolumeLots(253_200.2);
        trade2.setNotionalValueUsd(50_000.16);
        trade2.setVolumeLots(110.16);
        trade3.setNotionalValueUsd(50_000.14);
        trade3.setVolumeLots(5032.14);
        trade4.setNotionalValueUsd(25_000.5);
        trade4.setVolumeLots(21_000.5);
        trade5.setNotionalValueUsd(25_000.2);
        trade5.setVolumeLots(253_200.2);

        Symbol simbol1 = Symbol.getRandomSymbol();
        Symbol simbol2 = Symbol.getNextRandomSymbol(simbol1);
        Symbol simbol3 = Symbol.getNextRandomSymbol(simbol1, simbol2);
        Symbol simbol4 = Symbol.getNextRandomSymbol(simbol1, simbol2, simbol3);

        trade1.setSymbol(simbol1.getSymbolCode());
        trade2.setSymbol(simbol1.getSymbolCode());
        trade3.setSymbol(simbol2.getSymbolCode());
        trade4.setSymbol(simbol3.getSymbolCode());
        trade5.setSymbol(simbol4.getSymbolCode());

        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade1, trade2, trade3, trade4, trade5));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        tradingPage.enableViewAmount();
        tradingPage.hoverOverSymbolTradedBar(0);
        int expectedAmount = tradingPage.calculateNotionValueUsdByDealInt(trade1, trade2);
        tradingPage.checkSymbolTradedTooltipValue(trade1.getSymbol(), expectedAmount);
        tradingPage.hoverOverSymbolTradedBar(1);
        tradingPage.checkSymbolTradedTooltipValue(trade3.getSymbol(), trade3.getNotionalValueUsd());

        Allure.step("repeat the same for lots view");
        tradingPage.enableViewLots();
        tradingPage.hoverOverSymbolTradedBar(0);
        int expectedVolume = tradingPage.calculateLotsByDealInt(trade1, trade2);
        tradingPage.checkSymbolTradedTooltipValueLots(trade1.getSymbol(), expectedVolume);
        tradingPage.hoverOverSymbolTradedBar(1);
        tradingPage.checkSymbolTradedTooltipValueLots(trade5.getSymbol(), trade5.getVolumeLots());

        Allure.step("now try to repeat with two trades with one symbol and random values");
        tradingPage.deleteClientDeals(client.getUcid());
        MtMt4TradesCoercedObject trade21 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade22 = generateMt4TradesCoercedRandomized(client);

        trade21.setSymbol(simbol1.getSymbolCode());
        trade22.setSymbol(simbol1.getSymbolCode());

        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade21, trade22));

        tradingPage.navigate(client.getUcid());
        tradingPage.enableViewAmount();
        tradingPage.hoverOverSymbolTradedBar(0);
        int expectedAmount2 = tradingPage.calculateNotionValueUsdByDealInt(trade21, trade22);
        tradingPage.checkSymbolTradedTooltipValue(trade21.getSymbol(), expectedAmount2);

        tradingPage.enableViewLots();
        tradingPage.hoverOverSymbolTradedBar(0);
        int expectedVolume2 = tradingPage.calculateLotsByDealInt(trade21, trade22);
        tradingPage.checkSymbolTradedTooltipValueLots(trade21.getSymbol(), expectedVolume2);
    }

}
