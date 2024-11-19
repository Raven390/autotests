package businessObjects.db.mtTbUserTable;

import io.qameta.allure.Step;

public class MtTbUserObjectFactory {
    @Step("Generate user data in mt_tb_user table")
    public static MtTbUserObject generateMtTbUserData(String ucid, String uuid, Integer account, Integer serverId) {
        return new MtTbUserObject(
                ucid,account,"server1","MT4","Standart",serverId,"2023-02-10 07:00:04.408","Active",0.00,"USD",
                100,1,2,3,"S_VFX_EUR",9,1,"2023-02-10 07:00:04.408","2023-02-10 07:00:04.408");
    }
}