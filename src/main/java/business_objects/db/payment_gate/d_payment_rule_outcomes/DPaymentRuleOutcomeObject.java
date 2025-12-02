package business_objects.db.payment_gate.d_payment_rule_outcomes;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Builder
@Setter
@Getter
public class DPaymentRuleOutcomeObject {

    private Integer id;
    private Integer ruleId;
    private Integer endId;
    private String endName;
    private String endDescription;
    private String dateCreated;
    private String dateUpdated;

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