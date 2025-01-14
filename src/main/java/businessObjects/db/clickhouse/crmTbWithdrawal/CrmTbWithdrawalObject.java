package businessObjects.db.clickhouse.crmTbWithdrawal;


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
}