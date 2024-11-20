package businessObjects.db.clickhouse.csTbPayoutTable;

import static utils.Utils.getCurrentTimestampDbFormat;

public class PayoutTableEntryFactory {

    public static PayoutTableEntry payoutTableEntryForConnectionSearch() {
        return new PayoutTableEntry(
                "99995",
                99_995,
                "vantage",
                "testpayoutid",
                getCurrentTimestampDbFormat()
        );
    }
}
