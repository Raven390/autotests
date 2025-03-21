package business_objects.api.clickhouse_api_service.get_balance_orders;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class GetBalanceOrdersResponse {

    @JsonProperty("tradeDate")
    public String tradeDate;

    @JsonProperty("tradeId")
    public Integer tradeId;

    @JsonProperty("tradingAccount")
    public Integer tradingAccount;

    @JsonProperty("profit")
    public Double profit;

    @JsonProperty("profitUSD")
    public Double profitUsd;

    @JsonProperty("comment")
    public String comment;

    public GetBalanceOrdersResponse() {
    }

    public GetBalanceOrdersResponse(String tradeDate, Integer tradeId, Integer tradingAccount, Double profit,
            Double profitUSD, String comment) {
        this.tradeDate = tradeDate;
        this.tradeId = tradeId;
        this.tradingAccount = tradingAccount;
        this.profit = profit;
        this.profitUsd = profitUSD;
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetBalanceOrdersResponse that = (GetBalanceOrdersResponse) o;
        return Objects.equals(tradeDate, that.tradeDate) && Objects.equals(
                tradeId, that.tradeId) && Objects.equals(tradingAccount, that.tradingAccount) && Objects.equals(profit, that.profit) && Objects.equals(
                        profitUsd, that.profitUsd) && Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tradeDate, tradeId, tradingAccount, profit, profitUsd, comment);
    }

    @Override
    public String toString() {
        return "GetBalanceOrdersResponse{" + "tradeDate='" + tradeDate + '\'' + ", tradeId=" + tradeId + ", tradingAccount=" + tradingAccount + ", profit=" + profit + ", profitUSD=" + profitUsd + ", comment='" + comment + '\'' + '}';
    }
}
