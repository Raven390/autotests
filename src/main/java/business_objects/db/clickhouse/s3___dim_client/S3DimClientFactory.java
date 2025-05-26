package business_objects.db.clickhouse.s3___dim_client;

import net.datafaker.Faker;

import static utils.Utils.*;

public class S3DimClientFactory {

    static Faker faker = new Faker();

    public static S3DimClientObject generateS3DimClientObject() {
        S3DimClientObject manager = new S3DimClientObject();
        manager.setUserId(getRandomLongPositive());
        manager.setOrgName(faker.finalFantasyXIV().job());
        manager.setUserName(faker.touhou().characterName());
        manager.setRecordActiveFlag(1);
        return manager;
    }
}
