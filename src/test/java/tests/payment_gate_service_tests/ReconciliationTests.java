package tests.payment_gate_service_tests;

import business_objects.api.payment_gate.rule_executions.PutRuleExecutionsBody;
import business_objects.db.clickhouse.bo_alerts.BoAlertsObject;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalObject;
import business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObject;
import business_objects.db.payment_gate.payment_details.PaymentDetailsObject;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import helpers.data.ClientHelper;
import helpers.database.DbName;
import helpers.database.PaymentGateHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static business_objects.api.payment_gate.rule_executions.RuleExecutionsRequestBodyFactory.generatePutRuleExecutionsBody;
import static business_objects.db.clickhouse.bo_alerts.BoAlertsFactory.generateAlertCustomAttributes;
import static business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalObjectFactory.generateCrmTbWithdrawalObjectByClient;
import static business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObjectFactory.generatePaymentDecisionObject;
import static business_objects.db.payment_gate.payment_details.PaymentDetailsObjectFactory.generatePaymentDetailsObject;
import static business_objects.db.payment_gate.payment_events.PaymentEventsObjectFactory.generatePaymentEventsObject;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;

import static helpers.database.CleanTableHelper.cleanCrmTbWithdrawalTableByUcid;
import static helpers.database.CleanTableHelper.cleanPaymentGateData;
import static helpers.database.DbHelper.insertObjectsToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

@Feature(FEATURE_PAYMENT_GATE)
@Story(STORY_PAYMENT_GATE_RECONCILIATION)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_PAYMENT_GATE_TESTS)
class ReconciliationTests {

    private static ClientHelper client1;
    private static CrmTbWithdrawalObject crmTbWithdrawalObject1;
    private static PutRuleExecutionsBody putRuleExecutionsBody1;
    private static PaymentEventsObject paymentEventsObject1;
    private static PaymentDetailsObject paymentDetailsObject1;
    private static PaymentDecisionsObject paymentDecisionsObject1;

    private static ClientHelper client2;
    private static CrmTbWithdrawalObject crmTbWithdrawalObject2;
    private static PutRuleExecutionsBody putRuleExecutionsBody2;
    private static PaymentEventsObject paymentEventsObject2;
    private static PaymentDetailsObject paymentDetailsObject2;
    private static PaymentDecisionsObject paymentDecisionsObject2;

    private static ClientHelper client3;
    private static CrmTbWithdrawalObject crmTbWithdrawalObject3;
    private static PutRuleExecutionsBody putRuleExecutionsBody3;
    private static PaymentEventsObject paymentEventsObject3;
    private static PaymentDetailsObject paymentDetailsObject3;
    private static PaymentDecisionsObject paymentDecisionsObject3;

    private static ClientHelper client4;
    private static CrmTbWithdrawalObject crmTbWithdrawalObject4;
    private static PutRuleExecutionsBody putRuleExecutionsBody4;
    private static PaymentEventsObject paymentEventsObject4;
    private static PaymentDetailsObject paymentDetailsObject4;
    private static PaymentDecisionsObject paymentDecisionsObject4;

    private static ClientHelper client5;
    private static CrmTbWithdrawalObject crmTbWithdrawalObject5;
    private static PutRuleExecutionsBody putRuleExecutionsBody5;
    private static PaymentEventsObject paymentEventsObject5;
    private static PaymentDetailsObject paymentDetailsObject5;
    private static PaymentDecisionsObject paymentDecisionsObject5;

    @BeforeAll
    static void setupData() throws Exception {

        client1 = getRandomVantageClientAllFields();
        crmTbWithdrawalObject1 = generateCrmTbWithdrawalObjectByClient(client1);
        paymentEventsObject1 = generatePaymentEventsObject(client1);
        paymentEventsObject1.setCrmId(crmTbWithdrawalObject1.transferId.toString());
        paymentEventsObject1.setDateDecided(Timestamp.from(Instant.now().minusMillis(11 * 60 * 1000)));
        paymentDetailsObject1 = generatePaymentDetailsObject(paymentEventsObject1, client1);
        paymentDecisionsObject1 = generatePaymentDecisionObject(paymentEventsObject1);
        putRuleExecutionsBody1 = generatePutRuleExecutionsBody(paymentEventsObject1);

        client2 = getRandomVantageClientAllFields();
        crmTbWithdrawalObject2 = generateCrmTbWithdrawalObjectByClient(client2);
        paymentEventsObject2 = generatePaymentEventsObject(client2);
        paymentEventsObject2.setCrmId(crmTbWithdrawalObject2.transferId.toString());
        paymentEventsObject2.setDateDecided(Timestamp.from(Instant.now().minusMillis(11 * 60 * 1000)));
        paymentDetailsObject2 = generatePaymentDetailsObject(paymentEventsObject2, client2);
        putRuleExecutionsBody2 = generatePutRuleExecutionsBody(paymentEventsObject2);
        paymentDecisionsObject2 = generatePaymentDecisionObject(paymentEventsObject2);

        client3 = getRandomVantageClientAllFields();
        crmTbWithdrawalObject3 = generateCrmTbWithdrawalObjectByClient(client3);
        paymentEventsObject3 = generatePaymentEventsObject(client3);
        paymentEventsObject3.setCrmId(crmTbWithdrawalObject3.transferId.toString());
        paymentEventsObject3.setDateDecided(Timestamp.from(Instant.now().minusMillis(11 * 60 * 1000)));
        paymentDetailsObject3 = generatePaymentDetailsObject(paymentEventsObject3, client3);
        putRuleExecutionsBody3 = generatePutRuleExecutionsBody(paymentEventsObject3);
        paymentDecisionsObject3 = generatePaymentDecisionObject(paymentEventsObject3);

        client4 = getRandomVantageClientAllFields();
        crmTbWithdrawalObject4 = generateCrmTbWithdrawalObjectByClient(client4);
        paymentEventsObject4 = generatePaymentEventsObject(client4);
        paymentEventsObject4.setCrmId(crmTbWithdrawalObject4.transferId.toString());
        paymentEventsObject4.setDateDecided(Timestamp.from(Instant.now().minusMillis(11 * 60 * 1000)));
        paymentDetailsObject4 = generatePaymentDetailsObject(paymentEventsObject4, client4);
        putRuleExecutionsBody4 = generatePutRuleExecutionsBody(paymentEventsObject4);
        paymentDecisionsObject4 = generatePaymentDecisionObject(paymentEventsObject4);

        client5 = getRandomVantageClientAllFields();
        crmTbWithdrawalObject5 = generateCrmTbWithdrawalObjectByClient(client5);
        paymentEventsObject5 = generatePaymentEventsObject(client5);
        paymentEventsObject5.setCrmId(crmTbWithdrawalObject5.transferId.toString());
        paymentEventsObject5.setDateDecided(Timestamp.from(Instant.now().minusMillis(11 * 60 * 1000)));
        paymentDetailsObject5 = generatePaymentDetailsObject(paymentEventsObject5, client5);
        putRuleExecutionsBody5 = generatePutRuleExecutionsBody(paymentEventsObject5);
        paymentDecisionsObject5 = generatePaymentDecisionObject(paymentEventsObject5);
    }

    // @AfterAll
    static void deleteData() throws Exception {
        cleanCrmTbWithdrawalTableByUcid(client1.getUcid(), client1.getUcid());
        cleanCrmTbWithdrawalTableByUcid(client2.getUcid(), client2.getUcid());
        cleanCrmTbWithdrawalTableByUcid(client3.getUcid(), client3.getUcid());
        cleanCrmTbWithdrawalTableByUcid(client4.getUcid(), client4.getUcid());
        cleanCrmTbWithdrawalTableByUcid(client5.getUcid(), client5.getUcid());
        cleanPaymentGateData(client1.getUcid(), client1.getUserId(), putRuleExecutionsBody1.getPaymentId().toString());
        cleanPaymentGateData(client2.getUcid(), client2.getUserId(), putRuleExecutionsBody2.getPaymentId().toString());
        cleanPaymentGateData(client3.getUcid(), client3.getUserId(), putRuleExecutionsBody3.getPaymentId().toString());
        cleanPaymentGateData(client4.getUcid(), client4.getUserId(), putRuleExecutionsBody4.getPaymentId().toString());
        cleanPaymentGateData(client5.getUcid(), client5.getUserId(), putRuleExecutionsBody5.getPaymentId().toString());
    }

    @Test
    @AllureId("1575")
    @DisplayName("Payment reconciliation test 1. Not found in CH -> DELIVERED")
    void ReconciliationTest1() throws Exception {
        crmTbWithdrawalObject1.setStatus("Test");
        paymentEventsObject1.setDeliveryStatus("PENDING");
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, List.of(paymentEventsObject1));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, List.of(paymentDetailsObject1));
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
        crmTbWithdrawalObject3.setStatus("Risk Audit_");
        crmTbWithdrawalObject3.setStatusId(212);
        paymentEventsObject3.setDeliveryStatus("PENDING");

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
    @DisplayName("Payment reconciliation test 4. Status = Risk audit_ and id = 21 -> FAILED")
    void ReconciliationTest4() throws Exception {
        crmTbWithdrawalObject4.setStatus("Risk Audit_");
        crmTbWithdrawalObject4.setStatusId(21);
        paymentEventsObject4.setDeliveryStatus("PENDING");

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
    @DisplayName("Payment reconciliation test 5. Do nothing if alert is existing")
    void ReconciliationTest5() throws Exception {
        crmTbWithdrawalObject5.setStatus("Risk Audit_");
        crmTbWithdrawalObject5.setStatusId(21);
        paymentEventsObject5.setDeliveryStatus("PENDING");

        String json = String.format("{\"Withdrawal ID\": \"%s\"}", paymentEventsObject5.getCrmId());
        BoAlertsObject alert = generateAlertCustomAttributes(client5, json);
        alert.setRule("Withdrawal Review");
        alert.setStatus("OPEN");
        insertObjectsToDb(DbName.CLICKHOUSE, CLICKHOUSE_BO_ALERT_TABLE_NAME, List.of(alert));

        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, List.of(paymentEventsObject5));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, List.of(paymentDetailsObject5));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, List.of(paymentDecisionsObject5));
        insertObjectsToDb(DbName.CLICKHOUSE, CLICKHOUSE_CRM_TB_WITHDRAWAL, List.of(crmTbWithdrawalObject5));

        Thread.sleep(130_000);
        PaymentEventsObject event = PaymentGateHelper.getPaymentEvent(client5.getUcid());
        assertThat("Check status", event.getDeliveryStatus(), is("PENDING"));
        assertThat("Check status", event.getDetails(), is(nullValue()));
    }
}
