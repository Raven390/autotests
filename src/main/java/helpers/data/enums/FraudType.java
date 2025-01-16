package helpers.data.enums;

public enum FraudType {
    HEDGING("HEDGING", 1), CPA("CPA", 2), LOSS_VOUCHER_ABUSE("LOSS_VOUCHER_ABUSE", 3);

    private final String displayName;
    private final int fraudTypeId;

    FraudType(String displayName, int fraudTypeId) {
        this.displayName = displayName;
        this.fraudTypeId = fraudTypeId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getFraudTypeId() {
        return fraudTypeId;
    }
}
