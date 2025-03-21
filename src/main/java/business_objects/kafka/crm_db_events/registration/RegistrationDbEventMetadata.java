package business_objects.kafka.crm_db_events.registration;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class RegistrationDbEventMetadata {
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

    public RegistrationDbEventMetadata() {
    }

    public RegistrationDbEventMetadata(String timestamp, String recordType, String operation, String partitionKeyType,
            String schemaName, String tableName) {
        this.timestamp = timestamp;
        this.recordType = recordType;
        this.operation = operation;
        this.partitionKeyType = partitionKeyType;
        this.schemaName = schemaName;
        this.tableName = tableName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistrationDbEventMetadata that = (RegistrationDbEventMetadata) o;
        return Objects.equals(timestamp, that.timestamp) && Objects.equals(recordType, that.recordType) && Objects.equals(operation, that.operation) && Objects.equals(partitionKeyType, that.partitionKeyType) && Objects.equals(schemaName, that.schemaName) && Objects.equals(tableName, that.tableName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, recordType, operation, partitionKeyType, schemaName, tableName);
    }

    @Override
    public String toString() {
        return "RegistrationDbEventMetadata{" + "timestamp='" + timestamp + '\'' + ", recordType='" + recordType + '\'' + ", operation='" + operation + '\'' + ", partitionKeyType='" + partitionKeyType + '\'' + ", schemaName='" + schemaName + '\'' + ", tableName='" + tableName + '\'' + '}';
    }
}
