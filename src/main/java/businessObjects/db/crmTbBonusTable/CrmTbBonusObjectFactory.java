package businessObjects.db.crmTbBonusTable;


import helpers.data.ClientHelper;
import io.qameta.allure.Step;
import utils.Utils;

public class CrmTbBonusObjectFactory {
    @Step("Generate user object by user id")
    public static CrmTbBonusObject generateBonusByClient(ClientHelper client) {
        return new CrmTbBonusObject(Utils.getRandomIntPositive(), Utils.getRandomUuidString(), client.getUcid(),
                                    client.getBrand(), "VSFC2", client.getUserId(), client.getTradingAccount(),
                                    "2024-10-23 15:14:10.722", "2024-10-23 15:14:10.723",
                                    1.0, 2.0, "USD", 1, 2, "1",
                                    "Comment");
    }
}
