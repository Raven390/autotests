package businessObjects.db.clickhouse.aggrFloatingTradesGroupBy;

public class AggrFloatingTradesGroupBy {
    public Integer tradingAccount;
    public Integer serverId;
    public String date;
    public Integer entry;
    public Integer action;
    public String symbol;
    public Double floatingProfit;
    public Double floatingProfitUsd;
    public Double totalMargin;

    public AggrFloatingTradesGroupBy() {
    }

    public AggrFloatingTradesGroupBy(Integer tradingAccount, Integer serverId, String date, Integer entry,
            Integer action, String symbol, Double floatingProfit, Double floatingProfitUsd, Double totalMargin) {
        this.tradingAccount = tradingAccount;
        this.serverId = serverId;
        this.date = date;
        this.entry = entry;
        this.action = action;
        this.symbol = symbol;
        this.floatingProfit = floatingProfit;
        this.floatingProfitUsd = floatingProfitUsd;
        this.totalMargin = totalMargin;
    }

    @Override
    public String toString() {
        return "AggrFloatingTradesGroupBy{" + "tradingAccount=" + tradingAccount + ", serverId=" + serverId + ", date='" + date + '\'' + ", entry=" + entry + ", action=" + action + ", symbol='" + symbol + '\'' + ", floatingProfit=" + floatingProfit + ", floatingProfitUsd=" + floatingProfitUsd + ", totalMargin=" + totalMargin + '}';
    }
}