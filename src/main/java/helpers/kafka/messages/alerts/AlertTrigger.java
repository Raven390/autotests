package helpers.kafka.messages.alerts;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class AlertTrigger {
    @JsonProperty("type")
    public String type;

    @JsonProperty("timestamp")
    public String timestamp;

    @JsonProperty("account_id")
    public String accountId;

    @JsonProperty("trigger_data")
    public List<AlertTriggerData> triggerData;
}
