package businessObjects.db.csTbPhoneTable;

import static utils.Utils.getCurrentTimestampDbFormat;

public class PhoneTableEntryFactory {

    public static PhoneTableEntry phoneTableEntryForConnectionSearch() {
        return new PhoneTableEntry(
                "99994",
                99_994,
                "vantage",
                "357111111111",
                getCurrentTimestampDbFormat()
        );
    }
}
