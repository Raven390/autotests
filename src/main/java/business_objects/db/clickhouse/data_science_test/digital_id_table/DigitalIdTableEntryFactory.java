package business_objects.db.clickhouse.data_science_test.digital_id_table;

import helpers.data.ClientHelper;

public class DigitalIdTableEntryFactory {
    public static DigitalIdTableEntry digitalIdTableEntryForConnectionSearch(ClientHelper client) {
        return new DigitalIdTableEntry(client.getUcid(), client.getDigitalId());
    }

    public static DigitalIdTableEntry digitalIdTableEntryForConnectionSearch(ClientHelper client, String digitalId) {
        return new DigitalIdTableEntry(client.getUcid(), digitalId);
    }

    public static DigitalIdTableEntry digitalIdTableEntryForConnectionSearchFiltration(ClientHelper client) {
        return new DigitalIdTableEntry(client.getUcid(), "digitalIdFiltration");
    }
}
