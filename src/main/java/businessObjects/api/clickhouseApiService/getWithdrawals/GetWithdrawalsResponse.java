package businessObjects.api.clickhouseApiService.getWithdrawals;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GetWithdrawalsResponse {

    @JsonProperty
    public List<WithdrawalsItem> withdrawalsItem;

    public static class WithdrawalsItem {
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
