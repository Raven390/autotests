package helpers.kafka.crmDbEvents.eventGeneratorInbound.login;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginDbEventData {

    @JsonProperty("login_datetime")
    public String loginDatetime;

    @JsonProperty("user_id")
    public Integer userId;

    @JsonProperty("brand")
    public String brand;

    @JsonProperty("ip_address")
    public String ipAddress;

    @JsonProperty("ua_string")
    public String uaString;

    @JsonProperty("cookie")
    public String cookie;

    public static LoginDbEventData getLoginDbEventData(
            String loginDatetime, Integer userId, String brand, String ipAddress, String uaString, String cookie) {
        LoginDbEventData event = new LoginDbEventData();
        event.loginDatetime = loginDatetime;
        event.userId = userId;
        event.brand = brand;
        event.ipAddress = ipAddress;
        event.uaString = uaString;
        event.cookie = cookie;
        return event;
    }
}
