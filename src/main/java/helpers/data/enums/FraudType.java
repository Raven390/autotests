package helpers.data.enums;

import java.util.Random;

public enum FraudType {
    HEDGING(1, "HEDGING", "Hedging"), LATENCY_ARBITRAGE(2, "LATENCY_ARBITRAGE", "Latency arbitrage"), MARKET_MANIPULATION(3, "MARKET_MANIPULATION", "Market manipulation"), PRICING_ERRORS(4, "PRICING_ERRORS", "Pricing errors"), GAP_TRADING(5, "GAP_TRADING", "Gap trading"), SWAP_ARBITRAGE(6, "SWAP_ARBITRAGE", "Swap arbitrage"), CPA_ABUSE(7, "CPA_ABUSE", "CPA abuse"), RAF_ABUSE(9, "RAF_ABUSE", "RAF abuse"), REBATE_CHURNING(10, "REBATE_CHURNING", "Rebate churning"), LOSS_VOUCHER_ABUSE(11, "LOSS_VOUCHER_ABUSE", "Loss voucher abuse"), NBP_ABUSE(12, "NBP_ABUSE", "NBP abuse"), TLS_ABUSE(13, "TLS_ABUSE", "TLS abuse"), POTENTIAL_ABUSE(14, "POTENTIAL_ABUSE", "Potential abuse"), BONUS_ABUSE(15, "BONUS_ABUSE", "Bonus abuse"), LOOPHOLE_ABUSE(16, "LOOPHOLE_ABUSE", "Loophole abuse"), HFT_ABUSE(17, "HFT_ABUSE", "HFT abuse"), NEWS_TRADER(18, "NEWS_TRADER", "News trader"), ANOMALOUS_PROFIT(19, "ANOMALOUS_PROFIT", "Anomalous profit");

    private final int fraudTypeId;
    private final String key;
    private final String displayName;

    FraudType(int fraudTypeId, String key, String displayName) {
        this.fraudTypeId = fraudTypeId;
        this.key = key;
        this.displayName = displayName;
    }

    public int getFraudTypeId() {
        return fraudTypeId;
    }

    public String getKey() {
        return key;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static FraudType getById(int id) {
        for (FraudType fraud : values()) {
            if (fraud.getFraudTypeId() == id) {
                return fraud;
            }
        }
        throw new IllegalArgumentException("No FraudType with id " + id);
    }

    public static FraudType getByKey(String key) {
        for (FraudType issue : values()) {
            if (issue.getKey().equalsIgnoreCase(key)) {
                return issue;
            }
        }
        throw new IllegalArgumentException("No FraudType with key " + key);
    }

    public static FraudType getRandomFraudType() {
        FraudType[] frauds = values();
        Random random = new Random();
        return frauds[random.nextInt(frauds.length)];
    }
}

