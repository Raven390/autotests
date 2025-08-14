package business_objects.db.clickhouse.crm_tb_withdrawal;


import java.util.Objects;

public class CrmTbWithdrawalObject {

    public Integer sourceIdSt;
    public Integer brandUid;
    public String brand;
    public String regulator;
    public Integer userId;
    public String ucid;
    public Integer account;
    public Integer transferId;
    public String createTime;
    public String createTimeUtc;
    public String updateTime;
    public String updateTimeUtc;
    public String reversedTime;
    public String reversedTimeUtc;
    public Double amount;
    public Double amountUsd;
    public Double reversedAmount;
    public Double reversedAmountUsd;
    public String currency;
    public Integer statusId;
    public String status;
    public Integer paymentTypeId;
    public String paymentType;
    public Integer paymentChannelId;
    public String paymentChannel;
    public String paymentSystemAccount;
    public String paymentSystemCurrency;
    public String paymentDetails;
    public String paymentExpirationDate;
    public Integer ticket;
    public Double fee;
    public String processedNotes;
    public Integer isDel;
    public Integer isTrade;
    public Integer isNonApp;
    public String lastUpdated;

    public CrmTbWithdrawalObject() {
    }

    public CrmTbWithdrawalObject(Integer sourceIdSt, Integer brandUid, String brand, String regulator, Integer userId,
            String ucid, Integer account, Integer transferId, String createTime, String createTimeUtc,
            String updateTime, String updateTimeUtc, String reversedTime, String reversedTimeUtc, Double amount,
            Double amountUsd, Double reversedAmount, Double reversedAmountUsd, String currency, Integer statusId,
            String status, Integer paymentTypeId, String paymentType, Integer paymentChannelId, String paymentChannel,
            String paymentSystemAccount, String paymentSystemCurrency, String paymentDetails,
            String paymentExpirationDate, Integer ticket, Double fee, String processedNotes, Integer isDel,
            Integer isTrade, Integer isNonApp, String lastUpdated) {
        this.sourceIdSt = sourceIdSt;
        this.brandUid = brandUid;
        this.brand = brand;
        this.regulator = regulator;
        this.userId = userId;
        this.ucid = ucid;
        this.account = account;
        this.transferId = transferId;
        this.createTime = createTime;
        this.createTimeUtc = createTimeUtc;
        this.updateTime = updateTime;
        this.updateTimeUtc = updateTimeUtc;
        this.reversedTime = reversedTime;
        this.reversedTimeUtc = reversedTimeUtc;
        this.amount = amount;
        this.amountUsd = amountUsd;
        this.reversedAmount = reversedAmount;
        this.reversedAmountUsd = reversedAmountUsd;
        this.currency = currency;
        this.statusId = statusId;
        this.status = status;
        this.paymentTypeId = paymentTypeId;
        this.paymentType = paymentType;
        this.paymentChannelId = paymentChannelId;
        this.paymentChannel = paymentChannel;
        this.paymentSystemAccount = paymentSystemAccount;
        this.paymentSystemCurrency = paymentSystemCurrency;
        this.paymentDetails = paymentDetails;
        this.paymentExpirationDate = paymentExpirationDate;
        this.ticket = ticket;
        this.fee = fee;
        this.processedNotes = processedNotes;
        this.isDel = isDel;
        this.isTrade = isTrade;
        this.isNonApp = isNonApp;
        this.lastUpdated = lastUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CrmTbWithdrawalObject that = (CrmTbWithdrawalObject) o;
        return Objects.equals(sourceIdSt, that.sourceIdSt) && Objects.equals(brandUid, that.brandUid) && Objects.equals(brand, that.brand) && Objects.equals(regulator, that.regulator) && Objects.equals(userId, that.userId) && Objects.equals(ucid, that.ucid) && Objects.equals(account, that.account) && Objects.equals(transferId, that.transferId) && Objects.equals(createTime, that.createTime) && Objects.equals(createTimeUtc, that.createTimeUtc) && Objects.equals(updateTime, that.updateTime) && Objects.equals(updateTimeUtc, that.updateTimeUtc) && Objects.equals(reversedTime, that.reversedTime) && Objects.equals(reversedTimeUtc, that.reversedTimeUtc) && Objects.equals(amount, that.amount) && Objects.equals(amountUsd, that.amountUsd) && Objects.equals(reversedAmount, that.reversedAmount) && Objects.equals(reversedAmountUsd, that.reversedAmountUsd) && Objects.equals(currency, that.currency) && Objects.equals(statusId, that.statusId) && Objects.equals(status, that.status) && Objects.equals(paymentTypeId, that.paymentTypeId) && Objects.equals(paymentType, that.paymentType) && Objects.equals(paymentChannelId, that.paymentChannelId) && Objects.equals(paymentChannel, that.paymentChannel) && Objects.equals(paymentSystemAccount, that.paymentSystemAccount) && Objects.equals(paymentSystemCurrency, that.paymentSystemCurrency) && Objects.equals(paymentDetails, that.paymentDetails) && Objects.equals(paymentExpirationDate, that.paymentExpirationDate) && Objects.equals(ticket, that.ticket) && Objects.equals(fee, that.fee) && Objects.equals(processedNotes, that.processedNotes) && Objects.equals(isDel, that.isDel) && Objects.equals(isTrade, that.isTrade) && Objects.equals(isNonApp, that.isNonApp) && Objects.equals(lastUpdated, that.lastUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceIdSt, brandUid, brand, regulator, userId, ucid, account, transferId, createTime, createTimeUtc, updateTime, updateTimeUtc, reversedTime, reversedTimeUtc, amount, amountUsd, reversedAmount, reversedAmountUsd, currency, statusId, status, paymentTypeId, paymentType, paymentChannelId, paymentChannel, paymentSystemAccount, paymentSystemCurrency, paymentDetails, paymentExpirationDate, ticket, fee, processedNotes, isDel, isTrade, isNonApp, lastUpdated);
    }

    @Override
    public String toString() {
        return "CrmTbWithdrawalObject{" + "sourceIdSt=" + sourceIdSt + ", brandUid=" + brandUid + ", brand='" + brand + '\'' + ", regulator='" + regulator + '\'' + ", userId=" + userId + ", ucid='" + ucid + '\'' + ", account=" + account + ", transferId=" + transferId + ", createTime='" + createTime + '\'' + ", createTimeUtc='" + createTimeUtc + '\'' + ", updateTime='" + updateTime + '\'' + ", updateTimeUtc='" + updateTimeUtc + '\'' + ", reversedTime='" + reversedTime + '\'' + ", reversedTimeUtc='" + reversedTimeUtc + '\'' + ", amount=" + amount + ", amountUsd=" + amountUsd + ", reversedAmount=" + reversedAmount + ", reversedAmountUsd=" + reversedAmountUsd + ", currency='" + currency + '\'' + ", statusId=" + statusId + ", status='" + status + '\'' + ", paymentTypeId='" + paymentTypeId + '\'' + ", paymentType='" + paymentType + '\'' + ", paymentChannelId='" + paymentChannelId + '\'' + ", paymentChannel='" + paymentChannel + '\'' + ", paymentSystemAccount='" + paymentSystemAccount + '\'' + ", paymentSystemCurrency='" + paymentSystemCurrency + '\'' + ", paymentDetails='" + paymentDetails + '\'' + ", paymentExpirationDate='" + paymentExpirationDate + '\'' + ", ticket=" + ticket + ", fee=" + fee + ", processedNotes='" + processedNotes + '\'' + ", isDel=" + isDel + ", isTrade=" + isTrade + ", isNonApp=" + isNonApp + ", lastUpdated='" + lastUpdated + '\'' + '}';
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

    public Integer getAccount() {
        return account;
    }

    public void setAccount(Integer account) {
        this.account = account;
    }

    public Integer getTransferId() {
        return transferId;
    }

    public void setTransferId(Integer transferId) {
        this.transferId = transferId;
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

    public String getReversedTime() {
        return reversedTime;
    }

    public void setReversedTime(String reversedTime) {
        this.reversedTime = reversedTime;
    }

    public String getReversedTimeUtc() {
        return reversedTimeUtc;
    }

    public void setReversedTimeUtc(String reversedTimeUtc) {
        this.reversedTimeUtc = reversedTimeUtc;
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

    public Double getReversedAmount() {
        return reversedAmount;
    }

    public void setReversedAmount(Double reversedAmount) {
        this.reversedAmount = reversedAmount;
    }

    public Double getReversedAmountUsd() {
        return reversedAmountUsd;
    }

    public void setReversedAmountUsd(Double reversedAmountUsd) {
        this.reversedAmountUsd = reversedAmountUsd;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPaymentTypeId() {
        return paymentTypeId;
    }

    public void setPaymentTypeId(Integer paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Integer getPaymentChannelId() {
        return paymentChannelId;
    }

    public void setPaymentChannelId(Integer paymentChannelId) {
        this.paymentChannelId = paymentChannelId;
    }

    public String getPaymentChannel() {
        return paymentChannel;
    }

    public void setPaymentChannel(String paymentChannel) {
        this.paymentChannel = paymentChannel;
    }

    public String getPaymentSystemAccount() {
        return paymentSystemAccount;
    }

    public void setPaymentSystemAccount(String paymentSystemAccount) {
        this.paymentSystemAccount = paymentSystemAccount;
    }

    public String getPaymentSystemCurrency() {
        return paymentSystemCurrency;
    }

    public void setPaymentSystemCurrency(String paymentSystemCurrency) {
        this.paymentSystemCurrency = paymentSystemCurrency;
    }

    public String getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(String paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public String getPaymentExpirationDate() {
        return paymentExpirationDate;
    }

    public void setPaymentExpirationDate(String paymentExpirationDate) {
        this.paymentExpirationDate = paymentExpirationDate;
    }

    public Integer getTicket() {
        return ticket;
    }

    public void setTicket(Integer ticket) {
        this.ticket = ticket;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public String getProcessedNotes() {
        return processedNotes;
    }

    public void setProcessedNotes(String processedNotes) {
        this.processedNotes = processedNotes;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    public Integer getIsTrade() {
        return isTrade;
    }

    public void setIsTrade(Integer isTrade) {
        this.isTrade = isTrade;
    }

    public Integer getIsNonApp() {
        return isNonApp;
    }

    public void setIsNonApp(Integer isNonApp) {
        this.isNonApp = isNonApp;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}