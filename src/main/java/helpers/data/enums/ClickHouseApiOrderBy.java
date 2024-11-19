package helpers.data.enums;

public enum ClickHouseApiOrderBy {
    CREATE_TIME("createTime"),
    ACTUAL_AMOUNT("actualAmount"),
    ACTUAL_AMOUNT_USD("actualAmountUSD");

    private final String displayName;

    ClickHouseApiOrderBy(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}