package business_objects.api.clickhouse_api_service.get_trades_group_by;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class GetTradesGroupByResponse {

    @JsonProperty("tradingAccount")
    public String tradingAccount;

    @JsonProperty("serverId")
    public String serverId;

    @JsonProperty("clientId")
    public String clientId;

    @JsonProperty("symbol")
    public String symbol;

    @JsonProperty("profit")
    public Double profit;

    @JsonProperty("profitUSD")
    public Double profitUsd;

    @JsonProperty("volumeLots")
    public Double volumeLots;

    @JsonProperty("pnlUSD")
    public Double pnlUsd;

    @JsonProperty("storageUSD")
    public Double storageUsd;

    @JsonProperty("commissionUSD")
    public Double commissionUsd;

    public GetTradesGroupByResponse() {}

    @Deprecated(forRemoval = true)
    public GetTradesGroupByResponse(String symbol, Double profit, Double profitUsd, Double volumeLots) {
        this.symbol = symbol;
        this.profit = profit;
        this.profitUsd = profitUsd;
        this.volumeLots = volumeLots;
    }

    public GetTradesGroupByResponse(
            String tradingAccount,
            String serverId,
            String clientId,
            String symbol,
            Double profit,
            Double profitUsd,
            Double volumeLots,
            Double pnlUsd,
            Double storageUsd,
            Double commissionUsd) {
        this.tradingAccount = tradingAccount;
        this.serverId = serverId;
        this.clientId = clientId;
        this.symbol = symbol;
        this.profit = profit;
        this.profitUsd = profitUsd;
        this.volumeLots = volumeLots;
        this.pnlUsd = pnlUsd;
        this.storageUsd = storageUsd;
        this.commissionUsd = commissionUsd;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GetTradesGroupByResponse that)) return false;
        return Objects.equals(tradingAccount, that.tradingAccount)
                && Objects.equals(serverId, that.serverId)
                && Objects.equals(clientId, that.clientId)
                && Objects.equals(symbol, that.symbol)
                && Objects.equals(profit, that.profit)
                && Objects.equals(profitUsd, that.profitUsd)
                && Objects.equals(volumeLots, that.volumeLots)
                && Objects.equals(pnlUsd, that.pnlUsd)
                && Objects.equals(storageUsd, that.storageUsd)
                && Objects.equals(commissionUsd, that.commissionUsd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                tradingAccount,
                serverId,
                clientId,
                symbol,
                profit,
                profitUsd,
                volumeLots,
                pnlUsd,
                storageUsd,
                commissionUsd);
    }

    @Override
    public String toString() {
        return "GetTradesGroupByResponse{" + "tradingAccount='" + tradingAccount + '\'' + ", serverId='" + serverId
                + '\'' + ", symbol='" + symbol + '\'' + ", profit=" + profit + ", profitUsd=" + profitUsd
                + ", volumeLots=" + volumeLots + ", pnlUsd=" + pnlUsd + ", storageUsd=" + storageUsd
                + ", commissionUsd=" + commissionUsd + '}';
    }

    public String getTradingAccount() {
        return tradingAccount;
    }

    public void setTradingAccount(String tradingAccount) {
        this.tradingAccount = tradingAccount;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
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

    public Double getVolumeLots() {
        return volumeLots;
    }

    public void setVolumeLots(Double volumeLots) {
        this.volumeLots = volumeLots;
    }

    public Double getPnlUsd() {
        return pnlUsd;
    }

    public void setPnlUsd(Double pnlUsd) {
        this.pnlUsd = pnlUsd;
    }

    public Double getStorageUsd() {
        return storageUsd;
    }

    public void setStorageUsd(Double storageUsd) {
        this.storageUsd = storageUsd;
    }

    public Double getCommissionUsd() {
        return commissionUsd;
    }

    public void setCommissionUsd(Double commissionUsd) {
        this.commissionUsd = commissionUsd;
    }
}
