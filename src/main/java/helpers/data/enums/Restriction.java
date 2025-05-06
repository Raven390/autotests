package helpers.data.enums;

import static utils.Constants.RESTRICTION_TYPE_GENERAL;
import static utils.Constants.RESTRICTION_TYPE_TRADING;

public enum Restriction {
    ACCOUNT_CREATION_REVIEW(1, "01", "Account creation review", "Restricts the creation of a new account until a manual review is conducted by a specialist.", RESTRICTION_TYPE_GENERAL, false), INTERNAL_TRANSFER(2, "02", "Internal transfer", "Restrict internal transfers between client's accounts.", RESTRICTION_TYPE_GENERAL, true), DEPOSITS(3, "03", "Deposits", "All client’s accounts will be blocked for deposit.", RESTRICTION_TYPE_GENERAL, true), WITHDRAWALS(4, "04", "Withdrawals", "Withdrawals will be completely restricted for all client's accounts, preventing the user from submitting a withdrawal request.", RESTRICTION_TYPE_GENERAL, true), LOGIN_CRM(5, "05", "Login CRM", "Client will not be able to log in to Client Portal.", RESTRICTION_TYPE_GENERAL, true), CLOSE_ONLY_MODE(6, "06", "Close only mode", "The client will be able only to close deals that were opened in past 365 days.", RESTRICTION_TYPE_TRADING, true), READ_ONLY_MODE(10, "07", "Read-only mode", "The client Is not able to close or open a deals by 365 days.", RESTRICTION_TYPE_TRADING, false), OFF_QUOTES(11, "08", "Off quotes", "Temporarily suspends the availability of prices (quotes) for a particular asset.", RESTRICTION_TYPE_TRADING, true), B_BOOK_TO_A_BOOK(7, "09", "B-Book → A-Book", "A-book passes client orders to external markets, while B-book keeps them in-house, making the broker the counterparty.", RESTRICTION_TYPE_TRADING, false), TRADING_HOURS(12, "10", "Trading hours", "Restrict for internal account transfer", RESTRICTION_TYPE_TRADING, false), LOGIN_MT(13, "11", "Login MT", "Not access to trading account at all.", RESTRICTION_TYPE_TRADING, false), NOTE_FOR_WITHDRAWALS(14, "12", "Note for withdrawals", "Note for withdrawals (uses by others team)", RESTRICTION_TYPE_GENERAL, true), MANUAL_WITHDRAWAL_REVIEW(8, "13", "Manual withdrawal review", "All withdrawal requests initiated by the client will be placed on hold for manual review by a specialist.", RESTRICTION_TYPE_GENERAL, true), CREDIT_AND_BONUS(9, "14", "Credit and bonus", "Prevent the client from seeing a Deposit Bonus and No Deposit Bonus promotions.", RESTRICTION_TYPE_GENERAL, true), GROUP_CHANGE(15, "15", "Group Change", "", RESTRICTION_TYPE_TRADING, false), KYC(16, "16", "KYC", "", RESTRICTION_TYPE_GENERAL, false), LEVERAGE(17, "17", "Leverage", "", RESTRICTION_TYPE_GENERAL, false), WARNING_LETTER(18, "18", "Warning Letter", "", RESTRICTION_TYPE_GENERAL, false), REMOVE_SWAP_FREE_OPTION(19, "19", "Remove swap free option", "", RESTRICTION_TYPE_TRADING, false), ACCOUNT_CREATION(20, "20", "Account creation", "Automatically restricts creation of a new account, rejecting it without a confirmation from a specialist.", RESTRICTION_TYPE_GENERAL, true);

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