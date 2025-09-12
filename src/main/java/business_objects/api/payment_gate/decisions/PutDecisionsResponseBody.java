package business_objects.api.payment_gate.decisions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public class PutDecisionsResponseBody {
    // Single-item creation response fields
    @JsonProperty("decisionId")
    private String decisionId;

    @JsonProperty("paymentId")
    private String paymentId;

    @JsonProperty("decisionType")
    private String decisionType;

    // Represent as String to be consistent with request bodies and avoid number/string ambiguity in examples
    @JsonProperty("decisionCode")
    private String decisionCode;

    @JsonProperty("decidedAt")
    private String decidedAt; // ISO-8601 timestamp

    // Batch creation response
    @JsonProperty("created")
    private List<PostDecisionsResponseBody.CreatedItem> created;

    // Error response fields
    @JsonProperty("error")
    private String error;

    @JsonProperty("message")
    private String message;

    public PutDecisionsResponseBody() {
    }

    // Getters and setters
    public String getDecisionId() {
        return decisionId;
    }

    public void setDecisionId(String decisionId) {
        this.decisionId = decisionId;
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

    public List<PostDecisionsResponseBody.CreatedItem> getCreated() {
        return created;
    }

    public void setCreated(List<PostDecisionsResponseBody.CreatedItem> created) {
        this.created = created;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PutDecisionsResponseBody that = (PutDecisionsResponseBody) o;
        return Objects.equals(decisionId, that.decisionId) && Objects.equals(paymentId, that.paymentId) && Objects.equals(decisionType, that.decisionType) && Objects.equals(decisionCode, that.decisionCode) && Objects.equals(decidedAt, that.decidedAt) && Objects.equals(created, that.created) && Objects.equals(error, that.error) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(decisionId, paymentId, decisionType, decisionCode, decidedAt, created, error, message);
    }

    @Override
    public String toString() {
        return "PutDecisionsResponseBody{" + "decisionId='" + decisionId + '\'' + ", paymentId='" + paymentId + '\'' + ", decisionType='" + decisionType + '\'' + ", decisionCode='" + decisionCode + '\'' + ", decidedAt='" + decidedAt + '\'' + ", created=" + created + ", error='" + error + '\'' + ", message='" + message + '\'' + '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CreatedItem {

        @JsonProperty("decisionId")
        private String decisionId;

        @JsonProperty("paymentId")
        private String paymentId;

        @JsonProperty("decisionType")
        private String decisionType;

        @JsonProperty("decisionCode")
        private String decisionCode;

        @JsonProperty("decidedAt")
        private String decidedAt;

        public CreatedItem() {
        }

        public String getDecisionId() {
            return decisionId;
        }

        public void setDecisionId(String decisionId) {
            this.decisionId = decisionId;
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PutDecisionsResponseBody.CreatedItem that = (PutDecisionsResponseBody.CreatedItem) o;
            return Objects.equals(decisionId, that.decisionId) && Objects.equals(paymentId, that.paymentId) && Objects.equals(decisionType, that.decisionType) && Objects.equals(decisionCode, that.decisionCode) && Objects.equals(decidedAt, that.decidedAt);
        }

        @Override
        public int hashCode() {
            return Objects.hash(decisionId, paymentId, decisionType, decisionCode, decidedAt);
        }

        @Override
        public String toString() {
            return "CreatedItem{" + "decisionId='" + decisionId + '\'' + ", paymentId='" + paymentId + '\'' + ", decisionType='" + decisionType + '\'' + ", decisionCode='" + decisionCode + '\'' + ", decidedAt='" + decidedAt + '\'' + '}';
        }
    }
}
