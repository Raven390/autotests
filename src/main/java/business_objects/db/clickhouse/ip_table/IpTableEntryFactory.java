package business_objects.db.clickhouse.ip_table;

import helpers.data.ClientHelper;

import static utils.Constants.CONNECTION_SEARCH_DATA_IP3;

public class IpTableEntryFactory {

    public static IpTableEntry ipTableEntryForConnectionSearch(ClientHelper client) {
        return new IpTableEntry(
                client.getUcid(), CONNECTION_SEARCH_DATA_IP3
        );
    }

    public static IpTableEntry ipTableEntryForConnectionSearch(ClientHelper client, String ip) {
        return new IpTableEntry(
                client.getUcid(), ip
        );
    }
}
