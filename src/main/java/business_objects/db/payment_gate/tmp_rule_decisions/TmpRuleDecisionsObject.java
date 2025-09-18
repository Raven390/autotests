package business_objects.db.payment_gate.tmp_rule_decisions;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;


public class TmpRuleDecisionsObject {

    public Integer id;
    public UUID paymentId;
    public String decision;
    public Timestamp dateCreated;

    public TmpRuleDecisionsObject() {
    }

    public TmpRuleDecisionsObject(Integer id, UUID paymentId, String decision, Timestamp dateCreated) {
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

    public UUID getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(UUID paymentId) {
        this.paymentId = paymentId;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
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