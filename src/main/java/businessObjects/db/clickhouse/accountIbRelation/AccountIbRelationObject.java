package businessObjects.db.clickhouse.accountIbRelation;

import java.util.Objects;

public class AccountIbRelationObject {
    private Integer sourceIdSt;
    private Integer userId;
    private Long account;
    private String regulator;
    private String brand;
    private String ucid;
    private Integer serverId;
    private String serverName;
    private Integer directIb;
    private Integer directIbLevel;
    private Integer directIbRebateAccount;
    private Integer masterIb;
    private Integer masterIbRebateAccount;
    private Integer salesId;
    private Integer salesOrgId;
    private String accountPIds;
    private Boolean recordActiveFlag;
    private String createTime;
    private String createTimeUtc;
    private String lastUpdated;

    public Integer getSourceIdSt() {
        return sourceIdSt;
    }

    public void setSourceIdSt(Integer sourceIdSt) {
        this.sourceIdSt = sourceIdSt;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Long getAccount() {
        return account;
    }

    public void setAccount(Long account) {
        this.account = account;
    }

    public String getRegulator() {
        return regulator;
    }

    public void setRegulator(String regulator) {
        this.regulator = regulator;
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

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public Integer getDirectIb() {
        return directIb;
    }

    public void setDirectIb(Integer directIb) {
        this.directIb = directIb;
    }

    public Integer getDirectIbLevel() {
        return directIbLevel;
    }

    public void setDirectIbLevel(Integer directIbLevel) {
        this.directIbLevel = directIbLevel;
    }

    public Integer getDirectIbRebateAccount() {
        return directIbRebateAccount;
    }

    public void setDirectIbRebateAccount(Integer directIbRebateAccount) {
        this.directIbRebateAccount = directIbRebateAccount;
    }

    public Integer getMasterIb() {
        return masterIb;
    }

    public void setMasterIb(Integer masterIb) {
        this.masterIb = masterIb;
    }

    public Integer getMasterIbRebateAccount() {
        return masterIbRebateAccount;
    }

    public void setMasterIbRebateAccount(Integer masterIbRebateAccount) {
        this.masterIbRebateAccount = masterIbRebateAccount;
    }

    public Integer getSalesId() {
        return salesId;
    }

    public void setSalesId(Integer salesId) {
        this.salesId = salesId;
    }

    public Integer getSalesOrgId() {
        return salesOrgId;
    }

    public void setSalesOrgId(Integer salesOrgId) {
        this.salesOrgId = salesOrgId;
    }

    public String getAccountPIds() {
        return accountPIds;
    }

    public void setAccountPIds(String accountPIds) {
        this.accountPIds = accountPIds;
    }

    public Boolean getRecordActiveFlag() {
        return recordActiveFlag;
    }

    public void setRecordActiveFlag(Boolean recordActiveFlag) {
        this.recordActiveFlag = recordActiveFlag;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTimeUtc() {
        return createTimeUtc;
    }

    public void setCreateTimeUtc(String createTimeUtc) {
        this.createTimeUtc = createTimeUtc;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountIbRelationObject that = (AccountIbRelationObject) o;
        return Objects.equals(sourceIdSt, that.sourceIdSt) && Objects.equals(userId, that.userId) && Objects.equals(account, that.account) && Objects.equals(regulator, that.regulator) && Objects.equals(brand, that.brand) && Objects.equals(ucid, that.ucid) && Objects.equals(serverId, that.serverId) && Objects.equals(serverName, that.serverName) && Objects.equals(directIb, that.directIb) && Objects.equals(directIbLevel, that.directIbLevel) && Objects.equals(directIbRebateAccount, that.directIbRebateAccount) && Objects.equals(masterIb, that.masterIb) && Objects.equals(masterIbRebateAccount, that.masterIbRebateAccount) && Objects.equals(salesId, that.salesId) && Objects.equals(salesOrgId, that.salesOrgId) && Objects.equals(accountPIds, that.accountPIds) && Objects.equals(recordActiveFlag, that.recordActiveFlag) && Objects.equals(createTime, that.createTime) && Objects.equals(createTimeUtc, that.createTimeUtc) && Objects.equals(lastUpdated, that.lastUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceIdSt, userId, account, regulator, brand, ucid, serverId, serverName, directIb, directIbLevel, directIbRebateAccount, masterIb, masterIbRebateAccount, salesId, salesOrgId, accountPIds, recordActiveFlag, createTime, createTimeUtc, lastUpdated);
    }

    @Override
    public String toString() {
        return "accountIbRelationObject{" + "sourceIdSt=" + sourceIdSt + ", userId=" + userId + ", login=" + account + ", regulator='" + regulator + '\'' + ", brand='" + brand + '\'' + ", ucid='" + ucid + '\'' + ", serverId=" + serverId + ", serverName='" + serverName + '\'' + ", directIb=" + directIb + ", directIbLevel=" + directIbLevel + ", directIbRebateAccount=" + directIbRebateAccount + ", masterIb=" + masterIb + ", masterIbRebateAccount=" + masterIbRebateAccount + ", salesId=" + salesId + ", salesOrgId=" + salesOrgId + ", accountPIds='" + accountPIds + '\'' + ", recordActiveFlag=" + recordActiveFlag + ", createTime='" + createTime + '\'' + ", createTimeUtc='" + createTimeUtc + '\'' + ", lastUpdated='" + lastUpdated + '\'' + '}';
    }
}
