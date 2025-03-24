package helpers.database;

import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositObject;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalObject;
import helpers.data.ClientHelper;
import helpers.data.enums.Brand;
import helpers.data.enums.Regulator;
import io.qameta.allure.Allure;


import static business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositObjectFactory.generateDepositByClient;
import static business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalObjectFactory.generateWithdrawalByClient;
import static helpers.database.DbHelper.*;
import static utils.Constants.CRM_DEPOSIT_TABLE_NAME;
import static utils.Constants.CRM_WITHDRAWAL_TABLE_NAME;

public class OperationsHelper {

    public static void cleanUserCashflowDb(String ucid) throws Exception {
        Allure.step("delete user's cashflow transactions from DB");

        deleteEntryFromDb(CRM_WITHDRAWAL_TABLE_NAME, "ucid = '" + ucid + "'");
        System.out.println("withdrawals deleted");
        deleteEntryFromDb(CRM_DEPOSIT_TABLE_NAME, "ucid = '" + ucid + "'");
        System.out.println("deposits deleted");
        ClientHelper dummy = new ClientHelper(1, "e5880ca5-8578-4a1e-969d-7a64716ca41f", Brand.INFINOX, Regulator.FCA, 1001, 1002, 1);
        deleteEntryFromDb(CRM_WITHDRAWAL_TABLE_NAME, "ucid = '" + dummy.getUcid() + "'");
        deleteEntryFromDb(CRM_DEPOSIT_TABLE_NAME, "ucid = '" + dummy.getUcid() + "'");
        CrmTbWithdrawalObject dummyW = generateWithdrawalByClient(dummy);
        CrmTbDepositObject dummyD = generateDepositByClient(dummy);
        insertObjectToDb(CRM_WITHDRAWAL_TABLE_NAME, dummyW);
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, dummyD);
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
