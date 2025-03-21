package business_objects.db.clickhouse.device_id_table;

import java.util.Objects;

public class DeviceIdTableEntry {

    public String ucid;
    public String deviceId;

    public DeviceIdTableEntry() {
    }

    public DeviceIdTableEntry(String ucid, String deviceId) {
        this.ucid = ucid;
        this.deviceId = deviceId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DeviceIdTableEntry that = (DeviceIdTableEntry) o;
        return Objects.equals(ucid, that.ucid) && Objects.equals(deviceId, that.deviceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ucid, deviceId);
    }

    @Override
    public String toString() {
        return "DeviceIdTableEntry{" + "ucid='" + ucid + '\'' + ", deviceId='" + deviceId + '\'' + '}';
    }
}
