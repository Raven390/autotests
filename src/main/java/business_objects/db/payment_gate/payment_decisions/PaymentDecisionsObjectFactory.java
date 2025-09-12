package business_objects.db.payment_gate.payment_decisions;


import helpers.data.ClientHelper;

import static utils.Utils.getCurrentTimestampDbFormat;
import static utils.Utils.getRandomIntPositive;

public class PaymentDecisionsObjectFactory {

    public static PaymentDecisionsObject generatePaymentDecisionObject(ClientHelper client) {
        return new PaymentDecisionsObject(
                getRandomIntPositive(),                         // id
                client != null ? client.getUcid() : null,       // paymentId
                "UNKNOWN",                                     // decisionType
                0,                                              // decisionCode
                0,                                              // rejectionCode
                "SYSTEM",                                      // actor
                getCurrentTimestampDbFormat(),                  // dateCreated
                getCurrentTimestampDbFormat(),                  // dateUpdated
                getCurrentTimestampDbFormat()                   // dateDecided
        );
    }


}
