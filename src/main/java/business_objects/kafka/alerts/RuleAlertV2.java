package business_objects.kafka.alerts;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RuleAlertV2 {

    @JsonProperty("Reason")
    public String reason;

    @JsonProperty("timestamp")
    public String timestamp;

    @JsonProperty("alertId")
    public String alertId;

    @JsonProperty("rule")
    public Rule rule;

    @JsonProperty("triggerCreatedTime")
    public String triggerCreatedTime;

    @JsonProperty("fraudType")
    public String fraudType;

    @JsonProperty("trigger")
    public String trigger;

    @JsonProperty("ucid")
    public String ucid;

    @JsonProperty("type")
    public String type;

    @JsonProperty("attributes")
    public Attribute attributes;

    public static class Rule {

        @JsonProperty("ver")
        public String ver;

        @JsonProperty("name")
        public String name;
    }

    public static class Attribute {

        @JsonProperty("Details")
        public String details;

    }
}
