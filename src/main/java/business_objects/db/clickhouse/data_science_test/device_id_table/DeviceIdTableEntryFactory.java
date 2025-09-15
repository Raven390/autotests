package business_objects.db.clickhouse.data_science_test.device_id_table;

import helpers.data.ClientHelper;

public class DeviceIdTableEntryFactory {
    public static DeviceIdTableEntry deviceIdTableEntryForConnectionSearch(ClientHelper client) {
        return new DeviceIdTableEntry(
                client.getUcid(), client.getDeviceId()
        );
    }

    public static DeviceIdTableEntry deviceIdTableEntryForConnectionSearch(ClientHelper client, String deviceId) {
        return new DeviceIdTableEntry(
                client.getUcid(), deviceId
        );
    }
}