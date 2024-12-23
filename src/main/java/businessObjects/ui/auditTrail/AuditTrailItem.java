package businessObjects.ui.auditTrail;

import java.util.Objects;

public class AuditTrailItem {

    private String header;
    private String comment;
    private String details;
    private String time;

    public AuditTrailItem() {
    }

    public AuditTrailItem(String header, String comment, String details, String time) {
        this.header = header;
        this.comment = comment;
        this.details = details;
        this.time = time;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuditTrailItem that = (AuditTrailItem) o;
        return Objects.equals(header, that.header) && Objects.equals(comment, that.comment) && Objects.equals(details, that.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(header, comment, details);
    }

    @Override
    public String toString() {
        return "AuditTrailItem{" + "header='" + header + '\'' + ", comment='" + comment + '\'' + ", details='" + details + '\'' + ", time='" + time + '\'' + '}';
    }
}
