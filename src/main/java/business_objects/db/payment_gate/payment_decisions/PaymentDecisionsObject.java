package business_objects.db.payment_gate.payment_decisions;

import java.util.Objects;


public class PaymentDecisionsObject {

    public Integer id;
    public String paymentId;
    public String decisionType;
    public Integer decisionCode;
    public Integer rejectionCode;
    public String actor;
    public String dateCreated;
    public String dateUpdated;
    public String dateDecided;

    public PaymentDecisionsObject() {
    }

    public PaymentDecisionsObject(
            Integer id, String paymentId, String decisionType, Integer decisionCode, Integer rejectionCode,
            String actor,
            String dateCreated, String dateUpdated, String dateDecided) {
        this.id = id;
        this.paymentId = paymentId;
        this.decisionType = decisionType;
        this.decisionCode = decisionCode;
        this.rejectionCode = rejectionCode;
        this.actor = actor;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
        this.dateDecided = dateDecided;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(String dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public String getDateDecided() {
        return dateDecided;
    }

    public void setDateDecided(String dateDecided) {
        this.dateDecided = dateDecided;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PaymentDecisionsObject that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(paymentId, that.paymentId) && Objects.equals(
                decisionType, that.decisionType) && Objects.equals(decisionCode, that.decisionCode) && Objects.equals(
                        rejectionCode, that.rejectionCode) && Objects.equals(actor, that.actor) && Objects.equals(
                                dateCreated, that.dateCreated) && Objects.equals(dateUpdated, that.dateUpdated) && Objects.equals(
                                        dateDecided, that.dateDecided);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, paymentId, decisionType, decisionCode, rejectionCode, actor, dateCreated, dateUpdated, dateDecided);
    }

    @Override
    public String toString() {
        return "PaymentDecisionsObject{" + "id=" + id + ", paymentId='" + paymentId + '\'' + ", decisionType='" + decisionType + '\'' + ", decisionCode=" + decisionCode + ", rejectionCode=" + rejectionCode + ", actor='" + actor + '\'' + ", dateCreated='" + dateCreated + '\'' + ", dateUpdated='" + dateUpdated + '\'' + ", dateDecided='" + dateDecided + '\'' + '}';
    }
}