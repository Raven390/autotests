package business_objects.db.payment_gate.d_payment_rules;


import helpers.data.ClientHelper;

import static utils.Utils.getCurrentDate;
import static utils.Utils.getRandomIntPositive;

public class DPaymentRulesObjectFactory {

    public static DPaymentRulesObject generateDPaymentRulesObject(ClientHelper client) {
        return new DPaymentRulesObject(
                getRandomIntPositive(), // id
                "UNKNOWN",           // name
                "UNKNOWN",           // type
                "",                  // description
                getCurrentDate(),       // dateCreated
                getCurrentDate()        // dateUpdated
        );
    }


}
