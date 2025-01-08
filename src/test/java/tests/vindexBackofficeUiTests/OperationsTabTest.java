package tests.vindexBackofficeUiTests;

import businessObjects.db.clickhouse.dpAndWdByChannel.dpAndWdByChannelObject;
import businessObjects.db.clickhouse.paymentsTotal.PaymentsTotalObject;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseWeb;
import utils.Utils;

import static helpers.database.DbHelper.insertObjectToDb;
import static helpers.database.OperationsHelper.cleanUserCashflowDb;
import static helpers.database.OperationsHelper.cleanUserFinancialTransactionDbUcid;
import static utils.Constants.LAYER_WEB;
import static utils.Constants.TEAM_BACKOFFICE;
import static utils.Utils.*;

public class OperationsTabTest extends TestBaseWeb {

    String testUserUcid = "infinox-171701";

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("562")
    @DisplayName("Operations tab. Cashflow chart show empty state when it not have data DB")
    public void cashflowEmptyStateTest() throws Exception {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        cleanUserCashflowDb(testUserUcid);
        operationsPage.navigateOperationsTab(testUserUcid);
        operationsPage.checkCashflowEmptyStateIsVisible();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("573")
    @DisplayName("Operations tab. Cashflow chart show empty one side on deposit when it not have data DB")
    public void cashflowOnlyOneWithdrawalFilledTest() throws Exception {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        cleanUserCashflowDb(testUserUcid);
        dpAndWdByChannelObject withtdrawal = new dpAndWdByChannelObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Withdrawal", "TestPaymentService", "Payment Services", 12.0, 22, getCurrentTimestampDbFormat());
        Allure.step("add record about withdrawal");
        insertObjectToDb("vindex_test.dp_and_wd_by_channel", withtdrawal);
        operationsPage.navigateOperationsTab(testUserUcid);
        operationsPage.checkCashflowEmptyStateDepositIsVisible();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("574")
    @DisplayName("Operations tab. Cashflow chart show empty one side on deposit when it not have data DB")
    public void CashflowOnlyDepositSideFilledTest() throws Exception {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        cleanUserCashflowDb(testUserUcid);
        dpAndWdByChannelObject withtdrawal = new dpAndWdByChannelObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Deposit", "TestPaymentService", "Payment Services", 12.0, 22, getCurrentTimestampDbFormat());
        Allure.step("add record about deposit");
        insertObjectToDb("vindex_test.dp_and_wd_by_channel", withtdrawal);
        operationsPage.navigateOperationsTab(testUserUcid);
        operationsPage.checkCashflowEmptyStateWithdrawalIsVisible();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("588")
    @DisplayName("Operations tab. Cashflow chart show data from DB")
    public void cashflowTotalValueInTipTest() throws Exception {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        cleanUserCashflowDb(testUserUcid);
        dpAndWdByChannelObject transaction = new dpAndWdByChannelObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Deposit", "TestPaymentService", "Payment Services", 12.0, 22, getCurrentTimestampDbFormat());
        Allure.step("add record about deposit");
        insertObjectToDb("vindex_test.dp_and_wd_by_channel", transaction);
        operationsPage.navigateOperationsTab(testUserUcid);
        operationsPage.hoverOverCashflowLineByTypeDeposit(transaction.psCategory);
        operationsPage.checkTotalCountByPaymentSystem(transaction.paymentSystem, String.valueOf(Math.round(transaction.totalAmountUsd)));
        dpAndWdByChannelObject transaction2 = new dpAndWdByChannelObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Deposit", "TestPaymentService", "Payment Services", 12.0, 11, getCurrentTimestampDbFormat());
        Allure.step("add record about deposit");
        insertObjectToDb("vindex_test.dp_and_wd_by_channel", transaction2);
        page.reload();
        operationsPage.hoverOverCashflowLineByTypeDeposit(transaction2.psCategory);
        operationsPage.checkTotalCountByPaymentSystem(transaction2.paymentSystem, String.valueOf(Math.round(transaction2.totalAmountUsd)));
        dpAndWdByChannelObject transaction3 = new dpAndWdByChannelObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Deposit", "TestPaymentServiceSecond", "Payment Services", 12.0, 6, getCurrentTimestampDbFormat());
        Allure.step("add record about deposit");
        insertObjectToDb("vindex_test.dp_and_wd_by_channel", transaction3);
        page.reload();
        operationsPage.hoverOverCashflowLineByTypeDeposit(transaction3.psCategory);
        operationsPage.checkTotalCountByPaymentSystem(transaction3.paymentSystem, String.valueOf(Math.round(transaction3.totalAmountUsd)));
        dpAndWdByChannelObject transaction4 = new dpAndWdByChannelObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Deposit", "TestCards", "Cards", 12.0, 6, getCurrentTimestampDbFormat());
        Allure.step("add record about deposit");
        insertObjectToDb("vindex_test.dp_and_wd_by_channel", transaction4);
        page.reload();
        operationsPage.hoverOverCashflowLineByTypeDeposit(transaction4.psCategory);
        operationsPage.checkTotalCountByPaymentSystem(transaction4.paymentSystem, String.valueOf(Math.round(transaction4.totalAmountUsd)));
        dpAndWdByChannelObject transaction5 = new dpAndWdByChannelObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Deposit", "TestBank Transfers", "Bank Transfers", 12.1, 6, getCurrentTimestampDbFormat());
        dpAndWdByChannelObject transaction6 = new dpAndWdByChannelObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Deposit", "TestCrypto", "Crypto", 12.2, 6, getCurrentTimestampDbFormat());
        dpAndWdByChannelObject transaction7 = new dpAndWdByChannelObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Deposit", "TestP2P", "P2P", 12.3, 6, getCurrentTimestampDbFormat());
        dpAndWdByChannelObject transaction8 = new dpAndWdByChannelObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Deposit", "Other", "Other", 12.4, 6, getCurrentTimestampDbFormat());
        dpAndWdByChannelObject transaction9 = new dpAndWdByChannelObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Withdrawal", "TestBank Transfers", "Bank Transfers", 12.0, 7, getCurrentTimestampDbFormat());
        dpAndWdByChannelObject transaction10 = new dpAndWdByChannelObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Withdrawal", "TestCrypto", "Crypto", 12.5, 7, getCurrentTimestampDbFormat());
        dpAndWdByChannelObject transaction11 = new dpAndWdByChannelObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Withdrawal", "TestP2P", "P2P", 12.6, 7, getCurrentTimestampDbFormat());
        dpAndWdByChannelObject transaction12 = new dpAndWdByChannelObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Withdrawal", "TestOther", "Other", 12.7, 7, getCurrentTimestampDbFormat());
        dpAndWdByChannelObject transaction13 = new dpAndWdByChannelObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Withdrawal", "TestPayment Services", "Payment Services", 12.8, 7, getCurrentTimestampDbFormat());
        dpAndWdByChannelObject transaction14 = new dpAndWdByChannelObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Withdrawal", "TestCards", "Cards", 12.9, 7, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.dp_and_wd_by_channel", transaction5);
        insertObjectToDb("vindex_test.dp_and_wd_by_channel", transaction6);
        insertObjectToDb("vindex_test.dp_and_wd_by_channel", transaction7);
        insertObjectToDb("vindex_test.dp_and_wd_by_channel", transaction8);
        insertObjectToDb("vindex_test.dp_and_wd_by_channel", transaction9);
        insertObjectToDb("vindex_test.dp_and_wd_by_channel", transaction10);
        insertObjectToDb("vindex_test.dp_and_wd_by_channel", transaction11);
        insertObjectToDb("vindex_test.dp_and_wd_by_channel", transaction12);
        insertObjectToDb("vindex_test.dp_and_wd_by_channel", transaction13);
        insertObjectToDb("vindex_test.dp_and_wd_by_channel", transaction14);
        Allure.step("add other transactions ");
        page.waitForTimeout(10_000);
        page.reload();
        page.waitForTimeout(10_000);
        operationsPage.hoverOverCashflowLineByTypeDeposit(transaction5.psCategory);
        operationsPage.checkTotalCountByPaymentSystem(transaction5.paymentSystem, String.valueOf(Math.round(transaction5.totalAmountUsd)));
        operationsPage.hoverOverCashflowLineByTypeDeposit(transaction6.psCategory);
        operationsPage.checkTotalCountByPaymentSystem(transaction6.paymentSystem, String.valueOf(Math.round(transaction6.totalAmountUsd)));
        operationsPage.hoverOverCashflowLineByTypeDeposit(transaction7.psCategory);
        operationsPage.checkTotalCountByPaymentSystem(transaction7.paymentSystem, String.valueOf(Math.round(transaction7.totalAmountUsd)));
        operationsPage.hoverOverCashflowLineByTypeDeposit(transaction8.psCategory);
        operationsPage.checkTotalCountByPaymentSystem(transaction8.paymentSystem, String.valueOf(Math.round(transaction8.totalAmountUsd)));
        operationsPage.hoverOverCashflowLineByTypeWithdrawal(transaction9.psCategory);
        operationsPage.checkTotalCountByPaymentSystem(transaction9.paymentSystem, String.valueOf(Math.round(transaction9.totalAmountUsd)));
        operationsPage.hoverOverCashflowLineByTypeWithdrawal(transaction10.psCategory);
        operationsPage.checkTotalCountByPaymentSystem(transaction10.paymentSystem, String.valueOf(Math.round(transaction10.totalAmountUsd)));
        operationsPage.hoverOverCashflowLineByTypeWithdrawal(transaction11.psCategory);
        operationsPage.checkTotalCountByPaymentSystem(transaction11.paymentSystem, String.valueOf(Math.round(transaction11.totalAmountUsd)));
        operationsPage.hoverOverCashflowLineByTypeWithdrawal(transaction12.psCategory);
        operationsPage.checkTotalCountByPaymentSystem(transaction12.paymentSystem, String.valueOf(Math.round(transaction12.totalAmountUsd)));
        operationsPage.hoverOverCashflowLineByTypeWithdrawal(transaction13.psCategory);
        operationsPage.checkTotalCountByPaymentSystem(transaction13.paymentSystem, String.valueOf(Math.round(transaction13.totalAmountUsd)));
        operationsPage.hoverOverCashflowLineByTypeWithdrawal(transaction14.psCategory);
        operationsPage.checkTotalCountByPaymentSystem(transaction14.paymentSystem, String.valueOf(Math.round(transaction14.totalAmountUsd)));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("589")
    @DisplayName("Operations tab. Cashflow header show data from DB")
    public void cashflowValueInHeaderTest() throws Exception {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        cleanUserCashflowDb(testUserUcid);
        dpAndWdByChannelObject transaction = new dpAndWdByChannelObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Deposit", "TestPaymentService", "Payment Services", 12.1, 22, getCurrentTimestampDbFormat());
        Allure.step("add record about deposit");
        insertObjectToDb("vindex_test.dp_and_wd_by_channel", transaction);
        operationsPage.navigateOperationsTab(testUserUcid);
        operationsPage.checkCashflowTopPaymentSystemTypesHeaderDeposit(transaction.psCategory, String.valueOf(Math.round(transaction.totalAmountUsd)));

        dpAndWdByChannelObject transaction2 = new dpAndWdByChannelObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Deposit", "TestPaymentService", "Payment Services", 24.1, 22, getCurrentTimestampDbFormat());
        Allure.step("override record about deposit");
        insertObjectToDb("vindex_test.dp_and_wd_by_channel", transaction2);
        page.reload();
        operationsPage.checkCashflowTopPaymentSystemTypesHeaderDeposit(transaction2.psCategory, String.valueOf(Math.round(transaction2.totalAmountUsd)));

        dpAndWdByChannelObject transaction3 = new dpAndWdByChannelObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Deposit", "TestPaymentService2", "Payment Services", 14.1, 22, getCurrentTimestampDbFormat());
        Allure.step("add record about deposit with same cathegory but different name");
        insertObjectToDb("vindex_test.dp_and_wd_by_channel", transaction3);
        page.reload();
        operationsPage.checkCashflowTopPaymentSystemTypesHeaderDeposit(transaction2.psCategory, String.valueOf(Math.round(transaction2.totalAmountUsd + transaction3.totalAmountUsd)));

        Allure.step("add to DB transaction with another category  and the bigger amount than previous category ");
        dpAndWdByChannelObject transaction5 = new dpAndWdByChannelObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Deposit", "TestBank Transfers", "Bank Transfers", 40.1, 6, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.dp_and_wd_by_channel", transaction5);
        page.reload();
        operationsPage.checkCashflowTopPaymentSystemTypesHeaderDeposit(transaction5.psCategory, String.valueOf(Math.round(transaction5.totalAmountUsd)));

        Allure.step("add to DB withdrawal transaction");
        dpAndWdByChannelObject transaction6 = new dpAndWdByChannelObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Withdrawal", "TestBank Transfers", "Bank Transfers", 42.1, 6, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.dp_and_wd_by_channel", transaction6);
        page.reload();
        operationsPage.checkCashflowTopPaymentSystemTypesHeaderWithdrawal(transaction6.psCategory, String.valueOf(Math.round(transaction6.totalAmountUsd)));

        Allure.step("add to DB withdrawal transaction with another category  and the bigger amount than previous category ");
        dpAndWdByChannelObject transaction7 = new dpAndWdByChannelObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Withdrawal", "P2P withdrawal", "P2P", 43.1, 6, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.dp_and_wd_by_channel", transaction7);
        page.reload();
        operationsPage.checkCashflowTopPaymentSystemTypesHeaderWithdrawal(transaction7.psCategory, String.valueOf(Math.round(transaction7.totalAmountUsd)));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("574")
    @DisplayName("Operations tab. financialTransaction chart show empty state when it not have data DB")
    public void financialTransactionEmptyStateTest() throws Exception {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        cleanUserFinancialTransactionDbUcid(testUserUcid);
        operationsPage.navigateOperationsTab(testUserUcid);
        operationsPage.checkFinancialTransactionEmptyStateIsVisible();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("597")
    @DisplayName("Operations tab. financialTransaction tabs show data from DB")
    public void financialTransactionTabsShowsDataFromDb() throws Exception {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        Allure.step("add to DB transaction with another category  and the bigger amount than previous category ");
        cleanUserFinancialTransactionDbUcid(testUserUcid);
        PaymentsTotalObject payments = new PaymentsTotalObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.payments_total", payments);
        operationsPage.navigateOperationsTab(testUserUcid);
        operationsPage.checkFinancialTransactionsTilesValues("Net deposits", String.valueOf(Math.round((payments.netDepositsUsd))), String.valueOf(Math.round(payments.totalBalanceopsCount)));
        operationsPage.checkFinancialTransactionsTilesValues("Total deposits", String.valueOf(Math.round((payments.totalDepositUsd))), String.valueOf(Math.round(payments.totalDepositCount)));
        operationsPage.checkFinancialTransactionsTilesValues("Total withdrawals", String.valueOf(Math.round((payments.totalWithdrawalUsd))), String.valueOf(Math.round(payments.totalWithdrawalCount)));
        operationsPage.checkFinancialTransactionsTilesValues("Total Internal transfers", String.valueOf(Math.round((payments.totalTransfersUsd))), String.valueOf(Math.round(payments.totalTransfersCount)));
        operationsPage.checkFinancialTransactionsTilesValues("Total credit", String.valueOf(Math.round((payments.totalCreditsUsd))), String.valueOf(Math.round(payments.totalCreditsCount)));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("597")
    @DisplayName("Operations tab. financialTransaction graph show data from DB")
    public void financialTransactionGraphShowsDataFromDb() throws Exception {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        Allure.step("add to DB transaction with another category  and the bigger amount than previous category ");
        cleanUserFinancialTransactionDbUcid(testUserUcid);
        PaymentsTotalObject payments = new PaymentsTotalObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.payments_total", payments);
        operationsPage.navigateOperationsTab(testUserUcid);
        Allure.step("filter test date");
        operationsPage.selectDateFilter("Last 7 days");
        operationsPage.hoverOverFinancialTransactionsGraphByDateSingleDay(getCurrentDateMonthDay());
        operationsPage.checkFinancialTransactionsRowInTooltip("Deposit", String.valueOf(Math.round(payments.totalDepositUsd)));
        operationsPage.checkFinancialTransactionsRowInTooltip("Withdrawal", String.valueOf(Math.round(payments.totalWithdrawalUsd)));
        operationsPage.checkFinancialTransactionsRowInTooltip("Credit", String.valueOf(Math.round(payments.totalCreditsUsd)));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("617")
    @DisplayName("Operations tab. user can filter data by account")
    public void operationsTabCanBeFilteredByAccount() throws Exception {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        Allure.step("add to DB transaction with financial transaction fot the first trade account");
        cleanUserFinancialTransactionDbUcid(testUserUcid);
        PaymentsTotalObject payments = new PaymentsTotalObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.payments_total", payments);
        Allure.step("add to DB cashflow transaction for the first trade account");
        cleanUserCashflowDb(testUserUcid);
        dpAndWdByChannelObject transactionCashFlow = new dpAndWdByChannelObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Deposit", "TestBank Transfers", "Bank Transfers", 40.1, 6, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.dp_and_wd_by_channel", transactionCashFlow);
        page.reload();
        operationsPage.navigateOperationsTab(testUserUcid);
        operationsPage.checkFinancialTransactionEmptyStateIsNotVisible();
        operationsPage.clickOnAccountSelectionWindow();
        operationsPage.selectTradingAccount("17170102");
        operationsPage.checkFinancialTransactionEmptyStateIsVisible();
        operationsPage.clearSelectedTradingAccount();
        operationsPage.selectTradingAccount("17170101");
        operationsPage.checkFinancialTransactionEmptyStateIsNotVisible();
        Allure.step("add to DB transaction with financial transaction fot the second trade account");
        cleanUserFinancialTransactionDbUcid(testUserUcid);
        PaymentsTotalObject payments2 = new PaymentsTotalObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_102, getCurrentDate(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.payments_total", payments2);
        Allure.step("add to DB cashflow transaction for the second trade account");
        cleanUserCashflowDb(testUserUcid);
        dpAndWdByChannelObject transactionCashFlow2 = new dpAndWdByChannelObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_102, getCurrentDate(), "Deposit", "TestBank Transfers", "Bank Transfers", 40.1, 6, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.dp_and_wd_by_channel", transactionCashFlow2);
        page.reload();
        operationsPage.clearSelectedTradingAccount();
        operationsPage.clickOnAccountSelectionWindow();
        operationsPage.selectTradingAccount("17170101");
        operationsPage.checkFinancialTransactionEmptyStateIsVisible();
        operationsPage.clearSelectedTradingAccount();
        operationsPage.selectTradingAccount("17170102");
        operationsPage.checkFinancialTransactionEmptyStateIsNotVisible();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("610")
    @DisplayName("Operations tab. User can filter operations by Dates Custom - one day")
    public void filterCustomOneDayTest() throws Exception {
        investigationPage.navigate();
        cleanUserFinancialTransactionDbUcid(testUserUcid);
        cleanUserCashflowDb(testUserUcid);
        keycloackPage.loginWeb("dev", "123");
        Allure.step("add to DB transaction to a test date");
        PaymentsTotalObject payments = new PaymentsTotalObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, "2024-12-11", 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.payments_total", payments);
        Allure.step("add to DB cashflow transaction to a test date ");
        dpAndWdByChannelObject transactionCashFlow = new dpAndWdByChannelObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, "2024-12-11", "Deposit", "TestBank Transfers", "Bank Transfers", 40.1, 6, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.dp_and_wd_by_channel", transactionCashFlow);
        Allure.step("add to DB transaction to a date next to a test date");
        PaymentsTotalObject payments1 = new PaymentsTotalObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, "2024-12-13", 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.payments_total", payments1);
        Allure.step("add to DB cashflow transaction to a date next to a test date ");
        dpAndWdByChannelObject transactionCashFlow1 = new dpAndWdByChannelObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, "2024-12-13", "Deposit", "TestBank Transfers", "Bank Transfers", 40.1, 6, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.dp_and_wd_by_channel", transactionCashFlow1);
        Allure.step("add to DB transaction to a date previous to a test date");
        PaymentsTotalObject payments3 = new PaymentsTotalObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, "2024-12-10", 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.payments_total", payments3);
        Allure.step("add to DB cashflow transaction to a date previous to a test date ");
        dpAndWdByChannelObject transactionCashFlow3 = new dpAndWdByChannelObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, "2024-12-10", "Deposit", "TestBank Transfers", "Bank Transfers", 40.1, 6, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.dp_and_wd_by_channel", transactionCashFlow3);
        page.reload();
        operationsPage.navigateOperationsTab(testUserUcid);
        Allure.step("filter test date");
        operationsPage.selectDatesInCalendar("2024-12-11", "2024-12-11");
        Allure.step("check that only data for the test date is displayed");
        operationsPage.hoverOverFinancialTransactionsGraphByDateSingleDay("Dec 11");
        operationsPage.checkFinancialTransactionsRowInTooltip("Deposit", String.valueOf(Math.round(payments.totalDepositUsd)));
        operationsPage.checkFinancialTransactionsRowInTooltip("Withdrawal", String.valueOf(Math.round(payments.totalWithdrawalUsd)));
        operationsPage.checkFinancialTransactionsRowInTooltip("Credit", String.valueOf(Math.round(payments.totalCreditsUsd)));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("615")
    @DisplayName("Operations tab. User can filter operations by Dates Last 1 year")
    public void filterLastYearTest() throws Exception {
        investigationPage.navigate();
        cleanUserFinancialTransactionDbUcid(testUserUcid);
        cleanUserCashflowDb(testUserUcid);
        keycloackPage.loginWeb("dev", "123");
        Allure.step("add to DB transaction to a test date");
        PaymentsTotalObject payments = new PaymentsTotalObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getPreviousDayTimestampYearMonthDay(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.payments_total", payments);
        Allure.step("add to DB cashflow transaction to a test date ");
        dpAndWdByChannelObject transactionCashFlow = new dpAndWdByChannelObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getPreviousDayTimestampYearMonthDay(), "Deposit", "TestBank Transfers", "Bank Transfers", 40.1, 6, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.dp_and_wd_by_channel", transactionCashFlow);
        Allure.step("add to DB transaction to a date to a date outside of test period");
        PaymentsTotalObject payments1 = new PaymentsTotalObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getPreviousYearTimestampYearMonthDay(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.payments_total", payments1);
        Allure.step("add to DB cashflow transaction to a date outside of test period");
        dpAndWdByChannelObject transactionCashFlow1 = new dpAndWdByChannelObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getPreviousYearTimestampYearMonthDay(), "Deposit", "TestBank Transfers", "Bank Transfers", 40.1, 6, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.dp_and_wd_by_channel", transactionCashFlow1);
        page.reload();
        operationsPage.navigateOperationsTab(testUserUcid);
        Allure.step("filter test date");
        operationsPage.selectDateFilter("Last 1 year");
        Allure.step("check that only data for the test date is displayed");
        operationsPage.hoverOverFinancialTransactionsGraphByDateSingleDay(getPreviousDayMonthDay());
        operationsPage.checkFinancialTransactionsRowInTooltip("Deposit", String.valueOf(Math.round(payments.totalDepositUsd)));
        operationsPage.checkFinancialTransactionsRowInTooltip("Withdrawal", String.valueOf(Math.round(payments.totalWithdrawalUsd)));
        operationsPage.checkFinancialTransactionsRowInTooltip("Credit", String.valueOf(Math.round(payments.totalCreditsUsd)));
        operationsPage.checkCashflowTopPaymentSystemTypesHeaderDeposit(transactionCashFlow.psCategory, String.valueOf(Math.round(transactionCashFlow.totalAmountUsd)));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("611")
    @DisplayName("Operations tab. User can filter operations by Dates Last 30 days")
    public void filterLast30DaysTest() throws Exception {
        investigationPage.navigate();
        cleanUserFinancialTransactionDbUcid(testUserUcid);
        cleanUserCashflowDb(testUserUcid);
        keycloackPage.loginWeb("dev", "123");
        Allure.step("add to DB transaction to a test date");
        PaymentsTotalObject payments = new PaymentsTotalObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getPreviousDayTimestampYearMonthDay(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.payments_total", payments);
        Allure.step("add to DB cashflow transaction to a test date ");
        dpAndWdByChannelObject transactionCashFlow = new dpAndWdByChannelObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getPreviousDayTimestampYearMonthDay(), "Deposit", "TestBank Transfers", "Bank Transfers", 40.1, 6, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.dp_and_wd_by_channel", transactionCashFlow);
        Allure.step("add to DB transaction to a date to a date outside of test period");
        PaymentsTotalObject payments1 = new PaymentsTotalObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getPreviousMonthTimestampYearMonthDay(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.payments_total", payments1);
        Allure.step("add to DB cashflow transaction to a date outside of test period");
        dpAndWdByChannelObject transactionCashFlow1 = new dpAndWdByChannelObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getPreviousMonthTimestampYearMonthDay(), "Deposit", "TestBank Transfers", "Bank Transfers", 40.1, 6, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.dp_and_wd_by_channel", transactionCashFlow1);
        page.reload();
        operationsPage.navigateOperationsTab(testUserUcid);
        Allure.step("filter test date");
        operationsPage.selectDateFilter("Last 30 days");
        Allure.step("check that only data for the test date is displayed");
        operationsPage.hoverOverFinancialTransactionsGraphByDateSingleDay(getPreviousDayMonthDay());
        operationsPage.checkFinancialTransactionsRowInTooltip("Deposit", String.valueOf(Math.round(payments.totalDepositUsd)));
        operationsPage.checkFinancialTransactionsRowInTooltip("Withdrawal", String.valueOf(Math.round(payments.totalWithdrawalUsd)));
        operationsPage.checkFinancialTransactionsRowInTooltip("Credit", String.valueOf(Math.round(payments.totalCreditsUsd)));
        operationsPage.checkCashflowTopPaymentSystemTypesHeaderDeposit(transactionCashFlow.psCategory, String.valueOf(Math.round(transactionCashFlow.totalAmountUsd)));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("615")
    @DisplayName("Operations tab. User can filter operations by Dates Last 6 monts")
    public void filterLast6MonthsTest() throws Exception {
        investigationPage.navigate();
        cleanUserFinancialTransactionDbUcid(testUserUcid);
        cleanUserCashflowDb(testUserUcid);
        keycloackPage.loginWeb("dev", "123");
        Allure.step("add to DB transaction to a test date");
        PaymentsTotalObject payments = new PaymentsTotalObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getPreviousDayTimestampYearMonthDay(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.payments_total", payments);
        Allure.step("add to DB cashflow transaction to a test date ");
        dpAndWdByChannelObject transactionCashFlow = new dpAndWdByChannelObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getPreviousDayTimestampYearMonthDay(), "Deposit", "TestBank Transfers", "Bank Transfers", 40.1, 6, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.dp_and_wd_by_channel", transactionCashFlow);
        Allure.step("add to DB transaction to a date to a date outside of test period");
        PaymentsTotalObject payments1 = new PaymentsTotalObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getPrevious6MonthTimestampYearMonthDay(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.payments_total", payments1);
        Allure.step("add to DB cashflow transaction to a date outside of test period");
        dpAndWdByChannelObject transactionCashFlow1 = new dpAndWdByChannelObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getPrevious6MonthTimestampYearMonthDay(), "Deposit", "TestBank Transfers", "Bank Transfers", 40.1, 6, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.dp_and_wd_by_channel", transactionCashFlow1);
        page.reload();
        operationsPage.navigateOperationsTab(testUserUcid);
        Allure.step("filter test date");
        operationsPage.selectDateFilter("Last 6 months");
        Allure.step("check that only data for the test date is displayed");
        operationsPage.hoverOverFinancialTransactionsGraphByDateSingleDay(getPreviousDayMonthDay());
        operationsPage.checkFinancialTransactionsRowInTooltip("Deposit", String.valueOf(Math.round(payments.totalDepositUsd)));
        operationsPage.checkFinancialTransactionsRowInTooltip("Withdrawal", String.valueOf(Math.round(payments.totalWithdrawalUsd)));
        operationsPage.checkFinancialTransactionsRowInTooltip("Credit", String.valueOf(Math.round(payments.totalCreditsUsd)));
        operationsPage.checkCashflowTopPaymentSystemTypesHeaderDeposit(transactionCashFlow.psCategory, String.valueOf(Math.round(transactionCashFlow.totalAmountUsd)));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("609")
    @DisplayName("Operations tab. User can filter operations by Dates Last 7 days")
    public void filterLast7DaysTest() throws Exception {
        investigationPage.navigate();
        cleanUserFinancialTransactionDbUcid(testUserUcid);
        cleanUserCashflowDb(testUserUcid);
        keycloackPage.loginWeb("dev", "123");
        Allure.step("add to DB transaction to a test date");
        PaymentsTotalObject payments = new PaymentsTotalObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getPreviousDayTimestampYearMonthDay(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.payments_total", payments);
        Allure.step("add to DB cashflow transaction to a test date ");
        dpAndWdByChannelObject transactionCashFlow = new dpAndWdByChannelObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getPreviousDayTimestampYearMonthDay(), "Deposit", "TestBank Transfers", "Bank Transfers", 40.1, 6, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.dp_and_wd_by_channel", transactionCashFlow);
        Allure.step("add to DB transaction to a date to a date outside of test period");
        PaymentsTotalObject payments1 = new PaymentsTotalObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getPreviousWeekTimestampYearMonthDay(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.payments_total", payments1);
        Allure.step("add to DB cashflow transaction to a date outside of test period");
        dpAndWdByChannelObject transactionCashFlow1 = new dpAndWdByChannelObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getPreviousWeekTimestampYearMonthDay(), "Deposit", "TestBank Transfers", "Bank Transfers", 40.1, 6, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.dp_and_wd_by_channel", transactionCashFlow1);
        page.reload();
        operationsPage.navigateOperationsTab(testUserUcid);
        Allure.step("filter test date");
        operationsPage.selectDateFilter("Last 7 days");
        Allure.step("check that only data for the test date is displayed");
        operationsPage.hoverOverFinancialTransactionsGraphByDateSingleDay(getPreviousDayMonthDay());
        operationsPage.checkFinancialTransactionsRowInTooltip("Deposit", String.valueOf(Math.round(payments.totalDepositUsd)));
        operationsPage.checkFinancialTransactionsRowInTooltip("Withdrawal", String.valueOf(Math.round(payments.totalWithdrawalUsd)));
        operationsPage.checkFinancialTransactionsRowInTooltip("Credit", String.valueOf(Math.round(payments.totalCreditsUsd)));
        operationsPage.checkCashflowTopPaymentSystemTypesHeaderDeposit(transactionCashFlow.psCategory, String.valueOf(Math.round(transactionCashFlow.totalAmountUsd)));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("609")
    @DisplayName("Operations tab. User can filter operations by Dates Last 90 days")
    public void filterLast90DaysTest() throws Exception {
        investigationPage.navigate();
        cleanUserFinancialTransactionDbUcid(testUserUcid);
        cleanUserCashflowDb(testUserUcid);
        keycloackPage.loginWeb("dev", "123");
        Allure.step("add to DB transaction to a test date");
        PaymentsTotalObject payments = new PaymentsTotalObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getPreviousDayTimestampYearMonthDay(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.payments_total", payments);
        Allure.step("add to DB cashflow transaction to a test date ");
        dpAndWdByChannelObject transactionCashFlow = new dpAndWdByChannelObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getPreviousDayTimestampYearMonthDay(), "Deposit", "TestBank Transfers", "Bank Transfers", 40.1, 6, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.dp_and_wd_by_channel", transactionCashFlow);
        Allure.step("add to DB transaction to a date to a date outside of test period");
        PaymentsTotalObject payments1 = new PaymentsTotalObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getPrevious90DaysTimestampYearMonthDay(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.payments_total", payments1);
        Allure.step("add to DB cashflow transaction to a date outside of test period");
        dpAndWdByChannelObject transactionCashFlow1 = new dpAndWdByChannelObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getPrevious90DaysTimestampYearMonthDay(), "Deposit", "TestBank Transfers", "Bank Transfers", 40.1, 6, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.dp_and_wd_by_channel", transactionCashFlow1);
        page.reload();
        operationsPage.navigateOperationsTab(testUserUcid);
        Allure.step("filter test date");
        operationsPage.selectDateFilter("Last 90 days");
        Allure.step("check that only data for the test date is displayed");
        operationsPage.hoverOverFinancialTransactionsGraphByDateSingleDay(getPreviousDayMonthDay());
        operationsPage.checkFinancialTransactionsRowInTooltip("Deposit", String.valueOf(Math.round(payments.totalDepositUsd)));
        operationsPage.checkFinancialTransactionsRowInTooltip("Withdrawal", String.valueOf(Math.round(payments.totalWithdrawalUsd)));
        operationsPage.checkFinancialTransactionsRowInTooltip("Credit", String.valueOf(Math.round(payments.totalCreditsUsd)));
        operationsPage.checkCashflowTopPaymentSystemTypesHeaderDeposit(transactionCashFlow.psCategory, String.valueOf(Math.round(transactionCashFlow.totalAmountUsd)));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("642")
    @DisplayName("Operations tab. User can manipulate timeline by click to a half of timeline")
    public void manipulateTimelineByClickTest() throws Exception {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        operationsPage.navigateOperationsTab(testUserUcid);
        operationsPage.clickOnPreLastTimelineSection();
        operationsPage.checkLastTimelineSectionInactive();
        operationsPage.clickOnTimelineSectionByIndex(1);
        operationsPage.checkTimelineSectionInactive(0);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("641")
    @DisplayName("Operations tab. User can manipulate timeline by drag")
    public void manipulateTimelineByDragTest() {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        operationsPage.navigateOperationsTab(testUserUcid);
        operationsPage.shiftRightTimelineThumbToPreLastTimelineSection();
        operationsPage.checkLastTimelineSectionInactive();
        operationsPage.shiftLeftTimelineThumbToTimelineSectionIndex(2);
        operationsPage.checkTimelineSectionInactive(0);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("636")
    @DisplayName("Operations tab. When user filters 1-7 days one division on timeline is 1 day with date under each section")
    public void filterLegend1And7DaysTest() {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        operationsPage.navigateOperationsTab(testUserUcid);
        Allure.step("filter one day");
        operationsPage.selectDatesInCalendar(getCurrentDate(), getCurrentDate());
        operationsPage.checkTimelineSectionVisibleByDate(getCurrentDateMonthDay());
        page.reload();
        Allure.step("filter seven days");
        operationsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(6));
        operationsPage.checkTimelineSectionVisibleByDate(getCurrentDateMonthDay());
        operationsPage.checkTimelineSectionVisibleByDate(getPreviousDayMonthDayByIntDay(6));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("637")
    @DisplayName("Operations tab. When user filters 8-30 days one division on timeline is 1 day with date for every two days")
    public void filterLegend8And31DaysTest() {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        operationsPage.navigateOperationsTab(testUserUcid);
        Allure.step("filter 8 day");
        operationsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(7));
        operationsPage.checkTimelineSectionVisibleByDate(getPreviousDayMonthDayByIntDay(7));
        operationsPage.checkTimelineSectionVisibleByDate(getPreviousDayMonthDayByIntDay(1));
        page.reload();
        Allure.step("filter 30 days");
        operationsPage.selectDatesInCalendar(getPreviousDayByIntDaysYearMonthDay(29), getCurrentDate());
        operationsPage.checkTimelineSectionVisibleByDate(getPreviousDayMonthDayByIntDay(1));
        operationsPage.checkTimelineSectionVisibleByDate(getPreviousDayMonthDayByIntDay(29));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("638")
    @DisplayName("Operations tab. When user filters 31-98 days one division on timeline is 1 week with legend for every section")
    public void filterLegend31And98DaysTest() {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        operationsPage.navigateOperationsTab(testUserUcid);
        Allure.step("filter 31 day");
        operationsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(30));
        operationsPage.checkTimelineSectionVisibleByDate(getPreviousDayMonthDayByIntDay(30));
        operationsPage.checkTimelineSectionVisibleByDate(getPreviousDayMonthDayByIntDay(2));
        page.reload();
        Allure.step("filter 98 days");
        operationsPage.selectDatesInCalendar(getPreviousDayByIntDaysYearMonthDay(97), getCurrentDate());
        operationsPage.checkTimelineSectionVisibleByDate(getPreviousDayMonthDayByIntDay(6));
        operationsPage.checkTimelineSectionVisibleByDate(getPreviousDayMonthDayByIntDay(97));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("639")
    @DisplayName("Operations tab. When user filters 99 days - 3 years one division on timeline is month with legend for every two months")
    public void filterLegend98DaysAnd3YearTest() {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        operationsPage.navigateOperationsTab(testUserUcid);
        Allure.step("filter 99 days");
        operationsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(98));
        operationsPage.checkTimelineSectionVisibleByDate(getPreviousDateMonthYearIntMonth(1));
        operationsPage.checkTimelineSectionVisibleByDate(getPreviousDateMonthYearIntMonth(3));
        page.reload();
        Allure.step("filter 3 years");
        operationsPage.selectDatesInCalendar(getPreviousYearByIntYearMonthDayMinus1day(2), getCurrentDate());
        operationsPage.checkTimelineSectionVisibleByDate(getCurrentDateMonthYear());
        operationsPage.checkTimelineSectionVisibleByDate(getPreviousDateMonthYearIntYears(2));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("640")
    @DisplayName("Operations tab. When user filters 3+ years division on timeline is 1 year with legend for every year")
    public void filterLegend3YearsTest() {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        operationsPage.navigateOperationsTab(testUserUcid);
        Allure.step("filter 3 years");
        operationsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDateByIntYearMonthDay(3));
        operationsPage.checkTimelineSectionVisibleByDate(getPreviousYearByInt(3));
        operationsPage.checkTimelineSectionVisibleByDate(getCurrentYear());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("635")
    @DisplayName("Operations tab. When user uses timeline , when user filters 6 days must have 1 inactive day on the right.")
    public void timelineInactiveDaysFilter6DaysTest() {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        operationsPage.navigateOperationsTab(testUserUcid);
        Allure.step("filter 6 days");
        operationsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(5));
        operationsPage.checkTimelineSectionInactive(6);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("634")
    @DisplayName("Operations tab. When user uses timeline , when user filters 5 days must have 1 inactive day on both sides")
    public void timelineInactiveDaysFilter5DaysTest() {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        operationsPage.navigateOperationsTab(testUserUcid);
        Allure.step("filter 5 days");
        operationsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(4));
        operationsPage.checkTimelineSectionInactive(6);
        operationsPage.checkTimelineSectionInactive(0);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("633")
    @DisplayName("Operations tab. When user uses timeline , when user filters 4 days must have 1 inactive day on the left and 2 on the right.")
    public void timelineInactiveDaysFilter4DaysTest() {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        operationsPage.navigateOperationsTab(testUserUcid);
        Allure.step("filter 4 days");
        operationsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(3));
        operationsPage.checkTimelineSectionInactive(6);
        operationsPage.checkTimelineSectionInactive(5);
        operationsPage.checkTimelineSectionInactive(0);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("632")
    @DisplayName("Operations tab. When user uses timeline , when user filters three days must have 2 inactive days on both sides")
    public void timelineInactiveDaysFilter3DaysTest() {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        operationsPage.navigateOperationsTab(testUserUcid);
        Allure.step("filter 3 days");
        operationsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(2));
        operationsPage.checkTimelineSectionInactive(6);
        operationsPage.checkTimelineSectionInactive(5);
        operationsPage.checkTimelineSectionInactive(0);
        operationsPage.checkTimelineSectionInactive(1);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("631")
    @DisplayName("Operations tab. When user uses timeline , when user filters two days must have 2 inactive days on the left and 3 on the right")
    public void timelineInactiveDaysFilter2DaysTest() {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        operationsPage.navigateOperationsTab(testUserUcid);
        Allure.step("filter 2 days");
        operationsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(1));
        operationsPage.checkTimelineSectionInactive(6);
        operationsPage.checkTimelineSectionInactive(5);
        operationsPage.checkTimelineSectionInactive(4);
        operationsPage.checkTimelineSectionInactive(0);
        operationsPage.checkTimelineSectionInactive(1);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("630")
    @DisplayName("Operations tab. When user uses timeline , when user filters one day must have 3 inactive days on both sides")
    public void timelineInactiveDaysFilter1DayTest() {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        operationsPage.navigateOperationsTab(testUserUcid);
        Allure.step("filter 1 day");
        operationsPage.selectDatesInCalendar(getCurrentDate(), getCurrentDate());
        operationsPage.checkTimelineSectionInactive(6);
        operationsPage.checkTimelineSectionInactive(5);
        operationsPage.checkTimelineSectionInactive(4);
        operationsPage.checkTimelineSectionInactive(0);
        operationsPage.checkTimelineSectionInactive(1);
        operationsPage.checkTimelineSectionInactive(2);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("658")
    @DisplayName("Operations tab. Financial transaction graph, when filtered 99 days - 9 months Division = months Timeline = every month")
    public void filterLegendFinancialTransaction99DaysAnd10monthsTest() throws Exception {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        Allure.step("add to DB transaction in a testing interval so financial transaction graph will be visible");
        cleanUserFinancialTransactionDbUcid(testUserUcid);
        PaymentsTotalObject payments = new PaymentsTotalObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getPreviousDayTimestampYearMonthDay(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.payments_total", payments);
        operationsPage.navigateOperationsTab(testUserUcid);
        Allure.step("filter 99 days");
        operationsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(98));
        operationsPage.checkFinancialTransactionSectionVisibleByDate(Utils.getCurrentDateMonthYear());
        operationsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDateMonthYearIntMonth(3));
        page.reload();
        Allure.step("filter 98 days");
        operationsPage.selectDatesInCalendar(getPreviousDayTimestampYearMonthDayByIntMonthMinus1Day(8), getCurrentDate());
        operationsPage.checkFinancialTransactionSectionVisibleByDate(Utils.getCurrentDateMonthYear());
        operationsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDateMonthYearIntMonth(3));
        operationsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDateMonthYearIntMonth(8));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("701")
    @DisplayName("Operations tab. Financial transaction graph, when filtered 10 months - 20 months Division = months Timeline = every 4 month")
    public void filterLegendFinancialTransaction10monthsAnd20monthsTest() throws Exception {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        Allure.step("add to DB transaction in a testing interval so financial transaction graph will be visible");
        cleanUserFinancialTransactionDbUcid(testUserUcid);
        PaymentsTotalObject payments = new PaymentsTotalObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getPreviousDayTimestampYearMonthDay(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.payments_total", payments);
        operationsPage.navigateOperationsTab(testUserUcid);
        Allure.step("filter 10 months");
        operationsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDateYearMonthDayByIntMonthMinus1Day(10));
        operationsPage.checkFinancialTransactionSectionVisibleByDate(Utils.getPreviousDateMonthYearIntMonth(1));
        operationsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDateMonthYearIntMonth(4));
        operationsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDateMonthYearIntMonth(7));
        operationsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDateMonthYearIntMonth(10));
        page.reload();
        Allure.step("filter 20 months");
        operationsPage.selectDatesInCalendar(getPreviousDateYearMonthDayByIntMonthMinus1Day(19), getCurrentDate());
        operationsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDateMonthYearIntMonth(1));
        operationsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDateMonthYearIntMonth(4));
        operationsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDateMonthYearIntMonth(7));
        operationsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDateMonthYearIntMonth(10));
        operationsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDateMonthYearIntMonth(19));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("655")
    @DisplayName("Operations tab. Financial transaction graph, when filtered 1-14 days Division = 1 day Timeline = every day")
    public void filterLegendFinancialTransaction1DayAnd14DaysTest() throws Exception {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        Allure.step("add to DB transaction in a testing interval so financial transaction graph will be visible");
        cleanUserFinancialTransactionDbUcid(testUserUcid);
        PaymentsTotalObject payments = new PaymentsTotalObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.payments_total", payments);
        operationsPage.navigateOperationsTab(testUserUcid);
        Allure.step("filter 1 day");
        operationsPage.selectDatesInCalendar(getCurrentDate(), getCurrentDate());
        operationsPage.checkFinancialTransactionSectionVisibleByDate(Utils.getCurrentDateMonthDay());
        page.reload();
        Allure.step("filter 14 days");
        operationsPage.selectDatesInCalendar(getPreviousDayByIntDaysYearMonthDay(13), getCurrentDate());
        operationsPage.checkFinancialTransactionSectionVisibleByDate(Utils.getCurrentDateMonthDay());
        operationsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDayMonthDayByIntDay(13));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("656")
    @DisplayName("Operations tab. Financial transaction graph, when filtered 15-20 days Division = 1 day Timeline = every 4 day")
    public void filterLegendFinancialTransaction15DaysAnd20DaysTest() throws Exception {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        Allure.step("add to DB transaction in a testing interval so financial transaction graph will be visible");
        cleanUserFinancialTransactionDbUcid(testUserUcid);
        PaymentsTotalObject payments = new PaymentsTotalObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.payments_total", payments);
        operationsPage.navigateOperationsTab(testUserUcid);
        Allure.step("filter 15 days");
        operationsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(14));
        operationsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDayMonthDayByIntDay(2));
        operationsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDayMonthDayByIntDay(14));
        page.reload();
        Allure.step("filter 20 days");
        operationsPage.selectDatesInCalendar(getPreviousDayByIntDaysYearMonthDay(19), getCurrentDate());
        operationsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDayMonthDayByIntDay(3));
        operationsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDayMonthDayByIntDay(19));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("702")
    @DisplayName("Operations tab. Financial transaction graph, when filtered 20+ months Division = years Timeline = every year")
    public void filterLegendFinancialTransactionMoreThan20monthsDaysTest() throws Exception {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        Allure.step("add to DB transaction in a testing interval so financial transaction graph will be visible");
        cleanUserFinancialTransactionDbUcid(testUserUcid);
        PaymentsTotalObject payments = new PaymentsTotalObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.payments_total", payments);
        operationsPage.navigateOperationsTab(testUserUcid);
        Allure.step("filter 20 months ");
        operationsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDateYearMonthDayByIntMonthMinus1Day(21));
        operationsPage.checkFinancialTransactionSectionVisibleByDate(getCurrentYear());
        operationsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousYearByInt(1));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("657")
    @DisplayName("Operations tab. Financial transaction graph, when filtered 21-98 days Division = week Timeline = every week")
    public void filterLegendFinancialTransaction21DaysAnd98DaysTest() throws Exception {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        Allure.step("add to DB transaction in a testing interval so financial transaction graph will be visible");
        cleanUserFinancialTransactionDbUcid(testUserUcid);
        PaymentsTotalObject payments = new PaymentsTotalObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getPreviousDayTimestampYearMonthDay(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.payments_total", payments);
        operationsPage.navigateOperationsTab(testUserUcid);
        Allure.step("filter 21 days");
        operationsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(20));
        operationsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDayMonthDayByIntDay(6));
        operationsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDayMonthDayByIntDay(20));
        page.reload();
        Allure.step("filter 98 days");
        operationsPage.selectDatesInCalendar(getPreviousDayByIntDaysYearMonthDay(97), getCurrentDate());
        operationsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDayMonthDayByIntDay(6));
        operationsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDayMonthDayByIntDay(97));
    }

}
