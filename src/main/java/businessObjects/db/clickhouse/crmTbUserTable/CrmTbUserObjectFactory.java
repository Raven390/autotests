package businessObjects.db.clickhouse.crmTbUserTable;


import helpers.data.ClientHelper;
import io.qameta.allure.Step;

public class CrmTbUserObjectFactory {
    @Step("Generate user object by Client")
    public static CrmTbUserObject generateUserByClient(ClientHelper client) {
        return new CrmTbUserObject(client.getUserId(), client.getUcid(), client.getBrand(), "VFSC", "2024-10-23 14:56:59.000", "Test", "User", "1", "1961-02-01", "Cyprus", "CY", "CY", "en", "RUS", "DUrksdLPlqZB6byC9vfKk6qm9BpUmsOS", "BjrbbdAHkwhBFLnPclfvbg==", "357", "1", "2FA", "2", "2024-10-23 15:14:17.232", "2024-10-23 15:14:10.722", 1, 2, 3, "PARTIAL_KYC_ID_PASS", "2024-10-29 09:55:01.300");
    }
}
