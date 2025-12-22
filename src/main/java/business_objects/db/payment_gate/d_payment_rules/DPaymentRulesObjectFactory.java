package business_objects.db.payment_gate.d_payment_rules;

import static utils.Utils.getCurrentDate;
import static utils.Utils.getRandomIntPositive;

import helpers.data.ClientHelper;

public class DPaymentRulesObjectFactory {

    public static DPaymentRulesObject generateDPaymentRulesObject(ClientHelper client) {
        return new DPaymentRulesObject(
                getRandomIntPositive(), // id
                "UNKNOWN", // name
                "UNKNOWN", // type
                "", // description
                getCurrentDate(), // dateCreated
                getCurrentDate() // dateUpdated
                );
    }
}
