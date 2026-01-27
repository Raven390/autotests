package tests.rule_engine_service_tests.rules.payment.router_rule_crm_payment.subrules;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.api.RestrictionHelper.setRestrictionAPIGeneral;
import static helpers.data.DataDeleteHelper.deleteData;
import static helpers.data.DataSetupHelper.setupData;
import static helpers.data.enums.Restriction.MANUAL_WITHDRAWAL_REVIEW;
import static helpers.data.rules.payments.router_rule_crm_payment.ClearanceRuleTransferToWADataFactory.setupClearanceTransferToWAEventRuleData;
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
class ClearanceRuleTransferToWAEventTests extends TestBaseRule {

    private static Map<String, DataHelper> dataMap = new HashMap<>();

    @BeforeAll
    static void setup() throws IOException {
        startSshTunnel();
        enableCRMEmulator();
        dataMap = setupClearanceTransferToWAEventRuleData();
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteData(dataMap);
    }

    @Test
    @AllureId("2076")
    @DisplayName(
            "Clearance rule in router rule. TransferToWA Event. Exit with ruleEndId = 101 if Active WR restriction is set by rule engine")
    void clearanceRuleTransferToWAEventTest1() throws Exception {
        DataHelper data = dataMap.get("1");
        setupData(data);
        setRestrictionAPIGeneral(
                data.clientHelper.getUcid(),
                MANUAL_WITHDRAWAL_REVIEW.getCode(),
                "Clearance rule test",
                "Rule engine",
                "QA");

        produceTransferToWaMessageToCrmPaymentTopic(data.transferToWaEvent);

        checkElementId("end_event_101", data.transferToWaEvent.getId().toString(), Rule.CLEARANCE_RULE.getProcessId());
        checkElementId("Event_0sg27lc", data.transferToWaEvent.getId().toString(), Rule.CLEARANCE_RULE.getProcessId());
    }

    @Test
    @AllureId("2077")
    @DisplayName(
            "Clearance rule in router rule. Transfer to wa event. Exit with ruleEndId = 102 if grossDeposit > 10 000?")
    void clearanceRuleTransferToWAEventTest2() throws Exception {
        DataHelper data = dataMap.get("2");
        setupData(data);

        produceTransferToWaMessageToCrmPaymentTopic(data.transferToWaEvent);

        checkElementId("end_event_102", data.transferToWaEvent.getId().toString(), Rule.CLEARANCE_RULE.getProcessId());
        checkElementId("Event_0sg27lc", data.transferToWaEvent.getId().toString(), Rule.CLEARANCE_RULE.getProcessId());
    }

    @Test
    @AllureId("2078")
    @DisplayName(
            "Clearance rule in router rule. Transfer to wa event. Exit with ruleEndId = 401 if checks B and D are true")
    void clearanceRuleTransferToWAEventTest3() throws Exception {
        DataHelper data = dataMap.get("3");
        setupData(data);

        produceTransferToWaMessageToCrmPaymentTopic(data.transferToWaEvent);

        checkElementId("end_event_401", data.transferToWaEvent.getId().toString(), Rule.CLEARANCE_RULE.getProcessId());
        checkElementId("Event_0sg27lc", data.transferToWaEvent.getId().toString(), Rule.CLEARANCE_RULE.getProcessId());
        // check that router rule ended with the auto approve branch
        checkElementId(
                "Activity_197u1ti",
                data.transferToWaEvent.getId().toString(),
                Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());
    }

    @Test
    @AllureId("2079")
    @DisplayName(
            "Clearance rule in router rule. Transfer to wa event. Exit with ruleEndId = 402 if checks B and E are true")
    void clearanceRuleTransferToWAEventTest4() throws Exception {
        DataHelper data = dataMap.get("4");
        setupData(data);

        produceTransferToWaMessageToCrmPaymentTopic(data.transferToWaEvent);

        checkElementId("end_event_402", data.transferToWaEvent.getId().toString(), Rule.CLEARANCE_RULE.getProcessId());
        checkElementId("Event_0sg27lc", data.transferToWaEvent.getId().toString(), Rule.CLEARANCE_RULE.getProcessId());
    }

    @Test
    @AllureId("2080")
    @DisplayName(
            "Clearance rule in router rule. Transfer to wa event. Exit with ruleEndId = 403 if checks C and D are true")
    void clearanceRuleTransferToWAEventTest5() throws Exception {
        DataHelper data = dataMap.get("5");
        setupData(data);

        produceTransferToWaMessageToCrmPaymentTopic(data.transferToWaEvent);

        checkElementId("end_event_403", data.transferToWaEvent.getId().toString(), Rule.CLEARANCE_RULE.getProcessId());
        checkElementId("Event_0sg27lc", data.transferToWaEvent.getId().toString(), Rule.CLEARANCE_RULE.getProcessId());
    }

    @Test
    @AllureId("2082")
    @DisplayName(
            "Clearance rule in router rule. Transfer to wa event. Exit with ruleEndId = 103 if ALL checks are false")
    void clearanceRuleTransferToWAEventTest6() throws Exception {
        DataHelper data = dataMap.get("6");
        setupData(data);

        produceTransferToWaMessageToCrmPaymentTopic(data.transferToWaEvent);

        checkElementId("end_event_103", data.transferToWaEvent.getId().toString(), Rule.CLEARANCE_RULE.getProcessId());
        checkElementId("Event_0sg27lc", data.transferToWaEvent.getId().toString(), Rule.CLEARANCE_RULE.getProcessId());
    }

    @Disabled("Not easy reachable with default tools")
    @Test
    @AllureId("2083")
    @DisplayName("Clearance rule in router rule. Transfer to wa event. Exit with ruleEndId = 200 if error occurred")
    void clearanceRuleTransferToWAEventTest7() throws Exception {
        DataHelper data = dataMap.get("7");
        setupData(data);

        produceTransferToWaMessageToCrmPaymentTopic(data.transferToWaEvent);

        checkElementId("catch_error", data.transferToWaEvent.getId().toString(), Rule.CLEARANCE_RULE.getProcessId(), 3);
        checkElementId("Event_0sg27lc", data.transferToWaEvent.getId().toString(), Rule.CLEARANCE_RULE.getProcessId());
    }

    @Test
    @AllureId("2084")
    @DisplayName("Clearance rule in router rule. Transfer to wa event. Exit with ruleEndId = 201 if timeout occurred")
    void clearanceRuleTransferToWAEventTest8() throws Exception {
        DataHelper data = dataMap.get("8");
        setupData(data);

        produceTransferToWaMessageToCrmPaymentTopic(data.transferToWaEvent);

        checkElementId(
                "catch_timeout", data.transferToWaEvent.getId().toString(), Rule.CLEARANCE_RULE.getProcessId(), 3);
        checkElementId("Event_0sg27lc", data.transferToWaEvent.getId().toString(), Rule.CLEARANCE_RULE.getProcessId());
    }
}
