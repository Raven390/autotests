package tests.rule_engine_service_tests.rules.payment.router_rule_crm_payment.subrules.connection_search_tests;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static business_objects.db.payment_gate.payment_details.PaymentDetailsObjectFactory.generatePaymentDetailsObject;
import static business_objects.db.payment_gate.payment_events.PaymentEventsObjectFactory.generatePaymentEventsObject;
import static business_objects.db.payment_gate.payment_rule_executions.PaymentRuleExecutionsObjectFactory.generatePaymentRuleExecutionsObject;
import static helpers.data.rules.payments.router_rule_crm_payment.connection_search.ConnectionSearchDataFactory.setupConnectionSearchRuleData;
import static helpers.database.DbHelper.insertObjectsToDb;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Constants.*;

import business_objects.db.payment_gate.payment_details.PaymentDetailsObject;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import business_objects.db.payment_gate.payment_rule_executions.PaymentRuleExecutionsObject;
import helpers.data.ClientHelper;
import helpers.data.DataDeleteHelper;
import helpers.data.DataHelper;
import helpers.data.enums.Rule;
import helpers.database.DbName;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.io.IOException;
import java.util.*;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_CONNECTION_SEARCH_IN_ROUTER_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class ConnectionSearchTests extends TestBaseRule {

    private static Map<String, DataHelper> dataMap = new HashMap<>();

    @BeforeAll
    static void setupData() throws IOException {
        startSshTunnel();
        enableCRMEmulator();
        dataMap = setupConnectionSearchRuleData();
    }

    @AfterAll
    static void deleteData() throws Exception {
        DataDeleteHelper.deleteData(dataMap);
    }

    @Test
    @AllureId("1829")
    @DisplayName(
            "Connection Search in router rule. Exit without alert if Connection Search rule suspended a WD <= 3 days. ElementId: end_101")
    void connectionSearchRuleTest1() throws Exception {
        DataHelper data = dataMap.get("1");

        // create previous alert
        ClientHelper client1 = data.clientHelper;
        PaymentEventsObject paymentEventsObject1 = generatePaymentEventsObject(client1);
        PaymentDetailsObject paymentDetailsObject1 = generatePaymentDetailsObject(paymentEventsObject1, client1);
        PaymentRuleExecutionsObject paymentRuleExecutionsObject1 =
                generatePaymentRuleExecutionsObject(paymentEventsObject1);
        paymentRuleExecutionsObject1.setRuleEndId(202);
        paymentRuleExecutionsObject1.setRuleId(6);

        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, List.of(paymentEventsObject1));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, List.of(paymentDetailsObject1));
        insertObjectsToDb(
                DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_RULE_EXECUTIONS_TABLE, List.of(paymentRuleExecutionsObject1));

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId(
                "end_101", data.crmWithdrawalEventV2.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "Gateway_1jc7wmc", data.crmWithdrawalEventV2.getId(), Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());
    }

    @Test
    @AllureId("1830")
    @DisplayName(
            "Connection Search in router rule. Approve withdrawal if user has no connections(by deposits). ElementId: end_102")
    void connectionSearchRuleTest2() throws Exception {
        DataHelper data = dataMap.get("2");

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId(
                "end_102", data.crmWithdrawalEventV2.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "Activity_06e7z5l", data.crmWithdrawalEventV2.getId(), Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());
    }

    @Test
    @AllureId("1830")
    @DisplayName(
            "Connection Search in router rule. Approve withdrawal if user has no connections(by deposits and withdrawals. ElementId: end_102")
    void connectionSearchRuleTest3() throws Exception {
        DataHelper data = dataMap.get("3");

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId(
                "end_102", data.crmWithdrawalEventV2.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "Activity_06e7z5l", data.crmWithdrawalEventV2.getId(), Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());
    }

    @Test
    @AllureId("1831")
    @DisplayName(
            "Connection Search in router rule. Total deposits among connected UCIDs <= 500 USD. ElementId: end_103")
    void connectionSearchRuleTest4() throws Exception {
        DataHelper data = dataMap.get("4");

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId(
                "end_103", data.crmWithdrawalEventV2.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "Gateway_1jc7wmc", data.crmWithdrawalEventV2.getId(), Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());
    }

    @Test
    @AllureId("1832")
    @DisplayName(
            "Connection Search in router rule. Total withdrawals among connected UCIDs <= 500 USD. ElementId: end_104")
    void connectionSearchRuleTest5() throws Exception {
        DataHelper data = dataMap.get("5");

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId(
                "end_104", data.crmWithdrawalEventV2.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "Gateway_1jc7wmc", data.crmWithdrawalEventV2.getId(), Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());
    }

    @Test
    @AllureId("1833")
    @DisplayName("Connection Search in router rule. At least one rule finished with alert= false. ElementId: end_105")
    void connectionSearchRuleTest6() throws Exception {
        DataHelper data = dataMap.get("6");

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId(
                "Event_1v8iqld",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "Gateway_1jc7wmc", data.crmWithdrawalEventV2.getId(), Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());
    }

    @Test
    @AllureId("1834")
    @DisplayName(
            "Connection Search in router rule. at least one rule finished with alert= true. ElementId: Event_1v8iqld")
    void connectionSearchRuleTest7() throws Exception {
        DataHelper data = dataMap.get("7");

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId(
                "Event_0aybojb",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "Gateway_1jc7wmc", data.crmWithdrawalEventV2.getId(), Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());
    }

    @Test
    @AllureId("1834")
    @DisplayName("Connection Search in router rule. Payment method never used before for WD. ElementId: Event_1v8iqld")
    void connectionSearchRuleTest8() throws Exception {
        DataHelper data = dataMap.get("8");

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId(
                "put_rule_execution",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementIdNotPresent(
                "payment_abuse_cs",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
    }
}
