package businessObjects.db.clickhouse.crmTbUserTable;


import helpers.data.ClientHelper;
import io.qameta.allure.Step;

import static utils.Utils.*;

public class CrmTbUserObjectFactory {
    @Step("Generate user object by Client")
    public static CrmTbUserObject generateUserByClient(ClientHelper client) {
        return new CrmTbUserObject(client.getUserId(), client.getUcid(), client.getBrand(), client.getRegulator(), getCurrentDate(), "Test", "User", "1", "1961-02-01", "Cyprus", "CY", "CY", "en", "RUS", "VGlhbRQlxOaLfl/CgrjL1CfZEIYLXEQL", "cTsGbMYzhsD5SxSOhmgpmQ==", "357", "1", "2FA", "2", "1", "2", client.getIbId(), client.getCpaId(), client.getReferrerId(), "PARTIAL_KYC_ID_PASS", getCurrentTimestampDbFormat(), "2025-01-30 14:56:59.000", getRandomUuidString(), "nationalityId", "2024-11-29 09:55:01");
    }

    public static CrmTbUserObject generateStaticUserByClient(ClientHelper client) {
        return new CrmTbUserObject(client.getUserId(), client.getUcid(), client.getBrand(), client.getRegulator(), "2024-10-23", "Test", "User", "1", "1961-02-01", "Cyprus", "CY", "CY", "en", "RUS", "VGlhbRQlxOaLfl/CgrjL1CfZEIYLXEQL", "cTsGbMYzhsD5SxSOhmgpmQ==", "357", "1", "2FA", "2", "1", "2", 1, 2, 3, "PARTIAL_KYC_ID_PASS", getCurrentTimestampDbFormat(), "2024-10-23 14:56:59.000", "063cde3b-ea9d-48b5-8e2c-99f3d5f67999", "nationalityId", "2024-11-29 09:55:01");
    }
}
