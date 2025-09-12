package business_objects.db.payment_gate.tmp_rule_decisions;

import java.util.Objects;


public class TmpRuleDecisionsObject {

    public Integer id;
    public String paymentId;
    public String decision;
    public String dateCreated;

    public TmpRuleDecisionsObject() {
    }

    public TmpRuleDecisionsObject(Integer id, String paymentId, String decision, String dateCreated) {
        this.id = id;
        this.paymentId = paymentId;
        this.decision = decision;
        this.dateCreated = dateCreated;
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

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TmpRuleDecisionsObject that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(paymentId, that.paymentId) && Objects.equals(
                decision, that.decision) && Objects.equals(dateCreated, that.dateCreated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, paymentId, decision, dateCreated);
    }

    @Override
    public String toString() {
        return "TmpRuleDecisionsObject{" + "id=" + id + ", paymentId='" + paymentId + '\'' + ", decision='" + decision + '\'' + ", dateCreated='" + dateCreated + '\'' + '}';
    }
}