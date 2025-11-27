package helpers.data.enums;


import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public enum FraudSource {
    VINDEX("Vindex", List.of(1, 2)), RA_RAISE("RA raise", List.of(1, 2)), ADDITIONAL_REVIEW("Additional review", List.of(1)), INSIGHT("Insight", List.of(1)), FRONTEND("Frontend", List.of(1, 2)), POST_MONITORING("Post monitoring", List.of(2));

    private final String displayName;
    private final List<Integer> type;

    FraudSource(String displayName, List<Integer> type) {
        this.displayName = displayName;
        this.type = type;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<Integer> getType() {
        return type;
    }

    @Override
    public String toString() {
        return "FraudSource{" + "displayName='" + displayName + '\'' + '}';
    }


    public static FraudSource getRandomFraudSource() {
        FraudSource[] source = values();
        SecureRandom random = new SecureRandom();
        return source[random.nextInt(source.length)];
    }

    public static List<FraudSource> getTradingFraudSourcesList() {
        return Arrays.stream(values()).filter(fraudSource -> fraudSource.getType().contains(1)).toList();
    }

    public static List<FraudSource> getPaymentFraudSourcesList() {
        return Arrays.stream(values()).filter(fraudSource -> fraudSource.getType().contains(2)).toList();
    }

    public static List<FraudSource> getTradingOnlyFraudSourcesList() {
        return Arrays.stream(values()).filter(fraudSource -> !fraudSource.getType().contains(2)).toList();
    }

    public static List<FraudSource> getPaymentOnlyFraudSourcesList() {
        return Arrays.stream(values()).filter(fraudSource -> !fraudSource.getType().contains(1)).toList();
    }

    public static List<String> getFraudSourceNames(List<FraudSource> fraudSourceList) {
        List<String> sourceNames = new ArrayList<>();
        if (fraudSourceList != null) {
            for (FraudSource f : fraudSourceList) {
                sourceNames.add(f.getDisplayName());
            }
        }
        return sourceNames;
    }

}

