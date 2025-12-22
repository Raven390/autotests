package business_objects.db.clickhouse.s3___dim_client;

import java.util.Objects;

public class S3DimClientObject {
    protected Long userId;
    protected String orgName;
    protected String userName;
    protected String brand;
    protected String ucid;
    protected Integer recordActiveFlag;
    protected Float recordVersion;
    protected String last_process_date;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        S3DimClientObject that = (S3DimClientObject) o;
        return Objects.equals(userId, that.userId)
                && Objects.equals(orgName, that.orgName)
                && Objects.equals(userName, that.userName)
                && Objects.equals(brand, that.brand)
                && Objects.equals(ucid, that.ucid)
                && Objects.equals(recordActiveFlag, that.recordActiveFlag)
                && Objects.equals(recordVersion, that.recordVersion)
                && Objects.equals(last_process_date, that.last_process_date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, orgName, userName, brand, ucid, recordActiveFlag, recordVersion, last_process_date);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getUcid() {
        return ucid;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public Integer getRecordActiveFlag() {
        return recordActiveFlag;
    }

    public void setRecordActiveFlag(Integer recordActiveFlag) {
        this.recordActiveFlag = recordActiveFlag;
    }

    public Float getRecordVersion() {
        return recordVersion;
    }

    public void setRecordVersion(Float recordVersion) {
        this.recordVersion = recordVersion;
    }

    public String getLast_process_date() {
        return last_process_date;
    }

    public void setLast_process_date(String last_process_date) {
        this.last_process_date = last_process_date;
    }
}
