package businessObjects.api.clickhouseApiService.getDeposits;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetDepositsResponseError {

    @JsonProperty("error")
    public String error;

    @JsonProperty("status")
    public Integer status;

    @JsonProperty("type")
    public String type;

    @JsonProperty("title")
    public String title;

    @JsonProperty("detail")
    public String detail;

    @JsonProperty("instance")
    public String instance;

    @Override
    public String toString() {
        return "GetBonusesResponseError{" + "error='" + error + '\'' + ", status=" + status + ", type='" + type + '\'' + ", title='" + title + '\'' + ", detail='" + detail + '\'' + ", instance='" + instance + '\'' + '}';
    }
}
