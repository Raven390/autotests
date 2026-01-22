package business_objects.kafka;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomEvent {

    @JsonProperty("id")
    private String id;

    @JsonProperty("type")
    private String type;

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("serverId")
    private String serverId;

    @JsonProperty("tradingAccount")
    private String tradingAccount;

    @JsonProperty("brand")
    private String brand;

    @JsonProperty("clientId")
    private String clientId;

    @JsonProperty("alert")
    private String alert;

    @JsonProperty("fraudType")
    private String fraudType;

    @JsonProperty("restriction")
    private String restriction;

    @JsonProperty("source")
    private String source;

    @JsonProperty("message")
    private String message;

    @JsonProperty("level")
    private String level;
}
