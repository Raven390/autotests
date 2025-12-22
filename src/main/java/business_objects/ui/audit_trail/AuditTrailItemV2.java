package business_objects.ui.audit_trail;

import java.util.Objects;

public class AuditTrailItemV2 {

    private String header;
    private String details;

    public AuditTrailItemV2() {}

    public AuditTrailItemV2(String header, String details) {
        this.header = header;
        this.details = details;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuditTrailItemV2 that = (AuditTrailItemV2) o;
        return Objects.equals(header, that.header) && Objects.equals(details, that.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(header, details);
    }

    @Override
    public String toString() {
        return "AuditTrailItem{" + "header='" + header + '\'' + ", details='" + details + '\'' + '}';
    }
}
