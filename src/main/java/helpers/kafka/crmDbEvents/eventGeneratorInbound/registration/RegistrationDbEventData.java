package helpers.kafka.crmDbEvents.eventGeneratorInbound.registration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegistrationDbEventData {

    @JsonProperty("create_time")
    public String createTime;

    @JsonProperty("user_id")
    public Integer userId;

    @JsonProperty("brand")
    public String brand;

    @JsonProperty("regulator")
    public String regulator;

    @JsonProperty("mt4_account")
    public Integer mtAccount;

    public static RegistrationDbEventData getRegistrationDbEventData(String createTime, Integer userId, String brand, String regulator, Integer mtAccount) {
        RegistrationDbEventData event = new RegistrationDbEventData();
        event.createTime = createTime;
        event.userId = userId;
        event.brand = brand;
        event.regulator = regulator;
        event.mtAccount = mtAccount;
        return event;
    }
}
