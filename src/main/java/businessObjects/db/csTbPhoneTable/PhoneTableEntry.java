package businessObjects.db.csTbPhoneTable;

import java.util.Objects;

public class PhoneTableEntry {

    public String ucid;
    public Integer userId;
    public String brand;
    public String phoneNum;
    public String updateTs;

    public PhoneTableEntry() {
    }

    public PhoneTableEntry(String ucid, Integer userId, String brand, String phoneNum, String updateTs) {
        this.ucid = ucid;
        this.userId = userId;
        this.brand = brand;
        this.phoneNum = phoneNum;
        this.updateTs = updateTs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneTableEntry that = (PhoneTableEntry) o;
        return Objects.equals(ucid, that.ucid) && Objects.equals(userId, that.userId) && Objects.equals(brand, that.brand) && Objects.equals(phoneNum, that.phoneNum) && Objects.equals(updateTs, that.updateTs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ucid, userId, brand, phoneNum, updateTs);
    }

    @Override
    public String toString() {
        return "PhoneTableEntry{" +
                "ucid='" + ucid + '\'' +
                ", userId=" + userId +
                ", brand='" + brand + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", updateTs='" + updateTs + '\'' +
                '}';
    }
}
