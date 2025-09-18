package business_objects.db.payment_gate.payment_events;


import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;


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

    public PaymentEventsObject() {
    }

    public PaymentEventsObject(
            UUID paymentId, String crmId, String type, Integer finalDecisionId, String ucid, Timestamp dateCreated,
            Timestamp dateUpdated, Timestamp dateDecided, String deliveryStatus) {
        this.paymentId = paymentId;
        this.crmId = crmId;
        this.type = type;
        this.finalDecisionId = finalDecisionId;
        this.ucid = ucid;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
        this.dateDecided = dateDecided;
        this.deliveryStatus = deliveryStatus;
    }

    public UUID getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(UUID paymentId) {
        this.paymentId = paymentId;
    }

    public String getCrmId() {
        return crmId;
    }

    public void setCrmId(String crmId) {
        this.crmId = crmId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getFinalDecisionId() {
        return finalDecisionId;
    }

    public void setFinalDecisionId(Integer finalDecisionId) {
        this.finalDecisionId = finalDecisionId;
    }

    public String getUcid() {
        return ucid;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
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

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
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