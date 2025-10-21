package business_objects.api.payment_gate.payments_decisions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public class PutDecisionsRequestBody {

    @JsonProperty("decisionType")
    private String decisionType;

    @JsonProperty("decisionCode")
    private Integer decisionCode;

    @JsonProperty("rejectionCode")
    private Integer rejectionCode;

    @JsonProperty("actor")
    private String actor;

    @JsonProperty("decidedAt")
    private String decidedAt; // ISO-8601 timestamp

    @JsonProperty("rejectionAttributes")
    private List<Attribute> attributes;

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public static class Attribute {
        private String code;
        private String value;

        public Attribute() {
        }

        public Attribute(String code, String value) {
            this.code = code;
            this.value = value;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Attribute that)) return false;
            return Objects.equals(code, that.code) && Objects.equals(value, that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(code, value);
        }

        @Override
        public String toString() {
            return "Attribute{" + "code='" + code + '\'' + ", value='" + value + '\'' + '}';
        }
    }


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

    public Integer getRejectionCode() {
        return rejectionCode;
    }

    public void setRejectionCode(Integer rejectionCode) {
        this.rejectionCode = rejectionCode;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }
}
