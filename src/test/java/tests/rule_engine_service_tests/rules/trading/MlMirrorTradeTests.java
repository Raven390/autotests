package tests.rule_engine_service_tests.rules.trading;

import business_objects.db.backoffice_db.alert.Alert;
import business_objects.db.mitigation_service_db.ClientGeneralRestriction;
import business_objects.kafka.alerts.RuleAlertV2;
import helpers.data.DataHelper;
import io.qameta.allure.Allure;
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
import static helpers.data.rules.trading.MlMirrodTradeRuleDataFactory.setupMlMirrorTradeRuleData;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_ML_MIRROR_TRADE_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class MlMirrorTradeTests extends TestBaseRule {

    private static Map<String, DataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    static void setupData() throws IOException, InterruptedException {
        // Enable emulator to set restrictions to status APPLIED
        enableCRMEmulator();
        dbDataMap = setupMlMirrorTradeRuleData();
    }

    @AfterAll
    static void deleteData() throws Exception {
        DataHelper.deleteData(dbDataMap);
    }

    @Test
    @AllureId("1755")
    @DisplayName("ML Mirror trade rule. Exit if user has no credits. ElementId: Event_1flqa1d")
    void MlMirrorTradeRuleTest1() throws Exception {
        DataHelper data = dbDataMap.get("1");

        produceMirrorScoreMessageToKafka(data.mirrorScoreEvent);

        checkElementId("Event_end_3", data.mirrorScoreEvent.getId(), "ml_mirror_trade");
    }

    @Test
    @AllureId("1755")
    @DisplayName("ML Mirror trade rule. Exit if user has at least 1 closed alert. ElementId: Event_1flqa1d")
    void MlMirrorTradeRuleTest2() throws Exception {
        DataHelper data = dbDataMap.get("2");

        produceMirrorScoreMessageToKafka(data.mirrorScoreEvent);

        checkElementId("Event_1flqa1d", data.mirrorScoreEvent.getId(), "ml_mirror_trade");
    }

    @Test
    @AllureId("1756")
    @DisplayName("ML Mirror trade rule. Exit if user has ucidScore < 0.7. ElementId: Event_09pix7t")
    void MlMirrorTradeRuleTest3() throws Exception {
        DataHelper data = dbDataMap.get("3");

        produceMirrorScoreMessageToKafka(data.mirrorScoreEvent);

        checkElementId("Event_09pix7t", data.mirrorScoreEvent.getId(), "ml_mirror_trade");
    }

    @Test
    @AllureId("1757")
    @DisplayName("ML Mirror trade rule. alert and restriction if user has ucidScore > 0.7. ElementId: Event_1flqa1d")
    void MlMirrorTradeRuleTest4() throws Exception {
        DataHelper data = dbDataMap.get("4");

        produceMirrorScoreMessageToKafka(data.mirrorScoreEvent);

        checkElementId("Event_1flqa1d", data.mirrorScoreEvent.getId(), "ml_mirror_trade");

        //Verify alert
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, "Mirror Trading");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        assertThat("Verify alert", alerts.getFirst().getAlertId(), is(data.mirrorScoreEvent.getId()));
        assertThat("Verify alert", alerts.getFirst().getTimestamp(), matchesPattern("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(?:\\.\\d+)?(?:Z|[+-]\\d{2}:\\d{2})$"));
        assertThat("Verify alert", alerts.getFirst().getType(), is("TRADING"));
        assertThat("Verify alert", alerts.getFirst().getTrigger(), is("ML Mirror Model"));
        assertThat("Verify alert", alerts.getFirst().getTriggerCreatedTime(), is("123"));
        assertThat("Verify alert", alerts.getFirst().getUcid(), is(data.mirrorScoreEvent.getUcid()));
        assertThat("Verify alert", alerts.getFirst().getFraudType(), is("HEDGING"));
        assertThat("Verify alert", alerts.getFirst().getReason(), is("ML Model suspects the client of Mirror Trading (on ML Model trigger)"));
        assertThat("Verify alert", alerts.getFirst().getRule().getName(), is("Mirror Trading"));
        assertThat("Verify alert", alerts.getFirst().getRule().getVer(), notNullValue());
        assertThat("Verify alert", alerts.getFirst().getAttributes().getUcid(), is(data.mirrorScoreEvent.getUcid()));
        assertThat("Verify alert", alerts.getFirst().getAttributes().getUcidScore(), is(data.mirrorScoreEvent.getUcidScore()));

        // Verify restriction
        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getUserRestrictionsFromDb(data.clientHelper);
        assertThat("Verify that there is only 1 restriction", clientGeneralRestrictions.size(), equalTo(1));
        assertThat("Check ucid", clientGeneralRestrictions.getFirst().getUcid(), is(data.clientHelper.getUcid()));
        assertThat("Check regulator", clientGeneralRestrictions.getFirst().getRegulator(), is(data.clientHelper.getRegulator()));
        assertThat("Check restrictionId", clientGeneralRestrictions.getFirst().getRestrictionId(), is(8L));
        assertThat("Check comment", clientGeneralRestrictions.getFirst().getComment(), is("ML Model suspects the client of Mirror Trading (on ML Model trigger)"));
        assertThat("Check status", clientGeneralRestrictions.getFirst().getStatus(), is("APPLIED"));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(1));
    }

}
