package businessObjects.db.clickhouse.sessionIdTable;


import helpers.data.ClientHelper;

public class SessionIdTableEntryFactory {
    public static SessionIdTableEntry sessionIdTableEntryForConnectionSearch(ClientHelper client) {
        return new SessionIdTableEntry(
                client.getUserId(), client.getBrand().toLowerCase(), client.getSessionId()
        );
    }
}
