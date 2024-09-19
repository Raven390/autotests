package helpers.kafka.messages.triggers;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/**
 * Confluence link https://vantagefx-hytechs.atlassian.net/wiki/spaces/AntiFraud/pages/1087995943/Client+Registration
 */
public class RegistrationEvent {
    @JsonProperty("create_time")
    public Date createTime;

    @JsonProperty("user_id")
    public int userId;

    @JsonProperty("brand")
    public String brand;

    @JsonProperty("regulator")
    public String regulator;

    @JsonProperty("mt_account")
    public int mtAccount;

    public static RegistrationEvent registrationEvent(
            Date createTime, int userId, String brand, String regulator, int mtAccount) {
        RegistrationEvent registrationEvent = new RegistrationEvent();
        registrationEvent.createTime = createTime;
        registrationEvent.userId = userId;
        registrationEvent.mtAccount = mtAccount;
        registrationEvent.brand = brand;
        registrationEvent.regulator = regulator;
        return registrationEvent;
    }
}
