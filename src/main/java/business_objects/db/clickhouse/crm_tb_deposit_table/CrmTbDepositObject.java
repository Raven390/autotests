package business_objects.db.clickhouse.crm_tb_deposit_table;


import java.util.Objects;

public class CrmTbDepositObject {

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
    public Double amount;
    public Double amountUsd;
    public String currency;
    public Integer statusId;
    public String status;
    public Integer paymentTypeId;
    public String paymentType;
    public String paymentProfile;
    public String paymentFamily;
    public Integer paymentChannelId;
    public String paymentChannel;
    public String paymentSystemAccount;
    public String paymentSystemCurrency;
    public String paymentDetails;
    public String paymentExpirationDate;
    public String ticket;
    public Double fee;
    public String processedNotes;
    public Integer isDel;
    public Integer isNonApp;
    public String lastUpdated;

    public CrmTbDepositObject() {
    }

    public CrmTbDepositObject(
            Integer account, Double amount, Double amountUsd, String brand, String createTime,
            String currency, Double fee, String paymentChannel, String paymentDetails,
            String paymentExpirationDate, String paymentSystemAccount,
            String paymentSystemCurrency, String paymentType, String regulator, String status,
            Integer ticketId, Integer transferId, String ucid, String uid, String updateTime, Integer userId) {
        this.account = account;
        this.amount = amount;
        this.amountUsd = amountUsd;
        this.brand = brand;
        this.createTime = createTime;
        this.currency = currency;
        this.fee = fee;
        this.paymentChannel = paymentChannel;
        this.paymentDetails = paymentDetails;
        this.paymentExpirationDate = paymentExpirationDate;
        //this.paymentRequisite = paymentRequisite;
        this.paymentSystemAccount = paymentSystemAccount;
        this.paymentSystemCurrency = paymentSystemCurrency;
        this.paymentType = paymentType;
        this.regulator = regulator;
        this.status = status;
        //this.ticketId = ticketId;
        this.transferId = transferId;
        this.ucid = ucid;
        //this.uid = uid;
        this.updateTime = updateTime;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CrmTbDepositObject that = (CrmTbDepositObject) o;
        return Objects.equals(account, that.account) && Objects.equals(amount, that.amount) && Objects.equals(
                amountUsd, that.amountUsd) && Objects.equals(brand, that.brand) && Objects.equals(
                        createTime, that.createTime) && Objects.equals(currency, that.currency) && Objects.equals(fee, that.fee) && Objects.equals(
                                paymentChannel, that.paymentChannel) && Objects.equals(paymentDetails, that.paymentDetails) && Objects.equals(
                                        paymentExpirationDate, that.paymentExpirationDate) && Objects.equals(paymentSystemAccount, that.paymentSystemAccount) && Objects.equals(
                                                paymentSystemCurrency, that.paymentSystemCurrency) && Objects.equals(paymentType, that.paymentType) && Objects.equals(
                                                        regulator, that.regulator) && Objects.equals(status, that.status) && Objects.equals(
                                                                transferId, that.transferId) && Objects.equals(ucid, that.ucid) && Objects.equals(
                                                                        updateTime, that.updateTime) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(account, amount, amountUsd, brand, createTime, currency, fee, paymentChannel, paymentDetails, paymentExpirationDate, paymentSystemAccount, paymentSystemCurrency, paymentType, regulator, status, transferId, ucid, updateTime, userId);
    }

    @Override
    public String toString() {
        return "CrmTbDepositObject{" + "account=" + account + ", amount=" + amount + ", amountUsd=" + amountUsd + ", brand='" + brand + '\'' + ", createTime='" + createTime + '\'' + ", currency='" + currency + '\'' + ", fee=" + fee + ", paymentChannel='" + paymentChannel + '\'' + ", paymentDetails='" + paymentDetails + '\'' + ", paymentExpirationDate='" + paymentExpirationDate + '\'' + ", paymentSystemAccount='" + paymentSystemAccount + '\'' + ", paymentSystemCurrency='" + paymentSystemCurrency + '\'' + ", paymentType='" + paymentType + '\'' + ", regulator='" + regulator + '\'' + ", status=" + status + ", transferId=" + transferId + ", ucid='" + ucid + '\'' + ", updateTime='" + updateTime + '\'' + ", userId=" + userId + '}';
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

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
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