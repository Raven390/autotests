package business_objects.db.clickhouse.crm_tb_account_for_mt.crm_tb_account;

import java.util.Objects;

public class CrmTbAccountForMtObject {

    private Integer sourceIdSt;
    private Integer brandUid;
    private String brand;
    private String regulator;
    private Integer userId;
    private String ucid;
    private String uid;
    private Integer account;
    private Integer serverIdSt;
    private String serverName;
    private Integer accountTypeId;
    private String accountType;
    private String accountGroup;
    private String platform;
    private String createTime;
    private String createTimeUtc;
    private String createDate;
    private String createDateUtc;
    private String accountStatus;
    private String lastLogin;
    private String lastLoginUtc;
    private String lastOrder;
    private String lastOrderUtc;
    private Double balance;
    private String currency;
    private Double balanceUsd;
    private Double equity;
    private Double credit;
    private Double pnl;
    private Integer leverage;
    private Double marginFree;
    private Integer isRebateAccount;
    private Integer rebateAccountNr;
    private Integer ibId;
    private Integer pId;
    private Integer isSwapFree;
    private Integer isPamm;
    private Integer isCent;
    private Integer isArchive;
    private Integer isHidden;
    private Integer isDel;
    private Integer isDeleted;
    private String lastUpdated;
    private String internalComment;
    private Integer isTest;

    public CrmTbAccountForMtObject() {}

    public CrmTbAccountForMtObject(
            Integer sourceIdSt,
            Integer brandUid,
            String brand,
            String regulator,
            Integer userId,
            String ucid,
            Integer account,
            Integer serverIdSt,
            String serverName,
            Integer accountTypeId,
            String accountType,
            String accountGroup,
            String platform,
            String createTime,
            String createTimeUtc,
            String createDate,
            String createDateUtc,
            String accountStatus,
            String lastLogin,
            String lastLoginUtc,
            String lastOrder,
            String lastOrderUtc,
            Double balance,
            String currency,
            Double balanceUsd,
            Double equity,
            Double credit,
            Double pnl,
            Integer leverage,
            Double marginFree,
            Integer isRebateAccount,
            Integer rebateAccountNr,
            Integer ibId,
            Integer pId,
            Integer isSwapFree,
            Integer isPamm,
            Integer isCent,
            Integer isArchive,
            Integer isHidden,
            Integer isDel,
            Integer isDeleted,
            String lastUpdated,
            String internalComment,
            Integer isTest) {
        this.setSourceIdSt(sourceIdSt);
        this.setBrandUid(brandUid);
        this.setBrand(brand);
        this.setRegulator(regulator);
        this.setUserId(userId);
        this.setUcid(ucid);
        this.setAccount(account);
        this.setServerIdSt(serverIdSt);
        this.setServerName(serverName);
        this.setAccountTypeId(accountTypeId);
        this.setAccountType(accountType);
        this.setAccountGroup(accountGroup);
        this.setPlatform(platform);
        this.setCreateTime(createTime);
        this.setCreateTimeUtc(createTimeUtc);
        this.setCreateDate(createDate);
        this.setCreateDateUtc(createDateUtc);
        this.setAccountStatus(accountStatus);
        this.setLastLogin(lastLogin);
        this.setLastLoginUtc(lastLoginUtc);
        this.setLastOrder(lastOrder);
        this.setLastOrderUtc(lastOrderUtc);
        this.setBalance(balance);
        this.setCurrency(currency);
        this.setBalanceUsd(balanceUsd);
        this.setEquity(equity);
        this.setCredit(credit);
        this.setPnl(pnl);
        this.setLeverage(leverage);
        this.setMarginFree(marginFree);
        this.setIsRebateAccount(isRebateAccount);
        this.setRebateAccountNr(rebateAccountNr);
        this.setIbId(ibId);
        this.setpId(pId);
        this.setIsSwapFree(isSwapFree);
        this.setIsPamm(isPamm);
        this.setIsCent(isCent);
        this.setIsArchive(isArchive);
        this.setIsHidden(isHidden);
        this.setIsDel(isDel);
        this.setIsDeleted(isDeleted);
        this.setLastUpdated(lastUpdated);
        this.setInternalComment(internalComment);
        this.setIsTest(isTest);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CrmTbAccountForMtObject that = (CrmTbAccountForMtObject) o;
        return Objects.equals(getSourceIdSt(), that.getSourceIdSt())
                && Objects.equals(getBrandUid(), that.getBrandUid())
                && Objects.equals(getBrand(), that.getBrand())
                && Objects.equals(getRegulator(), that.getRegulator())
                && Objects.equals(getUserId(), that.getUserId())
                && Objects.equals(getUcid(), that.getUcid())
                && Objects.equals(getUid(), that.getUid())
                && Objects.equals(getAccount(), that.getAccount())
                && Objects.equals(getServerIdSt(), that.getServerIdSt())
                && Objects.equals(getServerName(), that.getServerName())
                && Objects.equals(getAccountTypeId(), that.getAccountTypeId())
                && Objects.equals(getAccountType(), that.getAccountType())
                && Objects.equals(getAccountGroup(), that.getAccountGroup())
                && Objects.equals(getPlatform(), that.getPlatform())
                && Objects.equals(getCreateTime(), that.getCreateTime())
                && Objects.equals(getCreateTimeUtc(), that.getCreateTimeUtc())
                && Objects.equals(getCreateDate(), that.getCreateDate())
                && Objects.equals(getCreateDateUtc(), that.getCreateDateUtc())
                && Objects.equals(getAccountStatus(), that.getAccountStatus())
                && Objects.equals(getLastLogin(), that.getLastLogin())
                && Objects.equals(getLastLoginUtc(), that.getLastLoginUtc())
                && Objects.equals(getLastOrder(), that.getLastOrder())
                && Objects.equals(getLastOrderUtc(), that.getLastOrderUtc())
                && Objects.equals(getBalance(), that.getBalance())
                && Objects.equals(getCurrency(), that.getCurrency())
                && Objects.equals(getBalanceUsd(), that.getBalanceUsd())
                && Objects.equals(getEquity(), that.getEquity())
                && Objects.equals(getCredit(), that.getCredit())
                && Objects.equals(getPnl(), that.getPnl())
                && Objects.equals(getLeverage(), that.getLeverage())
                && Objects.equals(getMarginFree(), that.getMarginFree())
                && Objects.equals(getIsRebateAccount(), that.getIsRebateAccount())
                && Objects.equals(getRebateAccountNr(), that.getRebateAccountNr())
                && Objects.equals(getIbId(), that.getIbId())
                && Objects.equals(getpId(), that.getpId())
                && Objects.equals(getIsSwapFree(), that.getIsSwapFree())
                && Objects.equals(getIsPamm(), that.getIsPamm())
                && Objects.equals(getIsCent(), that.getIsCent())
                && Objects.equals(getIsArchive(), that.getIsArchive())
                && Objects.equals(getIsHidden(), that.getIsHidden())
                && Objects.equals(getIsDel(), that.getIsDel())
                && Objects.equals(getIsDeleted(), that.getIsDeleted())
                && Objects.equals(getLastUpdated(), that.getLastUpdated())
                && Objects.equals(getInternalComment(), that.getInternalComment())
                && Objects.equals(getIsTest(), that.getIsTest());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getSourceIdSt(),
                getBrandUid(),
                getBrand(),
                getRegulator(),
                getUserId(),
                getUcid(),
                getUid(),
                getAccount(),
                getServerIdSt(),
                getServerName(),
                getAccountTypeId(),
                getAccountType(),
                getAccountGroup(),
                getPlatform(),
                getCreateTime(),
                getCreateTimeUtc(),
                getCreateDate(),
                getCreateDateUtc(),
                getAccountStatus(),
                getLastLogin(),
                getLastLoginUtc(),
                getLastOrder(),
                getLastOrderUtc(),
                getBalance(),
                getCurrency(),
                getBalanceUsd(),
                getEquity(),
                getCredit(),
                getPnl(),
                getLeverage(),
                getMarginFree(),
                getIsRebateAccount(),
                getRebateAccountNr(),
                getIbId(),
                getpId(),
                getIsSwapFree(),
                getIsPamm(),
                getIsCent(),
                getIsArchive(),
                getIsHidden(),
                getIsDel(),
                getIsDeleted(),
                getLastUpdated(),
                getInternalComment(),
                getIsTest());
    }

    @Override
    public String toString() {
        return "CrmTbAccountForMtObject{" + "sourceIdSt=" + getSourceIdSt() + ", brandUid=" + getBrandUid()
                + ", brand='" + getBrand() + '\'' + ", regulator='" + getRegulator() + '\'' + ", userId=" + getUserId()
                + ", ucid='" + getUcid() + '\'' + ", uid='" + getUid() + '\'' + ", account=" + getAccount()
                + ", serverIdSt=" + getServerIdSt() + ", serverName='" + getServerName() + '\'' + ", accountTypeId="
                + getAccountTypeId() + ", accountType='" + getAccountType() + '\'' + ", accountGroup='"
                + getAccountGroup() + '\'' + ", platform='" + getPlatform() + '\'' + ", createTime='" + getCreateTime()
                + '\'' + ", createTimeUtc='" + getCreateTimeUtc() + '\'' + ", createDate='" + getCreateDate() + '\''
                + ", createDateUtc='" + getCreateDateUtc() + '\'' + ", accountStatus='" + getAccountStatus() + '\''
                + ", lastLogin='" + getLastLogin() + '\'' + ", lastLoginUtc='" + getLastLoginUtc() + '\''
                + ", lastOrder='" + getLastOrder() + '\'' + ", lastOrderUtc='" + getLastOrderUtc() + '\'' + ", balance="
                + getBalance() + ", currency='" + getCurrency() + '\'' + ", balanceUsd=" + getBalanceUsd() + ", equity="
                + getEquity() + ", credit=" + getCredit() + ", pnl=" + getPnl() + ", leverage=" + getLeverage()
                + ", marginFree=" + getMarginFree() + ", isRebateAccount=" + getIsRebateAccount() + ", rebateAccountNr="
                + getRebateAccountNr() + ", ibId=" + getIbId() + ", pId=" + getpId() + ", isSwapFree=" + getIsSwapFree()
                + ", isPamm=" + getIsPamm() + ", isCent=" + getIsCent() + ", isArchive=" + getIsArchive()
                + ", isHidden=" + getIsHidden() + ", isDel=" + getIsDel() + ", isDeleted=" + getIsDeleted()
                + ", lastUpdated='" + getLastUpdated() + '\'' + ", internalComment='" + getInternalComment() + '\''
                + ", isTest=" + getIsTest() + '}';
    }

    public Integer getSourceIdSt() {
        return sourceIdSt;
    }

    public void setSourceIdSt(Integer sourceIdSt) {
        this.sourceIdSt = sourceIdSt;
    }

    public Integer getBrandUid() {
        return brandUid;
    }

    public void setBrandUid(Integer brandUid) {
        this.brandUid = brandUid;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUcid() {
        return ucid;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getAccount() {
        return account;
    }

    public void setAccount(Integer account) {
        this.account = account;
    }

    public Integer getServerIdSt() {
        return serverIdSt;
    }

    public void setServerIdSt(Integer serverIdSt) {
        this.serverIdSt = serverIdSt;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public Integer getAccountTypeId() {
        return accountTypeId;
    }

    public void setAccountTypeId(Integer accountTypeId) {
        this.accountTypeId = accountTypeId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountGroup() {
        return accountGroup;
    }

    public void setAccountGroup(String accountGroup) {
        this.accountGroup = accountGroup;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
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

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateDateUtc() {
        return createDateUtc;
    }

    public void setCreateDateUtc(String createDateUtc) {
        this.createDateUtc = createDateUtc;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getLastLoginUtc() {
        return lastLoginUtc;
    }

    public void setLastLoginUtc(String lastLoginUtc) {
        this.lastLoginUtc = lastLoginUtc;
    }

    public String getLastOrder() {
        return lastOrder;
    }

    public void setLastOrder(String lastOrder) {
        this.lastOrder = lastOrder;
    }

    public String getLastOrderUtc() {
        return lastOrderUtc;
    }

    public void setLastOrderUtc(String lastOrderUtc) {
        this.lastOrderUtc = lastOrderUtc;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getBalanceUsd() {
        return balanceUsd;
    }

    public void setBalanceUsd(Double balanceUsd) {
        this.balanceUsd = balanceUsd;
    }

    public Double getEquity() {
        return equity;
    }

    public void setEquity(Double equity) {
        this.equity = equity;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public Double getPnl() {
        return pnl;
    }

    public void setPnl(Double pnl) {
        this.pnl = pnl;
    }

    public Integer getLeverage() {
        return leverage;
    }

    public void setLeverage(Integer leverage) {
        this.leverage = leverage;
    }

    public Double getMarginFree() {
        return marginFree;
    }

    public void setMarginFree(Double marginFree) {
        this.marginFree = marginFree;
    }

    public Integer getIsRebateAccount() {
        return isRebateAccount;
    }

    public void setIsRebateAccount(Integer isRebateAccount) {
        this.isRebateAccount = isRebateAccount;
    }

    public Integer getRebateAccountNr() {
        return rebateAccountNr;
    }

    public void setRebateAccountNr(Integer rebateAccountNr) {
        this.rebateAccountNr = rebateAccountNr;
    }

    public Integer getIbId() {
        return ibId;
    }

    public void setIbId(Integer ibId) {
        this.ibId = ibId;
    }

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }

    public Integer getIsSwapFree() {
        return isSwapFree;
    }

    public void setIsSwapFree(Integer isSwapFree) {
        this.isSwapFree = isSwapFree;
    }

    public Integer getIsPamm() {
        return isPamm;
    }

    public void setIsPamm(Integer isPamm) {
        this.isPamm = isPamm;
    }

    public Integer getIsCent() {
        return isCent;
    }

    public void setIsCent(Integer isCent) {
        this.isCent = isCent;
    }

    public Integer getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(Integer isArchive) {
        this.isArchive = isArchive;
    }

    public Integer getIsHidden() {
        return isHidden;
    }

    public void setIsHidden(Integer isHidden) {
        this.isHidden = isHidden;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
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

    public String getInternalComment() {
        return internalComment;
    }

    public void setInternalComment(String internalComment) {
        this.internalComment = internalComment;
    }

    public Integer getIsTest() {
        return isTest;
    }

    public void setIsTest(Integer isTest) {
        this.isTest = isTest;
    }
}
