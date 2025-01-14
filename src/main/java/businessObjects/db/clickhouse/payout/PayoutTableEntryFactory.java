package businessObjects.db.clickhouse.payout;

import helpers.data.ClientHelper;

public class PayoutTableEntryFactory {

    public static PayoutTableEntry payoutTableEntryForConnectionSearch(ClientHelper client) {
        return new PayoutTableEntry(
                client.getUserId(), client.getBrand().toLowerCase(), "testpayout"
        );
    }
}
