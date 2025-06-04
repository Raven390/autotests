package business_objects.kafka.crm_events;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class EgWithdrawalEvent {
    @JsonProperty("id")
    public String id;

    @JsonProperty("eventDate")
    public String eventDate;

    @JsonProperty("withdrawalId")
    public Integer withdrawalId;

    @JsonProperty("clientId")
    public Integer clientId;

    @JsonProperty("metaTraderAccount")
    public Integer metaTraderAccount;

    @JsonProperty("brand")
    public String brand;

    @JsonProperty("regulator")
    public String regulator;

    @JsonProperty("paymentMethodCode")
    public String paymentMethodCode;

    @JsonProperty("withdrawType")
    public Integer withdrawType;

    @JsonProperty("withdrawalAmount")
    public Double withdrawalAmount;

    @JsonProperty("fee")
    public Double fee;

    @JsonProperty("actualAmount")
    public Double actualAmount;

    @JsonProperty("paymentAmount")
    public Double paymentAmount;

    @JsonProperty("cardHash")
    public String cardHash;

    @JsonProperty("wdIsDel")
    public Integer wdIsDel;

    @JsonProperty("updateTime")
    public String updateTime;

    @JsonProperty("orderNumber")
    public String orderNumber;

    @JsonProperty("cpsMandatoryField")
    public String cpsMandatoryField;

    @JsonProperty("wdIsRememberInfo")
    public Integer wdIsRememberInfo;

    @JsonProperty("deductCredit")
    public Double deductCredit;

    @JsonProperty("userSalesId")
    public Integer userSalesId;

    @JsonProperty("accountSalesId")
    public Integer accountSalesId;

    @JsonProperty("withdrawalCurrency")
    public String withdrawalCurrency;

    @JsonProperty("checkingStatus")
    public Integer checkingStatus;

    @JsonProperty("wdIsTrade")
    public Integer wdIsTrade;

    @JsonProperty("rate")
    public Double rate;

    @JsonProperty("wdIsNonApp")
    public Integer wdIsNonApp;

    @JsonProperty("initialEventTime")
    public String initialEventTime;

    @JsonProperty("checkName")
    public String checkName;

    @JsonProperty("toUsdRate")
    public Double toUsdRate;

    @JsonProperty("type")
    public String type;

    @JsonProperty("upiAccountName")
    public String upiAccountName;

    @JsonProperty("metadata")
    public CrmEventMetadata metadata;

    public EgWithdrawalEvent() {
    }

    public EgWithdrawalEvent(String eventDate, Integer withdrawalId, Integer clientId, Integer metaTraderAccount,
            String brand, String regulator, String paymentMethodCode, Integer withdrawType, Double withdrawalAmount,
            Double fee, Double actualAmount, Double paymentAmount, String cardHash, Integer wdIsDel, String updateTime,
            String orderNumber, String cpsMandatoryField, Integer wdIsRememberInfo,
            Double deductCredit, Integer userSalesId, Integer accountSalesId,
            String withdrawalCurrency, Integer checkingStatus, Integer wdIsTrade, Double rate, Integer wdIsNonApp,
            Double toUsdRate, String type) {
        this.eventDate = eventDate;
        this.withdrawalId = withdrawalId;
        this.clientId = clientId;
        this.metaTraderAccount = metaTraderAccount;
        this.brand = brand;
        this.regulator = regulator;
        this.paymentMethodCode = paymentMethodCode;
        this.withdrawType = withdrawType;
        this.withdrawalAmount = withdrawalAmount;
        this.fee = fee;
        this.actualAmount = actualAmount;
        this.paymentAmount = paymentAmount;
        this.cardHash = cardHash;
        this.wdIsDel = wdIsDel;
        this.updateTime = updateTime;
        this.orderNumber = orderNumber;
        this.cpsMandatoryField = cpsMandatoryField;
        this.wdIsRememberInfo = wdIsRememberInfo;
        this.deductCredit = deductCredit;
        this.userSalesId = userSalesId;
        this.accountSalesId = accountSalesId;
        this.withdrawalCurrency = withdrawalCurrency;
        this.checkingStatus = checkingStatus;
        this.wdIsTrade = wdIsTrade;
        this.rate = rate;
        this.wdIsNonApp = wdIsNonApp;
        this.toUsdRate = toUsdRate;
        this.type = type;
    }

    public EgWithdrawalEvent(String id, String eventDate, Integer withdrawalId, Integer clientId,
            Integer metaTraderAccount,
            String brand, String regulator, String paymentMethodCode, Integer withdrawType, Double withdrawalAmount,
            Double fee, Double actualAmount, Double paymentAmount, String cardHash, Integer wdIsDel, String updateTime,
            String orderNumber, String cpsMandatoryField, Integer wdIsRememberInfo,
            String upiAccountName, Double deductCredit, Integer userSalesId, Integer accountSalesId,
            String withdrawalCurrency, Integer checkingStatus, Integer wdIsTrade, Double rate, Integer wdIsNonApp,
            Double toUsdRate, String type) {
        this.id = id;
        this.eventDate = eventDate;
        this.withdrawalId = withdrawalId;
        this.clientId = clientId;
        this.metaTraderAccount = metaTraderAccount;
        this.brand = brand;
        this.regulator = regulator;
        this.paymentMethodCode = paymentMethodCode;
        this.withdrawType = withdrawType;
        this.withdrawalAmount = withdrawalAmount;
        this.fee = fee;
        this.actualAmount = actualAmount;
        this.paymentAmount = paymentAmount;
        this.cardHash = cardHash;
        this.wdIsDel = wdIsDel;
        this.updateTime = updateTime;
        this.orderNumber = orderNumber;
        this.cpsMandatoryField = cpsMandatoryField;
        this.wdIsRememberInfo = wdIsRememberInfo;
        this.deductCredit = deductCredit;
        this.userSalesId = userSalesId;
        this.accountSalesId = accountSalesId;
        this.withdrawalCurrency = withdrawalCurrency;
        this.checkingStatus = checkingStatus;
        this.wdIsTrade = wdIsTrade;
        this.rate = rate;
        this.wdIsNonApp = wdIsNonApp;
        this.toUsdRate = toUsdRate;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EgWithdrawalEvent that = (EgWithdrawalEvent) o;
        return Objects.equals(eventDate, that.eventDate) && Objects.equals(withdrawalId, that.withdrawalId) && Objects.equals(clientId, that.clientId) && Objects.equals(metaTraderAccount, that.metaTraderAccount) && Objects.equals(brand, that.brand) && Objects.equals(regulator, that.regulator) && Objects.equals(paymentMethodCode, that.paymentMethodCode) && Objects.equals(withdrawType, that.withdrawType) && Objects.equals(withdrawalAmount, that.withdrawalAmount) && Objects.equals(fee, that.fee) && Objects.equals(actualAmount, that.actualAmount) && Objects.equals(paymentAmount, that.paymentAmount) && Objects.equals(cardHash, that.cardHash) && Objects.equals(wdIsDel, that.wdIsDel) && Objects.equals(updateTime, that.updateTime) && Objects.equals(orderNumber, that.orderNumber) && Objects.equals(cpsMandatoryField, that.cpsMandatoryField) && Objects.equals(wdIsRememberInfo, that.wdIsRememberInfo) && Objects.equals(deductCredit, that.deductCredit) && Objects.equals(userSalesId, that.userSalesId) && Objects.equals(accountSalesId, that.accountSalesId) && Objects.equals(withdrawalCurrency, that.withdrawalCurrency) && Objects.equals(checkingStatus, that.checkingStatus) && Objects.equals(wdIsTrade, that.wdIsTrade) && Objects.equals(rate, that.rate) && Objects.equals(wdIsNonApp, that.wdIsNonApp) && Objects.equals(toUsdRate, that.toUsdRate) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventDate, withdrawalId, clientId, metaTraderAccount, brand, regulator, paymentMethodCode, withdrawType, withdrawalAmount, fee, actualAmount, paymentAmount, cardHash, wdIsDel, updateTime, orderNumber, cpsMandatoryField, wdIsRememberInfo, deductCredit, userSalesId, accountSalesId, withdrawalCurrency, checkingStatus, wdIsTrade, rate, wdIsNonApp, toUsdRate, type);
    }

    @Override
    public String toString() {
        return "WithdrawalEvent{" + "id='" + id + '\'' + ", eventDate='" + eventDate + '\'' + ", withdrawalId=" + withdrawalId + ", clientId=" + clientId + ", metaTraderAccount=" + metaTraderAccount + ", brand='" + brand + '\'' + ", regulator='" + regulator + '\'' + ", paymentMethodCode='" + paymentMethodCode + '\'' + ", withdrawType=" + withdrawType + ", withdrawalAmount=" + withdrawalAmount + ", fee=" + fee + ", actualAmount=" + actualAmount + ", paymentAmount=" + paymentAmount + ", cardHash='" + cardHash + '\'' + ", wdIsDel=" + wdIsDel + ", updateTime='" + updateTime + '\'' + ", orderNumber='" + orderNumber + '\'' + ", cpsMandatoryField='" + cpsMandatoryField + '\'' + ", wdIsRememberInfo=" + wdIsRememberInfo + ", deductCredit=" + deductCredit + ", userSalesId=" + userSalesId + ", accountSalesId=" + accountSalesId + ", withdrawalCurrency='" + withdrawalCurrency + '\'' + ", checkingStatus=" + checkingStatus + ", wdIsTrade=" + wdIsTrade + ", rate=" + rate + ", wdIsNonApp=" + wdIsNonApp + ", toUsdRate=" + toUsdRate + ", type='" + type + '\'' + '}';
    }
}
