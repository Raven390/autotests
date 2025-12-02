package tests.rule_engine_service_tests.rules.payment.router_rule_crm_payment.connection_search_tests;

import business_objects.db.payment_gate.payment_details.PaymentDetailsObject;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import business_objects.db.payment_gate.payment_rule_executions.PaymentRuleExecutionsObject;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import helpers.data.enums.Rule;
import helpers.database.DbName;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

import java.io.IOException;
import java.util.*;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static business_objects.db.payment_gate.payment_details.PaymentDetailsObjectFactory.generatePaymentDetailsObject;
import static business_objects.db.payment_gate.payment_events.PaymentEventsObjectFactory.generatePaymentEventsObject;
import static business_objects.db.payment_gate.payment_rule_executions.PaymentRuleExecutionsObjectFactory.generatePaymentRuleExecutionsObject;
import static helpers.data.rules.payments.router_rule_crm_payment.connection_search.ConnectionSearchDataFactory.setupConnectionSearchRuleData;
import static helpers.database.DbHelper.insertObjectsToDb;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Constants.*;


@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_CONNECTION_SEARCH_IN_ROUTER_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class ConnectionSearchTests extends TestBaseRule {

    private static Map<String, DataHelper> dataMap = new HashMap<>();
    private static PaymentEventsObject paymentEventsObject1;
    private static PaymentDetailsObject paymentDetailsObject1;
    private static PaymentRuleExecutionsObject paymentRuleExecutionsObject1;

    @BeforeAll
    static void setupData() throws IOException {
        startSshTunnel();
        enableCRMEmulator();
        dataMap = setupConnectionSearchRuleData();

    }

    @AfterAll
    static void deleteData() throws Exception {
        DataHelper.deleteData(dataMap);
    }

    @Test
    @AllureId("1829")
    @DisplayName("Connection Search in router rule. Exit without alert if Connection Search rule suspended a WD <= 3 days. ElementId: end_101")
    void connectionSearchRuleTest1() throws Exception {
        DataHelper data = dataMap.get("1");

        //create previous alert
        ClientHelper client1 = data.clientHelper;
        paymentEventsObject1 = generatePaymentEventsObject(client1);
        paymentDetailsObject1 = generatePaymentDetailsObject(paymentEventsObject1, client1);
        paymentRuleExecutionsObject1 = generatePaymentRuleExecutionsObject(paymentEventsObject1);
        paymentRuleExecutionsObject1.setRuleEndId(202);
        paymentRuleExecutionsObject1.setRuleId(6);

        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, List.of(paymentEventsObject1));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, List.of(paymentDetailsObject1));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_RULE_EXECUTIONS_TABLE, List.of(paymentRuleExecutionsObject1));

        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("end_101", data.crmWithdrawalEvent.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId("Gateway_1jc7wmc", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE.getProcessId());
    }

    @Test
    @AllureId("1830")
    @DisplayName("Connection Search in router rule. Approve withdrawal if user has no connections. ElementId: end_102")
    void connectionSearchRuleTest2() throws Exception {
        DataHelper data = dataMap.get("2");

        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("end_102", data.crmWithdrawalEvent.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId("Activity_06e7z5l", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE.getProcessId());
    }

    @Test
    @AllureId("1831")
    @DisplayName("Connection Search in router rule. Total deposits among connected UCIDs <= 500 USD. ElementId: end_103")
    void connectionSearchRuleTest3() throws Exception {
        DataHelper data = dataMap.get("3");

        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("end_103", data.crmWithdrawalEvent.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId("Gateway_1jc7wmc", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE.getProcessId());
    }

    @Test
    @AllureId("1832")
    @DisplayName("Connection Search in router rule. Total withdrawals among connected UCIDs <= 500 USD. ElementId: end_104")
    void connectionSearchRuleTest4() throws Exception {
        DataHelper data = dataMap.get("4");

        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("end_104", data.crmWithdrawalEvent.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId("Gateway_1jc7wmc", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE.getProcessId());
    }

    @Test
    @AllureId("1833")
    @DisplayName("Connection Search in router rule. At least one rule finished with alert= false. ElementId: end_105")
    void connectionSearchRuleTest5() throws Exception {
        DataHelper data = dataMap.get("5");

        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("Event_1v8iqld", data.crmWithdrawalEvent.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId("Gateway_1jc7wmc", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE.getProcessId());
    }

    @Test
    @AllureId("1834")
    @DisplayName("Connection Search in router rule. at least one rule finished with alert= true. ElementId: Event_1v8iqld")
    void connectionSearchRuleTest6() throws Exception {
        DataHelper data = dataMap.get("6");

        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("Event_0aybojb", data.crmWithdrawalEvent.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId("Gateway_1jc7wmc", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE.getProcessId());
    }
}
