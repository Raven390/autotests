package helpers.asserts;

import static helpers.database.PaymentGateHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;

import business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObject;
import business_objects.db.payment_gate.payment_details.PaymentDetailsObject;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import business_objects.db.payment_gate.payment_rule_executions.PaymentRuleExecutionsObject;
import helpers.data.DataHelper;
import io.qameta.allure.Step;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class PaymentGateAssertsHelper {

    @Step("Assert payment event")
    public static void assertPaymentEvent(DataHelper data, UUID paymentId, String type, String deliveryStatus)
            throws Exception {
        // Check payment event in PGS DB
        PaymentEventsObject paymentEvent = getPaymentEvent(data.clientHelper.getUcid());
        assert paymentEvent != null;
        assertThat(
                "PaymentEvent.paymentId should match provided paymentId", paymentEvent.getPaymentId(), is(paymentId));
        if (data.crmWithdrawalEventV2 == null) {
            assertThat(
                    "PaymentEvent.crmId should match transferToWaEvent.transferId",
                    paymentEvent.getCrmId(),
                    is(data.transferToWaEvent.getTransferId().toString()));
        } else {
            assertThat(
                    "PaymentEvent.crmId should match crmWithdrawalEventV2.withdrawalId",
                    paymentEvent.getCrmId(),
                    is(data.crmWithdrawalEventV2.getWithdrawalId().toString()));
        }
        assertThat("PaymentEvent.type should match provided type", paymentEvent.getType(), is(type));
        assertThat(
                "PaymentEvent.finalDecisionId should be null before decision",
                paymentEvent.getFinalDecisionId(),
                is(nullValue()));
        assertThat(
                "PaymentEvent.ucid should match client's ucid",
                paymentEvent.getUcid(),
                is(data.clientHelper.getUcid()));
        assertThat("PaymentEvent.dateCreated should not be null", paymentEvent.getDateCreated(), is(notNullValue()));
        assertThat("PaymentEvent.dateUpdated should not be null", paymentEvent.getDateUpdated(), is(notNullValue()));
        assertThat(
                "PaymentEvent.dateDecided should be null before decision",
                paymentEvent.getDateDecided(),
                is(nullValue()));
        assertThat(
                "PaymentEvent.deliveryStatus should match provided deliveryStatus",
                paymentEvent.getDeliveryStatus(),
                is(deliveryStatus));
    }

    @Step("Assert payment event details")
    public static void assertPaymentDetails(DataHelper data, UUID paymentId, String status) throws Exception {
        // check payment details in PGS DB
        PaymentEventsObject paymentEvent = getPaymentEvent(data.clientHelper.getUcid());
        PaymentDetailsObject paymentDetailsObject = getPaymentDetails(data.clientHelper.getUserId());
        assert paymentDetailsObject != null;
        assertThat(
                "PaymentEvent.paymentId should match provided paymentId", paymentEvent.getPaymentId(), is(paymentId));
        if (data.crmWithdrawalEventV2 == null) {
            assertThat(
                    "PaymentDetails.brand should match transferToWaEvent.brand (lowercased)",
                    paymentDetailsObject.getBrand(),
                    is(data.transferToWaEvent.getBrand().toLowerCase()));
            assertThat(
                    "PaymentDetails.regulator should match transferToWaEvent.regulator",
                    paymentDetailsObject.getRegulator(),
                    is(data.transferToWaEvent.getRegulator()));
            assertThat(
                    "PaymentDetails.type should match transferToWaEvent.type",
                    paymentDetailsObject.getType(),
                    is(data.transferToWaEvent.getType()));
            assertThat(
                    "PaymentDetails.clientId should match transferToWaEvent.clientId",
                    paymentDetailsObject.getClientId(),
                    is(data.transferToWaEvent.getClientId().toString()));
            assertThat(
                    "PaymentDetails.merchantOrderId should match transferToWaEvent.merchantOrderId",
                    paymentDetailsObject.getMerchantOrderId(),
                    is(data.transferToWaEvent.getMerchantOrderId()));
            assertThat(
                    "PaymentDetails.platform should match transferToWaEvent.platform",
                    paymentDetailsObject.getPlatform(),
                    is(data.transferToWaEvent.getPlatform()));
        } else {
            assertThat(
                    "PaymentDetails.brand should match crmWithdrawalEventV2.brand (lowercased)",
                    paymentDetailsObject.getBrand(),
                    is(data.crmWithdrawalEventV2.getBrand().toLowerCase()));
            assertThat(
                    "PaymentDetails.regulator should match crmWithdrawalEventV2.regulator",
                    paymentDetailsObject.getRegulator(),
                    is(data.crmWithdrawalEventV2.getRegulator()));
            assertThat(
                    "PaymentDetails.type should match crmWithdrawalEventV2.type",
                    paymentDetailsObject.getType(),
                    is(data.crmWithdrawalEventV2.getType()));
            assertThat(
                    "PaymentDetails.clientId should match crmWithdrawalEventV2.clientId",
                    paymentDetailsObject.getClientId(),
                    is(data.crmWithdrawalEventV2.getClientId().toString()));
            assertThat(
                    "PaymentDetails.merchantOrderId should match crmWithdrawalEventV2.merchantOrderId",
                    paymentDetailsObject.getMerchantOrderId(),
                    is(data.crmWithdrawalEventV2.getMerchantOrderId()));
            assertThat(
                    "PaymentDetails.platform should match crmWithdrawalEventV2.platform",
                    paymentDetailsObject.getPlatform(),
                    is(data.crmWithdrawalEventV2.getPlatform()));
        }
        assertThat(
                "PaymentDetails.paymentId should match provided paymentId",
                paymentDetailsObject.getPaymentId(),
                is(paymentId));
        assertThat(
                "PaymentDetails.eventDate should not be null", paymentDetailsObject.getEventDate(), is(notNullValue()));
        assertThat("PaymentDetails.status should match provided status", paymentDetailsObject.getStatus(), is(status));
        assertThat(
                "PaymentDetails.sourceSystem should not be null",
                paymentDetailsObject.getSourceSystem(),
                is(notNullValue()));
        assertThat(
                "PaymentDetails.sourceEnv should not be null", paymentDetailsObject.getSourceEnv(), is(notNullValue()));
        assertThat(
                "PaymentDetails.dateCreated should not be null",
                paymentDetailsObject.getDateCreated(),
                is(notNullValue()));
        assertThat(
                "PaymentDetails.amountUsd string should contain '1.'",
                paymentDetailsObject.getAmountUsd().toString(),
                containsString("1."));
        assertThat(
                "PaymentDetails.payload should contain '\"withdrawalAmountUSD\": 1'",
                paymentDetailsObject.getPayload(),
                containsString("\"withdrawalAmountUSD\": 1"));
    }

    @Deprecated
    @Step("Assert risk decision")
    public static void assertRiskDecision(UUID paymentId, int code) throws Exception {
        // check pending decision
        List<PaymentDecisionsObject> paymentDecisionsObject =
                Objects.requireNonNull(getPaymentDecisionsByPaymentId(paymentId)).stream()
                        .filter(payment -> "risk".equals(payment.getDecisionType()))
                        .toList();
        assertThat("There should be exactly 1 risk decision in DB", paymentDecisionsObject.size(), is(1));
        assertThat(
                "Risk decision paymentId should match provided paymentId",
                paymentDecisionsObject.getFirst().getPaymentId(),
                is(paymentId));
        assertThat(
                "DecisionType should be 'risk'",
                paymentDecisionsObject.getFirst().getDecisionType(),
                is("risk"));
        assertThat(
                "Risk decision code should match provided code",
                paymentDecisionsObject.getFirst().getDecisionCode(),
                is(code));
        assertThat(
                "Risk decision rejectionCode should be null",
                paymentDecisionsObject.getFirst().getRejectionCode(),
                is(nullValue()));
        assertThat(
                "Risk decision actor should be 'Rule engine'",
                paymentDecisionsObject.getFirst().getActor(),
                is("Rule engine"));
    }

    @Step("Assert risk decision")
    public static void assertRiskDecision(UUID paymentId, int decisionCode, int rejectionCode, String actor)
            throws Exception {
        // check pending decision
        List<PaymentDecisionsObject> paymentDecisionsObject =
                Objects.requireNonNull(getPaymentDecisionsByPaymentId(paymentId)).stream()
                        .filter(payment -> "risk".equals(payment.getDecisionType()))
                        .toList();
        assertThat("There should be exactly 1 risk decision in DB", paymentDecisionsObject.size(), is(1));
        assertThat(
                "Risk decision paymentId should match provided paymentId",
                paymentDecisionsObject.getFirst().getPaymentId(),
                is(paymentId));
        assertThat(
                "DecisionType should be 'risk'",
                paymentDecisionsObject.getFirst().getDecisionType(),
                is("risk"));
        assertThat(
                "Risk decision code should match provided code",
                paymentDecisionsObject.getFirst().getDecisionCode(),
                is(decisionCode));
        assertThat(
                "Risk decision rejectionCode should be null",
                paymentDecisionsObject.getFirst().getRejectionCode(),
                is(rejectionCode));
        assertThat(
                "Risk decision actor should be 'Rule engine'",
                paymentDecisionsObject.getFirst().getActor(),
                is(actor));
    }

    @Step("Assert payment decision")
    public static void assertPaymentDecision(UUID paymentId, int code) throws Exception {
        // check pending decision
        List<PaymentDecisionsObject> paymentDecisionsObject =
                Objects.requireNonNull(getPaymentDecisionsByPaymentId(paymentId)).stream()
                        .filter(payment -> "payment".equals(payment.getDecisionType()))
                        .toList();
        assertThat(
                "Payment decision paymentId should match provided paymentId",
                paymentDecisionsObject.getFirst().getPaymentId(),
                is(paymentId));
        assertThat(
                "DecisionType should be 'payment'",
                paymentDecisionsObject.getFirst().getDecisionType(),
                is("payment"));
        assertThat(
                "Payment decision code should match provided code",
                paymentDecisionsObject.getFirst().getDecisionCode(),
                is(code));
        assertThat(
                "Payment decision rejectionCode should be null",
                paymentDecisionsObject.getFirst().getRejectionCode(),
                is(nullValue()));
        assertThat(
                "Payment decision actor should be 'Rule engine'",
                paymentDecisionsObject.getFirst().getActor(),
                is("Rule engine"));
    }

    @Step("Assert decision")
    public static void assertDecision(
            List<PaymentDecisionsObject> decision, List<PaymentDecisionsObject> decision2, UUID paymentId) {
        assertThat(
                "Risk decision paymentId should match provided paymentId",
                decision.getFirst().getPaymentId(),
                is(paymentId));
        assertThat("Risk decision type should be 'risk'", decision.getFirst().getDecisionType(), is("risk"));
        assertThat("Risk decision code should be 1", decision.getFirst().getDecisionCode(), is(1));
        assertThat(
                "Risk decision rejectionCode should be 0", decision.getFirst().getRejectionCode(), is(0));
        assertThat(
                "Risk decision actor should be 'Auto qa'", decision.getFirst().getActor(), is("Auto qa"));
        assertThat(
                "Risk decision reasonString should be 'Account activity review.'",
                decision.getFirst().getReasonString(),
                is("Account activity review."));

        assertThat(
                "Final decision paymentId should match provided paymentId",
                decision2.getFirst().getPaymentId(),
                is(paymentId));
        assertThat("Final decision type should be 'final'", decision2.getFirst().getDecisionType(), is("final"));
        assertThat("Final decision code should be 1", decision2.getFirst().getDecisionCode(), is(1));
        assertThat(
                "Final decision rejectionCode should be null",
                decision2.getFirst().getRejectionCode(),
                is(nullValue()));
        assertThat(
                "Final decision actor should be 'Vindex BO'",
                decision2.getFirst().getActor(),
                is("Vindex BO"));
        assertThat(
                "Final decision reasonString should be null",
                decision2.getFirst().getReasonString(),
                is(nullValue()));
    }

    @Step("Assert put payment")
    public static void assertPutPayment(UUID paymentId) {
        // TODO
    }

    @Step("Assert rule execution")
    public static void assertRuleExecutions(UUID paymentId) throws Exception {
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString());
        assertThat(
                "RuleExecution.paymentId should match provided paymentId",
                paymentRuleExecutionsObject.getPaymentId(),
                is(paymentId));
        assertThat(
                "RuleExecution.runId should not be null", paymentRuleExecutionsObject.getRunId(), is(notNullValue()));
        assertThat(
                "RuleExecution.ruleId should not be null", paymentRuleExecutionsObject.getRuleId(), is(notNullValue()));
        assertThat(
                "RuleExecution.ruleVersion should not be null",
                paymentRuleExecutionsObject.getRuleVersion(),
                is(notNullValue()));
        assertThat(
                "RuleExecution.ruleEndId should not be null",
                paymentRuleExecutionsObject.getRuleEndId(),
                is(notNullValue()));
        assertThat(
                "RuleExecution.dateCreated should not be null",
                paymentRuleExecutionsObject.getDateCreated(),
                is(notNullValue()));
        assertThat(
                "RuleExecution.dateUpdated should not be null",
                paymentRuleExecutionsObject.getDateUpdated(),
                is(notNullValue()));
        assertThat(
                "RuleExecution.dateStarted should not be null",
                paymentRuleExecutionsObject.getDateStarted(),
                is(notNullValue()));
        assertThat(
                "RuleExecution.dateCompleted should not be null",
                paymentRuleExecutionsObject.getDateCompleted(),
                is(notNullValue()));
    }
}
