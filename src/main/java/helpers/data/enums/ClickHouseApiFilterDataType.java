package helpers.data.enums;

public enum ClickHouseApiFilterDataType {
    PERIOD("period"),
    BEFORE_DATE("before_date");

    private final String displayName;

    ClickHouseApiFilterDataType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}