package helpers.data.enums;

public enum Rule {
    ROUTER_RULE("Router Rule for crm_payment topic", "router_rule_crm_payment"),
    ROUTER_RULE_TRANSFER_TO_WA("Router Rule for crm_payment topic", "router_rule_crm_payment_transfer_to_wa"),
    ROUTER_RULE_SHADOW_MODE("Router Rule for crm_payment topic (shadow mode)", "router_rule_crm_payment_shadow_mode"),
    CONNECTION_SEARCH_IN_ROUTER_RULE("Connection Search Withdrawal", "cs_on_withdrawal"),
    MIRROR_TRADE_INTERNAL_HEDGE("Mirror trading (Internal Hedge)", "internal_hedge"),
    MARKET_MANIPULATION_RULE("marketManipulation_rule", "marketManipulation_rule"),
    LATENCY_ARBITRAGE_RULE("Latency Arbitrage", "latency_arbitrage"),
    MIRROR_TRADE_BYBIT("Mirror trading bybit", "mirror_trade_bybit"),
    ROUTER_RULE_WITHDRAWAL_INTEGRITY_CHECK("Withdrawal integrity check in Router rule", "withdrawal_integrity_check"),
    CPA_ABUSE_PAYMENT("Client CPA withdrawal(crm-payment)", "client_cpa_withdrawal_payment"),
    NDB_WITHDRAWAL_PAYMENT("Client NDB withdrawal(crm-payment)", "client_ndb_withdrawal_payment"),
    NO_SLIPPAGE_RULE("No slippage rule", "no_slippage"),
    NEWS_TRADE_RULE("News trade rule", "news_trade"),
    MIRROR_TRADE_ML("ML Mirror Trade", "ml_mirror_trade"),
    MIRROR_TRADE_OPEN_TRADE("Mirror trade on open trade event rule", "openTradeMirrorTrade"),
    GAP_TRADING("Gap trading rule", "gap_trade"),
    LOGIN_RULE("Login rule", "login_rule");

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
