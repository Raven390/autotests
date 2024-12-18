package businessObjects.api.clickhouseApiService.getCreditRiskFreeRevenueRatio.getWithdrawals;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public class GetCreditRiskFreeRevenueRatioResponse {

    @JsonProperty("tradingAccount")
    public Integer tradingAccount;

    @JsonProperty("serverId")
    public String serverId;

    @JsonProperty("tradingIndicators")
    public List<TradingIndicators> tradingIndicators;

    public static class TradingIndicators {

        @JsonProperty("indicatorDate")
        public String indicatorDate;

        @JsonProperty("currentRiskFreeRevenue")
        public Integer currentRiskFreeRevenue;

        @JsonProperty("sumCreditOrder")
        public Integer sumCreditOrder;

        @JsonProperty("creditRiskFreeRevenueRatio")
        public Integer creditRiskFreeRevenueRatio;

    }
}
