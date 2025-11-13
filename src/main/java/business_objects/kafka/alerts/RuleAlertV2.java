package business_objects.kafka.alerts;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RuleAlertV2 {

    @JsonProperty("reason")
    private String reason;

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("alertId")
    private String alertId;

    @JsonProperty("rule")
    private Rule rule;

    @JsonProperty("triggerCreatedTime")
    private String triggerCreatedTime;

    @JsonProperty("fraudType")
    private String fraudType;

    @JsonProperty("trigger")
    private String trigger;

    @JsonProperty("ucid")
    private String ucid;

    @JsonProperty("type")
    private String type;

    @JsonProperty("amount")
    private Double amount;

    @JsonProperty("amountUSD")
    private Double amountUsd;

    @JsonProperty("paymentMethod")
    private String paymentMethod;

    @JsonProperty("merchantOrderId")
    private String merchantOrderId;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("account")
    private Integer account;

    @JsonProperty("paymentEventId")
    private String paymentEventId;

    @JsonProperty("attributes")
    private Attribute attributes;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getAlertId() {
        return alertId;
    }

    public void setAlertId(String alertId) {
        this.alertId = alertId;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public String getTriggerCreatedTime() {
        return triggerCreatedTime;
    }

    public void setTriggerCreatedTime(String triggerCreatedTime) {
        this.triggerCreatedTime = triggerCreatedTime;
    }

    public String getFraudType() {
        return fraudType;
    }

    public void setFraudType(String fraudType) {
        this.fraudType = fraudType;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public String getUcid() {
        return ucid;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getMerchantOrderId() {
        return merchantOrderId;
    }

    public void setMerchantOrderId(String merchantOrderId) {
        this.merchantOrderId = merchantOrderId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getAccount() {
        return account;
    }

    public void setAccount(Integer account) {
        this.account = account;
    }

    public String getPaymentEventId() {
        return paymentEventId;
    }

    public void setPaymentEventId(String paymentEventId) {
        this.paymentEventId = paymentEventId;
    }

    public Attribute getAttributes() {
        return attributes;
    }

    public void setAttributes(Attribute attributes) {
        this.attributes = attributes;
    }

    public static class Rule {

        @JsonProperty("ver")
        private String ver;

        @JsonProperty("name")
        private String name;

        public String getVer() {
            return ver;
        }

        public void setVer(String ver) {
            this.ver = ver;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Attribute {

        @JsonProperty("Platform")
        private String platform;

        @JsonProperty("Create Time")
        private String createTime;

        @JsonProperty("Check")
        private String check;

        @JsonProperty("Date")
        private String date;

        @JsonProperty("Withdrawal ID")
        private Long withdrawalId;

        @JsonProperty("Regulator")
        private String regulator;

        @JsonProperty("Brand")
        private String brand;

        @JsonProperty("Payment channel")
        private String paymentChannel;

        @JsonProperty("Details")
        private String details;

        @JsonProperty("Ucid")
        private String ucid;

        @JsonProperty("UcidScore")
        private Double ucidScore;

        public String getPlatform() {
            return platform;
        }

        public void setPlatform(String platform) {
            this.platform = platform;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getCheck() {
            return check;
        }

        public void setCheck(String check) {
            this.check = check;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Long getWithdrawalId() {
            return withdrawalId;
        }

        public void setWithdrawalId(Long withdrawalId) {
            this.withdrawalId = withdrawalId;
        }

        public String getRegulator() {
            return regulator;
        }

        public void setRegulator(String regulator) {
            this.regulator = regulator;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getPaymentChannel() {
            return paymentChannel;
        }

        public void setPaymentChannel(String paymentChannel) {
            this.paymentChannel = paymentChannel;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public String getUcid() {
            return ucid;
        }

        public void setUcid(String ucid) {
            this.ucid = ucid;
        }

        public Double getUcidScore() {
            return ucidScore;
        }

        public void setUcidScore(Double ucidScore) {
            this.ucidScore = ucidScore;
        }
    }
}
