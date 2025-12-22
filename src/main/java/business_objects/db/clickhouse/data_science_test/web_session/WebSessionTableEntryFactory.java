package business_objects.db.clickhouse.data_science_test.web_session;

import helpers.data.ClientHelper;

public class WebSessionTableEntryFactory {
    public static WebSessionTableEntry webSessionTableEntryForConnectionSearch(ClientHelper client) {
        return new WebSessionTableEntry(client.getUcid(), client.getWebSessionId());
    }

    public static WebSessionTableEntry webSessionTableEntryForConnectionSearch(
            ClientHelper client, String webSessionId) {
        return new WebSessionTableEntry(client.getUcid(), webSessionId);
    }
}
