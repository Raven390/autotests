package business_objects.db.abuse_registry_db;

import java.sql.Timestamp;
import java.util.Objects;

public class DeductionKafkaRequest {
    private String messageId;
    private Integer deductionId;
    private String payload;
    private Timestamp createdAt;

    public DeductionKafkaRequest() {
    }

    public DeductionKafkaRequest(String messageId, Integer deductionId, String payload, Timestamp createdAt) {
        this.messageId = messageId;
        this.deductionId = deductionId;
        this.payload = payload;
        this.createdAt = createdAt;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Integer getDeductionId() {
        return deductionId;
    }

    public void setDeductionId(Integer deductionId) {
        this.deductionId = deductionId;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DeductionKafkaRequest that = (DeductionKafkaRequest) o;
        return Objects.equals(messageId, that.messageId) && Objects.equals(deductionId, that.deductionId) && Objects.equals(payload, that.payload) && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageId, deductionId, payload, createdAt);
    }

    @Override
    public String toString() {
        return "DeductionKafkaRequest{" + "messageId='" + messageId + '\'' + ", deductionId=" + deductionId + ", payload='" + payload + '\'' + ", createdAt='" + createdAt + '\'' + '}';
    }
}
