package business_objects.db.payment_gate.payment_events;


import helpers.data.ClientHelper;

import java.sql.Timestamp;
import java.time.Instant;

import static utils.Utils.*;

public class PaymentEventsObjectFactory {

    public static PaymentEventsObject generatePaymentEventsObject(ClientHelper client) {
        return new PaymentEventsObject(
                getRandomUuid(),                                            // paymentId
                getRandomIntPositive().toString(),                          // crmId
                "Withdrawal",                                               // type
                1,                                                          // finalDecisionId
                client.getUcid(),                                           // ucid (if differs from paymentId)
                Timestamp.from(Instant.now()),                              // dateCreated
                Timestamp.from(Instant.now()),                              // dateUpdated
                Timestamp.from(Instant.now()),                              // dateDecided
                "PENDING", ""
        );
    }
}
