package helpers.api;

import business_objects.api.payment_gate.payments_decisions.PutDecisionsRequestBody;
import helpers.data.enums.payment_gate.Decision;
import io.qameta.allure.Step;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static business_objects.api.payment_gate.payments_decisions.DecisionsRequests.putDecisions;
import static utils.Utils.getRandomDateTimeIsoUtc;

public class PaymentGateHelper {

    @Step("Send payment approve decision")
    public static void sendPaymentApproveDecision(UUID paymentId) throws IOException {
        PutDecisionsRequestBody putPaymentDecisionBody1 = new PutDecisionsRequestBody();
        putPaymentDecisionBody1.setDecisionType(Decision.PAYMENT_APPROVE.getType());
        putPaymentDecisionBody1.setDecisionCode(Decision.PAYMENT_APPROVE.getCode());
        putPaymentDecisionBody1.setRejectionCode(0);
        putPaymentDecisionBody1.setDecidedAt(getRandomDateTimeIsoUtc());
        putPaymentDecisionBody1.setActor("Auto qa");
        putDecisions(paymentId.toString(), List.of(putPaymentDecisionBody1));
    }

    @Step("Send payment reject decision")
    public static void sendPaymentRejectDecision(UUID paymentId) throws IOException {
        PutDecisionsRequestBody putPaymentDecisionBody1 = new PutDecisionsRequestBody();
        putPaymentDecisionBody1.setDecisionType(Decision.PAYMENT_REJECT.getType());
        putPaymentDecisionBody1.setDecisionCode(Decision.PAYMENT_REJECT.getCode());
        putPaymentDecisionBody1.setRejectionCode(3);
        putPaymentDecisionBody1.setDecidedAt(getRandomDateTimeIsoUtc());
        putPaymentDecisionBody1.setActor("Auto qa");
        putDecisions(paymentId.toString(), List.of(putPaymentDecisionBody1));
    }

    @Step("Send risk approve decision")
    public static void sendRiskApproveDecision(UUID paymentId) throws IOException {
        PutDecisionsRequestBody putPaymentDecisionBody1 = new PutDecisionsRequestBody();
        putPaymentDecisionBody1.setDecisionType(Decision.RISK_APPROVE.getType());
        putPaymentDecisionBody1.setDecisionCode(Decision.RISK_APPROVE.getCode());
        putPaymentDecisionBody1.setRejectionCode(0);
        putPaymentDecisionBody1.setDecidedAt(getRandomDateTimeIsoUtc());
        putPaymentDecisionBody1.setActor("Auto qa");
        putDecisions(paymentId.toString(), List.of(putPaymentDecisionBody1));
    }

    @Step("Send risk reject decision")
    public static void sendRiskRejectDecision(UUID paymentId) throws IOException {
        PutDecisionsRequestBody putPaymentDecisionBody1 = new PutDecisionsRequestBody();
        putPaymentDecisionBody1.setDecisionType(Decision.RISK_REJECT.getType());
        putPaymentDecisionBody1.setDecisionCode(Decision.RISK_REJECT.getCode());
        putPaymentDecisionBody1.setRejectionCode(1);
        putPaymentDecisionBody1.setDecidedAt(getRandomDateTimeIsoUtc());
        putPaymentDecisionBody1.setActor("Auto qa");
        putDecisions(paymentId.toString(), List.of(putPaymentDecisionBody1));
    }
}
