package businessObjects.api.clickhouseApiService.getClients;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetClientsResponseError {

    @JsonProperty("error")
    public String error;

    @JsonProperty("status")
    public String status;
}
