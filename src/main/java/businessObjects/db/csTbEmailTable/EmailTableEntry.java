package businessObjects.db.csTbEmailTable;

import java.util.Objects;

public class EmailTableEntry {

    public String ucid;
    public Integer userId;
    public String brand;
    public String email;
    public String updateTs;

    public EmailTableEntry() {
    }

    public EmailTableEntry(String ucid, Integer userId, String brand, String email, String updateTs) {
        this.ucid = ucid;
        this.userId = userId;
        this.brand = brand;
        this.email = email;
        this.updateTs = updateTs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailTableEntry that = (EmailTableEntry) o;
        return Objects.equals(ucid, that.ucid) && Objects.equals(userId, that.userId) && Objects.equals(brand, that.brand) && Objects.equals(email, that.email) && Objects.equals(updateTs, that.updateTs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ucid, userId, brand, email, updateTs);
    }

    @Override
    public String toString() {
        return "EmailTableEntry{" +
                "ucid='" + ucid + '\'' +
                ", userId=" + userId +
                ", brand='" + brand + '\'' +
                ", email='" + email + '\'' +
                ", updateTs='" + updateTs + '\'' +
                '}';
    }
}
