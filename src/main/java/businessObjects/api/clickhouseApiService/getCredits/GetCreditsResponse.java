package businessObjects.api.clickhouseApiService.getCredits;

import com.fasterxml.jackson.annotation.JsonProperty;


public class GetCreditsResponse {

    @JsonProperty("tradeId")
    public Integer tradeId;

    @JsonProperty("createTime")
    public String createTime;

    @JsonProperty("tradingAccount")
    public String tradingAccount;

    @JsonProperty("profitUSD")
    public Double profitUSD;

    @JsonProperty("profit")
    public Double profit;

    @JsonProperty("comment")
    public String comment;

    @Override
    public String toString() {
        return "GetCreditsResponse{" +
                "tradeId=" + tradeId +
                ", createTime='" + createTime + '\'' +
                ", tradingAccount='" + tradingAccount + '\'' +
                ", profitUSD=" + profitUSD +
                ", profit=" + profit +
                ", comment='" + comment + '\'' +
                '}';
    }
}
