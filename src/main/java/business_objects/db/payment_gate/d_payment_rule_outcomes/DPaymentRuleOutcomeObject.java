package business_objects.db.payment_gate.d_payment_rule_outcomes;

import java.util.Objects;


public class DPaymentRuleOutcomeObject {

    public Integer id;
    public Integer ruleId;
    public Integer endId;
    public String endName;
    public String endDescription;
    public String dateCreated;
    public String dateUpdated;

    public DPaymentRuleOutcomeObject() {
    }

    public DPaymentRuleOutcomeObject(
            Integer id, Integer ruleId, Integer endId, String endName, String endDescription, String dateCreated,
            String dateUpdated) {
        this.id = id;
        this.ruleId = ruleId;
        this.endId = endId;
        this.endName = endName;
        this.endDescription = endDescription;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public Integer getEndId() {
        return endId;
    }

    public void setEndId(Integer endId) {
        this.endId = endId;
    }

    public String getEndName() {
        return endName;
    }

    public void setEndName(String endName) {
        this.endName = endName;
    }

    public String getEndDescription() {
        return endDescription;
    }

    public void setEndDescription(String endDescription) {
        this.endDescription = endDescription;
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
        if (!(o instanceof DPaymentRuleOutcomeObject that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(ruleId, that.ruleId) && Objects.equals(
                endId, that.endId) && Objects.equals(endName, that.endName) && Objects.equals(
                        endDescription, that.endDescription) && Objects.equals(dateCreated, that.dateCreated) && Objects.equals(
                                dateUpdated, that.dateUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ruleId, endId, endName, endDescription, dateCreated, dateUpdated);
    }

    @Override
    public String toString() {
        return "DPaymentRuleOutcomeObject{" + "id=" + id + ", ruleId=" + ruleId + ", endId=" + endId + ", endName='" + endName + '\'' + ", endDescription='" + endDescription + '\'' + ", dateCreated='" + dateCreated + '\'' + ", dateUpdated='" + dateUpdated + '\'' + '}';
    }
}