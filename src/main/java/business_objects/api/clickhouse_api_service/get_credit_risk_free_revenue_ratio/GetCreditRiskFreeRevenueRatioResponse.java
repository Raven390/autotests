package business_objects.api.clickhouse_api_service.get_credit_risk_free_revenue_ratio;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public class GetCreditRiskFreeRevenueRatioResponse {

    @JsonProperty("tradingAccount")
    public String tradingAccount;

    @JsonProperty("serverId")
    public String serverId;

    @JsonProperty("tradingIndicators")
    public List<TradingIndicators> tradingIndicators;

    public static class TradingIndicators {

        @JsonProperty("indicatorDate")
        public String indicatorDate;

        @JsonProperty("currentRiskFreeRevenue")
        public String currentRiskFreeRevenue;

        @JsonProperty("sumCreditOrder")
        public String sumCreditOrder;

        @JsonProperty("creditRiskFreeRevenueRatio")
        public String creditRiskFreeRevenueRatio;

    }
}
