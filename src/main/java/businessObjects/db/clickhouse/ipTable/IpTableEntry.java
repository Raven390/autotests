package businessObjects.db.clickhouse.ipTable;

import java.util.Objects;

public class IpTableEntry {

    public Integer userId;
    public String brand;
    public String ip;

    public IpTableEntry() {
    }

    public IpTableEntry(Integer userId, String brand, String ip) {
        this.userId = userId;
        this.brand = brand;
        this.ip = ip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IpTableEntry that = (IpTableEntry) o;
        return Objects.equals(userId, that.userId) && Objects.equals(brand, that.brand) && Objects.equals(ip, that.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, brand, ip);
    }

    @Override
    public String toString() {
        return "IpTableEntry{" + "userId=" + userId + ", brand='" + brand + '\'' + ", ip='" + ip + '\'' + '}';
    }
}
