package helpers.database;

import io.qameta.allure.Allure;


import java.util.logging.Logger;

import static helpers.database.DbHelper.*;
import static utils.Constants.*;

public class OperationsHelper {

    static Logger logger = Logger.getLogger(OperationsHelper.class.getName());

    public static void cleanUserPaymentsDb(String ucid) throws Exception {
        Allure.step("delete user's cashflow transactions from DB");

        deleteEntryFromDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, "ucid = '" + ucid + "'");
        logger.info("withdrawals deleted");
        deleteEntryFromDb(CRM_DEPOSIT_TABLE_NAME, "ucid = '" + ucid + "'");
        logger.info("deposits deleted");
        deleteEntryFromDb(CRM_TRANSFERS_TABLE_NAME, "ucid = '" + ucid + "'");
        logger.info("transfers deleted");
        deleteEntryFromDb(MT_CREDITS_TABLE_NAME, "ucid = '" + ucid + "'");
        logger.info("credits deleted");
//        ClientHelper dummy = new ClientHelper(1, "e5880ca5-8578-4a1e-969d-7a64716ca41f", Brand.INFINOX, Regulator.FCA, 1001, 1002, 1);
//        deleteEntryFromDb(CRM_WITHDRAWAL_TABLE_NAME, "ucid = '" + dummy.getUcid() + "'");
//        deleteEntryFromDb(CRM_DEPOSIT_TABLE_NAME, "ucid = '" + dummy.getUcid() + "'");
//        CrmTbWithdrawalObject dummyW = generateWithdrawalByClient(dummy);
//        CrmTbDepositObject dummyD = generateDepositByClient(dummy);
//        insertObjectToDb(CRM_WITHDRAWAL_TABLE_NAME, dummyW);
//        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, dummyD);
        executeQueryToDb(DbName.CLICKHOUSE, "ALTER TABLE vindex_test.dp_and_wd_by_channel\n" + "DELETE WHERE ucid='" + ucid + "'");
        logger.info("aggregation table cleared");
        Thread.sleep(100);
    }

    public static void cleanUserFinancialTransactionDbUcid(String ucid) throws Exception {
        Allure.step("delete user's with financial transactions transactions from DB");
        deleteEntryFromDb("vindex_test.payments_total", "ucid = '" + ucid + "'");
        Thread.sleep(100);

    }

    public static void cleanUserCashflowDbDeposit(String ucid) throws Exception {
        Allure.step("delete user's cashflow Deposit  transactions from DB");
        try {
            deleteEntryFromDb("vindex_test.dp_and_wd_by_channel", "ucid = '" + ucid + "' AND transfer_type = 'Deposit'");
            Thread.sleep(100);
        } catch (Exception NoSuchElementException) {
            logger.info("No such Deposit records with provided ucid");
        }
    }

    public static void cleanUserCashflowDbWithdrawal(String ucid) throws Exception {
        Allure.step("delete user's cashflow Withdrawal  transactions from DB");
        try {
            deleteEntryFromDb(DbName.BACKOFFICE, "vindex_test.dp_and_wd_by_channel", "ucid = '" + ucid + "' AND transfer_type = 'Withdrawal'");
            Thread.sleep(100);
        } catch (Exception NoSuchElementException) {
            logger.info("No such Withdrawal records with provided ucid");
        }
    }
}
