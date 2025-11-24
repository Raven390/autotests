package helpers.data.enums;

public enum VerificationStatus {
    NOT_VERIFIED("Not verified"), AWAITING_DOCUMENTS("Awaiting documents"), VERIFIED("Verified"), REJECTED("Rejected");

    private final String displayName;

    VerificationStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
