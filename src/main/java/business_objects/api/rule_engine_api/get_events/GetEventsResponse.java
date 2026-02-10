package business_objects.api.rule_engine_api.get_events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetEventsResponse {
    @JsonProperty("name")
    public String name;

    @JsonProperty("type")
    public String type;
}
