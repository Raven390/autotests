package business_objects.kafka.deductions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class AccountDeductionRequestResponse {

    @JsonProperty("processedAt")
    private String processedAt;

    @JsonProperty("messageId")
    private String messageId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("failReason")
    private String failReason;

    public AccountDeductionRequestResponse() {
    }

    public AccountDeductionRequestResponse(String processedAt, String messageId, String status, String failReason) {
        this.processedAt = processedAt;
        this.messageId = messageId;
        this.status = status;
        this.failReason = failReason;
    }

    public String getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(String processedAt) {
        this.processedAt = processedAt;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AccountDeductionRequestResponse that = (AccountDeductionRequestResponse) o;
        return Objects.equals(processedAt, that.processedAt) && Objects.equals(messageId, that.messageId) && Objects.equals(status, that.status) && Objects.equals(failReason, that.failReason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(processedAt, messageId, status, failReason);
    }

    @Override
    public String toString() {
        return "AccountDeductionRequestResponse{" + "processedAt='" + processedAt + '\'' + ", messageId='" + messageId + '\'' + ", status='" + status + '\'' + ", failReason='" + failReason + '\'' + '}';
    }
}
