package businessObjects.api.clickhouseApiService.getFloatingTradesGroupBy;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetFloatingTradesGroupByResponseError {

    @JsonProperty("error")
    public String error;

    @JsonProperty("status")
    public String status;
}
