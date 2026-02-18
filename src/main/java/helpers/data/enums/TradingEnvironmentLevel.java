package helpers.data.enums;

import lombok.Getter;

@Getter
public enum TradingEnvironmentLevel {
    LOW("Low", "low"),
    MEDIUM("Medium", "medium"),
    HIGH("High", "high"),
    DEPTH_TWO("Depth Two", "depth_two"),
    SENIOR_HIGH("Senior High", "senior_high"),
    DEPTH_THREE("Depth Three", "depth_three"),
    DEPTH_FOUR("Depth Four", "depth_four"),
    DEPTH_FIVE("Depth Five", "depth_five"),
    DEPTH_SIX("Depth Six", "depth_six"),
    DEPTH_SEVEN("Depth Seven", "depth_seven"),
    DEPTH_EIGHT("Depth Eight", "depth_eight");
    private final String label;
    private final String externalValue;

    TradingEnvironmentLevel(String label, String externalValue) {
        this.label = label;
        this.externalValue = externalValue;
    }
}
