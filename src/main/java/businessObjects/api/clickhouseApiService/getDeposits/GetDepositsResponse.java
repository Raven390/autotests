package businessObjects.api.clickhouseApiService.getDeposits;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GetDepositsResponse {

    @JsonProperty
    public List<DepositItem> depositItems;

    public static class DepositItem{
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

        @JsonProperty("paymentChannel")
        public String paymentChannel;
    }

}
