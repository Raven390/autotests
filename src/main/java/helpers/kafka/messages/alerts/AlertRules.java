package helpers.kafka.messages.alerts;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AlertRules {
    @JsonProperty("code")
    public int code;

    @JsonProperty("ver")
    public String ver;

    @JsonProperty("name")
    public String name;

    @JsonProperty("trigger")
    public String trigger;

    @JsonProperty("fraudType")
    public String fraudType;

    @JsonProperty("attributes")
    public RuleAttributes attributes;

    public static AlertRules alertRules(
            int code, String ver, String name, String trigger, String fraudType, RuleAttributes attributes) {
        AlertRules alertRules = new AlertRules();
        alertRules.code = code;
        alertRules.ver = ver;
        alertRules.name = name;
        alertRules.trigger = trigger;
        alertRules.fraudType = fraudType;
        alertRules.attributes = attributes;
        return alertRules;
    }
}
