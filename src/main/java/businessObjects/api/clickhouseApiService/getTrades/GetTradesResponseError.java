package businessObjects.api.clickhouseApiService.getTrades;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetTradesResponseError {

    @JsonProperty("error")
    public String error;

    @JsonProperty("status")
    public String status;
}
