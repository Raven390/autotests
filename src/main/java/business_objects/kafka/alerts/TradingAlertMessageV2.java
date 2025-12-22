package business_objects.kafka.alerts;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public class TradingAlertMessageV2 extends BaseAlertMessageV2 {
    @JsonProperty(value = "account", required = true)
    public String account;

    @JsonProperty(value = "symbol", required = true)
    public String symbol;

    @JsonProperty(value = "serverId", required = true)
    public String serverId;

    public TradingAlertMessageV2(
            UUID id,
            AlertMessageType type,
            OffsetDateTime dateTime,
            OffsetDateTime triggerCreatedTime,
            String ucid,
            String fraudType,
            String trigger,
            String reason,
            Rule rule,
            Map<String, String> attributes) {
        super(id, type, dateTime, triggerCreatedTime, ucid, fraudType, trigger, reason, rule, attributes);
    }

    public TradingAlertMessageV2(
            UUID id,
            AlertMessageType type,
            OffsetDateTime dateTime,
            OffsetDateTime triggerCreatedTime,
            String ucid,
            String fraudType,
            String trigger,
            String reason,
            Rule rule,
            Map<String, String> attributes,
            String account,
            String symbol,
            String serverId) {
        super(id, type, dateTime, triggerCreatedTime, ucid, fraudType, trigger, reason, rule, attributes);
        this.account = account;
        this.symbol = symbol;
        this.serverId = serverId;
    }
}
