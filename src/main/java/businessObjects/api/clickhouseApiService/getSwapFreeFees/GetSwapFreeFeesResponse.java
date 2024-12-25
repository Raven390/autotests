package businessObjects.api.clickhouseApiService.getSwapFreeFees;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class GetSwapFreeFeesResponse {

    @JsonProperty("tradeDate")
    public String tradeDate;

    @JsonProperty("tradeId")
    public Integer tradeId;

    @JsonProperty("tradingAccount")
    public Integer tradingAccount;

    @JsonProperty("profit")
    public Double profit;

    @JsonProperty("profitUSD")
    public Double profitUSD;

    @JsonProperty("comment")
    public String comment;

    public GetSwapFreeFeesResponse() {
    }

    public GetSwapFreeFeesResponse(
            String tradeDate, Integer tradeId, Integer tradingAccount, Double profit, Double profitUSD,
            String comment) {
        this.tradeDate = tradeDate;
        this.tradeId = tradeId;
        this.tradingAccount = tradingAccount;
        this.profit = profit;
        this.profitUSD = profitUSD;
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetSwapFreeFeesResponse that = (GetSwapFreeFeesResponse) o;
        return Objects.equals(tradeDate, that.tradeDate) && Objects.equals(tradeId, that.tradeId) && Objects.equals(
                tradingAccount, that.tradingAccount) && Objects.equals(profit, that.profit) && Objects.equals(
                        profitUSD, that.profitUSD) && Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tradeDate, tradeId, tradingAccount, profit, profitUSD, comment);
    }

    @Override
    public String toString() {
        return "GetSwapFreeFeesResponse{" + "tradeDate='" + tradeDate + '\'' + ", tradeId=" + tradeId + ", tradingAccount=" + tradingAccount + ", profit=" + profit + ", profitUSD=" + profitUSD + ", comment='" + comment + '\'' + '}';
    }
}
