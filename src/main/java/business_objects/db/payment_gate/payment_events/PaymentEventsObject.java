package business_objects.db.payment_gate.payment_events;

import java.util.Objects;


public class PaymentEventsObject {

    private String paymentId;
    private String crmId;
    private String type;
    private Integer finalDecisionId;
    private String ucid;
    private String dateCreated;
    private String dateUpdated;
    private String dateDecided;

    public PaymentEventsObject() {
    }

    public PaymentEventsObject(
            String paymentId, String crmId, String type, Integer finalDecisionId, String ucid, String dateCreated,
            String dateUpdated, String dateDecided) {
        this.paymentId = paymentId;
        this.crmId = crmId;
        this.type = type;
        this.finalDecisionId = finalDecisionId;
        this.ucid = ucid;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
        this.dateDecided = dateDecided;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
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
        if (!(o instanceof PaymentEventsObject that)) return false;
        return Objects.equals(paymentId, that.paymentId) && Objects.equals(crmId, that.crmId) && Objects.equals(
                type, that.type) && Objects.equals(finalDecisionId, that.finalDecisionId) && Objects.equals(ucid, that.ucid) && Objects.equals(
                        dateCreated, that.dateCreated) && Objects.equals(dateUpdated, that.dateUpdated) && Objects.equals(
                                dateDecided, that.dateDecided);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentId, crmId, type, finalDecisionId, ucid, dateCreated, dateUpdated, dateDecided);
    }

    @Override
    public String toString() {
        return "PaymentEventsObject{" + "paymentId='" + paymentId + '\'' + ", crmId='" + crmId + '\'' + ", type='" + type + '\'' + ", finalDecisionId=" + finalDecisionId + ", ucid='" + ucid + '\'' + ", dateCreated='" + dateCreated + '\'' + ", dateUpdated='" + dateUpdated + '\'' + ", dateDecided='" + dateDecided + '\'' + '}';
    }
}