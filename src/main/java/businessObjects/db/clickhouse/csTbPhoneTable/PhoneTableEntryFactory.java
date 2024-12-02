package businessObjects.db.clickhouse.csTbPhoneTable;

import static utils.Utils.getCurrentTimestampDbFormat;

public class PhoneTableEntryFactory {

    public static PhoneTableEntry phoneTableEntryForConnectionSearch() {
        return new PhoneTableEntry(
                "vantage-99994",
                99_994,
                "Vantage",
                "357111111111",
                getCurrentTimestampDbFormat()
        );
    }
}
