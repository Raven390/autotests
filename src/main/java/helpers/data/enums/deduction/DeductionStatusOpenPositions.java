package helpers.data.enums.deduction;

public enum DeductionStatusOpenPositions {
    HOLDING("HOLDING"),
    NOT_HOLDING("NOT_HOLDING"),
    WAS_HOLDING("WAS_HOLDING");

    private final String displayName;

    DeductionStatusOpenPositions(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
