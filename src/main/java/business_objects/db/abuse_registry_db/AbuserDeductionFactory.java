package business_objects.db.abuse_registry_db;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;

import java.sql.Timestamp;
import java.time.Instant;

import static helpers.data.enums.deduction.DeductionStatusApproval.APPROVED;
import static helpers.data.enums.deduction.DeductionStatusDeduction.FAILED;
import static helpers.data.enums.deduction.DeductionStatusEmail.SENT;
import static helpers.data.enums.deduction.DeductionStatusOpenPositions.NOT_HOLDING;
import static helpers.data.enums.deduction.DeductionType.FULL_DEDUCTION;
import static helpers.data.enums.deduction.DeductionTypeAccount.ILLEGAL_PROFIT;
import static utils.ConfigFactory.FIRST_NAME_AUTOTEST_ONE;
import static utils.ConfigFactory.LAST_NAME_AUTOTEST_ONE;
import static utils.Constants.*;

public class AbuserDeductionFactory {

    private AbuserDeductionFactory() {
    }

    public static AbuserDeduction generateAbuserDeductionByAccount(CrmTbAccountObject account,
            Integer abuserHistoryId) {
        AbuserDeduction deduction = new AbuserDeduction();
        deduction.setUcid(account.ucid);
        deduction.setAbuserHistoryId(abuserHistoryId);
        deduction.setAccount(account.account.toString());
        deduction.setServerId(account.serverIdSt);
        deduction.setServerName(account.serverName);
        deduction.setCurrency(account.currency);
        deduction.setBrandGroup(account.brand);
        deduction.setStatusOpenPositions(NOT_HOLDING.getDisplayName());
        deduction.setStatusEmail(SENT.getDisplayName());
        deduction.setStatusDeduction(FAILED.getDisplayName());
        deduction.setStatusApproval(APPROVED.getDisplayName());
        deduction.setComment("Automation deduction comment");
        deduction.setIllegalProfit(10_345.678);
        deduction.setIllegalProfitUsd(12_345.6);
        deduction.setSuggestedDeduction(6346.24);
        deduction.setSuggestedDeductionUsd(7400.7);
        deduction.setActualDeduction(2346.24);
        deduction.setActualDeductionUsd(3400.7);
        deduction.setCreatedAt(Timestamp.from(Instant.now()));
        deduction.setUpdatedAt(Timestamp.from(Instant.now()));
        deduction.setModifiedByUser(String.format("%s %s", FIRST_NAME_AUTOTEST_ONE, LAST_NAME_AUTOTEST_ONE));
        deduction.setModifiedBySystem(VINDEX_BO_SYSTEM);
        deduction.setTypeAccount(ILLEGAL_PROFIT.getDisplayName());
        deduction.setDeductionDate(null);
        deduction.setApprovedDeduction(0.0);
        deduction.setApprovedDeductionUsd(0.0);
        deduction.setCrmId(account.userId.toString());
        deduction.setSendToLark(false);
        deduction.setCommentDeduction("Automation deduction comment batch");
        deduction.setDeductionType(FULL_DEDUCTION.getDisplayName());
        return deduction;
    }
}