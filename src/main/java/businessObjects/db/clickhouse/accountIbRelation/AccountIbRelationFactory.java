package businessObjects.db.clickhouse.accountIbRelation;

import helpers.data.ClientHelper;

import static utils.Utils.getCurrentTimestampDbFormat;

public class AccountIbRelationFactory {
    public static AccountIbRelationObject generateAccountIbRelationObjectByClient(ClientHelper client) {
        AccountIbRelationObject relation = new AccountIbRelationObject();
        relation.setSourceIdSt(client.getServerId());
        relation.setUserId(client.getUserId());
        relation.setAccount(client.getTradingAccount().longValue());
        relation.setRegulator(client.getRegulator());
        relation.setBrand(client.getBrand());
        relation.setUcid(client.getUcid());
        relation.setServerId(client.getServerId());
        relation.setRecordActiveFlag(true);
        relation.setCreateTime(getCurrentTimestampDbFormat());
        relation.setCreateTimeUtc(getCurrentTimestampDbFormat());
        relation.setLastUpdated(getCurrentTimestampDbFormat());
        return relation;
    }
}
