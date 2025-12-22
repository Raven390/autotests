package business_objects.api.rule_engine_api.rule_deployments;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class GetRuleDeploymentResponse {

    @JsonProperty("version")
    String version;

    @JsonProperty("uuid")
    String uuid;

    @JsonProperty("authorName")
    String authorName;

    @JsonProperty("processId")
    String processId;

    @JsonProperty("ruleName")
    String ruleName;

    @JsonProperty("zeebeRevision")
    Integer zeebeRevision;

    @JsonProperty("lastUpdate")
    String lastUpdate;

    @JsonProperty("status")
    String status;

    @JsonProperty("comment")
    String comment;

    public GetRuleDeploymentResponse() {}

    public GetRuleDeploymentResponse(
            String version,
            String uuid,
            String authorName,
            String processId,
            String ruleName,
            Integer zeebeRevision,
            String lastUpdate,
            String status,
            String comment) {
        this.version = version;
        this.uuid = uuid;
        this.authorName = authorName;
        this.processId = processId;
        this.ruleName = ruleName;
        this.zeebeRevision = zeebeRevision;
        this.lastUpdate = lastUpdate;
        this.status = status;
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GetRuleDeploymentResponse that = (GetRuleDeploymentResponse) o;
        return Objects.equals(version, that.version)
                && Objects.equals(uuid, that.uuid)
                && Objects.equals(authorName, that.authorName)
                && Objects.equals(processId, that.processId)
                && Objects.equals(ruleName, that.ruleName)
                && Objects.equals(zeebeRevision, that.zeebeRevision)
                && Objects.equals(lastUpdate, that.lastUpdate)
                && Objects.equals(status, that.status)
                && Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version, uuid, authorName, processId, ruleName, zeebeRevision, lastUpdate, status, comment);
    }

    @Override
    public String toString() {
        return "GetRuleDeploymentsResponse{" + "version='" + version + '\'' + ", uuid='" + uuid + '\''
                + ", authorName='" + authorName + '\'' + ", processId='" + processId + '\'' + ", ruleName='" + ruleName
                + '\'' + ", zeebeRevision=" + zeebeRevision + ", lastUpdate='" + lastUpdate + '\'' + ", status='"
                + status + '\'' + ", comment='" + comment + '\'' + '}';
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
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

    public Integer getZeebeRevision() {
        return zeebeRevision;
    }

    public void setZeebeRevision(Integer zeebeRevision) {
        this.zeebeRevision = zeebeRevision;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
