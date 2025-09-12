package business_objects.db.payment_gate.d_decisions;


import helpers.data.ClientHelper;

import static utils.Utils.getCurrentDate;
import static utils.Utils.getRandomIntPositive;

public class DDecisionObjectFactory {

    public static DDecisionObject generateDDecisionObject(ClientHelper client) {
        return new DDecisionObject(
                getRandomIntPositive(), // id
                "UNKNOWN",           // type
                0,                      // code
                "UNKNOWN",           // name
                "",                  // description
                getCurrentDate(),       // dateCreated
                getCurrentDate()        // dateUpdated
        );
    }


}
