package business_objects.api.clickhouse_api_service.get_trades_group_by;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class GetTradesGroupByResponse {

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

    public GetTradesGroupByResponse() {
    }

    public GetTradesGroupByResponse(
            String clientId, String symbol, Double profit, Double profitUsd, Double volumeLots, Double pnlUsd,
            Double storageUsd, Double commissionUsd) {
        this.clientId = clientId;
        this.symbol = symbol;
        this.profit = profit;
        this.profitUsd = profitUsd;
        this.volumeLots = volumeLots;
        this.pnlUsd = pnlUsd;
        this.storageUsd = storageUsd;
        this.commissionUsd = commissionUsd;
    }

    @Deprecated(forRemoval = true)
    public GetTradesGroupByResponse(String clientId, String symbol, Double profit, Double profitUsd,
            Double volumeLots) {
        this.clientId = clientId;
        this.symbol = symbol;
        this.profit = profit;
        this.profitUsd = profitUsd;
        this.volumeLots = volumeLots;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GetTradesGroupByResponse that = (GetTradesGroupByResponse) o;
        return Objects.equals(clientId, that.clientId) && Objects.equals(symbol, that.symbol) && Objects.equals(
                profit, that.profit) && Objects.equals(profitUsd, that.profitUsd) && Objects.equals(
                        volumeLots, that.volumeLots) && Objects.equals(pnlUsd, that.pnlUsd) && Objects.equals(
                                storageUsd, that.storageUsd) && Objects.equals(commissionUsd, that.commissionUsd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, symbol, profit, profitUsd, volumeLots, pnlUsd, storageUsd, commissionUsd);
    }

    @Override
    public String toString() {
        return "GetTradesGroupByResponse{" + "clientId='" + clientId + '\'' + ", symbol='" + symbol + '\'' + ", profit=" + profit + ", profitUsd=" + profitUsd + ", volumeLots=" + volumeLots + ", pnlUsd=" + pnlUsd + ", storageUsd=" + storageUsd + ", commissionUsd=" + commissionUsd + '}';
    }
}