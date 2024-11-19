package businessObjects.api.clickhouseApiService.getCredits;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GetCreditsResponse {

    @JsonProperty
    public List<CreditItem> creditItem;

    public static class CreditItem{
        @JsonProperty("tradeId")
        public Integer tradeId;

        @JsonProperty("openTime")
        public String openTime;

        @JsonProperty("tradingAccount")
        public String tradingAccount;

        @JsonProperty("profitUSD")
        public Float profitUSD;

        @JsonProperty("profit")
        public Float profit;

        @JsonProperty("comment")
        public String comment;
    }
}
