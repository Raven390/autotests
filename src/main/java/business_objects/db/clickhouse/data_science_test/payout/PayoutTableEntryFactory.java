package business_objects.db.clickhouse.data_science_test.payout;

import helpers.data.ClientHelper;

public class PayoutTableEntryFactory {

    public static PayoutTableEntry payoutTableEntryForConnectionSearch(ClientHelper client) {
        return new PayoutTableEntry(client.getUcid(), "testpayout");
    }

    public static PayoutTableEntry payoutTableEntryForConnectionSearch(ClientHelper client, String payout) {
        return new PayoutTableEntry(client.getUcid(), payout);
    }
}
