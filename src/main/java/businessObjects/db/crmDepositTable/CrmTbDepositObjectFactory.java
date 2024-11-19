package businessObjects.db.crmDepositTable;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;
import utils.Utils;

public class CrmTbDepositObjectFactory {
    @Step("Generate user object by user id")
    public static CrmTbDepositObject generateDepositByClient(ClientHelper client) {
        return new CrmTbDepositObject(client.getTradingAccount(), 1.0,2.0, client.getBrand(), "2024-10-23 15:14:10.723",
                                      "USD",1.0,"", "","","","",
                                      "","","",1,2,Utils.getRandomIntPositive(),
                                      client.getUcid(), "","2024-10-23 15:14:10.724", client.getUserId());
    }
}
