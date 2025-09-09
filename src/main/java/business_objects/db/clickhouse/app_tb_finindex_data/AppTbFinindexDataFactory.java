package business_objects.db.clickhouse.app_tb_finindex_data;

import net.datafaker.Faker;

import static utils.Utils.getCurrentTimestampDbFormat;
import static utils.Utils.getRandomIntPositive;

public class AppTbFinindexDataFactory {
    static Faker faker = new Faker();

    public static AppTbFinindexData generateAppFinindexData() {
        AppTbFinindexData newsObject = new AppTbFinindexData();
        newsObject.setCountryCode("US");
        newsObject.setImportance("high");
        newsObject.setBrandUid(1);
        newsObject.setBrand("Vantage");
        newsObject.setId(getRandomIntPositive().longValue());
        newsObject.setDataId(getRandomIntPositive().longValue());
        newsObject.setDataName(faker.starTrek().character());
        newsObject.setLanguage("EN");
        newsObject.setTitle(faker.movie().quote());
        newsObject.setPublishTime(getCurrentTimestampDbFormat());
        newsObject.setDescription(faker.movie().quote());
        newsObject.setCreateTime(getCurrentTimestampDbFormat());
        newsObject.setUpdateTime(getCurrentTimestampDbFormat());
        newsObject.setLastUpdated(getCurrentTimestampDbFormat());
        return newsObject;
    }

    public static AppTbFinindexData generateAppFinindexData(String time) {
        AppTbFinindexData newsObject = generateAppFinindexData();
        newsObject.setPublishTime(time);
        newsObject.setCreateTime(time);
        newsObject.setUpdateTime(time);
        newsObject.setLastUpdated(time);
        return newsObject;
    }
}
