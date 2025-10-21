package business_objects.db.payment_gate.payment_rejection_attributes.payment_events;


import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;


public class PaymentRejectionAttributesObject {

    private UUID paymentId;
    private Integer decisionId;
    private Integer id;
    private Integer attributeId;
    private String attributeValue;
    private Timestamp dateCreated;
    private Timestamp dateUpdated;

    public PaymentRejectionAttributesObject() {
    }

    public PaymentRejectionAttributesObject(
            UUID paymentId, Integer decisionId, Integer id, Integer attributeId, String attributeValue,
            Timestamp dateCreated, Timestamp dateUpdated) {
        this.paymentId = paymentId;
        this.decisionId = decisionId;
        this.id = id;
        this.attributeId = attributeId;
        this.attributeValue = attributeValue;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
    }

    public PaymentRejectionAttributesObject(
            UUID paymentId, Integer decisionId, Integer attributeId, String attributeValue,
            Timestamp dateCreated, Timestamp dateUpdated) {
        this.paymentId = paymentId;
        this.decisionId = decisionId;
        this.attributeId = attributeId;
        this.attributeValue = attributeValue;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
    }

    public UUID getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(UUID paymentId) {
        this.paymentId = paymentId;
    }

    public Integer getDecisionId() {
        return decisionId;
    }

    public void setDecisionId(Integer decisionId) {
        this.decisionId = decisionId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(Integer attributeId) {
        this.attributeId = attributeId;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PaymentRejectionAttributesObject that)) return false;
        return Objects.equals(paymentId, that.paymentId) && Objects.equals(decisionId, that.decisionId) && Objects.equals(
                id, that.id) && Objects.equals(attributeId, that.attributeId) && Objects.equals(
                        attributeValue, that.attributeValue) && Objects.equals(dateCreated, that.dateCreated) && Objects.equals(
                                dateUpdated, that.dateUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentId, decisionId, id, attributeId, attributeValue, dateCreated, dateUpdated);
    }

    @Override
    public String toString() {
        return "PaymentRejectionAttributesObject{" + "paymentId=" + paymentId + ", decisionId=" + decisionId + ", id=" + id + ", attributeId=" + attributeId + ", attributeValue='" + attributeValue + '\'' + ", dateCreated=" + dateCreated + ", dateUpdated=" + dateUpdated + '}';
    }
}