package businessObjects.db.clickhouse.deviceIdTable;

import java.util.Objects;

public class DeviceIdTableEntry {

    public Integer userId;
    public String brand;
    public String deviceId;

    public DeviceIdTableEntry() {
    }

    public DeviceIdTableEntry(Integer userId, String brand, String deviceId) {
        this.userId = userId;
        this.brand = brand;
        this.deviceId = deviceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceIdTableEntry that = (DeviceIdTableEntry) o;
        return Objects.equals(userId, that.userId) && Objects.equals(brand, that.brand) && Objects.equals(
                deviceId, that.deviceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, brand, deviceId);
    }

    @Override
    public String toString() {
        return "DeviceIdTableEntry{" + "userId=" + userId + ", brand='" + brand + '\'' + ", deviceId='" + deviceId + '\'' + '}';
    }
}
