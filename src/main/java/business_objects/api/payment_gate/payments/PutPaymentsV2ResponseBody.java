package business_objects.api.payment_gate.payments;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PutPaymentsV2ResponseBody {

    @JsonProperty("paymentId")
    private UUID paymentId;

    @JsonProperty("decisionId")
    private Integer decisionId;

    @JsonProperty("decidedAt")
    private String decidedAt;

    @JsonProperty("type")
    private String type;

    @JsonProperty("links")
    private Links links;

    @JsonProperty("error")
    private String error;

    @JsonProperty("message")
    private String message;

    public boolean isValidationError() {
        return error != null && message != null;
    }

    public boolean isUnprocessableEntity() {
        return error != null;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PutPaymentsV2ResponseBody that)) return false;
        return Objects.equals(paymentId, that.paymentId)
                && Objects.equals(decisionId, that.decisionId)
                && Objects.equals(decidedAt, that.decidedAt)
                && Objects.equals(type, that.type)
                && Objects.equals(links, that.links)
                && Objects.equals(error, that.error)
                && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentId, decisionId, decidedAt, type, links, error, message);
    }

    @Override
    public String toString() {
        return "PutPaymentsResponseBody{" + "paymentId=" + paymentId + ", decisionId=" + decisionId + ", decidedAt='"
                + decidedAt + '\'' + ", type='" + type + '\'' + ", links=" + links + ", error='" + error + '\''
                + ", message='" + message + '}';
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
}
