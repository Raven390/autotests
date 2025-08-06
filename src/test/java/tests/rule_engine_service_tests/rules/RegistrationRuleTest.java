package tests.rule_engine_service_tests.rules;

import business_objects.api.abuse_registry.GetStatusResponseBody;
import business_objects.db.backoffice_db.alert.Alert;
import business_objects.db.mitigation_service_db.ClientGeneralRestriction;
import business_objects.kafka.alerts.RuleAlert;
import helpers.data.rules.RuleDataHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.data.rules.registration_rule.RegistrationRuleDataFactory.deleteRegistrationRuleData;
import static helpers.data.rules.registration_rule.RegistrationRuleDataFactory.setupRegistrationRuleData;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_REGISTRATION_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class RegistrationRuleTest extends TestBaseRule {

    static Map<String, RuleDataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    static void setupData() throws IOException {
        // Enable emulator to set restrictions to status APPLIED
        enableCRMEmulator();
        dbDataMap = setupRegistrationRuleData();
    }

    @AfterAll
    static void deleteData() throws Exception {
        deleteRegistrationRuleData(dbDataMap);
    }

    @Test
    @DisplayName("Registration rule exit 'end_no_alert'")
    @AllureId("155")
    void registrationRuleExitEventEnd1Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("1");

        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        produceRegistrationEventToKafka(data.registrationEvent);

        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Disabled
    @Test
    @DisplayName("Registration rule exit 'End_registration_rule_alert1'")
    @AllureId("156")
    void registrationRuleExitEventEnd2Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("2");

        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        produceRegistrationEventToKafka(data.registrationEvent);

        List<ClientGeneralRestriction> clientGeneralRestrictions = getUserRestrictionsFromDb(data.clientHelper);
        assertThat("Verify that there is only 1 restriction", clientGeneralRestrictions.size(), equalTo(1));
        assertThat("Check ucid", clientGeneralRestrictions.getFirst().getUcid(), is(data.clientHelper.getUcid()));
        assertThat("Check regulator", clientGeneralRestrictions.getFirst().getRegulator(), is(data.clientHelper.getRegulator()));
        assertThat("Check restrictionId", clientGeneralRestrictions.getFirst().getRestrictionId(), is(8L));
        assertThat("Check comment", clientGeneralRestrictions.getFirst().getComment(), is("ML Model suspects the client of Mirror Trading"));
        assertThat("Check status", clientGeneralRestrictions.getFirst().getStatus(), is("APPLIED"));

        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));

        GetStatusResponseBody abuserStatus = getAbuserStatus(data.clientHelper);
        assertThat(abuserStatus, is(notNullValue()));


    }

}
