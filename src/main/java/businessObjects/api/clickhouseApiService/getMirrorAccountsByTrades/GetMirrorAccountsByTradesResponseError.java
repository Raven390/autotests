package businessObjects.api.clickhouseApiService.getMirrorAccountsByTrades;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetMirrorAccountsByTradesResponseError {

    @JsonProperty("error")
    public String error;

    @JsonProperty("status")
    public String status;
}
