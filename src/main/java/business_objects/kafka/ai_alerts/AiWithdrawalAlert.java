package business_objects.kafka.ai_alerts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class AiWithdrawalAlert implements AiAlert {
    /// {
    ///    "id":"d2a5c5e6-9a63-4a2a-9d32-2fd0e0a9bb55",
    ///    "eventId":"d2a5c5e6-9a63-4a2a-9d32-2fd0e0a9bb55",
    ///    "producedAtUtc":"2025-11-21T07:41:02.501Z",
    ///    "alertDate":"2025-11-21T07:39:01.501Z",
    ///    "alertType":"WITHDRAW_ALERT",
    ///    "ucid":"moneta-8045634",
    ///    "tradingAccount":20214711,
    ///    "serverId":122,
    ///    "rule":"Withdrawal Review",
    ///    "fraudType":"HEDGER",
    ///    "reason":"High Risk",
    ///    "alertText":{
    ///       "Account":"20214711",
    ///       "Amount":"261",
    ///       "Amount USD":"261",
    ///       "Brand":"moneta",
    ///       "Check":"WR_Blacklist",
    ///       "Create Time":"2025-11-17T08:21:50+02:00",
    ///       "Currency":"USD",
    ///       "Date":"2025-11-17T08:21:54+02:00",
    ///       "Order Id":"MOV2021471120251117082150",
    ///       "Payment ID":"51f3e2d0-0846-495e-9e20-c92ad927a452",
    ///       "Payment channel":"Cryptocurrency-USDT-TRC-CPS",
    ///       "Payment type":"CRYPTO",
    ///       "Platform":"MT5",
    ///       "Rate USD":"1",
    ///       "Regulator":"VFSC",
    ///       "Withdrawal ID":"198115"
    ///    }
    /// }

    @JsonProperty("id")
    private String id;

    @JsonProperty("eventId")
    private String eventId;

    @JsonProperty("producedAtUtc")
    private String producedAtUtc;

    @JsonProperty("alertDate")
    private String alertDate;

    @JsonProperty("eventDate")
    private String eventDate;

    @JsonProperty("alertType")
    private String alertType;

    @JsonProperty("ucid")
    private String ucid;

    @JsonProperty("tradingAccount")
    private Integer tradingAccount;

    @JsonProperty("serverId")
    private Integer serverId;

    @JsonProperty("rule")
    private String rule;

    @JsonProperty("fraudType")
    private String fraudType;

    @JsonProperty("reason")
    private String reason;

    @JsonProperty("alertText")
    private AlertText alertText;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlertText {

        @JsonProperty("Account")
        private String account;

        @JsonProperty("Amount")
        private String amount;

        @JsonProperty("Amount USD")
        private String amountUsd;

        @JsonProperty("Brand")
        private String brand;

        @JsonProperty("Check")
        private String check;

        @JsonProperty("Create Time")
        private String createTime;

        @JsonProperty("Currency")
        private String currency;

        @JsonProperty("Date")
        private String date;

        @JsonProperty("Order Id")
        private String orderId;

        @JsonProperty("Payment ID")
        private String paymentId;

        @JsonProperty("Payment channel")
        private String paymentChannel;

        @JsonProperty("Payment type")
        private String paymentType;

        @JsonProperty("Platform")
        private String platform;

        @JsonProperty("Rate USD")
        private String rateUsd;

        @JsonProperty("Regulator")
        private String regulator;

        @JsonProperty("Withdrawal ID")
        private Long withdrawalId;
    }
}
