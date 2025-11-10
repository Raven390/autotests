package tests.rule_engine_service_tests.rules.general;

import business_objects.db.backoffice_db.alert.Alert;
import business_objects.db.mitigation_service_db.ClientGeneralRestriction;
import business_objects.kafka.alerts.RuleAlert;
import helpers.data.DataHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.asserts.RestrictionsAssertsHelper.checkManualWithdrawalRestrictionApplied;
import static helpers.data.rules.general.RegistrationRuleDataFactory.setupRegistrationRuleData;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_REGISTRATION_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class RegistrationRuleTest extends TestBaseRule {

    private static Map<String, DataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    static void setupData() throws Exception {
        // Enable emulator to set restrictions to status APPLIED
        enableCRMEmulator();
        dbDataMap = setupRegistrationRuleData();
    }

    @AfterAll
    static void deleteData() throws Exception {
        DataHelper.deleteData(dbDataMap);
    }

    @Test
    @DisplayName("Registration rule: Exit without alert if amount of abusers in connections < 10% and lexis score is not high. ElementId: end_no_alert")
    @AllureId("155")
    void registrationRuleTest1() throws Exception {
        DataHelper data = dbDataMap.get("1");

        produceRegistrationEventToKafka(data.registrationEvent);

        checkElementId("end_no_alert", data.registrationEvent.getId(), "clientRegistration_event_rule");
    }

    @Test
    @DisplayName("Registration rule. Exit with alert if amount of abusers in connections < 10% and lexis score is high. ElementId: End_registration_rule_alert1")
    @AllureId("156")
    void registrationRuleTest2() throws Exception {
        DataHelper data = dbDataMap.get("2");

        produceRegistrationEventToKafka(data.registrationEvent);

        checkElementId("End_registration_rule_alert1", data.registrationEvent.getId(), "clientRegistration_event_rule");

        checkManualWithdrawalRestrictionApplied(data.clientHelper, "No alert. High Lexis score");

        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper, "Registration");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @AllureId("1484")
    @DisplayName("Registration rule. Connection search. Strong hedge confirmed. ElementId: end_registration_rule_cs")
    void registrationRuleTest3() throws Exception {
        DataHelper data = dbDataMap.get("3");

        produceRegistrationEventToKafka(data.registrationEvent);

        checkElementId("end_registration_rule_cs", data.registrationEvent.getId(), "clientRegistration_event_rule");

        List<ClientGeneralRestriction> clientGeneralRestrictions = getUserRestrictionsFromDb(data.clientHelper);
        assertThat("Verify that there is only 1 restriction", clientGeneralRestrictions.size(), equalTo(1));
        assertThat("Check ucid", clientGeneralRestrictions.getFirst().getUcid(), is(data.clientHelper.getUcid()));
        assertThat("Check regulator", clientGeneralRestrictions.getFirst().getRegulator(), is(data.clientHelper.getRegulator()));
        assertThat("Check restrictionId", clientGeneralRestrictions.getFirst().getRestrictionId(), is(9L));
        assertThat("Check status", clientGeneralRestrictions.getFirst().getStatus(), is("APPLIED"));

        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper, "Registration");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(1));
        assertThat("", dbAlerts.getFirst().getRuleAttributes(), containsString("{\"Reason\": \"Linked hedging abuser\", \"Max Connection Score\": \"0.75\"}"));
    }

    @Test
    @AllureId("1485")
    @DisplayName("Registration rule. Connection search. Medium hedge potential, ln risk rating = low. ElementId: end_no_alert")
    void registrationRuleTest4() throws Exception {
        DataHelper data = dbDataMap.get("4");

        produceRegistrationEventToKafka(data.registrationEvent);

        checkElementId("end_no_alert", data.registrationEvent.getId(), "clientRegistration_event_rule");
    }
}
