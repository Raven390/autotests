package business_objects.db.payment_gate.d_payment_rule_outcomes;

import static utils.Utils.getCurrentDate;
import static utils.Utils.getRandomIntPositive;

import helpers.data.ClientHelper;

public class DPaymentRuleOutcomeObjectFactory {

    public static DPaymentRuleOutcomeObject generateDPaymentRuleOutcomeObject(ClientHelper client) {
        return new DPaymentRuleOutcomeObject(
                getRandomIntPositive(), // id
                0, // ruleId
                0, // endId
                "UNKNOWN", // endName
                "", // endDescription
                getCurrentDate(), // dateCreated
                getCurrentDate() // dateUpdated
                );
    }
}
