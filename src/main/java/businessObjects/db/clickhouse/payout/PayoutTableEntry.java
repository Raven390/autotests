package businessObjects.db.clickhouse.payout;

import java.util.Objects;

public class PayoutTableEntry {

    public Integer userId;
    public String brand;
    public String payout;

    public PayoutTableEntry() {
    }

    public PayoutTableEntry(Integer userId, String brand, String payout) {
        this.userId = userId;
        this.brand = brand;
        this.payout = payout;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PayoutTableEntry that = (PayoutTableEntry) o;
        return Objects.equals(userId, that.userId) && Objects.equals(brand, that.brand) && Objects.equals(payout, that.payout);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, brand, payout);
    }

    @Override
    public String toString() {
        return "PayoutTableEntry{" + "userId=" + userId + ", brand='" + brand + '\'' + ", payoutId='" + payout + '\'' + '}';
    }
}
