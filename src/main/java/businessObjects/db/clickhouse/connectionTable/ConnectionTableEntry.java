package businessObjects.db.clickhouse.connectionTable;

import java.util.Objects;

public class ConnectionTableEntry {

    public String userFrom;
    public String userTo;
    public String degreeConnection;
    public Double connectionScore;
    public String connectionInfo;
    public String datetime;

    public ConnectionTableEntry() {
    }

    public ConnectionTableEntry(String userFrom, String userTo, String degreeConnection, Double connectionScore,
            String connectionInfo, String datetime) {
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.degreeConnection = degreeConnection;
        this.connectionScore = connectionScore;
        this.connectionInfo = connectionInfo;
        this.datetime = datetime;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ConnectionTableEntry that = (ConnectionTableEntry) o;
        return Objects.equals(userFrom, that.userFrom) && Objects.equals(userTo, that.userTo) && Objects.equals(degreeConnection, that.degreeConnection) && Objects.equals(connectionScore, that.connectionScore) && Objects.equals(connectionInfo, that.connectionInfo) && Objects.equals(datetime, that.datetime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userFrom, userTo, degreeConnection, connectionScore, connectionInfo, datetime);
    }

    @Override
    public String toString() {
        return "ConnectionTableEntry{" + "userFrom='" + userFrom + '\'' + ", userTo='" + userTo + '\'' + ", degreeConnection='" + degreeConnection + '\'' + ", connectionScore=" + connectionScore + ", connectionInfo='" + connectionInfo + '\'' + ", datetime='" + datetime + '\'' + '}';
    }
}
