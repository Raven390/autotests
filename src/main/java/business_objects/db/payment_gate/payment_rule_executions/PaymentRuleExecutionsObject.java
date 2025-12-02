package business_objects.db.payment_gate.payment_rule_executions;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Setter
@Getter
@Builder
public class PaymentRuleExecutionsObject {

    private UUID paymentId;
    private Integer runId;
    private Integer ruleId;
    private String ruleVersion;
    private Integer ruleEndId;
    private Timestamp dateCreated;
    private Timestamp dateUpdated;
    private Timestamp dateStarted;
    private Timestamp dateCompleted;

    public PaymentRuleExecutionsObject() {
    }

    public PaymentRuleExecutionsObject(
            UUID paymentId, Integer runId, Integer ruleId, String ruleVersion, Integer ruleEndId, Timestamp dateCreated,
            Timestamp dateUpdated, Timestamp dateStarted, Timestamp dateCompleted) {
        this.paymentId = paymentId;
        this.runId = runId;
        this.ruleId = ruleId;
        this.ruleVersion = ruleVersion;
        this.ruleEndId = ruleEndId;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
        this.dateStarted = dateStarted;
        this.dateCompleted = dateCompleted;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PaymentRuleExecutionsObject that)) return false;
        return Objects.equals(paymentId, that.paymentId) && Objects.equals(
                runId, that.runId) && Objects.equals(ruleId, that.ruleId) && Objects.equals(ruleVersion, that.ruleVersion) && Objects.equals(
                        ruleEndId, that.ruleEndId) && Objects.equals(dateCreated, that.dateCreated) && Objects.equals(
                                dateUpdated, that.dateUpdated) && Objects.equals(dateStarted, that.dateStarted) && Objects.equals(
                                        dateCompleted, that.dateCompleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentId, runId, ruleId, ruleVersion, ruleEndId, dateCreated, dateUpdated, dateStarted, dateCompleted);
    }

    @Override
    public String toString() {
        return "PaymentRuleExecutionsObject{" + "paymentId='" + paymentId + '\'' + ", runId=" + runId + ", ruleId=" + ruleId + ", ruleVersion='" + ruleVersion + '\'' + ", ruleEndId=" + ruleEndId + ", dateCreated='" + dateCreated + '\'' + ", dateUpdated='" + dateUpdated + '\'' + ", dateStarted='" + dateStarted + '\'' + ", dateCompleted='" + dateCompleted + '\'' + '}';
    }
}