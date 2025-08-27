package helpers.data.enums.deduction;

public enum DeductionType {
    PARTIAL_DEDUCTION("PARTIAL_DEDUCTION"), FULL_DEDUCTION("FULL_DEDUCTION"), NO_DEDUCTION("NO_DEDUCTION");

    private final String displayName;

    DeductionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}