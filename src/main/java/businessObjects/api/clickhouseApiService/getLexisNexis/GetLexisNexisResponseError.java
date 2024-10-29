package businessObjects.api.clickhouseApiService.getLexisNexis;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetLexisNexisResponseError {

    @JsonProperty("error")
    public String error;

    @JsonProperty("status")
    public String status;
}
