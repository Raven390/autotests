package business_objects.api.clickhouse_api_service.get_market_close;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class GetMarketCloseResponse {

    @JsonProperty("marketCloseTime")
    private String marketCloseTime;

    public GetMarketCloseResponse() {
    }

    public GetMarketCloseResponse(String marketCloseTime) {
        this.marketCloseTime = marketCloseTime;
    }

    public String getMarketCloseTime() {
        return marketCloseTime;
    }

    public void setMarketCloseTime(String marketCloseTime) {
        this.marketCloseTime = marketCloseTime;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GetMarketCloseResponse that)) return false;
        return Objects.equals(marketCloseTime, that.marketCloseTime);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(marketCloseTime);
    }

    @Override
    public String toString() {
        return "GetMarketCloseResponse{" + "marketCloseTime='" + marketCloseTime + '\'' + '}';
    }
}
