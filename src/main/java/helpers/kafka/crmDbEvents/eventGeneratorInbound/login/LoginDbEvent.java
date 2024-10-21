package helpers.kafka.crmDbEvents.eventGeneratorInbound.login;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginDbEvent {

    @JsonProperty("data")
    public LoginDbEventData data;

    @JsonProperty("metadata")
    public LoginDbEventMetadata metadata;

    public static LoginDbEvent getLoginDbEvent(LoginDbEventData LoginEventData, LoginDbEventMetadata LoginEventMetadata) {
        LoginDbEvent event = new LoginDbEvent();
        event.data = LoginEventData;
        event.metadata = LoginEventMetadata;
        return event;
    }
}
