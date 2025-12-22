package helpers.data.enums.deduction;

public enum DeductionTypeAccount {
    ILLEGAL_PROFIT("ILLEGAL_PROFIT"),
    NO_ILLEGAL_PROFIT("NO_ILLEGAL_PROFIT");

    private final String displayName;

    DeductionTypeAccount(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
