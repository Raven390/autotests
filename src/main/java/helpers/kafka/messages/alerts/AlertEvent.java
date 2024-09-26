package helpers.kafka.messages.alerts;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;

public class AlertEvent {

    @JsonProperty("alert_id")
    public String alertId;

    @JsonProperty("timestamp")
    public Date timestamp;

    @JsonProperty("un_cl_id")
    public String unClId;

    @JsonProperty("rules")
    public List<AlertRules> rules;

    @JsonProperty("trigger")
    public List<AlertTrigger> trigger;

    @JsonProperty("amount_cur")
    public Double amount;

    @JsonProperty("currency")
    public String currency;

    public static AlertEvent alertEvent(
            String alertId,
            Date timestamp,
            String unClId,
            List<AlertRules> rules,
            List<AlertTrigger> trigger,
            Double amount,
            String currency) {
        AlertEvent alertEvent = new AlertEvent();
        alertEvent.alertId = alertId;
        alertEvent.timestamp = timestamp;
        alertEvent.unClId = unClId;
        alertEvent.rules = rules;
        alertEvent.trigger = trigger;
        alertEvent.amount = amount;
        alertEvent.currency = currency;
        return alertEvent;
    }
}
