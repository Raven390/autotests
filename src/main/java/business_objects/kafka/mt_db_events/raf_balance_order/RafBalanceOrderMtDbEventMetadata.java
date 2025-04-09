package business_objects.kafka.mt_db_events.raf_balance_order;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * Confluence link - https://vantagefx-hytechs.atlassian.net/wiki/spaces/AntiFraud/pages/1112440834/RAF+balance+order
 */
public class RafBalanceOrderMtDbEventMetadata {
    @JsonProperty("timestamp")
    String timestamp;

    @JsonProperty("record-type")
    String recordType;

    @JsonProperty("operation")
    String operation;

    @JsonProperty("partition-key-type")
    String partitionKeyType;

    @JsonProperty("schema-name")
    String schemaName;

    @JsonProperty("table-name")
    String tableName;

    public RafBalanceOrderMtDbEventMetadata(
            String timestamp, String recordType, String operation, String partitionKeyType, String schemaName,
            String tableName) {
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
        RafBalanceOrderMtDbEventMetadata that = (RafBalanceOrderMtDbEventMetadata) o;
        return Objects.equals(timestamp, that.timestamp) && Objects.equals(recordType, that.recordType) && Objects.equals(operation, that.operation) && Objects.equals(partitionKeyType, that.partitionKeyType) && Objects.equals(schemaName, that.schemaName) && Objects.equals(tableName, that.tableName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, recordType, operation, partitionKeyType, schemaName, tableName);
    }

    @Override
    public String toString() {
        return "OpenTradeMtDbEventMetadata{" + "timestamp='" + timestamp + '\'' + ", recordType='" + recordType + '\'' + ", operation='" + operation + '\'' + ", partitionKeyType='" + partitionKeyType + '\'' + ", schemaName='" + schemaName + '\'' + ", tableName='" + tableName + '\'' + '}';
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getPartitionKeyType() {
        return partitionKeyType;
    }

    public void setPartitionKeyType(String partitionKeyType) {
        this.partitionKeyType = partitionKeyType;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
