package helpers.data.enums;

public enum Brand {
    PU_PRIME("PU Prime"), MONETA("Moneta"), INFINOX("Infinox"), VANTAGE("Vantage"), ULTIMA_MARKETS("Ultima Markets"), VT("VT"), VJP("VJP"), STAR_TRADER("StarTrader");

    private final String displayName;

    Brand(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}