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
}
