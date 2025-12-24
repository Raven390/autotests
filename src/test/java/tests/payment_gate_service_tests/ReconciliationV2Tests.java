package tests.payment_gate_service_tests;

import static business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObjectFactory.generatePaymentDecisionObject;
import static business_objects.db.payment_gate.payment_decisions_sent.PaymentDecisionSentPayloadMessageObjectFactory.generatePayload;
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
import static tests.TestBaseRule.sendCrmAcknowledgeToKafka;
import static utils.Constants.*;
import static utils.Utils.getRandomUuid;
import static utils.Utils.sleep;

import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntity;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntityFactory;
import business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObject;
import business_objects.db.payment_gate.payment_decisions_sent.PaymentDecisionsSentObject;
import business_objects.db.payment_gate.payment_details.PaymentDetailsObject;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import business_objects.kafka.CrmAcknowledgeEvent;
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

    @Test
    @AllureId("1933")
    @DisplayName(
            "Payment reconciliation v2 test 1. Resend if payment found and created more than 5 min ago and count < 5")
    void ReconciliationTest1() throws Exception {
        ClientHelper client1 = getRandomVantageClientAllFields();
        CrmTbWithdrawalEntity crmTbWithdrawalObject1 =
                CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client1);
        PaymentEventsObject paymentEventsObject1 = generatePaymentEventsObject(client1);
        PaymentDetailsObject paymentDetailsObject1 = generatePaymentDetailsObject(paymentEventsObject1, client1);
        PaymentDecisionsObject paymentDecisionsObject1 = generatePaymentDecisionObject(paymentEventsObject1);
        PaymentDecisionsSentObject paymentDecisionsSentObject1 = generatePaymentDecisionSentObject(
                paymentEventsObject1, generatePayload(client1, paymentDetailsObject1));
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

        sleep(35_000);

        PaymentDecisionsSentObject paymentDecisionSent = getPaymentDecisionSent(paymentDecisionsObject1.getPaymentId());
        assertThat("Assert paymentDecisionSent", paymentDecisionSent.getCount(), is(1));
        assertThat(
                "Assert paymentDecisionSent",
                paymentDecisionSent.getPayload(),
                containsString(paymentDetailsObject1.getMerchantOrderId()));

        List<WithdrawalApprovalsV2> withdrawalApprovalsV2 =
                getWithdrawalApprovalsV2FromKafka(paymentDetailsObject1.getMerchantOrderId());
        assertThat(
                withdrawalApprovalsV2.getFirst().toString(),
                is(containsString(paymentDetailsObject1.getMerchantOrderId())));
    }

    @Test
    @AllureId("1931")
    @DisplayName("Payment reconciliation v2 test 1. Mark as FAILED if payment found and count > 5")
    void ReconciliationTest2() throws Exception {
        ClientHelper client2 = getRandomVantageClientAllFields();
        CrmTbWithdrawalEntity crmTbWithdrawalObject2 =
                CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client2);
        PaymentEventsObject paymentEventsObject2 = generatePaymentEventsObject(client2);
        PaymentDetailsObject paymentDetailsObject2 = generatePaymentDetailsObject(paymentEventsObject2, client2);
        PaymentDecisionsObject paymentDecisionsObject2 = generatePaymentDecisionObject(paymentEventsObject2);
        PaymentDecisionsSentObject paymentDecisionsSentObject2 = generatePaymentDecisionSentObject(
                paymentEventsObject2, generatePayload(client2, paymentDetailsObject2));
        crmTbWithdrawalObject2.setStatus("21");
        paymentEventsObject2.setDeliveryStatus("PENDING");
        paymentEventsObject2.setCrmId(crmTbWithdrawalObject2.getTransferId().toString());
        paymentEventsObject2.setDateDecided(Timestamp.from(Instant.now().minusMillis(6 * 60 * 1000)));
        paymentDecisionsSentObject2.setDateSent(Timestamp.from(Instant.now().minusMillis(6 * 60 * 1000)));
        paymentDecisionsSentObject2.setCount(5);

        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, List.of(paymentEventsObject2));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, List.of(paymentDetailsObject2));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, List.of(paymentDecisionsObject2));
        insertObjectsToDb(
                DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_SENT_TABLE, List.of(paymentDecisionsSentObject2));

        sleep(35_000);

        PaymentEventsObject paymentEvent = getPaymentEvent(paymentDecisionsObject2.getPaymentId());
        Assertions.assertNotNull(paymentEvent);
        assertThat("Assert status", paymentEvent.getDeliveryStatus(), is("FAILED"));
        assertThat("Assert details", paymentEvent.getDetails(), is("Acknowledge not received"));
    }

    @Test
    @AllureId("1932")
    @DisplayName("Payment reconciliation v2 test 3. Do nothing if payment fresh")
    void ReconciliationTest3() throws Exception {
        ClientHelper client3 = getRandomVantageClientAllFields();
        CrmTbWithdrawalEntity crmTbWithdrawalObject3 =
                CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client3);
        PaymentEventsObject paymentEventsObject3 = generatePaymentEventsObject(client3);
        PaymentDetailsObject paymentDetailsObject3 = generatePaymentDetailsObject(paymentEventsObject3, client3);
        PaymentDecisionsObject paymentDecisionsObject3 = generatePaymentDecisionObject(paymentEventsObject3);
        PaymentDecisionsSentObject paymentDecisionsSentObject3 = generatePaymentDecisionSentObject(
                paymentEventsObject3, generatePayload(client3, paymentDetailsObject3));

        crmTbWithdrawalObject3.setStatus("21");
        paymentEventsObject3.setDeliveryStatus("PENDING");
        paymentEventsObject3.setCrmId(crmTbWithdrawalObject3.getTransferId().toString());
        paymentEventsObject3.setDateDecided(Timestamp.from(Instant.now()));
        paymentDecisionsSentObject3.setDateSent(Timestamp.from(Instant.now()));

        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, List.of(paymentEventsObject3));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, List.of(paymentDetailsObject3));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, List.of(paymentDecisionsObject3));
        insertObjectsToDb(
                DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_SENT_TABLE, List.of(paymentDecisionsSentObject3));

        sleep(35_000);

        PaymentDecisionsSentObject paymentDecisionSent = getPaymentDecisionSent(paymentDecisionsObject3.getPaymentId());
        Assertions.assertNotNull(paymentDecisionSent);
        assertThat("Assert paymentDecisionSent", paymentDecisionSent.getCount(), is(0));

        PaymentEventsObject paymentEvent = getPaymentEvent(paymentDecisionsObject3.getPaymentId());
        Assertions.assertNotNull(paymentEvent);
        assertThat("Assert status", paymentEvent.getDeliveryStatus(), is("PENDING"));
        assertThat("Assert details", paymentEvent.getDetails(), is(""));
    }

    @Test
    @AllureId("1933")
    @DisplayName("Payment reconciliation v2 test 4. Not withdrawal do not resend")
    void ReconciliationTest4() throws Exception {
        ClientHelper client4 = getRandomVantageClientAllFields();
        CrmTbWithdrawalEntity crmTbWithdrawalObject4 =
                CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client4);
        crmTbWithdrawalObject4.setStatus("21");
        PaymentEventsObject paymentEventsObject4 = generatePaymentEventsObject(client4);
        paymentEventsObject4.setType("deposit");
        paymentEventsObject4.setCrmId(crmTbWithdrawalObject4.getTransferId().toString());
        paymentEventsObject4.setDateDecided(Timestamp.from(Instant.now().minusMillis(6 * 60 * 1000)));
        paymentEventsObject4.setDeliveryStatus("PENDING");
        PaymentDetailsObject paymentDetailsObject4 = generatePaymentDetailsObject(paymentEventsObject4, client4);
        PaymentDecisionsObject paymentDecisionsObject4 = generatePaymentDecisionObject(paymentEventsObject4);
        paymentDecisionsObject4.setDecisionCode(1);
        PaymentDecisionsSentObject paymentDecisionsSentObject4 = generatePaymentDecisionSentObject(
                paymentEventsObject4, generatePayload(client4, paymentDetailsObject4));
        paymentDecisionsSentObject4.setDateSent(Timestamp.from(Instant.now().plusMillis(6 * 60 * 1000)));

        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, List.of(paymentEventsObject4));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, List.of(paymentDetailsObject4));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, List.of(paymentDecisionsObject4));
        insertObjectsToDb(
                DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_SENT_TABLE, List.of(paymentDecisionsSentObject4));

        sleep(35_000);

        PaymentEventsObject paymentEvent = getPaymentEvent(paymentDecisionsObject4.getPaymentId());
        Assertions.assertNotNull(paymentEvent);
        assertThat("Assert status", paymentEvent.getDeliveryStatus(), is("PENDING"));

        PaymentDecisionsSentObject paymentDecisionSent = getPaymentDecisionSent(paymentDecisionsObject4.getPaymentId());
        assertThat("Assert paymentDecisionSent", paymentDecisionSent, is(notNullValue()));

        List<WithdrawalApprovalsV2> withdrawalApprovalsV2 =
                getWithdrawalApprovalsV2FromKafka(String.valueOf(paymentEventsObject4.getCrmId()));
        assertThat(withdrawalApprovalsV2, is(empty()));
    }

    @Test
    @AllureId("1995")
    @DisplayName("Payment reconciliation v2 test 5. Not resend for empty payload")
    void ReconciliationTest5() throws Exception {
        ClientHelper client5 = getRandomVantageClientAllFields();
        CrmTbWithdrawalEntity crmTbWithdrawalObject5 =
                CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client5);
        PaymentEventsObject paymentEventsObject5 = generatePaymentEventsObject(client5);
        PaymentDetailsObject paymentDetailsObject5 = generatePaymentDetailsObject(paymentEventsObject5, client5);
        PaymentDecisionsObject paymentDecisionsObject5 = generatePaymentDecisionObject(paymentEventsObject5);
        PaymentDecisionsSentObject paymentDecisionsSentObject5 = generatePaymentDecisionSentObject(
                paymentEventsObject5, generatePayload(client5, paymentDetailsObject5));

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

        sleep(35_000);

        PaymentEventsObject paymentEvent = getPaymentEvent(paymentDecisionsObject5.getPaymentId());
        Assertions.assertNotNull(paymentEvent);
        assertThat("Assert status", paymentEvent.getDeliveryStatus(), is("PENDING"));

        List<WithdrawalApprovalsV2> withdrawalApprovalsV2 =
                getWithdrawalApprovalsV2FromKafka(String.valueOf(paymentEventsObject5.getCrmId()));
        assertThat(withdrawalApprovalsV2, is(empty()));
    }

    @Test
    @AllureId("1994")
    @DisplayName("Payment reconciliation v2 test 6. Not resend for final decision is null")
    void ReconciliationTest6() throws Exception {
        ClientHelper client6 = getRandomVantageClientAllFields();
        CrmTbWithdrawalEntity crmTbWithdrawalObject6 =
                CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client6);
        PaymentEventsObject paymentEventsObject6 = generatePaymentEventsObject(client6);
        PaymentDetailsObject paymentDetailsObject6 = generatePaymentDetailsObject(paymentEventsObject6, client6);
        PaymentDecisionsObject paymentDecisionsObject6 = generatePaymentDecisionObject(paymentEventsObject6);
        PaymentDecisionsSentObject paymentDecisionsSentObject6 = generatePaymentDecisionSentObject(
                paymentEventsObject6, generatePayload(client6, paymentDetailsObject6));

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

        sleep(35_000);

        PaymentEventsObject paymentEvent = getPaymentEvent(paymentDecisionsObject6.getPaymentId());
        Assertions.assertNotNull(paymentEvent);
        assertThat("Assert status", paymentEvent.getDeliveryStatus(), is("PENDING"));

        List<WithdrawalApprovalsV2> withdrawalApprovalsV2 =
                getWithdrawalApprovalsV2FromKafka(String.valueOf(paymentEventsObject6.getCrmId()));
        assertThat(withdrawalApprovalsV2, is(empty()));
    }

    @Test
    @AllureId("1996")
    @DisplayName(
            "Payment reconciliation v2 test 7. Not resend if payment is delivered AND acknowledge received and count<5")
    void ReconciliationTest7() throws Exception {
        ClientHelper client7 = getRandomVantageClientAllFields();
        CrmTbWithdrawalEntity crmTbWithdrawalObject7 =
                CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client7);
        PaymentEventsObject paymentEventsObject7 = generatePaymentEventsObject(client7);
        PaymentDetailsObject paymentDetailsObject7 = generatePaymentDetailsObject(paymentEventsObject7, client7);
        PaymentDecisionsObject paymentDecisionsObject7 = generatePaymentDecisionObject(paymentEventsObject7);
        PaymentDecisionsSentObject paymentDecisionsSentObject7 = generatePaymentDecisionSentObject(
                paymentEventsObject7, generatePayload(client7, paymentDetailsObject7));
        crmTbWithdrawalObject7.setStatus("21");
        paymentEventsObject7.setDeliveryStatus("PENDING");
        paymentDecisionsObject7.setDecisionCode(1);
        paymentEventsObject7.setCrmId(crmTbWithdrawalObject7.getTransferId().toString());
        paymentEventsObject7.setDateDecided(Timestamp.from(Instant.now().minusMillis(6 * 60 * 1000)));
        paymentDecisionsSentObject7.setDateSent(Timestamp.from(Instant.now().minusMillis(6 * 60 * 1000)));
        paymentDecisionsSentObject7.setCount(0);

        crmTbWithdrawalObject7.setStatus("21");
        paymentEventsObject7.setDeliveryStatus("PENDING");
        paymentDecisionsObject7.setDecisionCode(1);
        paymentEventsObject7.setCrmId(crmTbWithdrawalObject7.getTransferId().toString());
        paymentEventsObject7.setDateDecided(Timestamp.from(Instant.now().minusMillis(6 * 60 * 1000)));
        paymentDecisionsSentObject7.setDateSent(Timestamp.from(Instant.now().minusMillis(6 * 60 * 1000)));
        paymentDecisionsSentObject7.setCount(2);

        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, List.of(paymentEventsObject7));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, List.of(paymentDetailsObject7));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, List.of(paymentDecisionsObject7));
        insertObjectsToDb(
                DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_SENT_TABLE, List.of(paymentDecisionsSentObject7));

        CrmAcknowledgeEvent crmAcknowledgeEvent7 = new CrmAcknowledgeEvent();
        crmAcknowledgeEvent7.setBrand(client7.getBrand());
        crmAcknowledgeEvent7.setClientId(client7.getUserId().toString());
        crmAcknowledgeEvent7.setCorrelationId(getRandomUuid().toString());
        crmAcknowledgeEvent7.setId(getRandomUuid().toString());
        crmAcknowledgeEvent7.setMerchantOrderId(paymentDetailsObject7.getMerchantOrderId());
        crmAcknowledgeEvent7.setPaymentId(paymentEventsObject7.getPaymentId().toString());
        crmAcknowledgeEvent7.setRegulator(client7.getRegulator());
        crmAcknowledgeEvent7.setSchemaVersion("2.0");
        crmAcknowledgeEvent7.setSrcAppId("AUBRC041001PWM000000001");
        crmAcknowledgeEvent7.setSubtype("acknowledge");
        crmAcknowledgeEvent7.setTimestamp(Instant.now().toString());
        crmAcknowledgeEvent7.setType("withdrawal");
        sendCrmAcknowledgeToKafka(crmAcknowledgeEvent7);

        sleep(35_000);

        PaymentEventsObject paymentEvent = getPaymentEvent(paymentDecisionsObject7.getPaymentId());
        Assertions.assertNotNull(paymentEvent);
        assertThat("Assert status", paymentEvent.getDeliveryStatus(), is("DELIVERED"));

        List<WithdrawalApprovalsV2> withdrawalApprovalsV2 =
                getWithdrawalApprovalsV2FromKafka(String.valueOf(paymentEventsObject7.getCrmId()));
        assertThat(withdrawalApprovalsV2, is(empty()));
    }
}
