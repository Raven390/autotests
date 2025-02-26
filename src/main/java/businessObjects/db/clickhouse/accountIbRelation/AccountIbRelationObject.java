package businessObjects.db.clickhouse.accountIbRelation;

import java.util.Objects;

public class AccountIbRelationObject {
    private Integer userId;
    private String brand;
    private String regulator;
    private String ucid;
    private Long account;
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
    private String recordEffectiveStartDate;
    private String recordEffectiveEndDate;
    private Boolean recordActiveFlag;
    private Integer isDel;
    private String createTime;
    private String createTimeUtc;
    private String recordDeletedFlag;
    private String lastUpdated;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountIbRelationObject that = (AccountIbRelationObject) o;
        return Objects.equals(userId, that.userId) && Objects.equals(brand, that.brand) && Objects.equals(regulator, that.regulator) && Objects.equals(ucid, that.ucid) && Objects.equals(account, that.account) && Objects.equals(serverId, that.serverId) && Objects.equals(serverName, that.serverName) && Objects.equals(directIb, that.directIb) && Objects.equals(directIbLevel, that.directIbLevel) && Objects.equals(directIbRebateAccount, that.directIbRebateAccount) && Objects.equals(masterIb, that.masterIb) && Objects.equals(masterIbRebateAccount, that.masterIbRebateAccount) && Objects.equals(salesId, that.salesId) && Objects.equals(salesOrgId, that.salesOrgId) && Objects.equals(accountPIds, that.accountPIds) && Objects.equals(recordEffectiveStartDate, that.recordEffectiveStartDate) && Objects.equals(recordEffectiveEndDate, that.recordEffectiveEndDate) && Objects.equals(recordActiveFlag, that.recordActiveFlag) && Objects.equals(isDel, that.isDel) && Objects.equals(createTime, that.createTime) && Objects.equals(createTimeUtc, that.createTimeUtc) && Objects.equals(recordDeletedFlag, that.recordDeletedFlag) && Objects.equals(lastUpdated, that.lastUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, brand, regulator, ucid, account, serverId, serverName, directIb, directIbLevel, directIbRebateAccount, masterIb, masterIbRebateAccount, salesId, salesOrgId, accountPIds, recordEffectiveStartDate, recordEffectiveEndDate, recordActiveFlag, isDel, createTime, createTimeUtc, recordDeletedFlag, lastUpdated);
    }

    @Override
    public String toString() {
        return "AccountIbRelationObject{" + "userId=" + userId + ", brand='" + brand + '\'' + ", regulator='" + regulator + '\'' + ", ucid='" + ucid + '\'' + ", account=" + account + ", serverId=" + serverId + ", serverName='" + serverName + '\'' + ", directIb=" + directIb + ", directIbLevel=" + directIbLevel + ", directIbRebateAccount=" + directIbRebateAccount + ", masterIb=" + masterIb + ", masterIbRebateAccount=" + masterIbRebateAccount + ", salesId=" + salesId + ", salesOrgId=" + salesOrgId + ", accountPIds='" + accountPIds + '\'' + ", recordEffectiveStartDate='" + recordEffectiveStartDate + '\'' + ", recordEffectiveEndDate='" + recordEffectiveEndDate + '\'' + ", recordActiveFlag=" + recordActiveFlag + ", isDel=" + isDel + ", createTime='" + createTime + '\'' + ", createTimeUtc='" + createTimeUtc + '\'' + ", recordDeletedFlag='" + recordDeletedFlag + '\'' + ", lastUpdated='" + lastUpdated + '\'' + '}';
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getRegulator() {
        return regulator;
    }

    public void setRegulator(String regulator) {
        this.regulator = regulator;
    }

    public String getUcid() {
        return ucid;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public Long getAccount() {
        return account;
    }

    public void setAccount(Long account) {
        this.account = account;
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

    public String getRecordEffectiveStartDate() {
        return recordEffectiveStartDate;
    }

    public void setRecordEffectiveStartDate(String recordEffectiveStartDate) {
        this.recordEffectiveStartDate = recordEffectiveStartDate;
    }

    public String getRecordEffectiveEndDate() {
        return recordEffectiveEndDate;
    }

    public void setRecordEffectiveEndDate(String recordEffectiveEndDate) {
        this.recordEffectiveEndDate = recordEffectiveEndDate;
    }

    public Boolean getRecordActiveFlag() {
        return recordActiveFlag;
    }

    public void setRecordActiveFlag(Boolean recordActiveFlag) {
        this.recordActiveFlag = recordActiveFlag;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
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

    public String getRecordDeletedFlag() {
        return recordDeletedFlag;
    }

    public void setRecordDeletedFlag(String recordDeletedFlag) {
        this.recordDeletedFlag = recordDeletedFlag;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
