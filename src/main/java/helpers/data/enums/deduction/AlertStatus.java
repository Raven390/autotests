package helpers.data.enums.deduction;

import lombok.Getter;

@Getter
public enum AlertStatus {
    CLOSED("CLOSED"),
    OPEN("OPEN");

    private final String displayName;

    AlertStatus(String displayName) {
        this.displayName = displayName;
    }
}
