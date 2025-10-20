package business_objects.api.payment_gate.payments_decisions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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

    public String getDecisionId() {
        return decisionId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public String getDecisionType() {
        return decisionType;
    }

    public String getDecisionCode() {
        return decisionCode;
    }

    public String getDecision() {
        return decision;
    }

    public String getRejectionCode() {
        return rejectionCode;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public String getDecidedAt() {
        return decidedAt;
    }

    public String getActor() {
        return actor;
    }

    public void setDecisionId(String decisionId) {
        this.decisionId = decisionId;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public void setDecisionType(String decisionType) {
        this.decisionType = decisionType;
    }

    public void setDecisionCode(String decisionCode) {
        this.decisionCode = decisionCode;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public void setRejectionCode(String rejectionCode) {
        this.rejectionCode = rejectionCode;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public void setDecidedAt(String decidedAt) {
        this.decidedAt = decidedAt;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetDecisionsResponseBody that = (GetDecisionsResponseBody) o;
        return Objects.equals(decisionId, that.decisionId) && Objects.equals(paymentType, that.paymentType) && Objects.equals(decisionType, that.decisionType) && Objects.equals(decisionCode, that.decisionCode) && Objects.equals(decision, that.decision) && Objects.equals(rejectionCode, that.rejectionCode) && Objects.equals(rejectionReason, that.rejectionReason) && Objects.equals(decidedAt, that.decidedAt) && Objects.equals(actor, that.actor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(decisionId, paymentType, decisionType, decisionCode, decision, rejectionCode, rejectionReason, decidedAt, actor);
    }

    @Override
    public String toString() {
        return "GetDecisionsResponseBody{" + "decisionId='" + decisionId + '\'' + ", paymentType='" + paymentType + '\'' + ", decisionType='" + decisionType + '\'' + ", decisionCode='" + decisionCode + '\'' + ", decision='" + decision + '\'' + ", rejectionCode='" + rejectionCode + '\'' + ", rejectionReason='" + rejectionReason + '\'' + ", decidedAt='" + decidedAt + '\'' + ", actor='" + actor + '\'' + '}';
    }
}
