package business_objects.db.rule_engine_db;

import org.postgresql.jdbc.PgArray;

import java.util.Objects;

public class RuleDbObjectPgArray {
    private String id;
    private String eventType;
    private String ruleName;
    private PgArray brands;

    public RuleDbObjectPgArray() {
    }

    public RuleDbObjectPgArray(String id, String eventType, String ruleName, PgArray brands) {
        this.id = id;
        this.eventType = eventType;
        this.ruleName = ruleName;
        this.brands = brands;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RuleDbObjectPgArray that = (RuleDbObjectPgArray) o;
        return Objects.equals(id, that.id) && Objects.equals(eventType, that.eventType) && Objects.equals(
                ruleName, that.ruleName) && Objects.equals(brands, that.brands);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, eventType, ruleName, brands);
    }

    @Override
    public String toString() {
        return "RuleDbObject{" + "id='" + id + '\'' + ", eventType='" + eventType + '\'' + ", ruleName='" + ruleName + '\'' + ", brands='" + brands + '\'' + '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public PgArray getBrands() {
        return brands;
    }

    public void setBrands(PgArray brands) {
        this.brands = brands;
    }
}
