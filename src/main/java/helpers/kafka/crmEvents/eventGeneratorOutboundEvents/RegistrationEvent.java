package helpers.kafka.crmEvents.eventGeneratorOutboundEvents;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegistrationEvent {
    @JsonProperty("data")
    public RegistrationEventData data;
}
