package business_objects.db.payment_gate.payment_decisions_sent;

import helpers.data.ClientHelper;
import java.util.UUID;

public class PaymentDecisionSentPayloadMessageObjectFactory {
    public static PaymentDecisionSentPayloadMessageObject generatePaymentDecisionSentPayloadMessage(
            ClientHelper client) {
        PaymentDecisionSentPayloadMessageObject object = new PaymentDecisionSentPayloadMessageObject();

        object.setSchemaVersion("2.0");
        object.setId(UUID.randomUUID().toString());
        object.setTimestamp(String.valueOf(System.currentTimeMillis()));
        object.setTransferId(1L);
        object.setBrand(client.getBrand());
        object.setClientId(client.getUserId());
        object.setType("withdrawal");
        object.setRegulator(client.getRegulator());
        object.setInternalReason("");
        object.setStatus("");
        object.setMerchantOrderId("");
        object.setCheckName("");
        object.setRuleName("Rule engine");
        object.setRejectionReasonCode("");
        object.setRejectionReason("");
        object.setRejectionReasonAttr("");
        object.setRejectionReasonRecommend("");
        object.setPaymentId("");
        object.setUnderManualReview(0);

        return object;
    }
}
