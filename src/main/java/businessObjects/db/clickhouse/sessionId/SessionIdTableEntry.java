package businessObjects.db.clickhouse.sessionId;

import java.util.Objects;

public class SessionIdTableEntry {

    public String ucid;
    public String sessionId;

    public SessionIdTableEntry() {
    }

    public SessionIdTableEntry(String ucid, String sessionId) {
        this.ucid = ucid;
        this.sessionId = sessionId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SessionIdTableEntry that = (SessionIdTableEntry) o;
        return Objects.equals(ucid, that.ucid) && Objects.equals(sessionId, that.sessionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ucid, sessionId);
    }

    @Override
    public String toString() {
        return "SessionIdTableEntry{" + "ucid='" + ucid + '\'' + ", sessionId='" + sessionId + '\'' + '}';
    }
}
