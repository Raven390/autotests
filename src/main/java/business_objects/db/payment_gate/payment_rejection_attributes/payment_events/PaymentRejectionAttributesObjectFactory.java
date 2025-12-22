package business_objects.db.payment_gate.payment_rejection_attributes.payment_events;

import business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObject;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import java.sql.Timestamp;
import java.time.Instant;

public class PaymentRejectionAttributesObjectFactory {

    public static PaymentRejectionAttributesObject generatePaymentRejectionAttributesObject(
            PaymentEventsObject event, PaymentDecisionsObject decision) {
        return new PaymentRejectionAttributesObject(
                event.getPaymentId(),
                decision.getId(),
                1,
                "Passport",
                Timestamp.from(Instant.now()),
                Timestamp.from(Instant.now()));
    }
}
