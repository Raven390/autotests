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

    @JsonProperty("serverId")
    Integer serverId;

    public GetClientsResponse() {
    }

    public GetClientsResponse(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GetClientsResponse that = (GetClientsResponse) o;
        return Objects.equals(clientId, that.clientId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(clientId);
    }

    @Override
    public String toString() {
        return "GetClientsResponse{" + "clientId='" + clientId + '\'' + '}';
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public Integer getTradingAccount() {
        return tradingAccount;
    }

    public void setTradingAccount(Integer tradingAccount) {
        this.tradingAccount = tradingAccount;
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }
}
