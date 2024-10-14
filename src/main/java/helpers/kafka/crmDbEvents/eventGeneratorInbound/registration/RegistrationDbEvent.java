package helpers.kafka.crmDbEvents.eventGeneratorInbound.registration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegistrationDbEvent {

    @JsonProperty("data")
    public RegistrationDbEventData data;

    @JsonProperty("metadata")
    public RegistrationDbEventMetadata metadata;

    public static RegistrationDbEvent getRegistrationDbEvent(
            RegistrationDbEventData RegistrationEventData, RegistrationDbEventMetadata RegistrationEventMetadata) {
        RegistrationDbEvent event = new RegistrationDbEvent();
        event.data = RegistrationEventData;
        event.metadata = RegistrationEventMetadata;
        return event;
    }
}
