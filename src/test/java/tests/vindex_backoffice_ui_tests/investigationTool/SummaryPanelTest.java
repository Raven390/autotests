package tests.vindex_backoffice_ui_tests.investigationTool;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalObject;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.db.clickhouse.mt_mt4_trades.MtMt4TradesObject;
import business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedObject;
import business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObject;
import business_objects.db.clickhouse.s3_fact_login_metrics.S3FactLoginMetricsObject;
import business_objects.db.clickhouse.segmentation_table.SegmentationTableObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import helpers.data.ClientHelper;
import helpers.data.enums.*;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseWeb;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateStaticCrmTbAccountActive;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalObjectFactory.generateCrmTbWithdrawalObjectByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.mt_mt4_trades.MtMt4TradesObjectFactory.generateMt4TradesObject;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateTradeByClient;
import static business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObjectFactory.generateMtMt5PositionsObject;
import static business_objects.db.clickhouse.s3_fact_login_metrics.S3FactLoginMetricsFactory.generateS3FactLoginMetricsClient;
import static helpers.api.AbuseRegistryHelper.addFraudsForClient;
import static helpers.data.enums.FraudTypeOld.*;
import static helpers.database.BoHelper.*;
import static helpers.database.ChHelper.calculateWithdrawalsValue;
import static helpers.database.DbHelper.*;
import static utils.Constants.*;
import static utils.Utils.*;
import static utils.Utils.getRandomRoundedDouble;

public class SummaryPanelTest extends TestBaseWeb {
    // Format: US format with comma as thousands separator, dot as decimal, ALWAYS 2 decimals
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Locale.US));
    private static final ClientHelper client = new ClientHelper(222_201, "d555fa11-3e45-44d3-8070-e28eaff987c7", Brand.INFINOX, Regulator.VFSC2, 222_201_001, 22_201_002, 42);
    private static final CrmTbUserObject crmTbUser = generateStaticUserByClient(client);
    private static CrmTbAccountObject account1;
    private static MtAccountObject mtAccount1;

    static {
        DECIMAL_FORMAT.setMinimumFractionDigits(2);
    }

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException,
            InterruptedException {
        deleteObjectFromDb(ACCOUNT_IB_RELATION_TABLE_NAME, "ucid ='" + client.getUcid() + "'");
        deleteObjectFromDb(S3_FACT_IB_SALES_COMMISSIONS, "ucid ='" + client.getUcid() + "'");
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        account1 = generateStaticCrmTbAccountActive(client);
        insertCrmAccountsToDb(account1);
        mtAccount1 = generateMtAccountByCrmTbAccount(account1);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, mtAccount1);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1022")
    @Feature("BMS-62 Clients summary panel")
    @DisplayName("Clients summary panel PNL")
    public void clientSummaryPnlTest() {
        // Clean ALL related data
        deleteObjectFromDb(S3_FACT_LOGIN_METRICS_TABLE_NAME, "ucid ='" + client.getUcid() + "'");
        deleteObjectFromDb(MT5_DEALS_COERCED_TABLE_NAME, "ucid ='" + client.getUcid() + "'");
        deleteObjectFromDb(MT4_TRADES_COERCED_TABLE_NAME, "ucid ='" + client.getUcid() + "'");
        deleteObjectFromDb(MT5_POSITIONS_TABLE_NAME, "ucid ='" + client.getUcid() + "'");
        deleteObjectFromDb(MT4_TRADES_TABLE_NAME, "ucid ='" + client.getUcid() + "'");

        Allure.step("Generate historical data what not include current date");
        S3FactLoginMetricsObject historyMetrics1 = generateS3FactLoginMetricsClient(client);
        historyMetrics1.setDate(getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, 0, 0, 1, 0, 0));
        historyMetrics1.setDailyNetClosedPnl(getRandomRoundedDouble(0, 555_555));

        S3FactLoginMetricsObject historyMetrics2 = generateS3FactLoginMetricsClient(client);
        historyMetrics2.setDate(getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, 0, 0, 2, 0, 0));
        historyMetrics2.setDailyNetClosedPnl(getRandomRoundedDouble(0, 555_555));

        insertObjectsToDb(S3_FACT_LOGIN_METRICS_TABLE_NAME, List.of(historyMetrics1, historyMetrics2));

        // Calculate daily_net_closed_pnl_d1_usd (historical PNL)
        double dailyNetClosedPnlD1 = historyMetrics1.getDailyNetClosedPnl() + historyMetrics2.getDailyNetClosedPnl();

        Allure.step("Generate MT5 closed deals (today_pnl_usd)");
        Mt5DealsCoercedObject deal1 = generateTradeByClient(client);
        deal1.setTime(getCurrentTimestampDbFormat());
        deal1.setProfitUsd(getRandomRoundedDouble(100, 1000));
        deal1.setCommissionUsd(getRandomRoundedDouble(10, 100));
        deal1.setStorageUsd(getRandomRoundedDouble(5, 50));

        Mt5DealsCoercedObject deal2 = generateTradeByClient(client);
        deal2.setTime(getCurrentTimestampDbFormat());
        deal2.setProfitUsd(getRandomRoundedDouble(100, 1000));
        deal2.setCommissionUsd(getRandomRoundedDouble(10, 100));
        deal2.setStorageUsd(getRandomRoundedDouble(5, 50));

        insertObjectsToDb(MT5_DEALS_COERCED_TABLE_NAME, List.of(deal1, deal2));

        // Calculate today_pnl_usd (today's closed deals: profit + commission + storage)
        double todayPnlUsd = (deal1.getProfitUsd() + deal1.getCommissionUsd() + deal1.getStorageUsd()) + (deal2.getProfitUsd() + deal2.getCommissionUsd() + deal2.getStorageUsd());

        // realized_pnl_usd = daily_net_closed_pnl_d1_usd + today_pnl_usd
        double realizedPnlUsd = dailyNetClosedPnlD1 + todayPnlUsd;

        Allure.step("Generate MT4 open trades (floating_pnl_mt4)");
        MtMt4TradesObject mt4Trade1 = generateMt4TradesObject(client);
        mt4Trade1.setCloseTime("1970-01-01 00:00:00");
        mt4Trade1.setCmd(0); // Buy order
        mt4Trade1.setProfitUsd(getRandomRoundedDouble(50, 500));
        mt4Trade1.setCommissionUsd(getRandomRoundedDouble(5, 50));
        mt4Trade1.setStorageUsd(getRandomRoundedDouble(2, 20));

        MtMt4TradesObject mt4Trade2 = generateMt4TradesObject(client);
        mt4Trade2.setCloseTime("1970-01-01 00:00:00");
        mt4Trade2.setCmd(1); // Sell order
        mt4Trade2.setProfitUsd(getRandomRoundedDouble(50, 500));
        mt4Trade2.setCommissionUsd(getRandomRoundedDouble(5, 50));
        mt4Trade2.setStorageUsd(getRandomRoundedDouble(2, 20));

        insertObjectsToDb(MT4_TRADES_TABLE_NAME, List.of(mt4Trade1, mt4Trade2));

        // Calculate floating_pnl_mt4_usd (profit + storage + commission)
        double floatingPnlMt4 = (mt4Trade1.getProfitUsd() + mt4Trade1.getCommissionUsd() + mt4Trade1.getStorageUsd()) + (mt4Trade2.getProfitUsd() + mt4Trade2.getCommissionUsd() + mt4Trade2.getStorageUsd());

        Allure.step("Generate MT5 open positions (floating_pnl_mt5)");
        MtMt5PositionsObject position1 = generateMtMt5PositionsObject(client);
        position1.setAccount(client.getTradingAccount());
        position1.setServerId(client.getServerId());
        position1.setIsDeleted(0);
        position1.setAction(0); // Buy
        position1.setProfitUsd(getRandomRoundedDouble(50, 500));
        position1.setStorageUsd(getRandomRoundedDouble(2, 20));

        MtMt5PositionsObject position2 = generateMtMt5PositionsObject(client);
        position2.setAccount(client.getTradingAccount());
        position2.setServerId(client.getServerId());
        position2.setIsDeleted(0);
        position2.setAction(1); // Sell
        position2.setProfitUsd(getRandomRoundedDouble(50, 500));
        position2.setStorageUsd(getRandomRoundedDouble(2, 20));

        insertObjectsToDb(MT5_POSITIONS_TABLE_NAME, List.of(position1, position2));

        // Calculate floating_pnl_mt5_usd (profit + storage, NO commission for positions)
        double floatingPnlMt5 = (position1.getProfitUsd() + position1.getStorageUsd()) + (position2.getProfitUsd() + position2.getStorageUsd());

        // trading_client_pnl_usd = realized_pnl_usd + floating_pnl_usd
        // where: realized_pnl_usd = daily_net_closed_pnl_d1_usd + today_pnl_usd
        //        floating_pnl_usd = floating_pnl_mt4_usd + floating_pnl_mt5_usd
        double expectedPnl = realizedPnlUsd + floatingPnlMt4 + floatingPnlMt5;

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab(client.getUcid());

        String formattedExpectedPnl = DECIMAL_FORMAT.format(roundDouble(expectedPnl, 2));
        generalTab.checkSummaryPanelValue("Trading PNL", formattedExpectedPnl);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1027")
    @Feature("BMS-62 Clients summary panel")
    @DisplayName("Clients summary panel Withdrawals")
    public void clientSummaryWithdrawalsTest() {
        deleteObjectFromDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, "ucid ='" + client.getUcid() + "'");
        Allure.step("Prepare DB data for test user");
        CrmTbWithdrawalObject withdrawalObject1 = generateCrmTbWithdrawalObjectByClient(client);
        withdrawalObject1.amountUsd = getRandomRoundedDouble(0.00, 500_000);
        withdrawalObject1.reversedAmountUsd = roundDouble((withdrawalObject1.amountUsd / 2), 2);
        withdrawalObject1.statusId = 3;
        CrmTbWithdrawalObject withdrawalObject2 = generateCrmTbWithdrawalObjectByClient(client);
        withdrawalObject2.amountUsd = getRandomRoundedDouble(0.00, 500_000);
        withdrawalObject2.reversedAmountUsd = roundDouble((withdrawalObject2.amountUsd / 2), 2);
        withdrawalObject2.statusId = 5;
        CrmTbWithdrawalObject withdrawalObject3 = generateCrmTbWithdrawalObjectByClient(client);
        withdrawalObject3.amountUsd = getRandomRoundedDouble(0.00, 500_000);
        withdrawalObject3.reversedAmountUsd = roundDouble((withdrawalObject3.amountUsd / 2), 2);
        withdrawalObject3.statusId = 7;
        CrmTbWithdrawalObject withdrawalObject4 = generateCrmTbWithdrawalObjectByClient(client);
        withdrawalObject4.amountUsd = getRandomRoundedDouble(0.00, 500_000);
        withdrawalObject4.reversedAmountUsd = roundDouble((withdrawalObject4.amountUsd / 2), 2);
        withdrawalObject4.statusId = 9;
        CrmTbWithdrawalObject withdrawalObject5 = generateCrmTbWithdrawalObjectByClient(client);
        withdrawalObject5.amountUsd = getRandomRoundedDouble(0.00, 500_000);
        withdrawalObject5.reversedAmountUsd = roundDouble((withdrawalObject5.amountUsd / 2), 2);
        withdrawalObject5.statusId = 16;
        CrmTbWithdrawalObject withdrawalObject6 = generateCrmTbWithdrawalObjectByClient(client);
        withdrawalObject6.amountUsd = getRandomRoundedDouble(0.00, 500_000);
        withdrawalObject6.reversedAmountUsd = roundDouble((withdrawalObject6.amountUsd / 2), 2);
        withdrawalObject6.statusId = 17;
        CrmTbWithdrawalObject withdrawalObject7 = generateCrmTbWithdrawalObjectByClient(client);
        withdrawalObject7.amountUsd = getRandomRoundedDouble(0.00, 500_000);
        withdrawalObject7.reversedAmountUsd = roundDouble((withdrawalObject7.amountUsd / 2), 2);
        withdrawalObject7.statusId = 61;
        withdrawalObject7.status = "1";
        CrmTbWithdrawalObject withdrawalObject8 = generateCrmTbWithdrawalObjectByClient(client);
        withdrawalObject8.amountUsd = getRandomRoundedDouble(0.00, 500_000);
        withdrawalObject8.reversedAmountUsd = roundDouble((withdrawalObject8.amountUsd / 2), 2);
        withdrawalObject8.statusId = 61;
        withdrawalObject8.status = "2";
        insertObjectsToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, List.of(withdrawalObject1, withdrawalObject2, withdrawalObject3, withdrawalObject4, withdrawalObject5, withdrawalObject6, withdrawalObject7, withdrawalObject8));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab(client.getUcid());
        generalTab.checkSummaryPanelValue("Withdrawals", DECIMAL_FORMAT.format(calculateWithdrawalsValue(withdrawalObject1, withdrawalObject2, withdrawalObject3, withdrawalObject4, withdrawalObject5, withdrawalObject6, withdrawalObject7)));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1024")
    @Feature("BMS-62 Clients summary panel")
    @DisplayName("Clients summary panel Segment test")
    public void clientSummarySegmentTest() {
        Allure.step("Prepare DB data for test user");
        SegmentationTableObject segment1 = new SegmentationTableObject();
        segment1.setUcid(client.getUcid());
        segment1.setSegment("Low");
        segment1.setDate(getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, 0, 0, 2, 0, 0));
        SegmentationTableObject segment2 = new SegmentationTableObject();
        segment2.setUcid(client.getUcid());
        segment2.setSegment("Medium");
        segment2.setDate(getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, 0, 0, 1, 0, 0));
        insertObjectsToDb(SEGMENTATION_TABLE_NAME, List.of(segment1, segment2));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab(client.getUcid());
        generalTab.checkSummaryPanelValue("Segment", segment2.getSegment());

    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1025")
    @Feature("BMS-62 Clients summary panel")
    @DisplayName("Clients summary panel Fraud, no frauds")
    public void clientSummaryFraudEmptyTest() throws Exception {
        cleanUserAR(client.getUcid());
        Allure.step("Prepare DB data for test user");

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab(client.getUcid());
        generalTab.checkSummaryPanelValue("Fraud", "No fraud");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1026")
    @Feature("BMS-62 Clients summary panel")
    @DisplayName("Clients summary panel Fraud")
    public void clientSummaryFraudTest() throws Exception {
        cleanUserAR(client.getUcid());
        addFraudsForClient(client, List.of(FraudType.CPA_ABUSE), FraudTypeStatus.CONFIRMED);
        addFraudsForClient(client, List.of(FraudType.MARKET_MANIPULATION), FraudTypeStatus.POTENTIAL);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab(client.getUcid());
        generalTab.checkSummaryPanelFraudValue("Fraud", CPA_ABUSE.getDisplayName());
        generalTab.checkSummaryPanelFraudValue("Fraud", String.format("%s %s", FraudTypeStatus.POTENTIAL.getDisplayName(), MARKET_MANIPULATION.getDisplayName()));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1055")
    @Feature("BMS-976 [FE] Implement the version of layout with Revenue")
    @DisplayName("Clients summary panel Revenue section test")
    public void clientSummaryRevenueTest() {
        deleteObjectFromDb(S3_FACT_LOGIN_METRICS_TABLE_NAME, "ucid ='" + client.getUcid() + "'");
        Allure.step("Prepare DB data for test user");
        S3FactLoginMetricsObject revenue = generateS3FactLoginMetricsClient(client);
        revenue.setDailyCoreSpreadRevenuePe(getRandomRoundedDouble(0.0, 999_999_999.99));
        revenue.setDailyTakerSpreadRevenuePe(getRandomRoundedDouble(0.0, 999_999_999.99));
        revenue.setDailyLpSpreadRevenuePe(getRandomRoundedDouble(0.0, 999_999_999.99));
        revenue.setDailyVbSpreadRevenuePe(getRandomRoundedDouble(0.0, 999_999_999.99));
        revenue.setDailyAppliedMinSpreadRevenuePe(getRandomRoundedDouble(0.0, 999_999_999.99));
        revenue.setDailyAppliedMaxSpreadRevenuePe(getRandomRoundedDouble(0.0, 999_999_999.99));
        revenue.setDailyCoreSpreadRevenueOz(getRandomRoundedDouble(0.0, 999_999_999.99));
        revenue.setDailyTakerSpreadRevenueOz(getRandomRoundedDouble(0.0, 999_999_999.99));
        revenue.setDailyLpSpreadRevenueOz(getRandomRoundedDouble(0.0, 999_999_999.99));
        revenue.setDailyVbSpreadRevenueOz(getRandomRoundedDouble(0.0, 999_999_999.99));
        revenue.setDailyAppliedMinSpreadRevenueOz(getRandomRoundedDouble(0.0, 999_999_999.99));
        revenue.setDailyAppliedMaxSpreadRevenueOz(getRandomRoundedDouble(0.0, 999_999_999.99));
        revenue.setDailyCommissionRevenue(getRandomRoundedDouble(0.0, 999_999_999.99));
        revenue.setDailySwapsRevenue(getRandomRoundedDouble(0.0, 999_999_999.99));
        insertObjectToDb(S3_FACT_LOGIN_METRICS_TABLE_NAME, revenue);

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab(client.getUcid());
        generalTab.checkSummaryPanelValue(
                "Company RFR", DECIMAL_FORMAT.format(generalTab.calculateRevenue(revenue))
        );
    }
}
