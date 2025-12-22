package business_objects.db.payment_gate.d_decisions;

import static utils.Utils.getCurrentDate;
import static utils.Utils.getRandomIntPositive;

import helpers.data.ClientHelper;

public class DDecisionObjectFactory {

    public static DDecisionObject generateDDecisionObject(ClientHelper client) {
        return new DDecisionObject(
                getRandomIntPositive(), // id
                "UNKNOWN", // type
                0, // code
                "UNKNOWN", // name
                "", // description
                getCurrentDate(), // dateCreated
                getCurrentDate() // dateUpdated
                );
    }
}
