package business_objects.kafka.crm_events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CrmWithdrawalEventV2 {
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
    private Integer clientId;

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

    @JsonProperty("paymentTypeName")
    private String paymentTypeName;

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

    @JsonProperty("status")
    private String status;

    @JsonProperty("eWallet")
    private EWallet eWallet;

    @JsonProperty("localBankTransfer")
    private LocalBankTransfer localBankTransfer;

    @JsonProperty("crypto")
    private Crypto crypto;

    @JsonProperty("needReprocessing")
    private Boolean needReprocessing;

    @JsonProperty("card")
    private Card card;

    @Getter
    @Setter
    @Builder
    public static class Card {
        private int binNumber;
        private int lastFour;
        private int expMonth;
        private int expYear;
    }

    @Getter
    @Setter
    @Builder
    public static class EWallet {
        private String accountName;
        private String accountNumber;
    }

    @Getter
    @Setter
    @Builder
    public static class LocalBankTransfer {
        private String accountName;
        private String accountNumber;
    }

    @Getter
    @Setter
    @Builder
    public static class Crypto {
        private String walletAddress;
    }
}
