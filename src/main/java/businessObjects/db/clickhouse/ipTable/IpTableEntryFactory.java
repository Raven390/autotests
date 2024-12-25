package businessObjects.db.clickhouse.ipTable;

import helpers.data.ClientHelper;

public class IpTableEntryFactory {

    public static IpTableEntry ipTableEntryForConnectionSearch(ClientHelper client) {
        return new IpTableEntry(
                client.getUserId(), client.getBrand().toLowerCase(), "111.111.111.111"
        );
    }
}
