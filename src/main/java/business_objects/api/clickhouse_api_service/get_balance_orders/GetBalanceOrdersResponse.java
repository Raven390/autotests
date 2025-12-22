package business_objects.api.clickhouse_api_service.get_balance_orders;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class GetBalanceOrdersResponse {

    @JsonProperty("tradeDate")
    String tradeDate;

    @JsonProperty("tradeId")
    Integer tradeId;

    @JsonProperty("tradingAccount")
    Integer tradingAccount;

    @JsonProperty("profit")
    Double profit;

    @JsonProperty("profitUSD")
    Double profitUsd;

    @JsonProperty("comment")
    String comment;

    public GetBalanceOrdersResponse() {}

    public GetBalanceOrdersResponse(
            String tradeDate,
            Integer tradeId,
            Integer tradingAccount,
            Double profit,
            Double profitUSD,
            String comment) {
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
        return Objects.equals(tradeDate, that.tradeDate)
                && Objects.equals(tradeId, that.tradeId)
                && Objects.equals(tradingAccount, that.tradingAccount)
                && Objects.equals(profit, that.profit)
                && Objects.equals(profitUsd, that.profitUsd)
                && Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tradeDate, tradeId, tradingAccount, profit, profitUsd, comment);
    }

    @Override
    public String toString() {
        return "GetBalanceOrdersResponse{" + "tradeDate='" + tradeDate + '\'' + ", tradeId=" + tradeId
                + ", tradingAccount=" + tradingAccount + ", profit=" + profit + ", profitUSD=" + profitUsd
                + ", comment='" + comment + '\'' + '}';
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public Integer getTradeId() {
        return tradeId;
    }

    public void setTradeId(Integer tradeId) {
        this.tradeId = tradeId;
    }

    public Integer getTradingAccount() {
        return tradingAccount;
    }

    public void setTradingAccount(Integer tradingAccount) {
        this.tradingAccount = tradingAccount;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public Double getProfitUsd() {
        return profitUsd;
    }

    public void setProfitUsd(Double profitUsd) {
        this.profitUsd = profitUsd;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
