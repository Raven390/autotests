package business_objects.api.payment_gate.payments;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostPaymentsResponseBody {

    @JsonProperty("paymentId")
    private String paymentId;

    @JsonProperty("type")
    private String type;

    @JsonProperty("storedAt")
    private String storedAt;

    @JsonProperty("idempotencyKey")
    private Integer idempotencyKey;

    @JsonProperty("idempotent")
    private Boolean idempotent;

    @JsonProperty("links")
    private Links links;

    @JsonProperty("error")
    private String error;

    @JsonProperty("message")
    private String message;

    @JsonProperty("violations")
    private List<Violation> violations;

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStoredAt() {
        return storedAt;
    }

    public void setStoredAt(String storedAt) {
        this.storedAt = storedAt;
    }

    public Integer getIdempotencyKey() {
        return idempotencyKey;
    }

    public void setIdempotencyKey(Integer idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }

    public Boolean getIdempotent() {
        return idempotent;
    }

    public void setIdempotent(Boolean idempotent) {
        this.idempotent = idempotent;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
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

    public List<Violation> getViolations() {
        return violations;
    }

    public void setViolations(List<Violation> violations) {
        this.violations = violations;
    }

    public boolean isCreatedResponse() {
        return paymentId != null && links != null && idempotencyKey != null;
    }

    public boolean isIdempotentOkResponse() {
        return paymentId != null && Boolean.TRUE.equals(idempotent);
    }

    public boolean isValidationError() {
        return error != null && message != null;
    }

    public boolean isUnprocessableEntity() {
        return error != null && violations != null && !violations.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostPaymentsResponseBody that = (PostPaymentsResponseBody) o;
        return Objects.equals(paymentId, that.paymentId) && Objects.equals(type, that.type) && Objects.equals(storedAt, that.storedAt) && Objects.equals(idempotencyKey, that.idempotencyKey) && Objects.equals(idempotent, that.idempotent) && Objects.equals(links, that.links) && Objects.equals(error, that.error) && Objects.equals(message, that.message) && Objects.equals(violations, that.violations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentId, type, storedAt, idempotencyKey, idempotent, links, error, message, violations);
    }

    @Override
    public String toString() {
        return "PostPaymentsResponseBody{" + "paymentId='" + paymentId + '\'' + ", type='" + type + '\'' + ", storedAt='" + storedAt + '\'' + ", idempotencyKey='" + idempotencyKey + '\'' + ", idempotent=" + idempotent + ", links=" + links + ", error='" + error + '\'' + ", message='" + message + '\'' + ", violations=" + violations + '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Links {
        @JsonProperty("self")
        private String self;

        public String getSelf() {
            return self;
        }

        public void setSelf(String self) {
            this.self = self;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Links links = (Links) o;
            return Objects.equals(self, links.self);
        }

        @Override
        public int hashCode() {
            return Objects.hash(self);
        }

        @Override
        public String toString() {
            return "Links{" + "self='" + self + '\'' + '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Violation {
        @JsonProperty("field")
        private String field;

        @JsonProperty("message")
        private String message;

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
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
            Violation violation = (Violation) o;
            return Objects.equals(field, violation.field) && Objects.equals(message, violation.message);
        }

        @Override
        public int hashCode() {
            return Objects.hash(field, message);
        }

        @Override
        public String toString() {
            return "Violation{" + "field='" + field + '\'' + ", message='" + message + '\'' + '}';
        }
    }
}
