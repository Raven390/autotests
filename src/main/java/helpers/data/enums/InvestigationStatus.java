package helpers.data.enums;

public enum InvestigationStatus {
    ACTIVE("ACTIVE"),
    COMPLETED("COMPLETED"),
    NEW("NEW");

    private final String displayName;

    InvestigationStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
