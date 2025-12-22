package business_objects.db.clickhouse.data_science_test.ip_table;

import java.util.Objects;

public class IpTableEntry {

    public String ucid;
    public String ip;

    public IpTableEntry() {}

    public IpTableEntry(String ucid, String ip) {
        this.ucid = ucid;
        this.ip = ip;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        IpTableEntry that = (IpTableEntry) o;
        return Objects.equals(ucid, that.ucid) && Objects.equals(ip, that.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ucid, ip);
    }

    @Override
    public String toString() {
        return "IpTableEntry{" + "ucid='" + ucid + '\'' + ", ip='" + ip + '\'' + '}';
    }
}
