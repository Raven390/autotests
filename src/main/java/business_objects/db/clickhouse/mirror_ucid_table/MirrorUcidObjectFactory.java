package business_objects.db.clickhouse.mirror_ucid_table;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;


import static utils.Utils.*;

public class MirrorUcidObjectFactory {
    @Step("Generate mirror ucid object by Client")
    public static MirrorUcidObject generateMirrorUcidObjectByClient(ClientHelper client) {
        return new MirrorUcidObject("EURUSD", client.getUcid(), 1d, 1, client.getUcid() + 1, 1d, 1, 1f, 1f, 1f, 1f, 1f, 1f, getCurrentTimestampDbFormat());
    }

    @Step("Generate mirror ucid object by client from to")
    public static MirrorUcidObject generateMirrorUcidObjectByClients(ClientHelper clientFrom, ClientHelper clientTo) {
        return new MirrorUcidObject("EURUSD", clientFrom.getUcid(), 1d, 1, clientTo.getUcid(), 1d, 1, 1f, 1f, 1f, 1f, 1f, 1f, getCurrentTimestampDbFormat());
    }
}
