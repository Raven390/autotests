package business_objects.db.clickhouse.data_science_test.ip_table;

import static utils.Constants.CONNECTION_SEARCH_DATA_IP3;

import helpers.data.ClientHelper;
import net.datafaker.Faker;

public class IpTableEntryFactory {
    static Faker faker = new Faker();

    public static IpTableEntry ipTableEntryForConnectionSearch(ClientHelper client) {
        return new IpTableEntry(client.getUcid(), CONNECTION_SEARCH_DATA_IP3);
    }

    public static IpTableEntry ipTableEntryForConnectionSearch(ClientHelper client, String ip) {
        return new IpTableEntry(client.getUcid(), ip);
    }

    public static IpTableEntry ipTableEntryForConnectionSearchFiltration(ClientHelper client) {
        return new IpTableEntry(client.getUcid(), "filtrationIp");
    }

    public static IpTableEntry ipTableEntryForConnectionSearch(ClientHelper client, boolean randomIp) {
        IpTableEntry ipTableEntry = null;
        if (randomIp) {
            ipTableEntry = new IpTableEntry(client.getUcid(), faker.internet().ipV4Address());
        }
        return ipTableEntry;
    }
}
