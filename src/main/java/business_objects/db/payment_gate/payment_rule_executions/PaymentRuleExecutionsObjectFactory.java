package business_objects.db.payment_gate.payment_rule_executions;


import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import utils.Utils;

import java.sql.Timestamp;
import java.time.Instant;


public class PaymentRuleExecutionsObjectFactory {

    public static PaymentRuleExecutionsObject generatePaymentRuleExecutionsObject(PaymentEventsObject event) {
        return new PaymentRuleExecutionsObject(
                event.getPaymentId(), // paymentId
                Utils.getRandomIntPositive(),                            // runId
                1,                            // ruleId
                "1",                         // ruleVersion
                Utils.getRandomIntPositive(),                            // ruleEndId
                Timestamp.from(Instant.now()),// dateCreated
                Timestamp.from(Instant.now()),// dateUpdated
                Timestamp.from(Instant.now()),// dateStarted
                Timestamp.from(Instant.now()) // dateCompleted
        );
    }


}
