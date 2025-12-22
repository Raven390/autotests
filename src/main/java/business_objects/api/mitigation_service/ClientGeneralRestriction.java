package business_objects.api.mitigation_service;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * Client general restriction
 */
public class ClientGeneralRestriction extends ClientRestriction {
    @JsonProperty("status")
    private RestrictionStatus status = null;

    public ClientGeneralRestriction status(RestrictionStatus status) {
        this.status = status;
        return this;
    }

    /**
     * Get status
     *
     * @return status
     **/
    public RestrictionStatus getStatus() {
        return status;
    }

    public void setStatus(RestrictionStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClientGeneralRestriction clientGeneralRestriction = (ClientGeneralRestriction) o;
        return Objects.equals(this.status, clientGeneralRestriction.status) && super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, super.hashCode());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ClientGeneralRestriction {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
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
