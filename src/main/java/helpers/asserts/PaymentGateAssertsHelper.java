package helpers.asserts;

import business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObject;
import business_objects.db.payment_gate.payment_details.PaymentDetailsObject;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import business_objects.db.payment_gate.payment_rule_executions.PaymentRuleExecutionsObject;
import helpers.data.DataHelper;

import java.util.List;
import java.util.UUID;

import static helpers.database.PaymentGateHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;

public class PaymentGateAssertsHelper {

    public static void assertPaymentEvent(DataHelper data, UUID paymentId, String type, String deliveryStatus)
            throws Exception {
        // Check payment event in PGS DB
        PaymentEventsObject paymentEvent = getPaymentEvent(data.clientHelper.getUcid());
        assert paymentEvent != null;
        assertThat("Assert payment event", paymentEvent.getPaymentId(), is(paymentId));
        assertThat("Assert payment event", paymentEvent.getCrmId(), is(data.transferToWaEvent.getTransferId().toString()));
        assertThat("Assert payment event", paymentEvent.getType(), is(type));
        assertThat("Assert payment event", paymentEvent.getFinalDecisionId(), is(nullValue()));
        assertThat("Assert payment event", paymentEvent.getUcid(), is(data.clientHelper.getUcid()));
        assertThat("Assert payment event", paymentEvent.getDateCreated(), is(notNullValue()));
        assertThat("Assert payment event", paymentEvent.getDateUpdated(), is(notNullValue()));
        assertThat("Assert payment event", paymentEvent.getDateDecided(), is(nullValue()));
        assertThat("Assert payment event", paymentEvent.getDeliveryStatus(), is(deliveryStatus));
    }

    public static void assertPaymentDetails(DataHelper data, UUID paymentId, String status) throws Exception {
        //check payment details in PGS DB
        PaymentDetailsObject paymentDetailsObject = getPaymentDetails(data.clientHelper.getUserId());
        assert paymentDetailsObject != null;
        assertThat("Assert payment details", paymentDetailsObject.getPaymentId(), is(paymentId));
        assertThat("Assert payment details", paymentDetailsObject.getBrand(), is(data.transferToWaEvent.getBrand().toLowerCase()));
        assertThat("Assert payment details", paymentDetailsObject.getRegulator(), is(data.transferToWaEvent.getRegulator()));
        assertThat("Assert payment details", paymentDetailsObject.getType(), is(data.transferToWaEvent.getType()));
        assertThat("Assert payment details", paymentDetailsObject.getClientId(), is(data.transferToWaEvent.getClientId().toString()));
        assertThat("Assert payment details", paymentDetailsObject.getMerchantOrderId(), is(data.transferToWaEvent.getMerchantOrderId()));
        assertThat("Assert payment details", paymentDetailsObject.getEventDate(), is(notNullValue()));
        assertThat("Assert payment details", paymentDetailsObject.getStatus(), is(status));
        assertThat("Assert payment details", paymentDetailsObject.getPlatform(), is(data.transferToWaEvent.getPlatform()));
        assertThat("Assert payment details", paymentDetailsObject.getSourceSystem(), is(notNullValue()));
        assertThat("Assert payment details", paymentDetailsObject.getSourceEnv(), is(notNullValue()));
        assertThat("Assert payment details", paymentDetailsObject.getDateCreated(), is(notNullValue()));
        assertThat("Assert payment details", paymentDetailsObject.getAmountUsd().toString(), containsString("1."));
        assertThat("Assert payment details", paymentDetailsObject.getPayload(), containsString("\"withdrawalAmountUSD\": 1"));
    }

    public static void assertPendingDecision(UUID paymentId) throws Exception {
        // check pending decision
        List<PaymentDecisionsObject> paymentDecisionsObject = getPaymentDecisionsByPaymentId(paymentId);
        assertThat("Verify amount of decisions in DB", paymentDecisionsObject.size(), is(1));
        assertThat("Verify decisions have right decision ", paymentDecisionsObject.getFirst().getPaymentId(), is(paymentId));
        assertThat("Verify decisions have right decision ", paymentDecisionsObject.getFirst().getDecisionType(), is("risk"));
        assertThat("Verify decisions have right decision ", paymentDecisionsObject.getFirst().getDecisionCode(), is(0));
        assertThat("Verify decisions have right decision ", paymentDecisionsObject.getFirst().getRejectionCode(), is(nullValue()));
        assertThat("Verify decisions have right decision ", paymentDecisionsObject.getFirst().getActor(), is("Rule engine"));
    }

    public static void assertRuleExecutions(UUID paymentId) throws Exception {
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString());
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRunId(), is(notNullValue()));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(notNullValue()));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleVersion(), is(notNullValue()));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(notNullValue()));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getDateCreated(), is(notNullValue()));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getDateUpdated(), is(notNullValue()));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getDateStarted(), is(notNullValue()));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getDateCompleted(), is(notNullValue()));
    }


}
