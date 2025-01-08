package businessObjects.db.clickhouse.deviceIdTable;

import helpers.data.ClientHelper;

public class DeviceIdTableEntryFactory {
    public static DeviceIdTableEntry deviceIdTableEntryForConnectionSearch(ClientHelper client) {
        return new DeviceIdTableEntry(
                client.getUserId(), client.getBrand().toLowerCase(), client.getDeviceId()
        );
    }
}