package businessObjects.db.mtTbUser;

import io.qameta.allure.Step;

public class MtTbUserObjectFactory {
    @Step("Generate user data in mt_tb_user table")
    public static MtTbUserObject generateMtTbUserData(String id, String ucid, String uuid, Integer account, Integer serverId) {
        return new MtTbUserObject(
                id,ucid,uuid,account,"server1","MT4","Standart",serverId,
                "2023-02-10 07:00:04.408", "Active",0.00,"USD",
                100,1,2,3,"S_VFX_EUR",9,
                1,"2023-02-10 07:00:04.408","2023-02-10 07:00:04.408");
    }
}