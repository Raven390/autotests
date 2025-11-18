package business_objects.db.payment_gate.payment_rule_executions;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;


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

    public UUID getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(UUID paymentId) {
        this.paymentId = paymentId;
    }

    public Integer getRunId() {
        return runId;
    }

    public void setRunId(Integer runId) {
        this.runId = runId;
    }

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleVersion() {
        return ruleVersion;
    }

    public void setRuleVersion(String ruleVersion) {
        this.ruleVersion = ruleVersion;
    }

    public Integer getRuleEndId() {
        return ruleEndId;
    }

    public void setRuleEndId(Integer ruleEndId) {
        this.ruleEndId = ruleEndId;
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

    public Timestamp getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(Timestamp dateStarted) {
        this.dateStarted = dateStarted;
    }

    public Timestamp getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(Timestamp dateCompleted) {
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