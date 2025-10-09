package business_objects.kafka.alerts;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RuleAlert {

    @JsonProperty("alertId")
    public String alertId;

    @JsonProperty("timestamp")
    public String timestamp;

    @JsonProperty("ucid")
    public String ucid;

    @JsonProperty("rule")
    public Rule rule;

    @JsonProperty("triggerCreatedTime")
    public String triggerCreatedTime;

    @JsonProperty("type")
    public String type;

    public static class Rule {

        @JsonProperty("ver")
        public String ver;

        @JsonProperty("code")
        public Integer code;

        @JsonProperty("name")
        public String name;

        @JsonProperty("trigger")
        public String trigger;

        @JsonProperty("fraudType")
        public String fraudType;

        @JsonProperty("attributes")
        public Attribute attributes;

        public static class Attribute {

            @JsonProperty("Reason")
            public String reason;

            @JsonProperty("Risk rating")
            public String riskRating;

            @JsonProperty("stepName")
            public String stepName;

            @JsonProperty("ipAddress")
            public String ipAddress;

            @JsonProperty("country")
            public String country;

            @JsonProperty("fraudType")
            @JsonAlias({"Fraud type", "fraudType"})
            public String fraudType;

            @JsonProperty("refferalId")
            public Integer refferalId;

            @JsonProperty("ibId")
            public Integer ibId;

            @JsonProperty("Trading account")
            public Integer tradingAccount;

            @JsonProperty("serverId")
            public Integer serverId;

            @JsonProperty("Server Id")
            public Integer serverId2;

            @JsonProperty("clones")
            public String clones;

            @JsonProperty("Policy score")
            public String policyScore;

            @JsonProperty("ipCountry")
            public String ipCountry;

            @JsonProperty("mirrorTradeScore")
            public String mirrorTradeScore;

            @JsonProperty("Mirror trades")
            public String mirrorTrades;

            @JsonProperty("hedgingClone")
            public String hedgingClone;

            @JsonProperty("cpaId")
            public String cpaId;

            @JsonProperty("Withdrawal ID")
            public String withdrawalId;

            @JsonProperty("Amount")
            public String amount;

            @JsonProperty("Currency")
            public String currency;

            @JsonProperty("Loss vouchers amount in USD")
            public String lossVoucherAmount;

            @JsonProperty("Payment type")
            public String paymentType;

            @JsonProperty("Check")
            public String check;

            @JsonProperty("Symbol traded")
            public String symbolTraded;

            @JsonProperty("FMax Connection Score")
            @JsonAlias({"Max Connection Score", "FMax Connection Score"})
            public String maxConnectionScore;

            @JsonProperty("Fraud type of linked abuser")
            public String fraudTypeOfLinkedAbuser;

            @JsonProperty("Same CPA connections")
            public String sameCPAConnections;

            @JsonProperty("Days since first deposit")
            public String daysSinceFirstDeposit;

            @JsonProperty("First deposit in USD")
            public String firstDepositInUSD;

            @JsonProperty("Crypto as first deposit")
            public String cryptoAsFirstDeposit;

            @JsonProperty("Same brand connections")
            public String sameBrandConnections;

            @JsonProperty("Order Id")
            public String orderId;

            @JsonProperty("Payment channel")
            public String paymentChannel;

            @JsonProperty("Brand")
            public String brand;

            @JsonProperty("Account")
            public String account;

            @JsonProperty("Platform")
            public String platform;

            @JsonProperty("Create Time")
            public String createTime;

            @JsonProperty("Regulator")
            public String regulator;

            @JsonProperty("Date")
            public String date;

            @JsonProperty("Ticket id")
            public String ticketId;

            @JsonProperty("Toxicity Score")
            public String toxicityScore;

            @JsonProperty("MM counter")
            public String mmCounter;

            @JsonProperty("Rate USD")
            public Double rateUSD;

            @JsonProperty("Amount USD")
            public Double amountUSD;

            @JsonProperty("Payment ID")
            public String paymentId;
        }
    }
}
