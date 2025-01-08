package businessObjects.db.clickhouse.digitalIdTable;


import helpers.data.ClientHelper;

public class DigitalIdTableEntryFactory {
    public static DigitalIdTableEntry digitalIdTableEntryForConnectionSearch(ClientHelper client) {
        return new DigitalIdTableEntry(
                client.getUserId(), client.getBrand().toLowerCase(), client.getDigitalId()
        );
    }
}
