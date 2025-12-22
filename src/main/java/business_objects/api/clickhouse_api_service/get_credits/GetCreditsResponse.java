package business_objects.api.clickhouse_api_service.get_credits;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetCreditsResponse {

    @JsonProperty("tradeId")
    public Integer tradeId;

    @JsonProperty("clientId")
    public String clientId;

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
        return "GetCreditsResponse{" + "tradeId=" + tradeId + ", clientId=" + clientId + ", createTime='" + createTime
                + '\'' + ", tradingAccount='" + tradingAccount + '\'' + ", profitUSD=" + profitUSD + ", profit="
                + profit + ", comment='" + comment + '\'' + '}';
    }
}
