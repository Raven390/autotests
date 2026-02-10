package business_objects.api.rule_engine_api.get_brands;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetBrandsResponse {
    @JsonProperty("name")
    public String name;

    @JsonProperty("code")
    public String code;
}
