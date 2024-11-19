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

        @JsonProperty("currentEquity")
        public Double currentEquity;

        @JsonProperty("indicatorDate")
        public String indicatorDate;
    }

}
