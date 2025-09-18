package business_objects.api.payment_gate.rule_executions;

import business_objects.db.payment_gate.payment_events.PaymentEventsObject;

import java.time.Instant;

import static utils.Utils.getRandomIntPositive;

public class RuleExecutionsRequestBodyFactory {

    public static PostRuleExecutionsBody generatePostRuleExecutionsBody(PaymentEventsObject event) {
        PostRuleExecutionsBody body = new PostRuleExecutionsBody();

        body.setRunId(String.valueOf(System.currentTimeMillis()));
        body.setPaymentId(event.getPaymentId());
        body.setRuleId(101);
        body.setRuleVersion("1.0." + getRandomIntPositive());
        body.setRuleEndId("2");
        body.setStartedAt(Instant.now().toString());
        body.setCompletedAt(Instant.now().plusSeconds(2).toString());
        return body;
    }

    public static PutRuleExecutionsBody generatePutRuleExecutionsBody(PaymentEventsObject event) {
        PutRuleExecutionsBody body = new PutRuleExecutionsBody();

        body.setRunId(String.valueOf(System.currentTimeMillis()));
        body.setPaymentId(event.getPaymentId());
        body.setRuleId(101);
        body.setRuleVersion("1.0." + getRandomIntPositive());
        body.setRuleEndId("2");
        body.setStartedAt(Instant.now().toString());
        body.setCompletedAt(Instant.now().plusSeconds(2).toString());
        return body;
    }

    public static PutRuleExecutionsBody generatePutRuleExecutionsBody(PaymentEventsObject event, boolean empty) {
        PutRuleExecutionsBody body = new PutRuleExecutionsBody();
        body.setPaymentId(event.getPaymentId());
        return body;
    }

    public static PostRuleExecutionsBody generatePostRuleExecutionsBody(PaymentEventsObject event, boolean empty) {
        PostRuleExecutionsBody body = new PostRuleExecutionsBody();
        body.setPaymentId(event.getPaymentId());
        return body;
    }

}
