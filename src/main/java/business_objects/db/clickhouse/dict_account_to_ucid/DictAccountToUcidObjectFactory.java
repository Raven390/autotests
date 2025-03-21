package business_objects.db.clickhouse.dict_account_to_ucid;


import helpers.data.ClientHelper;
import io.qameta.allure.Step;

import static utils.Utils.*;

public class DictAccountToUcidObjectFactory {
    @Step("Generate dict object by Client")
    public static DictAccountToUcidObject generateDictByClient(ClientHelper client) {
        return new DictAccountToUcidObject(1, client.getBrand(), client.getRegulator(), client.getUserId(), client.getUcid(), client.getTradingAccount(), client.getServerId(), client.getServerId().toString(), 0, getCurrentTimestampDbFormat());
    }

}
