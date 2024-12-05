package businessObjects.db.clickhouse.csTbConnectionTableV3;

import java.util.Objects;

public class ConnectionTableEntryV3 {

    public String userFrom;
    public String userTo;
    public String degreeConnection;
    public Double connectionScore;
    public String connectionInfo;
    public String updateTs;

    public ConnectionTableEntryV3() {
    }

    public ConnectionTableEntryV3(String userFrom, String userTo, String degreeConnection, Double connectionScore,
            String connectionInfo, String updateTs) {
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.degreeConnection = degreeConnection;
        this.connectionScore = connectionScore;
        this.connectionInfo = connectionInfo;
        this.updateTs = updateTs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConnectionTableEntryV3 that = (ConnectionTableEntryV3) o;
        return Objects.equals(userFrom, that.userFrom) && Objects.equals(userTo, that.userTo) && Objects.equals(degreeConnection, that.degreeConnection) && Objects.equals(connectionScore, that.connectionScore) && Objects.equals(connectionInfo, that.connectionInfo) && Objects.equals(updateTs, that.updateTs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userFrom, userTo, degreeConnection, connectionScore, connectionInfo, updateTs);
    }

    @Override
    public String toString() {
        return "ConnectionTableEntryV3{" + "userFrom='" + userFrom + '\'' + ", userTo='" + userTo + '\'' + ", degreeConnection='" + degreeConnection + '\'' + ", connectionScore=" + connectionScore + ", connectionInfo='" + connectionInfo + '\'' + ", updateTs='" + updateTs + '\'' + '}';
    }
}
