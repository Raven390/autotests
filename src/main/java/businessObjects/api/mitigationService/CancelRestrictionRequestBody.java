package businessObjects.api.mitigationService;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class CancelRestrictionRequestBody {

    @JsonProperty("cancelReason")
    public String cancelReason;

    @JsonProperty("updatedBy")
    public UpdatedBy updatedBy;

    public CancelRestrictionRequestBody() {
    }

    public CancelRestrictionRequestBody(String cancelReason, UpdatedBy updatedBy) {
        this.cancelReason = cancelReason;
        this.updatedBy = updatedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CancelRestrictionRequestBody that = (CancelRestrictionRequestBody) o;
        return Objects.equals(cancelReason, that.cancelReason) && Objects.equals(updatedBy, that.updatedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cancelReason, updatedBy);
    }

    @Override
    public String toString() {
        return "CancelRestrictionRequestBody{" +
                "cancelReason='" + cancelReason + '\'' +
                ", updatedBy=" + updatedBy +
                '}';
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
            return "UpdatedBy{" +
                    "system='" + system + '\'' +
                    ", user='" + user + '\'' +
                    '}';
        }
    }
}
