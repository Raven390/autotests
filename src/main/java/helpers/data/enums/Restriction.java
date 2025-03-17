package helpers.data.enums;

public enum Restriction {
    ACCOUNT_CREATION_REVIEW(1, "01", "Account creation review", "Restricts the creation of a new account until a manual review is conducted by a specialist.", "GENERAL", true), INTERNAL_TRANSFER(2, "02", "Internal transfer", "Restrict for internal account transfer.", "GENERAL", true), DEPOSITS(3, "03", "Deposits", "All client’s accounts will be blocked for deposit.", "GENERAL", true), WITHDRAWALS(4, "04", "Withdrawals", "Withdrawals are completely restricted for all client accounts, preventing the user from even submitting a withdrawal request.", "GENERAL", true), LOGIN_CRM(5, "05", "Login CRM", "Client will not be able to login in Client Portal.", "GENERAL", true), CLOSE_ONLY_MODE(6, "06", "Close only mode", "The client to be able only to close opened deals by 365 days.", "TRADING", true), READ_ONLY_MODE(10, "07", "Read-only mode", "The client Is not able to close or open a deals by 365 days.", "TRADING", false), OFF_QUOTES(11, "08", "Off quotes", "Temporarily suspends the availability of prices (quotes) for a particular asset.", "TRADING", false), B_BOOK_TO_A_BOOK(7, "09", "B-Book → A-Book", "A-book passes client orders to external markets, while B-book keeps them in-house, making the broker the counterparty.", "TRADING", false), TRADING_HOURS(12, "10", "Trading hours", "", "TRADING", false), LOGIN_MT(13, "11", "Login MT", "Not access to trading account at all.", "TRADING", false), NOTE_FOR_WITHDRAWALS(14, "12", "Note for withdrawals", "Should we display it on UI? Note for withdrawals (used by other teams)", "GENERAL", false), MANUAL_WITHDRAWAL_REVIEW(8, "13", "Manual withdrawal Review", "All withdrawal requests initiated by the client will be placed on hold for manual review by specialist.", "GENERAL", false), CREDIT_AND_BONUS(9, "14", "Credit and Bonus", "Prevents the client from seeing the Deposit Bonus and No Deposit Bonus promotions.", "GENERAL", false), GROUP_CHANGE(15, "15", "Group Change", "", "TRADING", false), KYC(16, "16", "KYC", "", "GENERAL", false), LEVERAGE(17, "17", "Leverage", "", "GENERAL", false), WARNING_LETTER(18, "18", "Warning Letter", "", "GENERAL", false), REMOVE_SWAP_FREE_OPTION(19, "19", "Remove swap free option", "", "TRADING", false), ACCOUNT_CREATION(20, "20", "Automatically restricts the creation of a new account, rejecting it without confirmation from a specialist.", "", "GENERAL", true);

    private final int id;
    private final String code;
    private final String name;
    private final String description;
    private final String type;
    private final boolean boVisibility;

    Restriction(int id, String code, String name, String description, String type, boolean boVisibility) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.type = type;
        this.boVisibility = boVisibility;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
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