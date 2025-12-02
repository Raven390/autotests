package business_objects.db.payment_gate.d_payment_rules;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Builder
@Getter
@Setter
public class DPaymentRulesObject {

    private Integer id;
    private String name;
    private String type;
    private String description;
    private String dateCreated;
    private String dateUpdated;

    public DPaymentRulesObject() {
    }

    public DPaymentRulesObject(
            Integer id, String name, String type, String description, String dateCreated, String dateUpdated) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DPaymentRulesObject that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(
                type, that.type) && Objects.equals(description, that.description) && Objects.equals(
                        dateCreated, that.dateCreated) && Objects.equals(dateUpdated, that.dateUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, description, dateCreated, dateUpdated);
    }

    @Override
    public String toString() {
        return "DPaymentRulesObject{" + "id=" + id + ", name='" + name + '\'' + ", type='" + type + '\'' + ", description='" + description + '\'' + ", dateCreated='" + dateCreated + '\'' + ", dateUpdated='" + dateUpdated + '\'' + '}';
    }
}