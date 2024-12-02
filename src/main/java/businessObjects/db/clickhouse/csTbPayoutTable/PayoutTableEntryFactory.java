package businessObjects.db.clickhouse.csTbPayoutTable;

import static utils.Utils.getCurrentTimestampDbFormat;

public class PayoutTableEntryFactory {

    public static PayoutTableEntry payoutTableEntryForConnectionSearch() {
        return new PayoutTableEntry(
                "vantage-99995",
                99_995,
                "Vantage",
                "testpayoutid",
                getCurrentTimestampDbFormat()
        );
    }
}
