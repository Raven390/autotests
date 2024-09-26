package helpers.kafka.messages.alerts;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AlertRuleData {
    @JsonProperty("field1")
    public String field1;

    public static AlertRuleData alertRuleData(String field1) {
        AlertRuleData alertRuleData = new AlertRuleData();
        alertRuleData.field1 = field1;
        return alertRuleData;
    }
}
