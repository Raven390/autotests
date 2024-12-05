package businessObjects.db.clickhouse.csTbPayoutTable;

import java.util.Objects;

public class PayoutTableEntry {

    public String ucid;
    public Integer userId;
    public String brand;
    public String payoutId;
    public String updateTs;

    public PayoutTableEntry() {
    }

    public PayoutTableEntry(String ucid, Integer userId, String brand, String payoutId, String updateTs) {
        this.ucid = ucid;
        this.userId = userId;
        this.brand = brand;
        this.payoutId = payoutId;
        this.updateTs = updateTs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PayoutTableEntry that = (PayoutTableEntry) o;
        return Objects.equals(ucid, that.ucid) && Objects.equals(userId, that.userId) && Objects.equals(brand, that.brand) && Objects.equals(payoutId, that.payoutId) && Objects.equals(updateTs, that.updateTs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ucid, userId, brand, payoutId, updateTs);
    }

    @Override
    public String toString() {
        return "PayoutTableEntry{" + "ucid='" + ucid + '\'' + ", userId=" + userId + ", brand='" + brand + '\'' + ", payoutId='" + payoutId + '\'' + ", updateTs='" + updateTs + '\'' + '}';
    }
}
