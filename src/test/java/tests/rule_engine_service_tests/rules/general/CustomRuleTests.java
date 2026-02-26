package tests.rule_engine_service_tests.rules.general;

import static business_objects.api.clickhouse_api_service.get_abuse_types.GetAbuseTypesRequest.getAbuseTypes;
import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.asserts.AlertsAssertsHelper.assertCustomRuleAlert;
import static helpers.asserts.RestrictionsAssertsHelper.checkManualWithdrawalRestrictionApplied;
import static helpers.asserts.RestrictionsAssertsHelper.checkWorseTradingRestrictionApplied;
import static helpers.data.DataDeleteHelper.deleteData;
import static helpers.data.DataSetupHelper.setupData;
import static helpers.data.enums.rule_engine.Rule.CUSTOM_RULE;
import static helpers.data.rules.general.CustomRuleDataFactory.setupCustomRuleData;
import static helpers.database.DbHelper.startSshTunnel;
import static helpers.database.DbHelper.stopSshTunnel;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

import business_objects.api.clickhouse_api_service.get_abuse_types.GetAbuseTypesResponse;
import business_objects.kafka.alerts.RuleAlertV2;
import helpers.data.DataHelper;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_CUSTOM_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class CustomRuleTests extends TestBaseRule {

    private static Map<String, DataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    static void setup() throws IOException {
        startSshTunnel();
        enableCRMEmulator();
        dbDataMap = setupCustomRuleData();
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteData(dbDataMap);
        stopSshTunnel();
    }

    @Test
    @DisplayName("Custom rule. ucid -> fraud type + restriction + alert")
    void customRuleTest1() throws Exception {
        DataHelper data = dbDataMap.get("1");
        setupData(data);

        produceCustomMessageToKafka(data.customEvent);

        checkElementId("setUcid", data.customEvent.getId(), CUSTOM_RULE.getProcessId());
        checkElementId("setFraudType", data.customEvent.getId(), CUSTOM_RULE.getProcessId());
        checkElementId("setRestriction", data.customEvent.getId(), CUSTOM_RULE.getProcessId());
        checkElementId("setAlert", data.customEvent.getId(), CUSTOM_RULE.getProcessId());
        checkElementId("endEvent", data.customEvent.getId(), CUSTOM_RULE.getProcessId());

        // Verify restriction
        checkManualWithdrawalRestrictionApplied(data, data.customEvent.getMessage());

        // Verify alert
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, data.customEvent.getSource());
        assertCustomRuleAlert(data, alerts);

        GetAbuseTypesResponse[] mappedResponse = objectMapper.readValue(
                getAbuseTypes(List.of(data.clientHelper.getUcid())).body().string(), GetAbuseTypesResponse[].class);
        assertThat("Assert array size", mappedResponse.length, is(1));
        assertThat("Assert clientId", mappedResponse[0].getClientId(), is(data.clientHelper.getUcid()));
        assertThat(
                "Assert fraudType", mappedResponse[0].getFraudType(), hasItemInArray(data.customEvent.getFraudType()));
    }

    @Test
    @DisplayName("Custom rule. trading account + server -> fraud type + restriction + alert")
    void customRuleTest2() throws Exception {
        DataHelper data = dbDataMap.get("2");
        setupData(data);

        produceCustomMessageToKafka(data.customEvent);

        checkElementId("getUcid", data.customEvent.getId(), CUSTOM_RULE.getProcessId());
        checkElementId("setFraudType", data.customEvent.getId(), CUSTOM_RULE.getProcessId());
        checkElementId("setRestriction", data.customEvent.getId(), CUSTOM_RULE.getProcessId());
        checkElementId("setAlert", data.customEvent.getId(), CUSTOM_RULE.getProcessId());
        checkElementId("endEvent", data.customEvent.getId(), CUSTOM_RULE.getProcessId());
    }

    @Test
    @DisplayName("Custom rule. trading account + server -> fraud type")
    void customRuleTest3() throws Exception {
        DataHelper data = dbDataMap.get("3");
        setupData(data);

        produceCustomMessageToKafka(data.customEvent);

        checkElementId("getUcid", data.customEvent.getId(), CUSTOM_RULE.getProcessId());
        checkElementId("setFraudType", data.customEvent.getId(), CUSTOM_RULE.getProcessId());
        checkElementId("endEvent", data.customEvent.getId(), CUSTOM_RULE.getProcessId());

        checkElementIdNotPresent("setRestriction", data.customEvent.getId(), CUSTOM_RULE.getProcessId());
        checkElementIdNotPresent("setAlert", data.customEvent.getId(), CUSTOM_RULE.getProcessId());
    }

    @Test
    @DisplayName("Custom rule. trading account + server -> restriction")
    void customRuleTest4() throws Exception {
        DataHelper data = dbDataMap.get("4");
        setupData(data);

        produceCustomMessageToKafka(data.customEvent);

        checkElementId("getUcid", data.customEvent.getId(), CUSTOM_RULE.getProcessId());
        checkElementId("setRestriction", data.customEvent.getId(), CUSTOM_RULE.getProcessId());
        checkElementId("endEvent", data.customEvent.getId(), CUSTOM_RULE.getProcessId());

        checkElementIdNotPresent("setAlert", data.customEvent.getId(), CUSTOM_RULE.getProcessId());
        checkElementIdNotPresent("setFraudType", data.customEvent.getId(), CUSTOM_RULE.getProcessId());
    }

    @Test
    @DisplayName("Custom rule. trading account + server -> alert")
    void customRuleTest5() throws Exception {
        DataHelper data = dbDataMap.get("5");
        setupData(data);

        produceCustomMessageToKafka(data.customEvent);

        checkElementId("getUcid", data.customEvent.getId(), CUSTOM_RULE.getProcessId());
        checkElementId("setAlert", data.customEvent.getId(), CUSTOM_RULE.getProcessId());
        checkElementId("endEvent", data.customEvent.getId(), CUSTOM_RULE.getProcessId());

        checkElementIdNotPresent("setFraudType", data.customEvent.getId(), CUSTOM_RULE.getProcessId());
        checkElementIdNotPresent("setWtRestriction", data.customEvent.getId(), CUSTOM_RULE.getProcessId());
    }

    @Test
    @DisplayName("Custom rule. Apply WT restriction")
    void customRuleTest6() throws Exception {
        DataHelper data = dbDataMap.get("6");
        setupData(data);

        produceCustomMessageToKafka(data.customEvent);

        checkElementId("getUcid", data.customEvent.getId(), CUSTOM_RULE.getProcessId());
        checkElementId("Activity_03k4o2g", data.customEvent.getId(), CUSTOM_RULE.getProcessId());
        checkElementId("endEvent", data.customEvent.getId(), CUSTOM_RULE.getProcessId());

        checkElementIdNotPresent("setAlert", data.customEvent.getId(), CUSTOM_RULE.getProcessId());
        checkElementIdNotPresent("setFraudType", data.customEvent.getId(), CUSTOM_RULE.getProcessId());
        checkElementIdNotPresent("setRestriction", data.customEvent.getId(), CUSTOM_RULE.getProcessId());

        // Verify restriction
        checkWorseTradingRestrictionApplied(data, data.customEvent.getMessage(), "LOW");
    }
}
