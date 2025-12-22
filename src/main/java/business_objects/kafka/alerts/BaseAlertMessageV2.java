package business_objects.kafka.alerts;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeId;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        visible = true,
        defaultImpl = TradingAlertMessageV2.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = TradingAlertMessageV2.class, name = "TRADING"),
    @JsonSubTypes.Type(value = PaymentAlertMessageV2.class, name = "PAYMENT"),
})
@Getter
@Setter
public class BaseAlertMessageV2 {
    @JsonProperty(value = "alertId", required = true)
    public UUID id;

    @JsonTypeId
    @JsonProperty(value = "type", required = true)
    public AlertMessageType type;

    @JsonProperty(value = "timestamp", required = true)
    public OffsetDateTime dateTime;

    @JsonProperty(value = "triggerCreatedTime", required = true)
    public OffsetDateTime triggerCreatedTime;

    @JsonProperty(value = "ucid", required = true)
    public String ucid;

    @JsonProperty(value = "fraudType")
    public String fraudType;

    @JsonProperty(value = "trigger", required = true)
    public String trigger;

    @JsonProperty(value = "reason", required = true)
    public String reason;

    @JsonProperty(value = "rule", required = true)
    public Rule rule;

    @JsonProperty("attributes")
    public Map<String, String> attributes;

    public BaseAlertMessageV2(
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
        this.id = id;
        this.type = type;
        this.dateTime = dateTime;
        this.triggerCreatedTime = triggerCreatedTime;
        this.ucid = ucid;
        this.fraudType = fraudType;
        this.trigger = trigger;
        this.reason = reason;
        this.rule = rule;
        this.attributes = attributes;
    }

    public static class Rule {
        @JsonProperty(value = "name", required = true)
        public String name;

        @JsonProperty(value = "ver", required = true)
        public String ver;
    }
}
