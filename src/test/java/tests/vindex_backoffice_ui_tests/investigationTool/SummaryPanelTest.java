package tests.vindex_backoffice_ui_tests.investigationTool;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalObject;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedObject;
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
import java.util.List;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateStaticCrmTbAccountActive;
import static business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositObjectFactory.generateDepositByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalObjectFactory.generateWithdrawalByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateTradeByClient;
import static business_objects.db.clickhouse.s3_fact_login_metrics.S3FactLoginMetricsFactory.generateS3FactLoginMetricsClient;
import static helpers.api.AbuseRegistryHelper.addFraudsForClient;
import static helpers.data.enums.FraudTypeOld.*;
import static helpers.database.BoHelper.*;
import static helpers.database.DbHelper.*;
import static utils.Constants.*;
import static utils.Utils.*;
import static utils.Utils.getRandomRoundedDouble;

public class SummaryPanelTest extends TestBaseWeb {

    private static final ClientHelper client = new ClientHelper(222_201, "d555fa11-3e45-44d3-8070-e28eaff987c7", Brand.INFINOX, Regulator.VFSC2, 222_201_001, 22_201_002, 42);
    private static final CrmTbUserObject crmTbUser = generateStaticUserByClient(client);
    private static CrmTbAccountObject account1;
    private static MtAccountObject mtAccount1;

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException,
            InterruptedException {
        deleteObjectFromDb(ACCOUNT_IB_RELATION_TABLE_NAME, "ucid ='" + client.getUcid() + "'");
        deleteObjectFromDb(S3_FACT_IB_SALES_COMMISSIONS, "ucid ='" + client.getUcid() + "'");
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        account1 = generateStaticCrmTbAccountActive(client);
        insertObjectToDb(CRM_TB_ACCOUNT_TABLE_NAME, account1);
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
        deleteObjectFromDb(S3_FACT_LOGIN_METRICS_TABLE_NAME, "ucid ='" + client.getUcid() + "'");
        deleteObjectFromDb(MT5_DEALS_COERCED_TABLE_NAME, "account =" + client.getTradingAccount());
        deleteObjectFromDb(MT5_DEALS_COERCED_TABLE_NAME, "account =" + client.getTradingAccount2());
        Allure.step("Generate historical data what not include current date");
        S3FactLoginMetricsObject historyMetrics1 = generateS3FactLoginMetricsClient(client);
        historyMetrics1.setDate(getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, 0, 0, 1, 0, 0));
        historyMetrics1.setDailyNetClosedPnl(getRandomRoundedDouble(0, 555_555));
        S3FactLoginMetricsObject historyMetrics2 = generateS3FactLoginMetricsClient(client);
        historyMetrics2.setDate(getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, 0, 0, 2, 0, 0));
        historyMetrics2.setDailyNetClosedPnl(getRandomRoundedDouble(0, 555_555));
        insertObjectsToDb(S3_FACT_LOGIN_METRICS_TABLE_NAME, List.of(historyMetrics1, historyMetrics2));
        Allure.step("Generate MT5 deals for current date");
        Mt5DealsCoercedObject deal1 = generateTradeByClient(client);
        deal1.setTime(getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 0, 0, 1));
        deal1.setProfitUsd(getRandomRoundedDouble(0, 555_555));
        deal1.setCommissionUsd(getRandomRoundedDouble(0, 555_555));
        deal1.setStorageUsd(getRandomDouble());
        Allure.step("Generate MT5 deals outside of current date");
        Mt5DealsCoercedObject deal2 = generateTradeByClient(client);
        deal2.setTime(getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 1, 0, 0, 1));
        deal2.setProfitUsd(getRandomRoundedDouble(0, 555_555));
        deal2.setCommissionUsd(getRandomRoundedDouble(0, 555_555));
        deal2.setStorageUsd(getRandomDouble());
        insertObjectsToDb(MT5_DEALS_COERCED_TABLE_NAME, List.of(deal1, deal2));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab(client.getUcid());
        generalTab.checkSummaryPanelValue("Total PNL", tradingPage.calculatePnlByDeal(deal1));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1023")
    @Feature("BMS-62 Clients summary panel")
    @DisplayName("Clients summary panel Deposits")
    public void clientSummaryDepositsTest() {
        deleteObjectFromDb(CRM_DEPOSIT_TABLE_NAME, "ucid ='" + client.getUcid() + "'");
        Allure.step("Prepare DB data for test user");
        CrmTbDepositObject depositObject1 = generateDepositByClient(client);
        depositObject1.amountUsd = getRandomRoundedDouble(0.00, 500_000);
        depositObject1.statusId = 5;
        CrmTbDepositObject depositObject2 = generateDepositByClient(client);
        depositObject2.amountUsd = getRandomRoundedDouble(0.00, 500_000);
        depositObject2.statusId = 5;
        insertObjectsToDb(CRM_DEPOSIT_TABLE_NAME, List.of(depositObject1, depositObject2));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab(client.getUcid());
        generalTab.checkSummaryPanelValue("Deposits", depositObject1.amountUsd + depositObject2.amountUsd);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1027")
    @Feature("BMS-62 Clients summary panel")
    @DisplayName("Clients summary panel Withdrawals")
    public void clientSummaryWithdrawalsTest() {
        deleteObjectFromDb(CRM_WITHDRAWAL_TABLE_NAME, "ucid ='" + client.getUcid() + "'");
        Allure.step("Prepare DB data for test user");
        CrmTbWithdrawalObject withdrawalObject1 = generateWithdrawalByClient(client);
        withdrawalObject1.amountUsd = getRandomRoundedDouble(0.00, 500_000);
        withdrawalObject1.statusId = 16;
        CrmTbWithdrawalObject withdrawalObject2 = generateWithdrawalByClient(client);
        withdrawalObject2.amountUsd = getRandomRoundedDouble(0.00, 500_000);
        withdrawalObject2.statusId = 7;
        insertObjectsToDb(CRM_WITHDRAWAL_TABLE_NAME, List.of(withdrawalObject1, withdrawalObject2));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab(client.getUcid());
        generalTab.checkSummaryPanelValue("Withdrawals", withdrawalObject1.amountUsd + withdrawalObject2.amountUsd);
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
        generalTab.checkSummaryPanelValue("Revenue", generalTab.calculateRevenue(revenue));
    }


}
