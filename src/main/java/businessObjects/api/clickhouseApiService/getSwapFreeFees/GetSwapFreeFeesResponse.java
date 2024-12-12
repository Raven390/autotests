package businessObjects.api.clickhouseApiService.getSwapFreeFees;

import com.fasterxml.jackson.annotation.JsonProperty;


public class GetSwapFreeFeesResponse {

    @JsonProperty("tradeDate")
    public String tradeDate;

    @JsonProperty("tradeId")
    public Integer tradeId;

    @JsonProperty("tradingAccount")
    public Integer tradingAccount;

    @JsonProperty("profit")
    public Double profit;

    @JsonProperty("profitUSD") // Correct mapping for "profitUSD"
    public Double profitUSD;

    @JsonProperty("comment")
    public String comment;

}
