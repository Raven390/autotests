package businessObjects.api.clickhouseApiService.getBonuses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GetBonusesResponse {

    @JsonProperty
    public List<BonusItem> bonusItem;

    public static class BonusItem{
        @JsonProperty("transferId")
        public Integer transferId;

        @JsonProperty("createTime")
        public String createTime;

        @JsonProperty("clientId")
        public String clientId;

        @JsonProperty("actualAmountUSD")
        public Float actualAmountUsd;

        @JsonProperty("actualAmount")
        public Float actualAmount;

        @JsonProperty("bonusType")
        public String bonusType;
    }
}
