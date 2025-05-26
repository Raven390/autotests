package business_objects.db.clickhouse.s3___dim_client;


public class S3DimClientObject {
    protected Long userId;
    protected String orgName;
    protected String userName;
    protected String brand;
    protected Integer recordActiveFlag;

    @Override
    public String toString() {
        return "S3DimClientObject{" + "userId=" + userId + ", orgName='" + orgName + '\'' + ", userName='" + userName + '\'' + ", brand='" + brand + '\'' + ", recordActiveFlag=" + recordActiveFlag + '}';
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Integer getRecordActiveFlag() {
        return recordActiveFlag;
    }

    public void setRecordActiveFlag(Integer recordActiveFlag) {
        this.recordActiveFlag = recordActiveFlag;
    }

    public S3DimClientObject() {
    }

}
