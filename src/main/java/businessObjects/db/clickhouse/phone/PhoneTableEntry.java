package businessObjects.db.clickhouse.phone;

import java.util.Objects;

public class PhoneTableEntry {

    public Integer userId;
    public String brand;
    public String phoneNum;

    public PhoneTableEntry() {
    }

    public PhoneTableEntry(Integer userId, String brand, String phoneNum) {
        this.userId = userId;
        this.brand = brand;
        this.phoneNum = phoneNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneTableEntry that = (PhoneTableEntry) o;
        return Objects.equals(userId, that.userId) && Objects.equals(brand, that.brand) && Objects.equals(phoneNum, that.phoneNum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, brand, phoneNum);
    }

    @Override
    public String toString() {
        return "PhoneTableEntry{" + "userId=" + userId + ", brand='" + brand + '\'' + ", phoneNum='" + phoneNum + '\'' + '}';
    }
}
