package business_objects.db.payment_gate.payment_rule_executions;

import java.util.Objects;


public class PaymentRuleExecutionsObject {

    public Integer id;
    public String paymentId;
    public Integer runId;
    public Integer ruleId;
    public String ruleVersion;
    public Integer ruleEndId;
    public String dateCreated;
    public String dateUpdated;
    public String dateStarted;
    public String dateCompleted;


    public PaymentRuleExecutionsObject() {
    }

    public PaymentRuleExecutionsObject(
            Integer id, String paymentId, Integer runId, Integer ruleId, String ruleVersion, Integer ruleEndId,
            String dateCreated, String dateUpdated, String dateStarted, String dateCompleted) {
        this.id = id;
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

    public String getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(String dateStarted) {
        this.dateStarted = dateStarted;
    }

    public String getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(String dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PaymentRuleExecutionsObject that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(paymentId, that.paymentId) && Objects.equals(
                runId, that.runId) && Objects.equals(ruleId, that.ruleId) && Objects.equals(ruleVersion, that.ruleVersion) && Objects.equals(
                        ruleEndId, that.ruleEndId) && Objects.equals(dateCreated, that.dateCreated) && Objects.equals(
                                dateUpdated, that.dateUpdated) && Objects.equals(dateStarted, that.dateStarted) && Objects.equals(
                                        dateCompleted, that.dateCompleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, paymentId, runId, ruleId, ruleVersion, ruleEndId, dateCreated, dateUpdated, dateStarted, dateCompleted);
    }

    @Override
    public String toString() {
        return "PaymentRuleExecutionsObject{" + "id=" + id + ", paymentId='" + paymentId + '\'' + ", runId=" + runId + ", ruleId=" + ruleId + ", ruleVersion='" + ruleVersion + '\'' + ", ruleEndId=" + ruleEndId + ", dateCreated='" + dateCreated + '\'' + ", dateUpdated='" + dateUpdated + '\'' + ", dateStarted='" + dateStarted + '\'' + ", dateCompleted='" + dateCompleted + '\'' + '}';
    }
}