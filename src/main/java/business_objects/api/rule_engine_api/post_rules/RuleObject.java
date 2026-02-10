package business_objects.api.rule_engine_api.post_rules;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RuleObject {
    @JsonProperty("id")
    private String id;

    @JsonProperty("eventType")
    private String eventType;

    @JsonProperty("value")
    private Value value;

    @Data
    @AllArgsConstructor
    public static class Value {
        @JsonProperty("name")
        private String name;

        @JsonProperty("brands")
        private List<String> brands;
    }
}
