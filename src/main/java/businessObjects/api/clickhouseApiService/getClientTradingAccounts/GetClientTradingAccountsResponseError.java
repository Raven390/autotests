package businessObjects.api.clickhouseApiService.getClientTradingAccounts;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetClientTradingAccountsResponseError {

    @JsonProperty("error")
    public String error;

    @JsonProperty("status")
    public Integer status;
}
