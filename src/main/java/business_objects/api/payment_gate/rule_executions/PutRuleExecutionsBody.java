package business_objects.api.payment_gate.rule_executions;

import java.sql.Timestamp;
import java.util.UUID;

public class PutRuleExecutionsBody {
    private String runId;
    private UUID paymentId;
    private Integer ruleId;
    private String ruleVersion;
    private String ruleEndId;
    private Timestamp startedAt;
    private Timestamp completedAt;

    public String getRunId() {
        return runId;
    }

    public void setRunId(String runId) {
        this.runId = runId;
    }

    public UUID getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(UUID paymentId) {
        this.paymentId = paymentId;
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

    public String getRuleEndId() {
        return ruleEndId;
    }

    public void setRuleEndId(String ruleEndId) {
        this.ruleEndId = ruleEndId;
    }

    public Timestamp getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Timestamp startedAt) {
        this.startedAt = startedAt;
    }

    public Timestamp getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Timestamp completedAt) {
        this.completedAt = completedAt;
    }
}
