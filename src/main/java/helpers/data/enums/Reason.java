package helpers.data.enums;

public enum Reason {
    API("API"),
    CLIENT("Client"),
    GATEWAY("Gateway"),
    EXPERT("Expert"),
    DEALER("Dealer"),
    MOBILE("Mobile"),
    SIGNAL("Signal"),
    WEB("Web");

    private final String displayName;

    Reason(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
