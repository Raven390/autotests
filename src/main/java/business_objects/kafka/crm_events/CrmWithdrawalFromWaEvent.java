package business_objects.kafka.crm_events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CrmWithdrawalFromWaEvent {

    @JsonProperty("id")
    private String id;

    @JsonProperty("eventDate")
    private String eventDate;

    @JsonProperty("regulator")
    private String regulator;

    @JsonProperty("brand")
    private String brand;

    @JsonProperty("server")
    private String server;

    @JsonProperty("clientId")
    private Integer clientId;

    @JsonProperty("type")
    private String type;

    @JsonProperty("merchantOrderId")
    private String merchantOrderId;

    @JsonProperty("walletAccount")
    private String walletAccount;

    @JsonProperty("walletWithdrawTaskOrderNo")
    private String walletWithdrawTaskOrderNo;

    @JsonProperty("walletApplyChain")
    private String walletApplyChain;

    @JsonProperty("walletSymbol")
    private String walletSymbol;

    @JsonProperty("walletAddress")
    private String walletAddress;

    @JsonProperty("statusId")
    private Integer statusId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("withdrawalApplicationTime")
    private String withdrawalApplicationTime;

    @JsonProperty("withdrawalCurrency")
    private String withdrawalCurrency;

    @JsonProperty("withdrawalAmount")
    private Double withdrawalAmount;

    @JsonProperty("actualAmount")
    private Double actualAmount;

    @JsonProperty("withdrawalAmountUSD")
    private Double withdrawalAmountUSD;

    @JsonProperty("cost")
    private Double cost;

    @JsonProperty("paymentMethodCode")
    private String paymentMethodCode;

    @JsonProperty("paymentChannelCode")
    private String paymentChannelCode;

    @JsonProperty("paymentChannelName")
    private String paymentChannelName;

    @JsonProperty("paymentTypeCode")
    private String paymentTypeCode;

    @JsonProperty("paymentTypeName")
    private String paymentTypeName;

    @JsonProperty("needReprocessing")
    private Boolean needReprocessing;
}
