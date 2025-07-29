package tests.vindex_backoffice_ui_tests.investigationTool.trading;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import helpers.data.ClientHelper;
import helpers.data.enums.Brand;
import helpers.data.enums.DateTimeFormat;
import helpers.data.enums.Regulator;
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
import static business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoercedRandomized;
import static helpers.database.DbHelper.insertObjectToDb;
import static helpers.database.DbHelper.insertObjectsToDb;
import static utils.Constants.*;
import static utils.Utils.*;
import static utils.Utils.getCurrentTimestampMinusOffsetFormatted;

public class TradingSummaryPnlPerSymbolTest extends TestBaseWeb {


    private static ClientHelper client = new ClientHelper(202_002, "e5880ca5-8578-4a1e-969d-7a64716ca40f", Brand.INFINOX, Regulator.FCA, 202_002_001, 42);
    private static CrmTbUserObject crmTbUser = generateStaticUserByClient(client);
    private static CrmTbAccountObject account1 = generateStaticCrmTbAccountActive(client);

    @BeforeAll
    public static void setup() throws SQLException, JsonProcessingException {
        crmTbUser.firstName = "Pienell";
        crmTbUser.lastName = "Symboll";
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertObjectToDb(CRM_TB_ACCOUNT_TABLE_NAME, account1);
    }

    @BeforeEach
    public void cleanup() throws SQLException, InterruptedException {
        tradingPage.deleteClientDeals(client.getUcid());
    }

    @Test
    @AllureId("941")
    @Feature("BMS-721 PNL by symbol")
    @DisplayName("Test that PNL by symbol shows correct empty states")
    public void pnlBySymbolEmptyStatesTest() throws SQLException, InterruptedException {
        Allure.step("clean client's trade DB");
        tradingPage.deleteClientDeals(client.getUcid());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        tradingPage.checkBothPnlBySymbolBarsEmpty();
        tradingPage.checkPnlBySymbolBarDescriptionProfits("No profits");
        tradingPage.checkPnlBySymbolBarDescriptionLoses("No losses");
        Allure.step("clean client's trade DB and add one trade with negative PNL");
        tradingPage.deleteClientDeals(client.getUcid());
        MtMt4TradesCoercedObject trade0 = generateMt4TradesCoercedRandomized(client);
        trade0.commissionUsd = -3.0;
        trade0.profitUsd = -3.0;
        trade0.storageUsd = -3.0;
        insertObjectToDb(MT4_TRADES_COERCED_TABLE_NAME, trade0);
        page.reload();
        Allure.step("reload the page");
        tradingPage.checkProfitPnlBySymbolBarsEmpty();
        tradingPage.checkPnlBySymbolBarDescriptionProfits("No profits");
        tradingPage.checkPnlBySymbolBarDescriptionLoses(String.valueOf(tradingPage.calculatePnlByDealInt(trade0)), trade0.symbol);
        Allure.step("clean client's trade DB and add one trade with positive PNL");
        tradingPage.deleteClientDeals(client.getUcid());
        trade0.commissionUsd = 3.0;
        trade0.profitUsd = 3.0;
        trade0.storageUsd = 3.0;
        insertObjectToDb(MT4_TRADES_COERCED_TABLE_NAME, trade0);
        page.reload();
        Allure.step("reload the page");
        tradingPage.checkLossesPnlBySymbolBarsEmpty();
        tradingPage.checkPnlBySymbolBarDescriptionLoses("No losses");
        tradingPage.checkPnlBySymbolBarDescriptionProfits(String.valueOf(tradingPage.calculatePnlByDealInt(trade0)), trade0.symbol);
    }

    @Test
    @AllureId("941")
    @Feature("BMS-721 PNL by symbol")
    @DisplayName("Test that PNL by symbol group trades by symbol")
    public void pnlBySymbolGroupTradesTest() throws SQLException, InterruptedException {

        tradingPage.deleteClientDeals(client.getUcid());
        MtMt4TradesCoercedObject trade1 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade2 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade3 = generateMt4TradesCoercedRandomized(client);
        trade1.commissionUsd = 3.0;
        trade1.profitUsd = 3.0;
        trade1.storageUsd = 3.0;
        trade1.symbol = "USDDTS";

        trade2.commissionUsd = 4.0;
        trade2.profitUsd = 3.0;
        trade2.storageUsd = 3.0;
        trade2.symbol = "USDDTS";

        trade3.commissionUsd = 5.0;
        trade3.profitUsd = 3.0;
        trade3.storageUsd = 3.0;
        trade3.symbol = "EURSUR";
        Allure.step("clean client's trade DB, and add one 3 trades with positive PNL, with 2 of them have same symbol");
        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade1, trade2, trade3));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        tradingPage.checkPnlBySymbolBarPositiveCount(2);
        tradingPage.checkPnlBySymbolBarNegativeCount(0);

        trade1.commissionUsd = -3.0;
        trade1.profitUsd = 3.0;
        trade1.storageUsd = -3.0;
        trade1.symbol = "USDDTS";

        trade2.commissionUsd = -4.0;
        trade2.profitUsd = 3.0;
        trade2.storageUsd = -3.0;
        trade2.symbol = "USDDTS";

        trade3.commissionUsd = -5.0;
        trade3.profitUsd = -3.0;
        trade3.storageUsd = -3.0;
        trade3.symbol = "EURSUR";
        Allure.step("clean client's trade DB, and add one 3 trades with negative PNL, with 2 of them have same symbol");
        tradingPage.deleteClientDeals(client.getUcid());
        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade1, trade2, trade3));

        page.reload();
        Allure.step("reload the page");
        tradingPage.checkPnlBySymbolBarPositiveCount(0);
        tradingPage.checkPnlBySymbolBarNegativeCount(2);

        tradingPage.deleteClientDeals(client.getUcid());
        trade1.commissionUsd = -3.0;
        trade1.profitUsd = 3.0;
        trade1.storageUsd = -3.0;
        trade1.symbol = "USDDTS";

        trade2.commissionUsd = -4.0;
        trade2.profitUsd = 3.0;
        trade2.storageUsd = -3.0;
        trade2.symbol = "USDDTS";

        trade3.commissionUsd = -5.0;
        trade3.profitUsd = 6.0;
        trade3.storageUsd = 6.0;
        trade3.symbol = "USDDTS";
        Allure.step("clean client's trade DB, and add 2 trades with negative PNL and 1 trade with positive PNL, with all of them have same symbol, so sum of PNL of all 3 trades is negative");
        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade1, trade2, trade3));

        page.reload();
        Allure.step("reload the page");
        tradingPage.checkPnlBySymbolBarPositiveCount(1);
        tradingPage.checkPnlBySymbolBarNegativeCount(0);
    }

    @Test
    @AllureId("944")
    @Feature("BMS-721 PNL by symbol")
    @DisplayName("Test that tooltip show correct data PNL by symbol")
    public void pnlBySymbolTooltipShowCorrectDataTest() throws SQLException,
            InterruptedException {

        tradingPage.deleteClientDeals(client.getUcid());
        MtMt4TradesCoercedObject trade1 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade2 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade3 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade4 = generateMt4TradesCoercedRandomized(client);
        trade1.commissionUsd = getRandomRoundedDouble(5, 50_000);
        trade1.profitUsd = getRandomRoundedDouble(5, 50_000);
        trade1.storageUsd = getRandomRoundedDouble(5, 50_000);
        trade1.symbol = "USDDTS";

        trade2.commissionUsd = getRandomRoundedDouble(5, 50_000);
        trade2.profitUsd = getRandomRoundedDouble(5, 50_000);
        trade2.storageUsd = getRandomRoundedDouble(5, 50_000);
        trade2.symbol = "USDDTS";

        trade3.commissionUsd = getRandomRoundedDouble(1, 5);
        trade3.profitUsd = getRandomRoundedDouble(1, 5);
        trade3.storageUsd = getRandomRoundedDouble(1, 5);
        trade3.symbol = "EURSUR";

        Allure.step("clean client's trade DB, and add one 3 trades with positive PNL, with 2 of them have same symbol");
        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade1, trade2, trade3));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        tradingPage.hoverOverRightPositiveBarPnlSymbol();
        tradingPage.checkPnlBySymbolTooltipValue(trade1.symbol, tradingPage.calculatePnlByDealInt(trade1, trade2));

    }

    @Test
    @AllureId("1033")
    @Feature("BMS-929 Trading Summary. PnL per Symbol chart")
    @DisplayName("Test that PNL by symbol uses not cumulative values")
    public void pnlBySymbolTooltipShowCorrectDataAndNotCumulativeTest() throws SQLException,
            InterruptedException {

        tradingPage.deleteClientDeals(client.getUcid());
        MtMt4TradesCoercedObject trade1 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade2 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade3 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade4 = generateMt4TradesCoercedRandomized(client);
        trade1.commissionUsd = getRandomRoundedDouble(5, 50_000);
        trade1.profitUsd = getRandomRoundedDouble(5, 50_000);
        trade1.storageUsd = getRandomRoundedDouble(5, 50_000);
        trade1.symbol = "USDDTS";
        trade1.closeTime = getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 2, 10, 0);
        trade1.closeTimeUtc = getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 2, 10, 0);
        trade1.openTimeUtc = getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 2, 12, 0);
        trade1.openTimeUtc = getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 2, 12, 0);

        trade2.commissionUsd = getRandomRoundedDouble(5, 50_000);
        trade2.profitUsd = getRandomRoundedDouble(5, 50_000);
        trade2.storageUsd = getRandomRoundedDouble(5, 50_000);
        trade2.symbol = "USDDTS";
        trade2.closeTime = getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 2, 2, 24, 0);
        trade2.closeTimeUtc = getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 2, 2, 24, 0);
        trade2.openTimeUtc = getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 2, 6, 14, 0);
        trade2.openTimeUtc = getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 2, 6, 14, 0);

        trade3.commissionUsd = getRandomRoundedDouble(1, 5000);
        trade3.profitUsd = getRandomRoundedDouble(1, 5000);
        trade3.storageUsd = getRandomRoundedDouble(1, 500);
        trade3.symbol = "USDDTS";
        trade1.closeTime = getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 3, 2, 10, 0);
        trade1.closeTimeUtc = getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 3, 2, 10, 0);
        trade1.openTimeUtc = getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 3, 2, 12, 0);
        trade1.openTimeUtc = getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 3, 2, 12, 0);

        Allure.step("clean client's trade DB, and add one 3 trades with positive PNL and the same symbol");
        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade1, trade2, trade3));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        tradingPage.hoverOverRightPositiveBarPnlSymbol();
        tradingPage.checkPnlBySymbolTooltipValue(trade1.symbol, tradingPage.calculatePnlByDealInt(trade1, trade2, trade3));

    }

    @Test
    @AllureId("943")
    @Feature("BMS-721 PNL by symbol")
    @DisplayName("Test that symbols that have PNL less than 10 percent from total PNL united to 'other' bar")
    public void pnlBySymbolOtherBarTest() throws SQLException, InterruptedException {

        tradingPage.deleteClientDeals(client.getUcid());
        MtMt4TradesCoercedObject trade1 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade2 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade3 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade4 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade11 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade12 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade13 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade14 = generateMt4TradesCoercedRandomized(client);
        trade1.commissionUsd = 80.00;
        trade1.profitUsd = 80.00;
        trade1.storageUsd = 80.00;
        trade1.symbol = "USDDTS";

        trade2.commissionUsd = 9.00;
        trade2.profitUsd = 9.00;
        trade2.storageUsd = 9.00;
        trade2.symbol = "USDDT";

        trade3.commissionUsd = 1.00;
        trade3.profitUsd = 1.00;
        trade3.storageUsd = 1.00;
        trade3.symbol = "EURUS";

        trade4.commissionUsd = 10.0;
        trade4.profitUsd = 10.0;
        trade4.storageUsd = 10.0;
        trade4.symbol = "EURSUR";

        trade11.commissionUsd = -80.00;
        trade11.profitUsd = -80.00;
        trade11.storageUsd = -80.00;
        trade11.symbol = "NEGUSDDTS";

        trade12.commissionUsd = -9.00;
        trade12.profitUsd = -9.00;
        trade12.storageUsd = -9.00;
        trade12.symbol = "NEGUSDDT";

        trade13.commissionUsd = -1.00;
        trade13.profitUsd = -1.00;
        trade13.storageUsd = -1.00;
        trade13.symbol = "NEGEURUS";

        trade14.commissionUsd = -10.0;
        trade14.profitUsd = -10.0;
        trade14.storageUsd = -10.0;
        trade14.symbol = "NEGEURSUR";

        Allure.step("clean client's trade DB, sdd 4 trades, 2 of with have PNL less than 10% of total PNL");
        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade1, trade2, trade3, trade4, trade11, trade12, trade13, trade14));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        tradingPage.checkPnlBySymbolBarPositiveCount(3);
        tradingPage.hoverOverOtherPositiveBarPnlSymbol();
        tradingPage.checkPnlBySymbolOtherTooltipHeaderValue(2, tradingPage.calculatePnlByDealInt(trade2, trade3));
        tradingPage.checkPnlBySymbolTooltipValue(1, trade2.symbol, String.valueOf(tradingPage.calculatePnlByDealInt(trade2)));
        tradingPage.checkPnlBySymbolTooltipValue(2, trade3.symbol, String.valueOf(tradingPage.calculatePnlByDealInt(trade3)));

    }

    @Test
    @AllureId("942")
    @Feature("BMS-721 PNL by symbol")
    @DisplayName("Test that tooltip for 'other' tab have counter of symbols and shows more as 10 symbols as other")
    public void pnlBySymbolOtherBarTooltipTest() throws SQLException,
            InterruptedException {


        tradingPage.deleteClientDeals(client.getUcid());
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
        MtMt4TradesCoercedObject trade31 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade32 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade33 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade34 = generateMt4TradesCoercedRandomized(client);
        trade1.commissionUsd = 80.00;
        trade1.profitUsd = 80.00;
        trade1.storageUsd = 80.00;
        trade1.symbol = "USDDTS";

        trade2.commissionUsd = 1.00;
        trade2.profitUsd = 1.00;
        trade2.storageUsd = 1.00;
        trade2.symbol = "USDDT";

        trade3.commissionUsd = 1.00;
        trade3.profitUsd = 1.00;
        trade3.storageUsd = 1.00;
        trade3.symbol = "EURUS";

        trade4.commissionUsd = 1.00;
        trade4.profitUsd = 1.00;
        trade4.storageUsd = 1.00;
        trade4.symbol = "SMALQ";

        trade5.commissionUsd = 1.00;
        trade5.profitUsd = 1.00;
        trade5.storageUsd = 1.00;
        trade5.symbol = "SMALW";

        trade6.commissionUsd = 1.00;
        trade6.profitUsd = 1.00;
        trade6.storageUsd = 1.00;
        trade6.symbol = "SMALE";

        trade7.commissionUsd = 1.00;
        trade7.profitUsd = 1.00;
        trade7.storageUsd = 1.00;
        trade7.symbol = "SMALR";

        trade8.commissionUsd = 1.00;
        trade8.profitUsd = 1.00;
        trade8.storageUsd = 1.00;
        trade8.symbol = "SMALT";

        trade9.commissionUsd = 1.00;
        trade9.profitUsd = 1.00;
        trade9.storageUsd = 1.00;
        trade9.symbol = "SMALY";

        trade10.commissionUsd = 1.00;
        trade10.profitUsd = 1.00;
        trade10.storageUsd = 1.00;
        trade10.symbol = "SMALU";

        trade11.commissionUsd = 1.00;
        trade11.profitUsd = 1.00;
        trade11.storageUsd = 1.00;
        trade11.symbol = "SMALI";

        trade12.commissionUsd = 1.00;
        trade12.profitUsd = 1.00;
        trade12.storageUsd = 1.00;
        trade12.symbol = "SMALO";

        trade13.commissionUsd = 1.00;
        trade13.profitUsd = 1.00;
        trade13.storageUsd = 1.00;
        trade13.symbol = "SMALP";


        trade31.commissionUsd = -80.00;
        trade31.profitUsd = -80.00;
        trade31.storageUsd = -80.00;
        trade31.symbol = "NEGUSDDTS";

        trade32.commissionUsd = -9.00;
        trade32.profitUsd = -9.00;
        trade32.storageUsd = -9.00;
        trade32.symbol = "NEGUSDDT";

        trade33.commissionUsd = -1.00;
        trade33.profitUsd = -1.00;
        trade33.storageUsd = -1.00;
        trade33.symbol = "NEGEURUS";

        trade34.commissionUsd = -10.0;
        trade34.profitUsd = -10.0;
        trade34.storageUsd = -10.0;
        trade34.symbol = "NEGEURSUR";

        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade1, trade2, trade3, trade4, trade5, trade6, trade7, trade8, trade9, trade10, trade11, trade12, trade13, trade31, trade32, trade33, trade34));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        tradingPage.checkPnlBySymbolBarPositiveCount(2);
        tradingPage.hoverOverOtherPositiveBarPnlSymbol();
        int expectedAmountHeader = tradingPage.calculatePnlByDealInt(trade2, trade3, trade4, trade5, trade6, trade7, trade8, trade9, trade10, trade11, trade12, trade13);
        tradingPage.checkPnlBySymbolOtherTooltipHeaderValue(12, expectedAmountHeader);
        tradingPage.checkPnlBySymbolOtherTooltipLinesCount(12);


    }
}

