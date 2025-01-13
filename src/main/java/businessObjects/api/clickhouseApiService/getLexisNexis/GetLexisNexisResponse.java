package businessObjects.api.clickhouseApiService.getLexisNexis;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetLexisNexisResponse {

    @JsonProperty("uid")
    public String uid;

    @JsonProperty("ucid")
    public String ucid;

    @JsonProperty("id")
    public int id;

    @JsonProperty("brand")
    public String brand;

    @JsonProperty("sessionId")
    public String sessionId;

    @JsonProperty("userId")
    public int userId;

    @JsonProperty("email")
    public String email;

    @JsonProperty("mobileCode")
    public String mobileCode;

    @JsonProperty("mobile")
    public String mobile;

    @JsonProperty("eventType")
    public String eventType;

    @JsonProperty("isFromApp")
    public boolean isFromApp;

    @JsonProperty("createTime")
    public String createTime;

    @JsonProperty("policyScore")
    public int policyScore;

    @JsonProperty("riskRating")
    public String riskRating;

    @JsonProperty("deviceId")
    public String deviceId;

    @JsonProperty("digitalId")
    public String digitalId;

    @JsonProperty("eventDatetime")
    public String eventDatetime;

    @JsonProperty("eventId")
    public int eventId;

    @JsonProperty("proxyIp")
    public String proxyIp;

    @JsonProperty("proxyIpActivities")
    public String proxyIpActivities;

    @JsonProperty("proxyIpAttributes")
    public String[] proxyIpAttributes;

    @JsonProperty("proxyIpCity")
    public String proxyIpCity;

    @JsonProperty("proxyIpConnectionType")
    public String proxyIpConnectionType;

    @JsonProperty("proxyIpFirstSeen")
    public String proxyIpFirstSeen;

    @JsonProperty("proxyIpGeo")
    public String proxyIpGeo;

    @JsonProperty("proxyIpHome")
    public String proxyIpHome;

    @JsonProperty("proxyIpIsp")
    public String proxyIpIsp;

    @JsonProperty("proxyIpLatitude")
    public double proxyIpLatitude;

    @JsonProperty("proxyIpLongitude")
    public double proxyIpLongitude;

    @JsonProperty("proxyIpOrganization")
    public String proxyIpOrganization;

    @JsonProperty("proxyIpOrganizationType")
    public String proxyIpOrganizationType;

    @JsonProperty("proxyIpPostalCode")
    public String proxyIpPostalCode;

    @JsonProperty("proxyIpRegion")
    public String proxyIpRegion;

    @JsonProperty("proxyIpResult")
    public String proxyIpResult;

    @JsonProperty("proxyIpRoutingType")
    public String proxyIpRoutingType;

    @JsonProperty("proxyIpScore")
    public int proxyIpScore;

    @JsonProperty("proxyIpWorstScore")
    public int proxyIpWorstScore;

    @JsonProperty("proxyIpv6")
    public String proxyIpv6;

    @JsonProperty("proxyName")
    public String proxyName;

    @JsonProperty("proxyScore")
    public double proxyScore;

    @JsonProperty("proxyType")
    public String proxyType;

    @JsonProperty("trueIp")
    public String trueIp;

    @JsonProperty("trueIpActivities")
    public String trueIpActivities;

    @JsonProperty("trueIpAttributes")
    public String[] trueIpAttributes;

    @JsonProperty("trueIpCity")
    public String trueIpCity;

    @JsonProperty("trueIpCountryConfidence")
    public int trueIpCountryConfidence;

    @JsonProperty("trueIpFirstSeen")
    public String trueIpFirstSeen;

    @JsonProperty("trueIpGeo")
    public String trueIpGeo;

    @JsonProperty("trueIpIsp")
    public String trueIpIsp;

    @JsonProperty("trueIpLastEvent")
    public String trueIpLastEvent;

    @JsonProperty("trueIpOrganization")
    public String trueIpOrganization;

    @JsonProperty("trueIpOrganizationType")
    public String trueIpOrganizationType;

    @JsonProperty("trueIpPostalCode")
    public String trueIpPostalCode;

    @JsonProperty("trueIpRegion")
    public String trueIpRegion;

    @JsonProperty("trueIpResult")
    public String trueIpResult;

    @JsonProperty("trueIpRoutingType")
    public String trueIpRoutingType;

    @JsonProperty("trueIpScore")
    public int trueIpScore;

    @JsonProperty("trueIpWorstScore")
    public int trueIpWorstScore;

    @JsonProperty("trueIpv6")
    public String trueIpv6;

    @JsonProperty("vpnScore")
    public int vpnScore;

}
