package businessObjects.api.clickhouseApiService.getClientTradingAccounts;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class GetClientTradingAccountsResponse {

    @JsonProperty("tradingAccount")
    public String tradingAccount;

    @JsonProperty("serverId")
    public String serverId;

    public GetClientTradingAccountsResponse() {
    }

    public GetClientTradingAccountsResponse(String tradingAccount, String serverId) {
        this.tradingAccount = tradingAccount;
        this.serverId = serverId;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetClientTradingAccountsResponse that = (GetClientTradingAccountsResponse) o;
        return Objects.equals(tradingAccount, that.tradingAccount) && Objects.equals(serverId,
                                                                                     that.serverId);
    }

    @Override public int hashCode() {
        return Objects.hash(tradingAccount, serverId);
    }
}
