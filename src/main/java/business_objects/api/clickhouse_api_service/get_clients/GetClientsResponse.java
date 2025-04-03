package business_objects.api.clickhouse_api_service.get_clients;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class GetClientsResponse {

    @JsonProperty("clientId")
    String clientId;

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
}
