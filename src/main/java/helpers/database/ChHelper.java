package helpers.database;

import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositObject;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalObject;


public class ChHelper {

    public static double calculateWithdrawalsValue(CrmTbWithdrawalObject... withdrawal) {
        double result = 0;
        for (CrmTbWithdrawalObject w : withdrawal) {
            result += (w.amountUsd - w.reversedAmountUsd);
        }
        return result;
    }

    public static double calculateDepositValue(CrmTbDepositObject... deposit) {
        double result = 0;
        for (CrmTbDepositObject d : deposit) {
            result += d.amountUsd;
        }
        return result;
    }
}
