package businessObjects.api.clickhouseApiService.getUnclosedTrades;

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

    @JsonProperty("profitUSD")
    public Double profitUsd;

    @JsonProperty("comment")
    public String comment;

}
