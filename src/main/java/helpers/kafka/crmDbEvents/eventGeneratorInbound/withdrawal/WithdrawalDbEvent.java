package helpers.kafka.crmDbEvents.eventGeneratorInbound.withdrawal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WithdrawalDbEvent {
    @JsonProperty(value = "data", required = true)
    public WithdrawalDbEventData data;

    @JsonProperty(value = "metadata", required = true)
    public WithdrawalDbEventMetadata metadata;

    public static WithdrawalDbEvent getWithdrawalDbEvent(WithdrawalDbEventData withdrawalEventData, WithdrawalDbEventMetadata withdrawalEventMetadata) {
        WithdrawalDbEvent event = new WithdrawalDbEvent();
        event.data = withdrawalEventData;
        event.metadata = withdrawalEventMetadata;
        return event;
    }
}
