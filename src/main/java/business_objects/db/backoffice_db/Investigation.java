package business_objects.db.backoffice_db;

import java.util.Objects;

public class Investigation {

    private Integer id;
    private String type;
    private String assignedUserId;
    private String completedByUserId;
    private String createdAt;
    private String startedAt;
    private String completedAt;
    private String status;
    private String clientUcid;

    public Investigation() {
    }

    public Investigation(Integer id, String type, String assignedUserId, String completedByUserId, String createdAt,
            String startedAt, String completedAt, String status, String clientUcid) {
        this.id = id;
        this.type = type;
        this.assignedUserId = assignedUserId;
        this.completedByUserId = completedByUserId;
        this.createdAt = createdAt;
        this.startedAt = startedAt;
        this.completedAt = completedAt;
        this.status = status;
        this.clientUcid = clientUcid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAssignedUserId() {
        return assignedUserId;
    }

    public void setAssignedUserId(String assignedUserId) {
        this.assignedUserId = assignedUserId;
    }

    public String getCompletedByUserId() {
        return completedByUserId;
    }

    public void setCompletedByUserId(String completedByUserId) {
        this.completedByUserId = completedByUserId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(String startedAt) {
        this.startedAt = startedAt;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClientUcid() {
        return clientUcid;
    }

    public void setClientUcid(String clientUcid) {
        this.clientUcid = clientUcid;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Investigation that = (Investigation) o;
        return Objects.equals(id, that.id) && Objects.equals(type, that.type) && Objects.equals(assignedUserId, that.assignedUserId) && Objects.equals(completedByUserId, that.completedByUserId) && Objects.equals(createdAt, that.createdAt) && Objects.equals(startedAt, that.startedAt) && Objects.equals(completedAt, that.completedAt) && Objects.equals(status, that.status) && Objects.equals(clientUcid, that.clientUcid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, assignedUserId, completedByUserId, createdAt, startedAt, completedAt, status, clientUcid);
    }

    @Override
    public String toString() {
        return "Investigation{" + "id=" + id + ", type='" + type + '\'' + ", assignedUserId='" + assignedUserId + '\'' + ", completedByUserId='" + completedByUserId + '\'' + ", createdAt='" + createdAt + '\'' + ", startedAt='" + startedAt + '\'' + ", completedAt='" + completedAt + '\'' + ", status='" + status + '\'' + ", clientUcid='" + clientUcid + '\'' + '}';
    }
}
