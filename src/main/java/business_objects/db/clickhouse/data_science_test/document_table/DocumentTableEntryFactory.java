package business_objects.db.clickhouse.data_science_test.document_table;

import helpers.data.ClientHelper;

public class DocumentTableEntryFactory {

    public static DocumentTableEntry documentTableEntryForConnectionSearch(ClientHelper client) {
        return new DocumentTableEntry(client.getUcid(), "passport", "testaccidnum", 1);
    }

    public static DocumentTableEntry documentTableEntryForConnectionSearchRandomized(ClientHelper client) {
        return new DocumentTableEntry(client.getUcid(), "passport", "testaccidnum", 1);
    }

    public static DocumentTableEntry documentTableEntryByClientAndAccIdNum(ClientHelper client, String accIdNum) {
        return new DocumentTableEntry(client.getUcid(), "passport", accIdNum, 1);
    }
}
