package helpers.data.enums;

import lombok.Getter;

@Getter
public enum DateTimeFormat {
    DATE("yyyy-MM-dd"),
    DATE_AND_TIME("yyyy-MM-dd HH:mm:ss"),
    TIME("HH:mm:ss"),
    MONTH_TEXT_AND_DAY("MMM dd"),
    MONTH_TEXT_AND_YEAR("MMM yyyy"),
    YEAR("yyyy"),
    MONTH_TEXT_AND_DAY_WITH_YEAR("MMM dd, yyyy"),
    DAY_SHORT_MONTH_YEAR("dd MMM yyyy"),
    DATE_AND_TIME_MS("yyyy-MM-dd HH:mm:ss.SSS");

    private final String displayName;

    DateTimeFormat(String displayName) {
        this.displayName = displayName;
    }
}
