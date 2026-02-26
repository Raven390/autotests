package business_objects.api.payment_gate.payments_decisions;

import static utils.Utils.getRandomDateTimeIsoUtc;

import helpers.data.enums.payment_gate.Decision;
import java.util.ArrayList;

public class PaymentsRequestBodyFactory {

    public static PostDecisionsRequestBody createPostDecisionsRequestBody() {
        PostDecisionsRequestBody body = new PostDecisionsRequestBody();
        body.setDecisionType(Decision.FINAL_APPROVE.getType());
        body.setDecisionCode(Decision.FINAL_APPROVE.getCode());
        body.setDecidedAt(getRandomDateTimeIsoUtc());
        return body;
    }

    public static PostDecisionsRequestBody createPostDecisionsRequestBody(Boolean reject) {
        PostDecisionsRequestBody body = new PostDecisionsRequestBody();
        body.setDecisionType(Decision.FINAL_REJECT.getType());
        body.setDecisionCode(Decision.FINAL_REJECT.getCode());
        body.setRejectionCode(2);
        body.setDecidedAt(getRandomDateTimeIsoUtc());
        // Insert example attributes for rejection body
        ArrayList<PostDecisionsRequestBody.Attribute> attrs = new ArrayList<>();
        attrs.add(new PostDecisionsRequestBody.Attribute("PAYMENT_PROFILE_CURRENT", "Payment profile"));
        body.setAttributes(attrs);
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

    public static PutDecisionsRequestBody createPutDecisionsRequestBody(Boolean reject) {
        PutDecisionsRequestBody body = new PutDecisionsRequestBody();
        body.setDecisionType(Decision.FINAL_REJECT.getType());
        body.setDecisionCode(Decision.FINAL_REJECT.getCode());
        body.setRejectionCode(2);
        body.setDecidedAt(getRandomDateTimeIsoUtc());
        // Insert example attributes for rejection body
        ArrayList<PutDecisionsRequestBody.Attribute> attrs = new ArrayList<>();
        attrs.add(new PutDecisionsRequestBody.Attribute("PAYMENT_PROFILE_CURRENT", "Payment profile"));
        body.setAttributes(attrs);
        return body;
    }
}
