package helpers.data.enums;

public enum AlertType {
    TRADING("TRADING"),
    PAYMENT("PAYMENT");

    private final String displayName;

    AlertType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
