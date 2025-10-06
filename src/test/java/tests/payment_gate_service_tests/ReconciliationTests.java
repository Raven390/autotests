package tests.payment_gate_service_tests;

import business_objects.api.payment_gate.rule_executions.PutRuleExecutionsBody;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalObject;
import business_objects.db.payment_gate.payment_details.PaymentDetailsObject;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import business_objects.db.payment_gate.tmp_rule_decisions.TmpRuleDecisionsObject;
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
import static business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalObjectFactory.generateCrmTbWithdrawalObjectByClient;
import static business_objects.db.payment_gate.payment_details.PaymentDetailsObjectFactory.generatePaymentDetailsObject;
import static business_objects.db.payment_gate.payment_events.PaymentEventsObjectFactory.generatePaymentEventsObject;
import static business_objects.db.payment_gate.tmp_rule_decisions.TmpRuleDecisionsObjectFactory.generateTmpRuleDecisionsObject;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;

import static helpers.database.CleanTableHelper.cleanCrmTbWithdrawalTableByUcid;
import static helpers.database.CleanTableHelper.cleanPaymentGateData;
import static helpers.database.DbHelper.insertObjectsToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
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
    private static TmpRuleDecisionsObject tmpRuleDecisionsObject1;

    private static ClientHelper client2;
    private static CrmTbWithdrawalObject crmTbWithdrawalObject2;
    private static PutRuleExecutionsBody putRuleExecutionsBody2;
    private static PaymentEventsObject paymentEventsObject2;
    private static PaymentDetailsObject paymentDetailsObject2;
    private static TmpRuleDecisionsObject tmpRuleDecisionsObject2;

    private static ClientHelper client3;
    private static CrmTbWithdrawalObject crmTbWithdrawalObject3;
    private static PutRuleExecutionsBody putRuleExecutionsBody3;
    private static PaymentEventsObject paymentEventsObject3;
    private static PaymentDetailsObject paymentDetailsObject3;
    private static TmpRuleDecisionsObject tmpRuleDecisionsObject3;

    private static ClientHelper client4;
    private static CrmTbWithdrawalObject crmTbWithdrawalObject4;
    private static PutRuleExecutionsBody putRuleExecutionsBody4;
    private static PaymentEventsObject paymentEventsObject4;
    private static PaymentDetailsObject paymentDetailsObject4;
    private static TmpRuleDecisionsObject tmpRuleDecisionsObject4;

    @BeforeAll
    static void setupData() throws Exception {

        client1 = getRandomVantageClientAllFields();
        crmTbWithdrawalObject1 = generateCrmTbWithdrawalObjectByClient(client1);
        paymentEventsObject1 = generatePaymentEventsObject(client1);
        paymentEventsObject1.setCrmId(crmTbWithdrawalObject1.transferId.toString());
        paymentEventsObject1.setDateDecided(Timestamp.from(Instant.now().minusMillis(11 * 60 * 1000)));
        paymentDetailsObject1 = generatePaymentDetailsObject(paymentEventsObject1, client1);
        tmpRuleDecisionsObject1 = generateTmpRuleDecisionsObject(paymentEventsObject1);
        putRuleExecutionsBody1 = generatePutRuleExecutionsBody(paymentEventsObject1);

        client2 = getRandomVantageClientAllFields();
        crmTbWithdrawalObject2 = generateCrmTbWithdrawalObjectByClient(client2);
        paymentEventsObject2 = generatePaymentEventsObject(client2);
        paymentEventsObject2.setCrmId(crmTbWithdrawalObject2.transferId.toString());
        paymentEventsObject2.setDateDecided(Timestamp.from(Instant.now().minusMillis(11 * 60 * 1000)));
        paymentDetailsObject2 = generatePaymentDetailsObject(paymentEventsObject2, client2);
        tmpRuleDecisionsObject2 = generateTmpRuleDecisionsObject(paymentEventsObject2);
        putRuleExecutionsBody2 = generatePutRuleExecutionsBody(paymentEventsObject2);

        client3 = getRandomVantageClientAllFields();
        crmTbWithdrawalObject3 = generateCrmTbWithdrawalObjectByClient(client3);
        paymentEventsObject3 = generatePaymentEventsObject(client3);
        paymentEventsObject3.setCrmId(crmTbWithdrawalObject3.transferId.toString());
        paymentEventsObject3.setDateDecided(Timestamp.from(Instant.now().minusMillis(11 * 60 * 1000)));
        paymentDetailsObject3 = generatePaymentDetailsObject(paymentEventsObject3, client3);
        tmpRuleDecisionsObject3 = generateTmpRuleDecisionsObject(paymentEventsObject3);
        putRuleExecutionsBody3 = generatePutRuleExecutionsBody(paymentEventsObject3);

        client4 = getRandomVantageClientAllFields();
        crmTbWithdrawalObject4 = generateCrmTbWithdrawalObjectByClient(client4);
        paymentEventsObject4 = generatePaymentEventsObject(client4);
        paymentEventsObject4.setCrmId(crmTbWithdrawalObject4.transferId.toString());
        paymentEventsObject4.setDateDecided(Timestamp.from(Instant.now().minusMillis(11 * 60 * 1000)));
        paymentDetailsObject4 = generatePaymentDetailsObject(paymentEventsObject4, client4);
        tmpRuleDecisionsObject4 = generateTmpRuleDecisionsObject(paymentEventsObject4);
        putRuleExecutionsBody4 = generatePutRuleExecutionsBody(paymentEventsObject4);
    }

    @AfterAll
    static void deleteData() throws Exception {
        cleanCrmTbWithdrawalTableByUcid(client1.getUcid(), client1.getUcid());
        cleanCrmTbWithdrawalTableByUcid(client2.getUcid(), client2.getUcid());
        cleanCrmTbWithdrawalTableByUcid(client3.getUcid(), client3.getUcid());
        cleanPaymentGateData(client1.getUcid(), client1.getUserId(), putRuleExecutionsBody1.getPaymentId().toString());
        cleanPaymentGateData(client2.getUcid(), client2.getUserId(), putRuleExecutionsBody2.getPaymentId().toString());
        cleanPaymentGateData(client3.getUcid(), client3.getUserId(), putRuleExecutionsBody3.getPaymentId().toString());
    }

    @Test
    @AllureId("1575")
    @DisplayName("Payment reconciliation test 1. Payment not found")
    void ReconciliationTest1() throws Exception {
        crmTbWithdrawalObject1.setStatusId(20);

        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, List.of(paymentEventsObject1));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, List.of(paymentDetailsObject1));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_TMP_RULE_DECISIONS_TABLE, List.of(tmpRuleDecisionsObject1));

        Thread.sleep(125_000);
        PaymentEventsObject event = PaymentGateHelper.getPaymentEvent(client1.getUcid());
        assertThat("Check status", event.getDeliveryStatus(), is("FAILED"));
    }

    @Test
    @AllureId("1577")
    @DisplayName("Payment reconciliation test 2. Status != Risk audit and id != 21 -> DELIVERED")
    void ReconciliationTest2() throws Exception {
        crmTbWithdrawalObject2.setStatus("Risk Audit_");
        crmTbWithdrawalObject2.setStatusId(212);

        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, List.of(paymentEventsObject2));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, List.of(paymentDetailsObject2));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_TMP_RULE_DECISIONS_TABLE, List.of(tmpRuleDecisionsObject2));
        insertObjectsToDb(DbName.CLICKHOUSE, CLICKHOUSE_CRM_TB_WITHDRAWAL, List.of(crmTbWithdrawalObject2));

        Thread.sleep(125_000);
        PaymentEventsObject event = PaymentGateHelper.getPaymentEvent(client2.getUcid());
        assertThat("Check status", event.getDeliveryStatus(), is("DELIVERED"));
    }

    @Test
    @AllureId("1578")
    @DisplayName("Payment reconciliation test 3. Status = Risk audit_ and id = 21 -> FAILED")
    void ReconciliationTest3() throws Exception {
        crmTbWithdrawalObject3.setStatus("Risk Audit_");
        crmTbWithdrawalObject3.setStatusId(21);

        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, List.of(paymentEventsObject3));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, List.of(paymentDetailsObject3));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_TMP_RULE_DECISIONS_TABLE, List.of(tmpRuleDecisionsObject3));
        insertObjectsToDb(DbName.CLICKHOUSE, CLICKHOUSE_CRM_TB_WITHDRAWAL, List.of(crmTbWithdrawalObject3));

        Thread.sleep(125_000);
        PaymentEventsObject event = PaymentGateHelper.getPaymentEvent(client3.getUcid());
        assertThat("Check status", event.getDeliveryStatus(), is("FAILED"));
    }

    @Test
    @AllureId("1645")
    @DisplayName("Payment reconciliation test 3. Status = Risk audit and id = 22 -> FAILED")
    void ReconciliationTest4() throws Exception {
        crmTbWithdrawalObject4.setStatus("Risk Audit");
        crmTbWithdrawalObject4.setStatusId(22);

        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, List.of(paymentEventsObject4));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, List.of(paymentDetailsObject4));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_TMP_RULE_DECISIONS_TABLE, List.of(tmpRuleDecisionsObject4));
        insertObjectsToDb(DbName.CLICKHOUSE, CLICKHOUSE_CRM_TB_WITHDRAWAL, List.of(crmTbWithdrawalObject4));

        Thread.sleep(125_000);
        PaymentEventsObject event = PaymentGateHelper.getPaymentEvent(client3.getUcid());
        assertThat("Check status", event.getDeliveryStatus(), is("FAILED"));
    }

}
