package business_objects.api.rule_engine_api.post_rules;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public class RuleObject {
    @JsonProperty("id")
    private String id;

    @JsonProperty("eventType")
    private String eventType;

    @JsonProperty("value")
    private Value value;

    public RuleObject(String id, String eventType, Value value) {
        this.id = id;
        this.eventType = eventType;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RuleObject that = (RuleObject) o;
        return Objects.equals(id, that.id) && Objects.equals(eventType, that.eventType) && Objects.equals(
                value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, eventType, value);
    }

    @Override
    public String toString() {
        return "RuleObject{" + "id='" + id + '\'' + ", eventType='" + eventType + '\'' + ", value=" + value + '}';
    }

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

        public Value(String name, List<String> brands) {
            this.name = name;
            this.brands = brands;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Value value = (Value) o;
            return Objects.equals(name, value.name) && Objects.equals(brands, value.brands);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, brands);
        }

        @Override
        public String toString() {
            return "Value{" + "name='" + name + '\'' + ", brands=" + brands + '}';
        }

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
