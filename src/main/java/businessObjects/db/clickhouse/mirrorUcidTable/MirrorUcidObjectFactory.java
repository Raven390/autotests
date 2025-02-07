package businessObjects.db.clickhouse.mirrorUcidTable;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;


import static utils.Utils.*;

public class MirrorUcidObjectFactory {
    @Step("Generate lexis nexis object by Client")
    public static MirrorUcidObject generateMirrorUcidObjectByClient(ClientHelper client) {
        return new MirrorUcidObject("EURUSD", client.getUcid(), 1d, 1, client.getUcid() + 1, 1d, 1, 1f, 1f, 1f, 1f, 1f, 1f, getCurrentTimestampDbFormat());
    }
}
