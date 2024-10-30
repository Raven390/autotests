package businessObjects.db.csTbDocTable;

import static utils.Utils.getCurrentTimestampDbFormat;

public class DocumentTableEntryFactory {

    public static DocumentTableEntry documentTableEntryForConnectionSearch() {
     return new DocumentTableEntry(
             "99991",
             99_991,
             "vantage",
             "passport",
             "testaccidnum",
             1,
             getCurrentTimestampDbFormat()
     );
    }
}
