package business_objects.db.clickhouse.reporting_test;

import java.util.Objects;

public class ZeebeRulesElements {
    private String timestamp;
    private String ruleName;
    private Long runId;
    private String elementId;
    private Integer position;
    private Integer ruleVersion;

    public ZeebeRulesElements() {
    }

    public ZeebeRulesElements(
            String timestamp, String ruleName, Long runId, String elementId, Integer position, Integer ruleVersion) {
        this.timestamp = timestamp;
        this.ruleName = ruleName;
        this.runId = runId;
        this.elementId = elementId;
        this.position = position;
        this.ruleVersion = ruleVersion;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public Long getRunId() {
        return runId;
    }

    public void setRunId(Long runId) {
        this.runId = runId;
    }

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getRuleVersion() {
        return ruleVersion;
    }

    public void setRuleVersion(Integer ruleVersion) {
        this.ruleVersion = ruleVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ZeebeRulesElements that)) return false;
        return Objects.equals(timestamp, that.timestamp) && Objects.equals(ruleName, that.ruleName) && Objects.equals(
                runId, that.runId) && Objects.equals(elementId, that.elementId) && Objects.equals(
                        position, that.position) && Objects.equals(ruleVersion, that.ruleVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, ruleName, runId, elementId, position, ruleVersion);
    }

    @Override
    public String toString() {
        return "ZeebeRulesElements{" + "timestamp='" + timestamp + '\'' + ", ruleName='" + ruleName + '\'' + ", runId=" + runId + ", elementId=" + elementId + ", position=" + position + ", ruleVersion=" + ruleVersion + '}';
    }
}
