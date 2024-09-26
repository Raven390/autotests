package helpers.kafka.messages.alerts;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AlertTriggerData {
    @JsonProperty("field1")
    public String field1;

    public static AlertTriggerData alertTriggerData(String field1) {
        AlertTriggerData alertTriggerData = new AlertTriggerData();
        alertTriggerData.field1 = field1;
        return alertTriggerData;
    }
}
