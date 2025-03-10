package businessObjects.db.clickhouse.sessionId;


import helpers.data.ClientHelper;

public class SessionIdTableEntryFactory {
    public static SessionIdTableEntry sessionIdTableEntryForConnectionSearch(ClientHelper client) {
        return new SessionIdTableEntry(
                client.getUcid(), client.getSessionId()
        );
    }

    public static SessionIdTableEntry sessionIdTableEntryForConnectionSearch(ClientHelper client, String sessionId) {
        return new SessionIdTableEntry(
                client.getUcid(), sessionId
        );
    }
}
