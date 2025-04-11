package business_objects.db.clickhouse.bo_client_fraud_types;


import java.util.Objects;

public class ClientFraudTypesObject {

    String ucid;
    String fraudTypeCode;
    String source;
    Integer isDeleted;
    String lastUpdated;

    public ClientFraudTypesObject() {
    }

    public ClientFraudTypesObject(
            String ucid, String fraudTypeCode, String source, Integer isDeleted, String lastUpdated) {
        this.ucid = ucid;
        this.fraudTypeCode = fraudTypeCode;
        this.source = source;
        this.isDeleted = isDeleted;
        this.lastUpdated = lastUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ClientFraudTypesObject that = (ClientFraudTypesObject) o;
        return Objects.equals(ucid, that.ucid) && Objects.equals(fraudTypeCode, that.fraudTypeCode) && Objects.equals(
                source, that.source) && Objects.equals(isDeleted, that.isDeleted) && Objects.equals(
                        lastUpdated, that.lastUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ucid, fraudTypeCode, source, isDeleted, lastUpdated);
    }

    @Override
    public String toString() {
        return "ClientFraudTypesObject{" + "ucid='" + ucid + '\'' + ", fraudTypeCode='" + fraudTypeCode + '\'' + ", source='" + source + '\'' + ", isDeleted='" + isDeleted + '\'' + ", lastUpdated='" + lastUpdated + '\'' + '}';
    }

    public String getUcid() {
        return ucid;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public String getFraudTypeCode() {
        return fraudTypeCode;
    }

    public void setFraudTypeCode(String fraudTypeCode) {
        this.fraudTypeCode = fraudTypeCode;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}