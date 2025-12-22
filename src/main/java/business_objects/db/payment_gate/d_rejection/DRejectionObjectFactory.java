package business_objects.db.payment_gate.d_rejection;

import static utils.Utils.getCurrentDate;
import static utils.Utils.getRandomIntPositive;

import helpers.data.ClientHelper;

public class DRejectionObjectFactory {

    public static DRejectionObject generateDRejectionObject(ClientHelper client) {
        return new DRejectionObject(
                getRandomIntPositive(), // id
                0, // code
                "UNKNOWN", // name
                "", // description
                getCurrentDate(), // dateCreated
                getCurrentDate() // dateUpdated
                );
    }
}
