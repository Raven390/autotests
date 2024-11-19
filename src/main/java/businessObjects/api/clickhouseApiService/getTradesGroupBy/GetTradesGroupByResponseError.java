package businessObjects.api.clickhouseApiService.getTradesGroupBy;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetTradesGroupByResponseError {

    @JsonProperty("error")
    public String error;

    @JsonProperty("status")
    public String status;
}
