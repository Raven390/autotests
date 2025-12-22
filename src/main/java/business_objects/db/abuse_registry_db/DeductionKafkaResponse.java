package business_objects.db.abuse_registry_db;

import java.sql.Timestamp;
import java.util.Objects;

public class DeductionKafkaResponse {
    private String messageId;
    private Integer payload;
    private String status;
    private String failReason;
    private Timestamp processedAt;

    public DeductionKafkaResponse() {}

    public DeductionKafkaResponse(
            String messageId, Integer payload, String status, String failReason, Timestamp processedAt) {
        this.messageId = messageId;
        this.payload = payload;
        this.status = status;
        this.failReason = failReason;
        this.processedAt = processedAt;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Integer getPayload() {
        return payload;
    }

    public void setPayload(Integer payload) {
        this.payload = payload;
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

    public Timestamp getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(Timestamp processedAt) {
        this.processedAt = processedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DeductionKafkaResponse that = (DeductionKafkaResponse) o;
        return Objects.equals(messageId, that.messageId)
                && Objects.equals(payload, that.payload)
                && Objects.equals(status, that.status)
                && Objects.equals(failReason, that.failReason)
                && Objects.equals(processedAt, that.processedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageId, payload, status, failReason, processedAt);
    }

    @Override
    public String toString() {
        return "DeductionKafkaResponse{" + "messageId='" + messageId + '\'' + ", payload=" + payload + ", status='"
                + status + '\'' + ", failReason='" + failReason + '\'' + ", processedAt='" + processedAt + '\'' + '}';
    }
}
