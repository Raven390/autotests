package business_objects.db.payment_gate.payment_events;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Builder
@Getter
@Setter
public class PaymentEventsObject {

    private UUID paymentId;
    private String crmId;
    private String type;
    private Integer finalDecisionId;
    private String ucid;
    private Timestamp dateCreated;
    private Timestamp dateUpdated;
    private Timestamp dateDecided;
    private String deliveryStatus;
    private String details;

    public PaymentEventsObject() {
    }

    public PaymentEventsObject(
            UUID paymentId, String crmId, String type, Integer finalDecisionId, String ucid, Timestamp dateCreated,
            Timestamp dateUpdated, Timestamp dateDecided, String deliveryStatus, String details) {
        this.paymentId = paymentId;
        this.crmId = crmId;
        this.type = type;
        this.finalDecisionId = finalDecisionId;
        this.ucid = ucid;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
        this.dateDecided = dateDecided;
        this.deliveryStatus = deliveryStatus;
        this.deliveryStatus = details;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PaymentEventsObject that)) return false;
        return Objects.equals(paymentId, that.paymentId) && Objects.equals(crmId, that.crmId) && Objects.equals(
                type, that.type) && Objects.equals(finalDecisionId, that.finalDecisionId) && Objects.equals(ucid, that.ucid) && Objects.equals(
                        dateCreated, that.dateCreated) && Objects.equals(dateUpdated, that.dateUpdated) && Objects.equals(
                                dateDecided, that.dateDecided) && Objects.equals(deliveryStatus, that.deliveryStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentId, crmId, type, finalDecisionId, ucid, dateCreated, dateUpdated, dateDecided, deliveryStatus);
    }

    @Override
    public String toString() {
        return "PaymentEventsObject{" + "paymentId=" + paymentId + ", crmId='" + crmId + '\'' + ", type='" + type + '\'' + ", finalDecisionId=" + finalDecisionId + ", ucid='" + ucid + '\'' + ", dateCreated=" + dateCreated + ", dateUpdated=" + dateUpdated + ", dateDecided=" + dateDecided + ", deliveryStatus='" + deliveryStatus + '\'' + '}';
    }
}