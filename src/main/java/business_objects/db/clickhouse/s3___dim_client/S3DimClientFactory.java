package business_objects.db.clickhouse.s3___dim_client;

import helpers.data.ClientHelper;
import helpers.data.enums.DateTimeFormat;
import net.datafaker.Faker;

import static utils.Utils.*;

public class S3DimClientFactory {

    static Faker faker = new Faker();

    public static S3DimClientObject generateS3DimClientObjectRandom() {
        S3DimClientObject manager = new S3DimClientObject();
        manager.setUserId(getRandomLongPositive());
        manager.setOrgName(faker.finalFantasyXIV().job());
        manager.setUserName(faker.touhou().characterName());
        manager.setRecordActiveFlag(1);
        return manager;
    }

    public static S3DimClientObject generateS3DimClientObject(ClientHelper client, int salesID) {
        S3DimClientObject manager = new S3DimClientObject();
        manager.setUserId(Long.valueOf(salesID));
        manager.setBrand(client.getBrand());
        manager.setUcid(client.getBrand().toLowerCase() + "-" + salesID);
        manager.setOrgName(faker.finalFantasyXIV().job());
        manager.setUserName(faker.touhou().characterName());
        manager.setRecordActiveFlag(1);
        manager.setRecordVersion(1.0f);
        manager.setLast_process_date(getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, 0, 0, 0, 0, 0));
        return manager;
    }
}
