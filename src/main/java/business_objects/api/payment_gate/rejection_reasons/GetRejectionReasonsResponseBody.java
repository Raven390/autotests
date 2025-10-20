package business_objects.api.payment_gate.rejection_reasons;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetRejectionReasonsResponseBody {

    @JsonProperty("code")
    private String code;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("attributes")
    private List<Attributes> attributes;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Attributes> getAttributes() {
        return attributes;
    }

    public void setAttributes(
            List<Attributes> attributes) {
        this.attributes = attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GetRejectionReasonsResponseBody that)) return false;
        return Objects.equals(code, that.code) && Objects.equals(name, that.name) && Objects.equals(
                description, that.description) && Objects.equals(attributes, that.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, description, attributes);
    }

    @Override
    public String toString() {
        return "GetRejectionReasonsResponseBody{" + "code='" + code + '\'' + ", name='" + name + '\'' + ", description='" + description + '\'' + ", attributes=" + attributes + '}';
    }

    public static class Attributes {
        @JsonProperty("code")
        private String code;
        @JsonProperty("name")
        private String name;
        @JsonProperty("description")
        private String description;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Attributes decisions)) return false;
            return Objects.equals(code, decisions.code) && Objects.equals(name, decisions.name) && Objects.equals(description, decisions.description);
        }

        @Override
        public int hashCode() {
            return Objects.hash(code, name, description);
        }

        @Override
        public String toString() {
            return "Decisions{" + "code='" + code + '\'' + ", name='" + name + '\'' + ", description='" + description + '\'' + '}';
        }
    }
}