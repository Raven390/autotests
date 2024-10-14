package helpers.kafka.crmDbEvents.eventGeneratorInbound.withdrawal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WithdrawalDbEventMetadata {
    @JsonProperty(value = "timestamp", required = true)
    public String timestamp;

    @JsonProperty(value = "record-type", required = true)
    public String recordType;

    @JsonProperty(value = "operation", required = true)
    public String operation;

    @JsonProperty(value = "partition-key-type", required = true)
    public String partitionKeyType;

    @JsonProperty(value = "schema-name", required = true)
    public String schemaName;

    @JsonProperty(value = "table-name", required = true)
    public String tableName;

    @JsonProperty("transaction-id")
    public String transactionId;

    @JsonProperty("transaction-record-id")
    public String transactionRecordId;

    @JsonProperty("prev-transaction-id")
    public String prevTransactionId;

    @JsonProperty("prev-transaction-record-id")
    public String prevTransactionRecordId;

    @JsonProperty("commit-timestamp")
    public String commitTimestamp;

    @JsonProperty("stream-position")
    public String streamPosition;

    public static WithdrawalDbEventMetadata getWithdrawalDbEventMetadata(
            String timestamp,
            String recordType,
            String operation,
            String partitionKeyType,
            String schemaName,
            String tableName,
            String transactionId,
            String transactionRecordId,
            String prevTransactionId,
            String prevTransactionRecordId,
            String commitTimestamp,
            String streamPosition) {
        WithdrawalDbEventMetadata event = new WithdrawalDbEventMetadata();
        event.timestamp = timestamp;
        event.recordType = recordType;
        event.operation = operation;
        event.partitionKeyType = partitionKeyType;
        event.schemaName = schemaName;
        event.tableName = tableName;
        event.transactionId = transactionId;
        event.transactionRecordId = transactionRecordId;
        event.prevTransactionId = prevTransactionId;
        event.prevTransactionRecordId = prevTransactionRecordId;
        event.commitTimestamp = commitTimestamp;
        event.streamPosition = streamPosition;
        return event;
    }
}
