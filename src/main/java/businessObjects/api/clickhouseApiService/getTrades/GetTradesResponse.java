package businessObjects.api.clickhouseApiService.getTrades;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GetTradesResponse {

    @JsonProperty
    public List<TradesItem> tradesItems;

    public static class TradesItem{
        @JsonProperty("tradeDate")
        public String tradeDate;

        @JsonProperty("tradeId")
        public Integer tradeId;

        @JsonProperty("tradingAccount")
        public Integer tradingAccount;

        @JsonProperty("action")
        public Integer action;

        @JsonProperty("entry")
        public Integer entry;

        @JsonProperty("symbol")
        public String symbol;

        @JsonProperty("profit")
        public Float profit;

        @JsonProperty("profitUSD")
        public Float profitUsd;

        @JsonProperty("comment")
        public String comment;
    }

}
