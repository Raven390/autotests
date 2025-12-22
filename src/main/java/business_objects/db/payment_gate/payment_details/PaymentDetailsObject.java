package business_objects.db.payment_gate.payment_details;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDetailsObject {

    private UUID paymentId;
    private String brand;
    private String regulator;
    private String type;
    private String clientId;
    private String merchantOrderId;
    private Timestamp eventDate;
    private String status;
    private String platform;
    private String payload;
    private String sourceSystem;
    private String sourceEnv;
    private Timestamp dateCreated;
    private Double amountUsd;

    public PaymentDetailsObject() {}

    public PaymentDetailsObject(
            UUID paymentId,
            String brand,
            String regulator,
            String type,
            String clientId,
            String merchantOrderId,
            Timestamp eventDate,
            String status,
            String platform,
            String payload,
            String sourceSystem,
            String sourceEnv,
            Timestamp dateCreated) {
        this.paymentId = paymentId;
        this.brand = brand;
        this.regulator = regulator;
        this.type = type;
        this.clientId = clientId;
        this.merchantOrderId = merchantOrderId;
        this.eventDate = eventDate;
        this.status = status;
        this.platform = platform;
        this.payload = payload;
        this.sourceSystem = sourceSystem;
        this.sourceEnv = sourceEnv;
        this.dateCreated = dateCreated;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PaymentDetailsObject that)) return false;
        return Objects.equals(paymentId, that.paymentId)
                && Objects.equals(brand, that.brand)
                && Objects.equals(regulator, that.regulator)
                && Objects.equals(type, that.type)
                && Objects.equals(clientId, that.clientId)
                && Objects.equals(merchantOrderId, that.merchantOrderId)
                && Objects.equals(eventDate, that.eventDate)
                && Objects.equals(status, that.status)
                && Objects.equals(platform, that.platform)
                && Objects.equals(payload, that.payload)
                && Objects.equals(sourceSystem, that.sourceSystem)
                && Objects.equals(sourceEnv, that.sourceEnv)
                && Objects.equals(dateCreated, that.dateCreated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                paymentId,
                brand,
                regulator,
                type,
                clientId,
                merchantOrderId,
                eventDate,
                status,
                platform,
                payload,
                sourceSystem,
                sourceEnv,
                dateCreated);
    }

    @Override
    public String toString() {
        return "PaymentDetailsObject{" + "paymentId='" + paymentId + '\'' + ", brand='" + brand + '\'' + ", regulator='"
                + regulator + '\'' + ", type='" + type + '\'' + ", clientId='" + clientId + '\'' + ", merchantOrderId='"
                + merchantOrderId + '\'' + ", eventDate='" + eventDate + '\'' + ", status='" + status + '\''
                + ", platform='" + platform + '\'' + ", payload='" + payload + '\'' + ", sourceSystem='" + sourceSystem
                + '\'' + ", sourceEnv='" + sourceEnv + '\'' + ", dateCreated='" + dateCreated + '\'' + '}';
    }
}
