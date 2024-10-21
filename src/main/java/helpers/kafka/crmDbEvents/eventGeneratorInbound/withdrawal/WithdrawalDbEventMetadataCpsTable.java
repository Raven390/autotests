package helpers.kafka.crmDbEvents.eventGeneratorInbound.withdrawal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WithdrawalDbEventMetadataCpsTable {
    @JsonProperty("timestamp")
    public String timestamp;

    @JsonProperty("record-type")
    public String recordType;

    @JsonProperty("operation")
    public String operation;

    @JsonProperty("partition-key-type")
    public String partitionKeyType;

    @JsonProperty("schema-name")
    public String schemaName;

    @JsonProperty("table-name")
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

    public static WithdrawalDbEventMetadataCpsTable getWithdrawalDbEventMetadataCpsTable(String timestamp, String recordType, String operation, String partitionKeyType, String schemaName, String tableName, String transactionId, String transactionRecordId, String prevTransactionId, String prevTransactionRecordId, String commitTimestamp, String streamPosition) {
        WithdrawalDbEventMetadataCpsTable event = new WithdrawalDbEventMetadataCpsTable();
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
