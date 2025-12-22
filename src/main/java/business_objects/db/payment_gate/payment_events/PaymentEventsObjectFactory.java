package business_objects.db.payment_gate.payment_events;

import static utils.Utils.*;

import helpers.data.ClientHelper;
import java.sql.Timestamp;
import java.time.Instant;

public class PaymentEventsObjectFactory {

    public static PaymentEventsObject generatePaymentEventsObject(ClientHelper client) {
        return new PaymentEventsObject(
                getRandomUuid(), // paymentId
                getRandomIntPositive().toString(), // crmId
                "withdrawal", // type
                1, // finalDecisionId
                client.getUcid(), // ucid (if differs from paymentId)
                Timestamp.from(Instant.now()), // dateCreated
                Timestamp.from(Instant.now()), // dateUpdated
                Timestamp.from(Instant.now()), // dateDecided
                "PENDING",
                "");
    }
}
