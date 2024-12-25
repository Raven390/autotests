package helpers.data.enums;

public enum Regulators {
    VFSC2("VFSC2"), VFSC("VFSC"), SVG("SVG"), FSC("FSC"), FCA("FCA"), FSA("FSA"), ASIC("ASIC"), CIMA("CIMA"), FMA("FMA");

    private final String displayName;

    Regulators(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}