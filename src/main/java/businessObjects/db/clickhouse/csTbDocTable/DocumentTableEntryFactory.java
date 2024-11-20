package businessObjects.db.clickhouse.csTbDocTable;

import static utils.Utils.getCurrentTimestampDbFormat;

public class DocumentTableEntryFactory {

    public static DocumentTableEntry documentTableEntryForConnectionSearch() {
     return new DocumentTableEntry(
             "vantage-99991",
             99_991,
             "vantage",
             "passport",
             "testaccidnum",
             1,
             getCurrentTimestampDbFormat()
     );
    }

    public static DocumentTableEntry documentTableEntryForConnectionSearchDepth() {
        return new DocumentTableEntry(
                "vantage-99997",
                99_997,
                "vantage",
                "passport",
                "testaccidnum",
                1,
                getCurrentTimestampDbFormat()
        );
    }
}
