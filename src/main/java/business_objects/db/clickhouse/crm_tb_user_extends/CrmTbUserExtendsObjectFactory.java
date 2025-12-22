package business_objects.db.clickhouse.crm_tb_user_extends;

import static utils.Utils.*;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;

public class CrmTbUserExtendsObjectFactory {

    @Step("Generate crm_tb_user_extends object by Client")
    public static CrmTbUserExtendsObject generateCrmTbUserExtendsByClient(ClientHelper client) {
        return new CrmTbUserExtendsObject(
                1,
                1,
                client.getBrand(),
                client.getRegulator(),
                client.getUserId().longValue(),
                client.getUcid(),
                client.getCpaId().longValue(),
                "webSource",
                "income",
                "tradingExperience",
                "male",
                0,
                getCurrentTimestampDbFormat());
    }
}
