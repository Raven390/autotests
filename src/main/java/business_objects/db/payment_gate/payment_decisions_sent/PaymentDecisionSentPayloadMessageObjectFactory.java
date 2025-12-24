package business_objects.db.payment_gate.payment_decisions_sent;

import business_objects.db.payment_gate.payment_details.PaymentDetailsObject;
import helpers.data.ClientHelper;
import java.util.UUID;
import utils.Utils;

public class PaymentDecisionSentPayloadMessageObjectFactory {

    public static PaymentDecisionSentPayloadMessageObject generatePayload(
            ClientHelper client, PaymentDetailsObject paymentDetailsObject) {
        PaymentDecisionSentPayloadMessageObject payload = new PaymentDecisionSentPayloadMessageObject();
        payload.setId(UUID.randomUUID().toString());
        payload.setType("withdrawal");
        payload.setBrand(client.getBrand());
        payload.setStatus("Approve");
        payload.setClientId(client.getUserId());
        payload.setRuleName("Router rule");
        payload.setCheckName("");
        payload.setRegulator(client.getRegulator());
        payload.setTimestamp("2025-11-21T00:58:05.898Z");
        payload.setTransferId(Utils.getRandomLongPositive());
        payload.setSchemaVersion("1.0");
        payload.setInternalReason("");
        payload.setMerchantOrderId(paymentDetailsObject.getMerchantOrderId());
        payload.setRejectionReason("");
        payload.setUnderManualReview(1);
        payload.setRejectionReasonCode("");
        payload.setRejectionReasonRecommend("");
        return payload;
    }
}
