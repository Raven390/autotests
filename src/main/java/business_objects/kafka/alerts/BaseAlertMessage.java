package business_objects.kafka.alerts;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeId;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        visible = true,
        defaultImpl = TradingAlertMessage.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = TradingAlertMessage.class, name = "TRADING"),
    @JsonSubTypes.Type(value = PaymentAlertMessage.class, name = "PAYMENT"),
})
public class BaseAlertMessage {
    @JsonProperty(value = "alertId", required = true)
    private UUID id;

    @JsonTypeId
    @JsonProperty(value = "type", required = true)
    private AlertMessageType type;

    @JsonProperty(value = "timestamp", required = true)
    private OffsetDateTime dateTime;

    @JsonProperty(value = "triggerCreatedTime", required = false)
    private OffsetDateTime triggerCreatedTime;

    @JsonProperty(value = "ucid", required = true)
    private String ucid;

    @JsonProperty(value = "rule", required = true)
    private Rule rule;

    public BaseAlertMessage(
            UUID id,
            AlertMessageType type,
            OffsetDateTime dateTime,
            OffsetDateTime triggerCreatedTime,
            String ucid,
            Rule rule) {
        this.id = id;
        this.type = type;
        this.dateTime = dateTime;
        this.triggerCreatedTime = triggerCreatedTime;
        this.ucid = ucid;
        this.rule = rule;
    }

    public UUID getId() {
        return id;
    }

    public AlertMessageType getType() {
        return type;
    }

    public OffsetDateTime getDateTime() {
        return dateTime;
    }

    public OffsetDateTime getTriggerCreatedTime() {
        return triggerCreatedTime;
    }

    public String getUcid() {
        return ucid;
    }

    public Rule getRule() {
        return rule;
    }

    public static class Rule {
        @JsonProperty(value = "name", required = true)
        private String name;

        @JsonProperty(value = "fraudType", required = true)
        private String fraudType;

        @JsonProperty(value = "ver", required = true)
        private String ver;

        @JsonProperty(value = "trigger", required = true)
        private String trigger;

        @JsonProperty("attributes")
        private Map<String, String> attributes;

        public Rule(String name, String fraudType, String ver, String trigger, Map<String, String> attributes) {
            this.name = name;
            this.fraudType = fraudType;
            this.ver = ver;
            this.trigger = trigger;
            this.attributes = attributes;
        }
    }
}
