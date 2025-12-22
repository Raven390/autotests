package business_objects.db.payment_gate.payment_decisions_sent;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class PaymentDecisionsSentObject {

    private Integer id;
    private UUID paymentId;
    private String payload;
    private Integer count;
    private Timestamp dateCreated;
    private Timestamp dateSent;

    public PaymentDecisionsSentObject() {}

    public PaymentDecisionsSentObject(
            Integer id, UUID paymentId, String payload, Integer count, Timestamp dateCreated, Timestamp dateSent) {
        this.id = id;
        this.paymentId = paymentId;
        this.payload = payload;
        this.count = count;
        this.dateCreated = dateCreated;
        this.dateSent = dateSent;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PaymentDecisionsSentObject that)) return false;
        return Objects.equals(id, that.id)
                && Objects.equals(paymentId, that.paymentId)
                && Objects.equals(payload, that.payload)
                && Objects.equals(count, that.count)
                && Objects.equals(dateCreated, that.dateCreated)
                && Objects.equals(dateSent, that.dateSent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, paymentId, payload, count, dateCreated, dateSent);
    }

    @Override
    public String toString() {
        return "PaymentDecisionsSentObject{" + "id=" + id + ", paymentId=" + paymentId + ", payload='" + payload + '\''
                + ", count=" + count + ", dateCreated=" + dateCreated + ", dateSent=" + dateSent + '}';
    }
}
