package business_objects.db.clickhouse.reporting_test;

import java.util.Objects;

public class AuditRaw {
    private String instanceId;
    private String fromId;
    private String toId;

    public AuditRaw() {}

    public String getInstanceId() { return instanceId; }
    public void setInstanceId(String instanceId) { this.instanceId = instanceId; }

    public String getFromId() { return fromId; }
    public void setFromId(String fromId) { this.fromId = fromId; }

    public String getToId() { return toId; }
    public void setToId(String toId) { this.toId = toId; }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AuditRaw that)) return false;
        return Objects.equals(instanceId, that.instanceId) && Objects.equals(fromId, that.fromId) && Objects.equals(toId, that.toId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instanceId, fromId, toId);
    }
}