package helpers.data.enums;

import lombok.Getter;

@Getter
public enum AlertResolution {
    CONFIRMED("CONFIRMED"),
    FALSE_POSITIVE("FALSE_POSITIVE"),
    FRAUD_TYPE_MISMATCH("FRAUD_TYPE_MISMATCH"),
    UNDEFINED("UNDEFINED");

    private final String displayName;

    AlertResolution(String displayName) {
        this.displayName = displayName;
    }
}
