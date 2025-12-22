package business_objects.api.mitigation_service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Status of the restriction
 */
public enum RestrictionStatus {
    APPLY_REQUESTED("APPLY_REQUESTED"),
    APPLIED("APPLIED"),
    APPLY_FAILED("APPLY_FAILED"),
    CANCEL_REQUESTED("CANCEL_REQUESTED"),
    CANCELLED("CANCELLED");

    private String value;

    RestrictionStatus(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static RestrictionStatus fromValue(String text) {
        for (RestrictionStatus b : RestrictionStatus.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
