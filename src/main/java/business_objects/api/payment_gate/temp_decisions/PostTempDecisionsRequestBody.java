package business_objects.api.payment_gate.temp_decisions;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostTempDecisionsRequestBody {

    @JsonProperty("paymentId")
    private String paymentId;

    @JsonProperty("decision")
    private String decision;

    public PostTempDecisionsRequestBody() {
    }

    public PostTempDecisionsRequestBody(String paymentId, String decision) {
        this.paymentId = paymentId;
        this.decision = decision;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }
}
