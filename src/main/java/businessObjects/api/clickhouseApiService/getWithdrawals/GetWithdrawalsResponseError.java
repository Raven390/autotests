package businessObjects.api.clickhouseApiService.getWithdrawals;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetWithdrawalsResponseError {

    @JsonProperty("error")
    public String error;

    @JsonProperty("status")
    public String status;
}
