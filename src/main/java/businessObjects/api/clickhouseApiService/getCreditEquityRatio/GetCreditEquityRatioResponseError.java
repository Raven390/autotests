package businessObjects.api.clickhouseApiService.getCreditEquityRatio;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetCreditEquityRatioResponseError {

    @JsonProperty("error")
    public String error;

    @JsonProperty("status")
    public String status;
}
