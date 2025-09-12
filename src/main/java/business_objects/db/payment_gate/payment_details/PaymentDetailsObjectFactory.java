package business_objects.db.payment_gate.payment_details;


import helpers.data.ClientHelper;

import static utils.Utils.getCurrentTimestampDbFormat;
import static utils.Utils.getRandomIntPositive;

public class PaymentDetailsObjectFactory {

    public static PaymentDetailsObject generatePaymentDetailsObject(ClientHelper client) {
        return new PaymentDetailsObject(
                getRandomIntPositive(),                         // id
                client != null ? client.getUcid() : null,       // paymentId
                client != null ? client.getBrand() : null,      // brand
                client != null ? client.getRegulator() : null,  // regulator
                "UNKNOWN",                                     // type
                client != null && client.getUserId() != null ? String.valueOf(client.getUserId()) : null, // clientId
                null,                                           // merchantOrderId
                getCurrentTimestampDbFormat(),                  // eventDate
                "NEW",                                        // status
                null,                                           // platform
                "{}",                                         // payload
                "AF",                                         // sourceSystem
                "QA",                                         // sourceEnv
                getCurrentTimestampDbFormat()                   // dateCreated
        );
    }


}
