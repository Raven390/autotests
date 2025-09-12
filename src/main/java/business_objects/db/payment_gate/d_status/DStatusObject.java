package business_objects.db.payment_gate.d_status;

import java.util.Objects;


public class DStatusObject {

    public Integer id;
    public String status;
    public String description;
    public String dateCreated;
    public String dateUpdated;

    public DStatusObject() {
    }

    public DStatusObject(Integer id, String status, String description, String dateCreated, String dateUpdated) {
        this.id = id;
        this.status = status;
        this.description = description;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(String dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DStatusObject that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(status, that.status) && Objects.equals(
                description, that.description) && Objects.equals(dateCreated, that.dateCreated) && Objects.equals(
                        dateUpdated, that.dateUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, description, dateCreated, dateUpdated);
    }

    @Override
    public String toString() {
        return "DStatusObject{" + "id=" + id + ", status='" + status + '\'' + ", description='" + description + '\'' + ", dateCreated='" + dateCreated + '\'' + ", dateUpdated='" + dateUpdated + '\'' + '}';
    }
}