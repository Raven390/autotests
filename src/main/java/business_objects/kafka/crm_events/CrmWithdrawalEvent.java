package business_objects.kafka.crm_events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CrmWithdrawalEvent {
    @JsonProperty("accountType")
    private String accountType;

    @JsonProperty("accountCategory")
    private String accountCategory;

    @JsonProperty("binNumber")
    private String binNumber;

    @JsonProperty("brand")
    private String brand;

    @JsonProperty("checkName")
    private String checkName;

    @JsonProperty("clientId")
    private Long clientId;

    @JsonProperty("eventDate")
    private String eventDate;

    @JsonProperty("expMonth")
    private String expMonth;

    @JsonProperty("expYear")
    private String expYear;

    @JsonProperty("fullName")
    private String fullName;

    @JsonProperty("id")
    private String id;

    @JsonProperty("merchantOrderId")
    private String merchantOrderId;

    @JsonProperty("mt4Account")
    private Integer mt4Account;

    @JsonProperty("paymentChannelCode")
    private String paymentChannelCode;

    @JsonProperty("paymentChannelName")
    private String paymentChannelName;

    @JsonProperty("paymentMethodCode")
    private String paymentMethodCode;

    @JsonProperty("platform")
    private String platform;

    @JsonProperty("regulator")
    private String regulator;

    @JsonProperty("schemaVersion")
    private String schemaVersion;

    @JsonProperty("type")
    private String type;

    @JsonProperty("fundType")
    private String fundType;

    @JsonProperty("withdrawalAmount")
    private Double withdrawalAmount;

    @JsonProperty("withdrawalAmountUSD")
    private Double withdrawalAmountUSD;

    @JsonProperty("withdrawalApplicationTime")
    private String withdrawalApplicationTime;

    @JsonProperty("withdrawalCurrency")
    private String withdrawalCurrency;

    @JsonProperty("withdrawalId")
    private Long withdrawalId;

    public CrmWithdrawalEvent(
            String accountType,
            String binNumber,
            String brand,
            String checkName,
            long clientId,
            String eventDate,
            String expMonth,
            String expYear,
            String fullName,
            String id,
            String merchantOrderId,
            Integer mt4Account,
            String paymentChannelCode,
            String paymentChannelName,
            String paymentMethodCode,
            String platform,
            String regulator,
            String schemaVersion,
            String type,
            Double withdrawalAmount,
            Double withdrawalAmountUSD,
            String withdrawalApplicationTime,
            String withdrawalCurrency,
            Long withdrawalId) {
        this.accountType = accountType;
        this.binNumber = binNumber;
        this.brand = brand;
        this.checkName = checkName;
        this.clientId = clientId;
        this.eventDate = eventDate;
        this.expMonth = expMonth;
        this.expYear = expYear;
        this.fullName = fullName;
        this.id = id;
        this.merchantOrderId = merchantOrderId;
        this.mt4Account = mt4Account;
        this.paymentChannelCode = paymentChannelCode;
        this.paymentChannelName = paymentChannelName;
        this.paymentMethodCode = paymentMethodCode;
        this.platform = platform;
        this.regulator = regulator;
        this.schemaVersion = schemaVersion;
        this.type = type;
        this.withdrawalAmount = withdrawalAmount;
        this.withdrawalAmountUSD = withdrawalAmountUSD;
        this.withdrawalApplicationTime = withdrawalApplicationTime;
        this.withdrawalCurrency = withdrawalCurrency;
        this.withdrawalId = withdrawalId;
    }
}
