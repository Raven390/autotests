package helpers.clickhouseApiService.getLexisNexis;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetLexisNexisResponse {

//    {
//            "uid": "123e4567-e89b-12d3-a456-426614174000",
//            "id": 987654321,
//            "brand": "example_brand",
//            "sessionId": "example_session_id",
//            "userId": 98765,
//            "email": "example_email@example.com",
//            "mobileCode": "1",
//            "mobile": "1234567890",
//            "eventType": "example_event_type",
//            "isFromApp": true,
//            "createTime": "2024-10-24T13:02:01Z",
//            "policyScore": 7,
//            "riskRating": "high",
//            "deviceId": "example_device_id",
//            "digitalId": "example_digital_id",
//            "eventDatetime": "2024-10-24T13:02:01Z",
//            "eventId": 1234,
//            "proxyIp": "193.13.54.22",
//            "proxyIpActivities": "example_proxy_ip_activities",
//            "proxyIpAttributes": ["attr1", "attr2"],
//            "proxyIpCity": "example_city",
//            "proxyIpConnectionType": "example_connection_type",
//            "proxyIpFirstSeen": "2024-01-01",
//            "proxyIpGeo": "example_geo",
//            "proxyIpHome": "example_home",
//            "proxyIpIsp": "example_isp",
//            "proxyIpLatitude": 37.77490,
//            "proxyIpLongitude": -122.41940,
//            "proxyIpOrganization": "example_organization",
//            "proxyIpOrganizationType": "example_organization_type",
//            "proxyIpPostalCode": "94103",
//            "proxyIpRegion": "example_region",
//            "proxyIpResult": "example_result",
//            "proxyIpRoutingType": "example_routing_type",
//            "proxyIpScore": 6,
//            "proxyIpWorstScore": 2,
//            "proxyIpv6": "example_ipv6",
//            "proxyName": "example_proxy_name",
//            "proxyScore": 78.56,
//            "proxyType": "example_proxy_type",
//            "trueIp": null,
//            "trueIpActivities": "example_true_ip_activities",
//            "trueIpAttributes": ["attr3", "attr4"],
//            "trueIpCity": "example_true_ip_city",
//            "trueIpCountryConfidence": 95,
//            "trueIpFirstSeen": "2023-01-01",
//            "trueIpGeo": "example_true_ip_geo",
//            "trueIpIsp": "example_true_ip_isp",
//            "trueIpLastEvent": "2023-12-01",
//            "trueIpOrganization": "example_true_ip_organization",
//            "trueIpOrganizationType": "example_true_ip_organization_type",
//            "trueIpPostalCode": "94103",
//            "trueIpRegion": "example_true_ip_region",
//            "trueIpResult": "example_true_ip_result",
//            "trueIpRoutingType": "example_true_ip_routing_type",
//            "trueIpScore": 5,
//            "trueIpWorstScore": 3,
//            "trueIpv6": "example_true_ipv6",
//            "vpnScore": 100
//    }

    @JsonProperty("uid")
    public String uid;

    @JsonProperty("id")
    public long id;

    @JsonProperty("brand")
    public String brand;

    @JsonProperty("sessionId")
    public String sessionId;

    @JsonProperty("userId")
    public long userId;

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
