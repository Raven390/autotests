package business_objects.db.payment_gate.d_rejection;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Builder
@Getter
@Setter
public class DRejectionObject {

    private Integer id;
    private Integer code;
    private String name;
    private String description;
    private String dateCreated;
    private String dateUpdated;

    public DRejectionObject() {
    }

    public DRejectionObject(
            Integer id, Integer code, String name, String description, String dateCreated, String dateUpdated) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DRejectionObject that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(code, that.code) && Objects.equals(
                name, that.name) && Objects.equals(description, that.description) && Objects.equals(
                        dateCreated, that.dateCreated) && Objects.equals(dateUpdated, that.dateUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name, description, dateCreated, dateUpdated);
    }

    @Override
    public String toString() {
        return "DRejectionObject{" + "id=" + id + ", code=" + code + ", name='" + name + '\'' + ", description='" + description + '\'' + ", dateCreated='" + dateCreated + '\'' + ", dateUpdated='" + dateUpdated + '\'' + '}';
    }
}