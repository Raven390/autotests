package helpers.kafka.messages.alerts;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class AlertRules {
    @JsonProperty("rule_id")
    public String ruleId;

    @JsonProperty("rule_ver")
    public String ruleVer;

    @JsonProperty("rule_name")
    public String ruleName;

    @JsonProperty("rule_data")
    public List<AlertRuleData> ruleData;

    public static AlertRules alertRules(String ruleId, String ruleVer, String ruleName, List<AlertRuleData> ruleData) {
        AlertRules alertRules = new AlertRules();
        alertRules.ruleId = ruleId;
        alertRules.ruleVer = ruleVer;
        alertRules.ruleName = ruleName;
        alertRules.ruleData = ruleData;
        return alertRules;
    }
}
