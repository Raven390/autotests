package business_objects.kafka.crm_events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class TransferToWaEvent {

    // Example payload structure for reference:{
    //  "fromMt4account": 19601308,
    //  "schemaVersion": "1.0",
    //  "clientId": 10171050,
    //  "accountType": "MT5",
    //  "actualAmount": 0.03395896,
    //  "transferAmount": 101,
    //  "merchantOrderId": "AUVF1110171050ETH17640572580047",
    //  "type": "transferToWA",
    //  "transferId": 816662648,
    //  "checkName": "WR_Blacklist",
    //  "platform": "WEB",
    //  "businessOrderId": "AUVF1110171050ETH17640572580047",
    //  "statusId": 24,
    //  "toCurrency": "ETH",
    //  "transferApplicationTime": "2025-11-25 09:54:16",
    //  "regulator": "VFSC",
    //  "fromCurrency": "USD",
    //  "id": "9bbda2f7-2d7b-452d-b671-141248caba8c",
    //  "brand": "vantage",
    //  "status": "Risk Audit",
    //  "eventDate": "2025-11-25T07:55:46Z"

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

    public TransferToWaEvent(
            Integer fromMt4account, String schemaVersion, Integer clientId, String accountType, Double actualAmount,
            Double transferAmount, String merchantOrderId, String type, Long transferId, String checkName,
            String platform, String businessOrderId, Integer statusId, String toCurrency,
            String transferApplicationTime,
            String regulator, String fromCurrency, UUID id, String brand, String status, String eventDate) {
        this.fromMt4account = fromMt4account;
        this.schemaVersion = schemaVersion;
        this.clientId = clientId;
        this.accountType = accountType;
        this.actualAmount = actualAmount;
        this.transferAmount = transferAmount;
        this.merchantOrderId = merchantOrderId;
        this.type = type;
        this.transferId = transferId;
        this.checkName = checkName;
        this.platform = platform;
        this.businessOrderId = businessOrderId;
        this.statusId = statusId;
        this.toCurrency = toCurrency;
        this.transferApplicationTime = transferApplicationTime;
        this.regulator = regulator;
        this.fromCurrency = fromCurrency;
        this.id = id;
        this.brand = brand;
        this.status = status;
        this.eventDate = eventDate;
    }
}
