package business_objects.api.rule_engine_api.get_events;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class GetEventsResponse {
    @JsonProperty("name")
    public String name;

    @JsonProperty("type")
    public String type;

    public GetEventsResponse() {}

    public GetEventsResponse(String name, String type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GetEventsResponse that = (GetEventsResponse) o;
        return Objects.equals(name, that.name) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    @Override
    public String toString() {
        return "GetEventsResponse{" + "name='" + name + '\'' + ", type='" + type + '\'' + '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
