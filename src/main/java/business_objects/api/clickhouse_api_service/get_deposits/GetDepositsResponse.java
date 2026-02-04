package business_objects.api.clickhouse_api_service.get_deposits;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetDepositsResponse {

    @JsonProperty("tradingAccount")
    public String tradingAccount;

    @JsonProperty("transferId")
    public String transferId;

    @JsonProperty("createTime")
    public String createTime;

    @JsonProperty("clientId")
    public String clientId;

    @JsonProperty("actualAmountUSD")
    public Double actualAmountUsd;

    @JsonProperty("actualAmount")
    public Double actualAmount;

    @JsonProperty("paymentChannel")
    public String paymentChannel;

    @JsonProperty("paymentType")
    public String paymentType;

    @JsonProperty("merchantOrderId")
    public String merchantOrderId;
}
