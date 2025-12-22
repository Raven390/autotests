package tests.vindex_backoffice_ui_tests.investigationTool.trading;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateStaticCrmTbAccountActive;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoercedRandomized;
import static helpers.database.DbHelper.insertObjectToDb;
import static helpers.database.DbHelper.insertObjectsToDb;
import static utils.Constants.*;
import static utils.Utils.*;
import static utils.Utils.getCurrentTimestampMinusOffsetFormatted;

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
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.TestBaseWeb;

public class TradingSummaryPnlPerSymbolTest extends TestBaseWeb {

    private static final ClientHelper client;

    static {
        client = ClientHelper.builder()
                .userId(202_002)
                .uid("e5880ca5-8578-4a1e-969d-7a64716ca40f")
                .brand(Brand.INFINOX)
                .regulator(Regulator.FCA)
                .tradingAccount(202_002_001)
                .serverId(42)
                .build();
    }

    private static CrmTbUserObject crmTbUser = generateStaticUserByClient(client);
    private static CrmTbAccountObject account1 = generateStaticCrmTbAccountActive(client);

    @BeforeAll
    public static void setup() throws SQLException, JsonProcessingException {
        crmTbUser.firstName = "Pienell";
        crmTbUser.lastName = "Symboll";
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertCrmAccountsToDb(account1);
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
        trade0.setCommissionUsd(-3.0);
        trade0.setProfitUsd(-3.0);
        trade0.setStorageUsd(-3.0);
        insertObjectToDb(MT4_TRADES_COERCED_TABLE_NAME, trade0);
        page.reload();
        Allure.step("reload the page");
        tradingPage.checkProfitPnlBySymbolBarsEmpty();
        tradingPage.checkPnlBySymbolBarDescriptionProfits("No profits");
        tradingPage.checkPnlBySymbolBarDescriptionLoses(
                String.valueOf(tradingPage.calculatePnlByDealInt(trade0)), trade0.getSymbol());
        Allure.step("clean client's trade DB and add one trade with positive PNL");
        tradingPage.deleteClientDeals(client.getUcid());
        trade0.setCommissionUsd(3.0);
        trade0.setProfitUsd(3.0);
        trade0.setStorageUsd(3.0);
        insertObjectToDb(MT4_TRADES_COERCED_TABLE_NAME, trade0);
        page.reload();
        Allure.step("reload the page");
        tradingPage.checkLossesPnlBySymbolBarsEmpty();
        tradingPage.checkPnlBySymbolBarDescriptionLoses("No losses");
        tradingPage.checkPnlBySymbolBarDescriptionProfits(
                String.valueOf(tradingPage.calculatePnlByDealInt(trade0)), trade0.getSymbol());
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
        trade1.setCommissionUsd(3.0);
        trade1.setProfitUsd(3.0);
        trade1.setStorageUsd(3.0);
        trade1.setSymbol("USDDTS");

        trade2.setCommissionUsd(4.0);
        trade2.setProfitUsd(3.0);
        trade2.setStorageUsd(3.0);
        trade2.setSymbol("USDDTS");

        trade3.setCommissionUsd(5.0);
        trade3.setProfitUsd(3.0);
        trade3.setStorageUsd(3.0);
        trade3.setSymbol("EURSUR");
        Allure.step("clean client's trade DB, and add one 3 trades with positive PNL, with 2 of them have same symbol");
        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade1, trade2, trade3));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        tradingPage.checkPnlBySymbolBarPositiveCount(2);
        tradingPage.checkPnlBySymbolBarNegativeCount(0);

        trade1.setCommissionUsd(-3.0);
        trade1.setProfitUsd(3.0);
        trade1.setStorageUsd(-3.0);
        trade1.setSymbol("USDDTS");

        trade2.setCommissionUsd(-4.0);
        trade2.setProfitUsd(3.0);
        trade2.setStorageUsd(-3.0);
        trade2.setSymbol("USDDTS");

        trade3.setCommissionUsd(-5.0);
        trade3.setProfitUsd(-3.0);
        trade3.setStorageUsd(-3.0);
        trade3.setSymbol("EURSUR");
        Allure.step("clean client's trade DB, and add one 3 trades with negative PNL, with 2 of them have same symbol");
        tradingPage.deleteClientDeals(client.getUcid());
        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade1, trade2, trade3));

        page.reload();
        Allure.step("reload the page");
        tradingPage.checkPnlBySymbolBarPositiveCount(0);
        tradingPage.checkPnlBySymbolBarNegativeCount(2);

        tradingPage.deleteClientDeals(client.getUcid());
        trade1.setCommissionUsd(-3.0);
        trade1.setProfitUsd(3.0);
        trade1.setStorageUsd(-3.0);
        trade1.setSymbol("USDDTS");

        trade2.setCommissionUsd(-4.0);
        trade2.setProfitUsd(3.0);
        trade2.setStorageUsd(-3.0);
        trade2.setSymbol("USDDTS");

        trade3.setCommissionUsd(-5.0);
        trade3.setProfitUsd(6.0);
        trade3.setStorageUsd(6.0);
        trade3.setSymbol("USDDTS");
        Allure.step(
                "clean client's trade DB, and add 2 trades with negative PNL and 1 trade with positive PNL, with all of them have same symbol, so sum of PNL of all 3 trades is negative");
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
    public void pnlBySymbolTooltipShowCorrectDataTest() throws SQLException, InterruptedException {

        tradingPage.deleteClientDeals(client.getUcid());
        MtMt4TradesCoercedObject trade1 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade2 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade3 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade4 = generateMt4TradesCoercedRandomized(client);
        trade1.setCommissionUsd(getRandomRoundedDouble(5, 50_000));
        trade1.setProfitUsd(getRandomRoundedDouble(5, 50_000));
        trade1.setStorageUsd(getRandomRoundedDouble(5, 50_000));
        trade1.setSymbol("USDDTS");

        trade2.setCommissionUsd(getRandomRoundedDouble(5, 50_000));
        trade2.setProfitUsd(getRandomRoundedDouble(5, 50_000));
        trade2.setStorageUsd(getRandomRoundedDouble(5, 50_000));
        trade2.setSymbol("USDDTS");

        trade3.setCommissionUsd(getRandomRoundedDouble(1, 5));
        trade3.setProfitUsd(getRandomRoundedDouble(1, 5));
        trade3.setStorageUsd(getRandomRoundedDouble(1, 5));
        trade3.setSymbol("EURSUR");

        Allure.step("clean client's trade DB, and add one 3 trades with positive PNL, with 2 of them have same symbol");
        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade1, trade2, trade3));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        tradingPage.hoverOverRightPositiveBarPnlSymbol();
        tradingPage.checkPnlBySymbolTooltipValue(trade1.getSymbol(), tradingPage.calculatePnlByDealInt(trade1, trade2));
    }

    @Test
    @AllureId("1033")
    @Feature("BMS-929 Trading Summary. PnL per Symbol chart")
    @DisplayName("Test that PNL by symbol uses not cumulative values")
    public void pnlBySymbolTooltipShowCorrectDataAndNotCumulativeTest() throws SQLException, InterruptedException {

        tradingPage.deleteClientDeals(client.getUcid());
        MtMt4TradesCoercedObject trade1 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade2 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade3 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade4 = generateMt4TradesCoercedRandomized(client);
        trade1.setCommissionUsd(getRandomRoundedDouble(5, 50_000));
        trade1.setProfitUsd(getRandomRoundedDouble(5, 50_000));
        trade1.setStorageUsd(getRandomRoundedDouble(5, 50_000));
        trade1.setSymbol("USDDTS");
        trade1.setCloseTime(getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 2, 10, 0));
        trade1.setCloseTimeUtc(
                getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 2, 10, 0));
        trade1.setOpenTimeUtc(getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 2, 12, 0));
        trade1.setOpenTimeUtc(getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 2, 12, 0));

        trade2.setCommissionUsd(getRandomRoundedDouble(5, 50_000));
        trade2.setProfitUsd(getRandomRoundedDouble(5, 50_000));
        trade2.setStorageUsd(getRandomRoundedDouble(5, 50_000));
        trade2.setSymbol("USDDTS");
        trade2.setCloseTime(getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 2, 2, 24, 0));
        trade2.setCloseTimeUtc(
                getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 2, 2, 24, 0));
        trade2.setOpenTimeUtc(getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 2, 6, 14, 0));
        trade2.setOpenTimeUtc(getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 2, 6, 14, 0));

        trade3.setCommissionUsd(getRandomRoundedDouble(1, 5000));
        trade3.setProfitUsd(getRandomRoundedDouble(1, 5000));
        trade3.setStorageUsd(getRandomRoundedDouble(1, 500));
        trade3.setSymbol("USDDTS");
        trade1.setCloseTime(getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 3, 2, 10, 0));
        trade1.setCloseTimeUtc(
                getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 3, 2, 10, 0));
        trade1.setOpenTimeUtc(getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 3, 2, 12, 0));
        trade1.setOpenTimeUtc(getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 3, 2, 12, 0));

        Allure.step("clean client's trade DB, and add one 3 trades with positive PNL and the same symbol");
        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade1, trade2, trade3));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        tradingPage.hoverOverRightPositiveBarPnlSymbol();
        tradingPage.checkPnlBySymbolTooltipValue(
                trade1.getSymbol(), tradingPage.calculatePnlByDealInt(trade1, trade2, trade3));
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
        trade1.setCommissionUsd(80.00);
        trade1.setProfitUsd(80.00);
        trade1.setStorageUsd(80.00);
        trade1.setSymbol("USDDTS");

        trade2.setCommissionUsd(9.00);
        trade2.setProfitUsd(9.00);
        trade2.setStorageUsd(9.00);
        trade2.setSymbol("USDDT");

        trade3.setCommissionUsd(1.00);
        trade3.setProfitUsd(1.00);
        trade3.setStorageUsd(1.00);
        trade3.setSymbol("EURUS");

        trade4.setCommissionUsd(10.0);
        trade4.setProfitUsd(10.0);
        trade4.setStorageUsd(10.0);
        trade4.setSymbol("EURSUR");

        trade11.setCommissionUsd(-80.00);
        trade11.setProfitUsd(-80.00);
        trade11.setStorageUsd(-80.00);
        trade11.setSymbol("NEGUSDDTS");

        trade12.setCommissionUsd(-9.00);
        trade12.setProfitUsd(-9.00);
        trade12.setStorageUsd(-9.00);
        trade12.setSymbol("NEGUSDDT");

        trade13.setCommissionUsd(-1.00);
        trade13.setProfitUsd(-1.00);
        trade13.setStorageUsd(-1.00);
        trade13.setSymbol("NEGEURUS");

        trade14.setCommissionUsd(-10.0);
        trade14.setProfitUsd(-10.0);
        trade14.setStorageUsd(-10.0);
        trade14.setSymbol("NEGEURSUR");

        Allure.step("clean client's trade DB, sdd 4 trades, 2 of with have PNL less than 10% of total PNL");
        insertObjectsToDb(
                MT4_TRADES_COERCED_TABLE_NAME,
                List.of(trade1, trade2, trade3, trade4, trade11, trade12, trade13, trade14));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        tradingPage.checkPnlBySymbolBarPositiveCount(3);
        tradingPage.hoverOverOtherPositiveBarPnlSymbol();
        tradingPage.checkPnlBySymbolOtherTooltipHeaderValue(2, tradingPage.calculatePnlByDealInt(trade2, trade3));
        tradingPage.checkPnlBySymbolTooltipValue(
                1, trade2.getSymbol(), String.valueOf(tradingPage.calculatePnlByDealInt(trade2)));
        tradingPage.checkPnlBySymbolTooltipValue(
                2, trade3.getSymbol(), String.valueOf(tradingPage.calculatePnlByDealInt(trade3)));
    }

    @Test
    @AllureId("942")
    @Feature("BMS-721 PNL by symbol")
    @DisplayName("Test that tooltip for 'other' tab have counter of symbols and shows more as 10 symbols as other")
    public void pnlBySymbolOtherBarTooltipTest() throws SQLException, InterruptedException {

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
        trade1.setCommissionUsd(80.00);
        trade1.setProfitUsd(80.00);
        trade1.setStorageUsd(80.00);
        trade1.setSymbol("USDDTS");

        trade2.setCommissionUsd(1.00);
        trade2.setProfitUsd(1.00);
        trade2.setStorageUsd(1.00);
        trade2.setSymbol("USDDT");

        trade3.setCommissionUsd(1.00);
        trade3.setProfitUsd(1.00);
        trade3.setStorageUsd(1.00);
        trade3.setSymbol("EURUS");

        trade4.setCommissionUsd(1.00);
        trade4.setProfitUsd(1.00);
        trade4.setStorageUsd(1.00);
        trade4.setSymbol("SMALQ");

        trade5.setCommissionUsd(1.00);
        trade5.setProfitUsd(1.00);
        trade5.setStorageUsd(1.00);
        trade5.setSymbol("SMALW");

        trade6.setCommissionUsd(1.00);
        trade6.setProfitUsd(1.00);
        trade6.setStorageUsd(1.00);
        trade6.setSymbol("SMALE");

        trade7.setCommissionUsd(1.00);
        trade7.setProfitUsd(1.00);
        trade7.setStorageUsd(1.00);
        trade7.setSymbol("SMALR");

        trade8.setCommissionUsd(1.00);
        trade8.setProfitUsd(1.00);
        trade8.setStorageUsd(1.00);
        trade8.setSymbol("SMALT");

        trade9.setCommissionUsd(1.00);
        trade9.setProfitUsd(1.00);
        trade9.setStorageUsd(1.00);
        trade9.setSymbol("SMALY");

        trade10.setCommissionUsd(1.00);
        trade10.setProfitUsd(1.00);
        trade10.setStorageUsd(1.00);
        trade10.setSymbol("SMALU");

        trade11.setCommissionUsd(1.00);
        trade11.setProfitUsd(1.00);
        trade11.setStorageUsd(1.00);
        trade11.setSymbol("SMALI");

        trade12.setCommissionUsd(1.00);
        trade12.setProfitUsd(1.00);
        trade12.setStorageUsd(1.00);
        trade12.setSymbol("SMALO");

        trade13.setCommissionUsd(1.00);
        trade13.setProfitUsd(1.00);
        trade13.setStorageUsd(1.00);
        trade13.setSymbol("SMALP");

        trade31.setCommissionUsd(-80.00);
        trade31.setProfitUsd(-80.00);
        trade31.setStorageUsd(-80.00);
        trade31.setSymbol("NEGUSDDTS");

        trade32.setCommissionUsd(-9.00);
        trade32.setProfitUsd(-9.00);
        trade32.setStorageUsd(-9.00);
        trade32.setSymbol("NEGUSDDT");

        trade33.setCommissionUsd(-1.00);
        trade33.setProfitUsd(-1.00);
        trade33.setStorageUsd(-1.00);
        trade33.setSymbol("NEGEURUS");

        trade34.setCommissionUsd(-10.0);
        trade34.setProfitUsd(-10.0);
        trade34.setStorageUsd(-10.0);
        trade34.setSymbol("NEGEURSUR");

        insertObjectsToDb(
                MT4_TRADES_COERCED_TABLE_NAME,
                List.of(
                        trade1, trade2, trade3, trade4, trade5, trade6, trade7, trade8, trade9, trade10, trade11,
                        trade12, trade13, trade31, trade32, trade33, trade34));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        tradingPage.checkPnlBySymbolBarPositiveCount(2);
        tradingPage.hoverOverOtherPositiveBarPnlSymbol();
        int expectedAmountHeader = tradingPage.calculatePnlByDealInt(
                trade2, trade3, trade4, trade5, trade6, trade7, trade8, trade9, trade10, trade11, trade12, trade13);
        tradingPage.checkPnlBySymbolOtherTooltipHeaderValue(12, expectedAmountHeader);
        tradingPage.checkPnlBySymbolOtherTooltipLinesCount(12);
    }
}
