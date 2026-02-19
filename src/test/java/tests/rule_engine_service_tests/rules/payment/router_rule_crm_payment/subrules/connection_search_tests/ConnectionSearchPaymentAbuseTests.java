package tests.rule_engine_service_tests.rules.payment.router_rule_crm_payment.subrules.connection_search_tests;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.asserts.AlertsAssertsHelper.assertConnectionSearchPaymentAbuseSubruleAlert;
import static helpers.data.DataDeleteHelper.deleteData;
import static helpers.data.DataSetupHelper.setupData;
import static helpers.data.rules.payments.router_rule_crm_payment.connection_search.ConnectionSearchPaymentAbuseDataFactory.setupConnectionSearchPaymentRuleData;
import static helpers.database.DbHelper.startSshTunnel;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

import business_objects.kafka.alerts.RuleAlertV2;
import helpers.data.DataHelper;
import helpers.data.enums.rule_engine.Rule;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_CONNECTION_SEARCH_IN_ROUTER_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class ConnectionSearchPaymentAbuseTests extends TestBaseRule {
    /// cases id 2211-2220
    private static Map<String, DataHelper> dataMap = new HashMap<>();

    @BeforeAll
    static void setup() throws IOException, InterruptedException {
        startSshTunnel();
        enableCRMEmulator();
        dataMap = setupConnectionSearchPaymentRuleData();
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteData(dataMap);
    }

    @Test
    @AllureId("2211")
    @DisplayName(
            "Connection Search(payment branch) in router rule. Exit if no matching connections. ElementId: end_connections_not_found2")
    void connectionSearchPaymentAbuseTest1() throws Exception {
        DataHelper data = dataMap.get("1");
        setupData(data);

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId(
                "end_connections_not_found2",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "Gateway_154345n",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "payment_branch_end_for_withdrawal",
                data.crmWithdrawalEventV2.getId(),
                Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());
    }

    @Test
    @AllureId("2212")
    @DisplayName(
            "Connection Search(payment branch) in router rule. Exit if no required abuseTypes. ElementId: Event_1sc8b2t")
    void connectionSearchPaymentAbuseTest2() throws Exception {
        DataHelper data = dataMap.get("2");
        setupData(data);

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId(
                "Event_1sc8b2t",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "Gateway_154345n",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "payment_branch_end_for_withdrawal",
                data.crmWithdrawalEventV2.getId(),
                Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());
    }

    @Test
    @AllureId("2213")
    @DisplayName("Connection Search(payment branch) in router rule. Alert. ElementId: Event_0yh59iy")
    void connectionSearchPaymentAbuseTest3() throws Exception {
        DataHelper data = dataMap.get("3");
        setupData(data);

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId(
                "Event_0yh59iy",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "Gateway_154345n",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "payment_branch_end_for_withdrawal",
                data.crmWithdrawalEventV2.getId(),
                Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());

        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(
                data.clientHelper, "Connection search with known fraudster", "Connection to known fraudster");
        assertConnectionSearchPaymentAbuseSubruleAlert(data, alerts);
    }
}
