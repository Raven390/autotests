package businessObjects.kafka.crmDbEvents.withdrawal;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class WithdrawalDbEventCpsData {

    @JsonProperty(value = "create_time")
    public String createTime;

    @JsonProperty(value = "id")
    public Integer id;

    @JsonProperty(value = "user_id")
    public Integer userId;

    @JsonProperty("mt4Account_no")
    public Integer mt4Account;

    @JsonProperty(value = "brand")
    public String brand;

    @JsonProperty(value = "regulator")
    public String regulator;

    @JsonProperty("withdraw_type")
    public Integer withdrawType;

    @JsonProperty("withdraw_amount")
    public Double withdrawAmount;

    @JsonProperty("fee")
    public Double fee;

    @JsonProperty("actual_amount")
    public Double actualAmount;

    @JsonProperty("settlement_amount")
    public Double paymentAmount;

    @JsonProperty("bankcard_no")
    public String cardNumber;

    @JsonProperty("is_del")
    public Integer isDel;

    @JsonProperty("update_time")
    public String updateTime;

    @JsonProperty("order_no")
    public String orderNumber;

    @JsonProperty("deduct_credit")
    public Double deductCredit;

    @JsonProperty("user_sales_id")
    public Integer userSalesId;

    @JsonProperty("account_sales_id")
    public Integer accountSalesId;

    @JsonProperty("settlement_currency")
    public String orderCurrency;

    @JsonProperty("withdraw_status")
    public Integer checkingStatus;

    @JsonProperty("rate")
    public Double rate;

    @JsonProperty("to_usd_rate")
    public Double toUsdRate;

    public WithdrawalDbEventCpsData() {
    }

    public WithdrawalDbEventCpsData(String createTime, Integer id, Integer userId, Integer mt4Account, String brand, String regulator, Integer withdrawType, Double withdrawAmount, Double fee, Double actualAmount, Double paymentAmount, String cardNumber, Integer isDel, String updateTime, String orderNumber, Double deductCredit, Integer userSalesId, Integer accountSalesId, String orderCurrency, Integer checkingStatus, Double rate, Double toUsdRate) {
        this.createTime = createTime;
        this.id = id;
        this.userId = userId;
        this.mt4Account = mt4Account;
        this.brand = brand;
        this.regulator = regulator;
        this.withdrawType = withdrawType;
        this.withdrawAmount = withdrawAmount;
        this.fee = fee;
        this.actualAmount = actualAmount;
        this.paymentAmount = paymentAmount;
        this.cardNumber = cardNumber;
        this.isDel = isDel;
        this.updateTime = updateTime;
        this.orderNumber = orderNumber;
        this.deductCredit = deductCredit;
        this.userSalesId = userSalesId;
        this.accountSalesId = accountSalesId;
        this.orderCurrency = orderCurrency;
        this.checkingStatus = checkingStatus;
        this.rate = rate;
        this.toUsdRate = toUsdRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WithdrawalDbEventCpsData that = (WithdrawalDbEventCpsData) o;
        return Objects.equals(createTime, that.createTime) && Objects.equals(id, that.id) && Objects.equals(userId, that.userId) && Objects.equals(mt4Account, that.mt4Account) && Objects.equals(brand, that.brand) && Objects.equals(regulator, that.regulator) && Objects.equals(withdrawType, that.withdrawType) && Objects.equals(withdrawAmount, that.withdrawAmount) && Objects.equals(fee, that.fee) && Objects.equals(actualAmount, that.actualAmount) && Objects.equals(paymentAmount, that.paymentAmount) && Objects.equals(cardNumber, that.cardNumber) && Objects.equals(isDel, that.isDel) && Objects.equals(updateTime, that.updateTime) && Objects.equals(orderNumber, that.orderNumber) && Objects.equals(deductCredit, that.deductCredit) && Objects.equals(userSalesId, that.userSalesId) && Objects.equals(accountSalesId, that.accountSalesId) && Objects.equals(orderCurrency, that.orderCurrency) && Objects.equals(checkingStatus, that.checkingStatus) && Objects.equals(rate, that.rate) && Objects.equals(toUsdRate, that.toUsdRate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createTime, id, userId, mt4Account, brand, regulator, withdrawType, withdrawAmount, fee, actualAmount, paymentAmount, cardNumber, isDel, updateTime, orderNumber, deductCredit, userSalesId, accountSalesId, orderCurrency, checkingStatus, rate, toUsdRate);
    }

    @Override
    public String toString() {
        return "WithdrawalDbEventCpsData{" +
                "createTime='" + createTime + '\'' +
                ", id=" + id +
                ", userId=" + userId +
                ", mt4Account=" + mt4Account +
                ", brand='" + brand + '\'' +
                ", regulator='" + regulator + '\'' +
                ", withdrawType=" + withdrawType +
                ", withdrawAmount=" + withdrawAmount +
                ", fee=" + fee +
                ", actualAmount=" + actualAmount +
                ", paymentAmount=" + paymentAmount +
                ", cardNumber='" + cardNumber + '\'' +
                ", isDel=" + isDel +
                ", updateTime='" + updateTime + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", deductCredit=" + deductCredit +
                ", userSalesId=" + userSalesId +
                ", accountSalesId=" + accountSalesId +
                ", orderCurrency='" + orderCurrency + '\'' +
                ", checkingStatus=" + checkingStatus +
                ", rate=" + rate +
                ", toUsdRate=" + toUsdRate +
                '}';
    }
}
