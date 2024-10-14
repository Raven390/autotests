package helpers.kafka.crmDbEvents.eventGeneratorInbound.login;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginDbEventMetadata {
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

    public static LoginDbEventMetadata getLoginDbEventMetadata(
            String timestamp,
            String recordType,
            String operation,
            String partitionKeyType,
            String schemaName,
            String tableName) {
        LoginDbEventMetadata event = new LoginDbEventMetadata();
        event.timestamp = timestamp;
        event.recordType = recordType;
        event.operation = operation;
        event.partitionKeyType = partitionKeyType;
        event.schemaName = schemaName;
        event.tableName = tableName;
        return event;
    }
}
