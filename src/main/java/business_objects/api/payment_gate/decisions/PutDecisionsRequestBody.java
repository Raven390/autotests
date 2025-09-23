package business_objects.api.payment_gate.decisions;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PutDecisionsRequestBody {

    @JsonProperty("decisionType")
    private String decisionType;

    @JsonProperty("decisionCode")
    private Integer decisionCode;

    @JsonProperty("decidedAt")
    private String decidedAt; // ISO-8601 timestamp

    public PutDecisionsRequestBody() {
    }

    public PutDecisionsRequestBody(String decisionType, Integer decisionCode, String decidedAt) {
        this.decisionType = decisionType;
        this.decisionCode = decisionCode;
        this.decidedAt = decidedAt;
    }

    public String getDecisionType() {
        return decisionType;
    }

    public void setDecisionType(String decisionType) {
        this.decisionType = decisionType;
    }

    public Integer getDecisionCode() {
        return decisionCode;
    }

    public void setDecisionCode(Integer decisionCode) {
        this.decisionCode = decisionCode;
    }

    public String getDecidedAt() {
        return decidedAt;
    }

    public void setDecidedAt(String decidedAt) {
        this.decidedAt = decidedAt;
    }
}
