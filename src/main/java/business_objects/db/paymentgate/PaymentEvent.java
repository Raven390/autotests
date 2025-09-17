package business_objects.db.paymentgate;

import java.time.LocalDateTime;

public class PaymentEvent {

    private String paymentId;
    private String crmId;
    private String type;
    private Long finalDecisionId;
    private String ucid;
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;
    private LocalDateTime dateDecided;

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

    public Long getFinalDecisionId() {
        return finalDecisionId;
    }

    public void setFinalDecisionId(Long finalDecisionId) {
        this.finalDecisionId = finalDecisionId;
    }

    public String getUcid() {
        return ucid;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDateTime getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(LocalDateTime dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public LocalDateTime getDateDecided() {
        return dateDecided;
    }

    public void setDateDecided(LocalDateTime dateDecided) {
        this.dateDecided = dateDecided;
    }
}