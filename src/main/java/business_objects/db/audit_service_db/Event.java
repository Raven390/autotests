package business_objects.db.audit_service_db;


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

    public Event() {
    }

    public Event(String ucid, String type, String comment) {
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
        Event event = (Event) o;
        return Objects.equals(ucid, event.ucid) && Objects.equals(type, event.type) && Objects.equals(initiatedBySystem, event.initiatedBySystem) && Objects.equals(initiatedByUser, event.initiatedByUser) && Objects.equals(comment, event.comment) && Objects.equals(details, event.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ucid, type, initiatedBySystem, initiatedByUser, comment, details);
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setKafkaMessageId(String kafkaMessageId) {
        this.kafkaMessageId = kafkaMessageId;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setInitiatedBySystem(String initiatedBySystem) {
        this.initiatedBySystem = initiatedBySystem;
    }

    public void setInitiatedByUser(String initiatedByUser) {
        this.initiatedByUser = initiatedByUser;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
