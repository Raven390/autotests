package helpers.data.enums;

import lombok.Getter;

@Getter
public enum AlertType {
    TRADING("TRADING"),
    PAYMENT("PAYMENT");

    private final String displayName;

    AlertType(String displayName) {
        this.displayName = displayName;
    }
}
