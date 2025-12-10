package business_objects.api.payment_gate.payments;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public class PutPaymentsV2RequestBody {

    @JsonProperty("paymentId")
    private UUID paymentId;

    @JsonProperty("decisionId")
    private Integer decisionId;

    @JsonProperty("decisionMsg")
    private String decisionMsg;

    public PutPaymentsV2RequestBody() {
    }

    public PutPaymentsV2RequestBody(UUID paymentId, Integer decisionId) {
        this.paymentId = paymentId;
        this.decisionId = decisionId;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PutPaymentsV2RequestBody that)) return false;
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
