package business_objects.db.payment_gate.d_status;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Builder
@Getter
@Setter
public class DStatusObject {

    private Integer id;
    private String status;
    private String description;
    private String dateCreated;
    private String dateUpdated;

    public DStatusObject() {
    }

    public DStatusObject(Integer id, String status, String description, String dateCreated, String dateUpdated) {
        this.id = id;
        this.status = status;
        this.description = description;
        this.dateCreated = dateCreated;
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