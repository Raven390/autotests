package business_objects.api.mitigation_service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets CorrelationType
 */
public enum CorrelationType {
    INVESTIGATION("INVESTIGATION"), BATCH("BATCH"), FRAUD_MANAGEMENT("FRAUD_MANAGEMENT"), RESTRICTION_MANAGEMENT("RESTRICTION_MANAGEMENT"), RULE_ENGINE("RULE_ENGINE");

    private String value;

    CorrelationType(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static CorrelationType fromValue(String text) {
        for (CorrelationType b : CorrelationType.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
