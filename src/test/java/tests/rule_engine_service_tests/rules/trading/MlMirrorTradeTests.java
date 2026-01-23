package tests.rule_engine_service_tests.rules.trading;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.api.AbuseRegistryHelper.addFraudForClient;
import static helpers.asserts.AlertsAssertsHelper.assertThatAlertNotFailed;
import static helpers.asserts.RestrictionsAssertsHelper.checkManualWithdrawalRestrictionApplied;
import static helpers.data.DataDeleteHelper.deleteData;
import static helpers.data.DataSetupHelper.setupData;
import static helpers.data.rules.trading.MlMirrorTradeRuleDataFactory.setupMlMirrorTradeRuleData;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

import business_objects.kafka.alerts.RuleAlertV2;
import helpers.data.DataHelper;
import helpers.data.enums.FraudType;
import helpers.data.enums.FraudTypeStatus;
import helpers.data.enums.Rule;
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
@Story(STORY_RULE_ENGINE_ML_MIRROR_TRADE_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class MlMirrorTradeTests extends TestBaseRule {

    private static Map<String, DataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    static void setup() throws IOException, InterruptedException {
        // Enable emulator to set restrictions to status APPLIED
        enableCRMEmulator();
        dbDataMap = setupMlMirrorTradeRuleData();
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteData(dbDataMap);
    }

    @Test
    @AllureId("1755")
    @DisplayName("ML Mirror trade rule. Exit if user has no credits. ElementId: Event_1flqa1d")
    void MlMirrorTradeRuleTest1() throws Exception {
        DataHelper data = dbDataMap.get("1");
        setupData(data);

        produceMlMirrorTradeEventToKafka(data.mirrorScoreEvent);

        checkElementId("Event_end_3", data.mirrorScoreEvent.getId(), Rule.MIRROR_TRADE_ML.getProcessId());
    }

    @Test
    @AllureId("1755")
    @DisplayName(
            "ML Mirror trade rule.  user has at least 1 closed alert currentPnl - lastPnl < min(5000, 0.8 * depositsUcid). ElementId: Event_1fe3v0e")
    void MlMirrorTradeRuleTest2() throws Exception {
        DataHelper data = dbDataMap.get("2");
        setupData(data);

        produceMlMirrorTradeEventToKafka(data.mirrorScoreEvent);

        checkElementId("Event_1fe3v0e", data.mirrorScoreEvent.getId(), Rule.MIRROR_TRADE_ML.getProcessId());
    }

    @Test
    @AllureId("1756")
    @DisplayName(
            "ML Mirror trade rule. ML Mirror trade rule.  user has at least 1 closed alert currentPnl - lastPnl > min(5000, 0.8 * depositsUcid) marked hedger. ElementId: Event_1vz2lns")
    void MlMirrorTradeRuleTest3() throws Exception {
        DataHelper data = dbDataMap.get("3");
        setupData(data);

        addFraudForClient(data.clientHelper, FraudType.HEDGING, FraudTypeStatus.CONFIRMED, null);

        produceMlMirrorTradeEventToKafka(data.mirrorScoreEvent);

        checkElementId("Event_1vz2lns", data.mirrorScoreEvent.getId(), Rule.MIRROR_TRADE_ML.getProcessId());
    }

    @Test
    @AllureId("1757")
    @DisplayName(
            "ML Mirror trade rule. ML Mirror trade rule.  user has at least 1 closed alert currentPnl - lastPnl > min(5000, 0.8 * depositsUcid) not marked as hedger ElementId: Event_0pdqol0")
    void MlMirrorTradeRuleTest4() throws Exception {
        DataHelper data = dbDataMap.get("4");
        setupData(data);

        addFraudForClient(data.clientHelper, FraudType.HEDGING, FraudTypeStatus.CLEANED, null);

        produceMlMirrorTradeEventToKafka(data.mirrorScoreEvent);

        checkElementId("Event_0pdqol0", data.mirrorScoreEvent.getId(), Rule.MIRROR_TRADE_ML.getProcessId());

        // Verify alert
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, "Mirror Trading");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        assertThat("Verify alert", alerts.getFirst().getAlertId(), is(data.mirrorScoreEvent.getId()));
        assertThat(
                "Verify alert",
                alerts.getFirst().getTimestamp(),
                matchesPattern("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(?:\\.\\d+)?(?:Z|[+-]\\d{2}:\\d{2})$"));
        assertThat("Verify alert - type", alerts.getFirst().getType(), is("TRADING"));
        assertThat("Verify alert - trigger vlue ", alerts.getFirst().getTrigger(), is("Close Trade"));
        assertThat(
                "Verify alert - trigger create time is not null",
                alerts.getFirst().getTriggerCreatedTime(),
                is(notNullValue()));
        assertThat("Verify alert - ucid from event", alerts.getFirst().getUcid(), is(data.mirrorScoreEvent.getUcid()));
        assertThat(
                "Verify alert - account",
                alerts.getFirst().getAccount().toString(),
                is(data.mirrorScoreEvent.getAccount().toString()));
        assertThat(
                "Verify alert - server id",
                alerts.getFirst().getServerId().toString(),
                is(data.mirrorScoreEvent.getServerId().toString()));
        assertThat("Verify alert - symbol", alerts.getFirst().getSymbol(), is("ML Model"));
        assertThat("Verify alert- fraud", alerts.getFirst().getFraudType(), is("HEDGING"));
        assertThat(
                "Verify alert - reason",
                alerts.getFirst().getReason(),
                is("ML Model suspects the client of Mirror Trading (on ML Model trigger)"));
        assertThat("Verify alert- rule/name", alerts.getFirst().getRule().getName(), is("Mirror Trading"));
        assertThat(
                "Verify alert- rule/ver is not null",
                alerts.getFirst().getRule().getVer(),
                notNullValue());
        assertThat(
                "Verify alert",
                alerts.getFirst().getAttributes().getUcidScore(),
                is(data.mirrorScoreEvent.getUcidScore()));

        assertThatAlertNotFailed(data.clientHelper.getUcid(), "Mirror Trading");

        checkManualWithdrawalRestrictionApplied(
                data, "ML Model suspects the client of Mirror Trading (on ML Model trigger)");
    }

    @Test
    @AllureId("2085")
    @DisplayName(
            "No server in account in event .ML Mirror trade rule. ML Mirror trade rule.  user has at least 1 closed alert currentPnl - lastPnl > min(5000, 0.8 * depositsUcid) not marked as hedger ElementId: Event_0pdqol0")
    void MlMirrorTradeRuleTest5() throws Exception {
        DataHelper data = dbDataMap.get("5");
        setupData(data);

        addFraudForClient(data.clientHelper, FraudType.HEDGING, FraudTypeStatus.CLEANED, null);

        produceMlMirrorTradeEventToKafka(data.mirrorScoreEvent);

        checkElementId("Event_0pdqol0", data.mirrorScoreEvent.getId(), Rule.MIRROR_TRADE_ML.getProcessId());

        // Verify alert
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, "Mirror Trading");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        assertThat("Verify alert", alerts.getFirst().getAlertId(), is(data.mirrorScoreEvent.getId()));
        assertThat(
                "Verify alert",
                alerts.getFirst().getTimestamp(),
                matchesPattern("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(?:\\.\\d+)?(?:Z|[+-]\\d{2}:\\d{2})$"));
        assertThat("Verify alert - type", alerts.getFirst().getType(), is("TRADING"));
        assertThat("Verify alert - trigger vlue ", alerts.getFirst().getTrigger(), is("Close Trade"));
        assertThat(
                "Verify alert - trigger create time is not null",
                alerts.getFirst().getTriggerCreatedTime(),
                is(notNullValue()));
        assertThat("Verify alert - ucid from event", alerts.getFirst().getUcid(), is(data.mirrorScoreEvent.getUcid()));
        assertThat("Verify alert - account", alerts.getFirst().getAccount().toString(), is(String.valueOf(0)));
        assertThat("Verify alert - server id", alerts.getFirst().getServerId().toString(), is(String.valueOf(0)));
        assertThat("Verify alert - symbol", alerts.getFirst().getSymbol(), is("ML Model"));
        assertThat("Verify alert- fraud", alerts.getFirst().getFraudType(), is("HEDGING"));
        assertThat(
                "Verify alert - reason",
                alerts.getFirst().getReason(),
                is("ML Model suspects the client of Mirror Trading (on ML Model trigger)"));
        assertThat("Verify alert- rule/name", alerts.getFirst().getRule().getName(), is("Mirror Trading"));
        assertThat(
                "Verify alert- rule/ver is not null",
                alerts.getFirst().getRule().getVer(),
                notNullValue());
        assertThat(
                "Verify alert",
                alerts.getFirst().getAttributes().getUcidScore(),
                is(data.mirrorScoreEvent.getUcidScore()));

        assertThatAlertNotFailed(data.clientHelper.getUcid(), "Mirror Trading");

        checkManualWithdrawalRestrictionApplied(
                data, "ML Model suspects the client of Mirror Trading (on ML Model trigger)");
    }
}
