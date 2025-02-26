package businessObjects.db.clickhouse.accountIbRelation;

import helpers.data.ClientHelper;
import helpers.data.enums.DateTimeFormat;

import static utils.Utils.getCurrentTimestampDbFormat;
import static utils.Utils.getCurrentTimestampMinusOffsetFormatted;

public class AccountIbRelationFactory {
    public static AccountIbRelationObject generateAccountIbRelationObjectByClient(ClientHelper client) {
        AccountIbRelationObject relation = new AccountIbRelationObject();
        relation.setUserId(client.getUserId());
        relation.setBrand(client.getBrand());
        relation.setRegulator(client.getRegulator());
        relation.setUcid(client.getUcid());
        relation.setAccount(client.getTradingAccount().longValue());
        relation.setServerId(client.getServerId());
        relation.setRecordEffectiveStartDate(getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, 1, 0, 0, 0, 0));
        relation.setRecordEffectiveEndDate(getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, -1, 0, 0, 0, 0));
        relation.setRecordActiveFlag(true);
        relation.setCreateTime(getCurrentTimestampDbFormat());
        relation.setCreateTimeUtc(getCurrentTimestampDbFormat());
        relation.setLastUpdated(getCurrentTimestampDbFormat());
        return relation;
    }
}
