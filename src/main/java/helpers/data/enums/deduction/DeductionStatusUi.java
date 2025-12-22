package helpers.data.enums.deduction;

public enum DeductionStatusUi {
    NO_DEDUCTION("No deduction"),
    HOLDING("Holding"),
    TO_BE_DEDUCTED("To be deducted"),
    DEDUCTED("Deducted"),
    PROCESSING("Processing"),
    DEDUCTION_FAILED("Deduction failed");

    private final String displayName;

    DeductionStatusUi(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
