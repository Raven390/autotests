package businessObjects.api.mitigationService;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class GetRestrictionResponseBody {

    @JsonProperty("id")
    public Integer id;

    @JsonProperty("code")
    public String code;

    @JsonProperty("type")
    public String type;

    @JsonProperty("status")
    public String status;

    @JsonProperty("applyReason")
    public String applyReason;

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

    public GetRestrictionResponseBody() {
    }

    public GetRestrictionResponseBody(Integer id, String code, String type, String status, String applyReason,
            String cancelReason, String updatedAt, UpdatedBy updatedBy, Integer accountId) {
        this.id = id;
        this.code = code;
        this.type = type;
        this.status = status;
        this.applyReason = applyReason;
        this.cancelReason = cancelReason;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
        this.accountId = accountId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetRestrictionResponseBody that = (GetRestrictionResponseBody) o;
        return Objects.equals(id, that.id) && Objects.equals(code, that.code) && Objects.equals(type, that.type) && Objects.equals(status, that.status) && Objects.equals(applyReason, that.applyReason) && Objects.equals(cancelReason, that.cancelReason) && Objects.equals(updatedAt, that.updatedAt) && Objects.equals(updatedBy, that.updatedBy) && Objects.equals(accountId, that.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, type, status, applyReason, cancelReason, updatedAt, updatedBy, accountId);
    }

    @Override
    public String toString() {
        return "GetRestrictionResponseBody{" + "id=" + id + ", code='" + code + '\'' + ", type='" + type + '\'' + ", status='" + status + '\'' + ", applyReason='" + applyReason + '\'' + ", cancelReason='" + cancelReason + '\'' + ", updatedAt='" + updatedAt + '\'' + ", updatedBy=" + updatedBy + ", accountId=" + accountId + '}';
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
