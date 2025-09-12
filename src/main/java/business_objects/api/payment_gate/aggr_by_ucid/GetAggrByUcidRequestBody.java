package business_objects.api.payment_gate.aggr_by_ucid;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetAggrByUcidRequestBody {

    @JsonProperty("schemaVersion")
    private String schemaVersion;

    @JsonProperty("type")
    private String type; // e.g., withdrawal

    // using Integer to allow null and align with examples where it's numeric
    @JsonProperty("finalDecisionCode")
    private Integer finalDecisionCode;

    @JsonProperty("ruleName")
    private List<String> ruleName;

    @JsonProperty("ruleEndType")
    private List<String> ruleEndType;

    @JsonProperty("startedAt")
    private String startedAt; // ISO-8601 timestamp

    public GetAggrByUcidRequestBody() {
    }

    public GetAggrByUcidRequestBody(String schemaVersion, String type, Integer finalDecisionCode,
            List<String> ruleName, List<String> ruleEndType, String startedAt) {
        this.schemaVersion = schemaVersion;
        this.type = type;
        this.finalDecisionCode = finalDecisionCode;
        this.ruleName = ruleName;
        this.ruleEndType = ruleEndType;
        this.startedAt = startedAt;
    }

    public String getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getFinalDecisionCode() {
        return finalDecisionCode;
    }

    public void setFinalDecisionCode(Integer finalDecisionCode) {
        this.finalDecisionCode = finalDecisionCode;
    }

    public List<String> getRuleName() {
        return ruleName;
    }

    public void setRuleName(List<String> ruleName) {
        this.ruleName = ruleName;
    }

    public List<String> getRuleEndType() {
        return ruleEndType;
    }

    public void setRuleEndType(List<String> ruleEndType) {
        this.ruleEndType = ruleEndType;
    }

    public String getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(String startedAt) {
        this.startedAt = startedAt;
    }
}
