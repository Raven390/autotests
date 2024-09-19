package helpers.kafka.messages.triggers;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/**
 * Confluence link - https://vantagefx-hytechs.atlassian.net/wiki/spaces/AntiFraud/pages/1095205107/Client+Log+in
 */
public class LoginEvent {
    @JsonProperty("login_time")
    public Date loginTime;

    @JsonProperty("user_id")
    public Integer userId;

    @JsonProperty("brand")
    public String brand;

    @JsonProperty("ip_address")
    public Integer ipAddress;

    public static LoginEvent loginEvent(Date loginTime, Integer userId, String brand, Integer ipAddress) {
        LoginEvent loginEvent = new LoginEvent();
        loginEvent.loginTime = loginTime;
        loginEvent.userId = userId;
        loginEvent.brand = brand;
        loginEvent.ipAddress = ipAddress;
        return loginEvent;
    }
}
