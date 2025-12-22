package business_objects.db.clickhouse.crm_tb_withdraw_account;

import java.util.Objects;

public class CrmTbWithdrawAccountObject {

    public Integer sourceIdSt;
    public Long id;
    public Integer brandUid;
    public String brand;
    public String regulator;
    public String ucid;
    public Integer userId;
    public String bankName;
    public String bankAccountName;
    public String swiftCode;
    public String bankCard;
    public String sortCode;
    public String bankAddress;
    public String bankBranchName;
    public String accountHolderAddress;
    public String createTime;
    public String createTimeUtc;
    public String updateTime;
    public String updateTimeUtc;
    public Integer isDel;
    public String bankCity;
    public String ifscCode;
    public Integer isDeleted;
    public String lastUpdated;

    public CrmTbWithdrawAccountObject() {}

    public CrmTbWithdrawAccountObject(
            Integer sourceIdSt,
            Long id,
            Integer brandUid,
            String brand,
            String regulator,
            String ucid,
            Integer userId,
            String bankName,
            String bankAccountName,
            String swiftCode,
            String bankCard,
            String sortCode,
            String bankAddress,
            String bankBranchName,
            String accountHolderAddress,
            String createTime,
            String createTimeUtc,
            String updateTime,
            String updateTimeUtc,
            Integer isDel,
            String bankCity,
            String ifscCode,
            Integer isDeleted,
            String lastUpdated) {
        this.sourceIdSt = sourceIdSt;
        this.id = id;
        this.brandUid = brandUid;
        this.brand = brand;
        this.regulator = regulator;
        this.ucid = ucid;
        this.userId = userId;
        this.bankName = bankName;
        this.bankAccountName = bankAccountName;
        this.swiftCode = swiftCode;
        this.bankCard = bankCard;
        this.sortCode = sortCode;
        this.bankAddress = bankAddress;
        this.bankBranchName = bankBranchName;
        this.accountHolderAddress = accountHolderAddress;
        this.createTime = createTime;
        this.createTimeUtc = createTimeUtc;
        this.updateTime = updateTime;
        this.updateTimeUtc = updateTimeUtc;
        this.isDel = isDel;
        this.bankCity = bankCity;
        this.ifscCode = ifscCode;
        this.isDeleted = isDeleted;
        this.lastUpdated = lastUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CrmTbWithdrawAccountObject that = (CrmTbWithdrawAccountObject) o;
        return Objects.equals(sourceIdSt, that.sourceIdSt)
                && Objects.equals(id, that.id)
                && Objects.equals(brandUid, that.brandUid)
                && Objects.equals(brand, that.brand)
                && Objects.equals(regulator, that.regulator)
                && Objects.equals(ucid, that.ucid)
                && Objects.equals(userId, that.userId)
                && Objects.equals(bankName, that.bankName)
                && Objects.equals(bankAccountName, that.bankAccountName)
                && Objects.equals(swiftCode, that.swiftCode)
                && Objects.equals(bankCard, that.bankCard)
                && Objects.equals(sortCode, that.sortCode)
                && Objects.equals(bankAddress, that.bankAddress)
                && Objects.equals(bankBranchName, that.bankBranchName)
                && Objects.equals(accountHolderAddress, that.accountHolderAddress)
                && Objects.equals(isDel, that.isDel)
                && Objects.equals(bankCity, that.bankCity)
                && Objects.equals(ifscCode, that.ifscCode)
                && Objects.equals(isDeleted, that.isDeleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                sourceIdSt,
                id,
                brandUid,
                brand,
                regulator,
                ucid,
                userId,
                bankName,
                bankAccountName,
                swiftCode,
                bankCard,
                sortCode,
                bankAddress,
                bankBranchName,
                accountHolderAddress,
                createTime,
                createTimeUtc,
                updateTime,
                updateTimeUtc,
                isDel,
                bankCity,
                ifscCode,
                isDeleted,
                lastUpdated);
    }

    @Override
    public String toString() {
        return "CrmTbWithdrawAccountObject{" + "sourceIdSt=" + sourceIdSt + ", id=" + id + ", brandUid=" + brandUid
                + ", brand='" + brand + '\'' + ", regulator='" + regulator + '\'' + ", ucid='" + ucid + '\''
                + ", userId=" + userId + ", bankName='" + bankName + '\'' + ", bankAccountName='" + bankAccountName
                + '\'' + ", swiftCode='" + swiftCode + '\'' + ", bankCard='" + bankCard + '\'' + ", sortCode='"
                + sortCode + '\'' + ", bankAddress='" + bankAddress + '\'' + ", bankBranchName='" + bankBranchName
                + '\'' + ", accountHolderAddress='" + accountHolderAddress + '\'' + ", createTime='" + createTime + '\''
                + ", createTimeUtc='" + createTimeUtc + '\'' + ", updateTime='" + updateTime + '\''
                + ", updateTimeUtc='" + updateTimeUtc + '\'' + ", isDel=" + isDel + ", bankCity='" + bankCity + '\''
                + ", ifscCode='" + ifscCode + '\'' + ", isDeleted=" + isDeleted + ", lastUpdated='" + lastUpdated + '\''
                + '}';
    }

    public Integer getSourceIdSt() {
        return sourceIdSt;
    }

    public void setSourceIdSt(Integer sourceIdSt) {
        this.sourceIdSt = sourceIdSt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getUcid() {
        return ucid;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getBankBranchName() {
        return bankBranchName;
    }

    public void setBankBranchName(String bankBranchName) {
        this.bankBranchName = bankBranchName;
    }

    public String getAccountHolderAddress() {
        return accountHolderAddress;
    }

    public void setAccountHolderAddress(String accountHolderAddress) {
        this.accountHolderAddress = accountHolderAddress;
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

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateTimeUtc() {
        return updateTimeUtc;
    }

    public void setUpdateTimeUtc(String updateTimeUtc) {
        this.updateTimeUtc = updateTimeUtc;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    public String getBankCity() {
        return bankCity;
    }

    public void setBankCity(String bankCity) {
        this.bankCity = bankCity;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
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
