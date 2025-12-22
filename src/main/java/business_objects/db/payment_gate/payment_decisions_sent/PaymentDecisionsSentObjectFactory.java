package business_objects.db.payment_gate.payment_decisions_sent;

import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Timestamp;
import java.time.Instant;

public class PaymentDecisionsSentObjectFactory {
    static ObjectMapper objectMapper = new ObjectMapper();

    public static PaymentDecisionsSentObject generatePaymentDecisionSentObject(
            PaymentEventsObject event, PaymentDecisionSentPayloadMessageObject payload) throws JsonProcessingException {
        return new PaymentDecisionsSentObject(
                null,
                event.getPaymentId(),
                objectMapper.writeValueAsString(payload),
                0,
                Timestamp.from(Instant.now()),
                Timestamp.from(Instant.now()));
    }
}
