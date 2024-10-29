package businessObjects.api.clickhouseApiService.getClient;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetClientResponse {

    @JsonProperty("websiteUserType")
    public String websiteUserType;

    @JsonProperty("clientId")
    public String clientId;

    @JsonProperty("userId")
    public String userId;

    @JsonProperty("gender")
    public String gender;

    @JsonProperty("regulator")
    public String regulator;

    @JsonProperty("phoneNum")
    public String phoneNum;

    @JsonProperty("phoneCountryCode")
    public String phoneCountryCode;

    @JsonProperty("lastName")
    public String lastName;

    @JsonProperty("twoFaUser")
    public String twoFaUser;

    @JsonProperty("authentication")
    public String authentication;

    @JsonProperty("firstName")
    public String firstName;

    @JsonProperty("email")
    public String email;

    @JsonProperty("createTime")
    public String createTime;

    @JsonProperty("countryCode")
    public String countryCode;

    @JsonProperty("language")
    public String language;

    @JsonProperty("nationality")
    public String nationality;

    @JsonProperty("isoCountryCode")
    public String isoCountryCode;

    @JsonProperty("country")
    public String country;

    @JsonProperty("brand")
    public String brand;

    @JsonProperty("birthday")
    public String birthday;

    @JsonProperty("registrationDate")
    public String registrationDate;

    @JsonProperty("emailVerificationDate")
    public String emailVerificationDate;

    @JsonProperty("phoneVerificationDate")
    public String phoneVerificationDate;

    @JsonProperty("ibId")
    public String ibId;

    @JsonProperty("cpaId")
    public String cpaId;

    @JsonProperty("rafReferrerId")
    public String rafReferrerId;

    @JsonProperty("lastUpdated")
    public String lastUpdated;

    @JsonProperty("kycStatus")
    public String kycStatus;

}
