package business_objects.db.abuse_registry_db;

import java.util.Objects;

public class PendingProcessing {

    private String ucid;
    private String fraudTypeCode;
    private String fraudSubtypeCode;
    private Integer abuserHistoryId;

    public PendingProcessing() {
    }

    public PendingProcessing(String ucid, String fraudTypeCode, String fraudSubtypeCode, Integer abuserHistoryId) {
        this.ucid = ucid;
        this.fraudTypeCode = fraudTypeCode;
        this.fraudSubtypeCode = fraudSubtypeCode;
        this.abuserHistoryId = abuserHistoryId;
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

    public String getFraudSubtypeCode() {
        return fraudSubtypeCode;
    }

    public void setFraudSubtypeCode(String fraudSubtypeCode) {
        this.fraudSubtypeCode = fraudSubtypeCode;
    }

    public Integer getAbuserHistoryId() {
        return abuserHistoryId;
    }

    public void setAbuserHistoryId(Integer abuserHistoryId) {
        this.abuserHistoryId = abuserHistoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PendingProcessing that = (PendingProcessing) o;
        return Objects.equals(ucid, that.ucid) && Objects.equals(fraudTypeCode, that.fraudTypeCode) && Objects.equals(fraudSubtypeCode, that.fraudSubtypeCode) && Objects.equals(abuserHistoryId, that.abuserHistoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ucid, fraudTypeCode, fraudSubtypeCode, abuserHistoryId);
    }

    @Override
    public String toString() {
        return "PendingProcessing{" + "ucid='" + ucid + '\'' + ", fraudTypeCode='" + fraudTypeCode + '\'' + ", fraudSubtypeCode='" + fraudSubtypeCode + '\'' + ", abuserHistoryId=" + abuserHistoryId + '}';
    }
}
