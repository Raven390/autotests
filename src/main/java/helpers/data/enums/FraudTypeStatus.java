package helpers.data.enums;

public enum FraudTypeStatus {

    CONFIRMED("CONFIRMED", "Confirmed"), POTENTIAL("POTENTIAL", "Potential"), CLEANED("CLEANED", "Cleaned");

    private final String status;
    private final String displayName;

    FraudTypeStatus(String status, String displayName) {
        this.status = status;
        this.displayName = displayName;
    }

    public String getStatus() {
        return status;
    }

    public String getDisplayName() {
        return displayName;
    }
}
