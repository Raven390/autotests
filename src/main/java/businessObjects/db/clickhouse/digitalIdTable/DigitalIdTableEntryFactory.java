package businessObjects.db.clickhouse.digitalIdTable;


import helpers.data.ClientHelper;

public class DigitalIdTableEntryFactory {
    public static DigitalIdTableEntry digitalIdTableEntryForConnectionSearch(ClientHelper client) {
        return new DigitalIdTableEntry(
                client.getUcid(), client.getDigitalId()
        );
    }

    public static DigitalIdTableEntry digitalIdTableEntryForConnectionSearch(ClientHelper client, String digitalId) {
        return new DigitalIdTableEntry(
                client.getUcid(), digitalId
        );
    }
}
