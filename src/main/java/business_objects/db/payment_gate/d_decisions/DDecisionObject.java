package business_objects.db.payment_gate.d_decisions;

import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class DDecisionObject {

    private Integer id;
    private String type;
    private Integer code;
    private String name;
    private String description;
    private String dateCreated;
    private String dateUpdated;

    public DDecisionObject(
            Integer id,
            String type,
            Integer code,
            String name,
            String description,
            String dateCreated,
            String dateUpdated) {
        this.id = id;
        this.type = type;
        this.code = code;
        this.name = name;
        this.description = description;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
    }

    public DDecisionObject() {}

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DDecisionObject that)) return false;
        return Objects.equals(id, that.id)
                && Objects.equals(type, that.type)
                && Objects.equals(code, that.code)
                && Objects.equals(name, that.name)
                && Objects.equals(description, that.description)
                && Objects.equals(dateCreated, that.dateCreated)
                && Objects.equals(dateUpdated, that.dateUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, code, name, description, dateCreated, dateUpdated);
    }

    @Override
    public String toString() {
        return "DDecisionObject{" + "id=" + id + ", type='" + type + '\'' + ", code=" + code + ", name='" + name + '\''
                + ", description='" + description + '\'' + ", dateCreated='" + dateCreated + '\'' + ", dateUpdated='"
                + dateUpdated + '\'' + '}';
    }
}
