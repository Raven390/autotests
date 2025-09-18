package business_objects.db.payment_gate.payment_details;


import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import helpers.data.ClientHelper;

import java.sql.Timestamp;
import java.time.Instant;

import static utils.Utils.*;

public class PaymentDetailsObjectFactory {

    public static PaymentDetailsObject generatePaymentDetailsObject(ClientHelper client) {
        return new PaymentDetailsObject(
                getRandomIntPositive(),                         // id
                getRandomUuid(),                               // paymentId
                client != null ? client.getBrand() : null,      // brand
                client != null ? client.getRegulator() : null,  // regulator
                "UNKNOWN",                                     // type
                client != null && client.getUserId() != null ? String.valueOf(client.getUserId()) : null, // clientId
                getRandomUuid().toString(),                     // merchantOrderId
                Timestamp.from(Instant.now()),                  // eventDate
                "NEW",                                        // status
                null,                                           // platform
                "{}",                                         // payload
                "AF",                                         // sourceSystem
                "QA",                                         // sourceEnv
                Timestamp.from(Instant.now())                // dateCreated
        );
    }

    public static PaymentDetailsObject generatePaymentDetailsObject(PaymentEventsObject event, ClientHelper client) {
        return new PaymentDetailsObject(
                getRandomIntPositive(),                          // id
                event.getPaymentId(),                            // paymentId
                client != null ? client.getBrand() : null,       // brand
                client != null ? client.getRegulator() : null,   // regulator
                "UNKNOWN",                                       // type
                client != null && client.getUserId() != null ? String.valueOf(client.getUserId()) : null, // clientId
                getRandomUuid().toString(),                     // merchantOrderId
                event.getDateCreated(),                          // eventDate
                "NEW",                                          // status
                null,                                           // platform
                "{}",                                             // payload
                "AF",                                             // sourceSystem
                "QA",                                             // sourceEnv
                event.getDateCreated()                           // dateCreated
        );
    }


}
