package tests.vindex_backoffice_ui_tests.investigationTool;

import static business_objects.db.clickhouse.account_ib_relation_snapshot.AccountIbRelationSnapshotFactory.generateAccountIbRelationSnapshotObjectByAccounts;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.*;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.crm_tb_wallet_trade_order.CrmTbWalletTradeFactory.generateCrmTbWalletTradeObjectByClient;
import static business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.mt_balance_orders_table.MtBalanceOrdersObjectFactory.generateMtBalanceOrder;
import static business_objects.db.clickhouse.mt_balance_orders_table.MtBalanceOrdersObjectFactory.generateMtBalanceOrderByAccount;
import static business_objects.db.clickhouse.mt_mt4_trades.MtMt4TradesObjectFactory.generateMt4TradesObject;
import static business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoercedAccountProfitComment;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateTradeByClient;
import static business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObjectFactory.generateMtMt5PositionsObject;
import static business_objects.db.clickhouse.mt_tb_credits.MtTbCreditsObjectFactory.generateCreditsByClient;
import static business_objects.db.clickhouse.s3_fact_cpa_commissions.S3FactCpaCommissionsFactory.generates3FactCpaCommissionsObject;
import static business_objects.db.clickhouse.s3_fact_login_metrics.S3FactLoginMetricsFactory.generateS3FactLoginMetricsClient;
import static business_objects.db.clickhouse.s3_fact_login_metrics.S3FactLoginMetricsFactory.generateS3FactLoginMetricsClientZero;
import static helpers.data.ClientFactory.getRandomBybitClient;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.database.DbHelper.*;
import static utils.Constants.*;
import static utils.Utils.*;
import static utils.Utils.getRandomIntPositive;

import business_objects.db.clickhouse.account_ib_relation_snapshot.AccountIbRelationSnapshotObject;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntity;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.db.clickhouse.mt_balance_orders_table.MtBalanceOrdersObject;
import helpers.data.ClientHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
class PaymentsSummaryPanelTest extends TestBaseWeb {
    private static final ClientHelper client = getRandomVantageClient();
    private static final ClientHelper clientBybit = getRandomBybitClient();
    private static final ClientHelper clientRebatesParent = getRandomVantageClient();
    private static final ClientHelper clientRebatesChild = getRandomVantageClient();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    private static final CrmTbUserObject crmTbUserBybit = generateUserByClient(clientBybit);
    private static final CrmTbUserObject crmTbUserRebatesParent = generateUserByClient(clientRebatesParent);
    private static final CrmTbUserObject crmTbUserRebatesChild = generateUserByClient(clientRebatesChild);
    private static final DecimalFormat decimalFormat =
            new DecimalFormat("#,##0.##", new DecimalFormatSymbols(Locale.US));
    private static CrmTbAccountObject account;
    private static CrmTbAccountObject accountBybit;
    private static CrmTbAccountObject accountRebates;
    private static CrmTbAccountObject accountRebatesParent;
    private static CrmTbAccountObject accountRebatesChild;
    private static MtAccountObject mtAccount;
    private static MtAccountObject mtAccountBybit;
    private static MtAccountObject mtAccountRebates;
    private static MtAccountObject mtAccountRebatesParent;
    private static MtAccountObject mtAccountRebatesChild;
    private static final String CLIENT_UCID_WHERE = "ucid ='" + client.getUcid() + "'";
    private static final String CLIENT_BYBIT_UCID_WHERE = "ucid ='" + clientBybit.getUcid() + "'";
    private static final String ACCOUNT_GROUP_REBATES = "rebate group";

    @BeforeAll
    static void setup() {
        insertObjectsToDb(
                CRM_USER_TABLE_NAME, List.of(crmTbUser, crmTbUserBybit, crmTbUserRebatesParent, crmTbUserRebatesChild));
        account = generateCrmTbAccountDataForUi(client);
        accountBybit = generateCrmTbAccountDataBybit(clientBybit);
        accountBybit.serverName = "123bybit" + accountBybit.serverName;
        accountRebates = generateCrmTbAccountDataForUi(client);
        accountRebates.account = getRandomIntPositive();
        accountRebates.accountGroup = ACCOUNT_GROUP_REBATES;
        accountRebates.isRebateAccount = 1;
        accountRebatesParent = generateCrmTbAccountDataForUi(clientRebatesParent);
        accountRebatesParent.accountGroup = ACCOUNT_GROUP_REBATES;
        accountRebatesParent.isRebateAccount = 1;
        accountRebatesChild = generateCrmTbAccountDataForUi(clientRebatesChild);
        accountRebatesChild.accountGroup = ACCOUNT_GROUP_REBATES;
        accountRebatesChild.isRebateAccount = 1;
        insertCrmAccountsToDb(account, accountBybit, accountRebates, accountRebatesParent, accountRebatesChild);
        mtAccount = generateMtAccountByCrmTbAccount(account);
        mtAccountBybit = generateMtAccountByCrmTbAccount(accountBybit);
        mtAccountBybit.server = "123bybit" + mtAccountBybit.server;
        mtAccountRebates = generateMtAccountByCrmTbAccount(accountRebates);
        mtAccountRebatesParent = generateMtAccountByCrmTbAccount(accountRebatesParent);
        mtAccountRebatesChild = generateMtAccountByCrmTbAccount(accountRebatesChild);
        insertObjectsToDb(
                MT_ACCOUNT_TABLE_NAME,
                List.of(mtAccount, mtAccountBybit, mtAccountRebates, mtAccountRebatesParent, mtAccountRebatesChild));
    }

    @AfterAll
    static void teardown() {
        deleteObjectFromDb(CRM_USER_TABLE_NAME, CLIENT_UCID_WHERE);
        deleteObjectFromDb(CRM_USER_TABLE_NAME, CLIENT_BYBIT_UCID_WHERE);
        deleteObjectFromDb(MT5_DEALS_COERCED_TABLE_NAME, CLIENT_UCID_WHERE);
        deleteObjectFromDb(MT_BALANCE_ORDERS_TABLE_NAME, CLIENT_UCID_WHERE);
        deleteObjectFromDb(S3_FACT_CPA_COMMISSIONS, CLIENT_UCID_WHERE);
        deleteEntryFromDb(S3_FACT_LOGIN_METRICS_TABLE_NAME, CLIENT_UCID_WHERE);
        deleteObjectFromDb(MT4_TRADES_COERCED_TABLE_NAME, CLIENT_UCID_WHERE);
        deleteObjectFromDb(MT5_POSITIONS_TABLE_NAME, CLIENT_UCID_WHERE);
        deleteObjectFromDb(MT4_TRADES_TABLE_NAME, CLIENT_UCID_WHERE);
        deleteObjectFromDb(MT_BALANCE_ORDERS_TABLE_NAME, CLIENT_BYBIT_UCID_WHERE);
        deleteObjectFromDb(CRM_WALLET_TRADE_ORDER_TABLE, CLIENT_UCID_WHERE);
    }

    @Test
    @AllureId("1860")
    @Feature("BMS-2765 Payments summary deposits panel")
    @DisplayName("Payments summary deposits panel")
    void paymentsSummaryDepositsPanelTest() {
        deleteObjectFromDb(MT_BALANCE_ORDERS_TABLE_NAME, CLIENT_UCID_WHERE);
        deleteObjectFromDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, CLIENT_UCID_WHERE);

        // deposits
        Allure.step("Prepare DB data for test user");
        MtBalanceOrdersObject deposit1 = generateMtBalanceOrder(
                client,
                getRandomRoundedDouble(0.01, 99_999.99),
                getRandomRoundedDouble(0.01, 99_999.99),
                getCurrentTimestampDbFormat());
        deposit1.setComment("123 deposit 456");
        MtBalanceOrdersObject deposit2 = generateMtBalanceOrder(
                client,
                getRandomRoundedDouble(0.01, 99_999.99),
                getRandomRoundedDouble(0.01, 99_999.99),
                getCurrentTimestampDbFormat());
        deposit2.setComment("123 deposit 456");
        MtBalanceOrdersObject deposit3 = generateMtBalanceOrder(
                client,
                getRandomRoundedDouble(0.01, 99_999.99),
                getRandomRoundedDouble(0.01, 99_999.99),
                getCurrentTimestampDbFormat());
        deposit3.setComment("123 pam 456");
        MtBalanceOrdersObject deposit4 = generateMtBalanceOrder(
                client,
                getRandomRoundedDouble(0.01, 99_999.99),
                getRandomRoundedDouble(0.01, 99_999.99),
                getCurrentTimestampDbFormat());
        deposit4.setComment("123 transfer in 456");
        MtBalanceOrdersObject deposit5 = generateMtBalanceOrder(
                client,
                getRandomRoundedDouble(0.01, 99_999.99),
                getRandomRoundedDouble(0.01, 99_999.99),
                getCurrentTimestampDbFormat());
        deposit5.setComment("123 transfer in 456");
        MtBalanceOrdersObject deposit6 = generateMtBalanceOrder(
                client,
                getRandomRoundedDouble(-99_999.99, -0.01),
                getRandomRoundedDouble(-99_999.99, -0.01),
                getCurrentTimestampDbFormat());
        deposit6.setComment("123 transfer in 456");
        MtBalanceOrdersObject deposit7 = generateMtBalanceOrder(
                client,
                getRandomRoundedDouble(0.01, 99_999.99),
                getRandomRoundedDouble(0.01, 99_999.99),
                getCurrentTimestampDbFormat());
        deposit7.setComment("non standard");
        MtBalanceOrdersObject deposit8 = generateMtBalanceOrder(
                client,
                getRandomRoundedDouble(0.01, 99_999.99),
                getRandomRoundedDouble(0.01, 99_999.99),
                getCurrentTimestampDbFormat());
        deposit8.setComment("non standard");
        MtBalanceOrdersObject deposit9 = generateMtBalanceOrder(
                client,
                getRandomRoundedDouble(-99_999.99, -0.01),
                getRandomRoundedDouble(-99_999.99, -0.01),
                getCurrentTimestampDbFormat());
        deposit9.setComment("non standard");
        // withdrawals
        MtBalanceOrdersObject withdrawal1 = generateMtBalanceOrder(
                client,
                getRandomRoundedDouble(-99_999.99, -0.01),
                getRandomRoundedDouble(-99_999.99, -0.01),
                getCurrentTimestampDbFormat());
        withdrawal1.setComment("123 withdraw 456");
        insertObjectsToDb(
                MT_BALANCE_ORDERS_TABLE_NAME,
                List.of(
                        deposit1,
                        deposit2,
                        deposit3,
                        deposit4,
                        deposit5,
                        deposit6,
                        deposit7,
                        deposit8,
                        deposit9,
                        withdrawal1));
        CrmTbWithdrawalEntity crmTbWithdrawal1 = generateCrmTbWithdrawalEntityByClient(client);
        crmTbWithdrawal1.setStatusId(16);
        crmTbWithdrawal1.setStatusGroup("Success");
        CrmTbWithdrawalEntity crmTbWithdrawal2 = generateCrmTbWithdrawalEntityByClient(client);
        crmTbWithdrawal2.setStatusId(16);
        crmTbWithdrawal2.setStatusGroup("Success");
        insertObjectsToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, List.of(crmTbWithdrawal1, crmTbWithdrawal2));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigatePaymentsTab(client.getUcid());

        paymentsPage.verifyPaymentSummaryPanelValue("Deposits", "Plain deposits", "2");
        paymentsPage.verifyPaymentSummaryPanelValue(
                "Deposits",
                "Deposit amount",
                decimalFormat.format(roundDouble(deposit1.amountUsd + deposit2.amountUsd, 2)) + " USD");
        paymentsPage.verifyPaymentSummaryPanelValue(
                "Deposits",
                "Net deposits",
                decimalFormat.format(roundDouble(
                                deposit1.amountUsd
                                        + deposit2.amountUsd
                                        - crmTbWithdrawal1.getAmountUsd().doubleValue()
                                        - crmTbWithdrawal2.getAmountUsd().doubleValue(),
                                2))
                        + " USD");
        paymentsPage.verifyPaymentSummaryPanelValue(
                "Deposits",
                "Internal transfers in",
                decimalFormat.format(roundDouble(deposit4.amountUsd + deposit5.amountUsd, 2)) + " USD");
        paymentsPage.verifyPaymentSummaryPanelValue(
                "Deposits",
                "Positive adjustments",
                decimalFormat.format(roundDouble(deposit3.amountUsd + deposit7.amountUsd + deposit8.amountUsd, 2))
                        + " USD");
        paymentsPage.verifyPaymentSummaryPanelValue(
                "Deposits",
                "Total amount in",
                decimalFormat.format(roundDouble(
                                deposit1.amountUsd
                                        + deposit2.amountUsd
                                        + deposit3.amountUsd
                                        + deposit4.amountUsd
                                        + deposit5.amountUsd
                                        + deposit7.amountUsd
                                        + deposit8.amountUsd,
                                2))
                        + " USD");
    }

    @Test
    @AllureId("1864")
    @Feature("BMS-2765 Payments summary withdrawals panel")
    @DisplayName("Payments summary withdrawals panel")
    void paymentsSummaryWithdrawalsPanelTest() {
        deleteObjectFromDb(MT_BALANCE_ORDERS_TABLE_NAME, CLIENT_UCID_WHERE);
        deleteObjectFromDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, CLIENT_UCID_WHERE);
        Allure.step("Prepare DB data for test user");
        MtBalanceOrdersObject withdrawal1 = generateMtBalanceOrder(
                client,
                getRandomRoundedDouble(-99_999.99, -0.01),
                getRandomRoundedDouble(-99_999.99, -0.01),
                getCurrentTimestampDbFormat());
        withdrawal1.setComment("123 withdraw 456");
        MtBalanceOrdersObject withdrawal2 = generateMtBalanceOrder(
                client,
                getRandomRoundedDouble(-99_999.99, -0.01),
                getRandomRoundedDouble(-99_999.99, -0.01),
                getCurrentTimestampDbFormat());
        withdrawal2.setComment("123 withdraw 456");
        MtBalanceOrdersObject withdrawal3 = generateMtBalanceOrder(
                client,
                getRandomRoundedDouble(-99_999.99, -0.01),
                getRandomRoundedDouble(-99_999.99, -0.01),
                getCurrentTimestampDbFormat());
        withdrawal3.setComment("123 pam 456");
        MtBalanceOrdersObject withdrawal4 = generateMtBalanceOrder(
                client,
                getRandomRoundedDouble(-99_999.99, -0.01),
                getRandomRoundedDouble(-99_999.99, -0.01),
                getCurrentTimestampDbFormat());
        withdrawal4.setComment("123 rebat 456");
        MtBalanceOrdersObject withdrawal5 = generateMtBalanceOrder(
                client,
                getRandomRoundedDouble(-99_999.99, -0.01),
                getRandomRoundedDouble(-99_999.99, -0.01),
                getCurrentTimestampDbFormat());
        withdrawal5.setComment("123 back 456");
        MtBalanceOrdersObject withdrawal6 = generateMtBalanceOrder(
                client,
                getRandomRoundedDouble(-99_999.99, -0.01),
                getRandomRoundedDouble(-99_999.99, -0.01),
                getCurrentTimestampDbFormat());
        withdrawal6.setComment("123 transfer out 456");
        MtBalanceOrdersObject withdrawal7 = generateMtBalanceOrder(
                client,
                getRandomRoundedDouble(-99_999.99, -0.01),
                getRandomRoundedDouble(-99_999.99, -0.01),
                getCurrentTimestampDbFormat());
        withdrawal7.setComment("123 transfer out 456");
        MtBalanceOrdersObject withdrawal8 = generateMtBalanceOrder(
                client,
                getRandomRoundedDouble(0.01, 99_999.99),
                getRandomRoundedDouble(0.01, 99_999.99),
                getCurrentTimestampDbFormat());
        withdrawal8.setComment("123 transfer out 456");
        MtBalanceOrdersObject withdrawal9 = generateMtBalanceOrder(
                client,
                getRandomRoundedDouble(-99_999.99, -0.01),
                getRandomRoundedDouble(-99_999.99, -0.01),
                getCurrentTimestampDbFormat());
        withdrawal9.setComment("non standard");
        MtBalanceOrdersObject withdrawal10 = generateMtBalanceOrder(
                client,
                getRandomRoundedDouble(-99_999.99, -0.01),
                getRandomRoundedDouble(-99_999.99, -0.01),
                getCurrentTimestampDbFormat());
        withdrawal10.setComment("non standard");
        MtBalanceOrdersObject withdrawal11 = generateMtBalanceOrder(
                client,
                getRandomRoundedDouble(0.01, 99_999.99),
                getRandomRoundedDouble(0.01, 99_999.99),
                getCurrentTimestampDbFormat());
        withdrawal11.setComment("non standard");
        MtBalanceOrdersObject deposit1 = generateMtBalanceOrder(
                client,
                getRandomRoundedDouble(0.01, 99_999.99),
                getRandomRoundedDouble(0.01, 99_999.99),
                getCurrentTimestampDbFormat());
        deposit1.setComment("123 deposit 456");
        CrmTbWithdrawalEntity crmTbWithdrawal1 = generateCrmTbWithdrawalEntityByClient(client);
        crmTbWithdrawal1.setStatusId(16);
        crmTbWithdrawal1.setStatusGroup("Success");
        CrmTbWithdrawalEntity crmTbWithdrawal2 = generateCrmTbWithdrawalEntityByClient(client);
        crmTbWithdrawal2.setStatusId(16);
        crmTbWithdrawal2.setStatusGroup("Success");
        insertObjectsToDb(
                MT_BALANCE_ORDERS_TABLE_NAME,
                List.of(
                        withdrawal1,
                        withdrawal2,
                        withdrawal3,
                        withdrawal4,
                        withdrawal5,
                        withdrawal6,
                        withdrawal7,
                        withdrawal8,
                        withdrawal9,
                        withdrawal10,
                        withdrawal11,
                        deposit1));
        insertObjectsToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, List.of(crmTbWithdrawal1, crmTbWithdrawal2));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigatePaymentsTab(client.getUcid());

        paymentsPage.verifyPaymentSummaryPanelValue("Withdrawals", "Plain withdrawals", "2");
        paymentsPage.verifyPaymentSummaryPanelValue(
                "Withdrawals",
                "Withdrawal amount",
                decimalFormat.format(Math.abs(roundDouble(
                                crmTbWithdrawal1.getAmountUsd().doubleValue()
                                        + crmTbWithdrawal2.getAmountUsd().doubleValue(),
                                2)))
                        + " USD");
        paymentsPage.verifyPaymentSummaryPanelValue(
                "Withdrawals",
                "Net withdrawals",
                decimalFormat.format(roundDouble(
                                Math.abs(crmTbWithdrawal1.getAmountUsd().doubleValue()
                                                + crmTbWithdrawal2
                                                        .getAmountUsd()
                                                        .doubleValue())
                                        - deposit1.amountUsd,
                                2))
                        + " USD");
        paymentsPage.verifyPaymentSummaryPanelValue(
                "Withdrawals",
                "Internal transfers out",
                decimalFormat.format(Math.abs(roundDouble(withdrawal6.amountUsd + withdrawal7.amountUsd, 2))) + " USD");
        paymentsPage.verifyPaymentSummaryPanelValue(
                "Withdrawals",
                "Negative adjustments",
                decimalFormat.format(Math.abs(roundDouble(
                                withdrawal3.amountUsd
                                        + withdrawal9.amountUsd
                                        + withdrawal10.amountUsd
                                        + withdrawal5.amountUsd,
                                2)))
                        + " USD");
        paymentsPage.verifyPaymentSummaryPanelValue(
                "Withdrawals",
                "Total amount out",
                decimalFormat.format(Math.abs(roundDouble(
                                -1
                                        * (-crmTbWithdrawal1.getAmountUsd().doubleValue()
                                                - crmTbWithdrawal2
                                                        .getAmountUsd()
                                                        .doubleValue()
                                                + withdrawal3.amountUsd
                                                + withdrawal5.amountUsd
                                                + withdrawal6.amountUsd
                                                + withdrawal7.amountUsd
                                                + withdrawal9.amountUsd
                                                + withdrawal10.amountUsd),
                                2)))
                        + " USD");
    }

    @Test
    @AllureId("1868")
    @Feature("BMS-2765 Payments summary trading panel")
    @DisplayName("Payments summary trading panel")
    void paymentsSummaryTradingPanelTest() {
        deleteObjectFromDb(MT5_DEALS_COERCED_TABLE_NAME, CLIENT_UCID_WHERE);
        deleteObjectFromDb(MT4_TRADES_COERCED_TABLE_NAME, CLIENT_UCID_WHERE);
        deleteObjectFromDb(MT5_POSITIONS_TABLE_NAME, CLIENT_UCID_WHERE);
        deleteObjectFromDb(MT4_TRADES_TABLE_NAME, CLIENT_UCID_WHERE);
        deleteEntryFromDb(S3_FACT_LOGIN_METRICS_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));

        Allure.step("Prepare DB data for test user");
        var trade1 = generateTradeByClient(client);
        trade1.setEntry(1);
        trade1.setAction(1);
        var trade2 = generateTradeByClient(client);
        trade2.setEntry(1);
        trade2.setAction(1);
        var trade3 = generateTradeByClient(client);
        trade3.setEntry(0);
        trade3.setAction(1);
        var trade4 = generateTradeByClient(client);
        trade4.setEntry(0);
        trade4.setAction(1);
        insertObjectsToDb(MT5_DEALS_COERCED_TABLE_NAME, List.of(trade1, trade2, trade3, trade4));

        var mt4Trade1c = generateMt4TradesCoercedAccountProfitComment(account, 123.45d, "comment1");
        var mt4Trade2c = generateMt4TradesCoercedAccountProfitComment(account, 10.50d, "comment2");
        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(mt4Trade1c, mt4Trade2c));

        var mt5Position1 = generateMtMt5PositionsObject(client);
        var mt5Position2 = generateMtMt5PositionsObject(client);
        insertObjectsToDb(MT5_POSITIONS_TABLE_NAME, List.of(mt5Position1, mt5Position2));

        var mt4Trade1 = generateMt4TradesObject(client);
        var mt4Trade2 = generateMt4TradesObject(client);
        var mt4Trade3 = generateMt4TradesObject(client);
        insertObjectsToDb(MT4_TRADES_TABLE_NAME, List.of(mt4Trade1, mt4Trade2, mt4Trade3));

        var factLoginMetrics1 = generateS3FactLoginMetricsClient(client);
        factLoginMetrics1.setDate(getYesterdayDate());
        factLoginMetrics1.setDailyNetClosedPnl(878.23);
        factLoginMetrics1.setDailyNetDeposit(142.342);
        factLoginMetrics1.setEquity(getRandomRoundedDouble(0.01, 99_999.99));
        insertObjectsToDb(S3_FACT_LOGIN_METRICS_TABLE_NAME, List.of(factLoginMetrics1));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigatePaymentsTab(client.getUcid());

        paymentsPage.verifyPaymentSummaryPanelValue("Trading", "Closed trades", "2");
        paymentsPage.verifyPaymentSummaryPanelValue("Trading", "Open trades", "5");
        paymentsPage.verifyPaymentSummaryPanelValue(
                "Trading",
                "Lots traded",
                decimalFormat.format(roundDouble(trade3.getVolumeLots() + trade4.getVolumeLots(), 2)) + " lots");
        paymentsPage.verifyPaymentSummaryPanelValue(
                "Trading",
                "Volume",
                decimalFormat.format(roundDouble(trade3.getNotionalValueUsd() + trade4.getNotionalValueUsd(), 2))
                        + " USD");
        paymentsPage.verifyPaymentSummaryPanelValue(
                "Trading",
                "Equity",
                decimalFormat.format(roundDouble(account.equity + accountRebates.equity, 2)) + " USD");
        paymentsPage.verifyPaymentSummaryPanelValue("Trading", "Last activity", "Yesterday");
    }

    @Test
    @AllureId("1879")
    @Feature("BMS-2765 Payments summary PNL panel")
    @DisplayName("Payments summary PNL panel")
    void paymentsSummaryPNLPanelTest() {
        deleteObjectFromDb(MT5_DEALS_COERCED_TABLE_NAME, CLIENT_UCID_WHERE);
        deleteObjectFromDb(MT_BALANCE_ORDERS_TABLE_NAME, CLIENT_UCID_WHERE);
        deleteObjectFromDb(S3_FACT_CPA_COMMISSIONS, CLIENT_UCID_WHERE);
        deleteEntryFromDb(S3_FACT_LOGIN_METRICS_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));

        Allure.step("Prepare DB data for test user");
        var trade1 = generateTradeByClient(client);
        trade1.setEntry(1);
        trade1.setAction(1);
        var trade2 = generateTradeByClient(client);
        trade2.setEntry(1);
        trade2.setAction(1);
        var trade3 = generateTradeByClient(client);
        trade3.setEntry(0);
        trade3.setProfitUsd(0d);
        trade3.setStorageUsd(0d);
        trade3.setCommissionUsd(0d);
        trade3.setAction(1);
        var trade4 = generateTradeByClient(client);
        trade4.setEntry(0);
        trade4.setProfitUsd(0d);
        trade4.setStorageUsd(0d);
        trade4.setCommissionUsd(0d);
        trade4.setAction(1);
        insertObjectsToDb(MT5_DEALS_COERCED_TABLE_NAME, List.of(trade1, trade2, trade3, trade4));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigatePaymentsTab(client.getUcid());

        paymentsPage.verifyPaymentSummaryPanelValue(
                "PNL",
                "Profit",
                decimalFormat.format(roundDouble(trade1.getProfitUsd() + trade2.getProfitUsd(), 2)) + " USD");
        paymentsPage.verifyPaymentSummaryPanelValue(
                "PNL",
                "Realized PNL",
                decimalFormat.format(roundDouble(
                                trade1.getProfitUsd()
                                        + trade2.getProfitUsd()
                                        + trade1.getStorageUsd()
                                        + trade2.getStorage()
                                        + trade1.getCommissionUsd()
                                        + trade2.getCommissionUsd(),
                                2))
                        + " USD");
        paymentsPage.verifyPaymentSummaryPanelValue(
                "PNL",
                "Floating PNL",
                decimalFormat.format(roundDouble(mtAccount.floatingPnlUsd + mtAccountRebates.floatingPnlUsd, 2))
                        + " USD");
        paymentsPage.verifyPaymentSummaryPanelValue(
                "PNL",
                "Trading PNL",
                decimalFormat.format(roundDouble(
                                mtAccount.floatingPnlUsd
                                        + mtAccountRebates.floatingPnlUsd
                                        + trade1.getProfitUsd()
                                        + trade2.getProfitUsd()
                                        + trade1.getStorageUsd()
                                        + trade2.getStorage()
                                        + trade1.getCommissionUsd()
                                        + trade2.getCommissionUsd(),
                                2))
                        + " USD");
        // no positive or negative adjustments inserted
        paymentsPage.verifyPaymentSummaryPanelValue(
                "PNL",
                "Gross PNL",
                decimalFormat.format(roundDouble(
                                mtAccount.floatingPnlUsd
                                        + mtAccountRebates.floatingPnlUsd
                                        + trade1.getProfitUsd()
                                        + trade2.getProfitUsd()
                                        + trade1.getStorageUsd()
                                        + trade2.getStorage()
                                        + trade1.getCommissionUsd()
                                        + trade2.getCommissionUsd(),
                                2))
                        + " USD");
        // no ib rebates, sales commissions, cpa rebates inserted
        paymentsPage.verifyPaymentSummaryPanelValue(
                "PNL",
                "Net client PNL",
                decimalFormat.format(roundDouble(
                                mtAccount.floatingPnlUsd
                                        + mtAccountRebates.floatingPnlUsd
                                        + trade1.getProfitUsd()
                                        + trade2.getProfitUsd()
                                        + trade1.getStorageUsd()
                                        + trade2.getStorage()
                                        + trade1.getCommissionUsd()
                                        + trade2.getCommissionUsd(),
                                2))
                        + " USD");
    }

    @Test
    @AllureId("1880")
    @Feature("BMS-2765 Payments summary V-Wallet panel")
    @DisplayName("Payments summary V-Wallet panel")
    void paymentsSummaryVWalletPanelTest() {
        var wallerOrder1 = generateCrmTbWalletTradeObjectByClient(client);
        var wallerOrder2 = generateCrmTbWalletTradeObjectByClient(client);
        wallerOrder2.setTradeStyle(2);
        wallerOrder2.setFromAmount(BigDecimal.valueOf(65.52));
        wallerOrder2.setFromAmountUsd(BigDecimal.valueOf(65.52));
        wallerOrder2.setToAmount(BigDecimal.valueOf(65.52));
        wallerOrder2.setToAmountUsd(BigDecimal.valueOf(65.52));
        var wallerOrder3 = generateCrmTbWalletTradeObjectByClient(client);
        wallerOrder3.setTradeStyle(2);
        wallerOrder3.setTradeDirection(0);
        wallerOrder3.setFromAmount(BigDecimal.valueOf(1000));
        wallerOrder3.setFromAmountUsd(BigDecimal.valueOf(1000));
        wallerOrder3.setToAmount(BigDecimal.valueOf(1000));
        wallerOrder3.setToAmountUsd(BigDecimal.valueOf(1000));
        var wallerOrder4 = generateCrmTbWalletTradeObjectByClient(client);
        wallerOrder4.setTradeStyle(3);
        wallerOrder4.setFromAmount(BigDecimal.valueOf(424));
        wallerOrder4.setFromAmountUsd(BigDecimal.valueOf(424));
        wallerOrder4.setToAmount(BigDecimal.valueOf(424));
        wallerOrder4.setToAmountUsd(BigDecimal.valueOf(424));
        insertObjectsToDb(
                CRM_WALLET_TRADE_ORDER_TABLE, List.of(wallerOrder1, wallerOrder2, wallerOrder3, wallerOrder4));
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigatePaymentsTab(client.getUcid());

        paymentsPage.verifyPaymentSummaryPanelValue(
                "V-wallet",
                "Balance",
                decimalFormat.format(roundDouble(
                                wallerOrder3.getToAmount().doubleValue()
                                        - wallerOrder1.getFromAmount().doubleValue()
                                        - wallerOrder2.getFromAmount().doubleValue()
                                        - wallerOrder4.getFromAmount().doubleValue(),
                                2))
                        + " USD");
        paymentsPage.verifyPaymentSummaryPanelValue(
                "V-wallet",
                "MT to V-wallet",
                decimalFormat.format(roundDouble(wallerOrder3.getToAmountUsd().doubleValue(), 2)) + " USD");
        paymentsPage.verifyPaymentSummaryPanelValue(
                "V-wallet",
                "V-wallet to MT",
                decimalFormat.format(roundDouble(wallerOrder2.getFromAmountUsd().doubleValue(), 2)) + " USD");
        paymentsPage.verifyPaymentSummaryPanelValue(
                "V-wallet",
                "Client to V-wallet",
                decimalFormat.format(roundDouble(wallerOrder1.getToAmountUsd().doubleValue(), 2)) + " USD");
        paymentsPage.verifyPaymentSummaryPanelValue(
                "V-wallet",
                "V-wallet to Client",
                decimalFormat.format(roundDouble(wallerOrder4.getFromAmountUsd().doubleValue(), 2)) + " USD");
    }

    @Test
    @AllureId("1881")
    @Feature("BMS-2765 Payments summary Cost efficiency panel")
    @DisplayName("Payments summary Cost efficiency panel")
    void paymentsSummaryCostEfficiencyPanelTest() {
        deleteEntryFromDb(S3_FACT_LOGIN_METRICS_TABLE_NAME, CLIENT_UCID_WHERE);
        deleteEntryFromDb(MT5_DEALS_COERCED_TABLE_NAME, CLIENT_UCID_WHERE);
        var historyMetrics = generateS3FactLoginMetricsClientZero(client);
        historyMetrics.setDailyCoreSpreadRevenuePe(100.11);
        historyMetrics.setDailyCoreSpreadRevenueOz(200.22);
        historyMetrics.setIbCommission(33.33);
        insertObjectToDb(S3_FACT_LOGIN_METRICS_TABLE_NAME, historyMetrics);
        var mt5DealsCoerced = generateTradeByClient(client);
        mt5DealsCoerced.setStorageUsd(123.45);
        mt5DealsCoerced.setCommissionUsd(2345.6);
        insertObjectToDb(MT5_DEALS_COERCED_TABLE_NAME, mt5DealsCoerced);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigatePaymentsTab(client.getUcid());

        paymentsPage.verifyPaymentSummaryPanelValue(
                "Cost efficiency",
                "Spread revenue",
                decimalFormat.format(roundDouble(
                                historyMetrics.getDailyCoreSpreadRevenuePe()
                                        + historyMetrics.getDailyCoreSpreadRevenueOz(),
                                2))
                        + " USD");
        paymentsPage.verifyPaymentSummaryPanelValue(
                "Cost efficiency",
                "Company RFR",
                decimalFormat.format(roundDouble(
                                historyMetrics.getDailyCoreSpreadRevenuePe()
                                        + historyMetrics.getDailyCoreSpreadRevenueOz()
                                        - historyMetrics.getIbCommission(),
                                2))
                        + " USD");
        paymentsPage.verifyExistingPaymentSummaryPanelValue("Cost efficiency", "Payments cost");
        paymentsPage.verifyPaymentSummaryPanelValue(
                "Cost efficiency",
                "Swaps",
                decimalFormat.format(roundDouble(mt5DealsCoerced.getStorageUsd(), 2)) + " USD");
        paymentsPage.verifyPaymentSummaryPanelValue(
                "Cost efficiency",
                "Commissions",
                decimalFormat.format(roundDouble(mt5DealsCoerced.getCommissionUsd(), 2)) + " USD");
    }

    @Test
    @AllureId("1882")
    @Feature("BMS-2765 Payments summary Rewards panel")
    @DisplayName("Payments summary Rewards panel")
    void paymentsSummaryRewardsPanelTest() {
        deleteEntryFromDb(MT_CREDITS_TABLE_NAME, CLIENT_UCID_WHERE);
        deleteEntryFromDb(S3_FACT_LOGIN_METRICS_TABLE_NAME, CLIENT_UCID_WHERE);
        deleteObjectFromDb(S3_FACT_CPA_COMMISSIONS, CLIENT_UCID_WHERE);
        var credit = generateCreditsByClient(client);
        credit.amountUsd = 644.34;
        insertObjectToDb(MT_CREDITS_TABLE_NAME, credit);
        var historyMetrics = generateS3FactLoginMetricsClientZero(client);
        historyMetrics.setIbCommission(876.12);
        historyMetrics.setSalesCommission(789.53);
        insertObjectToDb(S3_FACT_LOGIN_METRICS_TABLE_NAME, historyMetrics);
        var cpaCommission = generates3FactCpaCommissionsObject(client);
        cpaCommission.setCommission(111.22);
        insertObjectToDb(S3_FACT_CPA_COMMISSIONS, cpaCommission);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigatePaymentsTab(client.getUcid());

        paymentsPage.verifyPaymentSummaryPanelValue(
                "Rewards", "Credits in", decimalFormat.format(roundDouble(credit.amountUsd, 2)) + " USD");
        paymentsPage.verifyPaymentSummaryPanelValue(
                "Rewards",
                "IB rebates",
                decimalFormat.format(roundDouble(historyMetrics.getIbCommission(), 2)) + " USD");
        paymentsPage.verifyPaymentSummaryPanelValue(
                "Rewards",
                "Sales commissions",
                decimalFormat.format(roundDouble(historyMetrics.getSalesCommission(), 2)) + " USD");
        paymentsPage.verifyPaymentSummaryPanelValue(
                "Rewards",
                "CPA commissions",
                decimalFormat.format(roundDouble(cpaCommission.getCommission(), 2)) + " USD");
    }

    @Test
    @AllureId("1883")
    @Feature("BMS-2765 Payments summary IB rebates panel")
    @DisplayName("Payments summary IB rebates panel")
    void paymentsSummaryIBRebatesPanelTest() {
        deleteEntryFromDb(MT_BALANCE_ORDERS_TABLE_NAME, CLIENT_UCID_WHERE);
        MtBalanceOrdersObject balanceOrderEarnedAsIb = generateMtBalanceOrderByAccount(accountRebates);
        balanceOrderEarnedAsIb.setComment("rebates earned");
        balanceOrderEarnedAsIb.setAmountUsd(2345.67);
        MtBalanceOrdersObject balanceOrderInternalTransferIn = generateMtBalanceOrderByAccount(account);
        balanceOrderInternalTransferIn.setComment(String.format("transfer from %s", accountRebates.account));
        balanceOrderInternalTransferIn.setAmountUsd(3456.78);
        MtBalanceOrdersObject balanceOrderInternalTransferOut = generateMtBalanceOrderByAccount(accountRebates);
        balanceOrderInternalTransferOut.setComment(String.format("transfer to %s", account.account));
        balanceOrderInternalTransferOut.setAmountUsd(-4567.89);
        MtBalanceOrdersObject balanceOrderReceivedFromIb = generateMtBalanceOrderByAccount(accountRebates);
        balanceOrderReceivedFromIb.setComment(String.format("transfer from %s", accountRebatesParent.account));
        balanceOrderReceivedFromIb.setAmountUsd(678.9);
        AccountIbRelationSnapshotObject ibRelationReceivedFromIB =
                generateAccountIbRelationSnapshotObjectByAccounts(accountRebates, accountRebatesParent);
        MtBalanceOrdersObject balanceOrderSentToClients = generateMtBalanceOrderByAccount(accountRebates);
        balanceOrderSentToClients.setComment(String.format("transfer to %s", accountRebatesChild.account));
        balanceOrderSentToClients.setAmountUsd(-789.12);
        AccountIbRelationSnapshotObject ibRelationSentToClients =
                generateAccountIbRelationSnapshotObjectByAccounts(accountRebatesChild, accountRebates);
        insertObjectsToDb(
                MT_BALANCE_ORDERS_TABLE_NAME,
                List.of(
                        balanceOrderEarnedAsIb,
                        balanceOrderInternalTransferIn,
                        balanceOrderInternalTransferOut,
                        balanceOrderReceivedFromIb,
                        balanceOrderSentToClients));
        insertObjectsToDb(
                ACCOUNT_IB_RELATION_SNAPSHOT_TABLE_NAME, List.of(ibRelationReceivedFromIB, ibRelationSentToClients));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigatePaymentsTab(client.getUcid());

        paymentsPage.verifyPaymentSummaryPanelValue(
                "IB rebates",
                "Earned as IB",
                decimalFormat.format(roundDouble(balanceOrderEarnedAsIb.getAmountUsd(), 2)) + " USD");
        paymentsPage.verifyPaymentSummaryPanelValue(
                "IB rebates",
                "Internal transfer in",
                decimalFormat.format(roundDouble(balanceOrderInternalTransferIn.getAmountUsd(), 2)) + " USD");
        paymentsPage.verifyPaymentSummaryPanelValue(
                "IB rebates",
                "Internal transfer out",
                decimalFormat.format(roundDouble(-balanceOrderInternalTransferOut.getAmountUsd(), 2)) + " USD");
        paymentsPage.verifyPaymentSummaryPanelValue(
                "IB rebates",
                "Received from IB",
                decimalFormat.format(roundDouble(balanceOrderReceivedFromIb.getAmountUsd(), 2)) + " USD");
        paymentsPage.verifyPaymentSummaryPanelValue(
                "IB rebates",
                "Sent to clients",
                decimalFormat.format(roundDouble(-balanceOrderSentToClients.getAmountUsd(), 2)) + " USD");
    }
}
