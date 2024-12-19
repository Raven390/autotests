package businessObjects.api.clickhouseApiService.getTradesGroupBy;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class GetTradesGroupByResponse {

    @JsonProperty("symbol")
    public String symbol;

    @JsonProperty("profit")
    public Double profit;

    @JsonProperty("profitUSD")
    public Double profitUsd;

    @JsonProperty("volumeLots")
    public Double volumeLots;

    public GetTradesGroupByResponse() {
    }

    public GetTradesGroupByResponse(String symbol, Double profit, Double profitUsd, Double volumeLots) {
        this.symbol = symbol;
        this.profit = profit;
        this.profitUsd = profitUsd;
        this.volumeLots = volumeLots;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetTradesGroupByResponse that = (GetTradesGroupByResponse) o;
        return Objects.equals(symbol, that.symbol) && Objects.equals(profit, that.profit) && Objects.equals(profitUsd, that.profitUsd) && Objects.equals(volumeLots, that.volumeLots);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, profit, profitUsd);
    }

    @Override
    public String toString() {
        return "GetTradesGroupByResponse{" + "symbol='" + symbol + '\'' + ", profit=" + profit + ", profitUsd=" + profitUsd + ", volumeLots=" + volumeLots + '}';
    }
}