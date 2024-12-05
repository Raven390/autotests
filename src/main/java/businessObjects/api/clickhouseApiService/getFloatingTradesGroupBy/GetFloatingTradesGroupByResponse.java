package businessObjects.api.clickhouseApiService.getFloatingTradesGroupBy;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GetFloatingTradesGroupByResponse {

    @JsonProperty
    public List<FloatingTradesItem> floatingTradesItems;

    public static class FloatingTradesItem {
        @JsonProperty("symbol")
        public String symbol;

        @JsonProperty("floatingProfit")
        public Integer floatingProfit;

        @JsonProperty("floatingProfitUSD")
        public Integer floatingProfitUSD;

        @JsonProperty("totalMargin")
        public Integer totalMargin;
    }

}
