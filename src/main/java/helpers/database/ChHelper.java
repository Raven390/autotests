package helpers.database;

import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntity;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntity;

public class ChHelper {

    public static double calculateWithdrawalsValue(CrmTbWithdrawalEntity... withdrawal) {
        double result = 0;
        for (CrmTbWithdrawalEntity w : withdrawal) {
            result += (w.getAmountUsd().subtract(w.getReversedAmountUsd())).doubleValue();
        }
        return result;
    }

    public static double calculateDepositValue(CrmTbDepositEntity... deposit) {
        double result = 0;
        for (CrmTbDepositEntity d : deposit) {
            result += d.getAmountUsd().doubleValue();
        }
        return result;
    }
}
