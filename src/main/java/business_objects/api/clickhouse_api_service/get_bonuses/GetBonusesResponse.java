package business_objects.api.clickhouse_api_service.get_bonuses;

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

    @JsonProperty("bonusGroup")
    public String bonusGroup;

    @Override
    public String toString() {
        return "GetBonusesResponse{" + "transferId=" + transferId + ", createTime='" + createTime + '\'' + ", clientId='" + clientId + '\'' + ", actualAmountUsd=" + actualAmountUsd + ", actualAmount=" + actualAmount + ", bonusType='" + bonusType + '\'' + ", bonusGroup='" + bonusGroup + '\'' + '}';
    }
}