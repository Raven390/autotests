package businessObjects.api.clickhouseApiService.getTradesGroupBy;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetTradesGroupByResponseError {

    @JsonProperty("error")
    public String error;

    @JsonProperty("status")
    public String status;

    @JsonProperty("type")
    public String type;

    @JsonProperty("title")
    public String title;

    @JsonProperty("detail")
    public String detail;

    @JsonProperty("instance")
    public String instance;
}
