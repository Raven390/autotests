package helpers.database;

import static helpers.database.DbHelper.*;
import static utils.Constants.*;
import static utils.Utils.writeLog;

import io.qameta.allure.Allure;

public class OperationsHelper {

    public static void cleanUserPaymentsDb(String ucid) throws Exception {
        Allure.step("delete user's cashflow transactions from DB");

        deleteEntryFromDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, "ucid = '" + ucid + "'");
        writeLog("withdrawals deleted");
        deleteEntryFromDb(CRM_DEPOSIT_TABLE_NAME, "ucid = '" + ucid + "'");
        writeLog("deposits deleted");
        deleteEntryFromDb(CRM_TRANSFERS_TABLE_NAME, "ucid = '" + ucid + "'");
        writeLog("transfers deleted");
        deleteEntryFromDb(MT_CREDITS_TABLE_NAME, "ucid = '" + ucid + "'");
        writeLog("credits deleted");
        executeQueryToDb(
                DbName.CLICKHOUSE,
                "ALTER TABLE consolidated.dp_and_wd_by_channel\n" + "DELETE WHERE ucid='" + ucid + "'");
        writeLog("aggregation table cleared");
    }

    public static void cleanUserFinancialTransactionDbUcid(String ucid) {
        Allure.step("delete user's with financial transactions transactions from DB");
        deleteEntryFromDb("consolidated.payments_total", "ucid = '" + ucid + "'");
    }

    public static void cleanUserCashflowDbDeposit(String ucid) {
        Allure.step("delete user's cashflow Deposit  transactions from DB");
        try {
            deleteEntryFromDb(
                    "consolidated.dp_and_wd_by_channel", "ucid = '" + ucid + "' AND transfer_type = 'Deposit'");
        } catch (Exception NoSuchElementException) {
            writeLog("No such Deposit records with provided ucid");
        }
    }

    public static void cleanUserCashflowDbWithdrawal(String ucid) {
        Allure.step("delete user's cashflow Withdrawal  transactions from DB");
        try {
            deleteEntryFromDb(
                    DbName.POSTGRES,
                    "consolidated.dp_and_wd_by_channel",
                    "ucid = '" + ucid + "' AND transfer_type = 'Withdrawal'");
        } catch (Exception NoSuchElementException) {
            writeLog("No such Withdrawal records with provided ucid");
        }
    }
}
