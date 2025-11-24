package tests.rule_engine_service_tests.rules.payment.router_rule_crm_payment;

import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import business_objects.db.payment_gate.payment_rule_executions.PaymentRuleExecutionsObject;
import helpers.data.DataHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.data.rules.payments.router_rule_crm_payment.EnoughTradesDataFactory.setupEnoughTradesRuleData;
import static helpers.database.PaymentGateHelper.getPaymentEvent;
import static helpers.database.PaymentGateHelper.getPaymentRuleExecution;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static tests.TestBaseRule.checkElementId;
import static tests.TestBaseRule.produceWithdrawalMessageToCrmPaymentTopic;

public class EnoughTradesRuleTest {

    private static Map<String, DataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    static void setupData() throws Exception {
        // Enable emulator to set restrictions to status APPLIED
        enableCRMEmulator();
        dbDataMap = setupEnoughTradesRuleData();
    }

    @AfterAll
    static void deleteData() throws Exception {
        DataHelper.deleteData(dbDataMap);
    }

    @Test
    @AllureId("1826")
    @DisplayName("Enough trades rule. Account type in event 'IB'. Element id: Event_1f8kqnh")
    void enoughTradesTest1() throws Exception {
        DataHelper data = dbDataMap.get("1");

        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("Event_1f8kqnh", data.crmWithdrawalEvent.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEvent.getId(), "enough_trades");
    }

    @Test
    @AllureId("1828")
    @DisplayName("Enough trades rule. Fund type in event 'PAMM'. Element id: Event_1f8kqnh")
    void enoughTradesTest2() throws Exception {
        DataHelper data = dbDataMap.get("2");
        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("Event_1f8kqnh", data.crmWithdrawalEvent.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEvent.getId(), "enough_trades");
    }

    @Test
    @AllureId("1827")
    @DisplayName("Enough trades rule. Fund type in event 'PAMM'. Element id: Event_1f8kqnh")
    void enoughTradesTest3() throws Exception {
        DataHelper data = dbDataMap.get("3");

        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("Event_1f8kqnh", data.crmWithdrawalEvent.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEvent.getId(), "enough_trades");
    }

    @Test
    @AllureId("1827")
    @DisplayName("Enough trades rule. Client have no restrictions . Element id: Event_1o3t1d2")
    void enoughTradesTest4() throws Exception {
        DataHelper data = dbDataMap.get("4");
        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("Event_1o3t1d2", data.crmWithdrawalEvent.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEvent.getId(), "enough_trades");
    }

    @Test
    @AllureId("")
    @DisplayName("Enough trades rule. Alert 1  . Element id: Event_1gmc8xt")
    void enoughTradesTestAlert1() throws Exception {
        DataHelper data = dbDataMap.get("770");
        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("Event_1gmc8xt", data.crmWithdrawalEvent.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEvent.getId(), "enough_trades");
    }

    @Test
    @AllureId("")
    @DisplayName("Enough trades rule. Alert2 . Element id: Event_1mzlm7b")
    void enoughTradesTestAlert2() throws Exception {
        DataHelper data = dbDataMap.get("771");
        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("Event_1mzlm7b", data.crmWithdrawalEvent.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEvent.getId(), "enough_trades");

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString());
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(3));
    }
}
