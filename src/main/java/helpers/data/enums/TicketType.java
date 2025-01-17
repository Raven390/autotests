package helpers.data.enums;

public enum TicketType {
    BUY("Buy"), BALANCE("Balance"), BUY_STOP("Buy Stop"), BUY_LIMIT("Buy limit"), SELL("Sell"), SELL_STOP("Sell stop"), SELL_LIMIT("Sell Limit"), CREDIT("Credit");

    private final String displayName;

    TicketType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
