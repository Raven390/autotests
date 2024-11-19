package businessObjects.api.clickhouseApiService.getTradesGroupBy;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GetTradesGroupByResponse {

    @JsonProperty()
    public List<TradesItem> groups;

    public static class TradesItem{
        @JsonProperty("symbol")
        public String symbol;

        @JsonProperty("profit")
        public Float profit;

        @JsonProperty("profitUSD")
        public Float profitUsd;
    }
}