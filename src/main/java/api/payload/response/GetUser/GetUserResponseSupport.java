package api.payload.response.GetUser;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetUserResponseSupport {
    @JsonProperty("url")
    public String url;
    @JsonProperty("text")
    public String text;
}
