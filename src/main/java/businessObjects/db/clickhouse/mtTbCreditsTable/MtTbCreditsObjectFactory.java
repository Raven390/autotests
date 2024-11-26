package businessObjects.db.clickhouse.mtTbCreditsTable;


import helpers.data.ClientHelper;
import io.qameta.allure.Step;
import utils.Utils;

import static utils.Utils.getCurrentTimestampDbFormat;
import static utils.Utils.getRandomIntPositive;

public class MtTbCreditsObjectFactory {
    @Step("Generate user object by user id")
    public static MtTbCreditsObject generateCreditsByClient(ClientHelper client) {
        return new MtTbCreditsObject(getRandomIntPositive(),1.0,1.0, client.getBrand(), "Comment",
                getCurrentTimestampDbFormat(), "USD", "VFSC2",1,
                "server1", getRandomIntPositive(), client.getUcid(), Utils.getRandomUuidString(), client.getUserId());
    }
}
