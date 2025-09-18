package business_objects.db.payment_gate.tmp_rule_decisions;


import business_objects.db.payment_gate.payment_events.PaymentEventsObject;

import java.sql.Timestamp;
import java.time.Instant;

import static utils.Utils.getRandomIntPositive;

public class TmpRuleDecisionsObjectFactory {

    public static TmpRuleDecisionsObject generateTmpRuleDecisionsObject(PaymentEventsObject paymentEventsObject)
            throws Exception {
        return new TmpRuleDecisionsObject(
                getRandomIntPositive(),                                           // id
                paymentEventsObject.getPaymentId(),    // paymentId
                "AUTO_APPROVE",                                     // decision
                Timestamp.from(Instant.now())                 // dateCreated
        );
    }
}
