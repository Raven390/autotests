package business_objects.db.clickhouse.mt_tb_user;

import io.qameta.allure.Step;

import static utils.Utils.*;

public class MtTbUserObjectFactory {
    @Step("Generate user data in mt_tb_user table")
    public static MtTbUserObject generateMtTbUserData(String ucid, Integer account, Integer serverId) {
        return new MtTbUserObject(
                Integer.valueOf(ucid.split("-")[1]), ucid, account, "server1", "MT4", "Standard", serverId, getCurrentTimestampDbFormat(), "Active", 0.00, "USD", 100.0, 1.0, 2.0, 3, "S_VFX_EUR", 9.0, 1.0, getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat());
    }

    public static MtTbUserObject generateMtTbUserDataForUi(String ucid) {
        return new MtTbUserObject(
                Integer.valueOf(ucid.split("-")[1]), ucid, getRandomIntPositive(), "server1", "MT4", "Standard", 11, getPreviousYearTimestampDbFormat(), "Active", 101.5, "EUR", 102.7, 11.1, 99.9, 3, "S_VFX_EUR", 7.77, 22.2, getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat());
    }
}