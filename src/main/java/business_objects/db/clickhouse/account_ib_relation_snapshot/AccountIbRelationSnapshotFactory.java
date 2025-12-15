package business_objects.db.clickhouse.account_ib_relation_snapshot;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import helpers.data.ClientHelper;
import helpers.data.enums.DateTimeFormat;

import static utils.Utils.getCurrentTimestampDbFormat;
import static utils.Utils.getCurrentTimestampMinusOffsetFormatted;

public class AccountIbRelationSnapshotFactory {
    public static AccountIbRelationSnapshotObject generateAccountIbRelationSnapshotObjectByClient(ClientHelper client) {
        AccountIbRelationSnapshotObject relation = new AccountIbRelationSnapshotObject();
        relation.setUserId(client.getUserId());
        relation.setBrand(client.getBrand());
        relation.setRegulator(client.getRegulator());
        relation.setUcid(client.getUcid());
        relation.setAccount(client.getTradingAccount().longValue());
        relation.setServerId(client.getServerId());
        relation.setRecordEffectiveStartDate(getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, 1, 0, 0, 0, 0));
        relation.setRecordEffectiveEndDate(getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, -1, 0, 0, 0, 0));
        relation.setRecordActiveFlag(true);
        relation.setLastUpdated(getCurrentTimestampDbFormat());
        relation.setRecordDeletedFlag("N");
        relation.setLastUpdated(getCurrentTimestampDbFormat());
        relation.setIsRebateAccount(1);
        relation.setIsDel(0);
        return relation;
    }

    public static AccountIbRelationSnapshotObject generateAccountIbRelationSnapshotObjectByAccounts(
            CrmTbAccountObject account, CrmTbAccountObject accountIb) {
        AccountIbRelationSnapshotObject relation = new AccountIbRelationSnapshotObject();
        relation.setUserId(account.userId);
        relation.setBrand(account.brand);
        relation.setRegulator(account.regulator);
        relation.setUcid(account.ucid);
        relation.setAccount(Long.valueOf(account.account));
        relation.setServerId(account.serverIdSt);
        relation.setServerName(account.serverName);
        relation.setDirectIb(accountIb.userId);
        relation.setDirectIbLevel(1);
        relation.setDirectIbRebateAccount(accountIb.account);
        relation.setMasterIb(0);
        relation.setMasterIbRebateAccount(0);
        relation.setRecordEffectiveStartDate("2020-01-01");
        relation.setRecordEffectiveEndDate("2040-12-31");
        relation.setRecordActiveFlag(true);
        relation.setIsDel(0);
        relation.setRecordDeletedFlag("N");
        relation.setLastUpdated(getCurrentTimestampDbFormat());
        relation.setIsRebateAccount(1);
        return relation;
    }
}
