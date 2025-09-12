package business_objects.db.payment_gate.d_status;


import helpers.data.ClientHelper;

import static utils.Utils.getCurrentDate;
import static utils.Utils.getRandomIntPositive;

public class DStatusObjectFactory {

    public static DStatusObject generateDStatusObject(ClientHelper client) {
        return new DStatusObject(
                getRandomIntPositive(), // id
                "UNKNOWN",         // status
                "",                // description
                getCurrentDate(),   // dateCreated
                getCurrentDate()    // dateUpdated
        );
    }


}
