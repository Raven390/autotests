package businessObjects.api.clickhouseApiService.getClient;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetClientResponseError {

    @JsonProperty("error")
    public String error;

    @JsonProperty("status")
    public String status;
}
