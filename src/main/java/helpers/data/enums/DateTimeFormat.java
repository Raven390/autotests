package helpers.data.enums;

public enum DateTimeFormat {
    DATE("yyyy-MM-dd"), DATE_AND_TIME("yyyy-MM-dd HH:mm:ss"), TIME("HH:mm:ss"), MONTH_TEXT_AND_DAY("MMM dd"), MONTH_TEXT_AND_YEAR("MMM yyyy"), YEAR("yyyy"), MONTH_TEXT_AND_DAY_WITH_YEAR("MMM dd, yyyy");

    private final String displayName;

    DateTimeFormat(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
