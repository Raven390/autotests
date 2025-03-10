package businessObjects.db.clickhouse.documentTable;

import helpers.data.ClientHelper;

public class DocumentTableEntryFactory {

    public static DocumentTableEntry documentTableEntryForConnectionSearch(ClientHelper client) {
        return new DocumentTableEntry(
                client.getUcid(), "passport", "testaccidnum", 1
        );
    }

    public static DocumentTableEntry documentTableEntryByClientAndAccIdNum(ClientHelper client, String accIdNum) {
        return new DocumentTableEntry(
                client.getUcid(), "passport", accIdNum, 1
        );
    }
}
