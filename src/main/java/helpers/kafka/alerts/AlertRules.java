package helpers.kafka.alerts;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AlertRules {
    @JsonProperty("code")
    public Integer code;

    @JsonProperty("ver")
    public String ver;

    @JsonProperty("name")
    public String name;

    @JsonProperty("trigger")
    public String trigger;

    @JsonProperty("fraudType")
    public String fraudType;

    @JsonProperty("attributes")
    public AlertRuleAttributes attributes;

    public static AlertRules alertRules(Integer code, String ver, String name, String trigger, String fraudType, AlertRuleAttributes attributes) {
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
