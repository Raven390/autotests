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

    @JsonProperty("boVisibility")
    public String boVisibility;

    public RestrictionCatalogEntry() {
    }

    public RestrictionCatalogEntry(String code, String name, String type, String description, String boVisibility) {
        this.code = code;
        this.name = name;
        this.type = type;
        this.description = description;
        this.boVisibility = boVisibility;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RestrictionCatalogEntry that = (RestrictionCatalogEntry) o;
        return Objects.equals(code, that.code) && Objects.equals(name, that.name) && Objects.equals(type, that.type) && Objects.equals(description, that.description) && Objects.equals(boVisibility, that.boVisibility);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, type, description, boVisibility);
    }

    @Override
    public String toString() {
        return "RestrictionCatalogEntry{" + "code='" + code + '\'' + ", name='" + name + '\'' + ", type='" + type + '\'' + ", description='" + description + '\'' + ", boVisibility='" + boVisibility + '\'' + '}';
    }
}
