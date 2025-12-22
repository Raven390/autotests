package business_objects.kafka.alerts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Audit event type for kafka messages
 */
public enum AlertMessageType {
    TRADING("TRADING"),
    PAYMENT("PAYMENT");

    private String value;

    AlertMessageType(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static AlertMessageType fromValue(String text) {
        for (AlertMessageType b : AlertMessageType.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
