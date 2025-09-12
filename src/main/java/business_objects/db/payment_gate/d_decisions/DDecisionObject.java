package business_objects.db.payment_gate.d_decisions;

import java.util.Objects;


public class DDecisionObject {

    public Integer id;
    public String type;
    public Integer code;
    public String name;
    public String description;
    public String dateCreated;
    public String dateUpdated;

    public DDecisionObject(
            Integer id, String type, Integer code, String name, String description, String dateCreated,
            String dateUpdated) {
        this.id = id;
        this.type = type;
        this.code = code;
        this.name = name;
        this.description = description;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
    }

    public DDecisionObject() {
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

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        if (!(o instanceof DDecisionObject that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(type, that.type) && Objects.equals(
                code, that.code) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(
                        dateCreated, that.dateCreated) && Objects.equals(dateUpdated, that.dateUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, code, name, description, dateCreated, dateUpdated);
    }

    @Override
    public String toString() {
        return "DDecisionObject{" + "id=" + id + ", type='" + type + '\'' + ", code=" + code + ", name='" + name + '\'' + ", description='" + description + '\'' + ", dateCreated='" + dateCreated + '\'' + ", dateUpdated='" + dateUpdated + '\'' + '}';
    }
}