package helpers.data.enums;

public enum Restriction {
    OPEN_NEW_ACCOUNT(1, "01", "Open new account", "Restrict to open a new account through Client portal", "General", true), INTERNAL_TRANSFER(2, "02", "Internal transfer", "Restrict for internal account transfer through Client portal", "General", true), DEPOSITS(3, "03", "Deposits", "All client’s accounts will be blocked for deposit", "General", true), WITHDRAWALS(4, "04", "Withdrawals", "All client’s accounts will be blocked for withdrawal", "General", true), LOGIN_CRM(5, "05", "Login CRM", "Client will not be able to login to account/Client Portal", "General", true), CLOSE_ONLY_MODE(6, "06", "Close only mode", "The client to be able only to close opened deals by 365 days.", "Trading", true), READ_ONLY_MODE(10, "07", "Read-only mode", "The client is not able to close or open deals.", "Trading", false), OFF_QUOTES(11, "08", "Off quotes", "Temporarily suspends the availability of prices (quotes) for a particular asset", "Trading", false), B_BOOK_TO_A_BOOK(14, "09", "B-Book → A-Book", "The difference between A-book and B-book in trading refers to how brokers handle client orders and the associated risk management.", "Trading", false), TRADING_HOURS(12, "10", "Trading hours", "", "Trading", false), LOGIN_MT(13, "11", "Login MT", "Not access to trading account at all", "Trading", false), NOTE_FOR_WITHDRAWALS(14, "12", "Note for withdrawals", "Should we display it on UI? Note for withdrawals (used by other teams)", "General", false), MANUAL_WITHDRAWAL_REVIEW(8, "13", "Manual Withdrawal Review", "This restriction requires all withdrawal requests initiated by the client to be placed on hold for manual review by a specialist.", "General", true), CREDIT_AND_BONUS(9, "14", "Credit and Bonus", "Client will not be eligible to receive any form of promotions on any account.", "General", true), GROUP_CHANGE(15, "15", "Group Change", "", "Trading", false), KYC(16, "16", "KYC", "", "General", false), LEVERAGE(17, "17", "Leverage", "", "General", false), WARNING_LETTER(18, "18", "Warning Letter", "", "General", false), REMOVE_SWAP_FREE_OPTION(19, "19", "Remove swap free option", "", "Trading", false);

    private final int id;
    private final String code;
    private final String name;
    private final String details;
    private final String type;
    private final boolean boVisibility;

    Restriction(int id, String code, String description, String details, String type, boolean boVisibility) {
        this.id = id;
        this.code = code;
        this.name = description;
        this.details = details;
        this.type = type;
        this.boVisibility = boVisibility;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public String getType() {
        return type;
    }

    public boolean isBoVisibility() {
        return boVisibility;
    }

    public Long getIdLong() {
        return (long) id;
    }

    public Integer getId() {
        return id;
    }


    @Override
    public String toString() {
        return String.format("TransactionType{id=%d, code='%s', name='%s'}", id, code, name);
    }
}