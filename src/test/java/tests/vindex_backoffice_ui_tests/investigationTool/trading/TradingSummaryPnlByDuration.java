package tests.vindex_backoffice_ui_tests.investigationTool.trading;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.*;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoercedRandomized;
import static helpers.data.enums.DateTimeFormat.DATE_AND_TIME;
import static helpers.database.DbHelper.insertObjectToDb;
import static helpers.database.DbHelper.insertObjectsToDb;
import static utils.Constants.*;
import static utils.Utils.*;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import helpers.data.ClientHelper;
import helpers.data.enums.Brand;
import helpers.data.enums.Regulator;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseWeb;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
public class TradingSummaryPnlByDuration extends TestBaseWeb {
    private static final ClientHelper client;

    static {
        client = ClientHelper.builder()
                .userId(202_001)
                .uid("e5880ca5-8578-4a1e-969d-7a64716ca40f")
                .brand(Brand.INFINOX)
                .regulator(Regulator.FCA)
                .tradingAccount(202_001_001)
                .serverId(42)
                .build();
    }

    private static CrmTbUserObject crmTbUser = generateStaticUserByClient(client);
    private static CrmTbAccountObject account1 = generateStaticCrmTbAccountActive(client);

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        crmTbUser.firstName = "Pienell";
        crmTbUser.lastName = "Duration";
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertCrmAccountsToDb(account1);
    }

    @Test
    @AllureId("914")
    @Feature("BMS-216 PNL by duration")
    @DisplayName("Test that PNL by duration shows correct amounts in tooltips")
    public void pnlByDurationAmountsTest() throws SQLException, InterruptedException {

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

        trade0.setProfitUsd(11.18);
        trade1.setOpenTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 0, 1));
        trade1.setCloseTime(getCurrentTimestampDbFormat());
        trade2.setOpenTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 9, 59));
        trade2.setCloseTime(getCurrentTimestampDbFormat());

        trade3.setOpenTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 10));
        trade3.setCloseTime(getCurrentTimestampDbFormat());
        trade4.setOpenTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 29, 59));
        trade4.setCloseTime(getCurrentTimestampDbFormat());

        trade5.setOpenTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 30));
        trade5.setCloseTime(getCurrentTimestampDbFormat());
        trade6.setOpenTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 5, 59, 59));
        trade6.setCloseTime(getCurrentTimestampDbFormat());

        trade7.setOpenTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 6, 0));
        trade7.setCloseTime(getCurrentTimestampDbFormat());
        trade8.setOpenTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 23, 59, 59));
        trade8.setCloseTime(getCurrentTimestampDbFormat());

        trade9.setOpenTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 24, 0));
        trade9.setCloseTime(getCurrentTimestampDbFormat());
        trade10.setOpenTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 1, 0, 1, 24, 0, 1));
        trade10.setCloseTime(getCurrentTimestampDbFormat());

        insertObjectsToDb(
                MT4_TRADES_COERCED_TABLE_NAME,
                List.of(trade0, trade1, trade2, trade3, trade4, trade5, trade6, trade7, trade8, trade9, trade10));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        tradingPage.openPnlDurationTooltip("0-10min");
        tradingPage.checkTextPnlDurationTooltipAmount(
                (trade0.getProfitUsd() + trade0.getCommissionUsd() + trade0.getStorageUsd())
                        + (trade1.getProfitUsd() + trade1.getCommissionUsd() + trade1.getStorageUsd())
                        + (trade2.getProfitUsd() + trade2.getCommissionUsd() + trade2.getStorageUsd()));
        //        tradingPage.checkTextPnlDurationTooltipPercentage("75");
        tradingPage.openPnlDurationTooltip("10-30min");
        tradingPage.checkTextPnlDurationTooltipAmount(
                (trade3.getProfitUsd() + trade3.getCommissionUsd() + trade3.getStorageUsd())
                        + (trade4.getProfitUsd() + trade4.getCommissionUsd() + trade4.getStorageUsd()));
        //        tradingPage.checkTextPnlDurationTooltipPercentage("25");
        tradingPage.openPnlDurationTooltip("0.5-6h");
        tradingPage.checkTextPnlDurationTooltipAmount(
                (trade5.getProfitUsd() + trade5.getCommissionUsd() + trade5.getStorageUsd())
                        + (trade6.getProfitUsd() + trade6.getCommissionUsd() + trade6.getStorageUsd()));
        tradingPage.openPnlDurationTooltip("6-24h");
        tradingPage.checkTextPnlDurationTooltipAmount(
                (trade7.getProfitUsd() + trade7.getCommissionUsd() + trade7.getStorageUsd())
                        + (trade8.getProfitUsd() + trade8.getCommissionUsd() + trade8.getStorageUsd())
                        + (trade9.getProfitUsd() + trade9.getCommissionUsd() + trade9.getStorageUsd()));
        tradingPage.openPnlDurationTooltip("24h>");
        tradingPage.checkTextPnlDurationTooltipAmount(
                trade10.getProfitUsd() + trade10.getCommissionUsd() + trade10.getStorageUsd());
    }

    @Test
    @AllureId("914")
    @Feature("BMS-216 PNL by duration")
    @DisplayName("Test that PNL by duration shows correct percentage in tooltips")
    public void pnlByDurationPercentage() throws SQLException, InterruptedException {

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

        trade0.setProfitUsd(11.18);
        trade1.setOpenTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 0, 1));
        trade1.setCloseTime(getCurrentTimestampDbFormat());
        trade2.setOpenTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 9, 59));
        trade2.setCloseTime(getCurrentTimestampDbFormat());

        trade3.setOpenTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 10));
        trade3.setCloseTime(getCurrentTimestampDbFormat());
        trade4.setOpenTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 29, 59));
        trade4.setCloseTime(getCurrentTimestampDbFormat());

        trade5.setOpenTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 30));
        trade5.setCloseTime(getCurrentTimestampDbFormat());
        trade6.setOpenTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 5, 59, 59));
        trade6.setCloseTime(getCurrentTimestampDbFormat());

        trade7.setOpenTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 6, 0));
        trade7.setCloseTime(getCurrentTimestampDbFormat());
        trade8.setOpenTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 23, 59, 59));
        trade8.setCloseTime(getCurrentTimestampDbFormat());

        trade9.setOpenTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 24, 0));
        trade9.setCloseTime(getCurrentTimestampDbFormat());
        trade10.setOpenTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 1, 0, 1, 23, 59, 59));
        trade10.setCloseTime(getCurrentTimestampDbFormat());

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
    public void pnlByDurationTop() throws SQLException, InterruptedException {

        tradingPage.deleteClientDeals(client.getUcid());

        MtMt4TradesCoercedObject trade0 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade1 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade2 = generateMt4TradesCoercedRandomized(client);

        trade0.setOpenTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 0, 1));
        trade0.setCloseTime(getCurrentTimestampDbFormat());
        trade0.setProfitUsd(5000.0);
        trade0.setStorageUsd(0.0);
        trade0.setCommissionUsd(0.0);

        trade1.setOpenTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 20, 1));
        trade1.setCloseTime(getCurrentTimestampDbFormat());
        trade1.setProfitUsd(200.0);
        trade1.setStorageUsd(0.0);
        trade1.setCommissionUsd(0.0);

        trade2.setOpenTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 3, 11));
        trade2.setCloseTime(getCurrentTimestampDbFormat());
        trade2.setProfitUsd(-100.0);
        trade2.setStorageUsd(0.0);
        trade2.setCommissionUsd(0.0);

        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade0, trade2, trade1));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigate(client.getUcid());
        tradingPage.checkMaxProfitableValue(
                (trade0.getProfitUsd() + trade0.getCommissionUsd() + trade0.getStorageUsd()));
        tradingPage.checkMaxLossValue((trade2.getProfitUsd() + trade2.getCommissionUsd() + trade2.getStorageUsd()));
        tradingPage.checkTopProfitCategory("0-10min");
        tradingPage.checkTopLossCategory("0.5-6h");
    }
}
