package tests.vindex_backoffice_ui_tests.investigationTool;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntity;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.db.clickhouse.mt_balance_orders_table.MtBalanceOrdersObject;
import business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedObject;
import business_objects.db.clickhouse.s3_fact_login_metrics.S3FactLoginMetricsObject;
import business_objects.db.clickhouse.segmentation_table.SegmentationTableObject;
import helpers.data.ClientHelper;
import helpers.data.enums.*;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateStaticCrmTbAccountActive;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.mt_balance_orders_table.MtBalanceOrdersObjectFactory.generateMtBalanceOrder;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateTradeByClient;
import static business_objects.db.clickhouse.s3_fact_login_metrics.S3FactLoginMetricsFactory.generateS3FactLoginMetricsClient;
import static helpers.api.AbuseRegistryHelper.addFraudsForClient;
import static helpers.data.enums.FraudTypeOld.CPA_ABUSE;
import static helpers.data.enums.FraudTypeOld.MARKET_MANIPULATION;
import static helpers.database.BoHelper.cleanUserAR;
import static helpers.database.DbHelper.*;
import static utils.Constants.*;
import static utils.Utils.*;

class SummaryPanelTest extends TestBaseWeb {
    private static final ClientHelper client = new ClientHelper(222_201, "d555fa11-3e45-44d3-8070-e28eaff987c7", Brand.INFINOX, Regulator.VFSC2, 222_201_001, 22_201_002, 42);
    private static final ClientHelper clientBybit = new ClientHelper(222_202, "d555fa11-3e45-44d3-8070-e28eaff987c8", Brand.BYBIT, Regulator.VFSC2, 222_201_003, 22_201_004, 42);
    private static final CrmTbUserObject crmTbUser = generateStaticUserByClient(client);
    private static final CrmTbUserObject crmTbUserBybit = generateStaticUserByClient(clientBybit);
    private static final DecimalFormat decimalFormat = new DecimalFormat("#,##0.##", new DecimalFormatSymbols(Locale.US));
    private static CrmTbAccountObject account1;
    private static MtAccountObject mtAccount1;
    private static CrmTbAccountObject accountBybit;
    private static MtAccountObject mtAccountBybit;
    private static final String CLIENT_UCID_WHERE = "ucid ='" + client.getUcid() + "'";
    private static final String CLIENT_BYBIT_UCID_WHERE = "ucid ='" + clientBybit.getUcid() + "'";

    @BeforeAll
    static void setup() {
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUserBybit);
        account1 = generateStaticCrmTbAccountActive(client);
        accountBybit = generateStaticCrmTbAccountActive(clientBybit);
        accountBybit.serverName = "123bybit" + accountBybit.serverName;
        insertCrmAccountsToDb(account1);
        insertCrmAccountsToDb(accountBybit);
        mtAccount1 = generateMtAccountByCrmTbAccount(account1);
        mtAccountBybit = generateMtAccountByCrmTbAccount(accountBybit);
        mtAccountBybit.server = "123bybit" + mtAccountBybit.server;
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, mtAccount1);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, mtAccountBybit);
    }

    @AfterAll
    static void teardown() {
        deleteObjectFromDb(CRM_USER_TABLE_NAME, CLIENT_UCID_WHERE);
        deleteObjectFromDb(CRM_USER_TABLE_NAME, CLIENT_BYBIT_UCID_WHERE);
        deleteObjectFromDb(MT5_DEALS_COERCED_TABLE_NAME, CLIENT_UCID_WHERE);
        deleteObjectFromDb(MT_BALANCE_ORDERS_TABLE_NAME, CLIENT_UCID_WHERE);
        deleteEntryFromDb(S3_FACT_LOGIN_METRICS_TABLE_NAME, CLIENT_UCID_WHERE);
        deleteObjectFromDb(MT_BALANCE_ORDERS_TABLE_NAME, CLIENT_BYBIT_UCID_WHERE);
        deleteEntryFromDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, CLIENT_UCID_WHERE);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1022")
    @Feature("BMS-2765 Clients summary panel")
    @DisplayName("Clients summary panel PNL")
    void clientSummaryPnlTest() {
        // Clean ALL related data
        deleteObjectFromDb(S3_FACT_LOGIN_METRICS_TABLE_NAME, CLIENT_UCID_WHERE);
        deleteObjectFromDb(MT5_DEALS_COERCED_TABLE_NAME, CLIENT_UCID_WHERE);

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
        double realizedPnl = (deal1.getProfitUsd() + deal1.getCommissionUsd() + deal1.getStorageUsd()) + (deal2.getProfitUsd() + deal2.getCommissionUsd() + deal2.getStorageUsd());

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab(client.getUcid());

        String formattedExpectedPnl = decimalFormat.format(roundDouble(realizedPnl + mtAccount1.floatingPnlUsd, 2));
        generalTab.checkSummaryPanelValue("Trading PNL", formattedExpectedPnl);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1849")
    @Feature("BMS-2765 Clients summary panel")
    @DisplayName("Clients summary panel Deposits")
    void clientSummaryDepositsTest() {
        deleteObjectFromDb(MT_BALANCE_ORDERS_TABLE_NAME, CLIENT_UCID_WHERE);

        Allure.step("Prepare DB data for test user");
        MtBalanceOrdersObject deposit1 = generateMtBalanceOrder(client, getRandomRoundedDouble(0.01, 99_999.99), getRandomRoundedDouble(0.01, 99_999.99), getCurrentTimestampDbFormat());
        deposit1.setComment("123 deposit 456");
        MtBalanceOrdersObject deposit2 = generateMtBalanceOrder(client, getRandomRoundedDouble(0.01, 99_999.99), getRandomRoundedDouble(0.01, 99_999.99), getCurrentTimestampDbFormat());
        deposit2.setComment("123 deposit 456");
        MtBalanceOrdersObject deposit3 = generateMtBalanceOrder(client, getRandomRoundedDouble(0.01, 99_999.99), getRandomRoundedDouble(0.01, 99_999.99), getCurrentTimestampDbFormat());
        deposit3.setComment("123 pam 456");
        insertObjectsToDb(MT_BALANCE_ORDERS_TABLE_NAME, List.of(deposit1, deposit2, deposit3));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab(client.getUcid());

        //we don't count deposits with comment containing '%pam%'
        String formattedExpectedPnl = decimalFormat.format(roundDouble(deposit1.amountUsd + deposit2.amountUsd, 2)) + " USD";
        generalTab.checkSummaryPanelValue("Deposits", formattedExpectedPnl);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1850")
    @Feature("BMS-2765 Clients summary panel")
    @DisplayName("Clients summary panel Deposits Bybit")
    void clientSummaryDepositsBybitTest() {
        deleteObjectFromDb(MT_BALANCE_ORDERS_TABLE_NAME, CLIENT_BYBIT_UCID_WHERE);

        Allure.step("Prepare DB data for test user");
        MtBalanceOrdersObject deposit1 = generateMtBalanceOrder(clientBybit, getRandomRoundedDouble(0.01, 99_999.99), getRandomRoundedDouble(0.01, 99_999.99), getCurrentTimestampDbFormat());
        deposit1.setServerName("123bybit" + deposit1.serverName);
        MtBalanceOrdersObject deposit2 = generateMtBalanceOrder(clientBybit, getRandomRoundedDouble(0.01, 99_999.99), getRandomRoundedDouble(0.01, 99_999.99), getCurrentTimestampDbFormat());
        deposit2.setServerName("123bybit" + deposit2.serverName);
        MtBalanceOrdersObject deposit3 = generateMtBalanceOrder(clientBybit, getRandomRoundedDouble(-99_999.99, -0.01), getRandomRoundedDouble(-99_999.99, -0.01), getCurrentTimestampDbFormat());
        deposit3.setServerName("123bybit" + deposit3.serverName);
        insertObjectsToDb(MT_BALANCE_ORDERS_TABLE_NAME, List.of(deposit1, deposit2, deposit3));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab(clientBybit.getUcid());

        //we don't count negative deposits
        String formattedExpectedPnl = decimalFormat.format(roundDouble(deposit1.amountUsd + deposit2.amountUsd, 2)) + " USD";
        generalTab.checkSummaryPanelValue("Deposits", formattedExpectedPnl);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1027")
    @Feature("BMS-2765 Clients summary panel")
    @DisplayName("Clients summary panel Withdrawals")
    void clientSummaryWithdrawalsTest() {
        deleteObjectFromDb(MT_BALANCE_ORDERS_TABLE_NAME, CLIENT_UCID_WHERE);
        deleteEntryFromDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, CLIENT_UCID_WHERE);

        CrmTbWithdrawalEntity crmTbWithdrawal1 = generateCrmTbWithdrawalEntityByClient(client);
        crmTbWithdrawal1.setStatusId(16);
        crmTbWithdrawal1.setStatusGroup("Success");
        CrmTbWithdrawalEntity crmTbWithdrawal2 = generateCrmTbWithdrawalEntityByClient(client);
        crmTbWithdrawal2.setStatusId(16);
        crmTbWithdrawal2.setStatusGroup("Success");
        insertObjectsToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, List.of(crmTbWithdrawal1, crmTbWithdrawal2));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab(client.getUcid());

        String formattedExpectedWithdrawals = decimalFormat.format(Math.abs(roundDouble(crmTbWithdrawal1.getAmountUsd().doubleValue() + crmTbWithdrawal2.getAmountUsd().doubleValue(), 2)));
        generalTab.checkSummaryPanelValue("Withdrawals", formattedExpectedWithdrawals);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1024")
    @Feature("BMS-62 Clients summary panel")
    @DisplayName("Clients summary panel Segment test")
    void clientSummarySegmentTest() {
        Allure.step("Prepare DB data for test user");
        SegmentationTableObject segment1 = SegmentationTableObject.builder().ucid(client.getUcid()).segment("Low").date(getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, 0, 0, 2, 0, 0)).build();
        SegmentationTableObject segment2 = SegmentationTableObject.builder().ucid(client.getUcid()).segment("Medium").date(getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, 0, 0, 1, 0, 0)).build();

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
    void clientSummaryFraudEmptyTest() throws Exception {
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
    void clientSummaryFraudTest() throws Exception {
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
    @Feature("BMS-2765 Clients summary panel Revenue section test")
    @DisplayName("Clients summary panel Revenue section test")
    void clientSummaryRevenueTest() {
        deleteObjectFromDb(S3_FACT_LOGIN_METRICS_TABLE_NAME, CLIENT_UCID_WHERE);

        Allure.step("Prepare DB data for test user");
        S3FactLoginMetricsObject revenue = generateS3FactLoginMetricsClient(client);
        revenue.setDailyCoreSpreadRevenueOz(getRandomRoundedDouble(0.0, 999_999_999.99));
        revenue.setDailyCoreSpreadRevenuePe(getRandomRoundedDouble(0.0, 999_999_999.99));
        revenue.setDailyTakerSpreadRevenueOz(getRandomRoundedDouble(0.0, 999_999_999.99));
        revenue.setDailyTakerSpreadRevenuePe(getRandomRoundedDouble(0.0, 999_999_999.99));
        revenue.setDailyLpSpreadRevenueOz(getRandomRoundedDouble(0.0, 999_999_999.99));
        revenue.setDailyLpSpreadRevenuePe(getRandomRoundedDouble(0.0, 999_999_999.99));
        revenue.setDailyVbSpreadRevenueOz(getRandomRoundedDouble(0.0, 999_999_999.99));
        revenue.setDailyVbSpreadRevenuePe(getRandomRoundedDouble(0.0, 999_999_999.99));
        revenue.setDailyAppliedMinSpreadRevenueOz(getRandomRoundedDouble(0.0, 999_999_999.99));
        revenue.setDailyAppliedMinSpreadRevenuePe(getRandomRoundedDouble(0.0, 999_999_999.99));
        revenue.setDailyAppliedMaxSpreadRevenueOz(getRandomRoundedDouble(0.0, 999_999_999.99));
        revenue.setDailyAppliedMaxSpreadRevenuePe(getRandomRoundedDouble(0.0, 999_999_999.99));
        revenue.setDailyMakerSpreadRevenueOz(getRandomRoundedDouble(0.0, 999_999_999.99));

        revenue.setDailyClientSlippageRevenueOz(getRandomRoundedDouble(0.0, 999_999_999.99));
        revenue.setDailyClientSlippageRevenuePe(getRandomRoundedDouble(0.0, 999_999_999.99));
        revenue.setDailyCommissionRevenue(getRandomRoundedDouble(0.0, 999_999_999.99));
        revenue.setDailySwapsRevenue(getRandomRoundedDouble(0.0, 999_999_999.99));
        revenue.setIbCommission(getRandomRoundedDouble(0.0, 999_999_999.99));
        revenue.setSalesCommission(getRandomRoundedDouble(0.0, 999_999_999.99));

        insertObjectToDb(S3_FACT_LOGIN_METRICS_TABLE_NAME, revenue);

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab(client.getUcid());
        generalTab.checkSummaryPanelValue(
                "Company RFR", decimalFormat.format(generalTab.calculateRevenue(revenue))
        );
    }
}
