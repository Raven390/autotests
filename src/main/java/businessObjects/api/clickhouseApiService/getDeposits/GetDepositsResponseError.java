package businessObjects.api.clickhouseApiService.getDeposits;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetDepositsResponseError {

    @JsonProperty("error")
    public String error;

    @JsonProperty("status")
    public String status;
}
