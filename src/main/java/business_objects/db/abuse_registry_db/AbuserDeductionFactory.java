package business_objects.db.abuse_registry_db;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;

import java.sql.Timestamp;
import java.time.Instant;

import static helpers.data.enums.deduction.DeductionStatusApproval.APPROVED;
import static helpers.data.enums.deduction.DeductionStatusDeduction.FAILED;
import static helpers.data.enums.deduction.DeductionStatusEmail.SENT;
import static helpers.data.enums.deduction.DeductionStatusOpenPositions.NOT_HOLDING;
import static helpers.data.enums.deduction.DeductionTypeAccount.ILLEGAL_PROFIT;
import static utils.ConfigFactory.FIRST_NAME_AUTOTEST_ONE;
import static utils.ConfigFactory.LAST_NAME_AUTOTEST_ONE;
import static utils.Constants.*;

public class AbuserDeductionFactory {

    private AbuserDeductionFactory() {
    }

    public static AbuserDeduction generateAbuserDeductionByAccount(CrmTbAccountObject account,
            Integer abuserHistoryId) {
        return new AbuserDeduction(account.ucid, abuserHistoryId, account.account.toString(), account.serverIdSt, account.serverName, account.currency, account.brand, NOT_HOLDING.getDisplayName(), SENT.getDisplayName(), FAILED.getDisplayName(), APPROVED.getDisplayName(), "Automation deduction comment", 10_345.678, 12_345.6, 6346.24, 7400.7, 2346.24, 3400.7, Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), String.format("%s %s", FIRST_NAME_AUTOTEST_ONE, LAST_NAME_AUTOTEST_ONE), VINDEX_BO_SYSTEM, ILLEGAL_PROFIT.getDisplayName());
    }
}