package business_objects.api.mitigation_service;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Site
 */
public class Site {
    @JsonProperty("name")
    private String name = null;

    @JsonProperty("cancellable")
    private Boolean cancellable = null;

    public Site name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Get name
     *
     * @return name
     **/
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Site cancellable(Boolean cancellable) {
        this.cancellable = cancellable;
        return this;
    }

    /**
     * Get cancellable
     *
     * @return cancellable
     **/
    public Boolean isCancellable() {
        return cancellable;
    }

    public void setCancellable(Boolean cancellable) {
        this.cancellable = cancellable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Site site = (Site) o;
        return Objects.equals(this.name, site.name) && Objects.equals(this.cancellable, site.cancellable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, cancellable);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Site {\n");

        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    cancellable: ").append(toIndentedString(cancellable)).append("\n");
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
