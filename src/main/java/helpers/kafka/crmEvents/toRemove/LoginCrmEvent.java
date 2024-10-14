package helpers.kafka.crmEvents.toRemove;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/**
 * Confluence link - https://vantagefx-hytechs.atlassian.net/wiki/spaces/AntiFraud/pages/1095205107/Client+Log+in
 */
public class LoginCrmEvent {

    @JsonProperty("uuid")
    public String uuid;

    @JsonProperty("login_time")
    public Date loginTime;

    @JsonProperty("user_id")
    public Integer userId;

    @JsonProperty("brand")
    public String brand;

    @JsonProperty("ip_address")
    public Integer ipAddress;

    public static LoginCrmEvent loginEvent(
            String uuid, Date loginTime, Integer userId, String brand, Integer ipAddress) {
        LoginCrmEvent event = new LoginCrmEvent();
        event.uuid = uuid;
        event.loginTime = loginTime;
        event.userId = userId;
        event.brand = brand;
        event.ipAddress = ipAddress;
        return event;
    }
}
