package helpers.kafka.crmDbEvents.eventGeneratorInbound.withdrawal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WithdrawalDbEventCpsTable {
    @JsonProperty("data")
    public WithdrawalDbEventDataCpsTable data;

    @JsonProperty("metadata")
    public WithdrawalDbEventMetadataCpsTable metadata;

    public static WithdrawalDbEventCpsTable getWithdrawalDbEventCpsTable(
            WithdrawalDbEventDataCpsTable withdrawalEventData,
            WithdrawalDbEventMetadataCpsTable withdrawalEventMetadata) {
        WithdrawalDbEventCpsTable event = new WithdrawalDbEventCpsTable();
        event.data = withdrawalEventData;
        event.metadata = withdrawalEventMetadata;
        return event;
    }
}
