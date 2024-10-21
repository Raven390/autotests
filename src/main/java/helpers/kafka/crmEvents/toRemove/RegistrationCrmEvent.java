package helpers.kafka.crmEvents.toRemove;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/**
 * Confluence link https://vantagefx-hytechs.atlassian.net/wiki/spaces/AntiFraud/pages/1087995943/Client+Registration
 */
public class RegistrationCrmEvent {

    @JsonProperty("uuid")
    public String uuid;

    @JsonProperty("create_time")
    public Date createTime;

    @JsonProperty("user_id")
    public Integer userId;

    @JsonProperty("brand")
    public String brand;

    @JsonProperty("regulator")
    public String regulator;

    @JsonProperty("mt_account")
    public Integer mtAccount;

    public static RegistrationCrmEvent registrationEvent(String uuid, Date createTime, Integer userId, String brand, String regulator, Integer mtAccount) {
        RegistrationCrmEvent event = new RegistrationCrmEvent();
        event.uuid = uuid;
        event.createTime = createTime;
        event.userId = userId;
        event.mtAccount = mtAccount;
        event.brand = brand;
        event.regulator = regulator;
        return event;
    }
}
