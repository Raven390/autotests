package api.payload.response.GetUser;


import com.fasterxml.jackson.annotation.JsonProperty;

public class GetUserResponse {

    @JsonProperty("data")
    public GetUserResponseData data;
    @JsonProperty("support")
    public GetUserResponseSupport support;

}

