package businessObjects.db.clickhouse.clientFraudTypes;

import java.util.Objects;

public class ClientFraudTypes {
    public String ucid;
    public String fraudTypeCode;
    public String source;
    public Integer isDeleted;
    public String lastUpdated;

    public ClientFraudTypes() {
    }

    public ClientFraudTypes(String ucid, String fraudTypeCode, String source, Integer isDeleted, String lastUpdated) {
        this.ucid = ucid;
        this.fraudTypeCode = fraudTypeCode;
        this.source = source;
        this.isDeleted = isDeleted;
        this.lastUpdated = lastUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ClientFraudTypes that = (ClientFraudTypes) o;
        return Objects.equals(ucid, that.ucid) && Objects.equals(fraudTypeCode, that.fraudTypeCode) && Objects.equals(source, that.source) && Objects.equals(isDeleted, that.isDeleted) && Objects.equals(lastUpdated, that.lastUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ucid, fraudTypeCode, source, isDeleted, lastUpdated);
    }

    @Override
    public String toString() {
        return "ClientFraudTypes{" + "ucid='" + ucid + '\'' + ", fraudTypeCode='" + fraudTypeCode + '\'' + ", source='" + source + '\'' + ", isDeleted=" + isDeleted + ", lastUpdated='" + lastUpdated + '\'' + '}';
    }
}
