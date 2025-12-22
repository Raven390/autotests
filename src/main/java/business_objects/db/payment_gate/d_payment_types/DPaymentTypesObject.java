package business_objects.db.payment_gate.d_payment_types;

import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class DPaymentTypesObject {

    private Integer id;
    private String type;
    private String dateCreated;
    private String dateUpdated;

    public DPaymentTypesObject() {}

    public DPaymentTypesObject(Integer id, String type, String dateCreated, String dateUpdated) {
        this.id = id;
        this.type = type;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DPaymentTypesObject that)) return false;
        return Objects.equals(id, that.id)
                && Objects.equals(type, that.type)
                && Objects.equals(dateCreated, that.dateCreated)
                && Objects.equals(dateUpdated, that.dateUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, dateCreated, dateUpdated);
    }

    @Override
    public String toString() {
        return "DPaymentTypesObject{" + "id=" + id + ", type='" + type + '\'' + ", dateCreated='" + dateCreated + '\''
                + ", dateUpdated='" + dateUpdated + '\'' + '}';
    }
}
