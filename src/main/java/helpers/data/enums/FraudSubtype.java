package helpers.data.enums;

import lombok.Getter;

@Getter
public enum FraudSubtype {
    EXTERNAL("EXTERNAL", "External"),
    INTERNAL("INTERNAL", "Internal"),
    BEFORE_NEWS("BEFORE_NEWS", "Before - no deduction"),
    BEFORE_NEWS_DEDUCTION("BEFORE_NEWS_DEDUCTION", "Before - with deduction"),
    AFTER_NEWS("AFTER_NEWS", "After news"),
    SINGLE_ACCOUNT("SINGLE_ACCOUNT", "No deduction"),
    HEDGING_STRATEGY("HEDGING_STRATEGY", "With deduction"),
    FIRST_TIME("FIRST_TIME", "First time"),
    SECOND_TIME("SECOND_TIME", "Second time"),
    MULTIPLE_TIMES("MULTIPLE_TIMES", "Three times and more");

    private final String code;
    private final String name;

    FraudSubtype(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
