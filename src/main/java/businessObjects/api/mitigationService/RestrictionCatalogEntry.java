package businessObjects.api.mitigationService;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class RestrictionCatalogEntry {

    @JsonProperty("code")
    public String code;

    @JsonProperty("name")
    public String name;

    @JsonProperty("type")
    public String type;

    @JsonProperty("description")
    public String description;

    public RestrictionCatalogEntry() {
    }

    public RestrictionCatalogEntry(String code, String name, String type, String description) {
        this.code = code;
        this.name = name;
        this.type = type;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestrictionCatalogEntry that = (RestrictionCatalogEntry) o;
        return Objects.equals(code, that.code) && Objects.equals(name, that.name) && Objects.equals(type, that.type) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, type, description);
    }

    @Override
    public String toString() {
        return "RestrictionCatalogEntry{" + "code='" + code + '\'' + ", name='" + name + '\'' + ", type='" + type + '\'' + ", description='" + description + '\'' + '}';
    }
}
