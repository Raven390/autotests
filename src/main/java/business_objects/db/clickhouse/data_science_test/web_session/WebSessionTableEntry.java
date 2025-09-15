package business_objects.db.clickhouse.data_science_test.web_session;

import java.util.Objects;

public class WebSessionTableEntry {

    public String ucid;
    public String webSessionId;


    public WebSessionTableEntry() {
    }

    public WebSessionTableEntry(String ucid, String webSessionId) {
        this.ucid = ucid;
        this.webSessionId = webSessionId;

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        WebSessionTableEntry that = (WebSessionTableEntry) o;
        return Objects.equals(ucid, that.ucid) && Objects.equals(webSessionId, that.webSessionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ucid, webSessionId);
    }

    @Override
    public String toString() {
        return "WebSessionTableEntry{" + "ucid='" + ucid + '\'' + ", webSessionId='" + webSessionId + '\'' + '}';
    }
}
