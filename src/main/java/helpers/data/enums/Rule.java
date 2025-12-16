package helpers.data.enums;

public enum Rule {
    ROUTER_RULE("Router Rule for crm_payment topic", "router_rule_crm_payment"), ROUTER_RULE_TRANSFER_TO_WA("Router Rule for crm_payment topic", "router_rule_crm_payment_transfer_to_wa"), CONNECTION_SEARCH_IN_ROUTER_RULE("Connection Search Withdrawal", "cs_on_withdrawal"), MIRROR_TRADE_INTERNAL_HEDGE("Mirror trading (Internal Hedge)", "mirror_trading_internal_hedge"), ROUTER_RULE_WITHDRAWAL_INTEGRITY_CHECK("Withdrawal integrity check in Router rule", "withdrawal_integrity_check");

    private final String name;
    private final String processId;

    Rule(String name, String processId) {
        this.name = name;
        this.processId = processId;
    }

    public String getName() {
        return name;
    }

    public String getProcessId() {
        return processId;
    }
}
