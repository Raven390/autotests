package business_objects.api.clickhouse_api_service.get_clients;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class GetClientsResponse {

    @JsonProperty("objectType")
    String objectType;

    @JsonProperty("clientId")
    String clientId;

    @JsonProperty("tradingAccount")
    Integer tradingAccount;

    @JsonProperty("tradingAccountCurrency")
    String tradingAccountCurrency;

    @JsonProperty("serverId")
    Integer serverId;

    public GetClientsResponse() {
    }

    public GetClientsResponse(String clientId) {
        this.clientId = clientId;
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

    public Integer getTradingAccount() {
        return tradingAccount;
    }

    public void setTradingAccount(Integer tradingAccount) {
        this.tradingAccount = tradingAccount;
    }

    public String getTradingAccountCurrency() {
        return tradingAccountCurrency;
    }

    public void setTradingAccountCurrency(String tradingAccountCurrency) {
        this.tradingAccountCurrency = tradingAccountCurrency;
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GetClientsResponse that)) return false;
        return Objects.equals(objectType, that.objectType) && Objects.equals(clientId, that.clientId) && Objects.equals(
                tradingAccount, that.tradingAccount) && Objects.equals(tradingAccountCurrency, that.tradingAccountCurrency) && Objects.equals(
                        serverId, that.serverId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectType, clientId, tradingAccount, tradingAccountCurrency, serverId);
    }

    @Override
    public String toString() {
        return "GetClientsResponse{" + "objectType='" + objectType + '\'' + ", clientId='" + clientId + '\'' + ", tradingAccount=" + tradingAccount + ", tradingAccountCurrency=" + tradingAccountCurrency + ", serverId=" + serverId + '}';
    }
}
