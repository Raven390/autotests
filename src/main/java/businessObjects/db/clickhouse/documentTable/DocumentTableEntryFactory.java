package businessObjects.db.clickhouse.documentTable;

import helpers.data.ClientHelper;

public class DocumentTableEntryFactory {

    public static DocumentTableEntry documentTableEntryForConnectionSearch(ClientHelper client) {
        return new DocumentTableEntry(
                client.getUserId(), client.getBrand().toLowerCase(), "passport", "testaccidnum", 1
        );
    }
}
