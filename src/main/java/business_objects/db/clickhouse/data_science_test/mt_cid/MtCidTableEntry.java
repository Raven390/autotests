package business_objects.db.clickhouse.data_science_test.mt_cid;

import java.util.Objects;

public class MtCidTableEntry {

    private String ucid;
    private String mtCid;

    public MtCidTableEntry() {}

    public MtCidTableEntry(String ucid, String mt_cid) {
        this.ucid = ucid;
        this.mtCid = mt_cid;
    }

    public String getUcid() {
        return ucid;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public String getMtCid() {
        return mtCid;
    }

    public void setMtCid(String mtCid) {
        this.mtCid = mtCid;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MtCidTableEntry that)) return false;
        return Objects.equals(ucid, that.ucid) && Objects.equals(mtCid, that.mtCid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ucid, mtCid);
    }

    @Override
    public String toString() {
        return "MtCidEntry{" + "ucid='" + ucid + '\'' + ", mt_cid='" + mtCid + '\'' + '}';
    }
}
