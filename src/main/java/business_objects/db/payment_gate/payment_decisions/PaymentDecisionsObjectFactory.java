package business_objects.db.payment_gate.payment_decisions;


import business_objects.db.payment_gate.payment_events.PaymentEventsObject;

import java.sql.Timestamp;
import java.time.Instant;


public class PaymentDecisionsObjectFactory {

    public static PaymentDecisionsObject generatePaymentDecisionObject(PaymentEventsObject event) {
        return new PaymentDecisionsObject(
                null, event.getPaymentId(),       // paymentId
                "final",                                     // decisionType
                1,                                              // decisionCode
                0,                                              // rejectionCode
                "QA",                                      // actor
                Timestamp.from(Instant.now()),                  // dateCreated
                Timestamp.from(Instant.now()),                // dateUpdated
                Timestamp.from(Instant.now())                   // dateDecided
        );
    }

    public static PaymentDecisionsObject generateRiskPaymentDecisionObject(PaymentEventsObject event) {
        return new PaymentDecisionsObject(
                null, event.getPaymentId(),       // paymentId
                "risk",                                     // decisionType
                0,                                              // decisionCode
                0,                                              // rejectionCode
                "QA",                                      // actor
                Timestamp.from(Instant.now()),                  // dateCreated
                Timestamp.from(Instant.now()),                // dateUpdated
                Timestamp.from(Instant.now())                   // dateDecided
        );
    }


}
