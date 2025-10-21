package business_objects.db.payment_gate.payment_decisions;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;


public class PaymentDecisionsObject {

    private Integer id;
    private UUID paymentId;
    private String decisionType;
    private Integer decisionCode;
    private Integer rejectionCode;
    private String actor;
    private Timestamp dateCreated;
    private Timestamp dateUpdated;
    private Timestamp dateDecided;
    private String reasonString;

    public PaymentDecisionsObject() {
    }

    @Deprecated
    public PaymentDecisionsObject(
            Integer id, UUID paymentId, String decisionType, Integer decisionCode, Integer rejectionCode, String actor,
            Timestamp dateCreated, Timestamp dateUpdated, Timestamp dateDecided) {
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

    public PaymentDecisionsObject(
            Integer id, UUID paymentId, String decisionType, Integer decisionCode, Integer rejectionCode, String actor,
            Timestamp dateCreated, Timestamp dateUpdated, Timestamp dateDecided, String reasonString) {
        this.id = id;
        this.paymentId = paymentId;
        this.decisionType = decisionType;
        this.decisionCode = decisionCode;
        this.rejectionCode = rejectionCode;
        this.actor = actor;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
        this.dateDecided = dateDecided;
        this.reasonString = reasonString;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getReasonString() {
        return reasonString;
    }

    public void setReasonString(String reasonString) {
        this.reasonString = reasonString;
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
        return "PaymentDecisionsObject{" + "id=" + id + ", paymentId=" + paymentId + ", decisionType='" + decisionType + '\'' + ", decisionCode=" + decisionCode + ", rejectionCode=" + rejectionCode + ", actor='" + actor + '\'' + ", dateCreated=" + dateCreated + ", dateUpdated=" + dateUpdated + ", dateDecided=" + dateDecided + '}';
    }
}