package tests.payment_gate_service_tests;

import static business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObjectFactory.generatePaymentDecisionObject;
import static business_objects.db.payment_gate.payment_decisions_sent.PaymentDecisionsSentObjectFactory.generatePaymentDecisionSentObject;
import static business_objects.db.payment_gate.payment_details.PaymentDetailsObjectFactory.generatePaymentDetailsObject;
import static business_objects.db.payment_gate.payment_events.PaymentEventsObjectFactory.generatePaymentEventsObject;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.DbHelper.*;
import static helpers.database.PaymentGateHelper.getPaymentDecisionSent;
import static helpers.database.PaymentGateHelper.getPaymentEvent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static tests.TestBaseRule.getWithdrawalApprovalsV2FromKafka;
import static utils.Constants.*;

import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntity;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntityFactory;
import business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObject;
import business_objects.db.payment_gate.payment_decisions_sent.PaymentDecisionSentPayloadMessageObject;
import business_objects.db.payment_gate.payment_decisions_sent.PaymentDecisionsSentObject;
import business_objects.db.payment_gate.payment_details.PaymentDetailsObject;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import business_objects.kafka.restriction_events.WithdrawalApprovalsV2;
import helpers.data.ClientHelper;
import helpers.database.DbName;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

@Feature(FEATURE_PAYMENT_GATE)
@Story(STORY_PAYMENT_GATE_RECONCILIATION)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_PAYMENT_GATE_TESTS)
class ReconciliationV2Tests extends TestBaseApi {

    private static ClientHelper client1;
    private static CrmTbWithdrawalEntity crmTbWithdrawalObject1;
    private static PaymentEventsObject paymentEventsObject1;
    private static PaymentDetailsObject paymentDetailsObject1;
    private static PaymentDecisionsObject paymentDecisionsObject1;
    private static PaymentDecisionsSentObject paymentDecisionsSentObject1;
    private static PaymentDecisionSentPayloadMessageObject payload1;

    private static ClientHelper client2;
    private static CrmTbWithdrawalEntity crmTbWithdrawalObject2;
    private static PaymentEventsObject paymentEventsObject2;
    private static PaymentDetailsObject paymentDetailsObject2;
    private static PaymentDecisionsObject paymentDecisionsObject2;
    private static PaymentDecisionsSentObject paymentDecisionsSentObject2;
    private static PaymentDecisionSentPayloadMessageObject payload2;

    private static ClientHelper client3;
    private static CrmTbWithdrawalEntity crmTbWithdrawalObject3;
    private static PaymentEventsObject paymentEventsObject3;
    private static PaymentDetailsObject paymentDetailsObject3;
    private static PaymentDecisionsObject paymentDecisionsObject3;
    private static PaymentDecisionsSentObject paymentDecisionsSentObject3;
    private static PaymentDecisionSentPayloadMessageObject payload3;

    private static ClientHelper client4;
    private static CrmTbWithdrawalEntity crmTbWithdrawalObject4;
    private static PaymentEventsObject paymentEventsObject4;
    private static PaymentDetailsObject paymentDetailsObject4;
    private static PaymentDecisionsObject paymentDecisionsObject4;
    private static PaymentDecisionsSentObject paymentDecisionsSentObject4;
    private static PaymentDecisionSentPayloadMessageObject payload4;

    private static ClientHelper client5;
    private static CrmTbWithdrawalEntity crmTbWithdrawalObject5;
    private static PaymentEventsObject paymentEventsObject5;
    private static PaymentDetailsObject paymentDetailsObject5;
    private static PaymentDecisionsObject paymentDecisionsObject5;
    private static PaymentDecisionsSentObject paymentDecisionsSentObject5;
    private static PaymentDecisionSentPayloadMessageObject payload5;

    private static ClientHelper client6;
    private static CrmTbWithdrawalEntity crmTbWithdrawalObject6;
    private static PaymentEventsObject paymentEventsObject6;
    private static PaymentDetailsObject paymentDetailsObject6;
    private static PaymentDecisionsObject paymentDecisionsObject6;
    private static PaymentDecisionsSentObject paymentDecisionsSentObject6;
    private static PaymentDecisionSentPayloadMessageObject payload6;

    private static ClientHelper client7;
    private static CrmTbWithdrawalEntity crmTbWithdrawalObject7;
    private static PaymentEventsObject paymentEventsObject7;
    private static PaymentDetailsObject paymentDetailsObject7;
    private static PaymentDecisionsObject paymentDecisionsObject7;
    private static PaymentDecisionsSentObject paymentDecisionsSentObject7;
    private static PaymentDecisionSentPayloadMessageObject payload7;

    @BeforeAll
    static void setupData() throws Exception {

        client1 = getRandomVantageClientAllFields();
        crmTbWithdrawalObject1 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client1);
        paymentEventsObject1 = generatePaymentEventsObject(client1);
        paymentDetailsObject1 = generatePaymentDetailsObject(paymentEventsObject1, client1);
        paymentDecisionsObject1 = generatePaymentDecisionObject(paymentEventsObject1);
        paymentDecisionsSentObject1 = generatePaymentDecisionSentObject(paymentEventsObject1, payload1);

        client2 = getRandomVantageClientAllFields();
        crmTbWithdrawalObject2 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client2);
        paymentEventsObject2 = generatePaymentEventsObject(client2);
        paymentDetailsObject2 = generatePaymentDetailsObject(paymentEventsObject2, client2);
        paymentDecisionsObject2 = generatePaymentDecisionObject(paymentEventsObject2);
        paymentDecisionsSentObject2 = generatePaymentDecisionSentObject(paymentEventsObject2, payload2);

        client3 = getRandomVantageClientAllFields();
        crmTbWithdrawalObject3 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client3);
        paymentEventsObject3 = generatePaymentEventsObject(client3);
        paymentDetailsObject3 = generatePaymentDetailsObject(paymentEventsObject3, client3);
        paymentDecisionsObject3 = generatePaymentDecisionObject(paymentEventsObject3);
        paymentDecisionsSentObject3 = generatePaymentDecisionSentObject(paymentEventsObject3, payload3);

        client4 = getRandomVantageClientAllFields();
        crmTbWithdrawalObject4 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client4);
        paymentEventsObject4 = generatePaymentEventsObject(client4);
        paymentDetailsObject4 = generatePaymentDetailsObject(paymentEventsObject4, client4);
        paymentDecisionsObject4 = generatePaymentDecisionObject(paymentEventsObject4);
        paymentDecisionsSentObject4 = generatePaymentDecisionSentObject(paymentEventsObject4, payload4);

        client5 = getRandomVantageClientAllFields();
        crmTbWithdrawalObject5 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client5);
        paymentEventsObject5 = generatePaymentEventsObject(client5);
        paymentDetailsObject5 = generatePaymentDetailsObject(paymentEventsObject5, client5);
        paymentDecisionsObject5 = generatePaymentDecisionObject(paymentEventsObject5);
        paymentDecisionsSentObject5 = generatePaymentDecisionSentObject(paymentEventsObject5, payload5);

        client6 = getRandomVantageClientAllFields();
        crmTbWithdrawalObject6 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client6);
        paymentEventsObject6 = generatePaymentEventsObject(client6);
        paymentDetailsObject6 = generatePaymentDetailsObject(paymentEventsObject6, client6);
        paymentDecisionsObject6 = generatePaymentDecisionObject(paymentEventsObject6);
        paymentDecisionsSentObject6 = generatePaymentDecisionSentObject(paymentEventsObject6, payload6);

        client7 = getRandomVantageClientAllFields();
        crmTbWithdrawalObject7 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client7);
        paymentEventsObject7 = generatePaymentEventsObject(client7);
        paymentDetailsObject7 = generatePaymentDetailsObject(paymentEventsObject7, client7);
        paymentDecisionsObject7 = generatePaymentDecisionObject(paymentEventsObject7);
        paymentDecisionsSentObject7 = generatePaymentDecisionSentObject(paymentEventsObject7, payload7);
    }

    //    @AfterAll
    //    static void deleteData() throws Exception {
    //        cleanCrmTbWithdrawalTableByUcid(client1.getUcid(), client1.getUcid());
    //
    //    }

    @Test
    @AllureId("1933")
    @DisplayName("Payment reconciliation v2 test 1. Resend if payment found and created than 5 min ago and count < 5")
    void ReconciliationTest1() throws Exception {
        crmTbWithdrawalObject1.setStatus("21");
        paymentEventsObject1.setDeliveryStatus("PENDING");
        paymentDecisionsObject1.setDecisionCode(1);
        paymentEventsObject1.setCrmId(crmTbWithdrawalObject1.getTransferId().toString());
        paymentEventsObject1.setDateDecided(Timestamp.from(Instant.now().minusMillis(6 * 60 * 1000)));
        paymentDecisionsSentObject1.setDateSent(Timestamp.from(Instant.now().minusMillis(6 * 60 * 1000)));
        paymentDecisionsSentObject1.setCount(0);

        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, List.of(paymentEventsObject1));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, List.of(paymentDetailsObject1));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, List.of(paymentDecisionsObject1));
        insertObjectsToDb(
                DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_SENT_TABLE, List.of(paymentDecisionsSentObject1));

        Thread.sleep(310_000);

        PaymentDecisionsSentObject paymentDecisionSent = getPaymentDecisionSent(paymentDecisionsObject1.getPaymentId());
        assertThat("Assert paymentDecisionSent", paymentDecisionSent.getCount(), is(1));
        assertThat(
                "Assert paymentDecisionSent",
                paymentDecisionSent.getPayload(),
                containsString(paymentDetailsObject1.getMerchantOrderId()));

        List<WithdrawalApprovalsV2> WithdrawalApprovalsV2 =
                getWithdrawalApprovalsV2FromKafka(String.valueOf(paymentEventsObject6.getCrmId()));
        assertThat(
                WithdrawalApprovalsV2.getFirst().toString(),
                is(containsString(paymentDetailsObject1.getMerchantOrderId())));
    }

    @Test
    @AllureId("1931")
    @DisplayName("Payment reconciliation v2 test 1. Mark as FAILED if payment found and count > 5")
    void ReconciliationTest2() throws Exception {
        crmTbWithdrawalObject1.setStatus("21");
        paymentEventsObject1.setDeliveryStatus("PENDING");
        paymentEventsObject2.setCrmId(crmTbWithdrawalObject2.getTransferId().toString());
        paymentEventsObject2.setDateDecided(Timestamp.from(Instant.now().minusMillis(6 * 60 * 1000)));
        paymentDecisionsSentObject2.setDateSent(Timestamp.from(Instant.now().minusMillis(6 * 60 * 1000)));
        paymentDecisionsSentObject2.setCount(5);

        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, List.of(paymentEventsObject2));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, List.of(paymentDetailsObject2));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, List.of(paymentDecisionsObject2));
        insertObjectsToDb(
                DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_SENT_TABLE, List.of(paymentDecisionsSentObject2));

        Thread.sleep(305_000);

        PaymentEventsObject paymentEvent = getPaymentEvent(paymentDecisionsObject2.getPaymentId());
        assertThat("Assert status", paymentEvent.getDeliveryStatus(), is("FAILED"));
        assertThat("Assert details", paymentEvent.getDetails(), is("Acknowledge not received"));
    }

    @Test
    @AllureId("1932")
    @DisplayName("Payment reconciliation v2 test 3. Do nothing if payment fresh")
    void ReconciliationTest3() throws Exception {
        crmTbWithdrawalObject3.setStatus("21");
        paymentEventsObject3.setDeliveryStatus("PENDING");
        paymentEventsObject3.setCrmId(crmTbWithdrawalObject3.getTransferId().toString());
        paymentEventsObject3.setDateDecided(Timestamp.from(Instant.now().minusMillis(4 * 60 * 1000)));
        paymentDecisionsSentObject3.setDateSent(Timestamp.from(Instant.now().plusMillis(4 * 60 * 1000)));

        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, List.of(paymentEventsObject3));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, List.of(paymentDetailsObject3));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, List.of(paymentDecisionsObject3));
        insertObjectsToDb(
                DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_SENT_TABLE, List.of(paymentDecisionsSentObject3));

        Thread.sleep(305_000);

        PaymentDecisionsSentObject paymentDecisionSent = getPaymentDecisionSent(paymentDecisionsObject3.getPaymentId());
        assertThat("Assert paymentDecisionSent", paymentDecisionSent.getCount(), is(0));

        PaymentEventsObject paymentEvent = getPaymentEvent(paymentDecisionsObject3.getPaymentId());
        assertThat("Assert status", paymentEvent.getDeliveryStatus(), is("PENDING"));
        assertThat("Assert details", paymentEvent.getDetails(), is(""));
    }

    @Test
    @AllureId("1933")
    @DisplayName("Payment reconciliation v2 test 4. Not withdrawal do not resend")
    void ReconciliationTest4() throws Exception {
        crmTbWithdrawalObject4.setStatus("21");
        paymentEventsObject4.setDeliveryStatus("PENDING");
        paymentDecisionsObject4.setDecisionCode(1);
        paymentEventsObject4.setType("deposit");
        paymentEventsObject4.setCrmId(crmTbWithdrawalObject4.getTransferId().toString());
        paymentEventsObject4.setDateDecided(Timestamp.from(Instant.now().minusMillis(11 * 60 * 1000)));
        paymentDecisionsSentObject4.setDateSent(Timestamp.from(Instant.now().plusMillis(6 * 60 * 1000)));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, List.of(paymentEventsObject4));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, List.of(paymentDetailsObject4));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, List.of(paymentDecisionsObject4));

        Thread.sleep(310_000);

        PaymentEventsObject paymentEvent = getPaymentEvent(paymentDecisionsObject4.getPaymentId());
        assertThat("Assert status", paymentEvent.getDeliveryStatus(), is("PENDING"));

        PaymentDecisionsSentObject paymentDecisionSent = getPaymentDecisionSent(paymentDecisionsObject4.getPaymentId());
        assertThat("Assert paymentDecisionSent", paymentDecisionSent.getCount(), is(0));

        List<WithdrawalApprovalsV2> WithdrawalApprovalsV2 =
                getWithdrawalApprovalsV2FromKafka(String.valueOf(paymentEventsObject4.getCrmId()));
        assertThat(WithdrawalApprovalsV2, is(empty()));
    }

    @Test
    @AllureId("")
    @DisplayName("Payment reconciliation v2 test 5. Not resend for empty payload")
    void ReconciliationTest5() throws Exception {
        crmTbWithdrawalObject5.setStatus("21");
        paymentEventsObject5.setDeliveryStatus("PENDING");
        paymentDecisionsObject5.setDecisionCode(1);
        paymentEventsObject5.setCrmId(crmTbWithdrawalObject5.getTransferId().toString());
        paymentEventsObject5.setDateDecided(Timestamp.from(Instant.now().minusMillis(11 * 60 * 1000)));
        paymentDecisionsSentObject5.setPayload("{}");
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, List.of(paymentEventsObject5));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, List.of(paymentDetailsObject5));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, List.of(paymentDecisionsObject5));
        insertObjectsToDb(
                DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_SENT_TABLE, List.of(paymentDecisionsSentObject5));

        Thread.sleep(310_000);

        PaymentEventsObject paymentEvent = getPaymentEvent(paymentDecisionsObject5.getPaymentId());
        assertThat("Assert status", paymentEvent.getDeliveryStatus(), is("PENDING"));

        PaymentDecisionsSentObject paymentDecisionSent = getPaymentDecisionSent(paymentDecisionsObject5.getPaymentId());
        assertThat("Assert paymentDecisionSent", paymentDecisionSent.getCount(), is(0));

        List<WithdrawalApprovalsV2> WithdrawalApprovalsV2 =
                getWithdrawalApprovalsV2FromKafka(String.valueOf(paymentEventsObject5.getCrmId()));
        assertThat(WithdrawalApprovalsV2, is(empty()));
    }

    @Test
    @AllureId("")
    @DisplayName("Payment reconciliation v2 test 6. Not resend for final decision is null")
    void ReconciliationTest6() throws Exception {
        crmTbWithdrawalObject6.setStatus("21");
        paymentEventsObject6.setDeliveryStatus("PENDING");
        paymentDecisionsObject6.setDecisionCode(1);
        paymentEventsObject6.setCrmId(crmTbWithdrawalObject6.getTransferId().toString());
        paymentEventsObject6.setDateDecided(Timestamp.from(Instant.now().minusMillis(6 * 60 * 1000)));
        paymentEventsObject6.setFinalDecisionId(null);
        paymentDecisionsSentObject6.setDateSent(Timestamp.from(Instant.now().minusMillis(6 * 60 * 1000)));
        paymentDecisionsSentObject6.setCount(0);

        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, List.of(paymentEventsObject6));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, List.of(paymentDetailsObject6));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, List.of(paymentDecisionsObject6));
        insertObjectsToDb(
                DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_SENT_TABLE, List.of(paymentDecisionsSentObject6));

        Thread.sleep(310_000);

        PaymentEventsObject paymentEvent = getPaymentEvent(paymentDecisionsObject6.getPaymentId());
        assertThat("Assert status", paymentEvent.getDeliveryStatus(), is("PENDING"));

        PaymentDecisionsSentObject paymentDecisionSent = getPaymentDecisionSent(paymentDecisionsObject6.getPaymentId());
        assertThat("Assert paymentDecisionSent", paymentDecisionSent.getCount(), is(0));

        List<WithdrawalApprovalsV2> WithdrawalApprovalsV2 =
                getWithdrawalApprovalsV2FromKafka(String.valueOf(paymentEventsObject6.getCrmId()));
        assertThat(WithdrawalApprovalsV2, is(empty()));
    }

    @Test
    @AllureId("")
    @DisplayName("Payment reconciliation v2 test 7. Not resend for fresh payment")
    void ReconciliationTest7() throws Exception {
        crmTbWithdrawalObject7.setStatus("21");
        paymentEventsObject7.setDeliveryStatus("PENDING");
        paymentDecisionsObject7.setDecisionCode(1);
        paymentEventsObject7.setCrmId(crmTbWithdrawalObject7.getTransferId().toString());
        paymentEventsObject7.setDateDecided(Timestamp.from(Instant.now().minusMillis(6 * 60 * 1000)));
        paymentDecisionsSentObject7.setDateSent(Timestamp.from(Instant.now().minusMillis(6 * 60 * 1000)));
        paymentDecisionsSentObject7.setCount(0);
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, List.of(paymentEventsObject7));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, List.of(paymentDetailsObject7));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, List.of(paymentDecisionsObject7));
        insertObjectsToDb(
                DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_SENT_TABLE, List.of(paymentDecisionsSentObject7));

        Thread.sleep(310_000);

        PaymentEventsObject paymentEvent = getPaymentEvent(paymentDecisionsObject7.getPaymentId());
        assertThat("Assert status", paymentEvent.getDeliveryStatus(), is("PENDING"));

        PaymentDecisionsSentObject paymentDecisionSent = getPaymentDecisionSent(paymentDecisionsObject7.getPaymentId());
        assertThat("Assert paymentDecisionSent", paymentDecisionSent.getCount(), is(0));

        List<WithdrawalApprovalsV2> WithdrawalApprovalsV2 =
                getWithdrawalApprovalsV2FromKafka(String.valueOf(paymentEventsObject7.getCrmId()));
        assertThat(WithdrawalApprovalsV2, is(empty()));
    }
}
