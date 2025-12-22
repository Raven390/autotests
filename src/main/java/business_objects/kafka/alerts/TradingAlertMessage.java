package business_objects.kafka.alerts;

import java.time.OffsetDateTime;
import java.util.UUID;

public class TradingAlertMessage extends BaseAlertMessage {
    public TradingAlertMessage(
            UUID id,
            AlertMessageType type,
            OffsetDateTime dateTime,
            OffsetDateTime triggerCreatedTime,
            String ucid,
            Rule rule) {
        super(id, type, dateTime, triggerCreatedTime, ucid, rule);
    }
}
