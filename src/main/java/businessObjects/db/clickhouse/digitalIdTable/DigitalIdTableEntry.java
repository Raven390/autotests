package businessObjects.db.clickhouse.digitalIdTable;

import java.util.Objects;

public class DigitalIdTableEntry {

    public String ucid;
    public String digitalId;

    public DigitalIdTableEntry() {
    }

    public DigitalIdTableEntry(String ucid, String digitalId) {
        this.ucid = ucid;
        this.digitalId = digitalId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DigitalIdTableEntry that = (DigitalIdTableEntry) o;
        return Objects.equals(ucid, that.ucid) && Objects.equals(digitalId, that.digitalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ucid, digitalId);
    }

    @Override
    public String toString() {
        return "DigitalIdTableEntry{" + "ucid='" + ucid + '\'' + ", digitalId='" + digitalId + '\'' + '}';
    }
}
