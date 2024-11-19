package businessObjects.db.mtCreditsTable;


import helpers.data.ClientHelper;
import io.qameta.allure.Step;
import utils.Utils;

public class MtTbCreditsObjectFactory {
    @Step("Generate user object by user id")
    public static MtTbCreditsObject generateCreditsByClient(ClientHelper client) {
        return new MtTbCreditsObject(1,1.0,1.0, client.getBrand(), "Comment",
                                     "2024-10-23 15:14:10.722", "USD", "VSFC2",1,
                                     "server1",1, client.getUcid(), Utils.getRandomUuidString(),
                                     client.getUserId());
    }
}
