package business_objects.api.mitigation_service;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class CancelRestrictionByBitRequest {

    @JsonProperty("id")
    public Integer id;

    @JsonProperty("cancelReason")
    public String cancelReason;

    @JsonProperty("updatedBy")
    public UpdatedBy updatedBy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public UpdatedBy getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(UpdatedBy updatedBy) {
        this.updatedBy = updatedBy;
    }

    public CancelRestrictionByBitRequest() {
    }

    public CancelRestrictionByBitRequest(Integer id, String cancelReason, UpdatedBy updatedBy) {
        this.id = id;
        this.cancelReason = cancelReason;
        this.updatedBy = updatedBy;
    }

    public static class UpdatedBy {

        @JsonProperty("system")
        String system;

        @JsonProperty("user")
        String user;

        public String getSystem() {
            return system;
        }

        public void setSystem(String system) {
            this.system = system;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public UpdatedBy() {
        }

        public UpdatedBy(String system, String user) {
            this.system = system;
            this.user = user;
        }

        public UpdatedBy(String system) {
            this.system = system;
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
