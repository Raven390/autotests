package business_objects.api.clickhouse_api_service.get_crypto_deposits;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetCryptoDepositsResponse {

    @JsonProperty("createTime")
    public String createTime;

    @JsonProperty("tradingAccount")
    public String tradingAccount;

    @JsonProperty("serverId")
    public String serverId;

    @JsonProperty("transferId")
    public String transferId;

    @JsonProperty("clientId")
    public String clientId;

    @JsonProperty("actualAmountUSD")
    public Double actualAmountUSD;

    @JsonProperty("actualAmount")
    public Double actualAmount;
}
