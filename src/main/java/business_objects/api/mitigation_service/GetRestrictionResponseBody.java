package business_objects.api.mitigation_service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Objects;

@Data
public class GetRestrictionResponseBody {

    @JsonProperty("id")
    public Integer id;

    @JsonProperty("ucid")
    public String ucid;

    @JsonProperty("code")
    public String code;

    @JsonProperty("type")
    public String type;

    @JsonProperty("status")
    public String status;

    @JsonProperty("comment")
    public String comment;

    @JsonProperty("cancelReason")
    public String cancelReason;

    @JsonProperty("updatedAt")
    public String updatedAt;

    @JsonProperty("createdAt")
    public String createdAt;

    @JsonProperty("updatedBy")
    public UpdatedBy updatedBy;

    @JsonProperty("accountId")
    public Integer accountId;

    @JsonProperty("account")
    public String account;

    @JsonProperty("serverId")
    public String serverId;

    @JsonProperty("editable")
    public String editable;

    public GetRestrictionResponseBody() {
    }

    public GetRestrictionResponseBody(Integer id, String ucid, String code, String type, String status,
            String applyReason, String cancelReason, String updatedAt, String createdAt, UpdatedBy updatedBy,
            Integer accountId, String account, String serverId, String comment) {
        this.id = id;
        this.ucid = ucid;
        this.code = code;
        this.type = type;
        this.status = status;
        this.comment = applyReason;
        this.cancelReason = cancelReason;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.updatedBy = updatedBy;
        this.accountId = accountId;
        this.account = account;
        this.serverId = serverId;
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GetRestrictionResponseBody that = (GetRestrictionResponseBody) o;
        return Objects.equals(id, that.id) && Objects.equals(ucid, that.ucid) && Objects.equals(code, that.code) && Objects.equals(type, that.type) && Objects.equals(status, that.status) && Objects.equals(comment, that.comment) && Objects.equals(cancelReason, that.cancelReason) && Objects.equals(updatedAt, that.updatedAt) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedBy, that.updatedBy) && Objects.equals(accountId, that.accountId) && Objects.equals(account, that.account) && Objects.equals(serverId, that.serverId) && Objects.equals(comment, that.comment);
    }

    @Override
    public String toString() {
        return "GetRestrictionResponseBody{" + "id=" + id + ", ucid='" + ucid + '\'' + ", code='" + code + '\'' + ", type='" + type + '\'' + ", status='" + status + '\'' + ", applyReason='" + comment + '\'' + ", cancelReason='" + cancelReason + '\'' + ", updatedAt='" + updatedAt + '\'' + ", createdAt='" + createdAt + '\'' + ", updatedBy=" + updatedBy + ", accountId=" + accountId + ", account='" + account + '\'' + ", serverId='" + serverId + '\'' + ", comment='" + comment + '\'' + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ucid, code, type, status, comment, cancelReason, updatedAt, createdAt, updatedBy, accountId, account, serverId, comment);
    }

    public static class UpdatedBy {

        @JsonProperty("system")
        public String system;

        @JsonProperty("user")
        public String user;

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
