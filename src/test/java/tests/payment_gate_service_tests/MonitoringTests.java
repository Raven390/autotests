package tests.payment_gate_service_tests;

import static business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObjectFactory.generatePaymentDecisionObject;
import static business_objects.db.payment_gate.payment_details.PaymentDetailsObjectFactory.generatePaymentDetailsObject;
import static business_objects.db.payment_gate.payment_events.PaymentEventsObjectFactory.generatePaymentEventsObject;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.CleanTableHelper.cleanCrmTbWithdrawalTableByUcid;
import static helpers.database.DbHelper.insertObjectsToDb;
import static helpers.database.PaymentGateHelper.getPaymentEvent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static tests.TestBaseRule.sendCrmAcknowledgeToKafka;
import static utils.Constants.*;
import static utils.Utils.getRandomUuid;

import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntity;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntityFactory;
import business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObject;
import business_objects.db.payment_gate.payment_details.PaymentDetailsObject;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import business_objects.kafka.CrmAcknowledgeEvent;
import helpers.data.ClientHelper;
import helpers.database.DbName;
import helpers.database.PaymentGateHelper;
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
class MonitoringTests extends TestBaseApi {

    private static ClientHelper client1;
    private static CrmTbWithdrawalEntity crmTbWithdrawalObject1;
    private static PaymentEventsObject paymentEventsObject1;
    private static PaymentDetailsObject paymentDetailsObject1;
    private static PaymentDecisionsObject paymentDecisionsObject1;

    private static ClientHelper client2;
    private static CrmTbWithdrawalEntity crmTbWithdrawalObject2;
    private static PaymentEventsObject paymentEventsObject2;
    private static PaymentDetailsObject paymentDetailsObject2;
    private static PaymentDecisionsObject paymentDecisionsObject2;

    private static ClientHelper client3;
    private static CrmTbWithdrawalEntity crmTbWithdrawalObject3;
    private static PaymentEventsObject paymentEventsObject3;
    private static PaymentDetailsObject paymentDetailsObject3;
    private static PaymentDecisionsObject paymentDecisionsObject3;

    private static ClientHelper client4;
    private static CrmTbWithdrawalEntity crmTbWithdrawalObject4;
    private static PaymentEventsObject paymentEventsObject4;
    private static PaymentDetailsObject paymentDetailsObject4;
    private static PaymentDecisionsObject paymentDecisionsObject4;

    private static ClientHelper client5;
    private static CrmTbWithdrawalEntity crmTbWithdrawalObject5;
    private static PaymentEventsObject paymentEventsObject5;
    private static PaymentDetailsObject paymentDetailsObject5;
    private static PaymentDecisionsObject paymentDecisionsObject5;

    private static ClientHelper client6;
    private static CrmTbWithdrawalEntity crmTbWithdrawalObject6;
    private static PaymentEventsObject paymentEventsObject6;
    private static PaymentDetailsObject paymentDetailsObject6;
    private static PaymentDecisionsObject paymentDecisionsObject6;

    private static ClientHelper client7;
    private static CrmTbWithdrawalEntity crmTbWithdrawalObject7;
    private static PaymentEventsObject paymentEventsObject7;
    private static PaymentDetailsObject paymentDetailsObject7;
    private static PaymentDecisionsObject paymentDecisionsObject7;

    private static ClientHelper client8;
    private static CrmTbWithdrawalEntity crmTbWithdrawalObject8;
    private static PaymentEventsObject paymentEventsObject8;
    private static PaymentDetailsObject paymentDetailsObject8;

    private static ClientHelper client9;
    private static CrmTbWithdrawalEntity crmTbWithdrawalObject9;
    private static PaymentEventsObject paymentEventsObject9;
    private static PaymentDetailsObject paymentDetailsObject9;

    @BeforeAll
    static void setupData() throws Exception {

        client1 = getRandomVantageClientAllFields();
        crmTbWithdrawalObject1 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client1);
        paymentEventsObject1 = generatePaymentEventsObject(client1);
        paymentEventsObject1.setCrmId(crmTbWithdrawalObject1.getTransferId().toString());
        paymentEventsObject1.setDateDecided(Timestamp.from(Instant.now().minusMillis(11 * 60 * 1000)));
        paymentDetailsObject1 = generatePaymentDetailsObject(paymentEventsObject1, client1);
        paymentDecisionsObject1 = generatePaymentDecisionObject(paymentEventsObject1);

        client2 = getRandomVantageClientAllFields();
        crmTbWithdrawalObject2 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client2);
        paymentEventsObject2 = generatePaymentEventsObject(client2);
        paymentEventsObject2.setCrmId(crmTbWithdrawalObject2.getTransferId().toString());
        paymentEventsObject2.setDateDecided(Timestamp.from(Instant.now().minusMillis(11 * 60 * 1000)));
        paymentDetailsObject2 = generatePaymentDetailsObject(paymentEventsObject2, client2);
        paymentDecisionsObject2 = generatePaymentDecisionObject(paymentEventsObject2);

        client3 = getRandomVantageClientAllFields();
        crmTbWithdrawalObject3 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client3);
        paymentEventsObject3 = generatePaymentEventsObject(client3);
        paymentEventsObject3.setCrmId(crmTbWithdrawalObject3.getTransferId().toString());
        paymentEventsObject3.setDateDecided(Timestamp.from(Instant.now().minusMillis(11 * 60 * 1000)));
        paymentDetailsObject3 = generatePaymentDetailsObject(paymentEventsObject3, client3);
        paymentDecisionsObject3 = generatePaymentDecisionObject(paymentEventsObject3);

        client4 = getRandomVantageClientAllFields();
        crmTbWithdrawalObject4 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client4);
        paymentEventsObject4 = generatePaymentEventsObject(client4);
        paymentEventsObject4.setCrmId(crmTbWithdrawalObject4.getTransferId().toString());
        paymentEventsObject4.setDateDecided(Timestamp.from(Instant.now().minusMillis(11 * 60 * 1000)));
        paymentDetailsObject4 = generatePaymentDetailsObject(paymentEventsObject4, client4);
        paymentDecisionsObject4 = generatePaymentDecisionObject(paymentEventsObject4);

        client5 = getRandomVantageClientAllFields();
        crmTbWithdrawalObject5 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client5);
        paymentEventsObject5 = generatePaymentEventsObject(client5);
        paymentEventsObject5.setCrmId(crmTbWithdrawalObject5.getTransferId().toString());
        paymentEventsObject5.setDateDecided(Timestamp.from(Instant.now().minusMillis(11 * 60 * 1000)));
        paymentDetailsObject5 = generatePaymentDetailsObject(paymentEventsObject5, client5);
        paymentDecisionsObject5 = generatePaymentDecisionObject(paymentEventsObject5);

        client6 = getRandomVantageClientAllFields();
        crmTbWithdrawalObject6 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client6);
        paymentEventsObject6 = generatePaymentEventsObject(client6);
        paymentEventsObject6.setCrmId(crmTbWithdrawalObject6.getTransferId().toString());
        paymentEventsObject6.setDateDecided(Timestamp.from(Instant.now().minusMillis(11 * 60 * 1000)));
        paymentDetailsObject6 = generatePaymentDetailsObject(paymentEventsObject6, client6);
        paymentDecisionsObject6 = generatePaymentDecisionObject(paymentEventsObject6);

        client7 = getRandomVantageClientAllFields();
        crmTbWithdrawalObject7 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client7);
        paymentEventsObject7 = generatePaymentEventsObject(client7);
        paymentEventsObject7.setCrmId(crmTbWithdrawalObject7.getTransferId().toString());
        paymentEventsObject7.setDateDecided(Timestamp.from(Instant.now().minusMillis(11 * 60 * 1000)));
        paymentDetailsObject7 = generatePaymentDetailsObject(paymentEventsObject7, client7);
        paymentDecisionsObject7 = generatePaymentDecisionObject(paymentEventsObject7);

        client8 = getRandomVantageClientAllFields();
        crmTbWithdrawalObject8 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client8);
        paymentEventsObject8 = generatePaymentEventsObject(client8);
        paymentEventsObject8.setCrmId(crmTbWithdrawalObject8.getTransferId().toString());
        paymentEventsObject8.setDateCreated(Timestamp.from(Instant.now().minusMillis(11 * 60 * 1000)));
        paymentDetailsObject8 = generatePaymentDetailsObject(paymentEventsObject8, client8);

        client9 = getRandomVantageClientAllFields();
        crmTbWithdrawalObject9 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client9);
        paymentEventsObject9 = generatePaymentEventsObject(client9);
        paymentEventsObject9.setCrmId(crmTbWithdrawalObject9.getTransferId().toString());
        paymentEventsObject9.setDateCreated(Timestamp.from(Instant.now().minusMillis(11 * 60 * 1000)));
        paymentDetailsObject9 = generatePaymentDetailsObject(paymentEventsObject9, client9);
    }

    @AfterAll
    static void deleteData() throws Exception {
        cleanCrmTbWithdrawalTableByUcid(client1.getUcid(), client1.getUcid());
        cleanCrmTbWithdrawalTableByUcid(client2.getUcid(), client2.getUcid());
        cleanCrmTbWithdrawalTableByUcid(client3.getUcid(), client3.getUcid());
        cleanCrmTbWithdrawalTableByUcid(client4.getUcid(), client4.getUcid());
        cleanCrmTbWithdrawalTableByUcid(client5.getUcid(), client5.getUcid());
        cleanCrmTbWithdrawalTableByUcid(client6.getUcid(), client6.getUcid());
        cleanCrmTbWithdrawalTableByUcid(client7.getUcid(), client7.getUcid());
    }

    @Test
    @AllureId("1575")
    @DisplayName("Payment reconciliation test 1. Not found in CH -> DELIVERED")
    void ReconciliationTest1() throws Exception {
        crmTbWithdrawalObject1.setStatus("Test");
        paymentEventsObject1.setDeliveryStatus("PENDING");
        paymentDecisionsObject1.setDecisionCode(1);
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, List.of(paymentEventsObject1));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, List.of(paymentDetailsObject1));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, List.of(paymentDecisionsObject1));
        Thread.sleep(130_000);

        PaymentEventsObject event = PaymentGateHelper.getPaymentEvent(client1.getUcid());
        assertThat("Check status", event.getDeliveryStatus(), is("FAILED"));
        assertThat("Check status", event.getDetails(), is("Not found in CH"));

        insertObjectsToDb(DbName.CLICKHOUSE, CLICKHOUSE_CRM_TB_WITHDRAWAL, List.of(crmTbWithdrawalObject1));
        Thread.sleep(130_000);

        PaymentEventsObject event2 = PaymentGateHelper.getPaymentEvent(client1.getUcid());
        assertThat("Check status", event2.getDeliveryStatus(), is("DELIVERED"));
        assertThat("Check status", event2.getDetails(), is(nullValue()));
    }

    @Test
    @DisplayName("Payment reconciliation test 2. Status = Risk audit and id != 21 -> FAILED")
    void ReconciliationTest2() throws Exception {
        crmTbWithdrawalObject2.setStatus("Risk Audit");
        crmTbWithdrawalObject2.setStatusId(22);
        paymentEventsObject2.setDeliveryStatus("PENDING");
        paymentDecisionsObject2.setDecisionCode(1);

        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, List.of(paymentEventsObject2));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, List.of(paymentDetailsObject2));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, List.of(paymentDecisionsObject2));
        insertObjectsToDb(DbName.CLICKHOUSE, CLICKHOUSE_CRM_TB_WITHDRAWAL, List.of(crmTbWithdrawalObject2));

        Thread.sleep(130_000);

        PaymentEventsObject event = PaymentGateHelper.getPaymentEvent(client2.getUcid());
        assertThat("Check status", event.getDeliveryStatus(), is("FAILED"));
        assertThat("Check status", event.getDetails(), is("Stuck in Risk Audit"));
    }

    @Test
    @AllureId("1577")
    @DisplayName("Payment reconciliation test 3. Status != Risk audit and id != 21 -> DELIVERED")
    void ReconciliationTest3() throws Exception {
        crmTbWithdrawalObject3.setStatus("21");
        crmTbWithdrawalObject3.setStatusId(212);
        paymentEventsObject3.setDeliveryStatus("PENDING");
        paymentDecisionsObject3.setDecisionCode(1);

        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, List.of(paymentEventsObject3));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, List.of(paymentDetailsObject3));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, List.of(paymentDecisionsObject3));
        insertObjectsToDb(DbName.CLICKHOUSE, CLICKHOUSE_CRM_TB_WITHDRAWAL, List.of(crmTbWithdrawalObject3));

        Thread.sleep(130_000);
        PaymentEventsObject event = PaymentGateHelper.getPaymentEvent(client3.getUcid());
        assertThat("Check status", event.getDeliveryStatus(), is("DELIVERED"));
        assertThat("Check status", event.getDetails(), is(nullValue()));
    }

    @Test
    @AllureId("1578")
    @DisplayName("Payment reconciliation test 4. Status = 21 and id = 21 -> FAILED")
    void ReconciliationTest4() throws Exception {
        crmTbWithdrawalObject4.setStatus("21");
        crmTbWithdrawalObject4.setStatusId(21);
        paymentEventsObject4.setDeliveryStatus("PENDING");
        paymentDecisionsObject4.setDecisionCode(1);

        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, List.of(paymentEventsObject4));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, List.of(paymentDetailsObject4));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, List.of(paymentDecisionsObject4));
        insertObjectsToDb(DbName.CLICKHOUSE, CLICKHOUSE_CRM_TB_WITHDRAWAL, List.of(crmTbWithdrawalObject4));

        Thread.sleep(130_000);
        PaymentEventsObject event = PaymentGateHelper.getPaymentEvent(client4.getUcid());
        assertThat("Check status", event.getDeliveryStatus(), is("FAILED"));
        assertThat("Check status", event.getDetails(), is("Stuck in Risk Audit"));
    }

    @Test
    @AllureId("1743")
    @DisplayName("Payment reconciliation test 5. Do nothing if final desision code is null")
    void ReconciliationTest5() throws Exception {
        crmTbWithdrawalObject5.setStatus("21");
        crmTbWithdrawalObject5.setStatusId(21);
        paymentEventsObject5.setDeliveryStatus("PENDING");
        paymentEventsObject5.setFinalDecisionId(null);

        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, List.of(paymentEventsObject5));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, List.of(paymentDetailsObject5));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, List.of(paymentDecisionsObject5));
        insertObjectsToDb(DbName.CLICKHOUSE, CLICKHOUSE_CRM_TB_WITHDRAWAL, List.of(crmTbWithdrawalObject5));

        Thread.sleep(130_000);
        PaymentEventsObject event = PaymentGateHelper.getPaymentEvent(client5.getUcid());
        assertThat("Check status", event.getDeliveryStatus(), is("PENDING"));
        assertThat("Check status", event.getDetails(), is(nullValue()));
    }

    @Test
    @AllureId("1759")
    @DisplayName(
            "Payment reconciliation test 6. Status = Risk audit and id != 21 -> FAILED  Status != Risk audit and id != 21 -> DELIVERED two payments with the same payment ID")
    void ReconciliationTest6() throws Exception {
        crmTbWithdrawalObject6.setStatus("Risk Audit");
        crmTbWithdrawalObject6.setStatusId(22);
        paymentEventsObject6.setDeliveryStatus("PENDING");

        crmTbWithdrawalObject7.setStatus("21");
        crmTbWithdrawalObject7.setStatusId(212);
        crmTbWithdrawalObject7.setTransferId(crmTbWithdrawalObject6.getTransferId());
        paymentEventsObject7.setDeliveryStatus("PENDING");
        paymentEventsObject7.setCrmId(paymentEventsObject6.getCrmId());

        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, List.of(paymentEventsObject7));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, List.of(paymentDetailsObject7));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, List.of(paymentDecisionsObject7));
        insertObjectsToDb(DbName.CLICKHOUSE, CLICKHOUSE_CRM_TB_WITHDRAWAL, List.of(crmTbWithdrawalObject7));

        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, List.of(paymentEventsObject6));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, List.of(paymentDetailsObject6));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, List.of(paymentDecisionsObject6));
        insertObjectsToDb(DbName.CLICKHOUSE, CLICKHOUSE_CRM_TB_WITHDRAWAL, List.of(crmTbWithdrawalObject6));

        Thread.sleep(130_000);

        PaymentEventsObject event1 = PaymentGateHelper.getPaymentEvent(client6.getUcid());
        assertThat("Check status", event1.getDeliveryStatus(), is("FAILED"));
        assertThat("Check status", event1.getDetails(), is("Stuck in Risk Audit"));
        PaymentEventsObject event2 = PaymentGateHelper.getPaymentEvent(client7.getUcid());
        assertThat("Check status", event2.getDeliveryStatus(), is("DELIVERED"));
        assertThat("Check status", event2.getDetails(), is(nullValue()));
    }

    @Test
    @AllureId("1763")
    @DisplayName("Payment reconciliation test 7. Decision not found")
    void ReconciliationTest7() throws Exception {
        crmTbWithdrawalObject8.setStatus("Test");
        paymentEventsObject8.setDeliveryStatus("PENDING");
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, List.of(paymentEventsObject8));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, List.of(paymentDetailsObject8));
        Thread.sleep(130_000);

        PaymentEventsObject event = PaymentGateHelper.getPaymentEvent(client8.getUcid());
        assertThat("Check status", event.getDeliveryStatus(), is("FAILED"));
        assertThat("Check status", event.getDetails(), is("Decision not found"));
    }

    @Test
    @AllureId("1928")
    @DisplayName("Receive crm acknowledge")
    void ReconciliationTest9() throws Exception {
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, List.of(paymentEventsObject9));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, List.of(paymentDetailsObject9));
        insertObjectsToDb(DbName.CLICKHOUSE, CLICKHOUSE_CRM_TB_WITHDRAWAL, List.of(crmTbWithdrawalObject9));

        CrmAcknowledgeEvent crmAcknowledgeEvent = new CrmAcknowledgeEvent();
        crmAcknowledgeEvent.setBrand(client9.getBrand());
        crmAcknowledgeEvent.setClientId(client9.getUserId().toString());
        crmAcknowledgeEvent.setId(getRandomUuid().toString());
        crmAcknowledgeEvent.setMerchantOrderId(paymentDetailsObject9.getMerchantOrderId());
        crmAcknowledgeEvent.setPaymentId(paymentEventsObject9.getPaymentId().toString());
        crmAcknowledgeEvent.setRegulator(client9.getRegulator());
        crmAcknowledgeEvent.setSchemaVersion("1.0");
        crmAcknowledgeEvent.setSrcAppId("AUBRC052001PWM000000001");
        crmAcknowledgeEvent.setSubtype("acknowledge");
        crmAcknowledgeEvent.setTimestamp(Instant.now().toString());
        crmAcknowledgeEvent.setType("withdrawal");
        sendCrmAcknowledgeToKafka(crmAcknowledgeEvent);

        Thread.sleep(10_000);
        PaymentEventsObject paymentEvent = getPaymentEvent(client9.getUcid());
        assertThat("Assert delivery status", paymentEvent.getDeliveryStatus(), is("DELIVERED"));
    }
}
