package business_objects.db.clickhouse.crm_tb_transfer;

import java.util.Objects;

public class CrmTbTransferObject {
    protected Long sourceIdSt;
    protected Long brandUid;
    protected String brand;
    protected String regulator;
    protected Long userId;
    protected String ucid;
    protected Long transferId;
    protected Long accountFrom;
    protected Long accountTo;
    protected String createTime;
    protected String createTimeUtc;
    protected String updateTime;
    protected String updateTimeUtc;
    protected Double amountFrom;
    protected Double amount;
    protected Double amountUsd;
    protected String currencyFrom;
    protected String currencyTo;
    protected Long statusId;
    protected String status;
    protected Integer isDel;
    protected Integer isDeleted;
    protected String internalComment;
    protected String lastUpdated;

    @Override
    public String toString() {
        return "CrmTbTransferObject{" + "sourceIdSt=" + sourceIdSt + ", brandUid=" + brandUid + ", brand='" + brand + '\'' + ", regulator='" + regulator + '\'' + ", userId=" + userId + ", ucid='" + ucid + '\'' + ", transferId=" + transferId + ", accountFrom=" + accountFrom + ", accountTo=" + accountTo + ", createTime='" + createTime + '\'' + ", createTimeUtc='" + createTimeUtc + '\'' + ", updateTime='" + updateTime + '\'' + ", updateTimeUtc='" + updateTimeUtc + '\'' + ", amountFrom=" + amountFrom + ", amount=" + amount + ", amountUsd=" + amountUsd + ", currencyFrom='" + currencyFrom + '\'' + ", currencyTo='" + currencyTo + '\'' + ", statusId=" + statusId + ", status='" + status + '\'' + ", isDel=" + isDel + ", isDeleted=" + isDeleted + ", internalComment='" + internalComment + '\'' + ", lastUpdated='" + lastUpdated + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CrmTbTransferObject that = (CrmTbTransferObject) o;
        return Objects.equals(sourceIdSt, that.sourceIdSt) && Objects.equals(brandUid, that.brandUid) && Objects.equals(brand, that.brand) && Objects.equals(regulator, that.regulator) && Objects.equals(userId, that.userId) && Objects.equals(ucid, that.ucid) && Objects.equals(transferId, that.transferId) && Objects.equals(accountFrom, that.accountFrom) && Objects.equals(accountTo, that.accountTo) && Objects.equals(createTime, that.createTime) && Objects.equals(createTimeUtc, that.createTimeUtc) && Objects.equals(updateTime, that.updateTime) && Objects.equals(updateTimeUtc, that.updateTimeUtc) && Objects.equals(amountFrom, that.amountFrom) && Objects.equals(amount, that.amount) && Objects.equals(amountUsd, that.amountUsd) && Objects.equals(currencyFrom, that.currencyFrom) && Objects.equals(currencyTo, that.currencyTo) && Objects.equals(statusId, that.statusId) && Objects.equals(status, that.status) && Objects.equals(isDel, that.isDel) && Objects.equals(isDeleted, that.isDeleted) && Objects.equals(internalComment, that.internalComment) && Objects.equals(lastUpdated, that.lastUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceIdSt, brandUid, brand, regulator, userId, ucid, transferId, accountFrom, accountTo, createTime, createTimeUtc, updateTime, updateTimeUtc, amountFrom, amount, amountUsd, currencyFrom, currencyTo, statusId, status, isDel, isDeleted, internalComment, lastUpdated);
    }

    public Long getSourceIdSt() {
        return sourceIdSt;
    }

    public void setSourceIdSt(Long sourceIdSt) {
        this.sourceIdSt = sourceIdSt;
    }

    public void setSourceIdSt(Integer sourceIdSt) {
        this.sourceIdSt = sourceIdSt.longValue();
    }

    public Long getBrandUid() {
        return brandUid;
    }

    public void setBrandUid(Long brandUid) {
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId.longValue();
    }

    public String getUcid() {
        return ucid;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public Long getTransferId() {
        return transferId;
    }

    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }

    public void setTransferId(Integer transferId) {
        this.transferId = transferId.longValue();
    }

    public Long getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(Long accountFrom) {
        this.accountFrom = accountFrom;
    }

    public void setAccountFrom(Integer accountFrom) {
        this.accountFrom = accountFrom.longValue();
    }

    public Long getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(Long accountTo) {
        this.accountTo = accountTo;
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

    public Double getAmountFrom() {
        return amountFrom;
    }

    public void setAmountFrom(Double amountFrom) {
        this.amountFrom = amountFrom;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getAmountUsd() {
        return amountUsd;
    }

    public void setAmountUsd(Double amountUsd) {
        this.amountUsd = amountUsd;
    }

    public String getCurrencyFrom() {
        return currencyFrom;
    }

    public void setCurrencyFrom(String currencyFrom) {
        this.currencyFrom = currencyFrom;
    }

    public String getCurrencyTo() {
        return currencyTo;
    }

    public void setCurrencyTo(String currencyTo) {
        this.currencyTo = currencyTo;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getInternalComment() {
        return internalComment;
    }

    public void setInternalComment(String internalComment) {
        this.internalComment = internalComment;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
