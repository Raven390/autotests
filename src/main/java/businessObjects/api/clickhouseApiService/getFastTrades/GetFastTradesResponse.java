package businessObjects.api.clickhouseApiService.getFastTrades;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class GetFastTradesResponse {

    @JsonProperty("tradeDateOpen")
    public String tradeDateOpen;

    @JsonProperty("tradeDateClose")
    public String tradeDateClose;

    @JsonProperty("tradeId")
    public Long tradeId;

    @JsonProperty("tradingAccount")
    public Integer tradingAccount;

    @JsonProperty("serverId")
    public Integer serverId;

    @JsonProperty("symbol")
    public String symbol;

    @JsonProperty("profit")
    public Double profit;

    @JsonProperty("profitUSD")
    public Double profitUsd;

    @JsonProperty("comment")
    public String comment;

    public GetFastTradesResponse() {
    }

    public GetFastTradesResponse(String tradeDateOpen, String tradeDateClose, Long tradeId, Integer tradingAccount,
            Integer serverId, String symbol, Double profit, Double profitUsd, String comment) {
        this.tradeDateOpen = tradeDateOpen;
        this.tradeDateClose = tradeDateClose;
        this.tradeId = tradeId;
        this.tradingAccount = tradingAccount;
        this.serverId = serverId;
        this.symbol = symbol;
        this.profit = profit;
        this.profitUsd = profitUsd;
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GetFastTradesResponse that = (GetFastTradesResponse) o;
        return Objects.equals(tradeDateOpen, that.tradeDateOpen) && Objects.equals(tradeDateClose, that.tradeDateClose) && Objects.equals(tradeId, that.tradeId) && Objects.equals(tradingAccount, that.tradingAccount) && Objects.equals(serverId, that.serverId) && Objects.equals(symbol, that.symbol) && Objects.equals(profit, that.profit) && Objects.equals(profitUsd, that.profitUsd) && Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tradeDateOpen, tradeDateClose, tradeId, tradingAccount, serverId, symbol, profit, profitUsd, comment);
    }

    @Override
    public String toString() {
        return "GetFastTradesResponse{" + "tradeDateOpen='" + tradeDateOpen + '\'' + ", tradeDateClose='" + tradeDateClose + '\'' + ", tradeId=" + tradeId + ", tradingAccount=" + tradingAccount + ", serverId=" + serverId + ", symbol='" + symbol + '\'' + ", profit=" + profit + ", profitUsd=" + profitUsd + ", comment='" + comment + '\'' + '}';
    }
}
