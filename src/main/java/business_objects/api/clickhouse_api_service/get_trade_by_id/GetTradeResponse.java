package business_objects.api.clickhouse_api_service.get_trade_by_id;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class GetTradeResponse {

    @JsonProperty("tradeId")
    private Long tradeId;

    @JsonProperty("positionId")
    private Long positionId;

    @JsonProperty("tradeDate")
    private String tradeDate;

    @JsonProperty("tradingAccount")
    private Integer tradingAccount;

    @JsonProperty("action")
    private Integer action;

    @JsonProperty("entry")
    private Integer entry;

    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("profitUSD")
    private Double profitUSD;

    @JsonProperty("profit")
    private Double profit;

    @JsonProperty("volumeInLots")
    private Double volumeInLots;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("reason")
    private Integer reason;

    @JsonProperty("symbolUnderlying")
    private String symbolUnderlying;

    public GetTradeResponse() {
    }

    public GetTradeResponse(
            Long tradeId, String tradeDate, Integer tradingAccount, Integer action, Integer entry, String symbol,
            Double profitUSD, Double profit, Double volumeInLots, String comment, Integer reason) {
        this.tradeId = tradeId;
        this.tradeDate = tradeDate;
        this.tradingAccount = tradingAccount;
        this.action = action;
        this.entry = entry;
        this.symbol = symbol;
        this.profitUSD = profitUSD;
        this.profit = profit;
        this.volumeInLots = volumeInLots;
        this.comment = comment;
        this.reason = reason;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GetTradeResponse that)) return false;
        return Objects.equals(tradeId, that.tradeId) && Objects.equals(tradeDate, that.tradeDate) && Objects.equals(
                tradingAccount, that.tradingAccount) && Objects.equals(action, that.action) && Objects.equals(
                        entry, that.entry) && Objects.equals(symbol, that.symbol) && Objects.equals(profitUSD, that.profitUSD) && Objects.equals(
                                profit, that.profit) && Objects.equals(volumeInLots, that.volumeInLots) && Objects.equals(comment, that.comment) && Objects.equals(
                                        reason, that.reason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tradeId, tradeDate, tradingAccount, action, entry, symbol, profitUSD, profit, volumeInLots, comment, reason);
    }

    @Override
    public String toString() {
        return "GetTradeResponse{" + "tradeId='" + tradeId + '\'' + ", tradeDate='" + tradeDate + '\'' + ", tradingAccount='" + tradingAccount + '\'' + ", action='" + action + '\'' + ", entry='" + entry + '\'' + ", symbol='" + symbol + '\'' + ", profitUSD='" + profitUSD + '\'' + ", profit='" + profit + '\'' + ", volumeInLots='" + volumeInLots + '\'' + ", comment='" + comment + '\'' + ", reason='" + reason + '\'' + '}';
    }

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    public String getSymbolUnderlying() {
        return symbolUnderlying;
    }

    public void setSymbolUnderlying(String symbolUnderlying) {
        this.symbolUnderlying = symbolUnderlying;
    }

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public Integer getTradingAccount() {
        return tradingAccount;
    }

    public void setTradingAccount(Integer tradingAccount) {
        this.tradingAccount = tradingAccount;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public Integer getEntry() {
        return entry;
    }

    public void setEntry(Integer entry) {
        this.entry = entry;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Double getProfitUSD() {
        return profitUSD;
    }

    public void setProfitUSD(Double profitUSD) {
        this.profitUSD = profitUSD;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public Double getVolumeInLots() {
        return volumeInLots;
    }

    public void setVolumeInLots(Double volumeInLots) {
        this.volumeInLots = volumeInLots;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getReason() {
        return reason;
    }

    public void setReason(Integer reason) {
        this.reason = reason;
    }
}
