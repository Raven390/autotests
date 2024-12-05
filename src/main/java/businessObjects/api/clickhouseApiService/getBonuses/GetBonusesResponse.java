package businessObjects.api.clickhouseApiService.getBonuses;

import com.fasterxml.jackson.annotation.JsonProperty;


public class GetBonusesResponse {

    @JsonProperty("transferId")
    public Integer transferId;

    @JsonProperty("createTime")
    public String createTime;

    @JsonProperty("clientId")
    public String clientId;

    @JsonProperty("actualAmountUSD")
    public Double actualAmountUsd;

    @JsonProperty("actualAmount")
    public Double actualAmount;

    @JsonProperty("bonusType")
    public String bonusType;

    @Override
    public String toString() {
        return "GetBonusesResponse{" + "transferId=" + transferId + ", createTime='" + createTime + '\'' + ", clientId='" + clientId + '\'' + ", actualAmountUSD=" + actualAmountUsd + ", actualAmount=" + actualAmount + ", bonusType='" + bonusType + '\'' + '}';
    }
}
