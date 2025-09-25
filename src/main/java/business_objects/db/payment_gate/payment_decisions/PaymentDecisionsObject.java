package business_objects.db.payment_gate.payment_decisions;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;


public class PaymentDecisionsObject {

    public UUID paymentId;
    public String decisionType;
    public Integer decisionCode;
    public Integer rejectionCode;
    public String actor;
    public Timestamp dateCreated;
    public Timestamp dateUpdated;
    public Timestamp dateDecided;

    public PaymentDecisionsObject() {
    }

    public PaymentDecisionsObject(
            UUID paymentId, String decisionType, Integer decisionCode, Integer rejectionCode,
            String actor,
            Timestamp dateCreated, Timestamp dateUpdated, Timestamp dateDecided) {
        this.paymentId = paymentId;
        this.decisionType = decisionType;
        this.decisionCode = decisionCode;
        this.rejectionCode = rejectionCode;
        this.actor = actor;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
        this.dateDecided = dateDecided;
    }

    public UUID getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(UUID paymentId) {
        this.paymentId = paymentId;
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

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Timestamp getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Timestamp dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public Timestamp getDateDecided() {
        return dateDecided;
    }

    public void setDateDecided(Timestamp dateDecided) {
        this.dateDecided = dateDecided;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PaymentDecisionsObject that)) return false;
        return Objects.equals(paymentId, that.paymentId) && Objects.equals(
                decisionType, that.decisionType) && Objects.equals(decisionCode, that.decisionCode) && Objects.equals(
                        rejectionCode, that.rejectionCode) && Objects.equals(actor, that.actor) && Objects.equals(
                                dateCreated, that.dateCreated) && Objects.equals(dateUpdated, that.dateUpdated) && Objects.equals(
                                        dateDecided, that.dateDecided);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentId, decisionType, decisionCode, rejectionCode, actor, dateCreated, dateUpdated, dateDecided);
    }

    @Override
    public String toString() {
        return "PaymentDecisionsObject{" + "paymentId='" + paymentId + '\'' + ", decisionType='" + decisionType + '\'' + ", decisionCode=" + decisionCode + ", rejectionCode=" + rejectionCode + ", actor='" + actor + '\'' + ", dateCreated='" + dateCreated + '\'' + ", dateUpdated='" + dateUpdated + '\'' + ", dateDecided='" + dateDecided + '\'' + '}';
    }
}