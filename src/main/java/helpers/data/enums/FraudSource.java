package helpers.data.enums;

public enum FraudSource {
    VINDEX("Vindex"), RA_RAISE("RA Raise"), ADDITIONAL_REVIEW("Additional Review"), INSIGHT("Insight"), FRONTEND("Frontend");

    private final String displayName;

    FraudSource(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return "FraudSource{" + "displayName='" + displayName + '\'' + '}';
    }
}

