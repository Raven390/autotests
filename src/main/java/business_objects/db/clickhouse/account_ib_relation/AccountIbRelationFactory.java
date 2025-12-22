package business_objects.db.clickhouse.account_ib_relation;

import static utils.Utils.*;

import helpers.data.ClientHelper;
import helpers.data.enums.DateTimeFormat;

public class AccountIbRelationFactory {
    public static AccountIbRelationObject generateAccountIbRelationObjectByClient(ClientHelper client) {
        AccountIbRelationObject relation = new AccountIbRelationObject();
        relation.setUserId(client.getUserId());
        relation.setBrand(client.getBrand());
        relation.setDirectIbRebateAccount(client.getIbId() + 1212);
        relation.setRegulator(client.getRegulator());
        relation.setUcid(client.getUcid());
        relation.setAccount(client.getTradingAccount().longValue());
        relation.setServerId(client.getServerId());
        relation.setRecordEffectiveStartDate(
                getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, 1, 0, 0, 0, 0));
        relation.setRecordEffectiveEndDate(
                getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, -1, 0, 0, 0, 0));
        relation.setRecordActiveFlag(true);
        relation.setCreateTime(getCurrentTimestampDbFormat());
        relation.setCreateTimeUtc(getCurrentTimestampDbFormat());
        relation.setLastUpdated(getCurrentTimestampDbFormat());
        relation.setSalesId(getRandomIntPositive());
        return relation;
    }

    public static AccountIbRelationObject generateAccountIbRelationObjectByClientAdditional(ClientHelper client) {
        AccountIbRelationObject relation = new AccountIbRelationObject();
        relation.setUserId(client.getUserId());
        relation.setBrand(client.getBrand());
        relation.setDirectIbRebateAccount(client.getIbId() + 1212);
        relation.setRegulator(client.getRegulator());
        relation.setUcid(client.getUcid());
        relation.setAccount(client.getTradingAccount2().longValue());
        relation.setServerId(client.getServerId());
        relation.setRecordEffectiveStartDate(
                getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, 1, 0, 0, 0, 0));
        relation.setRecordEffectiveEndDate(
                getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, -1, 0, 0, 0, 0));
        relation.setRecordActiveFlag(true);
        relation.setCreateTime(getCurrentTimestampDbFormat());
        relation.setCreateTimeUtc(getCurrentTimestampDbFormat());
        relation.setLastUpdated(getCurrentTimestampDbFormat());
        return relation;
    }
}
