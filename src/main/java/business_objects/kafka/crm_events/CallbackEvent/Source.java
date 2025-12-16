package business_objects.kafka.crm_events.CallbackEvent;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Source {
    private String name;

    @JsonProperty("ip_address")
    private String ipAddress;

    @JsonProperty("customer_id")
    private String customerId;

    private String email;
}
