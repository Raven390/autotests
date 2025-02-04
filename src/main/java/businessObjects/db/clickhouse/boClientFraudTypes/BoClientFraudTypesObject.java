package businessObjects.db.clickhouse.boClientFraudTypes;


import java.util.Objects;

public class BoClientFraudTypesObject {

    public String ucid;
    public Integer fraudTypeId;
    public String fraudTypeCode;
    public String updateTimeUtc;

    public BoClientFraudTypesObject() {
    }

    public BoClientFraudTypesObject(String ucid, Integer fraudTypeId, String fraudTypeCode) {
        this.ucid = ucid;
        this.fraudTypeId = fraudTypeId;
        this.fraudTypeCode = fraudTypeCode;
    }

    public BoClientFraudTypesObject(String ucid, Integer fraudTypeId, String fraudTypeCode, String updateTimeUtc) {
        this.ucid = ucid;
        this.fraudTypeId = fraudTypeId;
        this.fraudTypeCode = fraudTypeCode;
        this.updateTimeUtc = updateTimeUtc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoClientFraudTypesObject that = (BoClientFraudTypesObject) o;
        return Objects.equals(ucid, that.ucid) && Objects.equals(fraudTypeId, that.fraudTypeId) && Objects.equals(fraudTypeCode, that.fraudTypeCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ucid, fraudTypeId, fraudTypeCode);
    }

    @Override
    public String toString() {
        return "boClientFraudTypesObject{" + "ucid='" + ucid + '\'' + ", fraudTypeId=" + fraudTypeId + ", fraudTypeCode='" + fraudTypeCode + '\'' + '}';
    }
}