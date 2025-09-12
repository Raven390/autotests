package business_objects.db.payment_gate.payment_events;


import static utils.Utils.getCurrentTimestampDbFormat;
import static utils.Utils.getRandomIntPositive;

public class PaymentEventsObjectFactory {

    public static PaymentEventsObject generatePaymentEventsObject() {
        return new PaymentEventsObject(
                getRandomIntPositive().toString(),                 // paymentId
                getRandomIntPositive().toString(),                                 // crmId
                "UNKNOWN",                                               // type
                0,                                                        // finalDecisionId
                null,                                                     // ucid (if differs from paymentId)
                getCurrentTimestampDbFormat(),                            // dateCreated
                getCurrentTimestampDbFormat(),                            // dateUpdated
                getCurrentTimestampDbFormat()                             // dateDecided
        );
    }


}
