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
}
