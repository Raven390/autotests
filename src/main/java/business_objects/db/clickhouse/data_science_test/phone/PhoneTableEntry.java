package business_objects.db.clickhouse.data_science_test.phone;

import java.util.Objects;

public class PhoneTableEntry {

    public String ucid;
    public String phoneNum;

    public PhoneTableEntry() {
    }

    public PhoneTableEntry(String ucid, String phoneNum) {
        this.ucid = ucid;
        this.phoneNum = phoneNum;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PhoneTableEntry that = (PhoneTableEntry) o;
        return Objects.equals(ucid, that.ucid) && Objects.equals(phoneNum, that.phoneNum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ucid, phoneNum);
    }

    @Override
    public String toString() {
        return "PhoneTableEntry{" + "ucid='" + ucid + '\'' + ", phoneNum='" + phoneNum + '\'' + '}';
    }
}
