package businessObjects.api.clickhouseApiService.getBonuses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetBonusesResponseError {

    @JsonProperty("error")
    public String error;

    @JsonProperty("status")
    public String status;
}
