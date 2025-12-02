package helpers.data.enums;

public enum Rule {
    ROUTER_RULE("Router Rule for crm_payment topic", "router_rule_crm_payment"), CONNECTION_SEARCH_IN_ROUTER_RULE("Connection Search Withdrawal", "cs_on_withdrawal");

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
