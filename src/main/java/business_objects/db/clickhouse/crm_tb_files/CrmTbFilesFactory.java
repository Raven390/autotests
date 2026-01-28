package business_objects.db.clickhouse.crm_tb_files;

import static utils.Constants.*;
import static utils.Utils.getRandomLongPositive;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;
import java.math.BigInteger;
import java.time.OffsetDateTime;
import net.datafaker.Faker;

public class CrmTbFilesFactory {
    @Step("Generate crm___tb_files object by client")
    public static CrmTbFilesEntry generateCrmTbFilesByClient(ClientHelper client) {
        var now = OffsetDateTime.now();
        Faker faker = new Faker();
        String ip = faker.internet().ipV4Address();
        return CrmTbFilesEntry.builder()
                .sourceIdSt(client.getServerId())
                .brandUid(1)
                .brand(client.getBrand())
                .regulator(client.getRegulator())
                .userId((long) client.getUserId())
                .category(3)
                .ucid(client.getUcid())
                .id(BigInteger.valueOf(getRandomLongPositive()))
                .fileName(FILE_KYC_NAME)
                .filePath(FILE_KYC_PATH)
                .uploadTime(now)
                .uploadTimeUtc(now)
                .updateTime(now)
                .updateTimeUtc(now)
                .isDel(0)
                .lastUpdated(now)
                .updateUser(client.getUserId().toString())
                .updateIp(ip)
                .uploadIp(ip)
                .build();
    }
}
