package businessObjects.db.clickhouse.crmTbWithdrawalTable;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;
import utils.Utils;

public class CrmTbWithdrawalObjectFactory {
    @Step("Generate user object by user id")
    public static CrmTbWithdrawalObject generateWithdrawalByClient(ClientHelper client) {
        return new CrmTbWithdrawalObject(client.getTradingAccount(), 1.0,2.0, client.getBrand(), "2024-10-23 15:14:10.723",
                                         "USD",1.0,"", "","","","",
                                         "","","",1.0,"",1,2,Utils.getRandomIntPositive(),
                                         client.getUcid(), "","2024-10-23 15:14:10.724", client.getUserId());
    }
}
