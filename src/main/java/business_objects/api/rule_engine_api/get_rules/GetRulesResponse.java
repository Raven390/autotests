package business_objects.api.rule_engine_api.get_rules;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class GetRulesResponse {

    @JsonProperty("id")
    private String id;

    @JsonProperty("eventType")
    private String eventType;

    @JsonProperty("value")
    private Value value;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    // Static inner class
    public static class Value {
        @JsonProperty("name")
        private String name;

        @JsonProperty("brands")
        private List<String> brands;

        // Getters and Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getBrands() {
            return brands;
        }

        public void setBrands(List<String> brands) {
            this.brands = brands;
        }
    }
}
