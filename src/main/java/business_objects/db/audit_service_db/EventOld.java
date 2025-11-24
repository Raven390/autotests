package business_objects.db.audit_service_db;


import lombok.Data;

import java.util.Objects;

@Data
public class EventOld {
    Long id;
    String kafkaMessageId;
    String ucid;
    String type;
    String createdAt;
    String initiatedBySystem;
    String initiatedByUser;
    String comment;
    String details;

    public EventOld() {
    }

    public EventOld(String ucid, String type, String comment) {
        this.ucid = ucid;
        this.type = type;
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Event{" + "id=" + id + ", kafkaMessageId='" + kafkaMessageId + '\'' + ", ucid='" + ucid + '\'' + ", type='" + type + '\'' + ", createdAt='" + createdAt + '\'' + ", initiatedBySystem='" + initiatedBySystem + '\'' + ", initiatedByUser='" + initiatedByUser + '\'' + ", comment='" + comment + '\'' + ", details='" + details + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EventOld event = (EventOld) o;
        return Objects.equals(ucid, event.ucid) && Objects.equals(type, event.type) && Objects.equals(initiatedBySystem, event.initiatedBySystem) && Objects.equals(initiatedByUser, event.initiatedByUser) && Objects.equals(comment, event.comment) && Objects.equals(details, event.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ucid, type, initiatedBySystem, initiatedByUser, comment, details);
    }

}
