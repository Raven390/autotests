package helpers.clickhouseApiService;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetClientResponse {

    @JsonProperty("websiteUserType")
    public String websiteUserType;

    @JsonProperty("clientId")
    public String clientId;

    @JsonProperty("regulator")
    public String regulator;

    @JsonProperty("phoneNum")
    public String phoneNum;

    @JsonProperty("phoneCountryCode")
    public String phoneCountryCode;

    @JsonProperty("updateTime")
    public String updateTime;

    @JsonProperty("lastName")
    public String lastName;

    @JsonProperty("twoFaUser")
    public String twoFaUser;

    @JsonProperty("firstName")
    public String firstName;

    @JsonProperty("email")
    public String email;

    @JsonProperty("createTime")
    public String createTime;

    @JsonProperty("countryCode")
    public String countryCode;

    @JsonProperty("brand")
    public String brand;

    @JsonProperty("birthday")
    public String birthday;

    @JsonProperty("registrationDate")
    public String registrationDate;
}
