package businessObjects.db.csTbConnectionTableV2;

import java.util.Objects;

public class ConnectionTableEntry {

    public String userFrom;
    public String userTo;
    public Integer level;
    public String attr;
    public String updateTs;

    public ConnectionTableEntry() {
    }

    public ConnectionTableEntry(String userFrom, String userTo, Integer level, String attr, String updateTs) {
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.level = level;
        this.attr = attr;
        this.updateTs = updateTs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConnectionTableEntry that = (ConnectionTableEntry) o;
        return Objects.equals(userFrom, that.userFrom) && Objects.equals(userTo, that.userTo) && Objects.equals(level, that.level) && Objects.equals(attr, that.attr) && Objects.equals(updateTs, that.updateTs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userFrom, userTo, level, attr, updateTs);
    }

    @Override
    public String toString() {
        return "ConnectionTableEntry{" +
                "userFrom='" + userFrom + '\'' +
                ", userTo='" + userTo + '\'' +
                ", level=" + level +
                ", attr='" + attr + '\'' +
                ", updateTs='" + updateTs + '\'' +
                '}';
    }
}
