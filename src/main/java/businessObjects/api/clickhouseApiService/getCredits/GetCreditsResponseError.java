package businessObjects.api.clickhouseApiService.getCredits;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetCreditsResponseError {

    @JsonProperty("error")
    public String error;

    @JsonProperty("status")
    public String status;
}
