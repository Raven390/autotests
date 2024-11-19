package businessObjects.db.crmTbDepositTable;


import java.util.Objects;

public class CrmTbDepositObject {
    public Integer account;
    public Double amount;
    public Double amountUsd;
    public String brand;
    public String createTime;
    public String currency;
    public Double fee;
    public String paymentChannel;
    public String paymentDetails;
    public String paymentExpirationDate;
    public String paymentRequisite;
    public String paymentSystemAccount;
    public String paymentSystemCurrency;
    public String paymentType;
    public String regulator;
    public Integer status;
    public Integer ticketId;
    public Integer transferId;
    public String ucid;
    public String uid;
    public String updateTime;
    public Integer userId;

    public CrmTbDepositObject() {
    }

    // Constructor with all fields
    public CrmTbDepositObject(
            Integer account, Double amount, Double amountUsd, String brand, String createTime,
            String currency, Double fee, String paymentChannel, String paymentDetails,
            String paymentExpirationDate, String paymentRequisite, String paymentSystemAccount,
            String paymentSystemCurrency, String paymentType, String regulator, Integer status,
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
        this.paymentRequisite = paymentRequisite;
        this.paymentSystemAccount = paymentSystemAccount;
        this.paymentSystemCurrency = paymentSystemCurrency;
        this.paymentType = paymentType;
        this.regulator = regulator;
        this.status = status;
        this.ticketId = ticketId;
        this.transferId = transferId;
        this.ucid = ucid;
        this.uid = uid;
        this.updateTime = updateTime;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CrmTbDepositObject that = (CrmTbDepositObject) o;
        return Objects.equals(account, that.account) && Objects.equals(amount, that.amount) && Objects.equals(amountUsd, that.amountUsd) && Objects.equals(brand, that.brand) && Objects.equals(createTime, that.createTime) && Objects.equals(currency, that.currency) && Objects.equals(fee, that.fee) && Objects.equals(paymentChannel, that.paymentChannel) && Objects.equals(paymentDetails, that.paymentDetails) && Objects.equals(paymentExpirationDate, that.paymentExpirationDate) && Objects.equals(paymentRequisite, that.paymentRequisite) && Objects.equals(paymentSystemAccount, that.paymentSystemAccount) && Objects.equals(paymentSystemCurrency, that.paymentSystemCurrency) && Objects.equals(paymentType, that.paymentType) && Objects.equals(regulator, that.regulator) && Objects.equals(status, that.status) && Objects.equals(ticketId, that.ticketId) && Objects.equals(transferId, that.transferId) && Objects.equals(ucid, that.ucid) && Objects.equals(uid, that.uid) && Objects.equals(updateTime, that.updateTime) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(account, amount, amountUsd, brand, createTime, currency, fee, paymentChannel, paymentDetails, paymentExpirationDate, paymentRequisite, paymentSystemAccount, paymentSystemCurrency, paymentType, regulator, status, ticketId, transferId, ucid, uid, updateTime, userId);
    }

    @Override
    public String toString() {
        return "CrmTbDepositObject{" +
                "account=" + account +
                ", amount=" + amount +
                ", amountUsd=" + amountUsd +
                ", brand='" + brand + '\'' +
                ", createTime='" + createTime + '\'' +
                ", currency='" + currency + '\'' +
                ", fee=" + fee +
                ", paymentChannel='" + paymentChannel + '\'' +
                ", paymentDetails='" + paymentDetails + '\'' +
                ", paymentExpirationDate='" + paymentExpirationDate + '\'' +
                ", paymentRequisite='" + paymentRequisite + '\'' +
                ", paymentSystemAccount='" + paymentSystemAccount + '\'' +
                ", paymentSystemCurrency='" + paymentSystemCurrency + '\'' +
                ", paymentType='" + paymentType + '\'' +
                ", regulator='" + regulator + '\'' +
                ", status=" + status +
                ", ticketId=" + ticketId +
                ", transferId=" + transferId +
                ", ucid='" + ucid + '\'' +
                ", uid='" + uid + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", userId=" + userId +
                '}';
    }
}