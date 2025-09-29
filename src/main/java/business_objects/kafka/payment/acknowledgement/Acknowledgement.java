package business_objects.kafka.payment.acknowledgement;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Acknowledgement {

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("correlationId")
    private String correlationId;

    @JsonProperty("paymentId")
    private String paymentId;

    @JsonProperty("status")
    private String status;

    public Acknowledgement(String timestamp, String correlationId, String paymentId, String status) {
        this.timestamp = timestamp;
        this.correlationId = correlationId;
        this.paymentId = paymentId;
        this.status = status;
    }

    public Acknowledgement() {
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Acknowledgement that)) return false;
        return Objects.equals(timestamp, that.timestamp) && Objects.equals(correlationId, that.correlationId) && Objects.equals(
                paymentId, that.paymentId) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, correlationId, paymentId, status);
    }

    @Override
    public String toString() {
        return "Acknowledgement{" + "timestamp='" + timestamp + '\'' + ", correlationId='" + correlationId + '\'' + ", paymentId='" + paymentId + '\'' + ", status='" + status + '\'' + '}';
    }
}
