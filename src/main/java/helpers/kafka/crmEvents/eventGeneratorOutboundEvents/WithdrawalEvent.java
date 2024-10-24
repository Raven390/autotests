package helpers.kafka.crmEvents.eventGeneratorOutboundEvents;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WithdrawalEvent {
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
    public String withdrawalAmount;

    @JsonProperty("fee")
    public String fee;

    @JsonProperty("actualAmount")
    public String actualAmount;

    @JsonProperty("paymentAmount")
    public String paymentAmount;

    @JsonProperty("settlement_amount")
    public String settlement_amount;

    @JsonProperty("cardHash")
    public String cardHash;

    @JsonProperty("wdIsDel")
    public int wdIsDel;

    @JsonProperty("updateTime")
    public String updateTime;

    @JsonProperty("cpsAttachVariable")
    public String cpsAttachVariable;

    @JsonProperty("orderNumber")
    public String orderNumber;

    @JsonProperty("order_no")
    public String order_no;

    @JsonProperty("cpsMandatoryField")
    public String cpsMandatoryField;

    @JsonProperty("wdIsRememberInfo")
    public Integer wdIsRememberInfo;

    @JsonProperty("upiAccountName")
    public String upiAccountName;

    @JsonProperty("deductCredit")
    public String deductCredit;

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
    public String rate;

    @JsonProperty("wdIsNonApp")
    public Integer wdIsNonApp;

    @JsonProperty("toUsdRate")
    public String toUsdRate;

    @JsonProperty("type")
    public String type;
}
