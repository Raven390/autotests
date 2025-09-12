package business_objects.db.payment_gate.d_payment_rules;

import java.util.Objects;


public class DPaymentRulesObject {

    public Integer id;
    public String name;
    public String type;
    public String description;
    public String dateCreated;
    public String dateUpdated;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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