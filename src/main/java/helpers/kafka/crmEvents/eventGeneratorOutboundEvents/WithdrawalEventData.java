package helpers.kafka.crmEvents.eventGeneratorOutboundEvents;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WithdrawalEventData {
    @JsonProperty("UUID")
    public String UUID;

    @JsonProperty("create_time")
    public String create_time;

    @JsonProperty("transfer_id")
    public Integer transfer_id;

    @JsonProperty("user_id")
    public Integer user_id;

    @JsonProperty("mt_account")
    public Integer mt_account;

    @JsonProperty("brand")
    public String brand;

    @JsonProperty("regulator")
    public String regulator;

    @JsonProperty("payment_method_code")
    public String payment_method_code;

    @JsonProperty("withdraw_type")
    public Integer withdraw_type;

    @JsonProperty("withdraw_amount")
    public String withdraw_amount;

    @JsonProperty("fee")
    public String fee;

    @JsonProperty("actual_amount")
    public String actual_amount;

    @JsonProperty("payment_amount")
    public String payment_amount;

    @JsonProperty("settlement_amount")
    public String settlement_amount;

    @JsonProperty("card_number")
    public String card_number;

    @JsonProperty("wd_is_del")
    public int wd_is_del;

    @JsonProperty("update_time")
    public String update_time;

    @JsonProperty("cps_attach_variable")
    public String cps_attach_variable;

    @JsonProperty("order_number")
    public String order_number;

    @JsonProperty("order_no")
    public String order_no;

    @JsonProperty("cps_mandatory_field")
    public String cps_mandatory_field;

    @JsonProperty("wd_is_remember_info")
    public Integer wd_is_remember_info;

    @JsonProperty("upi_account_name")
    public String upi_account_name;

    @JsonProperty("deduct_credit")
    public String deduct_credit;

    @JsonProperty("user_sales_id")
    public Integer user_sales_id;

    @JsonProperty("account_sales_id")
    public Integer account_sales_id;

    @JsonProperty("order_currency")
    public String order_currency;

    @JsonProperty("checking_status")
    public Integer checking_status;

    @JsonProperty("is_trade")
    public Integer is_trade;

    @JsonProperty("rate")
    public String rate;

    @JsonProperty("is_non_app")
    public Integer is_non_app;

    @JsonProperty("to_usd_rate")
    public String to_usd_rate;
}
