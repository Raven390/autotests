package helpers.data.enums;

public enum ClickHouseApiSortOrder {
    ASC("asc"),
    DESC("desc");

    private final String displayName;

    ClickHouseApiSortOrder(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}