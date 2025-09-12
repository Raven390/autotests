package business_objects.db.payment_gate.d_payment_types;

import java.util.Objects;


public class DPaymentTypesObject {

    public Integer id;
    public String type;
    public String dateCreated;
    public String dateUpdated;

    public DPaymentTypesObject() {
    }

    public DPaymentTypesObject(Integer id, String type, String dateCreated, String dateUpdated) {
        this.id = id;
        this.type = type;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
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
        if (!(o instanceof DPaymentTypesObject that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(type, that.type) && Objects.equals(
                dateCreated, that.dateCreated) && Objects.equals(dateUpdated, that.dateUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, dateCreated, dateUpdated);
    }

    @Override
    public String toString() {
        return "DPaymentTypesObject{" + "id=" + id + ", type='" + type + '\'' + ", dateCreated='" + dateCreated + '\'' + ", dateUpdated='" + dateUpdated + '\'' + '}';
    }
}