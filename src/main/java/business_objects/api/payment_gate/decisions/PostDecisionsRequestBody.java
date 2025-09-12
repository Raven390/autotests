package business_objects.api.payment_gate.decisions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDecisionsRequestBody {

    @JsonProperty("paymentId")
    private String paymentId;

    @JsonProperty("decisionType")
    private String decisionType;

    @JsonProperty("decisionCode")
    private String decisionCode;

    @JsonProperty("decidedAt")
    private String decidedAt; // ISO-8601 timestamp

    public PostDecisionsRequestBody() {
    }

    public PostDecisionsRequestBody(String paymentId, String decisionType, String decisionCode, String decidedAt) {
        this.paymentId = paymentId;
        this.decisionType = decisionType;
        this.decisionCode = decisionCode;
        this.decidedAt = decidedAt;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getDecisionType() {
        return decisionType;
    }

    public void setDecisionType(String decisionType) {
        this.decisionType = decisionType;
    }

    public String getDecisionCode() {
        return decisionCode;
    }

    public void setDecisionCode(String decisionCode) {
        this.decisionCode = decisionCode;
    }

    public String getDecidedAt() {
        return decidedAt;
    }

    public void setDecidedAt(String decidedAt) {
        this.decidedAt = decidedAt;
    }
}
