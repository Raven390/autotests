package helpers.data.enums.deduction;

public enum AlertStatus {
    CLOSED("CLOSED"), OPEN("OPEN");

    private final String displayName;

    AlertStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
