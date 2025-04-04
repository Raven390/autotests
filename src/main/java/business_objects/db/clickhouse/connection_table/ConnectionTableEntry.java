package business_objects.db.clickhouse.connection_table;

import java.util.List;
import java.util.Objects;

public class ConnectionTableEntry {

    public String userFrom;
    public String userTo;
    public String degreeConnection;
    public Double connectionScore;
    public String connectionInfo;
    public String datetime;

    public ConnectionTableEntry(String userFrom, String userTo, String degreeConnection, Double connectionScore,
            List<ConnectionInfo> connectionInfo, String datetime) {
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.degreeConnection = degreeConnection;
        this.connectionScore = connectionScore;
        this.connectionInfo = connectionInfoToString(connectionInfo);
        this.datetime = datetime;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ConnectionTableEntry that = (ConnectionTableEntry) o;
        return Objects.equals(userFrom, that.userFrom) && Objects.equals(userTo, that.userTo) && Objects.equals(
                degreeConnection, that.degreeConnection) && Objects.equals(connectionScore, that.connectionScore) && Objects.equals(
                        connectionInfo, that.connectionInfo) && Objects.equals(datetime, that.datetime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userFrom, userTo, degreeConnection, connectionScore, connectionInfo, datetime);
    }

    @Override
    public String toString() {
        return "ConnectionTableEntry{" + "userFrom='" + userFrom + '\'' + ", userTo='" + userTo + '\'' + ", degreeConnection='" + degreeConnection + '\'' + ", connectionScore=" + connectionScore + ", connectionInfo=" + connectionInfo + ", datetime='" + datetime + '\'' + '}';
    }

    public static class ConnectionInfo {
        public String connectionAttributeName;
        public String connectionAttributeValue;
        public String sourceAttributeValue;
        public String relationType;

        public ConnectionInfo(
                String connectionAttributeName, String connectionAttributeValue, String sourceAttributeValue,
                String relationType) {
            this.connectionAttributeName = connectionAttributeName;
            this.connectionAttributeValue = connectionAttributeValue;
            this.sourceAttributeValue = sourceAttributeValue;
            this.relationType = relationType;
        }

        @Override
        public String toString() {
            return "{" + "\"connectionAttributeName\":\"" + connectionAttributeName + '\"' + ", \"connectionAttributeValue\":\"" + connectionAttributeValue + '\"' + ", \"sourceAttributeValue\":\"" + sourceAttributeValue + '\"' + ", \"relationType\":\"" + relationType + '\"' + '}';
        }
    }

    public static String connectionInfoToString(List<ConnectionInfo> connectionInfos) {
//
        StringBuilder bld = new StringBuilder();
        bld.append("[");
        int counter = connectionInfos.size();
        for (ConnectionInfo i : connectionInfos) {
            String infoString = i.toString();
            bld.append(infoString);
            if (counter > 1) {
                bld.append(",");
                counter -= 1;
            }
        }
        bld.append("]");
        return bld.toString();
    }
}
