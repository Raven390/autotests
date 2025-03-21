package business_objects.db.clickhouse.s3_fact_cpa_commissions;

import helpers.data.ClientHelper;


import static utils.Utils.*;

public class S3FactCpaCommissionsFactory {
    public static S3FactCpaCommissionsObject generates3FactCpaCommissionsObject(ClientHelper client) {
        S3FactCpaCommissionsObject commission = new S3FactCpaCommissionsObject();
        commission.id = ((long) getRandomIntPositive());
        commission.date = getCurrentDate();
        commission.brandUid = 1;
        commission.brand = client.getBrand();
        commission.regulator = client.getRegulator();
        commission.userId = client.getUserId();
        commission.ucid = client.getUcid();
        commission.cpaId = client.getCpaId();
        commission.traderId = "sampleTraderId";
        commission.afp = "sampleApf";
        commission.tradingCode = "sampleTradingCode";
        commission.commissionType = "sampleCommissionTypes";
        commission.commission = getRandomRoundedDouble(0.01, 99_999_999.99);
        commission.partitionBrand = "samplePartitionBrand";
        commission.dlInsertTs = getCurrentTimestampDbFormat();
        commission.dlUpdateTs = getCurrentTimestampDbFormat();

        return commission;
    }
}
