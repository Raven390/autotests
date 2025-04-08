package business_objects.db.clickhouse.account_ib_relation_snapshot;

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
}
