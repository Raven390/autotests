package business_objects.kafka;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CrmAcknowledgeEvent {

    @JsonProperty("brand")
    private String brand;

    @JsonProperty("clientId")
    private String clientId;

    @JsonProperty("correlationId")
    private String correlationId;

    @JsonProperty("id")
    private String id;

    @JsonProperty("merchantOrderId")
    private String merchantOrderId;

    @JsonProperty("paymentId")
    private String paymentId;

    @JsonProperty("regulator")
    private String regulator;

    @JsonProperty("schemaVersion")
    private String schemaVersion;

    @JsonProperty("srcAppId")
    private String srcAppId;

    @JsonProperty("subtype")
    private String subtype;

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("type")
    private String type;
}
