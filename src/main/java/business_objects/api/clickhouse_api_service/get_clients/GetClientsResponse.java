package business_objects.api.clickhouse_api_service.get_clients;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetClientsResponse {

    @JsonProperty("clientId")
    public String clientId;

}
