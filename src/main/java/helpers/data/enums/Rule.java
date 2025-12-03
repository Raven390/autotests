package helpers.data.enums;

public enum Rule {
    ROUTER_RULE("Router Rule for crm_payment topic", "router_rule_crm_payment"), CONNECTION_SEARCH_IN_ROUTER_RULE("Connection Search Withdrawal", "cs_on_withdrawal"), MIRROR_TRADE_INTERNAL_HEDGE("Mirror trading (Internal Hedge)", "mirror_trading_internal_hedge");

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
