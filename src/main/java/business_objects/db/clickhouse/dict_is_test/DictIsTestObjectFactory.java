package business_objects.db.clickhouse.dict_is_test;

import static utils.Utils.getCurrentTimestampDbFormat;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;

public class DictIsTestObjectFactory {
    @Step("Generate dict object by Client with test value")
    public static DictIsTestObject generateDictIsTestByClientTrue(ClientHelper client) {
        return new DictIsTestObject(
                client.getServerId(), client.getTradingAccount(), "Social/Test", 1, getCurrentTimestampDbFormat());
    }

    @Step("Generate dict object by Client with non test value")
    public static DictIsTestObject generateDictIsTestByClientFalse(ClientHelper client) {
        return new DictIsTestObject(
                client.getServerId(), client.getTradingAccount(), "S_VFX_EUR", 0, getCurrentTimestampDbFormat());
    }
}
