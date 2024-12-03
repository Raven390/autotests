package helpers.kafka.alerts;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AlertEvent {

    @JsonProperty("uuid")
    public String uuid;

    @JsonProperty("alertId")
    public String alertId;

    @JsonProperty("timestamp")
    public String timestamp;

    @JsonProperty("ucid")
    public String ucid;

    @JsonProperty("rule")
    public AlertRules rule;

    public static AlertEvent alertEvent(String traceId, String alertId, String timestamp, String unClId, AlertRules rule) {
        AlertEvent alertEvent = new AlertEvent();
        alertEvent.uuid = traceId;
        alertEvent.alertId = alertId;
        alertEvent.timestamp = timestamp;
        alertEvent.ucid = unClId;
        alertEvent.rule = rule;
        return alertEvent;
    }
}
