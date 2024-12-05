package businessObjects.api.clickhouseApiService.getAbuseTypes;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetAbuseTypesResponseError {

    @JsonProperty("error")
    public String error;

    @JsonProperty("status")
    public String status;

    @Override
    public String toString() {
        return "GetAbuseTypesResponseError{" + "error='" + error + '\'' + ", status='" + status + '\'' + '}';
    }
}
