package business_objects.kafka.crm_events.CallbackEvent;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Meta {

    @JsonProperty("server_time")
    private long serverTime;

    @JsonProperty("cashier_session_id")
    private String cashierSessionId;

    @JsonProperty("platform_id")
    private String platformId;

    @JsonProperty("server_timezone")
    private String serverTimezone;

    @JsonProperty("api_version")
    private String apiVersion;

    @JsonProperty("tracking_id")
    private String trackingId;
}
