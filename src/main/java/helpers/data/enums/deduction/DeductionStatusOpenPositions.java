package helpers.data.enums.deduction;

import lombok.Getter;

@Getter
public enum DeductionStatusOpenPositions {
    HOLDING("HOLDING"),
    NOT_HOLDING("NOT_HOLDING"),
    WAS_HOLDING("WAS_HOLDING");

    private final String displayName;

    DeductionStatusOpenPositions(String displayName) {
        this.displayName = displayName;
    }
}
