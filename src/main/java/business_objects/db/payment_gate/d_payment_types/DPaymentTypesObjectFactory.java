package business_objects.db.payment_gate.d_payment_types;


import helpers.data.ClientHelper;

import static utils.Utils.getCurrentDate;
import static utils.Utils.getRandomIntPositive;

public class DPaymentTypesObjectFactory {

    public static DPaymentTypesObject generateDPaymentRulesObject(ClientHelper client) {
        return new DPaymentTypesObject(
                getRandomIntPositive(),     // id
                "UNKNOWN",                  // type
                getCurrentDate(),           // dateCreated
                getCurrentDate()            // dateUpdated
        );
    }


}
