package helpers.api;

import static business_objects.api.payment_gate.payments_decisions.DecisionsRequests.putDecisions;
import static utils.Utils.getRandomDateTimeIsoUtc;

import business_objects.api.payment_gate.payments_decisions.PutDecisionsRequestBody;
import helpers.data.enums.payment_gate.Decision;
import io.qameta.allure.Step;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class PaymentGateHelper {

    @Step("Send payment approve decision")
    public static void sendPaymentApproveDecision(UUID paymentId) throws IOException {
        PutDecisionsRequestBody putPaymentDecisionBody = new PutDecisionsRequestBody();
        putPaymentDecisionBody.setDecisionType(Decision.PAYMENT_APPROVE.getType());
        putPaymentDecisionBody.setDecisionCode(Decision.PAYMENT_APPROVE.getCode());
        putPaymentDecisionBody.setRejectionCode(0);
        putPaymentDecisionBody.setDecidedAt(getRandomDateTimeIsoUtc());
        putPaymentDecisionBody.setActor("Auto qa");
        putDecisions(paymentId.toString(), List.of(putPaymentDecisionBody));
    }

    @Step("Send payment reject decision")
    public static void sendPaymentRejectDecision(UUID paymentId) throws IOException {
        PutDecisionsRequestBody putPaymentDecisionBody = new PutDecisionsRequestBody();
        putPaymentDecisionBody.setDecisionType(Decision.PAYMENT_REJECT.getType());
        putPaymentDecisionBody.setDecisionCode(Decision.PAYMENT_REJECT.getCode());
        putPaymentDecisionBody.setRejectionCode(3);
        putPaymentDecisionBody.setDecidedAt(getRandomDateTimeIsoUtc());
        putPaymentDecisionBody.setActor("Auto qa");
        putDecisions(paymentId.toString(), List.of(putPaymentDecisionBody));
    }

    @Step("Send risk approve decision")
    public static void sendRiskApproveDecision(UUID paymentId) throws IOException {
        PutDecisionsRequestBody putPaymentDecisionBody = new PutDecisionsRequestBody();
        putPaymentDecisionBody.setDecisionType(Decision.RISK_APPROVE.getType());
        putPaymentDecisionBody.setDecisionCode(Decision.RISK_APPROVE.getCode());
        putPaymentDecisionBody.setRejectionCode(0);
        putPaymentDecisionBody.setDecidedAt(getRandomDateTimeIsoUtc());
        putPaymentDecisionBody.setActor("Auto qa");
        putDecisions(paymentId.toString(), List.of(putPaymentDecisionBody));
    }

    @Step("Send risk reject decision")
    public static void sendRiskRejectDecision(UUID paymentId) throws IOException {
        PutDecisionsRequestBody putPaymentDecisionBody = new PutDecisionsRequestBody();
        putPaymentDecisionBody.setDecisionType(Decision.RISK_REJECT.getType());
        putPaymentDecisionBody.setDecisionCode(Decision.RISK_REJECT.getCode());
        putPaymentDecisionBody.setRejectionCode(1);
        putPaymentDecisionBody.setDecidedAt(getRandomDateTimeIsoUtc());
        putPaymentDecisionBody.setActor("Auto qa");
        putDecisions(paymentId.toString(), List.of(putPaymentDecisionBody));
    }
}
