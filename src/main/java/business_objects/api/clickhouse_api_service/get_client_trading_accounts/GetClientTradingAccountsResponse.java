package business_objects.api.clickhouse_api_service.get_client_trading_accounts;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class GetClientTradingAccountsResponse {

    @JsonProperty("objectType")
    String objectType;

    @JsonProperty("clientId")
    String clientId;

    @JsonProperty("tradingAccount")
    String tradingAccount;

    @JsonProperty("serverId")
    String serverId;

    public GetClientTradingAccountsResponse() {
    }

    public GetClientTradingAccountsResponse(String tradingAccount, String serverId) {
        this.tradingAccount = tradingAccount;
        this.serverId = serverId;
    }

    public GetClientTradingAccountsResponse(
            String objectType, String clientId, String tradingAccount, String serverId) {
        this.objectType = objectType;
        this.clientId = clientId;
        this.tradingAccount = tradingAccount;
        this.serverId = serverId;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GetClientTradingAccountsResponse that)) return false;
        return Objects.equals(objectType, that.objectType) && Objects.equals(clientId, that.clientId) && Objects.equals(
                tradingAccount, that.tradingAccount) && Objects.equals(serverId, that.serverId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectType, clientId, tradingAccount, serverId);
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getTradingAccount() {
        return tradingAccount;
    }

    public void setTradingAccount(String tradingAccount) {
        this.tradingAccount = tradingAccount;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    @Override
    public String toString() {
        return "GetClientTradingAccountsResponse{" + "objectType='" + objectType + '\'' + ", clientId='" + clientId + '\'' + ", tradingAccount='" + tradingAccount + '\'' + ", serverId='" + serverId + '\'' + '}';
    }
}
