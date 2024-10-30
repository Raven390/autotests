package businessObjects.db.csTbEmailTable;

import static utils.Utils.getCurrentTimestampDbFormat;

public class EmailTableEntryFactory {

    public static EmailTableEntry emailTableEntryForConnectionSearch() {
        return new EmailTableEntry(
                "99992",
                99_992,
                "vantage",
                "test@email.com",
                getCurrentTimestampDbFormat()
        );
    }
}
