package helpers.data.enums;

public enum Restriction {
    OPEN_NEW_ACCOUNT(1L, 1L, "Open new account", "GENERAL"), INTERNAL_TRANSFER(2L, 2L, "Internal transfer", "GENERAL"), DEPOSITS(3L, 3L, "Deposits", "GENERAL"), WITHDRAWALS(4L, 4L, "Withdrawals", "GENERAL"), LOGIN_CRM(5L, 5L, "Login CRM", "GENERAL"), CLOSE_ONLY_MODE(6L, 6L, "Close only mode", "TRADING"), B_BOOK_TO_A_BOOK(7L, 9L, "B-Book -> A-Book", "TRADING"), MANUAL_WITHDRAWAL_REVIEW(8L, 13L, "Manual Withdrawal Review", "GENERAL");

    private Long id;
    private Long code;
    private String name;
    private String type;

    Restriction(Long id, Long code, String name, String type) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public Long getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("TransactionType{id=%d, code='%s', name='%s'}", id, code, name);
    }
}