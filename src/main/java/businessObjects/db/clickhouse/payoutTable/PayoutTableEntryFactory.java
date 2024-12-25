package businessObjects.db.clickhouse.payoutTable;

import helpers.data.ClientHelper;

public class PayoutTableEntryFactory {

    public static PayoutTableEntry payoutTableEntryForConnectionSearch(ClientHelper client) {
        return new PayoutTableEntry(
                client.getUserId(), client.getBrand().toLowerCase(), "testpayout"
        );
    }
}
