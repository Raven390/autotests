package businessObjects.db.clickhouse.csTbIpTable;

import java.util.Objects;

public class IpTableEntry {

    public String ucid;
    public Integer userId;
    public String brand;
    public String ip;
    public String updateTs;

    public IpTableEntry() {
    }

    public IpTableEntry(String ucid, Integer userId, String brand, String ip, String updateTs) {
        this.ucid = ucid;
        this.userId = userId;
        this.brand = brand;
        this.ip = ip;
        this.updateTs = updateTs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IpTableEntry that = (IpTableEntry) o;
        return Objects.equals(ucid, that.ucid) && Objects.equals(userId, that.userId) && Objects.equals(brand, that.brand) && Objects.equals(ip, that.ip) && Objects.equals(updateTs, that.updateTs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ucid, userId, brand, ip, updateTs);
    }

    @Override
    public String toString() {
        return "IpTableEntry{" +
                "ucid='" + ucid + '\'' +
                ", userId=" + userId +
                ", brand='" + brand + '\'' +
                ", ip='" + ip + '\'' +
                ", updateTs='" + updateTs + '\'' +
                '}';
    }
}
