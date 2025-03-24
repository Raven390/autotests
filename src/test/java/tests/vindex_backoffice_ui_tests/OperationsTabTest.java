package tests.vindex_backoffice_ui_tests;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalObject;
import business_objects.db.clickhouse.mtAccount.MtAccountObject;
import business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObject;
import business_objects.db.clickhouse.payments_total.PaymentsTotalObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import helpers.data.ClientHelper;
import helpers.data.enums.Brand;
import helpers.data.enums.DateTimeFormat;
import helpers.data.enums.Regulator;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;
import utils.Utils;

import java.sql.SQLException;
import java.util.List;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateAdditionalStaticCrmTbAccountActive;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateStaticCrmTbAccountActive;
import static business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositObjectFactory.generateDepositByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalObjectFactory.generateWithdrawalByClient;
import static business_objects.db.clickhouse.mtAccount.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoerced;
import static helpers.database.DbHelper.insertObjectToDb;
import static helpers.database.DbHelper.insertObjectsToDb;
import static helpers.database.OperationsHelper.cleanUserCashflowDb;
import static helpers.database.OperationsHelper.cleanUserFinancialTransactionDbUcid;
import static utils.Constants.*;
import static utils.Utils.*;

public class OperationsTabTest extends TestBaseWeb {

    private static ClientHelper client = new ClientHelper(313_102, "e5880ca5-8578-4a1e-969d-7a64716ca41f", Brand.INFINOX, Regulator.FCA, 313_102_001, 313_102_002, 42);
    private static CrmTbUserObject crmTbUser = generateStaticUserByClient(client);
    private static CrmTbAccountObject account1 = generateStaticCrmTbAccountActive(client);
    private static CrmTbAccountObject account2 = generateAdditionalStaticCrmTbAccountActive(client);
    private static MtAccountObject mtAccount1 = generateMtAccountByCrmTbAccount(account1);
    private static MtAccountObject mtAccount2 = generateMtAccountByCrmTbAccount(account2);

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        crmTbUser.firstName = "Operator";
        crmTbUser.lastName = "Trademan";
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertObjectsToDb(CRM_ACCOUNT_TABLE_NAME, List.of(account1, account2));
        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccount1, mtAccount2));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("562")
    @DisplayName("Operations tab. Cashflow chart show empty state when it not have data DB")
    public void cashflowEmptyStateTest() throws Exception {
        cleanUserCashflowDb(client.getUcid());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigateOperationsTab(client.getUcid());
        paymentsPage.checkCashflowEmptyStateIsVisible();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("573")
    @DisplayName("Operations tab. Cashflow chart show empty one side on deposit when it not have data DB")
    public void cashflowOnlyOneWithdrawalFilledTest() throws Exception {

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        cleanUserCashflowDb(client.getUcid());
        CrmTbWithdrawalObject withdrawal = generateWithdrawalByClient(client);
        withdrawal.statusId = 7;
        Allure.step("add record about withdrawal");
        insertObjectToDb(CRM_WITHDRAWAL_TABLE_NAME, withdrawal);
        paymentsPage.navigateOperationsTab(client.getUcid());
        paymentsPage.checkCashflowEmptyStateDepositIsVisible();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("574")
    @DisplayName("Operations tab. Cashflow chart show empty one side on deposit when it not have data DB")
    public void CashflowOnlyDepositSideFilledTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        cleanUserCashflowDb(client.getUcid());
        CrmTbDepositObject deposit = generateDepositByClient(client);
        deposit.statusId = 5;
        Allure.step("add record about deposit");
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, deposit);
        paymentsPage.navigateOperationsTab(client.getUcid());
        paymentsPage.checkCashflowEmptyStateWithdrawalIsVisible();
    }

//    @Test
//    @Tag(TEAM_BACKOFFICE)
//    @Tag(LAYER_WEB)
//    @AllureId("588")
//    @DisplayName("Operations tab. Cashflow chart show data from DB")
//    public void cashflowTotalValueInTipTest() throws Exception {
//        investigationPage.navigateEnterPage();
//        keycloackPage.loginAsAutotestUser();
//        cleanUserCashflowDb(client.getUcid());
//        dpAndWdByChannelObject transaction = new dpAndWdByChannelObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Deposit", "TestPaymentService", "Payment Services", 12.0, 22, getCurrentTimestampDbFormat());
//        Allure.step("add record about deposit");
//        insertObjectToDb(DP_AND_WD_BY_CHANNEL_TABLE_NAME, transaction);
//        paymentsPage.navigateOperationsTab(client.getUcid());
//        paymentsPage.hoverOverCashflowLineByTypeDeposit(transaction.channelCategory);
//        paymentsPage.checkTotalCountByPaymentSystem(transaction.paymentChannel, String.valueOf(Math.round(transaction.totalAmountUsd)));
//        dpAndWdByChannelObject transaction2 = new dpAndWdByChannelObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Deposit", "TestPaymentService", "Payment Services", 12.0, 11, getCurrentTimestampDbFormat());
//        Allure.step("add record about deposit");
//        insertObjectToDb(DP_AND_WD_BY_CHANNEL_TABLE_NAME, transaction2);
//        page.reload();
//        paymentsPage.hoverOverCashflowLineByTypeDeposit(transaction2.channelCategory);
//        paymentsPage.checkTotalCountByPaymentSystem(transaction2.paymentChannel, String.valueOf(Math.round(transaction2.totalAmountUsd)));
//        dpAndWdByChannelObject transaction3 = new dpAndWdByChannelObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Deposit", "TestPaymentServiceSecond", "Payment Services", 12.0, 6, getCurrentTimestampDbFormat());
//        Allure.step("add record about deposit");
//        insertObjectToDb(DP_AND_WD_BY_CHANNEL_TABLE_NAME, transaction3);
//        page.reload();
//        paymentsPage.hoverOverCashflowLineByTypeDeposit(transaction3.channelCategory);
//        paymentsPage.checkTotalCountByPaymentSystem(transaction3.paymentChannel, String.valueOf(Math.round(transaction3.totalAmountUsd)));
//        dpAndWdByChannelObject transaction4 = new dpAndWdByChannelObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Deposit", "TestCards", "Cards", 12.0, 6, getCurrentTimestampDbFormat());
//        Allure.step("add record about deposit");
//        insertObjectToDb(DP_AND_WD_BY_CHANNEL_TABLE_NAME, transaction4);
//        page.reload();
//        paymentsPage.hoverOverCashflowLineByTypeDeposit(transaction4.channelCategory);
//        paymentsPage.checkTotalCountByPaymentSystem(transaction4.paymentChannel, String.valueOf(Math.round(transaction4.totalAmountUsd)));
//        dpAndWdByChannelObject transaction5 = new dpAndWdByChannelObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Deposit", "TestBank Transfers", "Bank Transfers", 12.1, 6, getCurrentTimestampDbFormat());
//        dpAndWdByChannelObject transaction6 = new dpAndWdByChannelObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Deposit", "TestCrypto", "Crypto", 12.2, 6, getCurrentTimestampDbFormat());
//        dpAndWdByChannelObject transaction7 = new dpAndWdByChannelObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Deposit", "TestP2P", "P2P", 12.3, 6, getCurrentTimestampDbFormat());
//        dpAndWdByChannelObject transaction8 = new dpAndWdByChannelObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Deposit", "Other", "Other", 12.4, 6, getCurrentTimestampDbFormat());
//        dpAndWdByChannelObject transaction9 = new dpAndWdByChannelObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Withdrawal", "TestBank Transfers", "Bank Transfers", 12.0, 7, getCurrentTimestampDbFormat());
//        dpAndWdByChannelObject transaction10 = new dpAndWdByChannelObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Withdrawal", "TestCrypto", "Crypto", 12.5, 7, getCurrentTimestampDbFormat());
//        dpAndWdByChannelObject transaction11 = new dpAndWdByChannelObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Withdrawal", "TestP2P", "P2P", 12.6, 7, getCurrentTimestampDbFormat());
//        dpAndWdByChannelObject transaction12 = new dpAndWdByChannelObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Withdrawal", "TestOther", "Other", 12.7, 7, getCurrentTimestampDbFormat());
//        dpAndWdByChannelObject transaction13 = new dpAndWdByChannelObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Withdrawal", "TestPayment Services", "Payment Services", 12.8, 7, getCurrentTimestampDbFormat());
//        dpAndWdByChannelObject transaction14 = new dpAndWdByChannelObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Withdrawal", "TestCards", "Cards", 12.9, 7, getCurrentTimestampDbFormat());
//        insertObjectToDb(DP_AND_WD_BY_CHANNEL_TABLE_NAME, transaction5);
//        insertObjectToDb(DP_AND_WD_BY_CHANNEL_TABLE_NAME, transaction6);
//        insertObjectToDb(DP_AND_WD_BY_CHANNEL_TABLE_NAME, transaction7);
//        insertObjectToDb(DP_AND_WD_BY_CHANNEL_TABLE_NAME, transaction8);
//        insertObjectToDb(DP_AND_WD_BY_CHANNEL_TABLE_NAME, transaction9);
//        insertObjectToDb(DP_AND_WD_BY_CHANNEL_TABLE_NAME, transaction10);
//        insertObjectToDb(DP_AND_WD_BY_CHANNEL_TABLE_NAME, transaction11);
//        insertObjectToDb(DP_AND_WD_BY_CHANNEL_TABLE_NAME, transaction12);
//        insertObjectToDb(DP_AND_WD_BY_CHANNEL_TABLE_NAME, transaction13);
//        insertObjectToDb(DP_AND_WD_BY_CHANNEL_TABLE_NAME, transaction14);
//        Allure.step("add other transactions ");
//        page.waitForTimeout(10_000);
//        page.reload();
//        page.waitForTimeout(10_000);
//        paymentsPage.hoverOverCashflowLineByTypeDeposit(transaction5.channelCategory);
//        paymentsPage.checkTotalCountByPaymentSystem(transaction5.paymentChannel, String.valueOf(Math.round(transaction5.totalAmountUsd)));
//        paymentsPage.hoverOverCashflowLineByTypeDeposit(transaction6.channelCategory);
//        paymentsPage.checkTotalCountByPaymentSystem(transaction6.paymentChannel, String.valueOf(Math.round(transaction6.totalAmountUsd)));
//        paymentsPage.hoverOverCashflowLineByTypeDeposit(transaction7.channelCategory);
//        paymentsPage.checkTotalCountByPaymentSystem(transaction7.paymentChannel, String.valueOf(Math.round(transaction7.totalAmountUsd)));
//        paymentsPage.hoverOverCashflowLineByTypeDeposit(transaction8.channelCategory);
//        paymentsPage.checkTotalCountByPaymentSystem(transaction8.paymentChannel, String.valueOf(Math.round(transaction8.totalAmountUsd)));
//        paymentsPage.hoverOverCashflowLineByTypeWithdrawal(transaction9.channelCategory);
//        paymentsPage.checkTotalCountByPaymentSystem(transaction9.paymentChannel, String.valueOf(Math.round(transaction9.totalAmountUsd)));
//        paymentsPage.hoverOverCashflowLineByTypeWithdrawal(transaction10.channelCategory);
//        paymentsPage.checkTotalCountByPaymentSystem(transaction10.paymentChannel, String.valueOf(Math.round(transaction10.totalAmountUsd)));
//        paymentsPage.hoverOverCashflowLineByTypeWithdrawal(transaction11.channelCategory);
//        paymentsPage.checkTotalCountByPaymentSystem(transaction11.paymentChannel, String.valueOf(Math.round(transaction11.totalAmountUsd)));
//        paymentsPage.hoverOverCashflowLineByTypeWithdrawal(transaction12.channelCategory);
//        paymentsPage.checkTotalCountByPaymentSystem(transaction12.paymentChannel, String.valueOf(Math.round(transaction12.totalAmountUsd)));
//        paymentsPage.hoverOverCashflowLineByTypeWithdrawal(transaction13.channelCategory);
//        paymentsPage.checkTotalCountByPaymentSystem(transaction13.paymentChannel, String.valueOf(Math.round(transaction13.totalAmountUsd)));
//        paymentsPage.hoverOverCashflowLineByTypeWithdrawal(transaction14.channelCategory);
//        paymentsPage.checkTotalCountByPaymentSystem(transaction14.paymentChannel, String.valueOf(Math.round(transaction14.totalAmountUsd)));
//    }

//    @Test
//    @Tag(TEAM_BACKOFFICE)
//    @Tag(LAYER_WEB)
//    @AllureId("589")
//    @DisplayName("Operations tab. Cashflow header show data from DB")
//    public void cashflowValueInHeaderTest() throws Exception {
//        investigationPage.navigateEnterPage();
//        keycloackPage.loginAsAutotestUser();
//        cleanUserCashflowDb(client.getUcid());
//        dpAndWdByChannelObject transaction = new dpAndWdByChannelObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Deposit", "TestPaymentService", "Payment Services", 12.1, 22, getCurrentTimestampDbFormat());
//        Allure.step("add record about deposit");
//        insertObjectToDb(DP_AND_WD_BY_CHANNEL_TABLE_NAME, transaction);
//        paymentsPage.navigateOperationsTab(client.getUcid());
//        paymentsPage.checkCashflowTopPaymentSystemTypesHeaderDeposit(transaction.channelCategory, String.valueOf(Math.round(transaction.totalAmountUsd)));
//
//        dpAndWdByChannelObject transaction2 = new dpAndWdByChannelObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Deposit", "TestPaymentService", "Payment Services", 24.1, 22, getCurrentTimestampDbFormat());
//        Allure.step("override record about deposit");
//        insertObjectToDb(DP_AND_WD_BY_CHANNEL_TABLE_NAME, transaction2);
//        page.reload();
//        paymentsPage.checkCashflowTopPaymentSystemTypesHeaderDeposit(transaction2.channelCategory, String.valueOf(Math.round(transaction2.totalAmountUsd)));
//
//        dpAndWdByChannelObject transaction3 = new dpAndWdByChannelObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Deposit", "TestPaymentService2", "Payment Services", 14.1, 22, getCurrentTimestampDbFormat());
//        Allure.step("add record about deposit with same cathegory but different name");
//        insertObjectToDb(DP_AND_WD_BY_CHANNEL_TABLE_NAME, transaction3);
//        page.reload();
//        paymentsPage.checkCashflowTopPaymentSystemTypesHeaderDeposit(transaction2.channelCategory, String.valueOf(Math.round(transaction2.totalAmountUsd + transaction3.totalAmountUsd)));
//
//        Allure.step("add to DB transaction with another category  and the bigger amount than previous category ");
//        dpAndWdByChannelObject transaction5 = new dpAndWdByChannelObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Deposit", "TestBank Transfers", "Bank Transfers", 40.1, 6, getCurrentTimestampDbFormat());
//        insertObjectToDb(DP_AND_WD_BY_CHANNEL_TABLE_NAME, transaction5);
//        page.reload();
//        paymentsPage.checkCashflowTopPaymentSystemTypesHeaderDeposit(transaction5.channelCategory, String.valueOf(Math.round(transaction5.totalAmountUsd)));
//
//        Allure.step("add to DB withdrawal transaction");
//        dpAndWdByChannelObject transaction6 = new dpAndWdByChannelObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Withdrawal", "TestBank Transfers", "Bank Transfers", 42.1, 6, getCurrentTimestampDbFormat());
//        insertObjectToDb(DP_AND_WD_BY_CHANNEL_TABLE_NAME, transaction6);
//        page.reload();
//        paymentsPage.checkCashflowTopPaymentSystemTypesHeaderWithdrawal(transaction6.channelCategory, String.valueOf(Math.round(transaction6.totalAmountUsd)));
//
//        Allure.step("add to DB withdrawal transaction with another category  and the bigger amount than previous category ");
//        dpAndWdByChannelObject transaction7 = new dpAndWdByChannelObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Withdrawal", "P2P withdrawal", "P2P", 43.1, 6, getCurrentTimestampDbFormat());
//        insertObjectToDb(DP_AND_WD_BY_CHANNEL_TABLE_NAME, transaction7);
//        page.reload();
//        paymentsPage.checkCashflowTopPaymentSystemTypesHeaderWithdrawal(transaction7.channelCategory, String.valueOf(Math.round(transaction7.totalAmountUsd)));
//    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("574")
    @DisplayName("Operations tab. financialTransaction chart show empty state when it not have data DB")
    public void financialTransactionEmptyStateTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        cleanUserFinancialTransactionDbUcid(client.getUcid());
        paymentsPage.navigateOperationsTab(client.getUcid());
        paymentsPage.checkFinancialTransactionEmptyStateIsVisible();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("597")
    @DisplayName("Operations tab. financialTransaction tabs show data from DB")
    public void financialTransactionTabsShowsDataFromDb() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        Allure.step("add to DB transactios with all presented types");
        MtMt4TradesCoercedObject trade1 = generateMt4TradesCoerced(client);
        trade1.ticketType = "Credit";
        insertObjectToDb(MT4_TRADES_COERCED_TABLE_NAME, trade1);
        cleanUserFinancialTransactionDbUcid(client.getUcid());
        PaymentsTotalObject payments = new PaymentsTotalObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.payments_total", payments);
        paymentsPage.navigateOperationsTab(client.getUcid());
        paymentsPage.checkFinancialTransactionsTilesValues("Net deposits", String.valueOf(Math.round((payments.netDepositsUsd))), String.valueOf(Math.round(payments.totalBalanceopsCount)));
        paymentsPage.checkFinancialTransactionsTilesValues("Total deposits", String.valueOf(Math.round((payments.totalDepositUsd))), String.valueOf(Math.round(payments.totalDepositCount)));
        paymentsPage.checkFinancialTransactionsTilesValues("Total withdrawals", String.valueOf(Math.round((payments.totalWithdrawalUsd))), String.valueOf(Math.round(payments.totalWithdrawalCount)));
        paymentsPage.checkFinancialTransactionsTilesValues("Total Internal transfers", String.valueOf(Math.round((payments.totalTransfersUsd))), String.valueOf(Math.round(payments.totalTransfersCount)));
        paymentsPage.checkFinancialTransactionsTilesValues("Total credit", String.valueOf(Math.round((payments.totalCreditsUsd))), String.valueOf(Math.round(payments.totalCreditsCount)));
    }

//    @Test
//    @Tag(TEAM_BACKOFFICE)
//    @Tag(LAYER_WEB)
//    @AllureId("597")
//    @DisplayName("Operations tab. financialTransaction graph show data from DB")
//    public void financialTransactionGraphShowsDataFromDb() throws Exception {
//        investigationPage.navigateEnterPage();
//        keycloackPage.loginAsAutotestUser();
//        Allure.step("add to DB transaction with another category  and the bigger amount than previous category ");
//        cleanUserFinancialTransactionDbUcid(client.getUcid());
//        PaymentsTotalObject payments = new PaymentsTotalObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
//        insertObjectToDb("vindex_test.payments_total", payments);
//        paymentsPage.navigateOperationsTab(client.getUcid());
//        Allure.step("filter test date");
//        paymentsPage.selectDateFilter("Last 7 days");
//        paymentsPage.hoverOverFinancialTransactionsGraphByDateSingleDay(getCurrentDateMonthDay());
//        paymentsPage.checkFinancialTransactionsRowInTooltip("Deposit", String.valueOf(Math.round(payments.totalDepositUsd)));
//        paymentsPage.checkFinancialTransactionsRowInTooltip("Withdrawal", String.valueOf(Math.round(payments.totalWithdrawalUsd)));
//        paymentsPage.checkFinancialTransactionsRowInTooltip("Credit", String.valueOf(Math.round(payments.totalCreditsUsd)));
//    }
//
//    @Test
//    @Tag(TEAM_BACKOFFICE)
//    @Tag(LAYER_WEB)
//    @AllureId("617")
//    @DisplayName("Operations tab. user can filter data by account")
//    public void operationsTabCanBeFilteredByAccount() throws Exception {
//        investigationPage.navigateEnterPage();
//        keycloackPage.loginAsAutotestUser();
//        Allure.step("add to DB transaction with financial transaction fot the first trade account");
//        cleanUserFinancialTransactionDbUcid(client.getUcid());
//        PaymentsTotalObject payments = new PaymentsTotalObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
//        insertObjectToDb("vindex_test.payments_total", payments);
//        Allure.step("add to DB cashflow transaction for the first trade account");
//        cleanUserCashflowDb(client.getUcid());
//        dpAndWdByChannelObject transactionCashFlow = new dpAndWdByChannelObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Deposit", "TestBank Transfers", "Bank Transfers", 40.1, 6, getCurrentTimestampDbFormat());
//        insertObjectToDb(DP_AND_WD_BY_CHANNEL_TABLE_NAME, transactionCashFlow);
//        page.reload();
//        paymentsPage.navigateOperationsTab(client.getUcid());
//        paymentsPage.checkFinancialTransactionEmptyStateIsNotVisible();
//        paymentsPage.clickOnAccountSelectionWindow();
//        paymentsPage.selectTradingAccount("17170102");
//        paymentsPage.checkFinancialTransactionEmptyStateIsVisible();
//        paymentsPage.clearSelectedTradingAccount();
//        paymentsPage.selectTradingAccount("17170101");
//        paymentsPage.checkFinancialTransactionEmptyStateIsNotVisible();
//        Allure.step("add to DB transaction with financial transaction fot the second trade account");
//        cleanUserFinancialTransactionDbUcid(client.getUcid());
//        PaymentsTotalObject payments2 = new PaymentsTotalObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_102, getCurrentDate(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
//        insertObjectToDb("vindex_test.payments_total", payments2);
//        Allure.step("add to DB cashflow transaction for the second trade account");
//        cleanUserCashflowDb(client.getUcid());
//        dpAndWdByChannelObject transactionCashFlow2 = new dpAndWdByChannelObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_102, getCurrentDate(), "Deposit", "TestBank Transfers", "Bank Transfers", 40.1, 6, getCurrentTimestampDbFormat());
//        insertObjectToDb(DP_AND_WD_BY_CHANNEL_TABLE_NAME, transactionCashFlow2);
//        page.reload();
//        paymentsPage.clearSelectedTradingAccount();
//        paymentsPage.clickOnAccountSelectionWindow();
//        paymentsPage.selectTradingAccount("17170101");
//        paymentsPage.checkFinancialTransactionEmptyStateIsVisible();
//        paymentsPage.clearSelectedTradingAccount();
//        paymentsPage.selectTradingAccount("17170102");
//        paymentsPage.checkFinancialTransactionEmptyStateIsNotVisible();
//    }
//
//    @Test
//    @Tag(TEAM_BACKOFFICE)
//    @Tag(LAYER_WEB)
//    @AllureId("610")
//    @DisplayName("Operations tab. User can filter operations by Dates Custom - one day")
//    public void filterCustomOneDayTest() throws Exception {
//        investigationPage.navigateEnterPage();
//        cleanUserFinancialTransactionDbUcid(client.getUcid());
//        cleanUserCashflowDb(client.getUcid());
//        keycloackPage.loginAsAutotestUser();
//        Allure.step("add to DB transaction to a test date");
//        PaymentsTotalObject payments = new PaymentsTotalObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, "2024-12-11", 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
//        insertObjectToDb("vindex_test.payments_total", payments);
//        Allure.step("add to DB cashflow transaction to a test date ");
//        dpAndWdByChannelObject transactionCashFlow = new dpAndWdByChannelObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, "2024-12-11", "Deposit", "TestBank Transfers", "Bank Transfers", 40.1, 6, getCurrentTimestampDbFormat());
//        insertObjectToDb(DP_AND_WD_BY_CHANNEL_TABLE_NAME, transactionCashFlow);
//        Allure.step("add to DB transaction to a date next to a test date");
//        PaymentsTotalObject payments1 = new PaymentsTotalObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, "2024-12-13", 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
//        insertObjectToDb("vindex_test.payments_total", payments1);
//        Allure.step("add to DB cashflow transaction to a date next to a test date ");
//        dpAndWdByChannelObject transactionCashFlow1 = new dpAndWdByChannelObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, "2024-12-13", "Deposit", "TestBank Transfers", "Bank Transfers", 40.1, 6, getCurrentTimestampDbFormat());
//        insertObjectToDb(DP_AND_WD_BY_CHANNEL_TABLE_NAME, transactionCashFlow1);
//        Allure.step("add to DB transaction to a date previous to a test date");
//        PaymentsTotalObject payments3 = new PaymentsTotalObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, "2024-12-10", 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
//        insertObjectToDb("vindex_test.payments_total", payments3);
//        Allure.step("add to DB cashflow transaction to a date previous to a test date ");
//        dpAndWdByChannelObject transactionCashFlow3 = new dpAndWdByChannelObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, "2024-12-10", "Deposit", "TestBank Transfers", "Bank Transfers", 40.1, 6, getCurrentTimestampDbFormat());
//        insertObjectToDb(DP_AND_WD_BY_CHANNEL_TABLE_NAME, transactionCashFlow3);
//        page.reload();
//        paymentsPage.navigateOperationsTab(client.getUcid());
//        Allure.step("filter test date");
//        paymentsPage.selectDatesInCalendar("2024-12-11", "2024-12-11");
//        Allure.step("check that only data for the test date is displayed");
//        paymentsPage.hoverOverFinancialTransactionsGraphByDateSingleDay("Dec 11");
//        paymentsPage.checkFinancialTransactionsRowInTooltip("Deposit", String.valueOf(Math.round(payments.totalDepositUsd)));
//        paymentsPage.checkFinancialTransactionsRowInTooltip("Withdrawal", String.valueOf(Math.round(payments.totalWithdrawalUsd)));
//        paymentsPage.checkFinancialTransactionsRowInTooltip("Credit", String.valueOf(Math.round(payments.totalCreditsUsd)));
//    }
//
//    @Test
//    @Tag(TEAM_BACKOFFICE)
//    @Tag(LAYER_WEB)
//    @AllureId("615")
//    @DisplayName("Operations tab. User can filter operations by Dates Last 1 year")
//    public void filterLastYearTest() throws Exception {
//        investigationPage.navigateEnterPage();
//        cleanUserFinancialTransactionDbUcid(client.getUcid());
//        cleanUserCashflowDb(client.getUcid());
//        keycloackPage.loginAsAutotestUser();
//        Allure.step("add to DB transaction to a test date");
//        PaymentsTotalObject payments = new PaymentsTotalObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getYesterdayDate(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
//        insertObjectToDb("vindex_test.payments_total", payments);
//        Allure.step("add to DB cashflow transaction to a test date ");
//        dpAndWdByChannelObject transactionCashFlow = new dpAndWdByChannelObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getYesterdayDate(), "Deposit", "TestBank Transfers", "Bank Transfers", 40.1, 6, getCurrentTimestampDbFormat());
//        insertObjectToDb(DP_AND_WD_BY_CHANNEL_TABLE_NAME, transactionCashFlow);
//        Allure.step("add to DB transaction to a date to a date outside of test period");
//        PaymentsTotalObject payments1 = new PaymentsTotalObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getPreviousYearTimestampYearMonthDay(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
//        insertObjectToDb("vindex_test.payments_total", payments1);
//        Allure.step("add to DB cashflow transaction to a date outside of test period");
//        dpAndWdByChannelObject transactionCashFlow1 = new dpAndWdByChannelObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getPreviousYearTimestampYearMonthDay(), "Deposit", "TestBank Transfers", "Bank Transfers", 40.1, 6, getCurrentTimestampDbFormat());
//        insertObjectToDb(DP_AND_WD_BY_CHANNEL_TABLE_NAME, transactionCashFlow1);
//        page.reload();
//        paymentsPage.navigateOperationsTab(client.getUcid());
//        Allure.step("filter test date");
//        paymentsPage.selectDateFilter("Last 1 year");
//        Allure.step("check that only data for the test date is displayed");
//        paymentsPage.hoverOverFinancialTransactionsGraphByDateSingleDay(getPreviousDayMonthDay());
//        paymentsPage.checkFinancialTransactionsRowInTooltip("Deposit", String.valueOf(Math.round(payments.totalDepositUsd)));
//        paymentsPage.checkFinancialTransactionsRowInTooltip("Withdrawal", String.valueOf(Math.round(payments.totalWithdrawalUsd)));
//        paymentsPage.checkFinancialTransactionsRowInTooltip("Credit", String.valueOf(Math.round(payments.totalCreditsUsd)));
//        paymentsPage.checkCashflowTopPaymentSystemTypesHeaderDeposit(transactionCashFlow.channelCategory, String.valueOf(Math.round(transactionCashFlow.totalAmountUsd)));
//    }
//
//    @Test
//    @Tag(TEAM_BACKOFFICE)
//    @Tag(LAYER_WEB)
//    @AllureId("611")
//    @DisplayName("Operations tab. User can filter operations by Dates Last 30 days")
//    public void filterLast30DaysTest() throws Exception {
//        investigationPage.navigateEnterPage();
//        cleanUserFinancialTransactionDbUcid(client.getUcid());
//        cleanUserCashflowDb(client.getUcid());
//        keycloackPage.loginAsAutotestUser();
//        Allure.step("add to DB transaction to a test date");
//        PaymentsTotalObject payments = new PaymentsTotalObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getYesterdayDate(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
//        insertObjectToDb("vindex_test.payments_total", payments);
//        Allure.step("add to DB cashflow transaction to a test date ");
//        dpAndWdByChannelObject transactionCashFlow = new dpAndWdByChannelObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getYesterdayDate(), "Deposit", "TestBank Transfers", "Bank Transfers", 40.1, 6, getCurrentTimestampDbFormat());
//        insertObjectToDb(DP_AND_WD_BY_CHANNEL_TABLE_NAME, transactionCashFlow);
//        Allure.step("add to DB transaction to a date to a date outside of test period");
//        PaymentsTotalObject payments1 = new PaymentsTotalObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getPreviousMonthTimestampYearMonthDay(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
//        insertObjectToDb("vindex_test.payments_total", payments1);
//        Allure.step("add to DB cashflow transaction to a date outside of test period");
//        dpAndWdByChannelObject transactionCashFlow1 = new dpAndWdByChannelObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getPreviousMonthTimestampYearMonthDay(), "Deposit", "TestBank Transfers", "Bank Transfers", 40.1, 6, getCurrentTimestampDbFormat());
//        insertObjectToDb(DP_AND_WD_BY_CHANNEL_TABLE_NAME, transactionCashFlow1);
//        page.reload();
//        paymentsPage.navigateOperationsTab(client.getUcid());
//        Allure.step("filter test date");
//        paymentsPage.selectDateFilter("Last 30 days");
//        Allure.step("check that only data for the test date is displayed");
//        paymentsPage.hoverOverFinancialTransactionsGraphByDateSingleDay(getPreviousDayMonthDay());
//        paymentsPage.checkFinancialTransactionsRowInTooltip("Deposit", String.valueOf(Math.round(payments.totalDepositUsd)));
//        paymentsPage.checkFinancialTransactionsRowInTooltip("Withdrawal", String.valueOf(Math.round(payments.totalWithdrawalUsd)));
//        paymentsPage.checkFinancialTransactionsRowInTooltip("Credit", String.valueOf(Math.round(payments.totalCreditsUsd)));
//        paymentsPage.checkCashflowTopPaymentSystemTypesHeaderDeposit(transactionCashFlow.channelCategory, String.valueOf(Math.round(transactionCashFlow.totalAmountUsd)));
//    }
//
//    @Test
//    @Tag(TEAM_BACKOFFICE)
//    @Tag(LAYER_WEB)
//    @AllureId("897")
//    @DisplayName("Operations tab. User can filter operations by Dates Last 6 months")
//    public void filterLast6MonthsTest() throws Exception {
//        investigationPage.navigateEnterPage();
//        cleanUserFinancialTransactionDbUcid(client.getUcid());
//        cleanUserCashflowDb(client.getUcid());
//        keycloackPage.loginAsAutotestUser();
//        Allure.step("add to DB transaction to a test date");
//        PaymentsTotalObject payments = new PaymentsTotalObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getYesterdayDate(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
//        insertObjectToDb("vindex_test.payments_total", payments);
//        Allure.step("add to DB cashflow transaction to a test date ");
//        dpAndWdByChannelObject transactionCashFlow = new dpAndWdByChannelObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getYesterdayDate(), "Deposit", "TestBank Transfers", "Bank Transfers", 40.1, 6, getCurrentTimestampDbFormat());
//        insertObjectToDb(DP_AND_WD_BY_CHANNEL_TABLE_NAME, transactionCashFlow);
//        Allure.step("add to DB transaction to a date to a date outside of test period");
//        PaymentsTotalObject payments1 = new PaymentsTotalObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getPrevious6MonthTimestampYearMonthDay(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
//        insertObjectToDb("vindex_test.payments_total", payments1);
//        Allure.step("add to DB cashflow transaction to a date outside of test period");
//        dpAndWdByChannelObject transactionCashFlow1 = new dpAndWdByChannelObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getPrevious6MonthTimestampYearMonthDay(), "Deposit", "TestBank Transfers", "Bank Transfers", 40.1, 6, getCurrentTimestampDbFormat());
//        insertObjectToDb(DP_AND_WD_BY_CHANNEL_TABLE_NAME, transactionCashFlow1);
//        page.reload();
//        paymentsPage.navigateOperationsTab(client.getUcid());
//        Allure.step("filter test date");
//        paymentsPage.selectDateFilter("Last 6 months");
//        Allure.step("check that only data for the test date is displayed");
//        paymentsPage.hoverOverFinancialTransactionsGraphByDateMMMyyyy(getPreviousDayMonthDay());
//        paymentsPage.checkFinancialTransactionsRowInTooltip("Deposit", String.valueOf(Math.round(payments.totalDepositUsd)));
//        paymentsPage.checkFinancialTransactionsRowInTooltip("Withdrawal", String.valueOf(Math.round(payments.totalWithdrawalUsd)));
//        paymentsPage.checkFinancialTransactionsRowInTooltip("Credit", String.valueOf(Math.round(payments.totalCreditsUsd)));
//        paymentsPage.checkCashflowTopPaymentSystemTypesHeaderDeposit(transactionCashFlow.channelCategory, String.valueOf(Math.round(transactionCashFlow.totalAmountUsd)));
//    }
//
//    @Test
//    @Tag(TEAM_BACKOFFICE)
//    @Tag(LAYER_WEB)
//    @AllureId("609")
//    @DisplayName("Operations tab. User can filter operations by Dates Last 7 days")
//    public void filterLast7DaysTest() throws Exception {
//        investigationPage.navigateEnterPage();
//        cleanUserFinancialTransactionDbUcid(client.getUcid());
//        cleanUserCashflowDb(client.getUcid());
//        keycloackPage.loginAsAutotestUser();
//        Allure.step("add to DB transaction to a test date");
//        PaymentsTotalObject payments = new PaymentsTotalObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getYesterdayDate(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
//        insertObjectToDb("vindex_test.payments_total", payments);
//        Allure.step("add to DB cashflow transaction to a test date ");
//        dpAndWdByChannelObject transactionCashFlow = new dpAndWdByChannelObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getYesterdayDate(), "Deposit", "TestBank Transfers", "Bank Transfers", 40.1, 6, getCurrentTimestampDbFormat());
//        insertObjectToDb(DP_AND_WD_BY_CHANNEL_TABLE_NAME, transactionCashFlow);
//        Allure.step("add to DB transaction to a date to a date outside of test period");
//        PaymentsTotalObject payments1 = new PaymentsTotalObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, 0, 0, 7, 0, 0), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
//        insertObjectToDb("vindex_test.payments_total", payments1);
//        Allure.step("add to DB cashflow transaction to a date outside of test period");
//        dpAndWdByChannelObject transactionCashFlow1 = new dpAndWdByChannelObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, 0, 0, 7, 0, 0), "Deposit", "TestBank Transfers", "Bank Transfers", 40.1, 6, getCurrentTimestampDbFormat());
//        insertObjectToDb(DP_AND_WD_BY_CHANNEL_TABLE_NAME, transactionCashFlow1);
//        page.reload();
//        paymentsPage.navigateOperationsTab(client.getUcid());
//        Allure.step("filter test date");
//        paymentsPage.selectDateFilter("Last 7 days");
//        Allure.step("check that only data for the test date is displayed");
//        paymentsPage.hoverOverFinancialTransactionsGraphByDateSingleDay(getPreviousDayMonthDay());
//        paymentsPage.checkFinancialTransactionsRowInTooltip("Deposit", String.valueOf(Math.round(payments.totalDepositUsd)));
//        paymentsPage.checkFinancialTransactionsRowInTooltip("Withdrawal", String.valueOf(Math.round(payments.totalWithdrawalUsd)));
//        paymentsPage.checkFinancialTransactionsRowInTooltip("Credit", String.valueOf(Math.round(payments.totalCreditsUsd)));
//        paymentsPage.checkCashflowTopPaymentSystemTypesHeaderDeposit(transactionCashFlow.channelCategory, String.valueOf(Math.round(transactionCashFlow.totalAmountUsd)));
//    }
//
//    @Test
//    @Tag(TEAM_BACKOFFICE)
//    @Tag(LAYER_WEB)
//    @AllureId("609")
//    @DisplayName("Operations tab. User can filter operations by Dates Last 90 days")
//    public void filterLast90DaysTest() throws Exception {
//        investigationPage.navigateEnterPage();
//        cleanUserFinancialTransactionDbUcid(client.getUcid());
//        cleanUserCashflowDb(client.getUcid());
//        keycloackPage.loginAsAutotestUser();
//        Allure.step("add to DB transaction to a test date");
//        PaymentsTotalObject payments = new PaymentsTotalObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getYesterdayDate(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
//        insertObjectToDb("vindex_test.payments_total", payments);
//        Allure.step("add to DB cashflow transaction to a test date ");
//        dpAndWdByChannelObject transactionCashFlow = new dpAndWdByChannelObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getYesterdayDate(), "Deposit", "TestBank Transfers", "Bank Transfers", 40.1, 6, getCurrentTimestampDbFormat());
//        insertObjectToDb(DP_AND_WD_BY_CHANNEL_TABLE_NAME, transactionCashFlow);
//        Allure.step("add to DB transaction to a date to a date outside of test period");
//        PaymentsTotalObject payments1 = new PaymentsTotalObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getPrevious90DaysTimestampYearMonthDay(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
//        insertObjectToDb("vindex_test.payments_total", payments1);
//        Allure.step("add to DB cashflow transaction to a date outside of test period");
//        dpAndWdByChannelObject transactionCashFlow1 = new dpAndWdByChannelObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getPrevious90DaysTimestampYearMonthDay(), "Deposit", "TestBank Transfers", "Bank Transfers", 40.1, 6, getCurrentTimestampDbFormat());
//        insertObjectToDb(DP_AND_WD_BY_CHANNEL_TABLE_NAME, transactionCashFlow1);
//        page.reload();
//        paymentsPage.navigateOperationsTab(client.getUcid());
//        Allure.step("filter test date");
//        paymentsPage.selectDateFilter("Last 90 days");
//        Allure.step("check that only data for the test date is displayed");
//        paymentsPage.hoverOverFinancialTransactionsGraphByDateSingleDay(getPreviousDayMonthDay());
//        paymentsPage.checkFinancialTransactionsRowInTooltip("Deposit", String.valueOf(Math.round(payments.totalDepositUsd)));
//        paymentsPage.checkFinancialTransactionsRowInTooltip("Withdrawal", String.valueOf(Math.round(payments.totalWithdrawalUsd)));
//        paymentsPage.checkFinancialTransactionsRowInTooltip("Credit", String.valueOf(Math.round(payments.totalCreditsUsd)));
//        paymentsPage.checkCashflowTopPaymentSystemTypesHeaderDeposit(transactionCashFlow.channelCategory, String.valueOf(Math.round(transactionCashFlow.totalAmountUsd)));
//    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("642")
    @DisplayName("Operations tab. User can manipulate timeline by click to a half of timeline")
    public void manipulateTimelineByClickTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigateOperationsTab(client.getUcid());
        paymentsPage.clickOnPreLastTimelineSection();
        paymentsPage.checkLastTimelineSectionInactive();
        paymentsPage.clickOnTimelineSectionByIndex(1);
        paymentsPage.checkTimelineSectionInactive(0);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("641")
    @DisplayName("Operations tab. User can manipulate timeline by drag")
    public void manipulateTimelineByDragTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigateOperationsTab(client.getUcid());
        paymentsPage.shiftRightTimelineThumbToPreLastTimelineSection();
        paymentsPage.checkLastTimelineSectionInactive();
        paymentsPage.shiftLeftTimelineThumbToTimelineSectionIndex(2);
        paymentsPage.checkTimelineSectionInactive(0);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("635")
    @DisplayName("Operations tab. When user uses timeline , when user filters 6 days must have 1 inactive day on the right.")
    public void timelineInactiveDaysFilter6DaysTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigateOperationsTab(client.getUcid());
        Allure.step("filter 6 days");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(5));
        paymentsPage.checkTimelineSectionInactive(6);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("634")
    @DisplayName("Operations tab. When user uses timeline , when user filters 5 days must have 1 inactive day on both sides")
    public void timelineInactiveDaysFilter5DaysTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigateOperationsTab(client.getUcid());
        Allure.step("filter 5 days");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(4));
        paymentsPage.checkTimelineSectionInactive(6);
        paymentsPage.checkTimelineSectionInactive(0);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("633")
    @DisplayName("Operations tab. When user uses timeline , when user filters 4 days must have 1 inactive day on the left and 2 on the right.")
    public void timelineInactiveDaysFilter4DaysTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigateOperationsTab(client.getUcid());
        Allure.step("filter 4 days");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(3));
        paymentsPage.checkTimelineSectionInactive(6);
        paymentsPage.checkTimelineSectionInactive(5);
        paymentsPage.checkTimelineSectionInactive(0);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("632")
    @DisplayName("Operations tab. When user uses timeline , when user filters three days must have 2 inactive days on both sides")
    public void timelineInactiveDaysFilter3DaysTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigateOperationsTab(client.getUcid());
        Allure.step("filter 3 days");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(2));
        paymentsPage.checkTimelineSectionInactive(6);
        paymentsPage.checkTimelineSectionInactive(5);
        paymentsPage.checkTimelineSectionInactive(0);
        paymentsPage.checkTimelineSectionInactive(1);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("631")
    @DisplayName("Operations tab. When user uses timeline , when user filters two days must have 2 inactive days on the left and 3 on the right")
    public void timelineInactiveDaysFilter2DaysTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigateOperationsTab(client.getUcid());
        Allure.step("filter 2 days");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(1));
        paymentsPage.checkTimelineSectionInactive(6);
        paymentsPage.checkTimelineSectionInactive(5);
        paymentsPage.checkTimelineSectionInactive(4);
        paymentsPage.checkTimelineSectionInactive(0);
        paymentsPage.checkTimelineSectionInactive(1);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("630")
    @DisplayName("Operations tab. When user uses timeline , when user filters one day must have 3 inactive days on both sides")
    public void timelineInactiveDaysFilter1DayTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigateOperationsTab(client.getUcid());
        Allure.step("filter 1 day");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getCurrentDate());
        paymentsPage.checkTimelineSectionInactive(6);
        paymentsPage.checkTimelineSectionInactive(5);
        paymentsPage.checkTimelineSectionInactive(4);
        paymentsPage.checkTimelineSectionInactive(0);
        paymentsPage.checkTimelineSectionInactive(1);
        paymentsPage.checkTimelineSectionInactive(2);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("658")
    @DisplayName("Operations tab. Financial transaction graph, when filtered 99 days - 9 months Division = months Timeline = every month")
    public void filterLegendFinancialTransaction99DaysAnd10monthsTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        Allure.step("add to DB transaction in a testing interval so financial transaction graph will be visible");
        cleanUserFinancialTransactionDbUcid(client.getUcid());
        PaymentsTotalObject payments = new PaymentsTotalObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getYesterdayDate(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.payments_total", payments);
        paymentsPage.navigateOperationsTab(client.getUcid());
        Allure.step("filter 99 days");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(98));
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(Utils.getCurrentDateMonthYear());
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDateMonthYearIntMonth(3));
        page.reload();
        Allure.step("filter 98 days");
        paymentsPage.selectDatesInCalendar(getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, 0, 8, 1, 0, 0), getCurrentDate());
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(Utils.getCurrentDateMonthYear());
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDateMonthYearIntMonth(3));
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDateMonthYearIntMonth(8));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("701")
    @DisplayName("Operations tab. Financial transaction graph, when filtered 10 months - 20 months Division = months Timeline = every 4 month")
    public void filterLegendFinancialTransaction10monthsAnd20monthsTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        Allure.step("add to DB transaction in a testing interval so financial transaction graph will be visible");
        cleanUserFinancialTransactionDbUcid(client.getUcid());
        PaymentsTotalObject payments = new PaymentsTotalObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getYesterdayDate(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.payments_total", payments);
        paymentsPage.navigateOperationsTab(client.getUcid());
        Allure.step("filter 10 months");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDateYearMonthDayByIntMonthMinus1Day(10));
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(Utils.getPreviousDateMonthYearIntMonth(1));
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDateMonthYearIntMonth(4));
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDateMonthYearIntMonth(7));
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDateMonthYearIntMonth(10));
        page.reload();
        Allure.step("filter 20 months");
        paymentsPage.selectDatesInCalendar(getPreviousDateYearMonthDayByIntMonthMinus1Day(19), getCurrentDate());
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDateMonthYearIntMonth(1));
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDateMonthYearIntMonth(4));
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDateMonthYearIntMonth(7));
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDateMonthYearIntMonth(10));
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDateMonthYearIntMonth(19));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("655")
    @DisplayName("Operations tab. Financial transaction graph, when filtered 1-14 days Division = 1 day Timeline = every day")
    public void filterLegendFinancialTransaction1DayAnd14DaysTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        Allure.step("add to DB transaction in a testing interval so financial transaction graph will be visible");
        cleanUserFinancialTransactionDbUcid(client.getUcid());
        PaymentsTotalObject payments = new PaymentsTotalObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.payments_total", payments);
        paymentsPage.navigateOperationsTab(client.getUcid());
        Allure.step("filter 1 day");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getCurrentDate());
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(Utils.getCurrentDateMonthDay());
        page.reload();
        Allure.step("filter 14 days");
        paymentsPage.selectDatesInCalendar(getPreviousDayByIntDaysYearMonthDay(13), getCurrentDate());
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(Utils.getCurrentDateMonthDay());
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDayMonthDayByIntDay(13));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("656")
    @DisplayName("Operations tab. Financial transaction graph, when filtered 15-20 days Division = 1 day Timeline = every 4 day")
    public void filterLegendFinancialTransaction15DaysAnd20DaysTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        Allure.step("add to DB transaction in a testing interval so financial transaction graph will be visible");
        cleanUserFinancialTransactionDbUcid(client.getUcid());
        PaymentsTotalObject payments = new PaymentsTotalObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.payments_total", payments);
        paymentsPage.navigateOperationsTab(client.getUcid());
        Allure.step("filter 15 days");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(14));
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDayMonthDayByIntDay(2));
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDayMonthDayByIntDay(14));
        page.reload();
        Allure.step("filter 20 days");
        paymentsPage.selectDatesInCalendar(getPreviousDayByIntDaysYearMonthDay(19), getCurrentDate());
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDayMonthDayByIntDay(3));
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDayMonthDayByIntDay(19));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("702")
    @DisplayName("Operations tab. Financial transaction graph, when filtered 20+ months Division = years Timeline = every year")
    public void filterLegendFinancialTransactionMoreThan20monthsDaysTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        Allure.step("add to DB transaction in a testing interval so financial transaction graph will be visible");
        cleanUserFinancialTransactionDbUcid(client.getUcid());
        PaymentsTotalObject payments = new PaymentsTotalObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.payments_total", payments);
        paymentsPage.navigateOperationsTab(client.getUcid());
        Allure.step("filter 20 months ");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDateYearMonthDayByIntMonthMinus1Day(21));
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(getCurrentYear());
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousYearByInt(1));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("657")
    @DisplayName("Operations tab. Financial transaction graph, when filtered 21-98 days Division = week Timeline = every week")
    public void filterLegendFinancialTransaction21DaysAnd98DaysTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        Allure.step("add to DB transaction in a testing interval so financial transaction graph will be visible");
        cleanUserFinancialTransactionDbUcid(client.getUcid());
        PaymentsTotalObject payments = new PaymentsTotalObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getYesterdayDate(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.payments_total", payments);
        paymentsPage.navigateOperationsTab(client.getUcid());
        Allure.step("filter 21 days");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(20));
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDayMonthDayByIntDay(6));
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDayMonthDayByIntDay(20));
        page.reload();
        Allure.step("filter 98 days");
        paymentsPage.selectDatesInCalendar(getPreviousDayByIntDaysYearMonthDay(97), getCurrentDate());
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDayMonthDayByIntDay(6));
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDayMonthDayByIntDay(97));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("636")
    @DisplayName("Operations tab. When user filters 1-7 days one division on timeline is 1 day with date under each section")
    public void filterLegend1And7DaysTest() throws Exception {
        cleanUserCashflowDb(client.getUcid());
        CrmTbDepositObject deposit = generateDepositByClient(client);
        deposit.createTimeUtc = (getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 6, 0, 0));
        deposit.createTime = (getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 6, 0, 0));
        deposit.statusId = 5;
        Allure.step("add record about deposit");
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, deposit);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigateOperationsTab(client.getUcid());
        Allure.step("filter one day");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getCurrentDate());
        paymentsPage.checkTimelineSectionVisibleByDate(getCurrentDateMonthDay());
        page.reload();
        Allure.step("filter seven days");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(6));
        paymentsPage.checkTimelineSectionVisibleByDate(getCurrentDateMonthDay());
        paymentsPage.checkTimelineSectionVisibleByDate(getPreviousDayMonthDayByIntDay(6));
    }

    @Deprecated
    @Disabled("requirements for timeline changed")
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("637")
    @DisplayName("Operations tab. When user filters 8-30 days one division on timeline is 1 day with date for every two days")
    public void filterLegend8And31DaysTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigateOperationsTab(client.getUcid());
        Allure.step("filter 8 day");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(7));
        paymentsPage.checkTimelineSectionVisibleByDate(getPreviousDayMonthDayByIntDay(7));
        paymentsPage.checkTimelineSectionVisibleByDate(getPreviousDayMonthDayByIntDay(1));
        page.reload();
        Allure.step("filter 30 days");
        paymentsPage.selectDatesInCalendar(getPreviousDayByIntDaysYearMonthDay(29), getCurrentDate());
        paymentsPage.checkTimelineSectionVisibleByDate(getPreviousDayMonthDayByIntDay(1));
        paymentsPage.checkTimelineSectionVisibleByDate(getPreviousDayMonthDayByIntDay(29));
    }


    @Deprecated
    @Disabled("requirements for timeline changed")
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("638")
    @DisplayName("Operations tab. When user filters 31-98 days one division on timeline is 1 week with legend for every section")
    public void filterLegend31And98DaysTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigateOperationsTab(client.getUcid());
        Allure.step("filter 31 day");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(30));
        paymentsPage.checkTimelineSectionVisibleByDate(getPreviousDayMonthDayByIntDay(30));
        paymentsPage.checkTimelineSectionVisibleByDate(getPreviousDayMonthDayByIntDay(2));
        page.reload();
        Allure.step("filter 98 days");
        paymentsPage.selectDatesInCalendar(getPreviousDayByIntDaysYearMonthDay(97), getCurrentDate());
        paymentsPage.checkTimelineSectionVisibleByDate(getPreviousDayMonthDayByIntDay(6));
        paymentsPage.checkTimelineSectionVisibleByDate(getPreviousDayMonthDayByIntDay(97));
    }

    @Deprecated
    @Disabled("requirements for timeline changed")
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("639")
    @DisplayName("Operations tab. When user filters 99 days - 3 years one division on timeline is month with legend for every two months")
    public void filterLegend98DaysAnd3YearTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigateOperationsTab(client.getUcid());
        Allure.step("filter 99 days");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(98));
        paymentsPage.checkTimelineSectionVisibleByDate(getPreviousDateMonthYearIntMonth(1));
        paymentsPage.checkTimelineSectionVisibleByDate(getPreviousDateMonthYearIntMonth(3));
        page.reload();
        Allure.step("filter 3 years");
        paymentsPage.selectDatesInCalendar(getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, 3, 0, -2, 0, 0), getCurrentDate());
        paymentsPage.checkTimelineSectionVisibleByDate(getCurrentDateMonthYear());
        paymentsPage.checkTimelineSectionVisibleByDate(getPreviousDateMonthYearIntYears(2));
    }

    @Deprecated
    @Disabled("requirements for timeline changed")
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("640")
    @DisplayName("Operations tab. When user filters 3+ years division on timeline is 1 year with legend for every year")
    public void filterLegend3YearsTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigateOperationsTab(client.getUcid());
        Allure.step("filter 3 years");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDateByIntYearMonthDay(3));
        paymentsPage.checkTimelineSectionVisibleByDate(getPreviousYearByInt(3));
        paymentsPage.checkTimelineSectionVisibleByDate(getCurrentYear());
    }

}
