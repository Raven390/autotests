package business_objects.kafka.crm_db_events.withdrawal;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class WithdrawalDbEventData {

    @JsonProperty(value = "create_time")
    public String createTime;

    @JsonProperty(value = "id")
    public Integer id;

    @JsonProperty(value = "user_id")
    public Integer userId;

    @JsonProperty("mt4_account")
    public Integer mt4Account;

    @JsonProperty(value = "brand")
    public String brand;

    @JsonProperty(value = "regulator")
    public String regulator;

    @JsonProperty("payment_method_code")
    public String paymentMethodCode;

    @JsonProperty("withdraw_type")
    public Integer withdrawType;

    @JsonProperty("withdraw_amount")
    public Double withdrawAmount;

    @JsonProperty("fee")
    public Double fee;

    @JsonProperty("actual_amount")
    public Double actualAmount;

    @JsonProperty("payment_amount")
    public Double paymentAmount;

    @JsonProperty("card_number")
    public String cardNumber;

    @JsonProperty("is_del")
    public Integer isDel;

    @JsonProperty("update_time")
    public String updateTime;

    @JsonProperty("order_number")
    public String orderNumber;

    @JsonProperty("cps_mandatory_field")
    public String cpsMandatoryField;

    @JsonProperty("is_remember_info")
    public Integer isRememberInfo;

    @JsonProperty("upi_account_name")
    public String upiAccountName;

    @JsonProperty("deduct_credit")
    public Double deductCredit;

    @JsonProperty("user_sales_id")
    public Integer userSalesId;

    @JsonProperty("account_sales_id")
    public Integer accountSalesId;

    @JsonProperty("order_currency")
    public String orderCurrency;

    @JsonProperty("checking_status")
    public Integer checkingStatus;

    @JsonProperty("is_trade")
    public Integer isTrade;

    @JsonProperty("rate")
    public Double rate;

    @JsonProperty("is_non_app")
    public Integer isNonApp;

    @JsonProperty("to_usd_rate")
    public Double toUsdRate;

    public WithdrawalDbEventData() {}

    public WithdrawalDbEventData(
            String createTime,
            Integer id,
            Integer userId,
            Integer mt4Account,
            String brand,
            String regulator,
            String paymentMethodCode,
            Integer withdrawType,
            Double withdrawAmount,
            Double fee,
            Double actualAmount,
            Double paymentAmount,
            String cardNumber,
            Integer isDel,
            String updateTime,
            String orderNumber,
            String cpsMandatoryField,
            Integer isRememberInfo,
            String upiAccountName,
            Double deductCredit,
            Integer userSalesId,
            Integer accountSalesId,
            String orderCurrency,
            Integer checkingStatus,
            Integer isTrade,
            Double rate,
            Integer isNonApp,
            Double toUsdRate) {
        this.createTime = createTime;
        this.id = id;
        this.userId = userId;
        this.mt4Account = mt4Account;
        this.brand = brand;
        this.regulator = regulator;
        this.paymentMethodCode = paymentMethodCode;
        this.withdrawType = withdrawType;
        this.withdrawAmount = withdrawAmount;
        this.fee = fee;
        this.actualAmount = actualAmount;
        this.paymentAmount = paymentAmount;
        this.cardNumber = cardNumber;
        this.isDel = isDel;
        this.updateTime = updateTime;
        this.orderNumber = orderNumber;
        this.cpsMandatoryField = cpsMandatoryField;
        this.isRememberInfo = isRememberInfo;
        this.upiAccountName = upiAccountName;
        this.deductCredit = deductCredit;
        this.userSalesId = userSalesId;
        this.accountSalesId = accountSalesId;
        this.orderCurrency = orderCurrency;
        this.checkingStatus = checkingStatus;
        this.isTrade = isTrade;
        this.rate = rate;
        this.isNonApp = isNonApp;
        this.toUsdRate = toUsdRate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        WithdrawalDbEventData that = (WithdrawalDbEventData) o;
        return Objects.equals(createTime, that.createTime)
                && Objects.equals(id, that.id)
                && Objects.equals(userId, that.userId)
                && Objects.equals(mt4Account, that.mt4Account)
                && Objects.equals(brand, that.brand)
                && Objects.equals(regulator, that.regulator)
                && Objects.equals(paymentMethodCode, that.paymentMethodCode)
                && Objects.equals(withdrawType, that.withdrawType)
                && Objects.equals(withdrawAmount, that.withdrawAmount)
                && Objects.equals(fee, that.fee)
                && Objects.equals(actualAmount, that.actualAmount)
                && Objects.equals(paymentAmount, that.paymentAmount)
                && Objects.equals(cardNumber, that.cardNumber)
                && Objects.equals(isDel, that.isDel)
                && Objects.equals(updateTime, that.updateTime)
                && Objects.equals(orderNumber, that.orderNumber)
                && Objects.equals(cpsMandatoryField, that.cpsMandatoryField)
                && Objects.equals(isRememberInfo, that.isRememberInfo)
                && Objects.equals(upiAccountName, that.upiAccountName)
                && Objects.equals(deductCredit, that.deductCredit)
                && Objects.equals(userSalesId, that.userSalesId)
                && Objects.equals(accountSalesId, that.accountSalesId)
                && Objects.equals(orderCurrency, that.orderCurrency)
                && Objects.equals(checkingStatus, that.checkingStatus)
                && Objects.equals(isTrade, that.isTrade)
                && Objects.equals(rate, that.rate)
                && Objects.equals(isNonApp, that.isNonApp)
                && Objects.equals(toUsdRate, that.toUsdRate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                createTime,
                id,
                userId,
                mt4Account,
                brand,
                regulator,
                paymentMethodCode,
                withdrawType,
                withdrawAmount,
                fee,
                actualAmount,
                paymentAmount,
                cardNumber,
                isDel,
                updateTime,
                orderNumber,
                cpsMandatoryField,
                isRememberInfo,
                upiAccountName,
                deductCredit,
                userSalesId,
                accountSalesId,
                orderCurrency,
                checkingStatus,
                isTrade,
                rate,
                isNonApp,
                toUsdRate);
    }

    @Override
    public String toString() {
        return "WithdrawalDbEventData{" + "createTime='" + createTime + '\'' + ", id=" + id + ", userId=" + userId
                + ", mt4Account=" + mt4Account + ", brand='" + brand + '\'' + ", regulator='" + regulator + '\''
                + ", paymentMethodCode='" + paymentMethodCode + '\'' + ", withdrawType=" + withdrawType
                + ", withdrawAmount=" + withdrawAmount + ", fee=" + fee + ", actualAmount=" + actualAmount
                + ", paymentAmount=" + paymentAmount + ", cardNumber='" + cardNumber + '\'' + ", isDel=" + isDel
                + ", updateTime='" + updateTime + '\'' + ", orderNumber='" + orderNumber + '\''
                + ", cpsMandatoryField='" + cpsMandatoryField + '\'' + ", isRememberInfo=" + isRememberInfo
                + ", upiAccountName='" + upiAccountName + '\'' + ", deductCredit=" + deductCredit + ", userSalesId="
                + userSalesId + ", accountSalesId=" + accountSalesId + ", orderCurrency='" + orderCurrency + '\''
                + ", checkingStatus=" + checkingStatus + ", isTrade=" + isTrade + ", rate=" + rate + ", isNonApp="
                + isNonApp + ", toUsdRate=" + toUsdRate + '}';
    }
}
