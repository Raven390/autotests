package businessObjects.db.clickhouse.crmTbBonusTable;


import helpers.data.ClientHelper;
import io.qameta.allure.Step;
import utils.Utils;

import static utils.Utils.getCurrentTimestampDbFormat;

public class CrmTbBonusObjectFactory {
    @Step("Generate bonus object by client")
    public static CrmTbBonusObject generateBonusByClient(ClientHelper client) {
        return new CrmTbBonusObject(Utils.getRandomIntPositive(), Utils.getRandomUuidString(), client.getUcid(),
                                    client.getBrand(), "VFSC2", client.getUserId(), client.getTradingAccount(),
                                    getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(),
                                    1.0, 2.0, "USD", 1, 2, "WelcomeBonus",
                                    "Comment");
    }
}
