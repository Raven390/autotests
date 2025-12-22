package business_objects.api.rule_engine_api.get_brands;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class GetBrandsResponse {
    @JsonProperty("name")
    public String name;

    @JsonProperty("code")
    public String code;

    public GetBrandsResponse() {}

    public GetBrandsResponse(String name, String code) {
        this.name = name;
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GetBrandsResponse that = (GetBrandsResponse) o;
        return Objects.equals(name, that.name) && Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, code);
    }

    @Override
    public String toString() {
        return "GetBrandsResponse{" + "name='" + name + '\'' + ", code='" + code + '\'' + '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
