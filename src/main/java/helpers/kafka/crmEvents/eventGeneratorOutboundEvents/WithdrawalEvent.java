package helpers.kafka.crmEvents.eventGeneratorOutboundEvents;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WithdrawalEvent {

    @JsonProperty("data")
    public WithdrawalEventData data;
}
