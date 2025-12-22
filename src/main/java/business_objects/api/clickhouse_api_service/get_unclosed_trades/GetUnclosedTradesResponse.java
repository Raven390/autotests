package business_objects.api.clickhouse_api_service.get_unclosed_trades;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetUnclosedTradesResponse {

    @JsonProperty("openTime")
    public String tradeDate;

    @JsonProperty("tradeId")
    public Long tradeId;

    @JsonProperty("tradingAccount")
    public Integer tradingAccount;

    @JsonProperty("profit")
    public Double profit;

    @JsonProperty("symbol")
    public String symbol;

    @JsonProperty("profitUSD")
    public Double profitUsd;

    @JsonProperty("comment")
    public String comment;
}
