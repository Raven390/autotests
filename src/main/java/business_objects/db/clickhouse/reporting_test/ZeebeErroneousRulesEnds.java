package business_objects.db.clickhouse.reporting_test;

import java.util.Objects;

public class ZeebeErroneousRulesEnds {
    private String runId;

    public ZeebeErroneousRulesEnds() {}

    public String getRunId() { return runId; }
    public void setRunId(String runId) { this.runId = runId; }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ZeebeErroneousRulesEnds that)) return false;
        return Objects.equals(runId, that.runId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(runId);
    }
}