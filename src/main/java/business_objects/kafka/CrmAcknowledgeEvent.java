package business_objects.kafka;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CrmAcknowledgeEvent {
//    {
//        "brand": "vantage",
//            "clientId": 10164034,
//            "correlationId": "e8116df2-b74e-4771-89ad-2a475774be09",
//            "id": "f623e92e-f03d-4fe0-8e6c-33acecc040d0",
//            "merchantOrderId": "AUV21110164034ETH1764835020003J",
//            "paymentId": "dbeb60e8-8b06-4380-81cc-b1dfa982d10f",
//            "regulator": "VFSC2",
//            "schemaVersion": "1.0",
//            "srcAppId": "AUBRC052001PWM000000001",
//            "subtype": "acknowledge",
//            "timestamp": "2025-12-04T08:03:38.088Z",
//            "type": "transferToWA"
//    }
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
