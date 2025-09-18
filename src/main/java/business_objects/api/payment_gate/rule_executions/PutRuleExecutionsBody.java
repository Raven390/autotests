package business_objects.api.payment_gate.rule_executions;

import java.util.UUID;

public class PutRuleExecutionsBody {
    private String runId;
    private UUID paymentId;
    private Integer ruleId;
    private String ruleVersion;
    private String ruleEndId;
    private String startedAt;
    private String completedAt;

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

    public String getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(String startedAt) {
        this.startedAt = startedAt;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }
}
