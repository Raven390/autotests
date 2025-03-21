package business_objects.db.clickhouse.crm_tb_user_extends;


import helpers.data.ClientHelper;
import io.qameta.allure.Step;

import static utils.Utils.*;

public class CrmTbUserExtendsObjectFactory {

    @Step("Generate crm_tb_user_extends object by Client")
    public static CrmTbUserExtendsObject generateCrmTbUserExtendsByClient(ClientHelper client) {
        return new CrmTbUserExtendsObject(1, 1, client.getBrand(), client.getRegulator(), client.getUserId().longValue(), client.getUcid(), client.getCpaId().longValue(), "webSource", "income", "tradingExperience", "male", 0, getCurrentTimestampDbFormat());
    }
}
