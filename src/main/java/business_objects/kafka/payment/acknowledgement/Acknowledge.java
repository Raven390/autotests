package business_objects.kafka.payment.acknowledgement;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Acknowledge {

    @JsonProperty("subtype")
    private String subtype;

    @JsonProperty("transferId")
    private Long transferId;

    @JsonProperty("merchantOrderId")
    private String merchantOrderId;

    @JsonProperty("brand")
    private String brand;

    @JsonProperty("id")
    private String id;

    @JsonProperty("status")
    private String status;

    @JsonProperty("correlationId")
    private String correlationId;

    @JsonProperty("type")
    private String type;

    @JsonProperty("schemaVersion")
    private String schemaVersion;

    @JsonProperty("regulator")
    private String regulator;

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("clientId")
    private Integer clientId;

    @JsonProperty("paymentId")
    private UUID paymentId;
}
