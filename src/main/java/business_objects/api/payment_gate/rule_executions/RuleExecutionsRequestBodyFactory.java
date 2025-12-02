package business_objects.api.payment_gate.rule_executions;

import business_objects.db.payment_gate.payment_events.PaymentEventsObject;

import java.sql.Timestamp;
import java.time.Instant;

import static utils.Utils.getRandomIntPositive;

public class RuleExecutionsRequestBodyFactory {

    public static PostRuleExecutionsBody generatePostRuleExecutionsBody(PaymentEventsObject event) {
        PostRuleExecutionsBody body = new PostRuleExecutionsBody();

        body.setRunId(getRandomIntPositive().toString());
        body.setPaymentId(event.getPaymentId());
        body.setRuleId(1);
        body.setRuleVersion("1.0." + getRandomIntPositive());
        body.setRuleEndId("2");
        body.setDateCreated(Instant.now().toString());
        body.setDateUpdated(Instant.now().toString());
        body.setDateStarted(Instant.now().toString());
        body.setDateCompleted(Instant.now().toString());
        return body;
    }

    public static PutRuleExecutionsBody generatePutRuleExecutionsBody(PaymentEventsObject event) {
        PutRuleExecutionsBody body = new PutRuleExecutionsBody();

        body.setRunId(getRandomIntPositive().toString());
        body.setPaymentId(event.getPaymentId());
        body.setRuleId(1);
        body.setRuleVersion("1.0." + getRandomIntPositive());
        body.setRuleEndId("2");
        body.setStartedAt(Timestamp.from(Instant.now()));
        body.setCompletedAt(Timestamp.from(Instant.now().plusSeconds(2)));
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
