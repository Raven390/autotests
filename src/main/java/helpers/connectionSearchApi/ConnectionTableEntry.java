package helpers.connectionSearchApi;

public class ConnectionTableEntry {
    String userFrom;
    String userTo;
    Integer level;
    String attr;
    String updateTs;

    public ConnectionTableEntry() {
    }

    public ConnectionTableEntry(String userFrom, String userTo, Integer level, String attr, String updateTs) {
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.level = level;
        this.attr = attr;
        this.updateTs = updateTs;
    }
}
