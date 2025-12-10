package business_objects.kafka.payment.acknowledgement;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

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
    private Long clientId;

    @JsonProperty("paymentId")
    private UUID paymentId;

    public Acknowledge() {
    }

    public Acknowledge(String timestamp, String correlationId, UUID paymentId, String status, String subtype) {
        this.timestamp = timestamp;
        this.correlationId = correlationId;
        this.paymentId = paymentId;
        this.status = status;
        this.subtype = subtype;
    }
}
