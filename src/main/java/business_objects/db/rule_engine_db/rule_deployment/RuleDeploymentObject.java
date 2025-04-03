package business_objects.db.rule_engine_db.rule_deployment;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class RuleDeploymentObject {
    UUID uuid;
    String authorName;
    String version;
    Integer zeebeRevision;
    String processId;
    String ruleName;
    byte[] ruleBody;
    LocalDateTime lastUpdate;
    Integer status;
    String comment;

    public RuleDeploymentObject() {
    }

    public RuleDeploymentObject(
            UUID uuid, String authorName, String version, Integer zeebeRevision, String processId, String ruleName,
            byte[] ruleBody, LocalDateTime lastUpdate, Integer status, String comment) {
        this.uuid = uuid;
        this.authorName = authorName;
        this.version = version;
        this.zeebeRevision = zeebeRevision;
        this.processId = processId;
        this.ruleName = ruleName;
        this.ruleBody = ruleBody;
        this.lastUpdate = lastUpdate;
        this.status = status;
        this.comment = comment;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getZeebeRevision() {
        return zeebeRevision;
    }

    public void setZeebeRevision(Integer zeebeRevision) {
        this.zeebeRevision = zeebeRevision;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public byte[] getRuleBody() {
        return ruleBody;
    }

    public void setRuleBody(byte[] ruleBody) {
        this.ruleBody = ruleBody;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RuleDeploymentObject that = (RuleDeploymentObject) o;
        return Objects.equals(uuid, that.uuid) && Objects.equals(authorName, that.authorName) && Objects.equals(
                version, that.version) && Objects.equals(zeebeRevision, that.zeebeRevision) && Objects.equals(
                        processId, that.processId) && Objects.equals(ruleName, that.ruleName) && Objects.equals(
                                ruleBody, that.ruleBody) && Objects.equals(lastUpdate, that.lastUpdate) && Objects.equals(status, that.status) && Objects.equals(
                                        comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, authorName, version, zeebeRevision, processId, ruleName, ruleBody, lastUpdate, status, comment);
    }

    @Override
    public String toString() {
        return "RuleDeploymentObject{" + "uuid='" + uuid + '\'' + ", authorName='" + authorName + '\'' + ", version='" + version + '\'' + ", zeebeRevision=" + zeebeRevision + ", precessId='" + processId + '\'' + ", ruleName='" + ruleName + '\'' + ", ruleBody='" + ruleBody + '\'' + ", lastUpdate='" + lastUpdate + '\'' + ", status=" + status + ", comment='" + comment + '\'' + '}';
    }
}
