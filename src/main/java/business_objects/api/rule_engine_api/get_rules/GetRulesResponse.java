package business_objects.api.rule_engine_api.get_rules;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class GetRulesResponse {

    @JsonProperty("id")
    private String id;

    @JsonProperty("topic")
    private String topic;

    @JsonProperty("eventType")
    private String eventType;

    @JsonProperty("value")
    private Value value;

    @Data
    public static class Value {
        @JsonProperty("name")
        private String name;

        @JsonProperty("brands")
        private List<String> brands;
    }
}
