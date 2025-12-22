package business_objects.db.payment_gate.d_payment_types;

import static utils.Utils.getCurrentDate;
import static utils.Utils.getRandomIntPositive;

import helpers.data.ClientHelper;

public class DPaymentTypesObjectFactory {

    public static DPaymentTypesObject generateDPaymentRulesObject(ClientHelper client) {
        return new DPaymentTypesObject(
                getRandomIntPositive(), // id
                "UNKNOWN", // type
                getCurrentDate(), // dateCreated
                getCurrentDate() // dateUpdated
                );
    }
}
