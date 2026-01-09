package business_objects.kafka.alerts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleAlertV2 {

    @JsonProperty("reason")
    private String reason;

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("alertId")
    private String alertId;

    @JsonProperty("rule")
    private Rule rule;

    @JsonProperty("triggerCreatedTime")
    private String triggerCreatedTime;

    @JsonProperty("fraudType")
    private String fraudType;

    @JsonProperty("trigger")
    private String trigger;

    @JsonProperty("ucid")
    private String ucid;

    @JsonProperty("type")
    private String type;

    @JsonProperty("amount")
    private Double amount;

    @JsonProperty("amountUSD")
    private Double amountUsd;

    @JsonProperty("paymentMethod")
    private String paymentMethod;

    @JsonProperty("merchantOrderId")
    private String merchantOrderId;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("account")
    private Integer account;

    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("serverId")
    private String serverId;

    @JsonProperty("paymentEventId")
    private String paymentEventId;

    @JsonProperty("attributes")
    private Attribute attributes;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Rule {

        @JsonProperty("ver")
        private String ver;

        @JsonProperty("name")
        private String name;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Attribute {

        @JsonProperty("Account 2nd leg")
        private String accountNegativeLeg;

        @JsonProperty("Server 2nd leg")
        private String serverIdNegativeLeg;

        @JsonProperty("Platform")
        private String platform;

        @JsonProperty("Create Time")
        private String createTime;

        @JsonProperty("Check")
        private String check;

        @JsonProperty("Date")
        private String date;

        @JsonProperty("Withdrawal ID")
        private Long withdrawalId;

        @JsonProperty("Regulator")
        private String regulator;

        @JsonProperty("Brand")
        private String brand;

        @JsonProperty("Payment channel")
        private String paymentChannel;

        @JsonProperty("Details")
        private String details;

        @JsonProperty("Ucid")
        private String ucid;

        @JsonProperty("Ucid 2nd leg")
        private String ucidNegativeLeg;

        @JsonProperty("UcidScore")
        private Double ucidScore;

        @JsonProperty("Profile deposits")
        private String profileDeposits;

        @JsonProperty("Profile withdrawals")
        private String profileWithdrawals;

        @JsonProperty("Shared payment profile")
        private String sharedPaymentProfile;

        @JsonProperty("Crypto withdrawal > 10k")
        private Boolean cryptoWithdrawal10k;

        @JsonProperty("Crypto deposit")
        private String cryptoDeposit;

        @JsonProperty("Crypto withdrawal")
        private String cryptoWithdrawal;

        @JsonProperty("Shared unique identifier")
        private String sharedUniqueIdentifier;

        @JsonProperty("Verified by other client")
        private String verifiedByOtherClient;

        @JsonProperty("Open trades")
        private String openTrades;

        @JsonProperty("High-value multi-card")
        private String highValueMultiCard;

        @JsonProperty("Same name: Card & KYC")
        private String sameNameCardKyc;

        @JsonProperty("1st deposit and no open trades")
        private String firstDepositNoOpenTrades;

        @JsonProperty("Fraud score")
        private Integer fraudScore;

        @JsonProperty("Shared card across UIDs")
        private String sharedCardAcrossUids;

        @JsonProperty("Multiple unique cards")
        private String multipleUniqueCards;

        @JsonProperty("Failed attempts")
        private String failedAttempts;

        @JsonProperty("Payment profile")
        private String paymentProfile;

        @JsonProperty("Is there a fraud decline")
        private String isThereAFraudDecline;

        @JsonProperty("3DS")
        private String threeDs;

        @JsonProperty("Card used by known fraudster")
        private String cardUsedByKnownFraudster;

        @JsonProperty("Profit")
        private String profit;

        @JsonProperty("Withdrawals")
        private String withdrawals;

        @JsonProperty("Risk free revenue")
        private String riskFreeRevenue;

        @JsonProperty("Amount of credits")
        private String amountOfCredits;

        @JsonProperty("Floating profit USD")
        private String floatingProfitUSD;

        @JsonProperty("Internal hedge time")
        private String internalHedgeTime;
    }
}
