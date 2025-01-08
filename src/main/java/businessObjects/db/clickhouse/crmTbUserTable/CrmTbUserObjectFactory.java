package businessObjects.db.clickhouse.crmTbUserTable;


import helpers.data.ClientHelper;
import io.qameta.allure.Step;

import static utils.Utils.getRandomUuidString;

public class CrmTbUserObjectFactory {
    @Step("Generate user object by Client")
    public static CrmTbUserObject generateUserByClient(ClientHelper client) {
        return new CrmTbUserObject(client.getUserId(), client.getUcid(), client.getBrand(), client.getRegulator(), "2024-10-23", "Test", "User", "1", "1961-02-01", "Cyprus", "CY", "CY", "en", "RUS", "DUrksdLPlqZB6byC9vfKk6qm9BpUmsOS", "BjrbbdAHkwhBFLnPclfvbg==", "357", "1", "2FA", "2", "1", "2", 1, 2, 3, "PARTIAL_KYC_ID_PASS", "2024-10-29 09:55:01.300", "2024-10-23 14:56:59.000", getRandomUuidString(), "nationalityId");
    }
}
