package businessObjects.api.mitigationService;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class PostRestrictionRequestBody {

    @JsonProperty("ucid")
    public String ucid;

    @JsonProperty("code")
    public String code;

    @JsonProperty("type")
    public String type;

    @JsonProperty("accountId")
    public Integer accountId;

    @JsonProperty("serverId")
    public Integer serverId;

    @JsonProperty("applyReason")
    public String applyReason;

    @JsonProperty("updatedBy")
    public UpdatedBy updatedBy;

    public PostRestrictionRequestBody() {
    }

    public PostRestrictionRequestBody(String ucid, String code, String type, Integer accountId, Integer serverId,
            String applyReason, UpdatedBy updatedBy) {
        this.ucid = ucid;
        this.code = code;
        this.type = type;
        this.accountId = accountId;
        this.serverId = serverId;
        this.applyReason = applyReason;
        this.updatedBy = updatedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostRestrictionRequestBody that = (PostRestrictionRequestBody) o;
        return Objects.equals(ucid, that.ucid) && Objects.equals(code, that.code) && Objects.equals(type, that.type) && Objects.equals(accountId, that.accountId) && Objects.equals(serverId, that.serverId) && Objects.equals(applyReason, that.applyReason) && Objects.equals(updatedBy, that.updatedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ucid, code, type, accountId, serverId, applyReason, updatedBy);
    }

    @Override
    public String toString() {
        return "PostRestrictionRequestBody{" + "ucid='" + ucid + '\'' + ", code='" + code + '\'' + ", type='" + type + '\'' + ", accountId=" + accountId + ", serverId=" + serverId + ", applyReason='" + applyReason + '\'' + ", updatedBy=" + updatedBy + '}';
    }

    public static class UpdatedBy {

        @JsonProperty("system")
        String system;

        @JsonProperty("user")
        String user;

        public UpdatedBy() {
        }

        public UpdatedBy(String system, String user) {
            this.system = system;
            this.user = user;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UpdatedBy updatedBy = (UpdatedBy) o;
            return Objects.equals(system, updatedBy.system) && Objects.equals(user, updatedBy.user);
        }

        @Override
        public int hashCode() {
            return Objects.hash(system, user);
        }

        @Override
        public String toString() {
            return "UpdatedBy{" + "system='" + system + '\'' + ", user='" + user + '\'' + '}';
        }
    }
}
