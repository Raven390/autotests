package businessObjects.db.clickhouse.mtTbCredits;


import helpers.data.ClientHelper;
import io.qameta.allure.Step;
import utils.Utils;

import static utils.Utils.getCurrentTimestampDbFormat;
import static utils.Utils.getRandomIntPositive;

public class MtTbCreditsObjectFactory {
    @Step("Generate user object by user id")
    public static MtTbCreditsObject generateCreditsByClient(ClientHelper client) {
        return new MtTbCreditsObject(client.getTradingAccount(), 1.0, 1.0, client.getBrand(), "Comment", getCurrentTimestampDbFormat(), "USD", "VFSC", client.getServerId(), "server1", getRandomIntPositive(), client.getUcid(), Utils.getRandomUuidString(), client.getUserId());
    }
}
