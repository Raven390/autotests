package businessObjects.db.clickhouse.emailTable;

import java.util.Objects;

public class EmailTableEntry {

    public String ucid;
    public String email;

    public EmailTableEntry() {
    }

    public EmailTableEntry(String ucid, String email) {
        this.ucid = ucid;
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EmailTableEntry that = (EmailTableEntry) o;
        return Objects.equals(ucid, that.ucid) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ucid, email);
    }

    @Override
    public String toString() {
        return "EmailTableEntry{" + "ucid='" + ucid + '\'' + ", email='" + email + '\'' + '}';
    }
}
