package business_objects.db.clickhouse.dict_is_test;

import static utils.Utils.getCurrentTimestampDbFormat;

import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import io.qameta.allure.Step;

public class DictIsTestObjectFactory {
    @Step("Generate dict object by Client with test value")
    public static DictIsTestObject generateDictIsTestByClientTrue(ClientHelper client) {
        return new DictIsTestObject(
                client.getServerId(),
                client.getTradingAccount(),
                "Social_Trading/Test",
                1,
                getCurrentTimestampDbFormat());
    }

    @Step("Generate dict object by Client with test value")
    public static DataHelper generateDictIsTestByClientTrue(DataHelper data) {
        data.getCrmTbAccountObject().setAccountGroup("Social_Trading");
        data.getMtAccountObject().setAccountGroup("Social_Trading");
        return data;
    }

    @Step("Generate dict object by Client with non test value")
    public static DictIsTestObject generateDictIsTestByClientFalse(ClientHelper client) {
        return new DictIsTestObject(
                client.getServerId(), client.getTradingAccount(), "S_VFX_EUR", 0, getCurrentTimestampDbFormat());
    }

    @Step("Generate dict object by Client with non test value")
    public static DataHelper generateDictIsTestByClientFalse(DataHelper data) {
        data.getCrmTbAccountObject().setAccountGroup("M_VUR_.EUR");
        data.getMtAccountObject().setAccountGroup("M_VUR_.EUR");
        return data;
    }
}
