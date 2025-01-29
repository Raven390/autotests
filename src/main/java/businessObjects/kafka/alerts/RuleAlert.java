package businessObjects.kafka.alerts;

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

    public static class Rule {

        @JsonProperty("ver")
        public String ver;

        @JsonProperty("name")
        public String name;

        @JsonProperty("trigger")
        public String trigger;

        @JsonProperty("fraudType")
        public String fraudType;

        @JsonProperty("attributes")
        public Attribute attributes;

        public static class Attribute {

            @JsonProperty("riskRating")
            public String riskRating;

            @JsonProperty("stepName")
            public String stepName;

            @JsonProperty("ipAddress")
            public String ipAddress;

            @JsonProperty("country")
            public String country;

            @JsonProperty("fraudType")
            public String fraudType;

            @JsonProperty("refferalId")
            public Integer refferalId;

            @JsonProperty("ibId")
            public Integer ibId;

            @JsonProperty("tradingAccount")
            public Integer tradingAccount;

            @JsonProperty("serverId")
            public Integer serverId;

            @JsonProperty("clones")
            public String clones;

            @JsonProperty("policyScore")
            public String policyScore;

            @JsonProperty("ipCountry")
            public String ipCountry;

            @JsonProperty("mirrorTradeScore")
            public String mirrorTradeScore;

            @JsonProperty("mirrorTrades")
            public String mirrorTrades;
        }
    }
}
