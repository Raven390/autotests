package business_objects.db.payment_gate.payment_decisions_sent;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

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

    public PaymentDecisionsSentObject() {
    }

    public PaymentDecisionsSentObject(
            Integer id, UUID paymentId, String payload, Integer count, Timestamp dateCreated, Timestamp dateSent) {
        this.id = id;
        this.paymentId = paymentId;
        this.payload = payload;
        this.count = count;
        this.dateCreated = dateCreated;
        this.dateSent = dateSent;
    }
}