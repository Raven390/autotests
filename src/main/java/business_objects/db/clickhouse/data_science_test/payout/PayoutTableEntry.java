package business_objects.db.clickhouse.data_science_test.payout;

import java.util.Objects;

public class PayoutTableEntry {

    public String ucid;
    public String payout;

    public PayoutTableEntry() {}

    public PayoutTableEntry(String ucid, String payout) {
        this.ucid = ucid;
        this.payout = payout;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PayoutTableEntry that = (PayoutTableEntry) o;
        return Objects.equals(ucid, that.ucid) && Objects.equals(payout, that.payout);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ucid, payout);
    }

    @Override
    public String toString() {
        return "PayoutTableEntry{" + "ucid='" + ucid + '\'' + ", payout='" + payout + '\'' + '}';
    }
}
