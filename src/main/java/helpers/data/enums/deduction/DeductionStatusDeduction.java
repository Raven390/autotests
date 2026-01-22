package helpers.data.enums.deduction;

import lombok.Getter;

@Getter
public enum DeductionStatusDeduction {
    NO_DEDUCTION("NO_DEDUCTION"),
    TO_BE_DEDUCTED("TO_BE_DEDUCTED"),
    PROCESSING("PROCESSING"),
    DEDUCTED("DEDUCTED"),
    FAILED("FAILED");

    private final String displayName;

    DeductionStatusDeduction(String displayName) {
        this.displayName = displayName;
    }
}
