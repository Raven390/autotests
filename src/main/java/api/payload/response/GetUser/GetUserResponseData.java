package api.payload.response.GetUser;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetUserResponseData {
    @JsonProperty("id")
    public int id;
    @JsonProperty("email")
    public String email;
    @JsonProperty("first_name")
    public String first_name;
    @JsonProperty("last_name")
    public String last_name;
    @JsonProperty("avatar")
    public String avatar;
}
