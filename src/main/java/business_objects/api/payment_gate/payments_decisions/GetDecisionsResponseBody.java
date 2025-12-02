package business_objects.api.payment_gate.payments_decisions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
public class GetDecisionsResponseBody {

    // One element of the array returned by the API
    @JsonProperty("decisionId")
    private String decisionId;

    @JsonProperty("paymentType")
    private String paymentType; // e.g., WITHDRAWAL

    @JsonProperty("decisionType")
    private String decisionType; // e.g., risk, payment

    @JsonProperty("decisionCode")
    private String decisionCode;

    @JsonProperty("decision")
    private String decision; // e.g., APPROVE, REFUSE

    @JsonProperty("rejectionCode")
    private String rejectionCode; // optional, appears for REFUSE decisions

    @JsonProperty("rejectionReason")
    private String rejectionReason; // optional

    @JsonProperty("decidedAt")
    private String decidedAt; // ISO-8601 string

    @JsonProperty("actor")
    private String actor;

    @JsonProperty("error")
    private String error;

    @JsonProperty("message")
    private String message;

    @JsonProperty("type")
    private String type;

    @JsonProperty("title")
    private String title;

    @JsonProperty("status")
    private String status;

    @JsonProperty("detail")
    private String detail;

    @JsonProperty("rejectionAttributes")
    private RejectionAttributes rejectionAttributes;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public class RejectionAttributes {
        @JsonProperty("code")
        private String code;

        @JsonProperty("value")
        private String value;
    }
}
