package businessObjects.db.clickhouse.webSessionTable;

import java.util.Objects;

public class WebSessionTableEntry {

    public Integer userId;
    public String brand;
    public String webSessionId;


    public WebSessionTableEntry() {
    }

    public WebSessionTableEntry(Integer userId, String brand, String webSessionId) {
        this.userId = userId;
        this.brand = brand;
        this.webSessionId = webSessionId;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WebSessionTableEntry that = (WebSessionTableEntry) o;
        return Objects.equals(userId, that.userId) && Objects.equals(brand, that.brand) && Objects.equals(
                webSessionId, that.webSessionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, brand, webSessionId);
    }

    @Override
    public String toString() {
        return "WebSessionTableEntry{" + "userId=" + userId + ", brand='" + brand + '\'' + ", webSessionId='" + webSessionId + '\'' + '}';
    }
}
