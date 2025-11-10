package tests.rule_engine_service_tests.rules.general;

import business_objects.api.clickhouse_api_service.get_abuse_types.GetAbuseTypesResponse;
import business_objects.kafka.alerts.RuleAlertV2;
import helpers.data.DataHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static business_objects.api.clickhouse_api_service.get_abuse_types.GetAbuseTypesRequest.getAbuseTypes;
import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.asserts.RestrictionsAssertsHelper.checkManualWithdrawalRestrictionApplied;
import static helpers.data.rules.general.CustomRuleDataFactory.setupCustomRuleData;
import static helpers.database.DbHelper.startSshTunnel;
import static helpers.database.DbHelper.stopSshTunnel;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.notNullValue;
import static utils.Constants.*;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_CUSTOM_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class CustomRuleTests extends TestBaseRule {

    private static Map<String, DataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    static void setupData() throws IOException, InterruptedException {
        startSshTunnel();
        enableCRMEmulator();
        dbDataMap = setupCustomRuleData();
    }

    @AfterAll
    static void deleteData() throws Exception {
        DataHelper.deleteData(dbDataMap);
        stopSshTunnel();
    }

    @Test
    @AllureId("1746")
    @DisplayName("Custom rule. ucid -> fraud type + restriction + alert")
    void customRuleTest1() throws Exception {
        DataHelper data = dbDataMap.get("1");

        produceCustomMessageToKafka(data.customEvent);

        checkElementId("setUcid", data.customEvent.getId(), "custom_rule");
        checkElementId("setFraudType", data.customEvent.getId(), "custom_rule");
        checkElementId("setRestriction", data.customEvent.getId(), "custom_rule");
        checkElementId("setAlert", data.customEvent.getId(), "custom_rule");
        checkElementId("endEvent", data.customEvent.getId(), "custom_rule");

        // Verify restriction
        checkManualWithdrawalRestrictionApplied(data.clientHelper, data.customEvent.getMessage());

        //Verify alert
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, data.customEvent.getSource());
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        assertThat("Verify alert", alerts.getFirst().getReason(), is("Client repeatedly opens opposite-direction trades using known hedging EA comments ('vef', 'My Order')."));
        assertThat("Verify alert", alerts.getFirst().getTimestamp(), matchesPattern("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(?:\\.\\d+)?(?:Z|[+-]\\d{2}:\\d{2})$"));
        assertThat("Verify alert", alerts.getFirst().getAlertId(), is(data.customEvent.getId()));
        assertThat("Verify alert", alerts.getFirst().getTriggerCreatedTime(), matchesPattern("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(?:\\.\\d+)?(?:Z|[+-]\\d{2}:\\d{2})$"));
        assertThat("Verify alert", alerts.getFirst().getFraudType(), is(data.customEvent.getFraudType()));
        assertThat("Verify alert", alerts.getFirst().getTrigger(), is(data.customEvent.getType()));
        assertThat("Verify alert", alerts.getFirst().getUcid(), is(data.clientHelper.getUcid()));
        assertThat("Verify alert", alerts.getFirst().getType(), is("TRADING"));
        assertThat("Verify alert", alerts.getFirst().getRule().getName(), is(data.customEvent.getSource()));
        assertThat("Verify alert", alerts.getFirst().getRule().getVer(), notNullValue());
        assertThat("Verify alert", alerts.getFirst().getAttributes().getDetails(), is(""));

        GetAbuseTypesResponse[] mappedResponse = objectMapper.readValue(getAbuseTypes(List.of(data.clientHelper.getUcid())).body().string(), GetAbuseTypesResponse[].class);
        assertThat("Assert array size", mappedResponse.length, is(1));
        assertThat("Assert clientId", mappedResponse[0].getClientId(), is(data.clientHelper.getUcid()));
        assertThat("Assert fraudType", mappedResponse[0].getFraudType(), hasItemInArray(data.customEvent.getFraudType()));
    }

    @Test
    @AllureId("1747")
    @DisplayName("Custom rule. trading account + server -> fraud type + restriction + alert")
    void customRuleTest2() throws Exception {
        DataHelper data = dbDataMap.get("2");

        produceCustomMessageToKafka(data.customEvent);

        checkElementId("getUcid", data.customEvent.getId(), "custom_rule");
        checkElementId("setFraudType", data.customEvent.getId(), "custom_rule");
        checkElementId("setRestriction", data.customEvent.getId(), "custom_rule");
        checkElementId("setAlert", data.customEvent.getId(), "custom_rule");
        checkElementId("endEvent", data.customEvent.getId(), "custom_rule");
    }

    @Test
    @AllureId("1748")
    @DisplayName("Custom rule. trading account + server -> fraud type")
    void customRuleTest3() throws Exception {
        DataHelper data = dbDataMap.get("3");

        produceCustomMessageToKafka(data.customEvent);

        checkElementId("getUcid", data.customEvent.getId(), "custom_rule");
        checkElementId("setFraudType", data.customEvent.getId(), "custom_rule");
        checkElementId("endEvent", data.customEvent.getId(), "custom_rule");

        checkElementIdNotPresent("setRestriction", data.customEvent.getId(), "custom_rule");
        checkElementIdNotPresent("setAlert", data.customEvent.getId(), "custom_rule");
    }

    @Test
    @AllureId("1749")
    @DisplayName("Custom rule. trading account + server -> restriction")
    void customRuleTest4() throws Exception {
        DataHelper data = dbDataMap.get("4");

        produceCustomMessageToKafka(data.customEvent);

        checkElementId("getUcid", data.customEvent.getId(), "custom_rule");
        checkElementId("setRestriction", data.customEvent.getId(), "custom_rule");
        checkElementId("endEvent", data.customEvent.getId(), "custom_rule");

        checkElementIdNotPresent("setAlert", data.customEvent.getId(), "custom_rule");
        checkElementIdNotPresent("setFraudType", data.customEvent.getId(), "custom_rule");
    }

    @Test
    @AllureId("1750")
    @DisplayName("Custom rule. trading account + server -> alert")
    void customRuleTest5() throws Exception {
        DataHelper data = dbDataMap.get("5");

        produceCustomMessageToKafka(data.customEvent);

        checkElementId("getUcid", data.customEvent.getId(), "custom_rule");
        checkElementId("setAlert", data.customEvent.getId(), "custom_rule");
        checkElementId("endEvent", data.customEvent.getId(), "custom_rule");

        checkElementIdNotPresent("setFraudType", data.customEvent.getId(), "custom_rule");
        checkElementIdNotPresent("setRestriction", data.customEvent.getId(), "custom_rule");
    }


}