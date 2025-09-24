package business_objects.api.payment_gate.payments;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.UUID;

public class PutPaymentsRequestBody {

    @JsonProperty("paymentId")
    private UUID paymentId;

    @JsonProperty("decisionId")
    private Integer decisionId;

    public PutPaymentsRequestBody() {
    }

    public PutPaymentsRequestBody(UUID paymentId, Integer decisionId) {
        this.paymentId = paymentId;
        this.decisionId = decisionId;
    }

    public UUID getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(UUID paymentId) {
        this.paymentId = paymentId;
    }

    public Integer getDecisionId() {
        return decisionId;
    }

    public void setDecisionId(Integer decisionId) {
        this.decisionId = decisionId;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PutPaymentsRequestBody that)) return false;
        return Objects.equals(paymentId, that.paymentId) && Objects.equals(decisionId, that.decisionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentId, decisionId);
    }

    @Override
    public String toString() {
        return "PutPaymentsRequestBody{" + "paymentId=" + paymentId + ", decisionId=" + decisionId + '}';
    }
}
