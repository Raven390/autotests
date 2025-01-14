package businessObjects.db.clickhouse.sessionId;

import java.util.Objects;

public class SessionIdTableEntry {

    public Integer userId;
    public String brand;
    public String sessionId;

    public SessionIdTableEntry() {
    }

    public SessionIdTableEntry(Integer userId, String brand, String sessionId) {
        this.userId = userId;
        this.brand = brand;
        this.sessionId = sessionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessionIdTableEntry that = (SessionIdTableEntry) o;
        return Objects.equals(userId, that.userId) && Objects.equals(brand, that.brand) && Objects.equals(
                sessionId, that.sessionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, brand, sessionId);
    }

    @Override
    public String toString() {
        return "SessionIdTableEntry{" + "userId=" + userId + ", brand='" + brand + '\'' + ", sessionId='" + sessionId + '\'' + '}';
    }
}
