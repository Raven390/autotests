package business_objects.db.payment_gate.payment_rejection_attributes.payment_events;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Builder
@Getter
@Setter
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