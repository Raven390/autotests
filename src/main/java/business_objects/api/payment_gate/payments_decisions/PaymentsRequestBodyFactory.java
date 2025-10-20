package business_objects.api.payment_gate.payments_decisions;

import helpers.data.enums.payment_gate.Decision;

import static utils.Utils.getRandomDateTimeIsoUtc;

public class PaymentsRequestBodyFactory {

    public static PostDecisionsRequestBody createPostDecisionsRequestBody() {
        PostDecisionsRequestBody body = new PostDecisionsRequestBody();
        body.setDecisionType(Decision.FINAL_APPROVE.getType());
        body.setDecisionCode(Decision.FINAL_APPROVE.getCode());
        body.setDecidedAt(getRandomDateTimeIsoUtc());
        return body;
    }

    public static PutDecisionsRequestBody createPutDecisionsRequestBody() {
        PutDecisionsRequestBody body = new PutDecisionsRequestBody();
        body.setDecisionType(Decision.FINAL_APPROVE.getType());
        body.setDecisionCode(Decision.FINAL_APPROVE.getCode());
        body.setDecidedAt(getRandomDateTimeIsoUtc());
        body.setActor("Vindex BO");
        return body;
    }
}
