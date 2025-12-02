package business_objects.api.payment_gate.rule_executions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetRuleExecutionsResponseBody {

    @JsonProperty("paymentId")
    private UUID paymentId;

    @JsonProperty("items")
    private List<Item> items;

    // Optional legacy error fields
    @JsonProperty("error")
    private String error;

    @JsonProperty("message")
    private String message;

    // Problem Details (RFC 7807) error fields for 4xx/5xx responses
    @JsonProperty("type")
    private String type;

    @JsonProperty("title")
    private String title;

    @JsonProperty("status")
    private Integer status;

    @JsonProperty("detail")
    private String detail;

    @JsonProperty("instance")
    private String instance;

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Item {
        @JsonProperty("id")
        private Integer id;
        @JsonProperty("runId")
        private String runId;
        @JsonProperty("paymentId")
        private String paymentId;
        @JsonProperty("ruleId")
        private Integer ruleId;
        @JsonProperty("ruleType")
        private String ruleType;
        @JsonProperty("ruleVersion")
        private String ruleVersion;
        @JsonProperty("ruleEndId")
        private Integer ruleEndId;
        @JsonProperty("startedAt")
        private String startedAt;
        @JsonProperty("completedAt")
        private String completedAt;
        @JsonProperty("createdAt")
        private String createdAt;
        @JsonProperty("updatedAt")
        private String updatedAt;
    }
}
