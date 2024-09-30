package helpers.kafka.messages.alerts;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;

public class AlertTrigger {
    @JsonProperty("type")
    public String type;

    @JsonProperty("timestamp")
    public Date timestamp;

    @JsonProperty("account_id")
    public String accountId;

    @JsonProperty("trigger_data")
    public List<AlertTriggerData> triggerData;

    public static AlertTrigger alertTrigger(
            String type, Date timestamp, String accountId, List<AlertTriggerData> triggerData) {
        AlertTrigger alertTrigger = new AlertTrigger();
        alertTrigger.type = type;
        alertTrigger.timestamp = timestamp;
        alertTrigger.accountId = accountId;
        alertTrigger.triggerData = triggerData;
        return alertTrigger;
    }
}
