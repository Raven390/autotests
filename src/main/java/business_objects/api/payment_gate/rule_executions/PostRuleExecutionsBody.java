package business_objects.api.payment_gate.rule_executions;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRuleExecutionsBody {

    private UUID paymentId;
    private String runId;
    private Integer ruleId;
    private String ruleVersion;
    private String ruleEndId;
    private String dateCreated;
    private String dateUpdated;
    private String dateStarted;
    private String dateCompleted;

    // Explicit setters to avoid any potential Lombok/unicode issues
    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setDateUpdated(String dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public void setDateStarted(String dateStarted) {
        this.dateStarted = dateStarted;
    }

    public void setDateCompleted(String dateCompleted) {
        this.dateCompleted = dateCompleted;
    }
}
