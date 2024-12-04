package businessObjects.api.clickhouseApiService.getCreditEquityRatio;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GetCreditEquityResponse {

    @JsonProperty("tradingAccount")
    public Integer tradingAccount;

    @JsonProperty("serverId")
    public Integer serverId;

    @JsonProperty("tradingIndicators")
    public List<TradingIndicators> tradingIndicators;

    public static class TradingIndicators{

        @JsonProperty("indicatorDate")
        public String indicatorDate;

        @JsonProperty("currentEquity")
        public String currentEquity;

        @JsonProperty("sumCreditOrder")
        public String sumCreditOrder;

        @JsonProperty("creditEquityRatio")
        public String creditEquityRatio;

    }
}
