package business_objects.api.mitigation_service;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Actor who requested for changing a restriction status (Apply requested/ Cancel requested)
 */
public class UpdatedBy {
    @JsonProperty("system")
    private String system = null;

    @JsonProperty("user")
    private String user = null;

    public UpdatedBy system(String system) {
        this.system = system;
        return this;
    }

    /**
     * System
     *
     * @return system
     **/
    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public UpdatedBy user(String user) {
        this.user = user;
        return this;
    }

    /**
     * (optional) user
     *
     * @return user
     **/
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UpdatedBy updatedBy = (UpdatedBy) o;
        return Objects.equals(this.system, updatedBy.system) && Objects.equals(this.user, updatedBy.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(system, user);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class UpdatedBy {\n");

        sb.append("    system: ").append(toIndentedString(system)).append("\n");
        sb.append("    user: ").append(toIndentedString(user)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
