package business_objects.api.payment_gate.rule_executions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Response model for GET /rule-executions endpoint
 *
 * Example:
 * {
 * "paymentId": "56b6b3e8-5be3-4909-b221-31d5316806c9",
 * "items": [
 * {
 * "id": 223344,
 * "runId": "2398529387692876",
 * "paymentId": "56b6b3e8-5be3-4909-b221-31d5316806c9",
 * "ruleId": 101,
 * "ruleVersion": "1.0.23",
 * "ruleEndId": 2,
 * "startedAt": "2025-08-27T09:31:00Z",
 * "completedAt": "2025-08-27T09:31:02Z",
 * "createdAt": "2025-08-27T09:31:02Z",
 * "updatedAt": "2025-08-27T09:31:02Z"
 * }
 * ]
 * }
 */
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

    //Response body: {"type":"about:blank","title":"Bad Request","status":400,"detail":"Failed to convert 'ruleId' with value: 'ааа'","instance":"/v1/payments/9ee25fa4-01ee-4380-8c5a-3eda96981806/ruleExecutions"}

    public GetRuleExecutionsResponseBody() {
    }

    public GetRuleExecutionsResponseBody(UUID paymentId, List<Item> items) {
        this.paymentId = paymentId;
        this.items = items;
    }

    public UUID getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(UUID paymentId) {
        this.paymentId = paymentId;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetRuleExecutionsResponseBody that = (GetRuleExecutionsResponseBody) o;
        return Objects.equals(paymentId, that.paymentId) && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentId, items);
    }

    @Override
    public String toString() {
        return "GetExecutionsResponseBody{" + "paymentId='" + paymentId + '\'' + ", items=" + items + '}';
    }

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

        public Item() {
        }

        public Item(Integer id, String runId, String paymentId, Integer ruleId, String ruleVersion, Integer ruleEndId,
                String startedAt, String completedAt, String createdAt, String updatedAt) {
            this.id = id;
            this.runId = runId;
            this.paymentId = paymentId;
            this.ruleId = ruleId;
            this.ruleVersion = ruleVersion;
            this.ruleEndId = ruleEndId;
            this.startedAt = startedAt;
            this.completedAt = completedAt;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getRunId() {
            return runId;
        }

        public void setRunId(String runId) {
            this.runId = runId;
        }

        public String getPaymentId() {
            return paymentId;
        }

        public void setPaymentId(String paymentId) {
            this.paymentId = paymentId;
        }

        public Integer getRuleId() {
            return ruleId;
        }

        public void setRuleId(Integer ruleId) {
            this.ruleId = ruleId;
        }

        public String getRuleVersion() {
            return ruleVersion;
        }

        public void setRuleVersion(String ruleVersion) {
            this.ruleVersion = ruleVersion;
        }

        public Integer getRuleEndId() {
            return ruleEndId;
        }

        public void setRuleEndId(Integer ruleEndId) {
            this.ruleEndId = ruleEndId;
        }

        public String getStartedAt() {
            return startedAt;
        }

        public void setStartedAt(String startedAt) {
            this.startedAt = startedAt;
        }

        public String getCompletedAt() {
            return completedAt;
        }

        public void setCompletedAt(String completedAt) {
            this.completedAt = completedAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Item item = (Item) o;
            return Objects.equals(id, item.id) && Objects.equals(runId, item.runId) && Objects.equals(paymentId, item.paymentId) && Objects.equals(ruleId, item.ruleId) && Objects.equals(ruleVersion, item.ruleVersion) && Objects.equals(ruleEndId, item.ruleEndId) && Objects.equals(startedAt, item.startedAt) && Objects.equals(completedAt, item.completedAt) && Objects.equals(createdAt, item.createdAt) && Objects.equals(updatedAt, item.updatedAt);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, runId, paymentId, ruleId, ruleVersion, ruleEndId, startedAt, completedAt, createdAt, updatedAt);
        }

        @Override
        public String toString() {
            return "Item{" + "id=" + id + ", runId='" + runId + '\'' + ", paymentId='" + paymentId + '\'' + ", ruleId=" + ruleId + ", ruleVersion='" + ruleVersion + '\'' + ", ruleEndId=" + ruleEndId + ", startedAt='" + startedAt + '\'' + ", completedAt='" + completedAt + '\'' + ", createdAt='" + createdAt + '\'' + ", updatedAt='" + updatedAt + '\'' + '}';
        }
    }
}
