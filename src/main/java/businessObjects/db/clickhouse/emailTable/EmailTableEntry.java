package businessObjects.db.clickhouse.emailTable;

import java.util.Objects;

public class EmailTableEntry {

    public Integer userId;
    public String brand;
    public String email;

    public EmailTableEntry() {
    }

    public EmailTableEntry(Integer userId, String brand, String email) {
        this.userId = userId;
        this.brand = brand;
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailTableEntry that = (EmailTableEntry) o;
        return Objects.equals(userId, that.userId) && Objects.equals(brand, that.brand) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, brand, email);
    }

    @Override
    public String toString() {
        return "EmailTableEntry{" + "userId=" + userId + ", brand='" + brand + '\'' + ", email='" + email + '\'' + '}';
    }
}
