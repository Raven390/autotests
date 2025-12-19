package business_objects.api.mitigation_service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The type of restriction
 */
public enum RestrictionType {
    GENERAL("GENERAL"), TRADING("TRADING"), TRADING_ENVIRONMENT("TRADING_ENVIRONMENT"), BYBIT("BYBIT");

    private String value;

    RestrictionType(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static RestrictionType fromValue(String text) {
        for (RestrictionType b : RestrictionType.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
