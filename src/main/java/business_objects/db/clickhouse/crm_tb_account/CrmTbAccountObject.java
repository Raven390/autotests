package business_objects.db.clickhouse.crm_tb_account;

import java.util.Objects;

public class CrmTbAccountObject {

    public Integer sourceIdSt;
    public Integer brandUid;
    public String brand;
    public String regulator;
    public Integer userId;
    public String ucid;
    public Integer account;
    public Integer serverIdSt;
    public String serverName;
    public Integer accountTypeId;
    public String accountType;
    public String accountGroup;
    public String platform;
    public String createTime;
    public String createTimeUtc;
    public String createDate;
    public String createDateUtc;
    public String accountStatus;
    public String lastLogin;
    public String lastLoginUtc;
    public String lastOrder;
    public String lastOrderUtc;
    public Double balance;
    public String currency;
    public Double balanceUsd;
    public Double equity;
    public Double credit;
    public Double pnl;
    public Integer leverage;
    public Double marginFree;
    public Integer isRebateAccount;
    public Integer rebateAccountNr;
    public Integer ibId;
    public Integer pId;
    public Integer isSwapFree;
    public Integer isPamm;
    public Integer isCent;
    public Integer isArchive;
    public Integer isHidden;
    public Integer isDel;
    public Integer isDeleted;
    public String lastUpdated;
    public String internalComment;

    public CrmTbAccountObject() {
    }

    public CrmTbAccountObject(
            Integer sourceIdSt, Integer brandUid, String brand, String regulator, Integer userId, String ucid,
            String uid,
            Integer account, Integer serverIdSt, String serverName, Integer accountTypeId, String accountType,
            String accountGroup, String platform, String createTime, String createTimeUtc, String createDate,
            String createDateUtc, String accountStatus, String lastLogin, String lastLoginUtc, String lastOrder,
            String lastOrderUtc, Double balance, String currency, Double balanceUsd, Double equity, Double credit,
            Double pnl, Integer leverage, Double marginFree, Integer isRebateAccount, Integer rebateAccountNr,
            Integer ibId,
            Integer pId, Integer isSwapFree, Integer isPamm, Integer isCent, Integer isArchive, Integer isHidden,
            Integer isDel, Integer isDeleted, String lastUpdated, String internalComment) {
        this.sourceIdSt = sourceIdSt;
        this.brandUid = brandUid;
        this.brand = brand;
        this.regulator = regulator;
        this.userId = userId;
        this.ucid = ucid;
        this.account = account;
        this.serverIdSt = serverIdSt;
        this.serverName = serverName;
        this.accountTypeId = accountTypeId;
        this.accountType = accountType;
        this.accountGroup = accountGroup;
        this.platform = platform;
        this.createTime = createTime;
        this.createTimeUtc = createTimeUtc;
        this.createDate = createDate;
        this.createDateUtc = createDateUtc;
        this.accountStatus = accountStatus;
        this.lastLogin = lastLogin;
        this.lastLoginUtc = lastLoginUtc;
        this.lastOrder = lastOrder;
        this.lastOrderUtc = lastOrderUtc;
        this.balance = balance;
        this.currency = currency;
        this.balanceUsd = balanceUsd;
        this.equity = equity;
        this.credit = credit;
        this.pnl = pnl;
        this.leverage = leverage;
        this.marginFree = marginFree;
        this.isRebateAccount = isRebateAccount;
        this.rebateAccountNr = rebateAccountNr;
        this.ibId = ibId;
        this.pId = pId;
        this.isSwapFree = isSwapFree;
        this.isPamm = isPamm;
        this.isCent = isCent;
        this.isArchive = isArchive;
        this.isHidden = isHidden;
        this.isDel = isDel;
        this.isDeleted = isDeleted;
        this.lastUpdated = lastUpdated;
        this.internalComment = internalComment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CrmTbAccountObject that = (CrmTbAccountObject) o;
        return Objects.equals(sourceIdSt, that.sourceIdSt) && Objects.equals(brandUid, that.brandUid) && Objects.equals(
                brand, that.brand) && Objects.equals(regulator, that.regulator) && Objects.equals(
                        userId, that.userId) && Objects.equals(ucid, that.ucid) && Objects.equals(
                                account, that.account) && Objects.equals(serverIdSt, that.serverIdSt) && Objects.equals(
                                        serverName, that.serverName) && Objects.equals(accountTypeId, that.accountTypeId) && Objects.equals(
                                                accountType, that.accountType) && Objects.equals(accountGroup, that.accountGroup) && Objects.equals(
                                                        platform, that.platform) && Objects.equals(createTime, that.createTime) && Objects.equals(
                                                                createTimeUtc, that.createTimeUtc) && Objects.equals(createDate, that.createDate) && Objects.equals(
                                                                        createDateUtc, that.createDateUtc) && Objects.equals(accountStatus, that.accountStatus) && Objects.equals(
                                                                                lastLogin, that.lastLogin) && Objects.equals(lastLoginUtc, that.lastLoginUtc) && Objects.equals(
                                                                                        lastOrder, that.lastOrder) && Objects.equals(lastOrderUtc, that.lastOrderUtc) && Objects.equals(
                                                                                                balance, that.balance) && Objects.equals(currency, that.currency) && Objects.equals(
                                                                                                        balanceUsd, that.balanceUsd) && Objects.equals(equity, that.equity) && Objects.equals(
                                                                                                                credit, that.credit) && Objects.equals(pnl, that.pnl) && Objects.equals(leverage, that.leverage) && Objects.equals(
                                                                                                                        marginFree, that.marginFree) && Objects.equals(isRebateAccount, that.isRebateAccount) && Objects.equals(
                                                                                                                                rebateAccountNr, that.rebateAccountNr) && Objects.equals(ibId, that.ibId) && Objects.equals(
                                                                                                                                        pId, that.pId) && Objects.equals(isSwapFree, that.isSwapFree) && Objects.equals(isPamm, that.isPamm) && Objects.equals(
                                                                                                                                                isCent, that.isCent) && Objects.equals(isArchive, that.isArchive) && Objects.equals(
                                                                                                                                                        isHidden, that.isHidden) && Objects.equals(isDel, that.isDel) && Objects.equals(
                                                                                                                                                                isDeleted, that.isDeleted) && Objects.equals(lastUpdated, that.lastUpdated) && Objects.equals(
                                                                                                                                                                        internalComment, that.internalComment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceIdSt, brandUid, brand, regulator, userId, ucid, account, serverIdSt, serverName, accountTypeId, accountType, accountGroup, platform, createTime, createTimeUtc, createDate, createDateUtc, accountStatus, lastLogin, lastLoginUtc, lastOrder, lastOrderUtc, balance, currency, balanceUsd, equity, credit, pnl, leverage, marginFree, isRebateAccount, rebateAccountNr, ibId, pId, isSwapFree, isPamm, isCent, isArchive, isHidden, isDel, isDeleted, lastUpdated, internalComment);
    }

    @Override
    public String toString() {
        return "CrmTbAccountObject{" + "sourceIdSt=" + sourceIdSt + ", brandUid=" + brandUid + ", brand='" + brand + '\'' + ", regulator='" + regulator + '\'' + ", userId=" + userId + ", ucid='" + ucid + '\'' + ", account=" + account + ", serverIdSt=" + serverIdSt + ", serverName='" + serverName + '\'' + ", accountTypeId=" + accountTypeId + ", accountType='" + accountType + '\'' + ", accountGroup='" + accountGroup + '\'' + ", platform='" + platform + '\'' + ", createTime='" + createTime + '\'' + ", createTimeUtc='" + createTimeUtc + '\'' + ", createDate='" + createDate + '\'' + ", createDateUtc='" + createDateUtc + '\'' + ", accountStatus='" + accountStatus + '\'' + ", lastLogin='" + lastLogin + '\'' + ", lastLoginUtc='" + lastLoginUtc + '\'' + ", lastOrder='" + lastOrder + '\'' + ", lastOrderUtc='" + lastOrderUtc + '\'' + ", balance=" + balance + ", currency='" + currency + '\'' + ", balanceUsd=" + balanceUsd + ", equity=" + equity + ", credit=" + credit + ", pnl=" + pnl + ", leverage=" + leverage + ", marginFree=" + marginFree + ", isRebateAccount='" + isRebateAccount + '\'' + ", rebateAccountNr='" + rebateAccountNr + '\'' + ", ibId=" + ibId + ", pId=" + pId + ", isSwapFree=" + isSwapFree + ", isPamm=" + isPamm + ", isCent=" + isCent + ", isArchive=" + isArchive + ", isHidden=" + isHidden + ", isDel=" + isDel + ", isDelete=" + isDeleted + ", lastUpdated='" + lastUpdated + '\'' + ", internalComment='" + internalComment + '\'' + '}';
    }
}
