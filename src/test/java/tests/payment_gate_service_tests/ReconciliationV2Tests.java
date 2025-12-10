package tests.payment_gate_service_tests;

import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntity;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntityFactory;
import business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObject;
import business_objects.db.payment_gate.payment_decisions_sent.PaymentDecisionsSentObject;
import business_objects.db.payment_gate.payment_details.PaymentDetailsObject;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import helpers.data.ClientHelper;
import helpers.database.DbName;
import helpers.database.PaymentGateHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObjectFactory.generatePaymentDecisionObject;
import static business_objects.db.payment_gate.payment_decisions_sent.PaymentDecisionsSentObjectFactory.generatePaymentDecisionSentObject;
import static business_objects.db.payment_gate.payment_details.PaymentDetailsObjectFactory.generatePaymentDetailsObject;
import static business_objects.db.payment_gate.payment_events.PaymentEventsObjectFactory.generatePaymentEventsObject;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.DbHelper.insertObjectsToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static utils.Constants.*;

@Disabled
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
        paymentEventsObject1.setDateDecided(Timestamp.from(Instant.now().minusMillis(6 * 60 * 1000)));
        paymentDetailsObject1 = generatePaymentDetailsObject(paymentEventsObject1, client1);
        paymentDecisionsObject1 = generatePaymentDecisionObject(paymentEventsObject1);
        paymentDecisionsSentObject1 = generatePaymentDecisionSentObject(paymentEventsObject1, paymentDecisionsObject1);

//        client2 = getRandomVantageClientAllFields();
//        crmTbWithdrawalObject2 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client2);
//        paymentEventsObject2 = generatePaymentEventsObject(client2);
//        paymentEventsObject2.setCrmId(crmTbWithdrawalObject2.getTransferId().toString());
//        paymentEventsObject2.setDateDecided(Timestamp.from(Instant.now().minusMillis(11 * 60 * 1000)));
//        paymentDetailsObject2 = generatePaymentDetailsObject(paymentEventsObject2, client2);
//        paymentDecisionsObject2 = generatePaymentDecisionObject(paymentEventsObject2);
//
//        client3 = getRandomVantageClientAllFields();
//        crmTbWithdrawalObject3 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client3);
//        paymentEventsObject3 = generatePaymentEventsObject(client3);
//        paymentEventsObject3.setCrmId(crmTbWithdrawalObject3.getTransferId().toString());
//        paymentEventsObject3.setDateDecided(Timestamp.from(Instant.now().minusMillis(11 * 60 * 1000)));
//        paymentDetailsObject3 = generatePaymentDetailsObject(paymentEventsObject3, client3);
//        paymentDecisionsObject3 = generatePaymentDecisionObject(paymentEventsObject3);
//
//        client4 = getRandomVantageClientAllFields();
//        crmTbWithdrawalObject4 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client4);
//        paymentEventsObject4 = generatePaymentEventsObject(client4);
//        paymentEventsObject4.setCrmId(crmTbWithdrawalObject4.getTransferId().toString());
//        paymentEventsObject4.setDateDecided(Timestamp.from(Instant.now().minusMillis(11 * 60 * 1000)));
//        paymentDetailsObject4 = generatePaymentDetailsObject(paymentEventsObject4, client4);
//        paymentDecisionsObject4 = generatePaymentDecisionObject(paymentEventsObject4);
//
//        client5 = getRandomVantageClientAllFields();
//        crmTbWithdrawalObject5 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client5);
//        paymentEventsObject5 = generatePaymentEventsObject(client5);
//        paymentEventsObject5.setCrmId(crmTbWithdrawalObject5.getTransferId().toString());
//        paymentEventsObject5.setDateDecided(Timestamp.from(Instant.now().minusMillis(11 * 60 * 1000)));
//        paymentDetailsObject5 = generatePaymentDetailsObject(paymentEventsObject5, client5);
//        paymentDecisionsObject5 = generatePaymentDecisionObject(paymentEventsObject5);
//
//        client6 = getRandomVantageClientAllFields();
//        crmTbWithdrawalObject6 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client6);
//        paymentEventsObject6 = generatePaymentEventsObject(client6);
//        paymentEventsObject6.setCrmId(crmTbWithdrawalObject6.getTransferId().toString());
//        paymentEventsObject6.setDateDecided(Timestamp.from(Instant.now().minusMillis(11 * 60 * 1000)));
//        paymentDetailsObject6 = generatePaymentDetailsObject(paymentEventsObject6, client6);
//        paymentDecisionsObject6 = generatePaymentDecisionObject(paymentEventsObject6);
//
//        client7 = getRandomVantageClientAllFields();
//        crmTbWithdrawalObject7 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client7);
//        paymentEventsObject7 = generatePaymentEventsObject(client7);
//        paymentEventsObject7.setCrmId(crmTbWithdrawalObject7.getTransferId().toString());
//        paymentEventsObject7.setDateDecided(Timestamp.from(Instant.now().minusMillis(11 * 60 * 1000)));
//        paymentDetailsObject7 = generatePaymentDetailsObject(paymentEventsObject7, client7);
//        paymentDecisionsObject7 = generatePaymentDecisionObject(paymentEventsObject7);
//
//        client8 = getRandomVantageClientAllFields();
//        crmTbWithdrawalObject8 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client8);
//        paymentEventsObject8 = generatePaymentEventsObject(client8);
//        paymentEventsObject8.setCrmId(crmTbWithdrawalObject8.getTransferId().toString());
//        paymentEventsObject8.setDateCreated(Timestamp.from(Instant.now().minusMillis(11 * 60 * 1000)));
//        paymentDetailsObject8 = generatePaymentDetailsObject(paymentEventsObject8, client8);
//
//        client9 = getRandomVantageClientAllFields();
//        crmTbWithdrawalObject9 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client9);
//        paymentEventsObject9 = generatePaymentEventsObject(client9);
//        paymentEventsObject9.setCrmId(crmTbWithdrawalObject9.getTransferId().toString());
//        paymentEventsObject9.setDateCreated(Timestamp.from(Instant.now().minusMillis(11 * 60 * 1000)));
//        paymentDetailsObject9 = generatePaymentDetailsObject(paymentEventsObject9, client9);
    }

//    @AfterAll
//    static void deleteData() throws Exception {
//        cleanCrmTbWithdrawalTableByUcid(client1.getUcid(), client1.getUcid());
//
//    }

    @Test
    @AllureId("")
    @DisplayName("Payment reconciliation v2 test 1. ")
    void ReconciliationTest1() throws Exception {
        crmTbWithdrawalObject1.setStatus("Test");
        paymentEventsObject1.setDeliveryStatus("PENDING");
        paymentDecisionsObject1.setDecisionCode(1);
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, List.of(paymentEventsObject1));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, List.of(paymentDetailsObject1));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, List.of(paymentDecisionsObject1));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_SENT_TABLE, List.of(paymentDecisionsSentObject1));

        Thread.sleep(310_000);

        PaymentEventsObject event = PaymentGateHelper.getPaymentEvent(client1.getUcid());
        assertThat("Check status", event.getDeliveryStatus(), is("FAILED"));
        assertThat("Check status", event.getDetails(), is("Not found in CH"));

        insertObjectsToDb(DbName.CLICKHOUSE, CLICKHOUSE_CRM_TB_WITHDRAWAL, List.of(crmTbWithdrawalObject1));
        Thread.sleep(130_000);

        PaymentEventsObject event2 = PaymentGateHelper.getPaymentEvent(client1.getUcid());
        assertThat("Check status", event2.getDeliveryStatus(), is("DELIVERED"));
        assertThat("Check status", event2.getDetails(), is(nullValue()));
    }

}
