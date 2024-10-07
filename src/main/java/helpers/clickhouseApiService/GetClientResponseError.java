package helpers.clickhouseApiService;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetClientResponseError {

    @JsonProperty("error")
    public String error;

    @JsonProperty("status")
    public String status;
}
