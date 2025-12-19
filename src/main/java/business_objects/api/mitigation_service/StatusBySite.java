package business_objects.api.mitigation_service;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * StatusBySite
 */
public class StatusBySite {
    @JsonProperty("site")
    private Site site = null;

    @JsonProperty("status")
    private RestrictionStatus status = null;

    public StatusBySite site(Site site) {
        this.site = site;
        return this;
    }

    /**
     * Get site
     *
     * @return site
     **/
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public StatusBySite status(RestrictionStatus status) {
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
        StatusBySite statusBySite = (StatusBySite) o;
        return Objects.equals(this.site, statusBySite.site) && Objects.equals(this.status, statusBySite.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(site, status);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class StatusBySite {\n");

        sb.append("    site: ").append(toIndentedString(site)).append("\n");
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
