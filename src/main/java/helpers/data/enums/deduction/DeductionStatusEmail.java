package helpers.data.enums.deduction;

public enum DeductionStatusEmail {
    SENT("SENT"),
    NOT_SENT("NOT_SENT");

    private final String displayName;

    DeductionStatusEmail(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
