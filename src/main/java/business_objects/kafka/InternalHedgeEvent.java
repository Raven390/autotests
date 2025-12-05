package business_objects.kafka;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class InternalHedgeEvent {

    // JSON example for reference:
/// {
///   "type": "internalHedge",
///   "id": "b47a1d6c-6f12-4f91-8e94-6a3c43f9ef8b",
///   "schemaVersion": "1.0",
///   "timestamp": 1734973200,
///   "symbolUnderlying": "XAUUSD",
///   "positiveLeg": {
///     "ucid": "vantage-8045634",
///     "tradingAccount": 7738339,
///     "serverId": 128,
///     "closeTime": "2025-11-17T10:21:32",
///     "closeTimeUtc": "2025-11-17T08:21:32Z",
///     "shortProfitUsd": 1523.45
///   },
///   "negativeLeg": {
///     "ucid": "vantage-8045510",
///     "tradingAccount": 8113568,
///     "serverId": 128,
///     "closeTime": "2025-11-17T10:21:33",
///     "closeTimeUtc": "2025-11-17T08:21:33Z",
///     "shortProfitUsd": -1498.62
///   }
/// }
    @JsonProperty("type")
    private String type;

    @JsonProperty("id")
    private String id;

    @JsonProperty("schemaVersion")
    private String schemaVersion;

    @JsonProperty("timestamp")
    private Integer timestamp;

    @JsonProperty("symbolUnderlying")
    private String symbolUnderlying;

    @JsonProperty("positiveLeg")
    private Leg positiveLeg;

    @JsonProperty("negativeLeg")
    private Leg negativeLeg;

    public InternalHedgeEvent() {
    }

    public InternalHedgeEvent(String type,
            String id,
            String schemaVersion,
            Integer timestamp,
            String symbolUnderlying,
            Leg positiveLeg,
            Leg negativeLeg) {
        this.type = type;
        this.id = id;
        this.schemaVersion = schemaVersion;
        this.timestamp = timestamp;
        this.symbolUnderlying = symbolUnderlying;
        this.positiveLeg = positiveLeg;
        this.negativeLeg = negativeLeg;
    }

    @Getter
    @Setter
    public static class Leg {
        @JsonProperty("ucid")
        private String ucid;

        @JsonProperty("tradingAccount")
        private Integer tradingAccount;

        @JsonProperty("serverId")
        private Integer serverId;

        @JsonProperty("closeTime")
        private String closeTime;

        @JsonProperty("closeTimeUtc")
        private String closeTimeUtc;

        @JsonProperty("shortProfitUsd")
        private Double shortProfitUsd;

        public Leg() {
        }

    }
}
