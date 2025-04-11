package business_objects.api.clickhouse_api_service.get_trades;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class GetTradesResponse {

    @JsonProperty("clientId")
    public String clientId;

    @JsonProperty("tradeDate")
    public String tradeDate;

    @JsonProperty("tradeId")
    public Long tradeId;

    @JsonProperty("tradingAccount")
    public Integer tradingAccount;

    @JsonProperty("action")
    public Integer action;

    @JsonProperty("entry")
    public Integer entry;

    @JsonProperty("symbol")
    public String symbol;

    @JsonProperty("profit")
    public Double profit;

    @JsonProperty("profitUSD")
    public Double profitUsd;

    @JsonProperty("comment")
    public String comment;

    public GetTradesResponse() {
    }

    public GetTradesResponse(
            String clientId, String tradeDate, Long tradeId, Integer tradingAccount, Integer action, Integer entry,
            String symbol, Double profit, Double profitUsd, String comment) {
        this.clientId = clientId;
        this.tradeDate = tradeDate;
        this.tradeId = tradeId;
        this.tradingAccount = tradingAccount;
        this.action = action;
        this.entry = entry;
        this.symbol = symbol;
        this.profit = profit;
        this.profitUsd = profitUsd;
        this.comment = comment;
    }

    public GetTradesResponse(String tradeDate, Long tradeId, Integer tradingAccount, Integer action, Integer entry,
            String symbol, Double profit, Double profitUsd, String comment) {
        this.tradeDate = tradeDate;
        this.tradeId = tradeId;
        this.tradingAccount = tradingAccount;
        this.action = action;
        this.entry = entry;
        this.symbol = symbol;
        this.profit = profit;
        this.profitUsd = profitUsd;
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GetTradesResponse that = (GetTradesResponse) o;
        return Objects.equals(clientId, that.clientId) && Objects.equals(tradeDate, that.tradeDate) && Objects.equals(
                tradeId, that.tradeId) && Objects.equals(tradingAccount, that.tradingAccount) && Objects.equals(action, that.action) && Objects.equals(
                        entry, that.entry) && Objects.equals(symbol, that.symbol) && Objects.equals(profit, that.profit) && Objects.equals(
                                profitUsd, that.profitUsd) && Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, tradeDate, tradeId, tradingAccount, action, entry, symbol, profit, profitUsd, comment);
    }

    @Override
    public String toString() {
        return "GetTradesResponse{" + "clientId='" + clientId + '\'' + ", tradeDate='" + tradeDate + '\'' + ", tradeId=" + tradeId + ", tradingAccount=" + tradingAccount + ", action=" + action + ", entry=" + entry + ", symbol='" + symbol + '\'' + ", profit=" + profit + ", profitUsd=" + profitUsd + ", comment='" + comment + '\'' + '}';
    }
}
