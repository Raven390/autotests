package business_objects.db.payment_gate.payment_rule_executions;


import helpers.data.ClientHelper;

import static utils.Utils.getCurrentTimestampDbFormat;
import static utils.Utils.getRandomIntPositive;

public class PaymentRuleExecutionsObjectFactory {

    public static PaymentRuleExecutionsObject generatePaymentRuleExecutionsObject(ClientHelper client) {
        return new PaymentRuleExecutionsObject(
                getRandomIntPositive(),                         // id
                client != null ? client.getUcid() : null, // paymentId
                0,                            // runId
                0,                            // ruleId
                "0",                         // ruleVersion
                0,                            // ruleEndId
                getCurrentTimestampDbFormat(),// dateCreated
                getCurrentTimestampDbFormat(),// dateUpdated
                getCurrentTimestampDbFormat(),// dateStarted
                getCurrentTimestampDbFormat() // dateCompleted
        );
    }


}
