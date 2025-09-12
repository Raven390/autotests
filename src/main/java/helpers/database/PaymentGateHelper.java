package helpers.database;

import business_objects.db.payment_gate.payment_details.PaymentDetailsObject;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;

import java.util.List;

import static helpers.database.DbHelper.getObjectsFromDB;
import static utils.Constants.PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE;
import static utils.Constants.PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE;

public class PaymentGateHelper {

    public static PaymentEventsObject getPaymentEventByUcid(String ucid) throws Exception {
        List<PaymentEventsObject> objects = getObjectsFromDB(DbName.PAYMENT_GATE, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, "ucid='%s'".replace("%s", ucid), PaymentEventsObject.class);
        System.out.println(objects);
        if (objects.isEmpty()) {
            return null;
        } else {
            return objects.get(0);
        }
    }

    public static PaymentDetailsObject getPaymentDetailsByClientId(Integer clientId) throws Exception {
        List<PaymentDetailsObject> objects = getObjectsFromDB(DbName.PAYMENT_GATE, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, "client_id='%s'".replace("%s", clientId.toString()), PaymentDetailsObject.class);
        System.out.println(objects);
        if (objects.isEmpty()) {
            return null;
        } else {
            return objects.get(0);
        }
    }
}
