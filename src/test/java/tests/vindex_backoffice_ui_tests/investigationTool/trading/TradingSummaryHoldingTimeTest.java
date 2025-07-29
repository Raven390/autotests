package tests.vindex_backoffice_ui_tests.investigationTool.trading;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import helpers.data.ClientHelper;
import helpers.data.enums.Brand;
import helpers.data.enums.Regulator;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.TestBaseWeb;

import java.sql.SQLException;
import java.util.List;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateStaticCrmTbAccountActive;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoercedRandomized;
import static helpers.data.enums.DateTimeFormat.DATE_AND_TIME;
import static helpers.database.DbHelper.insertObjectToDb;
import static helpers.database.DbHelper.insertObjectsToDb;
import static utils.Constants.*;
import static utils.Utils.getCurrentTimestampDbFormat;
import static utils.Utils.getCurrentTimestampMinusOffsetFormatted;

public class TradingSummaryHoldingTimeTest extends TestBaseWeb {


    private static ClientHelper client = new ClientHelper(202_006, "e5880ca5-8578-4a1e-969d-7a64716ca50f", Brand.INFINOX, Regulator.FCA, 202_006_001, 42);
    private static CrmTbUserObject crmTbUser = generateStaticUserByClient(client);
    private static CrmTbAccountObject account1 = generateStaticCrmTbAccountActive(client);
    private static MtAccountObject mtAccount = generateMtAccountByCrmTbAccount(account1);

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        crmTbUser.firstName = "Holden";
        crmTbUser.lastName = "Times";
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertObjectToDb(CRM_TB_ACCOUNT_TABLE_NAME, account1);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, mtAccount);
    }

    @Test
    @AllureId("970")
    @Feature("BMS-103 Holding time")
    @DisplayName("Test that Holding Time tooltip shows correct info in tooltips")
    public void holdingTimeTooltipTest() throws SQLException, InterruptedException {

        tradingPage.deleteClientDeals(client.getUcid());

        MtMt4TradesCoercedObject trade0 = generateMt4TradesCoercedRandomized(client);
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

        trade0.profitUsd = 11.18;
        trade1.openTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 0, 1);
        trade1.closeTime = getCurrentTimestampDbFormat();
        trade2.openTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 9, 59);
        trade2.closeTime = getCurrentTimestampDbFormat();

        trade3.openTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 10);
        trade3.closeTime = getCurrentTimestampDbFormat();
        trade4.openTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 29, 59);
        trade4.closeTime = getCurrentTimestampDbFormat();

        trade5.openTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 30);
        trade5.closeTime = getCurrentTimestampDbFormat();
        trade6.openTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 5, 59, 59);
        trade6.closeTime = getCurrentTimestampDbFormat();

        trade7.openTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 6, 0);
        trade7.closeTime = getCurrentTimestampDbFormat();
        trade8.openTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 23, 59, 59);
        trade8.closeTime = getCurrentTimestampDbFormat();

        trade9.openTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 24, 0);
        trade9.closeTime = getCurrentTimestampDbFormat();
        trade10.openTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 1, 0, 1, 24, 0, 1);
        trade10.closeTime = getCurrentTimestampDbFormat();

        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade0, trade1, trade2, trade3, trade4, trade5, trade6, trade7, trade8, trade9, trade10));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        tradingPage.openHoldingTimeTooltip("0-10min");
        tradingPage.checkHoldingTimeTooltip(3, 27);
        tradingPage.openHoldingTimeTooltip("10-30min");
        tradingPage.checkHoldingTimeTooltip(2, 18);
        tradingPage.openHoldingTimeTooltip("0.5-6h");
        tradingPage.checkHoldingTimeTooltip(2, 18);
        tradingPage.openHoldingTimeTooltip("6-24h");
        tradingPage.checkHoldingTimeTooltip(3, 27);
        tradingPage.openHoldingTimeTooltip("24h>");
        tradingPage.checkHoldingTimeTooltip(1, 9);
    }

    @Test
    @AllureId("971")
    @Feature("BMS-103 Holding time")
    @DisplayName("Test that Holding Time header shows correct info in tooltips")
    public void holdingTimeHeadersAnnotationsTest() throws SQLException, InterruptedException {

        tradingPage.deleteClientDeals(client.getUcid());

        MtMt4TradesCoercedObject trade0 = generateMt4TradesCoercedRandomized(client);
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

        trade0.profitUsd = 11.18;
        trade1.openTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 0, 1);
        trade1.closeTime = getCurrentTimestampDbFormat();
        trade2.openTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 9, 59);
        trade2.closeTime = getCurrentTimestampDbFormat();
        trade11.openTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 9, 59);
        trade11.closeTime = getCurrentTimestampDbFormat();

        trade3.openTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 10);
        trade3.closeTime = getCurrentTimestampDbFormat();
        trade4.openTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 29, 59);
        trade4.closeTime = getCurrentTimestampDbFormat();

        trade5.openTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 30);
        trade5.closeTime = getCurrentTimestampDbFormat();
        trade6.openTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 5, 59, 59);
        trade6.closeTime = getCurrentTimestampDbFormat();

        trade7.openTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 6, 0);
        trade7.closeTime = getCurrentTimestampDbFormat();
        trade8.openTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 23, 59, 59);
        trade8.closeTime = getCurrentTimestampDbFormat();

        trade9.openTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 24, 0);
        trade9.closeTime = getCurrentTimestampDbFormat();
        trade10.openTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 1, 0, 1, 24, 0, 1);
        trade10.closeTime = getCurrentTimestampDbFormat();

        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade0, trade1, trade2, trade3, trade4, trade5, trade6, trade7, trade8, trade9, trade10, trade11));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        tradingPage.checkHoldingTimeHeader("0-10min", 33);
    }

    @Test
    @AllureId("972")
    @Feature("BMS-103 Holding time")
    @DisplayName("Test that Holding Time header shows empty state when there is no records in DB")
    public void holdingTimeHeadersEmptyStateTest() throws SQLException, InterruptedException {
        tradingPage.deleteClientDeals(client.getUcid());

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        tradingPage.checkHoldingTimeEmpty();
    }

    @Test
    @AllureId("973")
    @Feature("BMS-103 Holding time")
    @DisplayName("Test that Holding Time graph shows error state")
    public void holdingTimeHeadersErrorStateTest() throws SQLException, InterruptedException {
        tradingPage.deleteClientDeals(client.getUcid());

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.mockDurationError();
        tradingPage.navigate(client.getUcid());
        tradingPage.checkHoldingTimeEmpty();
        tradingPage.checkHoldingTimeErrorState();
    }
}
