package businessObjects.db.auditServiceDb;


import java.util.Objects;

public class Event {
    Long id;
    String kafkaMessageId;
    String ucid;
    String type;
    String createdAt;
    String initiatedBySystem;
    String initiatedByUser;
    String comment;
    String details;

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", kafkaMessageId='" + kafkaMessageId + '\'' +
                ", ucid='" + ucid + '\'' +
                ", type='" + type + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", initiatedBySystem='" + initiatedBySystem + '\'' +
                ", initiatedByUser='" + initiatedByUser + '\'' +
                ", comment='" + comment + '\'' +
                ", details='" + details + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) && Objects.equals(kafkaMessageId, event.kafkaMessageId) && Objects.equals(ucid, event.ucid) && Objects.equals(type, event.type) && Objects.equals(createdAt, event.createdAt) && Objects.equals(initiatedBySystem, event.initiatedBySystem) && Objects.equals(initiatedByUser, event.initiatedByUser) && Objects.equals(comment, event.comment) && Objects.equals(details, event.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, kafkaMessageId, ucid, type, createdAt, initiatedBySystem, initiatedByUser, comment, details);
    }

    public Long getId() {
        return id;
    }

    public String getKafkaMessageId() {
        return kafkaMessageId;
    }

    public String getUcid() {
        return ucid;
    }

    public String getType() {
        return type;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getInitiatedBySystem() {
        return initiatedBySystem;
    }

    public String getInitiatedByUser() {
        return initiatedByUser;
    }

    public String getComment() {
        return comment;
    }

    public String getDetails() {
        return details;
    }
}
