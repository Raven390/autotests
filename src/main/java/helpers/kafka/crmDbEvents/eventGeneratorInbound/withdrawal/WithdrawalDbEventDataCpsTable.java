package helpers.kafka.crmDbEvents.eventGeneratorInbound.withdrawal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WithdrawalDbEventDataCpsTable {

    @JsonProperty("id")
    public Integer id;

    @JsonProperty("user_id")
    public String userId;

    @JsonProperty("mt4Account_no")
    public Integer mt4Account;

    @JsonProperty("account_name")
    public String accountName;

    @JsonProperty("currency")
    public String currency;

    @JsonProperty("status")
    public Integer status;

    @JsonProperty("withdraw_type")
    public Integer withdrawType;

    @JsonProperty("withdraw_amount")
    public String withdrawAmount;

    @JsonProperty("fee")
    public String fee;

    @JsonProperty("actual_amount")
    public String actualAmount;

    @JsonProperty("settlement_amount")
    public String paymentAmount;

    @JsonProperty("bankcard_no")
    public String cardNumber;

    @JsonProperty("is_del")
    public Integer isDel;

    @JsonProperty("create_time")
    public String createTime;

    @JsonProperty("update_time")
    public String updateTime;

    @JsonProperty("cps_attach_variable")
    public String cpsAttachVariable;

    @JsonProperty("order_no")
    public String orderNumber;

    @JsonProperty("cps_mandatory_field")
    public String cpsMandatoryField;

    @JsonProperty("is_remember_info")
    public Integer isRememberInfo;

    @JsonProperty("upi_account_name")
    public String upiAccountName;

    @JsonProperty("deduct_credit")
    public String deductCredit;

    @JsonProperty("user_sales_id")
    public Integer userSalesId;

    @JsonProperty("account_sales_id")
    public Integer accountSalesId;

    @JsonProperty("settlement_currency")
    public String orderCurrency;

    @JsonProperty("payment_method_code")
    public String paymentMethodCode;

    @JsonProperty("withdraw_status")
    public Integer checkingStatus;

    @JsonProperty("is_trade")
    public Integer isTrade;

    @JsonProperty("rate")
    public String rate;

    @JsonProperty("is_non_app")
    public Integer isNonApp;

    @JsonProperty("to_usd_rate")
    public String toUsdRate;

    @JsonProperty("brand")
    public String brand;

    @JsonProperty("regulator")
    public String regulator;

    public static WithdrawalDbEventDataCpsTable getWithdrawalDbEventDataCpsTable(Integer id, String userId, Integer mt4account, String accountName, String currency, Integer status, Integer withdrawType, String withdrawAmount, String fee, String actualAmount, String paymentAmount, String cardNumber, Integer isDel, String createTime, String updateTime, String cpsAttachVariable, String orderNumber, String cpsMandatoryField, Integer isRememberInfo, String upiAccountName, String deductCredit, Integer userSalesId, Integer accountSalesId, String orderCurrency, String paymentMethodCode, Integer checkingStatus, Integer isTrade, String rate, Integer isNonApp, String toUsdRate, String brand, String regulator) {
        WithdrawalDbEventDataCpsTable event = new WithdrawalDbEventDataCpsTable();
        event.id = id;
        event.userId = userId;
        event.mt4Account = mt4account;
        event.accountName = accountName;
        event.currency = currency;
        event.status = status;
        event.withdrawType = withdrawType;
        event.withdrawAmount = withdrawAmount;
        event.fee = fee;
        event.actualAmount = actualAmount;
        event.paymentAmount = paymentAmount;
        event.cardNumber = cardNumber;
        event.isDel = isDel;
        event.createTime = createTime;
        event.updateTime = updateTime;
        event.cpsAttachVariable = cpsAttachVariable;
        event.orderNumber = orderNumber;
        event.cpsMandatoryField = cpsMandatoryField;
        event.isRememberInfo = isRememberInfo;
        event.upiAccountName = upiAccountName;
        event.deductCredit = deductCredit;
        event.userSalesId = userSalesId;
        event.accountSalesId = accountSalesId;
        event.orderCurrency = orderCurrency;
        event.paymentMethodCode = paymentMethodCode;
        event.checkingStatus = checkingStatus;
        event.isTrade = isTrade;
        event.rate = rate;
        event.isNonApp = isNonApp;
        event.toUsdRate = toUsdRate;
        event.brand = brand;
        event.regulator = regulator;
        return event;
    }
}
