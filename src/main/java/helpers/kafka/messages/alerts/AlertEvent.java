package helpers.kafka.messages.alerts;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class AlertEvent {

    @JsonProperty("uuid")
    public String uuid;

    @JsonProperty("alertId")
    public String alertId;

    @JsonProperty("timestamp")
    public Float timestamp;

    @JsonProperty("ucid")
    public String ucid;

    @JsonProperty("rule")
    public List<AlertRules> rule;

    @JsonProperty("trigger")
    public String trigger;

    @JsonProperty("ruleAttributes")
    public List<AlertRuleData> ruleAttributes;

    public static AlertEvent alertEvent(
            String traceId, String alertId, Float timestamp, String unClId, List<AlertRules> rule, String trigger) {
        AlertEvent alertEvent = new AlertEvent();
        alertEvent.uuid = traceId;
        alertEvent.alertId = alertId;
        alertEvent.timestamp = timestamp;
        alertEvent.ucid = unClId;
        alertEvent.rule = rule;
        alertEvent.trigger = trigger;
        return alertEvent;
    }
}
