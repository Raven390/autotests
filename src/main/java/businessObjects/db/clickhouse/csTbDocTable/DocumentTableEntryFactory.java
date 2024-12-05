package businessObjects.db.clickhouse.csTbDocTable;

import static utils.Utils.getCurrentTimestampDbFormat;

public class DocumentTableEntryFactory {

    public static DocumentTableEntry documentTableEntryForConnectionSearch() {
        return new DocumentTableEntry(
                "vantage-99991", 99_991, "Vantage", "passport", "testaccidnum", 1, getCurrentTimestampDbFormat()
        );
    }
}
