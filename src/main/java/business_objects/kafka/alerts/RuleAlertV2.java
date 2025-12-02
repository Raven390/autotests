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

        @JsonProperty("Same data: eWallet & KYC")
        private String sameDataEwalletKyc;

    }
}
