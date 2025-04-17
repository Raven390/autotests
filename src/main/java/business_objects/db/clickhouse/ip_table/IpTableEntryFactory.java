package business_objects.db.clickhouse.ip_table;

import helpers.data.ClientHelper;
import net.datafaker.Faker;

import static utils.Constants.CONNECTION_SEARCH_DATA_IP3;

public class IpTableEntryFactory {
    static Faker faker = new Faker();

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

    public static IpTableEntry ipTableEntryForConnectionSearch(ClientHelper client, boolean randomIp) {
        IpTableEntry ipTableEntry = null;
        if (randomIp) {
            ipTableEntry = new IpTableEntry(client.getUcid(), faker.internet().ipV4Address()
            );
        }
        return ipTableEntry;
    }
}
