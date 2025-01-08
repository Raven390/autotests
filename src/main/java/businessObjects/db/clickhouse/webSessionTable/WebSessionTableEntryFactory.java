package businessObjects.db.clickhouse.webSessionTable;


import helpers.data.ClientHelper;

public class WebSessionTableEntryFactory {
    public static WebSessionTableEntry webSessionTableEntryForConnectionSearch(ClientHelper client) {
        return new WebSessionTableEntry(
                client.getUserId(), client.getBrand().toLowerCase(), client.getWebSessionId()
        );
    }
}
