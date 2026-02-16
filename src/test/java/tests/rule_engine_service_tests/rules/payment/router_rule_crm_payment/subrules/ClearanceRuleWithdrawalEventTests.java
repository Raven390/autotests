package tests.rule_engine_service_tests.rules.payment.router_rule_crm_payment.subrules;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.api.RestrictionHelper.setRestrictionAPIGeneral;
import static helpers.data.DataDeleteHelper.deleteData;
import static helpers.data.DataSetupHelper.setupData;
import static helpers.data.enums.Restriction.MANUAL_WITHDRAWAL_REVIEW;
import static helpers.data.rules.payments.router_rule_crm_payment.ClearanceRuleWithdrawalEventDataFactory.setupClearanceWithdrawalEventRuleData;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Constants.*;

import helpers.data.DataHelper;
import helpers.data.enums.rule_engine.Rule;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_CLEARANCE_IN_ROUTER_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class ClearanceRuleWithdrawalEventTests extends TestBaseRule {

    private static Map<String, DataHelper> dataMap = new HashMap<>();

    @BeforeAll
    static void setup() throws IOException {
        startSshTunnel();
        enableCRMEmulator();
        dataMap = setupClearanceWithdrawalEventRuleData();
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteData(dataMap);
    }

    @Test
    @AllureId("2240")
    @DisplayName(
            "Clearance rule in router rule. Withdrawal event. Exit with ruleEndId = 101 if Active WR restriction is set by rule engine")
    void clearanceRuleWithdrawalEventTest1() throws Exception {
        DataHelper data = dataMap.get("1");
        setupData(data);
        setRestrictionAPIGeneral(
                data.clientHelper.getUcid(),
                MANUAL_WITHDRAWAL_REVIEW.getCode(),
                "Clearance rule test",
                "Automation",
                "QA");

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("end_event_101", data.crmWithdrawalEventV2.getId(), Rule.CLEARANCE_RULE.getProcessId());
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), Rule.CLEARANCE_RULE.getProcessId());
    }

    @Test
    @AllureId("2241")
    @DisplayName("Clearance rule in router rule. Withdrawal event. Exit with ruleEndId = 102 if grossDeposit > 10 000?")
    void clearanceRuleWithdrawalEventTest2() throws Exception {
        DataHelper data = dataMap.get("2");
        setupData(data);

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("end_event_102", data.crmWithdrawalEventV2.getId(), Rule.CLEARANCE_RULE.getProcessId());
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), Rule.CLEARANCE_RULE.getProcessId());
    }

    @Test
    @AllureId("2242")
    @DisplayName(
            "Clearance rule in router rule. Withdrawal event. Exit with ruleEndId = 401 if checks B and D are true")
    void clearanceRuleWithdrawalEventTest3() throws Exception {
        DataHelper data = dataMap.get("3");
        setupData(data);

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("end_event_401", data.crmWithdrawalEventV2.getId(), Rule.CLEARANCE_RULE.getProcessId());
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), Rule.CLEARANCE_RULE.getProcessId());
        // check that router rule ended with the auto approve branch
        checkElementId(
                "Activity_197u1ti", data.crmWithdrawalEventV2.getId(), Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());
    }

    @Test
    @AllureId("2243")
    @DisplayName(
            "Clearance rule in router rule. Withdrawal event. Exit with ruleEndId = 402 if checks B and E are true")
    void clearanceRuleWithdrawalEventTest4() throws Exception {
        DataHelper data = dataMap.get("4");
        setupData(data);

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("end_event_402", data.crmWithdrawalEventV2.getId(), Rule.CLEARANCE_RULE.getProcessId());
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), Rule.CLEARANCE_RULE.getProcessId());
    }

    @Test
    @AllureId("2244")
    @DisplayName(
            "Clearance rule in router rule. Withdrawal event. Exit with ruleEndId = 403 if checks C and D are true")
    void clearanceRuleWithdrawalEventTest5() throws Exception {
        DataHelper data = dataMap.get("5");
        setupData(data);

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("end_event_403", data.crmWithdrawalEventV2.getId(), Rule.CLEARANCE_RULE.getProcessId());
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), Rule.CLEARANCE_RULE.getProcessId());
    }

    @Test
    @AllureId("2245")
    @DisplayName("Clearance rule in router rule. Withdrawal event. Exit with ruleEndId = 103 if ALL checks are false")
    void clearanceRuleWithdrawalEventTest6() throws Exception {
        DataHelper data = dataMap.get("6");
        setupData(data);

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("end_event_103", data.crmWithdrawalEventV2.getId(), Rule.CLEARANCE_RULE.getProcessId());
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), Rule.CLEARANCE_RULE.getProcessId());
    }

    @Disabled("Not easy reachable with default tools")
    @Test
    @DisplayName("Clearance rule in router rule. Withdrawal event. Exit with ruleEndId = 200 if error occurred")
    void clearanceRuleWithdrawalEventTest7() throws Exception {
        DataHelper data = dataMap.get("7");
        setupData(data);

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("catch_error", data.crmWithdrawalEventV2.getId(), Rule.CLEARANCE_RULE.getProcessId(), 3);
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), Rule.CLEARANCE_RULE.getProcessId());
    }

    @Disabled
    @Test
    @DisplayName("Clearance rule in router rule. Withdrawal event. Exit with ruleEndId = 201 if timeout occurred")
    void clearanceRuleWithdrawalEventTest8() throws Exception {
        DataHelper data = dataMap.get("8");
        setupData(data);

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("catch_timeout", data.crmWithdrawalEventV2.getId(), Rule.CLEARANCE_RULE.getProcessId(), 3);
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), Rule.CLEARANCE_RULE.getProcessId());
    }
}
