package business_objects.kafka.crm_events;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class TransferToWaEvent {

    @JsonProperty("fromMt4account")
    private Integer fromMt4account;

    @JsonProperty("schemaVersion")
    private String schemaVersion;

    @JsonProperty("clientId")
    private Integer clientId;

    @JsonProperty("accountType")
    private String accountType;

    @JsonProperty("actualAmount")
    private Double actualAmount;

    @JsonProperty("transferAmount")
    private Double transferAmount;

    @JsonProperty("merchantOrderId")
    private String merchantOrderId;

    @JsonProperty("type")
    private String type;

    @JsonProperty("transferId")
    private Long transferId;

    @JsonProperty("checkName")
    private String checkName;

    @JsonProperty("platform")
    private String platform;

    @JsonProperty("businessOrderId")
    private String businessOrderId;

    @JsonProperty("statusId")
    private Integer statusId;

    @JsonProperty("toCurrency")
    private String toCurrency;

    @JsonProperty("transferApplicationTime")
    private String transferApplicationTime;

    @JsonProperty("regulator")
    private String regulator;

    @JsonProperty("fromCurrency")
    private String fromCurrency;

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("brand")
    private String brand;

    @JsonProperty("status")
    private String status;

    @JsonProperty("eventDate")
    private String eventDate;
}
