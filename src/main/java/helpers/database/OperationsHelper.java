package helpers.database;

import io.qameta.allure.Allure;


import static helpers.database.DbHelper.deleteEntryFromDb;

public class OperationsHelper {

    public static void cleanUserCashflowDb(String ucid) throws Exception {
        Allure.step("delete user's cashflow transactions from DB");
        try {
            deleteEntryFromDb("vindex_test.dp_and_wd_by_channel", "ucid = '" + ucid + "'");
            Thread.sleep(100);
        } catch (Exception NoSuchElementException) {
            System.out.println("No such records with provided ucid");
        }
    }

    public static void cleanUserFinancialTransactionDbUcid(String ucid) throws Exception {
        Allure.step("delete user's with financial transactions transactions from DB");
        try {
            deleteEntryFromDb("vindex_test.payments_total", "ucid = '" + ucid + "'");
            Thread.sleep(100);
        } catch (Exception NoSuchElementException) {
            System.out.println("No such records with provided ucid");
        }
    }

    public static void cleanUserCashflowDbDeposit(String ucid) throws Exception {
        Allure.step("delete user's cashflow Deposit  transactions from DB");
        try {
            deleteEntryFromDb("vindex_test.dp_and_wd_by_channel", "ucid = '" + ucid + "' AND transfer_type = 'Deposit'");
            Thread.sleep(100);
        } catch (Exception NoSuchElementException) {
            System.out.println("No such Deposit records with provided ucid");
        }
    }

    public static void cleanUserCashflowDbWithdrawal(String ucid) throws Exception {
        Allure.step("delete user's cashflow Withdrawal  transactions from DB");
        try {
            deleteEntryFromDb(DbName.BO, "vindex_test.dp_and_wd_by_channel", "ucid = '" + ucid + "' AND transfer_type = 'Withdrawal'");
            Thread.sleep(100);
        } catch (Exception NoSuchElementException) {
            System.out.println("No such Withdrawal records with provided ucid");
        }
    }
}
