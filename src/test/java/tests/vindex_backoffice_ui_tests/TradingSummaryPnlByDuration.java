package tests.vindex_backoffice_ui_tests;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import helpers.data.ClientHelper;
import helpers.data.enums.Brand;
import helpers.data.enums.Regulator;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseWeb;

import java.sql.SQLException;
import java.util.List;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.*;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoercedRandomized;
import static helpers.data.enums.DateTimeFormat.DATE_AND_TIME;
import static helpers.database.DbHelper.insertObjectToDb;
import static helpers.database.DbHelper.insertObjectsToDb;
import static utils.Constants.*;
import static utils.Utils.*;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
public class TradingSummaryPnlByDuration extends TestBaseWeb {


    private static ClientHelper client = new ClientHelper(202_001, "e5880ca5-8578-4a1e-969d-7a64716ca40f", Brand.INFINOX, Regulator.FCA, 202_001_001, 42);
    private static CrmTbUserObject crmTbUser = generateStaticUserByClient(client);
    private static CrmTbAccountObject account1 = generateStaticCrmTbAccountActive(client);

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        crmTbUser.firstName = "Pienell";
        crmTbUser.lastName = "Duration";
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, account1);
    }

    @Test
    @AllureId("914")
    @Feature("BMS-216 PNL by duration")
    @DisplayName("Test that PNL by duration shows correct amounts in tooltips")
    public void pnlByDurationAmountsTest() throws ReflectiveOperationException, SQLException, InterruptedException {

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
        tradingPage.openPnlDurationTooltip("0-10min");
        tradingPage.checkTextPnlDurationTooltipAmount((trade0.profitUsd + trade0.commissionUsd + trade0.storageUsd) + (trade1.profitUsd + trade1.commissionUsd + trade1.storageUsd) + (trade2.profitUsd + trade2.commissionUsd + trade2.storageUsd));
//        tradingPage.checkTextPnlDurationTooltipPercentage("75");
        tradingPage.openPnlDurationTooltip("10-30min");
        tradingPage.checkTextPnlDurationTooltipAmount((trade3.profitUsd + trade3.commissionUsd + trade3.storageUsd) + (trade4.profitUsd + trade4.commissionUsd + trade4.storageUsd));
//        tradingPage.checkTextPnlDurationTooltipPercentage("25");
        tradingPage.openPnlDurationTooltip("0.5-6h");
        tradingPage.checkTextPnlDurationTooltipAmount((trade5.profitUsd + trade5.commissionUsd + trade5.storageUsd) + (trade6.profitUsd + trade6.commissionUsd + trade6.storageUsd));
        tradingPage.openPnlDurationTooltip("6-24h");
        tradingPage.checkTextPnlDurationTooltipAmount((trade7.profitUsd + trade7.commissionUsd + trade7.storageUsd) + (trade8.profitUsd + trade8.commissionUsd + trade8.storageUsd) + (trade9.profitUsd + trade9.commissionUsd + trade9.storageUsd));
        tradingPage.openPnlDurationTooltip("24h>");
        tradingPage.checkTextPnlDurationTooltipAmount(trade10.profitUsd + trade10.commissionUsd + trade10.storageUsd);
    }

    @Test
    @AllureId("914")
    @Feature("BMS-216 PNL by duration")
    @DisplayName("Test that PNL by duration shows correct percentage in tooltips")
    public void pnlByDurationPercentage() throws ReflectiveOperationException, SQLException, InterruptedException {

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
        trade10.openTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 1, 0, 1, 23, 59, 59);
        trade10.closeTime = getCurrentTimestampDbFormat();

        insertObjectToDb(MT4_TRADES_COERCED_TABLE_NAME, trade0);

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        tradingPage.openPnlDurationTooltip("0-10min");
        tradingPage.checkTextPnlDurationTooltipPercentage(100);
        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade1, trade3));
        page.reload();
        tradingPage.openPnlDurationTooltip("0-10min");
        tradingPage.checkTextPnlDurationTooltipPercentage("66.7");
        tradingPage.openPnlDurationTooltip("10-30min");
        tradingPage.checkTextPnlDurationTooltipPercentage("33.3");
        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade4, trade5));
        page.reload();
        tradingPage.openPnlDurationTooltip("0-10min");
        tradingPage.checkTextPnlDurationTooltipPercentage("40");
        tradingPage.openPnlDurationTooltip("10-30min");
        tradingPage.checkTextPnlDurationTooltipPercentage("40");
        tradingPage.openPnlDurationTooltip("0.5-6h");
        tradingPage.checkTextPnlDurationTooltipPercentage("20");
    }

    @Test
    @AllureId("915")
    @Feature("BMS-216 PNL by duration")
    @DisplayName("Test that PNL by duration shows correct tops")
    public void pnlByDurationTop() throws ReflectiveOperationException, SQLException, InterruptedException {

        tradingPage.deleteClientDeals(client.getUcid());

        MtMt4TradesCoercedObject trade0 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade1 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade2 = generateMt4TradesCoercedRandomized(client);

        trade0.openTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 0, 1);
        trade0.closeTime = getCurrentTimestampDbFormat();
        trade0.profitUsd = 5000.0;
        trade0.storageUsd = 0.0;
        trade0.commissionUsd = 0.0;

        trade1.openTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 20, 1);
        trade1.closeTime = getCurrentTimestampDbFormat();
        trade1.profitUsd = 200.0;
        trade1.storageUsd = 0.0;
        trade1.commissionUsd = 0.0;

        trade2.openTime = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 3, 11);
        trade2.closeTime = getCurrentTimestampDbFormat();
        trade2.profitUsd = -100.0;
        trade2.storageUsd = 0.0;
        trade2.commissionUsd = 0.0;


        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade0, trade2, trade1));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        tradingPage.checkMaxProfitableValue((trade0.profitUsd + trade0.commissionUsd + trade0.storageUsd));
        tradingPage.checkMaxLossValue((trade2.profitUsd + trade2.commissionUsd + trade2.storageUsd));
        tradingPage.checkTopProfitCategory("0-10min");
        tradingPage.checkTopLossCategory("0.5-6h");
    }
}
