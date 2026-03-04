package business_objects.kafka.ai_alerts;

import com.fasterxml.jackson.annotation.JsonAlias;
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
public class AiTradingAlert implements AiAlert {
    /// {
    ///    "id":"7b2b7b2a-7e51-4b6e-9bd2-6b9e0e7a2c11",
    ///    "eventId":"d2a5c5e6-9a63-4a2a-9d32-2fd0e0a9bb55",
    ///    "producedAtUtc":"2025-11-21T07:40:15.123Z",
    ///    "alertDate":"2025-11-21T07:39:01.501Z",
    ///    "alertType":"TRADING_ALERT",
    ///    "ucid":"vantage-8045634",
    ///    "tradingAccount":3101888,
    ///    "serverId":49,
    ///    "rule":"Latency Arbitrage",
    ///    "fraudType":"HEDGER",
    ///    "reason":"WEB Hedge",
    ///    "alertText":{
    ///       "Account":"3101888",
    ///       "Reason":"Detected suspicious Latency Arbitrage pattern",
    ///       "Symbol traded":"XAUUSD+",
    ///       "Ticket id":"13971228",
    ///       "serverId":"49"
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

        @JsonProperty("Reason")
        private String reason;

        @JsonProperty("Symbol traded")
        private String symbolTraded;

        @JsonProperty("Ticket id")
        @JsonAlias("Ticket ID")
        private String ticketId;

        @JsonProperty("serverId")
        private String serverId;
    }
}
