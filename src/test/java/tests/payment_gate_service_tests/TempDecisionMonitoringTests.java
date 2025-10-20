package tests.payment_gate_service_tests;

import business_objects.api.payment_gate.rule_executions.PutRuleExecutionsBody;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalObject;
import business_objects.db.payment_gate.payment_details.PaymentDetailsObject;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import business_objects.db.payment_gate.tmp_rule_decisions.TmpRuleDecisionsObject;
import helpers.data.ClientHelper;
import helpers.database.CleanTableHelper;
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
import static helpers.database.DbHelper.insertObjectsToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;
import static utils.Constants.CLICKHOUSE_CRM_TB_WITHDRAWAL;

@Feature(FEATURE_PAYMENT_GATE)
@Story(STORY_PAYMENT_GATE_DECISION_MONITORING)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_PAYMENT_GATE_TESTS)
class TempDecisionMonitoringTests {
    private static ClientHelper client1;
    private static CrmTbWithdrawalObject crmTbWithdrawalObject1;
    private static PutRuleExecutionsBody putRuleExecutionsBody1;
    private static PaymentEventsObject paymentEventsObject1;
    private static PaymentDetailsObject paymentDetailsObject1;

    private static ClientHelper client2;
    private static CrmTbWithdrawalObject crmTbWithdrawalObject2;
    private static PutRuleExecutionsBody putRuleExecutionsBody2;
    private static PaymentEventsObject paymentEventsObject2;
    private static PaymentDetailsObject paymentDetailsObject2;
    private static TmpRuleDecisionsObject tmpRuleDecisionsObject2;

    @BeforeAll
    static void setupData() throws Exception {

        client1 = getRandomVantageClientAllFields();

        crmTbWithdrawalObject1 = generateCrmTbWithdrawalObjectByClient(client1);
        paymentEventsObject1 = generatePaymentEventsObject(client1);
        paymentEventsObject1.setCrmId(crmTbWithdrawalObject1.transferId.toString());
        paymentEventsObject1.setDateCreated(Timestamp.from(Instant.now().minusMillis(6 * 60 * 1000)));
        paymentDetailsObject1 = generatePaymentDetailsObject(paymentEventsObject1, client1);
        putRuleExecutionsBody1 = generatePutRuleExecutionsBody(paymentEventsObject1);

        client2 = getRandomVantageClientAllFields();
        crmTbWithdrawalObject2 = generateCrmTbWithdrawalObjectByClient(client2);
        paymentEventsObject2 = generatePaymentEventsObject(client2);
        paymentEventsObject2.setCrmId(crmTbWithdrawalObject2.transferId.toString());
        paymentEventsObject2.setDateDecided(Timestamp.from(Instant.now().minusMillis(4 * 60 * 1000)));
        paymentDetailsObject2 = generatePaymentDetailsObject(paymentEventsObject2, client2);
        tmpRuleDecisionsObject2 = generateTmpRuleDecisionsObject(paymentEventsObject2);
        putRuleExecutionsBody2 = generatePutRuleExecutionsBody(paymentEventsObject2);
    }

    @AfterAll
    static void deleteData() throws Exception {
        cleanCrmTbWithdrawalTableByUcid(client1.getUcid());
        CleanTableHelper.cleanPaymentGateData(client1.getUcid(), client1.getUserId(), putRuleExecutionsBody1.getPaymentId().toString());
        cleanCrmTbWithdrawalTableByUcid(client2.getUcid());
        CleanTableHelper.cleanPaymentGateData(client2.getUcid(), client2.getUserId(), putRuleExecutionsBody2.getPaymentId().toString());
    }

    @Test
    @AllureId("1576")
    @DisplayName("TmpDecisionMonitoringTest1. Mark entry as failed if no tmp_decision entry found")
    void TmpDecisionMonitoringTest1() throws Exception {
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, List.of(paymentEventsObject1));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, List.of(paymentDetailsObject1));
        insertObjectsToDb(DbName.CLICKHOUSE, CLICKHOUSE_CRM_TB_WITHDRAWAL, List.of(crmTbWithdrawalObject1));

        Thread.sleep(125_000);
        PaymentEventsObject event = PaymentGateHelper.getPaymentEvent(client1.getUcid());
        assertThat("Check status", event.getDeliveryStatus(), is("FAILED"));
    }
}
