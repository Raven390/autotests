package businessObjects.db.clickhouse.digitalIdTable;

import java.util.Objects;

public class DigitalIdTableEntry {

    public Integer userId;
    public String brand;
    public String digitalId;

    public DigitalIdTableEntry() {
    }

    public DigitalIdTableEntry(Integer userId, String brand, String digitalId) {
        this.userId = userId;
        this.brand = brand;
        this.digitalId = digitalId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DigitalIdTableEntry that = (DigitalIdTableEntry) o;
        return Objects.equals(userId, that.userId) && Objects.equals(brand, that.brand) && Objects.equals(
                digitalId, that.digitalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, brand, digitalId);
    }

    @Override
    public String toString() {
        return "DigitalIdTableEntry{" + "userId=" + userId + ", brand='" + brand + '\'' + ", digitalId='" + digitalId + '\'' + '}';
    }
}
