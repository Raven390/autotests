package businessObjects.kafka.crmDbEvents.withdrawal;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class WithdrawalDbEventMetadata {
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

    public WithdrawalDbEventMetadata() {
    }

    public WithdrawalDbEventMetadata(String timestamp, String recordType, String operation, String partitionKeyType,
            String schemaName, String tableName, String transactionId, String transactionRecordId,
            String prevTransactionId, String prevTransactionRecordId, String commitTimestamp, String streamPosition) {
        this.timestamp = timestamp;
        this.recordType = recordType;
        this.operation = operation;
        this.partitionKeyType = partitionKeyType;
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.transactionId = transactionId;
        this.transactionRecordId = transactionRecordId;
        this.prevTransactionId = prevTransactionId;
        this.prevTransactionRecordId = prevTransactionRecordId;
        this.commitTimestamp = commitTimestamp;
        this.streamPosition = streamPosition;
    }

    public WithdrawalDbEventMetadata(String timestamp, String recordType, String operation, String partitionKeyType,
            String schemaName, String transactionId, String transactionRecordId, String prevTransactionId,
            String commitTimestamp, String streamPosition, String prevTransactionRecordId) {
        this.timestamp = timestamp;
        this.recordType = recordType;
        this.operation = operation;
        this.partitionKeyType = partitionKeyType;
        this.schemaName = schemaName;
        this.transactionId = transactionId;
        this.transactionRecordId = transactionRecordId;
        this.prevTransactionId = prevTransactionId;
        this.commitTimestamp = commitTimestamp;
        this.streamPosition = streamPosition;
        this.prevTransactionRecordId = prevTransactionRecordId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WithdrawalDbEventMetadata that = (WithdrawalDbEventMetadata) o;
        return Objects.equals(timestamp, that.timestamp) && Objects.equals(recordType, that.recordType) && Objects.equals(operation, that.operation) && Objects.equals(partitionKeyType, that.partitionKeyType) && Objects.equals(schemaName, that.schemaName) && Objects.equals(tableName, that.tableName) && Objects.equals(transactionId, that.transactionId) && Objects.equals(transactionRecordId, that.transactionRecordId) && Objects.equals(prevTransactionId, that.prevTransactionId) && Objects.equals(prevTransactionRecordId, that.prevTransactionRecordId) && Objects.equals(commitTimestamp, that.commitTimestamp) && Objects.equals(streamPosition, that.streamPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, recordType, operation, partitionKeyType, schemaName, tableName, transactionId, transactionRecordId, prevTransactionId, prevTransactionRecordId, commitTimestamp, streamPosition);
    }

    @Override
    public String toString() {
        return "WithdrawalDbEventMetadata{" + "timestamp='" + timestamp + '\'' + ", recordType='" + recordType + '\'' + ", operation='" + operation + '\'' + ", partitionKeyType='" + partitionKeyType + '\'' + ", schemaName='" + schemaName + '\'' + ", tableName='" + tableName + '\'' + ", transactionId='" + transactionId + '\'' + ", transactionRecordId='" + transactionRecordId + '\'' + ", prevTransactionId='" + prevTransactionId + '\'' + ", prevTransactionRecordId='" + prevTransactionRecordId + '\'' + ", commitTimestamp='" + commitTimestamp + '\'' + ", streamPosition='" + streamPosition + '\'' + '}';
    }
}
