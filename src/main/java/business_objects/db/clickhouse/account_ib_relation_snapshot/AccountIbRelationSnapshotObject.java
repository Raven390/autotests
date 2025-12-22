package business_objects.db.clickhouse.account_ib_relation_snapshot;

import java.util.Objects;

public class AccountIbRelationSnapshotObject {
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
    private String recordEffectiveStartDate;
    private String recordEffectiveEndDate;
    private Boolean recordActiveFlag;
    private Integer isDel;
    private String recordDeletedFlag;
    private String lastUpdated;
    private Integer isRebateAccount;

    public AccountIbRelationSnapshotObject() {}

    public AccountIbRelationSnapshotObject(
            Integer userId,
            String brand,
            String regulator,
            String ucid,
            Long account,
            Integer serverId,
            String serverName,
            Integer directIb,
            Integer directIbLevel,
            Integer directIbRebateAccount,
            Integer masterIb,
            Integer masterIbRebateAccount,
            String recordEffectiveStartDate,
            String recordEffectiveEndDate,
            Boolean recordActiveFlag,
            Integer isDel,
            String recordDeletedFlag,
            String lastUpdated,
            Integer isRebateAccount) {
        this.userId = userId;
        this.brand = brand;
        this.regulator = regulator;
        this.ucid = ucid;
        this.account = account;
        this.serverId = serverId;
        this.serverName = serverName;
        this.directIb = directIb;
        this.directIbLevel = directIbLevel;
        this.directIbRebateAccount = directIbRebateAccount;
        this.masterIb = masterIb;
        this.masterIbRebateAccount = masterIbRebateAccount;
        this.recordEffectiveStartDate = recordEffectiveStartDate;
        this.recordEffectiveEndDate = recordEffectiveEndDate;
        this.recordActiveFlag = recordActiveFlag;
        this.isDel = isDel;
        this.recordDeletedFlag = recordDeletedFlag;
        this.lastUpdated = lastUpdated;
        this.isRebateAccount = isRebateAccount;
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

    public Integer getIsRebateAccount() {
        return isRebateAccount;
    }

    public void setIsRebateAccount(Integer isRebateAccount) {
        this.isRebateAccount = isRebateAccount;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AccountIbRelationSnapshotObject that = (AccountIbRelationSnapshotObject) o;
        return Objects.equals(userId, that.userId)
                && Objects.equals(brand, that.brand)
                && Objects.equals(regulator, that.regulator)
                && Objects.equals(ucid, that.ucid)
                && Objects.equals(account, that.account)
                && Objects.equals(serverId, that.serverId)
                && Objects.equals(serverName, that.serverName)
                && Objects.equals(directIb, that.directIb)
                && Objects.equals(directIbLevel, that.directIbLevel)
                && Objects.equals(directIbRebateAccount, that.directIbRebateAccount)
                && Objects.equals(masterIb, that.masterIb)
                && Objects.equals(masterIbRebateAccount, that.masterIbRebateAccount)
                && Objects.equals(recordEffectiveStartDate, that.recordEffectiveStartDate)
                && Objects.equals(recordEffectiveEndDate, that.recordEffectiveEndDate)
                && Objects.equals(recordActiveFlag, that.recordActiveFlag)
                && Objects.equals(isDel, that.isDel)
                && Objects.equals(recordDeletedFlag, that.recordDeletedFlag)
                && Objects.equals(lastUpdated, that.lastUpdated)
                && Objects.equals(isRebateAccount, that.isRebateAccount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                userId,
                brand,
                regulator,
                ucid,
                account,
                serverId,
                serverName,
                directIb,
                directIbLevel,
                directIbRebateAccount,
                masterIb,
                masterIbRebateAccount,
                recordEffectiveStartDate,
                recordEffectiveEndDate,
                recordActiveFlag,
                isDel,
                recordDeletedFlag,
                lastUpdated,
                isRebateAccount);
    }

    @Override
    public String toString() {
        return "AccountIbRelationSnapshotObject{" + "userId=" + userId + ", brand='" + brand + '\'' + ", regulator='"
                + regulator + '\'' + ", ucid='" + ucid + '\'' + ", account=" + account + ", serverId=" + serverId
                + ", serverName='" + serverName + '\'' + ", directIb=" + directIb + ", directIbLevel=" + directIbLevel
                + ", directIbRebateAccount=" + directIbRebateAccount + ", masterIb=" + masterIb
                + ", masterIbRebateAccount=" + masterIbRebateAccount + ", recordEffectiveStartDate='"
                + recordEffectiveStartDate + '\'' + ", recordEffectiveEndDate='" + recordEffectiveEndDate + '\''
                + ", recordActiveFlag=" + recordActiveFlag + ", isDel=" + isDel + ", recordDeletedFlag='"
                + recordDeletedFlag + '\'' + ", lastUpdated='" + lastUpdated + '\'' + ", isRebateAccount="
                + isRebateAccount + '}';
    }
}
